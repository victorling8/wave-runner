package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.ModesScreen.Mode;
import waverunner.example.victorling8.waverunner.R;

public class GameOverDialogFragment extends DialogFragment
{
    private GameSurfaceView gameSurfaceView;

    private int score;
    private int coins;

    public static final int BRONZE_MIN = 7000;
    public static final int SILVER_MIN = 11000;
    public static final int GOLD_MIN = 15000;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        gameSurfaceView = ((GameActivity) activity).getSurfaceView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.game_over_dialog_layout, null);

        score = getArguments().getInt("score");
        coins = getArguments().getInt("coins");

        TextView distanceTextView = (TextView) view.findViewById(R.id.game_over_dialog_distance_text_view);
        TextView coinTextView = (TextView) view.findViewById(R.id.game_over_dialog_coin_text_view);
        TextView playAgainTextView = (TextView) view.findViewById(R.id.game_over_dialog_play_again);
        TextView quitTextView = (TextView) view.findViewById(R.id.game_over_dialog_quit);

        distanceTextView.setText(Integer.toString(score));
        coinTextView.setText(Integer.toString(coins));

        final ImageView medal = (ImageView) view.findViewById(R.id.game_over_dialog_medal);

        final boolean shouldAnimateMedal = setMedalBackground(medal, score, AppData.getMode());

        final Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.GameOverDialogTheme)
                .setView(view)
                .setCancelable(false)
                .create();

        playAgainTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                gameSurfaceView.restartGame();
            }
        });

        quitTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                params.width = (int) ScreenUtil.convertDpToPixel(350, getActivity());

                dialog.getWindow().setAttributes(params);

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (shouldAnimateMedal)
                        {
                            AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
                            fadeIn.setDuration(2000);

                            medal.startAnimation(fadeIn);
                        }
                    }
                });
            }
        });

        return dialog;
    }

    public static boolean setMedalBackground(ImageView medal, int score, int mode)
    {
        int bronzeMin = Mode.getModifiedMedalScore(BRONZE_MIN, mode);
        int silverMin = Mode.getModifiedMedalScore(SILVER_MIN, mode);
        int goldMin = Mode.getModifiedMedalScore(GOLD_MIN, mode);

        if (bronzeMin <= score && score < silverMin)
        {
            medal.setBackgroundResource(R.drawable.bronze_medal);

            return true;
        }
        else if (silverMin <= score && score < goldMin)
        {
            medal.setBackgroundResource(R.drawable.silver_medal);

            return true;
        }
        else if (goldMin <= score)
        {
            medal.setBackgroundResource(R.drawable.gold_medal);

            return true;
        }

        return false;
    }

    public void shouldDismiss()
    {
        if (getDialog() != null && getDialog().isShowing())
        {
            getDialog().dismiss();
        }
    }
}
