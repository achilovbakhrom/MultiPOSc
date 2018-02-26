package com.jim.multipos.ui.service_fee_new.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
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
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.service_fee_new.ServiceFeePresenterImpl;
import com.jim.multipos.ui.service_fee_new.model.ServiceFeeAdapterDetails;
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
 * Created by Portable-Acer on 28.10.2017.
 */

public class ServiceFeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ADD_SERVICE_FEE = 0;
    public static final int SERVICE_FEE_ITEM = 1;
    private ServiceFeeAdapterDetails addingState;
    private List<ServiceFeeAdapterDetails> items;
    private OnDiscountCallback onDiscountCallback;
    private Context context;
    private String[] discountUsedType;
    private String[] discountAmountType;

    ServiceFeePresenterImpl.ServiceFeeSortTypes currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Default;

    public ServiceFeeAdapter(Context context) {
        this.context = context;
        discountUsedType = context.getResources().getStringArray(R.array.discount_used_types);
        discountAmountType = context.getResources().getStringArray(R.array.discount_amount_types);
        addingState = new ServiceFeeAdapterDetails();
    }

    public void setListners(OnDiscountCallback onDiscountCallback) {
        this.onDiscountCallback = onDiscountCallback;
    }

    public void setData(List<ServiceFeeAdapterDetails> items) {
        this.items = items;
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setActive(true);
        serviceFee.setAmount(0);
        serviceFee.setName("");
        serviceFee.setType(ServiceFee.PERCENT);
        serviceFee.setApplyingType(ServiceFee.ITEM);
        addingState.setObject(serviceFee);
    }

    public interface OnDiscountCallback {
        void onAddPressed(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active);
        void onSave(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active, ServiceFee serviceFee);
        void onDelete(ServiceFee serviceFee);
        void sortList(ServiceFeePresenterImpl.ServiceFeeSortTypes ServiceFeeSortTypes);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_SERVICE_FEE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_type_item, parent, false);
            return new AddServiceFeeViewHolder(view);
        } else if (viewType == SERVICE_FEE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_item, parent, false);
            return new ItemServiceFeeViewHolder(view);
        } else return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if (holder1 instanceof AddServiceFeeViewHolder) {
            AddServiceFeeViewHolder holder = (AddServiceFeeViewHolder) holder1;
            if (!addingState.isChanged()) {
                holder.etAmmount.setText("");
                holder.etName.setText("");
                holder.chbActive.setChecked(true);
                holder.spTypeAmount.setSelectedPosition(0);
                holder.spUsed.setSelectedPosition(0);
            } else {
                holder.etAmmount.setText((addingState.getChangedObject().getAmount() == 0) ? "" : String.valueOf(addingState.getChangedObject().getAmount()));
                holder.etName.setText((addingState.getChangedObject().getName() == null) ? "" : addingState.getChangedObject().getName());
                holder.chbActive.setChecked(addingState.getChangedObject().getIsActive());
                int a = 0;
                if (ServiceFee.VALUE == addingState.getChangedObject().getType()) {
                    a = 1;
                }
                holder.spTypeAmount.setSelectedPosition(a);
                a = 0;
                if (ServiceFee.ORDER == addingState.getChangedObject().getApplyingType()) {
                    a = 1;
                } else if (ServiceFee.ALL == (addingState.getChangedObject().getApplyingType())) {
                    a = 2;
                }
                holder.spUsed.setSelectedPosition(a);
            }

            holder.tvActive.setTypeface(Typeface.create(holder.tvActive.getTypeface(), Typeface.NORMAL));
            holder.tvAmount.setTypeface(Typeface.create(holder.tvAmount.getTypeface(), Typeface.NORMAL));
            holder.tvReason.setTypeface(Typeface.create(holder.tvReason.getTypeface(), Typeface.NORMAL));
            holder.tvType.setTypeface(Typeface.create(holder.tvType.getTypeface(), Typeface.NORMAL));
            holder.tvUsed.setTypeface(Typeface.create(holder.tvUsed.getTypeface(), Typeface.NORMAL));
            switch (currentServiceFeeSortTypes) {
                case Active:
                    holder.tvActive.setTypeface(Typeface.create(holder.tvActive.getTypeface(), Typeface.BOLD));
                    break;
                case Amount:
                    holder.tvAmount.setTypeface(Typeface.create(holder.tvAmount.getTypeface(), Typeface.BOLD));
                    break;
                case Description:
                    holder.tvReason.setTypeface(Typeface.create(holder.tvReason.getTypeface(), Typeface.BOLD));
                    break;
                case AppType:
                    holder.tvUsed.setTypeface(Typeface.create(holder.tvUsed.getTypeface(), Typeface.BOLD));
                    break;
                case Type:
                    holder.tvType.setTypeface(Typeface.create(holder.tvType.getTypeface(), Typeface.BOLD));
                    break;
                case Default:
                    break;
            }

        } else if (holder1 instanceof ItemServiceFeeViewHolder) {
            ItemServiceFeeViewHolder holder = (ItemServiceFeeViewHolder) holder1;
            ServiceFee serviceFee = null;
            if (items.get(position).isChanged()) {
                serviceFee = items.get(position).getChangedObject();
                holder.btnSave.enable();
            } else {
                serviceFee = items.get(position).getObject();
                holder.btnSave.disable();

            }

            holder.etName.setText(serviceFee.getName());
            holder.etAmmount.setText(String.valueOf(serviceFee.getAmount()));
            holder.chbActive.setChecked(serviceFee.getIsActive());
            if (!serviceFee.getIsActive()) {
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
            if (serviceFee.getType() == ServiceFee.PERCENT) {
                holder.spTypeAmount.setSelectedPosition(0);
            } else if (serviceFee.getType() == ServiceFee.VALUE) {
                holder.spTypeAmount.setSelectedPosition(1);
            }

            if (serviceFee.getApplyingType() == ServiceFee.ITEM) {
                holder.spUsed.setSelectedPosition(0);
            } else if (serviceFee.getApplyingType() == ServiceFee.ORDER) {
                holder.spUsed.setSelectedPosition(1);
            } else if (serviceFee.getApplyingType() == ServiceFee.ALL)
                holder.spUsed.setSelectedPosition(2);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return ADD_SERVICE_FEE;
        } else return SERVICE_FEE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class AddServiceFeeViewHolder extends RecyclerView.ViewHolder {
        @NotEmpty(messageId = R.string.service_fee_amount)
        @BindView(R.id.etAmount)
        MpEditText etAmmount;
        @BindView(R.id.etReason)
        MpEditText etName;
        @BindView(R.id.spType)
        MPosSpinner spTypeAmount;
        @BindView(R.id.spAppType)
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
        @BindView(R.id.tvAppType)
        TextView tvUsed;
        @BindView(R.id.tvActive)
        TextView tvActive;

        public AddServiceFeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //TODO FROM RESURS
            spTypeAmount.setArrowTint(R.color.colorDiscount);
            spUsed.setArrowTint(R.color.colorDiscount);
            spTypeAmount.setAdapter(discountAmountType);
            spUsed.setAdapter(discountUsedType);

            tvActive.setOnClickListener(view -> {
                if (currentServiceFeeSortTypes == ServiceFeePresenterImpl.ServiceFeeSortTypes.Active) {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Active);
                }
            });
            tvAmount.setOnClickListener(view -> {
                if (currentServiceFeeSortTypes == ServiceFeePresenterImpl.ServiceFeeSortTypes.Amount) {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Amount);
                }
            });
            tvReason.setOnClickListener(view -> {
                if (currentServiceFeeSortTypes == ServiceFeePresenterImpl.ServiceFeeSortTypes.Description) {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Description);
                }
            });
            tvType.setOnClickListener(view -> {
                if (currentServiceFeeSortTypes == ServiceFeePresenterImpl.ServiceFeeSortTypes.Type) {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Type);
                }
            });
            tvUsed.setOnClickListener(view -> {
                if (currentServiceFeeSortTypes == ServiceFeePresenterImpl.ServiceFeeSortTypes.AppType) {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.Default);
                } else {
                    onDiscountCallback.sortList(currentServiceFeeSortTypes = ServiceFeePresenterImpl.ServiceFeeSortTypes.AppType);
                }
            });

            spTypeAmount.setItemSelectionListener((view, position) -> {

                addingState.setNewType(position);
                if (addingState.getActualType() == ServiceFee.PERCENT) {
                    if (addingState.getActualAmount() > 100) {
                        etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                        return;
                    }
                } else etAmmount.setError(null);
            });
            spUsed.setItemSelectionListener((view, position) -> {
                addingState.setNewApplyingType(position);
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
                        if (addingState.getActualType() == ServiceFee.PERCENT) {
                            double percent = 0;
                            try {
                                percent = Double.parseDouble(etAmmount.getText().toString());
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            addingState.setNewAmount(percent);
                            if (percent > 100) {
                                etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                                return;
                            }
                        } else {
                            double percent = 0;
                            try {
                                percent = Double.parseDouble(etAmmount.getText().toString());
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            addingState.setNewAmount(percent);
                        }
                }
            });

            btnAdd.setOnClickListener(view -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (addingState.getActualType() == ServiceFee.PERCENT) {
                        if (addingState.getActualAmount() > 100) {
                            etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                            return;
                        }
                    } else etAmmount.setError(null);
                    if (addingState.getActualDescription().equals("")) {
                        etName.setError(context.getString(R.string.enter_service_fee_name));
                        return;
                    }
                    onDiscountCallback.onAddPressed(addingState.getActualAmount(), addingState.getActualType(), addingState.getActualDescription(), addingState.getActualApplyingType(), addingState.getActualActiveStatus());
                    addingState.setChangedObject(null);
                    notifyItemChanged(0);
                    etName.requestFocus();
                }
            });
        }

    }

    public class ItemServiceFeeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etAmount)
        MpEditText etAmmount;
        @BindView(R.id.etReason)
        MpEditText etName;
        @BindView(R.id.spType)
        MPosSpinner spTypeAmount;
        @BindView(R.id.spAppType)
        MPosSpinner spUsed;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnRemove)
        MpMiniActionButton btnDelete;

        public ItemServiceFeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            spTypeAmount.setArrowTint(R.color.colorDiscount);
            spUsed.setArrowTint(R.color.colorDiscount);
            spTypeAmount.setAdapter(discountAmountType);
            spUsed.setAdapter(discountUsedType);
            spUsed.setItemSelectionListenerWithPos((view, position2) -> {
                if (items.get(getAdapterPosition()).setNewApplyingType(position2)) {

                    btnSave.enable();
                }
            });
            spTypeAmount.setItemSelectionListenerWithPos((view, position2) -> {
                if (items.get(getAdapterPosition()).setNewType(position2)) {
                    if (items.get(getAdapterPosition()).getActualType() == ServiceFee.VALUE) {
                        if (items.get(getAdapterPosition()).getActualAmount() > 100) {
                            etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
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
                        if (items.get(getAdapterPosition()).getActualType() == ServiceFee.PERCENT) {
                            double percent = 0;
                            try {
                                percent = Double.parseDouble(etAmmount.getText().toString());
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            if (items.get(getAdapterPosition()).setNewAmount(percent))
                                btnSave.enable();
                            if (percent > 100) {
                                etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                                return;
                            } else etAmmount.setError(null);
                        } else {
                            double percent = 0;
                            try {
                                percent = Double.parseDouble(etAmmount.getText().toString());
                            } catch (Exception e) {
                                etAmmount.setError(context.getString(R.string.invalid));
                                return;
                            }
                            if (items.get(getAdapterPosition()).setNewAmount(percent))
                                btnSave.enable();
                            etAmmount.setError(null);
                        }
                }
            });

            btnSave.setOnClickListener(view -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (items.get(getAdapterPosition()).getActualType() == ServiceFee.PERCENT) {
                        if (items.get(getAdapterPosition()).getActualAmount() > 100) {
                            etAmmount.setError(context.getString(R.string.percent_can_not_be_more_hunder));
                            return;
                        }
                    } else etAmmount.setError(null);
                    if (items.get(getAdapterPosition()).getActualDescription().equals("")) {
                        etName.setError(context.getString(R.string.enter_service_fee_name));
                        return;
                    }
                    onDiscountCallback.onSave(items.get(getAdapterPosition()).getActualAmount(), items.get(getAdapterPosition()).getActualType(), items.get(getAdapterPosition()).getActualDescription(), items.get(getAdapterPosition()).getActualApplyingType(), items.get(getAdapterPosition()).getActualActiveStatus(), items.get(getAdapterPosition()).getObject());
                    UIUtils.closeKeyboard(etName, context);
                }
            });

            btnDelete.setOnClickListener(view -> {
                UIUtils.closeKeyboard(etName, context);
                ServiceFee serviceFee = items.get(getAdapterPosition()).getObject();
                if (serviceFee.getIsActive()) {
                    WarningDialog warningDialog = new WarningDialog(context);
                    warningDialog.onlyText(true);
                    warningDialog.setWarningMessage(context.getString(R.string.change_to_not_delete_when_not_active));
                    warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
                    warningDialog.show();
                    return;
                }
                WarningDialog warningDialog = new WarningDialog(context);
                String type = "";
                if (serviceFee.getType() == ServiceFee.PERCENT) {
                    type = "%";
                }
                warningDialog.setWarningMessage(context.getString(R.string.do_you_want_delete_item) + serviceFee.getName() + " " + String.valueOf(serviceFee.getAmount()) + type + " " + "?");
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    onDiscountCallback.onDelete(serviceFee);
                    UIUtils.closeKeyboard(etName, context);
                    warningDialog.dismiss();
                });
                warningDialog.show();
            });

        }

    }
}
