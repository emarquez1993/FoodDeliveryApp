package com.cogent.fooddeliveryapp.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cogent.fooddeliveryapp.dto.User;
import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		try {
			user = userRepository.findByUserName(username).orElseThrow(()-> new NoDataFoundException("user does not exist"));
		} catch (NoDataFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return UserDetailsImpl.build(user);
	}

}
