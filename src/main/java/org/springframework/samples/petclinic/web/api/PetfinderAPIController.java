package org.springframework.samples.petclinic.web.api;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class PetfinderAPIController {

	private static final String URL_BASE = "https://api.petfinder.com/v2";
	
	@ModelAttribute("access_token")
	public String initTokenAttribute() {
		return getToken();
	}
	
	@GetMapping(value = "/adoptions")
	public String initAdoptionsSearchForm() {
		return "adoptions/adoptionsSearchForm";
	}
	
	@GetMapping(value = "/adoptions/find")
	public String processAdoptionsSearch(@RequestParam String type, @RequestParam String size, @RequestParam String gender, ModelMap modelMap) {
		HttpHeaders headers = new HttpHeaders();
		String token = modelMap.get("access_token").toString();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity req = new HttpEntity(headers);
		
		try {
			String url = URL_BASE+"/animals?"+"type="+type+"&size="+size+"&gender="+gender;
			ResponseEntity<String> res = new RestTemplate().exchange(url, HttpMethod.GET, req, String.class);
			String json = res.getBody();
			// TODO
			modelMap.put("res", json);
		} catch (Exception e) {
			modelMap.put("notFound", "Not Found");
		}
		
		return "adoptions/adoptionsListResult";
	}
	
	private String getToken() {
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
