////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2011 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 1.1 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is Vulnerability Manager.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.webapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.denimgroup.threadfix.data.entities.Waf;
import com.denimgroup.threadfix.data.entities.WafType;
import com.denimgroup.threadfix.service.WafService;

@Controller
@RequestMapping("/wafs/new")
@SessionAttributes("waf")
public class AddWafController {

	private WafService wafService = null;
	
	private final Log log = LogFactory.getLog(AddApplicationChannelController.class);

	@Autowired
	public AddWafController(WafService wafService) {
		this.wafService = wafService;
	}

	@ModelAttribute
	public List<WafType> populateWafTypes() {
		return wafService.loadAllWafTypes();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String newForm(Model model) {
		Waf waf = new Waf();
		model.addAttribute(waf);
		return "wafs/form";
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setAllowedFields(new String [] { "name", "wafType.id" });
	}

	@RequestMapping(method = RequestMethod.POST)
	public String newSubmit(@Valid @ModelAttribute Waf waf, BindingResult result,
			SessionStatus status) {
		if (result.hasErrors()) {
			return "wafs/form";
		} else {
			if (waf.getName().trim().equals("")) {
				result.rejectValue("name", null, null, "This field cannot be blank");
			} else {
				Waf databaseWaf = wafService.loadWaf(waf.getName().trim());
				if (databaseWaf != null)
					result.rejectValue("name", "errors.nameTaken");
			}
			
			if (waf.getWafType() == null)
				result.rejectValue("wafType.id", "errors.required", new String [] { "WAF Type" }, null );
			else if (wafService.loadWafType(waf.getWafType().getId()) == null)
				result.rejectValue("wafType.id", "errors.invalid", new String [] { waf.getWafType().getId().toString() }, null );
			
			if (result.hasErrors())
				return "wafs/form";
			
			wafService.storeWaf(waf);
			
			String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
			log.debug(currentUser + " has created a WAF with the name " + waf.getName() + 
					", the type " + waf.getWafType().getName() + 
					" and ID " + waf.getId() + ".");
			
			status.setComplete();
			return "redirect:/wafs/" + waf.getId();
		}
	}

}
