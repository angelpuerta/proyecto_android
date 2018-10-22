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
        MockEvent mockEvent = new MockEvent(Long.valueOf(1));
        mockEvent.addMarkAndComment("Muy buen lugar", Float.valueOf(3));
        mockEvent.addMarkAndComment("No me gustó", Float.valueOf(1));
        mockEvent.tittle = "Tour Leon";
        mockEvent.description = "Iniciaremos nuestro free tour por León en la Plaza de la Regla. Desde aquí podremos admirar en toda su majestuosidad la arquitectura exterior de la Catedral de León, una joya del gótico que tiene el privilegio de haber sido el primer Monumento Nacional declarado en España.\n" +
                "\n" +
                "Continuaremos nuestra ruta guiada gratuita por León en el Parque del Cid, más conocido como el “Jardín Romántico de León”. Además de disfrutar de sus tranquilos paseos, conoceremos las historias de dos edificios próximos a este espacio verde: el Palacio de los Guzmanes (actual sede de la Diputación de León) y la Casa de los Botines, diseñada al estilo modernista por el genial arquitecto Antonio Gaudí.\n" +
                "\n" +
                "Nuestros pasos nos llevarán además hasta la Plaza de San Isidoro, que toma su nombre de la Basílica de San Isidoro de León. Advertiremos sus elementos románicos antes de dirigirnos hacia la Plaza Mayor de León, donde conoceremos algunas curiosidades de este emblemático punto de reunión en la ciudad. Realizaremos una última parada junto al Palacio del Conde de Luna, un regio edificio del siglo XIV con muchos secretos por conocer…\n" +
                "\n" +
                "Culminaremos esta visita gratuita por León regresando a la Plaza de la Regla.";
        mockEvent.imgURL = R.raw.image1;
    }

    public MockEvent getEvent (Long id){
        for (MockEvent event: events) {
            if(event.getId().equals(id))
                return event;
        }
        return null;
    }

    public void addPuntuationAndComment(Long id, String comment, float puntuation){
        getEvent(id).addMarkAndComment(comment,puntuation);
    }


}
