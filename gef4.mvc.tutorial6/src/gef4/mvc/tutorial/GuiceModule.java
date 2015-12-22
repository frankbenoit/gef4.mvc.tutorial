package gef4.mvc.tutorial;

import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.common.inject.AdapterMaps;
import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultFeedbackPartFactory;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultHandlePartFactory;
import org.eclipse.gef4.mvc.fx.policies.FXFocusAndSelectOnClickPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXHoverOnHoverPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTranslateSelectedOnDragPolicy;
import org.eclipse.gef4.mvc.fx.providers.ShapeOutlineProvider;
import org.eclipse.gef4.mvc.fx.tools.FXTypeTool;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import gef4.mvc.tutorial.parts.ModelPartFactory;
import gef4.mvc.tutorial.parts.TextNodePart;
import gef4.mvc.tutorial.policies.GlobalOnTypePolicy;
import gef4.mvc.tutorial.policies.TextNodeOnDoubleClickPolicy;
import gef4.mvc.tutorial.policies.TextNodeOnTypePolicy;
import gef4.mvc.tutorial.policies.TextNodeTransformPolicy;
import javafx.scene.Node;

public final class GuiceModule extends MvcFxModule {
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
		
		// interaction policies to relocate on drag (including anchored elements, which are linked)
		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
			.to(FXTranslateSelectedOnDragPolicy.class);
		
		
		// edit node label policies
		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
			.to( TextNodeOnDoubleClickPolicy.class);
		
		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
			.to( TextNodeOnTypePolicy.class);

		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
			.to( GlobalOnTypePolicy.class);

	}

	@Override
	protected void bindAbstractRootPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractRootPartAdapters(adapterMapBinder);
		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
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
			.toInstance(new ModelPartFactory());
		
		bindTextNodePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), TextNodePart.class));
		
	}
}