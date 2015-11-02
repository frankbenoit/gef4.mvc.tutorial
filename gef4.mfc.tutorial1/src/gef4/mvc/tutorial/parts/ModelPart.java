package gef4.mvc.tutorial.parts;

import org.eclipse.gef4.fx.nodes.FXGeometryNode;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;

import gef4.mvc.tutorial.model.Model;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class ModelPart extends AbstractFXContentPart<Group> {

	@Override
	public Model getContent() {
		return (Model)super.getContent();
	}

	@Override
	protected Group createVisual() {
		return new Group();
	}

	@Override
	protected void doRefreshVisual(Group visual) {
		Model model = getContent();
		RoundedRectangle roundRect = new RoundedRectangle( model.getRect(), 10, 10 );
		FXGeometryNode<RoundedRectangle> fxGeometryNode = new FXGeometryNode<>(roundRect);
		visual.getChildren().add( fxGeometryNode );
		fxGeometryNode.setFill( model.getColor() );
		fxGeometryNode.setStroke( Color.BLACK );
		fxGeometryNode.setStrokeWidth(2);
	}

	
}
