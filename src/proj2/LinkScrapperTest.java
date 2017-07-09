package proj2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

public class LinkScrapperTest {

	@Test
	public void testScrapeLink() {
		String contents=null;
		try { //Don't be a jerk and pass an empty string
			Scanner scan=new Scanner(new File("linkscrapper_sample.txt"));
			contents= scan.useDelimiter("\\A").next();
			scan.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		LinkScrapper tScrape=new LinkScrapper(contents);
		ArrayList<String> actual=tScrape.scrapeLink();
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

}
