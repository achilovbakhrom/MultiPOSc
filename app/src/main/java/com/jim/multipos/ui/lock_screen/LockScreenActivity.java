package com.jim.multipos.ui.lock_screen;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpNumPad;
import com.jim.multipos.BuildConfig;
import com.jim.multipos.DeviceAdminReceiver;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.admin_auth_signin.AdminAuthSigninActivity;
import com.jim.multipos.ui.lock_screen.dialog.PlugRefreshDialog;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.main_order_events.NotPermissionUsbEvent;
import com.jim.multipos.utils.rxevents.main_order_events.RebootedEvent;
import com.jim.multipos.utils.rxevents.main_order_events.UsbConnectedWithPermissionEvent;
import com.jim.multipos.utils.usb_barcode.USBService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static android.app.admin.DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED;
import static android.app.admin.DevicePolicyManager.PERMISSION_POLICY_AUTO_GRANT;


/**
 * Created by DEV on 04.08.2017.
 */

public class LockScreenActivity extends BaseActivity implements LockScreenView {
    private final static int EMPTY = 0;
    private final static int WRONG_PASS = 1;
    @Inject
    LockScreenPresenter presenter;
    @BindView(R.id.btnClear)
    MpNumPad btnClear;
    @BindView(R.id.btnLogin)
    MpNumPad btnLogin;
    @BindView(R.id.tvEmployerPassword)
    TextView tvEmployerPassword;
    @BindViews({R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine})
    List<MpNumPad> numPad;
    @BindView(R.id.tvPinOrExit)
    TextView tvPinOrExit;
    @BindView(R.id.tvUnpin)
    TextView tvUnpin;
    @Inject
    RxBus rxBus;
    @Inject
    PreferencesHelper preferencesHelper;
    PlugRefreshDialog plugRefreshDialog;
    int clickedcount = 0;
    private ArrayList<Disposable> subscriptions;
    private HashMap<String, String> hashesWithSerial = (HashMap<String, String>) BuildConfig.LOCAL_SERIAL_HASH;
    private char values[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private Timer timer;
    private String star = "*";
    private String password = "";
    private WarningDialog dialog;
    //FOR SECURE
    private PackageManager mPackageManager;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

    private static List<String> getRuntimePermissions(PackageManager packageManager, String packageName) {
        List<String> permissions = new ArrayList<>();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS); //get the list of all requested permissions
        } catch (PackageManager.NameNotFoundException e) {
            return permissions;
        }

        if (packageInfo != null && packageInfo.requestedPermissions != null) {
            for (String requestedPerm : packageInfo.requestedPermissions) {
                if (isRuntimePermission(packageManager, requestedPerm)) { //keep only runtime permissions
                    permissions.add(requestedPerm);
                }
            }
        }
        return permissions;
    }

    private static boolean isRuntimePermission(PackageManager packageManager, String permission) {
        try {
            PermissionInfo pInfo = packageManager.getPermissionInfo(permission, 0);
            if (pInfo != null) {
                if ((pInfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {

        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen_page);
        ButterKnife.bind(this);
        if (!isMyServiceRunning(USBService.class)) {
            startService(new Intent(this, USBService.class));
        }

//        if(preferencesHelper.getSerialValue().equals("") || preferencesHelper.getRegistrationToken().equals("") || ! hashesWithSerial.get(preferencesHelper.getSerialValue()).equals(SecurityTools.hashPassword(preferencesHelper.getSerialValue()+preferencesHelper.getRegistrationToken()))){
//            addFragment(R.id.flMain,new AuthFragment());
//        }
        mDevicePolicyManager = (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponentName = DeviceAdminReceiver.getComponentName(this);
        mPackageManager = this.getPackageManager();

        if (mDevicePolicyManager.isDeviceOwnerApp(
                getApplicationContext().getPackageName())) {

            mPackageManager.setComponentEnabledSetting(
                    new ComponentName(getApplicationContext(),
                            LockScreenActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please connect our company support, somethink is wrong", Toast.LENGTH_SHORT)
                    .show();
        }


        if (mDevicePolicyManager.isDeviceOwnerApp(getPackageName())) {
            setDefaultCosuPolicies(true);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please connect our company support, some think is wrong", Toast.LENGTH_SHORT)
                    .show();
        }


        final long DELAY = 150;
        for (int i = 0; i < numPad.size(); i++) {
            final int count = i;
            RxView.clicks(numPad.get(i)).subscribe(aVoid -> {
                String text = tvEmployerPassword.getText().toString();
                text = text + values[count];
                tvEmployerPassword.setText(text);
                if (password.length() <= 5)
                    password = password + values[count];
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    tvEmployerPassword.setText(star);
                                    star = tvEmployerPassword.getText().toString();
                                    star = star + "*";
                                });
                            }
                        },
                        DELAY
                );

            });
        }
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof NotPermissionUsbEvent) {
                        if (plugRefreshDialog == null) {
                            plugRefreshDialog = new PlugRefreshDialog(LockScreenActivity.this);
                            plugRefreshDialog.setOnDismissListener(dialogInterface -> {
                                plugRefreshDialog = null;
                            });
                            plugRefreshDialog.show();
                        }
                    } else if (o instanceof UsbConnectedWithPermissionEvent) {
                        if (plugRefreshDialog != null)
                            plugRefreshDialog.dismiss();
                        plugRefreshDialog = null;
                    } else if (o instanceof RebootedEvent) {

                    }
                })
        );
