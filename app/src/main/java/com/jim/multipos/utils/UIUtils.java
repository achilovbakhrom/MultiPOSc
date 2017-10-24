package com.jim.multipos.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by bakhrom on 10/18/17.
 */

public class UIUtils {

    public static void showAlert(Context context, String positiveButtonText, String negativeButtonText, String title, String message, AlertListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    if (listener != null) {
                        listener.onPositiveButtonClicked();
                    }
                })
                .setNegativeButton(negativeButtonText, (dialog, id) -> {
                    if (listener != null) {
                        listener.onNegativeButtonClicked();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlert(Context context, String buttonText, String title, String message, SingleButtonAlertListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, (dialog, which) -> {
                    if (listener != null) {
                        listener.onButtonClicked();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public interface AlertListener {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    public interface SingleButtonAlertListener {
        void onButtonClicked();
    }

    public static void closeKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void makeToast(Context context, String text, int toastType) {
        Toast.makeText(context, text, toastType).show();
    }
}
