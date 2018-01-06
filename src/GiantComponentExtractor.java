import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiantComponentExtractor {
	private static final String INPUT_FILE = "unconnected.txt";
	private static final String OUTPUT_FILE = "connected.txt";
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		buildConnectedNetwork();
		System.out.println("Finished");
	}
	
	/**
	 * Remove all nodes that are not in the giant component of the network.
	 * @throws IOException 
	 */
	public static void buildConnectedNetwork() throws IOException {	
		// Add all nodes to the list
		List<String> nodes = new ArrayList<String>();
		FileInputStream fstream = new FileInputStream(INPUT_FILE);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String[] lineArray = strLine.split(" ");
			if(!nodes.contains(lineArray[0])) {
				nodes.add(lineArray[0]);
			}
			if(!nodes.contains(lineArray[1])) {
				nodes.add(lineArray[1]);
			}
		}
		br.close();
		
		System.out.println("Count component sizes");
		// Get the component size for each node
		Map<String, Integer> nodeComponentSizes = new HashMap<String, Integer>();	
		while(!nodes.isEmpty()) {
			boolean nodeAdded = true;
			String currentNode = nodes.get(0);
			ArrayList<String> currentComponent = new ArrayList<String>();
			currentComponent.add(currentNode);
			// As long as nodes were added in the previous pass, go through the file again
			while(nodeAdded) {
				System.out.println("Pass with " + nodes.size() + " remaining nodes");
				nodeAdded = false;
				fstream = new FileInputStream(INPUT_FILE);
				br = new BufferedReader(new InputStreamReader(fstream));
				while ((strLine = br.readLine()) != null)   {
					String[] lineArray = strLine.split(" ");
					if(currentComponent.contains(lineArray[0]) && !currentComponent.contains(lineArray[1])) {
						String adjacentNode = lineArray[1];
						currentComponent.add(adjacentNode);
						nodeAdded = true;
						nodes.remove(adjacentNode);
					}
					if(currentComponent.contains(lineArray[1]) && !currentComponent.contains(lineArray[0])) {
						String adjacentNode = lineArray[0];
						currentComponent.add(adjacentNode);
						nodeAdded = true;
						nodes.remove(adjacentNode);
					}
				}
				br.close();
			}
			int componentSize = currentComponent.size();
			for(String s : currentComponent) {
				nodeComponentSizes.put(s, componentSize);
			}
			nodes.remove(0);
		}
		
		System.out.println("Build connected network");
		// Write the edges of the connected network to the output file
		int giantComponentSize = Collections.max(nodeComponentSizes.values());
		System.out.println("The giant component has a size of " + giantComponentSize);
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		fstream = new FileInputStream(INPUT_FILE);
		br = new BufferedReader(new InputStreamReader(fstream));
		while ((strLine = br.readLine()) != null)   {
			String[] lineArray = strLine.split(" ");
			if(nodeComponentSizes.get(lineArray[0]) == giantComponentSize) {
				bw.write(strLine);
				bw.newLine();
			}
		}
		br.close();
		bw.close();
	}
}
