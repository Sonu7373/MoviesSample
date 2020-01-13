package com.movies.moviesapp.Home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.movies.moviesapp.HelperClasses.ConstantMethods;
import com.movies.moviesapp.HelperClasses.Constants;
import com.movies.moviesapp.ModalClasses.MovieModal;
import com.movies.moviesapp.R;
import com.movies.moviesapp.ServiceManager.RetrofitCallback;
import com.movies.moviesapp.ServiceManager.RetrofitClient;

import retrofit2.Call;

public class MovieDetailPageActivity extends AppCompatActivity {

    Context context;
    TextView tvMovieName, tvDirectorName, tvProducerName, tvReleaseDate, tvDescription;
    MovieModal movieModal;
    RelativeLayout rlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_page);
        context = MovieDetailPageActivity.this;
        initializeFields();
    }

    private void initializeFields() {
        tvMovieName = findViewById(R.id.tvMovieName);
        tvDirectorName = findViewById(R.id.tvDirectorName);
        tvProducerName = findViewById(R.id.tvProducerName);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvDescription = findViewById(R.id.tvDescription);
        rlBack = findViewById(R.id.rlBack);
        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.Keys.movieDetail.toString())) {
                movieModal = (MovieModal) getIntent().getSerializableExtra(Constants.Keys.movieDetail.toString());
            }
        }

        setMoviesInfo(movieModal);
        setListeners();

        //callApiToGetMovieDetails();
    }

    private void setListeners() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setMoviesInfo(MovieModal movieModal) {
        if (movieModal != null) {
            tvMovieName.setText(movieModal.getTitle());
            tvDirectorName.setText(movieModal.getDirector());
            tvProducerName.setText(movieModal.getProducer());
            tvDescription.setText(movieModal.getOpeningCrawl());
            tvReleaseDate.setText(movieModal.getReleaseDate());
        } else {
            tvMovieName.setText("N/A");
            tvDirectorName.setText("N/A");
            tvProducerName.setText("N/A");
            tvDescription.setText("N/A");
            tvReleaseDate.setText("N/A");
        }
    }

    /**
     * Not using this
     */
    private void callApiToGetMovieDetails() {

        if (ConstantMethods.isNetworkAvailable(context)) {
            Call<MovieModal> apiCall = RetrofitClient.getClient().getMovieDetail(null);
            apiCall.enqueue(new RetrofitCallback<MovieModal>(context, ConstantMethods.showApiCallProgress(context), true) {
                @Override
                public void onSuccess(MovieModal result) {
                    if (result != null) {

                    } else {

                    }
                }

                @Override
                public void onFailure() {

                }
            });
        } else {
            ConstantMethods.showErrorInfo(context, getString(R.string.no_internet_msg));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
