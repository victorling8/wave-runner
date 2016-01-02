package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.MainScreen.ColourAdapter;
import waverunner.example.victorling8.waverunner.R;

public class GameSurfaceView extends SurfaceView
{
    private Activity activity;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private MyThread beforeGameThread;
    private MyThread gameThread;

    GameOverDialogFragment gameOverDialogFragment;

    private boolean skinEnabled = false;
    private int skinDrawableId;
    private Bitmap skinBitmap;
    private int skinBitmapWidth;

    private boolean isRealGame = true;
    private boolean isTutorial = false;

    private ArrayList<TutorialTipRequest> tipRequests = new ArrayList<>();
    private ArrayList<ClockAnimationRequest> clockAnimationRequests = new ArrayList<>();

    public static final Object clockLock = new Object();

    private ArrayList<ClockManager> clockManagers = new ArrayList<>();

    private ImageView tipImageView;

    private boolean tutorialShouldFinish = false;

    private int tutorialOverDuration = 100;
    private int tutorialOverCounter = tutorialOverDuration;

    private boolean tutorialTipShowing = false;

    private boolean showedPlayerTipAlready = false;
    private boolean showedColourCircleTipAlready = false;

    private boolean canTouchToDismissTip = false;

    public static final int TUTORIAL_MOVING_LEFT_WAIT = 75;

    public static final int TUTORIAL_PLAYER_WAIT = 0;
    public static final int TUTORIAL_BLOCK_WAIT = TUTORIAL_MOVING_LEFT_WAIT;
    public static final int TUTORIAL_TURRET_WAIT = 75;
    public static final int TUTORIAL_MISSILE_WARNING_WAIT = 20;
    public static final int TUTORIAL_LASER_TURRET_WAIT = 100;
    public static final int TUTORIAL_PATH_SHOOTER_WAIT = 200;
    public static final int TUTORIAL_BOSS_WAIT = 100;
    public static final int TUTORIAL_COIN_WAIT = TUTORIAL_MOVING_LEFT_WAIT;
    public static final int TUTORIAL_POWER_UP_WAIT = TUTORIAL_MOVING_LEFT_WAIT;
    public static final int TUTORIAL_COLOUR_CIRCLE_WAIT = 20;
    public static final int TUTORIAL_ALL_AROUND_SHOOTER_WAIT = 75;

    public static final int TUTORIAL_PLAYER = 0;
    public static final int TUTORIAL_BLOCK = 1;
    public static final int TUTORIAL_TURRET = 2;
    public static final int TUTORIAL_MISSILE_WARNING = 3;
    public static final int TUTORIAL_LASER_TURRET = 4;
    public static final int TUTORIAL_PATH_SHOOTER = 5;
    public static final int TUTORIAL_BOSS = 6;
    public static final int TUTORIAL_COIN = 7;
    public static final int TUTORIAL_POWER_UP = 8;
    public static final int TUTORIAL_COLOUR_CIRCLE = 9;
    public static final int TUTORIAL_ALL_AROUND_SHOOTER = 10;

    private boolean tutorialPlayerHappened = false;

    private Factory factory;

    private ImageView coinImage;
    private TextView coinTextView;
    private TextView distanceTextView;
    private View powerUpSection;
    private RelativeLayout container;
    private ImageView healthImage;
    private TextView healthTextView;
    private TextView countDownTextView;
    private ImageView flyingCoinFadeForeground;

    private boolean gameHasBegun = false;

    private RaceBlock raceBlock;
    private int raceBlockDuration = 200;
    private int raceBlockCounter;

    private boolean foundViews = false;

    private double playerX;
    private double playerY;

    private double playerSpeed;

    private int numberOfPowerUps = 0;
    private int startingNumberOfPowerUps;
    private int maxNumberOfPowerUps;

    private ArrayList<PowerUp> startingPowerUps = new ArrayList<>();

    private boolean shielded = false;
    private int shieldRadius;

    private int playerSmallPulse;
    private int playerLargePulse;

    private int playerMinSmallPulse;
    private int playerMaxSmallPulse;
    private int playerMinLargePulse;
    private int playerMaxLargePulse;

    private int distance = 0;
    private int score = 0;
    private int health = 500;

    private int oneCoin = 1;

    private boolean acceleratingUp = false;

    private int groundTimeUp;
    private int groundCounter;
    private int groundDamage;

    private ArrayList<Block> blocks;
    private ArrayList<Coin> coins;
    private ArrayList<Gravel> gravel;
    private ArrayList<Bullet> bullets;
    private ArrayList<Remnant> remnants;
    private ArrayList<Turret> turrets;
    private ArrayList<LaserTurret> laserTurrets;
    private ArrayList<MissileWarning> missileWarnings;
    private ArrayList<Missile> missiles;
    private ArrayList<Explosion> explosions;
    private ArrayList<Boss> bosses;
    private ArrayList<ColourCircle> colourCircles;
    private ArrayList<PathShooter> pathShooters;
    private ArrayList<Path> paths;
    private ArrayList<PathBullet> pathBullets;
    private ArrayList<AllAroundShooter> allAroundShooters;

    private ArrayList<PowerUp> powerUpsOnScreen;
    private ArrayList<PowerUp> powerUpsAtBottom;

    private ArrayList<Integer> clockXCoordinates;

    private ValueAnimator initialPlayerAnimator;
    private ValueAnimator playerSmallPulseAnimator;
    private ValueAnimator playerLargePulseAnimator;
    private ValueAnimator coinFloatingAnimator;
    private ValueAnimator powerUpFloatingAnimator;
    private ValueAnimator laserLoadingAnimator;
    private ValueAnimator shieldRadiusAnimator;
    private ValueAnimator touchLineAnimator;
    private ValueAnimator flyingCoinForegroundAnimator;

    private Bitmap currentLaserLoading;

    private int relativeCoinFloat;

    private Paint transparentPaint = new Paint();
    private Paint canvasPaint = new Paint();
    private Paint playerPaint = new Paint();
    private Paint playerSmallPulsePaint = new Paint();
    private Paint playerLargePulsePaint = new Paint();
    private Paint coinPaint = new Paint();
    private Paint coinSmallPulsePaint = new Paint();
    private Paint coinLargePulsePaint = new Paint();
    private Paint groundPaint = new Paint();
    private Paint blockPaint = new Paint();
    private Paint remnantPaint = new Paint();
    private Paint gravelPaint = new Paint();
    private Paint turretPaint = new Paint();
    private Paint turretSmallPulsePaint = new Paint();
    private Paint turretLargePulsePaint = new Paint();
    private Paint bulletPaint = new Paint();

    private Rect keepRectangle;
    private Rect smallKeepRectangle;

    private int shouldBeEfficient;
    private int efficiencyCounter;

    private int colour;
    private int healthRegainFromPowerUp = 50;

    private int shieldDuration = 500;
    private int shieldCounter;

    private int shieldMinRadius;
    private int shieldMaxRadius;

    private boolean shieldGettingSmaller;

    private int slightLaserOffset;

    private int touchDuration = 500;
    private int touchCounter;

    private float touchLineAlpha;

    private boolean firstTouch = false;

    private boolean touchActivated;
    private boolean touchGettingWeaker;

    private int coinsDuration = 500;
    private int coinsCounter;

    private boolean coinsActivated;
    private boolean coinsGettingWeaker;

    private boolean freshTouch = false;

    private int touchY;

    private int relativePowerUpFloat;

    private int numberDisplayedCurrently = 4;

    private float flyingCoinFadeForegroundAlpha = 0;

    private float maxFlyingCoinForegroundAlpha = 0.2f;

    int blockDamage = 0;
    int turretDamage = 0;
    int missileDamage = 0;
    int laserDamage = 0;
    int pathShooterDamage = 0;
    int allAroundShooterDamage = 0;
    int bossDamage = 0;

    int healthPowerUpsUsed = 0;
    int shieldPowersUpsUsed = 0;
    int touchPowerUpsUsed = 0;
    int coinPowerUpsUsed = 0;
    int clockPowerUpsUsed = 0;

    public GameSurfaceView(Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
        this.context = context;
        init();
    }

    public void setTutorial()
    {
        isRealGame = false;
        isTutorial = true;

        factory.setTutorial();
    }

    public Thread getGameThread()
    {
        return gameThread;
    }

    public void setShouldDraw(boolean state)
    {
        ((BeforeGameRunnable) beforeGameThread.getRunnable()).setShouldDraw(state);
        ((GameRunnable) gameThread.getRunnable()).setShouldDraw(state);
    }

    public void setSkinId(int id, Context context)
    {
        skinEnabled = true;
        skinDrawableId = id;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);

        skinBitmapWidth = Math.round(ScreenUtil.convertDpToPixel(20, context));

