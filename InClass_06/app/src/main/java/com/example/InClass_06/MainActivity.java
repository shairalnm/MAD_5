package com.example.InClass_06;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GetJSONData.test
{
    TextView tvKeyword,tvTitle,tvDescription,tvPublishedAt,tvPageCount;
    Button btnGo;
    ProgressBar progressBar;
    ImageView ivPictures, ivPrev, ivNext;
    String[] category= null;
    String values=null;
    ArrayList<article> imageURLs;
    int currentPosition=0,size=0;
    String  title,description,urlToImage, publishedAt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        tvKeyword = (TextView) findViewById(R.id.tvKeyword);
        tvTitle = (TextView)findViewById(R.id.textViewTitle);
        tvPublishedAt = (TextView)findViewById(R.id.textViewPublishedAt);
        tvDescription  = (TextView)findViewById(R.id.textViewDescription);
        tvPageCount = (TextView)findViewById(R.id.textViewPageCount);
        btnGo = (Button) findViewById(R.id.btnGo);
        ivPictures = (ImageView) findViewById(R.id.ivPictures);
        ivPrev = (ImageView) findViewById(R.id.ivPrev);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        progressBar = (ProgressBar) findViewById(R.id.progressImages);

        category = new String[]{"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};


        System.out.println(category);

        if (isConnected())
        {
            Toast.makeText(MainActivity.this, "Internet Access", Toast.LENGTH_SHORT).show();
            btnGo.setEnabled(true);

        }
        else
        {
            Toast.makeText(MainActivity.this, "No internet Connection.", Toast.LENGTH_SHORT).show();
        }
        ivPrev.setEnabled(false);
        ivNext.setEnabled(false);
        tvPublishedAt.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        tvDescription.setVisibility(View.INVISIBLE);
        tvPageCount.setVisibility(View.INVISIBLE);

        setEventHandlers();
    }

    private void setEventHandlers()
    {
        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPrev.setEnabled(true);
                ivNext.setEnabled(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Category");
                builder.setItems(category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //Log.d("demo","onclick"+ category[i]);

                        values = (String) category[i];
                        tvKeyword.setText(category[i]);
                        //Log.d("demo", values);
                        new GetJSONData(MainActivity.this).execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=35a9ff5a9be141b3b2ea73621bd226b9&category="+values);

                    }
                });
                builder.create().show();

            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Log.d("demo","prev clicked");
                if (currentPosition == 0)
                {
                    currentPosition = (imageURLs.size()-1);
                    //Log.d("demo", "onClick: prev 0 ");
                }
                else
                {

                    currentPosition = currentPosition - 1;
                    //Log.d("demo","current position in prev "+currentPosition);
                }

                progressBar.setVisibility(View.VISIBLE);
                tvTitle.setText(imageURLs.get(currentPosition).title);
                tvDescription.setText(imageURLs.get(currentPosition).description);
                tvPublishedAt.setText(imageURLs.get(currentPosition).publishedAt);
                tvPageCount.setText((currentPosition+1)+" out of "+imageURLs.size());
                new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition).urlToImage);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("demo","next clicked");
                //Log.d("demo","current position in next"+currentPosition);
                if (currentPosition<(size-1))
                {
                    currentPosition = currentPosition + 1;
                    //Log.d("demo","current position in next  "+currentPosition);
                }
                else if (currentPosition == (size-1))
                {
                    currentPosition = 0;
                }

                progressBar.setVisibility(View.VISIBLE);
                tvTitle.setText(imageURLs.get(currentPosition).title);
                tvDescription.setText(imageURLs.get(currentPosition).description);
                tvPublishedAt.setText(imageURLs.get(currentPosition).publishedAt);
                tvPageCount.setText((currentPosition+1)+" out of "+imageURLs.size());
                new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition).urlToImage);

            }
        });
    }
    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() &&
                (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

@Override
    public void handledata(ArrayList<article> random)
    {
            imageURLs =new ArrayList<article>();
            imageURLs =random;

            size = imageURLs.size();
            //Log.d("demo", "Size of array list"+String.valueOf(size));
            //Log.d("demo","Handle data in main activity");
            if (imageURLs.size() == 0)
            {
                ivPrev.setEnabled(false);
                ivNext.setEnabled(false);
                tvTitle.setVisibility(View.INVISIBLE);
                tvPublishedAt.setVisibility(View.INVISIBLE);
                tvDescription.setVisibility(View.INVISIBLE);
                tvPageCount.setVisibility(View.INVISIBLE);
                ivPictures.setImageBitmap(null);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "No images found.", Toast.LENGTH_SHORT).show();
            }
            else if (random.size() == 1)
            {
                ivPrev.setEnabled(false);
                ivNext.setEnabled(false);
                tvTitle.setVisibility(View.VISIBLE);
                tvPublishedAt.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                tvPageCount.setVisibility(View.VISIBLE);

                title =(imageURLs.get(currentPosition).title);
                Log.d("demo","Handle data in main activity (Title)"+title);
                description = imageURLs.get(currentPosition).description;
                Log.d("demo","Handle data in main activity (description)"+description);
                publishedAt = imageURLs.get(currentPosition).publishedAt;
                Log.d("demo","Handle data in main activity (publishedat)"+publishedAt);
                urlToImage = imageURLs.get(currentPosition).urlToImage;
                Log.d("demo","Handle data in main activity (url to image)"+urlToImage);

                tvTitle.setText(imageURLs.get(currentPosition).title);
                tvDescription.setText(imageURLs.get(currentPosition).description);
                tvPublishedAt.setText(imageURLs.get(currentPosition).publishedAt);
                tvPageCount.setText((currentPosition+1)+" out of "+imageURLs.size());
                new GetImages(MainActivity.this).execute(urlToImage);
            }
            else
                {
                ivPrev.setEnabled(true);
                ivNext.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvPublishedAt.setVisibility(View.VISIBLE);
                    tvDescription.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    tvPageCount.setVisibility(View.VISIBLE);
                    title =(imageURLs.get(currentPosition).title);
                    description = imageURLs.get(currentPosition).description;
                    publishedAt = imageURLs.get(currentPosition).publishedAt;
                    urlToImage = imageURLs.get(currentPosition).urlToImage;
                    tvPageCount.setText((currentPosition+1)+" out of "+imageURLs.size());
                    tvTitle.setText(imageURLs.get(currentPosition).title);
                    tvDescription.setText(imageURLs.get(currentPosition).description);
                    tvPublishedAt.setText(imageURLs.get(currentPosition).publishedAt);
                new GetImages(MainActivity.this).execute(urlToImage);

            }
        }
}




