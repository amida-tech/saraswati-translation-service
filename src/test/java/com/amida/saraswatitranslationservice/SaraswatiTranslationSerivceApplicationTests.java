package com.amida.saraswatitranslationservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SaraswatiTranslationSerivceApplicationTests {

	@Test
	void contextLoads() {

		String fmlTestString = "fmltestobjects/r3patient.json -output fmltestobjects/test.xml -transform fmltestobjects/PatientR3toR4.txt -ig ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:3.8.0";
		String fmlTestSourceLocation = "fmltestobjects/r3patient.json";
		String fmlOutPutLocation = "fmltestobjects/test.xml";
		String fmlMapLocation = "fmltestobjects/PatientR3toR4.txt";
		String fmlInfoSource = "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:3.8.0";

		ClassLoader classLoader = getClass().getClassLoader();

		URL resource1 = classLoader.getResource(fmlTestSourceLocation);
		File fmlTestSourceData = new File(resource1.getFile());
		try (InputStream inputStream = classLoader.getResourceAsStream(fmlTestSourceLocation)) {

			String fmlTestSourceStream = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		URL resource2 = classLoader.getResource(fmlOutPutLocation);
		File fmlOutPutLocationData = new File(resource2.getFile());
		try (InputStream inputStream = classLoader.getResourceAsStream(fmlOutPutLocation)) {

			String fmlOutPutLocationStream = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		URL resource3 = classLoader.getResource(fmlMapLocation);
		File fmlMapLocationData = new File(resource3.getFile());
		try (InputStream inputStream = classLoader.getResourceAsStream(fmlMapLocation)) {

			String fmlMapLocationStream = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
