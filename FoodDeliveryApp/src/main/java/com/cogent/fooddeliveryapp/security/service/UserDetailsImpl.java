package com.cogent.fooddeliveryapp.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cogent.fooddeliveryapp.dto.User;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class UserDetailsImpl implements UserDetails {
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private long id;
	private String username;
	private String email;
	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;
	// Roles

	private UserDetailsImpl(long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	public static UserDetailsImpl build(User user)
	{
		List<GrantedAuthority> authorities =  user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
				.collect(Collectors.toList());
		return new UserDetailsImpl(user.getId(), 
									user.getUserName(), 
									user.getEmail(), 
									user.getPassword(), 
									authorities);
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		UserDetailsImpl user = (UserDetailsImpl) obj;
		return Objects.equals(id, user.id);
	}


}
