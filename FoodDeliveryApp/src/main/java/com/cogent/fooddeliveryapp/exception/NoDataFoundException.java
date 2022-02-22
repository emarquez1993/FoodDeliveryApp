package com.cogent.fooddeliveryapp.exception;

public class NoDataFoundException extends Exception {
	public NoDataFoundException(String msg)
	{
		super(msg);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
}
