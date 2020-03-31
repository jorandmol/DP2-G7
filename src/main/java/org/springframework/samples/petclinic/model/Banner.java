package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "banners")
public class Banner extends BaseEntity{
	
	@Column(name = "picture")
	@NotBlank
	@URL
	private String				picture;

	@Column(name = "slogan")
	@NotBlank
	private String				slogan;
	
	@Column(name = "target_url")
	@NotBlank
	@URL
	private String				targetUrl;
	
	@Column(name = "organization_name")
	@NotBlank
	private String				organizationName;
	
	@Column(name= "init_colab_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate 			initColabDate;
	
	@Column(name= "end_colab_date")
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate 			endColabDate;
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture=picture;
	}
	
	public String getSlogan() {
		return slogan;
	}
	
	public void setSlogan(String slogan) {
		this.slogan=slogan;
	}
	
	public String getTargetUrl() {
		return targetUrl;
	}
	
	public void setTargetUrl(String targetUrl) {
		this.targetUrl=targetUrl;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName=organizationName;
	}

	public LocalDate getInitColabDate() {
		return initColabDate;
	}

	public void setInitColabDate(LocalDate initColabDate) {
		this.initColabDate = initColabDate;
	}

	public LocalDate getEndColabDate() {
		return endColabDate;
	}

	public void setEndColabDate(LocalDate endColabDate) {
		this.endColabDate = endColabDate;
	}
	
}
