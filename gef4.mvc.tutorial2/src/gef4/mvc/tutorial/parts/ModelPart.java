package gef4.mvc.tutorial.parts;

import org.eclipse.gef4.fx.nodes.GeometryNode;
import org.eclipse.gef4.geometry.planar.Dimension;
import org.eclipse.gef4.geometry.planar.Rectangle;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;

import gef4.mvc.tutorial.model.Model;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
		
		Text text = new Text( model.getText() );
		text.setFont( Font.font("Monospace", FontWeight.BOLD, 50 ) );
		text.setFill(Color.BLACK);
		text.setStrokeWidth(2);
		
		// measure size
		new Scene(new Group(text));
		Bounds textBounds = text.getLayoutBounds();

		Rectangle bounds = new Rectangle( 
				model.getPosition(), 
				new Dimension( textBounds.getWidth() + textBounds.getHeight(), textBounds.getHeight() * 1.5 ));

		// the rounded rectangle
		{
			RoundedRectangle roundRect = new RoundedRectangle( bounds, 10, 10 );
			GeometryNode<RoundedRectangle> GeometryNode = new GeometryNode<>(roundRect);
			GeometryNode.setFill( model.getColor() );
			GeometryNode.setStroke( Color.BLACK );
			GeometryNode.setStrokeWidth(2);
			
			visual.getChildren().add( GeometryNode );
		}
		// the text
		{
			text.setTextOrigin( VPos.CENTER );
			text.setY( bounds.getY() + bounds.getHeight()/2);
			text.setX( bounds.getX() + bounds.getWidth()/2 - textBounds.getWidth()/2 );
			visual.getChildren().add( text );
		}
	}

	
}
