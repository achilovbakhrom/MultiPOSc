package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.matrix.AttributeType;
import com.jim.multipos.ui.products.fragments.AddMatrixAttributeView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;

import java.util.List;

/**
 * Created by DEV on 20.09.2017.
 */

public class AddMatrixAttributesPresenterImpl implements AddMatrixAttributePresenter {
    private AddMatrixAttributeView view;
    private DatabaseManager databaseManager;
    private RxBus rxBus;
    private List<AttributeType> attributeTypes;
    private RxBusLocal rxBusLocal;

    public AddMatrixAttributesPresenterImpl(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.databaseManager = databaseManager;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
    }

    @Override
    public void init(AddMatrixAttributeView view) {
        this.view = view;
        setAttributeTypes();
    }

    private void setAttributeTypes() {
        databaseManager.getAllAttributeTypes().subscribe(attributeTypes -> {
                    this.attributeTypes = attributeTypes;
                    view.setAttributeTypes(attributeTypes);
                }
        );
    }

    @Override
    public void addAttributeType(String name, boolean checkboxChecked) {
        AttributeType attributeType = new AttributeType();
        attributeType.setIsActive(checkboxChecked);
        attributeType.setName(name);
        databaseManager.addAttributeType(attributeType).subscribe();
    }

    @Override
    public void removeAttrType(String attrTypeName) {
        databaseManager.removeAttributeTypesByName(attrTypeName).subscribe()               ;
    }
}
