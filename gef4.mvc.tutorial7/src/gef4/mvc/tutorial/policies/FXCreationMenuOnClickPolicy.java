/*******************************************************************************
 * Copyright (c) 2015 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package gef4.mvc.tutorial.policies;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.gef4.geometry.planar.Point;
import org.eclipse.gef4.mvc.fx.policies.AbstractFXOnClickPolicy;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.parts.IContentPart;
import org.eclipse.gef4.mvc.parts.IRootPart;
import org.eclipse.gef4.mvc.policies.CreationPolicy;
import org.eclipse.gef4.mvc.viewer.IViewer;

import com.google.common.collect.HashMultimap;

import gef4.mvc.tutorial.model.TextNode;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

// TODO: only applicable for FXRootPart and FXViewer
public class FXCreationMenuOnClickPolicy extends AbstractFXOnClickPolicy {

	/**
	 * The adapter role for the
	 * <code>Provider&lt;List&lt;IFXCreationMenuItem&gt;&gt;</code>.
	 */
	public static final String MENU_ITEM_PROVIDER_ROLE = "Provider<List<IFXCreationMenuItem>>";

	private Point2D initialMousePositionInScene;

	private Point2D initialMousePositionInScreen;

	private Popup popup;

	@Override
	public void click(MouseEvent e) {
		// open menu on right click
		if (MouseButton.SECONDARY.equals(e.getButton())) {
			EventTarget target = e.getTarget();
			if (target instanceof Node) {
				Node t = (Node) target;
				initialMousePositionInScreen = new Point2D(e.getScreenX(), e.getScreenY());
//				.screenToLocal()
				initialMousePositionInScene = getViewer().getCanvas().getContentGroup().screenToLocal(initialMousePositionInScreen);
				openMenu(e);
			}
		}
	}

	private FXViewer getViewer() {
		return (FXViewer) getHost().getRoot().getViewer();
	}

	private void openMenu(final MouseEvent me) {
		System.out.println("FXCreationMenuOnClickPolicy.openMenu()");
		
		
		try {
			popup = new Popup(); 
	
			popup.autoFixProperty().set(true);
			popup.autoHideProperty().set(true);
			popup.setX(initialMousePositionInScreen.getX()); 
			popup.setY(initialMousePositionInScreen.getY());
	
			HBox hb = new HBox();
			hb.setStyle("-fx-border-width: 1px; -fx-border-color: DIMGRAY; -fx-background-color: lightgray" );
//			hb.setPrefSize( 100, 50 );
			Button first = new Button();
			first.setOnAction(e-> this.addTextNode() );
			ImageView iv = new ImageView( new Image(new FileInputStream( "images/AddTextNode.png")));
			iv.autosize();
			first.setGraphic(iv);
			hb.getChildren().add( first );
			hb.setEffect( new DropShadow( 4, 4, 2, Color.GRAY ) );
			hb.setSpacing( 4 );
			hb.setPadding( new Insets( 4, 4, 4, 4));
			popup.getContent().addAll( hb );
			popup.show(getViewer().getScene().getWindow());
		
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	private void addTextNode(){
		
		IRootPart<Node, ? extends Node> root = getHost().getRoot();
		IViewer<Node> viewer = root.getViewer();

		TextNode textNode = new TextNode( initialMousePositionInScene.getX(), initialMousePositionInScene.getY(), "A" );
		
		IContentPart<Node, ? extends Node> contentPartModel = getHost().getRoot().getContentPartChildren().get(0);
		
		// build create operation
		CreationPolicy<Node> creationPolicy = root
				.<CreationPolicy<Node>> getAdapter(CreationPolicy.class);
		creationPolicy.init();
		creationPolicy.create(
				textNode, 
				contentPartModel, 
				HashMultimap .<IContentPart<Node, ? extends Node>, String> create());

		// execute on stack
		viewer.getDomain().execute(creationPolicy.commit());

		popup.hide();
	}

}
