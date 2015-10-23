package com.groupon.vgudla.topphotos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.groupon.vgudla.topphotos.R;
import com.groupon.vgudla.topphotos.dto.Comment;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {
    public CommentAdapter(Context context, int resource, List<Comment> comments) {
        super(context, resource, comments);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }
        TextView userName = (TextView) convertView.findViewById(R.id.tvCommentUserName);
        TextView userComment = (TextView) convertView.findViewById(R.id.tvCommentText);
        userName.setText(comment.getUserName());
        userComment.setText(comment.getCommentText());

        return convertView;
    }
}
