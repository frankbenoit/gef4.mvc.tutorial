package gef4.mvc.tutorial;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.gef4.fx.nodes.InfiniteCanvas;
import org.eclipse.gef4.mvc.fx.domain.FXDomain;
import org.eclipse.gef4.mvc.fx.viewer.FXViewer;
import org.eclipse.gef4.mvc.models.ContentModel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import gef4.mvc.tutorial.model.Model;
import gef4.mvc.tutorial.model.TextNode;
import gef4.mvc.tutorial.parts.TextNodePart;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gef4MvcTutorial extends Application {

	private Model model;
	private JAXBContext jaxbContext;
	private FXDomain domain;

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(final Stage primaryStage) throws Exception {
		
		
		jaxbContext = JAXBContext.newInstance(Model.class, TextNode.class);
		
		Injector injector = Guice.createInjector(createGuiceModule());
		
		domain = injector.getInstance(FXDomain.class);
		
		FXViewer viewer = domain.getAdapter(FXViewer.class);
		
		HBox paneCtrl = new HBox();

		AnchorPane paneDraw = new AnchorPane();
		VBox vbox = new VBox( paneCtrl, paneDraw );
		//vbox.setPrefWidth(Double.MAX_VALUE);
		vbox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		Button btnUndo = new Button("Undo");
		btnUndo.setMaxWidth(Double.MAX_VALUE);
		btnUndo.setDisable( true );
		btnUndo.setOnAction( e -> {
			try {
				IOperationHistory history = domain.getOperationHistory();
				history.undo( domain.getUndoContext(), null, null );
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Button btnRedo = new Button("Redo");
		btnRedo.setMaxWidth(Double.MAX_VALUE);
		btnRedo.setDisable( true );
		btnRedo.setOnAction( e -> {
			try {
				IOperationHistory history = domain.getOperationHistory();
				history.redo( domain.getUndoContext(), null, null );
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		domain.getOperationHistory().addOperationHistoryListener( e -> {
			updateUnReDoButton(btnUndo, "Undo", e.getHistory()::getUndoHistory );
			updateUnReDoButton(btnRedo, "Redo", e.getHistory()::getRedoHistory );
		});
		
		paneCtrl.getChildren().addAll(btnUndo, btnRedo);

		InfiniteCanvas drawingPane = viewer.getCanvas();
		drawingPane.clipContentProperty().set(true);
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
		primaryStage.setTitle("GEF4 MVC Tutorial 8 - Layout and connections");
		
		primaryStage.show();

		domain.activate();

		List<Object> contents = createContents();
		viewer.getAdapter(ContentModel.class).setContents(contents);
		
		TextNodePart p = (TextNodePart) viewer.getContentPartMap().get(model.getRootNode());
		p.layout();
		//TextNodeLayout nodeTop = createLayoutContext(contents);
		
	}
	
//	static class TextNodeLayout implements ISubgraphLayout {
//		private TextNode textNode;
//		private TextNode parent;
//		private List<TextNodeLayout> childs = new ArrayList<>();
//
//		public TextNodeLayout(TextNode textNode){
//			this.textNode = textNode;
//		}
//		public void setParent( TextNode parent ){
//			this.parent = parent;
//		}
//		public void addChild( TextNodeLayout child ){
//			childs.add( child );
//		}
//		@Override
//		public IEntityLayout[] getSuccessingEntities() {
//			return new IEntityLayout[]{};
//		}
//
//		@Override
//		public IEntityLayout[] getPredecessingEntities() {
//			return new IEntityLayout[]{};
//		}
//
//		@Override
//		public Object[] getItems() {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void setProperty(String name, Object value) {
//		}
//
//		@Override
//		public Object getProperty(String name) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void addPropertyChangeListener(PropertyChangeListener listener) {
//		}
//
//		@Override
//		public void removePropertyChangeListener(PropertyChangeListener listener) {
//		}
//
//		@Override
//		public INodeLayout[] getNodes() {
//			return (INodeLayout[]) childs.toArray(new INodeLayout[childs.size()]);
//		}
//
//		@Override
//		public int countNodes() {
//			return childs.size();
//		}
//
//		@Override
//		public void addNodes(INodeLayout[] nodes) {
//			throw new UnsupportedOperationException();
//		}
//
//		@Override
//		public void removeNodes(INodeLayout[] nodes) {
//			throw new UnsupportedOperationException();
//		}
//		
//	}
//	private TextNodeLayout createLayoutContext(List<? extends TextNode> contents) {
//		HashMap<TextNode, TextNodeLayout> layoutNodes = new HashMap<>();
//		TextNode root = null;
//		for( TextNode tn : contents ){
//			TextNodeLayout tnl = new TextNodeLayout(tn);
//			layoutNodes.put( tn, tnl );
//			if( tn.isRootNode() ){
//				root = tn;
//			}
//		}
//		
//		for( TextNode tn : contents ){
//			TextNodeLayout tnl = layoutNodes.get(tn);
//			for( TextNode c : tn.childs ){
//				TextNodeLayout tnc = layoutNodes.get(c);
//				tnl.addChild(tnc);
//			}
//		}
//		return layoutNodes.get(root);
//	}

	private void updateUnReDoButton( Button btn, String label, Function<IUndoContext, IUndoableOperation[]> getHist ){
		IUndoableOperation[] entries = getHist.apply(domain.getUndoContext());
		btn.setDisable( entries.length == 0 );
		if( entries.length == 0 ){
			btn.setText( label );
		}
		else {
			IUndoableOperation currentEntry = entries[entries.length-1];
			btn.setText( String.format("%s: %s", label, currentEntry.getLabel()));
		}
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
	protected List<Object> createContents() {
//		if( Files.isReadable(Paths.get("model.xml"))){
//			try{
//				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//				model = (Model) jaxbUnmarshaller.unmarshal(new File("model.xml"));
//			}
//			catch( Exception e){
//				e.printStackTrace();
//			}
//		}

		if( model == null ){
			model = new Model();

			TextNode ch1   = new TextNode( "Node-1");
			TextNode ch11  = new TextNode( "Node-1-1");
			TextNode ch12  = new TextNode( "Node-1-2");
			TextNode ch13  = new TextNode( "Node-1-3");
			TextNode ch121 = new TextNode( "Node-1-2-1");
			TextNode ch131 = new TextNode( "Node-1-3-1");
			TextNode ch132 = new TextNode( "Node-1-3-2");
			
			model.setRootNode( ch1 );
			
			ch1.addChild(ch11);
			ch1.addChild(ch12);
			ch1.addChild(ch13);
			ch12.addChild(ch121);
			ch13.addChild(ch131);
			ch13.addChild(ch132);
		}

		model.init();
		
		LinkedList<Object> res = new LinkedList<>(model.allNodes);
		model.getAllRelations(res);
		return res;
	}

	protected Module createGuiceModule() {
		return new GuiceModule();
	}
}
