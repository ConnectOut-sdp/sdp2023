package com.sdpteam.connectout.profileList;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profileList.filter.ProfileFilter;
import com.sdpteam.connectout.profileList.filter.ProfileNameFilter;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class FilteredProfileListFragment extends Fragment {

    private final ProfileFilter baseFilter;
    private final Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private ProfilesViewModel viewModel;

    public FilteredProfileListFragment() {
        this(ProfileFilter.NONE);
    }

    public FilteredProfileListFragment(ProfileFilter baseFilter) {
        this.baseFilter = baseFilter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_filter, container, false);

        viewModel = new ProfilesViewModel(new ProfileFirebaseDataSource());
        viewModel.setFilter(baseFilter);

        final EditText searchFilter = contentView.findViewById(R.id.text_filter);

        searchRunnable = () -> {
            final ProfileNameFilter nameFilter = new ProfileNameFilter(searchFilter.getText().toString());
            viewModel.setFilter(baseFilter.and(nameFilter));
            viewModel.refreshProfiles();
        };

        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchHandler.removeCallbacks(searchRunnable);
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });

        final ProfileListFragment fragment = new ProfileListFragment(viewModel);
        getChildFragmentManager().beginTransaction().replace(R.id.filter_container, fragment).commit();

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshProfiles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchHandler.removeCallbacks(searchRunnable);
    }
}