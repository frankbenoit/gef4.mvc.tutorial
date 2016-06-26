package gef4.mvc.tutorial.policies;

import org.eclipse.gef4.mvc.fx.policies.IFXOnTypePolicy;
import org.eclipse.gef4.mvc.policies.AbstractInteractionPolicy;

import gef4.mvc.tutorial.parts.TextNodePart;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

//only applicable for NodeContentPart (see #getHost())
public class TextNodeOnTypePolicy extends AbstractInteractionPolicy<Node>  implements IFXOnTypePolicy {

	@Override
	public TextNodePart getHost() {
		return (TextNodePart) super.getHost();
	}

	@Override
	public void pressed(KeyEvent event) {
		if (KeyCode.F2.equals(event.getCode()) && !getHost().isEditing()) {
			System.out.println("ExitEditingNodeLabelOnEnterPolicy.pressed() 1");
			getHost().editModeStart();
		}
		else if (KeyCode.ENTER.equals(event.getCode())) {
			if( getHost().isEditing() ){
				System.out.println("ExitEditingNodeLabelOnEnterPolicy.pressed() 2");
				event.consume();
				getHost().editModeEnd(true);
			}
			else {
				System.out.println("ExitEditingNodeLabelOnEnterPolicy.pressed() 3");
				event.consume();
				getHost().editModeStart();
			}
		}
		else if (KeyCode.ESCAPE.equals(event.getCode())) {
			event.consume();
			getHost().editModeEnd(false);
		}
	}

	@Override
	public void released(KeyEvent event) {
	}

	@Override
	public void typed(KeyEvent event) {
		
	}

	@Override
	public void unfocus() {
		// TODO Auto-generated method stub
		
	}

}
