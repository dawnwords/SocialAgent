package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ming
 * @since 2014-06-15 15:03.
 */
public interface ResourceManager extends ISystemService {
	Map<String, Serializable> getResources();
}
