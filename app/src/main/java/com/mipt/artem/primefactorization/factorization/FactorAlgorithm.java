package com.mipt.artem.primefactorization.factorization;

import android.util.Log;

import com.mipt.artem.primefactorization.utils.numbers.NumberFactory;
import com.mipt.artem.primefactorization.utils.numbers.SimpleMathOperation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by artem on 23.07.16.
 */
public class FactorAlgorithm {
    private static final String TAG = "FactorAlgorithm";

    public  static List start(String number, ProgressChangeListener progressChangerListener) {


        NumberFactory numberFactory = NumberFactory.build(number);
        SimpleMathOperation n = numberFactory.valueOf(number);

        SimpleMathOperation two = numberFactory.valueOf(2);

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }

        PercentCalculator percentCalculator = new PercentCalculator(n, progressChangerListener, numberFactory);
        ArrayList fs = new ArrayList<>();


        SimpleMathOperation zero = numberFactory.valueOf(0);

        while (numberFactory.mod(n, two).compareTo(zero) == 0) {
            fs.add(two);
            Log.d(TAG, "start: 1" + n);
            n = n.divide(two);
            Log.d(TAG, "start: 2" + n);
            percentCalculator.updateProgressAfterNewDiver(two,n);
        }
        SimpleMathOperation temp = numberFactory.sqrt(n);

        if (n.compareTo(numberFactory.valueOf(1)) > 0) {
            SimpleMathOperation f = numberFactory.valueOf(3);
            while (f.compareTo(temp) <= 0) {
                if(progressChangerListener.isCanceled()) {
                    Log.d(TAG, "start: canceled");
                    return null;
                }
                if (numberFactory.mod(n,f).compareTo(numberFactory.valueOf(0)) == 0) {
                    fs.add(f.copy());
                    n = n.divide(f);
                    temp  = numberFactory.sqrt(n);
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
        private SimpleMathOperation mNextStepProgressUpdateValue;
        private SimpleMathOperation mStep;
        private int mCurrentPercentUpdateValue;
        private NumberFactory mNumberFactory;

        public PercentCalculator(SimpleMathOperation n, ProgressChangeListener progressChangerListener, NumberFactory numberFactory) {
            mNumberFactory = numberFactory;
            mProgressChangerListener = progressChangerListener;
            mCurrentPercentUpdateValue = 0;
            mNextStepProgressUpdateValue = mNumberFactory.valueOf(0);
            mStep = calculateNextStep(mNumberFactory.sqrt(n));
        }

        // sqrt(x)/100 * ( 100 - mCurrentPercentUpdateValue)/100
        private SimpleMathOperation calculateNextStep(SimpleMathOperation n) {
            SimpleMathOperation temp = n.copy();
            SimpleMathOperation nextStep = temp
                    .multiply(mNumberFactory.valueOf(WHOLE_NUMBER_OF_PERCENTS - mCurrentPercentUpdateValue))
                    .divide(mNumberFactory.valueOf(WHOLE_NUMBER_OF_PERCENTS * WHOLE_NUMBER_OF_PERCENTS));
            Log.d(TAG, "calculateNextStep: " + nextStep);
            Log.d(TAG, "calculateNextStep: number" + n);
            return nextStep;
        }

        public void updateProgressAfterNewDiver(SimpleMathOperation f, SimpleMathOperation n) {
            Log.d(TAG, "updateProgressAfterNewDiver: " + f);
            updateProgress(f);
            mStep = calculateNextStep(n);
            SimpleMathOperation temp = f.copy();
            mNextStepProgressUpdateValue = temp.add(mStep);
        }

        public void updateProgress(SimpleMathOperation currentValue) {
//            Log.d(TAG, "updateProgress: " + currentValue);
            if (currentValue.compareTo(mNextStepProgressUpdateValue) > 0) {
                mCurrentPercentUpdateValue += 1;
                if (mCurrentPercentUpdateValue >= WHOLE_NUMBER_OF_PERCENTS - 1) {
                    mCurrentPercentUpdateValue =  WHOLE_NUMBER_OF_PERCENTS - 1;
                }
                if (mProgressChangerListener != null) {
                    Log.d(TAG, "updateProgress: " + currentValue);
                    Log.d(TAG, "updateProgress: " + Integer.toString(mCurrentPercentUpdateValue));
                    mProgressChangerListener.setProgress(mCurrentPercentUpdateValue);
                }
                mNextStepProgressUpdateValue = mNextStepProgressUpdateValue.add(mStep);
            }
        }
    }




}
