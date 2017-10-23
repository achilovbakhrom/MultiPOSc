package com.jim.multipos.ui.discount.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.validator.MultipleCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by DEV on 17.08.2017.
 */

public class DiscountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADD_DISCOUNT = 0;
    private static final int DISCOUNT_ITEM = 1;

    private List<Object> items;
    private OnDiscountCallback onDiscountCallback;
    private Context context;

    public enum DiscountAdapterItemTypes{AddDiscount};

    public DiscountListAdapter(Context context) {

        this.context = context;
    }
    public void setListners(OnDiscountCallback onDiscountCallback){
        this.onDiscountCallback = onDiscountCallback;
    }
    public void setData(List<Object> items){
        this.items = items;
    }
    public interface OnDiscountCallback{
        void onAddPressed(String name, boolean active);
        void onSave(String name, boolean active, ProductClass productClass);
        void onDelete(ProductClass productClass);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_DISCOUNT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_discont_item, parent, false);
            return new AddDiscountViewHolder(view);
        } else if(viewType==DISCOUNT_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discont_item, parent, false);
            return new ItemDiscountViewHolder(view);
        }else return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if(holder1 instanceof AddDiscountViewHolder){

        }else if(holder1 instanceof ItemDiscountViewHolder){

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position) == DiscountAdapterItemTypes.AddDiscount){
            return ADD_DISCOUNT;
        }else return DISCOUNT_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class AddDiscountViewHolder extends RecyclerView.ViewHolder {


        public AddDiscountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    public class ItemDiscountViewHolder extends RecyclerView.ViewHolder {


        public ItemDiscountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }


}
