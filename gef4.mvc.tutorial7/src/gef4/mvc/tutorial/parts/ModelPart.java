package gef4.mvc.tutorial.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import gef4.mvc.tutorial.model.Model;
import gef4.mvc.tutorial.model.TextNode;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

public class ModelPart extends AbstractFXContentPart<Group> implements PropertyChangeListener {

	@Override
	protected void doActivate() {
		super.doActivate();
		// TODO: fix this
		// getContent().addPropertyChangeListener(this);

	}

	@Override
	protected void doDeactivate() {
		// TODO: fix this
		// getContent().removePropertyChangeListener(this);

		super.doDeactivate();
	}

	@Override
	public Model getContent() {
		return (Model) super.getContent();
	}

	@Override
	protected Group createVisual() {
		return new Group();
	}

	@Override
	protected void doRefreshVisual(Group visual) {
	}

	@Override
	public List<? extends TextNode> doGetContentChildren() {
		Model model = getContent();
		return model.getNodes();
	}

	@Override
	protected void addChildVisual(IVisualPart<Node, ? extends Node> child, int index) {
		ObservableList<Node> children = getVisual().getChildren();
		Node visual = child.getVisual();
		children.add(visual);
	}

	@Override
	protected void doAddContentChild(Object contentChild, int index) {
		getContent().addNode((TextNode) contentChild, index);
	}

	@Override
	public void doRemoveContentChild(Object contentChild) {
		getContent().getNodes().remove(contentChild);
	}

	@Override
	protected void removeChildVisual(IVisualPart<Node, ? extends Node> child, int index) {
		ObservableList<Node> children = getVisual().getChildren();
		children.remove(index);
	}

	@Override
	public SetMultimap<? extends Object, String> doGetContentAnchorages() {
		return HashMultimap.create();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getContent()) {
			refreshVisual();
		}
	}

}
