package com.daffodils.psychiatry.model;

public class ValidityDetailsGetterSetter {

    String CourseName, ModuleName, StartDate, EndDate, DaysLeft;

    public ValidityDetailsGetterSetter(String CourseName, String ModuleName, String StartDate, String EndDate, String DaysLeft){
        this.CourseName = CourseName;
        this.ModuleName = ModuleName;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.DaysLeft = DaysLeft;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getDaysLeft() {
        return DaysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        DaysLeft = daysLeft;
    }
}
