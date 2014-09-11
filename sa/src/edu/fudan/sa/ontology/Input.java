package edu.fudan.sa.ontology;

import jade.content.Concept;

import java.io.Serializable;

/**
 * @author ming
 * @since 2014-04-16 14:15.
 */
public class Input<T extends Serializable> implements Concept {
	private String description;
	private Class<T> type;
	private T value;
	private String strType;

	public Input() {
	}

	public Input(Class<T> type) {
		this.setType(type);
	}

	public Input(Class<T> type, T value) {
		this.value = value;
		this.setType(type);
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
		this.strType = this.type.getName();
	}
}
