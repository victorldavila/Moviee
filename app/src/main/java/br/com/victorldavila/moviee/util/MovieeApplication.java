package br.com.victorldavila.moviee.util;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import br.com.victorldavila.moviee.service.MovieeService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Victor on 10/02/2016.
 */
public class MovieeApplication extends Application {

    Retrofit retrofit;
    MovieeService service;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize Fresco
        Fresco.initialize(this);

        retrofit = new Retrofit.Builder().baseUrl(MovieeService.ServiceURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieeService.class);
    }

    public MovieeService getService() {
        return service;
    }

    public void setService(MovieeService service) {
        this.service = service;
    }
}
