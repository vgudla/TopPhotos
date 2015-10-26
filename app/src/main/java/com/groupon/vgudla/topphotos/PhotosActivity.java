package com.groupon.vgudla.topphotos;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.groupon.vgudla.topphotos.adapters.PhotoAdapter;
import com.groupon.vgudla.topphotos.dto.Comment;
import com.groupon.vgudla.topphotos.dto.Photo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "1a33427cc3dc488987166e18d4e1ddf6";
    public static final String PHOTOS_URL = "https://api.instagram.com/v1/media/popular?client_id=";
    private List<Photo> photos = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
        photoAdapter = new PhotoAdapter(this, photos);
        ListView listView = (ListView)findViewById(R.id.lvPhotos);
        listView.setAdapter(photoAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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


    public void fetchPopularPhotos() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String photosUrl = PHOTOS_URL + CLIENT_ID;
        asyncHttpClient.get(photosUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonArray;
                photoAdapter.clear();
                try {
                    jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String createdTime = jsonObject.getString("created_time");
                        String id = jsonObject.getString("id");

                        JSONObject user = jsonObject.getJSONObject("user");
                        String userName = user.getString("username");
                        String userProfileUrl = user.getString("profile_picture");

                        JSONObject comments = jsonObject.getJSONObject("comments");
                        JSONArray commentsData = comments.getJSONArray("data");
                        int commentCount = comments.getInt("count");
                        List<Comment> commentList = new ArrayList<>();
                        for (int j = 0; j < commentsData.length(); j++) {
                            JSONObject commentObj = commentsData.getJSONObject(j);
                            String commentText = commentObj.getString("text");
                            JSONObject commentFrom = commentObj.getJSONObject("from");
                            String commentUser = commentFrom.getString("username");
                            Comment comment = new Comment(commentUser, commentText);
                            commentList.add(comment);
                        }

                        String captionText = "";
                        if (jsonObject.optJSONObject("caption") != null) {
                            JSONObject caption = jsonObject.getJSONObject("caption");
                            captionText = caption.getString("text");
                        }

                        JSONObject likes = jsonObject.getJSONObject("likes");
                        int likesCount = likes.getInt("count");

                        JSONObject images = jsonObject.getJSONObject("images");
                        JSONObject stdResolutionImage = images.getJSONObject("standard_resolution");
                        String imageUrl = stdResolutionImage.getString("url");
                        int imageHeight = stdResolutionImage.getInt("height");
                        int imageWidth = stdResolutionImage.getInt("width");
                        Photo photo = new Photo(userName, imageUrl, captionText, imageHeight,
                                likesCount, imageWidth, userProfileUrl, createdTime, commentList,
                                commentCount, id);
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                photoAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                System.out.println(errorResponse);
            }
        });
    }

}
