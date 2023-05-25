package com.sdpteam.connectout.remoteStorage;

import static android.app.Activity.RESULT_OK;

import com.sdpteam.connectout.R;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

public class ImageSelectionFragment extends Fragment {

    private final int PICK_IMAGE_REQUEST = 1;
    private final int previewResourceId;
    private ImageView preview;
    private Button chooseButton;
    private OnImageSelectedListener mListener;
    private String initialImageRemoteUrl;

    public ImageSelectionFragment() {
        this(R.drawable.account_image);
    }

    public ImageSelectionFragment(int previewResourceId) {
        this.previewResourceId = previewResourceId;
    }

    /**
     * @param initialImageRemoteUrl the initial image to show in the preview
     */
    public ImageSelectionFragment(String initialImageRemoteUrl) {
        this.initialImageRemoteUrl = initialImageRemoteUrl;
        this.previewResourceId = R.drawable.account_image;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_selection, container, false);

        preview = view.findViewById(R.id.preview_image_view);
        preview.setImageResource(previewResourceId);

        chooseButton = view.findViewById(R.id.choose_image_button);

        chooseButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        preselectRemoteImageUrl();
        return view;
    }

    private void preselectRemoteImageUrl() {
        if (initialImageRemoteUrl != null && !initialImageRemoteUrl.equals("")) {
            chooseButton.setText("Change selection");
            Picasso.get().load(initialImageRemoteUrl).into(preview);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mImageUri = data.getData();
            preview.setImageURI(mImageUri);
            chooseButton.setText("Change selection");

            if (mListener != null) {
                mListener.onImageSelected(mImageUri);
            }
        }
    }

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        mListener = listener;
    }

    public void performOpenSelection() {
        chooseButton.performClick();
    }

    public void reset() {
        preview.setImageResource(previewResourceId);
        chooseButton.setText("Choose");
    }

    public interface OnImageSelectedListener {
        void onImageSelected(Uri imageUri);
    }
}