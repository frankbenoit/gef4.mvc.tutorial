package gef4.mvc.tutorial.parts;

import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.operations.ForwardUndoCompositeOperation;
import org.eclipse.gef4.mvc.operations.ITransactionalOperation;

import gef4.mvc.tutorial.ChangeTextNodePositionOperation;

public class ItemTransformPolicy extends FXTransformPolicy {
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
