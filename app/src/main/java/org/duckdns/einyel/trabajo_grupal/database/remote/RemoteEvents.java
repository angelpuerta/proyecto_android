package org.duckdns.einyel.trabajo_grupal.database.remote;

import org.duckdns.einyel.trabajo_grupal.model.MockEvent;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteEvents {

    @GET("events")
    Flowable<List<MockEvent>> getEvents();

}
