@file:Suppress("SpellCheckingInspection", "PackageName", "unused", "UNCHECKED_CAST")

package com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseNet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import com.mygdx.honestmirror.application.common.DebugLog
import com.mygdx.honestmirror.application.domain.feedback.DesignTimeFeedbackDataContainer
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.Resolution
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.NNInterpreter
import com.mygdx.honestmirror.application.nnanalysis.poseestimation.nn.PoseModels.NNModelPosenet
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import kotlin.math.abs

class PoseNetHandler(
        val context: Context,
        private val filename: String,
        private val nnInterpreter: NNInterpreter,
        private val resolution: Resolution
) : AutoCloseable {

    private var interpreter: Interpreter? = getInterpreter()
    //private var gpuDelegate: GpuDelegate? = null

    private fun getInterpreter(): Interpreter {
        if (interpreter != null) {
            return interpreter!!
        }
        val options = Interpreter.Options()
        when (nnInterpreter) {
            NNInterpreter.CPU -> {
            }
            NNInterpreter.GPU -> {
                val gpuDelegate = GpuDelegate()
                options.addDelegate(gpuDelegate)
            }
            NNInterpreter.NNAPI -> options.setUseNNAPI(true)
        }
        interpreter = Interpreter(loadModelFile(filename, context), options)
        return interpreter!!
    }


    override fun close() {
        interpreter?.close()
        interpreter = null
        //gpuDelegate?.close()
        //gpuDelegate = null
    }

    // Returns value within [0,1].
    private fun sigmoid(x: Float): Float {
        // 1 / (1 + (-value)**2)
        return (1 / (1 + (Math.pow(-x.toDouble(), 2.0)))).toFloat()
    }

    //Scale the image to a byteBuffer of [-1,1] values.
    private fun initInputArray(bitmap: Bitmap): ByteBuffer {
        val bytesPerChannel = 4
        val inputChannels = 3
        val batchSize = 1
        val inputBuffer = ByteBuffer.allocateDirect(
                batchSize * bytesPerChannel * bitmap.height * bitmap.width * inputChannels
        )
        inputBuffer.order(ByteOrder.nativeOrder())
        inputBuffer.rewind()

        val mean = 128.0f
        val std = 128.0f
        for (row in 0 until bitmap.height) {
            for (col in 0 until bitmap.width) {
                val pixelValue = bitmap.getPixel(col, row)
                inputBuffer.putFloat(((pixelValue shr 16 and 0xFF) - mean) / std)
                inputBuffer.putFloat(((pixelValue shr 8 and 0xFF) - mean) / std)
                inputBuffer.putFloat(((pixelValue and 0xFF) - mean) / std)
            }
        }
        return inputBuffer
    }

    //Preload and memory map the model file, returning a MappedByteBuffer containing the model.
    private fun loadModelFile(path: String, context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(path)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        return inputStream.channel.map(
                FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength
        )
    }

    //Initializes an outputMap of 1 * x * y * z FloatArrays for the model processing to populate.
    private fun initOutputMap(interpreter: Interpreter): HashMap<Int, Any> {
        val outputMap = HashMap<Int, Any>()

        // 1 * 9 * 9 * 17 contains heatmaps
        val heatmapsShape = interpreter.getOutputTensor(0).shape()
        outputMap[0] = Array(heatmapsShape[0]) {
            Array(heatmapsShape[1]) {
                Array(heatmapsShape[2]) { FloatArray(heatmapsShape[3]) }
            }
        }

        // 1 * 9 * 9 * 34 contains offsets
        val offsetsShape = interpreter.getOutputTensor(1).shape()
        outputMap[1] = Array(offsetsShape[0]) {
            Array(offsetsShape[1]) { Array(offsetsShape[2]) { FloatArray(offsetsShape[3]) } }
        }

        // 1 * 9 * 9 * 32 contains forward displacements
        val displacementsFwdShape = interpreter.getOutputTensor(2).shape()
        outputMap[2] = Array(offsetsShape[0]) {
            Array(displacementsFwdShape[1]) {
                Array(displacementsFwdShape[2]) { FloatArray(displacementsFwdShape[3]) }
            }
        }

        // 1 * 9 * 9 * 32 contains backward displacements
        val displacementsBwdShape = interpreter.getOutputTensor(3).shape()
        outputMap[3] = Array(displacementsBwdShape[0]) {
            Array(displacementsBwdShape[1]) {
                Array(displacementsBwdShape[2]) { FloatArray(displacementsBwdShape[3]) }
            }
        }

        return outputMap
    }

    // Crop Bitmap to maintain aspect ratio of model input.
    private fun cropBitmap(bitmap: Bitmap): Bitmap {
        val bitmapRatio = bitmap.height.toFloat() / bitmap.width
        val modelInputRatio = resolution.modelHeight.toFloat() / resolution.modelWidth
        var croppedBitmap = bitmap

        // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
        val maxDifference = 1e-5

        // Checks if the bitmap has similar aspect ratio as the required model input.
        when {
            abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
            modelInputRatio < bitmapRatio -> {
                // New image is taller so we are height constrained.
                val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
                croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        (cropHeight / 2).toInt(),
                        bitmap.width,
                        (bitmap.height - cropHeight).toInt()
                )
            }
            else -> {
                val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
                croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        (cropWidth / 2).toInt(),
                        0,
                        (bitmap.width - cropWidth).toInt(),
                        bitmap.height
                )
            }
        }
        return croppedBitmap
    }

    //Estimates the pose for a single person.
    fun estimateSinglePose(bitmap: Bitmap): Person {
        //    person = if(bitmap.height / bitmap.width != 1){
//            val croppedBitmap = cropBitmap(bitmap)
//            trueEstimation(croppedBitmap)
//        }
//        else{
//            trueEstimation(bitmap)
//        }
        return trueEstimation(bitmap)
    }

    private fun trueEstimation(bitmap: Bitmap): Person{
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, resolution.modelWidth, resolution.modelHeight, true)
        val inputArray = arrayOf(initInputArray(scaledBitmap))
        val outputMap = initOutputMap(interpreter!!)
        try{
            interpreter!!.runForMultipleInputsOutputs(inputArray, outputMap)
        }
        catch (e: Exception){
            DebugLog.log("Exception: $e")
        }

        val heatmaps = outputMap[0] as Array<Array<Array<FloatArray>>>
        val offsets = outputMap[1] as Array<Array<Array<FloatArray>>>

