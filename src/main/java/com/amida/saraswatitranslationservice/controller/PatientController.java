package com.amida.saraswatitranslationservice.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r5.context.SimpleWorkerContext;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.elementmodel.Manager;
import org.hl7.fhir.r5.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r5.formats.IParser.OutputStyle;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.hl7.fhir.validation.ValidationEngine;
import org.hl7.fhir.validation.cli.model.CliContext;
import org.hl7.fhir.validation.cli.services.ValidationService;
import org.hl7.fhir.validation.cli.utils.Params;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
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

	// Still need to figure out a way to set the mapping name to a variable.

	@GetMapping(path = "/patient")
	public @ResponseBody ResponseEntity<?> patientResponse(@RequestBody String directory) {
		ApiResponse apiResponse = new ApiResponse();
		byte[] request = null;
		FileInputStream requestInput;
		// Reads file off of file system to turn it into byte-stream. Will be moved to a
		// development mode flag operation at some point.
		try {
			requestInput = new FileInputStream(directory);
			try {
				request = IOUtils.toByteArray(requestInput);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		if (conversionStatus) {
			FileInputStream fileOutput = null;
//			Params params = new Params();
			
			//Generate a file name for the output made of date signatures
			String strDate = Calendar.getInstance().getTime().toString();
			String output = "./temp/" + directory +strDate + ".json";
//			String[] args = { " -version" + inputVersion, " -to-version" + outputVersion, " -output" + output };
			Content content = new Content();
			content.cntType = FhirFormat.JSON;
			content.focus = request;

			try {
//				CliContext cliContext = params.loadCliContext(args);
				String definitions = "hl7.fhir.r3.core#3.0.2";
				// see SimpleWorkerContext 143 for instructions
				
				//Trying to build the Context here from NPM package.
				NpmPackage pi = NpmPackage.();
				
				SimpleWorkerContext context = SimpleWorkerContext.fromPackage(pi);

				Element e = Manager.parse(context, new ByteArrayInputStream(content.focus), content.cntType);
				Manager.compose(context, e, new FileOutputStream(output),
						(output.endsWith(".json") ? FhirFormat.JSON : FhirFormat.XML), OutputStyle.PRETTY, null);
				fileOutput = new FileInputStream(output);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new ResponseEntity<>(fileOutput, HttpStatus.OK);
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