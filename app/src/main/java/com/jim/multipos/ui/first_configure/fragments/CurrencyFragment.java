package com.jim.multipos.ui.first_configure.fragments;

/**
 * Created by user on 10.10.17.
 */

public class CurrencyFragment {
//    extends BaseFragment {
//    @BindView(R.id.btnNext)
//    MpButton btnNext;
//    @BindView(R.id.btnRevert)
//    MpButton btnRevert;
//    @BindView(R.id.spCurrency)
//    MpSpinner spCurrency;
//
//    @Override
//    protected int getLayout() {
//        return R.layout.currency_fragment;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ((FirstConfigureActivity) getActivity()).getPresenter().setupCurrencyData();
//
//        spCurrency.setOnItemSelectedListener((adapterView, view, i, l) -> {
//            ((FirstConfigureActivity) getActivity()).getPresenter().changeCurrency(((Currency) spCurrency.getAdapter().getItem(i)));
//        });
//
//        RxView.clicks(btnNext).subscribe(aVoid -> {
//            ((FirstConfigureActivity) getActivity()).getPresenter().setCompletedFragments(MpCompletedStateView.COMPLETED_STATE, CURRENCY_FRAGMENT_ID);
//            ((FirstConfigureActivity) getActivity()).getPresenter().openNextFragment();
//        });
//
//        RxView.clicks(btnRevert).subscribe(aVoid -> {
//            ((FirstConfigureActivity) getActivity()).getPresenter().openPrevFragment(CURRENCY_FRAGMENT_ID);
//        });
//    }
//
//    @Override
//    protected void rxConnections() {
//
//    }
//
//    public void setCurrencySpinnerData(List<Currency> currencies, int position) {
//        spCurrency.setAdapter(new CurrencySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, currencies));
//        spCurrency.setSelection(position);
//    }
}
