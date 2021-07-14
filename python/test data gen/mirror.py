import os, sys, getopt
#Prevent tensorflow from printing warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import numpy as np
import cv2, math, json
from PIL import Image as im


 def main(argv):
	image_directory = 'chin_up'
	mirrod_images = []	
	images = []
	for filename in os.listdir(image_directory):
		if filename.endswith(".jpg") or filename.endswith(".png") or filename.endswith(".jpeg"):
			images.append(filename)

	# Will contain all the poses we want to add to the data_set.json file
	update_data = {}
	for chosen_image in images:
		filepath = image_directory + "/" + chosen_image

		# get the picture of which we want the keypoints
		image_src = cv2.imread(filepath)

		# resize the image so it fits the model
		#image = np.float32(cv2.resize(image_src, (width, height)))
		mirror_image = 
		
		
