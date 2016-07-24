package com.mipt.artem.primefactorization.cache;

import java.util.List;

/**
 * Created by artem on 24.07.16.
 */
public interface CacheRepository {
    void save(String number, List dividers);
    List getDividers(String number);
}
