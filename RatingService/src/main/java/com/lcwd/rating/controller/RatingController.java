package com.lcwd.rating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.service.RatingService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ratings")
@Slf4j
public class RatingController {

	@Autowired
	private RatingService ratingService;
	
	@PostMapping
	public ResponseEntity<Rating> create(@RequestBody Rating rating){
		return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(rating));
	}
	
	@GetMapping
	public ResponseEntity<List<Rating>> getAllRatings(){
		log.debug("**** Get All ratings ****");
		return ResponseEntity.ok(ratingService.getAllRatings());
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable(name = "userId") String userId){
		log.debug("**** getRatingsByUserId ****:"+userId);
		return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
	}
	
	@GetMapping("/hotels/{hotelId}")
	public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable(name = "hotelId") String hotelId){
		log.debug("**** getRatingsByHotelId ****:"+hotelId);
		return ResponseEntity.ok(ratingService.getRatingsByHotelId(hotelId));
	}
	
}
