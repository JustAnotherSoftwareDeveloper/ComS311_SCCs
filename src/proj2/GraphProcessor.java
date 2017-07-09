package proj2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import com.google.common.base.Stopwatch;

public class GraphProcessor {
	public HashMap<String,GraphNode> nodes;
	private Integer vertexNum; 
	//SCC Stuff
	private HashSet<HashSet<GraphNode>> SCCs;
	private HashMap<String,HashSet<GraphNode>> SCCSet;
	private Stack<GraphNode> SCCStack;
	private HashSet<GraphNode> SCCStackVisited;
	private int SCCIndexMaster;
	
	/**
	 * Constructor
	 * @param graphData
	 */
	public GraphProcessor(String graphData) {
		try {
			this.buildMap(graphData);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		this.generateSCCs(); //Generate Strongly Connected Graph
	}
	
	/**
	 * Builds the map with incoming/outgoing nodes for Graph
	 * taken from my 416 HW
	 * @throws FileNotFoundException Scanner Error
	 */
	private void buildMap(String fileName) throws Exception {
		nodes=new HashMap<>();
		Scanner fileRead=new Scanner(new File(fileName)); 
		//Assuming correct format
		vertexNum=Integer.valueOf(fileRead.nextInt()); 
		fileRead.nextLine(); //Finish Reading number line
		while (fileRead.hasNextLine()) {
			String line=fileRead.nextLine();
			String[] line_arr=line.split("\\s+");
			String fromNode=line_arr[0];
			String toNode;
			if (line_arr.length>1) {
				toNode=line_arr[1];
			}
			else {
				toNode="";
			}
			
			/*
			 * Create Nodes if Needed
			 */
			GraphNode to=null,from;
			if (nodes.containsKey(fromNode)) {
				from=nodes.get(fromNode); //Use existing Node
			}
			else {
				from=new GraphNode(fromNode); //Create New node
				nodes.put(from.getName(), from);
			}
			
			if (!toNode.equals("")) {
				if (nodes.containsKey(toNode)) {
					to=nodes.get(toNode);
				}
				else {
					to=new GraphNode(toNode);
					nodes.put(to.getName(), to);
				}
			}
			
			/*
			 * Add outgoing and incoming edges
			 * Will not add if the connection is to itself
			 * Since incoming and outgoing are sets, duplicates will automatically be filtered out
			 */
			if (to!=null && to.getName()!=from.getName()) {
				to.addIncomingNode(from);
				from.addOutgoingNode(to);
			}
			
		}
		fileRead.close();
		if (!vertexNum.equals(nodes.size())) {
			throw new Exception("vertex size != read size");
		}
	}
	/**
	 * Outdegree, simply gets from the internal map
	 * @param v: vertex name
	 * @return the outdegree, or -1 in error
	 */
	public int outDegree(String v) {
		if (nodes.containsKey(v)) {
			return nodes.get(v).getOutgoing().size();
		}
		else {
			//Node not found
			return -1;
		}
	}
	/*
	 * All of this is taken from the wikipedia page on
	 * Tarjans SCC algorithm. As such, it won't be commented on
	 * in any real detail
	 */
	private void generateSCCs() {
		this.SCCStackVisited=new HashSet<>();
		this.SCCIndexMaster=0;
		this.SCCSet=new HashMap<>();
		this.SCCs=new HashSet<>();
		this.SCCStack=new Stack<>();
		for (GraphNode v: nodes.values()) {
			if (v.SCCindex==-1) {
			
				SCCHelper(v);
			}
		}
	}
	
	private void SCCHelper(GraphNode v) {
		v.SCCindex=this.SCCIndexMaster;
		v.SCClowlink=this.SCCIndexMaster;
		
		this.SCCIndexMaster++;
		this.SCCStackVisited.add(v);
		this.SCCStack.push(v);
		
		for(GraphNode w: v.getOutgoing()) {
			if (w.SCCindex==-1) {
				this.SCCHelper(w);
				v.SCClowlink=Math.min(v.SCClowlink, w.SCClowlink);
			}
			else if (SCCStackVisited.contains(w)) {
				v.SCClowlink=Math.min(v.SCClowlink, w.SCClowlink);
			}
			
		}
		if (v.SCClowlink==v.SCCindex) {
			HashSet<GraphNode> component=new HashSet<>(); //Start new component
			GraphNode w;
			do {
				w=SCCStack.pop();
				this.SCCStackVisited.remove(w);
				component.add(w);
				
			}
			while (!w.getName().equals(v.getName()));
			this.SCCs.add(component);
		}
		//Add SSCs to map
		for(HashSet<GraphNode> s: SCCs) {
			for(GraphNode n: s) {
				this.SCCSet.put(n.getName(), s);
			}
		}
	}
	private HashSet<GraphNode> getSCC(GraphNode v) {
		if (this.SCCSet.containsKey(v.getName())) {
			return this.SCCSet.get(v.getName());
		}
		//This will never happen
		return new HashSet<>();
	}
	
	public ArrayList<String> componentVertices(String v) {
		GraphNode n;
		if (nodes.containsKey(v)) {
			n=nodes.get(v);
		}
		else {
			return new ArrayList<>(); //not found;
		}
		ArrayList<String> vertexNames=new ArrayList<>();
		HashSet<GraphNode> scc=this.getSCC(n);
		for(GraphNode g: scc) {
			vertexNames.add(g.getName());
		}
		return vertexNames;
	}
	/**
	 * 
	 * @param v one node
	 * @param u another node
	 * @return if they are in the same scc
	 */
	public boolean sameComponent(String v, String u) {
		if (!nodes.containsKey(v) || !nodes.containsKey(u)) { //they both have to be real nodes
			return false;
		}
		HashSet<GraphNode> vSCC=this.getSCC(nodes.get(v)); //get the scc
		return vSCC.contains(nodes.get(u)); //see if the other node is in it
		
	}
	/*
	 * simple getter
	 */
	public int numComponents() {
		return this.SCCs.size();
	}
	/**
	 * Simple for loop
	 */
	public int largestComponent() {
		int max=0;
		for(HashSet<GraphNode> s: this.SCCs) {
			if (s.size()>max) {
				max=s.size();
			}
		}
		return max;
	}
	
	ArrayList<String> bfsPath(String u,String v) {
		for(GraphNode g: nodes.values()) {
			g.bfsPred=null;
		}
		if (!nodes.containsKey(v)||!nodes.containsKey(u)) {
			return new ArrayList<>(); //one or more node invalid
		}  
		if (u.equals(v)) {
			return new ArrayList<>();// u and v are the same
		}
		//Init variables
		GraphNode vNode=nodes.get(v);
		GraphNode uNode=nodes.get(u);
		ArrayList<String> pathList=new ArrayList<>();
		LinkedList<GraphNode> queue=new LinkedList<>();
		HashSet<GraphNode> visited=new HashSet<>();
		//Prepare BFS loop
		queue.add(uNode);
		visited.add(uNode);
		//BFS Loop
		while(!queue.isEmpty()) {
			GraphNode g=queue.poll();
			
			for(GraphNode n: g.getOutgoing()) {
				if (!visited.contains(n)) {
					queue.add(n);
					n.bfsPred=g;
					visited.add(n);
					String gName=g.getName();
					if (gName.equals(v)) {
						break; //We've reached the destination
					}
				}
			}
		}
		if (!visited.contains(vNode)) {
			return new ArrayList<>(); //not found
		}
		GraphNode currNode=vNode;
		while (currNode!=null) {
			pathList.add(currNode.getName());
			currNode=currNode.bfsPred;
		}
		Collections.reverse(pathList);
		return pathList;
		
	}
}
