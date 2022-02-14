package server.dto;

import java.util.Objects;

public class Food {
    private int fdcId;
    private String description;
    private String dataType;
    private String gtinUpc;

    public Food(int fdcId, String description, String dataType, String gtinUpc) {
        this.fdcId = fdcId;
        this.description = description;
        this.dataType = dataType;
        this.gtinUpc = gtinUpc;
    }

    public int getFdcId() {
        return fdcId;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return fdcId == food.fdcId &&
                Objects.equals(description, food.description) &&
                Objects.equals(dataType, food.dataType) &&
                Objects.equals(gtinUpc, food.gtinUpc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fdcId, description, dataType, gtinUpc);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("Food{ fdcId='%d", fdcId));
        result.append(String.format("', description='%s", description));
        result.append(String.format("', dataType='%s", dataType));
        if (gtinUpc != null) {
            result.append(String.format("', GTIN/UPC='%s", gtinUpc));
        }
        result.append("'}");

        return result.toString();
    }

}
