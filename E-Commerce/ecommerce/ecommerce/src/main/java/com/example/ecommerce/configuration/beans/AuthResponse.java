package com.example.ecommerce.configuration.beans;

public class AuthResponse {
    private String token;
    private String message;
    private String status;

    public AuthResponse(Object object, String string) {
		// TODO Auto-generated constructor stub
    	this.message=string;
	}

	public AuthResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthResponse(String token, String message, String status) {
		super();
		this.token = token;
		this.message = message;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
