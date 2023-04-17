package com.smarthomebear.restaurantroulette;

/*
This is an implementation of an Android AsyncTask class named FetchDataRoulette. It is used to fetch data from the Google Places API for nearby restaurants based on the user's location. The fetched data is in JSON format and is parsed to extract information about each restaurant such as its name, address, latitude, longitude, rating, price level, and more.

The class extends AsyncTask and overrides two of its methods: doInBackground() and onPostExecute(). The former is called when the task is executed, and the latter is called when the task is completed. The doInBackground() method is responsible for downloading the data from the API in the background, while the onPostExecute() method is responsible for parsing the downloaded data and storing it in a 2D string array named res.

The res array contains information about each restaurant, and each row of the array corresponds to a single restaurant. The columns of the array contain the following information:

Business status (OPERATIONAL, CLOSED_TEMPORARILY, CLOSED_PERMANENTLY)
Restaurant name
Full address
Latitude
Longitude
Photo ID
Place ID
Open now (true/false)
Price level (0-4)
Rating (1.0-5.0)
The getRes() method is provided to allow external classes to access the res array.
 */

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchDataRoulette extends AsyncTask<Object,String,String> {
    String googleNearByPlacesData;
    String url;

    //Set variables for the array
    static String[][] res;
    String openNow, priceLevel, rating, address, lat, lng, name, placeId, photo, businessStatus;
    JSONObject getLocation;

    @Override
    protected void onPostExecute(String s) {
        //Try to read the data as json
        try{
            JSONObject jsonObject=new JSONObject(s);
            //ToDo: Abfrage bei "status" : "OK" "ZERO_RESULTS" und allen anderen
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            //set the array for the values of the restaurants
            res=new String[jsonArray.length()][10];

            //set the values for each restaurant
            for(int i=0;i<jsonArray.length();i++){

                //set the business state("OPERATIONAL", "CLOSED_TEMPORARILY" and "CLOSED_PERMANENTLY"), example: "OPERATIONAL"
                JSONObject getBusinessStatus=jsonArray.getJSONObject(i);
                if(getBusinessStatus.has("business_status")) {
                    businessStatus=getBusinessStatus.getString("business_status");
                }else{
                    businessStatus="no data";
                }

                //set the full address, example: "Bahnhofstraße 55, 32469 Petershagen, Deutschland"
                JSONObject getAddress=jsonArray.getJSONObject(i);
                if(getAddress.has("formatted_address")) {
                    address=getAddress.getString("formatted_address");
                }else{
                    address="no data";
                }

                //find the geo data
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                if(jsonObject1.has("geometry")) {
                    getLocation=jsonObject1.getJSONObject("geometry").getJSONObject("location");
                }

                //set the latitude, example: "52.36828"
                if(getLocation.has("lat")) {
                    lat=getLocation.getString("lat");
                }else{
                    lat="no data";
                }

                //set the longitude, example: "9.003250099999999"
                if(getLocation.has("lng")) {
                    lng=getLocation.getString("lng");
                }else{
                    lng="no data";
                }

                //set the name of the restaurant, example: "Odisseus Christu Grill-Taverne Akropolis"
                JSONObject getName=jsonArray.getJSONObject(i);
                if(getName.has("name")) {
                    name=getName.getString("name");
                }else{
                    name="no data";
                }

                //set true/false if the restaurant is now open, example: "false"
                JSONObject getOpenNow=jsonArray.getJSONObject(i);
                if(getOpenNow.has("opening_hours")) {
                    openNow = getOpenNow.getJSONObject("opening_hours").getString("open_now");
                }else{
                    openNow="no data";
                }

                //ToDo: aus dem Array die werte entnehmen
                /*
                //set id of the photo from a restaurant, example: "AUjq9jnTQ4TQE-SrhrDtFpKj46VvZsMLVBRU-k5brCAwEcG8rLmw9dV4aaWssn36BudPGBCYT2OpFZC52O3Qz8Tp50-FnU5veeUuU7Qf-hDykM0clJJ0smNMemkJV367m-x-dul84oR2ikZinjQqPSWelIAFtO0Xkrbsm8j8rymqWYIFrLPK"
                JSONObject getPhoto=jsonArray.getJSONObject(i);
                if(getPhoto.has("photos")) {
                    photo = getPhoto.getJSONObject("photos").getString("photo_reference");
                }else{
                    photo="no data";
                }
                */
                photo="no data";

                //set the id for further search, example: "ChIJeSGcXnidsEcRXOpnOugV7HY"
                JSONObject getPlaceId=jsonArray.getJSONObject(i);
                if(getPlaceId.has("place_id")) {
                    placeId=getPlaceId.getString("place_id");
                }else {
                    placeId = "no data";
                }

                //set the price level from 0(Free)-4(Very Expensive) , example: "2"
                JSONObject getPriceLevel=jsonArray.getJSONObject(i);
                if(getPriceLevel.has("price_level")) {
                    priceLevel=getPriceLevel.getString("price_level");
                }
                else{priceLevel="no data";
                }

                //set the rating from 1.0-5.0 Stars , example: "4.3"
                JSONObject getRating=jsonArray.getJSONObject(i);
                if(getPriceLevel.has("rating")) {
                    rating=getRating.getString("rating");
                }
                else{rating="no data";
                }

                res[i][0]=businessStatus;
                res[i][1]=name;
                res[i][2]=address;
                res[i][3]=lat;
                res[i][4]=lng;
                res[i][5]=photo;
                res[i][6]=placeId;
                res[i][7]=openNow;
                res[i][8]=priceLevel;
                res[i][9]=rating;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            //ToDo: fehler zur oberen klasse werfen, damit app nicht abstürzt, wenn daten leer sind
        }
    }

    //pass the values of the query as an array to external classes
    public static String[][] getRes(){
        return res;
    }

    //receive the data of the website in the background
    @Override
    protected String doInBackground(Object... objects) {

        try{
            url=(String) objects[0];
            DownloadUrl downloadUrl=new DownloadUrl();
            googleNearByPlacesData= downloadUrl.receiveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            //ToDo: fehler zur oberen klasse werfen, damit app nicht abstürzt, wenn daten leer sind
        }
        return googleNearByPlacesData;
    }
}