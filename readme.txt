JavaLearner - A GUI for use with the Logistic Regression Model

// Contents //

Source code is included in the src folder
Three sample datasets and three sample outputs are also included in respective folders
Runnable JAR file for the JavaLearner Application

// JAR Compatability & GUI Sizing //

The runnable jar file should run on any machine with java installed, although it has been developed on MacOS on a screen 1280x800 resolution so it may need to be slightly adjusted for viewing on a different screen or operating system. 

It may also very small on high resolution screen (1080p or greater). Further work would need to be done to create a truly distributable GUI for any machine but this was not needed to fulfill the purpose of this assignment. The size can however be easily changed in the source code to produce a GUI that is suitable for any screen. 

// Using the Application //

The GUI is desigend to be easy for the user to run multiple tests. Parameters are eaily configured and messages are displayed on screen.

The user can choose a data file from any location on their computer. The data file should be in CSV form as .csv or .txt file. The file must contain data entries in rows, where the last column on the right is the label for the feature vales in each row. The data file shoudld also contain a feature row as the first row.

The user can specify an output file name. This will be written as .txt file in the same direcory as the JAR file or compiled source code is run in.

Once the user has picked their parameters, the run button starts the test. The system may appear unresponsive during this period and running a test with a small tolerance, small learning rate or large number of maximum iterations may take more time depening on the power of the machine. 

The system runs 10 tests and calculates the average results. Once this test is finished, a summary of the results is displayed in the messages panel and the full results are available as the file output.

// Sample Data sets and results // 

The sample data sets and results provide an idea to the user of the capability of the classifier and possible input paramter configurations.

