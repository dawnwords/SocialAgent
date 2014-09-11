package edu.fudan.sa.service;

import edu.fudan.sa.ISystemService;
import edu.fudan.sa.SocialCircle;
import edu.fudan.sa.ontology.Service;

import java.util.ArrayList;

/**
 * @author ming
 * @since 2014-03-27 10:32.
 */
public interface PublicationService extends ISystemService {

    void publish(SocialCircle circle, ArrayList<Service> service) throws Exception;

    void unpublish(SocialCircle circle, ArrayList<Service> services) throws Exception;
}