package com.example.user.bottlerecyclemap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<POIData> poiDatas;

    private SearchMarketThread searchMarketThread;
    private NetworkStatusReceiver netReceiver;

    private URL url;

    private String siteUrl = "https://empty-bottle-skyversion.c9users.io";
    private String json = null;
    private TMapView mapView;

    private EditText inputAddr;
    private boolean isGpsOn = false;

    private HttpURLConnection conn;
    private InputStream is;
    private String response;
    private ImageButton gpsIcon, markerIcon;

    private LocationManager locManager;
    private MapLocationListener locListener;
    private PopupMenu popup;
    private boolean isRegisterReceiver = false;

    private String oldSelectedTitle = "전체";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poiDatas = new ArrayList<POIData>();

        try{
            url = new URL(siteUrl);
            setConnection();
            searchMarketThread = new SearchMarketThread();
            searchMarketThread.execute();
        }catch (MalformedURLException urlEx) {}
        catch (IOException ioEx) {
            Toast.makeText(this, "서버와 연결이 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_main);

//        mapView = new TMapView(this);
//
//        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mapView);
//        layout.addView(mapView);
        mapView = (TMapView)findViewById(R.id.mapView);

        init();

        mapView.setSKPMapApiKey("ede306f3-4fc5-38e9-8569-0ec0fb956cb7");
        mapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKPMapApikeySucceed() {
                Log.d("TAG", "Map apiKey Successd");
            }

            @Override
            public void SKPMapApikeyFailed(String s) {
                Log.d("TAG", "Map apiKey Failed");
            }
        });

        mapView.setTMapLogoPosition(TMapView.TMapLogoPositon.POSITION_BOTTOMRIGHT);
        mapView.setLocationPoint(129.110917, 35.137424);
        mapView.setCenterPoint(129.110917, 35.137424);
        mapView.setZoomLevel(12);
    }

    public void turnGps(View view){
        try{
            String msg;

            if(isGpsOn){
                locManager.removeUpdates(locListener);
                msg = "GPS OFF";

                try{
                    mapView.removeMarkerItem("여기");
                }catch (NullPointerException nullEx) {}
            }else{
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 1, locListener);

                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if(mobile.isAvailable() || wifi.isAvailable())
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 250, 1, locListener);
                msg = "GPS ON";
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            isGpsOn = !isGpsOn;
        }catch (SecurityException securityEx) { }
    }

    public void selectMarker(View view){
        popup.show();
    }

    @Override
    protected void onDestroy() {
        try{
            if(isGpsOn){
                locManager.removeUpdates(locListener);
                unregisterReceiver(netReceiver);
            }
        }catch (NullPointerException nullEx){ }
        finally {
        }
        super.onDestroy();
    }

    private void init(){
        gpsIcon = (ImageButton) findViewById(R.id.turnGps);
        markerIcon = (ImageButton) findViewById(R.id.selectMarker);
        gpsIcon.bringToFront();
        markerIcon.bringToFront();
        // gps and marker icon is index 0

        locListener = new MapLocationListener(this, mapView);
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        netReceiver = new NetworkStatusReceiver(locManager, locListener);

        IntentFilter filter = new IntentFilter(Context.CONNECTIVITY_SERVICE);
        registerReceiver(netReceiver, filter);

        popup = new PopupMenu(this, findViewById(R.id.selectMarker));
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String selected = menuItem.getTitle().toString();

                if(!oldSelectedTitle.equals(selected)){
                    oldSelectedTitle = selected;

                    Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                    mapView.removeAllMarkerItem();

                    Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.ic_pin_01);
                    TMapMarkerItem markerItem;
                    TMapPoint point;
                    POIData data;

                    for(int i=0;i<poiDatas.size();i++){
                        data = poiDatas.get(i);
                        String martName = data.getTitle();

                        if(martName.contains(selected) || selected.equals("전체")
                                || (selected.equals("기타") &&
                                (!martName.contains("롯데") && !martName.contains("메가") && !martName.contains("하나로") && !martName.contains("이마트") && !martName.contains("홈플러스")))){
                            double lat = data.getLat();
                            double lng = data.getLng();

                            point = new TMapPoint(lat, lng);
                            markerItem = new TMapMarkerItem();
                            setMarker(markerItem, point, bitmap, martName);

                            mapView.addMarkerItem(martName, markerItem);
                        }
                    }
                }

                return true;
            }
        });
    }

    private void setConnection(){
        try{
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            conn.setDoInput(true);
        }catch (IOException ioEx) {}
    }

    @Override
    protected void onPause() {
        try{
            if(isGpsOn){
                locManager.removeUpdates(locListener);
                unregisterReceiver(netReceiver);
            }
        }catch (NullPointerException nullEx) { }
        super.onPause();
    } // 자원제거 할 때 사용

    public class SearchMarketThread extends AsyncTask<Void, String, JSONArray>{
        @Override
        protected void onPreExecute() {
            setConnection();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try{
                /*
                conn.setDoOutput(true);
                os = conn.getOutputStream();
                os.write(json.getBytes());
                // 이 부분이 추가되면 자동으로 POST방식으로 바뀜.
                */
                int resCode = conn.getResponseCode();

                if(resCode == HttpURLConnection.HTTP_OK){
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();

                    while((response = reader.readLine())!= null)
                        sb.append(response);

                    response = sb.toString();
                    sb.delete(0, sb.length());
                    sb = null;

                    JSONArray array = new JSONArray(response);

                    return array;
                }else{
                    throw new IOException(String.valueOf(resCode));
                }
            }catch (IOException ioEx) { publishProgress(ioEx.getMessage()); }
            catch (JSONException jsonEx) {}
            catch (NullPointerException nullEx) {}

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            try{
                int size = array.length();

                String martName, newAddr, addr, phone, addrBCode;
                Integer zipCode;
                double lat, lng;
                Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.ic_pin_01);
                TMapMarkerItem markerItem;
                TMapPoint point;

                for(int cnt=0;cnt<size;cnt++){
                    JSONObject obj = array.getJSONObject(cnt);
                    martName = obj.getString("title");
                    phone = obj.getString("phone");
                    newAddr = obj.getString("newAddress");
                    addr = obj.getString("address");
                    zipCode = Integer.parseInt(obj.getString("zipcode"));
                    lat = obj.getDouble("latitude");
                    lng = obj.getDouble("longitude");
                    addrBCode = obj.getString("addressBCode");
                    Log.d("TAG1", martName);

                    point = new TMapPoint(lat, lng);
                    markerItem = new TMapMarkerItem();
                    setMarker(markerItem, point, bitmap, martName);

                    mapView.addMarkerItem(martName, markerItem);
                    poiDatas.add(new POIData(martName, phone, addr, newAddr, zipCode, lat, lng, addrBCode));
                }
            }catch (JSONException jsonEx){}
            catch (NullPointerException nullEx) {}
        }

        @Override
        protected void onProgressUpdate(String... values) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("서버 점검 중입니다. 잠시 후 다시 시도해주세요.")
                    .setTitle("에러")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private TMapMarkerItem setMarker(TMapMarkerItem item, TMapPoint point, Bitmap bitmap, String name){
        item.setTMapPoint(point);
        item.setName(name);
        item.setVisible(item.VISIBLE);
        item.setIcon(bitmap);
        item.setCalloutTitle(name);
        item.setCanShowCallout(true);
        item.setAutoCalloutVisible(false);

        return item;
    }
}