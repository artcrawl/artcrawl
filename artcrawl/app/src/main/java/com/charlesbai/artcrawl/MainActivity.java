package com.charlesbai.artcrawl;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.MalformedURLException;
import org.json.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import android.content.Intent;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.view.*;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends ActionBarActivity {

    private ImageView mImageView;
    private ImageButton mImageButton;
    private TextView mTextView;
    boolean isLiked;
    public long firstClick = 0;
    public long secondClick = 0;
    public int noOfLikes = 999;

//    private ListView mPictureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Find the reference to the ImageView
        mImageView = (ImageView) findViewById(R.id.image);
        mImageButton = (ImageButton) findViewById(R.id.likebutton);
        mTextView = (TextView) findViewById(R.id.likeView);
        isLiked = false;



//        mPictureList = (ListView) findViewById(R.id.picture_list);
//        mPictureList.setFastScrollEnabled(true);
//        mPictureList.setFastScrollAlwaysVisible(true);
//        mPictureList.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        String http = "http://www.hubnest.com/artcrawlbackend/artcrawl.php?cmd=listposts&locality=toronto";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(http, new JsonHttpResponseHandler() {
            //PictureList adapter;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
//                    Log.d("this", response.toString());
                    final JSONArray results = response.getJSONArray("data");

                    int[] artworkIds = new int [results.length()];
                    int[] createDates = new int[results.length()];
                    String[] userIds  = new String[results.length()];
                    int[] prices = new int[results.length()];
                    int[] likess = new int[results.length()];
                    double [] geolats = new double[results.length()];
                    double [] geolongs  = new double[results.length()];
                    String[] images = new String[results.length()];
                    int[] onSales = new int[results.length()];
                    String[] titles  = new String[results.length()];
                    String[] localitys = new String[results.length()];

                    for(int i = 0; i < results.length(); i++) {
                        artworkIds[i]  = results.getJSONObject(i).getInt("artworkid");
                        createDates[i] = results.getJSONObject(i).getInt("createdate");
                        userIds[i] = results.getJSONObject(i).getString("userid");
                        prices[i]  = results.getJSONObject(i).getInt("price");
                        likess[i] = results.getJSONObject(i).getInt("likes");
                        geolats[i] = results.getJSONObject(i).getDouble("geolat");
                        geolongs[i] = results.getJSONObject(i).getDouble("geolong");
                        images[i] = results.getJSONObject(i).getString("image");
                        onSales[i] = results.getJSONObject(i).getInt("onsale");
                        titles[i] = results.getJSONObject(i).getString("title");
                        localitys[i] = results.getJSONObject(i).getString("locality");
                    }

//                    adapter = new PictureList(MainActivity.this, artworkIds, createDates, userIds, prices
//                        likess, geolats, geolongs, images, onSales, titles, localitys);
//                    mPictureList.setAdapter(adapter);
//                    mPictureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
////                            final String seed = seeds[p];
////                            Log.i(TAG, "Selected contact " + seed + " at " + p);
//
//                            // Load Single Contact Activity
//                            Intent intent = new Intent(MainActivity.this, SinglePictureActivity.class);
//                            intent.putExtra(EXTRA_CONTACT_SEED, seed);
//                            startActivity(intent);
//                        }
//                    });


                    //noOfLikes = results.getJSONObject(0).getInt("likes");
                    noOfLikes = likess[0];
                    new DownloadImage().execute(images[0]);
                    updateData();
                    Log.d("test", Integer.toString(noOfLikes));

                } catch (JSONException e) {
                    Log.e("error", "Failed to parse JSON. " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("error", "Failed to load pictures. " + throwable.toString());
            }

        });

        mImageView.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(android.view.View view) {
                // TODO Auto-generated method stub
                Date i = new Date();
                firstClick = secondClick;
                secondClick = i.getTime();


                if (secondClick - firstClick <= 500) {
                    if (isLiked == false) {
                        mImageButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_filled));
                        isLiked = true;
//                        noOfLikes++;
//                        updateData();
                    }
                }
            }
        });

        mImageButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (isLiked == false) {
                    mImageButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_filled));
                    isLiked = true;
//                    noOfLikes ++;
//                    updateData();
                } else {
                    mImageButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_unfilled));
                    isLiked = false;
//                    noOfLikes --;
//                    updateData();
                }
            }
        });



        // You can set a temporary background here
        //image.setImageResource(null);

        // Start the DownloadImage task with the given url
//        new DownloadImage().execute("http://wallpaperswa.com/thumbnails/detail/20120415/paintings%20colorful%20leonid%20afremov%20artwork" +
//                "%201920x1080%20wallpaper_wallpaperswa.com_17.jpg");
    }

    /**
     * Simple functin to set a Drawable to the image View
     *
     * @param //drawable
     */
    private void setImage(Drawable drawable) {
        mImageView.setBackgroundDrawable(drawable);
    }

    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

        @Override
        protected Drawable doInBackground(String... arg0) {
            // This is done in a background thread
            return downloadImage(arg0[0]);
        }

        /**
         * Called after the image has been downloaded
         * -> this calls a function on the main thread again
         */
        protected void onPostExecute(Drawable image) {
            setImage(image);
        }


        /**
         * Actually download the Image from the _url
         *
         * @param _url
         * @return
         */
        private Drawable downloadImage(String _url) {
            //Prepare to download image
            URL url;
            BufferedOutputStream out;
            InputStream in;
            BufferedInputStream buf;

            //BufferedInputStream buf;
            try {
                url = new URL(_url);
                in = url.openStream();

            /*
             * THIS IS NOT NEEDED
             *
             * YOU TRY TO CREATE AN ACTUAL IMAGE HERE, BY WRITING
             * TO A NEW FILE
             * YOU ONLY NEED TO READ THE INPUTSTREAM
             * AND CONVERT THAT TO A BITMAP
            out = new BufferedOutputStream(new FileOutputStream("testImage.jpg"));
            int i;

             while ((i = in.read()) != -1) {
                 out.write(i);
             }
             out.close();
             in.close();
             */

                // Read the inputstream
                buf = new BufferedInputStream(in);

                // Convert the BufferedInputStream to a Bitmap
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }

                return new BitmapDrawable(bMap);

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }

            return null;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateData(){
        if (noOfLikes != 1) {
            mTextView.setText(noOfLikes + " likes");
        } else {
            mTextView.setText("1 like");
        }
    }
}

