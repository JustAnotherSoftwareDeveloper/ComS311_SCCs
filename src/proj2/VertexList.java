package proj2;

import java.util.*;

public class VertexList {
	ArrayList<Integer> list;
	
	
	public VertexList() {
		list = new ArrayList<Integer>();
	}
	
	public void add(int n){
		list.add(n);
	}
	
	
	public int size(){
		return list.size();
	}
	
	public int get(int i) {
		return list.get(i);
	}
	
	public boolean search(int j) {
		for (int i = 0; i<list.size(); i++)
			if (list.get(i)==j)
				return true;
		return false;
	}

}