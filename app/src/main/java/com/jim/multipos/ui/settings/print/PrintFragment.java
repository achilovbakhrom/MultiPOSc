package com.jim.multipos.ui.settings.print;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.fragment.ProductAddEditFragment;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.printer.CheckPrinter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import lombok.Getter;

import static android.app.Activity.RESULT_OK;
import static com.jim.multipos.utils.OpenPickPhotoUtils.RESULT_PICK_IMAGE;

public class PrintFragment extends BaseFragment implements PrintView {

    @BindView(R.id.tvConnectionStatus)
    TextView tvConnectionStatus;
    @BindView(R.id.flCheckConnection)
    FrameLayout flCheckConnection;
    @BindView(R.id.llHaveLogoPicture)
    LinearLayout llHaveLogoPicture;
    @BindView(R.id.chbGaveLogo)
    MpCheckbox chbGaveLogo;
    @BindView(R.id.flOpenFolderForLogo)
    FrameLayout flOpenFolderForLogo;
    @BindView(R.id.tvLogoPath)
    TextView tvLogoPath;
    @BindView(R.id.llHintAboutSerDis)
    LinearLayout llHintAboutSerDis;
    @BindView(R.id.flPrint)
    FrameLayout flPrint;
    @BindView(R.id.llHideIfWithOutLogo)
    LinearLayout llHideIfWithOutLogo;
    @BindView(R.id.flHideIfWithOutLogoSecLine)
    FrameLayout flHideIfWithOutLogoSecLine;
    @BindView(R.id.flHideIfWithOutLogoLine)
    FrameLayout flHideIfWithOutLogoLine;
    @BindView(R.id.chbHideAbout)
    MpCheckbox chbHideAbout;

    @Inject
    PrintPresenter presenter;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    DatabaseManager databaseManager;

    @Inject
    RxPermissions permissions;

    @Override
    protected int getLayout() {
        return R.layout.print_fragment;
    }
    CheckPrinter checkPrinter;
    @Override
    protected void init(Bundle savedInstanceState) {

        checkPrinter = new CheckPrinter(getActivity(),preferencesHelper,databaseManager);
        checkPrinter.connectDevice();

        if(preferencesHelper.getUriPathCheckPicture().toString().equals("")){
            tvLogoPath.setText("../MultiPos.jpg");
        }else {
            tvLogoPath.setText("../"+getFileName(preferencesHelper.getUriPathCheckPicture()));
        }

        chbGaveLogo.setCheckedChangeListener(isChecked -> {
            if(isChecked) {
                showSelectPathPanel();
            }else {
                hideSelectPathPanel();
            }
            preferencesHelper.setPrintPictureInCheck(isChecked);
        });

        chbHideAbout.setCheckedChangeListener(isChecked -> {
            preferencesHelper.setHintAbout(isChecked);
        });

        chbGaveLogo.setChecked(preferencesHelper.isPrintPictureInCheck());
        chbHideAbout.setChecked(preferencesHelper.isHintAbout());

        llHaveLogoPicture.setOnClickListener(view -> {
             chbGaveLogo.setChecked(!preferencesHelper.isPrintPictureInCheck());
        });
        llHintAboutSerDis.setOnClickListener(view -> {
            chbHideAbout.setChecked(!preferencesHelper.isHintAbout());
        });



        if(checkPrinter.checkConnect()){
            tvConnectionStatus.setText(getContext().getString(R.string.connected));
            tvConnectionStatus.setTextColor(Color.parseColor("#23d730"));
        }else {
            tvConnectionStatus.setText(getContext().getString(R.string.disconnected));
            tvConnectionStatus.setTextColor(Color.parseColor("#9a9a9a"));
        }


        flCheckConnection.setOnClickListener(view -> {
            checkPrinter = new CheckPrinter(getActivity(),preferencesHelper,databaseManager);
            checkPrinter.connectDevice();
            if(checkPrinter.checkConnect()){
                tvConnectionStatus.setText(R.string.connected);
                tvConnectionStatus.setTextColor(Color.parseColor("#23d730"));
            }else {
                tvConnectionStatus.setText(R.string.disconnected);
                tvConnectionStatus.setTextColor(Color.parseColor("#9a9a9a"));
            }
        });

        flPrint.setOnClickListener(view -> {
            if(checkPrinter.checkConnect()){
                tvConnectionStatus.setText(getContext().getString(R.string.connected));
                tvConnectionStatus.setTextColor(Color.parseColor("#23d730"));
                checkPrinter.examplePrint(getActivity());
            }else {
                tvConnectionStatus.setText(getContext().getString(R.string.disconnected));
                tvConnectionStatus.setTextColor(Color.parseColor("#9a9a9a"));

            }
        });
        flOpenFolderForLogo.setOnClickListener(view -> {
            permissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    OpenPickPhotoUtils.startPhotoPick(PrintFragment.this);
                }
            });
        });
    }

    void hideSelectPathPanel(){
        llHideIfWithOutLogo.setVisibility(View.GONE);
        flHideIfWithOutLogoLine.setVisibility(View.GONE);
        flHideIfWithOutLogoSecLine.setVisibility(View.GONE);
    }
    void showSelectPathPanel(){
        llHideIfWithOutLogo.setVisibility(View.VISIBLE);
        flHideIfWithOutLogoLine.setVisibility(View.VISIBLE);
        flHideIfWithOutLogoSecLine.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
            Uri imageUri = data.getData();
            preferencesHelper.setUriPathCheckPicture(imageUri);
            tvLogoPath.setText("../"+getFileName(preferencesHelper.getUriPathCheckPicture()));
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals(getContext().getString(R.string.content))) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
