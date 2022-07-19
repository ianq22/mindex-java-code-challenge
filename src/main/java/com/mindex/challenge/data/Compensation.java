package com.mindex.challenge.data;

import java.util.Date;

/**
 * Tracks the salary and effective date of offers made to employees
 */
public class Compensation {
    private Employee employee;
    private Integer salary;
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
