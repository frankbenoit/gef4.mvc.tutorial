package gef4.mvc.tutorial.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.fx.nodes.GeometryNode;
import org.eclipse.gef4.geometry.planar.Point;
import org.eclipse.gef4.geometry.planar.Rectangle;
import org.eclipse.gef4.geometry.planar.RoundedRectangle;
import org.eclipse.gef4.mvc.fx.parts.AbstractFXContentPart;
import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import gef4.mvc.tutorial.model.TextNode;
import javafx.geometry.Bounds;
import javafx.scene.Group;
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
	private static final int TEXT_STROKE_WIDTH = 2;
	private static final double LAYOUT_VSPACE = 20;
	private static final double LAYOUT_HSPACE = 50;
	private Font font;
	
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
		
		font = Font.font("Monospace", FontWeight.BOLD, 50 );
		Color textColor = Color.BLACK;
		
		text.setText( model.getText() );
		text.setFont( font );
		text.setFill(textColor);
		text.setStrokeWidth(TEXT_STROKE_WIDTH);

		// measure size
		Bounds textBounds = msrText(model.getText(), font, TEXT_STROKE_WIDTH );

		Rectangle bounds = new Rectangle( 
				0, 0, 
				textBounds.getWidth() + textBounds.getHeight(), textBounds.getHeight() * 1.5 );

		// the rounded rectangle
		RoundedRectangle roundRect = new RoundedRectangle( bounds, 10, 10 );
		fxRoundedRectNode.setGeometry(roundRect);
		fxRoundedRectNode.setFill( COLOR );
		fxRoundedRectNode.setStroke( Color.BLACK );
		fxRoundedRectNode.setStrokeWidth(2);
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
	public void setContent(Object content) {
		System.out.println("setContent "+content);
		super.setContent(content);
	}
	@Override
	public SetMultimap<? extends Object, String> getContentAnchorages() {
		return HashMultimap.create();
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
		
		
		Bounds textBounds = msrText(getContent().getText(), font, TEXT_STROKE_WIDTH );

		layoutBounds.setHeight( textBounds.getHeight()*1.5 );
		layoutBounds.setWidth( textBounds.getWidth() + textBounds.getHeight() );
		
		if( !getContent().childs.isEmpty() ){
			layoutBounds.setHeight( Math.max( layoutBounds.getHeight(), childsHeight ));
			layoutBounds.setWidth( layoutBounds.getWidth() + LAYOUT_HSPACE + childsWidth );
		}
		
	}
	private void layoutPosition( Point p ){

		Bounds textBounds = msrText(getContent().getText(), font, TEXT_STROKE_WIDTH );
		double rectHeight = textBounds.getHeight()*1.5;
		double rectWidth = textBounds.getWidth() + textBounds.getHeight();
		
		layoutBounds.setX(p.x);
		layoutBounds.setY(p.y);
		layoutVisualPosition.x = p.x;
		layoutVisualPosition.y = p.y + layoutBounds.getHeight() / 2  - rectHeight/2;
		
		double x = p.x + rectWidth + LAYOUT_HSPACE;
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


}
