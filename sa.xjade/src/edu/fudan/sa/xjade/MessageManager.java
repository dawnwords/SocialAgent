package edu.fudan.sa.xjade;

import edu.fudan.sa.agent.MessageListener;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Project {SocialAgent}
 *
 * @author Ming
 * @since 19:31 2014-08-28.
 */
public class MessageManager extends ReceivingWorker {
	private static final int port = 8880;
	private static MessageManager instance;
	private final ExecutorService executors;
	private List<MessageListener> listeners;
	private SendingWorker sendingWorker;
	private Agent agent;

	private MessageManager() {
		super(MessageManager.port);//FIXME
		this.listeners = new LinkedList<MessageListener>();
		this.executors = Executors.newFixedThreadPool(5);
		this.sendingWorker = SendingWorker.getInstance();
	}

	public static MessageManager getInstance() {
		if (instance == null)
			instance = new MessageManager();
		return instance;
	}

	public void send(ACLMessage message, String ip) {
		String content = message.toString();
		SendingTask task = new SendingTask(ip, MessageManager.port, content);
		this.sendingWorker.send(task);
	}

	public void addMessageListener(MessageListener listener) {
		listener.setContainer(listeners);
		listeners.add(listener);
	}

	public ACLMessage request(final ACLMessage message, String ip, MessageTemplate template) throws InterruptedException, ExecutionException, TimeoutException {
		if (template == null)
			template = XFIPAService.getReplyTemplate(this.agent, message);
		final BlockingQueue<ACLMessage> messages = new ArrayBlockingQueue<>(1);
		final MessageTemplate finalTemplate = template;
		this.addMessageListener(new MessageListener() {
			@Override
			public void onMessageReceived(Agent agent, ACLMessage msg) {
				messages.add(msg);
				this.remove();
			}

			@Override
			public MessageTemplate getPattern() {
				return finalTemplate;
			}
		});
		this.send(message, ip);
		return messages.poll(30, TimeUnit.SECONDS);
	}

	protected void onContentReceived(final String content) {
		this.executors.submit(() -> {
			ACLMessage msg = XFIPAService.decode(content);
			if (msg != null) {
				for (int i = listeners.size() - 1; i > -1; i--) {
					MessageListener listener = listeners.get(i);
					MessageTemplate pattern = listener.getPattern();
					if (pattern == null || pattern.match(msg)) {
						Agent agent = null;//FIXME
						listener.onMessageReceived(agent, msg);
					}
				}
			}
		});
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
