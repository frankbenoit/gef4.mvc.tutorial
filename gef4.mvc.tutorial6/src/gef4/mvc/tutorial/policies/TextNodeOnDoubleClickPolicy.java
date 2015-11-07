package gef4.mvc.tutorial.policies;

import org.eclipse.gef4.mvc.fx.policies.AbstractFXOnClickPolicy;

import gef4.mvc.tutorial.parts.TextNodePart;
import javafx.scene.input.MouseEvent;

// only applicable for NodeContentPart (see #getHost())
public class TextNodeOnDoubleClickPolicy extends AbstractFXOnClickPolicy {

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
