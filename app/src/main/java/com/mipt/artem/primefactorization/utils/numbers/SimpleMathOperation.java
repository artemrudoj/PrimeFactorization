package com.mipt.artem.primefactorization.utils.numbers;

import java.math.*;

/**
 * Created by artem on 26.07.16.
 */
public interface  SimpleMathOperation {
    SimpleMathOperation mod(SimpleMathOperation number);
    int compareTo(SimpleMathOperation number);
    SimpleMathOperation divide(SimpleMathOperation number);
    SimpleMathOperation multiply(SimpleMathOperation number);
    SimpleMathOperation createObjectOf(String number);
    SimpleMathOperation sqrt(SimpleMathOperation number);
    SimpleMathOperation add(SimpleMathOperation number);
    SimpleMathOperation copy();
}
