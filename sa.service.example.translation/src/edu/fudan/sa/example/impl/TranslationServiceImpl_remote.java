package edu.fudan.sa.example.impl;

import android.util.Log;
import edu.fudan.sa.example.TranslationService;
import edu.fudan.sa.ontology.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * s Created by ming on 2/20/14.
 */
public class TranslationServiceImpl_remote implements TranslationService {

    private static final String TAG = "Translator";
    private static final String ENCODING = "UTF-8";
    private static final String KEY_FROM = "fudanselab";
    private static final String KEY = "2046097113";

    /**
     * @param fromLocale only zh and en are accepted
     * @param text       text to be translated
     * @param toLocale   only en and zh are accepted
     * @return
     */
    @Override
    public String translate(Locale fromLocale, String text, Locale toLocale) {
        try {
            return this.translate(text);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * <pre>
     * 请求标准：
     * http://fanyi.youdao.com/openapi.do?
     * 		keyfrom=<keyfrom>& key=<key>
     * 		&type=data&doctype=<doctype>&version=1.1
     * 		&q=要翻译的文本
     * 版本：1.1，请求方式：get，编码方式：utf-8
     * 主要功能：
     * 		中英互译，同时获得有道翻译结果和有道词典结果（可能没有）
     * 参数说明：
     * 		type - 返回结果的类型，固定为data
     * 		doctype - 返回结果的数据格式，xml或json或jsonp
     * 		version - 版本，当前最新版本为1.1
     * 		q - 要翻译的文本，不能超过200个字符，需要使用utf-8编码
     * 	errorCode：
     * 		0 - 正常
     * 		20 - 要翻译的文本过长
     * 		30 - 无法进行有效的翻译
     * 		40 - 不支持的语言类型
     * 		50 - 无效的key
     * </pre>
     */
    private String translate(String text) throws IOException, JSONException {
        String url = "http://fanyi.youdao.com/openapi.do?"
                + "keyfrom=" + KEY_FROM + "&key=" + KEY
                + "&type=data&doctype=json&version=1.0&q=" + text;
        InputStream is = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
            StringBuilder result = new StringBuilder();
            String string;
            if (null != (string = reader.readLine()))
                result.append(string).append('\n');
            String translation = (String) new JSONObject(result.toString()).getJSONArray("translation").get(0);
            Log.i(TAG, "result: " + result.toString());
            return translation;
        } finally {
            if (is != null)
                is.close();
        }
    }

    @Override
    public Service getDescription() {
        return null;
    }
}