package com.movies.moviesapp.ModalClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MoviesListModal implements Serializable {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private Boolean next;
    @SerializedName("results")
    @Expose
    private List<MovieModal> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }

    public List<MovieModal> getResults() {
        return results;
    }

    public void setResults(List<MovieModal> results) {
        this.results = results;
    }
}
