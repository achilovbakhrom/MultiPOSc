package com.jim.multipos.ui.product_class_new.adapters;

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

public class ProductsClassListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADD_CLASS = 0;
    private static final int CLASS_ITEM = 1;
    private static final int SUB_CLASS_ITEM = 2;
    private static final int SUB_ADD_CLASS = 3;
    private List<Object> items;
    private OnProductClassCallback onProductClassCallback;
    private Context context;

    public enum ProductClassItemTypes{AddClass,SubAddClass};

    public ProductsClassListAdapter(Context context) {

        this.context = context;
    }
    public void setListners(OnProductClassCallback onProductClassCallback){
        this.onProductClassCallback = onProductClassCallback;
    }
    public void setData(List<Object> items){
        this.items = items;
    }
    public interface OnProductClassCallback{
        void onAddPressed(String name, boolean active);
        void onAddSubPressed(String name, boolean active, ProductClass parent);
        void onSave(String name, boolean active,ProductClass productClass);
        void onDelete(ProductClass productClass);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_CLASS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_class_item, parent, false);
            return new AddClassViewHolder(view);
        } else if(viewType==CLASS_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_class_item, parent, false);
            return new ItemClassViewHolder(view);
        }else if(viewType==SUB_CLASS_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sub_class_item, parent, false);
            return new ItemSubClassViewHolder(view);
        }else if(viewType==SUB_ADD_CLASS){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_product_sub_class_item, parent, false);
            return new AddSubClassViewHolder(view);
        }else return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if(holder1 instanceof AddClassViewHolder){
            ((AddClassViewHolder) holder1).etName.setText("");
            ((AddClassViewHolder) holder1).chbActive.setChecked(true);
        }else if(holder1 instanceof ItemClassViewHolder){
            ItemClassViewHolder holder = (ItemClassViewHolder) holder1;
            ProductClass productClass = (ProductClass) items.get(position);
            holder.etName.setText(productClass.getName());
            holder.chbActive.setChecked(productClass.getActive());
            holder.btnSave.disable();
        }else if(holder1 instanceof ItemSubClassViewHolder){
            ItemSubClassViewHolder holder = (ItemSubClassViewHolder) holder1;
            ProductClass subProductClass = (ProductClass) items.get(position);
            holder.etName.setText(subProductClass.getName());
            holder.chbActive.setChecked(subProductClass.getActive());
            holder.btnSave.disable();
        }else if(holder1 instanceof AddSubClassViewHolder){
            ((AddSubClassViewHolder) holder1).etName.setText("");
            ((AddSubClassViewHolder) holder1).chbActive.setChecked(true);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position) == ProductClassItemTypes.AddClass){
            return ADD_CLASS;
        }else if(items.get(position) instanceof ProductClass){
            ProductClass productClass = (ProductClass) items.get(position);
            if(productClass.getParentId()==null){
                return CLASS_ITEM;
            }else {
                return SUB_CLASS_ITEM;
            }
        }else  if(items.get(position) == ProductClassItemTypes.SubAddClass){
            return SUB_ADD_CLASS;
        }else return ADD_CLASS;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class AddClassViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etName)
        @NotEmpty(messageId = R.string.class_name_empty)
        MpEditText etName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;

        public AddClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnAdd.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())){
                    onProductClassCallback.onAddPressed(etName.getText().toString(),chbActive.isChecked());
                    etName.setText("");
                    chbActive.setChecked(true);
                    UIUtils.closeKeyboard(etName,context);

                }
            });
        }

    }

    public class ItemClassViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etName)
        @NotEmpty(messageId = R.string.class_name_empty)
        MpEditText etName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnDelete)
        MpMiniActionButton btnDelete;

        public ItemClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            btnSave.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())) {
                    onProductClassCallback.onSave(etName.getText().toString(),chbActive.isChecked(),(ProductClass) items.get(getAdapterPosition()));
                }
            });
            btnDelete.setOnClickListener(view -> {
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningText(context.getString(R.string.do_you_want_delete_item)+" "+((ProductClass) items.get(getAdapterPosition())).getName()+"?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onProductClassCallback.onDelete((ProductClass) items.get(getAdapterPosition()));
                    warningDialog.dismiss();

                });
                warningDialog.show();
            });
        }

    }
    public class ItemSubClassViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etName)
        @NotEmpty(messageId = R.string.class_name_empty)
        MpEditText etName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnDelete)
        MpMiniActionButton btnDelete;
        public ItemSubClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            btnSave.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())) {
                    onProductClassCallback.onSave(etName.getText().toString(),chbActive.isChecked(),(ProductClass) items.get(getAdapterPosition()));
                    UIUtils.closeKeyboard(etName,context);
                }
            });
            btnDelete.setOnClickListener(view -> {
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningText(context.getString(R.string.do_you_want_delete_item)+" "+((ProductClass) items.get(getAdapterPosition())).getName()+"?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onProductClassCallback.onDelete((ProductClass) items.get(getAdapterPosition()));
                    UIUtils.closeKeyboard(etName,context);
                    warningDialog.dismiss();
                });
                warningDialog.show();
            });
        }

    }
    public class AddSubClassViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etName)
        @NotEmpty(messageId = R.string.class_name_empty)
        MpEditText etName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;
        public AddSubClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnAdd.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())){
                    int adapterPosition = getAdapterPosition();
                    for (int i = adapterPosition;i>=0;i--)
                        if(items.get(i) instanceof ProductClass){
                            if(((ProductClass) items.get(i)).getParentId() == null){
                                ProductClass productClass = (ProductClass) items.get(i);
                                onProductClassCallback.onAddSubPressed(etName.getText().toString(),chbActive.isChecked(),productClass);

                                etName.setText("");
                                chbActive.setChecked(true);
                                break;
                            }
                        }
                }
            });
        }

    }



}
