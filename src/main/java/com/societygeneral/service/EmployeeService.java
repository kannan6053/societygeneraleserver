package com.societygeneral.service;


import java.util.List;

import com.societygeneral.exception.ResourceNotFoundException;
import com.societygeneral.model.Employee;

public interface EmployeeService {

	public boolean deleteEmployee(Long employeeId)
			throws ResourceNotFoundException;
	
	public Employee updateEmployee(Long employeeId,Employee employeeDetails) throws ResourceNotFoundException; 
	
	public List<Employee> getAllEmployees();
	
	public Employee getEmployeeById(Long employeeId)
			throws ResourceNotFoundException;
	
	public Employee createEmployee(Employee employee);

	
}
