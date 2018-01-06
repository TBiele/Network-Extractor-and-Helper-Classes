import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoopRemover {
	private static final String INPUT_FILE = "withLoops.txt";
	private static final String OUTPUT_FILE = "withoutLoops.txt";
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		removeLoops();
		System.out.println("Finished");
	}
	
	/**
	 * Removes edges where source and target are the same.
	 * @throws IOException
	 */
	public static void removeLoops() throws IOException {
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		int loopCount = 0;
		while ((strLine = br.readLine()) != null)   {
			String[] lineArray = strLine.split(" ");
			if(!lineArray[0].equals(lineArray[1])) {
				bw.write(strLine);
				bw.newLine();
			}
			else {
				loopCount++;
			}
		}
		br.close();
	    bw.close();
	    System.out.println(loopCount + " loops were removed");
	}
}
