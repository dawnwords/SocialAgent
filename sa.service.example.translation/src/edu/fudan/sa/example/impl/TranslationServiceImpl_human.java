package edu.fudan.sa.example.impl;

import android.app.Fragment;
import android.content.Context;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.example.TranslationService;
import edu.fudan.sa.ontology.Service;

import java.util.Locale;

/**
 * <p>human based translation service</p>
 * Created by ming on 2/20/14.
 */
public class TranslationServiceImpl_human implements TranslationService, GUIService {
    @Override
    public Fragment getView(Context activity) {
        return null;
    }

    @Override
    public String translate(Locale fromLocale, String words, Locale toLocale) {
        return null;
    }

    @Override
    public Service getDescription() {
        return null;
    }
}
