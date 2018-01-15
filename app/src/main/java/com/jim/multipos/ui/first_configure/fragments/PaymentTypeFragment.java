package com.jim.multipos.ui.first_configure.fragments;

/**
 * Created by user on 10.10.17.
 */

public class PaymentTypeFragment {
//    extends BaseFragment {
//    @BindView(R.id.etPaymentTypeName)
//    @NotEmpty(messageId = R.string.enter_payment_type_name)
//    EditText etPaymentTypeName;
//    @BindView(R.id.btnNext)
//    MpButton btnNext;
//    @BindView(R.id.btnRevert)
//    MpButton btnRevert;
//    @BindView(R.id.ivAdd)
//    ImageView ivAdd;
//    @BindView(R.id.rvSystemPaymentType)
//    RecyclerView rvSystemPaymentType;
//    @BindView(R.id.spAccount)
//    MpSpinner spAccount;
//    @BindView(R.id.tvCurrency)
//    TextView tvCurrency;
//
//    @Override
//    protected int getLayout() {
//        return R.layout.payment_type_fragment;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        rvSystemPaymentType.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        if (((FirstConfigureActivity) getActivity()).getPresenter().isNextButton()) {
//            btnNext.setText(R.string.next);
//        } else {
//            btnNext.setText(R.string.save);
//        }
//
//        ((FirstConfigureActivity) getActivity()).getPresenter().setupPaymentTypeCurrency();
//        ((FirstConfigureActivity) getActivity()).getPresenter().setupPaymentTypeAccount();
//        ((FirstConfigureActivity) getActivity()).getPresenter().fillPaymentTypesRV(rvSystemPaymentType);
//
//        RxView.clicks(btnNext).subscribe(aVoid -> {
//            ((FirstConfigureActivity) getActivity()).getPresenter().checkPaymentTypeData();
//        });
//
//        RxView.clicks(ivAdd).subscribe(aVoid -> {
//            if (isValid()) {
//                ((FirstConfigureActivity) getActivity()).getPresenter().addPaymentType(
//                        etPaymentTypeName.getText().toString(),
//                        (Account) spAccount.getAdapter().getItem(spAccount.selectedItemPosition())
//                );
//
//                etPaymentTypeName.setText("");
//            }
//        });
//
//        RxView.clicks(btnRevert).subscribe(aVoid -> {
//            ((FirstConfigureActivity) getActivity()).getPresenter().openPrevFragment(PAYMENT_TYPE_FRAGMENT_ID);
//        });
//    }
//
//    @Override
//    protected void rxConnections() {
//
//    }
//
//    @Override
//    public boolean isValid() {
//        boolean result = super.isValid();
//        boolean isAccountsEmpty = true;
//        boolean isPaymentTypeNameExists = false;
//
//        if (((FirstConfigureActivity) getActivity()).getPresenter().isPaymentTypeNameExists(etPaymentTypeName.getText().toString())) {
//            etPaymentTypeName.setError(getString(R.string.payment_type_name_exists));
//            isPaymentTypeNameExists = true;
//        }
//
//        if (spAccount.getAdapter().isEmpty()) {
//            showAccountToast();
//
//            isAccountsEmpty = false;
//        }
//
//        if (tvCurrency.getText().toString().isEmpty()) {
//            showCurrencyToast();
//
//            return false;
//        }
//
//        if (isPaymentTypeNameExists) {
//            return false;
//        }
//
//        if (!isAccountsEmpty) {
//            return false;
//        }
//
//        return result;
//    }
//
//    public void showCurrencyToast() {
//        Toast.makeText(getContext(), R.string.choose_least_one_currency, Toast.LENGTH_LONG).show();
//    }
//
//    public void showAccountToast() {
//        Toast.makeText(getContext(), R.string.create_least_one_account, Toast.LENGTH_LONG).show();
//    }
//
//    public void showPaymentTypeToast() {
//        Toast.makeText(getContext(), R.string.create_least_one_payment_type, Toast.LENGTH_LONG).show();
//    }
//
//    public void setCurrency(Currency currency) {
//        tvCurrency.setText(currency.getName());
//    }
//
//    public void setAccount(List<Account> accounts) {
//        spAccount.setAdapter(new AccountSpinnerAdapter(getContext(), R.layout.item_spinner, accounts));
//    }
//
//    public void removePaymentTypeItem(PaymentType paymentType) {
//        ((SystemPaymentTypesAdapter) rvSystemPaymentType.getAdapter()).removePaymentTypeItem(paymentType);
//    }
//
//    public void addPaymentTypeItem(PaymentType paymentType) {
//        ((SystemPaymentTypesAdapter) rvSystemPaymentType.getAdapter()).addPaymentTypeItem(paymentType);
//    }
}
