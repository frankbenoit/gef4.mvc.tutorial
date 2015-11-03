package gef4.mvc.tutorial.parts;

import org.eclipse.gef4.fx.nodes.FXGeometryNode;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;

import gef4.mvc.tutorial.model.Model;
import javafx.scene.paint.Color;

public class ModelPart extends AbstractFXContentPart<FXGeometryNode<RoundedRectangle>> {

	@Override
	public Model getContent() {
		return (Model)super.getContent();
	}

	@Override
	protected FXGeometryNode<RoundedRectangle> createVisual() {
		Model model = getContent();
		RoundedRectangle roundRect = new RoundedRectangle(model.getRect(), 10, 10 );
		return new FXGeometryNode<>(roundRect);
	}

	@Override
	protected void doRefreshVisual(FXGeometryNode<RoundedRectangle> visual) {
		Model model = getContent();
		visual.getGeometry().setBounds(model.getRect());
		visual.setFill( model.getColor() );
		visual.setStroke( Color.BLACK );
		visual.setStrokeWidth(2);
	}

	
}
