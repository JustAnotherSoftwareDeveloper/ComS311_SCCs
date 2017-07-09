package proj2;

import java.util.HashSet;

/**
 *  The 
 * @author michael sila 
 *
 */
public class GraphNode {
	//corresponding with /wiki/... name
	private String name;
	//All incoming nodes
	private HashSet<GraphNode> incoming;
	//All outgoing nodes
	private HashSet<GraphNode> outgoing;
	//methods for SCC generation
	public int SCCindex;
	public int SCClowlink;
	//variable for bfs
	public GraphNode bfsPred;
	/**
	 *  Simple constructor
	 * @param n: name of graphnode
	 */
	public GraphNode(String n) {
		this.name=n;
		incoming=new HashSet<>();
		outgoing=new HashSet<>();
		this.SCCindex=-1;
		this.SCClowlink=-1;
	}
	/**
	 * 
	 * @param n: Node to add
	 */
	public void addIncomingNode(GraphNode n) {
		incoming.add(n);
	}
	/**
	 * 
	 * @param n: Node to add
	 */
	public void addOutgoingNode(GraphNode n) {
		outgoing.add(n);
	}
	
	public HashSet<GraphNode> getIncoming() {
		return this.incoming;
	}
	
	public HashSet<GraphNode> getOutgoing() {
		return this.outgoing;
	}
	
	public String getName() {
		return this.name;
	}
}
