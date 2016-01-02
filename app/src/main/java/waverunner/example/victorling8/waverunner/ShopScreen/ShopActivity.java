package waverunner.example.victorling8.waverunner.ShopScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;

public class ShopActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView shop;
    private ShopAdapter shopAdapter;
    private ArrayList<AnyShopItem> shopItems = new ArrayList<>();

    public static final String GOOSE_SKIN = "Goose Skin";
    public static final String RAINBOW_SKIN = "Rainbow Skin";
    public static final String CODE_SKIN = "Code Skin";
    public static final String GALAXY_SKIN = "Galaxy Skin";
    public static final String BLOCKS_SKIN = "Blocks Skin";
    public static final String ILLUMINATI_SKIN = "Illuminati Skin";
    public static final String NYAN_CAT_SKIN = "Nyan Cat Skin";
    public static final String PINK_TIGER_SKIN = "Pink Tiger Skin";
    public static final String STARS_SKIN = "Stars Skin";
    public static final String WAVE_SKIN = "Wave Skin";
    public static final String SPACE_SKIN = "Space Skin";
    public static final String FIBRE_OPTIC_SKIN = "Fibre Optic Skin";

    public static final String MORE_POWER_UPS_1 = "Power Ups Tier I";
    public static final String MORE_POWER_UPS_2 = "Power Ups Tier II";
    public static final String MORE_POWER_UPS_3 = "Power Ups Tier III";
    public static final String COIN_POWER_UP = "Coin power up";
    public static final String CLOCK_POWER_UP = "Clock power up";

    public static final String BOUGHT_SKIN = "waverunner.example.victorling8.waverunner.bought_skin";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_layout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        shop = (ListView) findViewById(R.id.shop_section);

        int coins = AppData.getCoinAmount();

        ShopTitleItem title = new ShopTitleItem(coins);

        shopItems.add(title);

        initializeBuyItems();

        shopAdapter = new ShopAdapter(this, R.layout.shop_item, shopItems);

        shop.setAdapter(shopAdapter);
        shop.setOnItemClickListener(this);
    }

    private void initializeBuyItems()
    {
        if (! AppData.hasBoughtShopItem(this, GOOSE_SKIN))
        {
            ShopItem gooseSkin = new ShopItem(R.drawable.shop_goose_skin,
                    GOOSE_SKIN,
                    "",
                    250);

            shopItems.add(gooseSkin);
        }

        if (! AppData.hasBoughtShopItem(this, PINK_TIGER_SKIN))
        {
            ShopItem pinkTigerSkin = new ShopItem(R.drawable.shop_pink_skin,
                    PINK_TIGER_SKIN,
                    "",
                    250);

            shopItems.add(pinkTigerSkin);
        }

        if (! AppData.hasBoughtShopItem(this, CODE_SKIN))
        {
            ShopItem codeSkin = new ShopItem(R.drawable.shop_code_skin,
                    CODE_SKIN,
                    "",
                    250);

            shopItems.add(codeSkin);
        }

        if (! AppData.hasBoughtShopItem(this, WAVE_SKIN))
        {
            ShopItem waveSkin = new ShopItem(R.drawable.shop_rainbow_2_skin,
                    WAVE_SKIN,
                    "",
                    250);

            shopItems.add(waveSkin);
        }

        if (! AppData.hasBoughtShopItem(this, NYAN_CAT_SKIN))
        {
            ShopItem nyanCatSkin = new ShopItem(R.drawable.shop_nyan_cat_skin,
                    NYAN_CAT_SKIN,
                    "",
                    500);

            shopItems.add(nyanCatSkin);
        }

        if (! AppData.hasBoughtShopItem(this, SPACE_SKIN))
        {
            ShopItem spaceSkin = new ShopItem(R.drawable.shop_space_skin,
                    SPACE_SKIN,
                    "",
                    500);

            shopItems.add(spaceSkin);
        }

        if (! AppData.hasBoughtShopItem(this, RAINBOW_SKIN))
        {
            ShopItem rainbowSkin = new ShopItem(R.drawable.shop_rainbow_skin,
                    RAINBOW_SKIN,
                    "",
                    500);

            shopItems.add(rainbowSkin);
        }

        if (! AppData.hasBoughtShopItem(this, STARS_SKIN))
        {
            ShopItem starsSkin = new ShopItem(R.drawable.shop_stars_skin,
                    STARS_SKIN,
                    "",
                    500);

            shopItems.add(starsSkin);
        }

        if (! AppData.hasBoughtShopItem(this, BLOCKS_SKIN))
        {
            ShopItem blocksSkin = new ShopItem(R.drawable.shop_blocks_skin,
                    BLOCKS_SKIN,
                    "",
                    750);

            shopItems.add(blocksSkin);
        }

        if (! AppData.hasBoughtShopItem(this, GALAXY_SKIN))
        {
            ShopItem galaxySkin = new ShopItem(R.drawable.shop_abstract_skin,
                    GALAXY_SKIN,
                    "",
                    750);

            shopItems.add(galaxySkin);
        }

        if (! AppData.hasBoughtShopItem(this, FIBRE_OPTIC_SKIN))
        {
            ShopItem fibreOpticSkin = new ShopItem(R.drawable.shop_yellow_skin,
                    FIBRE_OPTIC_SKIN,
                    "",
                    1000);

            shopItems.add(fibreOpticSkin);
        }

        if (! AppData.hasBoughtShopItem(this, ILLUMINATI_SKIN))
        {
            ShopItem illuminatiSkin = new ShopItem(R.drawable.shop_pyramid_skin,
                    ILLUMINATI_SKIN,
                    "",
                    1000);

            shopItems.add(illuminatiSkin);
        }

        if (! AppData.hasBoughtShopItem(this, MORE_POWER_UPS_1))
        {
            ShopItem morePowerUps1 = new ShopItem(R.drawable.shop_more_power_ups_1,
                    MORE_POWER_UPS_1,
                    "Why be a peasant, start with two power ups for free.",
                    250);

            shopItems.add(morePowerUps1);
        }

        if (! AppData.hasBoughtShopItem(this, MORE_POWER_UPS_2))
        {
            ShopItem morePowerUps2 = new ShopItem(R.drawable.shop_more_power_ups_2,
                    MORE_POWER_UPS_2,
                    "You get three free power ups and can store a maximum of four.",
                    500);

            shopItems.add(morePowerUps2);
        }

        if (! AppData.hasBoughtShopItem(this, MORE_POWER_UPS_3))
        {
            ShopItem morePowerUps3 = new ShopItem(R.drawable.shop_more_power_ups_3,
                    MORE_POWER_UPS_3,
                    "You can store five power ups and you start with five, OP.",
                    1000);

            shopItems.add(morePowerUps3);
        }

        if (! AppData.hasBoughtShopItem(this, COIN_POWER_UP))
        {
            ShopItem coinPowerUp = new ShopItem(R.drawable.shop_coins_power_up,
                    COIN_POWER_UP,
                    "Make it rain when you use this power up!",
                    500);

            shopItems.add(coinPowerUp);
        }

        if (! AppData.hasBoughtShopItem(this, CLOCK_POWER_UP))
        {
            ShopItem clockPowerUp = new ShopItem(R.drawable.shop_clock_power_up,
                    CLOCK_POWER_UP,
                    "You lose 100 to 250 HP, but then you gain twice that amount.",
                    1000);

            shopItems.add(clockPowerUp);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        AnyShopItem anyShopItem = shopItems.get(position);

        if (anyShopItem instanceof ShopItem)
        {
            ShopItem shopItem = (ShopItem) anyShopItem;

            int coins = AppData.getCoinAmount();
            int cost = shopItem.cost;

            if (cost > coins)
            {
                startFailAnimation(view);
            }
            else
            {
                startConsiderAnimation(view);

                showBuyDialog(shopItem);
            }
        }
    }

    private void showBuyDialog(final ShopItem shopItem)
    {
        final String title = shopItem.title;

        new AlertDialog.Builder(this)
                .setMessage("I would like to buy " + title + ".")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        shopItems.remove(shopItem);
                        ((ShopTitleItem) shopItems.get(0)).coins -= shopItem.cost;

                        AppData.boughtShopItem(ShopActivity.this, title);

                        AppData.changeCoinAmount(- shopItem.cost);

                        shopAdapter.notifyDataSetChanged();

                        if (shopItem.title.contains("Skin"))
                        {
                            Intent intent = new Intent(BOUGHT_SKIN);
                            sendBroadcast(intent);
                        }
                    }
                })
                .create()
                .show();
    }

    private void startAnimation(View view, int colour)
    {
        ImageView imageView = ((ShopAdapter.ViewHolder) view.getTag()).shopItemFail;

        imageView.setBackgroundColor(colour);
        imageView.setAlpha(1f);

        AlphaAnimation animation = new AlphaAnimation(0, 0.3f);
        animation.setDuration(300);
        animation.setFillAfter(true);

        animation.setInterpolator(new Interpolator()
        {
            @Override
            public float getInterpolation(float input)
            {
                if (input < 0.5)
                {
                    return 2 * input;
                }
                else
                {
                    return (float) (1 - ((input - 0.5) / 0.5));
                }
            }
        });

        imageView.startAnimation(animation);
    }

    private void startFailAnimation(View view)
    {
        startAnimation(view, Color.RED);
    }

    private void startConsiderAnimation(View view)
    {
        startAnimation(view, Color.GREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
