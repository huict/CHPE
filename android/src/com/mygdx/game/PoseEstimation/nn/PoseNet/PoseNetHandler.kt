@file:Suppress("NON_EXHAUSTIVE_WHEN", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")

package com.mygdx.game.PoseEstimation.nn.PoseNet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.mygdx.game.DebugLog
import com.mygdx.game.Exceptions.InvalidFrameAccess
import com.mygdx.game.PoseEstimation.NNInserts
import com.mygdx.game.PoseEstimation.Resolution
import com.mygdx.game.PoseEstimation.nn.NNInterpreter
import com.mygdx.game.PoseEstimation.nn.PoseModels.NNModelPosenet
import com.mygdx.game.VideoHandler.VideoSplicer
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.json.Json
import javax.json.JsonArrayBuilder
import kotlin.math.exp


@SuppressLint("NewApi")
class PoseNetHandler(
        val context: Context,
        val filename: String,
        val nnInterpreter: NNInterpreter,
        val resolution: Resolution
) : AutoCloseable {

    private var interpreter: Interpreter? = null
    //private var gpuDelegate: GpuDelegate? = null

    private fun getInterpreter(): Interpreter {
        if (interpreter != null) {
            return interpreter!!
        }
        val options = Interpreter.Options()
        when (nnInterpreter) {
            NNInterpreter.CPU -> {
            }
            //NNInterpreter.GPU -> {
            //    gpuDelegate = GpuDelegate()
            //    options.addDelegate(gpuDelegate)
            //}
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

    /** Returns value within [0,1].   */
    private fun sigmoid(x: Float): Float {
        return (1.0f / (1.0f + exp(-x)))
    }

    /**
     * Scale the image to a byteBuffer of [-1,1] values.
     */
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

    /** Preload and memory map the model file, returning a MappedByteBuffer containing the model. */
    fun loadModelFile(path: String, context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(path)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        return inputStream.channel.map(
                FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength
        )
    }

    /**
     * Initializes an outputMap of 1 * x * y * z FloatArrays for the model processing to populate.
     */
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

    /** Crop Bitmap to maintain aspect ratio of model input.   */
//    private fun cropBitmap(bitmap: Bitmap): Bitmap {
//        val bitmapRatio = bitmap.height.toFloat() / bitmap.width
//        val modelInputRatio = resolution.modelHeight.toFloat() / resolution.modelWidth
//        var croppedBitmap = bitmap
//
//        // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
//        val maxDifference = 1e-5
//
//        // Checks if the bitmap has similar aspect ratio as the required model input.
//        when {
//            abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
//            modelInputRatio < bitmapRatio -> {
//                // New image is taller so we are height constrained.
//                val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
//                croppedBitmap = Bitmap.createBitmap(
//                        bitmap,
//                        0,
//                        (cropHeight / 2).toInt(),
//                        bitmap.width,
//                        (bitmap.height - cropHeight).toInt()
//                )
//            }
//            else -> {
//                val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
//                croppedBitmap = Bitmap.createBitmap(
//                        bitmap,
//                        (cropWidth / 2).toInt(),
//                        0,
//                        (bitmap.width - cropWidth).toInt(),
//                        bitmap.height
//                )
//            }
//        }
//        return croppedBitmap
//    }

    /**
     * Estimates the pose for a single person.
     * args:
     *      bitmap: image bitmap of frame that should be processed
     * returns:
     *      person: a Person object containing data about keypoint locations and confidence scores
     */

    fun startTheAnalysis(videoSplicer: VideoSplicer, videoId: Long, nnInsert: NNInserts): JsonArrayBuilder {
        val jsonArray = Json.createArrayBuilder()
        while (videoSplicer.isNextFrameAvailable) {
            try {
                val totalStartTime = System.nanoTime()

                // 500 - 1000 ms in java
                // in kotlin
                val ideka = System.nanoTime()
                val startEstimateTime = System.nanoTime()
                val p = estimateSinglePose(videoSplicer.nextFrame, ideka )
                val endEstimateTime = System.nanoTime()
                DebugLog.log("Estimate Pose call in Kotlin startAnalysis took: " + (endEstimateTime - startEstimateTime) / 1000000 + "ms")

                // 50 - 60 ms in java
                // in kotlin
                val startJsonTime = System.nanoTime()
                jsonArray.add(p.toJson())
                val endJsonTime = System.nanoTime()
                DebugLog.log("Add Person to Json in Kotlin took: " + (endJsonTime - startJsonTime) / 1000000 + "ms")

                // 160 - 180 ms in java
                // in kotlin
                val startInsertTime = System.nanoTime()
                nnInsert.insertPerson(p, videoId, videoSplicer.framesProcessed)
                val endInsertTime = System.nanoTime()
                DebugLog.log("Insert into database in Kotlin took: " + (endInsertTime - startInsertTime) / 1000000 + "ms")

                val totalEndTime = System.nanoTime()
                DebugLog.log("Total time in Kotlin took: " + (totalEndTime - totalStartTime) / 1000000 + "ms")
            }
            catch (invalidFrameAccess: InvalidFrameAccess) {
                Log.e("runVideo -> PoseNet - Iterator", "runVideo: ", invalidFrameAccess)
            }
        }
        return jsonArray
    }

    fun estimateSinglePose(bitmapb: Bitmap, ideka: Long): Person {
        //vereiste video 1:1, crop overbodig
        //val croppedBitmap = cropBitmap(bitmapb)

        val idekaEnd = System.nanoTime()
        DebugLog.log("Call from another function to EstimatePose: " + (idekaEnd - ideka) / 1000000 + "ms")

        // Created scaled version of bitmap for model input.
        val totalStartTime = System.nanoTime()
        val bitmap = Bitmap.createScaledBitmap(bitmapb, resolution.modelWidth, resolution.modelHeight, true)
        val inputArray = arrayOf(initInputArray(bitmap))

        val outputMap = initOutputMap(getInterpreter())

        getInterpreter().runForMultipleInputsOutputs(inputArray, outputMap)

        @SuppressWarnings("unchecked")
        val heatmaps = outputMap[0] as Array<Array<Array<FloatArray>>>
        val offsets = outputMap[1] as Array<Array<Array<FloatArray>>>

        val height = heatmaps[0].size
        val width = heatmaps[0][0].size
        val numKeypoints = heatmaps[0][0][0].size

        // Finds the (row, col) locations of where the keypoints are most likely to be.
        //0-1 ms
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
        // 0 ms
        val xCoords = IntArray(numKeypoints)
        val yCoords = IntArray(numKeypoints)
        val confidenceScores = FloatArray(numKeypoints)
        keypointPositions.forEachIndexed { idx, position ->
            val positionY = keypointPositions[idx].first
            val positionX = keypointPositions[idx].second
            yCoords[idx] = (
                    position.first / (height - 1).toFloat() * bitmap.height +
                            offsets[0][positionY][positionX][idx]
                    ).toInt()
            xCoords[idx] = (
                    position.second / (width - 1).toFloat() * bitmap.width +
                            offsets[0][positionY]
                                    [positionX][idx + numKeypoints]
                    ).toInt()
            confidenceScores[idx] = sigmoid(heatmaps[0][positionY][positionX][idx])
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

        val totalEndTime = System.nanoTime()
        DebugLog.log("Total function estimatePose() in Kotlin took: " + (totalEndTime - totalStartTime) / 1000000 + "ms")
        return person

    }
}
