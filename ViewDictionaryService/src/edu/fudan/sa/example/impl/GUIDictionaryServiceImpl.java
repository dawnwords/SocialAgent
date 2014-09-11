package edu.fudan.sa.example.impl;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.fudan.sa.android.GUIService;
import edu.fudan.sa.android.ServiceView;
import edu.fudan.sa.example.DictionaryService;
import edu.fudan.sa.ontology.Service;

/**
 * A private inner class that implements a dictionary service; see
 * DictionaryService for details of the service.
 */
public class GUIDictionaryServiceImpl implements DictionaryService, GUIService {
    // The set of words contained in the dictionary.
    String[] m_dictionary = {"welcome", "to", "the", "osgi", "tutorial"};
    private EditText inText;
    private TextView txtResult;
    private Context context;

    @Override
    public Fragment getView(Context context) {
        this.context = context;
        return new DictionaryServiceView();
    }

    @Override
    public Service getDescription() {
        return null;
    }

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
        for (String aM_dictionary : m_dictionary) {
            if (aM_dictionary.equals(word)) {
                return true;
            }
        }
        return false;
    }

    class DictionaryServiceView extends ServiceView {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            System.out.println("=== Create the view ===");
            LinearLayout container = new LinearLayout(context);
            container.setOrientation(LinearLayout.VERTICAL);

            inText = new EditText(context);
            inText.setLines(1);

            Button btnCheck = new Button(context);
            btnCheck.setText("check");
            txtResult = new TextView(context);

            btnCheck.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String word = inText.getText().toString();
                    boolean result = GUIDictionaryServiceImpl.this.checkWord(word);
                    if (!result) {
                        txtResult.setText("mispelled:" + word);
                    } else {
                        txtResult.setText("great!");
                    }
                }
            });

            container.addView(inText);
            container.addView(btnCheck);
            container.addView(txtResult);
            container.setVisibility(View.VISIBLE);
            return container;
        }
    }
}