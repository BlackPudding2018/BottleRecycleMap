package com.example.user.bottlerecyclemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

/**
 * Created by user on 2017-04-13.
 */

public class MapLocationListener implements LocationListener {
    private Context mContext;
    private TMapView mapView;

    public MapLocationListener(Context mContext, TMapView mapView) {
        this.mContext = mContext;
        this.mapView = mapView;
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        float accuracy = location.getAccuracy();
        String provider = location.getProvider();

        Toast.makeText(mContext, "정보 : " + provider + " / 위도 : " + lng + " / 경도 : " + lat +
                " / 정확도 : " + accuracy, Toast.LENGTH_SHORT).show();
        mapView.setLocationPoint(lng, lat);
        mapView.setCenterPoint(lng, lat);

        try{
            mapView.removeMarkerItem("여기");
        }catch (NullPointerException nullEx) {}
        finally {
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.satellite_resized);
            TMapMarkerItem item = new TMapMarkerItem();
            item.setTMapPoint(new TMapPoint(lat, lng));
            item.setName("여기");
            item.setVisible(item.VISIBLE);
            item.setIcon(bitmap);
            item.setCalloutTitle("정보 : " + provider + "\n위도 : " + lng + "\n경도 : " + lat +
                    "\n정확도 : " + accuracy);
            item.setCanShowCallout(true);
            item.setAutoCalloutVisible(false);
            mapView.addMarkerItem("여기", item);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
        Toast.makeText(mContext, "onStatusChanged provider : " + provider + " / Status : " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "onProviderEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext, "onProviderDisabled", Toast.LENGTH_SHORT).show();
    }
}
