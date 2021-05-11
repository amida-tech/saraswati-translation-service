package com.amida.saraswatitranslationservice.utils;

public class ApiResponse {
	private Integer responseCode;
	private String responseOrigin;
	private String responseText;
	
	public ApiResponse() {};
	
	public ApiResponse(Integer responseCode, String responseOrigin, String responseText) {
		this.responseCode = responseCode;
		this.responseOrigin = responseOrigin;
		this.responseText = responseText;
	}
	
	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseOrigin() {
		return responseOrigin;
	}

	public void setResponseOrigin(String responseOrigin) {
		this.responseOrigin = responseOrigin;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}


	
}
