package com.mipt.artem.primefactorization.factorization;

import android.util.Log;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by artem on 23.07.16.
 */
public class FactorAlgorithm {
    private static final String TAG = "FactorAlgorithm";

    public  static List start(BigInteger n, ProgressChangeListener progressChangerListener) {
        BigInteger two = BigInteger.valueOf(2);

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }

        PercentCalculator percentCalculator = new PercentCalculator(n, progressChangerListener);
        LinkedList fs = new LinkedList();


        while (n.mod(two).equals(BigInteger.ZERO)) {
            fs.add(two);
            n = n.divide(two);
            percentCalculator.updatePercentAfterNewDiver(n);
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger f = BigInteger.valueOf(3);
            while (f.multiply(f).compareTo(n) <= 0) {
                if(progressChangerListener.isCanceled()) {
                    return null;
                }
                if (n.mod(f).equals(BigInteger.ZERO))
                {
                    fs.add(f);
                    n = n.divide(f);
                    percentCalculator.updatePercentAfterNewDiver(n);
                } else {
                    f = f.add(two);
                    //percentCalculator.updatePercentAfterNewDiver(n);
                }

            }
            fs.add(n);
        }
        return fs;
    }

    private static class PercentCalculator {
        private static final int STEP_PERCENT = 2;
        private static final int WHOLE_NUMBER_OF_PERCENTS = 100;
        private ProgressChangeListener mProgressChangerListener;
        private BigInteger mStartValue;
        private BigInteger mCurrentValue;
        private BigInteger mNextStepProgressUpdateValue;
        private int mCurrentPercentUpdateValue;

        public PercentCalculator(BigInteger n, ProgressChangeListener progressChangerListener) {
            mStartValue = n;
            mProgressChangerListener = progressChangerListener;
        }

        public void updatePercentAfterNewDiver(BigInteger n) {
            mCurrentValue = n;
            if(mProgressChangerListener != null) {
                int progress = 100 - getPartInPercentFromNumber(n, mStartValue);
                Log.d(TAG, "updatePercentAfterNewDiver = " + Integer.toString(progress));
                mProgressChangerListener.setProgress(progress);
            }
        }

        private int getPartInPercentFromNumber(BigInteger partNumber, BigInteger wholeNumber) {
            BigInteger percent = partNumber.multiply(BigInteger.valueOf(WHOLE_NUMBER_OF_PERCENTS))
                    .divide(wholeNumber);
            int percentCount = percent.intValue();
            return percentCount;
        }
    }




}
