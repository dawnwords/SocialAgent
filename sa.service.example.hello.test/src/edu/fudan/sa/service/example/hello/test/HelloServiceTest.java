package edu.fudan.sa.service.example.hello.test;

import edu.fudan.sa.Work;
import edu.fudan.sa.exception.RemoteException;
import edu.fudan.sa.exception.ResolutionException;
import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.ResolutionService;
import edu.fudan.sa.service.example.hello.HelloService;
import jade.core.behaviours.OneShotBehaviour;

import java.util.concurrent.*;

import static edu.fudan.sa.service.ResolutionService.ResolutionRequired;

/**
 * @author ming
 * @since 2014-04-28 10:15.
 */
public class HelloServiceTest extends OneShotBehaviour implements Work, ResolutionRequired {
    private HelloService helloService;

    @Override
    public void action() {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            FutureTask<String> future =
                    new FutureTask<String>(new Callable<String>() {//使用Callable接口作为构造参数
                        public String call() {
                            try {
                                return helloService.sayHello("wang");
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    });
            executor.execute(future);
            System.out.println(future.get(240000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resolve(ResolutionService resolver) throws ResolutionException {
        Service<HelloService> service = new Service<HelloService>();
        service.setType(HelloService.class);
        service.setName(HelloService.class.getName());
        helloService = resolver.resolve(service);
    }
}
