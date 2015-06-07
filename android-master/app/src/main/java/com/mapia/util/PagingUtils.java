package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 6..
 */

public class PagingUtils
{
    public static int getCommonGridPagingSize() {
        if ("Y".equals(DataSaveUtils.getDataSaveYn())) {
            return 15;
        }
        return 24;
    }

    public static int getCommonListPagingSize() {
        if ("Y".equals(DataSaveUtils.getDataSaveYn())) {
            return 20;
        }
        return 30;
    }
}