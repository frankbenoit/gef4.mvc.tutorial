/*******************************************************************************
 * Copyright (c) 2014, 2015 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API & implementation
 *
 *******************************************************************************/
package gef4.mvc.tutorial.policies;

import org.eclipse.gef4.mvc.fx.policies.AbstractFXOnClickPolicy;
import org.eclipse.gef4.mvc.parts.IContentPart;
import org.eclipse.gef4.mvc.parts.IVisualPart;
import org.eclipse.gef4.mvc.policies.DeletionPolicy;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DeleteOnClickPolicy extends AbstractFXOnClickPolicy {

	@Override
	public void click(MouseEvent e) {
		System.out.println("DeleteOnClickPolicy.click()");
		IVisualPart<Node, ? extends Node> targetPart = getTargetPart();
		if (targetPart instanceof IContentPart) {
			DeletionPolicy<Node> policy = getHost().getRoot().getAdapter(DeletionPolicy.class);
			if (policy != null) {
				init(policy);
				// un establish anchor relations
				policy.delete((IContentPart<Node, ? extends Node>) targetPart);
				commit(policy);
			}
		}
	}

	/**
	 * Returns the target {@link IVisualPart} for this policy. Per default the
	 * first anchorage is returned.
	 *
	 * @return The target {@link IVisualPart} for this policy.
	 */
	protected IVisualPart<Node, ? extends Node> getTargetPart() {
		return getHost().getAnchorages().keySet().iterator().next();
	}

}
