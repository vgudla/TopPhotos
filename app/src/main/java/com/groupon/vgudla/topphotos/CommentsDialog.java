package com.groupon.vgudla.topphotos;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.groupon.vgudla.topphotos.adapters.CommentAdapter;
import com.groupon.vgudla.topphotos.dto.Comment;

import java.util.Arrays;
import java.util.List;

public class CommentsDialog extends DialogFragment {
    private ListView commentListView;
    private static final String COMMENTS_LABEL = "comments";

    public CommentsDialog() {}

    public static CommentsDialog newInstance(List<Comment> commentList) {
        CommentsDialog commentsDialog = new CommentsDialog();
        Bundle args = new Bundle();
        args.putSerializable(COMMENTS_LABEL, commentList.toArray(new Comment[commentList.size()]));
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
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentListView = (ListView) view.findViewById(R.id.lvCommentList);
        List<Comment> comments = Arrays.asList((Comment[])getArguments().getSerializable(COMMENTS_LABEL));
        CommentAdapter commentAdapter = new CommentAdapter(getActivity(), 0, comments);
        commentListView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }

}
