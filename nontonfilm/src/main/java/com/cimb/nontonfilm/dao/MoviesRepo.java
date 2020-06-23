package com.cimb.nontonfilm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.nontonfilm.entity.Movies;

public interface MoviesRepo extends JpaRepository<Movies, Integer> {
	
}