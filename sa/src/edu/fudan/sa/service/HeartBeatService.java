package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;

/**
 * @author ming
 * @since 2014-06-15 14:23.
 */
public interface HeartBeatService extends ISystemService {

	void start();

	void restart();

	void stop();

	void addListener(HeartBeatListener listener);

	void removeListener(HeartBeatListener listener);

	interface HeartBeatListener {
		void onHeartBeat(long delay);
	}
}
