#===========================================================================#
# 							pose-estimation.py								#
#---------------------------------------------------------------------------#
#	Authors	:	Duur Alblas & Maaike Hovenkamp								#
#	Version	:	V1.0														#
#	Date	:	20-10-2020													#
#---------------------------------------------------------------------------#
# This is a small application with which you can generate a .json file		#
# containing pose data for each image in the image directory using a		#
# PoseNet model.															#
#===========================================================================#

import os, sys, getopt
#Prevent tensorflow from printing warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import numpy as np
import cv2, math, json

PARTS = {
	0: 'NOSE',
	1: 'LEFT_EYE',
	2: 'RIGHT_EYE',
	3: 'LEFT_EAR',
	4: 'RIGHT_EAR',
	5: 'LEFT_SHOULDER',
	6: 'RIGHT_SHOULDER',
	7: 'LEFT_ELBOW',
	8: 'RIGHT_ELBOW',
	9: 'LEFT_WRIST',
	10: 'RIGHT_WRIST',
	11: 'LEFT_HIP',
	12: 'RIGHT_HIP',
	13: 'LEFT_KNEE',
	14: 'RIGHT_KNEE',
	15: 'LEFT_ANKLE',
	16: 'RIGHT_ANKLE'
}

class KeyPoint():
	"""Keypoint class that holds a found body part from the tensorflow posenet network.
	"""	
	def __init__(self, index, position, confidence):
		x, y = position
		self.x = x
		self.y = y
		self.part = PARTS.get(index)
		self.confidence = confidence

	def to_string(self):
		"""For debugging purposes. Return the keypoint as a string.
		"""
		return 'part: {} location: {} confidence: {}'.format(
			self.part, (self.x, self.y), self.confidence)

def sigmoid(value):
	"""Sigmoid function

	Args:
		value (int,float): Value to apply sigmoid function on

	Returns:
		float: sigmoid(value)
	"""	
	return 1 / (1 + (-value)**2)

def get_keypoints( heatmaps, offsets, output_stride=32):
	"""get_keypoints fuction that gets the coordinates of bodyparts found in the heatmap en offset. 
	function made by stackoverflow user: https://stackoverflow.com/users/2806421/josh-sharkey
	post: https://stackoverflow.com/questions/60032705/how-to-parse-the-heatmap-output-for-the-pose-estimation-tflite-model/60105770#60105770

	Args:
		heatmaps (numpy array): heatmap indicating posible positions for body parts.
		offsets (numpy array): array containing values to calculate to vectors for the position of the body part.
		output_stride (int, optional): Depends the amount of squares in the heatmap. Defaults to 32.

	Returns:
		list of Keypoint: list of Keypoint containing the found body parts.
	"""	
	scores = sigmoid(heatmaps)
	num_keypoints = scores.shape[2]
	heatmap_positions = []
	offset_vectors = []
	confidences = []
	for ki in range(0, num_keypoints ):
		x,y = np.unravel_index(np.argmax(scores[:,:,ki]), scores[:,:,ki].shape)
		confidences.append(scores[x,y,ki])
		offset_vector = (offsets[y,x,ki], offsets[y,x,num_keypoints+ki])
		heatmap_positions.append((x,y))
		offset_vectors.append(offset_vector)
	image_positions = np.add(np.array(heatmap_positions) * output_stride, offset_vectors)
	keypoints = [KeyPoint(i, pos, confidences[i]) for i, pos in enumerate(image_positions)]
	return keypoints

def update_json_file(update_data, filename, overwrite):
	"""Update JSON file function, overwrites found poses to given JSON file

	Args:
		update_data (Dictionary): A dictionary containing poses to write to the JSON file.
		filename (str, optional): file name of which file you want the pose to be written to.
		overwrite (bool): overwrite indicates whether the file should be opened using option "w" regardless of it's existance
	"""	
	if overwrite:
		json_data = {}
	else:
		try:
			file = open(filename, "r")
			json_data = json.load(file)
			file.close()
		except:
			json_data = {}

	json_data.update(update_data)

	file = open(filename, "w")
	json.dump(json_data, file, indent=4)
	file.close()

def createPoseDict(keypoint_list):
	"""createPoseDict function, creates a dictionary containing the given pose keypoints.

	Args:
		keypoint_list (Keypoint): Keypoint item holding coordinates of found body part.

	Returns:
		Dict: Dictionary holding a pose.
	"""	
	pose = {}
	pose["keypoints"] = []
	keypoint_count = 0
	score_total = 0
	for keypoint in keypoint_list:
		pose["keypoints"].append(
			{"position":
				{"y":keypoint.y, 
				 "x":keypoint.x}, 
			 "part": keypoint.part, 
			 "score": float(keypoint.confidence) # float32 is not accepted by json
			}
		)
		score_total += keypoint.confidence
		keypoint_count += 1

	pose["score"] = score_total / keypoint_count
	return pose

