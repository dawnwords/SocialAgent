package edu.fudan.sa.example.impl;

import edu.fudan.sa.ServiceDescription;
import edu.fudan.sa.example.DictionaryService;

import java.net.URI;


/**
 * A private inner class that implements a dictionary service; see
 * DictionaryService for details of the service.
 */
public class DictionaryServiceImpl implements DictionaryService {
    // The set of words contained in the dictionary.
    String[] m_dictionary = {"welcome", "to", "the", "osgi", "tutorial"};

    /**
     * Implements DictionaryService.checkWord(). Determines if the passed in
     * word is contained in the dictionary.
     *
     * @param word the word to be checked.
     * @return true if the word is in the dictionary, false otherwise.
     */
    public boolean checkWord(String word) {
        word = word.toLowerCase();

        // This is very inefficient
        for (int i = 0; i < m_dictionary.length; i++) {
            if (m_dictionary[i].equals(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ServiceDescription getServiceDescription() {
        ServiceDescription description = new ServiceDescription();
        description.setName("dictionary service");
        description.setVersion("1.0.0");
        return description;
    }

    @Override
    public URI getServiceDescriptionURI() {
        return null;
    }
}