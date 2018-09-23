package drinkshop.cp102.drinkshopstore.amain.product;

/**
 * 商品
 * @author mrosstro
 * */
public class Product {
    private long id;
    private String Category;
    private String Name;
    private int MPrice;
    private int LPrice;

    public Product(long id, String category, String name, int MPrice, int LPrice) {
        this.id = id;
        Category = category;
        Name = name;
        this.MPrice = MPrice;
        this.LPrice = LPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getMPrice() {
        return MPrice;
    }

    public void setMPrice(int MPrice) {
        this.MPrice = MPrice;
    }

    public int getLPrice() {
        return LPrice;
    }

    public void setLPrice(int LPrice) {
        this.LPrice = LPrice;
    }
}