        skinBitmap = Bitmap.createScaledBitmap(bitmap, skinBitmapWidth, skinBitmapWidth, false);
    }

    public void setColour(int colour)
    {
        this.colour = colour;

        initializePaints();
        Paints.initializePaints();
    }

    private void initializePaints()
    {
        transparentPaint.setColor(0);
        canvasPaint.setColor(0xff252525);
        playerPaint.setColor(ColourAdapter.getColour(colour));
        playerSmallPulsePaint.setColor(ColourAdapter.getMostlyOpaque(colour));
        playerLargePulsePaint.setColor(ColourAdapter.getMostlyTransparent(colour));
        coinPaint.setColor(Color.YELLOW);
        coinSmallPulsePaint.setColor(ColourAdapter.getMostlyOpaque(Color.YELLOW));
        coinLargePulsePaint.setColor(ColourAdapter.getMostlyTransparent(Color.YELLOW));
        groundPaint.setColor(Color.GRAY);
        blockPaint.setColor(Color.GRAY);
        remnantPaint.setColor(Color.GRAY);
        gravelPaint.setColor(Color.GRAY);
        turretPaint.setColor(0xffcc0099);
        turretSmallPulsePaint.setColor(ColourAdapter.getMostlyOpaque(0xcc0099));
        turretLargePulsePaint.setColor(ColourAdapter.getMostlyTransparent(0xcc0099));
        bulletPaint.setColor(0xffcc3399);
    }

    public void stopThreads()
    {
        boolean retry = true;

        ((BeforeGameRunnable) beforeGameThread.getRunnable()).setRunning(false);

        if (beforeGameThread.isAlive()) {
            while (retry) {
                try {
                    beforeGameThread.join();
                    retry = false;
                } catch (Exception ex) {

                }
            }
        }

        retry = true;

        GameRunnable runnable = (GameRunnable) gameThread.getRunnable();

        runnable.setRunning(false);

        if (gameThread.isAlive()) {
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (Exception ex) {

                }
            }
        }

        stopAnimators();
    }

    public void canFindViews(Activity activity)
    {
        this.activity = activity;

        coinImage = (ImageView) activity.findViewById(R.id.bottom_bar_coin_image);
        coinTextView = (TextView) activity.findViewById(R.id.bottom_bar_coin_text_view);
        distanceTextView = (TextView) activity.findViewById(R.id.bottom_bar_distance_text_view);
        powerUpSection = activity.findViewById(R.id.bottom_bar_power_up_section);
        container = (RelativeLayout) activity.findViewById(R.id.game_container);
        healthImage = (ImageView) activity.findViewById(R.id.bottom_bar_health_image);
        healthTextView = (TextView) activity.findViewById(R.id.bottom_bar_health_text_view);
        countDownTextView = (TextView) activity.findViewById(R.id.game_count_down);
        flyingCoinFadeForeground = (ImageView) activity.findViewById(R.id.flying_coin_fade_foreground);

        ((RelativeLayout.LayoutParams) powerUpSection.getLayoutParams()).width = maxNumberOfPowerUps * Dimensions.powerUpWidthBottomBar;

        foundViews = true;
    }

    public void passPowerUpInfo(PowerUp.PowerUpInfo info)
    {
        maxNumberOfPowerUps = info.maxNumber;
        startingNumberOfPowerUps = info.startingNumber;

        fillStartingPowerUps();
    }

    private void fillStartingPowerUps()
    {
        startingPowerUps.clear();

        for (int i = 0; i < startingNumberOfPowerUps; i++)
        {
            startingPowerUps.add(factory.getRandomPowerUp());
        }
    }

    public boolean haveFoundViews()
    {
        return foundViews;
    }

    public void initializeBottomBar()
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                coinImage.setImageBitmap(Pictures.coin);
                healthImage.setImageBitmap(Pictures.healthIcon);

                ((GameRunnable) gameThread.getRunnable()).setBottomBarInitialized(true);
            }
        });
    }

    private void initVariables()
    {
        blocks = new ArrayList<>();
        coins = new ArrayList<>();
        gravel = new ArrayList<>();
        bullets = new ArrayList<>();
        remnants = new ArrayList<>();
        turrets = new ArrayList<>();
        laserTurrets = new ArrayList<>();
        missileWarnings = new ArrayList<>();
        missiles = new ArrayList<>();
        explosions = new ArrayList<>();
        bosses = new ArrayList<>();
        colourCircles = new ArrayList<>();
        pathShooters = new ArrayList<>();
        paths = new ArrayList<>();
        pathBullets = new ArrayList<>();
        allAroundShooters = new ArrayList<>();

        powerUpsOnScreen = new ArrayList<>();
        powerUpsAtBottom = new ArrayList<>();

        clockManagers = new ArrayList<>();
        clockAnimationRequests = new ArrayList<>();

        clockXCoordinates = new ArrayList<>();

        factory = Factory.getInstance(this);

        playerSpeed = 0;

        Dimensions.playerRadius = Math.round(ScreenUtil.convertDpToPixel(10, context));

        playerMinSmallPulse = Math.round(ScreenUtil.convertDpToPixel(10, context));
        playerMaxSmallPulse = Math.round(ScreenUtil.convertDpToPixel(13, context));
        playerMinLargePulse = Math.round(ScreenUtil.convertDpToPixel(10, context));
        playerMaxLargePulse = Math.round(ScreenUtil.convertDpToPixel(16, context));

        shieldMinRadius = Math.round(ScreenUtil.convertDpToPixel(5, context));
        shieldMaxRadius = Math.round(ScreenUtil.convertDpToPixel(75, context));

        slightLaserOffset = Math.round(ScreenUtil.convertDpToPixel(5, context));

        groundTimeUp = 2;
        groundCounter = groundTimeUp;
        groundDamage = 3;

        shouldBeEfficient = 5;
        efficiencyCounter = shouldBeEfficient;
    }

    public void init()
    {
        beforeGameThread = new MyThread(new BeforeGameRunnable(this));
        gameThread = new MyThread(new GameRunnable(this));

        ((BeforeGameRunnable) beforeGameThread.getRunnable()).setThread(beforeGameThread);
        ((GameRunnable) gameThread.getRunnable()).setThread(gameThread);

        initVariables();
        initAnimators();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = event.getAction();

                switch (id) {
                    case MotionEvent.ACTION_DOWN:
                        firstTouch = true;
                        acceleratingUp = true;
                        freshTouch = true;

                        if (isRealGame) break;

                        if (! showedPlayerTipAlready)
                        {
                            showedPlayerTipAlready = true;

                            synchronized(tipRequests)
                            {
                                tipRequests.add(new TutorialTipRequest(TUTORIAL_PLAYER,
                                        null,
                                        TUTORIAL_PLAYER_WAIT));
                            }
                        }

                        if (tutorialTipShowing && canTouchToDismissTip)
                        {
                            synchronized(tipRequests)
                            {
                                tutorialTipShowing = false;

                                setShouldDraw(true);

                                container.removeView(tipImageView);
                                tipImageView.setVisibility(View.INVISIBLE);

                                canTouchToDismissTip = false;

                                startAnimators();
                            }
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        acceleratingUp = false;
                        freshTouch = false;
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

        surfaceHolder = getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                ((BeforeGameRunnable) beforeGameThread.getRunnable()).setCanvasCreated(true);
                ((GameRunnable) gameThread.getRunnable()).setCanvasCreated(true);

                if (!gameHasBegun) {
                    gameHasBegun = true;

                    beforeGameThread.start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                ((BeforeGameRunnable) beforeGameThread.getRunnable()).setCanvasCreated(false);
                ((GameRunnable) gameThread.getRunnable()).setCanvasCreated(false);
            }
        });
    }

    public ArrayList<Block> getBlocks()
    {
        return blocks;
    }

    public ArrayList<Coin> getCoins()
    {
        return coins;
    }

    public ArrayList<Turret> getTurrets()
    {
        return turrets;
    }

    public ArrayList<LaserTurret> getLaserTurrets()
    {
        return laserTurrets;
    }

    public ArrayList<PowerUp> getPowerUpsOnScreen()
    {
        return powerUpsOnScreen;
    }

    public ArrayList<MissileWarning> getMissileWarnings()
    {
        return missileWarnings;
    }

    public ArrayList<Boss> getBosses()
    {
        return bosses;
    }

    public ArrayList<ColourCircle> getColourCircles()
    {
        return colourCircles;
    }

    public ArrayList<PathShooter> getPathShooters()
    {
        return pathShooters;
    }

    public ArrayList<AllAroundShooter> getAllAroundShooters()
    {
        return allAroundShooters;
    }

    public ArrayList<TutorialTipRequest> getTipRequests()
    {
        return tipRequests;
    }

    private void gameOver()
    {
        int value;

        if (skinEnabled)
        {
            value = skinDrawableId;
        }
        else
        {
            value = colour;
        }

        AppData.saveHighScore(distance, score, skinEnabled, value);

        AppData.updateEnemyDamages(blockDamage, turretDamage, missileDamage, laserDamage, pathShooterDamage, allAroundShooterDamage, bossDamage);

        AppData.updateItemsCollected(score, healthPowerUpsUsed, shieldPowersUpsUsed,
                touchPowerUpsUsed, coinPowerUpsUsed, clockPowerUpsUsed);

        ((GameRunnable) gameThread.getRunnable()).setRunning(false);
        showGameOverDialog();
    }

    private void showGameOverDialog()
    {
        if (gameOverDialogFragment != null)
        {
            gameOverDialogFragment.shouldDismiss();

            activity.getFragmentManager().beginTransaction().remove(gameOverDialogFragment).commit();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("score", distance);
        bundle.putInt("coins", score);

        AppData.changeCoinAmount(score);

        gameOverDialogFragment = new GameOverDialogFragment();
        gameOverDialogFragment.setArguments(bundle);

        gameOverDialogFragment.setCancelable(false);
        gameOverDialogFragment.show(activity.getFragmentManager(), "gameOverDialog");
    }

    public void restartGame()
    {
        playerY = Dimensions.height / 2;
        playerSpeed = 0;
        numberOfPowerUps = 0;
        shielded = false;
        distance = 0;
        score = 0;
        health = 500;
        acceleratingUp = false;
        touchActivated = false;
        shieldRadius = shieldMinRadius;
        touchLineAlpha = 0;
        freshTouch = false;
        firstTouch = false;
        numberDisplayedCurrently = 4;
        coinsActivated = false;

        blockDamage = 0;
        turretDamage = 0;
        missileDamage = 0;
        laserDamage = 0;
        pathShooterDamage = 0;
        allAroundShooterDamage = 0;
        bossDamage = 0;

        healthPowerUpsUsed = 0;
        shieldPowersUpsUsed = 0;
        touchPowerUpsUsed = 0;
        coinPowerUpsUsed = 0;
        clockPowerUpsUsed = 0;

        clockXCoordinates.clear();

        fillStartingPowerUps();

        stopAnimators();

        for (PowerUp powerUp : powerUpsAtBottom)
        {
            powerUp.removeView();
        }

        for (ClockManager clockManager : clockManagers)
        {
            clockManager.removeView();
        }

        init();

        beforeGameThread.start();
    }

    private void startAnimators()
    {
        initialPlayerAnimator.start();
        playerSmallPulseAnimator.start();
        playerLargePulseAnimator.start();
        coinFloatingAnimator.start();
        powerUpFloatingAnimator.start();
        laserLoadingAnimator.start();
    }

    private void stopAnimators()
    {
        try
        {
            initialPlayerAnimator.cancel();
            playerSmallPulseAnimator.cancel();
            playerLargePulseAnimator.cancel();
            coinFloatingAnimator.cancel();
            powerUpFloatingAnimator.cancel();
            laserLoadingAnimator.cancel();
            // shieldRadiusAnimator.cancel();
            // touchLineAnimator.cancel();
        }
        catch (NullPointerException ex)
        {

        }
    }

    public void initializeDimensions(Canvas canvas)
    {
        Dimensions.width = canvas.getWidth();
        Dimensions.height = canvas.getHeight();

        Dimensions.gravity = ScreenUtil.convertDpToPixel(0.08f, context);

        Dimensions.floorHeight = Math.round(ScreenUtil.convertDpToPixel(15, context));

        playerX = Dimensions.width / 6;
        playerY = Dimensions.height / 2;

        Dimensions.coinFloat = Math.round(ScreenUtil.convertDpToPixel(20, context));
        Dimensions.powerUpFloat = Math.round(ScreenUtil.convertDpToPixel(20, context));

        Dimensions.normalMoveLeftSpeed = Math.round(ScreenUtil.convertDpToPixel(4, context));
        Dimensions.turretSpeed = ScreenUtil.convertDpToPixel(1.4f, context);
        Dimensions.missileSpeed = 10 * Dimensions.turretSpeed;
        Dimensions.laserTurretSpeed = 0.45 * Dimensions.turretSpeed;
        Dimensions.pathShooterSpeed = 0.85 * Dimensions.turretSpeed;
        Dimensions.bossSpeed = 0.85 * Dimensions.turretSpeed;
        Dimensions.bossEscapeSpeed = 0.7 * Dimensions.turretSpeed;
        Dimensions.allAroundShooterSpeed = 0.9 * Dimensions.turretSpeed;

        Dimensions.bulletSpeed = 6 * Dimensions.turretSpeed;

        Dimensions.pathBulletRadius = Math.round(ScreenUtil.convertDpToPixel(8, context));
        Dimensions.minPathAmplitude = Math.round(ScreenUtil.convertDpToPixel(10, context));
        Dimensions.maxPathAmplitude = Math.round(ScreenUtil.convertDpToPixel(25, context));
        Dimensions.minPathPeriod = Math.round(ScreenUtil.convertDpToPixel(50, context));
        Dimensions.maxPathPeriod = Math.round(ScreenUtil.convertDpToPixel(100, context));
        Dimensions.pathBulletDistance = Math.round(ScreenUtil.convertDpToPixel(5, context));

        factory.initializeDimensions();

        smallKeepRectangle = new Rect(- Dimensions.width / 10, - Dimensions.height / 10, 11 * Dimensions.width / 10, 11 * Dimensions.height / 10);
        keepRectangle = new Rect(- Dimensions.width / 5, - Dimensions.height / 5, 6 * Dimensions.width / 5, 6 * Dimensions.height / 5);

        initializeImages();
    }

    public void initializeImages()
    {
        if (Pictures.missile1 != null) return;

        // missile1
        Pictures.missile1 = BitmapFactory.decodeResource(getResources(), R.drawable.missile1);
        Dimensions.missileHeight = Math.round(ScreenUtil.convertDpToPixel(25, context));
        Dimensions.missileWidth = (int) (Pictures.missile1.getWidth() * ((double) Dimensions.missileHeight / Pictures.missile1.getHeight()));
        Pictures.missile1 = Bitmap.createScaledBitmap(Pictures.missile1, Dimensions.missileWidth, Dimensions.missileHeight, false);

        // coin
        Pictures.coin = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
        Dimensions.coinRadius = Math.round(ScreenUtil.convertDpToPixel(22, context));
        Pictures.coin = Bitmap.createScaledBitmap(Pictures.coin, Dimensions.coinRadius, Dimensions.coinRadius, false);

        // turretBase
        Pictures.turretBase = BitmapFactory.decodeResource(getResources(), R.drawable.turret_base);
        Dimensions.turretBaseWidth = Math.round(ScreenUtil.convertDpToPixel(50, context));
        Dimensions.turretBaseHeight = (int) (Pictures.turretBase.getHeight() * ((double) Dimensions.turretBaseWidth / Pictures.turretBase.getWidth()));
        Pictures.turretBase = Bitmap.createScaledBitmap(Pictures.turretBase, Dimensions.turretBaseWidth, Dimensions.turretBaseHeight, false);

        // turretGun
        Pictures.turretGun = BitmapFactory.decodeResource(getResources(), R.drawable.turret_gun);
        Dimensions.turretGunWidth = Math.round(ScreenUtil.convertDpToPixel(40, context));
        Dimensions.turretGunHeight = (int) (Pictures.turretGun.getHeight() * ((double) Dimensions.turretGunWidth / Pictures.turretGun.getWidth()));
        Pictures.turretGun = Bitmap.createScaledBitmap(Pictures.turretGun, Dimensions.turretGunWidth, Dimensions.turretGunHeight, false);

        Pictures.turretBullet = BitmapFactory.decodeResource(getResources(), R.drawable.turret_bullet);
        Dimensions.bulletWidth = Math.round(ScreenUtil.convertDpToPixel(16, context));
        Pictures.turretBullet = Bitmap.createScaledBitmap(Pictures.turretBullet, Dimensions.bulletWidth, Dimensions.bulletWidth, false);

        // laser turret left and right side
        Pictures.laserTurretLeftSide = BitmapFactory.decodeResource(getResources(), R.drawable.laser_left_side);
        Dimensions.laserTurretWidth = Math.round(ScreenUtil.convertDpToPixel(50, context));
        Dimensions.laserTurretHeight = (int) (Pictures.laserTurretLeftSide.getWidth() * ((double) Dimensions.laserTurretWidth / Pictures.laserTurretLeftSide.getWidth()));
        Pictures.laserTurretLeftSide = Bitmap.createScaledBitmap(Pictures.laserTurretLeftSide, Dimensions.laserTurretWidth, Dimensions.laserTurretHeight, false);

        Pictures.laserTurretRightSide = BitmapFactory.decodeResource(getResources(), R.drawable.laser_right_side);
        Pictures.laserTurretRightSide = Bitmap.createScaledBitmap(Pictures.laserTurretRightSide, Dimensions.laserTurretWidth, Dimensions.laserTurretHeight, false);

        // laser loading images
        Pictures.laserLoading1 = BitmapFactory.decodeResource(getResources(), R.drawable.laser_loading_1);
        Dimensions.laserLoadingWidth = Math.round(ScreenUtil.convertDpToPixel(25, context));
        Dimensions.laserLoadingHeight = (int) (Pictures.laserLoading1.getHeight() * ((double) Dimensions.laserLoadingWidth / Pictures.laserLoading1.getWidth()));
        Pictures.laserLoading1 = Bitmap.createScaledBitmap(Pictures.laserLoading1, Dimensions.laserLoadingWidth, Dimensions.laserLoadingHeight, false);

        Pictures.laserLoading2 = BitmapFactory.decodeResource(getResources(), R.drawable.laser_loading_2);
        Pictures.laserLoading2 = Bitmap.createScaledBitmap(Pictures.laserLoading2, Dimensions.laserLoadingWidth, Dimensions.laserLoadingHeight, false);

        Pictures.laserLoading3 = BitmapFactory.decodeResource(getResources(), R.drawable.laser_loading_3);
        Pictures.laserLoading3 = Bitmap.createScaledBitmap(Pictures.laserLoading3, Dimensions.laserLoadingWidth, Dimensions.laserLoadingHeight, false);

        Pictures.laserOn = BitmapFactory.decodeResource(getResources(), R.drawable.laser_on);
        Dimensions.laserHeight = Dimensions.laserTurretHeight / 6;
        Pictures.laserOn = Bitmap.createScaledBitmap(Pictures.laserOn, Dimensions.width, Dimensions.laserHeight, false);

        Pictures.laserOff = BitmapFactory.decodeResource(getResources(), R.drawable.laser_off);
        Pictures.laserOff = Bitmap.createScaledBitmap(Pictures.laserOff, Dimensions.width, Dimensions.laserHeight, false);

        // health power up on screen and on bottom bar
        Pictures.healthPowerUpOnScreen = BitmapFactory.decodeResource(getResources(), R.drawable.health_power_up);
        Dimensions.powerUpWidthOnScreen = Math.round(ScreenUtil.convertDpToPixel(35, context));
        Pictures.healthPowerUpOnScreen = Bitmap.createScaledBitmap(Pictures.healthPowerUpOnScreen, Dimensions.powerUpWidthOnScreen, Dimensions.powerUpWidthOnScreen, false);

        Pictures.healthPowerUpBottomBar = BitmapFactory.decodeResource(getResources(), R.drawable.health_power_up);
        Dimensions.powerUpWidthBottomBar = Math.round(ScreenUtil.convertDpToPixel(50, context));
        Pictures.healthPowerUpBottomBar = Bitmap.createScaledBitmap(Pictures.healthPowerUpBottomBar, Dimensions.powerUpWidthBottomBar, Dimensions.powerUpWidthBottomBar, false);

        // health icon on bottom bar
        Pictures.healthIcon = BitmapFactory.decodeResource(getResources(), R.drawable.bottom_bar_health_icon);
        Dimensions.healthIconWidth = Math.round(ScreenUtil.convertDpToPixel(22, context));
        Pictures.healthIcon = Bitmap.createScaledBitmap(Pictures.healthIcon, Dimensions.healthIconWidth, Dimensions.healthIconWidth, false);

        // shield power up on screen and on bottom bar
        Pictures.shieldPowerUpOnScreen = BitmapFactory.decodeResource(getResources(), R.drawable.shield_power_up);
        Pictures.shieldPowerUpOnScreen = Bitmap.createScaledBitmap(Pictures.shieldPowerUpOnScreen, Dimensions.powerUpWidthOnScreen, Dimensions.powerUpWidthOnScreen, false);

        Pictures.shieldPowerUpBottomBar = BitmapFactory.decodeResource(getResources(), R.drawable.shield_power_up);
        Pictures.shieldPowerUpBottomBar = Bitmap.createScaledBitmap(Pictures.shieldPowerUpBottomBar, Dimensions.powerUpWidthBottomBar, Dimensions.powerUpWidthBottomBar, false);

        // shield for the player and the full radius one
        Pictures.shield = BitmapFactory.decodeResource(getResources(), R.drawable.shield);
        Pictures.shieldFullRadius = Bitmap.createScaledBitmap(Pictures.shield, 2 * shieldMaxRadius, 2 * shieldMaxRadius, false);

        // touch power up on screen and on bottom bar
        Pictures.touchPowerUpOnScreen = BitmapFactory.decodeResource(getResources(), R.drawable.touch_power_up);
        Pictures.touchPowerUpOnScreen = Bitmap.createScaledBitmap(Pictures.touchPowerUpOnScreen, Dimensions.powerUpWidthOnScreen, Dimensions.powerUpWidthOnScreen, false);

        Pictures.touchPowerUpBottomBar = BitmapFactory.decodeResource(getResources(), R.drawable.touch_power_up);
        Pictures.touchPowerUpBottomBar = Bitmap.createScaledBitmap(Pictures.touchPowerUpBottomBar, Dimensions.powerUpWidthBottomBar, Dimensions.powerUpWidthBottomBar, false);

        // touch line
        Pictures.touchLine = BitmapFactory.decodeResource(getResources(), R.drawable.dotted_line);
        Pictures.touchLine = Bitmap.createScaledBitmap(Pictures.touchLine, Dimensions.width, Pictures.touchLine.getHeight(), false);
        Dimensions.touchLineHeight = Pictures.touchLine.getHeight();

        // missile warning icon
        Pictures.missileWarning = BitmapFactory.decodeResource(getResources(), R.drawable.missile_warning);
        Dimensions.missileWarningWidth = Math.round(ScreenUtil.convertDpToPixel(45, context));
        Pictures.missileWarning = Bitmap.createScaledBitmap(Pictures.missileWarning, Dimensions.missileWarningWidth, Dimensions.missileWarningWidth, false);

        // explosion gif
        int ids[] = new int[] {R.drawable.explosion1,
        R.drawable.explosion2,
        R.drawable.explosion3,
        R.drawable.explosion4,
        R.drawable.explosion5,
        R.drawable.explosion6,
        R.drawable.explosion7,
        R.drawable.explosion8};

        Dimensions.explosionWidth = Math.round(ScreenUtil.convertDpToPixel(200, context));

        for (int i = 0; i < 8; i++)
        {
            Pictures.explosion[i] = BitmapFactory.decodeResource(getResources(), ids[i]);

            Dimensions.explosionHeight = (int) (Pictures.explosion[i].getHeight() * ((double) Dimensions.explosionWidth / Pictures.explosion[i].getWidth()));

            Pictures.explosion[i] = Bitmap.createScaledBitmap(Pictures.explosion[i], Dimensions.explosionWidth, Dimensions.explosionHeight, false);
        }

        // boss base
        Pictures.bossBase = BitmapFactory.decodeResource(getResources(), R.drawable.boss1_base);
        Dimensions.bossBaseWidth = Math.round(ScreenUtil.convertDpToPixel(70, context));
        Pictures.bossBase = Bitmap.createScaledBitmap(Pictures.bossBase, Dimensions
                .bossBaseWidth, Dimensions.bossBaseWidth, false);

        Dimensions.bossMoveDistance = 3 * Dimensions.bossBaseWidth;

        // colour circle
        Pictures.colourCircle = BitmapFactory.decodeResource(getResources(), R.drawable
                .boss1_colours);
        Dimensions.minColourCircleComebackRadius = (int) (((double) 4 / 5) * (Dimensions.bossBaseWidth / 2));
        Dimensions.maxColourCircleComebackRadius = (int) (((double) 7 / 5) * (Dimensions.bossBaseWidth / 2));
        Dimensions.minColourCircleAttackRadius = Math.round(ScreenUtil.convertDpToPixel(50,
                context));
        Dimensions.maxColourCircleAttackRadius = Math.round(ScreenUtil.convertDpToPixel(60,
                context));

        Pictures.bossBullet = BitmapFactory.decodeResource(getResources(), R.drawable.boss_bullet);
        Pictures.bossBullet = Bitmap.createScaledBitmap(Pictures.bossBullet, Dimensions
                .bulletWidth, Dimensions.bulletWidth, false);

        Pictures.pathShooterBase = BitmapFactory.decodeResource(getResources(), R.drawable
                .path_shooter_base);
        Pictures.pathShooterBase = Bitmap.createScaledBitmap(Pictures.pathShooterBase, Dimensions
                .turretBaseWidth, Dimensions.turretBaseHeight, false);

        // race block
        Dimensions.raceBlockHeight = Dimensions.height;
        Pictures.raceBlock = BitmapFactory.decodeResource(getResources(), R.drawable.race_block);
        Dimensions.raceBlockWidth = (int) (Pictures.raceBlock.getWidth() * ((double) Dimensions.raceBlockHeight / Pictures.raceBlock.getHeight()));
        Pictures.raceBlock = Bitmap.createScaledBitmap(Pictures.raceBlock, Dimensions
                .raceBlockWidth, Dimensions.raceBlockHeight, false);

        // tutorial assets
        Pictures.tutorialPlayer[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_player);
        Dimensions.tutorialPlayerWidth = Math.round(ScreenUtil.convertDpToPixel(225, context));
        Dimensions.tutorialPlayerHeight = (int) (Pictures.tutorialPlayer[0].getHeight() * ((double) Dimensions.tutorialPlayerWidth / Pictures.tutorialPlayer[0].getWidth()));
        Pictures.tutorialPlayer[0] = Bitmap.createScaledBitmap(Pictures.tutorialPlayer[0],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialBlock[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_block);
        Pictures.tutorialBlock[0] = Bitmap.createScaledBitmap(Pictures.tutorialBlock[0],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialTurret[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_turret);
        Pictures.tutorialTurret[0] = Bitmap.createScaledBitmap(Pictures.tutorialTurret[0],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialMissileWarning[0] = BitmapFactory.decodeResource(getResources(), R
                .drawable.tutorial_missile_warning);
        Pictures.tutorialMissileWarning[0] = Bitmap.createScaledBitmap(Pictures
                .tutorialMissileWarning[0], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialLaserTurret[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_laser_turret);
        Pictures.tutorialLaserTurret[0] = Bitmap.createScaledBitmap(Pictures
                .tutorialLaserTurret[0], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialBoss[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_boss);
        Pictures.tutorialBoss[0] = Bitmap.createScaledBitmap(Pictures.tutorialBoss[0], Dimensions
                .tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialCoin[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_coin);
        Pictures.tutorialCoin[0] = Bitmap.createScaledBitmap(Pictures.tutorialCoin[0], Dimensions
                .tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialPowerUp[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_power_up);
        Pictures.tutorialPowerUp[0] = Bitmap.createScaledBitmap(Pictures.tutorialPowerUp[0],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialCoin[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_coin);
        Pictures.tutorialCoin[0] = Bitmap.createScaledBitmap(Pictures.tutorialCoin[0], Dimensions
                .tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialPathShooter[0] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_path_shooter);
        Pictures.tutorialPathShooter[0] = Bitmap.createScaledBitmap(Pictures
                .tutorialPathShooter[0], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialColourCircle[0] = BitmapFactory.decodeResource(getResources(), R
                .drawable.tutorial_colour_circle);
        Pictures.tutorialColourCircle[0] = Bitmap.createScaledBitmap(Pictures
                .tutorialColourCircle[0], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialAllAroundShooter[0] = BitmapFactory.decodeResource(getResources(), R
                .drawable.tutorial_all_around_shooter);
        Pictures.tutorialAllAroundShooter[0] = Bitmap.createScaledBitmap(Pictures
                .tutorialAllAroundShooter[0], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);




        Pictures.tutorialPlayer[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_player_2);
        Pictures.tutorialPlayer[1] = Bitmap.createScaledBitmap(Pictures.tutorialPlayer[1],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialBlock[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_block_2);
        Pictures.tutorialBlock[1] = Bitmap.createScaledBitmap(Pictures.tutorialBlock[1],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);


        Pictures.tutorialTurret[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_turret_2);
        Pictures.tutorialTurret[1] = Bitmap.createScaledBitmap(Pictures.tutorialTurret[1],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialMissileWarning[1] = BitmapFactory.decodeResource(getResources(), R
                .drawable.tutorial_missile_warning_2);
        Pictures.tutorialMissileWarning[1] = Bitmap.createScaledBitmap(Pictures
                .tutorialMissileWarning[1], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialLaserTurret[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_laser_turret_2);
        Pictures.tutorialLaserTurret[1] = Bitmap.createScaledBitmap(Pictures.tutorialLaserTurret
                [1], Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialBoss[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_boss_2);
        Pictures.tutorialBoss[1] = Bitmap.createScaledBitmap(Pictures.tutorialBoss[1], Dimensions
                .tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialCoin[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_coin_2);
        Pictures.tutorialCoin[1] = Bitmap.createScaledBitmap(Pictures.tutorialCoin[1], Dimensions
                .tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialPowerUp[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_power_up_2);
        Pictures.tutorialPowerUp[1] = Bitmap.createScaledBitmap(Pictures.tutorialPowerUp[1],
                Dimensions.tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialCoin[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_coin_2);
        Pictures.tutorialCoin[1] = Bitmap.createScaledBitmap(Pictures.tutorialCoin[1], Dimensions
                .tutorialPlayerWidth, Dimensions.tutorialPlayerHeight, false);

        Pictures.tutorialPathShooter[1] = BitmapFactory.decodeResource(getResources(), R.drawable
                .tutorial_path_shooter_2);
        Pictures.tutorialPathShooter[1] = Bitmap.createScaledBitmap(Pictures
                .tutorialPathShooter[1], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialColourCircle[1] = BitmapFactory.decodeResource(getResources(), R
                .drawable.tutorial_colour_circle_2);
        Pictures.tutorialColourCircle[1] = Bitmap.createScaledBitmap(Pictures
                .tutorialColourCircle[1], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.tutorialAllAroundShooter[1] = BitmapFactory.decodeResource(getResources(), R
                .drawable.tutorial_all_around_shooter_2);
        Pictures.tutorialAllAroundShooter[1] = Bitmap.createScaledBitmap(Pictures
                .tutorialAllAroundShooter[1], Dimensions.tutorialPlayerWidth, Dimensions
                .tutorialPlayerHeight, false);

        Pictures.coinPowerUpOnScreen = BitmapFactory.decodeResource(getResources(), R.drawable
                .coin_power_up);
        Pictures.coinPowerUpOnScreen = Bitmap.createScaledBitmap(Pictures.coinPowerUpOnScreen,
                Dimensions.powerUpWidthOnScreen, Dimensions.powerUpWidthOnScreen, false);

        Pictures.coinPowerUpBottomBar = BitmapFactory.decodeResource(getResources(), R.drawable
                .coin_power_up);
        Pictures.coinPowerUpBottomBar = Bitmap.createScaledBitmap(Pictures.coinPowerUpBottomBar,
                Dimensions.powerUpWidthBottomBar, Dimensions.powerUpWidthBottomBar, false);

        Pictures.allAroundShooterBase = BitmapFactory.decodeResource(getResources(), R.drawable
                .all_around_shooter_base);
        Dimensions.allAroundShooterBaseWidth = Math.round(ScreenUtil.convertDpToPixel(50, context));
        Pictures.allAroundShooterBase = Bitmap.createScaledBitmap(Pictures.allAroundShooterBase,
                Dimensions.allAroundShooterBaseWidth, Dimensions.allAroundShooterBaseWidth, false);

        Pictures.allAroundShooterCircle = BitmapFactory.decodeResource(getResources(), R.drawable
                .all_around_shooter_circle);
        Dimensions.allAroundShooterCircleRadius = (int) (((double) 9 / 10) * Dimensions.allAroundShooterBaseWidth / 2);
        Pictures.allAroundShooterCircle = Bitmap.createScaledBitmap(Pictures
                .allAroundShooterCircle, 2 * Dimensions.allAroundShooterCircleRadius, 2 * Dimensions.allAroundShooterCircleRadius, false);

        Pictures.allAroundShooterSquare = BitmapFactory.decodeResource(getResources(), R.drawable
                .all_around_shooter_center);
        Dimensions.allAroundShooterSquareWidth = Dimensions.allAroundShooterBaseWidth / 3;
        Pictures.allAroundShooterSquare = Bitmap.createScaledBitmap(Pictures.allAroundShooterSquare, Dimensions.allAroundShooterSquareWidth, Dimensions.allAroundShooterSquareWidth, false);

        Pictures.allAroundShooterBullet = BitmapFactory.decodeResource(getResources(), R.drawable
                .all_around_shooter_bullet);
        Pictures.allAroundShooterBullet = Bitmap.createScaledBitmap(Pictures
                .allAroundShooterBullet, Dimensions.bulletWidth, Dimensions.bulletWidth, false);

        Pictures.clockPowerUpOnScreen = BitmapFactory.decodeResource(getResources(), R.drawable
                .clock_power_up);
        Pictures.clockPowerUpOnScreen = Bitmap.createScaledBitmap(Pictures.clockPowerUpOnScreen,
                Dimensions.powerUpWidthOnScreen, Dimensions.powerUpWidthOnScreen, false);

        Pictures.clockPowerUpBottomBar = BitmapFactory.decodeResource(getResources(), R.drawable
                .clock_power_up);
        Pictures.clockPowerUpBottomBar = Bitmap.createScaledBitmap(Pictures.clockPowerUpBottomBar,
                Dimensions.powerUpWidthBottomBar, Dimensions.powerUpWidthBottomBar, false);

        Pictures.redClock = BitmapFactory.decodeResource(getResources(), R.drawable.red_clock);
        Dimensions.clockWidth = Math.round(ScreenUtil.convertDpToPixel(50, context));
        Dimensions.clockHeight = (int) Math.round(Pictures.redClock.getHeight() * ((double) Dimensions.clockWidth / Pictures.redClock.getWidth()));
        Pictures.redClock = Bitmap.createScaledBitmap(Pictures.redClock, Dimensions.clockWidth,
                Dimensions.clockHeight, false);

        Pictures.greenClock = BitmapFactory.decodeResource(getResources(), R.drawable.green_clock);
        Pictures.greenClock = Bitmap.createScaledBitmap(Pictures.greenClock, Dimensions
                .clockWidth, Dimensions.clockHeight, false);

        Dimensions.startClockX = Math.round(ScreenUtil.convertDpToPixel(30, context));
        Dimensions.distanceBetweenClocks = Math.round(ScreenUtil.convertDpToPixel(60, context));
        Dimensions.clockYCoordinate = (int) (0.95 * Dimensions.clockHeight);
    }

    public void initializeRaceBar()
    {
        raceBlock = factory.produceRaceBlock(raceBlockDuration);

        raceBlockCounter = raceBlockDuration;
    }

    public void makeCounterVisible()
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                countDownTextView.setAlpha(1f);

                AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(500);
                fadeIn.setFillAfter(true);
                countDownTextView.startAnimation(fadeIn);
            }
        });
    }

    public void hideBottomBar()
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                coinImage.setVisibility(View.INVISIBLE);
                coinTextView.setVisibility(View.INVISIBLE);
                distanceTextView.setVisibility(View.INVISIBLE);
                healthImage.setVisibility(View.INVISIBLE);
                healthTextView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void showBottomBar()
    {
        distance = 0;

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                coinImage.setVisibility(View.VISIBLE);
                coinTextView.setVisibility(View.VISIBLE);
                distanceTextView.setVisibility(View.VISIBLE);
                healthImage.setVisibility(View.VISIBLE);
                healthTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void initAnimators()
    {
        TimeInterpolator trigInterpolator = new TimeInterpolator()
        {
            @Override
            public float getInterpolation(float input)
            {
                if (input <= 0.5)
                {
                    return (float) (-Math.cos(Math.PI * (2 * input)) / 2 + 0.5);
                }
                else
                {
                    return (float) (-Math.cos(Math.PI * (1 - ((input - 0.5) / 0.5))) / 2 + 0.5);
                }
            }
        };

        initialPlayerAnimator = ValueAnimator.ofInt(Dimensions.height / 2 - Dimensions.coinFloat, Dimensions.height / 2 + Dimensions.coinFloat);
        initialPlayerAnimator.setDuration(1500);
        initialPlayerAnimator.setRepeatCount(ValueAnimator.INFINITE);

        initialPlayerAnimator.setInterpolator(trigInterpolator);

        initialPlayerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                if (!firstTouch)
                {
                    playerY = (Integer) animation.getAnimatedValue();
                }
            }
        });

        initialPlayerAnimator.start();

        coinFloatingAnimator = ValueAnimator.ofInt(- Dimensions.coinFloat, Dimensions.coinFloat);
        coinFloatingAnimator.setDuration(2000);
        coinFloatingAnimator.setRepeatCount(ValueAnimator.INFINITE);

        coinFloatingAnimator.setInterpolator(trigInterpolator);

        coinFloatingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                relativeCoinFloat = (Integer) animation.getAnimatedValue();
            }
        });

        coinFloatingAnimator.start();

        powerUpFloatingAnimator = ValueAnimator.ofInt(- Dimensions.powerUpFloat, Dimensions.powerUpFloat);
        powerUpFloatingAnimator.setDuration(2000);
        powerUpFloatingAnimator.setRepeatCount(ValueAnimator.INFINITE);

        powerUpFloatingAnimator.setInterpolator(trigInterpolator);

        powerUpFloatingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                relativePowerUpFloat = (Integer) animation.getAnimatedValue();
            }
        });

        powerUpFloatingAnimator.start();

        playerSmallPulseAnimator = ValueAnimator.ofInt(playerMinSmallPulse, playerMaxSmallPulse);
        playerSmallPulseAnimator.setDuration(1000);
        playerSmallPulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        playerSmallPulseAnimator.setInterpolator(new TimeInterpolator()
        {
            @Override
            public float getInterpolation(float input)
            {
                if (input <= 0.5)
                {
                    return 2 * input;
                } else
                {
                    return (float) (1 - (input - 0.5) / 0.5);
                }
            }
        });

        playerSmallPulseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                playerSmallPulse = (Integer) animation.getAnimatedValue();
            }
        });

        playerSmallPulseAnimator.start();

        playerLargePulseAnimator = ValueAnimator.ofInt(playerMinLargePulse, playerMaxLargePulse);
        playerLargePulseAnimator.setDuration(1000);
        playerLargePulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        playerLargePulseAnimator.setInterpolator(new TimeInterpolator()
        {
            @Override
            public float getInterpolation(float input)
            {
                if (input <= 0.5)
                {
                    return 2 * input;
                } else
                {
                    return (float) (1 - (input - 0.5) / 0.5);
                }
            }
        });

        playerLargePulseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                playerLargePulse = (Integer) animation.getAnimatedValue();
            }
        });

        playerLargePulseAnimator.start();

        laserLoadingAnimator = ValueAnimator.ofFloat(0, 1);
        laserLoadingAnimator.setDuration(1000);
        laserLoadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        laserLoadingAnimator.setInterpolator(new LinearInterpolator());

        laserLoadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (Float) animation.getAnimatedValue();

                if (value < 0.33)
                {
                    currentLaserLoading = Pictures.laserLoading1;
                } else if (value < 0.66)
                {
                    currentLaserLoading = Pictures.laserLoading2;
                } else
                {
                    currentLaserLoading = Pictures.laserLoading3;
                }
            }
        });

        laserLoadingAnimator.start();
    }

    public void updateScreen(Canvas canvas)
    {
        clearCanvas(canvas);

        drawFloorAndCeiling(canvas);

        moveAndDrawTouchLineIfNecessary(canvas);
        moveAndDrawBlocks(canvas);
        moveAndDrawCoins(canvas);
        moveAndDrawPlayer(canvas);
        moveAndDrawGravel(canvas);
        moveAndDrawRemnants(canvas);
        moveAndDrawTurretsAndBullets(canvas);
        moveAndDrawLasers(canvas);
        moveAndDrawLaserTurrets(canvas);
        moveAndDrawMissileWarnings(canvas);
        moveAndDrawMissiles(canvas);
        moveAndDrawExplosions(canvas);
        moveAndDrawBosses(canvas);
        moveAndDrawColourCircles(canvas);
        moveAndDrawPathShooters(canvas);
        moveAndDrawPathsAndPathBullets(canvas);
        moveAndDrawAllAroundShooters(canvas);

        moveAndDrawPowerUps(canvas);

        moveAndDrawShieldIfNecessary(canvas);

        startMissileWarnings();
        checkMissileWarningsDone();

        checkExplosionsDone();

        checkCollisionsWithBlocks();
        checkCollisionsWithCoins();
        checkCollisionsWithGround();
        checkCollisionsWithBullets();
        checkCollisionsWithLasers();
        checkCollisionsWithMissiles();
        checkCollisionsWithPowerUps();
        checkCollisionsWithColourCircles();
        checkCollisionsWithPathsIfShielded();
        checkCollisionsWithPathBullets();

        clocksChangeHealth();
        startClockAnimation();

        removeShieldIfNecessary();
        removeTouchLineIfNecessary();
        removeCoinPowerUpIfNecessary();

        addStartingPowerUpIfNecessary();

        updateBottomBar();

        getEnemies();

        efficiency();

        showTips(canvas);

        checkGameOver();
        checkTutorialOver();
    }

    public void updateScreenBeforeGame(Canvas canvas)
    {
        clearCanvas(canvas);

        moveAndDrawPlayer(canvas);
        drawFloorAndCeiling(canvas);
        checkCollisionsWithGround();
        moveAndDrawRaceBlock(canvas);

        updateBeforeGameCounter();

        checkCollisionWithRaceBlock();
    }

    private void moveAndDrawRaceBlock(Canvas canvas)
    {
        raceBlock.move();
        raceBlock.draw(canvas);
    }

    private void clearCanvas(Canvas canvas)
    {
        canvas.drawRect(0, 0, Dimensions.width, Dimensions.height, canvasPaint);
    }

    private void updateBeforeGameCounter()
    {
        raceBlockCounter--;

        final int newNumber = (int) (((double) raceBlockCounter / raceBlockDuration) * 3) + 1;

        if (numberDisplayedCurrently != newNumber)
        {
            numberDisplayedCurrently = newNumber;

            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    countDownTextView.setText(Integer.toString(newNumber));
                }
            });
        }
    }

    private void checkCollisionWithRaceBlock()
    {
        if (playerX >= raceBlock.getRectangle().left)
        {
            becomeBlackAndWhiteRemnants(raceBlock, new Point((int) playerX, (int) playerY));

            raceBlock = null;

            ((BeforeGameRunnable) beforeGameThread.getRunnable()).setRunning(false);

            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    countDownTextView.setText("0");

                    AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setDuration(1000);
                    fadeOut.setFillAfter(true);
                    countDownTextView.startAnimation(fadeOut);
                }
            });
        }
    }

    private void drawFloorAndCeiling(Canvas canvas)
    {
        canvas.drawRect(0, 0, Dimensions.width, Dimensions.floorHeight, groundPaint);
        canvas.drawRect(0, Dimensions.height - Dimensions.floorHeight, Dimensions.width,
                Dimensions.height, groundPaint);
    }

    private void moveAndDrawPlayer(Canvas canvas)
    {
        if (firstTouch)
        {
            distance++;

            if (touchActivated)
            {
                if (freshTouch)
                {
                    adjustTouchY();
                    playerY = touchY;
                }
            }
            else
            {
                double acceleration;

                if (acceleratingUp)
                {
                    acceleration = - 2 * Dimensions.gravity;
                }
                else
                {
                    acceleration = Dimensions.gravity;
                }

                playerSpeed += acceleration;

                playerY += playerSpeed;
            }
        }

        RectF smallPulse = new RectF((float) (playerX - playerSmallPulse),
                (float) (playerY - playerSmallPulse),
                (float) (playerX + playerSmallPulse),
                (float) (playerY + playerSmallPulse));

        RectF largePulse = new RectF((float) (playerX - playerLargePulse),
                (float) (playerY - playerLargePulse),
                (float) (playerX + playerLargePulse),
                (float) (playerY + playerLargePulse));

        canvas.drawOval(largePulse, playerLargePulsePaint);
        canvas.drawOval(smallPulse, playerSmallPulsePaint);

        if (skinEnabled)
        {
            canvas.drawBitmap(skinBitmap,
                    (int) playerX - skinBitmapWidth / 2,
                    (int) playerY - skinBitmapWidth / 2,
                    Paints.blackPaint);
        }
        else
        {
            RectF rectF = new RectF((float) (playerX - Dimensions.playerRadius),
                    (float) (playerY - Dimensions.playerRadius),
                    (float) (playerX + Dimensions.playerRadius),
                    (float) (playerY + Dimensions.playerRadius));

            canvas.drawOval(rectF, playerPaint);
        }
    }

    private void adjustTouchY()
    {
        if (touchY > Dimensions.height - Dimensions.playerRadius - 10)
        {
            touchY = Dimensions.height - Dimensions.playerRadius - 10;
        }
        else if (touchY < Dimensions.playerRadius + 10)
        {
            touchY = Dimensions.playerRadius + 10;
        }
    }

    private void moveAndDrawBlocks(Canvas canvas)
    {
        for (Block block : blocks)
        {
            block.move();

            canvas.drawRect(block.getRectangle(), blockPaint);
        }
    }

    private void moveAndDrawCoins(Canvas canvas)
    {
        for (Coin coin : coins)
        {
            coin.move(relativeCoinFloat);
            coin.draw(canvas);
        }
    }

    private void moveAndDrawGravel(Canvas canvas)
    {
        int radius = Gravel.gravelRadius;

        RectF rect = new RectF();

        for (Gravel rock : gravel)
        {
            rock.move();
            rock.gravitate(Dimensions.gravity);

            rect.set(rock.x - radius, rock.y - radius, rock.x + radius, rock.y + radius);

            canvas.drawOval(rect, gravelPaint);
        }
    }

    private void moveAndDrawRemnants(Canvas canvas)
    {
        Rect rect;

        for (Remnant remnant : remnants)
        {
            canvas.save(Canvas.MATRIX_SAVE_FLAG);

            remnant.move();
            remnant.gravitate(Dimensions.gravity);

            rect = remnant.getRectangle();
            canvas.rotate(remnant.angle, (float) remnant.x, (float) remnant.y);

            Paints.anyPaint.setColor(remnant.colour);

            canvas.drawRect(rect, Paints.anyPaint);

            canvas.restore();
        }
    }

    private void moveAndDrawTurretsAndBullets(Canvas canvas)
    {
        for (Turret turret : turrets)
        {
            turret.move();
            turret.fireIfPossible(bullets, (int) playerX, (int) playerY);
            turret.draw(canvas, (int) playerX, (int) playerY);
        }

        for (Bullet bullet : bullets)
        {
            bullet.move();
            bullet.draw(canvas);
        }
    }

    private void moveAndDrawLaserTurrets(Canvas canvas)
    {
        for (LaserTurret laserTurret : laserTurrets)
        {
            laserTurret.move();
            laserTurret.draw(canvas, currentLaserLoading);
        }
    }

    private void moveAndDrawLasers(Canvas canvas)
    {
        if (shielded)
        {
            moveAndDrawLasersShielded(canvas);
            return;
        }

        for (LaserTurret laserTurret : laserTurrets)
        {
            if (laserTurret.isLaserShooting())
            {
                canvas.drawBitmap(Pictures.laserOn,
                        0,
                        (int) laserTurret.y - Dimensions.laserHeight / 2,
                        Paints.blackPaint);
            }
            else
            {
                canvas.drawBitmap(Pictures.laserOff,
                        0,
                        (int) laserTurret.y - Dimensions.laserHeight / 2,
                        Paints.blackPaint);
            }
        }
    }

    private void moveAndDrawLasersShielded(Canvas canvas)
    {
        double distanceX, distanceY;
        int drawLeft;
        double theta = 0;
        boolean intersects;
        Bitmap laserPicture;

        for (LaserTurret laserTurret : laserTurrets)
        {
            intersects = false;

            if (laserTurret.isLaserShooting())
            {
                laserPicture = Pictures.laserOn;
            }
            else
            {
                laserPicture = Pictures.laserOff;
            }

            if (laserTurret.y >= playerY - shieldRadius &&
                    laserTurret.y <= playerY + shieldRadius)
            {
                intersects = true;

                distanceY = playerY - laserTurret.y;
                distanceX = Math.sqrt(Math.pow(shieldRadius, 2) - Math.pow(distanceY, 2));

                Point p = getPointOnCircle(shieldRadius, getAngle(distanceX, distanceY));
                p.set(p.x + (int) playerX, p.y + (int) playerY);

                drawLeft = p.x;

                double tangentSlope;

                if (distanceY == 0)
                {
                    tangentSlope = 10000;
                }
                else
                {
                    tangentSlope = - distanceX / distanceY;
                }

                Point q = getNewSpeedsReflectedOverTangent(tangentSlope, -50, 0);

                theta = getAngle(q.x, q.y);
            }
            else
            {
                drawLeft = 0;
            }

            canvas.drawBitmap(laserPicture,
                    drawLeft,
                    (int) laserTurret.y - Dimensions.laserHeight / 2,
                    Paints.blackPaint);

            if (intersects)
            {
                canvas.save(Canvas.MATRIX_SAVE_FLAG);

                canvas.rotate((float) - theta, drawLeft, (float) laserTurret.y);

                canvas.drawBitmap(laserPicture,
                        drawLeft + slightLaserOffset,
                        (int) laserTurret.y - Dimensions.laserHeight / 2,
                        Paints.blackPaint);

                canvas.restore();
            }
        }
    }

    private void moveAndDrawMissileWarnings(Canvas canvas)
    {
        for (MissileWarning missileWarning : missileWarnings)
        {
            Paints.transparentPaint.setAlpha((int) (missileWarning.alpha * 255));

            canvas.drawBitmap(Pictures.missileWarning,
                    missileWarning.x - Dimensions.missileWarningWidth / 2,
                    missileWarning.y - Dimensions.missileWarningWidth / 2,
                    Paints.transparentPaint);
        }
    }

    private void moveAndDrawMissiles(Canvas canvas)
    {
        for (Missile missile : missiles)
        {
            missile.move();

            canvas.drawBitmap(Pictures.missile1,
                    (int) missile.x - Dimensions.missileWidth / 2,
                    missile.y - Dimensions.missileHeight / 2,
                    Paints.blackPaint);
        }
    }

    private void moveAndDrawExplosions(Canvas canvas)
    {
        for (Explosion explosion : explosions)
        {
            canvas.drawBitmap(Pictures.explosion[explosion.index],
                    explosion.x - Dimensions.explosionWidth / 2,
                    explosion.y - Dimensions.explosionHeight / 2,
                    Paints.blackPaint);
        }
    }

    private void moveAndDrawBosses(Canvas canvas)
    {
        for (final Boss boss : bosses)
        {
            boss.moveIn();
            boss.fireIfPossible(bullets, (int) playerX);
            boss.getCloserToMovingOut();
            boss.moveOut();

            canvas.drawBitmap(Pictures.bossBase,
                    (int) boss.x - Dimensions.bossBaseWidth / 2,
                    (int) boss.y - Dimensions.bossBaseWidth / 2,
                    Paints.blackPaint);
        }
    }

    private void moveAndDrawColourCircles(Canvas canvas)
    {
        changeColourCircleStagesIfNecessary();

        for (ColourCircle colourCircle : colourCircles)
        {
            colourCircle.rotate();
            colourCircle.move();

            Paints.transparentPaint.setAlpha((int) (colourCircle.alpha * 255));

            Matrix matrix = new Matrix();

            float factor = (float) ((double) colourCircle.radius / (Pictures.colourCircle.getWidth() / 2));

            matrix.postScale(factor, factor);
            matrix.postTranslate(colourCircle.x - colourCircle.radius, colourCircle.y -
                    colourCircle.radius);
            matrix.postRotate(colourCircle.theta, colourCircle.x, colourCircle.y);

            canvas.drawBitmap(Pictures.colourCircle, matrix, Paints.transparentPaint);
        }
    }

    private void moveAndDrawPathShooters(Canvas canvas)
    {
        for (PathShooter pathShooter : pathShooters)
        {
            pathShooter.move();
            pathShooter.fireIfPossible(paths, (int) playerX, (int) playerY, getResources());

            pathShooter.draw(canvas, (int) playerX, (int) playerY);
        }
    }

    private void moveAndDrawPathsAndPathBullets(Canvas canvas)
    {
        for (Path path : paths)
        {
            path.fireIfPossible(pathBullets);
        }

        for (int i = 0; i < pathBullets.size(); i++)
        {
            PathBullet pathBullet = pathBullets.get(i);

            pathBullet.weaken();

            if (! pathBullet.alive)
            {
                pathBullets.remove(i);
                i--;
            }
            else
            {
                pathBullet.draw(canvas);
            }
        }
    }

    private void moveAndDrawAllAroundShooters(Canvas canvas)
    {
        for (AllAroundShooter allAroundShooter : allAroundShooters)
        {
            allAroundShooter.move();
            allAroundShooter.fireIfPossible(bullets);

            allAroundShooter.draw(canvas);
        }
    }

    private void moveAndDrawPowerUps(Canvas canvas)
    {
        for (PowerUp powerUp : powerUpsOnScreen)
        {
            powerUp.move(relativePowerUpFloat);
            powerUp.draw(canvas);
        }
    }

    private void moveAndDrawShieldIfNecessary(Canvas canvas)
    {
        if (shielded)
        {
            if (shieldRadius == shieldMaxRadius)
            {
                canvas.drawBitmap(Pictures.shieldFullRadius,
                        (int) playerX - shieldMaxRadius,
                        (int) playerY - shieldMaxRadius,
                        Paints.blackPaint);
            }
            else
            {
                Rect rectangle = new Rect((int) playerX - shieldRadius,
                        (int) playerY - shieldRadius,
                        (int) playerX + shieldRadius,
                        (int) playerY + shieldRadius);

                canvas.drawBitmap(Pictures.shield, null, rectangle, Paints.blackPaint);
            }
        }
    }

    private void moveAndDrawTouchLineIfNecessary(Canvas canvas)
    {
        if (touchActivated)
        {
            Paints.touchLinePaint.setAlpha((int) (touchLineAlpha * 255));

            canvas.drawBitmap(Pictures.touchLine,
                    0,
                    (int) playerY - Dimensions.touchLineHeight / 2,
                    Paints.touchLinePaint);
        }
    }

    private void changeColourCircleStagesIfNecessary()
    {
        for (ColourCircle colourCircle : colourCircles)
        {
            colourCircle.tryToChangeStage((int) playerX, (int) playerY, activity);

            if (colourCircle.stage == ColourCircle.ATTACK_STAGE_2)
            {
                if (isTutorial && ! showedColourCircleTipAlready)
                {
                    showedColourCircleTipAlready = true;

                    synchronized(tipRequests)
                    {
                        tipRequests.add(new TutorialTipRequest(TUTORIAL_COLOUR_CIRCLE,
                                colourCircle,
                                TUTORIAL_COLOUR_CIRCLE_WAIT));
                    }
                }
            }
        }
    }

    private void startMissileWarnings()
    {
        for (final MissileWarning missileWarning : missileWarnings)
        {
            if (! missileWarning.animationStarted)
            {
                missileWarning.animationStarted = true;

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        missileWarning.startAnimation();
                    }
                });
            }
        }
    }

    private void checkMissileWarningsDone()
    {
        for (int i = 0; i < missileWarnings.size(); i++)
        {
            MissileWarning missileWarning = missileWarnings.get(i);

            if (missileWarning.completed)
            {
                missileWarnings.remove(missileWarning);
                i--;

                missiles.add(new Missile(Dimensions.width + Dimensions.missileWidth / 2, missileWarning.y));
            }
        }
    }

    private void checkExplosionsDone()
    {
        for (int i = 0; i < explosions.size(); i++)
        {
            if (explosions.get(i).finished)
            {
                explosions.remove(i);
                i--;
            }
        }
    }

    private void checkCollisionsWithBlocks()
    {
        if (shielded)
        {
            checkCollisionsWithBlocksShielded();
            return;
        }

        ArrayList<Block> removeBlocks = new ArrayList<>();

        int inRectangle;
        Point points[] = new Point[4];

        int incrementX[] = new int[] {-1, -1, 1, 1};
        int incrementY[] = new int[] {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++)
        {
            points[i] = new Point((int) playerX + incrementX[i] * Dimensions.playerRadius, (int) playerY + incrementY[i] * Dimensions.playerRadius);
        }

        for (Block block : blocks)
        {
            inRectangle = containsOneOfPoints(block.getRectangle(), points);

            if (inRectangle != -1)
            {
                becomeRemnants(block, points[inRectangle]);
                removeBlocks.add(block);

                health -= block.getDamage();

                blockDamage += block.getDamage();
            }
        }

        for (int i = 0; i < blocks.size(); i++)
        {
            if (removeBlocks.contains(blocks.get(i)))
            {
                blocks.remove(i);
                i--;
            }
        }
    }

    private void checkCollisionsWithBlocksShielded()
    {
        ArrayList<Block> removeBlocks = new ArrayList<>();

        int circleDistanceX;
        int circleDistanceY;

        for (Block block : blocks)
        {
            Rect rectangle = block.getRectangle();

            circleDistanceX = Math.abs((int) playerX - rectangle.centerX());
            circleDistanceY = Math.abs((int) playerY - rectangle.centerY());

            if (circleDistanceX > (rectangle.width() / 2 + shieldRadius)) continue;
            if (circleDistanceY > (rectangle.height() / 2 + shieldRadius)) continue;

            if (circleDistanceX <= (rectangle.width() / 2))
            {
                if (playerY < rectangle.centerY())
                {
                    becomeRemnants(block, new Point(rectangle.centerX(), rectangle.centerY() - rectangle.height() / 2));
                }
                else
                {
                    becomeRemnants(block, new Point(rectangle.centerX(), rectangle.centerY() + rectangle.height() / 2));
                }

                removeBlocks.add(block);
            }
            else if (circleDistanceY <= (rectangle.height() / 2))
            {
                if (playerX < rectangle.centerX())
                {
                    becomeRemnants(block, new Point(rectangle.centerX() - rectangle.width() / 2, rectangle.centerY()));
                }
                else
                {
                    becomeRemnants(block, new Point(rectangle.centerX() + rectangle.width() / 2, rectangle.centerY()));
                }

                removeBlocks.add(block);
            }
            else
            {
                int cornerDistanceSquared = (circleDistanceX - rectangle.width() / 2) * (circleDistanceX - rectangle.width() / 2)
                        + (circleDistanceY - rectangle.height() / 2) * (circleDistanceY - rectangle.height() / 2);

                if (cornerDistanceSquared <= shieldRadius * shieldRadius)
                {
                    becomeRemnants(block, getNearestCorner(rectangle, (int) playerX, (int) playerY));
                    removeBlocks.add(block);
                }
            }
        }

        for (Block block : removeBlocks)
        {
            blocks.remove(block);
        }
    }

    private Point getNearestCorner(Rect rectangle, int centerX, int centerY)
    {
        double distance1 = Math.pow(centerX - rectangle.left, 2) + Math.pow(centerY - rectangle.top, 2);
        double distance2 = Math.pow(centerX - rectangle.left, 2) + Math.pow(centerY - rectangle.bottom, 2);
        double distance3 = Math.pow(centerX - rectangle.right, 2) + Math.pow(centerY - rectangle.top, 2);
        double distance4 = Math.pow(centerX - rectangle.right, 2) + Math.pow(centerY - rectangle.bottom, 2);

        double min = Math.min(distance1, Math.min(distance2, Math.min(distance3, distance4)));

        if (distance1 == min)
        {
            return new Point(rectangle.left, rectangle.top);
        }
        else if (distance2 == min)
        {
            return new Point(rectangle.left, rectangle.bottom);
        }
        else if (distance3 == min)
        {
            return new Point(rectangle.right, rectangle.top);
        }
        else
        {
            return new Point(rectangle.right, rectangle.bottom);
        }
    }

    private int containsOneOfPoints(Rect rectangle, Point[] points)
    {
        for (int i = 0; i < points.length; i++)
        {
            if (rectangle.contains(points[i].x, points[i].y))
            {
                return i;
            }
        }

        return -1;
    }

    private void becomeRemnants(Block block, Point p)
    {
        Rect rectangle = block.getRectangle();

        int left = rectangle.left;
        int right = rectangle.right;
        int top = rectangle.top;
        int bottom = rectangle.bottom;

        int width = right - left;
        int height = bottom - top;

        int length = width / 3;

        for (int x = left + length / 2; x < right; x += length)
        {
            for (int y = top + length / 2; y < bottom + length / 2; y += length)
            {
                remnants.add(new Remnant(x, y, length / 2, p.x, p.y, width, height, shielded));
            }
        }
    }

    private void becomeBlackAndWhiteRemnants(RaceBlock raceBlock, Point p)
    {
        Rect rectangle = raceBlock.getRectangle();

        int left = rectangle.left;
        int right = rectangle.right;
        int top = rectangle.top;
        int bottom = rectangle.bottom;

        int width = right - left;
        int height = bottom - top;

        int length = width / 6;

        int colour = Color.BLACK;
        int columns;

        for (int x = left + length / 2; x < right; x += length)
        {
            columns = 0;

            for (int y = top + length / 2; y < bottom + length / 2; y += length)
            {
                columns++;

                remnants.add(new Remnant(x, y, length / 2, p.x, p.y, width, height, true, colour));

                colour = toggleColour(colour);
            }

            if (columns % 2 == 0)
            {
                colour = toggleColour(colour);
            }
        }
    }

    private int toggleColour(int colour)
    {
        if (colour == Color.BLACK)
        {
            return Color.WHITE;
        }
        else
        {
            return Color.BLACK;
        }
    }

    private void checkCollisionsWithCoins()
    {
        Coin coin;

        int inRectangle;
        Point points[] = new Point[5];

        int incrementX[] = new int[] {-1, -1, 1, 1, 0};
        int incrementY[] = new int[] {-1, 1, -1, 1, 0};

        Rect rectangle = new Rect((int) (playerX - Dimensions.playerRadius),
                (int) (playerY - Dimensions.playerRadius),
                (int) (playerX + Dimensions.playerRadius),
                (int) (playerY + Dimensions.playerRadius));

        for (int i = 0; i < coins.size(); i++)
        {
            coin = coins.get(i);

            for (int q = 0; q < 5; q++)
            {
                points[q] = new Point((int) coin.x + incrementX[q] * Dimensions.coinRadius,
                        coin.yIncludingFloat + incrementY[q] * Dimensions.coinRadius);
            }

            inRectangle = containsOneOfPoints(rectangle, points);

            if (inRectangle != -1)
            {
                coins.remove(i);
                i--;

                score += oneCoin;
            }
        }
    }

    private void checkCollisionsWithGround()
    {
        if (playerTouchingEdges())
        {
            createGravel();
            reorientPlayer();

            if (groundCounter == 0)
            {
                groundCounter = groundTimeUp;

                if (raceBlock != null) return;

                health -= groundDamage;
            }
            else
            {
                groundCounter--;
            }
        }
    }

    private void checkCollisionsWithBullets()
    {
        if (shielded)
        {
            checkCollisionsWithBulletsShielded();
            return;
        }

        Rect rectangle = new Rect((int) (playerX - Dimensions.playerRadius),
                (int) (playerY - Dimensions.playerRadius),
                (int) (playerX + Dimensions.playerRadius),
                (int) (playerY + Dimensions.playerRadius));

        for (int i = 0; i < bullets.size(); i++)
        {
            Bullet bullet = bullets.get(i);

            if (rectangle.contains(bullet.x, bullet.y))
            {
                health -= bullet.getDamage();

                if (bullet instanceof AllAroundShooterBullet)
                {
                    allAroundShooterDamage += bullet.getDamage();
                }
                else if (bullet instanceof BossBullet)
                {
                    bossDamage += bullet.getDamage();
                }
                else
                {
                    turretDamage += bullet.getDamage();
                }

                bullets.remove(i);
                i--;
            }
        }
    }

    private void checkCollisionsWithBulletsShielded()
    {
        int distance;
        int distanceX, distanceY;

        for (Bullet bullet : bullets)
        {
            if (bullet.bouncedOffShield)
            {
                continue;
            }

            distance = (int) Math.sqrt(Math.pow(playerX - bullet.x, 2) + Math.pow(playerY - bullet.y, 2));

            if (distance > shieldRadius) continue;

            distanceX = bullet.x - (int) playerX;
            distanceY = (int) playerY - bullet.y;

            double tangentSlope;

            if (distanceY == 0)
            {
                tangentSlope = 10000;
            }
            else
            {
                tangentSlope = - (double) distanceX / distanceY;
            }

            Point p = getNewSpeedsReflectedOverTangent(tangentSlope, bullet.incrementX, - bullet.incrementY);

            bullet.incrementX = p.x;
            bullet.incrementY = - p.y;

            bullet.bouncedOffShield = true;
        }
    }

    private Point getNewSpeedsReflectedOverTangent(double tangentSlope, double speedX, double speedY)
    {
        double theta;
        double speedTheta;
        double radius = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));

        speedTheta = getAngle(speedX, speedY);

        if (tangentSlope >= 0)
        {
            theta = Math.toDegrees(Math.atan(tangentSlope));
        }
        else
        {
            theta = 180 - Math.toDegrees(Math.atan(Math.abs(tangentSlope)));
        }

        Point p = getPointOnCircle(radius, speedTheta - theta);

        p.set(p.x, - p.y);

        p = getPointOnCircle(radius, getAngle(p.x, p.y) + theta);

        return p;
    }

    private double getAngle(double x, double y)
    {
        if (x == 0)
        {
            if (y > 0)
            {
                return 90;
            }
            else
            {
                return 270;
            }
        }
        else if (y == 0)
        {
            if (x > 0)
            {
                return 0;
            }
            else
            {
                return 180;
            }
        }

        if (x > 0 && y > 0)
        {
            return Math.toDegrees(Math.atan(y / x));
        }
        else if (x > 0 && y < 0)
        {
            return 360 - Math.toDegrees(Math.atan(Math.abs(y) / x));
        }
        else if (x < 0 && y > 0)
        {
            return 180 - Math.toDegrees(Math.atan(y / Math.abs(x)));
        }
        else
        {
            return 180 + Math.toDegrees(Math.atan(y / x));
        }
    }

    private Point getPointOnCircle(double radius, double theta)
    {
        return new Point((int) (radius * Math.cos(Math.toRadians(theta))), (int) (radius * Math.sin(Math.toRadians(theta))));
    }

    private void checkCollisionsWithLasers()
    {
        if (shielded)
        {
            return;
        }

        Point points[] = new Point[5];

        int incrementX[] = new int[] {-1, -1, 1, 1, 0};
        int incrementY[] = new int[] {-1, 1, -1, 1, 0};

        for (int i = 0; i < 5; i++)
        {
            points[i] = new Point((int) playerX + incrementX[i] * Dimensions.playerRadius, (int) playerY + incrementY[i] * Dimensions.playerRadius);
        }

        for (LaserTurret laserTurret : laserTurrets)
        {
            if (containsOneOfPoints(laserTurret.getLaser(), points) != -1 && laserTurret.isLaserShooting())
            {
                health -= laserTurret.getDamage();

                laserDamage += laserTurret.getDamage();
            }
        }
    }

    private void checkCollisionsWithMissiles()
    {
        if (shielded)
        {
            checkCollisionsWithMissilesShielded();
        }

        int inRectangle;
        Point points[] = new Point[4];

        int incrementX[] = new int[] {-1, -1, 1, 1};
        int incrementY[] = new int[] {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++)
        {
            points[i] = new Point((int) playerX + incrementX[i] * Dimensions.playerRadius, (int) playerY + incrementY[i] * Dimensions.playerRadius);
        }

        for (int i = 0; i < missiles.size(); i++)
        {
            Missile missile = missiles.get(i);

            Rect rectangle = missile.getRectangle();

            inRectangle = containsOneOfPoints(rectangle, points);

            if (inRectangle != -1)
            {
                health -= missile.getDamage();

                missileDamage += missile.getDamage();

                missiles.remove(i);
                i--;

                final Explosion explosion = new Explosion(points[inRectangle].x, points[inRectangle].y);

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        explosion.startAnimation();
                    }
                });

                explosions.add(explosion);
            }
        }
    }

    private void checkCollisionsWithMissilesShielded()
    {
        int circleDistanceX;
        int circleDistanceY;

        for (int i = 0; i < missiles.size(); i++)
        {
            Missile missile = missiles.get(i);
            Rect rectangle = missile.getRectangle();

            circleDistanceX = Math.abs((int) playerX - rectangle.centerX());
            circleDistanceY = Math.abs((int) playerY - rectangle.centerY());

            if (circleDistanceX > (rectangle.width() / 2 + shieldRadius)) continue;
            if (circleDistanceY > (rectangle.height() / 2 + shieldRadius)) continue;

            if (circleDistanceX <= (rectangle.width() / 2) ||
                    circleDistanceY <= (rectangle.height() / 2))
            {
                missiles.remove(i);
                i--;

                final Explosion explosion = new Explosion((int) missile.x, missile.y);

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        explosion.startAnimation();
                    }
                });

                explosions.add(explosion);
            }

            else
            {
                int cornerDistanceSquared = (circleDistanceX - rectangle.width() / 2) * (circleDistanceX - rectangle.width() / 2)
                        -  (circleDistanceY - rectangle.height() / 2) * (circleDistanceY - rectangle.height() / 2);

                if (cornerDistanceSquared <= shieldRadius * shieldRadius)
                {
                    missiles.remove(i);
                    i--;

                    final Explosion explosion = new Explosion((int) missile.x, missile.y);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            explosion.startAnimation();
                        }
                    });

                    explosions.add(explosion);
                }
            }
        }
    }

    private void checkCollisionsWithPowerUps()
    {
        Rect rectangle = new Rect((int) (playerX - Dimensions.playerRadius),
                (int) (playerY - Dimensions.playerRadius),
                (int) (playerX + Dimensions.playerRadius),
                (int) (playerY + Dimensions.playerRadius));

        Point points[] = new Point[5];

        int incrementX[] = new int[] {-1, -1, 1, 1, 0};
        int incrementY[] = new int[] {-1, 1, -1, 1, 0};

        for (int i = 0; i < powerUpsOnScreen.size(); i++)
        {
            PowerUp powerUp = powerUpsOnScreen.get(i);

            for (int q = 0; q < 5; q++)
            {
                points[q] = new Point((int) powerUp.x + incrementX[q] * Dimensions.powerUpWidthOnScreen / 2, powerUp.yIncludingFloat + incrementY[q] * Dimensions.powerUpWidthOnScreen / 2);
            }

            if (containsOneOfPoints(rectangle, points) != -1)
            {
                powerUpsOnScreen.remove(i);
                i--;

                addPowerUp(powerUp);
            }
        }
    }

    private void checkCollisionsWithColourCircles()
    {
        for (ColourCircle colourCircle : colourCircles)
        {
            if (colourCircle.stage == ColourCircle.ATTACK_STAGE_2 ||
                    colourCircle.stage == ColourCircle.COMEBACK_STAGE_1)
            {
                double distance = Math.sqrt(Math.pow(playerX - colourCircle.x, 2) + Math.pow(playerY - colourCircle.y, 2));

                if (distance <= colourCircle.radius && distance >= ((double) 3 / 4) * colourCircle.radius && colourCircle.alpha > 0)
                {
                    health -= colourCircle.getDamage();

                    bossDamage += colourCircle.getDamage();
                }
            }
        }
    }

    private void checkCollisionsWithPathsIfShielded()
    {
        if (! shielded) return;

        int distance;

        for (int i = 0; i < paths.size(); i++)
        {
            Path path = paths.get(i);

            distance = (int) Math.sqrt(Math.pow(playerX - path.currentX, 2) + Math.pow(playerY - path.currentY, 2));

            if (distance <= shieldRadius)
            {
                paths.remove(i);
                i--;
            }
        }
    }

    private void checkCollisionsWithPathBullets()
    {
        Rect rectangle = new Rect((int) (playerX - Dimensions.playerRadius),
                (int) (playerY - Dimensions.playerRadius),
                (int) (playerX + Dimensions.playerRadius),
                (int) (playerY + Dimensions.playerRadius));

        for (PathBullet pathBullet : pathBullets)
        {
            if (rectangle.contains(pathBullet.x, pathBullet.y))
            {
                health -= pathBullet.getDamage();

                pathShooterDamage += pathBullet.getDamage();
            }
        }
    }

    private void clocksChangeHealth()
    {
        synchronized (clockLock)
        {
            ClockManager clockManager;

            for (int i = 0; i < clockManagers.size(); i++)
            {
                clockManager = clockManagers.get(i);

                health += clockManager.getHealthChange();

                if (! clockManager.isAlive())
                {
                    clockManagers.remove(i);
                    i--;

                    clockManager.removeView();

                    clockXCoordinates.remove(Integer.valueOf(clockManager.getX()));
                }
            }
        }
    }

    private void startClockAnimation()
    {
        synchronized (clockLock)
        {
            if (clockAnimationRequests.size() == 0) return;

            ClockAnimationRequest request = clockAnimationRequests.get(0);

            if (request.isRequestDone())
            {
                clockAnimationRequests.remove(0);

                startClockAnimation();
            }
            else
            {
                request.tryToStartAnimation(activity);
            }
        }
    }

    private void addPowerUp(PowerUp powerUp)
    {
        if (numberOfPowerUps == maxNumberOfPowerUps) return;

        powerUpsAtBottom.add(powerUp);
        powerUp.addViewAndAnimate(activity, calculatePowerUpDestination(), container, this);

        numberOfPowerUps++;
    }

    private void addStartingPowerUp(PowerUp powerUp)
    {
        if (numberOfPowerUps == maxNumberOfPowerUps) return;

        powerUpsAtBottom.add(powerUp);
        powerUp.addViewNoAnimation(activity, calculatePowerUpDestination(), container, this);

        numberOfPowerUps++;
    }

    public void usePowerUp(PowerUp powerUp)
    {
        int index = powerUpsAtBottom.indexOf(powerUp);

        for (int i = index + 1; i < powerUpsAtBottom.size(); i++)
        {
            powerUpsAtBottom.get(i).moveToLeft();
        }

        numberOfPowerUps--;
        powerUpsAtBottom.get(index).removeView();
        powerUpsAtBottom.remove(index);

        if (powerUp instanceof HealthPowerUp)
        {
            health += healthRegainFromPowerUp;

            healthPowerUpsUsed++;
        }
        else if (powerUp instanceof ShieldPowerUp)
        {
            shieldPowersUpsUsed++;

            if (shielded && ! shieldGettingSmaller)
            {
                shieldCounter = shieldDuration;
                return;
            }

            if (! shielded)
            {
                shieldRadius = shieldMinRadius;

                shieldRadiusAnimator = ValueAnimator.ofInt(shieldMinRadius, shieldMaxRadius);
                shieldRadiusAnimator.setDuration(750);
            }
            else if (shieldGettingSmaller)
            {
                shieldRadiusAnimator.cancel();

                shieldRadiusAnimator = ValueAnimator.ofInt(shieldRadius, shieldMaxRadius);
                shieldRadiusAnimator.setDuration(500);
            }

            shielded = true;
            shieldGettingSmaller = false;
            shieldCounter = shieldDuration;

            shieldRadiusAnimator.setRepeatCount(0);

            shieldRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    shieldRadius = (Integer) animation.getAnimatedValue();
                }
            });

            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    shieldRadiusAnimator.start();
                }
            });
        }
        else if (powerUp instanceof TouchPowerUp)
        {
            touchPowerUpsUsed++;

            if (touchActivated && ! touchGettingWeaker)
            {
                touchCounter = touchDuration;
                return;
            }

            if (! touchActivated)
            {
                touchLineAnimator = ValueAnimator.ofFloat(0, 1);
                touchLineAnimator.setDuration(750);
            }
            else if (touchGettingWeaker)
            {
                touchLineAnimator.cancel();

                touchLineAnimator = ValueAnimator.ofFloat(touchLineAlpha, 1);
                touchLineAnimator.setDuration(500);
            }

            touchActivated = true;
            touchGettingWeaker = false;
            touchCounter = touchDuration;

            touchLineAnimator.setRepeatCount(0);
            touchLineAnimator.setInterpolator(new LinearInterpolator());
            touchLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    touchLineAlpha = (Float) animation.getAnimatedValue();
                }
            });

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    touchLineAnimator.start();
                }
            });
        }
        else if (powerUp instanceof CoinPowerUp)
        {
            coinPowerUpsUsed++;

            flyingCoinFadeForeground.setVisibility(View.VISIBLE);

            if (coinsActivated && ! coinsGettingWeaker)
            {
                coinsCounter = coinsDuration;
                return;
            }

            if (! coinsActivated)
            {
                flyingCoinForegroundAnimator = ValueAnimator.ofFloat(0, maxFlyingCoinForegroundAlpha);
                flyingCoinForegroundAnimator.setDuration(1000);
            }
            else if (coinsGettingWeaker)
            {
                flyingCoinForegroundAnimator.cancel();

                flyingCoinForegroundAnimator = ValueAnimator.ofFloat(flyingCoinFadeForegroundAlpha, maxFlyingCoinForegroundAlpha);
                flyingCoinForegroundAnimator.setDuration(500);
            }

            coinsActivated = true;
            coinsGettingWeaker = false;
            coinsCounter = coinsDuration;

            flyingCoinForegroundAnimator.setRepeatCount(0);
            flyingCoinForegroundAnimator.setInterpolator(new LinearInterpolator());
            flyingCoinForegroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    float value = (Float) animation.getAnimatedValue();

                    flyingCoinFadeForegroundAlpha = value;
                    flyingCoinFadeForeground.setAlpha(value);

                    factory.setFlyingCoinProbability(value / maxFlyingCoinForegroundAlpha);
                }
            });

            flyingCoinForegroundAnimator.start();
        }
        else if (powerUp instanceof ClockPowerUp)
        {
            clockPowerUpsUsed++;

            synchronized (clockLock)
            {
                Point p = getAvailableClockPoint();

                ClockManager clockManager = new ClockManager(activity, container, clockAnimationRequests, p.x, p.y);
                clockManager.addViewAndStart();

                clockManagers.add(clockManager);
            }
        }
    }

    private Point getAvailableClockPoint()
    {
        int possibleX = Dimensions.startClockX;

        while (true)
        {
            if (! clockXCoordinates.contains(possibleX))
            {
                clockXCoordinates.add(possibleX);
                break;
            }

            possibleX += Dimensions.distanceBetweenClocks;
        }

        return new Point(possibleX, Dimensions.clockYCoordinate);
    }

    private int calculatePowerUpDestination()
    {
        int x = Math.round(((RelativeLayout) powerUpSection.getParent()).getLeft() +
                powerUpSection.getLeft());

        return x + numberOfPowerUps * Dimensions.powerUpWidthBottomBar;
    }

    private void updateBottomBar()
    {
        if (health < 0)
        {
            health = 0;
        }

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                coinTextView.setText(Integer.toString(score));
                distanceTextView.setText(Integer.toString(distance));
                healthTextView.setText(Integer.toString(health));
            }
        });
    }

    private void removeShieldIfNecessary()
    {
        if (shielded && ! shieldGettingSmaller)
        {
            shieldCounter--;

            if (shieldCounter == 0)
            {
                shieldGettingSmaller = true;

                shieldRadiusAnimator = ValueAnimator.ofInt(shieldMaxRadius, shieldMinRadius);
                shieldRadiusAnimator.setDuration(1000);
                shieldRadiusAnimator.setRepeatCount(0);
                shieldRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        shieldRadius = (Integer) animation.getAnimatedValue();
                    }
                });

                shieldRadiusAnimator.addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        shielded = false;
                        shieldGettingSmaller = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        shieldRadiusAnimator.start();
                    }
                });
            }
        }
    }

    private void removeTouchLineIfNecessary()
    {
        if (touchActivated && ! touchGettingWeaker)
        {
            touchCounter--;

            if (touchCounter == 0)
            {
                touchGettingWeaker = true;

                touchLineAnimator = ValueAnimator.ofFloat(1, 0);
                touchLineAnimator.setDuration(2000);
                touchLineAnimator.setRepeatCount(0);
                touchLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        touchLineAlpha = (Float) animation.getAnimatedValue();
                    }
                });

                touchLineAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        touchActivated = false;
                        touchGettingWeaker = false;

                        acceleratingUp = false;

                        playerSpeed = 0;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        touchLineAnimator.start();
                    }
                });
            }
        }
    }

    private void removeCoinPowerUpIfNecessary()
    {
        if (coinsActivated && ! coinsGettingWeaker)
        {
            coinsCounter--;

            if (coinsCounter == 0)
            {
                coinsGettingWeaker = true;

                flyingCoinForegroundAnimator = ValueAnimator.ofFloat(maxFlyingCoinForegroundAlpha, 0);
                flyingCoinForegroundAnimator.setDuration(2000);
                flyingCoinForegroundAnimator.setRepeatCount(0);

                flyingCoinForegroundAnimator.addUpdateListener(new ValueAnimator
                        .AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        float value = (Float) animation.getAnimatedValue();

                        flyingCoinFadeForegroundAlpha = value;
                        flyingCoinFadeForeground.setAlpha(value);

                        factory.setFlyingCoinProbability(value / maxFlyingCoinForegroundAlpha);
                    }
                });

                flyingCoinForegroundAnimator.addListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        coinsActivated = false;
                        coinsGettingWeaker = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        flyingCoinForegroundAnimator.start();
                    }
                });
            }
        }
    }

    private void addStartingPowerUpIfNecessary()
    {
        if (startingPowerUps.size() == 0) return;

        while (true)
        {
            addStartingPowerUp(startingPowerUps.remove(0));

            if (startingPowerUps.size() == 0) break;
        }
    }

    private void reorientPlayer()
    {
        playerSpeed = 0;

        if (playerY - Dimensions.playerRadius <= Dimensions.floorHeight)
        {
            playerY = Dimensions.floorHeight + Dimensions.playerRadius + 1;
        }
        else
        {
            playerY = Dimensions.height - Dimensions.floorHeight - Dimensions.playerRadius - 1;
        }
    }

    private void createGravel()
    {
        if (playerY - Dimensions.playerRadius <= Dimensions.floorHeight)
        {
            gravel.add(new Gravel((int) playerX, Dimensions.floorHeight, false));
        }
        else
        {
            gravel.add(new Gravel((int) playerX, Dimensions.height - Dimensions.floorHeight, true));
        }
    }

    private boolean playerTouchingEdges()
    {
        return (playerY - Dimensions.playerRadius <= Dimensions.floorHeight || playerY + Dimensions.playerRadius >= Dimensions.height - Dimensions.floorHeight);
    }

    private void getEnemies()
    {
        factory.updateEnemies();
    }

    private void efficiency()
    {
        if (efficiencyCounter > 0)
        {
            efficiencyCounter--;
            return;
        }

        efficiencyCounter = shouldBeEfficient;

        for (int i = 0; i < blocks.size(); i++)
        {
            int x = blocks.get(i).getRectangle().centerX();
            int y = blocks.get(i).getRectangle().centerY();

            if (! keepRectangle.contains(x, y))
            {
                blocks.remove(i);
                i--;
            }
        }

        for (int i = 0; i < coins.size(); i++)
        {
            int x = (int) coins.get(i).x;
            int y = coins.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                coins.remove(i);
                i--;
            }
        }

        for (int i = 0; i < gravel.size(); i++)
        {
            int x = gravel.get(i).x;
            int y = gravel.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                gravel.remove(i);
                i--;
            }
        }

        for (int i = 0; i < bullets.size(); i++)
        {
            int x = bullets.get(i).x;
            int y = bullets.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < remnants.size(); i++)
        {
            int x = remnants.get(i).getRectangle().centerX();
            int y = remnants.get(i).getRectangle().centerY();

            if (! keepRectangle.contains(x, y))
            {
                remnants.remove(i);
                i--;
            }
        }

        for (int i = 0; i < turrets.size(); i++)
        {
            int x = (int) turrets.get(i).x;
            int y = (int) turrets.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                turrets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < laserTurrets.size(); i++)
        {
            int x = laserTurrets.get(i).x;
            int y = (int) laserTurrets.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                laserTurrets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < missileWarnings.size(); i++)
        {
            int x = missileWarnings.get(i).x;
            int y = missileWarnings.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                missileWarnings.remove(i);
                i--;
            }
        }

        for (int i = 0; i < missiles.size(); i++)
        {
            int x = (int) missiles.get(i).x;
            int y = missiles.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                missiles.remove(i);
                i--;
            }
        }

        for (int i = 0; i < bosses.size(); i++)
        {
            if (! bosses.get(i).alive)
            {
                bosses.remove(i);
                i--;
            }
        }

        for (int i = 0; i < colourCircles.size(); i++)
        {
            if (! colourCircles.get(i).alive)
            {
                colourCircles.remove(i);
                i--;
            }
        }

        for (int i = 0; i < pathShooters.size(); i++)
        {
            int x = (int) pathShooters.get(i).x;
            int y = pathShooters.get(i).y;

            if (! smallKeepRectangle.contains(x, y))
            {
                pathShooters.remove(i);
                i--;
            }
        }

        for (int i = 0; i < paths.size(); i++)
        {
            int x = paths.get(i).currentX;
            int y = paths.get(i).currentY;

            if (! keepRectangle.contains(x, y))
            {
                paths.remove(i);
                i--;
            }
        }

        for (int i = 0; i < allAroundShooters.size(); i++)
        {
            int x = (int) allAroundShooters.get(i).x;
            int y = (int) allAroundShooters.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                allAroundShooters.remove(i);
                i--;
            }
        }

        for (int i = 0; i < powerUpsOnScreen.size(); i++)
        {
            int x = (int) powerUpsOnScreen.get(i).x;
            int y = powerUpsOnScreen.get(i).y;

            if (! keepRectangle.contains(x, y))
            {
                powerUpsOnScreen.remove(i);
                i--;
            }
        }
    }

    private void showTips(Canvas canvas)
    {
        if (isRealGame) return;

        synchronized(tipRequests)
        {
            for (int i = 0; i < tipRequests.size(); i++)
            {
                TutorialTipRequest tipRequest = tipRequests.get(i);

                tipRequest.getCloser();

                if (tipRequest.shouldShow())
                {
                    tipRequests.remove(i);

                    drawTip(canvas, tipRequest);

                    break;
                }
            }
        }
    }

    private void drawTip(final Canvas canvas, final TutorialTipRequest tipRequest)
    {
        setShouldDraw(false);

        final Object object = tipRequest.object;

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                tutorialTipShowing = true;

                ImageView image = new ImageView(context);
                int x = 0;
                int y = 0;

                tipImageView = image;

                boolean upperPartOfScreen = false;
                int index;

                switch (tipRequest.type)
                {
                    case TUTORIAL_PLAYER:
                        x = (int) playerX;
                        y = (int) playerY;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialPlayer[index]);
                        break;
                    case TUTORIAL_BLOCK:
                        x = ((Block) object).rectangle.centerX() - Dimensions.tutorialPlayerWidth;
                        y = ((Block) object).rectangle.centerY();

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialBlock[index]);
                        break;
                    case TUTORIAL_TURRET:
                        x = (int) ((Turret) object).x - Dimensions.tutorialPlayerWidth;
                        y = (int) ((Turret) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialTurret[index]);
                        break;
                    case TUTORIAL_MISSILE_WARNING:
                        x = ((MissileWarning) object).x - Dimensions.tutorialPlayerWidth;
                        y = ((MissileWarning) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialMissileWarning[index]);
                        break;
                    case TUTORIAL_LASER_TURRET:
                        x = ((LaserTurret) object).x - Dimensions.tutorialPlayerWidth;
                        y = (int) ((LaserTurret) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialLaserTurret[index]);
                        break;
                    case TUTORIAL_BOSS:
                        x = (int) ((Boss) object).x - Dimensions.tutorialPlayerWidth;
                        y = (int) ((Boss) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialBoss[index]);
                        break;
                    case TUTORIAL_COIN:
                        x = (int) ((Coin) object).x - Dimensions.tutorialPlayerWidth;
                        y = ((Coin) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialCoin[index]);
                        break;
                    case TUTORIAL_POWER_UP:
                        x = (int) ((PowerUp) object).x - Dimensions.tutorialPlayerWidth;
                        y = ((PowerUp) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialPowerUp[index]);
                        break;
                    case TUTORIAL_COLOUR_CIRCLE:
                        x = ((ColourCircle) object).x - Dimensions.tutorialPlayerWidth;
                        y = ((ColourCircle) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialColourCircle[index]);
                        break;
                    case TUTORIAL_PATH_SHOOTER:
                        x = (int) (((PathShooter) object).x - Dimensions.tutorialPlayerWidth);
                        y = ((PathShooter) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialPathShooter[index]);
                        break;
                    case TUTORIAL_ALL_AROUND_SHOOTER:
                        x = (int) (((AllAroundShooter) object).x) - Dimensions.tutorialPlayerWidth;
                        y = (int) ((AllAroundShooter) object).y;

                        upperPartOfScreen = isUpper(y);
                        index = upperPartOfScreen ? 0 : 1;

                        image.setImageBitmap(Pictures.tutorialAllAroundShooter[index]);
                        break;
                    default:
                        image.setImageBitmap(Pictures.tutorialBlock[0]);
                        break;
                }

                y = changeTipYIfNecessary(y, ! upperPartOfScreen);

                image.setX(x);
                image.setY(y);

                container.addView(image);

                AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(1000);
                fadeIn.setFillAfter(true);

                canTouchToDismissTip = false;

                fadeIn.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        canTouchToDismissTip = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                tipImageView.startAnimation(fadeIn);

                stopAnimators();
            }
        });
    }

    private boolean isUpper(int y)
    {
        return y <= Dimensions.height / 2;
    }

    private int changeTipYIfNecessary(int y, boolean needChange)
    {
        if (! needChange) return y;

        return y - Dimensions.tutorialPlayerHeight;
    }

    public void notifyTutorialShouldFinish()
    {
        tutorialShouldFinish = true;
    }

    private void checkGameOver()
    {
        if (isTutorial) return;

        if (health == 0)
        {
            gameOver();
        }
    }

    private void checkTutorialOver()
    {
        if (isRealGame || ! tutorialShouldFinish) return;

        tutorialOverCounter--;

        if (tutorialOverCounter == 0)
        {
            tutorialOverCounter = tutorialOverDuration;

            if (nothingOnScreen())
            {
                showTutorialOverDialog();
            }
        }
    }

    private void showTutorialOverDialog()
    {
        ((GameRunnable) gameThread.getRunnable()).setRunning(false);

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                new AlertDialog.Builder(context)
                        .setTitle("You completed the tutorial!")
                        .setMessage("Go back to the main screen.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                activity.finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        });
    }

    private boolean nothingOnScreen()
    {
        return blocks.isEmpty() && coins.isEmpty() && gravel.isEmpty() && bullets.isEmpty() &&
                remnants.isEmpty() && turrets.isEmpty() && laserTurrets.isEmpty() && missileWarnings.isEmpty() &&
                missiles.isEmpty() && explosions.isEmpty() && bosses.isEmpty() && colourCircles.isEmpty() &&
                pathShooters.isEmpty() && paths.isEmpty() && pathBullets.isEmpty() && powerUpsOnScreen.isEmpty() &&
                allAroundShooters.isEmpty();
    }
}