//        tvPinOrExit.setOnClickListener(view -> {
//            setDefaultCosuPolicies(true);
//            if ( mDevicePolicyManager.isDeviceOwnerApp(
//                    getApplicationContext().getPackageName())) {
//                mPackageManager.setComponentEnabledSetting(
//                        new ComponentName(getApplicationContext(),
//                                LockScreenActivity.class),
//                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                        PackageManager.DONT_KILL_APP);
//                ActivityManager am = (ActivityManager) getSystemService(
//                        Context.ACTIVITY_SERVICE);
//                if (am.getLockTaskModeState() ==
//                        ActivityManager.LOCK_TASK_MODE_NONE) {
//                    startLockTask();
//                }
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        R.string.not_lock_whitelisted,Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
        tvUnpin.setOnClickListener(view -> clickedcount++);
        tvUnpin.setOnLongClickListener(view -> {
            if (clickedcount != 10) return true;
//            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
//                    mAdminComponentName,getPackageName());
//            mPackageManager.setComponentEnabledSetting(
//                    new ComponentName(getApplicationContext(), LockScreenActivity.class),
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);

            setDefaultCosuPolicies(false);
            mDevicePolicyManager.clearDeviceOwnerApp(getPackageName());
            stopLockTask();
            finish();
//            ComponentName devAdminReceiver = new ComponentName(this, DeviceAdminReceiver.class);
//            DevicePolicyManager dpm = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
//            dpm.removeActiveAdmin(devAdminReceiver);

//            finish();
            return true;
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Disposable disposable : subscriptions) {
            disposable.dispose();
        }
    }

    @OnClick(R.id.btnClear)
    public void onClear() {
        clear();
    }

    private void clear() {
        tvEmployerPassword.setText("");
        star = "*";
        password = "";
    }

    @Override
    protected void onStart() {
        super.onStart();
        clickedcount = 0;
//        if(!preferencesHelper.getSerialValue().equals("") &&  !preferencesHelper.getRegistrationToken().equals("") &&  hashesWithSerial.get(preferencesHelper.getSerialValue()).equals(SecurityTools.hashPassword(preferencesHelper.getSerialValue()+preferencesHelper.getRegistrationToken())))
        if (preferencesHelper.isAppRunFirstTime()) {
            try {
                Intent intro = new Intent(this, StartConfigurationActivity.class);
                startActivity(intro);
            } catch (Exception o) {
            }
            finish();
        }

        if (mDevicePolicyManager.isDeviceOwnerApp(
                getApplicationContext().getPackageName())) {
            setDefaultCosuPolicies(true);
            mPackageManager.setComponentEnabledSetting(
                    new ComponentName(getApplicationContext(),
                            LockScreenActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            ActivityManager am = (ActivityManager) getSystemService(
                    Context.ACTIVITY_SERVICE);
            if (am.getLockTaskModeState() ==
                    ActivityManager.LOCK_TASK_MODE_NONE) {
                startLockTask();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.not_lock_whitelisted, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void openFirstConfigure() {
        if (preferencesHelper.isAppRunFirstTime()) {
            try {
                Intent intro = new Intent(this, StartConfigurationActivity.class);
                startActivity(intro);
            } catch (Exception o) {
            }
            finish();
        }
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        presenter.checkPassword(password);
    }

    @Override
    public void successCheck() {
        Intent intent = new Intent(this, MainPosPageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clear();

    }

    @Override
    public void setError(int state) {
        switch (state) {
            case EMPTY:
                dialog = new WarningDialog(this);
                dialog.onlyText(true);
                dialog.setWarningMessage(this.getString(R.string.enter_pass));
                dialog.setOnYesClickListener(view -> {
                    clear();
                    dialog.dismiss();
                });
                dialog.show();
                break;
            case WRONG_PASS:
                dialog = new WarningDialog(this);
                dialog.onlyText(true);
                dialog.setWarningMessage(getString(R.string.wrong_password));
                dialog.setOnYesClickListener(view -> {
                    clear();
                    dialog.dismiss();
                });
                dialog.show();
                break;
        }
    }

    private void setDefaultCosuPolicies(boolean active) {
        // set user restrictions
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, active);
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, active);
        setUserRestriction(UserManager.DISALLOW_ADD_USER, active);
//        setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, active);
        setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, active);
        setUserRestriction(UserManager.DISALLOW_OUTGOING_CALLS, active);


        // disable keyguard and status bar
        mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, active);
        mDevicePolicyManager.setStatusBarDisabled(mAdminComponentName, active);
        // enable STAY_ON_WHILE_PLUGGED_IN
        enableStayOnWhilePluggedIn(active);

        // set system update policy
        if (active) {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
                    SystemUpdatePolicy.createWindowedInstallPolicy(60, 120));
        } else {
            mDevicePolicyManager.setSystemUpdatePolicy(mAdminComponentName,
                    null);
        }


        mDevicePolicyManager.setPermissionPolicy(mAdminComponentName, PERMISSION_POLICY_AUTO_GRANT);

        // set this ActivityContex as OnItemClickListener lock task package

        mDevicePolicyManager.setLockTaskPackages(mAdminComponentName,
                active ? new String[]{getPackageName()} : new String[]{});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            autoGrantRequestedPermissionsToSelf(this);
        }

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        if (active) {
            // set Cosu activity as home intent receiver so that it is started
            // on reboot
            mDevicePolicyManager.addPersistentPreferredActivity(
                    mAdminComponentName, intentFilter, new ComponentName(
                            getPackageName(), LockScreenActivity.class.getName()));
        } else {
            mDevicePolicyManager.clearPackagePersistentPreferredActivities(
                    mAdminComponentName, getPackageName());
        }
    }

    private void setUserRestriction(String restriction, boolean disallow) {
        if (disallow) {
            mDevicePolicyManager.addUserRestriction(mAdminComponentName,
                    restriction);
        } else {
            mDevicePolicyManager.clearUserRestriction(mAdminComponentName,
                    restriction);
        }
    }

    private void enableStayOnWhilePluggedIn(boolean enabled) {
        if (enabled) {
            mDevicePolicyManager.setGlobalSetting(
                    mAdminComponentName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    Integer.toString(BatteryManager.BATTERY_PLUGGED_AC
                            | BatteryManager.BATTERY_PLUGGED_USB
                            | BatteryManager.BATTERY_PLUGGED_WIRELESS));

        } else {


            mDevicePolicyManager.setGlobalSetting(
                    mAdminComponentName,
                    Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                    "0"
            );
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void autoGrantRequestedPermissionsToSelf(Context context) {
        String packageName = context.getPackageName();
        List<String> permissions = getRuntimePermissions(context.getPackageManager(), packageName); //retrieve OnItemClickListener list of requested runtime permissions
        for (String permission : permissions) {
            boolean success = mDevicePolicyManager.setPermissionGrantState(mAdminComponentName, packageName, permission, PERMISSION_GRANT_STATE_GRANTED);

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void enterAsAdmin(View view) {
        startActivity(new Intent(this, AdminAuthSigninActivity.class));
    }
}
