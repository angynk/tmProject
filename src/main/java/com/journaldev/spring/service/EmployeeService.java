package com.journaldev.spring.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.journaldev.hibernate.data.Employee;

@Component
public class EmployeeService {
	@Autowired
	private SessionFactory sessionFactorySqlServer;

	public SessionFactory getSessionFactory() {
		return sessionFactorySqlServer;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactorySqlServer = sessionFactory;
	}
	
	@Transactional
	public void register(Employee emp){
		// Acquire session
		Session session = sessionFactorySqlServer.getCurrentSession();
		// Save employee, saving behavior get done in a transactional manner
		session.save(emp);		
	}

}
