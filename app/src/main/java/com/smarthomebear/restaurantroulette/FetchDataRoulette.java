package com.smarthomebear.restaurantroulette;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchDataRoulette extends AsyncTask<Object,String,String> {
    String googleNearByPlacesData;
    String url;
    static String[][] res;

    String openNow,priceLevel,rating;

    @Override
    protected void onPostExecute(String s) {

        try{
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            res=new String[jsonArray.length()][8];

            for(int i=0;i<jsonArray.length();i++){

                JSONObject getAddress=jsonArray.getJSONObject(i);
                String address=getAddress.getString("formatted_address");

                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                JSONObject getLocation=jsonObject1.getJSONObject("geometry").getJSONObject("location");

                String lat=getLocation.getString("lat");
                String lng=getLocation.getString("lng");

                JSONObject getName=jsonArray.getJSONObject(i);
                String name=getName.getString("name");

                JSONObject getOpenNow=jsonArray.getJSONObject(i);
                if(getOpenNow.has("opening_hours")) {
                    openNow = getOpenNow.getJSONObject("opening_hours").getString("open_now");
                }else{
                    openNow="no data";
                }
                JSONObject getPlaceId=jsonArray.getJSONObject(i);
                String placeId=getPlaceId.getString("place_id");

                JSONObject getPriceLevel=jsonArray.getJSONObject(i);
                if(getPriceLevel.has("price_level")) {
                    priceLevel=getPriceLevel.getString("price_level");
                }
                else{priceLevel="no data";
                }

                JSONObject getRating=jsonArray.getJSONObject(i);
                if(getPriceLevel.has("rating")) {
                    rating=getRating.getString("rating");
                }
                else{rating="no data";
                }

                res[i][0]=name;
                res[i][1]=address;
                res[i][2]=lat;
                res[i][3]=lng;
                res[i][4]=placeId;
                res[i][5]=openNow;
                res[i][6]=priceLevel;
                res[i][7]=rating;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String[][] getRes(){
        return res;
    }
    @Override

    protected String doInBackground(Object... objects) {

        try{
            url=(String) objects[0];
            DownloadUrl downloadUrl=new DownloadUrl();
            googleNearByPlacesData= downloadUrl.receiveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearByPlacesData;
    }
}