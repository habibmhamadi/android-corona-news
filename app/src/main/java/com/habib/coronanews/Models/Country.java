package com.habib.coronanews.Models;

public class Country {
    private String name="",flag="",newCases="",newDeaths="",day="";
    private int totalCases=0,active=0,critical=0,recovered=0,totalDeaths=0;

    public Country() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public void setNewDeaths(String newDeaths) {
        this.newDeaths = newDeaths;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public String getNewCases() {
        return newCases;
    }

    public String getNewDeaths() {
        return newDeaths;
    }

    public String getDay() {
        return day;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getActive() {
        return active;
    }

    public int getCritical() {
        return critical;
    }

    public int getRecovered() {
        return recovered;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }
}
