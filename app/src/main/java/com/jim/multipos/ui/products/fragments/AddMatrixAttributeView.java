package com.jim.multipos.ui.products.fragments;

import com.jim.multipos.data.db.model.matrix.AttributeType;

import java.util.List;

/**
 * Created by DEV on 20.09.2017.
 */

public interface AddMatrixAttributeView {
    void setAttributeTypes(List<AttributeType> attributeTypes);
    void clearFields();
}
