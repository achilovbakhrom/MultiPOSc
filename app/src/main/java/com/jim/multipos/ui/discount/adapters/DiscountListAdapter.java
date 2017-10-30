package com.jim.multipos.ui.discount.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenterImpl;
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
    String[] discountUsedType;
    String[] discountUsedTypeAbbr;
    String[] discountAmountType;
    String[] discountAmountTypeAbbr;

    DiscountAddingPresenterImpl.DiscountSortTypes currentDiscountSortTypes;
    public enum DiscountAdapterItemTypes{AddDiscount};

    public DiscountListAdapter(Context context) {
        this.context = context;
        discountUsedType = context.getResources().getStringArray(R.array.discount_used_types);
        discountUsedTypeAbbr = context.getResources().getStringArray(R.array.discount_used_types_abr);
        discountAmountType = context.getResources().getStringArray(R.array.discount_amount_types);
        discountAmountTypeAbbr = context.getResources().getStringArray(R.array.discount_amount_types_abr);

    }
    public void setListners(OnDiscountCallback onDiscountCallback){
        this.onDiscountCallback = onDiscountCallback;
    }
    public void setData(List<Object> items){
        this.items = items;
    }
    public interface OnDiscountCallback{
        void onAddPressed(double amount,String amountTypeAbbr, String discription, String usedTypeAbbr, boolean active);
        void onSave(double amount,String amountTypeAbbr, String discription, String usedTypeAbbr, boolean active, Discount discount);
        void onDelete(Discount discount);
        void sortList(DiscountAddingPresenterImpl.DiscountSortTypes discountSortTypes);
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
            AddDiscountViewHolder holder = (AddDiscountViewHolder) holder1;
            holder.etAmmount.setText("");
            holder.etName.setText("");
            holder.chbActive.setChecked(true);
            holder.spTypeAmount.setSelectedPosition(0);
            holder.spUsed.setSelectedPosition(0);
        }else if(holder1 instanceof ItemDiscountViewHolder){
            Log.d("recycc", "onBindViewHolder: "+position);
            ItemDiscountViewHolder holder = (ItemDiscountViewHolder) holder1;
            Discount discount = (Discount) items.get(position);

            holder.etName.setText(discount.getDiscription());
            holder.etAmmount.setText(String.valueOf(discount.getAmount()));
            holder.chbActive.setChecked(discount.getActive());
            int a = 0, b = 0;
            if(discount.getAmountType().equals(discountAmountTypeAbbr[0])){
                holder.spTypeAmount.setSelectedPosition(0);
                a = 0;
            }else if(discount.getAmountType().equals(discountAmountTypeAbbr[1])){
                holder.spTypeAmount.setSelectedPosition(1);
                a = 1;
            }
            if(discount.getUsedType().equals(discountUsedTypeAbbr[0])){
                holder.spUsed.setSelectedPosition(0);
                b = 0;
            }else if(discount.getUsedType().equals(discountUsedTypeAbbr[1])){
                holder.spUsed.setSelectedPosition(1);
                b = 1;
            }

            holder.spUsed.setItemSelectionListenerWithPos((view, position2) -> {
                holder.btnSave.enable();
            },b);
            holder.spTypeAmount.setItemSelectionListenerWithPos((view, position2) -> {
                holder.btnSave.enable();
            },a);
            holder.btnSave.disable();

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
        @NotEmpty(messageId = R.string.discount_amount_empty)
        @BindView(R.id.etAmmount)
        MpEditText etAmmount;
        @BindView(R.id.etName)
        MpEditText etName;
        @BindView(R.id.spTypeAmount)
        MPosSpinner spTypeAmount;
        @BindView(R.id.spUsed)
        MPosSpinner spUsed;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;


        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvReason)
        TextView tvReason;
        @BindView(R.id.tvUsed)
        TextView tvUsed;
        @BindView(R.id.tvActive)
        TextView tvActive;

        public AddDiscountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //TODO FROM RESURS
            spTypeAmount.setArrowTint(Color.parseColor("#d3d3d3"));
            spUsed.setArrowTint(Color.parseColor("#d3d3d3"));
            spTypeAmount.setAdapter(discountAmountType);
            spUsed.setAdapter(discountUsedType);

            tvActive.setOnClickListener(view -> {
                if(currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Active){
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));

                }else {
                    onDiscountCallback.sortList(DiscountAddingPresenterImpl.DiscountSortTypes.Active);
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Active;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                    tvActive.setTypeface(tvActive.getTypeface(), Typeface.BOLD);

                }
            });
            tvAmount.setOnClickListener(view -> {
                if(currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Ammount){
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                }else {
                    onDiscountCallback.sortList(DiscountAddingPresenterImpl.DiscountSortTypes.Ammount);
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Ammount;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(tvAmount.getTypeface(), Typeface.BOLD);

                }
            });
            tvReason.setOnClickListener(view -> {
                if(currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Discription){
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));

                }else {
                    onDiscountCallback.sortList(DiscountAddingPresenterImpl.DiscountSortTypes.Discription);
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Discription;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(tvReason.getTypeface(), Typeface.BOLD);

                }
            });
            tvType.setOnClickListener(view -> {
                if(currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Type){
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                }else {
                    onDiscountCallback.sortList(DiscountAddingPresenterImpl.DiscountSortTypes.Type);
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Type;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(tvType.getTypeface(), Typeface.BOLD);

                }
            });
            tvUsed.setOnClickListener(view -> {
                if(currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Used){
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(Typeface.create(tvUsed.getTypeface(), Typeface.NORMAL));
                }else {
                    onDiscountCallback.sortList(DiscountAddingPresenterImpl.DiscountSortTypes.Used);
                    currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Used;
                    onDiscountCallback.sortList(currentDiscountSortTypes);
                    tvActive.setTypeface(Typeface.create(tvActive.getTypeface(), Typeface.NORMAL));
                    tvAmount.setTypeface(Typeface.create(tvAmount.getTypeface(), Typeface.NORMAL));
                    tvReason.setTypeface(Typeface.create(tvReason.getTypeface(), Typeface.NORMAL));
                    tvType.setTypeface(Typeface.create(tvType.getTypeface(), Typeface.NORMAL));
                    tvUsed.setTypeface(tvUsed.getTypeface(), Typeface.BOLD);
                }
            });

            spTypeAmount.setItemSelectionListener((view, position) -> {
                etAmmount.requestFocus();
            });
            btnAdd.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())){
                    if(spTypeAmount.getSelectedPosition()==0){
                        double percent = 0;
                        try{
                            percent = Double.parseDouble(etAmmount.getText().toString());
                        }catch (Exception e){
                            etAmmount.setError(context.getString(R.string.invalid));
                            return;
                        }

                        if(percent>100) {
                            etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                            return;
                        }
                        onDiscountCallback.onAddPressed(percent,discountAmountTypeAbbr[spTypeAmount.getSelectedPosition()],etName.getText().toString(),discountUsedTypeAbbr[spUsed.getSelectedPosition()],chbActive.isChecked());
                    }else {
                        double percent = 0;
                        try{
                            percent = Double.parseDouble(etAmmount.getText().toString());
                        }catch (Exception e){
                            etAmmount.setError(context.getString(R.string.invalid));
                            return;
                        }
                        onDiscountCallback.onAddPressed(percent,discountAmountTypeAbbr[spTypeAmount.getSelectedPosition()],etName.getText().toString(),discountUsedTypeAbbr[spUsed.getSelectedPosition()],chbActive.isChecked());
                    }
                }
            });
        }

    }

    public class ItemDiscountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etAmmount)
        @NotEmpty(messageId = R.string.discount_amount_empty)
        MpEditText etAmmount;
        @BindView(R.id.etName)
        MpEditText etName;
        @BindView(R.id.spTypeAmount)
        MPosSpinner spTypeAmount;
        @BindView(R.id.spUsed)
        MPosSpinner spUsed;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnDelete)
        MpMiniActionButton btnDelete;

        public ItemDiscountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            spTypeAmount.setArrowTint(Color.parseColor("#d3d3d3"));
            spUsed.setArrowTint(Color.parseColor("#d3d3d3"));
            spTypeAmount.setAdapter(discountAmountType);
            spUsed.setAdapter(discountUsedType);
            Log.d("recycc", "ItemDiscountViewHolder: "+getAdapterPosition());

            chbActive.setCheckedChangeListener(isChecked -> {
                btnSave.enable();
            });
            etName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    btnSave.enable();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etAmmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    btnSave.enable();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            btnSave.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())){
                    if(spTypeAmount.getSelectedPosition()==0){
                        double percent = 0;
                        try{
                            percent = Double.parseDouble(etAmmount.getText().toString());
                        }catch (Exception e){
                            etAmmount.setError(context.getString(R.string.invalid));
                            return;
                        }

                        if(percent>100) {
                            etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                            return;
                        }
                        UIUtils.closeKeyboard(etName,context);

                        onDiscountCallback.onSave(percent,discountAmountTypeAbbr[spTypeAmount.getSelectedPosition()],etName.getText().toString(),discountUsedTypeAbbr[spUsed.getSelectedPosition()],chbActive.isChecked(),(Discount)items.get(getAdapterPosition()));
                    }else {
                        double percent = 0;
                        try{
                            percent = Double.parseDouble(etAmmount.getText().toString());
                        }catch (Exception e){
                            etAmmount.setError(context.getString(R.string.invalid));
                            return;
                        }
                        UIUtils.closeKeyboard(etName,context);

                        onDiscountCallback.onSave(percent,discountAmountTypeAbbr[spTypeAmount.getSelectedPosition()],etName.getText().toString(),discountUsedTypeAbbr[spUsed.getSelectedPosition()],chbActive.isChecked(),(Discount)items.get(getAdapterPosition()));

                    }
                }
            });
            btnDelete.setOnClickListener(view -> {
                UIUtils.closeKeyboard(etName,context);
                WarningDialog warningDialog = new WarningDialog(context);
                String type = "";
                Discount discount = (Discount) items.get(getAdapterPosition());
                if(discount.getAmountType().equals(discountAmountTypeAbbr[0])){
                    type = "%";
                }
                warningDialog.setWarningText(context.getString(R.string.do_you_want_delete_item)+" "+String.valueOf(discount.getAmount())+type+" "+discount.getDiscription()+"?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onDiscountCallback.onDelete(discount);
                    warningDialog.dismiss();
                });
                warningDialog.show();
            });

        }

    }

}
