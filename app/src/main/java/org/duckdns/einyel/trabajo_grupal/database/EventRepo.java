package org.duckdns.einyel.trabajo_grupal.database;

import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;

import io.reactivex.Observable;

public interface EventRepo {

    public Observable<List<MockEvent>> getAll();
}