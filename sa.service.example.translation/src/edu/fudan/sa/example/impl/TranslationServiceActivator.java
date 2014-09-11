package edu.fudan.sa.example.impl;

import edu.fudan.sa.example.TranslationService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Translation Service Activator
 * <p/>
 * Created by ming on 2/20/14.
 */
public class TranslationServiceActivator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Translation Service Bundle starting...");
        this.registerAndroidService(bundleContext);
        this.registerHumanService(bundleContext);
        this.registerRemoteService(bundleContext);
        this.registerWebService(bundleContext);

    }

    private void registerWebService(BundleContext bundleContext) {
        TranslationService service = new TranslationServiceImpl_android();
        Dictionary<String, Serializable> properties = new Hashtable<String, Serializable>();
        Locale[] locales = new Locale[]{new Locale("zh"), new Locale("en")};
        properties.put("toLocales", locales);
        properties.put("fromLocales", locales);
        properties.put("type", "word");
        properties.put("vendor", "webxml.com.cn");
        bundleContext.registerService(TranslationService.class, service, properties);
    }

    private void registerRemoteService(BundleContext bundleContext) {
        TranslationService service = new TranslationServiceImpl_remote();
        Dictionary<String, Serializable> properties = new Hashtable<String, Serializable>();
        properties.put("toLocales", new Locale[]{});
        properties.put("fromLocales", new Locale[]{});
        properties.put("type", "text");
        properties.put("vendor", "youdao corp");
        bundleContext.registerService(TranslationService.class, service, properties);
    }

    private void registerHumanService(BundleContext bundleContext) {
        TranslationService service = new TranslationServiceImpl_human();
        Dictionary<String, Serializable> properties = new Hashtable<String, Serializable>();
        Locale[] locales = new Locale[]{new Locale("zh"), new Locale("en")};
        properties.put("toLocales", locales);
        properties.put("fromLocales", locales);
        properties.put("type", "limited text");
        bundleContext.registerService(TranslationService.class, service, properties);
    }

    private void registerAndroidService(BundleContext bundleContext) {
        TranslationService service = new TranslationServiceImpl_android();
        Dictionary<String, Serializable> properties = new Hashtable<String, Serializable>();
        Locale[] locales = new Locale[]{new Locale("zh"), new Locale("en")};
        properties.put("toLocales", new Locale[]{new Locale("en")});
        properties.put("fromLocales", new Locale[]{new Locale("zh")});
        properties.put("type", "limited words");
        bundleContext.registerService(TranslationService.class, service, properties);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
