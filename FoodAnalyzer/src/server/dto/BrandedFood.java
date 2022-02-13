package server.dto;

import java.util.Objects;

public class BrandedFood extends Food {
    private String grtinUpc;

    public BrandedFood(int fdcId, String description, String dataType, String grtinUpc) {
        super(fdcId, description, dataType);
        this.grtinUpc = grtinUpc;
    }

    public String getGrtinUpc() {
        return grtinUpc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BrandedFood that = (BrandedFood) o;
        return Objects.equals(grtinUpc, that.grtinUpc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), grtinUpc);
    }

    @Override
    public String toString() {
        return "BrandedFood{" +
                "fdcId=" + super.getFdcId() +
                ", description='" + super.getDescription() + '\'' +
                ", dataType='" + super.getDataType() + '\'' +
                ", grtinUpc='" + grtinUpc + '\'' +
                '}';
    }
}
