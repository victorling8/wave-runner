package waverunner.example.victorling8.waverunner.GameAndTutorial;

import java.lang.reflect.Field;
import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.ModesScreen.Mode;
import waverunner.example.victorling8.waverunner.ModesScreen.ModeFactoryValues;
import waverunner.example.victorling8.waverunner.ShopScreen.ShopActivity;

public class Factory {
    private int level = 0;

    public static final int LEVEL_TIME = 750;

    private boolean isRealGame = true;
    private boolean isTutorial = false;

    private boolean createdBlockAlready = false;
    private boolean createdTurretAlready = false;
    private boolean createdMissileWarningAlready = false;
    private boolean createdLaserTurretAlready = false;
    private boolean createdPathShooterAlready = false;
    private boolean createdBossAlready = false;
    private boolean createdCoinAlready = false;
    private boolean createdPowerUpAlready = false;
    private boolean createdAllAroundShooterAlready = false;

    private int levelCounter = LEVEL_TIME;

    private int[] blocksPerTutorialLevel = new int[]
            {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
    private int[] turretsPerTutorialLevel = new int[]
            {0, 6, 3, 0, 0, 2, 0, 0, 2, 0, 3, 4};
    private int[] missileWarningsPerTutorialLevel = new int[]
            {0, 0, 8, 4, 0, 0, 2, 0, 0, 2, 3, 5};
    private int[] laserTurretsPerTutorialLevel = new int[]
            {0, 0, 0, 3, 2, 1, 0, 1, 0, 0, 1, 1};
    private int[] pathShootersPerTutorialLevel = new int[]
            {0, 0, 0, 0, 0, 0, 0, 3, 2, 0, 1, 1};
    private int[] bossesPerTutorialLevel = new int[]
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
    private int[] allAroundShootersPerTutorialLevel = new int[]
            {0, 0, 0, 0, 0, 4, 3, 0, 1, 0, 2, 0};

    private int[] coinsPerTutorialLevel = new int[]
            {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    private int[] powerUpsPerTutorialLevel = new int[]
            {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

    private int[] blocksPerLevel;
    private int[] turretsPerLevel;
    private int[] missileWarningsPerLevel;
    private int[] laserTurretsPerLevel;
    private int[] bossesPerLevel;
    private int[] pathShootersPerLevel;
    private int[] allAroundShootersPerLevel;

    private int[] coinsPerLevel;
    private int[] powerUpsPerLevel;

    private ArrayList<Integer> createBlockPoints = new ArrayList<>();
    private ArrayList<Integer> createTurretPoints = new ArrayList<>();
    private ArrayList<Integer> createMissileWarningPoints = new ArrayList<>();
    private ArrayList<Integer> createLaserTurretPoints = new ArrayList<>();
    private ArrayList<Integer> createBossPoints = new ArrayList<>();
    private ArrayList<Integer> createCoinPoints = new ArrayList<>();
    private ArrayList<Integer> createPowerUpPoints = new ArrayList<>();
    private ArrayList<Integer> createPathShooterPoints = new ArrayList<>();
    private ArrayList<Integer> createAllAroundShooterPoints = new ArrayList<>();

    private GameSurfaceView game;

    private ArrayList<Block> blocks;
    private ArrayList<Coin> coins;
    private ArrayList<Turret> turrets;
    private ArrayList<LaserTurret> laserTurrets;
    private ArrayList<MissileWarning> missileWarnings;
    private ArrayList<Boss> bosses;
    private ArrayList<ColourCircle> colourCircles;
    private ArrayList<PathShooter> pathShooters;
    private ArrayList<AllAroundShooter> allAroundShooters;
    private ArrayList<TutorialTipRequest> tipRequests;

    private ArrayList<PowerUp> powerUpsOnScreen;

    private int turretX;
    private int turretYBottom;
    private int turretYTop;

    public int width;
    public int height;

    public int floorHeight;

    private int minNormalBlockHeight;
    private int maxNormalBlockHeight;
    private int minMiddleBlockHeight;
    private int maxMiddleBlockHeight;

    private int blockWidth;

    private double normalBlockProbability;

    private boolean coinPowerUpActivated;
    private boolean clockPowerUpActivated;

    private int flyingCoinDuration = 10;
    private int flyingCoinCounter = flyingCoinDuration;
    private double flyingCoinProbability = 0;

    private int minFlyingCoinReachTargetDuration = 20;
    private int maxFlyingCoinReachTargetDuration = 40;

    public static Factory getInstance(GameSurfaceView game)
    {
        Factory factory = new Factory(game);

        return factory;
    }

    private Factory(GameSurfaceView game)
    {
        this.game = game;

        reinitializeLists(game);

        initializeDimensions();

        normalBlockProbability = (double) 2 / 3;

        initializeModeValues(Mode.getSearchString(AppData.getMode()));

        updateCreatePoints();

        lookAtSharedPreferences();
    }

    private void initializeModeValues(String searchString)
    {
        Field fields[] = ModeFactoryValues.class.getFields();

        for (Field field : fields)
        {
            try
            {
                String name = field.getName();

                if (name.contains(searchString))
                {
                    int[] value = (int[]) field.get(null);

                    if (name.contains("blocks"))
                    {
                        blocksPerLevel = value;
                    }
                    else if (name.contains("turrets"))
                    {
                        turretsPerLevel = value;
                    }
                    else if (name.contains("missileWarnings"))
                    {
                        missileWarningsPerLevel = value;
                    }
                    else if (name.contains("laserTurrets"))
                    {
                        laserTurretsPerLevel = value;
                    }
                    else if (name.contains("bosses"))
                    {
                        bossesPerLevel = value;
                    }
                    else if (name.contains("pathShooters"))
                    {
                        pathShootersPerLevel = value;
                    }
                    else if (name.contains("allAroundShooters"))
                    {
                        allAroundShootersPerLevel = value;
                    }
                    else if (name.contains("coins"))
                    {
                        coinsPerLevel = value;
                    }
                    else if (name.contains("powerUps"))
                    {
                        powerUpsPerLevel = value;
                    }
                }
            }
            catch (Exception ex)
            {

            }
        }
    }

    private void lookAtSharedPreferences()
    {
        coinPowerUpActivated = AppData.hasBoughtShopItem(game.getContext(), ShopActivity.COIN_POWER_UP);
        clockPowerUpActivated = AppData.hasBoughtShopItem(game.getContext(), ShopActivity.CLOCK_POWER_UP);
    }

    public void setTutorial()
    {
        isRealGame = false;
        isTutorial = true;
    }

    public void initializeDimensions()
    {
        width = Dimensions.width;
        height = Dimensions.height;

        floorHeight = Dimensions.floorHeight;

        minNormalBlockHeight = height / 3;
        maxNormalBlockHeight = 2 * height / 3;
        minMiddleBlockHeight = height / 3;
        maxMiddleBlockHeight = 3 * height / 5;

        blockWidth = width / 10;

        turretX = width - (int) ScreenUtil.convertDpToPixel(40, game.getContext());
        turretYBottom = height + (int) ScreenUtil.convertDpToPixel(30, game.getContext());
        turretYTop = (int) ScreenUtil.convertDpToPixel(-30, game.getContext());
    }

    private void reinitializeLists(GameSurfaceView game)
    {
        blocks = game.getBlocks();
        coins = game.getCoins();
        turrets = game.getTurrets();
        laserTurrets = game.getLaserTurrets();
        missileWarnings = game.getMissileWarnings();
        bosses = game.getBosses();
        colourCircles = game.getColourCircles();
        pathShooters = game.getPathShooters();
        allAroundShooters = game.getAllAroundShooters();
        tipRequests = game.getTipRequests();

        powerUpsOnScreen = game.getPowerUpsOnScreen();
    }

    public void setFlyingCoinProbability(double probability)
    {
        flyingCoinProbability = probability;
    }

    private void updateCreatePoints()
    {
        createBlockPoints.clear();
        createTurretPoints.clear();
        createMissileWarningPoints.clear();
        createLaserTurretPoints.clear();
        createBossPoints.clear();
        createCoinPoints.clear();
        createPowerUpPoints.clear();
        createPathShooterPoints.clear();
        createAllAroundShooterPoints.clear();

        if (isTutorial && level >= blocksPerTutorialLevel.length)
        {
            game.notifyTutorialShouldFinish();
            return;
        }

        int numberOfBlocks;
        int numberOfTurrets;
        int numberOfMissileWarnings;
        int numberOfLaserTurrets;
        int numberOfBosses;
        int numberOfCoins;
        int numberOfPowerUps;
        int numberOfPathShooters;
        int numberOfAllAroundShooters;

        if (isRealGame)
        {
            int index;

            if (level >= blocksPerLevel.length)
            {
                index = getRandomHardLevelIndex();
            }
            else
            {
                index = level;
            }

            numberOfBlocks = blocksPerLevel[index];
            numberOfTurrets = turretsPerLevel[index];
            numberOfMissileWarnings = missileWarningsPerLevel[index];
            numberOfLaserTurrets = laserTurretsPerLevel[index];
            numberOfBosses = bossesPerLevel[index];
            numberOfCoins = coinsPerLevel[index];
            numberOfPowerUps = (Math.random() < getPowerUpProbability()) ? powerUpsPerLevel[index] : 0;
            numberOfPathShooters = pathShootersPerLevel[index];
            numberOfAllAroundShooters = allAroundShootersPerLevel[index];
        }
        else
        {
            numberOfBlocks = blocksPerTutorialLevel[level];
            numberOfTurrets = turretsPerTutorialLevel[level];
            numberOfMissileWarnings = missileWarningsPerTutorialLevel[level];
            numberOfLaserTurrets = laserTurretsPerTutorialLevel[level];
            numberOfBosses = bossesPerTutorialLevel[level];
            numberOfCoins = coinsPerTutorialLevel[level];
            numberOfPowerUps = powerUpsPerTutorialLevel[level];
            numberOfPathShooters = pathShootersPerTutorialLevel[level];
            numberOfAllAroundShooters = allAroundShootersPerTutorialLevel[level];
        }

        for (int i = 0; i < numberOfBlocks; i++)
        {
            createBlockPoints.add((int) (((double) i / numberOfBlocks) * levelCounter));
        }

        for (int i = 0; i < numberOfTurrets; i++)
        {
            createTurretPoints.add((int) (((double) (i + 1) / (numberOfTurrets + 1)) * levelCounter));
        }

        for (int i = 0; i < numberOfMissileWarnings; i++)
        {
            createMissileWarningPoints.add((int) (((double) (i + 1) / (numberOfMissileWarnings + 1)) * levelCounter));
        }

        for (int i = 0; i < numberOfLaserTurrets; i++)
        {
            createLaserTurretPoints.add((int) (((double) (i + 1) / (numberOfLaserTurrets + 1)) * levelCounter));
        }

        for (int i = 0; i < numberOfBosses; i++)
        {
            createBossPoints.add((int) (((double) (i + 1) / (numberOfBosses + 1)) * levelCounter));
        }

        for (int i = 0; i < numberOfPathShooters; i++)
        {
            createPathShooterPoints.add((int) (((double) (i + 1) / (numberOfPathShooters + 1)) * levelCounter));
        }

        for (int i = 0; i < numberOfAllAroundShooters; i++)
        {
            createAllAroundShooterPoints.add((int) (((double) (i + 1) / (numberOfAllAroundShooters + 1)) * levelCounter));
        }

        for (int i = 0; i < numberOfCoins; i++)
        {
            int start = (int) Math.round(((double) i / numberOfCoins) * levelCounter) + 1;
            int end = (int) Math.round(((double) (i + 1) / numberOfCoins) * levelCounter);

            createCoinPoints.add(getRandomInt(start, end));
        }

        for (int i = 0; i < numberOfPowerUps; i++)
        {
            int start = (int) Math.round(((double) i / numberOfPowerUps) * levelCounter) + 1;
            int end = (int) Math.round(((double) (i + 1) / numberOfPowerUps) * levelCounter);

            createPowerUpPoints.add(getRandomInt(start, end));
        }
    }

    private int getRandomHardLevelIndex()
    {
        int max = blocksPerLevel.length - 1;

        return getRandomInt(max - 5, max);
    }

    private double getPowerUpProbability()
    {
        if (level < blocksPerLevel.length) return 1;

        return 1 - (level - blocksPerLevel.length) * 0.05;
    }

    public static int getRandomInt(int min, int max)
    {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private void incrementLevelIfNecessary()
    {
        levelCounter--;

        if (levelCounter == 0)
        {
            level++;
            levelCounter = getLevelTime();

            updateCreatePoints();
        }
    }

    private int getLevelTime()
    {
        return LEVEL_TIME;
    }

    public void updateEnemies()
    {
        incrementLevelIfNecessary();

        produceTurretIfNecessary();
        produceBlockIfNecessary();
        produceCoinIfNecessary();
        produceLaserTurretIfNecessary();
        producePowerUpIfNecessary();
        produceMissileWarningIfNecessary();
        produceBossIfNecessary();
        producePathShooterIfNecessary();
        produceAllAroundShooterIfNecessary();

        produceFlyingCoinIfNecessary();
    }

    private void produceTurretIfNecessary()
    {
        if (createTurretPoints.contains(levelCounter))
        {
            Turret turret;

            if (Math.random() < 0.5)
            {
                turret = new Turret(turretX, turretYBottom, true);
                turrets.add(turret);
            }
            else
            {
                turret = new Turret(turretX, turretYTop, false);
                turrets.add(turret);
            }

            if (isTutorial && ! createdTurretAlready)
            {
                createdTurretAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_TURRET,
                            turret,
                            GameSurfaceView.TUTORIAL_TURRET_WAIT));
                }
            }
        }
    }

    private void produceBlockIfNecessary()
    {
        if (createBlockPoints.contains(levelCounter))
        {
            Block block;

            if (Math.random() < normalBlockProbability)
            {
                block = produceNormalBlock();
                blocks.add(block);
            }
            else
            {
                block = produceMiddleBlock();
                blocks.add(block);
            }

            if (isTutorial && ! createdBlockAlready)
            {
                createdBlockAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_BLOCK,
                            block,
                            GameSurfaceView.TUTORIAL_BLOCK_WAIT));
                }
            }
        }
    }

    private Block produceNormalBlock()
    {
        int blockHeight = getRandomInt(minNormalBlockHeight, maxNormalBlockHeight);
        int top;

        if (Math.random() < 0.5)
        {
            top = floorHeight;
        }
        else
        {
            top = height - blockHeight - floorHeight;
        }

        return new Block(width, top, width + blockWidth, top + blockHeight);
    }

    private Block produceMiddleBlock()
    {
        int blockHeight = getRandomInt(minMiddleBlockHeight, maxMiddleBlockHeight);

        int bottom = height - floorHeight;

        int blockTop = getRandomInt(floorHeight, bottom - blockHeight);

        return new Block(width, blockTop, width + blockWidth, blockTop + blockHeight);
    }

    private void produceCoinIfNecessary()
    {
        if (createCoinPoints.contains(levelCounter))
        {
            Coin coin = new Coin(width, (int) (Math.random() * height));

            coins.add(coin);

            if (isTutorial && ! createdCoinAlready)
            {
                createdCoinAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_COIN,
                            coin,
                            GameSurfaceView.TUTORIAL_COIN_WAIT));
                }
            }
        }
    }

    private void produceLaserTurretIfNecessary()
    {
        if (createLaserTurretPoints.contains(levelCounter))
        {
            LaserTurret laserTurret;

            if (Math.random() < 0.5)
            {
                laserTurret = new LaserTurret(width - Dimensions.laserTurretWidth / 2,
                    - Dimensions.laserTurretHeight / 2,
                    false);
                laserTurrets.add(laserTurret);
            }
            else
            {
                laserTurret = new LaserTurret(width - Dimensions.laserTurretWidth / 2,
                        height + Dimensions.laserTurretHeight / 2,
                        true);
                laserTurrets.add(laserTurret);
            }

            if (isTutorial && ! createdLaserTurretAlready)
            {
                createdLaserTurretAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_LASER_TURRET,
                            laserTurret,
                            GameSurfaceView.TUTORIAL_LASER_TURRET_WAIT));
                }
            }
        }
    }

    public PowerUp getRandomPowerUp()
    {
        PowerUp powerUp;
        int total = 3;

        if (coinPowerUpActivated)
        {
            total++;
        }

        outer:
        {
            if (clockPowerUpActivated)
            {
                if (Math.random() < 0.15)
                {
                    powerUp = new ClockPowerUp(width + Dimensions.powerUpWidthOnScreen / 2,
                            getRandomInt(floorHeight, Dimensions.height - floorHeight - Dimensions.powerUpWidthOnScreen));

                    break outer;
                }
            }

            if (coinPowerUpActivated)
            {
                if (Math.random() < ((double) 1 / total))
                {
                    powerUp = new CoinPowerUp(width + Dimensions.powerUpWidthOnScreen / 2,
                            getRandomInt(floorHeight, Dimensions.height - floorHeight - Dimensions.powerUpWidthOnScreen));

                    break outer;
                }
            }

            double random = Math.random();

            if (random < 0.33)
            {
                powerUp = new HealthPowerUp(width + Dimensions.powerUpWidthOnScreen / 2,
                        getRandomInt(floorHeight, Dimensions.height - floorHeight - Dimensions.powerUpWidthOnScreen));
            }
            else if (random < 0.66)
            {
                powerUp = new ShieldPowerUp(width + Dimensions.powerUpWidthOnScreen / 2,
                        getRandomInt(floorHeight, Dimensions.height - floorHeight - Dimensions.powerUpWidthOnScreen));
            }
            else
            {
                powerUp = new TouchPowerUp(width + Dimensions.powerUpWidthOnScreen / 2,
                        getRandomInt(floorHeight, Dimensions.height - floorHeight - Dimensions.powerUpWidthOnScreen));
            }
        }

        return powerUp;
    }

    private void producePowerUpIfNecessary()
    {
        if (createPowerUpPoints.contains(levelCounter))
        {
            PowerUp powerUp = getRandomPowerUp();

            powerUpsOnScreen.add(powerUp);

            if (isTutorial && ! createdPowerUpAlready)
            {
                createdPowerUpAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_POWER_UP,
                            powerUp,
                            GameSurfaceView.TUTORIAL_POWER_UP_WAIT));
                }
            }
        }
    }

    private void produceMissileWarningIfNecessary()
    {
        if (createMissileWarningPoints.contains(levelCounter))
        {
            MissileWarning missileWarning = new MissileWarning((int) (Dimensions.width - 1.2 * Dimensions.missileWarningWidth / 2),
                    getRandomInt(floorHeight + Dimensions.missileWarningWidth / 2, Dimensions.height - floorHeight - Dimensions.missileWarningWidth / 2));

            missileWarnings.add(missileWarning);

            if (isTutorial && ! createdMissileWarningAlready)
            {
                createdMissileWarningAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_MISSILE_WARNING,
                            missileWarning,
                            GameSurfaceView.TUTORIAL_MISSILE_WARNING_WAIT));
                }
            }
        }
    }

    private void produceBossIfNecessary()
    {
        if (createBossPoints.contains(levelCounter))
        {
            int x = Dimensions.width + Dimensions.bossBaseWidth;
            int y = getRandomInt(Dimensions.height / 3, 2 * Dimensions.height / 3);

            ColourCircle colourCircle = new ColourCircle(x, y);

            Boss boss = new Boss(x, y, colourCircle);

            bosses.add(boss);
            colourCircles.add(colourCircle);

            if (isTutorial && ! createdBossAlready)
            {
                createdBossAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_BOSS,
                            boss,
                            GameSurfaceView.TUTORIAL_BOSS_WAIT));
                }
            }
        }
    }

    private void producePathShooterIfNecessary()
    {
        if (createPathShooterPoints.contains(levelCounter))
        {
            int x = Dimensions.width + Dimensions.turretBaseWidth / 2;
            int y = (Math.random() < 0.5) ? Dimensions.turretBaseHeight / 2 : Dimensions.height - Dimensions.turretBaseHeight / 2;

            PathShooter pathShooter = new PathShooter(x, y);

            pathShooters.add(pathShooter);

            if (isTutorial && ! createdPathShooterAlready)
            {
                createdPathShooterAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_PATH_SHOOTER,
                            pathShooter,
                            GameSurfaceView.TUTORIAL_PATH_SHOOTER_WAIT));
                }
            }
        }
    }

    private void produceAllAroundShooterIfNecessary()
    {
        if (createAllAroundShooterPoints.contains(levelCounter))
        {
            boolean goingDown = Math.random() < 0.5;

            int x = getRandomInt((int) (((double) 4 / 5) * Dimensions.width), (int) (((double) 3 / 5) * Dimensions.width));
            int y = goingDown ? - Dimensions.allAroundShooterBaseWidth / 2 : Dimensions.height + Dimensions.allAroundShooterBaseWidth / 2;

            AllAroundShooter allAroundShooter = new AllAroundShooter(x, y, ! goingDown);

            allAroundShooters.add(allAroundShooter);

            if (isTutorial && ! createdAllAroundShooterAlready)
            {
                createdAllAroundShooterAlready = true;

                synchronized(tipRequests)
                {
                    tipRequests.add(new TutorialTipRequest(GameSurfaceView.TUTORIAL_ALL_AROUND_SHOOTER,
                            allAroundShooter,
                            GameSurfaceView.TUTORIAL_ALL_AROUND_SHOOTER_WAIT));
                }
            }
        }
    }

    private void produceFlyingCoinIfNecessary()
    {
        flyingCoinCounter--;

        if (flyingCoinCounter == 0)
        {
            flyingCoinCounter = flyingCoinDuration;

            if (Math.random() < flyingCoinProbability)
            {
                flyingCoinCounter = flyingCoinDuration;

                int x = Dimensions.width;
                int y = getRandomFlyingCoinY();

                int targetY = getRandomFlyingCoinTargetY();

                double gravity = getRandomFlyingCoinGravity();

                double speedX = getRandomFlyingCoinSpeedX();
                double speedY = getFlyingCoinSpeedY(- speedX, gravity, y, targetY);

                coins.add(new FlyingCoin(x, y, speedX, speedY, gravity));
            }
        }
    }

    public int getRandomFlyingCoinY()
    {
        return getRandomInt(Dimensions.height / 3, Dimensions.height);
    }

    public int getRandomFlyingCoinTargetY()
    {
        return getRandomInt(0, Dimensions.height);
    }

    public double getRandomFlyingCoinGravity()
    {
        return (2 +  2 * Math.random()) * Dimensions.gravity;
    }

    public double getRandomFlyingCoinSpeedX()
    {
        int duration = getRandomInt(minFlyingCoinReachTargetDuration, maxFlyingCoinReachTargetDuration);

        int distance = 5 * Dimensions.width / 6;

        return - (double) distance / duration;
    }

    public double getFlyingCoinSpeedY(double speedX, double gravity, int y, int targetY)
    {
        int distance = 5 * Dimensions.width / 6;

        double time = distance / speedX;

        int distanceY = targetY - y;

        double averageSpeedY = - distanceY / time;

        return averageSpeedY + (gravity * time) / 2;
    }

    public RaceBlock produceRaceBlock(int collisionCounter)
    {
        int left = Dimensions.width / 6 + collisionCounter * Dimensions.normalMoveLeftSpeed;

        return new RaceBlock(left, 0, left + Dimensions.raceBlockWidth, Dimensions.height);
    }
}
