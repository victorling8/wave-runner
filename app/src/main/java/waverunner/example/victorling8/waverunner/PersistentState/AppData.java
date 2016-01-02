package waverunner.example.victorling8.waverunner.PersistentState;

import android.content.Context;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.ScoresScreen.HighScore;
import waverunner.example.victorling8.waverunner.ModesScreen.Mode;
import waverunner.example.victorling8.waverunner.ModesScreen.ModesFragment;

public class AppData
{
    private static Context context;

    private static final String FIRST_OPEN = "first open";
    private static final String COINS = "coins";
    private static final String SKIN = "skin";
    private static final String COLOUR = "colour";
    private static final String IS_SKIN = "is_skin";

    private static final String COINS_COLLECTED = "coins collected";
    private static final String HEALTH_POWER_UPS_COLLECTED = "health power ups collected";
    private static final String SHIELD_POWER_UPS_COLLECTED = "shield power ups collected";
    private static final String TOUCH_POWER_UPS_COLLECTED = "touch power ups collected";
    private static final String COIN_POWER_UPS_COLLECTED = "coin power ups collected";
    private static final String CLOCK_POWER_UPS_COLLECTED = "clock power ups collected";

    private static final String BLOCK = "block";
    private static final String TURRET = "turret";
    private static final String MISSILE = "missile";
    private static final String LASER = "laser";
    private static final String PATH_SHOOTER = "path shooter";
    private static final String ALL_AROUND_SHOOTER = "all around shooter";
    private static final String BOSS = "boss";

    public static final String MODE = "mode";

    public static final int NO_SKIN_ID = -40;

    public static final int MAX_HIGH_SCORES_SAVED = 10;

    public static void setContext(Context otherContext)
    {
        context = otherContext;
    }

    public static boolean isFirstOpen()
    {
        return SharedPreferencesUtil.getBoolean(context, FIRST_OPEN, true);
    }

    public static void saveFirstOpen()
    {
        SharedPreferencesUtil.putBoolean(context, FIRST_OPEN, false);
    }

    public static int getCoinAmount()
    {
        return SharedPreferencesUtil.getInt(context, COINS, 0);
    }

    public static void saveCoinAmount(int amount)
    {
        SharedPreferencesUtil.putInt(context, COINS, amount);
    }

    public static void boughtShopItem(Context context, String key)
    {
        SharedPreferencesUtil.boughtShopItem(context, key);
    }

    public static void changeCoinAmount(int amount)
    {
        int coins = getCoinAmount();

        coins += amount;

        saveCoinAmount(coins);
    }

    public static boolean hasBoughtShopItem(Context context, String title)
    {
        return SharedPreferencesUtil.hasBoughtShopItem(context, title);
    }

    public static int getSkinId()
    {
        return SharedPreferencesUtil.getInt(context, SKIN, NO_SKIN_ID);
    }

    public static void saveSkinId(int id)
    {
        SharedPreferencesUtil.putInt(context, SKIN, id);
    }

    public static int getColour()
    {
        return SharedPreferencesUtil.getInt(context, COLOUR, -1);
    }

    public static void saveColour(int colour)
    {
        SharedPreferencesUtil.putInt(context, COLOUR, colour);
    }

    public static boolean getIsSkin()
    {
        return SharedPreferencesUtil.getBoolean(context, IS_SKIN, false);
    }

    public static void saveIsSkin(boolean state)
    {
        SharedPreferencesUtil.putBoolean(context, IS_SKIN, state);
    }

    public static HighScore getHighScore(String mode, int index)
    {
        return HighScore.parseStringToHighScore(SharedPreferencesUtil.getString(context, mode + "high_score_" + index, null));
    }

