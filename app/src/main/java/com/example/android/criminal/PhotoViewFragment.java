package com.example.android.criminal;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Administrator on 2016/3/19.
 */
public class PhotoViewFragment extends DialogFragment {
    private static final String PHOTOFILE = "Photo file";

    public static PhotoViewFragment newInstance(File photoFile){
        Bundle args = new Bundle();
        args.putSerializable(PHOTOFILE, photoFile);
        PhotoViewFragment photoViewFragment = new PhotoViewFragment();
        photoViewFragment.setArguments(args);
        return photoViewFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        File mPhotoFile = (File)getArguments().getSerializable(PHOTOFILE);
        Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.photo_view_fragment, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.photo_view_fragment_image_view);
        imageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity()).setView(imageView).create();

    }
}
