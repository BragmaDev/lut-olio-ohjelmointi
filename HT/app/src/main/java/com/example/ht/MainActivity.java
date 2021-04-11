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

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context;

    DrawerLayout drawer_layout;
    Toolbar toolbar;
    NavigationView nav_view;

    FragmentManager frag_manager;
    Fragment entry_fragment;

    EntryManager entry_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        entry_manager = new EntryManager();
        entry_fragment = new EntryFragment();

        frag_manager = getSupportFragmentManager();
        frag_manager.beginTransaction().replace(R.id.frag_container, entry_fragment);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.nav_draw_open, R.string.nav_draw_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            frag_manager.beginTransaction().replace(R.id.frag_container, entry_fragment).commit();
            nav_view.setCheckedItem(R.id.home);
        }

        entry_manager.getResponse();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.home) {
            frag_manager.beginTransaction().replace(R.id.frag_container, entry_fragment).commit();
        } else {
            frag_manager.beginTransaction().replace(R.id.frag_container, entry_fragment).commit();
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
}