
package org.springframework.samples.petclinic.petfinderapi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "animals",
    "pagination"
})
@JsonIgnoreProperties(value = {"pagination"})
public class Animals {

    @JsonProperty("animals")
    private List<Animal> animals = null;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("animals")
    public List<Animal> getAnimals() {
        return animals;
    }

    @JsonProperty("animals")
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
