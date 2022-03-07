package com.purupanda.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.purupanda.login.databinding.ActivityNewsBinding;

public class news extends AppCompatActivity {

    private FirebaseAuth auth;
//        stop restrict back button

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.purupanda.login.databinding.ActivityNewsBinding binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_news);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu homemenu)
    {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.homemenu,homemenu);
        return super.onCreateOptionsMenu(homemenu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.settings:
                Toast.makeText(this, "Opening settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),news.class);
                startActivity(intent);
                break;

            case R.id.logout:
                auth.signOut();
                Toast.makeText(this, "Logging you out", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(),signInPage.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}