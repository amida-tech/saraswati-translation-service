package com.amida.saraswatitranslationservice.controller;

import java.io.FileWriter;
import java.io.IOException;

import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.validation.ValidationEngine;
import org.hl7.fhir.validation.cli.model.CliContext;
import org.hl7.fhir.validation.cli.services.ValidationService;
import org.hl7.fhir.validation.cli.utils.Params;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amida.saraswatitranslationservice.utils.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(path = "/api")

public class PatientController {

	@Value("${conversion.status}")
	private boolean conversionStatus;

	@Value("${transform.status}")
	private boolean transformStatus;

	@Value("${server.resourcePath}")
	private String resourcePath;

	@Value("${conversion.inputVersion}")
	private String inputVersion;

	@Value("${conversion.outputVersion}")
	private String outputVersion;

	@GetMapping(path = "/patient")
	public @ResponseBody ResponseEntity<?> patientResponse(@RequestBody Patient requestPatient) {
		ApiResponse apiResponse = new ApiResponse();

		if (conversionStatus) {
			// Build the CLI command here out of our variables
			Params params = new Params();
			String[] args = { "-version" + inputVersion, "-to-version" + outputVersion, "" };
			FileWriter file;
			try {
				file = new FileWriter("./temp.json");
				ObjectMapper mapper = new ObjectMapper();
				String jsonString = mapper.writeValueAsString(requestPatient);
	            file.write(jsonString);
				try {
					CliContext cliContext = params.loadCliContext(args);
					ValidationEngine validator = ValidationService.getValidator(cliContext, null);
					validator.convert(cliContext.getSources().get(0), cliContext.getOutput());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}




			// Execute mapper here and set it as this test patient
			Patient conversion = new Patient();

			return new ResponseEntity<>(conversion, HttpStatus.OK);
		} else if (transformStatus) {
			apiResponse.setResponseText("This feature is not yet implemented");
			return new ResponseEntity<>(apiResponse, HttpStatus.NOT_IMPLEMENTED);
		} else {
			apiResponse.setResponseText("Incorrect Server Configuration");
			return new ResponseEntity<>(apiResponse, HttpStatus.I_AM_A_TEAPOT);
		}
	}
}