package com.groupon.vgudla.topphotos;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.groupon.vgudla.topphotos.adapters.CommentAdapter;
import com.groupon.vgudla.topphotos.dto.Comment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentsDialog extends DialogFragment {
    private ListView commentListView;
    private List<Comment> comments = new ArrayList<>();
    private CommentAdapter commentAdapter;

    private static final String MEDIA_ID = "mediaId";
    private static final String COMMENTS_URL = "https://api.instagram.com/v1/media/%s/comments?client_id=1a33427cc3dc488987166e18d4e1ddf6";

    public CommentsDialog() {}

    public static CommentsDialog newInstance(String mediaId) {
        CommentsDialog commentsDialog = new CommentsDialog();
        Bundle args = new Bundle();
        args.putSerializable(MEDIA_ID, mediaId);
        commentsDialog.setArguments(args);
        return commentsDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_comments, container);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.setTitle("Comments");
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentAdapter = new CommentAdapter(getActivity(), 0, comments);
        commentListView = (ListView) view.findViewById(R.id.lvCommentList);
        String mediaId = getArguments().getString(MEDIA_ID);
        getAllComments(mediaId);
        commentListView.setAdapter(commentAdapter);
    }

    private void getAllComments(String mediaId) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String commentsUrl = String.format(COMMENTS_URL, mediaId);
        asyncHttpClient.get(commentsUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray jsonArray;
                try {
                    jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject commentsData = jsonArray.getJSONObject(i);
                        String commentText = commentsData.getString("text");
                        JSONObject commentFrom = commentsData.getJSONObject("from");
                        String commentUser = commentFrom.getString("username");
                        Comment comment = new Comment(commentUser, commentText);
                        comments.add(comment);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                System.out.println(errorResponse);
            }
        });
    }
}
