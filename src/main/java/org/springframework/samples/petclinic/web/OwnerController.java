/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private static final String REDIRECT_TO_OUPS = "redirect:/oups";

	private final OwnerService ownerService;
	
	@Autowired
	public OwnerController(final OwnerService ownerService, final UserService userService,
			final AuthoritiesService authoritiesService) {
		this.ownerService = ownerService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("owner")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new OwnerValidator());
	}

	@GetMapping(value = "/owners/new")
	public String initCreationForm(ModelMap model) {
		if (isAdmin()) {
			Owner owner = new Owner();
			model.put("owner", owner);
			return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping(value = "/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, ModelMap model) {
		if (isAdmin()) {
			if (result.hasErrors()) {
				model.addAttribute("owner", owner);
				return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
			} else {
				// creating owner, user and authorities
				try {
					this.ownerService.saveOwner(owner);
				} catch (Exception ex) {

					if (ex.getClass().equals(DataIntegrityViolationException.class))
						result.rejectValue("user.username", "duplicate");

					return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
				}

				return "redirect:/owners/" + owner.getId();
			}
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners/find")
	public String initFindForm(final Map<String, Object> model) {
		if (isAdmin()) {
			model.put("owner", new Owner());
			return "owners/findOwners";
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@GetMapping(value = "/owners")
	public String processFindForm(Owner owner, final BindingResult result, final Map<String, Object> model) {

		if (isAdmin()) {

			// allow parameterless GET request for /owners to return all records
			if (owner.getLastName() == null) {
				owner.setLastName(""); // empty string signifies broadest possible search
			}

			// find owners by last name
			Collection<Owner> results = this.ownerService.findOwnerByLastName(owner.getLastName());
			if (results.isEmpty()) {
				// no owners found
				result.rejectValue("lastName", "notFound", "not found");
				return "owners/findOwners";
			} else if (results.size() == 1) {
				// 1 owner found
				owner = results.iterator().next();
				return "redirect:/owners/" + owner.getId();
			} else {
				// multiple owners found
				model.put("selections", results);
				return "owners/ownersList";
			}
		} else {
			return REDIRECT_TO_OUPS;
		}

	}

	@GetMapping(value = "/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") final int ownerId, final Model model) {
		if (securityAccessRequestProfile(ownerId)) {
			Owner owner = this.ownerService.findOwnerById(ownerId);
			model.addAttribute("owner", owner);
			model.addAttribute("username", owner.getUser().getUsername());
			model.addAttribute("edit", true);
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	@PostMapping(value = "/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId,
			ModelMap model) {
		if (securityAccessRequestProfile(ownerId)) {

			model.addAttribute("edit", true);
			Owner ownerToUpdate = this.ownerService.findOwnerById(ownerId);
			model.addAttribute("username", ownerToUpdate.getUser().getUsername());

			if (result.hasErrors()) {
				model.put("owner", owner);
				return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
			} else {

				BeanUtils.copyProperties(owner, ownerToUpdate, "id", "user");
				User user = ownerToUpdate.getUser();
				user.setPassword(owner.getUser().getPassword());
				ownerToUpdate.setUser(user);
				this.ownerService.saveOwner(ownerToUpdate);

				return "redirect:/owners/{ownerId}";
			}
		} else {
			return REDIRECT_TO_OUPS;
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * 
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") final int ownerId) {
		if (securityAccessRequestProfile(ownerId)) {
			ModelAndView mav = new ModelAndView("owners/ownerDetails");
			mav.addObject(this.ownerService.findOwnerById(ownerId));
			return mav;
		} else {
			ModelAndView mavOups = new ModelAndView("redirect:/oups");
			return mavOups;
		}
	}

	private boolean isAdmin() {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		return authority.equals("admin");
	}

	private boolean securityAccessRequestProfile(int ownerId) {
		String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.collect(Collectors.toList()).get(0).toString();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Owner owner = this.ownerService.findOwnerById(ownerId);

		return authority.equals("admin") || authority.equals("owner") && username.equals(owner.getUser().getUsername());
	}

}
