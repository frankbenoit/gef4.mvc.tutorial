package gef4.mvc.tutorial.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.gef4.common.properties.IPropertyChangeNotifier;
import org.eclipse.gef4.geometry.planar.Point;

import javafx.scene.paint.Color;

public class TextNode implements IPropertyChangeNotifier {
	
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public final String POSITION_PROPERTY = "position";
	public final String TEXT_PROPERTY = "text";
	
	public Point position;
	
	private String text;
	private Color color = Color.LIGHTSKYBLUE;
	
	public TextNode( double x, double y, String text ){
		position = new Point(x, y);
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

	public void doChange(){
		setPosition( new Point(
				position.x + Math.random()*10 - 5, 
				position.y + Math.random()*10 - 5));
		setText( String.format("%s %s", text.split(" ")[0], Math.round(Math.random() * 100)));
	}
	
	 @Override
	 public void addPropertyChangeListener(PropertyChangeListener listener) {
		 pcs.addPropertyChangeListener(listener);
	 }
	 @Override
	 public void removePropertyChangeListener(PropertyChangeListener listener) {
		 pcs.removePropertyChangeListener(listener);
	 }

	 public void setText(String text) {
		 String textOld = this.text;
		 this.text = text;
		 pcs.firePropertyChange(TEXT_PROPERTY, textOld, text);
	 }
	 public void setPosition(Point position) {
		 Point positionOld = this.position;
		 this.position = position;
		 pcs.firePropertyChange(POSITION_PROPERTY, positionOld, position);
	 }


}
