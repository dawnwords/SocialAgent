/*
 * Apache Felix OSGi tutorial.
**/

package edu.fudan.sa.example;

import edu.fudan.sa.IService;

/**
 * A simple service interface that defines a dictionary service.
 * A dictionary service simply verifies the existence of a word.
 */
public interface DictionaryService extends IService {
    /**
     * Check for the existence of a word.
     *
     * @param word the word to be checked.
     * @return true if the word is in the dictionary,
     * false otherwise.
     */
    public boolean checkWord(String word);
}