package com.amida.saraswatitranslationservice.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hl7.fhir.r5.context.SimpleWorkerContext;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.elementmodel.Manager;
import org.hl7.fhir.r5.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r5.formats.IParser.OutputStyle;
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
	
	//Still need to figure out a way to set the mapping name to a variable.

	@GetMapping(path = "/patient")
	public @ResponseBody ResponseEntity<?> patientResponse(@RequestBody byte[] request) {
		ApiResponse apiResponse = new ApiResponse();
		ByteArrayOutputStream returnOutput = new ByteArrayOutputStream();

		if (conversionStatus) {
			// Build the CLI command here out of our variables
			Params params = new Params();
			String[] args = { " -version" + inputVersion, " -to-version" + outputVersion, "" };
			Content content = new Content();
			content.cntType = FhirFormat.JSON;
			content.focus = request;

			try {
				CliContext cliContext = params.loadCliContext(args);
				SimpleWorkerContext context = new SimpleWorkerContext();
				ValidationEngine validator = ValidationService.getValidator(cliContext, null);

				// Need to set up context to equal something, which is different than CLI
				// context. It contains all of the information about what operation is being
				// performed, and what version is being written to. Gotta dig that out.

				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				String strDate = dateFormat.format(date);
				String output = "./temp/" + strDate + ".json";

				// output needs to be rectified into something returnable. Currently it's a
				// string representing a filepath destination. Theoretically this could be used
				// then turned into a file read as the return. Currently returning empty
				// byteArrayStream.
				Element e = Manager.parse(context, new ByteArrayInputStream(content.focus), content.cntType);
				Manager.compose(context, e, new FileOutputStream(output),
						(output.endsWith(".json") ? FhirFormat.JSON : FhirFormat.XML), OutputStyle.PRETTY, null);
				//Need to do a file read in here as of now.
				String readPath = output;
				
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new ResponseEntity<>(returnOutput, HttpStatus.OK);
		} else if (transformStatus) {
			apiResponse.setResponseText("This feature is not yet implemented");
			return new ResponseEntity<>(apiResponse, HttpStatus.NOT_IMPLEMENTED);
		} else {
			apiResponse.setResponseText("Incorrect Server Configuration");
			return new ResponseEntity<>(apiResponse, HttpStatus.I_AM_A_TEAPOT);
		}
	}

	private class Content {
		byte[] focus = null;
		FhirFormat cntType = null;
	}
}