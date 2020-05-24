package org.springframework.samples.petfinder.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.petfinderapi.web.PetfinderAPIController;

public class PetfinderAPITest {
	
	private static final String URL_BASE = "https://api.petfinder.com/v2";
	private static final int petId = 48030239;
	
	@Test
	public void shouldGetToken() {
		given().
			contentType("application/x-www-form-urlencoded").
			body("grant_type=client_credentials&client_id=ldKg019S92FI0uTWakbgc49jLlkTuwtXVUJTWKk0XVWzZE9Jem&client_secret=EyRXEJATZyuPARDOxJJfNLMFBIXGpx9FEuRvaKEj").			
		when().
			post(URL_BASE+"/oauth2/token").
		then().
			statusCode(200).
		and().
			assertThat().body("token_type", equalTo("Bearer")).
			assertThat().body(containsString("access_token")).
		and().
			time(lessThan(3L), TimeUnit.SECONDS);
	}
	
	@Test
	public void shouldGetAnimals() {
		String token = PetfinderAPIController.getToken();
		given().
			header("Authorization", "Bearer " + token).
		when().
			get(URL_BASE+"/animals?"+"type=dog&size=small&gender=male&limit=10").
		then().
			statusCode(200).
		and().
			assertThat().body("animals", hasSize(equalTo(10))).
		and().
			time(lessThan(6L), TimeUnit.SECONDS);
	}
	
	@Test
	public void shouldGetAnimalTypes() {
		String token = PetfinderAPIController.getToken();
		given().
			header("Authorization", "Bearer " + token).
		when().
			get(URL_BASE+"/types").
		then().
			statusCode(200).
		and().
		assertThat().body(containsString("types")).
		and().
			time(lessThan(5L), TimeUnit.SECONDS);
	}
	
	@Test
	public void shouldGetAnimal() {
		String token = PetfinderAPIController.getToken();
		given().
			header("Authorization", "Bearer " + token).
		when().
			get(URL_BASE+"/animals/"+petId).
		then().
			statusCode(200).
		and().
			assertThat().body("animal.id", equalTo(petId)).
		and()
			.body("animal.name", equalTo("Bunny")).
		and().
			time(lessThan(6L), TimeUnit.SECONDS);
	}
}
