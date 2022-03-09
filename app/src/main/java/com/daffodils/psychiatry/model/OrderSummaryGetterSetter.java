package com.daffodils.psychiatry.model;

public class OrderSummaryGetterSetter {

    String CourseName, ModuleName, Amount;

    public OrderSummaryGetterSetter(String CourseName, String ModuleName, String Amount){

        this.CourseName = CourseName;
        this.ModuleName = ModuleName;
        this.Amount = Amount;

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

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
