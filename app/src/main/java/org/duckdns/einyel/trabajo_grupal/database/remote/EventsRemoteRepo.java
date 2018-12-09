package org.duckdns.einyel.trabajo_grupal.database.remote;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import org.duckdns.einyel.trabajo_grupal.database.EventRepo;
import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;


public class EventsRemoteRepo extends BaseRemote {

    public Observable<List<MockEvent>> getAll() {
        return create(RemoteEvents.class, "http://einyel.duckdns.org/android/").getEvents();
    }

}
