package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "banners")
public class Banner extends BaseEntity{
	
//	@Column(name = "picture")
//	@NotBlank
//	@URL
//	private String				picture;
//
//	@Column(name = "slogan")
//	@NotBlank
//	private String				slogan;
//	
//	@Column(name = "target_url")
//	@NotBlank
//	@URL
//	private String				targetUrl;
	
	@Column(name = "organization_name")
	@NotBlank
	private String				organizationName;
	
//	public String getPicture() {
//		return picture;
//	}
//	
//	public void setPicture(String picture) {
//		this.picture=picture;
//	}
//	
//	public String getSlogan() {
//		return slogan;
//	}
//	
//	public void setSlogan(String slogan) {
//		this.slogan=slogan;
//	}
//	
//	public String getTargetUrl() {
//		return targetUrl;
//	}
//	
//	public void setTargetUrl(String targetUrl) {
//		this.targetUrl=targetUrl;
//	}
//	
	public String getOrganizationName() {
		return organizationName;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName=organizationName;
	}


	
}
