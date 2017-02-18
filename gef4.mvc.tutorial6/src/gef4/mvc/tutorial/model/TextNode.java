package gef4.mvc.tutorial.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.gef.geometry.planar.Point;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;

@XmlRootElement
public class TextNode {
	@XmlTransient
	public static final String POSITION_PROPERTY = "position";
	@XmlTransient
	public static final String TEXT_PROPERTY = "text";

	@XmlTransient // handled over setter/getter
	private SimpleObjectProperty<Point> position = new SimpleObjectProperty<Point>(this, POSITION_PROPERTY);

	@XmlTransient // handled over setter/getter
	private SimpleObjectProperty<String> text = new SimpleObjectProperty<String>(this, TEXT_PROPERTY);

	@XmlTransient // not handled now
	private Color color = Color.LIGHTSKYBLUE;

	public TextNode() {
		this.position.setValue(new Point(0, 0));
		this.text.setValue("");
	}

	public TextNode(double x, double y, String text) {
		this.position.setValue(new Point(x, y));
		this.text.setValue(text);
	}

	public String getText() {
		return text.getValue();
	}

	public Point getPosition() {
		return position.getValue().getCopy();
	}

	public void setText(String text) {
		this.text.setValue(text);
	}

	public void setPosition(Point position) {
		this.position.setValue(position);
	}

	public Color getColor() {
		return color;
	}

	public void doChange() {
		setPosition(new Point(getPosition().x + Math.random() * 10 - 5, getPosition().y + Math.random() * 10 - 5));
		setText(String.format("%s %s", getText().split(" ")[0], Math.round(Math.random() * 100)));
	}

	public void addPropertyChangeListener(ChangeListener<Object> observer) {
		position.addListener(observer);
		text.addListener(observer);
	}

	public void removePropertyChangeListener(ChangeListener<Object> observer) {
		position.removeListener(observer);
		text.removeListener(observer);
	}
}
