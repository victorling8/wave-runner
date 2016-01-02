package waverunner.example.victorling8.waverunner.MainScreen;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;
import waverunner.example.victorling8.waverunner.ShopScreen.ShopActivity;

public class ColourFragment extends Fragment
{
    SkinListener listener;
    GridView grid;
    ColourAdapter adapter;
    View useAsDefaultBackground;
    Context context;
    TextView ready;
    int indexChosen = -1;

    boolean shouldUseAsDefault = false;
    boolean isReady = false;
    boolean backgroundAnimationRunning = false;

    private boolean instantiated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int[] colourArray = getResources().getIntArray(R.array.colour_array);

        final ArrayList<Skin> skins = new ArrayList<>();

        initializeSkins(skins, colourArray);

        View view = inflater.inflate(R.layout.colour_panel, container, false);

        final GridView grid = (GridView) view.findViewById(R.id.colourGridView);
        final View useAsDefault = view.findViewById(R.id.use_as_default);
        useAsDefaultBackground = view.findViewById(R.id.use_as_default_background);
        ready = (TextView) view.findViewById(R.id.ready);

        grid.setNumColumns(4);

        this.grid = grid;

        final ColourAdapter adapter = new ColourAdapter(getContext(), R.layout.colour_circle, skins, indexChosen, grid);
        this.adapter = adapter;

        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                isReady = true;

                indexChosen = position;

                listener.onSkinPressed(skins.get(position));

                adapter.setIndexChosen(position);

                adapter.startAnimation(view, true);
            }
        });

        useAsDefault.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (! backgroundAnimationRunning)
                {
                    if (shouldUseAsDefault)
                    {
                        slideOut(true);
                    }
                    else
                    {
                        slideIn(true);
                    }

                    shouldUseAsDefault = !shouldUseAsDefault;
                }
            }
        });

        ready.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isReady)
                {
                    listener.onReady(skins.get(indexChosen), shouldUseAsDefault);

                    if (shouldUseAsDefault)
                    {
                        ((ColourAdapter) grid.getAdapter()).clearAnimations();
                    }
                }
            }
        });

        if (shouldUseAsDefault)
        {
            slideIn(false);
        }

        instantiated = true;

        return view;
    }

    public void notifyShouldUpdate()
    {
        if (instantiated)
        {
            int[] colourArray = getResources().getIntArray(R.array.colour_array);

            final ArrayList<Skin> skins = new ArrayList<>();

            initializeSkins(skins, colourArray);

            adapter.setSkinsAndUpdate(skins);
        }
    }

    private void initializeSkins(ArrayList<Skin> skins, int[] colourArray)
    {
        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.GOOSE_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.GOOSE_SKIN, R.drawable.goose_skin, Skin.getColourFromSkin(R.drawable.goose_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.PINK_TIGER_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.PINK_TIGER_SKIN, R.drawable.pink_tiger_skin, Skin.getColourFromSkin(R.drawable.pink_tiger_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.CODE_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.CODE_SKIN, R.drawable.code_skin, Skin.getColourFromSkin(R.drawable.code_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.WAVE_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.WAVE_SKIN, R.drawable.wave_skin, Skin.getColourFromSkin(R.drawable.wave_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.NYAN_CAT_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.NYAN_CAT_SKIN, R.drawable.nyan_cat_skin, Skin.getColourFromSkin(R.drawable.nyan_cat_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.SPACE_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.SPACE_SKIN, R.drawable.space_skin, Skin.getColourFromSkin(R.drawable.space_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.RAINBOW_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.RAINBOW_SKIN, R.drawable.rainbow_skin, Skin.getColourFromSkin(R.drawable.rainbow_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.STARS_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.STARS_SKIN, R.drawable.stars_skin, Skin.getColourFromSkin(R.drawable.stars_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.BLOCKS_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.BLOCKS_SKIN, R.drawable.blocks_skin, Skin.getColourFromSkin(R.drawable.blocks_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.GALAXY_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.GALAXY_SKIN, R.drawable.galaxy_skin, Skin.getColourFromSkin(R.drawable.galaxy_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.FIBRE_OPTIC_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.FIBRE_OPTIC_SKIN, R.drawable.fibre_optic_skin, Skin.getColourFromSkin(R.drawable.fibre_optic_skin, context)));
        }

        if (AppData.hasBoughtShopItem(getActivity(), ShopActivity.ILLUMINATI_SKIN))
        {
            skins.add(new Skin(true, ShopActivity.ILLUMINATI_SKIN, R.drawable.illuminati_skin, Skin.getColourFromSkin(R.drawable.illuminati_skin, context)));
        }

        for (int colour : colourArray)
        {
            skins.add(new Skin(false, "", 0, colour));
        }
    }

    @Override
    public void onAttach(Context context)
    {
        this.context = context;
        super.onAttach(context);

        try
        {
            listener = (SkinListener) context;
        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException("Activity must implement the listener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        context = null;
        adapter = null;
        listener = null;
        grid = null;
    }

    private void slideIn(boolean withDuration)
    {
        backgroundAnimationRunning = true;

        final View background = useAsDefaultBackground;

        TranslateAnimation animation = new TranslateAnimation(0, ScreenUtil.convertDpToPixel(105, context), 0, 0);

        if (withDuration)
        {
            animation.setDuration(500);
        }
        else
        {
            animation.setDuration(0);
        }

        animation.setFillAfter(true);
        animation.setInterpolator(new OvershootInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) background.getLayoutParams();

                params.leftMargin = (int) ScreenUtil.convertDpToPixel(20, context);

                background.setLayoutParams(params);

                background.clearAnimation();

                backgroundAnimationRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(0.3f, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                background.setAlpha((float) animation.getAnimatedValue());
            }
        });

        alphaAnimation.start();
        useAsDefaultBackground.startAnimation(animation);
    }

    private void slideOut(boolean withDuration)
    {
        backgroundAnimationRunning = true;

        final View background = useAsDefaultBackground;

        TranslateAnimation animation = new TranslateAnimation(0, ScreenUtil.convertDpToPixel(-105, context), 0, 0);

        if (withDuration)
        {
            animation.setDuration(500);
        }
        else
        {
            animation.setDuration(0);
        }

        animation.setFillAfter(true);
        animation.setInterpolator(new OvershootInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) background.getLayoutParams();

                params.leftMargin = (int) ScreenUtil.convertDpToPixel(-85, context);

                background.setLayoutParams(params);

                background.clearAnimation();

                backgroundAnimationRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(1, 0.3f);
        alphaAnimation.setDuration(500);
        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                background.setAlpha((float) animation.getAnimatedValue());
            }
        });

        alphaAnimation.start();

        useAsDefaultBackground.startAnimation(animation);
    }

    public interface SkinListener
    {
        public void onSkinPressed(Skin skin);
        public void onReady(Skin skin, boolean useAsDefault);
    }
}
