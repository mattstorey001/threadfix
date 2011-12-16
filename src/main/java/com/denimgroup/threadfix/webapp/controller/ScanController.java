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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.denimgroup.threadfix.data.entities.Application;
import com.denimgroup.threadfix.data.entities.ChannelType;
import com.denimgroup.threadfix.data.entities.Scan;
import com.denimgroup.threadfix.service.ApplicationService;
import com.denimgroup.threadfix.service.ChannelTypeService;
import com.denimgroup.threadfix.service.ScanService;
import com.denimgroup.threadfix.service.queue.QueueSender;
import com.denimgroup.threadfix.webapp.validator.BeanValidator;

@Controller
@RequestMapping("/organizations/{orgId}/applications/{appId}/scans")
public class ScanController {
	
	private final Log log = LogFactory.getLog(UsersController.class);

	private ScanService scanService;
	private QueueSender queueSender;
	private ApplicationService applicationService;
	private ChannelTypeService channelTypeService;

	@Autowired
	public ScanController(ScanService scanService, QueueSender queueSender,
			ApplicationService applicationService, ChannelTypeService channelTypeService) {
		this.scanService = scanService;
		this.queueSender = queueSender;
		this.applicationService = applicationService;
		this.channelTypeService = channelTypeService;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(new BeanValidator());
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView viewScans(@PathVariable("orgId") Integer orgId, 
			@PathVariable("appId") Integer appId) {
		Application application = applicationService.loadApplication(appId);
		
		if (application == null) {
			log.warn(ResourceNotFoundException.getLogMessage("Application", appId));
			throw new ResourceNotFoundException();
		}
		
		ModelAndView mav = new ModelAndView("scans/index");
		mav.addObject(application);
		return mav;
	}

	@RequestMapping(value = "/{scanId}", method = RequestMethod.GET)
	public ModelAndView detailScan(@PathVariable("orgId") Integer orgId, 
			@PathVariable("appId") Integer appId,
			@PathVariable("scanId") Integer scanId) {
		Scan scan = null;
		if (scanId != null)
			scan = scanService.loadScan(scanId);
		if (scan == null) {
			if (orgId != null && appId != null)
				return new ModelAndView("redirect:/organizations/" + orgId + "/applications/" + appId + "/scans");
			else if (orgId != null)
				return new ModelAndView("redirect:/organizations/" + orgId);
			else
				return new ModelAndView("redirect:/");
		}
		
		ModelAndView mav = new ModelAndView("scans/detail");
		mav.addObject(scan);
		mav.addObject("vulnData", scan.getReportList());
		return mav;
	}

	@RequestMapping(value = "/sentinel", method = RequestMethod.GET)
	public String processSentinelRequest(@PathVariable("appId") int appId,
			@PathVariable("orgId") int orgId) {
		Application application = applicationService.loadApplication(appId);
		
		if (application == null) {
			log.warn(ResourceNotFoundException.getLogMessage("Application", appId));
			throw new ResourceNotFoundException();
		}
		
		List<Application> applications = new ArrayList<Application>();
		applications.add(application);
		
		ChannelType sentinelChannelType = channelTypeService.loadChannel(ChannelType.SENTINEL);
		String sentinelApiKey = sentinelChannelType.getApiKey();
		
		if (sentinelApiKey != null) {
			queueSender.importSentinelAppScans(application.getOrganization()
					.getId(), appId, sentinelApiKey);
			return "redirect:/jobs/open";
		} else {
			return "redirect:/configuration/whitehat";
		}
	}
}
