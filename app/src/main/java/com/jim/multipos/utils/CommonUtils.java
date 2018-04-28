package com.jim.multipos.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.intosystem.Activatable;
import com.jim.multipos.data.db.model.intosystem.Editable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDiscountTypeName(Context context, int discount_abr){
        String[] discountUsedTypes = context.getResources().getStringArray(R.array.discount_amount_types);
        if(discount_abr== Discount.PERCENT){
            return discountUsedTypes[0];
        }else if(discount_abr == Discount.VALUE){
            return discountUsedTypes[1];
        }
        return "NoN";
    }
    public static String getServiceTypeName(Context context, int service_abr){
        String[] serviceUsedTypes = context.getResources().getStringArray(R.array.service_fee_type);

        if(service_abr== ServiceFee.PERCENT){
            return serviceUsedTypes[0];
        }else if(service_abr == ServiceFee.VALUE){
            return serviceUsedTypes[1];
        }
        return "NoN";
    }

    public static int[] getDateDifferenceInDDMMYYYY(Date from, Date to) {

        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;

        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        return new int[]{year, month, day};

    }
}
