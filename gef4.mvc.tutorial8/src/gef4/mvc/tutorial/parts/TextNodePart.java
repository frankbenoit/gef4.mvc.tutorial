package gef4.mvc.tutorial.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.fx.nodes.GeometryNode;
import org.eclipse.gef4.geometry.planar.Dimension;
import org.eclipse.gef4.geometry.planar.Point;
import org.eclipse.gef4.geometry.planar.Rectangle;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import gef4.mvc.tutorial.model.TextNode;
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

	private Point     layoutVisualPosition = new Point();
	private Rectangle layoutBounds = new Rectangle();
	
	private Text text;
	private GeometryNode<RoundedRectangle> fxRoundedRectNode;
	private TextField editText;
	private static final Color COLOR = Color.LIGHTSKYBLUE;
	private static final int TEXT_STROKE_WIDTH = 1;
	private static final double LAYOUT_VSPACE = 10;
	private static final double LAYOUT_HSPACE = 25;
	private Font font = Font.font("Monospace", FontWeight.BOLD, 25 );
	
	@Override
	protected void doActivate() {
		super.doActivate();
		getContent().addPropertyChangeListener(this);
	}

	@Override
	protected void doDeactivate() {
		getContent().removePropertyChangeListener(this);
		super.doDeactivate();
	}

	
	@Override
	public TextNode getContent() {
		return (TextNode)super.getContent();
	}

	@Override
	protected StackPane createVisual() {
		
		StackPane stack = new StackPane();
		text = new Text();
		fxRoundedRectNode = new GeometryNode<>();
		editText = new TextField();
		
		editText.setManaged(false);
		editText.setVisible(false);
		
		stack.getChildren().add(fxRoundedRectNode);
		stack.getChildren().add(text);
		stack.getChildren().add(editText);
		
		return stack;
	}

	@Override
	protected void doRefreshVisual(StackPane visual) {
		TextNode model = getContent();
		
		Color textColor = Color.BLACK;
		
		text.setText( model.getText() );
		text.setFont( font );
		text.setFill(textColor);
		text.setStrokeWidth(TEXT_STROKE_WIDTH);

		// measure size
		Dimension size = msrVisual();
		Rectangle bounds = new Rectangle( 0, 0, size.width, size.height );

		// the rounded rectangle
		RoundedRectangle roundRect = new RoundedRectangle( bounds, 4, 4 );
		fxRoundedRectNode.setGeometry(roundRect);
		fxRoundedRectNode.setFill( COLOR );
		fxRoundedRectNode.setStroke( Color.BLACK );
		fxRoundedRectNode.setStrokeWidth(1.2);
		fxRoundedRectNode.toBack();

		text.toFront();
		
		editText.toFront();
		editText.setPrefWidth(bounds.getWidth());
		
		
//		getRoot().getViewer().getContentPartMap().get(
		
				
		{
//			Point position = model.getPosition();
			Affine affine = getAdapter(FXTransformPolicy.TRANSFORM_PROVIDER_KEY).get();
			affine.setTx(layoutVisualPosition.x);
			affine.setTy(layoutVisualPosition.y);
		}

		
		
	}
	
	private Dimension msrVisual() {
		Bounds textBounds = msrText(getContent().getText(), font, TEXT_STROKE_WIDTH );
		return new Dimension(textBounds.getWidth() + textBounds.getHeight(), textBounds.getHeight() * 1.5);
	}

	private Bounds msrText(String string, Font font, int textStrokeWidth) {
		Text msrText = new Text(string);
		msrText.setFont( font );
		msrText.setStrokeWidth(textStrokeWidth);

		new Scene(new Group(msrText));
		Bounds textBounds = msrText.getLayoutBounds();
		return textBounds;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getSource() == getContent() ){
			refreshVisual();
		}
	}
	
	@Override
	public SetMultimap<TextNode, String> getContentAnchorages() {
		HashMultimap<TextNode, String> res = HashMultimap.create();
		res.put(getContent(), "START");
		res.put(getContent(), "END");
		return res;
	}

	@Override
	public List<? extends Object> getContentChildren() {
		return Collections.emptyList();
	}

	private void layoutExtends(){
		double childsHeight = 0;
		double childsWidth = 0;
		for( TextNode tn : getContent().childs ){
			TextNodePart part = (TextNodePart) getViewer().getContentPartMap().get(tn);
			part.layoutExtends();
			childsHeight += part.layoutBounds.getHeight();
			childsWidth = Math.max(childsWidth, part.layoutBounds.getWidth());
		}
		if( !getContent().childs.isEmpty() ){
			childsHeight += (getContent().childs.size()-1) * LAYOUT_VSPACE;
		}
		
		
		Dimension size = msrVisual();

		layoutBounds.setHeight( size.height );
		layoutBounds.setWidth( size.width );
		
		if( !getContent().childs.isEmpty() ){
			layoutBounds.setHeight( Math.max( layoutBounds.getHeight(), childsHeight ));
			layoutBounds.setWidth( layoutBounds.getWidth() + LAYOUT_HSPACE + childsWidth );
		}
		
	}
	private void layoutPosition( Point p ){

		Dimension size = msrVisual();
		
		layoutBounds.setX(p.x);
		layoutBounds.setY(p.y);
		layoutVisualPosition.x = p.x;
		layoutVisualPosition.y = p.y + layoutBounds.getHeight() / 2  - size.height/2;
		
		double x = p.x + size.width + LAYOUT_HSPACE;
		double y = p.y;
		for( TextNode tn : getContent().childs ){
			TextNodePart part = (TextNodePart) getViewer().getContentPartMap().get(tn);
			
			part.layoutPosition( new Point( x, y ) );
			y = part.layoutBounds.getY() + part.layoutBounds.getHeight() + LAYOUT_VSPACE;
		}
	}
	
	public void layout() {
		layoutExtends();
		layoutPosition( new Point( 10, 10 ));
		layoutRefreshUi();
	}

	private void layoutRefreshUi() {
		refreshVisual();
		for( TextNode tn : getContent().childs ){
			TextNodePart part = (TextNodePart) getViewer().getContentPartMap().get(tn);
			part.layoutRefreshUi();
		}		
	}

	@Override
	protected void attachToAnchorageVisual(IVisualPart<Node, ? extends Node> anchorage, String role) {
	}

}
