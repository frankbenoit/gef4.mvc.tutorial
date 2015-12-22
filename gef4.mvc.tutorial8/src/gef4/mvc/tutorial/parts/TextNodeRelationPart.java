package gef4.mvc.tutorial.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.fx.anchors.IAnchor;
import org.eclipse.gef4.fx.nodes.Connection;
import org.eclipse.gef4.fx.nodes.IConnectionRouter;
import org.eclipse.gef4.fx.nodes.PolyBezierConnectionRouter;
import org.eclipse.gef4.geometry.planar.ICurve;
import org.eclipse.gef4.geometry.planar.IMultiShape;
import org.eclipse.gef4.geometry.planar.Line;
import org.eclipse.gef4.geometry.planar.Path;
import org.eclipse.gef4.geometry.planar.Point;
import org.eclipse.gef4.geometry.planar.Polygon;
import org.eclipse.gef4.geometry.planar.Polyline;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;

import gef4.mvc.tutorial.model.TextNodeRelation;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class TextNodeRelationPart extends AbstractFXContentPart<Connection> {

	private static final String ROLE_START = "START";
	private static final String ROLE_END   = "END";
	protected static final double VSPACE = 10;

	public TextNodeRelationPart(){
	}
	@Override
	public TextNodeRelation getContent() {
		return (TextNodeRelation)super.getContent();
	}

	@Override
	protected Connection createVisual() {
		
		Connection visual = new Connection();
		visual.setRouter(new IConnectionRouter(){
			@Override
			public ICurve routeConnection(Point[] points) {
				if (points == null || points.length < 2) {
					return new Line(0, 0, 0, 0);
				}
				if( points.length > 2 ) throw new RuntimeException("len: "+points.length);
				Point start = ( points[0].x < points[1].x ) ? points[0] : points[1];
				Point end   = ( points[0].x > points[1].x ) ? points[0] : points[1];
				Point p1 = new Point( start.x + VSPACE, start.y );
				Point p2 = new Point( start.x + VSPACE, end.y );
				Polyline poly = new Polyline(start, p1, p2, end );
				return poly;
			}
		});
		visual.getCurveNode().setStroke(Color.BLACK);
		visual.getCurveNode().setStrokeWidth(1.5);
		return visual;
	}

	@Override
	protected void doRefreshVisual(Connection visual) {
	}
	
	@Override
	public SetMultimap<? extends Object, String> getContentAnchorages() {
		HashMultimap<Object, String> res = HashMultimap.create();
		TextNodeRelation nr = getContent();
		res.put( nr.getParent(), ROLE_START );
		res.put( nr.getChild(), ROLE_END );
		return res;
	}

	@Override
	public List<? extends Object> getContentChildren() {
		return Collections.emptyList();
	}

	@SuppressWarnings("serial")
	@Override
	protected void attachToAnchorageVisual( IVisualPart<Node, ? extends Node> anchorage, String role) {
		Provider<? extends IAnchor> provider = anchorage.getAdapter(new TypeToken<Provider<? extends IAnchor>>() {});
		
		IAnchor anchor = provider.get();
		if (role.equals(ROLE_START)) {
			getVisual().setStartAnchor(anchor);
		} else if (role.equals(ROLE_END)) {
			getVisual().setEndAnchor(anchor);
		} else {
			throw new IllegalStateException( "Cannot attach to anchor with role <" + role + ">.");
		}
	}

}
