package gef4.mvc.tutorial.model;

import org.eclipse.gef4.geometry.planar.Point;

import javafx.scene.paint.Color;

public class Model {

	public final Point position = new Point();
	
	private String text = "Text ...";
	private Color color = Color.LIGHTSKYBLUE;
	
	public Model(){
		position.setLocation(50, 50);
	}
	
	public String getText() {
		return text;
	}

	public Point getPosition() {
		return position;
	}
	
	public Color getColor() {
		return color;
	}
}
