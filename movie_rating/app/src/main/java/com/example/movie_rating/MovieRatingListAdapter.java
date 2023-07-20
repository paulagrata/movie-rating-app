package com.example.movie_rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class MovieRatingListAdapter
        extends FirebaseRecyclerAdapter<MovieRating, MovieRatingListAdapter.RatingHolder> {
    private List<MovieRating> mMovieRatings;
    private Context context;

    public MovieRatingListAdapter(@NonNull FirebaseRecyclerOptions<MovieRating> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingHolder holder, int position, @NonNull MovieRating model) {
        holder.mRowMovieTitleView.setText(model.getMovieTitle());
        int rating = model.getRating();
        String ratingString = context.getResources().getQuantityString(R.plurals.star_rating, rating, rating);
        holder.mRowRatingView.setText(ratingString);

    }

    @NonNull
    @Override
    public RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rating_row,parent,false);
        return new MovieRatingListAdapter.RatingHolder(view);
    }


    class RatingHolder extends RecyclerView.ViewHolder{
        private final TextView mRowMovieTitleView;
        private final TextView mRowRatingView;


        public RatingHolder(@NonNull View itemView) {
            super(itemView);
            mRowMovieTitleView = itemView.findViewById(R.id.row_movie_title_view);
            mRowRatingView = itemView.findViewById(R.id.row_movie_rating_view);
        }

    }

    /*
    @Override
    public int getItemCount() {
        if (mMovieRatings != null) {
            return mMovieRatings.size();
        }
        return 0;
    }

    public void setMovieRatings(List<MovieRating> movieRatings) {
        mMovieRatings = movieRatings;
        notifyDataSetChanged();
    }

     */


}