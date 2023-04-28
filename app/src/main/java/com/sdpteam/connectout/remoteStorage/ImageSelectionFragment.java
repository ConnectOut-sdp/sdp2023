package com.sdpteam.connectout.remoteStorage;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;

public class ImageSelectionFragment extends Fragment {

    private ImageView preview;
    private OnImageSelectedListener mListener;

    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_selection, container, false);

        preview = view.findViewById(R.id.preview_image_view);
        Button chooseButton = view.findViewById(R.id.choose_image_button);

        chooseButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mImageUri = data.getData();
            preview.setImageURI(mImageUri);

            if (mListener != null) {
                mListener.onImageSelected(mImageUri);
            }
        }
    }

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        mListener = listener;
    }

    public interface OnImageSelectedListener {
        void onImageSelected(Uri imageUri);
    }
}