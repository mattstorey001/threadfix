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
package com.denimgroup.threadfix.service.queue;

import java.util.List;

/**
 * @author bbeverly
 * 
 */
public interface QueueSender {

	/**
	 * 
	 */
	void startDefectTrackerSync();

	/**
	 * 
	 */
	void startImportScans();

	/**
	 * @param orgId
	 */
	void importSentinelScans(int orgId, String apiKey);

	/**
	 * @param appId
	 */
	void importSentinelAppScans(int orgId, int appId, String apiKey);

	/**
	 * @param fileName
	 * @param channelId
	 */
	void addScanToQueue(String fileName, Integer channelId, Integer orgId, Integer appId);

	/**
	 * @param appId
	 */
	void addDefectTrackerVulnUpdate(Integer orgId, Integer appId);

	/**
	 * @param vulns
	 * @param summary
	 * @param preamble
	 * @param component
	 * @param version
	 * @param severity
	 */
	void addSubmitDefect(List<Integer> vulns, String summary, String preamble, String component,
			String version, String severity, Integer orgId, Integer applicationId);
}
