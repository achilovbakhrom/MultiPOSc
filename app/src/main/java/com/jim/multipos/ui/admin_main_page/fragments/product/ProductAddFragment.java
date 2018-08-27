package com.jim.multipos.ui.admin_main_page.fragments.product;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.model.Product;
import com.jim.multipos.ui.admin_main_page.fragments.product.presenter.ProductPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.product.presenter.ProductView;

import javax.inject.Inject;

import butterknife.BindView;

public class ProductAddFragment extends BaseFragment implements ProductView{

    @BindView(R.id.etName)
    MpEditText etName;
    @BindView(R.id.etBarcode)
    MpEditText etBarcode;
    @BindView(R.id.etSKU)
    MpEditText etSKU;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.isActive)
    Switch isActive;
    @BindView(R.id.madeCountry)
    MPosSpinner madeCountry;
    @BindView(R.id.category)
    MPosSpinner category;
    @BindView(R.id.subcategory)
    MPosSpinner subcategory;
    @BindView(R.id.unit)
    MPosSpinner unit;
    @BindView(R.id.productClass)
    MPosSpinner productClass;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnEdit)
    Button btnEdit;


    @Inject
    ProductPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.admin_product_add_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setEditable(false);
        presenter.startObserving();
    }

    @Override
    public void setUpEditor(Product product) {
        Toast.makeText(getContext(), product.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddMode() {
        Toast.makeText(getContext(), "Add Mode", Toast.LENGTH_LONG).show();
    }

    public void setEditable(boolean mode) {
        if (mode) {
            etName.setKeyListener((KeyListener) etName.getTag());
            etBarcode.setKeyListener((KeyListener) etBarcode.getTag());
            etSKU.setKeyListener((KeyListener) etSKU.getTag());
            etDescription.setKeyListener((KeyListener) etDescription.getTag());
            isActive.setClickable(true);
//            btnEdit.setText(getText(R.string.save));
        } else {
            etName.setTag(etName.getKeyListener());
            etName.setKeyListener(null);
            etBarcode.setTag(etBarcode.getKeyListener());
            etBarcode.setKeyListener(null);
            etSKU.setTag(etSKU.getKeyListener());
            etSKU.setKeyListener(null);
            etDescription.setTag(etDescription.getKeyListener());
            etDescription.setKeyListener(null);
            isActive.setClickable(false);
        }
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }
}
