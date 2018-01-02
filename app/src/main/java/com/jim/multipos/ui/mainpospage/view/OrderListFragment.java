package com.jim.multipos.ui.mainpospage.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpLightButton;
import com.jim.mpviews.utils.VibrateManager;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog;
import com.jim.multipos.ui.mainpospage.adapter.OrderProductAdapter;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.presenter.OrderListPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.MessageWithIdEvent;
import com.jim.multipos.utils.rxevents.OrderProductAddEvent;
import com.jim.multipos.utils.managers.NotifyManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment.CONSIGNMENT_UPDATE;

public class OrderListFragment extends BaseFragment implements OrderListView {
    @Inject
    OrderListPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    NotifyManager notifyManager;
    @Inject
    MainPageConnection mainPageConnection;
    @BindView(R.id.llPay)
    LinearLayout llPay;
    @BindView(R.id.llDiscount)
    LinearLayout llDiscount;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.llPrintCheck)
    LinearLayout llPrintCheck;

    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvDiscountAmount)
    TextView tvDiscountAmount;
    @BindView(R.id.tvServiceAmount)
    TextView tvServiceAmount;
    @BindView(R.id.tvBalanceDue)
    TextView tvBalanceDue;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvOrderProducts)
    RecyclerView rvOrderProducts;
    @Inject
    OrderProductAdapter orderProductAdapter;
    @BindView(R.id.lbChoiseCustomer)
    MpLightButton lbChooseCustomer;
    private OrderProductItem orderProductItem;
    private int currentPosition;
    public static final String PRODUCT_ADD_TO_ORDER = "addorderproduct";
    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setOrderListView(this);
        presenter.onCreateView(savedInstanceState);

        lbChooseCustomer.setOnLightButtonClickListener(view1 -> {
            CustomerDialog customerDialog = new CustomerDialog(getContext(), databaseManager, notifyManager);
            customerDialog.show();
        });

        RxView.clicks(llPay)
                .subscribe(view1 -> {

                });
        RxView.clicks(llDiscount)
                .subscribe(view1 -> {
                    DiscountDialog discountDialog = new DiscountDialog(getContext(), databaseManager);
                    discountDialog.show();
                });
        RxView.clicks(llPrintCheck)
                .subscribe(view1 -> {

                });
        RxView.clicks(llServiceFee)
                .subscribe(view1 -> {
                    ServiceFeeDialog serviceFeeDialog = new ServiceFeeDialog(getContext(),databaseManager);
                    serviceFeeDialog.setServiceFee(presenter.getServiceFees());
                    serviceFeeDialog.show();
                });
    }

    @Override
    public void initOrderList(List<Object> objectList) {
        rvOrderProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderProducts.setAdapter(orderProductAdapter);
        ((SimpleItemAnimator) rvOrderProducts.getItemAnimator()).setSupportsChangeAnimations(false);
        orderProductAdapter.setData(objectList, new OrderProductAdapter.CallbackOrderProductList() {
            @Override
            public void onPlusCount(int position) {
                presenter.onPlusCount(position);
            }

            @Override
            public void onMinusCount(int position) {
                presenter.onMinusCount(position);
            }

            @Override
            public void onOrderProductClick(int position) {
                presenter.onOrderProductClick(position);
            }

            @Override
            public void onOrderDiscountClick() {
                presenter.onOrderDiscountClick();
            }

            @Override
            public void onOrderServiceFeeClick() {
                presenter.onOrderServiceFeeClick();
            }
        });
    }

    @Override
    public void notifyList() {
        orderProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemAdded(int adapterPosition) {
        orderProductAdapter.notifyItemInserted(adapterPosition);
    }

    @Override
    public void notifyItemChanged(int adapterPosition) {
        orderProductAdapter.notifyItemChanged(adapterPosition);
    }

    @Override
    public void openProductInfoFragment(OrderProductItem orderProductItem,int position) {
        this.orderProductItem = orderProductItem;
        currentPosition = position;
        ProductInfoFragment fragment = new ProductInfoFragment();
        ((MainPosPageActivity)getActivity()).openRightFragment(fragment);
    }

    @Override
    public void sendToProductInfoProductItem() {
        mainPageConnection.sendProductItemToProductInfo(orderProductItem);
    }

    @Override
    public void plusProductCount() {
        presenter.onPlusCount(currentPosition);
    }

    @Override
    public void minusProductCount() {
        presenter.onMinusCount(currentPosition);
    }

    @Override
    public void addProductToOrder(Long productId) {
        presenter.addProductToList(productId);
    }

    @Override
    public void onDetach() {
        mainPageConnection.setOrderListView(null);
        super.onDetach();
    }
    private ArrayList<Disposable> subscriptions;



    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
