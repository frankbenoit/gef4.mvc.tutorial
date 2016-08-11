package gef4.mvc.tutorial.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.geometry.planar.RoundedRectangle;
import org.eclipse.gef.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef.mvc.models.FocusModel;
import org.eclipse.gef.mvc.parts.IContentPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;

import gef4.mvc.tutorial.model.TextNode;
import gef4.mvc.tutorial.policies.ChangeTextNodeTextOperation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;

public class TextNodePart extends AbstractFXContentPart<StackPane> implements PropertyChangeListener {

	private Text text;
	private GeometryNode<RoundedRectangle> fxRoundedRectNode;

	private boolean isEditing = false;
	private TextField editText;

	private final ChangeListener<Object> objectObserver = new ChangeListener<Object>() {
		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {

			refreshVisual();
		}
	};

	private class FocusListener implements ChangeListener<IContentPart<Node, ? extends Node>> {

		private final TextNodePart nodePart;

		FocusListener(TextNodePart nodePart) {
			this.nodePart = nodePart;
		}

		@Override
		public void changed(ObservableValue<? extends IContentPart<Node, ? extends Node>> observable,
				IContentPart<Node, ? extends Node> oldValue, IContentPart<Node, ? extends Node> newValue) {

			if (nodePart != newValue) {

				editModeEnd(false);

			}
		}
	}

	private ChangeListener<IContentPart<Node, ? extends Node>> focusObserver = new FocusListener(this);

	@SuppressWarnings("serial")
	@Override
	protected void doActivate() {
		super.doActivate();
		getContent().addPropertyChangeListener(objectObserver);

		FocusModel<Node> focusModel = getRoot().getViewer().getAdapter(new TypeToken<FocusModel<Node>>() {
		});

		focusModel.focusProperty().addListener(focusObserver);

	}

	@SuppressWarnings("serial")
	@Override
	protected void doDeactivate() {
		getContent().removePropertyChangeListener(objectObserver);

		FocusModel<Node> focusModel = getRoot().getViewer().getAdapter(new TypeToken<FocusModel<Node>>() {
		});
		focusModel.focusProperty().removeListener(focusObserver);

		super.doDeactivate();
	}

	@Override
	public TextNode getContent() {
		return (TextNode) super.getContent();
	}

	@Override
	protected StackPane createVisual() {
		StackPane group = new StackPane();
		text = new Text();
		fxRoundedRectNode = new GeometryNode<>();
		editText = new TextField();

		editText.setManaged(false);
		editText.setVisible(false);

		group.getChildren().add(fxRoundedRectNode);
		group.getChildren().add(text);
		group.getChildren().add(editText);

		return group;
	}

	@Override
	protected void doRefreshVisual(StackPane visual) {
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

		Rectangle bounds = new Rectangle(0, 0, textBounds.getWidth() + textBounds.getHeight(),
				textBounds.getHeight() * 1.5);

		// the rounded rectangle
		RoundedRectangle roundRect = new RoundedRectangle(bounds, 10, 10);
		fxRoundedRectNode.setGeometry(roundRect);
		fxRoundedRectNode.setFill(model.getColor());
		fxRoundedRectNode.setStroke(Color.BLACK);
		fxRoundedRectNode.setStrokeWidth(2);
		fxRoundedRectNode.toBack();

		text.toFront();

		editText.toFront();
		editText.setPrefWidth(bounds.getWidth());

		{
			Point position = model.getPosition();
			Affine affine = getAdapter(FXTransformPolicy.TRANSFORM_PROVIDER_KEY).get();
			affine.setTx(position.x);
			affine.setTy(position.y);
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

	public void setPosition(Point newPos) {
		getContent().setPosition(newPos);
	}

	public void editModeStart() {
		if (isEditing) {
			return;
		}

		isEditing = true;
		setVisualsForEditing();

		editText.setText(text.getText());
		editText.requestFocus();
		refreshVisual();
	}

	public void editModeEnd(boolean commit) {
		if (!isEditing) {
			return;
		}
		if (commit) {
			String newText = editText.getText();
			text.setText(newText);
			ChangeTextNodeTextOperation op = new ChangeTextNodeTextOperation(this, getContent().getText(), newText);

			try {
				getViewer().getDomain().execute(op, null);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		isEditing = false;
		setVisualsForEditing();
	}

	private void setVisualsForEditing() {
		editText.setManaged(isEditing);
		editText.setVisible(isEditing);
		text.setManaged(!isEditing);
		text.setVisible(!isEditing);

	}

	public boolean isEditing() {
		return isEditing;
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
