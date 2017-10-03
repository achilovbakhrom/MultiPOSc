package com.jim.multipos.ui.products.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.matrix.Attribute;
import com.jim.multipos.data.db.model.matrix.AttributeType;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.presenters.AddMatrixAttributePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DEV on 19.09.2017.
 */

public class AddMatrixAttributesFragment extends BaseFragment implements AddMatrixAttributeView {
    private Unbinder unbinder;
    @BindView(R.id.ivAddAttribute)
    ImageView ivAddAttribute;
    @BindView(R.id.etAttributeName)
    MpEditText etAttributeName;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    private ViewGroup insertPoint;
    private List<View> viewList;
    private List<View> viewAttrList;
    @Inject
    AddMatrixAttributePresenter presenter;
    private LayoutInflater layoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_attribute_fragment, container,
                false);
        setRetainInstance(true);
        this.getComponent(ProductsComponent.class).inject(this);
        this.layoutInflater = inflater;
        unbinder = ButterKnife.bind(this, rootView);
        viewList = new ArrayList<>();
        viewAttrList = new ArrayList<>();
        insertPoint = (ViewGroup) rootView.findViewById(R.id.llFirstContainer);
        presenter.init(this);
        if (viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                View attrView = viewList.get(i);
                TextView tvAttrTypeName = (TextView) attrView.findViewById(R.id.tvAttributeName);
                ImageView ivMore = (ImageView) attrView.findViewById(R.id.ivMore);
                ImageView ivRemoveAttrType = (ImageView) attrView.findViewById(R.id.ivDeleteAttrType);
                ImageView ivAddAttr = (ImageView) attrView.findViewById(R.id.ivAddAttribute);
                LinearLayout llAddAttrs = (LinearLayout) attrView.findViewById(R.id.llAddAttributes);
                LinearLayout llAttrsContainer = (LinearLayout) attrView.findViewById(R.id.llAttributesContainer);
                MpEditText etAttribute = (MpEditText) attrView.findViewById(R.id.etAttributeName);
                final boolean[] visible = {false};
                RxView.clicks(ivMore).subscribe(o -> {
                    if (!visible[0]) {
                        llAddAttrs.setVisibility(View.VISIBLE);
                        ivMore.setImageResource(R.drawable.up_arrow);
                        visible[0] = true;
                    } else {
                        llAddAttrs.setVisibility(View.GONE);
                        visible[0] = false;
                        ivMore.setImageResource(R.drawable.down_arrow);
                    }
                });
                RxView.clicks(ivRemoveAttrType).subscribe(o -> {
                    presenter.removeAttrType(tvAttrTypeName.getText().toString());
                    viewList.remove(attrView);
                    insertPoint.removeView(attrView);
                });
                RxView.clicks(ivAddAttr).subscribe(o -> {
                    if (!etAttribute.getText().toString().isEmpty()) {
                        View attributeView = layoutInflater.inflate(R.layout.add_attr_third_item, null);
                        MpEditText etAttributeName = (MpEditText) attributeView.findViewById(R.id.etAttributeName);
                        etAttributeName.setText(etAttribute.getText().toString());
//                        viewAttrList.add(attributeView);
                        llAttrsContainer.addView(attributeView, 0);
                    }
                });
            }
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ivAddAttribute)
    public void onAddAttribute() {
        if (!etAttributeName.getText().toString().isEmpty()) {
            View attributeTypeView = layoutInflater.inflate(R.layout.add_attr_item_second, null);
            TextView textView = (TextView) attributeTypeView.findViewById(R.id.tvAttributeName);
            textView.setText(etAttributeName.getText().toString());
            presenter.addAttributeType(etAttributeName.getText().toString(), chbActive.isCheckboxChecked());
            viewList.add(attributeTypeView);
            insertPoint.addView(attributeTypeView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            clearFields();
        }
    }

    @Override
    public void setAttributeTypes(List<AttributeType> attributeTypes) {
        if (attributeTypes.size() > 0) {
            viewList.clear();
            for (int i = 0; i < attributeTypes.size(); i++) {
                View attributeTypeView = layoutInflater.inflate(R.layout.add_attr_item_second, null);
                TextView textView = (TextView) attributeTypeView.findViewById(R.id.tvAttributeName);
                textView.setText(attributeTypes.get(i).getName());
                attributeTypeView.setId(i);
                viewList.add(attributeTypeView);
                insertPoint.addView(attributeTypeView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }

    @Override
    public void clearFields() {
        etAttributeName.setText("");
    }
}
