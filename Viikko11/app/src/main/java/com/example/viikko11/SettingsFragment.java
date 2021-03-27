package com.example.viikko11;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    View view;
    ArrayAdapter<CharSequence> adapter;
    TextView text_toggled;
    Button button_toggle;
    EditText edit_font_size;
    EditText edit_display_text;
    SeekBar seek_width, seek_height;
    Switch switch_all_caps;
    Spinner spin_languages;
    SettingsSingleton settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        settings = SettingsSingleton.getInstance();

        edit_font_size = (EditText) view.findViewById(R.id.editFontSize);
        edit_font_size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    settings.setFontSize(Integer.valueOf(edit_font_size.getText().toString()));
                } catch (NumberFormatException e) {
                    settings.setFontSize(14);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit_display_text = (EditText) view.findViewById(R.id.editDisplayText);
        edit_display_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                settings.setDisplayText(edit_display_text.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        text_toggled = (TextView) view.findViewById(R.id.textToggled);
        if (settings.getEditingEnabled()) {
            text_toggled.setText("Editing enabled");
        } else {
            text_toggled.setText("Editing disabled");
        }

        button_toggle = (Button) view.findViewById(R.id.buttonToggle);
        button_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setEditingEnabled(!settings.getEditingEnabled());
                if (settings.getEditingEnabled()) {
                    text_toggled.setText("Editing enabled");
                } else {
                    text_toggled.setText("Editing disabled");
                }
            }
        });

        seek_width = (SeekBar) view.findViewById(R.id.seekWidth);
        seek_width.setMax(999);
        seek_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { settings.setWidth(progress); }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seek_height = (SeekBar) view.findViewById(R.id.seekHeight);
        seek_height.setMax(999);
        seek_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { settings.setHeight(progress); }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        switch_all_caps = (Switch) view.findViewById(R.id.switchAllCaps);
        switch_all_caps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { settings.setAllCaps(isChecked); }
        });

        spin_languages = (Spinner) view.findViewById(R.id.spinLanguages);
        spin_languages.setAdapter(adapter);
    }
}
