package com.example.ht;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.w3c.dom.Text;

public class UserSettingsFragment extends Fragment {
    View view;
    MainActivity main = null;
    UserManager um = UserManager.getInstance();
    TextView text_title;
    Button button_change;
    TextView text_weight;
    Spinner spinner_diet;
    Spinner spinner_low_carbon;
    Button button_delete;

    boolean diet_initialized;
    boolean low_carbon_initialized;

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
        button_change = (Button) view.findViewById(R.id.buttonLogIn);
        text_weight = (TextView) view.findViewById(R.id.textWeight);
        spinner_diet = (Spinner) view.findViewById(R.id.spinnerDiet);
        spinner_low_carbon = (Spinner) view.findViewById(R.id.spinnerLowCarbon);
        button_delete = (Button) view.findViewById(R.id.buttonDelete);

        /* These booleans are used to prevent the spinners' setOnItemSelectedListeners from activating
        immediately when opening the fragment */
        diet_initialized = false;
        low_carbon_initialized = false;

        text_title.setText(um.getUser().getName());
        text_weight.setText(Float.toString(um.getUser().getWeight()) + " kg");

        // Setting up the spinners
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
        R.array.diets, R.layout.spinner_layout);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_diet.setAdapter(adapter1);
        spinner_diet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!diet_initialized) {
                    spinner_diet.setSelection(adapter1.getPosition(um.getUser().getDiet()));
                    diet_initialized = true;
                } else {
                    um.getUser().setDiet(spinner_diet.getItemAtPosition(position).toString());
                }
            }
            @Override
            // If nothing is selected, the item is set to the user's saved setting
            public void onNothingSelected(AdapterView<?> parent) { spinner_diet.setSelection(adapter1.getPosition(um.getUser().getDiet())); }

        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.prefs, R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_low_carbon.setAdapter(adapter2);
        spinner_low_carbon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!low_carbon_initialized) {
                    if (um.getUser().getLowCarbon()) {
                        spinner_low_carbon.setSelection(0);
                    } else {
                        spinner_low_carbon.setSelection(1);
                    }
                    low_carbon_initialized = true;
                } else {
                    if (spinner_low_carbon.getItemAtPosition(position).toString().equals("Yes")) {
                        um.getUser().setLowCarbon(true);
                    } else {
                        um.getUser().setLowCarbon(false);
                    }
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

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_confirmation_dialog();
            }
        });
    }

    private void show_confirmation_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete this account?")
                .setTitle("Confirmation")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        um.removeUser();
                        main.changeFragment("login");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        builder.create().show();
    }
}
