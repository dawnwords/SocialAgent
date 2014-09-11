package edu.fudan.sa.service.rsi.dispatcher.impl;

import edu.fudan.sa.IService;
import edu.fudan.sa.Work;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.Input;
import edu.fudan.sa.ontology.Operation;
import edu.fudan.sa.service.ResolutionService;
import edu.fudan.sa.service.WorkerService;
import edu.fudan.sa.service.rsi.ServiceInvocation;
import edu.fudan.sa.service.rsi.ServiceInvocator;
import jade.core.behaviours.OneShotBehaviour;

import java.io.Serializable;

/**
 * @author ming
 * @since 2014-04-16 16:18.
 */
public class DefaultServiceInvocator extends OneShotBehaviour implements ServiceInvocator, ResolutionService.ResolutionRequired, Work {

	private IService service;
	private ServiceInvocation invocation;

	@Override
	public boolean execute(ServiceInvocation invocation, WorkerService workerService) {
		this.invocation = invocation;
		try {
			workerService.work(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getNamespace() {
		return null;
	}

	@Override
	public void action() {
		Operation operation = this.invocation.getOperation();
		Input[] inputs = operation.getInputs();
//        Class[] classes = new Class[inputs.length];
//        Serializable[] values = new Serializable[inputs.length];
//        int i = 0;
//        for (Input input : operation.getInputs()) {
//            Class clazz = input.getType();
//            classes[i] = clazz;
//            values[i] = input.getValue();
//            i++;
//        }
		try {
//            Method method = service.getClass().getMethod(operation.getName(), classes);
			Serializable result = "hello," + inputs[0].getValue();//(Serializable) method.invoke(service, values);
			invocation.returnResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resolve(ResolutionService resolver) throws ResolutionException {


//        Service service = invocation.getOperation().getService();
//        this.service = resolver.resolve(service);
	}
}
