package com.mipt.artem.primefactorization.utils.numbers;

import java.nio.charset.Charset;

/**
 * Created by artem on 26.07.16.
 */
public class NumberFactory {
    private SimpleMathOperation mHelperNumber;
    static public NumberFactory build(String number) {
        long value = Long.parseLong(number);
        NumberFactory numberFactory = new NumberFactory();
        numberFactory.setHelperNumber(new MutableLongWrapper(value));
        return numberFactory;
    }

    private void setHelperNumber(SimpleMathOperation helperNumber) {
        mHelperNumber = helperNumber;
    }

    public SimpleMathOperation valueOf(long number) {
        return mHelperNumber.valueOf(number);
    }

    public SimpleMathOperation valueOf(String number) {

        return mHelperNumber.valueOf(Long.parseLong(number));
    }

    public SimpleMathOperation mod(SimpleMathOperation n, SimpleMathOperation two) {
        mHelperNumber = n.copy();
        mHelperNumber.mod(two);
        return mHelperNumber;
    }

    public SimpleMathOperation sqrt(SimpleMathOperation n) {
        mHelperNumber = n.copy();
        return mHelperNumber.sqrt(n);
    }
}