//        val person1 = Person()
//        val floatHeatmapArray = person1.readHeatmapFile(context)
//        val floatOffsetArray = person1.readOffsetFile(context)
//
//        var i = 0
//        var j = 0
//        for(x in 0 until 9){
//            for(y in 0 until 9){
//                for(z in 0 until 17){
//                    if(i < 1378){
//                        heatmaps[0][x][y][z] = floatHeatmapArray[i]
//                        i++
//                    }
//                }
//            }
//        }
//
//        for(x in 0 until 9){
//            for(y in 0 until 9){
//                for(z in 0 until 34){
//                    if(j < 2755){
//                        offsets[0][x][y][z] = floatOffsetArray[j]
//                        j++
//                    }
//                }
//            }
//        }

        val sigmoidConversion = heatmaps
        for(x in 0 until 9){
            for(y in 0 until 9){
                for(z in 0 until 17){
                    val f = sigmoidConversion[0][x][y][z]
                    val sigmoid = sigmoid(f)
                    val df = DecimalFormat("#.########")
                    df.roundingMode = RoundingMode.HALF_UP
                    val sigmoidf = df.format(sigmoid)
                    heatmaps[0][x][y][z] = sigmoidf.toFloat()
                }
            }
        }

        val height = heatmaps[0].size
        val width = heatmaps[0][0].size
        val numKeypoints = heatmaps[0][0][0].size
        // Finds the (row, col) locations of where the keypoints are most likely to be.
        val keypointPositions = Array(numKeypoints) { Pair(0, 0) }
        for (keypoint in 0 until numKeypoints) {
            var maxVal = heatmaps[0][0][0][keypoint]
            var maxRow = 0
            var maxCol = 0
            for (row in 0 until height) {
                for (col in 0 until width) {
                    heatmaps[0][row][col][keypoint] = heatmaps[0][row][col][keypoint]
                    if (heatmaps[0][row][col][keypoint] > maxVal) {
                        maxVal = heatmaps[0][row][col][keypoint]
                        maxRow = row
                        maxCol = col
                    }
                }
            }
            keypointPositions[keypoint] = Pair(maxRow, maxCol)
        }

        // Calculating the x and y coordinates of the keyPoints with offset adjustment.
        val xCoords = FloatArray(numKeypoints)
        val yCoords = FloatArray(numKeypoints)
        val confidenceScores = FloatArray(numKeypoints)
        keypointPositions.forEachIndexed { idx, position ->
            val positionY = keypointPositions[idx].second
            val positionX = keypointPositions[idx].first
            val hFirst = offsets[0][positionY][positionX][idx]
            val hSecond = offsets[0][positionY][positionX][idx + numKeypoints]
            val yCoord = (positionY * 32 + hSecond)
            val xCoord = (positionX * 32 + hFirst)
            yCoords[idx] = yCoord
            xCoords[idx] = xCoord
            confidenceScores[idx] = heatmaps[0][positionX][positionY][idx]
        }

        val person = Person()
        val keypointList = Array(numKeypoints) { KeyPoint() }
        var totalScore = 0.0f


        enumValues<NNModelPosenet.bodyPart>().forEachIndexed { idx, it ->
            keypointList[idx].bodyPart = it
            keypointList[idx].position.setX(xCoords[idx], resolution.screenWidth)
            keypointList[idx].position.setY(yCoords[idx], resolution.screenHeight)

            keypointList[idx].score = confidenceScores[idx]
            totalScore += confidenceScores[idx]
        }

        person.keyPoints = keypointList.toList()
        person.score = totalScore / numKeypoints
        return person
    }
}
