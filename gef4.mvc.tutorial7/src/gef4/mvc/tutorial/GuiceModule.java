package gef4.mvc.tutorial;

import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.common.inject.AdapterMaps;
import org.eclipse.gef4.mvc.fx.MvcFxModule;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultFeedbackPartFactory;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultHandlePartFactory;
import org.eclipse.gef4.mvc.fx.policies.FXDeleteSelectedOnTypePolicy;
import org.eclipse.gef4.mvc.fx.policies.FXFocusAndSelectOnClickPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXHoverOnHoverPolicy;
import org.eclipse.gef4.mvc.fx.policies.FXTranslateSelectedOnDragPolicy;
import org.eclipse.gef4.mvc.fx.providers.ShapeOutlineProvider;
import org.eclipse.gef4.mvc.parts.IContentPartFactory;
import org.eclipse.gef4.mvc.parts.IHandlePartFactory;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import gef4.mvc.tutorial.parts.ContentPartFactory;
import gef4.mvc.tutorial.parts.DeleteHoverHandlePart;
import gef4.mvc.tutorial.parts.HandlePartFactory;
import gef4.mvc.tutorial.parts.TextNodePart;
import gef4.mvc.tutorial.policies.CreationMenuOnClickPolicy;
import gef4.mvc.tutorial.policies.DeleteOnClickPolicy;
import gef4.mvc.tutorial.policies.GlobalOnTypePolicy;
import gef4.mvc.tutorial.policies.TextNodeOnDoubleClickPolicy;
import gef4.mvc.tutorial.policies.TextNodeOnTypePolicy;
import gef4.mvc.tutorial.policies.TextNodeTransformPolicy;
import javafx.scene.Node;

@SuppressWarnings("serial")
public final class GuiceModule extends MvcFxModule {

	@Override
	protected void bindAbstractContentPartAdapters( MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractContentPartAdapters(adapterMapBinder);
		
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

		// interaction policy to delete on key type
		adapterMapBinder.addBinding(AdapterKey.defaultRole())
			.to(FXDeleteSelectedOnTypePolicy.class);
	}

	@Override
	protected void bindAbstractRootPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractRootPartAdapters(adapterMapBinder);
		
		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
			.to( GlobalOnTypePolicy.class);

		adapterMapBinder
			.addBinding(AdapterKey.defaultRole())
			.to(CreationMenuOnClickPolicy.class);
	}

	@Override
	protected void bindIHandlePartFactory() {
		binder()
			.bind(new TypeLiteral<IHandlePartFactory<Node>>(){})
			.toInstance(new HandlePartFactory());
	}

	protected void bindDeleteHoverHandlePartAdapters( MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder
				.addBinding(AdapterKey.role("0"))
				.to(DeleteOnClickPolicy.class);
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
		
		bindDeleteHoverHandlePartAdapters(
			AdapterMaps.getAdapterMapBinder(binder(), DeleteHoverHandlePart.class));

	}
}