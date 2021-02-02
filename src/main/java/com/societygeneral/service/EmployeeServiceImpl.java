package com.societygeneral.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.societygeneral.exception.ResourceNotFoundException;
import com.societygeneral.model.Employee;
import com.societygeneral.repo.EmployeeRepository;


@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService{
	

	@Autowired
	private EmployeeRepository employeeRepository;


	@Override
	public boolean deleteEmployee(Long employeeId) throws ResourceNotFoundException {
		Employee employee = getEmployeeById(employeeId);
		employeeRepository.delete(employee);
		return true;
	}

	@Override
	public Employee updateEmployee(Long employeeId, Employee employeeDetails) throws ResourceNotFoundException {
		Employee employee = getEmployeeById(employeeId);
		BeanUtils.copyProperties(employeeDetails, employee);
		Employee updatedEmployee = createEmployee(employee);	
		return updatedEmployee;
	}

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(Long employeeId) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return employee;
	}

	@Override
	public Employee createEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return employeeRepository.save(employee);
	}

}
