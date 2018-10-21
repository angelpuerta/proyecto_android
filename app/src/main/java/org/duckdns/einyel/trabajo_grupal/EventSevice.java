package org.duckdns.einyel.trabajo_grupal;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

class EventSevice {
    private static final EventSevice ourInstance = new EventSevice();

    static EventSevice getInstance() {
        return ourInstance;
    }

    private List<MockEvent> events;

    private EventSevice() {
        events = new ArrayList<MockEvent>();
        events.add(new MockEvent("Hola", Long.valueOf(1), 2));
    }

    public MockEvent getEvent (Long id){
        for (MockEvent event: events) {
            if(event.id.equals(id))
                return event;
        }
        return null;
    }

    public void addPuntuationAndComment(Long id, String comment, float puntuation){
        getEvent(id).comment = comment;
        getEvent(id).puntuation = puntuation;
    }


}
