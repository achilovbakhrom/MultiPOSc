package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.operations.UnitOperations;
import com.jim.multipos.ui.product.view.AdvancedOptionView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by DEV on 06.09.2017.
 */
@PerFragment
public class AdvancedOptionsPresenterImpl extends AdvancedOptionConnector implements AdvancedOptionPresenter {
    private AdvancedOptionView view;
    private DatabaseManager databaseManager;
    private RxBus rxBus;
    private RxBusLocal rxBusLocal;
    private UnitOperations unitOperations;
    private List<Unit> unitList;
    private Product product;
    public final static String FRAGMENT_OPENED = "advanced_options";
    private boolean isVisible = false;
    @Inject
    public AdvancedOptionsPresenterImpl(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.databaseManager = databaseManager;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        unitOperations = databaseManager.getUnitOperations();
        unitList = new ArrayList<>();
    }

    @Override
    public void init(AdvancedOptionView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
        rxBusLocal.send(new MessageEvent(FRAGMENT_OPENED));
        isVisible = true;
    }

    private void getSubUnits() {
//        unitList = product.getSubUnits();
        view.setSubUnitsRecyclerView(unitList);
    }

    @Override
    public void setSubUnits(String name, String qty, boolean active) {
        if (name.isEmpty()) {
            view.setError("Please, enter stock unit name", "");
        } else if (qty.isEmpty()) {
            view.setError("", "Please, enter unit value");
        } else {
            Unit subUnit = new Unit();
            subUnit.setName(name);
            subUnit.setAbbr(name);
            subUnit.setIsStaticUnit(false);
            subUnit.setIsActive(active);
            subUnit.setFactorRoot(Float.parseFloat(qty));
//            subUnit.setUnitCategory(product.getMainUnit().getUnitCategory());
            subUnit.setSubUnitAbbr(product.getMainUnit().getAbbr());
            if (product != null) {
                unitOperations.addUnit(subUnit).subscribe(aLong -> {
                    unitList.add(subUnit);
                    view.setSubUnitsRecyclerView(unitList);
                    SubUnitsList subUnitsList = new SubUnitsList();
                    subUnitsList.setProductId(product.getId());
                    subUnitsList.setUnitId(subUnit.getId());
                    databaseManager.addSubUnitList(subUnitsList).subscribe();
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        this.view = null;
        isVisible = false;
    }

    @Override
    public void saveOptions(String description) {
        product.setDescription(description);
        rxBusLocal.send(new ProductEvent(this.product, FRAGMENT_OPENED));
        view.popBackStack();
    }

    @Override
    public void removeSubUnit(int position) {
        Unit unit = unitList.get(position);
        databaseManager.getSubUnitList().subscribe(subUnitsLists ->
        {
            for (SubUnitsList subUnitsList : subUnitsLists) {
                if (subUnitsList.getUnitId().equals(unit.getId())) {
                    databaseManager.deleteSubUnitList(subUnitsList).subscribe();
                    view.updateSubUnits();
                }
            }
        });
        unitList.remove(position);
        databaseManager.removeUnit(unit).subscribe();
    }

    @Override
    void addProduct(Product product) {
        if (product != null) {
            this.product = product;
            view.setFields(product.getMainUnit(), product.getDescription());
            getSubUnits();
        }
    }
}