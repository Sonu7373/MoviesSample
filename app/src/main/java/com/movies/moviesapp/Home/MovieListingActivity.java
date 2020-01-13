package com.movies.moviesapp.Home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.movies.moviesapp.CustomViews.shimmer.ShimmerFrameLayout;
import com.movies.moviesapp.HelperClasses.ConstantMethods;
import com.movies.moviesapp.Home.Adapters.MoviesAdapter;
import com.movies.moviesapp.ModalClasses.MovieModal;
import com.movies.moviesapp.ModalClasses.MoviesListModal;
import com.movies.moviesapp.R;
import com.movies.moviesapp.ServiceManager.RetrofitCallback;
import com.movies.moviesapp.ServiceManager.RetrofitClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;

public class MovieListingActivity extends AppCompatActivity {

    Context context;
    boolean doubleBackToExit = false;
    LinearLayout llMoviesInfo, llNoDataFromServer;
    RecyclerView rvMovies;
    SwipeRefreshLayout spRefresh;
    int pageNumber = 1;
    boolean isApiCallRunning = false;
    boolean callApi = false;
    int prevAdapterSize = 0;
    LinearLayoutManager layoutManagerMovies;
    MoviesListModal moviesModal;
    List<MovieModal> moviesList = new ArrayList<>();
    MoviesAdapter moviesAdapter;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_listing);
        context = MovieListingActivity.this;
        initializeFields();
    }

    /**
     * To initialize widgets
     */
    private void initializeFields() {
        llMoviesInfo = findViewById(R.id.llMoviesInfo);
        llNoDataFromServer = findViewById(R.id.llNoDataFromServer);
        rvMovies = findViewById(R.id.rvMovies);
        spRefresh = findViewById(R.id.spRefresh);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        layoutManagerMovies = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvMovies.setLayoutManager(layoutManagerMovies);

        callApiForMovies(false);

        //Swipe to refresh
        spRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                callApiForMovies(false);
            }
        });

        //To handle pagination
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (layoutManagerMovies.findFirstCompletelyVisibleItemPosition() == 0 && !isApiCallRunning) {
                    spRefresh.setEnabled(true);
                } else {
                    spRefresh.setEnabled(false);
                }

                final int itemAtEnd = layoutManagerMovies.findLastCompletelyVisibleItemPosition();

                if (itemAtEnd == moviesList.size() - 1 && moviesModal.getNext() != null) {
                    if (moviesModal.getNext()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (callApi) {
                                    callApiForMovies(true);
                                }
                            }
                        }, 1500);
                    }
                }

            }
        });
    }

    /**
     * To get the movies list
     *
     * @param isPagination
     */

    private void callApiForMovies(final boolean isPagination) {
        spRefresh.setRefreshing(false);
        llNoDataFromServer.setVisibility(View.GONE);
        llMoviesInfo.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        if (ConstantMethods.isNetworkAvailable(context)) {
            isApiCallRunning = true;
            Call<MoviesListModal> apiCall = RetrofitClient.getClient().getMovies(pageNumber, 20);
            apiCall.enqueue(new RetrofitCallback<MoviesListModal>(context, shimmerFrameLayout, true) {
                @Override
                public void onSuccess(MoviesListModal result) {
                    isApiCallRunning = false;
                    if (result != null) {
                        moviesModal = result;
                        if (!isPagination) {
                            prevAdapterSize = 0;
                            moviesList.clear();
                            if (moviesAdapter != null) {
                                moviesAdapter.notifyDataSetChanged();
                            }
                        }

                        if (result.getResults().size() == 0) {
                            callApi = false;
                            if (!isPagination) {
                                llNoDataFromServer.setVisibility(View.VISIBLE);
                                llMoviesInfo.setVisibility(View.GONE);
                            }
                            return;
                        } else {
                            callApi = true;
                            llNoDataFromServer.setVisibility(View.GONE);
                            llMoviesInfo.setVisibility(View.VISIBLE);
                            if (moviesList.size() > 0) {
                                prevAdapterSize = moviesList.size();
                                Iterator iterator = result.getResults().iterator();
                                while (iterator.hasNext()) {
                                    moviesList.add((MovieModal) iterator.next());
                                }
                                setAdapter(true);
                            } else {
                                moviesList = result.getResults();
                                prevAdapterSize = moviesList.size();
                                setAdapter(false);
                            }
                            pageNumber += 1;
                        }

                    } else {
                        if (!isPagination) {
                            llMoviesInfo.setVisibility(View.GONE);
                            llNoDataFromServer.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure() {
                    if (!isPagination) {
                        llMoviesInfo.setVisibility(View.GONE);
                        llNoDataFromServer.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            llNoDataFromServer.setVisibility(View.VISIBLE);
            llMoviesInfo.setVisibility(View.GONE);
            ConstantMethods.showErrorInfo(context, getString(R.string.no_internet_msg));
        }
    }

    /**
     * setting the adapter
     * @param refreshAdapter
     */
    private void setAdapter(boolean refreshAdapter) {
        if (refreshAdapter) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (moviesAdapter != null) {
                        moviesAdapter.notifyDataSetChanged();
                    }
                }
            }, 1000);

        } else {
            moviesAdapter = new MoviesAdapter(context, moviesList);
            rvMovies.setAdapter(moviesAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity();
        } else {
            ConstantMethods.showToast(context, getString(R.string.press_again_exit_msg));
        }
        //close drawer here
        this.doubleBackToExit = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000);
    }
}
