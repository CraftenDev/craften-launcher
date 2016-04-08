package de.craften.craftenlauncher.logic.json;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONConnector {
    private static final Logger LOGGER = LogManager.getLogger(JSONConnector.class);

    public static String executePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            byte[] bytes = urlParameters.getBytes("UTF-8");
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(bytes.length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            LOGGER.debug(connection.getURL());
            //Send request
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(bytes);
            }

            //Get Response
            try (InputStream is = connection.getInputStream();
                 BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                return response.toString();
            }
        } catch (Exception e) {
            LOGGER.error("AuthSer->executePost error: " + e.getMessage(), e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
