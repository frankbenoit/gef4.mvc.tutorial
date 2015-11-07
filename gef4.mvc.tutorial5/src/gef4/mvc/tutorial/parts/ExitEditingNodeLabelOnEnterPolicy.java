package gef4.mvc.tutorial.parts;

import org.eclipse.gef4.mvc.fx.policies.AbstractFXOnTypePolicy;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

//only applicable for NodeContentPart (see #getHost())
public class ExitEditingNodeLabelOnEnterPolicy extends AbstractFXOnTypePolicy {

	@Override
	public TextNodePart getHost() {
		return (TextNodePart) super.getHost();
	}

	@Override
	public void pressed(KeyEvent event) {
		if (KeyCode.ENTER.equals(event.getCode())) {
			getHost().editModeEnd(true);
		}
		if (KeyCode.ESCAPE.equals(event.getCode())) {
			getHost().editModeEnd(false);
		}
	}

	@Override
	public void released(KeyEvent event) {
	}

	@Override
	public void typed(KeyEvent event) {
		
	}

}
