import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WikipediaNetworkExtractor {
	// Wikipedia dataset can be found at "https://snap.stanford.edu/data/wiki-topcats.html"
	private static final String INPUT_FILE = "wiki-topcats.txt";
	// File containing a space separated list of the article ids (e.g. from "wiki-topcats-categories.txt")
	private static final String ARTICLES_FILE = "articles.txt";
	private static final String OUTPUT_FILE = "wiki-reduced.txt";
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		DefaultHashMap<String, Boolean> idMap = new DefaultHashMap<String, Boolean>(false);
		System.out.println("Read articles file");
		putArticleIds(idMap);
		System.out.println("Build network");
		buildWikipediaNetworkConsideringMutialLinks(idMap);
		System.out.println("Finished");
	}
	
	/**
	 * For each line check if both source and target page are contained in the map and if so write the line to the output file
	 * @param idMap Map that returns true for the id of each article that should be included and false for every other id
	 * @throws IOException
	 */
	public static void buildWikipediaNetwork(DefaultHashMap<String, Boolean> idMap) throws IOException {
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] edgeEndPoints = strLine.split(" ");
			if(idMap.get(edgeEndPoints[0]) && idMap.get(edgeEndPoints[1])) {
				bw.write(strLine);
				bw.newLine();
			}
		}
		bw.close();
		br.close();
	}
	
	/**
	 * For each line check if both source and target page are contained in the map and if so write the line to the output file.
	 * If two pages are linked to by the another page, add a link between those two pages.
	 * @param idMap Map that returns true for the id of each article that should be included and false for every other id
	 * @throws IOException
	 */
	public static void buildWikipediaNetworkConsideringMutialLinks(DefaultHashMap<String, Boolean> idMap) throws IOException {
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		Map<String, Set<String>> thirdPageLinks = new HashMap<String, Set<String>>();
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] edgeEndPoints = strLine.split(" ");
			if(idMap.get(edgeEndPoints[1])) {
				if(thirdPageLinks.get(edgeEndPoints[0]) == null) {
					thirdPageLinks.put(edgeEndPoints[0], new HashSet<String>());
				}
				Set<String> set = thirdPageLinks.get(edgeEndPoints[0]);
				for(String s : set) {
					String additionalEdge = edgeEndPoints[1] + " " + s;
					bw.write(additionalEdge);
					bw.newLine();
				}
				set.add(edgeEndPoints[1]);
			}
			if(idMap.get(edgeEndPoints[0]) && idMap.get(edgeEndPoints[1])) {
				bw.write(strLine);
				bw.newLine();
			}
		}
		bw.close();
		br.close();
	}
	
	/**
	 * For each line check if either source or target page are contained in the map and if so write the line to the output file
	 * @param idMap Map that returns true for the id of each article that should be included and false for every other id
	 * @throws IOException
	 */
	public static void buildExtendedWikipediaNetwork(DefaultHashMap<String, Boolean> idMap) throws IOException {
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] edgeEndPoints = strLine.split(" ");
			if(idMap.get(edgeEndPoints[0]) || idMap.get(edgeEndPoints[1])) {
				bw.write(strLine);
				bw.newLine();
			}
		}
		bw.close();
		br.close();
	}
	
	/**
	 * Put all article ids from the articles file into the given map
	 * @param idMap Map that is used
	 * @throws IOException
	 */
	public static void putArticleIds(DefaultHashMap<String, Boolean> idMap) throws IOException {
		FileInputStream fstream = new FileInputStream(ARTICLES_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] ids = strLine.split(" ");
			for(String s : ids) {
				idMap.put(s, true);
			}
		}
		br.close();
	}
}
