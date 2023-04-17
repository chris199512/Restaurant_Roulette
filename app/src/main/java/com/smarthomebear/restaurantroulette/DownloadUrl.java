package com.smarthomebear.restaurantroulette;
/*
This is a Java class named "DownloadUrl" that contains a method named "receiveUrl" which takes a string parameter "url" representing the URL of a web resource to be downloaded. The method uses the HTTPURLConnection class to connect to the specified URL and retrieve the data as an input stream.

The input stream is then read using a BufferedReader to convert it into a string. The resulting string is returned by the method.

The method throws an IOException in case there is an error while attempting to connect to the specified URL or reading from the input stream. The exception is caught and logged using the Android Log.d() method.

Finally, the input stream and connection are closed in a finally block to ensure that they are properly closed regardless of any exceptions that may have occurred.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {

    public String receiveUrl(String url) throws IOException {
        String urlData="";
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;

        //try to establish connection
        try {
            URL getUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) getUrl.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                sb.append(line);
            }

            urlData = sb.toString();
            bufferedReader.close();
        }
        //throw error if no valid values are received
        catch (Exception e){
            Log.d("Exception", e.toString());
            //ToDo: Bei fehler in die obere klasse werfen, damit app nicht abstürzt.
        }
        //close the connection
        finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return urlData;
    }
}
