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
//     The Original Code is JobStatus Manager.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "JobStatus")
public class JobStatus extends BaseEntity {

	private static final long serialVersionUID = 8339606417348417904L;

	private String status;
	private String type;
	private String urlText;
	private String urlPath;
	private Date startDate;
	private Date endDate;
	private Date modifiedDate;
	private boolean open = true;

	@Column(length = 128, nullable = true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(length = 128, nullable = true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(length = 128, nullable = true)
	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	@Column(length = 128, nullable = true)
	public String getUrlText() {
		return urlText;
	}

	public void setUrlText(String urlText) {
		this.urlText = urlText;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(nullable = false)
	public Boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
