package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.operations.UnitCategoryOperations;
import com.jim.multipos.data.operations.UnitOperations;
import com.jim.multipos.ui.first_configure.Constants;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragmentsView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by user on 05.08.17.
 */

public class UnitsFragmentPresenterImpl implements UnitsFragmentPresenter {
    private UnitsFragmentsView view;
    private Context context;
    private UnitCategoryOperations unitCategoryOperations;
    private UnitOperations unitOperations;
    private List<UnitCategory> unitCategories;
    private List<Unit> units;
    private List<Unit> weightUnits;
    private List<Unit> lengthUnits;
    private List<Unit> areaUnits;
    private List<Unit> volumeUnits;

    public UnitsFragmentPresenterImpl(Context context, UnitCategoryOperations unitCategoryOperations, UnitOperations unitOperations) {
        this.context = context;
        this.unitCategoryOperations = unitCategoryOperations;
        this.unitOperations = unitOperations;
        unitCategories = new ArrayList<>();
        units = new ArrayList<>();
        weightUnits = new ArrayList<>();
        lengthUnits = new ArrayList<>();
        areaUnits = new ArrayList<>();
        volumeUnits = new ArrayList<>();

        unitCategoryOperations.getAllUnitCategories().subscribe(categories -> {
            unitCategories = categories;
        });

        unitOperations.getAllStaticUnits().subscribe(units -> {
            this.units = units;
        });
    }

    @Override
    public void init(UnitsFragmentsView view) {
        this.view = view;
    }

    @Override
    public void setData() {

    }

    @Override
    public boolean isCompleteData() {
        return true;
    }

    @Override
    public void addUnit(int position, int whichAdapter) {
        if (whichAdapter == Constants.WEIGHT) {
            weightUnits.get(position).setIsActive(true);
            weightUnits.get(position).setIsStaticUnit(true);
        } else if (whichAdapter == Constants.LENGTH) {
            lengthUnits.get(position).setIsActive(true);
            lengthUnits.get(position).setIsStaticUnit(true);
        } else if (whichAdapter == Constants.AREA) {
            areaUnits.get(position).setIsActive(true);
            areaUnits.get(position).setIsStaticUnit(true);
        } else if (whichAdapter == Constants.VOLUME) {
            volumeUnits.get(position).setIsActive(true);
            volumeUnits.get(position).setIsStaticUnit(true);
        }

        view.unitAdded(position, whichAdapter);
    }

    @Override
    public void removeUnit(int position, int whichAdapter) {
        if (whichAdapter == Constants.WEIGHT) {
            weightUnits.get(position).setIsActive(false);
            weightUnits.get(position).setIsStaticUnit(false);
        } else if (whichAdapter == Constants.LENGTH) {
            lengthUnits.get(position).setIsActive(false);
            lengthUnits.get(position).setIsStaticUnit(false);
        } else if (whichAdapter == Constants.AREA) {
            areaUnits.get(position).setIsActive(false);
            areaUnits.get(position).setIsStaticUnit(false);
        } else if (whichAdapter == Constants.VOLUME) {
            volumeUnits.get(position).setIsActive(false);
            volumeUnits.get(position).setIsStaticUnit(false);
        }

        view.unitRemoved(position, whichAdapter);
    }

    @Override
    public void openNextFragment() {
        view.openNextFragment();
    }

    @Override
    public void setWeightRV() {
        String[] weightsUnitsTitle = getStringArray(R.array.weigth_title);
        String[] weightsAbbr = getStringArray(R.array.weigth_abbr);
        String[] weightsUnitsProperty = getStringArray(R.array.weight_property);
        String[] weightRootFactors = getStringArray(R.array.weightRootFactor);

        UnitCategory category = getCategory((int) Constants.WEIGHT);

        if (weightUnits.isEmpty()) {
            for (int i = 0; i < weightsUnitsTitle.length; i++) {
                Unit unit = new Unit();
                unit.setName(weightsUnitsTitle[i]);
                unit.setAbbr(weightsAbbr[i]);
                unit.setFactorRoot(Float.parseFloat(weightRootFactors[i]));
                unit.setUnitCategory(category);
                weightUnits.add(unit);
            }
        }

        if (!units.isEmpty()) {
            for (Unit u : units) {
                if (u.getRootId().equals(Constants.WEIGHT)) {
                    for (int i = 0; i < weightUnits.size(); i++) {
                        if (weightUnits.get(i).getName().equals(u.getName()) && u.getIsActive()) {
                            weightUnits.set(i, u);
                        }
                    }
                }
            }
        }

        view.showWeightRV(weightUnits, weightsUnitsProperty);
    }

    @Override
    public void setLengthRV() {
        String[] lengthsUnitsTitle = getStringArray(R.array.length_title);
        String[] lengthsAbbr = getStringArray(R.array.length_abbr);
        String[] lengthsUnitsProperty = getStringArray(R.array.length_property);
        String[] lengthRootFactors = getStringArray(R.array.lengthRootFactor);

        UnitCategory category = getCategory((int) Constants.LENGTH);

        if (lengthUnits.isEmpty()) {
            for (int i = 0; i < lengthsUnitsTitle.length; i++) {
                Unit unit = new Unit();
                unit.setName(lengthsUnitsTitle[i]);
                unit.setAbbr(lengthsAbbr[i]);
                unit.setFactorRoot(Float.parseFloat(lengthRootFactors[i]));
                unit.setUnitCategory(category);
                lengthUnits.add(unit);
            }
        }

        if (!units.isEmpty()) {
            for (Unit u : units) {
                if (u.getRootId().equals(Constants.LENGTH)) {
                    for (int i = 0; i < lengthUnits.size(); i++) {
                        if (lengthUnits.get(i).getName().equals(u.getName()) && u.getIsActive()) {
                            lengthUnits.set(i, u);
                        }
                    }
                }
            }
        }

        view.showLengthRV(lengthUnits, lengthsUnitsProperty);
    }

