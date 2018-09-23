package drinkshop.cp102.drinkshopstore.amain.product;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable {

    private int product_id;
    private String category_name;


    public Category(int product_id, String catogory_name) {
        this.product_id = product_id;
        this.category_name = category_name;

    }

    @Override
    public boolean equals(Object obj){
        return this.product_id == ((Category) obj).product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }



}
