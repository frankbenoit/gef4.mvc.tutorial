package gef4.mvc.tutorial.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.gef.common.beans.property.ReadOnlyListWrapperEx;
import org.eclipse.gef.common.collections.CollectionUtils;

import javafx.collections.ListChangeListener;

@XmlRootElement
public class Model {

	private static final String TEXT_NODES_PROPERTY = "nodes";

	@XmlElement
	private final ReadOnlyListWrapperEx<TextNode> nodes = new ReadOnlyListWrapperEx<>(this, TEXT_NODES_PROPERTY,
			CollectionUtils.<TextNode>observableArrayList());

	public List<TextNode> getNodes() {
		return nodes;
	}

	public void doChanges() {
		for (TextNode tn : nodes) {
			tn.doChange();
		}
	}

	public void addNode(TextNode textNode) {
		int atIndex = nodes.size();
		addNode(textNode, atIndex);
	}

	public void addNode(TextNode textNode, int atIndex) {
		nodes.add(atIndex, textNode);
	}

	public void addPropertyChangeListener(ListChangeListener<Object> listener) {
		nodes.addListener(listener);
	}

	public void removePropertyChangeListener(ListChangeListener<Object> listener) {
		nodes.removeListener(listener);
	}
}
