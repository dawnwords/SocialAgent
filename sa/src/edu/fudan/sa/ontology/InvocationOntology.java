package edu.fudan.sa.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

/**
 * @author ming
 * @since 2014-05-04 21:12.
 */
public class InvocationOntology extends BeanOntology {
	public static final String NAME = "invocation-edu.fudan.sa.ontology";
	private static Ontology theInstance = new InvocationOntology();

	private InvocationOntology() {
		super(NAME);
		try {
			add(Input.class);
			add(Invocation.class);
			add(Operation.class);
			add(Output.class);
			add(Return.class);
			add(Service.class);
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	}

	public static Ontology getInstance() {
		return theInstance;
	}
}
