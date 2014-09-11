package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;
import edu.fudan.sa.SocialCircle;

import java.util.Collection;

/**
 * @author ming
 * @since 2014-04-21 14:36.
 */
public interface CircleDriver extends ISystemService {

    Collection<SocialCircle> getAvailCircles() throws Exception;
}
