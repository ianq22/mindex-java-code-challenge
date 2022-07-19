package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String generateReportingStructureUrl;
    private Integer expectedReports;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        generateReportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
        expectedReports = 4;
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testGenerateReportingStructure() {
        //  Create some test employees
        Employee lowLevelEmployee1 = new Employee();
        lowLevelEmployee1.setFirstName("John");
        lowLevelEmployee1.setLastName("Doe");
        lowLevelEmployee1.setDepartment("Engineering");
        lowLevelEmployee1.setPosition("Developer");

        Employee lowLevelEmployee2 = new Employee();
        lowLevelEmployee2.setFirstName("Jane");
        lowLevelEmployee2.setLastName("Doe");
        lowLevelEmployee2.setDepartment("Engineering");
        lowLevelEmployee2.setPosition("Developer Operations");

        Employee midLevelEmployee1 = new Employee();
        midLevelEmployee1.setFirstName("Matt");
        midLevelEmployee1.setLastName("Walsh");
        midLevelEmployee1.setDepartment("Customer Support");
        midLevelEmployee1.setPosition("Support Lead");

        Employee midLevelEmployee2 = new Employee();
        midLevelEmployee2.setFirstName("Amanda");
        midLevelEmployee2.setLastName("Mason");
        midLevelEmployee2.setDepartment("Human Resources");
        midLevelEmployee2.setPosition("Talent Acquisition");

        Employee topLevelEmployee = new Employee();
        topLevelEmployee.setFirstName("Puja");
        topLevelEmployee.setLastName("Gokhale");
        topLevelEmployee.setDepartment("Management");
        topLevelEmployee.setPosition("Team Lead");

        //  Persist the low level employees to the entity layer
        lowLevelEmployee1 = restTemplate.postForEntity(employeeUrl, lowLevelEmployee1, Employee.class).getBody();
        lowLevelEmployee2 = restTemplate.postForEntity(employeeUrl, lowLevelEmployee2, Employee.class).getBody();

        //  Now add the low level employees to the mid level employee and persist
        midLevelEmployee1.setDirectReports(new ArrayList<Employee>());
        midLevelEmployee1.getDirectReports().add(lowLevelEmployee1);
        midLevelEmployee1.getDirectReports().add(lowLevelEmployee2);

        midLevelEmployee1 = restTemplate.postForEntity(employeeUrl, midLevelEmployee1, Employee.class).getBody();
        midLevelEmployee2 = restTemplate.postForEntity(employeeUrl, midLevelEmployee2, Employee.class).getBody();

        //  Finally finish out the reporting structure with the top level employee
        topLevelEmployee.setDirectReports(new ArrayList<Employee>());
        topLevelEmployee.getDirectReports().add(midLevelEmployee1);
        topLevelEmployee.getDirectReports().add(midLevelEmployee2);

        topLevelEmployee = restTemplate.postForEntity(employeeUrl, topLevelEmployee, Employee.class).getBody();

        //  Now test that we pull the reporting structure as described in the spec
        ReportingStructure reportingStructure = restTemplate.getForEntity(generateReportingStructureUrl, ReportingStructure.class, topLevelEmployee.getEmployeeId()).getBody();
        assertNotNull(reportingStructure);
        assertEmployeeEquivalence(reportingStructure.getEmployee(), topLevelEmployee);
        assertEquals(reportingStructure.getNumberOfReports(), expectedReports);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
