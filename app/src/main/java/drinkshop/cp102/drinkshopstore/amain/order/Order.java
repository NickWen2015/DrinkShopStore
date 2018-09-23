package drinkshop.cp102.drinkshopstore.amain.order;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private int order_id;
    private String invoice_prefix;
    private String invoice_no;
    private int store_id;
    private int member_id;
    private String member_name;
    private String order_accept_time;
    private String order_finish_time;
    private String order_type;
    private int delivery_id;
    private int coupon_id;
    private String order_status;
    private List<OrderDetail> orderDetailList;
    // 訂單資訊(額外欄位)
    private String invoice;
    private String store_name;
    private String store_telephone;
    private String store_mobile;
    private String store_address;
    private String store_location_x;
    private String store_location_y;
    private float coupon_discount;

    public Order() {}

    public Order(int order_id, String invoice_prefix, String invoice_no, int store_id, int member_id,
                 String order_accept_time, String order_finish_time, String order_type, int delivery_id, int coupon_id,
                 String order_status, String invoice, String store_name,
                 String store_telephone, String store_mobile, String store_address, String store_location_x,
                 String store_location_y, float coupon_discount, List<OrderDetail> orderDetailList) {
        super();
        this.order_id = order_id;
        this.invoice_prefix = invoice_prefix;
        this.invoice_no = invoice_no;
        this.store_id = store_id;
        this.member_id = member_id;
        this.order_accept_time = order_accept_time;
        this.order_finish_time = order_finish_time;
        this.order_type = order_type;
        this.delivery_id = delivery_id;
        this.coupon_id = coupon_id;
        this.order_status = order_status;
        this.invoice = invoice;
        this.store_name = store_name;
        this.store_telephone = store_telephone;
        this.store_mobile = store_mobile;
        this.store_address = store_address;
        this.store_location_x = store_location_x;
        this.store_location_y = store_location_y;
        this.coupon_discount = coupon_discount;
        this.orderDetailList = orderDetailList;
    }

    public Order(int order_id, String order_accept_time, String order_finish_time, String order_type, String invoice,
                 String store_name, String store_telephone, String store_mobile, String store_address,
                 String store_location_x, String store_location_y, float coupon_discount,
                 List<OrderDetail> orderDetailList) {
        super();
        this.order_id = order_id;
        this.order_accept_time = order_accept_time;
        this.order_finish_time = order_finish_time;
        this.order_type = order_type;
        this.invoice = invoice;
        this.store_name = store_name;
        this.store_telephone = store_telephone;
        this.store_mobile = store_mobile;
        this.store_address = store_address;
        this.store_location_x = store_location_x;
        this.store_location_y = store_location_y;
        this.coupon_discount = coupon_discount;
        this.orderDetailList = orderDetailList;
    }


    public Order(int order_id, String invoice, String member_name, String order_accept_time, String order_status) {
        super();
        this.order_id = order_id;
        this.invoice = invoice;
        this.member_name = member_name;
        this.order_accept_time = order_accept_time;
        this.order_status = order_status;
    }

    public Order(int order_id, String invoice_prefix, String invoice_no, int store_id, int member_id, String member_name,
                 String order_accept_time, String order_finish_time, String order_type, int delivery_id, int coupon_id,
                 String order_status, String invoice, String store_name,
                 String store_telephone, String store_mobile, String store_address, String store_location_x,
                 String store_location_y, float coupon_discount, List<OrderDetail> orderDetailList) {
        super();
        this.order_id = order_id;
        this.invoice_prefix = invoice_prefix;
        this.invoice_no = invoice_no;
        this.store_id = store_id;
        this.member_id = member_id;
        this.member_name = member_name;
        this.order_accept_time = order_accept_time;
        this.order_finish_time = order_finish_time;
        this.order_type = order_type;
        this.delivery_id = delivery_id;
        this.coupon_id = coupon_id;
        this.order_status = order_status;
        this.invoice = invoice;
        this.store_name = store_name;
        this.store_telephone = store_telephone;
        this.store_mobile = store_mobile;
        this.store_address = store_address;
        this.store_location_x = store_location_x;
        this.store_location_y = store_location_y;
        this.coupon_discount = coupon_discount;
        this.orderDetailList = orderDetailList;
    }


    @Override
    // 要比對欲加入的Order的order_id是否相同，是則值相同
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Order)) {
            return false;
        }

        return this.getOrder_id() == ((Order) obj).getOrder_id();
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getInvoice_prefix() {
        return invoice_prefix;
    }

    public void setInvoice_prefix(String invoice_prefix) {
        this.invoice_prefix = invoice_prefix;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getOrder_accept_time() {
        return order_accept_time;
    }

    public void setOrder_accept_time(String order_accept_time) {
        this.order_accept_time = order_accept_time;
    }

    public String getOrder_finish_time() {
        return order_finish_time;
    }

    public void setOrder_finish_time(String order_finish_time) {
        this.order_finish_time = order_finish_time;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_telephone() {
        return store_telephone;
    }

    public void setStore_telephone(String store_telephone) {
        this.store_telephone = store_telephone;
    }

    public String getStore_mobile() {
        return store_mobile;
    }

    public void setStore_mobile(String store_mobile) {
        this.store_mobile = store_mobile;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getStore_location_x() {
        return store_location_x;
    }

    public void setStore_location_x(String store_location_x) {
        this.store_location_x = store_location_x;
    }

    public String getStore_location_y() {
        return store_location_y;
    }

    public void setStore_location_y(String store_location_y) {
        this.store_location_y = store_location_y;
    }

    public float getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(float coupon_discount) {
        this.coupon_discount = coupon_discount;
    }
}
