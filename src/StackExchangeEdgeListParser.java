import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StackExchangeEdgeListParser {
	// Applicable to "Posts" files from the Stack Exchange data dump
	private static final String INPUT_FILE = "Posts.xml";
	private static final String OUTPUT_FILE = "edgeList.txt";
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		writeAcceptedAnswerEdgeList();
	}
	
	/**
	 * Converts the input file into an edge list that is written into the output file.
	 * An edge points from a user (referred to by id or display name) that asked a question 
	 * to the user that posted the accepted answer.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void writeAcceptedAnswerEdgeList() throws ParserConfigurationException, SAXException, IOException {
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    System.out.println("Parse input file");
	    Document document = builder.parse(new File(INPUT_FILE));
	    System.out.println("Input file parsed");
	    NodeList list = document.getElementsByTagName("row");
	    // For each question save accepted answer id and question owner id
	    Map<String, String> acceptedAnswers = new HashMap<String, String>();
	    Map<String, String> questionOwnerIds = new HashMap<String, String>();
	    System.out.println("Start reading");
	    for(int i = 0; i < list.getLength(); i++) {
	    	NamedNodeMap attributeMap = list.item(i).getAttributes();
	    	if(attributeMap.getNamedItem("PostTypeId").getNodeValue().equals("1") && attributeMap.getNamedItem("AcceptedAnswerId") != null) {
	    		String key = attributeMap.getNamedItem("Id").getNodeValue();
	    		String answerId = attributeMap.getNamedItem("AcceptedAnswerId").getNodeValue();
	    		String ownerId;
	    		
	    		if(attributeMap.getNamedItem("OwnerUserId") != null)
	    			ownerId = attributeMap.getNamedItem("OwnerUserId").getNodeValue();
	    		else
	    			ownerId = attributeMap.getNamedItem("OwnerDisplayName").getNodeValue().replaceAll("\\s","");
	    		
	    		acceptedAnswers.put(key, answerId);  
	    		questionOwnerIds.put(key, ownerId);
	    	}
	    }
	    System.out.println("Finished reading");
	    // Write edge list between owner ids
	    System.out.println("Start writing");
	    for(int i = 0; i < list.getLength(); i++) {
	    	NamedNodeMap attributeMap = list.item(i).getAttributes();
	    	if(attributeMap.getNamedItem("PostTypeId").getNodeValue().equals("2")) {
	    		String id = attributeMap.getNamedItem("Id").getNodeValue();
	    		String parentId = attributeMap.getNamedItem("ParentId").getNodeValue();
	    		if(acceptedAnswers.get(parentId) != null && acceptedAnswers.get(parentId).equals(id)) {
	    			String questionOwnerId = questionOwnerIds.get(parentId);
	    			String answerOwnerId;
	    			
		    		if(attributeMap.getNamedItem("OwnerUserId") != null)
		    			answerOwnerId = attributeMap.getNamedItem("OwnerUserId").getNodeValue();
		    		else
		    			answerOwnerId = attributeMap.getNamedItem("OwnerDisplayName").getNodeValue().replaceAll("\\s","");
		    		
	    			bw.write(questionOwnerId + " " + answerOwnerId);
	    			bw.newLine();	
	    		}
	    	}
	    }
	    bw.close();
	    System.out.println("Finished writing");
	}
	
	/**
	 * Converts the input file into an edge list that is written into the output file.
	 * An edge points from a user (referred to by id or display name) that asked a question 
	 * to a user that posted an answer (not necessarily the accepted one).
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void writeAnswerEdgeList() throws ParserConfigurationException, SAXException, IOException {
		File fout = new File(OUTPUT_FILE);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    System.out.println("Parse input file");
	    Document document = builder.parse(new File(INPUT_FILE));
	    System.out.println("Input file parsed");
	    NodeList list = document.getElementsByTagName("row");
	    // For each question save question owner id
	    Map<String, String> questionOwnerIds = new HashMap<String, String>();
	    System.out.println("Start reading");
	    for(int i = 0; i < list.getLength(); i++) {
	    	NamedNodeMap attributeMap = list.item(i).getAttributes();
	    	if(attributeMap.getNamedItem("PostTypeId").getNodeValue().equals("1")) {
	    		String key = attributeMap.getNamedItem("Id").getNodeValue();
	    		String ownerId;
	    		
	    		if(attributeMap.getNamedItem("OwnerUserId") != null)
	    			ownerId = attributeMap.getNamedItem("OwnerUserId").getNodeValue();
	    		else
	    			ownerId = attributeMap.getNamedItem("OwnerDisplayName").getNodeValue();
  
	    		questionOwnerIds.put(key, ownerId);
	    	}
	    }
	    System.out.println("Finished reading");
	    // Write edge list between owner ids
	    System.out.println("Start writing");
	    for(int i = 0; i < list.getLength(); i++) {
	    	NamedNodeMap attributeMap = list.item(i).getAttributes();
	    	if(attributeMap.getNamedItem("PostTypeId").getNodeValue().equals("2")) {
	    		String parentId = attributeMap.getNamedItem("ParentId").getNodeValue();
    			String questionOwnerId = questionOwnerIds.get(parentId);
    			String answerOwnerId;
    			
	    		if(attributeMap.getNamedItem("OwnerUserId") != null)
	    			answerOwnerId = attributeMap.getNamedItem("OwnerUserId").getNodeValue();
	    		else
	    			answerOwnerId = attributeMap.getNamedItem("OwnerDisplayName").getNodeValue();
	    		
    			bw.write(questionOwnerId + " " + answerOwnerId);
    			bw.newLine();	
	    	}
	    }
	    bw.close();
	    System.out.println("Finished writing");
	}
}
