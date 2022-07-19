package com.mindex.challenge.data;

import java.util.Date;

/**
 * Tracks the salary and effective date of offers made to employees
 */
public class Compensation {
    /** The employee associated with a given salary */
    private Employee employee;
    /** Total compensation for an employee */
    private Integer salary;
    /** The date at which the given salary went into effect */
    private Date effectiveDate;

    public Compensation() {  
    }

    public Compensation(Employee employee, Integer salary, Date effectiveDate) {
        setEmployee(employee);
        setSalary(salary);
        setEffectiveDate(effectiveDate);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
