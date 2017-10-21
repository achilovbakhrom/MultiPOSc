package com.jim.multipos.ui.first_configure_last.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure_last.adapter.FirstConfigureListAdapter;
import com.jim.multipos.ui.first_configure_last.adapter.FirstConfigureListItem;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;

/**
 * Created by Achilov Bakhrom on 10/17/17.
 */

public class FirstConfigureListFragment extends BaseFragment implements ClickableBaseAdapter.OnItemClickListener<FirstConfigureListItem>{

    @BindView(R.id.rvSettings)
    RecyclerView list;

    private FirstConfigureListAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.start_configuration_fragment;
    }

    /**
     * initialization method
     * called int onCreateView method
     * @param savedInstanceState - inherited from the onCreateView method
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FirstConfigureListAdapter(getContext(),
                ((FirstConfigureActivity) getContext()).getPresenter().getFirstConfigureListItems());
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    /**
     * excluding dagger 2
     * @return if false, then AndroidInjection will not work,
     * otherwise must be contributed in the module
     */
    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    /**
     * Listens clicking on the item of
     * recycler view
     * @param position - clicked position
     */
    @Override
    public void onItemClicked(int position) {
        UIUtils.closeKeyboard(list, getContext());
        ((FirstConfigureActivity) getContext()).getPresenter().leftMenuItemClicked(position);
    }

    /**
     * Listens clicking on the item of
     * recycler view
     * @param item - clicked item object
     */
    @Override
    public void onItemClicked(FirstConfigureListItem item) {}

    /**
     * the method will change state of item, according to
     * how to filled the appropriate fragment
     * @param position - changing item position
     * @param state - changing item state in the position
     */
    public void changeStateOfItem(int position, int state) {
        ((FirstConfigureListAdapter) list.getAdapter()).changeState(position, state);
    }

    /**
     * changes selected position
     * @param position - new position, that must be selected
     */
    public void changePosition(int position) {
        ((FirstConfigureListAdapter) list.getAdapter()).changePosition(position);
    }

}