package com.jim.multipos.utils;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Achilov Bakhrom on 11/4/17.
 */

public class UnitUtils {

    public static List<UnitCategory> generateUnitCategories(Context context, DatabaseManager databaseManager) {
        List<UnitCategory> result = new ArrayList<>();
        int unitCategoriesId = R.array.unit_categories;
        List<Integer> unitTypes = new ArrayList<>();
        unitTypes.add(UnitCategory.PIECE);
        unitTypes.add(UnitCategory.WEIGHT);
        unitTypes.add(UnitCategory.LENGTH);
        unitTypes.add(UnitCategory.AREA);
        unitTypes.add(UnitCategory.VOLUME);
        Map<Integer, Integer> titles = new HashMap<>();
        titles.put(0, R.array.piece_title);
        titles.put(1, R.array.weight_title);
        titles.put(2, R.array.length_title);
        titles.put(3, R.array.area_title);
        titles.put(4, R.array.volume_title);
        Map<Integer, Integer> abbrs = new HashMap<>();
        abbrs.put(0, R.array.piece_abbr);
        abbrs.put(1, R.array.weight_abbr);
        abbrs.put(2, R.array.length_abbr);
        abbrs.put(3, R.array.area_abbr);
        abbrs.put(4, R.array.volume_abbr);
        Map<Integer, Integer> factors = new HashMap<>();
        factors.put(0, R.array.piece_factor);
        factors.put(1, R.array.weight_factor);
        factors.put(2, R.array.length_factor);
        factors.put(3, R.array.area_factor);
        factors.put(4, R.array.volume_factor);
        String[] unitCategories = context.getResources().getStringArray(unitCategoriesId);
        for (int i = 0; i < unitCategories.length; i++) {
            UnitCategory category = new UnitCategory();
            category.setName(unitCategories[i]);
            category.setUnitType(unitTypes.get(i));
            databaseManager.addUnitCategory(category).subscribe();
            String[] unitTitles = context.getResources().getStringArray(titles.get(i));
            String[] unitAbbrs = context.getResources().getStringArray(abbrs.get(i));
            String[] unitFactors = context.getResources().getStringArray(factors.get(i));
            for (int j = 0; j < unitTitles.length; j++) {
                Unit unit = new Unit();
                unit.setName(unitTitles[j]);
                unit.setAbbr(unitAbbrs[j]);
                unit.setFactorRoot(Float.valueOf(unitFactors[j]));
                unit.setUnitCategoryId(category.getId());
                databaseManager.addUnit(unit).subscribe();
            }
            result.add(category);
        }
        return result;
    }

}
