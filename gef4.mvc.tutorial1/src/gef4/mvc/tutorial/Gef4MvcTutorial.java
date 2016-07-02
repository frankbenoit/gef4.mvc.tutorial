package gef4.mvc.tutorial;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.domain.FXDomain;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ContentModel;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

import gef4.mvc.tutorial.model.Model;
import gef4.mvc.tutorial.parts.ModelPartFactory;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gef4MvcTutorial extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(final Stage primaryStage) throws Exception {

		Injector injector = Guice.createInjector(createGuiceModule());

		FXDomain domain = injector.getInstance(FXDomain.class);

		// hook the (single) viewer into the stage
		FXViewer viewer = domain.getAdapter(FXViewer.class);
		primaryStage.setScene(new Scene(viewer.getCanvas()));

		primaryStage.setResizable(true);
		primaryStage.setWidth(640);
		primaryStage.setHeight(480);
		primaryStage.setTitle("GEF4 MVC Tutorial 1 - minimal MVC");
		primaryStage.sizeToScene();
		primaryStage.show();

		// activate domain only after viewers have been hooked
		domain.activate();

		// set viewer contents
		viewer.getAdapter(ContentModel.class).getContents().setAll(createContents());
	}

	protected List<? extends Object> createContents() {
		return Collections.singletonList(new Model());
	}

	protected Module createGuiceModule() {
		return new MvcFxModule() {
			@Override
			protected void configure() {
				super.configure();

				binder().bind(new TypeLiteral<IContentPartFactory<Node>>() {
				}).toInstance(new ModelPartFactory());

			}
		};
	}
}
