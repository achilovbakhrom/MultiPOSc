package com.jim.multipos.ui.mainpospage.view;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.BarcodeScannerPresenter;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
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
    public static final int CAMER_REQUEST_CODE = 1010;
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
    @Inject
    PreferencesHelper preferencesHelper;

    private Dialog dialog, addProductDialog;
    private BeepManager beepManager;


    @Override
    protected int getLayout() {
        return R.layout.barcode_scanner_dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMER_REQUEST_CODE);
        }
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
        textView.setText(getContext().getString(R.string.found_products));
        dialog.setContentView(dialogView);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        vendorItemsListAdapter.setListener(product -> {
            mainPageConnection.addProductToOrder(product.getId());
            dialog.dismiss();
        });

        addProductDialog = new Dialog(getContext());
        View productView = LayoutInflater.from(getContext()).inflate(R.layout.add_new_product_dialog, null, false);
        TextView title = productView.findViewById(R.id.tvDialogTitle);
        title.setText(getContext().getString(R.string.notification));
        TextView warningText = productView.findViewById(R.id.tvWarningText);
        warningText.setText(getContext().getString(R.string.there_is_no_product_with_such_barcode));
        MpCheckbox chbShowMode = productView.findViewById(R.id.chbDontShowAgain);
        chbShowMode.setChecked(false);
        chbShowMode.setCheckedChangeListener(isChecked -> preferencesHelper.setShowMode(isChecked));
        MpButton btnNo = productView.findViewById(R.id.btnWarningNO);
        MpButton btnYes = productView.findViewById(R.id.btnWarningYES);
        btnYes.setOnClickListener(view -> {
            addProductDialog.dismiss();
            openProductActivity();
        });
        btnNo.setOnClickListener(view -> addProductDialog.dismiss());
        addProductDialog.setContentView(productView);
        addProductDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

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
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void sendFoundProductToOrder(Long id) {
        mainPageConnection.addProductToOrder(id);
    }

    @Override
    public void openProductActivity() {
        ((MainPosPageActivity) getActivity()).openAddProductActivity(lastResult);
    }

    @Override
    public void openChooseProductDialog(List<Product> productList) {
        vendorItemsListAdapter.setData(productList);
        vendorItemsListAdapter.notifyDataSetChanged();
        dialog.show();
    }

    @Override
    public void openAddNewProductNotificationDialog() {
        addProductDialog.show();
    }
}
