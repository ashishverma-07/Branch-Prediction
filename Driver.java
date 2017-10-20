package tournamentPredictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class Driver {

	private static String input;
	private static String output;
	private static String statistics = "statistic.txt";
	private static BufferedReader bufferedReaderInput;
	
	/**
	 * @param args= arguments from command line
	 */
	public static void main(String[] args) {
		Driver.validateArgs(args);
		FileProcessor fpInp = new FileProcessor(bufferedReaderInput);
		FileProcessor fpOut = new FileProcessor(output);
		FileProcessor fpStat = new FileProcessor(statistics);
		BranchPrediction branchPrediction = new BranchPrediction(fpInp,fpOut,fpStat);
		branchPrediction.fileOperation();
	}


	
	
	private static void validateArgs(String args[]) {
		if (args.length == 2) {
			input = args[0];
			output = args[1];
			try{
			File inputFile = new File(input);
				if (inputFile.length() == 0) {
					System.err.println("Input file is empty");
					System.exit(-1);
				}
				FileInputStream fileInputStream = new FileInputStream(
						inputFile);
				bufferedReaderInput = new BufferedReader(
						new InputStreamReader(fileInputStream));


			}catch (FileNotFoundException e) {
				System.err.println("FIleNotFoundException- File not found in arguments passed at command line");
				e.printStackTrace();
				System.exit(-1);
			}
		} else {
			System.err.println("Invalid number of arguments. Expected 2 arguments");
			System.exit(-1);
		}
		
	}
	

}
