package gef4.mvc.tutorial.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef4.mvc.operations.ITransactionalOperation;

import gef4.mvc.tutorial.parts.TextNodePart;

public class ChangeTextNodeTextOperation extends AbstractOperation implements ITransactionalOperation {

	TextNodePart part;
	private String oldText;
	private String newText;
	
	public ChangeTextNodeTextOperation(TextNodePart part, String oldText, String newText ) {
		super( "Change Text in TextNode" );
		this.part = part;
		this.oldText = oldText;
		this.newText = newText;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		part.getContent().setText(newText);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		execute(monitor, info);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		part.getContent().setText(oldText);
		return Status.OK_STATUS;
	}

	@Override
	public boolean isNoOp() {
		return oldText.equals(newText);
	}

}
