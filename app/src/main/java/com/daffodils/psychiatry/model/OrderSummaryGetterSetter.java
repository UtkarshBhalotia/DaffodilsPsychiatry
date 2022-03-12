package com.daffodils.psychiatry.model;

public class OrderSummaryGetterSetter {

    String CourseName, ModuleName, Amount, ModuleID, SubscriptionTypeID;

    public OrderSummaryGetterSetter(String CourseName, String ModuleName, String Amount, String ModuleID, String SubscriptionTypeID){

        this.CourseName = CourseName;
        this.ModuleName = ModuleName;
        this.Amount = Amount;
        this.ModuleID = ModuleID;
        this.SubscriptionTypeID = SubscriptionTypeID;
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

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getSubscriptionTypeID() {
        return SubscriptionTypeID;
    }

    public void setSubscriptionTypeID(String subscriptionTypeID) {
        SubscriptionTypeID = subscriptionTypeID;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
