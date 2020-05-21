package org.springframework.samples.petclinic.petfinderapi.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.petfinderapi.model.Animal;
import org.springframework.samples.petclinic.petfinderapi.model.Animals;
import org.springframework.samples.petclinic.petfinderapi.model.Pet;
import org.springframework.samples.petclinic.petfinderapi.model.Type;
import org.springframework.samples.petclinic.petfinderapi.model.Types;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class PetfinderAPIController {

	private static final String URL_BASE = "https://api.petfinder.com/v2";

	@ModelAttribute("access_token")
	public String initTokenAttribute() {
		return getToken();
	}

	@GetMapping(value = "/adoptions")
	public String initAdoptionsSearchForm(ModelMap modelMap) {
        HttpEntity req = setupRequest(modelMap);

        try {
            String url = URL_BASE+"/types";
            String json = getResponse(url, HttpMethod.GET, req);

            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Types results = mapper.readValue(json, Types.class);
            List<Type> types = results.getTypes();
            modelMap.put("types", types);
        } catch (Exception e) {
            modelMap.put("error", "No types have been found. There may be a problem connecting to the Petfinder API.");
        }

	    return "adoptions/adoptionsSearchForm";
	}

	@GetMapping(value = "/adoptions/find")
	public String processAdoptionsSearch(@RequestParam String type, @RequestParam String size, @RequestParam String gender, ModelMap modelMap) {
		HttpEntity req = setupRequest(modelMap);

		try {
			String url = URL_BASE+"/animals?"+"type="+type+"&size="+size+"&gender="+gender;
			String json = getResponse(url, HttpMethod.GET, req);

            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Animals results = mapper.readValue(json, Animals.class);

            modelMap.put("animals", results.getAnimals());
		} catch (Exception e) {
			modelMap.put("notFound", "Pets not found with these parameters.");
		}

		return "adoptions/adoptionsListResult";
	}

	@GetMapping(value = "/adoptions/pet/{petId}")
	public String showPetToAdopt(@PathVariable("petId") final int petId, ModelMap modelMap) {
		HttpEntity req = setupRequest(modelMap);

		try {
			String url = URL_BASE+"/animals/"+petId;
			String json = getResponse(url, HttpMethod.GET, req);
			
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        Pet results = mapper.readValue(json, Pet.class);
	        			
	        modelMap.put("numPhotos", results.getAnimal().getPhotos().size());
			modelMap.put("pet", results.getAnimal());
		}catch (Exception e) {
            modelMap.put("error", "No details of this pet have been found. There may be a problem connecting to the Petfinder API.");
		}
		
		return "adoptions/adoptionsPetDetails";
	}
	
	private String getResponse(String url, HttpMethod method, HttpEntity req) {
        ResponseEntity<String> res = new RestTemplate().exchange(url, method.GET, req, String.class);
        return res.getBody();
    }

	private HttpEntity setupRequest(ModelMap modelMap) {
        HttpHeaders headers = new HttpHeaders();
        String token = modelMap.get("access_token").toString();
        headers.add("Authorization", "Bearer " + token);
        return new HttpEntity(headers);
    }

	public static String getToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "client_credentials");
		map.add("client_id", "ldKg019S92FI0uTWakbgc49jLlkTuwtXVUJTWKk0XVWzZE9Jem");
		map.add("client_secret", "EyRXEJATZyuPARDOxJJfNLMFBIXGpx9FEuRvaKEj");

		HttpEntity req = new HttpEntity(map, headers);
		ResponseEntity<String> res = new RestTemplate().exchange(URL_BASE+"/oauth2/token", HttpMethod.POST, req, String.class);
		JSONObject json = new JSONObject(res.getBody().toString());

		return json.get("access_token").toString();
	}

}
