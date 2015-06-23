package com.adamitskiy.anatoliy.nobel_prize_winners;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    /*
        Network Connection Check
     */
    public static Boolean checkNetwork(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager)mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {

                if (info.isConnected()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
