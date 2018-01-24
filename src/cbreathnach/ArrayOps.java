// Cathal Breathnach
// 13362896
// 4BP

package cbreathnach;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayOps {
	
	// Class with a number of useful methods for Array manipulation
	
	// Copies 2D String Array
	public static String[][] copyArray(String[][] dataArray){
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		
		String[][] returnData = new String[rows][cols];
			
		//loop over array and copy contents
		for( int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				returnData[i][j] = dataArray[i][j];	
			}
		}
		return returnData;
	}
	
	// Copies 2D double Array
	public static double[][] copyArray(double[][] dataArray){
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		
		double[][] returnData = new double[rows][cols];
		
		//loop over array and copy contents
		for( int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				returnData[i][j] = dataArray[i][j];	
			}
		}
		return returnData;
	}
	
	// Copies 1D Array
	public static String[] copyArray (String[] dataArray) {
		return Arrays.copyOf(dataArray,dataArray.length);
	}
	
	// Return Selection of a 2D Array
	public static String[][] selectSubArray (int rowStart, int rowEnd, int colStart, 
											int colEnd, String[][] dataArray) {
		
		int colIndex;
		int rowIndex;
		
		int newRows = (rowEnd - rowStart) +1;
		int newCols = (colEnd - colStart) +1;
		
		String[][] returnData = new String[newRows][newCols];
		rowIndex = rowStart;
		
		for (int i = 0; i < newRows ; i++) {  
			colIndex = colStart;
			for (int j = 0; j < newCols; j++) {
				returnData[i][j] = dataArray[rowIndex][colIndex];
				colIndex++;
			}
			rowIndex++;
		}
		return returnData;
	}
	
	// Return Selection of a 1D Array
	public static String[] selectSubArray(int start, int end, String[] dataArray) {
		int length = (end - start + 1);
		String[] returnData = new String[length];
		returnData = Arrays.copyOfRange(dataArray, start, end);
		return returnData;
	}
	
	// Return Row from 2D Array
	public static String[] getRow(int rowIndex, String[][] dataArray) {
		String[] row = new String[dataArray[rowIndex].length];
		row = dataArray[rowIndex];
		return row;
	}
	
	// Return Column from 2D Array
	public static String[] getColumn(int colIndex, String[][] dataArray) {
		
		int rows = dataArray.length;
		String[] column = new String[rows];
		
		for (int i = 0; i < rows; i++) {
			column[i] = dataArray[i][colIndex];
		}
		
		return column;
	}

	// Print contents of 2D String Array
	public static void printArray(Object[][] dataArray) {
		
		if (dataArray == null) {
			System.out.print("Empty Array");
			return;
		}
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		
		System.out.print("\nData: \n");
		System.out.print("Number of Rows: " + (rows) + " \n");
		System.out.print("Number of Columns: " + (cols) + " \n\n");
	
		for (int i = 0; i < rows ; i++) {  
			for (int j = 0; j < cols; j++) {
				System.out.print(dataArray[i][j].toString() + "\t");
			}
			System.out.print("\n");
		}
	}
	
	// Print contents of 2D double Array
	public static void printArray(double[][] dataArray) {
		
		if (dataArray == null) {
			System.out.print("Empty Array");
			return;
		}
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		
		System.out.print("\nData: \n");
		System.out.print("Number of Rows: " + (rows) + " \n");
		System.out.print("Number of Columns: " + (cols) + " \n\n");
	
		for (int i = 0; i < rows ; i++) {  
			for (int j = 0; j < cols; j++) {
				System.out.printf("%.3f \t", dataArray[i][j] );
			}
			System.out.print("\n");
		}
	}
	
	// Print contents of 1D String Array
	public static void printArray(String[] dataArray) {
		
		if (dataArray == null) {
			System.out.print("Empty Array");
			return;
		}
		
		int length = dataArray.length;
		
		System.out.print("\nData: \n");
		System.out.print("Number of Entries: " + (length) + " \n");
		
		for (int i = 0; i < length ; i++) {  
			System.out.print(dataArray[i].toString() + "\t");
		}
		System.out.print("\n");
	}
	
	// Shuffle Rows of 2D String Array
	public static String[][] shuffleRows (String[][] dataArray){
		
		String[] swapFrom;
		String[] swapTo;
		
		int rows = dataArray.length;
		String[][] returnData = copyArray(dataArray);
		
		//swap rows a number of times
		//choose a random index and swap random*10*number of rows
		for ( int i = 0; i < (int)(rows*Math.random()*10); i++) {
			
			int row1 = (int) (Math.random()*rows);
			int row2 = (int) (Math.random()*rows);
			
			swapFrom = returnData[row1];
			swapTo = returnData[row2];
			
			returnData[row1] = swapTo;
			returnData[row2] = swapFrom;
		}
		return returnData;
	}
	
	// Shuffle Rows of 2D String Array Excluding the top row
	public static String[][] shuffleRowsExceptTop (String[][] dataArray){
		
		String[] swapFrom;
		String[] swapTo;

		int rows = dataArray.length;
		String[][] returnData = copyArray(dataArray);
			  
		//swap rows a number of times
		//choose a random index and swap 10*number of rows
		for ( int i = 0; i < (rows*10); i++) {
			//avoid choosing the top row by shifting the selection up by one
			int row1 = (int) ((Math.random()*(rows-1)) + 1);
			int row2 = (int) ((Math.random()*(rows-1)) + 1);
			
			swapFrom = returnData[row1];
			swapTo = returnData[row2];
			
			returnData[row1] = swapTo;
			returnData[row2] = swapFrom;
		}
		return returnData;
	}
	
	// Convert contents of 2D string array to double
	public static double[][] stringToDouble(String[][] dataArray){
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		
		double[][] returnData = new double[rows][cols];
		
		for (int i = 0; i < rows ; i++) {
			for (int j = 0; j < cols ; j++) {
				returnData[i][j] = Double.parseDouble(dataArray[i][j]);
			}
		}
		return returnData;
	}
	
	//Convert contents of 1D string array to double
	public static double[] stringToDouble(String[] dataArray){
		
		int entries = dataArray.length;
		double[] returnData = new double[entries];
		
		for (int i = 0; i < entries ; i++) {
			returnData[i] = Double.parseDouble(dataArray[i]);

		}
		return returnData;
	}
	
	// Normalise columns of double array
	public static double[][] normaliseColumns (double[][] dataArray){
		
		int rows = dataArray.length;
		int cols = dataArray[0].length;
		double[][] returnData = new double[rows][cols];
		double[] colMaxValues = new double[cols];
		double[] colMinValues = new double[cols];
		double[] colDiffValues = new double[cols];
		
		//find max in each column
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++ ) {
				
				//initialise with first values
				if (i == 0) {
					colMaxValues[j] = dataArray[i][j];
					colMinValues[j] = dataArray[i][j];
				}
				
				else if (dataArray[i][j] > colMaxValues[j]) {
					colMaxValues[j] = dataArray[i][j];
				}
				
				else if (dataArray[i][j] < colMinValues[j]) {
					colMinValues[j] = dataArray[i][j];
				}
			}
		}
		
		//get difference values 
		for (int i = 0; i < cols; i++) {
			colDiffValues[i] = colMaxValues[i] - colMinValues[i];
		}
		
		//normalise
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++ ) {
				returnData[i][j] = (dataArray[i][j]-colMinValues[j])/colDiffValues[j];
			}
		}
		return returnData;
	}

	// Returns Unique values in 1D String Array
	public static String[] getUniqueValues(String[] labelColumn) {
		
		ArrayList<String> labelValues = new ArrayList<String>();
		
		//initialise
		labelValues.add(labelColumn[0]);
		
		for (int i = 1; i < labelColumn.length; i++){	
			if (!labelValues.contains(labelColumn[i])) {
				labelValues.add(labelColumn[i]);	
			}
		}
		
		// Convert to String Array from Array List
		String[] returnData = new String[labelValues.size()];
		
		for (int i = 0; i < labelValues.size(); i++) {
			returnData[i] = labelValues.get(i);
		}
		return returnData;
	}
	
	// Get Average of 1D double Array
	public static double getAverage(double[] dataArray) {
		
		double average = 0.0;
		
		for (int i = 0; i <dataArray.length; i++) {
			average += dataArray[i];
		}
		
		average = average/dataArray.length;
		
		return average;
	}
	
	// Get Varience of 1D double Array
	public static double getVarience(double[] dataArray) {
		
		double varience = 0.0;
		double average = getAverage(dataArray);
		
		for (int i = 0; i <dataArray.length; i++) {
			varience += Math.pow((dataArray[i]- average),2);
		}
		
		varience = varience/dataArray.length;
		return varience;
	}
	
	// Get Standard Deviation of 1D Double Array
	public static double getStdDev(double[] dataArray){
		return Math.sqrt(getVarience(dataArray));
	}

}