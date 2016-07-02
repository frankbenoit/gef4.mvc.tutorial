package gef4.mvc.tutorial.policies;


import org.eclipse.gef4.mvc.fx.policies.IFXOnClickPolicy;
import org.eclipse.gef4.mvc.policies.AbstractInteractionPolicy;

import gef4.mvc.tutorial.parts.TextNodePart;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

// only applicable for NodeContentPart (see #getHost())
public class TextNodeOnDoubleClickPolicy extends AbstractInteractionPolicy<Node> implements IFXOnClickPolicy  {

	@Override
	public void click(MouseEvent e) {
		if (e.getClickCount() == 2 && e.isPrimaryButtonDown()) {
			getHost().editModeStart();
		}
	}

	@Override
	public TextNodePart getHost() {
		return (TextNodePart) super.getHost();
	}

}
