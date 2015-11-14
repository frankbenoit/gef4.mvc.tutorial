/*******************************************************************************
 * Copyright (c) 2014, 2015 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package gef4.mvc.tutorial.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef4.mvc.behaviors.HoverBehavior;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultHandlePartFactory;
import org.eclipse.gef4.mvc.parts.IHandlePart;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javafx.scene.Node;

public class HandlePartFactory extends FXDefaultHandlePartFactory {

	@Inject
	private Injector injector;

//	@Override
//	public IHandlePart<Node, ? extends Node> createCurveSelectionHandlePart(
//			final IVisualPart<Node, ? extends Node> targetPart,
//			final Provider<BezierCurve[]> segmentsProvider,
//			int segmentCount,
//			int segmentIndex,
//			double segmentParameter) {
//		final FXCircleSegmentHandlePart part = (FXCircleSegmentHandlePart) super.createCurveSelectionHandlePart(
//				targetPart, segmentsProvider, segmentCount, segmentIndex,
//				segmentParameter);
//		injector.injectMembers(part);
//
//		if (segmentIndex + segmentParameter > 0
//				&& segmentIndex + segmentParameter < segmentCount) {
//			// make way points (middle segment vertices) draggable
//			// TODO: binding the following policy requires dynamic binding
//			part.setAdapter(AdapterKey.get(AbstractFXOnDragPolicy.class),
//					new FXBendOnSegmentHandleDragPolicy());
//		} else {
//			// make end points reconnectable
//			// TODO: binding the following policy requires dynamic binding
//			part.setAdapter(AdapterKey.get(AbstractFXOnDragPolicy.class),
//					new FXBendOnSegmentHandleDragPolicy());
//		}
//
//		return part;
//	}

	@Override
	protected List<IHandlePart<Node, ? extends Node>> createHoverHandleParts(
			IVisualPart<Node, ? extends Node> target,
			HoverBehavior<Node> contextBehavior,
			Map<Object, Object> contextMap) {
		
		List<IHandlePart<Node, ? extends Node>> handles = new ArrayList<IHandlePart<Node, ? extends Node>>();
		
		if (target instanceof TextNodePart) {
			// create root handle part
			FXHoverHandleRootPart parentHp = new FXHoverHandleRootPart();
			injector.injectMembers(parentHp);
			handles.add(parentHp);

			FXDeleteHoverHandlePart deleteHp = new FXDeleteHoverHandlePart();
			injector.injectMembers(deleteHp);
			parentHp.addChild(deleteHp);

			return handles;
		}
		return super.createHoverHandleParts(target, contextBehavior,
				contextMap);
	}

}
