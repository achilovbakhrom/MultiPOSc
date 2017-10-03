package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Recipe;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by DEV on 14.09.2017.
 */

public interface RecipeOperations {
    Observable<Long> addRecipe(Recipe recipe);
    Observable<List<Recipe>> getAllRecipes();
    Observable<Boolean> deleteRecipes(Recipe recipe);
}
