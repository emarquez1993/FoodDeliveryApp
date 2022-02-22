package com.cogent.fooddeliveryapp.exception;

public class IdNotFoundException extends RuntimeException {
	public IdNotFoundException(String msg)
	{
		super(msg);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
}
