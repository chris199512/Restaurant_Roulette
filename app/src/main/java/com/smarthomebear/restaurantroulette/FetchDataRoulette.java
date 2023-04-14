package com.smarthomebear.restaurantroulette;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchDataRoulette extends AsyncTask<Object,String,String> {
    String googleNearByPlacesData;
    String url;
    static String[] res;

    @Override
    protected void onPostExecute(String s) {

        try{
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            res=new String[jsonArray.length()];


            for(int i=0;i<jsonArray.length();i++){

                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                JSONObject getLocation=jsonObject1.getJSONObject("geometry").getJSONObject("location");

                String lat=getLocation.getString("lat");
                String lng=getLocation.getString("lng");

                JSONObject getName=jsonArray.getJSONObject(i);
                String name=getName.getString("name");

                LatLng latLng=new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                res[i]=name;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String[] getRes(){
        Log.d("Test",res[1]);
        return res;
    }
    @Override

    protected String doInBackground(Object... objects) {

        try{
            url=(String) objects[0];
            DownloadUrl downloadUrl=new DownloadUrl();
            googleNearByPlacesData= downloadUrl.retireveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearByPlacesData;
    }
}