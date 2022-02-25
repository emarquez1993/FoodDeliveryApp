package com.cogent.fooddeliveryapp.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.cogent.fooddeliveryapp.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${com.cogent.fooddeliveryapp.jwtSecret}")
	private String jwtSecret;
	
	@Value("${com.cogent.fooddeliveryapp.jwtExpirationMs}")
	private long jwtExpirationMs;
	
	public String generateToken(Authentication authentication)
	{
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		JwtBuilder jwts = Jwts.builder();
		jwts.setSubject(userPrincipal.getUsername());
		jwts.setIssuedAt(new Date());
		jwts.setExpiration(new Date(new Date().getTime()+jwtExpirationMs));
		jwts.signWith(SignatureAlgorithm.HS512, jwtSecret);
		/*
		 * return Jwts.builder() .setSubject(userPrincipal.getUsername())
		 * .setIssuedAt(new Date()) .setExpiration(new Date(new
		 * Date().getTime()+jwtExpirationMs)) .signWith(SignatureAlgorithm.HS512,
		 * jwtSecret) .compact();
		 */
		return jwts.compact();
	}
	
	public boolean validateJwtToken(String authToken)
	{
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (ExpiredJwtException e) {
			// TODO Auto-generated catch block
			logger.error("Jwt token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			// TODO Auto-generated catch block
			logger.error("Jwt token is not supported: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			// TODO Auto-generated catch block
			logger.error("Invalid Jwt token: {}", e.getMessage());
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			logger.error("Jwt token signature failed: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logger.error("Jwt token illegal argument: {}", e.getMessage());
		}
		return false;
	}
	
	public String getUserNamefromJwtToken(String authToken)
	{
		return Jwts.parser() //compact token ---> java object
				.setSigningKey(jwtSecret) // secret key ---> encoding is done
				.parseClaimsJws(authToken) // providing actual token
				.getBody() //extracting the body of the content
				.getSubject(); // extracting the subject
	}
}
