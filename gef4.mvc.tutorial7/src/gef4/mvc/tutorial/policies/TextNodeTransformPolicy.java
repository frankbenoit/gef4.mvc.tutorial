package gef4.mvc.tutorial.policies;

import org.eclipse.gef4.geometry.planar.Point;
import org.eclipse.gef4.mvc.fx.policies.FXTransformPolicy;
import org.eclipse.gef4.mvc.operations.ForwardUndoCompositeOperation;
import org.eclipse.gef4.mvc.operations.ITransactionalOperation;

import gef4.mvc.tutorial.parts.TextNodePart;
import javafx.scene.transform.Affine;

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
		TextNodePart part = (TextNodePart) getHost();
		Affine transform = part.getAdapter(FXTransformPolicy.TRANSFORM_PROVIDER_KEY).get();
		Point newPos = new Point(transform.getTx(), transform.getTy());
		Point oldPos = part.getContent().getPosition();
		return new ChangeTextNodePositionOperation((TextNodePart)getHost(), oldPos, newPos);
	}
}
