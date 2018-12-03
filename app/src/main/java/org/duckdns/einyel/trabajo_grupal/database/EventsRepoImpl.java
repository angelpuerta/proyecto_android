package org.duckdns.einyel.trabajo_grupal.database;

import org.duckdns.einyel.trabajo_grupal.database.local.EventsLocalRepo;
import org.duckdns.einyel.trabajo_grupal.database.remote.EventsRemoteRepo;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;

import io.reactivex.Flowable;

import io.reactivex.schedulers.Schedulers;

public class EventsRepoImpl {

    EventsRemoteRepo remoteRepo;
    EventsLocalRepo localRepo;

    public EventsRepoImpl(EventsRemoteRepo remoteRepo, EventsLocalRepo localRepo) {
        this.remoteRepo = remoteRepo;
        this.localRepo = localRepo;
    }


    public Flowable<List<MockEvent>> getAll() {
        return Flowable.mergeDelayError(remoteRepo.getAll().doOnNext(mockEvent -> localRepo.addEvents(mockEvent)).subscribeOn(Schedulers.io()),
                localRepo.getAll().subscribeOn(Schedulers.io()));
    }



    public EventsLocalRepo getLocalRepo() {
        return localRepo;
    }


}
