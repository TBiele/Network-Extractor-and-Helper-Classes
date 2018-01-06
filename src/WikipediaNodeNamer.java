import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class WikipediaNodeNamer {
	private static final String INPUT_FILE = "wiki-reduced.txt";
	private static final String OUTPUT_FILE = "wiki-reduced-named.txt";
	// Original "wiki-topcats-page-names.txt" from "https://snap.stanford.edu/data/wiki-topcats.html" needs to be modified
	private static final String NAMES_FILE = "wiki-topcats-page-names-modified.txt";
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		replaceIdsWithPageNames();
		System.out.println("Finished");
	}
	
	/**
	 * Replaces the article ids in the input file with the article names.
	 * @throws IOException
	 */
	public static void replaceIdsWithPageNames() throws IOException {
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		System.out.println("Build map from ids to names");
		Map<String, String> idToNameMap = buildIdToNameMap();
		
		System.out.println("Replace ids with page names");
		String strLine;
		int i = 0;
		while ((strLine = br.readLine()) != null)   {
			System.out.println("Line " + i);
			String[] edgeEndPoints = strLine.split(" ");
			System.out.println("Search for " + edgeEndPoints[0]);
			String source = idToNameMap.get(edgeEndPoints[0]);
			if(source == null) {
				source = getNameOfId(edgeEndPoints[0]);
				idToNameMap.put(edgeEndPoints[0], source);
			}
			System.out.println("Result: " + source);
			String target = idToNameMap.get(edgeEndPoints[1]);
			if(target == null) {
				target = getNameOfId(edgeEndPoints[1]);
				idToNameMap.put(edgeEndPoints[1], target);
			}
			String newStrLine = source + " " + target;
			bw.write(newStrLine);
			bw.newLine();
			i++;
		}
		bw.close();
		br.close();
	}
	
	public static Map<String, String> buildIdToNameMap() throws IOException {
		Map<String, String> resultMap = new HashMap<String, String>();
		FileInputStream fstream = new FileInputStream(NAMES_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] lineArray = strLine.split(" ");
			resultMap.put(lineArray[0], lineArray[1]);
		}
		br.close();
		return resultMap;
	}
	
	public static String getNameOfId(String id) throws IOException {
		FileInputStream fstream = new FileInputStream(NAMES_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] lineArray = strLine.split(" ");
			if(lineArray[0].equals(id)) {
				br.close();
				return lineArray[1];
			}
		}
		br.close();
		return null;
	}
}
