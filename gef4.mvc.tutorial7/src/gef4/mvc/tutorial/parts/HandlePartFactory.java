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

import org.eclipse.gef4.mvc.behaviors.IBehavior;
import org.eclipse.gef4.mvc.fx.parts.FXDefaultHoverHandlePartFactory;
import org.eclipse.gef4.mvc.parts.IHandlePart;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javafx.scene.Node;

public class HandlePartFactory extends FXDefaultHoverHandlePartFactory {

	@Inject
	private Injector injector;

	@Override
	public List<IHandlePart<Node, ? extends Node>> createHandleParts(
			List<? extends IVisualPart<Node, ? extends Node>> targets, IBehavior<Node> contextBehavior,
			Map<Object, Object> contextMap) {

		List<IHandlePart<Node, ? extends Node>> handles = new ArrayList<IHandlePart<Node, ? extends Node>>();

		final IVisualPart<Node, ? extends Node> target = targets.get(0);
		if (target instanceof TextNodePart) {
			// create root handle part
			HoverHandleRootPart parentHp = new HoverHandleRootPart();
			injector.injectMembers(parentHp);
			handles.add(parentHp);

			DeleteHoverHandlePart deleteHp = new DeleteHoverHandlePart();
			injector.injectMembers(deleteHp);
			parentHp.addChild(deleteHp);

			return handles;
		}
		return super.createHandleParts(targets, contextBehavior, contextMap);
	}

}
