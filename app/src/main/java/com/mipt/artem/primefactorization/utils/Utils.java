package com.mipt.artem.primefactorization.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.math.BigInteger;

/**
 * Created by artem on 23.07.16.
 */
public class Utils {

    static public boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    static public String createProgressString(String number) {
        return number + " " + "%";
    }

    public static BigInteger bigIntSqRootCeil(BigInteger x)
            throws IllegalArgumentException {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }
        // square roots of 0 and 1 are trivial and
        // y == 0 will cause a divide-by-zero exception
        if (x == BigInteger.ZERO || x == BigInteger.ONE) {
            return x;
        } // end if
        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;
        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = x.divide(two);
             y.compareTo(x.divide(y)) > 0;
             y = ((x.divide(y)).add(y)).divide(two));
        if (x.compareTo(y.multiply(y)) == 0) {
            return y;
        } else {
            return y.add(BigInteger.ONE);
        }
    } // end bigIntSqRootCeil
} // end class bigIntSqRoot

