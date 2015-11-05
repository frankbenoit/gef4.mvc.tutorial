package gef4.mvc.tutorial.model;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Model {

	@XmlElement
	LinkedList<TextNode> nodes = new LinkedList<>();
	
	
	public LinkedList<TextNode> getNodes() {
		return nodes;
	}

	public void doChanges() {
		for (TextNode tn : nodes) {
			tn.doChange();
		}
	}

	public void addNode(TextNode textNode) {
		nodes.add( textNode );
	}
}
