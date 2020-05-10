
package org.springframework.samples.petclinic.petfinderapi.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "primary",
    "secondary",
    "tertiary"
})
public class Colors {

    @JsonProperty("primary")
    private Object primary;
    @JsonProperty("secondary")
    private Object secondary;
    @JsonProperty("tertiary")
    private Object tertiary;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("primary")
    public Object getPrimary() {
        return primary;
    }

    @JsonProperty("primary")
    public void setPrimary(Object primary) {
        this.primary = primary;
    }

    @JsonProperty("secondary")
    public Object getSecondary() {
        return secondary;
    }

    @JsonProperty("secondary")
    public void setSecondary(Object secondary) {
        this.secondary = secondary;
    }

    @JsonProperty("tertiary")
    public Object getTertiary() {
        return tertiary;
    }

    @JsonProperty("tertiary")
    public void setTertiary(Object tertiary) {
        this.tertiary = tertiary;
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
