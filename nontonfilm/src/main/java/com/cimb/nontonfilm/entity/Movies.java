package com.cimb.nontonfilm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Movies {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String moviesName;
	private String moviesPic;
	public String getMoviesPic() {
		return moviesPic;
	}
	public void setMoviesPic(String moviesPic) {
		this.moviesPic = moviesPic;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMoviesName() {
		return moviesName;
	}
	public void setMoviesName(String moviesName) {
		this.moviesName = moviesName;
	}
	public int getMovieYear() {
		return movieYear;
	}
	public void setMovieYear(int movieYear) {
		this.movieYear = movieYear;
	}
	public String getMovieDesc() {
		return movieDesc;
	}
	public void setMovieDesc(String movieDesc) {
		this.movieDesc = movieDesc;
	}
	private int movieYear;
	private String movieDesc;

}
