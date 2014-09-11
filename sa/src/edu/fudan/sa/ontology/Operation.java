package edu.fudan.sa.ontology;

import jade.content.Concept;

/**
 * @author ming
 * @since 2014-04-16 14:13.
 */
public class Operation implements Concept {

	private Service service;
	private String name;
	private Input[] inputs;
	private Output output;
	private String effect;
	private String description;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Input[] getInputs() {
		return inputs;
	}

	public void setInputs(Input[] inputs) {
		this.inputs = inputs;
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
