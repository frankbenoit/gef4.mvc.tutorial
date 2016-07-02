package gef4.mvc.tutorial.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.fx.nodes.GeometryNode;
import org.eclipse.gef4.geometry.planar.Dimension;
import org.eclipse.gef4.geometry.planar.Rectangle;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import gef4.mvc.tutorial.model.TextNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TextNodePart extends AbstractFXContentPart<Group> implements PropertyChangeListener {

	private Text text;
	private GeometryNode<RoundedRectangle> fxRoundedRectNode;

	private final ChangeListener<Object> objectObserver = new ChangeListener<Object>() {
		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
			refreshVisual();
		}
	};

	@Override
	protected void doActivate() {
		super.doActivate();
		getContent().addPropertyChangeListener(objectObserver);
	}

	@Override
	protected void doDeactivate() {
		getContent().removePropertyChangeListener(objectObserver);
		super.doDeactivate();
	}

	@Override
	public TextNode getContent() {
		return (TextNode) super.getContent();
	}

	@Override
	protected Group createVisual() {
		Group group = new Group();
		text = new Text();
		fxRoundedRectNode = new GeometryNode<>();

		group.getChildren().add(fxRoundedRectNode);
		group.getChildren().add(text);
		return group;
	}

	@Override
	protected void doRefreshVisual(Group visual) {
		TextNode model = getContent();

		Font font = Font.font("Monospace", FontWeight.BOLD, 50);
		Color textColor = Color.BLACK;
		int textStrokeWidth = 2;

		text.setText(model.getText());
		text.setFont(font);
		text.setFill(textColor);
		text.setStrokeWidth(textStrokeWidth);

		// measure size
		Bounds textBounds = msrText(model.getText(), font, textStrokeWidth);

		Rectangle bounds = new Rectangle(model.getPosition(),
				new Dimension(textBounds.getWidth() + textBounds.getHeight(), textBounds.getHeight() * 1.5));

		// the rounded rectangle
		{
			RoundedRectangle roundRect = new RoundedRectangle(bounds, 10, 10);
			fxRoundedRectNode.setGeometry(roundRect);
			fxRoundedRectNode.setFill(model.getColor());
			fxRoundedRectNode.setStroke(Color.BLACK);
			fxRoundedRectNode.setStrokeWidth(2);
			fxRoundedRectNode.toBack();
		}
		// the text
		{
			text.setTextOrigin(VPos.CENTER);
			text.setY(bounds.getY() + bounds.getHeight() / 2);
			text.setX(bounds.getX() + bounds.getWidth() / 2 - textBounds.getWidth() / 2);
			text.toFront();
		}
	}

	private Bounds msrText(String string, Font font, int textStrokeWidth) {
		Text msrText = new Text(string);
		msrText.setFont(font);
		msrText.setStrokeWidth(textStrokeWidth);

		new Scene(new Group(msrText));
		Bounds textBounds = msrText.getLayoutBounds();
		return textBounds;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getContent()) {
			refreshVisual();
		}
	}

	@Override
	public SetMultimap<? extends Object, String> doGetContentAnchorages() {
		return HashMultimap.create();
	}

	@Override
	public List<? extends Object> doGetContentChildren() {
		return Collections.emptyList();
	}

}
