// Cathal Breathnach
// 13362896
// 4BP

package cbreathnach;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JavaLearner extends JFrame {
	
	// GUI and test class to use with the logistic regression class
	// Allows user to easily configure the algorithm options without causing any errors
	// The user can select the input file and output file name
	// Each test run displays results in a messages panel
	// User must select a file for input before trying any tests
	
	private static final long serialVersionUID = 1L;
	private Container container;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	//GUI Components
	private JFileChooser fileChooser;
	private JTextField chosenFile;
	private JTextField outputFile;
	private JRadioButton noScaleButton;
	private JRadioButton ZNormButton;
	private JRadioButton ZeroOneButton;
	private JSpinner trainingSpinner;
	private JTextField learningRate;
	private JTextField iterValue;
	private JTextField tolValue;
	private JButton runTestButton;
	private JTextArea statusReport;
	
	//Variables
	private File input;
	private String fileName;
	private String writePath;
	private String[][] data;
	private int features;
	private int dataEntries;
	private int split;
	private double splitValue;
	private int trainSize;
	private int testSize;
	private double trainAccuracy;
	private double testAccuracy;
	private double trainStdDev;
	private double testStdDev;
	private double trainAccuracyValues[] = new double[10];;
	private double testAccuracyValues[] = new double[10];;
	private String scaleType;
	private double learningRateNumValue;
	private double tolNumValue;
	private int iterNumValue;	
	
	public JavaLearner(){
		
		super("Logistic Regression");
		container = getContentPane();
		layout = new GridBagLayout();
		container.setLayout(layout);
		constraints = new GridBagConstraints();
		
		//CREATE GUI COMPONENTS

	    //Get file for input
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv", "txt");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Select Data File");
		
	    JButton chooseFileButton = new JButton("Select Input File");
	    ChooseFileHandler chooseFileHandler = new ChooseFileHandler();
		chooseFileButton.addActionListener(chooseFileHandler);
		
		//Display Chosen File
		Box chosenFileBox = Box.createHorizontalBox();
	    chosenFile = new JTextField();
	    chosenFile.setEditable(false);
	    chosenFileBox.add(chosenFile);
	    chosenFileBox.setBorder(BorderFactory.createTitledBorder("Chosen Data File"));
	    chosenFileBox.setPreferredSize(new Dimension(750,50));   
	    
	    //Display Chosen File Output Location
	    Box outputFileBox = Box.createHorizontalBox();
	    outputFile = new JTextField("results");
	    outputFile.setEnabled(false);
	    outputFileBox.add(outputFile);
	    outputFileBox.add(new JLabel(".txt"));
	    outputFileBox.setBorder(BorderFactory.createTitledBorder("Enter Output File Location"));
	    outputFileBox.setPreferredSize(new Dimension(375,50));
	    
		//Scale Buttons
		noScaleButton = new JRadioButton("None", false);
		ZNormButton = new JRadioButton("Z-Norm.", false);
		ZeroOneButton = new JRadioButton("0-1 Norm.", true);
		noScaleButton.setEnabled(false);
		ZNormButton.setEnabled(false);
		ZeroOneButton.setEnabled(false);
		Box scaleBox = Box.createHorizontalBox();
	    ButtonGroup scaleButtons = new ButtonGroup();
	    scaleButtons.add(ZeroOneButton);
	    scaleButtons.add(ZNormButton);
	    scaleButtons.add(noScaleButton);
	    scaleBox.add(ZeroOneButton);
	    scaleBox.add(ZNormButton);
	    scaleBox.add(noScaleButton);
	    scaleBox.setBorder(BorderFactory.createTitledBorder("Scaling"));
	    scaleBox.setPreferredSize(new Dimension(375,50));
	    
	    //Split
	    Box trainingSplitBox = Box.createHorizontalBox();
	    JLabel splitLabel = new JLabel("Training Split %:");
	    SpinnerNumberModel spinNumber = new SpinnerNumberModel(67,10,90,1);
	    trainingSpinner = new JSpinner(spinNumber);
	    trainingSpinner.setEnabled(false);
	    trainingSplitBox.add(splitLabel);
	    trainingSplitBox.add(trainingSpinner);
	    trainingSplitBox.setBorder(BorderFactory.createTitledBorder("Data Split"));
	    trainingSplitBox.setPreferredSize(new Dimension(375,50));
	    
	    //Learning Rate
	    Box learningRateBox = Box.createHorizontalBox();
	    learningRate = new JTextField("0.05");
	    learningRate.setEnabled(false);
	    learningRateBox.add(learningRate);
	    learningRateBox.setBorder(BorderFactory.createTitledBorder("Learning Rate"));
	    learningRateBox.setPreferredSize(new Dimension(375,50));
	    
	    //Max Iterations
	    Box iterValueBox = Box.createHorizontalBox();
	    iterValue = new JTextField("50000");
	    iterValue.setEnabled(false);
	    iterValueBox.add(iterValue);
	    iterValueBox.setBorder(BorderFactory.createTitledBorder("Maximum Iterations"));
	    iterValueBox.setPreferredSize(new Dimension(375,50));
	 
	    //Tolerance
	    Box tolValueBox = Box.createHorizontalBox();
	    tolValue = new JTextField("0.00000001");
	    tolValue.setEnabled(false);
	    tolValueBox.add(tolValue);
	    tolValueBox.setBorder(BorderFactory.createTitledBorder("Convergence Tolerance"));
	    tolValueBox.setPreferredSize(new Dimension(375,50));
	    
		//Button to check status & add handler
	    runTestButton = new JButton("Run Test");
		RunTestHandler testHandler = new RunTestHandler();
		runTestButton.addActionListener(testHandler);
		runTestButton.setEnabled(false); 
		
		//add status report text field
		statusReport = new JTextArea();
		statusReport.setEditable(false);
		statusReport.setBorder(BorderFactory.createTitledBorder("Messages"));
		statusReport.setRows(15);
		JScrollPane statusReportScroll = new JScrollPane (statusReport);
	    statusReportScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    statusReportScroll.setPreferredSize(new Dimension(500,300));
		
		//ADD COMPONENTS TO OVERALL LAYOUT
		constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(chooseFileButton, 0,0,2,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(chosenFileBox, 1,0,2,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(outputFileBox, 2,0,1,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(trainingSplitBox, 2,1,1,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(scaleBox, 3,0,1,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(learningRateBox, 3,1,1,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(iterValueBox, 4,0,1,1);
	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(tolValueBox, 4,1,1,1);
	   	    
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    addComponent(runTestButton, 5,0,2,1);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(statusReportScroll, 6,0,2,1);
		
		setSize(770,590);
		setResizable(true);
		setVisible(true);
	}
	
	// ADD COMPONENT METHOD
	private void addComponent(Component component, int row, int column, int width, int height){
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(component, constraints);
		container.add(component);
	}
	
	// MAIN METHOD - Starts the GUI
	public static void main (String args[]){
		JavaLearner application = new JavaLearner();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// EVENT HANDLERS
	
	// Method to run the test based on the chosen parameters
	private class RunTestHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			try {
				
				//Get Scale Type
				if (noScaleButton.isSelected()) {
					scaleType = "NoScale";
				}
				else if (ZNormButton.isSelected()) {
					scaleType = "ZNorm";
				}
				else if (ZeroOneButton.isSelected()) {
					scaleType = "ZeroOne";
				}
				
				//Get Data Split
				split = (int) trainingSpinner.getValue();
				splitValue = split/100.0;
				
				//Get Learning Rate
				learningRateNumValue = Double.parseDouble(learningRate.getText());
				
				//Get Tolerance Value
				tolNumValue = Double.parseDouble(tolValue.getText());
				
				//Get Iterations Value
				iterNumValue = Integer.parseInt(iterValue.getText());
				
				//calculate training and test sizes
				trainSize = (int) (dataEntries*splitValue);
				testSize = dataEntries - trainSize;
				
				//Get write file name and add .txt extension
				writePath = (outputFile.getText()+".txt");
								
				//create file writer 
				PrintWriter writer;
				
				//write data characteristics
				writer = new PrintWriter(writePath, "UTF-8");
				writer.println("Logistic Regression Test Results\n");
				
				writer.println("Data File: " + chosenFile.getText());
				writer.println("Output File Name: " + writePath + "\n");
				
				writer.println("Data Entries: " + dataEntries);
				writer.println("Number of Features: " + features);
				writer.println("Data Split: " + split + "%");
				writer.println("Train Size: " + trainSize);
				writer.println("Test Size: " + testSize + "\n");
				
				writer.println("Scaling: " + scaleType);
				writer.println("Learning Rate: " + learningRate.getText());
				writer.println("Maximum Iterations: " + iterValue.getText());
				writer.println("Convergence Tolerance: " + tolValue.getText() + "\n\n");
				
				statusReport.append("Logistic Regression Test: \n");
				
				statusReport.append("\nData File: " + chosenFile.getText());
				statusReport.append("\nOutput File Name: " + writePath + "\n");
				
				statusReport.append("\nData Entries: " + dataEntries);
				statusReport.append("\nNumber of Features: " + features);
				statusReport.append("\nData Split: " + split + "%");
				statusReport.append("\nTrain Size: " + trainSize);
				statusReport.append("\nTest Size: " + testSize + "\n");
				
				statusReport.append("\nScaling: " + scaleType);
				statusReport.append("\nLearning Rate: " + learningRate.getText());
				statusReport.append("\nMaximum Iterations: " + iterValue.getText());
				statusReport.append("\nConvergence Tolerance: " + tolValue.getText() +"\n");
				
				//loop to run 10 different tests
				for (int i = 0; i<10; i++) {
				
					//shuffle data
					String[][] shuffledData = ArrayOps.shuffleRowsExceptTop(data); 
					
					//select training and test data
					String[][] trainData = ArrayOps.selectSubArray(0, trainSize, 0, features-1, shuffledData);
					String[] trainLabels = ArrayOps.getColumn(features, ArrayOps.selectSubArray(1, trainSize, 0, features, shuffledData));
					String[][] testData = ArrayOps.selectSubArray(trainSize+1, dataEntries, 0, features-1, shuffledData);
					String[] testLabels = ArrayOps.getColumn(features, ArrayOps.selectSubArray(trainSize+1, dataEntries, 0, features, shuffledData));
				
					//create classifier
					LogisticRegression logreg = new LogisticRegression(learningRateNumValue, iterNumValue, tolNumValue, scaleType);
					
					//fit data with classifier
					logreg.fitModel(trainData,trainLabels,1);
					
					//test with training data & store training accuracy
					logreg.testModel(trainData,trainLabels,1);
					trainAccuracyValues[i] = logreg.getAccuracy();
					
					//test with testing data & store testing accuracy
					logreg.testModel(testData,testLabels,0);
					testAccuracyValues[i] = logreg.getAccuracy();
					
					//print results to message panel
					String runMessage = String.format("\nRun %d: \tTrain Accuracy: %.2f%% \tTest Accuracy: %.2f%%",
										i+1, trainAccuracyValues[i], testAccuracyValues[i]);
					
					statusReport.append(runMessage);
					
					//get results to write to file
					String[] testResults = logreg.getResults();
					
					//write data to file
					writer.printf("\nRun: %d\n",i+1);
					
					for(int j=0; j<testResults.length; j++) {
						writer.println(testResults[j].toString());
					}
				}
				
				//calculate average accuracy and std deviations values
				trainAccuracy = ArrayOps.getAverage(trainAccuracyValues);
				testAccuracy = ArrayOps.getAverage(testAccuracyValues);
				
				trainStdDev = ArrayOps.getStdDev(trainAccuracyValues);
				testStdDev = ArrayOps.getStdDev(testAccuracyValues);
				
				String trainFinalMessage = String.format("\n\nTrain Accuracy Average: %.2f%%, "
						+ "Standard Deviation: \u00B1 %.2f%%", trainAccuracy,trainStdDev);
				String testFinalMessage = String.format("\nTest Accuracy Average: %.2f%%, "
						+ "Standard Deviation: \u00B1 %.2f%%\n", testAccuracy,testStdDev);
				
				statusReport.append(trainFinalMessage);
				statusReport.append(testFinalMessage);
				statusReport.append("====================================================\n");
				
				writer.printf("\nTrain Accuracy Average: %.2f%%, " + "Standard Deviation: \u00B1 %.2f%%", trainAccuracy,trainStdDev);
				writer.printf("\nTest Accuracy Average: %.2f%%, " + "Standard Deviation: \u00B1 %.2f%%", testAccuracy,testStdDev);
				
				writer.close();
		
			} catch (FileNotFoundException e) {
				statusReport.append("\n===============================\n FILE NOT FOUND EXCEPTION"
									+ "\n===============================\n");
			} catch (UnsupportedEncodingException e) {
				statusReport.append("\n===============================\n FILE ENCODING EXCEPTION"
						+ "\n===============================\n");
			} catch (NumberFormatException e) {
				statusReport.append("\n===============================\n ERROR IN INPUTTED VALUES"
						+ "\n===============================\n");
			}
		}
	}
	
	
	private class ChooseFileHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				input = fileChooser.getSelectedFile();
				
				fileName = input.getPath();
				
				CSVReader fileReader = new CSVReader(fileName);
				
				fileReader.readFile();
				data = fileReader.getData();
				features = fileReader.getCols() - 1;
				dataEntries = fileReader.getRows() - 1;
				chosenFile.setText(fileName);
				
				outputFile.setEnabled(true);
				noScaleButton.setEnabled(true);
				ZNormButton.setEnabled(true);
				ZeroOneButton.setEnabled(true);
				trainingSpinner.setEnabled(true);
				learningRate.setEnabled(true);
				iterValue.setEnabled(true);
				tolValue.setEnabled(true);
				runTestButton.setEnabled(true);
			}
			
		}
	}
}

