package com.mipt.artem.primefactorization.utils.numbers;

import com.mipt.artem.primefactorization.utils.Utils;

import java.math.BigInteger;

/**
 * Created by artem on 28.07.16.
 */
public class BigIntegerWarapper  implements SimpleMathOperation {

    BigInteger getValue() {
        return value;
    }

    BigInteger value;
    public BigIntegerWarapper(BigInteger bigInteger) {
        value = bigInteger;
    }

    @Override
    public SimpleMathOperation mod(SimpleMathOperation number) {
        if (number instanceof BigIntegerWarapper) {
            return new BigIntegerWarapper(value.mod(((BigIntegerWarapper) number).getValue()));
        } else {
            throw new IllegalArgumentException("number must be BigIntegerWarapper");
        }
    }

    @Override
    public int compareTo(SimpleMathOperation number) {
        if (number instanceof BigIntegerWarapper) {
            return value.compareTo(((BigIntegerWarapper) number).getValue());
        } else {
            throw new IllegalArgumentException("number must be BigIntegerWarapper");
        }
    }

    @Override
    public SimpleMathOperation divide(SimpleMathOperation number) {
        if (number instanceof BigIntegerWarapper) {
            return new BigIntegerWarapper(value.divide(((BigIntegerWarapper) number).getValue()));
        } else {
            throw new IllegalArgumentException("number must be BigIntegerWarapper");
        }
    }

    @Override
    public SimpleMathOperation multiply(SimpleMathOperation number) {
        if (number instanceof BigIntegerWarapper) {
            return new BigIntegerWarapper(value.multiply(((BigIntegerWarapper) number).getValue()));
        } else {
            throw new IllegalArgumentException("number must be BigIntegerWarapper");
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public SimpleMathOperation createObjectOf(String number) {
        return new BigIntegerWarapper(new BigInteger(number));
    }

    @Override
    public SimpleMathOperation sqrt(SimpleMathOperation number) {
        if (number instanceof BigIntegerWarapper) {
            BigInteger one = BigInteger.valueOf(1);
            if (((BigIntegerWarapper) number).getValue().compareTo(one) == 0) {
                return new BigIntegerWarapper(one);
            }
            return new BigIntegerWarapper(Utils.bigIntSqRootCeil(((BigIntegerWarapper) number).getValue()));
        } else {
            throw new IllegalArgumentException("number must be BigIntegerWarapper");
        }
    }

    @Override
    public SimpleMathOperation add(SimpleMathOperation number) {
        if (number instanceof BigIntegerWarapper) {
            return new BigIntegerWarapper(value.add(((BigIntegerWarapper) number).getValue()));
        } else {
            throw new IllegalArgumentException("number must be BigIntegerWarapper");
        }
    }

    @Override
    public SimpleMathOperation copy() {
        return new BigIntegerWarapper(value);
    }
}
