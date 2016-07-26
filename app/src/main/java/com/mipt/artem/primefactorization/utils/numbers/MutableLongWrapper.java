package com.mipt.artem.primefactorization.utils.numbers;

/**
 * Created by artem on 26.07.16.
 */
public class MutableLongWrapper implements SimpleMathOperation {

    long value;

    public long getValue() {
        return value;
    }

    @Override
    public SimpleMathOperation mod(SimpleMathOperation number) {
        if(number instanceof MutableLongWrapper) {
            value = value % ((MutableLongWrapper) number).getValue();
            return this;
        } else {
            throw new IllegalArgumentException("number should be MutableLongWrapper");
        }
    }

    @Override
    public int compareTo(SimpleMathOperation number) {
        if(number instanceof MutableLongWrapper) {
            long diff =  value - ((MutableLongWrapper) number).getValue();
            if(diff > 0) {
                return 1;
            } else if(diff < 0) {
                return -1;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("number should be MutableLongWrapper");
        }
    }

    @Override
    public SimpleMathOperation divide(SimpleMathOperation number) {
        if(number instanceof MutableLongWrapper) {
            value = value / ((MutableLongWrapper) number).getValue();
            return this;
        } else {
            throw new IllegalArgumentException("number should be MutableLongWrapper");
        }
    }

    @Override
    public SimpleMathOperation multiply(SimpleMathOperation number) {
        if(number instanceof MutableLongWrapper) {
            value = value * ((MutableLongWrapper) number).getValue();
            return this;
        } else {
            throw new IllegalArgumentException("number should be MutableLongWrapper");
        }
    }

    public MutableLongWrapper(long value) {
        this.value = value;
    }

    @Override
    public SimpleMathOperation valueOf(int number) {
        return new MutableLongWrapper(number);
    }
}
