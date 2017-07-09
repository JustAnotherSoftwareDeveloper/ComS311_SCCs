package proj2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 
 * @author michael sila
 * 
 * Just like you guys wanted
 */
public class WikiCrawler {
	static String BASE_URL="https://en.wikipedia.org";
	private String seedUrl,fileName;
	private Integer maxPages;
	/**
	 * See project description
	 * @param seedUrl
	 * @param max
	 * @param fileName
	 */
	public WikiCrawler(String seedUrl,int max,String fileName) {
		this.seedUrl=seedUrl;
		this.maxPages=Integer.valueOf(max);
		this.fileName=fileName;
	}
	/**
	 * The logic is in the linkscrapper class
	 * @param doc the contents of an html wikipedia page
	 * @return an list of html links (relative)
	 */
	public ArrayList<String> extractLinks(String doc) {
		LinkScrapper extractor=new LinkScrapper(doc);
		return extractor.scrapeLink();
	}
	
	public void crawl() {
		HashSet<String> visited=new HashSet<>(); 
		LinkedList<String> queue=new LinkedList<>();

		HashSet<String> inGraph=new HashSet<>();
		queue.add(seedUrl);
		//Lines to write to output
		ArrayList<String[]> outputLines=new ArrayList<>();
		int currentEdges=0;
		int timesPolled=0;
		inGraph.add(queue.peekFirst());
		//Start BFS
		while (!queue.isEmpty()) {
			String currLink=queue.removeFirst();
			timesPolled++;
			if (timesPolled%100==0) { //Sleep as required
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			visited.add(currLink);
			ArrayList<String> neighbors=this.extractLinks(this.readHTML(currLink));
			for(String n: neighbors) {
				if (!visited.contains(n) && inGraph.size()<this.maxPages) {
					inGraph.add(n);
					visited.add(n);
					queue.addLast(n);
				}
				if (inGraph.contains(n) && !n.equals(currLink)) {
					String[] output={currLink,n};
					outputLines.add(output);
					currentEdges++;
				}
				
			}
		}
		/* Remove non-graph Strings
		ArrayList<String> trueOutput=new ArrayList<String>();
		int graphSize=inGraph.size();
		for(String[] s: outputLines) {
			boolean firstNode=inGraph.contains(s[0]);
			boolean SecondNode=inGraph.contains(s[1]);
			boolean same=s[0].equals(s[1]);
			if (firstNode && SecondNode && !same) {
				trueOutput.add(s[0]+" "+s[1]);
			}
		}
		*/
		//End BFS
		//Write to file
		PrintWriter writer=null;
		try {
			writer=new PrintWriter(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.println(this.maxPages);
		for (String[] line: outputLines) {
			writer.println(line[0]+" "+line[1]);
		}
		writer.close();
	}
	//Reads html from a link into a String.
	private String readHTML(String relativePath) {
		String fullPath=BASE_URL+relativePath; //Yes I know strings are immutable in java
		String contents=null;
		try { //Don't be a jerk and pass an empty string
			Scanner scan=new Scanner(new URL(fullPath).openStream(), "UTF-8");
			contents= scan.useDelimiter("\\A").next();
			scan.close();
	
		} catch (Exception e) {
			return "";
		}
		return contents;
	}
}
