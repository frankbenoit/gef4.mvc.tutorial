package gef4.mvc.tutorial.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.gef4.common.properties.IPropertyChangeNotifier;
import org.eclipse.gef4.geometry.planar.Point;

import javafx.scene.paint.Color;

@XmlRootElement
public class TextNode implements IPropertyChangeNotifier {
	
	@XmlTransient
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	@XmlTransient
	public final String POSITION_PROPERTY = "position";
	@XmlTransient
	public final String TEXT_PROPERTY = "text";
	
	@XmlTransient // handled over setter/getter
	private Point position;
	
	@XmlTransient // handled over setter/getter
	private String text;
	
	@XmlTransient // not handled now
	private Color color = Color.LIGHTSKYBLUE;

	public TextNode(){
		this.position = new Point(0, 0);
		this.text = "";
	}
	
	public TextNode( double x, double y, String text ){
		position = new Point(x, y);
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public Point getPosition() {
		return position.getCopy();
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
