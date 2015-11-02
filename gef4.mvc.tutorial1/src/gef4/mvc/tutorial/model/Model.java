package gef4.mvc.tutorial.model;

import org.eclipse.gef4.geometry.planar.Rectangle;

import javafx.scene.paint.Color;

public class Model {

	public final Rectangle rect = new Rectangle();
	
//	private String text = "Text";
	private Color color = Color.LIGHTSKYBLUE;
	
	public Model(){
		rect.setBounds(20, 20, 150, 80);
	}
	
//	public String getText() {
//		return text;
//	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public Color getColor() {
		return color;
	}
	
	
}
