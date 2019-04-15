package com.example.InClass_06;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetJSONData extends AsyncTask<String,Void, ArrayList<article>>
{
    @Override
    protected void onPreExecute()
    {
        //Log.d("demo","in GetJSONData PreExecute");
        currentActivity.progressBar.setVisibility(View.VISIBLE);
    }

    MainActivity currentActivity;

    public GetJSONData (MainActivity currentActivity)
        {
            this.currentActivity = currentActivity;
        }
    @Override
    protected void onPostExecute(ArrayList<article> result)
    {
        //Log.d("demo","in GetJSONData PostExecute");
        currentActivity.progressBar.setVisibility(View.INVISIBLE);
        //Log.d("demo", "Data retrieved:(post execute) " + result.toString());
        currentActivity.handledata(result);
    }

    @Override
    protected ArrayList<article> doInBackground(String... params)
    {
        //Log.d("demo","in GetJSONData do in ");
        HttpURLConnection connection = null;
        ArrayList<article> result = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);
                JSONArray articles = root.getJSONArray("articles");

                for (int i=0;i<articles.length();i++)
                {

                    JSONObject articlesJson = articles.getJSONObject(i);
                    article article = new article();

                    article.title=articlesJson.getString("title");
                    article.description=articlesJson.getString("description");
                    article.urlToImage=articlesJson.getString("urlToImage");
                    article.publishedAt=articlesJson.getString("publishedAt");


                    result.add(article);

                    //Log.d("demo",article.toString());


                }
                if (result.size()==0)
                {
                    Toast.makeText(currentActivity, "No news found.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            //Handle Exceptions
        } finally {
            //Close the connections
        }
        return result;

    }

    public interface test
    {
        public void handledata(ArrayList<article> random);
    }
}
