// Cathal Breathnach
// 13362896
// 4BP

package cbreathnach;

import cbreathnach.ArrayOps;

public class LogisticRegression{
	
	// Class to create and test logistic regression model
	// Allows for multi-class classification
	
	// Allows for user configuration of the learning rate, maximum iterations and tolerance inputted as doubles
	// Also allows for user defined scaling type. Must be a string input of "ZeroOne", "ZNorm" or else "NoScale"
	// The default scaling is not to scale if an different string is entered
	
	private String[][] trainData;
	private double[][] numTrainData;
	private String[] trainLabels;
	private int numTrainEntries;
	
	private String[][] testData;
	private double[][] numTestData;
	private String[] testLabels;
	private int numTestEntries;
	
	private String[] features;
	private String[] labels;
	
	private int numLabels;	
	private int numFeatures;
	
	private double[][] thetaValues;
	
	private double[] maxFeatureValues;
	private double[] minFeatureValues;
	private double[] diffFeatureValues;
	
	private double[] stdDevValues;
	private double[] meanValues;
	
	private int maxIter;
	private double alpha;
	private double tolerance;
	private String scaleType;
	
	private String[] results;
	
	private double accuracy;
	
	public LogisticRegression(double learningRate, int maxIter, double tolerance, String scaleTypeValue) {
		this.alpha = learningRate;
		this.tolerance = tolerance;
		this.maxIter = maxIter;
		this.scaleType = scaleTypeValue;
	}

	// Method to fit the logistic regression model while minimising loss
	// Data must be inputted as string array of data and a second string array of the same size containing the entry labels
	// A feature row is not used in the training of the model, but a third input parameter allows it's removal
	// The training data inputted should be of numeric form as read from a CSV file or otherwise
	// The training labels may be in numeric or string format. The algorithm will convert them to numeric values for training
	public void fitModel(String[][] inputData, String[] inputLabels, int featureRowIncluded) {
		
		//copy in test data into array
		trainData = ArrayOps.copyArray(inputData);
		trainLabels = inputLabels;
		numFeatures = trainData[0].length;
		
		// convert the training data into numeric data and scale if required
		if (featureRowIncluded == 0) {
			numTrainData = scaleTrainData(ArrayOps.stringToDouble(trainData));
		}
		
		// select the training data from the 2nd row downwards if a feature row is included
		else {
			features = ArrayOps.getRow(0, trainData);
			numTrainData = scaleTrainData(ArrayOps.stringToDouble(ArrayOps.selectSubArray
							(1,trainData.length-1,0,numFeatures-1,trainData)));
		}
	
		numTrainEntries = numTrainData.length;
		
		//count the labels
		labels = ArrayOps.getUniqueValues(trainLabels);
		numLabels = labels.length;
		
		// If only two labels -> Create a single model for binary classification
		if (numLabels == 2) {
			
			// create a 1D array of theta values
			thetaValues = new double[1][numFeatures+1];
			
			// create a new array to hold the numeric training label values
			double[] trainLabelsAdjusted = new double[trainLabels.length];
			
			for (int j = 0; j< numTrainEntries; j++) {
				if (trainLabels[j].equals(labels[0])) {
					trainLabelsAdjusted[j] = 0;
				}
				else {
					trainLabelsAdjusted[j] = 1;
				}
			}
			// Calculate Theta Values
			thetaValues[0] = calculateTheta(numTrainData, trainLabelsAdjusted);	
		}
		
		// Else if we have more than two labels -> create n models for n labels
		else {
			
			// create a 2D array based on the number of labels available
			thetaValues = new double[numLabels][numFeatures+1];
			
			// Loop over each label case and calculate the corresponding theta values
			for(int i = 0; i < labels.length; i++) {
			
				double[] trainLabelsAdjusted = new double[trainLabels.length];
				
				//adjust data values
				for (int j = 0; j< numTrainEntries; j++) {
					if (trainLabels[j].equals(labels[i])) {
						trainLabelsAdjusted[j] = 1;
					}
					else {
						trainLabelsAdjusted[j] = 0;
					}
				}
				thetaValues[i] = calculateTheta(numTrainData, trainLabelsAdjusted);
			}
		}	
	}

