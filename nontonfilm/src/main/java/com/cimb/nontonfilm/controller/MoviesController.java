package com.cimb.nontonfilm.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cimb.nontonfilm.dao.MoviesRepo;
import com.cimb.nontonfilm.entity.Movies;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
public class MoviesController {
private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";
	
	@Autowired
	private MoviesRepo moviesRepo;
	
	@GetMapping("/movies")
	public Iterable<Movies> getMovies(){
		return moviesRepo.findAll();
	}
	
	// DELETE
	@DeleteMapping("/delete/{id}")
	public void deleteMoviesById(@PathVariable int id ) {
		Optional<Movies> findMovies = moviesRepo.findById(id);
		
		if (findMovies.toString()=="Optional.empty")
			throw new RuntimeException("Movies Not Found");
			
		moviesRepo.deleteById(id);
	}
	
	
	//EDIT
	@PutMapping("/edit/{id}")
	public String editMovies(@RequestParam("file") MultipartFile file, @RequestParam("moviesData") String movieString, @PathVariable int id) throws JsonMappingException, JsonProcessingException {
		
		Movies findMovies = moviesRepo.findById(id).get();
				
		findMovies = new ObjectMapper().readValue(movieString, Movies.class);
		
		Date date = new Date();
		
		String fileExtension = file.getContentType().split("/")[1];
		String newFileName = "MOV-" + date.getTime() + "." + fileExtension;
		
		String fileName = StringUtils.cleanPath(newFileName);
		
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/movies/download/")
				.path(fileName).toUriString();
		
		findMovies.setMoviesPic(fileDownloadUri);
		
		moviesRepo.save(findMovies);
		
		return fileDownloadUri;
	}
	
	// DOWNLOAD FILM 
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Object> downloadFile(@PathVariable String fileName){
		Path path = Paths.get(uploadPath + fileName);
		Resource resource = null;
		
		try {
			resource = new UrlResource(path.toUri());
		} catch(MalformedURLException e) {
			e.printStackTrace();
		}
		
		System.out.println("downloaded");
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@PostMapping("/addMovie")
	public String addMovies(@RequestParam("file") MultipartFile file, @RequestParam("moviesData") String movieString) throws JsonMappingException, JsonProcessingException {
		Date date = new Date();
		
		Movies movies = new ObjectMapper().readValue(movieString, Movies.class);
		
		String fileExtension = file.getContentType().split("/")[1];
		
		String newFileName = "MOV-" + date.getTime() + "." + fileExtension;
		String fileName = StringUtils.cleanPath(newFileName);
		
		Path path = Paths.get( StringUtils.cleanPath(uploadPath) + fileName);
		
		try {
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/movies/download/")
				.path(fileName).toUriString();
		
		movies.setMoviesPic(fileDownloadUri);;
		moviesRepo.save(movies);
		
		return fileDownloadUri;
				
	}
	
}

