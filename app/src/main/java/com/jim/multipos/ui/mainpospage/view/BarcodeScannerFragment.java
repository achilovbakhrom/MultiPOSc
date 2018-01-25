package com.jim.multipos.ui.mainpospage.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.BarcodeScannerPresenter;
import com.jim.multipos.utils.UIUtils;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 20.01.2018.
 */

public class BarcodeScannerFragment extends BaseFragment implements BarcodeScannerView {

    private float dX;
    private float dY;
    @BindView(R.id.llScanner)
    LinearLayout llScanner;
    @BindView(R.id.barcode)
    DecoratedBarcodeView barcodeView;
    @BindView(R.id.ivHide)
    ImageView ivHide;

    @Inject
    MainPageConnection mainPageConnection;
    @Inject
    BarcodeScannerPresenter presenter;
    @Inject
    VendorItemsListAdapter vendorItemsListAdapter;

    private Dialog dialog;
    private BeepManager beepManager;

    @Override
    protected int getLayout() {
        return R.layout.barcode_scanner_dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        barcodeView.decodeContinuous(barcodeCallback);

        llScanner.setOnTouchListener((view, motionEvent) -> {
            float newX, newY;
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - motionEvent.getRawX();
                    dY = view.getY() - motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    newX = motionEvent.getRawX() + dX;
                    newY = motionEvent.getRawY() + dY;
                    if (newY < 5f)
                        view.setY(5f);
                    else if (newY > 700f)
                        view.setY(700f);
                    else view.setY(newY);
                    if (newX < 5f)
                        view.setX(5f);
                    else if (newX > 1380f)
                        view.setX(1380f);
                    else view.setX(newX);
                    Log.d("barcode", "newX: " + newX + " , newY: " + newY);
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    return false;
            }
            return true;
        });
        ivHide.setOnClickListener(view -> hideBarcodeScannerFragment());

        dialog = new Dialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        RecyclerView rvProductList = dialogView.findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductList.setAdapter(vendorItemsListAdapter);
        TextView textView = dialogView.findViewById(R.id.tvDialogTitle);
        textView.setText("Found products");
        dialog.setContentView(dialogView);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        vendorItemsListAdapter.setListener(product -> {
            mainPageConnection.addProductToOrder(product.getId());
            dialog.dismiss();
        });
        beepManager = new BeepManager(getActivity());
    }


    String lastResult = "";
    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            barcodeView.setStatusText(result.getText());
            if (!lastResult.equals(result.getText())) {
                presenter.findProductByBarcode(result.getText());
                lastResult = result.getText();
                beepManager.playBeepSound();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
    }

    public void hideBarcodeScannerFragment() {
        BarcodeScannerFragment barcodeScannerFragment = (BarcodeScannerFragment) getActivity().getSupportFragmentManager().findFragmentByTag(BarcodeScannerFragment.class.getName());
        if (barcodeScannerFragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(barcodeScannerFragment).commit();
        }
    }

    @Override
    public void sendFoundProductToOrder(Long id) {
        mainPageConnection.addProductToOrder(id);
    }

    @Override
    public void openProductActivity() {
        ((MainPosPageActivity) getActivity()).openAddProductActivity();
    }

    @Override
    public void openChooseProductDialog(List<Product> productList) {
        vendorItemsListAdapter.setData(productList);
        vendorItemsListAdapter.notifyDataSetChanged();
        dialog.show();
    }

    @Override
    public void openAddNewProductNotificationDialog() {
        UIUtils.showAlert(getContext(), getContext().getString(R.string.yes), getContext().getString(R.string.no), "Notification", "There is no product with such barcode. Do you want add it?", new UIUtils.AlertListener() {
            @Override
            public void onPositiveButtonClicked() {
                openProductActivity();
            }

            @Override
            public void onNegativeButtonClicked() {

            }
        });
    }
}
