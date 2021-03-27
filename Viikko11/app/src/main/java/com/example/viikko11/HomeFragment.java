package com.example.viikko11;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    TextView text_read;
    TextView text_display_text;
    EditText edit_text;
    String edited_text = "";
    View view;
    SettingsSingleton settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        settings = SettingsSingleton.getInstance();
        text_read = (TextView) this.view.findViewById(R.id.textRead);
        text_display_text = (TextView) this.view.findViewById(R.id.textDisplayText);
        edit_text = (EditText) this.view.findViewById(R.id.editText);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settings.getEditingEnabled() == false) {
            edited_text = edit_text.getText().toString();
            edit_text.setEnabled(false);
        } else {
            edit_text.setEnabled(true);
        }
        text_read.setText(edited_text);
        text_read.setTextSize(settings.getFontSize());
        text_read.setWidth(settings.getWidth());
        text_read.setHeight(settings.getHeight());
        text_read.setAllCaps(settings.getAllCaps());
        text_display_text.setText(settings.getDisplayText());
    }
}
