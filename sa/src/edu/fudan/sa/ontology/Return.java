package edu.fudan.sa.ontology;

import jade.content.AgentAction;

import java.io.Serializable;

/**
 * @author ming
 * @since 2014-05-06 11:43.
 */
public class Return implements AgentAction {
	private Serializable result;

	public Return() {
	}

	public Return(Serializable result) {
		this.result = result;
	}

	public Serializable getResult() {
		return result;
	}

	public void setResult(Serializable result) {
		this.result = result;
	}
}
