package com.jim.multipos.ui.first_configure;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.first_configure.adapters.SystemAccountsAdapter;
import com.jim.multipos.ui.first_configure.adapters.SystemPaymentTypesAdapter;
import com.jim.multipos.ui.first_configure.adapters.UnitAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;

import static com.jim.multipos.ui.first_configure.Constants.ACCOUNT_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.CURRENCY_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.PAYMENT_TYPE_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.POS_DETAIL_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.UNITS_FRAGMENT_ID;

/**
 * Created by user on 07.10.17.
 */

public class FirstConfigurePresenterImpl extends BasePresenterImpl<FirstConfigureView> implements FirstConfigurePresenter {
    @Inject
    PreferencesHelper preferences;
    private FirstConfigureView view;
    @Getter
    private boolean[] completedFragments = null;
    private final DatabaseManager databaseManager;
    @Getter
    private final String[] types;
    @Getter
    private final String[] circulations;
    private final String[] currencyName;
    private final String[] currencyAbbr;
    private final String[] baseUnitsTitle;
    private final String[] baseUnitsAbbr;
    private final String[] weightUnitsTitle;
    private final String[] lengthUnitsTitle;
    private final String[] areaUnitsTitle;
    private final String[] volumeUnitsTitle;
    private final String[] weightUnitsAbbr;
    private final String[] lengthUnitsAbbr;
    private final String[] areaUnitsAbbr;
    private final String[] volumeUnitsAbbr;
    private final String[] weightUnitsRootFactor;
    private final String[] lengthUnitsRootFactor;
    private final String[] areaUnitsRootFactor;
    private final String[] volumeUnitsRootFactor;

    public static final String COMPLETED_FRAGMENTS_KEY = "COMPLETED_FRAGMENTS_KEY";

