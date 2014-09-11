package edu.fudan.sa.service.example.hello.impl;

import edu.fudan.sa.ontology.Service;
import edu.fudan.sa.service.example.hello.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String to) {
        return "hello, " + to;
    }

    @Override
    public String sayHelloWorld() {
        return "hello, world";
    }

    @Override
    public Service getDescription() {
        Service<HelloService> description = new Service<HelloService>();
        description.setType(HelloService.class);
        description.setName(HelloService.class.getName());
        description.setDescription("say hello");
        return description;
    }
}