	// Method to test the trained model 
	// Data must be inputted as string array of data and a second string array of the same size containing the entry labels
	// The testing data inputted should be of numeric form as read from a CSV file or otherwise
	// The data must be of the same format as the testing data - same number of features
	// The testing labels may be in numeric or string format. The algorithm will convert them to numeric values for testing
	// The labels must only contain the same labels as the training labels
	// Setting the featureRowIncluded variable will ignore the first row in the training data
	public void testModel(String[][] data, String[] inputLabels, int featureRowIncluded) {
				
		int correct = 0;
		int incorrect = 0;
		String check;
		
		testLabels = inputLabels;
		
		// remove feature row if necessary
		if (featureRowIncluded == 0) {
			numTestEntries = data.length;
			testData = data;
		}
		else {
			numTestEntries = data.length-1;
			testData = ArrayOps.selectSubArray(1, numTestEntries, 0, numFeatures-1, data);
		}
		
		// convert data to numeric form
		numTestData = scaleTestData(ArrayOps.stringToDouble(testData));
		
		// create array to hold Strings as results
		// Add two to the length for the summary strings at the end
		results = new String[numTestData.length+2];
		
		// Test each entry by comparing to the expected result
		for (int i = 0; i < numTestData.length; i++) {
			
			String predicted = predictLabel(numTestData[i]);
			String expected = testLabels[i];
			
			if (predicted.equals(expected)) {
				correct++;
				check = "Correct";
			}
			else {
				incorrect++;
				check = "Incorrect";
			}
			
			// append result to results for output
			results[i] = String.format("Expected:%s \tPredicted:%s \t%s", expected,predicted,check);	
		}	
		
		//calculate accuracy and add to the results with summary statements
		accuracy = ((double)correct / ((double)correct + (double)incorrect)*100);
		String add1 = String.format("\nCorrect: %d \tIncorrect: %d", correct,incorrect);
		String add2 = String.format("Accuracy: %.3f %% \n", accuracy);
		results[numTestData.length] = add1;
		results[numTestData.length+1] = add2;
	}
	
	// calculate probability using the logistic function
	private double predict(double[] data, double[] theta) {
	
		double pred = theta[0];
		
		for (int i = 0; i < data.length; i++) {
			pred += theta[i+1] * data[i];
		}
		
		return  (1.0 / (1.0 + Math.exp(-pred)));
	}
	
	// Function to calculate the theta values by gradient descent
	// Takes available numeric data and binary label set as inputs
	// Loops until the max number of iterations are reached or until the model converges
	// The cost of the model is calculated after each iteration and the difference is calculated
	// The gradient descent function to minimise loss is used to update the theta values every iteration
	private double[] calculateTheta(double[][] data, double[] labels) {
		
		int length = data[0].length;
		double[] theta = new double[length+1];
		
		double oldCostSum = 0.0;
		double newCostSum;
		
		int iterCount = 0;
		int toleranceReached = 0;
		
		// Begin Loop
		while (iterCount < maxIter && toleranceReached == 0) {
			
			//reset cost values
			newCostSum = 0.0;
			
			//update theta values
			for (int i = 0; i < data.length; i++) {
				
				// prediction for set of input parameters
				double h = predict( data[i], theta);
				
				// calculate the gradient of the cost function
				double gradCost = (labels[i]-h)*h*(1.0-h);
				
				//update the intercept - does not have related data value
				theta[0] = theta[0] + alpha*gradCost;
				
				//Loop over the remaining data values and update the theta based on the cost
				for (int j = 0; j < data[i].length; j++) {
					theta[j+1] = theta[j+1] + alpha*gradCost*data[i][j];
				}
			}
			
			//calculate new cost
			for (int i = 0; i < data.length; i++) {
				double h = predict( data[i], theta);
				double cost = (labels[i]-h);
				newCostSum += cost;
			}
			
			// check for difference in squared error and compare to tolerance
			// Model converges if the difference is below the tolerance
			if ( (Math.abs( (oldCostSum*oldCostSum) - (newCostSum*newCostSum) ) ) < tolerance) {
				toleranceReached = 1;
				
				//Print Line to console for debugging to show point of convergence
				//System.out.println("\nTolerance Broken at " + iterCount);
			}
			
			//if (iterCount == (maxIter-1)) {
				//Print Line to console for debugging to show it has passed the maximum
				//System.out.println("\nIteration Count Reached");
			//}
			
			// New Cost becomes the old cost for the next run
			oldCostSum = newCostSum;
			
			iterCount++;
		}
		return theta;
	}
	
