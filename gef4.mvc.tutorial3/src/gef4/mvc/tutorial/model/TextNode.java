package gef4.mvc.tutorial.model;

import org.eclipse.gef.geometry.planar.Point;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;

public class TextNode {

	public static final String POSITION_PROPERTY = "position";
	public static final String TEXT_PROPERTY = "text";

	public ObjectProperty<Point> position;
	private ObjectProperty<String> text;

	private Color color = Color.LIGHTSKYBLUE;

	public TextNode(double x, double y, String text) {
		Point point = new Point(x, y);

		this.position = new SimpleObjectProperty<Point>(this, POSITION_PROPERTY);
		this.text = new SimpleObjectProperty<String>(this, TEXT_PROPERTY);

		this.position.setValue(point);
		this.text.setValue(text);
	}

	public String getText() {
		return text.getValue();
	}

	public Point getPosition() {
		return position.getValue();
	}

	public Color getColor() {
		return color;
	}

	public void setText(String text) {
		this.text.setValue(text);
	}

	public void setPosition(Point position) {
		this.position.setValue(position);
	}

	public void doChange() {
		setPosition(new Point(getPosition().x + Math.random() * 10 - 5, getPosition().y + Math.random() * 10 - 5));
		setText(String.format("%s %s", getText().split(" ")[0], Math.round(Math.random() * 100)));
	}

	public void addPropertyChangeListener(ChangeListener<Object> pointObserver) {
		position.addListener(pointObserver);
	}

	public void removePropertyChangeListener(ChangeListener<Object> pointObserver) {
		position.removeListener(pointObserver);
	}
}
