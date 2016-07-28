package com.mipt.artem.primefactorization.utils.numbers;

import java.math.BigInteger;

/**
 * Created by artem on 26.07.16.
 */
public class NumberFactory {
    private SimpleMathOperation mHelperNumber;
    static public NumberFactory build(String number) {
        BigInteger testNumber = new BigInteger(number);
        NumberFactory numberFactory = new NumberFactory();
        if (testNumber.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            numberFactory.setHelperNumber(new BigIntegerWarapper(testNumber));
        } else {
            long value = Long.parseLong(number);
            numberFactory.setHelperNumber(new MutableLongWrapper(value));
        }
        return numberFactory;
    }

    private void setHelperNumber(SimpleMathOperation helperNumber) {
        mHelperNumber = helperNumber;
    }

    public SimpleMathOperation valueOf(long number) {
        return mHelperNumber.createObjectOf(Long.toString(number));
    }

    public SimpleMathOperation valueOf(String number) {
        return mHelperNumber.createObjectOf(number);
    }

    public SimpleMathOperation mod(SimpleMathOperation n, SimpleMathOperation two) {
        mHelperNumber = n.copy();
        return mHelperNumber.mod(two);
    }

    public SimpleMathOperation sqrt(SimpleMathOperation n) {
        mHelperNumber = n.copy();
        return mHelperNumber.sqrt(n);
    }
}