	// Function to predict the label based on probability for String input of data
	public String predictLabel(String[] predictData) {
		double[] data = scaleData(ArrayOps.stringToDouble(predictData));
		return predictLabel(data);
	}
	
	// Function to predict the label using a numeric array of data
	public String predictLabel(double[] predictData) {
		
		// Binary case - only compare for two outcomes
		if (numLabels == 2) {
			double prediction = predict(predictData, thetaValues[0]);
			return (labels[(int)Math.round(prediction)]);
		}
		
		// If trained for more that two labels
		// Check each one for the maximum probability
		else {
			
			int label_max_index;
			double max_prob;
			
			//check first label 
			label_max_index = 0;
			max_prob = predict(predictData, thetaValues[0]);
			
			//compare to the rest
			for (int j = 1; j < labels.length; j++){
				
				double prob = predict(predictData, thetaValues[j]);
				
				if(prob > max_prob) {
					label_max_index = j;
					max_prob = prob;
				}				
			}
			return labels[label_max_index];
		}
	}

	public String[] getResults() {
		return results;
	}
	
	public double getAccuracy() {
		return accuracy;
	}
	
	public String[] getFeatures() {
		return features;
	}

	public void printResults() {
		for(int j=0; j<results.length; j++) {
			System.out.println(results[j].toString());
		}
	}
	
	// Scale Training Data
	// Three possible scaling types available - ZeroOne, ZNorm and NoScale
	private double[][] scaleTrainData(double[][] dataArray){
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		double[][] returnData = new double[rows][cols];
		
		// Z Normalisation
		// Return the difference from the mean divided by the standard deviation for a column
		if (scaleType.equals("ZNorm")) {
			
			stdDevValues = new double[cols];
			meanValues = new double[cols];
			
			//get the standard deviation and mean values
			for(int i = 0; i<cols; i++) {
				stdDevValues[i] = ArrayOps.getStdDev(dataArray[i]);
				meanValues[i] = ArrayOps.getStdDev(dataArray[i]);
			}
			
			//update each value to return
			for (int i = 0; i < rows; i++) {
				returnData[i] = scaleData(dataArray[i]);
			}
		}
		
		// Zero-One normalisation
		// Scales the data between zero and one for all columns
		else if (scaleType.equals("ZeroOne")) {
		
			maxFeatureValues = new double[cols];
			minFeatureValues = new double[cols];
			diffFeatureValues = new double[cols];
			
			//find max in each column
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++ ) {
					
					//initialise with first values
					if (i == 0) {
						maxFeatureValues[j] = dataArray[i][j];
						minFeatureValues[j] = dataArray[i][j];
					}
					else if (dataArray[i][j] > maxFeatureValues[j]) {
						maxFeatureValues[j] = dataArray[i][j];
					}
					else if (dataArray[i][j] < minFeatureValues[j]) {
						minFeatureValues[j] = dataArray[i][j];
					}
				}
			}
			//get difference values 
			for (int i = 0; i < cols; i++) {
				diffFeatureValues[i] = maxFeatureValues[i] - minFeatureValues[i];
			}
			//normalise
			for (int i = 0; i < rows; i++) {
				returnData[i] = scaleData(dataArray[i]);
			}
		}
		
		// Else - default to NoScale. Return the same array
		else{
			returnData = ArrayOps.copyArray(dataArray);
		}
		
		return returnData;
	}
	
	// Scale 2D Array of Testing Data
	// Testing data must be scaled with the same scaling as the training data
	private double[][] scaleTestData(double[][] dataArray) {
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		double[][] returnData = new double[rows][cols];
		
		// Loop over each row and scale as appropriate
		for (int i = 0; i < rows; i++) {
			returnData[i] = scaleData(dataArray[i]);
		}
		return returnData;
	}
	
	// Scale 1D Array of Data as determined by the scaling method
	private double[] scaleData(double[] dataArray) {
		
		int entries = dataArray.length;
		double[] returnData = new double[entries];
		
		for (int i = 0; i<entries; i++) {
			
			if (scaleType.equals("ZNorm")) {
				returnData[i] = (dataArray[i] - meanValues[i])/stdDevValues[i];
			}
			
			else if (scaleType.equals("ZeroOne")) {
				returnData[i] = (dataArray[i]-minFeatureValues[i])/diffFeatureValues[i];	
			}
			
			// Default to NoScale
			else {
				returnData[i] =  dataArray[i];
			}
		}
		return returnData;
	}
	
}
