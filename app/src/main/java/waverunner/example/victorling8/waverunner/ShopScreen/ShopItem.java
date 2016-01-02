package waverunner.example.victorling8.waverunner.ShopScreen;

public class ShopItem extends AnyShopItem
{
    public int imageId;
    public String title;
    public String description;
    public int cost;

    public ShopItem(int imageId, String title, String description, int cost)
    {
        this.imageId = imageId;
        this.title = title;
        this.description = description;
        this.cost = cost;
    }
}
