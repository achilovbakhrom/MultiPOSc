package com.jim.multipos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jim.mpviews.MpSpinner;
import com.jim.multipos.data.db.model.intosystem.Activatable;
import com.jim.multipos.data.db.model.intosystem.Editable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by Developer on 5/25/17.
 */

public class CommonUtils {

    public static <T> List<List<T>> combination(List<T> list) {
        if (list.size() == 0) {
            List<List<T>> result = new ArrayList<>();
            result.add(new ArrayList<T>());
            return result;
        }
        List<List<T>> returnMe = new ArrayList<>();
        T firstElement = list.remove(0);
        List<List<T>> recursiveReturn = combination(list);
        for (List<T> li : recursiveReturn) {

            for (int index = 0; index <= li.size(); index++) {
                List<T> temp = new ArrayList<>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }

        }
        return returnMe;
    }

    public static boolean isEmailValid(String email) {
//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        @SuppressLint("WrongConstant") Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public static <T extends Editable>  List<T> getAllNewVersionPlusId(List<T> entityList, String id){
        List<T> modifyList = new ArrayList<>();
//        for (T entity: entityList){
//            if(entity.isNewVersion()||entity.getId().equals(id)){
//                modifyList.add(entity);
//            }
//        }
        return modifyList;
    }

    public static  <T extends Editable>  List<T>  getAllActiveNewVersionPlusId(List<T> entityList, String id){
        List<T> modifyList = new ArrayList<>();
//        for (T entity: entityList){
//            if((entity.isNewVersion()&&entity.isActive())||entity.getId().equals(id)){
//                modifyList.add(entity);
//            }
//        }
        return modifyList;
    }

    public static  <T extends Activatable>  List<T>  getAllActivePlusId(List<T> entityList, String id){
        List<T> modifyList = new ArrayList<>();
        for (T entity: entityList){
            if(entity.isActive()||entity.getId().equals(id)){
                modifyList.add(entity);
            }
        }
        return modifyList;
    }
    public static  <T extends Activatable>  int  getPositionWithId(List<T> entityList, String id){
        int postion = 0;
        for (int i = 0; i<entityList.size(); i++){
            if(entityList.get(i).getId().equals(id)){
                postion = i;
            }
        }
        return postion;
    }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (NullPointerException e){
            return contentUri.getPath();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
