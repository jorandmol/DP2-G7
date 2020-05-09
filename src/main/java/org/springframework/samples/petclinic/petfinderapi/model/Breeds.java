
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
    "mixed",
    "unknown"
})
public class Breeds {

    @JsonProperty("primary")
    private String primary;
    @JsonProperty("secondary")
    private String secondary;
    @JsonProperty("mixed")
    private Boolean mixed;
    @JsonProperty("unknown")
    private Boolean unknown;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("primary")
    public String getPrimary() {
        return primary;
    }

    @JsonProperty("primary")
    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @JsonProperty("secondary")
    public String getSecondary() {
        return secondary;
    }

    @JsonProperty("secondary")
    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    @JsonProperty("mixed")
    public Boolean getMixed() {
        return mixed;
    }

    @JsonProperty("mixed")
    public void setMixed(Boolean mixed) {
        this.mixed = mixed;
    }

    @JsonProperty("unknown")
    public Boolean getUnknown() {
        return unknown;
    }

    @JsonProperty("unknown")
    public void setUnknown(Boolean unknown) {
        this.unknown = unknown;
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
