package gef4.mvc.tutorial.policies;

import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.operations.ForwardUndoCompositeOperation;
import org.eclipse.gef4.mvc.operations.ITransactionalOperation;

public class TextNodeTransformPolicy extends FXTransformPolicy {

	@Override
	public ITransactionalOperation commit() {
	    ITransactionalOperation visualOperation = super.commit();
	    ITransactionalOperation modelOperation = createUpdateModelOperation();
	    ForwardUndoCompositeOperation commit = new ForwardUndoCompositeOperation("Translate()");
	    if (visualOperation != null) commit.add(visualOperation);
	    if (modelOperation != null) commit.add(modelOperation);
	    return commit.unwrap(true);
	}

	private ITransactionalOperation createUpdateModelOperation() {
		return new ChangeTextNodePositionOperation(getHost());
	}
}
