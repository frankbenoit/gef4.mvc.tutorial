package gef4.mvc.tutorial;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.common.inject.AdapterMaps;
import org.eclipse.gef4.fx.nodes.InfiniteCanvas;
import org.eclipse.gef4.geometry.planar.IGeometry;
import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.domain.FXDomain;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultFeedbackPartFactory;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultHandlePartFactory;
import org.eclipse.gef4.mvc.fx.providers.GeometricBoundsProvider;
import org.eclipse.gef4.mvc.fx.providers.GeometricOutlineProvider;
import org.eclipse.gef4.mvc.fx.providers.ShapeOutlineProvider;
import org.eclipse.gef4.mvc.fx.policies.FXFocusAndSelectOnClickPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXHoverOnHoverPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTranslateSelectedOnDragPolicy;
import org.eclipse.gef4.mvc.fx.tools.FXClickDragTool;
import org.eclipse.gef4.mvc.fx.tools.FXHoverTool;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ContentModel;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;

import com.google.common.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import gef4.mvc.tutorial.model.Model;
import gef4.mvc.tutorial.model.TextNode;
import gef4.mvc.tutorial.parts.ModelPartFactory;
import gef4.mvc.tutorial.parts.TextNodePart;
import gef4.mvc.tutorial.policies.TextNodeTransformPolicy;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@SuppressWarnings("serial")
public class Gef4MvcTutorial extends Application {

	private Model model;
	private JAXBContext jaxbContext;

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(final Stage primaryStage) throws Exception {
		jaxbContext = JAXBContext.newInstance(Model.class, TextNode.class);
		
		Injector injector = Guice.createInjector(createGuiceModule());
		
		FXDomain domain = injector.getInstance(FXDomain.class);

		FXViewer viewer = domain.getAdapter(FXViewer.class);
		
		AnchorPane paneCtrl = new AnchorPane();
		AnchorPane paneDraw = new AnchorPane();
		VBox vbox = new VBox( paneCtrl, paneDraw );
		vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		Button btnUpdateModel = new Button("update model");
		btnUpdateModel.setOnAction( e -> model.doChanges() );
		btnUpdateModel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		paneCtrl.getChildren().add(btnUpdateModel);
		AnchorPane.setTopAnchor(btnUpdateModel, 10d);
		AnchorPane.setLeftAnchor(btnUpdateModel, 10d);
		AnchorPane.setRightAnchor(btnUpdateModel, 10d);

		InfiniteCanvas drawingPane = viewer.getCanvas();
		paneDraw.getChildren().add(drawingPane);
		paneDraw.setPrefHeight(2000);
		AnchorPane.setTopAnchor(drawingPane, 10d);
		AnchorPane.setLeftAnchor(drawingPane, 10d);
		AnchorPane.setRightAnchor(drawingPane, 10d);
		AnchorPane.setBottomAnchor(drawingPane, 10d);
		
		primaryStage.setScene(new Scene(vbox));

		primaryStage.setResizable(true);
		primaryStage.setWidth(640);
		primaryStage.setHeight(480);
		primaryStage.setTitle("GEF4 MVC Tutorial 4 - Drag and persist");

		primaryStage.show();

		domain.activate();

		viewer.getAdapter(ContentModel.class).setContents(createContents());
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		try{
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal( model, new File("model.xml"));
		}
		catch( Exception e){
			e.printStackTrace();
		}
	}
	protected List<? extends Object> createContents() {
		if( Files.isReadable(Paths.get("model.xml"))){
			try{
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				model = (Model) jaxbUnmarshaller.unmarshal(new File("model.xml"));
			}
			catch( Exception e){
				e.printStackTrace();
			}
		}

		if( model == null ){
			model = new Model();
			model.addNode( new TextNode( 20, 20, "First"));
			model.addNode( new TextNode( 20, 120, "Second"));

		}
		
		return Collections.singletonList(model);
	}

	protected Module createGuiceModule() {
		return new MvcFxModule(){
			
			@Override
			protected void bindAbstractContentPartAdapters( MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
				super.bindAbstractContentPartAdapters(adapterMapBinder);
				// register (default) interaction policies (which are based on viewer
				// models and do not depend on transaction policies)
				
				adapterMapBinder
						.addBinding(AdapterKey.defaultRole())
						.to(FXFocusAndSelectOnClickPolicy.class);
				
				adapterMapBinder
						.addBinding(AdapterKey.defaultRole())
						.to(FXHoverOnHoverPolicy.class);
				
				// geometry provider for selection feedback
				adapterMapBinder
						.addBinding(AdapterKey.defaultRole())
						.to(GeometricBoundsProvider.class);
				
				// geometry provider for hover feedback
				adapterMapBinder
					.addBinding(AdapterKey.role("4"))
					.to(GeometricBoundsProvider.class);
			}
			
			protected void bindTextNodePartAdapters( MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
				adapterMapBinder
					.addBinding( AdapterKey.role(
						FXDefaultFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER))
					.to(ShapeOutlineProvider.class);
				
				// geometry provider for selection handles
				adapterMapBinder 
					.addBinding(AdapterKey.role(
						FXDefaultHandlePartFactory.SELECTION_HANDLES_GEOMETRY_PROVIDER))
					.to(ShapeOutlineProvider.class);
				
				adapterMapBinder
					.addBinding(AdapterKey.role(
						FXDefaultFeedbackPartFactory.SELECTION_LINK_FEEDBACK_GEOMETRY_PROVIDER))
					.to(ShapeOutlineProvider.class);
				
				// geometry provider for hover feedback
				adapterMapBinder
					.addBinding(AdapterKey.role(
						FXDefaultFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
					.to(ShapeOutlineProvider.class);

				// register resize/transform policies (writing changes also to model)
				adapterMapBinder
					.addBinding(AdapterKey.defaultRole())
					.to(TextNodeTransformPolicy.class);
					//.to(FXTransformPolicy.class);
				
				// interaction policies to relocate on drag (including anchored elements, which are linked)
				adapterMapBinder
					.addBinding(AdapterKey.defaultRole())
					.to(FXTranslateSelectedOnDragPolicy.class);
			}

			@Override
			protected void configure() {
				super.configure();

				binder()
					.bind(new TypeLiteral<IContentPartFactory<Node>>(){})
					.toInstance(new ModelPartFactory());
				
				bindTextNodePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), TextNodePart.class));
				
			}
		};
	}
}
