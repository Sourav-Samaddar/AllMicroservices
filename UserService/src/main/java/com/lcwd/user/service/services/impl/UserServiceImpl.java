package com.lcwd.user.service.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public User saveUser(User user) {
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}
	
	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User getUser(String id) {
		User user = userRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("User with given id not found"));
		
		Rating[] ratingOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(),
				Rating[].class);
		List<Rating> ratings = Arrays.stream(ratingOfUser).toList();
		
		List<Rating> ratingList = ratings.stream().map(rating -> {
			
//			ResponseEntity<Hotel> hotelEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(),
//					Hotel.class);
//			Hotel hotel = hotelEntity.getBody();
			Hotel hotel = hotelService.getHotel(rating.getHotelId());
			rating.setHotel(hotel);
			
			return rating;
		}).collect(Collectors.toList());
		
		user.setRatings(ratingList);
		
		//Fire-and-forget RabbitMQ producer call — no blocking
		sendToRabbitProducerAsync(user);
		
		return user;
	}
	
	private void sendToRabbitProducerAsync(User user) {
        CompletableFuture.runAsync(() -> {
        	webClientBuilder.build()
            .post()
            .uri("http://RABBIT-PRODUCER/publish/simple")
            .bodyValue("Hello from non blocking simplequeue call")
            .retrieve()
            .bodyToMono(Void.class)
            .doOnError(e -> System.out.println(e.getMessage()))
            .subscribe();
        });
        
        CompletableFuture.runAsync(() -> {
        	webClientBuilder.build()
            .post()
            .uri("http://RABBIT-PRODUCER/publish/fanout")
            .bodyValue("Hello from non blocking Fanout call")
            .retrieve()
            .bodyToMono(Void.class)
            .doOnError(e -> System.out.println(e.getMessage()))
            .subscribe();
        });
    }

}
