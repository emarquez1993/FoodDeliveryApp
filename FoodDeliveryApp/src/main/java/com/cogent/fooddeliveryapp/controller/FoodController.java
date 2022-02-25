package com.cogent.fooddeliveryapp.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Food;
import com.cogent.fooddeliveryapp.enums.FoodType;
import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.repository.FoodRepository;

@RestController
@RequestMapping("/food")
@Validated
public class FoodController {
	
	@Autowired
	FoodRepository foodRepository;
	
	@PostMapping(value = "/add")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> createFood(@Valid @RequestBody Food food) {
		
		Food food2 = foodRepository.save(food);
		
		return ResponseEntity.status(201).body(food2);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getFoodById(@PathVariable("id") @Min(1) Long id) throws NoDataFoundException {
		Food food = foodRepository.findById(id).orElseThrow(()-> new NoDataFoundException("sorry food not found"));
		return ResponseEntity.ok(food);
	}
	
	@GetMapping(value = "/")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getAllFood() throws NoDataFoundException {
		List<Food> food = foodRepository.findAll();
		if(food.size()<1)
			throw new NoDataFoundException("No food records exist");
		else
			return ResponseEntity.ok(food);
	}

	@GetMapping(value = "/:{foodType}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getFoodByFoodType(@PathVariable("foodType") FoodType foodType) throws NoDataFoundException {
		List<Food> food = foodRepository.getFoodByFoodType(foodType);
		if(food.size()<1)
			throw new NoDataFoundException("No food records exist with that type");
		else
			return ResponseEntity.ok(food);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/:{foodId}")
	public ResponseEntity<?> deleteFoodById(@PathVariable("foodId") @Min(1) Long id) throws NoDataFoundException
	{
		Food food = foodRepository.findById(id).orElseThrow(()-> new NoDataFoundException("Food not found. Delete Aborted."));
		foodRepository.deleteById(id);
		Map<String, String> message = new HashMap<>();
		message.put("message", "food record successfully deleted");
		return ResponseEntity.status(201).body(message);
	}
	
	@GetMapping(value = "/all/desc")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getAllFoodDesc() throws NoDataFoundException {
		List<Food> food = foodRepository.findAll();
		if(food.size()<1)
			throw new NoDataFoundException("No food records found.");
		else
		{
			Collections.sort(food, (a,b)->b.getFoodName().compareTo(a.getFoodName()));
			return ResponseEntity.status(201).body(food);
		}
	}
	
	@GetMapping(value = "/all/asc")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getAllFoodAsc() throws NoDataFoundException {
		List<Food> food = foodRepository.findAll();
		if(food.size()<1)
			throw new NoDataFoundException("No food records found.");
		else
		{
			Collections.sort(food, (a,b)->a.getFoodName().compareTo(b.getFoodName()));
			return ResponseEntity.status(201).body(food);
		}
	}

}
