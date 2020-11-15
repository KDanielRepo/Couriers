package com.pkisi.dkuc.couriers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Map extends AppCompatActivity implements LocationListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private LocationManager locationManager;
    private static Location location;
    private Button button;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    Context ctx;

    ArrayList<GeoPoint> points;


    ArrayList<Package> packages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        button = this.findViewById(R.id.mapButton);
        button.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.naviagtion_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(54.1985, 16.1906);
        mapController.setCenter(startPoint);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                System.out.println(("Adres w tym punkcie to: " + strReturnedAddress.toString()));
            } else {
                System.out.println(("Nie ma w tym punkcie zadnego adresu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(("nie mozna znalezc adresu"));
        }
        return strAdd;
    }

    private LatLng getLatLongFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;
        try {
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

    public ArrayList<Integer> tsp(List<Package> al) {
        ArrayList<Point> points = new ArrayList<>();
        GeoPoint geo = new GeoPoint(54.1985, 16.1906);
        Point p = new Point(false, false, geo);
        points.add(p);
        for (int i = 0; i < al.size(); i++) {
            LatLng a = getLatLongFromAddress(al.get(i).getUlica() + " " + al.get(i).getNr_domu() + " " + al.get(i).getMiasto());
            GeoPoint g = new GeoPoint(a.latitude, a.longitude);
            Point point = new Point(false, false, g);
            points.add(point);
        }

        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);

        double x = points.get(0).getGeoPoint().getLatitude();
        double y = points.get(0).getGeoPoint().getLongitude();
        double min = 0;
        int seen = 0;

        points.get(0).setSeen(true);

        while (!allSeen(points)) {
            min = 0;
            for (int i = 0; i < points.size(); i++) {
                if (!points.get(i).isSeen()) {
                    double x2 = points.get(i).getGeoPoint().getLatitude();
                    double y2 = points.get(i).getGeoPoint().getLongitude();
                    double wynik = path(x, y, x2, y2);
                    if (min == 0) {
                        min = wynik;
                        seen = i;
                    } else if (wynik < min) {
                        min = wynik;
                        seen = i;
                    }
                }
            }
            order.add(seen);
            points.get(seen).setSeen(true);
            x = points.get(seen).getGeoPoint().getLatitude();
            y = points.get(seen).getGeoPoint().getLongitude();
        }
        return order;
    }

    public double path(double x1, double y1, double x2, double y2) {
        double R = 6371*10^3;
        double fi1 = x1 * Math.PI/180;
        double fi2 = x2 * Math.PI/180;
        double deltaFi = (x2-x1) * Math.PI/180;
        double deltaLambda = (y2-y1) * Math.PI/180;

        double a = Math.sin(deltaFi/2) * Math.sin(deltaFi/2) + Math.cos(fi1) * Math.cos(fi2) *
                Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double d = R*c;
        return d;
    }

    public boolean allSeen(List<Point> list) {
        int seen = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSeen()) {
                seen++;
            }
        }
        return seen == list.size();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onLocationChanged(Location location) {
        setLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mapButton) {
            DatabaseConnect databaseConnect = new DatabaseConnect();
            databaseConnect.execute();
            while (databaseConnect.getResult() == null) {

            }
            packages = databaseConnect.getAllPackages(databaseConnect.getResult());
            ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            points = new ArrayList<>();
            long a = System.nanoTime();
            for (int i = 0; i < packages.size(); i++) {
                LatLng aa = getLatLongFromAddress(packages.get(i).getUlica() + " " + packages.get
                        (i).getNr_domu() + " " + packages.get(i).getMiasto());
                GeoPoint g = new GeoPoint(aa.latitude, aa.longitude);
                points.add(g);
            }
            ArrayList<Integer> order = tsp(packages);
            final ArrayList<GeoPoint> newPoints = new ArrayList<>();
            for (int j = 0; j < order.size(); j++) {
                if (order.get(j) == 0) {
                    items.add(new OverlayItem("Start", "Start", new GeoPoint(54.1985, 16.1906)));
                    newPoints.add(new GeoPoint(54.1985, 16.1906));
                } else {
                    items.add(new OverlayItem(packages.get(order.get(j) - 1).getNr_paczki(), Float.toString(packages.get(order.get(j) - 1).getKoszt()), new GeoPoint(points.get(order.get(j) - 1).getLatitude(), points.get(order.get(j) - 1).getLongitude())));
                    newPoints.add(new GeoPoint(points.get(order.get(j) - 1).getLatitude(), points
                            .get(order.get(j) - 1).getLongitude()));
                }
            }
            ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                    //do something
                    return true;
                }

                @Override
                public boolean onItemLongPress(final int index, final OverlayItem item) {
                    return false;
                }
            }, ctx);
            mOverlay.setFocusItemsOnTap(true);
            map.getOverlays().add(mOverlay);
            //}
            AsyncTask mapOverlay = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    RoadManager roadManager = new OSRMRoadManager(ctx);
                    Road road = roadManager.getRoad(newPoints);
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    map.getOverlays().add(roadOverlay);
                    return null;
                }
            }.execute();
            long b = System.nanoTime();
            System.out.println("czas wykonania: "+(b-a));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        System.out.println(menuItem.getItemId());
        switch (menuItem.getItemId()) {
            case R.id.nav_list:
                Intent intent = new Intent(this, Packages.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                break;
            case R.id.nav_map:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
//16.1906 54.1985