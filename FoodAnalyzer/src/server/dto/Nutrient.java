package server.dto;

import java.util.Objects;

public class Nutrient {
    private final String name;
    private final String unitName;

    public Nutrient(String name, String unitName) {
        this.name = name;
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nutrient nutrient = (Nutrient) o;
        return Objects.equals(name, nutrient.name) && Objects.equals(unitName, nutrient.unitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unitName);
    }

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return unitName;
    }

    @Override
    public String toString() {
        return "Nutrient{" +
                "name='" + name + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
