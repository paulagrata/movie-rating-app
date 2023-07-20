package com.example.movie_rating;

public class MovieRating {
    private String movieTitle;
    private String genre;
    private String comment;
    private int rating;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public MovieRating() {
        movieTitle = "";
        genre = "";
        comment = "";
        rating = 0;
    }

    public MovieRating(String movieTitle, String comment, String genre, int rating) {
        this.movieTitle = movieTitle;
        this.comment = comment;
        this.genre = genre;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "movieTitle=" + movieTitle + '\n' +
                "genre=" + genre + '\n' +
                "comment='" + comment + '\n' +
                "rating=" + rating;
    }

}
