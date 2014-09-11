package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;
import edu.fudan.sa.SocialCircle;

import java.util.Collection;

/**
 * @author ming
 * @since 2014-05-06 22:37.
 */
public interface CircleManager extends ISystemService {

	public Collection<SocialCircle> getMyCircles();

	void joinCircle(SocialCircle circle) throws Exception;

	void leftCircle(SocialCircle circle) throws Exception;

	Collection<SocialCircle> getAvailCircles() throws Exception;
}
