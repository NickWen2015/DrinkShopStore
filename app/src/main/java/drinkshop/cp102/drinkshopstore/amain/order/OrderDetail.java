package drinkshop.cp102.drinkshopstore.amain.order;

public class OrderDetail {
    private int order_detail_id;
    private int order_id;
    private int product_id;
    private int size_id;
    private int sugar_id;
    private int ice_id;
    private int product_quantity;
    private String product_name;
    private String ice_name;
    private String sugar_name;
    private String size_name;
    private int product_price;

    public OrderDetail() {
        super();
    }

    public OrderDetail(int order_detail_id, int order_id, int product_id, int size_id, int sugar_id, int ice_id,
                       int product_quantity, String product_name, String ice_name, String sugar_name, String size_name, int product_price) {
        super();
        this.order_detail_id = order_detail_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.size_id = size_id;
        this.sugar_id = sugar_id;
        this.ice_id = ice_id;
        this.product_quantity = product_quantity;
        this.product_name = product_name;
        this.ice_name = ice_name;
        this.sugar_name = sugar_name;
        this.size_name = size_name;
        this.product_price = product_price;
    }

    public OrderDetail(int order_id, String product_name, String ice_name, String sugar_name, String size_name, int product_price,int product_quantity) {
        this.order_id = order_id;
        this.product_name = product_name;
        this.ice_name = ice_name;
        this.sugar_name = sugar_name;
        this.size_name = size_name;
        this.product_price = product_price;
        this.product_quantity = product_quantity;

    }


    public int getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(int order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getSugar_id() {
        return sugar_id;
    }

    public void setSugar_id(int sugar_id) {
        this.sugar_id = sugar_id;
    }

    public int getIce_id() {
        return ice_id;
    }

    public void setIce_id(int ice_id) {
        this.ice_id = ice_id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getIce_name() {
        return ice_name;
    }

    public void setIce_name(String ice_name) {
        this.ice_name = ice_name;
    }

    public String getSugar_name() {
        return sugar_name;
    }

    public void setSugar_name(String sugar_name) {
        this.sugar_name = sugar_name;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }
}
