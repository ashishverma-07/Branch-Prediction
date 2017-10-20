package tournamentPredictor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



/**
 * FileProcessor is used to 
 * process file operations
 * @author shubham
 * 
 */
public class FileProcessor {
	BufferedReader in;
	BufferedWriter bw;
	String read,write;

	public FileProcessor(BufferedReader bufReaderIn) {
			in = bufReaderIn;
	}
	
	public FileProcessor(String output) {
		write = output;
		File writeFile = new File(write);
		FileWriter fwrite;
		try {
			if(writeFile.exists()){
				writeFile.delete();
			}
			fwrite = new FileWriter(writeFile);
			bw = new BufferedWriter(fwrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * * readLineFromFile is used to read contents of input
	 * file.
	 * @return String as output
	 */
	
	public synchronized String readLineFromFile() {
		try {
			read = in.readLine();
		} catch (IOException e) {
			System.err.println("Error in Reading File");
			e.printStackTrace();
		}
		return read;
	}
	

	public synchronized void writeLineToFile(String data) {
		try {
			bw.write(data);
			bw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
