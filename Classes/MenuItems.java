package IceCream;

public class MenuItems {
    private int id;
    private int categoryId;
    private String category;
    private String name;
    private String flavour;
    private double price;
    private int quantity;
    private String description;
    private String origin;
    private String rawMaterials;
    private String status;

    public MenuItems() {}

    public MenuItems(int id, int categoryId, String category, String name, String flavour,
                     double price, int quantity, String description, String origin,
                     String rawMaterials, String status) {
        this.id=id; this.categoryId=categoryId; this.category=category; this.name=name;
        this.flavour=flavour; this.price=price; this.quantity=quantity;
        this.description=description; this.origin=origin; this.rawMaterials=rawMaterials;
        this.status=status;
    }
    public int getId(){return id;}
    public int getCategoryId(){return categoryId;}
    public String getCategory(){return category;}
    public String getName(){return name;}
    public String getFlavour(){return flavour;}
    public double getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public String getDescription(){return description;}
    public String getOrigin(){return origin;}
    public String getRawMaterials(){return rawMaterials;}
    public String getStatus(){return status;}
}
