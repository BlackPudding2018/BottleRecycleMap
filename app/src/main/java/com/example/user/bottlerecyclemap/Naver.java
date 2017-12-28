package com.example.user.bottlerecyclemap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

//import com.nhn.android.maps.NMapController;
//import com.nhn.android.maps.NMapView;
//import com.nhn.android.maps.nmapmodel.NMapError;
//import com.nhn.android.maps.overlay.NMapPOIdata;
//import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
//import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-04-09.
 */

public class Naver extends Activity {
//    private NMapView mapView;
//    private NMapController mapViewController;
//
//    private final String CLIENT_ID = "6NP3WYz3QXC_UtmaibvJ";
//    private NMapView.OnMapStateChangeListener mapStateChangeListener;
//    private NMapView.OnMapViewTouchEventListener mapViewTouchEventListener;
//    private NMapPOIdataOverlay.OnStateChangeListener poidataStateChangeListener;
//    private NMapPOIdataOverlay poidataOverlay;
//    private NMapPOIdata poidata;
//    private NMapViewerResourceProvider provider;
//    private NMapOverlayManager manager;
//
//    private ArrayList<POIData> poiDatas;
//
//    private MainActivity.SearchMarketThread searchMarketThread;
//
//    private URL url;
//    private String siteUrl = "https://empty-bottle-skyversion.c9users.io";
//    int markerId = 0x0100+1+1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        try{
//            url = new URL(siteUrl);
//            searchMarketThread = new MainActivity.SearchMarketThread();
//            searchMarketThread.execute();
//        }catch (MalformedURLException urlEx) {}
//
//        mapView = new NMapView(this);
//        mapView.setClientId(CLIENT_ID);
//
//        setContentView(mapView);
//
//        manager = new NMapOverlayManager(this, mapView, provider);
//        provider = new NMapViewerResourceProvider(this);
//
//        init();
//        initEvent();
//        setListener();
//
//        poidata.beginPOIdata(1);
//        poidata.addPOIitem(129.110917, 35.137424, "남천메가마트", markerId, 0);
//        poidata.endPOIdata();
//
//    }
//
//    private void init(){
//        poiDatas = new ArrayList<POIData>();
//
//        mapView.setClickable(true);
//        mapView.setBuiltInZoomControls(true, null);
//
//        mapViewController = mapView.getMapController();
//    }
//
//    private void setListener(){
//        mapView.setOnMapStateChangeListener(mapStateChangeListener);
//        mapView.setOnMapViewTouchEventListener(mapViewTouchEventListener);
//    }
//
//    public class SearchMarketThread extends AsyncTask<Void, Void, JSONArray> {
//        private HttpURLConnection conn;
//        private OutputStream os;
//        private InputStream is;
//        private String testValue = "{}";
//        private String response;
//
//        @Override
//        protected void onPreExecute() {
//            try{
//                conn = (HttpURLConnection)url.openConnection();
//                conn.setReadTimeout(100000);
//                conn.setConnectTimeout(100000);
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Cache-Control", "no-cache");
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Accept", "application/json");
//
////                JSONObject obj = new JSONObject();
////                obj.put("");
//            }catch (IOException ioEx) {}
//        }
//
//        @Override
//        protected JSONArray doInBackground(Void... voids) {
//            try{
//                conn.setDoInput(true);
//
////                conn.setDoOutput(true);
////                os = conn.getOutputStream();
////                os.write(testValue.toString().getBytes());
//                // 이 부분이 추가되면 자동으로 POST방식으로 바뀜.
//
//                int resCode = conn.getResponseCode();
//
//                if(resCode == HttpURLConnection.HTTP_OK){
//                    is = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                    StringBuilder sb = new StringBuilder();
//
//                    while((response = reader.readLine())!= null){
//                        sb.append(response);
//                    }
//
//                    response = sb.toString();
//                    sb.delete(0, sb.length());
//                    sb = null;
//
//                    return new JSONArray(response);
//                }
//            }catch (IOException ioEx) {}
//            catch (JSONException jsonEx) {}
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(JSONArray array) {
//            super.onPostExecute(array);
//
//            int size = array.length();
//
//            try{
//                poidata = new NMapPOIdata(size, provider);
//                poidata.beginPOIdata(size);
//
//                String martName;
//                double lat, lng;
//
//                for(int cnt=0;cnt<size;cnt++){
//                    Log.d("TAG1", array.getJSONObject(cnt).getString("title"));
//                    JSONObject obj = array.getJSONObject(cnt);
//                    martName = obj.getString("title");
//                    lat = obj.getDouble("latitude");
//                    lng = obj.getDouble("longitude");
//
//                    poidata.addPOIitem(lng, lat, martName, markerId, 0);
//                    poiDatas.add(new POIData(martName, "", lat, lng, markerId));
//                }
//
//                poidata.endPOIdata();
//
//                poidataOverlay = manager.createPOIdataOverlay(poidata, null);
//
//                poidataOverlay.setOnStateChangeListener(poidataStateChangeListener);
//                poidataOverlay.showAllPOIdata(0);
//            }catch (JSONException jsonEx){}
//        }
//    }
//
//    private void initEvent(){
//        mapStateChangeListener = new NMapView.OnMapStateChangeListener() {
//            @Override
//            public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
//                if(nMapError == null){
//                    mapViewController.setMapCenter(new NGeoPoint(129.110917, 35.137424), 15);
//                }else{
//                    Log.e("TAG", "onMapInitHandler : error=" + nMapError.toString());
//                }
//            }
//
//            @Override
//            public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
//            }
//
//            @Override
//            public void onMapCenterChangeFine(NMapView nMapView) {
//            }
//
//            @Override
//            public void onZoomLevelChange(NMapView nMapView, int i) {
//            }
//
//            @Override
//            public void onAnimationStateChange(NMapView nMapView, int i, int i1) {
//            }
//        };
//
//        mapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {
//            @Override
//            public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
//            }
//
//            @Override
//            public void onLongPressCanceled(NMapView nMapView) {
//            }
//
//            @Override
//            public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
//            }
//
//            @Override
//            public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
//            }
//
//            @Override
//            public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {
//            }
//
//            @Override
//            public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
//            }
//        };
//
//        poidataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
//            @Override
//            public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
//                try{
//                    Toast.makeText(MainActivity.this, "onCalloutClick: " + nMapPOIitem.getTitle(), Toast.LENGTH_LONG).show();
//                }catch (NullPointerException nullEx){}
//            }
//
//            @Override
//            public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
//                if (nMapPOIitem != null) {
//                    Log.i("TAG", "onFocusChanged: " + nMapPOIitem.toString());
//                } else {
//                    Log.i("TAG", "onFocusChanged: ");
//                }
//            }
//        };
//    }
}
