package com.github.mouse0w0.eventbus;

public class EventException extends RuntimeException {

	public EventException(String message) {
		super(message);
	}
	
	public EventException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
