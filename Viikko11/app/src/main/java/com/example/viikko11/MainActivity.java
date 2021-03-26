package com.example.viikko11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer_layout;
    Toolbar toolbar;
    NavigationView nav_view;
    FragmentManager manager;
    Fragment home_fragment;
    Fragment settings_fragment;
    SettingsSingleton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer_layout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        home_fragment = new HomeFragment();
        settings_fragment = new SettingsFragment();
        settings = SettingsSingleton.getInstance();

        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frag_container, home_fragment);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.nav_draw_open, R.string.nav_draw_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            manager.beginTransaction().replace(R.id.frag_container, home_fragment).commit();
            nav_view.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.home) {
            manager.beginTransaction().replace(R.id.frag_container, home_fragment).commit();
        } else {
            manager.beginTransaction().replace(R.id.frag_container, settings_fragment).commit();
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}