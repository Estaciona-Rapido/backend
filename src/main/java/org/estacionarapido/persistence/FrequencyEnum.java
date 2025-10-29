package org.estacionarapido.persistence;

public enum FrequencyEnum {
    MINUTE(1),
    HOUR(60),
    MONTH(43200);

    private int valueInMinutes;

    private FrequencyEnum(int valueInMinutes){
        this.valueInMinutes = valueInMinutes;
    }

    public int getValueInMinutes() {
        return valueInMinutes;
    }
}
