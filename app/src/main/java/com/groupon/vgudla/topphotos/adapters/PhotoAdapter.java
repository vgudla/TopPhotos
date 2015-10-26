package com.groupon.vgudla.topphotos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupon.vgudla.topphotos.CommentsDialog;
import com.groupon.vgudla.topphotos.dto.Comment;
import com.groupon.vgudla.topphotos.dto.Photo;
import com.groupon.vgudla.topphotos.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class PhotoAdapter extends ArrayAdapter<Photo> {
    private static final String ABBR_YEAR = "y";
    private static final String ABBR_WEEK = "w";
    private static final String ABBR_DAY = "d";
    private static final String ABBR_HOUR = "h";
    private static final String ABBR_MINUTE = "m";

    public PhotoAdapter(Context context, List<Photo> photos) {
        super(context, 0, photos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvViewAll);
        TextView tvTimeElapsed = (TextView) convertView.findViewById(R.id.tvTimeElapsed);
        ImageView lvImage = (ImageView) convertView.findViewById(R.id.imageView);
        ImageView userImage = (ImageView) convertView.findViewById(R.id.idProfileView);
        LinearLayout comments = (LinearLayout) convertView.findViewById(R.id.lvComments);

        tvUserName.setText(photo.getUserName());
        tvLikesCount.setText(photo.getLikes() + " likes");
        tvTimeElapsed.setText(getAbbreviatedTimeSpan(1000 * Long.parseLong(photo.getCreatedTime())));

        //Displaying at most 2 comments
        final List<Comment> commentList = photo.getCommentsList();
        final String mediaId = photo.getId();
        if (commentList.size() > 2) {
            tvComments.setText("view all " + photo.getCommentCount() + " comments");
            tvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentActivity activity = (FragmentActivity)PhotoAdapter.this.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    CommentsDialog commentsDialog = CommentsDialog.newInstance(mediaId);
                    commentsDialog.show(fragmentManager, "fragment_edit_name");
                }
            });
        }

        int numCommentsToDisplay = Math.min(2, commentList.size());
        comments.removeAllViews();
        for (int i = 0; i < numCommentsToDisplay; i++) {
            Comment comment = commentList.get(i);
            View line = comments.inflate(getContext(), R.layout.item_comment, null);
            TextView tvCommentUserName = (TextView) line.findViewById(R.id.tvCommentUserName);
            TextView tvCommentText = (TextView) line.findViewById(R.id.tvCommentText);
            tvCommentUserName.setText(comment.getUserName());
            tvCommentText.setText(comment.getCommentText());
            comments.addView(line);
        }

        //Loading main image
        lvImage.setImageResource(0);
        Picasso.with(getContext())
                .load(photo.getImageUrl())
                .placeholder(R.drawable.clock)
                .into(lvImage);

        //Loading user profile image
        userImage.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .oval(false)
                .borderWidthDp(0)
                .cornerRadiusDp(100)
                .build();

        Picasso.with(getContext())
                .load(photo.getUserProfileUrl())
                .transform(transformation)
                .into(userImage);

        return convertView;
    }

    public String getAbbreviatedTimeSpan(long timeMillis) {
        long span = Math.max(System.currentTimeMillis() - timeMillis, 0);
        if (span >= DateUtils.YEAR_IN_MILLIS) {
            return (span / DateUtils.YEAR_IN_MILLIS) + ABBR_YEAR;
        }
        if (span >= DateUtils.WEEK_IN_MILLIS) {
            return (span / DateUtils.WEEK_IN_MILLIS) + ABBR_WEEK;
        }
        if (span >= DateUtils.DAY_IN_MILLIS) {
            return (span / DateUtils.DAY_IN_MILLIS) + ABBR_DAY;
        }
        if (span >= DateUtils.HOUR_IN_MILLIS) {
            return (span / DateUtils.HOUR_IN_MILLIS) + ABBR_HOUR;
        }
        return (span / DateUtils.MINUTE_IN_MILLIS) + ABBR_MINUTE;
    }
}
