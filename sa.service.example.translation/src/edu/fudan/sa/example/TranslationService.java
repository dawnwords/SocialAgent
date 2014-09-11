package edu.fudan.sa.example;

import edu.fudan.sa.IService;

import java.util.Locale;

/**
 * a tow-way translation service between English and Chinese
 * <p/>
 * Created by ming on 2/19/14.
 */
public interface TranslationService extends IService {
    /**
     * @param fromLocale
     * @param words      words to be translated
     * @param toLocale
     * @return translated words(word or text)
     */
    String translate(Locale fromLocale, String words, Locale toLocale);
}
