package waverunner.example.victorling8.waverunner.MainScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.GameAndTutorial.GameActivity;
import waverunner.example.victorling8.waverunner.GameAndTutorial.PowerUp;
import waverunner.example.victorling8.waverunner.ScoresScreen.HighScoreActivity;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.ModesScreen.SettingsActivity;
import waverunner.example.victorling8.waverunner.ShopScreen.ShopActivity;
import waverunner.example.victorling8.waverunner.GameAndTutorial.TutorialActivity;

public class MainActivity extends AppCompatActivity
        implements MainScreenButtonFragment.MainScreenButtonListener, ColourFragment.SkinListener
{
    private MainScreenButtonFragment buttonFragment;
    private ColourFragment colourFragment;

    boolean receivedInformationFromFragment = false;

    boolean isSkinFromFragment;
    int colourFromFragment;
    int skinIdFromFragment;

    boolean boughtSkinFromShop = false;

    BroadcastReceiver skinReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            boughtSkinFromShop = true;
        }
    };

    private static final int SHOP_ACTIVITY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ShopActivity.BOUGHT_SKIN);

        registerReceiver(skinReceiver, filter);

        AppData.setContext(getApplicationContext());

        if (AppData.isFirstOpen())
        {
            AppData.saveFirstOpen();
            AppData.saveCoinAmount(0);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (savedInstanceState == null)
        {
            buttonFragment = new MainScreenButtonFragment();
            colourFragment = new ColourFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_screen_fragment_container, colourFragment, "colourFragment")
                    .detach(colourFragment)
                    .add(R.id.main_screen_fragment_container, buttonFragment, "buttonFragment")
                    .attach(buttonFragment)
                    .commit();
        }
        else
        {
            buttonFragment = (MainScreenButtonFragment) getSupportFragmentManager().findFragmentByTag("buttonFragment");
            colourFragment = (ColourFragment) getSupportFragmentManager().findFragmentByTag("colourFragment");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (boughtSkinFromShop)
        {
            boughtSkinFromShop = false;

            if (colourFragment != null)
            {
                colourFragment.notifyShouldUpdate();
            }
        }
    }

    private PowerUp.PowerUpInfo getPowerUpInfo()
    {
        if (AppData.hasBoughtShopItem(this, ShopActivity.MORE_POWER_UPS_3))
        {
            return new PowerUp.PowerUpInfo(5, 5);
        }
        else if (AppData.hasBoughtShopItem(this, ShopActivity.MORE_POWER_UPS_2))
        {
            return new PowerUp.PowerUpInfo(4, 3);
        }
        else if (AppData.hasBoughtShopItem(this, ShopActivity.MORE_POWER_UPS_1))
        {
            return new PowerUp.PowerUpInfo(3, 2);
        }
        else
        {
            return new PowerUp.PowerUpInfo(3, 0);
        }
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

    @Override
    public void onMainButtonClick(int id)
    {
        switch(id)
        {
            case R.id.new_game_button:

                if (! skinFromShared())
                {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .attach(colourFragment)
                            .detach(buttonFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
                else
                {
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra("Skin", AppData.getSkinId());
                    intent.putExtra("Colour", AppData.getColour());
                    intent.putExtra("isSkin", AppData.getIsSkin());

                    PowerUp.PowerUpInfo info = getPowerUpInfo();

                    intent.putExtra("maxPowerUps", info.maxNumber);
                    intent.putExtra("startingPowerUps", info.startingNumber);

                    startActivity(intent);
                }
                break;

            case R.id.instructions_button:

                int skin;
                int colour;
                boolean isSkin;

                if (skinFromShared())
                {
                    skin = AppData.getSkinId();
                    colour = AppData.getColour();
                    isSkin = AppData.getIsSkin();
                }
                else
                {
                    if (receivedInformationFromFragment)
                    {
                        skin = skinIdFromFragment;
                        colour = colourFromFragment;
                        isSkin = isSkinFromFragment;
                    }
                    else
                    {
                        skin = 0;
                        colour = 0xff33ccff;
                        isSkin = false;
                    }
                }

                {
                    Intent intent = new Intent(this, TutorialActivity.class);
                    intent.putExtra("Skin", skin);
                    intent.putExtra("Colour", colour);
                    intent.putExtra("isSkin", isSkin);

                    PowerUp.PowerUpInfo info = getPowerUpInfo();

                    intent.putExtra("maxPowerUps", info.maxNumber);
                    intent.putExtra("startingPowerUps", info.startingNumber);

                    startActivity(intent);
                }

                break;

            case R.id.settings_button:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            case R.id.shop_button:
                Intent intent = new Intent(this, ShopActivity.class);
                startActivityForResult(intent, SHOP_ACTIVITY);
                break;

            case R.id.stats_button:
                Intent highScoreIntent = new Intent(this, HighScoreActivity.class);
                startActivity(highScoreIntent);
                break;
        }
    }

    private boolean skinFromShared()
    {
        return AppData.getSkinId() != AppData.NO_SKIN_ID;
    }

    @Override
    public void onSkinPressed(Skin skin)
    {
        receivedInformationFromFragment = true;

        skinIdFromFragment = skin.id;
        colourFromFragment = skin.colour;
        isSkinFromFragment = skin.isSkin;
    }

    @Override
    public void onReady(Skin skin, boolean useAsDefault)
    {
        if (useAsDefault)
        {
            AppData.saveSkinId(skin.id);
            AppData.saveColour(skin.colour);
            AppData.saveIsSkin(skin.isSkin);

            getSupportFragmentManager().popBackStack();
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("Skin", skin.id);
        intent.putExtra("Colour", skin.colour);
        intent.putExtra("isSkin", skin.isSkin);

        PowerUp.PowerUpInfo info = getPowerUpInfo();

        intent.putExtra("maxPowerUps", info.maxNumber);
        intent.putExtra("startingPowerUps", info.startingNumber);

        startActivity(intent);
    }
}
