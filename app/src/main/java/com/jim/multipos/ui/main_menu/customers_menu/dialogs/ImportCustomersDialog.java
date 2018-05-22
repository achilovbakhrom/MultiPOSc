package com.jim.multipos.ui.main_menu.customers_menu.dialogs;

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

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImportCustomersDialog extends Dialog {

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnImport)
    MpButton btnImport;
    @BindView(R.id.spImportType)
    MPosSpinner spImportType;
    @BindView(R.id.tvFilePath)
    TextView tvFilePath;
    @BindView(R.id.spFileType)
    MPosSpinner spFileType;
    private String path;
    private boolean fromUsb = false;
    private Context context;
    private DatabaseManager databaseManager;
    private UsbFile root;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    public ImportCustomersDialog(@NonNull Context context, DatabaseManager databaseManager) {
        super(context);
        this.context = context;
        this.databaseManager = databaseManager;
        View dialogView = getLayoutInflater().inflate(R.layout.import_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        String[] strings = {context.getString(R.string.import_from_internal_memory), context.getString(R.string.import_from_usb)};
        String[] fileTypes = {context.getString(R.string.customers)};
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        spImportType.setAdapter(strings);
        spImportType.setItemSelectionListener((view, position) -> {
            if (position == 0) {
                fromUsb = false;
                properties.root = new File(DialogConfigs.DEFAULT_DIR);
            } else {
                fromUsb = true;
                properties.root = new File("/storage");
                checkDevice();
            }
        });
        spFileType.setAdapter(fileTypes);
        tvFilePath.setOnClickListener(view -> {
            FilePickerDialog dialog = new FilePickerDialog(context, properties);
            dialog.setTitle(getContext().getString(R.string.select_the_file_location));
            dialog.setDialogSelectionListener(files -> {
                path = files[0];
                tvFilePath.setText(path);
            });
            dialog.show();
        });
        btnCancel.setOnClickListener(view -> dismiss());
        btnImport.setOnClickListener(view -> {
            if (!tvFilePath.getText().toString().isEmpty()) {
                if (fromUsb) {
                    selectFileForImport();
                } else ExportUtils.importCustomers(context, path, databaseManager);
                dismiss();
            } else tvFilePath.setError(context.getString(R.string.select_file_location));
        });
    }

    private void selectFileForImport() {
        try {
            UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);
            if (devices.length == 1) {
                for (UsbMassStorageDevice device : devices) {
                    try {
                        device.init();
                        FileSystem currentFs = device.getPartitions().get(0).getFileSystem();
                        if (currentFs.getFreeSpace() > 10485760) {
                            root = currentFs.getRootDirectory();
                            UsbFile[] files = root.listFiles();
                            for (UsbFile file : files) {
                                if (!file.isDirectory()) {
                                    if (path.contains(file.getName())) {
                                        ExportUtils.importCustomersFromUsb(context, file, databaseManager);
                                    }
                                }
                            }
                        } else
                            Toast.makeText(context, context.getString(R.string.not_enough_place_in_memory), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(context, context.getString(R.string.device_hasnt_got_permission), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                spImportType.setSelection(0);
                Toast.makeText(context, context.getString(R.string.connect_only_one_device), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            spImportType.setSelection(0);
            Toast.makeText(context, context.getString(R.string.pos_currency), Toast.LENGTH_SHORT).show();
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
            spImportType.setSelection(0);
            Toast.makeText(context, context.getString(R.string.please_connect_usb_drive), Toast.LENGTH_SHORT).show();
        }
    }
}
