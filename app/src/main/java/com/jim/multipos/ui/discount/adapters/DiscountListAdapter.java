package com.jim.multipos.ui.discount.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.discount.model.DiscountApaterDetials;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenterImpl;
import com.jim.multipos.utils.NumberTextWatcher;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.validator.MultipleCallback;

import java.text.DecimalFormat;
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
    private DiscountApaterDetials addingState;
    private List<DiscountApaterDetials> items;
    private OnDiscountCallback onDiscountCallback;
    private Context context;
    String[] discountUsedType;
    String[] discountAmountType;
    DecimalFormat decimalFormat;
    DiscountAddingPresenterImpl.DiscountSortTypes currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default;

    public DiscountListAdapter(Context context) {
        this.context = context;
        decimalFormat = BaseAppModule.getFormatterWithoutGroupingFourDecimal();
        discountUsedType = context.getResources().getStringArray(R.array.discount_used_types);
        discountAmountType = context.getResources().getStringArray(R.array.discount_amount_types);
        addingState = new DiscountApaterDetials();
    }

    public void setListners(OnDiscountCallback onDiscountCallback) {
        this.onDiscountCallback = onDiscountCallback;
    }

    public void setData(List<DiscountApaterDetials> items) {
        this.items = items;
        Discount discount = new Discount();
        discount.setActive(true);
        discount.setAmount(0);
        discount.setName("");
        discount.setAmountType(Discount.PERCENT);
        discount.setUsedType(Discount.ITEM);
        addingState.setObject(discount);
    }

    public interface OnDiscountCallback {
        void onAddPressed(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active);

        void onSave(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active, Discount discount);

        void onDelete(Discount discount);

        void sortList(DiscountAddingPresenterImpl.DiscountSortTypes discountSortTypes);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_DISCOUNT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_discont_item, parent, false);
            return new AddDiscountViewHolder(view);
        } else if (viewType == DISCOUNT_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discont_item, parent, false);
            return new ItemDiscountViewHolder(view);
        } else return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if (holder1 instanceof AddDiscountViewHolder) {
            AddDiscountViewHolder holder = (AddDiscountViewHolder) holder1;
            if (!addingState.isChanged()) {
                holder.etAmmount.setText("");
                holder.etName.setText("");
                holder.chbActive.setChecked(true);
                holder.spTypeAmount.setSelectedPosition(0);
                holder.spUsed.setSelectedPosition(0);
            } else {
                holder.etAmmount.setText((addingState.getChangedObject().getAmount() == 0) ? "" : decimalFormat.format(addingState.getChangedObject().getAmount()));
                holder.etName.setText((addingState.getChangedObject().getName() == null) ? "" : addingState.getChangedObject().getName());
                holder.chbActive.setChecked(addingState.getChangedObject().getActive());
                int a = 0;
                if (Discount.VALUE == addingState.getChangedObject().getAmountType()) {
                    a = 1;
                }
                holder.spTypeAmount.setSelectedPosition(a);
                a = 0;
                if (Discount.ORDER == addingState.getChangedObject().getUsedType()) {
                    a = 1;
                } else if (Discount.ALL == (addingState.getChangedObject().getUsedType())) {
                    a = 2;
                }
                holder.spUsed.setSelectedPosition(a);
            }

            holder.tvActive.setTypeface(Typeface.create(holder.tvActive.getTypeface(), Typeface.NORMAL));
            holder.tvAmount.setTypeface(Typeface.create(holder.tvAmount.getTypeface(), Typeface.NORMAL));
            holder.tvReason.setTypeface(Typeface.create(holder.tvReason.getTypeface(), Typeface.NORMAL));
            holder.tvType.setTypeface(Typeface.create(holder.tvType.getTypeface(), Typeface.NORMAL));
            holder.tvUsed.setTypeface(Typeface.create(holder.tvUsed.getTypeface(), Typeface.NORMAL));
            switch (currentDiscountSortTypes) {
                case Active:
                    holder.tvActive.setTypeface(Typeface.create(holder.tvActive.getTypeface(), Typeface.BOLD));
                    break;
                case Ammount:
                    holder.tvAmount.setTypeface(Typeface.create(holder.tvAmount.getTypeface(), Typeface.BOLD));
                    break;
                case Discription:
                    holder.tvReason.setTypeface(Typeface.create(holder.tvReason.getTypeface(), Typeface.BOLD));
                    break;
                case Used:
                    holder.tvUsed.setTypeface(Typeface.create(holder.tvUsed.getTypeface(), Typeface.BOLD));
                    break;
                case Type:
                    holder.tvType.setTypeface(Typeface.create(holder.tvType.getTypeface(), Typeface.BOLD));
                    break;
                case Default:
                    break;
            }

        } else if (holder1 instanceof ItemDiscountViewHolder) {
            ItemDiscountViewHolder holder = (ItemDiscountViewHolder) holder1;
            Discount discount = null;
            if (items.get(position).isChanged()) {
                discount = items.get(position).getChangedObject();
                holder.btnSave.enable();
            } else {
                discount = items.get(position).getObject();
                holder.btnSave.disable();

            }

            holder.etName.setText(discount.getName());
            holder.etAmmount.setText(decimalFormat.format(discount.getAmount()));
            holder.chbActive.setChecked(discount.getActive());
            if (!discount.getActive()) {
                holder.etName.setAlpha(0.5f);
                holder.spTypeAmount.setAlpha(0.5f);
                holder.spUsed.setAlpha(0.5f);
                holder.etAmmount.setAlpha(0.5f);
            } else {
                holder.etName.setAlpha(1);
                holder.spTypeAmount.setAlpha(1);
                holder.spUsed.setAlpha(1);
                holder.etAmmount.setAlpha(1);
            }
            if (discount.getAmountType() == Discount.PERCENT) {
                holder.spTypeAmount.setSelectedPosition(0);
            } else if (discount.getAmountType() == Discount.VALUE) {
                holder.spTypeAmount.setSelectedPosition(1);
            }

            if (discount.getUsedType() == Discount.ITEM) {
                holder.spUsed.setSelectedPosition(0);
            } else if (discount.getUsedType() == Discount.ORDER) {
                holder.spUsed.setSelectedPosition(1);
            } else if (discount.getUsedType() == Discount.ALL)
                holder.spUsed.setSelectedPosition(2);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return ADD_DISCOUNT;
        } else return DISCOUNT_ITEM;
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
            spTypeAmount.setArrowTint(R.color.colorDiscount);
            spUsed.setArrowTint(R.color.colorDiscount);
            spTypeAmount.setAdapter(discountAmountType);
            spUsed.setAdapter(discountUsedType);
            etAmmount.addTextChangedListener(new NumberTextWatcher(etAmmount));
            tvActive.setOnClickListener(view -> {
                if (currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Active) {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Active);
                }
            });
            tvAmount.setOnClickListener(view -> {
                if (currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Ammount) {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Ammount);
                }
            });
            tvReason.setOnClickListener(view -> {
                if (currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Discription) {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Discription);
                }
            });
            tvType.setOnClickListener(view -> {
                if (currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Type) {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Type);
                }
            });
            tvUsed.setOnClickListener(view -> {
                if (currentDiscountSortTypes == DiscountAddingPresenterImpl.DiscountSortTypes.Used) {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentDiscountSortTypes = DiscountAddingPresenterImpl.DiscountSortTypes.Used);
                }
            });

            spTypeAmount.setItemSelectionListener((view, position) -> {

                addingState.setNewAmmountType(position);
                if (addingState.getActualyAmmountType() == Discount.PERCENT) {
                    if (addingState.getActualyAmmount() > 100) {
                        etAmmount.setError(context.getString(R.string.valid_entry_value_100));
                        return;
                    }
                } else etAmmount.setError(null);
            });
            spUsed.setItemSelectionListener((view, position) -> {
                addingState.setNewUsedType(position);
            });
            etName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addingState.setNewDiscription(etName.getText().toString());
                }
            });
            chbActive.setCheckedChangeListener(isChecked -> {
                addingState.setNewActive(isChecked);
            });
            etAmmount.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0)
                        if (addingState.getActualyAmmountType() == Discount.PERCENT) {
                            double percent = 0;
                            try {
                                percent = decimalFormat.parse(etAmmount.getText().toString().replace(',', '.')).doubleValue();
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            addingState.setNewAmmount(percent);
                            if (percent > 100) {
                                etAmmount.setError(context.getString(R.string.valid_entry_value_100));
                                return;
                            }
                        } else {
                            double percent = 0;
                            try {
                                percent = decimalFormat.parse(etAmmount.getText().toString().replace(',', '.')).doubleValue();
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            addingState.setNewAmmount(percent);
                        }
                }
            });

            btnAdd.setOnClickListener(view -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (addingState.getActualyAmmountType() == Discount.PERCENT) {
                        if (addingState.getActualyAmmount() > 100) {
                            etAmmount.setError(context.getString(R.string.valid_entry_value_100));
                            return;
                        }
                    } else etAmmount.setError(null);
                    if (addingState.getActualDiscription().equals("")) {
                        etName.setError(context.getString(R.string.discount_cant_empty));
                        return;
                    }
                    onDiscountCallback.onAddPressed(addingState.getActualyAmmount(), addingState.getActualyAmmountType(), addingState.getActualDiscription(), addingState.getActualyUsedType(), addingState.getActualyActive());
                    addingState.setChangedObject(null);
                    notifyItemChanged(0);
                    etName.requestFocus();
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
            spTypeAmount.setArrowTint(R.color.colorDiscount);
            spUsed.setArrowTint(R.color.colorDiscount);
            spTypeAmount.setAdapter(discountAmountType);
            spUsed.setAdapter(discountUsedType);
            Log.d("recycc", "ItemServiceFeeViewHolder: " + getAdapterPosition());
            spUsed.setItemSelectionListenerWithPos((view, position2) -> {
                if (items.get(getAdapterPosition()).setNewUsedType(position2)) {

                    btnSave.enable();
                }
            });
            etAmmount.addTextChangedListener(new NumberTextWatcher(etAmmount));
            spTypeAmount.setItemSelectionListenerWithPos((view, position2) -> {
                if (items.get(getAdapterPosition()).setNewAmmountType(position2)) {
                    if (items.get(getAdapterPosition()).getActualyAmmountType() == Discount.VALUE) {
                        if (items.get(getAdapterPosition()).getActualyAmmount() > 100) {
                            etAmmount.setError(context.getString(R.string.valid_entry_value_100));
                            return;
                        }
                    } else etAmmount.setError(null);
                    btnSave.enable();
                }
            });
            chbActive.setCheckedChangeListener(isChecked -> {
                if (items.get(getAdapterPosition()).setNewActive(isChecked))
                    btnSave.enable();
            });
            etName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (items.get(getAdapterPosition()).setNewDiscription(etName.getText().toString()))
                        btnSave.enable();
                }
            });
            etAmmount.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0)
                        if (items.get(getAdapterPosition()).getActualyAmmountType() == Discount.PERCENT) {
                            double percent = 0;
                            try {
                                percent = decimalFormat.parse(etAmmount.getText().toString().replace(',', '.')).doubleValue();
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            if (items.get(getAdapterPosition()).setNewAmmount(percent))
                                btnSave.enable();
                            if (percent > 100) {
                                etAmmount.setError(context.getString(R.string.valid_entry_value_100));
                                return;
                            } else etAmmount.setError(null);
                        } else {
                            double percent = 0;
                            try {
                                percent = decimalFormat.parse(etAmmount.getText().toString().replace(',', '.')).doubleValue();
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            if (items.get(getAdapterPosition()).setNewAmmount(percent))
                                btnSave.enable();
                            etAmmount.setError(null);
                        }
                }
            });

            btnSave.setOnClickListener(view -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (items.get(getAdapterPosition()).getActualyAmmountType() == Discount.PERCENT) {
                        if (items.get(getAdapterPosition()).getActualyAmmount() > 100) {
                            etAmmount.setError(context.getString(R.string.valid_entry_value_100));
                            return;
                        }
                    } else etAmmount.setError(null);
                    if (items.get(getAdapterPosition()).getActualDiscription().equals("")) {
                        etName.setError(context.getString(R.string.discount_cant_empty));
                        return;
                    }
                    onDiscountCallback.onSave(items.get(getAdapterPosition()).getActualyAmmount(), items.get(getAdapterPosition()).getActualyAmmountType(), items.get(getAdapterPosition()).getActualDiscription(), items.get(getAdapterPosition()).getActualyUsedType(), items.get(getAdapterPosition()).getActualyActive(), items.get(getAdapterPosition()).getObject());
                    UIUtils.closeKeyboard(etName, context);
                }
            });

            btnDelete.setOnClickListener(view -> {
                UIUtils.closeKeyboard(etName, context);
                Discount discount = items.get(getAdapterPosition()).getObject();
                if (discount.getActive()) {
                    WarningDialog warningDialog = new WarningDialog(context);
                    warningDialog.onlyText(true);
                    warningDialog.setWarningMessage(context.getString(R.string.change_to_not_delete_when_not_active));
                    warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
                    warningDialog.show();
                    return;
                }
                WarningDialog warningDialog = new WarningDialog(context);
                String type = "";
                if (discount.getAmountType() == Discount.PERCENT) {
                    type = "%";
                }
                warningDialog.setWarningMessage(context.getString(R.string.do_you_want_delete_item) + discount.getName() + " " + decimalFormat.format(discount.getAmount()) + type + " " + "?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onDiscountCallback.onDelete(discount);
                    UIUtils.closeKeyboard(etName, context);
                    warningDialog.dismiss();
                });
                warningDialog.show();
            });

        }

    }


}
