package com.jim.multipos.ui.product_class_new.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class_new.model.ProductsClassAdapterDetials;
import com.jim.multipos.utils.TextWatcherOnTextChange;
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
    private List<ProductsClassAdapterDetials> items;
    private OnProductClassCallback onProductClassCallback;
    private Context context;
    public enum ProductClassItemTypes{AddClass,SubAddClass,ProductClass,SubProductClass};

    public ProductsClassListAdapter(Context context) {
        this.context = context;
    }
    public void setListners(OnProductClassCallback onProductClassCallback){
        this.onProductClassCallback = onProductClassCallback;
    }
    public void setData(List<ProductsClassAdapterDetials> items){
        this.items = items;
    }
    public interface OnProductClassCallback{
        void onAddPressed(String name, boolean active);
        void onAddSubPressed(String name, boolean active, ProductClass parent);
        void onSave(String name, boolean active,ProductClass productClass);
        void onDelete(ProductClass productClass);
        boolean nameIsUnique(String checkName,ProductClass currentProductClass);
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
            ((AddClassViewHolder) holder1).etName.setText(items.get(position).getActualName());
            ((AddClassViewHolder) holder1).chbActive.setChecked(items.get(position).getActualActive());
        }else if(holder1 instanceof ItemClassViewHolder){
            ItemClassViewHolder holder = (ItemClassViewHolder) holder1;
            ProductClass productClass;
            if(items.get(position).isChanged()) {
                productClass = items.get(position).getChangedObject();
                holder.btnSave.enable();
            }
            else {
                productClass = items.get(position).getObject();
                holder.btnSave.disable();
            }
            holder.etName.setText(productClass.getName());
            holder.chbActive.setChecked(productClass.getActive());
            if(!productClass.getActive()){
                holder.etName.setAlpha(0.5f);
            }else holder.etName.setAlpha(1f);
        }else if(holder1 instanceof ItemSubClassViewHolder){
            ItemSubClassViewHolder holder = (ItemSubClassViewHolder) holder1;
            ProductClass subProductClass;
            if(items.get(position).isChanged()) {
                subProductClass = items.get(position).getChangedObject();
                holder.btnSave.enable();
            }
            else {
                subProductClass = items.get(position).getObject();
                holder.btnSave.disable();
            }

            holder.etName.setText(subProductClass.getName());
            holder.chbActive.setChecked(subProductClass.getActive());
            holder.btnSave.disable();
            if(!subProductClass.getActive()){
                holder.etName.setAlpha(0.5f);
                holder.ivArrow.setAlpha(0.5f);
            }else{
                holder.etName.setAlpha(1f);
                holder.ivArrow.setAlpha(1f);
            }

        }else if(holder1 instanceof AddSubClassViewHolder){
            ((AddSubClassViewHolder) holder1).etName.setText(items.get(position).getActualName());
            ((AddSubClassViewHolder) holder1).chbActive.setChecked(items.get(position).getActualActive());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getType() == ProductClassItemTypes.AddClass){
            return ADD_CLASS;
        }else if(items.get(position).getType() == ProductClassItemTypes.SubAddClass){
            return SUB_ADD_CLASS;
        }else  if(items.get(position).getType() == ProductClassItemTypes.ProductClass){
            return CLASS_ITEM;
        }else return SUB_CLASS_ITEM;
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
            etName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    items.get(getAdapterPosition()).setNewName(etName.getText().toString());
                }
            });
            chbActive.setCheckedChangeListener(isChecked -> {
                items.get(getAdapterPosition()).setNewActive(isChecked);
            });
            btnAdd.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())){
                    if(onProductClassCallback.nameIsUnique(items.get(getAdapterPosition()).getActualName(),null)) {
                        onProductClassCallback.onAddPressed(items.get(getAdapterPosition()).getActualName(), items.get(getAdapterPosition()).getActualActive());
                        items.get(getAdapterPosition()).setChangedObject(null);
                        items.get(getAdapterPosition()).contanierMode();
                        etName.setText("");
                        chbActive.setChecked(true);
                        etName.requestFocus();
                        UIUtils.closeKeyboard(etName, context);
                    }else {
                        etName.setError(context.getString(R.string.product_class_name_unique));
                    }
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
        View mainview;
        public ItemClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainview = itemView;
            chbActive.setCheckedChangeListener(isChecked -> {
                if(items.get(getAdapterPosition()).setNewActive(isChecked))
                    btnSave.enable();
            });
            etName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(items.get(getAdapterPosition()).setNewName(etName.getText().toString()))
                        btnSave.enable();
                }
            });
            btnSave.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())) {
                    if(onProductClassCallback.nameIsUnique(items.get(getAdapterPosition()).getActualName(),items.get(getAdapterPosition()).getObject())) {
                        onProductClassCallback.onSave(items.get(getAdapterPosition()).getActualName(), items.get(getAdapterPosition()).getActualActive(), items.get(getAdapterPosition()).getObject());
                    }else {
                        etName.setError(context.getString(R.string.product_class_name_unique));
                    }
                }
            });
            btnDelete.setOnClickListener(view -> {
                ProductClass productClass = items.get(getAdapterPosition()).getObject();
                if(productClass.getActive()){
                    WarningDialog warningDialog = new WarningDialog(context);
                    warningDialog.onlyText(true);
                    warningDialog.setWarningMessage(context.getString(R.string.change_to_not_delete_when_not_active));
                    warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
                    warningDialog.show();
                    return;
                }
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningMessage(context.getString(R.string.do_you_want_delete_item)+" "+ productClass.getName()+"?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onProductClassCallback.onDelete(items.get(getAdapterPosition()).getObject());
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
        @BindView(R.id.ivArrow)
        ImageView ivArrow;
        View mainview;
        public ItemSubClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainview = itemView;
            chbActive.setCheckedChangeListener(isChecked -> {
                if(items.get(getAdapterPosition()).setNewActive(isChecked))
                btnSave.enable();
            });
            etName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(items.get(getAdapterPosition()).setNewName(etName.getText().toString()));
                    btnSave.enable();

                }
            });
            btnSave.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())) {
                    if(onProductClassCallback.nameIsUnique(items.get(getAdapterPosition()).getActualName(),items.get(getAdapterPosition()).getObject())){
                        onProductClassCallback.onSave(items.get(getAdapterPosition()).getActualName(),items.get(getAdapterPosition()).getActualActive(), items.get(getAdapterPosition()).getObject());
                        UIUtils.closeKeyboard(etName,context);
                    }else {
                        etName.setError(context.getString(R.string.product_class_name_unique));
                    }
                }
            });
            btnDelete.setOnClickListener(view -> {
                ProductClass productClass = items.get(getAdapterPosition()).getObject();
                if(productClass.getActive()){
                    WarningDialog warningDialog = new WarningDialog(context);
                    warningDialog.onlyText(true);
                    warningDialog.setWarningMessage(context.getString(R.string.change_to_not_delete_when_not_active));
                    warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
                    warningDialog.show();
                    return;
                }
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningMessage(context.getString(R.string.do_you_want_delete_item)+" "+productClass.getName()+"?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onProductClassCallback.onDelete(items.get(getAdapterPosition()).getObject());
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
            etName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    items.get(getAdapterPosition()).setNewName(etName.getText().toString());
                }
            });
            chbActive.setCheckedChangeListener(isChecked -> {
                items.get(getAdapterPosition()).setNewActive(isChecked);
            });
            btnAdd.setOnClickListener(view -> {
                if(FormValidator.validate(context,this, new MultipleCallback())){
                    int adapterPosition = getAdapterPosition();
                    for (int i = adapterPosition;i>=0;i--)
                        if(items.get(i).getType() ==  ProductClassItemTypes.ProductClass){
                                ProductClass productClass =  items.get(i).getObject();
                                if(onProductClassCallback.nameIsUnique(items.get(getAdapterPosition()).getActualName(),null)){
                                onProductClassCallback.onAddSubPressed(items.get(getAdapterPosition()).getActualName(),items.get(getAdapterPosition()).getActualActive(),productClass);
                                    items.get(getAdapterPosition()).setChangedObject(null);
                                    items.get(getAdapterPosition()).contanierMode();
                                    etName.setText("");
                                    chbActive.setChecked(true);
                                }else {
                                    etName.setError(context.getString(R.string.product_class_name_unique));
                                }
//                                UIUtils.closeKeyboard(etName,context);

                                break;
                        }
                }
            });
        }

    }


}
