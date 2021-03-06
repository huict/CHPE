#===========================================================================#
# 							generate_model.py								#
#---------------------------------------------------------------------------#
#	Authors	:	Duur Alblas & Maaike Hovenkamp								#
#	Version	:	V1.2														#
#	Date	:	28-1-2021													#
#---------------------------------------------------------------------------#
# A application that will train a neural network in classifying the outputs #
# from a PoseNet model. It will also export the model in a tflite format.	#
# Several CLI arguments for flexibility and testing purposes.				#
# WARNING! : Added theoretical implementation for hand values.				#
#																			#
#    This program is free software: you can redistribute it and/or modify	#
#    it under the terms of the GNU General Public License as published by	#
#    the Free Software Foundation, either version 3 of the License, or		#
#    (at your option) any later version.									#
#																			#
#    This program is distributed in the hope that it will be useful,		#
#    but WITHOUT ANY WARRANTY; without even the implied warranty of			#
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			#
#    GNU General Public License for more details.							#
#																			#
#   You should have received a copy of the GNU General Public License		#
#    along with this program.  If not, see <https://www.gnu.org/licenses/>.	#
#===========================================================================#
from typing import List
import os, sys, getopt
import numpy as np
import tensorflow as tf
import random

from itertools import islice
from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.models import Sequential
import json
import random

def load_data_from_JSON( file_name ):
	"""load_data_from_JSON

	Args:
		file_name (string): file name that contains the json formatted data.

	Returns:
		dictionary: Loaded data.
	"""	
	with open(file_name) as file:
		data = json.load(file)
	return data

def normalize_value(value, upper_range):
	"""Normalize_value function, normalizes a given value between 0,1

	Args:
		value (int, float): Value to normalize
		upper_range (int, float): Maximum posible range for the value

	Returns:
		int, float: normalized value
	"""	
	return value / upper_range

def prepare_pose_data(pose, do_normalize = False, x_max = 257, y_max = 257, finger_max = 2014):
	"""prepare_pose_data, takes a pose dictionary and returns it as a list.

	Args:
		pose (dictionary): pose to be transformed to list
		x_max (int): max value for x
		y_max (int): max value for y

	Returns:
		[type]: [description]
	"""	
	inputs = []
	keypoints = pose['keypoints']
	for keypoint in keypoints:
		inputs.append(normalize_value(keypoint['position']['x'], x_max) if do_normalize else keypoint['position']['x'])
		inputs.append(normalize_value(keypoint['position']['y'], y_max) if do_normalize else keypoint['position']['y'])
	
	try:
		fingers = pose['hand']
		for finger in fingers:
			inputs.append(normalize_value(finger['value'], finger_max) if do_normalize else fingers['value'])
	except:
		print("No hand data found.")		
	return inputs

def create_model(nr_inputs, amount_output_neurons, layer_list):
	"""create_model function, function that creates a NN to use on posenet output data.

	Args:
		nr_inputs (int): Amount of input neurons.
		amount_output_neurons (int, optional): Amount of possible outputs.

	Returns:
		keras sequential model: Created model
	"""	
	activation_function = 'relu'
	# Creating the base of the model
	model = Sequential()
	# First layer is the input layer
	model.add(tf.keras.layers.Dense(nr_inputs, input_shape=(nr_inputs,)))

	for size in layer_list:
		model.add(layers.Dense((size*nr_inputs), activation=activation_function))
	
	# Last layer is the output layer
	model.add(layers.Dense(amount_output_neurons, activation='softmax'))

	# Compile the model before return it
	#learning_rate=0.1
	model.compile(  optimizer=keras.optimizers.RMSprop(),
					loss=keras.losses.SparseCategoricalCrossentropy(),
					metrics=[keras.metrics.SparseCategoricalAccuracy()],)
	return model

def train_model(model, training_data, training_labels, batch_size, epochs, verbose = 2 ):
	"""Trains a given model

	Args:
		model (keras sequential model): Model to train.
		training_data (numpy array): Data to train the model with.
		training_labels (list): Corresponding labels of training data.
		batch_size (int): Training batch size.
		epochs (int): Amount of epochs to train the model.
		verbose (int): Display properties of keras, defaults = 0

	Returns:
		keras sequential model: trained model
	"""	
	#shuffle = True
	model.fit(np.array(training_data),np.array(training_labels), batch_size=batch_size, shuffle = True, epochs=epochs, verbose=2, validation_split=0.2)
	return model

