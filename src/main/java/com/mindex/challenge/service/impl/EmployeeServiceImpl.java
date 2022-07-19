package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure generateReportingStructure(String id) {
        LOG.debug("Generating reporting structure for employee with id [{}]", id);

        //  Need to check if the employee themselves exists
        Employee employee = read(id);
        Integer reports = 0;
         
        //  Now we calculate the number of direct reports by moving through their own direct reports with BFS
        if (employee != null && employee.getDirectReports() != null && !employee.getDirectReports().isEmpty()) {
            Deque<Employee> directReports = new LinkedList<Employee>(employee.getDirectReports());
             
            while (!directReports.isEmpty()) {
                Employee directReport = read(directReports.pop().getEmployeeId());
                reports++;
                
                //  If they have any reports themselves, we'll want to add those to the pile
                if (directReport.getDirectReports() != null && !directReport.getDirectReports().isEmpty()) {
                    directReports.addAll(directReport.getDirectReports());
                }
            }
        }
 
        return new ReportingStructure(employee, reports);
    }
}
