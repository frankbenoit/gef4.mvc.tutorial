package gef4.mvc.tutorial;

import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.common.inject.AdapterMaps;
import org.eclipse.gef4.fx.anchors.IAnchor;
import org.eclipse.gef4.geometry.planar.IGeometry;
import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.parts.ChopBoxAnchorProvider;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultFeedbackPartFactory;
import org.eclipse.gef4.mvc.fx.parts.VisualBoundsGeometryProvider;
import org.eclipse.gef4.mvc.fx.policies.FXFocusAndSelectOnClickPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXHoverOnHoverPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTranslateSelectedOnDragPolicy;
import org.eclipse.gef4.mvc.fx.tools.FXClickDragTool;
import org.eclipse.gef4.mvc.fx.tools.FXHoverTool;
import org.eclipse.gef4.mvc.fx.tools.FXTypeTool;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;

import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import gef4.mvc.tutorial.parts.ContentPartFactory;
import gef4.mvc.tutorial.parts.TextNodePart;
import gef4.mvc.tutorial.parts.TextNodeRelationPart;
import gef4.mvc.tutorial.policies.GlobalOnTypePolicy;
import javafx.scene.Node;

@SuppressWarnings("serial")
public final class GuiceModule extends MvcFxModule {

	@Override
	protected void bindAbstractContentPartAdapters( MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractContentPartAdapters(adapterMapBinder);
		
		adapterMapBinder
			.addBinding(AdapterKey.get(FXClickDragTool.CLICK_TOOL_POLICY_KEY))
			.to(FXFocusAndSelectOnClickPolicy.class);
		
		adapterMapBinder
			.addBinding(AdapterKey.get(FXHoverTool.TOOL_POLICY_KEY))
			.to(FXHoverOnHoverPolicy.class);
		
		// geometry provider for selection feedback
		adapterMapBinder
			.addBinding(AdapterKey.get(
				new TypeToken<Provider<IGeometry>>(){}, 
				FXDefaultFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER))
			.to(VisualBoundsGeometryProvider.class);
		
		// geometry provider for hover feedback
		adapterMapBinder
			.addBinding(AdapterKey.get(
				new TypeToken<Provider<IGeometry>>(){}, 
				FXDefaultFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER))
			.to(VisualBoundsGeometryProvider.class);
	}

	protected void bindTextNodePartAdapters( MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// interaction policies to relocate on drag (including anchored elements, which are linked)
		adapterMapBinder
			.addBinding( AdapterKey.get(FXClickDragTool.DRAG_TOOL_POLICY_KEY))
			.to(FXTranslateSelectedOnDragPolicy.class);
		
		adapterMapBinder
			.addBinding( AdapterKey.get(new TypeToken<Provider<IAnchor>>() { }))
			.to(ChopBoxAnchorProvider.class);

	}

	private void bindTextNodePartRelationAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
	}

	@Override
	protected void bindAbstractRootPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractRootPartAdapters(adapterMapBinder);
		
		adapterMapBinder
			.addBinding( AdapterKey.get( FXTypeTool.TOOL_POLICY_KEY, "GlobalOnTypePolicy"))
			.to( GlobalOnTypePolicy.class);

	}

	@Override
	protected void configure() {
		super.configure();

		binder()
			.bind(GlobalOnTypePolicy.class )
			.in(Scopes.SINGLETON);

		binder()
			.bind(new TypeLiteral<IContentPartFactory<Node>>(){})
			.toInstance(new ContentPartFactory());
		
		bindTextNodePartAdapters(
			AdapterMaps.getAdapterMapBinder(binder(), TextNodePart.class));
		
		bindTextNodePartRelationAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), TextNodeRelationPart.class));
		
	}

}