import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class MultiEdgesToWeightedEdgesConverter {
	private static final String INPUT_FILE = "multiEdges.txt";
	private static final String OUTPUT_FILE = "weightedEdges.txt";
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		convertMultiEdges();
		System.out.println("Finished");
	}
	
	/**
	 * Converts an unweighted edge list in which edges can appear multiple times into a weighted edge list where the weight
	 * of an edge corresponds to the number of times it occurs in the original edge list.
	 * @throws IOException
	 */
	public static void convertMultiEdges() throws IOException {
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		Set<String> edgeSet = new HashSet<String>();
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			if(!edgeSet.contains(strLine)) {
				FileInputStream fstreamInner = new FileInputStream(INPUT_FILE);
				BufferedReader brInner = new BufferedReader(new InputStreamReader(fstreamInner));
				String strLineInner;
				int weight = 0;
				// Count number of occurrences 
				while ((strLineInner = brInner.readLine()) != null)   {
					if(strLine.equals(strLineInner)) {
						weight++;
					}
				}
				brInner.close();
				// Write weighted edge to output file
				bw.write(strLine + " " + weight);
				bw.newLine();
				// Add edge to the set of completed edges
				edgeSet.add(strLine);
			}
		}
		br.close();
	    bw.close();
	}
}
