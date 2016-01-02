package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.ModesScreen.Mode;
import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;

public class HighScoreFragment extends Fragment
{
    private RecyclerView recyclerView;
    private HighScoreModeRecyclerViewAdapter recyclerViewAdapter;

    private ListView listView;
    private View noHighScores;
    private HighScoreAdapter adapter;

    private int indexChosen = 0;

    private ArrayList<HighScore> highScores = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.high_score_fragment_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.high_score_mode_recycler_view);
        listView = (ListView) view.findViewById(R.id.high_score_list_view);
        noHighScores = view.findViewById(R.id.no_high_scores_text_view);

        if (savedInstanceState != null)
        {
            indexChosen = savedInstanceState.getInt("indexChosen");
        }

        populateHighScores();

        adapter = new HighScoreAdapter(getActivity(), R.layout.high_score_list_item, highScores);

        adapter.setMode(indexChosen);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                adapter.setIndexChosen(position);

                adapter.animateView(view);
            }
        });


        ArrayList<Integer> drawableIds = new ArrayList<>();

        drawableIds.add(R.drawable.normal_mode);
        drawableIds.add(R.drawable.missile_mode);
        drawableIds.add(R.drawable.turret_hell_mode);
        drawableIds.add(R.drawable.survival_mode);
        drawableIds.add(R.drawable.impossible_mode);

        recyclerViewAdapter = new HighScoreModeRecyclerViewAdapter(drawableIds, new HighScoreModeRecyclerViewAdapter.HighScoreModeRecyclerViewCallback()
        {
            @Override
            public void onClick(int index)
            {
                indexChosen = index;

                populateHighScores();
                adapter.setIndexChosen(-1);
                adapter.setMode(indexChosen);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerViewAdapter.setSelectedIndex(indexChosen);

        MyLinearLayoutManager manager = new MyLinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.post(new Runnable()
        {
            @Override
            public void run()
            {
                recyclerView.smoothScrollToPosition(indexChosen);
            }
        });

        return view;
    }

    private void populateHighScores()
    {
        highScores.clear();

        HighScore highScore;

        for (int i = 0; i < AppData.MAX_HIGH_SCORES_SAVED; i++)
        {
            highScore = AppData.getHighScore(Mode.getSearchString(indexChosen) + "_", i);

            if (highScore != null)
            {
                highScores.add(highScore);
            }
        }

        if (highScores.isEmpty())
        {
            listView.setVisibility(View.GONE);
            noHighScores.setVisibility(View.VISIBLE);
        }
        else
        {
            listView.setVisibility(View.VISIBLE);
            noHighScores.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt("indexChosen", indexChosen);
    }
}
