package gef4.mvc.tutorial.model;

public class TextNodeRelation {
	private TextNode parent;
	private TextNode child;
	
	
	public TextNodeRelation(){
	}
	
	public void setParent(TextNode parent) {
		this.parent = parent;
	}
	
	public TextNode getParent() {
		return parent;
	}
	
	public void setChild(TextNode child) {
		this.child = child;
	}
	public TextNode getChild() {
		return child;
	}
	
}
