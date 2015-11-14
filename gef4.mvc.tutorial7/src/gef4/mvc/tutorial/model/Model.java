package gef4.mvc.tutorial.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.gef4.common.properties.IPropertyChangeNotifier;

@XmlRootElement
public class Model implements IPropertyChangeNotifier {

	@XmlTransient
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
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
		LinkedList<TextNode> oldNodes = new LinkedList<>(nodes);
		nodes.add( textNode );
		pcs.firePropertyChange("nodes", oldNodes, nodes );
	}
	
	 @Override
	 public void addPropertyChangeListener(PropertyChangeListener listener) {
		 pcs.addPropertyChangeListener(listener);
	 }
	 @Override
	 public void removePropertyChangeListener(PropertyChangeListener listener) {
		 pcs.removePropertyChangeListener(listener);
	 }

	 
	
	
}
