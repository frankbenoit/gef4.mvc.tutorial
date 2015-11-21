package gef4.mvc.tutorial.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.gef4.common.properties.IPropertyChangeNotifier;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TextNode implements IPropertyChangeNotifier {

	public final String POSITION_PROPERTY = "position";
	public final String TEXT_PROPERTY = "text";

	@XmlAttribute
	@XmlID
	String id = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

	@XmlElement
	private String text = "";

	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
//	private Color color = Color.LIGHTSKYBLUE;

	@XmlElementWrapper(name="Childs")
	@XmlElement(name="TextNode")
	@XmlIDREF
	public List<TextNode> childs = new LinkedList<>();

	private Model model;
	public int x;
	public int y;

	public TextNode(){
	}
	public TextNode( int x, int y, String text){
		this.x = x;
		this.y = y;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void setText(String text) {
		String textOld = this.text;
		this.text = text;
		pcs.firePropertyChange(TEXT_PROPERTY, textOld, text);
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void addChild(TextNode child) {
		childs.add(child);
		model.ensureRegistered(child);
	}

	void init(){
		
	}
	
	public boolean isRootNode(){
		return model.getRootNode() == this;
	}
	
}
