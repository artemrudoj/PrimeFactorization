package com.mipt.artem.primefactorization.factorization;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by artem on 23.07.16.
 */
public class FactorAlgorithm {
    public  static List start(BigInteger n, ProgressChangerListener progressChangerListener) {
        BigInteger two = BigInteger.valueOf(2);
        LinkedList fs = new LinkedList();

        if (n.compareTo(two) < 0)
        {
            throw new IllegalArgumentException("must be greater than one");
        }

        while (n.mod(two).equals(BigInteger.ZERO))
        {
            fs.add(two);
            n = n.divide(two);
        }

        if (n.compareTo(BigInteger.ONE) > 0)
        {
            BigInteger f = BigInteger.valueOf(3);
            while (f.multiply(f).compareTo(n) <= 0)
            {
                if (n.mod(f).equals(BigInteger.ZERO))
                {
                    fs.add(f);
                    n = n.divide(f);
                }
                else
                {
                    f = f.add(two);
                }

            }
            fs.add(n);
        }

        return fs;
    }
}
