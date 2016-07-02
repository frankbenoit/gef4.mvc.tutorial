package gef4.mvc.tutorial.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
		int atIndex = nodes.size();
		addNode( textNode, atIndex );
	}
	public void addNode(TextNode textNode, int atIndex ) {
		LinkedList<TextNode> oldNodes = new LinkedList<>(nodes);
		nodes.add( atIndex, textNode );
		//pcs.firePropertyChange("nodes", oldNodes, nodes );
	}
	
	 @Override
	 public void addPropertyChangeListener(PropertyChangeListener listener) {
		 //pcs.addPropertyChangeListener(listener);
	 }
	 @Override
	 public void removePropertyChangeListener(PropertyChangeListener listener) {
		 //pcs.removePropertyChangeListener(listener);
	 }
}
