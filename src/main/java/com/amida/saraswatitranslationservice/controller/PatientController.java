package com.amida.saraswatitranslationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amida.saraswatitranslationservice.utils.ApiResponse;

@Controller
@RequestMapping(path = "/api")

public class PatientController {

	@GetMapping(path = "/patient")
	public @ResponseBody ResponseEntity<?> patientResponse() {

		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setResponseText("This is a Test");

		return new ResponseEntity<>(apiResponse, HttpStatus.I_AM_A_TEAPOT);
	}
}