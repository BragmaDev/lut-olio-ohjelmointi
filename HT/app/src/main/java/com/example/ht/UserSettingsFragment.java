package com.example.ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserSettingsFragment extends Fragment {
    View view;
    MainActivity main = null;
    UserManager um = UserManager.getInstance();
    TextView text_title;
    Button button_change;
    TextView text_weight;
    Spinner spinner_diet;
    Spinner spinner_low_carbon;

    public UserSettingsFragment(MainActivity main) { this.main = main; }
    public UserSettingsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        text_title = (TextView) view.findViewById(R.id.textTitle);
        button_change = (Button) view.findViewById(R.id.buttonChange);
        text_weight = (TextView) view.findViewById(R.id.textWeight);
        spinner_diet = (Spinner) view.findViewById(R.id.spinnerDiet);
        spinner_low_carbon = (Spinner) view.findViewById(R.id.spinnerLowCarbon);

        text_title.setText(um.getUser().getName());
        text_weight.setText(Float.toString(um.getUser().getWeight()));

        // Setting up the spinners
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
        R.array.diets, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_diet.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
        R.array.prefs, R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_low_carbon.setAdapter(adapter2);

        spinner_diet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                um.getUser().setDiet(spinner_diet.getItemAtPosition(position).toString());
            }
            @Override
            // If nothing is selected, the item is set to the user's saved setting
            public void onNothingSelected(AdapterView<?> parent) { spinner_diet.setSelection(adapter1.getPosition(um.getUser().getDiet())); }
        });

        spinner_low_carbon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_low_carbon.getItemAtPosition(position).toString().equals("Yes")) {
                    um.getUser().setLowCarbon(true);
                } else {
                    um.getUser().setLowCarbon(false);
                }
            }
            @Override
            // If nothing is selected, the item is set to the user's saved setting
            public void onNothingSelected(AdapterView<?> parent) {
                if (um.getUser().getLowCarbon()) {
                    spinner_low_carbon.setSelection(0);
                } else {
                    spinner_low_carbon.setSelection(1);
                }
            }
        });
    }


}
