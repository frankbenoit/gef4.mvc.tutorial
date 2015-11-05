package gef4.mvc.tutorial.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.operations.ITransactionalOperation;
import org.eclipse.gef4.mvc.parts.IVisualPart;

import gef4.mvc.tutorial.parts.TextNodePart;
import javafx.scene.Node;
import javafx.scene.transform.Affine;

public class ChangeTextNodePositionOperation extends AbstractOperation implements ITransactionalOperation {

	TextNodePart part;
	public ChangeTextNodePositionOperation(IVisualPart<Node, ? extends Node> part) {
		super( "" );
		Assert.isLegal(part instanceof TextNodePart, "Only TestNodePart supported for ChangeItemPositionOperation");
		this.part = (TextNodePart) part;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		Affine transform = part.getAdapter(FXTransformPolicy.TRANSFORM_PROVIDER_KEY).get();
		// tell the part, which updates the model, will also trigger a doRefreshVisuals
		part.translate(transform.getTx(), transform.getTy()); 
		// reset the transformation
		transform.setTx(0);
		transform.setTy(0);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return null;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return null;
	}

	@Override
	public boolean isNoOp() {
		return false;
	}

}
