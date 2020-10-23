#Python training of feedback neuranl network

## pose_estimation.py

__pose_estimation.py__ is a Python application that creates a useable dataset of PoseNet results out of a directory of images. It creates JSON files that can be loaded into __generate_model.py__ and are used to train and test the model. 
The directory to be used with this application should look like this:
```
    - Base Directory
        - Pose directory
            - Images ( png/jpg )
            - feedback.txt
        - Pose directory
            - Images ( png/jpg )
            - feedback.txt
        - etc.
```
#### JSON files

Pose.JSON is a JSON file containing the results of the PoseNet neural network on a directory of images. This wil be the data that the neural network from __generate_model.py__ wil train and test on. 

the created pose.JSON file will contain the following data:
```
    - Name of the image
        - Keypoints
            - Position
                - X
                - Y
            - part
            - score
        - Score
```

labels.JSON is a JSON file containing the corresponding labels and feedback of the poses.JSON file. This wil be the data that the neural network of __generate_model.py__ will base its classifiers on. 

The created labels.JSON file looks like this:
```
    -Name of the image
        - feedback
        - pose
```
#### CLI arguments

This python file can be called from the command line with a few arguments:
1. ```-d or --imgdir```, Specify which directory should be used as a base directory. default = Data_set/
2. ```-m or --model```, Specify which model should be used as a keypoint identifier, default = PoseNet.
3. ```-p or --poses``` Specify which poses.JSON file should be used to write the poses to, default = poses.JSON
4. ```-l or --labels``` Specify which labels.JSON file should be used to write the labels to, default = label.JSON
5. ```-c or --clean``` Clean the jason files.

```pose-estimation.py -d <image directory> -m <model file> -p <poses file> -l <labels file> -c <clean file>```