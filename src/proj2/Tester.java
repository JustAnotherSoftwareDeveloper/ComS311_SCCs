package proj2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.common.base.Stopwatch;

public class Tester {

	public static FileWriter p;
	public static void main(String[] args) throws IOException {
		/*
		p.write("Start This bullshit");
		p.write("Gen Small Graphs");
		generateWikiGraph(5000,20000,5000);
		p.write("Gen Large Graphs");
		generateWikiGraph(30000,120000,30000);
		p.close();
		*/
		createGraph("5000_graph.txt");
		createGraph("10000_graph.txt");
		createGraph("15000_graph.txt");
		createGraph("20000_graph.txt");
		createGraph("30000_graph.txt");
		createGraph("60000_graph.txt");
		createGraph("90000_graph.txt");
		createGraph("120000_graph.txt");

	}
	
	public static void createGraph(String filename) throws IOException {
		p=new FileWriter("graph_timing.txt",true);
		p.write(filename+"\n");
		p.write("Start Ref\n");
		Stopwatch s=Stopwatch.createStarted();
		RefGraphProcessor refProccessor=new RefGraphProcessor(filename);
		s.stop();
		p.write("Finish Ref: "+s.toString()+"\n");
		p.write("Start Real \n");
		s=Stopwatch.createStarted();
		GraphProcessor reportProccesor=new GraphProcessor(filename);
		p.write("Finish Real: "+s.toString()+"\n");
		p.close();
	}
	
	public static void generateWikiGraph(int initial, int number, int increment) throws IOException {
		WikiCrawler crawler;
		Stopwatch s;
		for(int i=initial; i<=number; i+=increment) {
			p=new FileWriter("graph_timing.txt",true);
			s=Stopwatch.createStarted();
			crawler=new WikiCrawler("/wiki/Computer_science",i,i+"_graph.txt");
			crawler.crawl();
			s.stop();
			p.write("Time to Create Graph of Size: "+i+" "+s.toString());
			p.close();
		}
	}

}
