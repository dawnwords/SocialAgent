package edu.fudan.sa.xjade;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ReceivingWorker {
	private ExecutorService service;
	private boolean toStop = false;
	private int port;

	public ReceivingWorker(int port) {
		this.port = port;
		this.service = Executors.newSingleThreadExecutor();
	}

	public void startWork() {
		this.service.execute(() -> {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(port);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			while (!toStop) {
				byte[] buf = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buf, 1024);
				try {
					ds.receive(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String content = new String(dp.getData(), 0, dp.getLength());
				onContentReceived(content);
			}
			ds.close();
		});
	}

	protected abstract void onContentReceived(String content);

	public void stop() {
		this.toStop = true;
	}
}
