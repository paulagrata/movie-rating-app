package com.example.movie_rating;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StartActivity extends AppCompatActivity{
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private Query mQuery;
    private FirebaseRecyclerAdapter<MovieRating, MovieRatingListAdapter.RatingHolder> mFirebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseApp.initializeApp(this);


/*
        RecyclerView recyclerView = findViewById(R.id.rating_recycler_view);
        MovieRatingListAdapter mMovieRatingListAdapter = new MovieRatingListAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(mMovieRatingListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
 */
        //mMovieRatingListAdapter.setMovieRatings(MovieRatingDatabase.getInstance(this).getMovieRatings());

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.rating_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mQuery = mFirebaseDatabase.getReference().child("movie_rating").orderByChild("movieTitle");

        FirebaseRecyclerOptions<MovieRating> options = new FirebaseRecyclerOptions.Builder<MovieRating>()
                .setQuery(mQuery,MovieRating.class)
                .build();
        mFirebaseRecyclerAdapter= new MovieRatingListAdapter(options);
        recyclerView.setAdapter(mFirebaseRecyclerAdapter);
        mFirebaseRecyclerAdapter.startListening();

                ActivityResultLauncher < Intent > launcher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                Intent data = result.getData();
                                int rating = data.getIntExtra("rating", 0);
                                String movieTitle = data.getStringExtra("movieTitle");
                                String toastString = getResources().getString(R.string.review_entered);
                                String displayMovie = getResources().getString(R.string.display_movie, movieTitle);
                                toastString += displayMovie;
                                Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
                                String ratingString = getResources().getQuantityString(R.plurals.star_rating, rating, rating);
                                toastString += ratingString;
                            }
                        });
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ratingIntent = new Intent(StartActivity.this, MainActivity.class);
                //ratingIntent.putExtra("id",MovieRating.getRatingId());
                startActivity(ratingIntent);
            }
        });

        /*

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int)viewHolder.itemView.getTag();

            }
        }).attachToRecyclerView(recyclerView);

         */

        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                (result) -> {
                    if(result.getResultCode() == RESULT_CANCELED){
                        finish();
                    }
                }
        );

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.Theme_Movie_Rating)
                            .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build();
                    signInLauncher.launch(signInIntent);
                }
            }
        };
    }

    public void onClick(MovieRating movieRating) {
        Intent ratingIntent = new Intent(this, MainActivity.class);
        startActivity(ratingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
        }

        if (id==R.id.sign_out){
            AuthUI.getInstance().signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }
}