package com.jim.multipos.ui.first_configure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityModule;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.fragments.AccountFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.FirstConfigureLeftSideFragment;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.StockFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragmentFirstConfig;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.utils.managers.PosFragmentManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FirstConfigureActivity extends BaseActivity implements HasComponent<FirstConfigureActivityComponent>, FirstConfigureView {
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    PreferencesHelper preferencesHelper;
    private FirstConfigureActivityComponent firstConfigureComponent;
    List<BaseFragmentFirstConfig> firstConfigureFragments;
    private FirstConfigureLeftSideFragment leftSideFragment;
    private int currentFragmentPosition = 0;
    List<TitleDescription> titleDescriptions;
    boolean isFragmentCompleteItems[];


    public static int SAVE_BUTTON_MODE = 0;
    public static int NEXT_BUTTON_MODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_configure);
        titleDescriptions = new ArrayList<>();
        firstConfigureFragments = new ArrayList<>();

//        firstConfigureFragments.add(new PosDetailsFragmentFirstConfig());
//        firstConfigureFragments.add(new AccountFragmentFirstConfig());
//        firstConfigureFragments.add(new CurrencyFragmentFirstConfig());
//        firstConfigureFragments.add(new PaymentTypeFragmentFirstConfig());
//        firstConfigureFragments.add(new UnitsFragmentFirstConfig());
//        firstConfigureFragments.add(new StockFragmentFirstConfig());
        String title[] = getResources().getStringArray(R.array.start_configuration_title);
        String description[] = getResources().getStringArray(R.array.start_configuration_description);

        isFragmentCompleteItems = new boolean[firstConfigureFragments.size()];


        for (int i = 0; i < title.length; i++) {
            titleDescriptions.add(new TitleDescription(title[i], description[i]));
        }

        leftSideFragment = new FirstConfigureLeftSideFragment();
        posFragmentManager.replaceFragment(leftSideFragment, R.id.leftContainer);
        posFragmentManager.replaceFragment(firstConfigureFragments.get(0), R.id.rightContainer);
    }

    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        firstConfigureComponent = baseAppComponent.plus(new FirstConfigureActivityModule(this));
        firstConfigureComponent.inject(this);
    }

    @Override
    public FirstConfigureActivityComponent getComponent() {
        return firstConfigureComponent;
    }

    public void replaceFragment(int position) {
        currentFragmentPosition = position;
        posFragmentManager.replaceFragment(firstConfigureFragments.get(position), R.id.rightContainer);
    }

    public void openNextFragment() {
        int position = 0;

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rightContainer);
        BaseFragmentFirstConfig baseFragmentFirstConfig = (BaseFragmentFirstConfig) fragment;

        for (int i = 0; i < firstConfigureFragments.size(); i++) {
            if (baseFragmentFirstConfig.equals(firstConfigureFragments.get(i))) {
                position = i;
                break;
            }
        }

        boolean checked = baseFragmentFirstConfig.checkData();
        isFragmentCompleteItems[position] = checked;

        int nextPosition = -1;
        int notCompleteCounter = 0;

        for (int i = 0; i < isFragmentCompleteItems.length; i++) {
            if (!isFragmentCompleteItems[i]) {
                nextPosition = i;
                break;
            }
        }

        for (int i = 0; i < isFragmentCompleteItems.length; i++) {
            if (!isFragmentCompleteItems[i])
                notCompleteCounter++;
        }

        if (nextPosition == -1) {

            // TODO Hammasi To'ldi Save metod iwlaw kere

            for (BaseFragmentFirstConfig fragment1 : firstConfigureFragments) {
                fragment1.saveData();
            }

            if ((!firstConfigureFragments.get(3).checkData()) && notCompleteCounter == 0) {
                nextPosition = 3;
                leftSideFragment.showCheckBoxIndicator(nextPosition, false);
            } else {
               preferencesHelper.setFirestConfigured(true);
               Intent intent = new Intent(FirstConfigureActivity.this, SignActivity.class);
               startActivity(intent);
                return;
            }

        }
        if (notCompleteCounter == 1) {
            modeButton = SAVE_BUTTON_MODE;

        } else if (notCompleteCounter > 1) {
            modeButton = NEXT_BUTTON_MODE;
        }

        leftSideFragment.showCheckBoxIndicator(position, checked, nextPosition);
        replaceFragment(nextPosition);

        //TODO Adapterga qaysi fragment ochlgan bosa owani title va disciptonini oq rang qib qoyish haqida etish kere
    }


    public void checkCurrentAndChangePositonFragment(int nextFragmentPosition) {
        int position = 0;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rightContainer);
        BaseFragmentFirstConfig baseFragmentFirstConfig = (BaseFragmentFirstConfig) fragment;

        for (int i = 0; i < firstConfigureFragments.size(); i++) {
            if (baseFragmentFirstConfig.equals(firstConfigureFragments.get(i))) {
                position = i;
                break;
            }
        }

        boolean checked = baseFragmentFirstConfig.checkData();
        isFragmentCompleteItems[position] = checked;
        int notCompleteCounter = 0;

        for (int i = 0; i < isFragmentCompleteItems.length; i++) {
            if (!isFragmentCompleteItems[i])
                notCompleteCounter++;
        }

        if (notCompleteCounter == 1) {
            modeButton = SAVE_BUTTON_MODE;
        } else if (notCompleteCounter > 1) {
            modeButton = NEXT_BUTTON_MODE;
        }

        leftSideFragment.showCheckBoxIndicator(position, checked, nextFragmentPosition);
        replaceFragment(nextFragmentPosition);
    }

    int modeButton = NEXT_BUTTON_MODE;

    public int getButtonMode() {
        return modeButton;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
