package com.example.diabetesmanagement.data;

public class LogMedication extends Log {

    private int amount;
    private Medication medication;

    public LogMedication() {
        setAmount(0);
        setMedication(new Medication());
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String logType()
    {
        return "Medication";
    }
}
