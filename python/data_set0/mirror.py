import os, sys, getopt
#Prevent tensorflow from printing warnings
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
import numpy as np
import cv2, math, json
from PIL import Image as im
from PIL import ImageOps as imops


def main(argv):
	print("start")
	image_directory = 'standing_with_the_bodyweight_on_one_leg/'
	mirrod_images = []	
	images = []
	
	for filename in os.listdir(image_directory):
		if filename.endswith(".jpg") or filename.endswith(".png") or filename.endswith(".jpeg"):
			images.append(filename)
			#print(filename)

    
 
    #rotated_image.show()
	# Will contain all the poses we want to add to the data_set.json file
	for chosen_image in images:
		print(chosen_image)
		filepath = image_directory + "/" + chosen_image

		# get the picture of which we want the keypoints
		#image_obj = im.open(filepath)
		originalImage = cv2.imread(filepath)
		#rotated_image = image_obj.rotate(-90)
		flipHorizontal = cv2.flip(originalImage, 1)
		
		#fliped_image = imops.flip(image_obj)
		saved_location = 'mirror_'  + chosen_image
		cv2.imwrite(saved_location, flipHorizontal)
		#image_obj.save(saved_location)

		
if __name__ == "__main__":
	main(sys.argv[1:])
		
		
