package edu.fudan.sa.ontology;

import edu.fudan.sa.IService;
import jade.content.Concept;

/**
 * @author ming
 * @since 2014-05-05 22:34.
 */
public class Service<T extends IService> implements Concept {

	private String name = "no name";
	private String description = "no description";
	private Class<T> type;
	private String strType;

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
		this.strType = type.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}
}
