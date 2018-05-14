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

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class ProductExportDialog extends Dialog {

    private UsbFile root;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    @BindView(R.id.spChooseSaveLocation)
    MPosSpinner spChooseSaveLocation;
    @BindView(R.id.spExportItem)
    MPosSpinner spExportItem;
    @BindView(R.id.spDocType)
    MPosSpinner spDocType;
    @BindView(R.id.tvFilePath)
    TextView tvFilePath;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnExport)
    MpButton btnExport;
    private String path;
    private int pos, docType = EXCEL;
    private Context context;
    private boolean fromUsb = false;

    public ProductExportDialog(@NonNull Context context, DatabaseManager databaseManager) {
        super(context);
        this.context = context;
        View dialogView = getLayoutInflater().inflate(R.layout.choose_item_for_export_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        String[] items = {context.getString(R.string.price_list), context.getString(R.string.vendors), context.getString(R.string.products), context.getString(R.string.template)};
        String[] strings = {context.getString(R.string.save_to_internal_memory), context.getString(R.string.save_to_usb)};
        String[] type = {context.getString(R.string.excel), context.getString(R.string.pdf_name)};
        spDocType.setAdapter(type);
        spChooseSaveLocation.setAdapter(strings);
        spExportItem.setAdapter(items);
        spExportItem.setItemSelectionListener((view, position) -> {
            pos = position;
            if (position != 3) {
                spDocType.setVisibility(View.VISIBLE);
            } else spDocType.setVisibility(View.GONE);
        });

        spChooseSaveLocation.setItemSelectionListener((view, position) -> {
            if (position == 0) {
                fromUsb = false;
                tvFilePath.setVisibility(View.VISIBLE);
            } else {
                checkDevice();
                fromUsb = true;
                tvFilePath.setVisibility(View.GONE);
            }
        });
        tvFilePath.setOnClickListener(view -> {
            DialogProperties properties = new DialogProperties();
            properties.selection_mode = DialogConfigs.SINGLE_MODE;
            properties.selection_type = DialogConfigs.DIR_SELECT;
            properties.root = new File(DialogConfigs.DEFAULT_DIR);
            properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
            properties.offset = new File(DialogConfigs.DEFAULT_DIR);
            properties.extensions = null;
            FilePickerDialog dialog = new FilePickerDialog(context, properties);
            dialog.setTitle(getContext().getString(R.string.select_a_directory));
            dialog.setDialogSelectionListener(files -> {
                path = files[0];
                tvFilePath.setText(path);
                tvFilePath.setError(null);
            });
            dialog.show();
        });
        spDocType.setItemSelectionListener((view, position) -> {
            if (position == 0)
                docType = EXCEL;
            else docType = PDF;
        });
        btnCancel.setOnClickListener(view -> dismiss());
        btnExport.setOnClickListener(view -> {
            if (fromUsb) {
                findRootDirectory();
                switch (pos) {
                    case 0:
                        if (docType == EXCEL) {
                            ExportUtils.exportPriceListToUsb(context, root, databaseManager);
                        } else {
                            ExportUtils.exportPriceListPdfToUsb(context, root, databaseManager);
                        }
                        break;
                    case 1:
                        if (docType == EXCEL) {
                            ExportUtils.exportVendorsToUsb(context, root, databaseManager);
                        } else {
                            ExportUtils.exportVendorsPdfToUsb(context, root, databaseManager);
                        }
                        break;
                    case 2:
                        if (docType == EXCEL) {
                            ExportUtils.exportProductsToUsb(context, root, databaseManager);
                        } else {
                            ExportUtils.exportProductsPdfToUsb(context, root, databaseManager);
                        }
                        break;
                    case 3:
                        ExportUtils.exportProductVendorTemplateToUsb(context, root);
                        break;
                }
                dismiss();
            } else {
                if (!tvFilePath.getText().toString().isEmpty()) {
                    switch (pos) {
                        case 0:
                            if (docType == EXCEL) {
                                ExportUtils.exportPriceList(context, path, databaseManager);
                            } else {
                                ExportUtils.exportPriceListPdf(context, path, databaseManager);
                            }
                            break;
                        case 1:
                            if (docType == EXCEL) {
                                ExportUtils.exportVendors(context, path, databaseManager);
                            } else {
                                ExportUtils.exportVendorsPdf(context, path, databaseManager);
                            }
                            break;
                        case 2:
                            if (docType == EXCEL) {
                                ExportUtils.exportProducts(context, path, databaseManager);
                            } else {
                                ExportUtils.exportProductsPdf(context, path, databaseManager);
                            }
                            break;
                        case 3:
                            ExportUtils.exportProductVendorTemplate(context, path);
                            break;
                    }
                    dismiss();
                } else tvFilePath.setError(context.getString(R.string.choose_file_location));
            }
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
                    } else
                        Toast.makeText(context, "Not enough place in memory", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(context, "Device hasn\'t got permission", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            spChooseSaveLocation.setSelection(0);
            Toast.makeText(context, "Please, connect USB storage device", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Please, connect USB storage device", Toast.LENGTH_SHORT).show();
        }
    }
}
