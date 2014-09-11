package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;
import edu.fudan.sa.Me;
import edu.fudan.sa.Work;
import edu.fudan.sa.agent.MessageListener;

/**
 * Created by ming on 2014-03-14 14:23.
 */
public interface WorkerService extends ISystemService {
	void work(Work work) throws Exception;

//    void work(Behaviour work) throws Exception;

	void listen(MessageListener listener);

	Me getMe();
}