    @Override
    public void setAreaRV() {
        String[] areasUnitsTitle = getStringArray(R.array.area_title);
        String[] areasAbbr = getStringArray(R.array.area_abbr);
        String[] areasUnitsProperty = getStringArray(R.array.area_property);
        String[] areaRootFactors = getStringArray(R.array.areaRootFactor);

        UnitCategory category = getCategory((int) Constants.AREA);

        if (areaUnits.isEmpty()) {
            for (int i = 0; i < areasUnitsTitle.length; i++) {
                Unit unit = new Unit();
                unit.setName(areasUnitsTitle[i]);
                unit.setAbbr(areasAbbr[i]);
                unit.setFactorRoot(Float.parseFloat(areaRootFactors[i]));
                unit.setUnitCategory(category);
                areaUnits.add(unit);
            }
        }

        if (!units.isEmpty()) {
            for (Unit u : units) {
                if (u.getRootId().equals(Constants.AREA)) {
                    for (int i = 0; i < areaUnits.size(); i++) {
                        if (areaUnits.get(i).getName().equals(u.getName()) && u.getIsActive()) {
                            areaUnits.set(i, u);
                        }
                    }
                }
            }
        }

        view.showAreaRV(areaUnits, areasUnitsProperty);
    }

    @Override
    public void setVolumeRV() {
        String[] volumesUnitsTitle = getStringArray(R.array.volume_title);
        String[] volumesAbbr = getStringArray(R.array.volume_abbr);
        String[] volumesUnitsProperty = getStringArray(R.array.volume_property);
        String[] volumeRootFactors = getStringArray(R.array.volumeRootFactor);

        UnitCategory category = getCategory((int) Constants.VOLUME);

        if (volumeUnits.isEmpty()) {
            for (int i = 0; i < volumesUnitsTitle.length; i++) {
                Unit unit = new Unit();
                unit.setName(volumesUnitsTitle[i]);
                unit.setAbbr(volumesAbbr[i]);
                unit.setFactorRoot(Float.parseFloat(volumeRootFactors[i]));
                unit.setUnitCategory(category);
                volumeUnits.add(unit);
            }
        }

        if (!units.isEmpty()) {
            for (Unit u : units) {
                if (u.getRootId().equals(Constants.VOLUME)) {
                    for (int i = 0; i < volumeUnits.size(); i++) {
                        if (volumeUnits.get(i).getName().equals(u.getName()) && u.getIsActive()) {
                            volumeUnits.set(i, u);
                        }
                    }
                }
            }
        }

        view.showVolumeRV(volumeUnits, volumesUnitsProperty);
    }

    private String[] getStringArray(int resId) {
        return context.getResources().getStringArray(resId);
    }

    @Override
    public void saveData() {
        List<Unit> total = new ArrayList();

        for (Unit unit : weightUnits) {
            if (unit.getIsActive()) {
                total.add(unit);
            }
        }

        for (Unit unit : lengthUnits) {
            if (unit.getIsActive()) {
                total.add(unit);
            }
        }

        for (Unit unit : areaUnits) {
            if (unit.getIsActive()) {
                total.add(unit);
            }
        }

        for (Unit unit : volumeUnits) {
            if (unit.getIsActive()) {
                total.add(unit);
            }
        }

        unitOperations.removeAllUnits().subscribe(aBoolean -> {
            unitOperations.addUnits(total).subscribe();
        });
    }

    private UnitCategory getCategory(int position) {
        UnitCategory category;

        if (unitCategories.isEmpty()) {
            List<UnitCategory> categories = new ArrayList<>();
            categories.add(new UnitCategory(Constants.WEIGHT, "Weight (kg)"));
            categories.add(new UnitCategory(Constants.LENGTH, "Length (m)"));
            categories.add(new UnitCategory(Constants.AREA, "Area (m2)"));
            categories.add(new UnitCategory(Constants.VOLUME, "Volume (m3)"));

            unitCategoryOperations.addUnitCategories(categories).subscribe(aBoolean -> {
                this.unitCategories = categories;
            });

            category = categories.get(position);

            addBaseUnits();
        } else {
            category = unitCategories.get(position);
        }

        return category;
    }

    private void addBaseUnits() {
        if (units.isEmpty()) {
            String[] base_units_title = getStringArray(R.array.base_units_title);
            String[] base_units_abbr = getStringArray(R.array.base_units_abbr);

            for (int i = 0; i < base_units_title.length; i++) {
                Unit unit = new Unit();
                unit.setName(base_units_title[i]);
                unit.setAbbr(base_units_abbr[i]);
                unit.setIsStaticUnit(true);
                unit.setIsActive(true);

                if (!unitCategories.isEmpty()) {
                    unit.setRootId(getCategory(i).getId());
                }

                units.add(unit);
            }

            unitOperations.addUnits(units).subscribe();
        }
    }
}
