package model;

import java.io.BufferedReader;
import java.io.FileReader;

import javafx.util.Pair;

public class CSVReader {
	BufferedReader mBr;
	
	public CSVReader(String file) {
		try {
		BufferedReader br = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			System.out.println("Error opening file ");
		}
	}
	
	public CSVFile readFile() {
		return null;
	}
}
