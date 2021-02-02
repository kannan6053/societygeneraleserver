package com.societygeneral.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.societygeneral.exception.ResourceNotFoundException;
import com.societygeneral.model.Employee;
import com.societygeneral.repo.EmployeeRepository;
import com.societygeneral.service.EmployeeService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/societygenerale")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
				return ResponseEntity.ok().body(employeeService.getEmployeeById(employeeId));
	}

	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.createEmployee(employee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			 @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employeeDetails));
	}

	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", employeeService.deleteEmployee(employeeId));
		return response;
	}
}
