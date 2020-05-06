package com.amida.saraswatitranslationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SaraswatiTranslationSerivceApplicationTests {

	@Test
	void contextLoads() {
		
		String fmlTestString = "fmltestobjects/r3patient.json -output fmltestobjects/test.xml -transform fmltestobjects/PatientR3toR4.txt -ig ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:3.8.0";
		String fmlTestSource = "fmltestobjects/r3patient.json";
		String fmlOutPutLocation = "fmltestobjects/test.xml";
		String fmlMapLocation= "fmltestobjects/PatientR3toR4.txt";
		String fmlInfoSource= "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:3.8.0";
	}

}
