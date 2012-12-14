import java.io.*;
import java.util.Random;

class FileWriteRandom {
	public static void main(String args[]) {
		Random randomGenerator = new Random();
		try {
			// Create file
			FileWriter fstream = new FileWriter("dummy.png");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < 10485760; i++) {
				out.write((char) randomGenerator.nextInt(256));
			}
			// Close the output stream
			out.close();
		} catch (Exception e) {
			// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}