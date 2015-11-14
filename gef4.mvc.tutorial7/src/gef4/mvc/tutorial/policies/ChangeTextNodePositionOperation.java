package gef4.mvc.tutorial.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef4.geometry.planar.Point;
import org.eclipse.gef4.mvc.operations.ITransactionalOperation;

import gef4.mvc.tutorial.parts.TextNodePart;

public class ChangeTextNodePositionOperation extends AbstractOperation implements ITransactionalOperation {

	private TextNodePart part;
	private Point oldPos;
	private Point newPos;
	public ChangeTextNodePositionOperation(TextNodePart part, Point oldPos, Point newPos) {
		super( "Change TextNode Position" );
		this.oldPos = oldPos;
		this.newPos = newPos;
		this.part = part;
	}


	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		part.setPosition(newPos); 
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute( monitor, info );
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		part.setPosition(oldPos); 
		return Status.OK_STATUS;
	}

	@Override
	public boolean isNoOp() {
		return newPos.equals(oldPos);
	}

}
