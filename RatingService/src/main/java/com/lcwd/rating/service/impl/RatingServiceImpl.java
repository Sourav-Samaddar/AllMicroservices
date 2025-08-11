package com.lcwd.rating.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.repository.RatingRepository;
import com.lcwd.rating.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService{
	
	@Autowired
	RatingRepository repository;

	@Override
	public Rating create(Rating rating) {
		String randomRatingId = UUID.randomUUID().toString();
		rating.setRatingId(randomRatingId);
		return repository.save(rating); 
	}

	@Override
	public List<Rating> getAllRatings() {
		return repository.findAll();
	}

	@Override
	public List<Rating> getRatingsByUserId(String userId) {
		return repository.findByUserId(userId);
	}

	@Override
	public List<Rating> getRatingsByHotelId(String hotelId) {
		return repository.findByHotelId(hotelId);
	}

}
