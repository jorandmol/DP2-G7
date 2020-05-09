
package org.springframework.samples.petclinic.petfinderapi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "organization_id",
    "url",
    "type",
    "species",
    "breeds",
    "colors",
    "age",
    "gender",
    "size",
    "coat",
    "attributes",
    "environment",
    "tags",
    "name",
    "description",
    "photos",
    "primary_photo_cropped",
    "videos",
    "status",
    "status_changed_at",
    "published_at",
    "distance",
    "contact",
    "_links"
})
@JsonIgnoreProperties(value = {"attributes", "coat", "organization_id", "environment", "tags", "contact", "_links"})
public class Animal {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("type")
    private String type;
    @JsonProperty("species")
    private String species;
    @JsonProperty("breeds")
    private Breeds breeds;
    @JsonProperty("colors")
    private Colors colors;
    @JsonProperty("age")
    private String age;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("size")
    private String size;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("photos")
    private List<Photo> photos = null;
    @JsonProperty("primary_photo_cropped")
    private Object primaryPhotoCropped;
    @JsonProperty("videos")
    private List<Object> videos = null;
    @JsonProperty("status")
    private String status;
    @JsonProperty("status_changed_at")
    private String statusChangedAt;
    @JsonProperty("published_at")
    private String publishedAt;
    @JsonProperty("distance")
    private Object distance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("species")
    public String getSpecies() {
        return species;
    }

    @JsonProperty("species")
    public void setSpecies(String species) {
        this.species = species;
    }

    @JsonProperty("breeds")
    public Breeds getBreeds() {
        return breeds;
    }

    @JsonProperty("breeds")
    public void setBreeds(Breeds breeds) {
        this.breeds = breeds;
    }

    @JsonProperty("colors")
    public Colors getColors() {
        return colors;
    }

    @JsonProperty("colors")
    public void setColors(Colors colors) {
        this.colors = colors;
    }

    @JsonProperty("age")
    public String getAge() {
        return age;
    }

    @JsonProperty("age")
    public void setAge(String age) {
        this.age = age;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("size")
    public String getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(String size) {
        this.size = size;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("photos")
    public List<Photo> getPhotos() {
        return photos;
    }

    @JsonProperty("photos")
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @JsonProperty("primary_photo_cropped")
    public Object getPrimaryPhotoCropped() {
        return primaryPhotoCropped;
    }

    @JsonProperty("primary_photo_cropped")
    public void setPrimaryPhotoCropped(Object primaryPhotoCropped) {
        this.primaryPhotoCropped = primaryPhotoCropped;
    }

    @JsonProperty("videos")
    public List<Object> getVideos() {
        return videos;
    }

    @JsonProperty("videos")
    public void setVideos(List<Object> videos) {
        this.videos = videos;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("status_changed_at")
    public String getStatusChangedAt() {
        return statusChangedAt;
    }

    @JsonProperty("status_changed_at")
    public void setStatusChangedAt(String statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }

    @JsonProperty("published_at")
    public String getPublishedAt() {
        return publishedAt;
    }

    @JsonProperty("published_at")
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @JsonProperty("distance")
    public Object getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Object distance) {
        this.distance = distance;
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