def estimate_images(image_directory, model_path):
	"""Create JSON file containing positions found in images in image_directory

	Args:
		image_directory (string): name of directory containing images to process
		model_path (string): path to model to use

	Returns:
		dictionary: dictionary containing al found poses
	"""	
	interpreter = tf.lite.Interpreter(model_path)
	interpreter.allocate_tensors()

	input_details = interpreter.get_input_details()
	output_details = interpreter.get_output_details()

	height = input_details[0]['shape'][1]
	width = input_details[0]['shape'][2]

	images = []
	for filename in os.listdir(image_directory):
		if filename.endswith(".jpg") or filename.endswith(".png"):
			images.append(filename)

	# Will contain all the poses we want to add to the data_set.json file
	update_data = {}

	for chosen_image in images:
		filepath = image_directory + "/" + chosen_image

		# get the picture of which we want the keypoints
		image_src = cv2.imread(filepath)

		# resize the image so it fits the model
		image = np.float32(cv2.resize(image_src, (width, height)))
		# Add another dimension so it fits the model
		image_input = np.expand_dims(image.copy(), axis=0)

		if input_details[0]['dtype'] == np.float32 :
			image_input = (np.float32(image_input)-(height/2)) / (width/2)

		# insert the image in the model
		interpreter.set_tensor(input_details[0]['index'], image_input)

		# Run the model on the image
		interpreter.invoke()

		image_output_data = interpreter.get_tensor(output_details[0]['index'])
		image_offset_data = interpreter.get_tensor(output_details[1]['index'])

		image_heatmaps = np.squeeze(image_output_data)
		image_offset = np.squeeze(image_offset_data)

		# Calculate the coordinates using the heatmap, offset and output_stride
		KeyPointList = get_keypoints(image_heatmaps, image_offset)
		
		# add new pose to the updata_data dict using the file name (without extension) as key
		pose = image_directory
		index = 0
		for letter in pose:
			if letter == '/':
				pose = pose[index+1:]
			index += 1

		update_data[pose+"-"+chosen_image[:-4]] = createPoseDict(KeyPointList)
		
	return update_data

def create_labels(starting_directory):
	"""create_labels function, creates dictionary containing als images and corresponding feedback

	Args:
		starting_directory (string): path to pose folder

	Returns:
		dictionary: dictionary containging dirctionary with pose and feedback data
	"""	
	update_labels = {}
		
	f = open(starting_directory+"/feedback.txt", "r")
	feedback = f.read()
	pose = starting_directory
	index = 0
	for letter in pose:
		if letter == '/':
			pose = pose[index+1:]
		index += 1

	images = []
	for filename in os.listdir(starting_directory):
		if filename.endswith(".jpg") or filename.endswith(".png"):
			images.append(filename)

	for image in images:
		update_labels[pose+"-"+image[:-4]] = {"feedback": feedback, "pose": pose}

	return update_labels

def create_dataset(starting_directory, model_path):
	"""create_dataset function, creates the update data and update labels dictionary's

	Args:
		starting_directory (string): strating dictionary containing the folders with poses
		model_path (string): path to the PoseNet model

	Returns:
		dictionary: update_data : dictionary containing the x's and y's of the pose, update_labels, dictionary containig the feedback for each image.
	"""	
	update_data = {}
	update_labels = {}
	sub_folders = [f.path for f in os.scandir(starting_directory) if f.is_dir()]
	for folder in sub_folders:
		update_data.update(estimate_images(folder, model_path))
		update_labels.update(create_labels(folder))
	return update_data, update_labels

def main(argv):
	# Use the same model used in the Java Android environment
	model_file = "posenet_mobilenet_v1_100_257x257_multi_kpt_stripped.tflite"
	image_directory = 'Data_set/'
	poses_json = "poses.json"
	labels_json = "labels.json"
	help_message = 'pose-estimation.py -d <image directory> -m <model file> -p <poses file> -l <labels file> -c <clean file>'
	overwrite_file = False

	try:
		options, _ = getopt.getopt(argv,"hd:m:p:l:c",["imgdir=","model=","poses=","labels="])
	except getopt.GetoptError:
		print(help_message)
		sys.exit(2)

	for option, arg in options:
		if option == '-h':
			print(help_message)
			sys.exit()
		elif option in ("-d", "--imgdir"):
			image_directory = arg
		elif option in ("-m", "--model"):
			model_file = arg
		elif option in ("-p", "--poses"):
			poses_json = arg
		elif option in ("-l", "--labels"):
			labels_json = arg
		elif option in ("-c", "--clean"):
			overwrite_file = True

	if not os.path.exists(image_directory):
		print("The image directory does not exist!")
		sys.exit(2)
	elif not os.path.exists(model_file):
		print("The ",model_file," file does not exist!")
		sys.exit(2)

	# get all the poses found in the image_directory using the model_file model and generate labels
	update_data, update_labels = create_dataset(image_directory, model_file)
	print(update_labels)

	print(overwrite_file)

	# Update the poses.json file if it exists otherwise create it.
	update_json_file(update_data, poses_json, overwrite_file)
	update_json_file(update_labels, labels_json, overwrite_file)

# Remove the application's file name itself from the argument list
if __name__ == "__main__":
	main(sys.argv[1:])