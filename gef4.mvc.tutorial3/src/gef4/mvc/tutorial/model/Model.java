package gef4.mvc.tutorial.model;

import java.util.LinkedList;

public class Model {

	LinkedList<TextNode> nodes = new LinkedList<>();
	public Model(){
		nodes.add( new TextNode( 20, 20, "First"));
		nodes.add( new TextNode( 20, 120, "Second"));
	}
	
	public LinkedList<TextNode> getNodes() {
		return nodes;
	}

	public void doChanges() {
		for (TextNode tn : nodes) {
			tn.doChange();
		}
	}
}
