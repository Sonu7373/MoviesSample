package com.movies.moviesapp.HelperClasses;

public class Constants {

    public static String MoviesAppPreference = "moviesAppPreference";

    public enum Keys {
        movieDetail("movieDetail");

        private final String type;

        Keys(final String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }
}