def classify_pose(pose, model, label_names):
	"""classify_pose, classifies a given pose using the given model. For Debugging purposes.

	Args:
		pose (numpy array): pose to identify from the poses json file
		model (keras sequential model): model to be used to identify a pose

	Returns:
		prediction class: predicted output
	"""	
	test = np.array(pose).reshape(1,-1)
	result_index = np.argmax(model.predict(test))
	for label_name, index in label_names.items():  # for name, age in dictionary.iteritems():  (for Python 2.x)
		if index == result_index:
			answer = label_name
	return answer

def export_tfl_model(model, model_name):
	"""export_rfl_model, exports the model to tflite format.

	Args:
		model (tflite model): model to be exported
		model_name (string): the name of the model without extension.

	Returns:
		tflite model: tflite_model
	"""	
	model.save('dir_'+model_name)

	converter = tf.lite.TFLiteConverter.from_saved_model('dir_'+model_name) # path to the SavedModel directory
	tflite_model = converter.convert()

	with open(model_name+'.tflite', 'wb') as file:
  		file.write(tflite_model)

	return tflite_model

def load_tflite(model):
	"""load_tflite, loads and displays a tflite model. For Debugging purposes

	Args:
		model (string): name of the file to be loaded without the extension.
	"""	
	test_interpreter = tf.lite.Interpreter(model+'.tflite')
	print(test_interpreter.get_input_details())
	print(test_interpreter.get_output_details())

def shuffle(a,seed):
	#seed = 12345
	test = np.array(a)
	rand_state = np.random.RandomState(seed)
	rand_state.shuffle(test)
	return test

	
def creat_validation_data(a):
	N = 153 * 2
	res = list(islice(reversed(a), 0, N))
	res.reverse()
	#print (len(res))
	#print (len(a))
	return res
	

