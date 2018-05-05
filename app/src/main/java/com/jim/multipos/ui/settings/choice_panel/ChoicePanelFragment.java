package com.jim.multipos.ui.settings.choice_panel;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.ui.settings.choice_panel.adapter.ChoicePanelAdapter;
import com.jim.multipos.ui.settings.choice_panel.data.PanelData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChoicePanelFragment extends BaseFragment implements ChoicePanelView {

    @BindView(R.id.rvPanel)
    RecyclerView rvPanel;
    ChoicePanelAdapter choicePanelAdapter;
    List<PanelData> panelData;

    @Override
    protected int getLayout() {
        return R.layout.settings_choice_panel_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String[] title = getResources().getStringArray(R.array.settings_titles);
        String[] subTitle = getResources().getStringArray(R.array.settings_sub_titles);
        panelData = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            panelData.add(new PanelData(title[i],subTitle[i]));
        }
        choicePanelAdapter = new ChoicePanelAdapter(panelData,clickedPosition -> {
            ((SettingsActivity)getActivity()).onPanelClicked(clickedPosition);
        });
        ((SimpleItemAnimator) rvPanel.getItemAnimator()).setSupportsChangeAnimations(false);

        rvPanel.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPanel.setAdapter(choicePanelAdapter);
    }
}
