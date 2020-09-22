package com.azapps.newsapp.utils;

import android.text.TextUtils;
import android.util.Log;

import com.azapps.newsapp.data.Article;

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
import java.util.List;

public class QueryUtils {
    public static final String TAG = "QueryUtils";

    private QueryUtils() {
    }

    public static List<Article> getArticleData(String urlRequest) {
        URL url = createURL(urlRequest);
        String jsonRespond = null;

        try {
            jsonRespond = makeHTTPRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Article> articleList = extractDataFromJson(jsonRespond);
        return articleList;
    }

    private static URL createURL(String urlRequest) {
        URL url = null;
        try {
            url = new URL(urlRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // can i use try and catch inside ?, yes i know i want who ever implements this method
    // handle exception but can not it be done inside ?

    private static String makeHTTPRequest(URL url) throws IOException {
        Log.e(TAG, "makeHTTPRequest: ");
        String jsonRespond = "";
        if (url == null) return jsonRespond;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                Log.e(TAG, "makeHTTPRequest: if " + urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                jsonRespond = readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHTTPRequest: connection failed " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHTTPRequest: error parsing json respond " + e.toString());
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            inputStream.close();
        }

        return jsonRespond;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String oneLine = bufferedReader.readLine();
            while (oneLine != null) {
                stringBuilder.append(oneLine);
                oneLine = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static List<Article> extractDataFromJson(String jsonRespond) {
        if (TextUtils.isEmpty(jsonRespond)) {
            Log.e(TAG, "extractDataFromJson: null json");
            return null;
        } else {
//            Log.e(TAG, "extractDataFromJson: json respond is ok\n\n"+jsonRespond+"\n\n" );
        }
        List<Article> articleList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonRespond);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentJsonObject = results.getJSONObject(i);
                String webTitle = currentJsonObject.getString("webTitle");
                String sectionName = currentJsonObject.getString("sectionName");
                String author;
                try {
                    author = currentJsonObject.getString("author");
                } catch (JSONException e) {
                    author = null;
                }
                String webPublicationDate = currentJsonObject.getString("webPublicationDate");
                try {
                    webPublicationDate = webPublicationDate.substring(0, 10);
                } catch (Exception e) {
                    Log.e(TAG, "extractDataFromJson: " + e.getMessage());
                }
                String webUrl = currentJsonObject.getString("webUrl");


                articleList.add(new Article(webTitle, sectionName, author, webPublicationDate, webUrl));
            }
        } catch (JSONException e) {
            Log.e(TAG, "extractDataFromJson: error");
            Log.e(TAG, "extractDataFromJson: " + e.getMessage());
        }
        return articleList;
    }
}
