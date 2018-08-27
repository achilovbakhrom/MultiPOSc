package com.jim.multipos.ui.admin_main_page.fragments.establishment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter.EstablishmentAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.adapter.EstablishmentPosAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.EstablishmentPos;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.AddMode;
import com.jim.multipos.utils.rxevents.admin_main_page.ItemEditClick;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class EstablishmentFragment extends BaseFragment implements EstablishmentAdapter.OnItemClick, EstablishmentPosAdapter.OnEstablishmentPosItemClick {

    @BindView(R.id.rvEstablishment)
    RecyclerView rvEstablishment;
    @BindView(R.id.rvEstablishmentPos)
    RecyclerView rvEstablishmentPos;


    @OnClick(R.id.fabEstablishment)
    public void onFabClick(View view){
        if(getActivity() instanceof AdminMainPageActivity && getActivity()!=null) {
            bus.send(new AddMode(1));
            ((AdminMainPageActivity) getActivity()).openEditor();
        }
    }

    List<Establishment> items;
    List<EstablishmentPos> posItems;
    EstablishmentAdapter establishmentAdapter;

    @Inject
    RxBus bus;

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

        establishmentAdapter = new EstablishmentAdapter(items, this, getContext());
        rvEstablishment.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEstablishment.setAdapter(establishmentAdapter);
        setUpPosRV();
    }


    public void isEditorOpen(boolean isOpen) {
        if(isOpen){
            rvEstablishmentPos.setLayoutManager(new LinearLayoutManager(getContext()));
        }else rvEstablishmentPos.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    void setUpPosRV(){
        posItems = new ArrayList<>();
        posItems.add(new EstablishmentPos("POS 1", "Lorem", "asda"));
        posItems.add(new EstablishmentPos("POS 2", "Lorem", "asda"));
        posItems.add(new EstablishmentPos("POS 3", "Lorem", "asda"));
        rvEstablishmentPos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvEstablishmentPos.setAdapter(new EstablishmentPosAdapter(posItems, this));
        bus.send(items.get(0));
    }

    @Override
    public void onClick(Establishment establishment) {
        rvEstablishmentPos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEstablishmentPos.setAdapter(new EstablishmentPosAdapter(posItems, this));
        bus.send(establishment);
        if(getActivity() instanceof AdminMainPageActivity)
            ((AdminMainPageActivity) getActivity()).openEditor();

    }

    @Override
    public void onClick(EstablishmentPos pos) {
        bus.send(pos);
        if(getActivity() instanceof AdminMainPageActivity)
            ((AdminMainPageActivity) getActivity()).openEditor();
    }
}
