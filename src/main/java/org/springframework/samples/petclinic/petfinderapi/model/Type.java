
package org.springframework.samples.petclinic.petfinderapi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "coats",
    "colors",
    "genders",
    "_links"
})
@JsonIgnoreProperties(value = {"_links"})
public class Type {

    @JsonProperty("name")
    private String name;
    @JsonProperty("coats")
    private List<String> coats = null;
    @JsonProperty("colors")
    private List<String> colors = null;
    @JsonProperty("genders")
    private List<String> genders = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("coats")
    public List<String> getCoats() {
        return coats;
    }

    @JsonProperty("coats")
    public void setCoats(List<String> coats) {
        this.coats = coats;
    }

    @JsonProperty("colors")
    public List<String> getColors() {
        return colors;
    }

    @JsonProperty("colors")
    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    @JsonProperty("genders")
    public List<String> getGenders() {
        return genders;
    }

    @JsonProperty("genders")
    public void setGenders(List<String> genders) {
        this.genders = genders;
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
