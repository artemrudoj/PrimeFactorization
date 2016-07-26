package com.mipt.artem.primefactorization.factorization;

import android.util.Log;

import com.mipt.artem.primefactorization.utils.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
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

        PercentCalculator percentCalculator = new PercentCalculator(n,progressChangerListener);
        ArrayList fs = new ArrayList<>();


        while (n.mod(two).equals(BigInteger.ZERO)) {
            fs.add(two);
            n = n.divide(two);
            percentCalculator.updateProgressAfterNewDiver(two,n);
        }
        BigInteger temp = Utils.bigIntSqRootCeil(n);

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger f = BigInteger.valueOf(3);
            while (f.compareTo(temp) <= 0) {
                if(progressChangerListener.isCanceled()) {
                    Log.d(TAG, "start: canceled");
                    return null;
                }
                if (n.mod(f).equals(BigInteger.ZERO))
                {
                    fs.add(f);
                    n = n.divide(f);
                    temp  = Utils.bigIntSqRootCeil(n);
                    percentCalculator.updateProgressAfterNewDiver(f,temp);
                } else {
                    f = f.add(two);
                    percentCalculator.updateProgress(f);
                }

            }
            fs.add(n);
        }
        return fs;
    }

    private static class PercentCalculator {
        private static final int WHOLE_NUMBER_OF_PERCENTS = 100;
        private ProgressChangeListener mProgressChangerListener;
        private BigInteger mNextStepProgressUpdateValue;
        private BigInteger mStep;
        private int mCurrentPercentUpdateValue;

        public PercentCalculator(BigInteger n, ProgressChangeListener progressChangerListener) {
            mProgressChangerListener = progressChangerListener;
            mCurrentPercentUpdateValue = 0;
            mNextStepProgressUpdateValue = BigInteger.valueOf(0);
            mStep = calculateNextStep(Utils.bigIntSqRootCeil(n));
        }

        // sqrt(x)/100 * ( 100 - mCurrentPercentUpdateValue)/100
        private BigInteger calculateNextStep(BigInteger n) {
            BigInteger nextStep = n
                    .multiply(BigInteger.valueOf(WHOLE_NUMBER_OF_PERCENTS - mCurrentPercentUpdateValue))
                    .divide(BigInteger.valueOf(WHOLE_NUMBER_OF_PERCENTS * WHOLE_NUMBER_OF_PERCENTS));
            Log.d(TAG, "calculateNextStep: " + nextStep);
            Log.d(TAG, "calculateNextStep: number" + n);
            return nextStep;
        }

        public void updateProgressAfterNewDiver(BigInteger f, BigInteger n) {
            Log.d(TAG, "updateProgressAfterNewDiver: " + f);
            updateProgress(f);
            mStep = calculateNextStep(n);
            mNextStepProgressUpdateValue = f.add(mStep);
        }

        public void updateProgress(BigInteger currentValue) {
//            Log.d(TAG, "updateProgress: " + currentValue);
            if(currentValue.compareTo(mNextStepProgressUpdateValue) > 0) {
                mCurrentPercentUpdateValue += 1;
                if  (mProgressChangerListener != null) {
                    Log.d(TAG, "updateProgress: " + currentValue);
                    Log.d(TAG, "updateProgress: " + Integer.toString(mCurrentPercentUpdateValue));
                    mProgressChangerListener.setProgress(mCurrentPercentUpdateValue);
                }
                mNextStepProgressUpdateValue = mNextStepProgressUpdateValue.add(mStep);
            }
        }

//        private int getPartInPercentFromNumber(BigInteger partNumber, BigInteger wholeNumber) {
//            BigInteger percent = partNumber.multiply(BigInteger.valueOf(WHOLE_NUMBER_OF_PERCENTS))
//                    .divide(wholeNumber);
//            int percentCount = percent.intValue();
//            return percentCount;
//        }
    }




}
