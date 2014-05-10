package de.craften.craftenlauncher.logic.json;

import de.craften.craftenlauncher.logic.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Saschb2b on 10.05.2014.
 */
public class JSONConnector {
    public static String executePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            byte[] bytes = urlParameters.getBytes("UTF-8");
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(bytes.length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            System.out.println(connection.getURL());
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.write(bytes);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            Logger.getInstance().logError("AuthSer->executePost error: " + e.getMessage());
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
