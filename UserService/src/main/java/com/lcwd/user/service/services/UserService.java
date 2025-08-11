package com.lcwd.user.service.services;

import java.util.List;

import com.lcwd.user.service.entities.User;

public interface UserService {

	User saveUser(User user);
	
	List<User> getAllUser();
	
	User getUser(String id);
}
