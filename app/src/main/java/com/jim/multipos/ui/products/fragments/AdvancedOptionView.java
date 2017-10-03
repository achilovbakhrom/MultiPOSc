package com.jim.multipos.ui.products.fragments;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Recipe;
import com.jim.multipos.data.db.model.unit.Unit;

import java.util.List;

/**
 * Created by DEV on 06.09.2017.
 */

public interface AdvancedOptionView {
    void setSubUnitsRecyclerView(List<Unit> subUnitsLists);
    void setIngredientsRecyclerView(List<Recipe> recipe);
    void setSpinnerUnits(List<Unit> unitList);
    void updateSubUnits();
    void updateIngredients();
    void setMainUnit(Unit mainUnit);
    void setRecipeStatus(boolean hasRecipe);
    void popBackStack();
    void setRecipeState(boolean state);
    void setIngredientSpinner(List<Product> productsList);
}
