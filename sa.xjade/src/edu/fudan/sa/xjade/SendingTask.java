package edu.fudan.sa.xjade;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Project {SocialAgent}
 *
 * @author Ming
 * @since 15:51 2014-08-28.
 */
public class SendingTask implements Runnable {
	String ip;
	int port;
	String content;

	public SendingTask(String ip, int port, String content) {
		this.ip = ip;
		this.port = port;
		this.content = content;
	}

	@Override
	public void run() {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(content.getBytes(),
					content.getBytes().length, InetAddress.getByName(ip), port);
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ds.close();
		}
	}
}

