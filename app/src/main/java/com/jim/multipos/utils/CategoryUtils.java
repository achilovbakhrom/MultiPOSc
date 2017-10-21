package com.jim.multipos.utils;

import com.jim.multipos.data.db.model.products.Category;

import static com.jim.multipos.data.db.model.products.Category.WITHOUT_PARENT;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class CategoryUtils {
    public static Boolean isSubcategory(Category category) {
        return !category.getParentId().equals(WITHOUT_PARENT);
    }
}
