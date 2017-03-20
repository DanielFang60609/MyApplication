package com.example.jason.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tauyuan = new LatLng(25.04, 121.33);
        mMap.setPadding(20, 20, 20, 20);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude = 25;
        double longitude = 121;
        LatLng newLatLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(newLatLng)              // Sets the center of the map to ZINTUN
                .zoom(20)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(newLatLng).title("Marker"));


        int minTime = 1000;//ms
        int minDist = 1;//meter
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        db = openOrCreateDatabase("db1.db", MODE_PRIVATE,null);
        try{
            db.execSQL("DROP TABLE table01");
            String createTable01 = "CREATE TABLE table01(_id INTEGER PRIMARY KEY , num INTEGER , data TEXT, latitude NUMERIC, longitude NUMERIC, information TEXT)";
            db.execSQL(createTable01);
            String insert_1 = "INSERT INTO table01 (num,data,latitude,longitude,information) values (1, '國立故宮博物院', 25.103016, 121.548449, '國立故宮博物院（簡稱故宮或故宮博物院，俗稱臺灣故宮或臺北故宮，別名中山博物院），坐落在中華民國臺北市士林區至善路2段221號和嘉義縣太保市故宮大道888號，為臺灣規模最大的博物館以及八景之一，同時也是古代中國藝術史與漢學研究重鎮，所藏近70萬件冊文物尤以古代中國藝術珍藏著稱。館舍一年可接待超過614萬人次參訪旅客，位居全球參觀人數最多的藝術博物館前列。')";
            db.execSQL(insert_1);
            String insert_2 = "INSERT INTO table01 (num,data,latitude,longitude,information) values (2, '台大體育館', 25.021918, 121.535285,'國立臺灣大學綜合體育館（NTU Sports Center，或稱台大巨蛋體育館、台大小巨蛋[1]）是國立臺灣大學總校區的一項設施，座落於臺北市大安區辛亥路與新生南路交叉口，無獨立門牌號碼，台大校內一般稱呼為新體。建設目的是為了替代台大內的舊體育館以提供更多的運動休閒空間與更新的運動器材，解決台大校內缺乏可以舉辦大型活動的場所的問題；並因為該館符合其他非台大校內團體的需求，亦外借給其他社會團體舉辦活動。')";
            db.execSQL(insert_2);
            String insert_3 = "INSERT INTO table01 (num,data,latitude,longitude,information) values (3, '台北車站', 25.047908, 121.517315, '')";
            db.execSQL(insert_3);



        }
        catch (Exception exception){
        }

        locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDist, (android.location.LocationListener) locationListener);

    }

    LocationListener locationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();
            LatLng newLatLng = new LatLng(currentLatitude, currentLongitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(newLatLng)              // Sets the center of the map to ZINTUN
                    .zoom(13)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(newLatLng).title("me"));

            Cursor query = db.query("table01", new String[] {"num", "data", "latitude", "longitude", "information"}, null, null, null, null, null, null);
            int row_num = query.getCount();

            final Bundle bundle = new Bundle();
            double shortestDis = 100000;
            int num = 0;
            double latitude = 0;
            double longitude =0;
            String name = null;
            String information = null;
            if(row_num != 0){
                query.moveToFirst();
                for(int row_i = 0; row_i < row_num; row_i++){
                    double latitude_i = query.getDouble(2);
                    double longitude_i = query.getDouble(3);
                    double distance = Distance(longitude_i, latitude_i, currentLongitude, currentLatitude);
                    if(distance > shortestDis) {
                        continue;
                    }
                    else{
                        shortestDis = distance;

                    }
                    num = query.getInt(0);
                    name = (String) query.getString(1);
                    latitude = latitude_i;
                    longitude = longitude_i;
                    information = (String) query.getString(4);
                    bundle.putString("name",name);
                    bundle.putDouble("latitude", latitude);
                    bundle.putDouble("longitude", longitude);
                    bundle.putString("information",information);
                    Log.e("show", name);
                    query.moveToNext();



                }

            }

            /*double gymLongitude = 121.535285;
            double gymLatitude = 25.021918;
            double gymDistance = Distance(currentLongitude, currentLatitude, gymLongitude, gymLatitude);

            double stationLongitude = 121.517315;
            double stationLatitude = 25.047908;
            double stationDistance = Distance(currentLongitude, currentLatitude, stationLongitude, stationLatitude);

            Log.e("distance",Double.toString(gymDistance));
            Log.e("distance",Double.toString(stationDistance));
            MarkerOptions gymMarkerOption = new MarkerOptions().position(new LatLng(gymLatitude, gymLongitude)).title("gym");
            final Marker gymMarker = mMap.addMarker(gymMarkerOption);
            MarkerOptions stationMarkerOption = new MarkerOptions().position(new LatLng(stationLatitude, stationLongitude)).title("station");
            Marker stationMarker = mMap.addMarker(stationMarkerOption);*/

            MarkerOptions shortestmarkerOption = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);
            final Marker shortestmarker = mMap.addMarker(shortestmarkerOption);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(marker.equals(shortestmarker)){
                        Log.e("", "123");
                        Intent intent =new Intent();
                        intent.setClass(MapsActivity.this, InformationActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                    return false;
                }
            });







        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public double Distance(double longitude1, double latitude1, double longitude2,double latitude2)
    {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;

        return distance ;
    }

}
