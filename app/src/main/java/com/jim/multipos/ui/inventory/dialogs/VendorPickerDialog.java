package com.jim.multipos.ui.inventory.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.adapters.VendorPickerAdapter;
import com.jim.multipos.ui.inventory.model.VendorPickerItem;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog.VendorSortingStates.SORTED_BY_CONTACT;
import static com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog.VendorSortingStates.SORTED_BY_CONTACT_INVERT;
import static com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog.VendorSortingStates.SORTED_BY_NAME;
import static com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog.VendorSortingStates.SORTED_BY_NAME_INVERT;
import static com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog.VendorSortingStates.SORTED_BY_TYPE;
import static com.jim.multipos.ui.inventory.dialogs.VendorPickerDialog.VendorSortingStates.SORTED_BY_TYPE_INVERT;

public class VendorPickerDialog extends Dialog {

    private List<Vendor> vendorList;
    private List<Vendor> vendorsWithProduct;
    private List<VendorPickerItem> searchResults;
    private List<VendorPickerItem> items;
    private OnVendorSelectListener listener;
    private VendorPickerAdapter adapter;
    @BindView(R.id.svVendorSearch)
    MpSearchView svVendorSearch;
    @BindView(R.id.rvVendorList)
    RecyclerView rvVendorList;
    @BindView(R.id.llVendorName)
    LinearLayout llVendorName;
    @BindView(R.id.llContact)
    LinearLayout llContact;
    @BindView(R.id.llSupplyStatus)
    LinearLayout llSupplyStatus;
    @BindView(R.id.ivContactSort)
    ImageView ivContactSort;
    @BindView(R.id.ivVendorNameSort)
    ImageView ivVendorNameSort;
    @BindView(R.id.ivSupplySort)
    ImageView ivSupplySort;
    private VendorSortingStates filterMode = SORTED_BY_TYPE_INVERT;

    public enum VendorSortingStates {
        SORTED_BY_NAME, SORTED_BY_NAME_INVERT, SORTED_BY_CONTACT, SORTED_BY_CONTACT_INVERT, SORTED_BY_TYPE, SORTED_BY_TYPE_INVERT
    }

    public VendorPickerDialog(@NonNull Context context, List<Vendor> vendorList, List<Vendor> vendorsWithProduct) {
        super(context);
        this.vendorList = vendorList;
        this.vendorsWithProduct = vendorsWithProduct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_picker_dialog, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        items = new ArrayList<>();
        if (vendorList == null) {
            for (Vendor vendor : vendorsWithProduct) {
                VendorPickerItem item = new VendorPickerItem();
                item.setVendor(vendor);
                item.setWasSupplied(true);
                items.add(item);
            }
        } else {
            for (Vendor vendor : vendorList) {
                VendorPickerItem item = new VendorPickerItem();
                item.setVendor(vendor);
                item.setWasSupplied(vendorsWithProduct.contains(vendor));
                items.add(item);
            }
        }

        rvVendorList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VendorPickerAdapter(getContext());
        adapter.setData(items);
        adapter.setCallback(vendor -> {
            listener.onSelect(vendor);
            dismiss();
        });
        rvVendorList.setAdapter(adapter);

        sortList();
        svVendorSearch.getSearchView().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                String searchText = svVendorSearch.getSearchView().getText().toString();
                if (searchText.isEmpty()) {
                    searchResults = null;
                    sortList();
                    adapter.setData(items);
                } else {
                    searchResults = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getVendor().getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(items.get(i));
                            continue;
                        }
                        if (items.get(i).getVendor().getContactName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(items.get(i));
                        }
                    }
                    sortList();
                    adapter.setSearchResult(searchResults, searchText);
                }
            }
        });


        llVendorName.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_NAME) {
                filterMode = SORTED_BY_NAME;
                sortList();
                ivVendorNameSort.setVisibility(View.VISIBLE);
                ivVendorNameSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_NAME_INVERT;
                ivVendorNameSort.setVisibility(View.VISIBLE);
                ivVendorNameSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        llContact.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_CONTACT) {
                filterMode = SORTED_BY_CONTACT;
                sortList();
                ivContactSort.setVisibility(View.VISIBLE);
                ivContactSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_CONTACT_INVERT;
                ivContactSort.setVisibility(View.VISIBLE);
                ivContactSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        llSupplyStatus.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_TYPE) {
                filterMode = SORTED_BY_TYPE;
                sortList();
                ivSupplySort.setVisibility(View.VISIBLE);
                ivSupplySort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_TYPE_INVERT;
                ivSupplySort.setVisibility(View.VISIBLE);
                ivSupplySort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

    }

    private void deselectAll() {
        ivSupplySort.setVisibility(View.GONE);
        ivContactSort.setVisibility(View.GONE);
        ivVendorNameSort.setVisibility(View.GONE);
    }

    private void sortList() {
        List<VendorPickerItem> tempList;
        if (searchResults != null)
            tempList = searchResults;
        else tempList = items;
        switch (filterMode) {
            case SORTED_BY_CONTACT:
                Collections.sort(tempList, (item, t1) -> item.getVendor().getContactName().toUpperCase().compareTo(t1.getVendor().getContactName().toUpperCase()));
                break;
            case SORTED_BY_NAME:
                Collections.sort(tempList, (item, t1) -> item.getVendor().getName().toUpperCase().compareTo(t1.getVendor().getName().toUpperCase()));
                break;
            case SORTED_BY_TYPE:
                Collections.sort(tempList, (item, t1) -> item.isWasSupplied().compareTo(t1.isWasSupplied()));
                break;
            case SORTED_BY_CONTACT_INVERT:
                Collections.sort(tempList, (item, t1) -> item.getVendor().getContactName().toUpperCase().compareTo(t1.getVendor().getContactName().toUpperCase()) * -1);
                break;
            case SORTED_BY_NAME_INVERT:
                Collections.sort(tempList, (item, t1) -> item.getVendor().getName().toUpperCase().compareTo(t1.getVendor().getName().toUpperCase()) * -1);
                break;
            case SORTED_BY_TYPE_INVERT:
                Collections.sort(tempList, (item, t1) -> item.isWasSupplied().compareTo(t1.isWasSupplied()) * -1);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void setListener(OnVendorSelectListener listener) {
        this.listener = listener;
    }

    public interface OnVendorSelectListener {
        void onSelect(Vendor vendor);
    }
}
