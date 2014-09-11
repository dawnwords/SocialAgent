package edu.fudan.sa.xjade;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendingWorker {
	private static SendingWorker worker;
	private ExecutorService executors;

	private SendingWorker() {
		this.executors = Executors.newFixedThreadPool(5);
	}

	public static SendingWorker getInstance() {
		if (worker == null)
			worker = new SendingWorker();
		return worker;
	}

	void send(SendingTask task) {
		this.executors.execute(task);
	}
}