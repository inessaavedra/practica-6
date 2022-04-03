package com.icai.practicas.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProcessControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void given_app_when_login_using_right_credencials_then_Ok() throws Exception{
        
        //Le pasamos el puerto
        String address = "http://localhost:"+port+"/api/v1/process-step1";

        
        ProcessController.DataRequest dataPrueba = new ProcessController.DataRequest("Ines", "50484888A", "684145339");
        //Request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProcessController.DataRequest> request = new HttpEntity<>(dataPrueba, headers);
        //Response
        ResponseEntity<String> result = this.restTemplate.postForEntity(address, request, String.class);
		
        then(result.getBody()).isEqualTo("{\"result\":\"OK\"}");
        then(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

    @Test
    public void given_app_when_login_using_right_credencials_then_ko() throws Exception{
        
        //Le pasamos el puerto
        String address = "http://localhost:"+port+"/api/v1/process-step1";

        ProcessController.DataRequest dataErrorDNI = new ProcessController.DataRequest("Ines", "11111111A", "684145339");
        ProcessController.DataRequest dataErrorTelf = new ProcessController.DataRequest("Ines", "50484888A", "11111111111111111");
        //Request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<ProcessController.DataRequest> requestErrorDNI = new HttpEntity<>(dataErrorDNI, headers);
        HttpEntity<ProcessController.DataRequest> requestErrorTelf = new HttpEntity<>(dataErrorTelf, headers);
        //Response
        
		ResponseEntity<String> resultErrorDNI = this.restTemplate.postForEntity(address, requestErrorDNI, String.class);
        ResponseEntity<String> resulttErrorTelf = this.restTemplate.postForEntity(address, requestErrorTelf, String.class);
        
        
        then(resultErrorDNI.getBody()).isEqualTo("{\"result\":\"KO\"}");
        then(resulttErrorTelf.getBody()).isEqualTo("{\"result\":\"KO\"}");
        
        then(resultErrorDNI.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(resulttErrorTelf.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void given_app_when_login_using_right_credentials_then_ok_legacy() throws Exception{
            
            //Le pasamos el puerto
            String address = "http://localhost:" + port + "/api/v1/process-step1-legacy";

            //Los datos introducidos son correctos
            MultiValueMap<String, String> datosCorrectos = new LinkedMultiValueMap<>();
            datosCorrectos.add("fullName", "Ines");
            datosCorrectos.add("dni", "50484888A");
            datosCorrectos.add("telefono", "684145339");
            
            //Request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestCorrecto = new HttpEntity<>(datosCorrectos, headers);
             //Response
            ResponseEntity<String> resultCorrecto = this.restTemplate.postForEntity(address, requestCorrecto, String.class);
            
            then(resultCorrecto.getBody()).contains("Gracias por registrarte.");

            then(resultCorrecto.getStatusCode()).isEqualTo(HttpStatus.OK);
            
    }

    @Test
    public void given_app_when_login_using_right_credentials_then_ko_legacy() throws Exception{
            
            //Le pasamos el puerto
            String address = "http://localhost:" + port + "/api/v1/process-step1-legacy";


            //El nombre introducido es incorrecto
            MultiValueMap<String, String> datosNombreIncorrecto = new LinkedMultiValueMap<>();
            datosNombreIncorrecto.add("fullName", "Ana");
            datosNombreIncorrecto.add("dni", "50484888A");
            datosNombreIncorrecto.add("telefono", "684145339");

            //El nombre vacío
            MultiValueMap<String, String> datosNombreNull = new LinkedMultiValueMap<>();
            datosNombreIncorrecto.add("fullName", "");
            datosNombreIncorrecto.add("dni", "50484888A");
            datosNombreIncorrecto.add("telefono", "684145339");

            //El DNI introducido es incorrecto
            MultiValueMap<String, String> datosDNIIncorrecto = new LinkedMultiValueMap<>();
            datosDNIIncorrecto.add("fullName", "Ines");
            datosDNIIncorrecto.add("dni", "11111111A");
            datosDNIIncorrecto.add("telefono", "684145339");

            //El número introducido es incorrecto
            MultiValueMap<String, String> datosNumeroIncorrecto = new LinkedMultiValueMap<>();
            datosNumeroIncorrecto.add("fullName", "Ines");
            datosNumeroIncorrecto.add("dni", "50484888A");
            datosNumeroIncorrecto.add("telefono", "11111111111111111");

            //Request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestErrorNombre = new HttpEntity<>(datosNombreIncorrecto, headers);
            HttpEntity<MultiValueMap<String, String>> requestVacioNombre = new HttpEntity<>(datosNombreNull, headers);
            HttpEntity<MultiValueMap<String, String>> requestErrorDNI = new HttpEntity<>(datosDNIIncorrecto, headers);
            HttpEntity<MultiValueMap<String, String>> requestErrorTelefono = new HttpEntity<>(datosNumeroIncorrecto, headers);
            //Response
            ResponseEntity<String> resultErrorNombre = this.restTemplate.postForEntity(address, requestErrorNombre, String.class);
            ResponseEntity<String> resultVacioNombre = this.restTemplate.postForEntity(address, requestVacioNombre, String.class);
            ResponseEntity<String> resultErrorDNI = this.restTemplate.postForEntity(address, requestErrorDNI, String.class);
            ResponseEntity<String> resultErrorTelefono = this.restTemplate.postForEntity(address, requestErrorTelefono, String.class);
            
            then(resultErrorDNI.getBody()).contains("Ha habido un problema con la solictud deseada");
            then(resultErrorTelefono.getBody()).contains("Ha habido un problema con la solictud deseada");
            
            then(resultErrorNombre.getStatusCode()).isEqualTo(HttpStatus.OK);
            then(resultVacioNombre.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            then(resultErrorDNI.getStatusCode()).isEqualTo(HttpStatus.OK);
            then(resultErrorTelefono.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}