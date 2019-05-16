package com.yen.CA107G1.Emp;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.DirectionsJSONParser;
import com.yen.CA107G1.VO.SendBackVO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Maps extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {
    private GoogleMap mMap;
    private List<SendBackVO> hotelOrderVOList;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location mLastLocation;
    private Button startDirection;
    private String url = "";
    List<LatLng> latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String ord = getIntent().getStringExtra("hOrdList");
        Log.e("我EXTRA有東西嗎?", ord.toString());
        Gson gson = new Gson();
        Type listType = new TypeToken<List<SendBackVO>>() {
        }.getType();
        hotelOrderVOList = gson.fromJson(ord, listType);

        Log.e("我LIST有東西嗎?", hotelOrderVOList.toString());
        try {
            Log.e("我LIST有地址嗎?", hotelOrderVOList.get(0).getH_ord_address().toString());
        } catch (NullPointerException e) {
            Log.e("我是空的", e.toString());
        }
        initOrder();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    @SuppressLint("MissingPermission")
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // 1
        mMap.setMyLocationEnabled(true);

// 2
        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            // 3
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            // 4
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }
        }

mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
    }

    public void initOrder() {
        latLng = new ArrayList();
        try {

            for (SendBackVO h : hotelOrderVOList) {
                String getAddress = h.getH_ord_address();

                latLng.add(locationNameToMarker(getAddress));

            }
            getDirectionsUrl(latLng);

        } catch (NullPointerException e) {
            Log.e("我是Maps的InitOrder", e.toString());
        }
    }

    // 將地名或地址轉成位置後在地圖打上對應標記
    private LatLng locationNameToMarker(String locationName) {
        // 增加新標記前，先清除舊有標記
//        mMap.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 10;
        String myDirection = null;
        LatLng latLng = null;
        try {
            // 解譯地名/地址後可能產生多筆位置資訊，所以回傳List<Address>
            // 將maxResults設為1，限定只回傳1筆
            addressList = geocoder.getFromLocationName(locationName, maxResults);
//            geocoder.getFromLocation()

            // 如果無法連結到提供服務的伺服器，會拋出IOException
        } catch (Exception ie) {
            Log.e("我是MapsActivity的ＴＡＧ", ie.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            Toast.makeText(this, "地址出錯囉！", Toast.LENGTH_SHORT).show();
        } else {
            // 因為當初限定只回傳1筆，所以只要取得第1個Address物件即可
            Address address = addressList.get(0);

            // Address物件可以取出緯經度並轉成LatLng物件
            latLng = new LatLng(address.getLatitude(), address.getLongitude());


            // 將地址取出當作標記的描述文字
            String snippet = address.getAddressLine(0);

//            String code = address.getPostalCode();


            // 將地名或地址轉成位置後在地圖打上對應標記
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(locationName)
                    .snippet(snippet));

            // 將鏡頭焦點設定在使用者輸入的地點上
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(13)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        return latLng;
    }

    public void onLocationNameClick(View view) {
        mMap.clear();

        initOrder();
        Log.e("URLLLLLLLLLL", url);
    }


    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void getDirectionsUrl(List<LatLng> wayPoint) {

        // Origin of route
        String str_origin = "origin=" + "24.967820" + ","
                + "121.191511";

        // Destination of route
        String str_dest = "destination=" + "24.967820" + "," + "121.191511";

        // Sensor enabled
        String sensor = "sensor=false";

        // Travelling Mode
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
                + mode + "&" + "waypoints=";

        StringBuilder strbud = new StringBuilder();
        strbud.append(parameters);
        if (wayPoint != null) {
            String waypointLatLng = wayPoint.get(0).latitude + "," + wayPoint.get(0).longitude;
            strbud.append(waypointLatLng);
            for (int i = 1; i < wayPoint.size(); i++) {
                waypointLatLng = wayPoint.get(i).latitude + "," + wayPoint.get(i).longitude;
                strbud.append("|").append(waypointLatLng);
            }
        }
        parameters = strbud.toString();
        // Output format
        String output = "json";


        // Building the url to the web service
        String url2 = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters + "&key=AIzaSyAnKoOPsLYLvieImStuPZ_A3-onSLPiozQ";
        System.out.println("getDerectionsURL--->: " + url2);
        url = url2;
//        return url;
    }


    public void startDirection(View view) {
        new DownloadTask(url).execute();
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {
        private String myURL;

        public DownloadTask(String url) {
            this.myURL = url;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(myURL);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.e("我是林威志DownloadTask", data + "33456783345678");
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuilder sb = new StringBuilder();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception download url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        System.out.println("url:" + strUrl + "---->   downloadurl:" + data);
        Log.e("我是MAPs裡又見", data.toString() + "9999999999999999999999");
        return data;

    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                System.out.println("do in background:" + routes);
                Log.e("我是陳鶴麟ParserTask", routes.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("我是陳鶴麟ParserTask空的", e.toString());
            }

            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            List points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.zIndex(2);
                lineOptions.isClickable();
                // Changing the color polyline according to the mode
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }


    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private  View infoWindow;

        private MyInfoWindowAdapter() {
            infoWindow = LayoutInflater.from(Activity_Maps.this)
                    .inflate(R.layout.custom_infowindow, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {
            String title = marker.getTitle();
            TextView tvTitle = infoWindow.findViewById(R.id.customTitle);
            tvTitle.setText(title);

            String snippet = marker.getSnippet();
            TextView tvSnippet = infoWindow.findViewById(R.id.customSnippet);
            tvSnippet.setText(snippet);

            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }




}
