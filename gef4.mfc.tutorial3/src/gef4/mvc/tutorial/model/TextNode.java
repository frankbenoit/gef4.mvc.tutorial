package gef4.mvc.tutorial.model;

import org.eclipse.gef4.geometry.planar.Point;

import javafx.scene.paint.Color;

public class TextNode {
	public final Point position = new Point();
	
	private String text;
	private Color color = Color.LIGHTSKYBLUE;
	
	public TextNode( double x, double y, String text ){
		position.setLocation(x, y);
		this.text = text;
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