    public static void saveHighScore(int distance, int coins, boolean skinEnabled, int skinOrColourValue)
    {
        String highscores[] = new String[MAX_HIGH_SCORES_SAVED];

        String string;

        if (skinEnabled)
        {
            string = "skin";
        }
        else
        {
            string = "colour";
        }

        String mode = Mode.getSearchString(AppData.getMode()) + "_";

        String saveValue = distance + " " + coins + " " + string + " " + skinOrColourValue;

        for (int i = 0; i < MAX_HIGH_SCORES_SAVED; i++)
        {
            highscores[i] = SharedPreferencesUtil.getString(context, mode + "high_score_" + i, null);

            if (highscores[i] == null)
            {
                SharedPreferencesUtil.putString(context, mode + "high_score_" + i, saveValue);

                return;
            }
            else
            {
                int highscoreDistance = Integer.parseInt(highscores[i].substring(0, highscores[i].indexOf(" ")));

                if (distance >= highscoreDistance)
                {
                    putHighScoreAtIndex(saveValue, i);

                    return;
                }
            }
        }
    }

    private static void putHighScoreAtIndex(String highScore, int index)
    {
        String mode = Mode.getSearchString(AppData.getMode()) + "_";

        for (int i = MAX_HIGH_SCORES_SAVED - 1; i >= index + 1; i--)
        {
            String betterHighScore = SharedPreferencesUtil.getString(context, mode + "high_score_" + (i - 1), null);

            if (betterHighScore != null)
            {
                SharedPreferencesUtil.putString(context, mode + "high_score_" + i, betterHighScore);
            }
        }

        SharedPreferencesUtil.putString(context, mode + "high_score_" + index, highScore);
    }

    public static void eraseSkinSaved()
    {
        SharedPreferencesUtil.putInt(context, SKIN, NO_SKIN_ID);
    }

    public static int getMode()
    {
        return SharedPreferencesUtil.getInt(context, MODE, ModesFragment.NORMAL_MODE);
    }

    public static void saveMode(int mode)
    {
        SharedPreferencesUtil.putInt(context, MODE, mode);
    }

    public static void updateItemsCollected(int coins, int health, int shield, int touch, int coinPowerUp, int clock)
    {
        int storeCoins = SharedPreferencesUtil.getInt(context, COINS_COLLECTED, 0) + coins;
        int storeHealth = SharedPreferencesUtil.getInt(context, HEALTH_POWER_UPS_COLLECTED, 0) + health;
        int storeShield = SharedPreferencesUtil.getInt(context, SHIELD_POWER_UPS_COLLECTED, 0) + shield;
        int storeTouch = SharedPreferencesUtil.getInt(context, TOUCH_POWER_UPS_COLLECTED, 0) + touch;
        int storeCoinPowerUp = SharedPreferencesUtil.getInt(context, COIN_POWER_UPS_COLLECTED, 0) + coinPowerUp;
        int storeClock = SharedPreferencesUtil.getInt(context, CLOCK_POWER_UPS_COLLECTED, 0) + clock;

        SharedPreferencesUtil.putInt(context, COINS_COLLECTED, storeCoins);
        SharedPreferencesUtil.putInt(context, HEALTH_POWER_UPS_COLLECTED, storeHealth);
        SharedPreferencesUtil.putInt(context, SHIELD_POWER_UPS_COLLECTED, storeShield);
        SharedPreferencesUtil.putInt(context, TOUCH_POWER_UPS_COLLECTED, storeTouch);
        SharedPreferencesUtil.putInt(context, COIN_POWER_UPS_COLLECTED, storeCoinPowerUp);
        SharedPreferencesUtil.putInt(context, CLOCK_POWER_UPS_COLLECTED, storeClock);
    }

    public static ItemSummary getItemSummary()
    {
        int coins = SharedPreferencesUtil.getInt(context, COINS_COLLECTED, 0);
        int health = SharedPreferencesUtil.getInt(context, HEALTH_POWER_UPS_COLLECTED, 0);
        int shield = SharedPreferencesUtil.getInt(context, SHIELD_POWER_UPS_COLLECTED, 0);
        int touch = SharedPreferencesUtil.getInt(context, TOUCH_POWER_UPS_COLLECTED, 0);
        int coinPowerUp = SharedPreferencesUtil.getInt(context, COIN_POWER_UPS_COLLECTED, 0);
        int clock = SharedPreferencesUtil.getInt(context, CLOCK_POWER_UPS_COLLECTED, 0);

        return new ItemSummary(coins, health, shield, touch, coinPowerUp, clock);

    }

