package gef4.mvc.tutorial.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef4.mvc.domain.IDomain;
import org.eclipse.gef4.mvc.fx.policies.IFXOnTypePolicy;
import org.eclipse.gef4.mvc.policies.AbstractInteractionPolicy;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class GlobalOnTypePolicy extends AbstractInteractionPolicy<Node>  implements IFXOnTypePolicy {

	private static final KeyCodeCombination ctrlY = new KeyCodeCombination( KeyCode.Y, KeyCombination.CONTROL_DOWN );
	private static final KeyCodeCombination ctrlZ = new KeyCodeCombination( KeyCode.Z, KeyCombination.CONTROL_DOWN );

	@Override
	public void pressed(KeyEvent event) {
		if (ctrlZ.match(event)) {
			try {
				getDomain().getOperationHistory().undo(getDomain().getUndoContext(), null, null);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
		if (ctrlY.match(event)) {
			try {
				getDomain().getOperationHistory().redo(getDomain().getUndoContext(), null, null);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private IDomain<?> getDomain() {
		return getHost().getRoot().getViewer().getDomain();
	}

	@Override
	public void released(KeyEvent event) {
	}

	@Override
	public void typed(KeyEvent event) {
		
	}

	@Override
	public void unfocus() {
		
	}

}
