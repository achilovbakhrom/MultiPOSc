package com.jim.multipos.ui.main_menu.product_menu.dialogs;

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
import com.jim.multipos.utils.RxBus;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImportDialog extends Dialog {

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
    private int fileType = 0;
    private String path;
    private boolean fromUsb = false;
    private Context context;
    private DatabaseManager databaseManager;
    private RxBus rxBus;
    private UsbFile root;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    public ImportDialog(@NonNull Context context, DatabaseManager databaseManager, RxBus rxBus) {
        super(context);
        this.context = context;
        this.databaseManager = databaseManager;
        this.rxBus = rxBus;
        View dialogView = getLayoutInflater().inflate(R.layout.import_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        String[] strings = {context.getString(R.string.import_from_internal_memory), context.getString(R.string.import_from_usb)};
        String[] fileTypes = {context.getString(R.string.vendors), context.getString(R.string.products)};
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        spFileType.setAdapter(fileTypes);
        spFileType.setItemSelectionListener((view, position) -> fileType = position);
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
        tvFilePath.setOnClickListener(view -> {
            FilePickerDialog dialog = new FilePickerDialog(context, properties);
            dialog.setTitle(getContext().getString(R.string.select_the_file_location));
            dialog.setNegativeBtnName(context.getString(R.string.cancel));
            dialog.setPositiveBtnName(context.getString(R.string.select));
            dialog.setDialogSelectionListener(files -> {
                path = files[0];
                tvFilePath.setText(path);
            });
            dialog.show();
        });
        btnCancel.setOnClickListener(view -> dismiss());
        btnImport.setOnClickListener(view -> {
            if (!tvFilePath.getText().toString().isEmpty()) {
                dismiss();
                if (fromUsb) {
                    selectFileForImport();
                } else if (fileType == 1)
                    ExportUtils.importProducts(context, path, databaseManager, rxBus);
                else
                    ExportUtils.importVendors(context, path, databaseManager);
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
                                        if (fileType == 1)
                                            ExportUtils.importProductsFromUsb(context, file, databaseManager, rxBus);
                                        else
                                            ExportUtils.importVendorsFromUsb(context, file, databaseManager);
                                    }
                                }
                            }
                        } else
                            Toast.makeText(context, R.string.not_enough_place_in_memory, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(context, R.string.device_hasnt_got_permission, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                spImportType.setSelection(0);
                Toast.makeText(context, R.string.connect_only_one_device, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            spImportType.setSelection(0);
            Toast.makeText(context, R.string.connect_usb_device, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDevice() {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);
        if (devices.length > 0) {
            for (UsbMassStorageDevice device : devices) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                if (usbManager != null) {
                    if (usbManager.hasPermission(device.getUsbDevice()))
                        usbManager.requestPermission(device.getUsbDevice(), permissionIntent);
                }
            }
        } else {
            spImportType.setSelection(0);
            Toast.makeText(context, R.string.connect_usb_device, Toast.LENGTH_SHORT).show();
        }
    }
}
