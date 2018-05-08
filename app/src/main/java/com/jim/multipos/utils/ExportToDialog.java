package com.jim.multipos.utils;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.utils.ExportUtils.EXCEL;


public class ExportToDialog extends Dialog {

    @BindView(R.id.etFileName)
    MpEditText etFileName;
    @BindView(R.id.tvFilePath)
    TextView tvFilePath;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.tvExportFormat)
    TextView tvExportFormat;
    @BindView(R.id.spChooseSaveLocation)
    MPosSpinner spChooseSaveLocation;
    private String path;
    private int saveMode = 0;
    private Context context;
    private OnExportListener listener;
    private UsbFile root;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    public ExportToDialog(@NonNull Context context, int mode, String fileName, OnExportListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        View dialogView = getLayoutInflater().inflate(R.layout.export_excel_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm");
        if (mode == EXCEL)
            tvExportFormat.setText(context.getString(R.string.xls));
        else tvExportFormat.setText(context.getString(R.string.pdf));
        etFileName.setText(fileName + " " + dateFormat.format(new Date(System.currentTimeMillis())));
        String[] strings = {context.getString(R.string.save_to_internal_memory), context.getString(R.string.save_to_usb)};
        spChooseSaveLocation.setAdapter(strings);
        spChooseSaveLocation.setItemSelectionListener((view, position) -> {
            if (position == 0) {
                saveMode = 0;
                tvFilePath.setVisibility(View.VISIBLE);
            } else {
                tvFilePath.setVisibility(View.GONE);
                saveMode = 1;
                checkDevice();
            }
        });
        tvFilePath.setOnClickListener(view -> listener.onFilePickerClicked());
        btnCancel.setOnClickListener(view -> dismiss());
        btnSave.setOnClickListener(view -> {
            if (saveMode == 0)
                listener.onSaveClicked(etFileName.getText().toString(), path);
            else findRootDirectory();
            dismiss();
        });
    }

    private void findRootDirectory() {
        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);
        if (devices.length > 0) {
            for (UsbMassStorageDevice device : devices) {
                try {
                    device.init();
                    FileSystem currentFs = device.getPartitions().get(0).getFileSystem();
                    if (currentFs.getFreeSpace() > 10485760) {
                        root = currentFs.getRootDirectory();
                        listener.onSaveToUSBClicked(etFileName.getText().toString(), root);
                    } else
                        Toast.makeText(context, "Not enough place in memory", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(context, "Device hasn\'t got permission", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            spChooseSaveLocation.setSelection(0);
            Toast.makeText(context, "Please, connect USB drive", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDevice() {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);
        if (devices.length > 0) {
            for (UsbMassStorageDevice device : devices) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                if (usbManager != null) {
                    usbManager.requestPermission(device.getUsbDevice(), permissionIntent);
                }
            }
        } else {
            spChooseSaveLocation.setSelection(0);
            Toast.makeText(context, "Please, connect USB drive", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPath(String[] path) {
        this.path = path[0];
        tvFilePath.setText(path[0]);
    }


    public interface OnExportListener {
        void onFilePickerClicked();

        void onSaveToUSBClicked(String filename, UsbFile root);

        void onSaveClicked(String fileName, String path);
    }

}
