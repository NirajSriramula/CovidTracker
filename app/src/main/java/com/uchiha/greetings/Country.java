package com.uchiha.greetings;

public class Country {
    String name;
    String cases;
    String deaths;
    String recovered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
    public Country(String country,String cases,String deaths,String recoveries ){
        this.name = country;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recoveries;
    }


}
