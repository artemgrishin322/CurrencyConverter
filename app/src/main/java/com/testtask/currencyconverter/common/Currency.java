package com.testtask.currencyconverter.common;

public class Currency {
    private int numCode;
    private String charCode;
    private int nominal;
    private String name;
    private double rubValue;
    private double value;

    public Currency(int numCode, String charCode, int nominal, String name, double rubValue) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.rubValue = rubValue;
    }

    public int getNumCode() {
        return numCode;
    }

    public void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRubValue() {
        return rubValue;
    }

    public void setRubValue(double rubValue) {
        this.rubValue = rubValue;
    }
}
