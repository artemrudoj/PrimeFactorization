package com.mipt.artem.primefactorization.factorization;


import java.util.List;

/**
 * Created by artem on 23.07.16.
 */
interface ProgressChangerListener {
    void setProgress(int progress);
    void setResult(List result);
}
