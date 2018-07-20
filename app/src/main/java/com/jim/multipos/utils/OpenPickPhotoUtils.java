package com.jim.multipos.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by developer on 06.09.2017.
 */

public class OpenPickPhotoUtils {
    public static final int RESULT_PICK_IMAGE = 555;
    public static final int RESULT_CAPTURE_IMAGE = 777;

    public static void startPhotoPick(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, RESULT_PICK_IMAGE);
    }
}
