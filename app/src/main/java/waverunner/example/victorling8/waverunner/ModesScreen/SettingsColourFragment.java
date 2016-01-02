package waverunner.example.victorling8.waverunner.ModesScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.MainScreen.ColourAdapter;
import waverunner.example.victorling8.waverunner.MainScreen.Skin;
import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.ShopScreen.ShopActivity;

public class SettingsColourFragment extends Fragment
{
    GridView grid;
    ColourAdapter adapter;
    Context context;
    View useAsDefault;
    View clearDefault;
    int indexChosen = -1;

    boolean isSkinFromFragment;
    int colourFromFragment;
    int skinIdFromFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int[] colourArray = getResources().getIntArray(R.array.colour_array);

        final ArrayList<Skin> skins = new ArrayList<>();

        initializeSkins(skins, colourArray);

        View view = inflater.inflate(R.layout.settings_colour_panel, container, false);

        final GridView grid = (GridView) view.findViewById(R.id.colourGridView);
        useAsDefault = view.findViewById(R.id.use_as_default);
        clearDefault = view.findViewById(R.id.clear_default);

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
                indexChosen = position;

                Skin skin = skins.get(position);

                isSkinFromFragment = skin.isSkin;
                colourFromFragment = skin.colour;
                skinIdFromFragment = skin.id;

                adapter.setIndexChosen(position);
                adapter.startAnimation(view, true);
            }
        });

        useAsDefault.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (indexChosen == -1) return;

                AppData.saveIsSkin(isSkinFromFragment);
                AppData.saveColour(colourFromFragment);
                AppData.saveSkinId(skinIdFromFragment);

                makeSuccessToast();
            }
        });

        clearDefault.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AppData.eraseSkinSaved();

                makeSuccessToast();
            }
        });

        return view;
    }

    private void makeSuccessToast()
    {
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context)
    {
        this.context = context;

        super.onAttach(context);
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
}
