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
package com.denimgroup.threadfix.data.dao.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.denimgroup.threadfix.data.dao.JobStatusDao;
import com.denimgroup.threadfix.data.entities.JobStatus;

/**
 * Hibernate JobStatus DAO implementation. Most basic methods are implemented in
 * the AbstractGenericDao
 * 
 * @author mcollins, dwolf
 * @see AbstractGenericDao
 */
@Repository
public class HibernateJobStatusDao implements JobStatusDao {

	private SessionFactory sessionFactory;

	@Autowired
	public HibernateJobStatusDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<JobStatus> retrieveAll() {
		return sessionFactory.getCurrentSession()
				.createQuery("from JobStatus jobStatus order by jobStatus.modifiedDate desc")
				.list();
	}

	@Override
	public JobStatus retrieveById(int id) {
		return (JobStatus) sessionFactory.getCurrentSession().get(JobStatus.class, id);
	}

	@Override
	public void saveOrUpdate(JobStatus jobStatus) {
		sessionFactory.getCurrentSession().saveOrUpdate(jobStatus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobStatus> retrieveAllOpen() {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from JobStatus jobStatus where jobStatus.open = :open "
								+ "order by jobStatus.modifiedDate desc").setBoolean("open", true)
				.list();
	}

}
