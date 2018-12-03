package org.duckdns.einyel.trabajo_grupal.database.local;


import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;

import io.reactivex.Flowable;

public class EventsLocalRepo {

    private EventDao eventDao;

    public EventsLocalRepo(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public Flowable<List<MockEvent>> getAll() {
        return eventDao.getAll();
    }

    public void addEvent(MockEvent event) {
        eventDao.insert(event);
    }

    public void addEvents(List<MockEvent> events){eventDao.insertAll(events);}


}
