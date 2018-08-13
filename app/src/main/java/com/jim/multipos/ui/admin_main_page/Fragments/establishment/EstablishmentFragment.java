package com.jim.multipos.ui.admin_main_page.fragments.establishment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter.EstablishmentAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter.EstablishmentPosAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.EstablishmentPos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EstablishmentFragment extends BaseFragment implements EstablishmentAdapter.OnItemClick {

    @BindView(R.id.rvEstablishment)
    RecyclerView rvEstablishment;
    @BindView(R.id.rvEstablishmentPos)
    RecyclerView rvEstablishmentPos;
    @BindView(R.id.tvEstablishmentName)
    TextView tvEstablishmentName;

    List<Establishment> items;
    List<EstablishmentPos> posItems;
    EstablishmentAdapter establishmentAdapter;

    @Override
    protected int getLayout() {
        return R.layout.admin_establishment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        items = new ArrayList<>();
        items.add(new Establishment("Establishment 1", "", "", "", "", "Some text", "7 POS"));
        items.add(new Establishment("Establishment 2", "", "", "", "", "Some text", "7 POS"));
        items.add(new Establishment("Establishment 3", "", "", "", "", "Some text", "7 POS"));
        items.add(new Establishment("Establishment 4", "", "", "", "", "Some text", "7 POS"));

        establishmentAdapter = new EstablishmentAdapter(items);
        establishmentAdapter.setItemClick(this);
        rvEstablishment.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEstablishment.setAdapter(establishmentAdapter);
        onItemClick(0);
    }

    @Override
    public void onItemClick(int pos) {
        Toast.makeText(getContext(), items.get(pos).getName(), Toast.LENGTH_LONG).show();
        posItems = new ArrayList<>();
        posItems.add(new EstablishmentPos("POS 1", "Lorem", "asda"));
        posItems.add(new EstablishmentPos("POS 2", "Lorem", "asda"));
        posItems.add(new EstablishmentPos("POS 3", "Lorem", "asda"));
        rvEstablishmentPos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEstablishmentPos.setAdapter(new EstablishmentPosAdapter(posItems));
    }
}
