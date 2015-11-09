package gef4.mvc.tutorial.parts;

import org.eclipse.gef4.fx.nodes.GeometryNode;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;

import gef4.mvc.tutorial.model.Model;
import javafx.scene.paint.Color;

public class ModelPart extends AbstractFXContentPart<GeometryNode<RoundedRectangle>> {

	@Override
	public Model getContent() {
		return (Model)super.getContent();
	}

	@Override
	protected GeometryNode<RoundedRectangle> createVisual() {
		return new GeometryNode<>();
	}

	@Override
	protected void doRefreshVisual(GeometryNode<RoundedRectangle> visual) {
		Model model = getContent();
		visual.setGeometry(new RoundedRectangle(model.getRect(), 10, 10 ));
		visual.setFill( model.getColor() );
		visual.setStroke( Color.BLACK );
		visual.setStrokeWidth(2);
	}

	
}
