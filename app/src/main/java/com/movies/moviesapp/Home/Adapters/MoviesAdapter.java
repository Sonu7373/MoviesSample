package com.movies.moviesapp.Home.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movies.moviesapp.HelperClasses.Constants;
import com.movies.moviesapp.Home.MovieDetailPageActivity;
import com.movies.moviesapp.ModalClasses.MovieModal;
import com.movies.moviesapp.R;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    Context context;
    List<MovieModal> moviesList;

    public MoviesAdapter(Context context, List<MovieModal> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_each_movie, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvMovieName.setText(moviesList.get(position).getTitle());
        holder.tvDirectorName.setText("Director : " + moviesList.get(position).getDirector());
        holder.tvProducerName.setText("Producer : " + moviesList.get(position).getProducer());
        holder.tvReleaseDate.setText("Release Date : " + moviesList.get(position).getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llBookInfo;
        TextView tvMovieName, tvDirectorName, tvProducerName, tvReleaseDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llBookInfo = itemView.findViewById(R.id.llBookInfo);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            tvDirectorName = itemView.findViewById(R.id.tvDirectorName);
            tvProducerName = itemView.findViewById(R.id.tvProducerName);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            llBookInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, MovieDetailPageActivity.class);
                    intent.putExtra(Constants.Keys.movieDetail.toString(), moviesList.get(pos));
                    context.startActivity(intent);
                }
            });
        }
    }
}
