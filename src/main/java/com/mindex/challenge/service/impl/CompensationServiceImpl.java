package com.mindex.challenge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Creating compensation from employee with id [{}]", employeeId);

        Compensation compensation = new Compensation();
        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        //   If we have an employee, we'll check to see if they have an associated compensation record
        if (employee != null) {
            LOG.debug("Found employee with id [{}]", employeeId);

            compensation = compensationRepository.findByEmployee(employee);

            if (compensation == null) {
                throw new RuntimeException("Unable to find compensation for employee: " + employeeId);
            }
        } else {
            throw new RuntimeException("Employee with id [{}] does not exist: " + employeeId);
        }

        return compensation;
    }
}
