package gef4.mvc.tutorial;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.fx.nodes.ScrollPaneEx;
import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.domain.FXDomain;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ContentModel;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;
import org.eclipse.gef4.mvc.viewer.IViewer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

import gef4.mvc.tutorial.model.Model;
import gef4.mvc.tutorial.parts.ModelPartFactory;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gef4MvcTutorial extends Application {

	private Model model;

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(final Stage primaryStage) throws Exception {
		
		Injector injector = Guice.createInjector(createGuiceModule());
		
		FXDomain domain = injector.getInstance(FXDomain.class);

		FXViewer viewer = domain.getAdapter(IViewer.class);
		
		AnchorPane paneCtrl = new AnchorPane();
		AnchorPane paneDraw = new AnchorPane();
		VBox vbox = new VBox( paneCtrl, paneDraw );

		Button btnUpdateModel = new Button("update model");
		btnUpdateModel.setOnAction( e -> model.doChanges() );
		btnUpdateModel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		paneCtrl.getChildren().add(btnUpdateModel);
		AnchorPane.setTopAnchor(btnUpdateModel, 10d);
		AnchorPane.setLeftAnchor(btnUpdateModel, 10d);
		AnchorPane.setRightAnchor(btnUpdateModel, 10d);

		ScrollPaneEx drawingPane = viewer.getScrollPane();
		paneDraw.getChildren().add(drawingPane);
		paneDraw.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		AnchorPane.setTopAnchor(drawingPane, 10d);
		AnchorPane.setLeftAnchor(drawingPane, 10d);
		AnchorPane.setRightAnchor(drawingPane, 10d);
		AnchorPane.setBottomAnchor(drawingPane, 10d);
		
		primaryStage.setScene(new Scene(vbox));

		primaryStage.setResizable(true);
		primaryStage.setWidth(640);
		primaryStage.setHeight(480);
		primaryStage.setTitle("GEF4 MVC Tutorial 3");
		primaryStage.show();

		domain.activate();

		viewer.getAdapter(ContentModel.class).setContents(createContents());
	}

	protected List<? extends Object> createContents() {
		model = new Model();
		return Collections.singletonList(model);
	}

	protected Module createGuiceModule() {
		return new MvcFxModule(){
			@Override
			protected void configure() {
				super.configure();

				binder()
					.bind(new TypeLiteral<IContentPartFactory<Node>>(){})
					.toInstance(new ModelPartFactory());
				
			}
		};
	}
}
