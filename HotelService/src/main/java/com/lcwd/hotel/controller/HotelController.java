package com.lcwd.hotel.controller;

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

import com.lcwd.hotel.entities.Hotel;
import com.lcwd.hotel.service.HotelService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/hotels")
@Slf4j
public class HotelController {
	
	@Autowired
	HotelService hotelService;

	@PostMapping
	public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel){
		Hotel hotel1 = hotelService.createHotel(hotel);
		return ResponseEntity.status(HttpStatus.CREATED).body(hotel1);
	}
	
	@GetMapping("/{hotelId}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable(name = "hotelId") String hotelId){
		Hotel hotel = hotelService.getHotelById(hotelId);
		return ResponseEntity.ok(hotel);
	} 
	
	@GetMapping
	public ResponseEntity<List<Hotel>> getAllHotels(){
		log.debug("********Hello from hotel*********");
		List<Hotel> allHotels = hotelService.getAllHotels();
		return ResponseEntity.ok(allHotels);
	}
}
