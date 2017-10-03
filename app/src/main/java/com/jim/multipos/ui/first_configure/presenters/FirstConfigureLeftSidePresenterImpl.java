package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.first_configure.fragments.FirstConfigureLeftSideFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 31.07.17.
 */

public class FirstConfigureLeftSidePresenterImpl implements FirstConfigureLeftSidePresenter {
    private FirstConfigureLeftSideFragmentView view;
    private Context context;

    public FirstConfigureLeftSidePresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void init(FirstConfigureLeftSideFragmentView view) {
        this.view = view;
    }

    @Override
    public void setAdapterData() {
        String title[] = context.getResources().getStringArray(R.array.start_configuration_title);
        String description[] = context.getResources().getStringArray(R.array.start_configuration_description);
        List<TitleDescription> titleDescriptions = new ArrayList<>();

        for (int i = 0; i < title.length; i++) {
            titleDescriptions.add(new TitleDescription(title[i], description[i]));
        }

        view.showRecyclerView(titleDescriptions);
    }


}
