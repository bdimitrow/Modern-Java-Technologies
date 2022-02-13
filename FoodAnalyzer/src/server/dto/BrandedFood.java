//package server.dto;
//
//import java.util.Objects;
//
//public class BrandedFood extends Food {
//    private final int gtinUpc;
//
//    public BrandedFood(int fdcId, String description, String dataType, int gtinUpc) {
//        super(fdcId, description, dataType);
//        this.gtinUpc = gtinUpc;
//    }
//
//    public int getGtinUpc() {
//        return gtinUpc;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        if (!super.equals(o)) {
//            return false;
//        }
//        BrandedFood that = (BrandedFood) o;
//        return Objects.equals(gtinUpc, that.gtinUpc);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), gtinUpc);
//    }
//
//    @Override
//    public String toString() {
//        return "BrandedFood{" +
//                "fdcId=" + super.getFdcId() +
//                ", description='" + super.getDescription() + '\'' +
//                ", dataType='" + super.getDataType() + '\'' +
//                ", grtinUpc='" + gtinUpc + '\'' +
//                '}';
//    }
//
//}
