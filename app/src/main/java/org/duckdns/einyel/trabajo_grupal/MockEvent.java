package org.duckdns.einyel.trabajo_grupal;

public class MockEvent {

    public String comment;
    public Long id;
    public float puntuation;


    public MockEvent(String comment, Long id, float puntuation) {
        this.comment = comment;
        this.id = id;
        this.puntuation = puntuation;
    }

    @Override
    public String toString() {
        return "MockEvent{" +
                "comment='" + comment + '\'' +
                ", id=" + id +
                ", puntuation=" + puntuation +
                '}';
    }
}
