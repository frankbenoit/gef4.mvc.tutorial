package gef4.mvc.tutorial.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.fx.anchors.IAnchor;
import org.eclipse.gef4.fx.nodes.Connection;
import org.eclipse.gef4.fx.nodes.PolyBezierConnectionRouter;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;

import gef4.mvc.tutorial.model.TextNode;
import gef4.mvc.tutorial.model.TextNodeRelation;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class TextNodeRelationPart extends AbstractFXContentPart<Connection> {

	public TextNodeRelationPart(){
	}
	@Override
	public TextNodeRelation getContent() {
		return (TextNodeRelation)super.getContent();
	}

	@Override
	protected Connection createVisual() {
		
		Connection visual = new Connection();
		visual.setRouter(new PolyBezierConnectionRouter());
		visual.getCurveNode().setStroke(Color.BLACK);
		visual.getCurveNode().setStrokeWidth(1);
		return visual;
	}

	@Override
	protected void doRefreshVisual(Connection visual) {
		TextNodeRelation model = getContent();
	}
	
	@Override
	public SetMultimap<TextNode, String> getContentAnchorages() {
		HashMultimap<TextNode, String> res = HashMultimap.create();
		TextNodeRelation nr = getContent();
		res.put( nr.getParent(), "START" );
		res.put( nr.getChild(), "END" );
		return res;
	}

	@Override
	public List<? extends Object> getContentChildren() {
		return Collections.emptyList();
	}

	@SuppressWarnings("serial")
	@Override
	protected void attachToAnchorageVisual( IVisualPart<Node, ? extends Node> anchorage, String role) {
		IAnchor anchor = anchorage.getAdapter(new TypeToken<Provider<? extends IAnchor>>() {}).get();
		if (role.equals("START")) {
			getVisual().setStartAnchor(anchor);
		} else if (role.equals("END")) {
			getVisual().setEndAnchor(anchor);
		} else {
			throw new IllegalStateException( "Cannot attach to anchor with role <" + role + ">.");
		}
	}

}
