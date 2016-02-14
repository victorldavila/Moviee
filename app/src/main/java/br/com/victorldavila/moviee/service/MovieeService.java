package br.com.victorldavila.moviee.service;

import java.util.ArrayList;
import java.util.Map;

import br.com.victorldavila.moviee.service.model.BaseResponse;
import br.com.victorldavila.moviee.service.model.InfoMovie;
import br.com.victorldavila.moviee.service.model.Movie;
import br.com.victorldavila.moviee.service.model.Trailer;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Victor on 10/02/2016.
 */
public interface MovieeService {

    public static final int SUCCESS_CODE = 200;
    public static final int NOT_FOUND_CODE = 404;
    public static final int BAD_REQUEST_CODE = 400;
    public static final int INTERNAL_ERROR_CODE = 500;

    public static final String ServiceURL = "https://api.themoviedb.org/3/";
    public static final String ImageUrl = "https://image.tmdb.org/t/p/w500";
    public static final String VideoImageUrl = "http://img.youtube.com/vi/";

    public static final String API_KEY = "?api_key=fcfc8516fa3b8989e68936634bc5ea84";
    public static final String MOVIE_URL = "discover/movie";

    @Headers({
            "Content-type: application/json"
    })
    @GET(MovieeService.MOVIE_URL + MovieeService.API_KEY)
    Call<BaseResponse<ArrayList<Movie>>> getMovies(@Query("page") String page, @Query("sort_by") String sort);

    @Headers({
            "Content-type: application/json"
    })
    @GET("movie/{id}" + API_KEY)
    Call<InfoMovie> getMovie(@Path("id") String id);

    @Headers({
            "Content-type: application/json"
    })
    @GET("movie/{id}/videos" + API_KEY)
    Call<BaseResponse<ArrayList<Trailer>>> getVideos(@Path("id") String id);
}
