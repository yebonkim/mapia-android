package com.mapia.network;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class NetworkStatusManager {
    private static boolean isAvailableNetwork = true;
    public static boolean getIsAvailableNetwork(){
        try{
            boolean bool = isAvailableNetwork;
            return bool;
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
        return false;   //수정 필요
    }



}
