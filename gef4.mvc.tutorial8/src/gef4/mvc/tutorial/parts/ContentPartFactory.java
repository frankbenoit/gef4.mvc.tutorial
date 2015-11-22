package gef4.mvc.tutorial.parts;

import java.util.Map;

import org.eclipse.gef4.mvc.behaviors.IBehavior;
import org.eclipse.gef4.mvc.parts.IContentPart;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

import gef4.mvc.tutorial.model.TextNode;
import gef4.mvc.tutorial.model.TextNodeRelation;
import javafx.scene.Node;

public class ContentPartFactory implements IContentPartFactory<Node> {

	@Inject
	private Injector injector;

	@Override
	public IContentPart<Node, ? extends Node> createContentPart(Object content, IBehavior<Node> contextBehavior, Map<Object, Object> contextMap) {

		if (content instanceof TextNode) {
			return injector.getInstance(TextNodePart.class);
		} else if (content instanceof TextNodeRelation) {
			return injector.getInstance(TextNodeRelationPart.class);
		} else {
			throw new IllegalArgumentException(content.getClass().toString());
		}
	};

}