def main(argv):
	# Prepare variables

		# Files 
	poses_json = 'poses.json'
	labels_json = 'labels.json'
	model_name = 'model'
		# Used for normalization
	max_xy = (257,257)
	max_finger_value = 1024
	normalize_poses = True
		# Used in training network
	layer_list = [17,5,3]
	#[4,3,3,2]77%b10
	#[17,5,3,1]69%
	batch_size = 10
	epochs = 100000
		# Used for debugging
	show_model = False
	classify_me = False
	help_message = "generate_model.py  Copyright (C) 2020  Maaike Hovenkamp, Duur Alblas \n" + "This program comes with ABSOLUTELY NO WARRANTY; for details : https://www.gnu.org/licenses/gpl-3.0.nl.html \n" +"This is free software, and you are welcome to redistribute it \n" + "under certain conditions; https://www.gnu.org/licenses/gpl-3.0.nl.html \n"
	help_message += "\n"
	help_message +=	" -h or --help | This help message\n -p or --poses <poses.json> | A .json with poses.\n -l or --labels <labels.json> | A .json with labels.\n -m or --model <model> | A name for model .tflite file.\n -M or --max_xy <257,257> | A maximum X and Y value.\n -b or --batch <32> | Size of a batch.\n -e or --epochs <10000> | Number of epochs.\n -c or --classify <> | Classify a pose from the poses .json.\n -n or --no_normalize | Do not normalize data.\n -s or --show_model | Show the model statistics.\n -S or --shape <6,2> | Shape of the neural network layers.\n"

	# Listen for command line interface arguments
	try:
		options, _ = getopt.getopt(argv,"hp:l:m:M:b:e:c:nsS:",["help","poses=","labels=","model=","max_xy=","batch=","epochs=","classify=","no_normalize","show_model","shape="])
	except getopt.GetoptError:
		print(help_message)
		sys.exit(2)

	for option, arg in options:
		if option in ("-h", "--help"):
			print(help_message)
			sys.exit()
		elif option in ("-p", "--poses"):
			poses_json = arg
		elif option in ("-l", "--labels"):
			labels_json = arg
		elif option in ("-m", "--model"):
			model_name = arg
		elif option in ("-M", "--max_xy"):
			try:
				max_xy = tuple(map(int, arg.split(',')))
			except ValueError:
				print("The max_xy argument must be a of type integer 257,257")
				sys.exit(2)
		elif option in ("-b", "--batch"):
			batch_size = arg
		elif option in ("-e", "--epochs"):
			epochs = arg
		elif option in ("-c", "--classify"):
			classify_me = arg
		elif option in ("-n", "--no_normalize"):
			normalize_poses = False
		elif option in ("-s", "--show_model"):
			show_model = True
		elif option in ("-S", "--shape"):
			try:
				layer_list = list(map(int, arg.split(',')))
			except ValueError:
				print("The shape argument must be of type integer 2,6")
				sys.exit(2)

	# Check if the variables contain expected values and whether files exist
	if not os.path.exists(poses_json):
		print("The ",poses_json," file does not exist!")
		sys.exit(2)
	elif not os.path.exists(labels_json):
		print("The ",labels_json," file does not exist!")
		sys.exit(2)
	elif type(batch_size) is not int:
		print("The batch argument must be of type integer")
		sys.exit(2)
	elif type(epochs) is not int:
		print("The epochs argument must be of type integer")
		sys.exit(2)

	if not show_model:
		#Prevent tensorflow from printing warnings
		os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
		#Prevent tensorflow converter from printing warnings
		tf.get_logger().setLevel('ERROR')

	# Load the json data
	poses_load = load_data_from_JSON(poses_json)
	labels_load = load_data_from_JSON(labels_json)

	# Prepare labels for training
	keys = []
	for key in poses_load.keys():
		keys.append(key)
	training_data = []
	label_per_image = []
	label_indexes = {}
	index = 0
	for key in keys:
		training_data.append(prepare_pose_data(poses_load[key], normalize_poses, max_xy[0], max_xy[1], max_finger_value))
		temp_label = labels_load[key]['pose']
		if temp_label not in label_indexes.keys():
			label_indexes[temp_label] = index
			index+=1
		label_per_image.append(label_indexes[temp_label])
	
	

	#print(len(training_data[0])
	print(len(label_indexes))
	
	# Create model
	model = create_model(len(training_data[0]), len(label_indexes), layer_list)
	# Train the model using the training data, labels and defined batch size and amount of epochs
	#print(training_data.shape())
	#print(label_per_image.shape())
	
	training_data = shuffle(training_data,12345)
	label_per_image = shuffle(label_per_image,12345)
	
	print(training_data.shape)
	print(label_per_image.shape)
	#temp1 = training_data[1227:]
	#temp2 = label_per_image[1227:]
	#training_data = np.concatenate((training_data[:1226], training_data[:1226]))
	#label_per_image = np.concatenate((label_per_image[:1226],label_per_image[:1226]))
	#training_data = shuffle(training_data,12347)
	#label_per_image = shuffle(label_per_image,12347)
	#training_data = np.concatenate((training_data, temp1))
	#label_per_image = np.concatenate((label_per_image,temp2))
	model.summary()
	
	
	#print(training_data)
	#print(label_per_image)
	
	
	#valitation_data = creat_validation_data(training_data)
	#valitation_label = creat_validation_data(label_per_image)
#	print(len(label_per_image))
#	print(len(training_data))

	trained_model = train_model(model, training_data, label_per_image, batch_size ,epochs)
	
	
	

	# Classify a supplied pose from the supplied poses json file
	if classify_me:
		test_pose = prepare_pose_data(poses_load[classify_me], normalize_poses, max_xy[0], max_xy[1])
		print( "______________________________________")
		print("classified pose: ", classify_pose(test_pose, model, label_indexes))
		print("Feedback short: ", labels_load[classify_me]["feedback_short"])
		print("Feedback long: ", labels_load[classify_me]["feedback_long"])
		print( "______________________________________")

	# Export the trained model using the supplied name
	export_tfl_model( trained_model , model_name)

	# Show the details of the created model !Warning has to be a tflite model!
	if show_model:
		load_tflite(model_name)

# Remove the application's file name itself from the argument list
if __name__ == "__main__":
	main(sys.argv[1:])