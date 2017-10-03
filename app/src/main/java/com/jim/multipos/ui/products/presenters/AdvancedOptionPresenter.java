package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.products.fragments.AdvancedOptionView;

/**
 * Created by DEV on 06.09.2017.
 */

public interface AdvancedOptionPresenter extends BaseFragmentPresenter<AdvancedOptionView>{
    void setSubUnits(String name, String qty, boolean checkboxChecked);
    void onDestroy();
    void onHasRecipeChange(boolean checkboxChecked);
    void saveOptions(boolean recipeState);
    void setIngredientsSpinner();
    void setIngredientsUnits();
    void setIngredients(Product product, String quantity, Unit unit);
    void removeSubUnit(int position);
    void removeRecipe(int position);
}