    public static void updateEnemyDamages(int block, int turret, int missile, int laser, int pathShooter, int allAroundShooter, int boss)
    {
        int storeBlock = SharedPreferencesUtil.getInt(context, BLOCK, 0) + block;
        int storeTurret = SharedPreferencesUtil.getInt(context, TURRET, 0) + turret;
        int storeMissile = SharedPreferencesUtil.getInt(context, MISSILE, 0) + missile;
        int storeLaser = SharedPreferencesUtil.getInt(context, LASER, 0) + laser;
        int storePathShooter = SharedPreferencesUtil.getInt(context, PATH_SHOOTER, 0) + pathShooter;
        int storeAllAroundShooter = SharedPreferencesUtil.getInt(context, ALL_AROUND_SHOOTER, 0) + allAroundShooter;
        int storeBoss = SharedPreferencesUtil.getInt(context, BOSS, 0) + boss;

        SharedPreferencesUtil.putInt(context, BLOCK, storeBlock);
        SharedPreferencesUtil.putInt(context, TURRET, storeTurret);
        SharedPreferencesUtil.putInt(context, MISSILE, storeMissile);
        SharedPreferencesUtil.putInt(context, LASER, storeLaser);
        SharedPreferencesUtil.putInt(context, PATH_SHOOTER, storePathShooter);
        SharedPreferencesUtil.putInt(context, ALL_AROUND_SHOOTER, storeAllAroundShooter);
        SharedPreferencesUtil.putInt(context, BOSS, storeBoss);
    }

    public static EnemyDamageSummary getEnemyDamageSummary()
    {
        int block = SharedPreferencesUtil.getInt(context, BLOCK, 0);
        int turret = SharedPreferencesUtil.getInt(context, TURRET, 0);
        int missile = SharedPreferencesUtil.getInt(context, MISSILE, 0);
        int laser = SharedPreferencesUtil.getInt(context, LASER, 0);
        int pathShooter = SharedPreferencesUtil.getInt(context, PATH_SHOOTER, 0);
        int allAroundShooter = SharedPreferencesUtil.getInt(context, ALL_AROUND_SHOOTER, 0);
        int boss = SharedPreferencesUtil.getInt(context, BOSS, 0);

        return new EnemyDamageSummary(block, turret, missile, laser, pathShooter, allAroundShooter, boss);
    }

    public static class EnemyDamageSummary
    {
        public int block;
        public int turret;
        public int missile;
        public int laser;
        public int pathShooter;
        public int allAroundShooter;
        public int boss;

        public ArrayList<Integer> allDamages;

        public EnemyDamageSummary(int block, int turret, int missile, int laser, int pathShooter, int allAroundShooter, int boss)
        {
            this.block = block;
            this.turret = turret;
            this.missile = missile;
            this.laser = laser;
            this.pathShooter = pathShooter;
            this.allAroundShooter = allAroundShooter;
            this.boss = boss;

            allDamages = new ArrayList<>();

            allDamages.add(block);
            allDamages.add(turret);
            allDamages.add(missile);
            allDamages.add(laser);
            allDamages.add(pathShooter);
            allDamages.add(allAroundShooter);
            allDamages.add(boss);
        }
    }

    public static class ItemSummary
    {
        public int coin;
        public int health;
        public int shield;
        public int touch;
        public int coinPowerUp;
        public int clock;

        public ArrayList<Integer> allItems;

        public ItemSummary(int coin, int health, int shield, int touch, int coinPowerUp, int clock)
        {
            this.coin = coin;
            this.health = health;
            this.shield = shield;
            this.touch = touch;
            this.coinPowerUp = coinPowerUp;
            this.clock = clock;

            allItems = new ArrayList<>();

            allItems.add(coin);
            allItems.add(health);
            allItems.add(shield);
            allItems.add(touch);
            allItems.add(coinPowerUp);
            allItems.add(clock);
        }
    }
}
