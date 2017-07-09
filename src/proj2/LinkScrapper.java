package proj2;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author michael sila
 * Class that parses an html link in a variety of different ways
 * used to extract the wikipedia links from an html page
 *
 */
public class LinkScrapper {
	private String fileContents; //Note that this is meant to be the contents of an html file, not the file name
	/**
	 * Simple constructor
	 */
	public LinkScrapper(String fileContents) {
		this.fileContents=fileContents;
	}
	/**
	 * 
	 * @return An arraylist with the modified wikipedia links, in order of when they appear (after 
	 * the first <p> tag). NOTE THIS will return everything down to the </html>
	 */
	public ArrayList<String> scrapeLink() {
		/*
		 * This has nothing to do with algorithms and everything to do with regex's 
		 */
		int PTagIndex= fileContents.toLowerCase().indexOf("<p>");
		if (PTagIndex==-1) {
			return new ArrayList<>();
		}
		String truncatedContents=fileContents.substring(PTagIndex);
		//I'm assuming all /wiki/ links are contained in hrefs, that seems to be a fair description
		String hrefRegex="href=\"\\/[wW]iki\\/[^:#\"]*\"";
		// Now we use pattern and matcher classes
		Pattern linkPattern=Pattern.compile(hrefRegex);
		truncatedContents=truncatedContents.replace('\n', ' ');
		Matcher linkMatcher=linkPattern.matcher(truncatedContents);
		//Now we add this to the arrayList
		ArrayList<String> links=new ArrayList<>();
		while (linkMatcher.find()) {
			String fullLink=linkMatcher.group(0); //This java library can fuck right off
			//Now we grab the substring that is the actual link
			int firstIndex=fullLink.indexOf("\"")+1;
			int lastIndex=fullLink.indexOf("\"",firstIndex);
			String actualLink=fullLink.substring(firstIndex,lastIndex);
			if (!links.contains(actualLink)) { //for times links appear multiple times
				links.add(actualLink);
			}
			
		}
		return links;
	}
	
}
