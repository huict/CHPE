# Python training of feedback neural network

## pose_estimation.py

__pose_estimation.py__ is a Python application that creates a useable dataset of PoseNet results out of a directory of images. It creates ```.json``` files that can be loaded into __generate_model.py__ and are used to train and test the model.  The application offers several command line interface (CLI) arguments to use for flexibility and testing purposes. Use the ```-h``` argument for a help message ([CLI Arguments](#cli_arguments)).
The directory to be used with this application should look like this:
```
    - Base Directory
        - Pose directory
            - Images ( png/jpg )
            - feedback_kort.txt
            - feedback_lang.txt
        - Pose directory
            - Images ( png/jpg )
            - feedback_kort.txt
            - feedback_lang.txt
        - etc.
```
#### JSON files

```poses.json``` is a ```.json``` file containing the results of the PoseNet neural network on a directory of images. This wil be the data that the neural network from __generate_model.py__ wil train and test on. 

the created ```poses.json``` file will contain the following data:
```
    - Name of the image
        - Keypoints
            - Position
                - X
                - Y
            - part
            - score
        - Score
    - Name of the image
        - Keypoints
            - Position
                - X
                - Y
            - part
            - score
        - Score
    - etc.
```

```labels.json``` is a ```.json``` file containing the corresponding labels and feedback of the ```poses.json``` file. This wil be the data that the neural network of __generate_model.py__ will base its classifiers on. 

The created ```labels.json``` file looks like this:
```
    -Name of the image
        - feedback_short
        - feedback_long
        - pose
    -Name of the image
        - feedback_short
        - feedback_long
        - pose
    - etc.
```
#### CLI arguments

This python file can be called from the command line with a few arguments:
1. ```-d or --imgdir```, Specify which directory should be used as a base directory. default = data_set/
2. ```-m or --model```, Specify which model should be used as a keypoint identifier, default = PoseNet.
3. ```-p or --poses``` Specify which ```poses.json``` file should be used to write the poses to, default = ```poses.json```
4. ```-l or --labels``` Specify which ```labels.json``` file should be used to write the labels to, default = ```labels.json```
5. ```-c or --clean``` Clean the ```.json``` files.

```pose-estimation.py -d <image directory> -m <model file> -p <poses file> -l <labels file> -c <clean file>```

## generate_model.py

#### Description
__generate_model.py__ is used to train a model and export it in ```.tflite``` format. It is designed to be used with the outputs from a PoseNet NN. The application offers several command line interface (CLI) arguments to use for flexibility and testing purposes. Use the ```-h``` argument for a help message ([CLI Arguments](#cli_arguments)).

#### Walkthrough
The application works roughly as follows:
1. It will try to find CLI arguments to use. If any are found they are used instead of the default values. If necessary the application will throw a error and exit.
2. Now it will load 2 ```.json``` files. 1 for the poses, a pose contains a name, keypoints with a score and a overall score for the pose. The second is a ```.json``` file with labels and feedback for all the poses. We need these to train the model.
3. After having prepared the data the application will start to create a model based on the supplied ```.json``` files.
4. Now that we have a initial model it will be trained using the labels.
5. Optionally a user can test the model by using the ```-c or --classify``` argument and supplying a pose from the supplied ```.json``` files. The application will then classify that pose.
6. Finally the model will be exported to a ```.tflite``` model.
7. Optionally the user can load the exported ```.tflite``` model using the ```-s or --show_model``` arguments to make sure exporting was succesfull.

#### <a name="cli_arguments"></a>CLI Arguments
The application uses the following arguments to give the user more flexibility and means to debug:

1. ```-p or --poses```, Specify which ```.json``` file you want to use for the poses that will be used in training the model. default = ```poses.json``` (See: [Requirements - json files](#req_json))
2. ```-l or --labels```, Specify which ```.json``` file you want to use for the labels that will be used in training the model. default = ```labels.json``` (See: [Requirements - json files](#req_json))
3. ```-m or --model```, Specify how the model will be named default = ```model``` (See: [Requirements - model file](#req_model))
4. ```--max_xy```, Specify the maximal x and y values used in normalizing poses. default = (257,257) (See: [Requirements - max_xy](#req_max))
5. ```-b or --batch```, Specify the batch size used in training the model. Must be a integer. default = 1
6. ```-e or --epochs```, Specify the amount of epochs used in training the mode. Must be a integer. default = 1000
7. ```-c or --classify```, Specify a pose to classify using the newly trained model. (See: [Requirements - classify](#req_classify))
8. ```-n or --no_normalize```, Specify that the supplied data must **NOT** be normalized.
9. ```-s or --show_model```, Specify that you want to see the newly created model. You will see the exported ```.tflite``` model after it has been loaded in.

#### Requirements
- <a name="req_json"></a>*```.json``` files*
The supplied ```.json``` files for the poses and labels must have the same index for each item. This is important since the training of the model depends on the fact that the index of a pose from the pose ```.json``` file corresponds the the index of that pose in the labels ```.json``` file. If the indexes do not match the network will be trained wrong.
- <a name="req_model"></a>*model file*
The user can supply a name for the model file but it is imortant to note that the user must **NOT** use a file extension in the name for the model.
- <a name="req_max"></a>*max_xy*
A user can specify the maximal x and y values used in normalizing the the pose data. It is important to note that the user must supply a tuple when using this argument (x,y).
- <a name="req_classify"></a> *classify*
A user can supply a pose to classify using the newly trained model. It is important to note that the supplied pose must be present in the pose ```.json``` file since we do not support supplying a whole pose in the CLI.