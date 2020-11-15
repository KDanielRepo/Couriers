package com.pkisi.dkuc.couriers;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Packages extends AppCompatActivity implements View.OnClickListener, NavigationView
        .OnNavigationItemSelectedListener {
    private TextView searchBar;
    private RadioButton one, two;
    private ArrayList<Package> al;
    private ListView ls;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packages_activity);
        Button search = this.findViewById(R.id.searchButton);
        Button menu = this.findViewById(R.id.menuButton);
        searchBar = this.findViewById(R.id.searchBar);
        search.setOnClickListener(this);
        menu.setOnClickListener(this);
        one = this.findViewById(R.id.radioButton);
        two = this.findViewById(R.id.radioButton2);

        long a = System.nanoTime();
        DatabaseConnect databaseConnect = new DatabaseConnect();
        databaseConnect.execute();
        while (databaseConnect.getResult() == null) {

        }
        long b = System.nanoTime();
        System.out.println("czas polaczenia: "+(b-a));
        ls = findViewById(R.id.itemList);
        ArrayAdapter<Package> arrayAdapter;
        al = databaseConnect.getAllPackages(databaseConnect.getResult());
        arrayAdapter = new ArrayAdapter<Package>(this, R.layout.list_element, al);
        ls.setAdapter(arrayAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.naviagtion_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }



    private LatLng getLatLongFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }
        if (p1 != null) {
            System.out.println(p1.latitude + " " + p1.longitude);
        }
        return p1;

    }

    public void searchForAddress(String pc) {
        ArrayList<String> temp = new ArrayList<>();
        String st = "";
        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).getMiasto().contains(pc)) {
                st = al.get(i).getMiasto();
                temp.add(st);
            }
        }
        if(st.equals("")){
            Toast toast = Toast.makeText(Packages.this, "Nie znaleziono podanej paczki lub " +
                            "jej adresu",
                    Toast.LENGTH_SHORT);
            toast.show();
            ArrayAdapter<Package> arrayAdapter = new ArrayAdapter<Package>(this, R.layout.list_element,
                    al);
            ls.setAdapter(arrayAdapter);
        }else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_element,
                    temp);
            ls.setAdapter(arrayAdapter);
        }
    }

    public void searchForPackage(String adr) {
        ArrayList<String> temp = new ArrayList<>();
        String st = "";
        for (int i = 0; i < al.size(); i++) {
            if (al.get(i).getMiasto().contains(adr)) {
                st = al.get(i).getMiasto();
                temp.add(st);
            }
        }
        if(st.equals("")){
            Toast toast = Toast.makeText(Packages.this, "Nie znaleziono podanej paczki lub " +
                            "jej adresu",
                    Toast.LENGTH_SHORT);
            toast.show();
            ArrayAdapter<Package> arrayAdapter = new ArrayAdapter<Package>(this, R.layout.list_element,
                    al);
            ls.setAdapter(arrayAdapter);
        }else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_element,
                    temp);
            ls.setAdapter(arrayAdapter);
        }
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.searchButton) {
            if (one.isChecked()) {
                searchForAddress(searchBar.getText().toString());
            } else if (two.isChecked()) {
                searchForPackage(searchBar.getText().toString());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        System.out.println(menuItem.getItemId());
        switch (menuItem.getItemId()){
            case R.id.nav_list:

                break;
            case R.id.nav_map:
                Intent intent = new Intent(this, Map.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
