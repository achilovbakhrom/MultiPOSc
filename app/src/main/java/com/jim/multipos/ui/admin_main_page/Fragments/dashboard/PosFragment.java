package com.jim.multipos.ui.admin_main_page.fragments.dashboard;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.adapter.PosAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.model.Pos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PosFragment extends BaseFragment {

    @BindView(R.id.tvPosCount)
    TextView posCount;
    @BindView(R.id.rvPos)
    RecyclerView rvPos;

    List<Pos> items;

    @Override
    protected int getLayout() {
        return R.layout.admin_dashboard_pos_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        items = new ArrayList<>();
        items.add(new Pos("1", "34500", "50000", "140"));
        items.add(new Pos("2", "34500", "50000", "140"));
        items.add(new Pos("3", "34500", "50000", "140"));
        items.add(new Pos("4", "34500", "50000", "140"));
        rvPos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPos.setAdapter(new PosAdapter(items));
        rvPos.addItemDecoration(new DividerItemDecoration(rvPos.getContext(), DividerItemDecoration.VERTICAL));
    }
}
