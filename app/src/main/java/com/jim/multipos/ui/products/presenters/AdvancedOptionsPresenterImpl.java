package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Recipe;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.operations.UnitOperations;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.products.fragments.AdvancedOptionView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.BooleanMessageEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEV on 06.09.2017.
 */

public class AdvancedOptionsPresenterImpl extends AdvancedOptionConnector implements AdvancedOptionPresenter {
    private AdvancedOptionView view;
    private DatabaseManager databaseManager;
    private RxBus rxBus;
    private RxBusLocal rxBusLocal;
    private UnitOperations unitOperations;
    private List<Unit> unitList;
    private List<Unit> ingredientUnitsList;
    private List<Product> productsList;
    private List<Recipe> recipeList;
    private Product product;
    public final static String FRAGMENT_OPENED = "advanced_options";
    public final static String BOOLEAN_STATE = "recipe";
    private boolean isVisible = false;

    public AdvancedOptionsPresenterImpl(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.databaseManager = databaseManager;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        unitOperations = databaseManager.getUnitOperations();
        unitList = new ArrayList<>();
        recipeList = new ArrayList<>();
        productsList = new ArrayList<>();
    }

    @Override
    public void init(AdvancedOptionView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
        rxBusLocal.send(new MessageEvent(FRAGMENT_OPENED));
        isVisible = true;
    }

    private void getSubUnits() {
        unitList = product.getSubUnits();
        view.setSubUnitsRecyclerView(unitList);
    }

    private void getRecipes() {
        recipeList = product.getIngredients();
        view.setIngredientsRecyclerView(recipeList);
    }


    @Override
    public void setSubUnits(String name, String qty, boolean checkboxChecked) {
        Unit subUnit = new Unit();
        subUnit.setName(name);
        subUnit.setAbbr(name);
        subUnit.setIsStaticUnit(false);
        subUnit.setIsActive(checkboxChecked);
        subUnit.setFactorRoot(Float.parseFloat(qty));
        subUnit.setUnitCategory(product.getMainUnit().getUnitCategory());
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

    @Override
    public void onDestroy() {
        this.view = null;
        isVisible = false;
    }

    @Override
    public void onHasRecipeChange(boolean checkboxChecked) {
        rxBusLocal.send(new BooleanMessageEvent(checkboxChecked, BOOLEAN_STATE));
    }

    @Override
    public void saveOptions(boolean recipeState) {
        product.setRecipe(recipeState);
        rxBusLocal.send(new ProductEvent(this.product, FRAGMENT_OPENED));
        view.popBackStack();
    }

    @Override
    public void setIngredientsSpinner() {
        databaseManager.getAllIngredients().subscribe(products ->
        {
            productsList.clear();
            for (Product product : products) {
                if (!this.product.getId().equals(product.getId())) {
                    productsList.add(product);
                }
            }
            view.setIngredientSpinner(productsList);
        });
    }

    @Override
    public void setIngredientsUnits() {
        databaseManager.getAllStaticUnits().subscribe(units ->
        {
            ingredientUnitsList = units;
            view.setSpinnerUnits(ingredientUnitsList);
        });
    }

    @Override
    public void setIngredients(Product product, String quantity, Unit unit) {
        Recipe recipe = new Recipe();
        recipe.setIngredientId(product.getId());
        recipe.setFactorRoot(Float.parseFloat(quantity));
        recipe.setIngredientUnit(unit);
        recipe.setUnitId(unit.getId());
        recipe.setRecipeId(this.product.getId());
        databaseManager.addRecipe(recipe).subscribe(aLong -> {
            recipeList.add(recipe);
            view.setIngredientsRecyclerView(recipeList);
        });
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
    public void removeRecipe(int position) {
        databaseManager.deleteRecipes(recipeList.get(position)).subscribe();
        recipeList.remove(position);
        view.updateIngredients();
    }

    @Override
    void addProduct(Product product) {
        if (product != null) {
            this.product = product;
            view.setMainUnit(product.getMainUnit());
            view.setRecipeStatus(product.getRecipe());
            getSubUnits();
            getRecipes();
            setIngredientsSpinner();
            setIngredientsUnits();
        }
    }

    @Override
    protected void setRecipeState(boolean state) {
        if (isVisible)
            view.setRecipeState(state);
    }
}