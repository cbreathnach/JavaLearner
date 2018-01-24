// Cathal Breathnach
// 13362896
// 4BP

package cbreathnach;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
	
	// Class to read a CSV file and return a String array with the contents
	
	private String filename;
	private int numRows;
	private int numCols;
	private ArrayList< ArrayList<String> > dataArray = new ArrayList< ArrayList<String> >();
	private BufferedReader reader;

	public CSVReader(String name) {
		filename = name;
	}
	
	public void readFile() {
		
		try {
			
			reader = new BufferedReader( new FileReader(filename));
			
			try {
				
				// Read first line & count rows
				String line;
				String[] readRow;
				
				// Read each line and split contents based on comma separator
				while ((line = reader.readLine()) != null) {
					
					ArrayList<String> addRow = new ArrayList<String>();
			
					readRow = line.split(",");
					   
					for(int i = 0; i < readRow.length ; i++) {
						addRow.add(readRow[i]);  
					}      
					
					dataArray.add(addRow);
				}
				
				numRows = dataArray.size();
				numCols = dataArray.get(0).size();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public int getRows() {
		return numRows;
	}
	
	public int getCols(){
		return numCols;
	}
	
	public String[][] getData(){
		
		String[][] dataReturn = new String[numRows][numCols];
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				dataReturn[i][j] = dataArray.get(i).get(j);
			}
		}
		return dataReturn;
	}
	
}
