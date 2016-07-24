package com.mipt.artem.primefactorization.factorization;

import java.util.List;

/**
 * Created by artem on 24.07.16.
 */
interface  ProgressChangeListener {
    void setProgress(int progress);

    boolean isCanceled();
}
