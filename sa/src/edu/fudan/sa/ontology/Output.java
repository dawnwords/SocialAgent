package edu.fudan.sa.ontology;

import jade.content.Concept;

import java.io.Serializable;

/**
 * @author ming
 * @since 2014-04-16 14:15.
 */
public class Output<T extends Serializable> implements Concept {
	private T value;
	private String description;
	private String strType;
	private Class<T> type;

	public Output() {
	}

	public Output(Class<T> type) {
		this.setType(type);
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
		this.strType = type.getName();
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}
}
