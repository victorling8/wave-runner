package waverunner.example.victorling8.waverunner.MainScreen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import waverunner.example.victorling8.waverunner.R;

public class MainScreenButtonFragment extends Fragment
{
    private TextView newGameButton;
    private TextView instructionsButton;
    private TextView settingsButton;
    private TextView shopButton;
    private TextView statsButton;

    private MainScreenButtonListener listener;

    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            listener.onMainButtonClick(v.getId());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.button_fragment, container, false);

        newGameButton = (TextView) view.findViewById(R.id.new_game_button);
        instructionsButton = (TextView) view.findViewById(R.id.instructions_button);
        settingsButton = (TextView) view.findViewById(R.id.settings_button);
        shopButton = (TextView) view.findViewById(R.id.shop_button);
        statsButton = (TextView) view.findViewById(R.id.stats_button);

        /*
        newGameButton.setOnTouchListener(touchListener);
        instructionsButton.setOnTouchListener(touchListener);
        settingsButton.setOnTouchListener(touchListener);
        shopButton.setOnTouchListener(touchListener);
        statsButton.setOnTouchListener(touchListener);
        */

        newGameButton.setOnClickListener(clickListener);
        instructionsButton.setOnClickListener(clickListener);
        settingsButton.setOnClickListener(clickListener);
        shopButton.setOnClickListener(clickListener);
        statsButton.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            listener = (MainScreenButtonListener) activity;
        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException("Activity must implement the listener");
        }
    }

    public interface MainScreenButtonListener
    {
        public void onMainButtonClick(int id);
    }
}
