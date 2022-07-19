package com.mindex.challenge.data;

/**
 * Contains the number of reports underneath a given employee
 */
public class ReportingStructure {
    private Employee employee;
    private Integer numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(Employee employee, Integer numberOfReports) {
        setEmployee(employee);
        setNumberOfReports(numberOfReports);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(Integer numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}