package server.dto;

import java.util.Objects;

public class Food {
    private int fdcId;
    private String description;
    private String dataType;

    public Food(int fdcId, String description, String dataType) {
        this.fdcId = fdcId;
        this.description = description;
        this.dataType = dataType;
    }

    public int getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Food food = (Food) o;
        return fdcId == food.fdcId && Objects.equals(description, food.description) && Objects.equals(dataType, food.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fdcId, description, dataType);
    }

    @Override
    public String toString() {
        return "Food{" +
                "fdcId=" + fdcId +
                ", description='" + description + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
