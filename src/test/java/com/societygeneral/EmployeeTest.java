package com.societygeneral;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import com.societygeneral.congfig.JpaConfiguration;
import com.societygeneral.exception.ResourceNotFoundException;
import com.societygeneral.model.Employee;
import com.societygeneral.repo.EmployeeRepository;
import com.societygeneral.service.EmployeeService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(JpaConfiguration.class)
@ActiveProfiles("test")
public class EmployeeTest {

	@LocalServerPort
	private int randomServerPort;

	@Autowired
	EmployeeService employeeService;
	@Autowired
	EmployeeRepository empRepo;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testcreateEmployeeSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/societygenerale/employees/";
		URI uri = new URI(baseUrl);
		Employee employee = new Employee("test", "contoller", "controllerTest@email.com");
		HttpEntity<Employee> request = new HttpEntity<>(employee);
		ResponseEntity<Employee> responseEntity = this.restTemplate.postForEntity(uri, request, Employee.class);

		// Verify request succeed
		assertEquals(200, responseEntity.getStatusCodeValue());
	}

	@Test
	public void testUpdateAndGetEmployeeSuccess() throws URISyntaxException, ResourceNotFoundException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/societygenerale/employees";
		Map<String,Long> map=new HashMap<String,Long>();
		map.put("id", Long.valueOf(10000));
		
		String url=UriComponentsBuilder.fromUriString(baseUrl + "/{id}")
        .build(map)
        .toString();
		
		Employee employee = new Employee(Long.valueOf(10000),"society", "generale", "societygenerale@gmail.com");
		this.restTemplate.put(url, employee);
		Employee employeeCompare = employeeService.getEmployeeById(Long.valueOf(10000));
		
		// Verify request succeed
		assertEquals("generale",employeeCompare.getLastName() );
	}

	@Test
	public void testUpdateAndGetEmployeeFailure() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/societygenerale/employees";
		Map<String,Long> map=new HashMap<String,Long>();
		map.put("id", Long.valueOf(10500));
		
		String url=UriComponentsBuilder.fromUriString(baseUrl + "/{id}")
		        .build(map)
        .toString();
		
		Employee employee = new Employee(Long.valueOf(10500),"society", "generale", "societygenerale@gmail.com");
		this.restTemplate.put(url, employee);
		
		
		// Verify request succeed
		assertThrows(ResourceNotFoundException.class,()->{employeeService.getEmployeeById(Long.valueOf(10500));
    });
	}


	@Test
	public void testGetAllEmployeesSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/societygenerale/employees";
		
		String url=UriComponentsBuilder.fromUriString(baseUrl).build().toString();
		
		List<Employee> employeeList= this.restTemplate.getForObject(url,List.class);
		
		// Verify request succeed
		
		if (employeeList.size() > 0) {
			assertTrue(true);
		} else {
			assertTrue(false);
			;

		}	}
	@Test
	public void testDeleteEmployeesSuccess() throws URISyntaxException {
		
		Employee employeeCompare = null;
		try {
			employeeCompare = employeeService.getEmployeeById(Long.valueOf(10001));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Verify request succeed
		assertEquals("delete",employeeCompare.getLastName() );
		final String baseUrl = "http://localhost:" + randomServerPort + "/societygenerale/employees";
		Map<String,Long> map=new HashMap<String,Long>();
		map.put("id", Long.valueOf(10001));
		String url=UriComponentsBuilder.fromUriString(baseUrl + "/{id}")
		        .build(map)
		        .toString();
				
				this.restTemplate.delete(url);
						
		// Verify request succeed
				assertThrows(ResourceNotFoundException.class,()->{employeeService.getEmployeeById(Long.valueOf(10001)); });
	}
	@Test
	public void testEmployeeIdSuccess() throws URISyntaxException {
		
		Employee employeeCompare = null;
		try {
			employeeCompare = employeeService.getEmployeeById(Long.valueOf(10000));
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Verify request succeed
		final String baseUrl = "http://localhost:" + randomServerPort + "/societygenerale/employees";
		Map<String,Long> map=new HashMap<String,Long>();
		map.put("id", Long.valueOf(10000));
		String url=UriComponentsBuilder.fromUriString(baseUrl + "/{id}")
		        .build(map)
		        .toString();
				
				Employee employee= this.restTemplate.getForObject(url,Employee.class);
						
		// Verify request succeed
				assertEquals(employeeCompare,employee);
	}

	@Test
	public void createEmployeeServiceTest() throws Exception {

		Employee employee = employeeService.createEmployee(getEmployee());
		Employee employeeCompare = employeeService.getEmployeeById(employee.getId());
		assertEquals(employee, employeeCompare);

	}

	@Test
	public void updateEmployeeTest() throws Exception {
		String updatedStringToCompare = "kanishka";
		Employee employee = employeeService.createEmployee(getEmployeeForUpdate());
		employee.setFirstName("kanishka");
		Employee employeeCompare = employeeService.updateEmployee(employee.getId(), employee);
		assertEquals(employeeCompare.getFirstName(), updatedStringToCompare);

	}

	@Test
	public void getAllEmployeeTest() throws Exception {
		List<Employee> employeeList = employeeService.getAllEmployees();
		if (employeeList.size() > 3) {
			assertTrue(true);
		} else {
			assertTrue(false);
			;

		}

	}

	private Employee getEmployee() {
		// TODO Auto-generated method stub
		/*
		 * { "emailId": "string", "firstName": "string", "id": 0, "lastName": "string" }
		 */

		Employee employee = new Employee("kannan", "chermachamy", "kannan70067@gmail.com");
		return employee;

	}

	private Employee getEmployeeForUpdate() {
		// TODO Auto-generated method stub
		/*
		 * { "emailId": "string", "firstName": "string", "id": 0, "lastName": "string" }
		 */

		Employee employee = new Employee("kannan", "kannan", "kannan6053@gmail.com");
		return employee;

	}

}
