package com.example.movie_rating;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {

    private static final String ARG_RATING_id = "ratingId";
    private MovieRating mMovieRating;
    private EditText mMovieTitleEditText;
    private EditText mCommentEditText;
    private Spinner mGenreSpinner;
    private RatingBar mRatingBar;
    private Button mSubmitButton;
    private SharedPreferences mPreferences;
    private boolean mAdding = false;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    public RatingFragment() {
        // Required empty public constructor
    }

    public static RatingFragment newInstance(int ratingId) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RATING_id, ratingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("movie_rating");
        int ratingId = 0;
        if (getArguments() != null) {
            ratingId = getArguments().getInt(ARG_RATING_id);
            //mMovieRating = MovieRatingDatabase.getInstance(getContext()).getMovieRating(ratingId);
        }
        if (ratingId != 0) {
        } else {
            mAdding = true;
            mMovieRating = new MovieRating();
            mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String ratingString = mPreferences.getString(getString(R.string.pref_rating_key),"0");
            int default_rating = Integer.parseInt(ratingString);
            mMovieRating.setRating(default_rating);

            String defaultMovieTitle = mPreferences.getString("movie","");
            mMovieRating.setMovieTitle(defaultMovieTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        mMovieTitleEditText = view.findViewById(R.id.movieTitleEditText);
        mMovieTitleEditText.addTextChangedListener(new NameTextListener(mMovieTitleEditText));

        mCommentEditText = view.findViewById(R.id.commentEditText);
        mCommentEditText.addTextChangedListener(new NameTextListener(mCommentEditText));

        mGenreSpinner = view.findViewById(R.id.genreSpinner);
        mGenreSpinner.setOnItemSelectedListener(new GenreSelectedListener());

        mRatingBar = view.findViewById(R.id.ratingbar);
        mRatingBar.setOnRatingBarChangeListener(new RatingChangedListener());

        mMovieTitleEditText.setText(mMovieRating.getMovieTitle());
        mCommentEditText.setText(mMovieRating.getComment());
        List<String> genre = Arrays.asList(getResources().getStringArray(R.array.genre));
        int index = genre.indexOf(mMovieRating.getGenre());
        mGenreSpinner.setSelection(index);
        mRatingBar.setRating(mMovieRating.getRating());

        mSubmitButton = view.findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(new SubmitButtonListener());


        return view;
    }

    private class SubmitButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(mAdding){
                mDatabaseReference.push().setValue(mMovieRating);
            } else {
            }
        }
    }

    public void submitButtonClickListener (View v){
        String movieTitle = mMovieRating.getMovieTitle();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("movie", movieTitle);
        editor.apply();
        int rating = mMovieRating.getRating();
    }

    private class GenreSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String genre = (String)adapterView.getItemAtPosition(i);
            if (i!=0){
                mMovieRating.setGenre(genre);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class RatingChangedListener implements RatingBar.OnRatingBarChangeListener{
        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            mMovieRating.setRating((int)v);
        }
    }

    private class NameTextListener implements TextWatcher {
        private View editText;

        public NameTextListener(View editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (editText == mMovieTitleEditText) {
                mMovieRating.setMovieTitle(charSequence.toString());
            }else if (editText == mCommentEditText){
                mMovieRating.setComment((charSequence.toString()));
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}