package com.mipt.artem.primefactorization.factorization;


/**
 * Created by artem on 24.07.16.
 */
interface ProcessController {
    void setProgress(int progress);
    boolean isCanceled();
}
