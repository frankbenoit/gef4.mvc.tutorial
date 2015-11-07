package gef4.mvc.tutorial.parts;

import java.util.List;

import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import gef4.mvc.tutorial.model.Model;
import gef4.mvc.tutorial.model.TextNode;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

public class ModelPart extends AbstractFXContentPart<Group> {

	private Group contentLayer;
	@Override
	public Model getContent() {
		return (Model)super.getContent();
	}

	@Override
	protected Group createVisual() {
		return (contentLayer = new Group());
	}

	@Override
	protected void doRefreshVisual(Group visual) {
	}

	@Override
	public List<? extends TextNode> getContentChildren() {
		Model model = getContent();
		return model.getNodes();
	}
	
	protected Group getContentLayer() {
		if (contentLayer == null) {
			createVisual();
		}
		return contentLayer;
	}

	@Override
	protected void addChildVisual(IVisualPart<Node, ? extends Node> child, int index) {
		ObservableList<Node> children = getContentLayer().getChildren();
		Node visual = child.getVisual();
		children.add(visual);
	}
}
