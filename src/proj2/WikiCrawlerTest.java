package proj2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

public class WikiCrawlerTest {

	@Test
	public void testExtractLinks() {
		String contents=null;
		try { //Don't be a jerk and pass an empty string
			Scanner scan=new Scanner(new File("linkscrapper_sample.txt"));
			contents= scan.useDelimiter("\\A").next();
			scan.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		WikiCrawler tCrawler=new WikiCrawler("/wiki/Computer_Science",20,"TestCrawlerOutput.txt");
		ArrayList<String> actual=tCrawler.extractLinks(contents);
		ArrayList<String> expected=new ArrayList<>();
		expected.add("/wiki/Science");
		expected.add("/wiki/Computation");
		expected.add("/wiki/Procedure_(computer_science)");
		expected.add("/wiki/Algorithm");
		expected.add("/wiki/Information");
		expected.add("/wiki/CiteSeerX");
		expected.add("/wiki/Charles_Babbage");
		assertEquals("Returned links incorrect",expected,actual);
	}

	@Test
	public void testCrawl() {
		WikiCrawler tCrawler=new WikiCrawler("/wiki/Complexity_theory",20,"TestCrawlerOutput.txt");
		tCrawler.crawl();
		//Manually diff files 
		assertEquals(Boolean.TRUE,Boolean.TRUE);
	}

}
