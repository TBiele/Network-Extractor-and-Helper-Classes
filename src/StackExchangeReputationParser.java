import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StackExchangeReputationParser {
	// Applicable to "Users" files from the Stack Exchange data dump
	private static final String INPUT_FILE = "Users.xml";
	private static final String OUTPUT_FILE = "out.txt";
	
	public static void main( String[] args ) throws Exception {
		writeReputationScores();
	}
	
	/**
	 * Extract a list of (ID, reputation score) pairs from the input file and write it to the output file.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void writeReputationScores() throws ParserConfigurationException, SAXException, IOException {
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.parse(new File(INPUT_FILE));
	    NodeList list = document.getElementsByTagName("row");

	    System.out.println("Start writing");
	    for(int i = 0; i < list.getLength(); i++) {
	    	NamedNodeMap attributeMap = list.item(i).getAttributes();
	    	String id = attributeMap.getNamedItem("Id").getNodeValue();
	    	String reputation = attributeMap.getNamedItem("Reputation").getNodeValue();
	    	bw.write(id + " " + reputation);
			bw.newLine();
	    }
	    bw.close();
	    System.out.println("Finished writing");
	}
}
