package com.lcwd.user.service.controller;

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

import com.lcwd.user.service.annotation.ResilientEndpoint;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user){
		User user1 = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
	}
	
	@ResilientEndpoint
	@GetMapping("/{userId}")
//	@Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
//	@RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getSingleUserById(@PathVariable(name = "userId") String userId){
		User user = userService.getUser(userId);
		return ResponseEntity.ok(user);
	} 
	
	public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
//      logger.info("Fallback is executed because service is down : ", ex.getMessage());

      ex.printStackTrace();

      User user = User.builder().email("dummy@gmail.com").name("Dummy").about("This user is created dummy because some service is down").userId("141234").build();
      return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
  }

	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> allUser = userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}
}
