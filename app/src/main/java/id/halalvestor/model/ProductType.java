package id.halalvestor.model;

public class ProductType {

    private int id;
    private String name;
    private String pieColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPieColor() {
        return pieColor;
    }

    public void setPieColor(String pieColor) {
        this.pieColor = pieColor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.id;
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductType other = (ProductType) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
