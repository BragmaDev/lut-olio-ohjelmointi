package com.example.ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentChangeListener {

    Context context = MainActivity.this;

    DrawerLayout drawer_layout;
    Toolbar toolbar;
    NavigationView nav_view;

    FragmentManager frag_manager;
    Fragment entry_fragment;
    Fragment co2_fragment;
    Fragment user_settings_fragment;
    Fragment login_fragment;
    Fragment weight_fragment;

    UserManager um = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        um.loadUsers(context);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        entry_fragment = new EntryFragment(this);
        co2_fragment = new CO2Fragment(this);
        user_settings_fragment = new UserSettingsFragment(this);
        login_fragment = new LoginFragment(this);
        weight_fragment = new WeightFragment(this);

        frag_manager = getSupportFragmentManager();
        frag_manager.beginTransaction().replace(R.id.frag_container, entry_fragment);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.nav_draw_open, R.string.nav_draw_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            frag_manager.beginTransaction().replace(R.id.frag_container, login_fragment).commit();
            nav_view.setCheckedItem(R.id.home);
        }

    }

    // This method changes the fragment if the user is logged in and an item is selected in the side menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (um.getUser() != null) {
            if (item.getItemId() == R.id.co2_emission) {
                frag_manager.beginTransaction().replace(R.id.frag_container, co2_fragment).commit();
            } else if (item.getItemId() == R.id.settings) {
                frag_manager.beginTransaction().replace(R.id.frag_container, user_settings_fragment).commit();
            } else if (item.getItemId() == R.id.account){
                frag_manager.beginTransaction().replace(R.id.frag_container, login_fragment).commit();
            } else if (item.getItemId() == R.id.weight) {
                frag_manager.beginTransaction().replace(R.id.frag_container, weight_fragment).commit();
            } else {
                frag_manager.beginTransaction().replace(R.id.frag_container, co2_fragment).commit();
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    // This method changes the fragment based on the string parameter
    @Override
    public void changeFragment(String frag_name) {
        Fragment frag;
        switch (frag_name) {
            case "entry":
                frag = entry_fragment;
                break;
            case "settings":
                frag = user_settings_fragment;
                break;
            case "co2":
                frag = co2_fragment;
                break;
            case "login":
                frag = login_fragment;
                break;
            case "weight":
                frag = weight_fragment;
                break;
            default:
                frag = co2_fragment;
                break;
        }
        frag_manager.beginTransaction().replace(R.id.frag_container, frag).commit();
    }
}