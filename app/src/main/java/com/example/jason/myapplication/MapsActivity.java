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

        mMap.setPadding(20, 20, 20, 20);

        //get the initial position
        getInitialPosition(mMap);

        //deposit the information
        db = openOrCreateDatabase("db1.db", MODE_PRIVATE,null);
        depositInformation(db);

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
        int minTime = 1000;//ms
        int minDist = 1;//meter
        LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
                    Log.e("distance", distance+"");
                    if(distance > shortestDis) {
                        query.moveToNext();
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

    public void getInitialPosition(GoogleMap mMap){
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
    }
    public void depositInformation(SQLiteDatabase db){
        try{
            db.execSQL("DROP TABLE table01");
            String createTable01 = "CREATE TABLE table01(_id INTEGER PRIMARY KEY , num INTEGER , data TEXT, latitude NUMERIC, longitude NUMERIC, information TEXT)";
            db.execSQL(createTable01);

            String insert_1 = "INSERT INTO table01 (num,data,latitude,longitude,information) values (1, '中正紀念堂', 25.035556, 121.519722, '中正紀念堂是一座為紀念故前中華民國總統蔣中正而興建的建築，位於臺北市中正區，也是眾多紀念蔣中正總統的建築中規模最大者。全區250,000平方公尺，主樓高76公尺，管理機關為「國立中正紀念堂管理處」；而園區廣場的南北側，另建有國家表演藝術中心國家兩廳院管理的國家戲劇院和國家音樂廳，落成以來成為臺北市在國際上最著名地標之一。')";
            db.execSQL(insert_1);
            String insert_2 = "INSERT INTO table01 (num,data,latitude,longitude,information) values (2, '台大體育館', 25.021918, 121.535285,'國立臺灣大學綜合體育館（NTU Sports Center，或稱台大巨蛋體育館、台大小巨蛋[1]）是國立臺灣大學總校區的一項設施，座落於臺北市大安區辛亥路與新生南路交叉口，無獨立門牌號碼，台大校內一般稱呼為新體。建設目的是為了替代台大內的舊體育館以提供更多的運動休閒空間與更新的運動器材，解決台大校內缺乏可以舉辦大型活動的場所的問題；並因為該館符合其他非台大校內團體的需求，亦外借給其他社會團體舉辦活動。')";
            db.execSQL(insert_2);
            String insert_3 = "INSERT INTO table01 (num,data,latitude,longitude,information) values (3, '台北車站', 25.047908, 121.517315, '臺北車站於1891年7月5日設站，歷經多次遷移與改建後，現今站體為1989年9月2日啟用，臺鐵局本部也設於此地；隨著捷運、高鐵進駐之後，形成幅員廣闊的地下街區。車站周邊有商辦、補習班聚集的站前商圈，若再連結臺北轉運站、捷運北門站、機場捷運臺北站、華山特區等「臺北車站特定區」關聯設施，面積可達46.31公頃[8]。另外臺北車站常被稱為「臺北火車站」，臺語則稱為「臺北火車頭」或「臺北車頭」，年輕族群則常簡稱為「北車」')";
            db.execSQL(insert_3);
        }
        catch (Exception exception){
        }
    }

}
