package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User{
	@Id
	@NotEmpty
	@Column(name = "username", length = 16)
	String username;
	
	@NotEmpty
	@Pattern(regexp = "^(?=(.*\\d))(?=(.*[A-Za-z]))(?=(.*[\\pP]))([^\\s]){10,}$|^$")
	@Column(name = "password")
	String password;
	
	@Column(name = "enabled")
	boolean enabled;
}
