package com.movies.moviesapp.ServiceManager;

import com.movies.moviesapp.ModalClasses.MovieModal;
import com.movies.moviesapp.ModalClasses.MoviesListModal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("films")
    Call<MoviesListModal> getMovies(@Query("page") Integer page,
                                    @Query("perPage") Integer perPage);

    @GET("films/{id}")
    Call<MovieModal> getMovieDetail(@Path("id") String id);
}
