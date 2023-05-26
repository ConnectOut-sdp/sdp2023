package com.sdpteam.connectout.drawer;

import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * Fragments of a drawer.
 */
public abstract class DrawerFragment extends Fragment {

    /**
     * Make sure that if the fragment is self contained in a drawer to use its toolbar and button.
     * Otherwise, uses its own.
     * Useful for fragments that are used in drawers and independent activity containers.
     *
     * @param fragmentButton  (Button): button of the drawer fragment
     * @param fragmentToolbar (ToolBar): Toolbar of the fragment
     * @param text            (String): text to setup on the button
     * @param listener        (View.OnClickListener): action to apply the button is clicked
     */
    public void setupToolBar(Button fragmentButton, Toolbar fragmentToolbar, String text, View.OnClickListener listener) {
        if (getActivity() instanceof DrawerActivity) {
            ((DrawerActivity) getActivity()).setupButton(text, listener);

            fragmentButton.setVisibility(View.GONE);
            fragmentToolbar.setVisibility(View.GONE);
        } else {
            ((WithFragmentActivity) getActivity()).setSupportActionBar(fragmentToolbar);

            fragmentButton.setOnClickListener(listener);
            fragmentButton.setText(text);
            fragmentToolbar.setNavigationOnClickListener(v -> getActivity().finish());
        }
    }

    public void setupToolBar(Toolbar fragmentToolbar) {
        setupToolBar(new Button(null), fragmentToolbar, "", null);
    }
}