    @Inject
    public FirstConfigurePresenterImpl(FirstConfigureView view,
                                       DatabaseManager databaseManager,
                                       @Named(value = "account_circulations") String[] circulations,
                                       @Named(value = "account_types") String[] types,
                                       @Named(value = "currency_name") String[] currencyName,
                                       @Named(value = "currency_abbr") String[] currencyAbbr,
                                       @Named(value = "base_units_title") String[] baseUnitsTitle,
                                       @Named(value = "base_units_abbr") String[] baseUnitsAbbr,
                                       @Named(value = "weight_units_title") String[] weightUnitsTitle,
                                       @Named(value = "length_units_title") String[] lengthUnitsTitle,
                                       @Named(value = "area_units_title") String[] areaUnitsTitle,
                                       @Named(value = "volume_units_title") String[] volumeUnitsTitle,
                                       @Named(value = "weight_units_abbr") String[] weightUnitsAbbr,
                                       @Named(value = "length_units_abbr") String[] lengthUnitsAbbr,
                                       @Named(value = "area_units_abbr") String[] areaUnitsAbbr,
                                       @Named(value = "volume_units_abbr") String[] volumeUnitsAbbr,
                                       @Named(value = "weight_units_root_factor") String[] weightUnitsRootFactor,
                                       @Named(value = "length_units_root_factor") String[] lengthUnitsRootFactor,
                                       @Named(value = "area_units_root_factor") String[] areaUnitsRootFactor,
                                       @Named(value = "volume_units_root_factor") String[] volumeUnitsRootFactor) {
        super(view);
        this.databaseManager = databaseManager;
        this.view = view;
        this.completedFragments = new boolean[5];
        this.types = types;
        this.circulations = circulations;
        this.currencyName = currencyName;
        this.currencyAbbr = currencyAbbr;
        this.baseUnitsTitle = baseUnitsTitle;
        this.baseUnitsAbbr = baseUnitsAbbr;
        this.weightUnitsTitle = weightUnitsTitle;
        this.lengthUnitsTitle = lengthUnitsTitle;
        this.areaUnitsTitle = areaUnitsTitle;
        this.volumeUnitsTitle = volumeUnitsTitle;
        this.weightUnitsAbbr = weightUnitsAbbr;
        this.lengthUnitsAbbr = lengthUnitsAbbr;
        this.areaUnitsAbbr = areaUnitsAbbr;
        this.volumeUnitsAbbr = volumeUnitsAbbr;
        this.weightUnitsRootFactor = weightUnitsRootFactor;
        this.lengthUnitsRootFactor = lengthUnitsRootFactor;
        this.areaUnitsRootFactor = areaUnitsRootFactor;
        this.volumeUnitsRootFactor = volumeUnitsRootFactor;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            completedFragments = bundle.getBooleanArray(COMPLETED_FRAGMENTS_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBooleanArray(COMPLETED_FRAGMENTS_KEY, completedFragments);
    }

    @Override
    public void openNextFragment() {
        if (completedFragments == null) return;

        int nextPosition = -1;
        int notCompletedCount = 0;

        for (int i = 0; i < completedFragments.length; i++) {
            if (!completedFragments[i]) {
                nextPosition = i;
                break;
            }
        }

        for (int i = 0; i < completedFragments.length; i++) {
            if (!completedFragments[i]) {
                notCompletedCount++;
            }
        }

        if (notCompletedCount == 0) {
            view.closeActivity();

            return;
        }

        if (nextPosition != -1) {
            view.replaceFragment(nextPosition);
            view.updateLeftSideFragment(nextPosition);
        }
    }

    @Override
    public void openNextFragment(int position, int nextPosition) {
        switch (position) {
            case POS_DETAIL_FRAGMENT_ID:
                if (preferences.getPosDetailPosId() == null || preferences.getPosDetailPosId().isEmpty()) {
                    completedFragments[POS_DETAIL_FRAGMENT_ID] = false;
                } else {
                    completedFragments[POS_DETAIL_FRAGMENT_ID] = true;
                }

                view.replaceFragment(nextPosition);
                view.updateLeftSideFragment(nextPosition);
                break;
            case ACCOUNT_FRAGMENT_ID:
                databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
                    if (accounts.isEmpty()) {
                        completedFragments[ACCOUNT_FRAGMENT_ID] = false;
                    } else {
                        completedFragments[ACCOUNT_FRAGMENT_ID] = true;
                    }

                    view.replaceFragment(nextPosition);
                    view.updateLeftSideFragment(nextPosition);
                });
                break;
            case CURRENCY_FRAGMENT_ID:
                completedFragments[CURRENCY_FRAGMENT_ID] = true;

                view.replaceFragment(nextPosition);
                view.updateLeftSideFragment(nextPosition);
                break;
            case PAYMENT_TYPE_FRAGMENT_ID:
                databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
                    if (paymentTypes.isEmpty()) {
                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = false;
                    } else {
                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = true;
                    }

                    view.replaceFragment(nextPosition);
                    view.updateLeftSideFragment(nextPosition);
                });
                break;
            case UNITS_FRAGMENT_ID:
                completedFragments[UNITS_FRAGMENT_ID] = true;

                view.replaceFragment(nextPosition);
                view.updateLeftSideFragment(nextPosition);
                break;
        }
    }

    @Override
    public void openPrevFragment(int position) {
        switch (position) {
            case POS_DETAIL_FRAGMENT_ID:
                if (preferences.getPosDetailPosId() == null || preferences.getPosDetailPosId().isEmpty()) {
                    completedFragments[POS_DETAIL_FRAGMENT_ID] = false;
                } else {
                    completedFragments[POS_DETAIL_FRAGMENT_ID] = true;
                }
                break;
            case ACCOUNT_FRAGMENT_ID:
                databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
                    if (accounts.isEmpty()) {
                        completedFragments[ACCOUNT_FRAGMENT_ID] = false;
                    } else {
                        completedFragments[ACCOUNT_FRAGMENT_ID] = true;
                    }
                });
                break;
            case CURRENCY_FRAGMENT_ID:
                completedFragments[CURRENCY_FRAGMENT_ID] = true;
                break;
            case PAYMENT_TYPE_FRAGMENT_ID:
                databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
                    if (paymentTypes.isEmpty()) {
                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = false;
                    } else {
                        completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = true;
                    }
                });
                break;
            case UNITS_FRAGMENT_ID:
                completedFragments[UNITS_FRAGMENT_ID] = true;
                break;
        }

        view.openPrevFragment();
    }

    @Override
    public boolean isNextButton() {
        if (completedFragments == null) return false;
        int i = 0;
        for (boolean isCompleted : completedFragments) {
            if (!isCompleted) i++;
            if (i > 1) return true;
        }
        return false;
    }

    @Override
    public void fillPosDetailsData(EditText etPosId, EditText etAlias, EditText etAddress, EditText etPassword, EditText etRepeatPassword) {
        etPosId.setText(preferences.getPosDetailPosId());
        etAlias.setText(preferences.getPosDetailAlias());
        etAddress.setText(preferences.getPosDetailAddress());
        etPassword.setText(preferences.getPosDetailPassword());
        etRepeatPassword.setText(preferences.getPosDetailPassword());
    }

    @Override
    public void fillAccountsRV(RecyclerView recyclerView) {
        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
            recyclerView.setAdapter(new SystemAccountsAdapter(accounts, types, circulations, account -> {
                databaseManager.getAccountOperations().removeAccount(account).subscribe(aBoolean -> {
                    view.removeAccountItem(account);
                });
            }));
        });
    }

    @Override
    public void fillPaymentTypesRV(RecyclerView recyclerView) {
        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
            recyclerView.setAdapter(new SystemPaymentTypesAdapter(paymentTypes, paymentType -> {
                databaseManager.getPaymentTypeOperations().removePaymentType(paymentType).subscribe(aBoolean -> {
                    view.removePaymentTypeItem(paymentType);
                });
            }));
        });
    }

    @Override
    public void setupCurrencyData() {
        List<Currency> currencies = new ArrayList<>();

        for (int i = 0; i < currencyName.length; i++) {
            Currency currency = new Currency();
            currency.setName(currencyName[i]);
            currency.setAbbr(currencyAbbr[i]);

            currencies.add(currency);
        }

        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(dbCurrencies -> {
            int position = 0;


            if (!dbCurrencies.isEmpty()) {
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getAbbr().equals(dbCurrencies.get(0).getAbbr())) {
                        currencies.get(i).setId(dbCurrencies.get(0).getId());
                        position = i;
                    }
                }
            } else {
                databaseManager.getCurrencyOperations().addCurrency(currencies.get(position)).subscribe();
            }

            view.setCurrencySpinnerData(currencies, position);
        });
    }

    @Override
    public void changeCurrency(Currency currency) {
        databaseManager.getCurrencyOperations().removeAllCurrencies().subscribe(aBoolean -> {
            databaseManager.getCurrencyOperations().addCurrency(currency).subscribe();
        });
    }

    @Override
    public void addAccount(String name, int type, int circulation) {
        Account account = new Account();

        account.setName(name);
        account.setType(type);
        account.setCirculation(circulation);

        databaseManager.getAccountOperations().addAccount(account).subscribe(account1 -> {
            view.addAccountItem(account1);
        });
    }

    @Override
    public void addPaymentType(String name, Account account) {
        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
            PaymentType paymentType = new PaymentType();
            paymentType.setName(name);
            paymentType.setAccount(account);
            paymentType.setCurrency(currencies.get(0));

            databaseManager.getPaymentTypeOperations().addPaymentType(paymentType).subscribe(aLong -> {
                view.addPaymentTypeItem(paymentType);
            });
        });

    }

    @Override
    public Boolean isAccountNameExists(String name) {
        return databaseManager.getAccountOperations().isAccountNameExists(name);
    }

    @Override
    public Boolean isPaymentTypeNameExists(String name) {
        return databaseManager.getPaymentTypeOperations().isPaymentTypeNameExists(name);
    }

    @Override
    public void checkAccountData() {
        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
            if (accounts.size() > 0) {
                completedFragments[ACCOUNT_FRAGMENT_ID] = true;
                openNextFragment();
            } else {
                view.showAccountToast();
                completedFragments[ACCOUNT_FRAGMENT_ID] = false;
            }
        });
    }

    @Override
    public void checkPaymentTypeData() {
        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
            if (paymentTypes.isEmpty()) {
                view.showPaymentTypeToast();
                completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = false;
            } else {
                completedFragments[PAYMENT_TYPE_FRAGMENT_ID] = true;
                openNextFragment();
            }
        });
    }

    @Override
    public void savePosDetailsData(String posId, String alias, String address, String password) {
        preferences.setPosDetailPosId(posId);
        preferences.setPosDetailAlias(alias);
        preferences.setPosDetailAddress(address);
        preferences.setPosDetailPassword(password);
    }

    @Override
    public void setupPaymentTypeCurrency() {
        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
            if (currencies.isEmpty()) {
                view.showPaymentTypeCurrencyToast();
            } else {
                view.setPaymentTypeCurrency(currencies.get(0));
            }
        });
    }

    @Override
    public void setupPaymentTypeAccount() {
        databaseManager.getAccountOperations().getAllAccounts().subscribe(accounts -> {
            if (accounts.isEmpty()) {
                view.showPaymentTypeAccountToast();
            }

            view.setPaymentTypeAccount(accounts);
        });
    }

    @Override
    public void setupUnits(RecyclerView rvWeight, RecyclerView rvLength, RecyclerView rvArea, RecyclerView rvVolume) {
        databaseManager.getUnitCategoryOperations().getAllUnitCategories().subscribe(categories -> {
            if (categories.isEmpty()) {
                databaseManager.getUnitCategoryOperations().addUnitCategories(createUnitCategories()).subscribe(aBoolean -> {
                    databaseManager.getUnitCategoryOperations().getAllUnitCategories().subscribe(categories1 -> {
                        if (!categories1.isEmpty()) {
                            databaseManager.getUnitOperations().addUnits(createUnits(categories1)).subscribe(aBoolean1 -> {
                                databaseManager.getUnitOperations().getUnits(categories1.get(0).getId(), categories1.get(0).getName()).subscribe(units -> {
                                    rvWeight.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                        @Override
                                        public void addUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.addWeightUnit(item);
                                            });
                                        }

                                        @Override
                                        public void removeUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(unit -> {
                                                view.removeWeightUnit(item);
                                            });
                                        }
                                    }));
                                });
                                databaseManager.getUnitOperations().getUnits(categories1.get(1).getId(), categories1.get(1).getName()).subscribe(units -> {
                                    rvLength.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                        @Override
                                        public void addUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.addLengthUnit(item);
                                            });
                                        }

                                        @Override
                                        public void removeUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.removeLengthUnit(item);
                                            });
                                        }
                                    }));
                                });
                                databaseManager.getUnitOperations().getUnits(categories1.get(2).getId(), categories1.get(2).getName()).subscribe(units -> {
                                    rvArea.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                        @Override
                                        public void addUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.addAreaUnit(item);
                                            });
                                        }

                                        @Override
                                        public void removeUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.removeAreaUnit(item);
                                            });
                                        }
                                    }));
                                });
                                databaseManager.getUnitOperations().getUnits(categories1.get(3).getId(), categories1.get(3).getName()).subscribe(units -> {
                                    rvVolume.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                        @Override
                                        public void addUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.addVolumeUnit(item);
                                            });
                                        }

                                        @Override
                                        public void removeUnitItem(Unit item) {
                                            databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                                view.removeVolumeUnit(item);
                                            });
                                        }
                                    }));
                                });
                            });
                        }
                    });
                });
            } else {
                databaseManager.getUnitCategoryOperations().getAllUnitCategories().subscribe(categories1 -> {
                    if (!categories1.isEmpty()) {
                        databaseManager.getUnitOperations().getUnits(categories1.get(0).getId(), categories1.get(0).getName()).subscribe(units -> {
                            rvWeight.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                @Override
                                public void addUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.addWeightUnit(item);
                                    });
                                }

                                @Override
                                public void removeUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.removeWeightUnit(item);
                                    });
                                }
                            }));
                        });
                        databaseManager.getUnitOperations().getUnits(categories1.get(1).getId(), categories1.get(1).getName()).subscribe(units -> {
                            rvLength.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                @Override
                                public void addUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.addLengthUnit(item);
                                    });
                                }

                                @Override
                                public void removeUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.removeLengthUnit(item);
                                    });
                                }
                            }));
                        });
                        databaseManager.getUnitOperations().getUnits(categories1.get(2).getId(), categories1.get(2).getName()).subscribe(units -> {
                            rvArea.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                @Override
                                public void addUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.addAreaUnit(item);
                                    });
                                }

                                @Override
                                public void removeUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.removeAreaUnit(item);
                                    });
                                }
                            }));
                        });
                        databaseManager.getUnitOperations().getUnits(categories1.get(3).getId(), categories1.get(3).getName()).subscribe(units -> {
                            rvVolume.setAdapter(new UnitAdapter(units, new UnitAdapter.OnClickListener() {
                                @Override
                                public void addUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.addVolumeUnit(item);
                                    });
                                }

                                @Override
                                public void removeUnitItem(Unit item) {
                                    databaseManager.getUnitOperations().updateUnit(item).subscribe(aLong -> {
                                        view.removeVolumeUnit(item);
                                    });
                                }
                            }));
                        });
                    }
                });
            }
        });
    }

    private List<UnitCategory> createUnitCategories() {
        List<UnitCategory> unitCategories = new ArrayList<>();

        for (int i = 0; i < baseUnitsTitle.length; i++) {
            unitCategories.add(new UnitCategory(baseUnitsTitle[0], baseUnitsAbbr[0]));
        }

        return unitCategories;
    }

    private List<Unit> createUnits(List<UnitCategory> unitCategories) {
        List<Unit> units = new ArrayList<>();

        for (int i = 0; i < weightUnitsTitle.length; i++) {
            Unit unit = new Unit();
            unit.setName(weightUnitsTitle[i]);
            unit.setAbbr(weightUnitsAbbr[i]);
            unit.setFactorRoot(Float.parseFloat(weightUnitsRootFactor[i]));
            unit.setUnitCategory(unitCategories.get(0));
            unit.setIsActive(false);
            unit.setIsStaticUnit(true);

            units.add(unit);
        }

        for (int i = 0; i < lengthUnitsTitle.length; i++) {
            Unit unit = new Unit();
            unit.setName(lengthUnitsTitle[i]);
            unit.setAbbr(lengthUnitsAbbr[i]);
            unit.setFactorRoot(Float.parseFloat(lengthUnitsRootFactor[i]));
            unit.setUnitCategory(unitCategories.get(1));
            unit.setIsActive(false);
            unit.setIsStaticUnit(true);

            units.add(unit);
        }

        for (int i = 0; i < areaUnitsTitle.length; i++) {
            Unit unit = new Unit();
            unit.setName(areaUnitsTitle[i]);
            unit.setAbbr(areaUnitsAbbr[i]);
            unit.setFactorRoot(Float.parseFloat(areaUnitsRootFactor[i]));
            unit.setUnitCategory(unitCategories.get(2));
            unit.setIsActive(false);
            unit.setIsStaticUnit(true);

            units.add(unit);
        }

        for (int i = 0; i < volumeUnitsTitle.length; i++) {
            Unit unit = new Unit();
            unit.setName(volumeUnitsTitle[i]);
            unit.setAbbr(volumeUnitsAbbr[i]);
            unit.setFactorRoot(Float.parseFloat(volumeUnitsRootFactor[i]));
            unit.setUnitCategory(unitCategories.get(3));
            unit.setIsActive(false);
            unit.setIsStaticUnit(true);

            units.add(unit);
        }

        for (int i = 0; i < unitCategories.size(); i++) {
            Unit unit = new Unit();
            unit.setName(unitCategories.get(i).getName());
            unit.setAbbr(unitCategories.get(i).getAbbr());
            unit.setFactorRoot(1);
            unit.setUnitCategory(unitCategories.get(i));
            unit.setIsActive(true);
            unit.setIsStaticUnit(true);

            units.add(unit);
        }

        return units;
    }

    @Override
    public void setCompletedFragments(boolean isCompleted, int position) {
        completedFragments[position] = isCompleted;
    }
}
