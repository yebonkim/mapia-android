package com.mapia.util;

//import org.apache.commons.lang3.StringUtils;

public class DataSaveUtils
{
    private static String dataSaveYn;

    static {
        DataSaveUtils.dataSaveYn = null;
    }

    public static String getDataSaveYn() {
//        if (StringUtils.isNotBlank(DataSaveUtils.dataSaveYn)) {
//            return DataSaveUtils.dataSaveYn;
//        }
        DataSaveUtils.dataSaveYn = PreferenceUtils.getPreference("dataSaveYn");
//        if (StringUtils.isNotBlank(DataSaveUtils.dataSaveYn)) {
//            return DataSaveUtils.dataSaveYn;
//        }
        return "N";
    }

    public static void setDataSaveYn(final String dataSaveYn) {
        PreferenceUtils.putPreference("dataSaveYn", DataSaveUtils.dataSaveYn = dataSaveYn);
    }
}