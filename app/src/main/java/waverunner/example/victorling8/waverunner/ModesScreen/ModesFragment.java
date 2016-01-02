package waverunner.example.victorling8.waverunner.ModesScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;

public class ModesFragment extends Fragment
{
    public static final int NORMAL_MODE = 0;
    public static final int MISSILE_MODE = 1;
    public static final int TURRET_HELL_MODE = 2;
    public static final int SURVIVAL_MODE = 3;
    public static final int IMPOSSIBLE_MODE = 4;

    private ListView listView;
    private ModesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState)
    {
        View view = inflater.inflate(R.layout.modes_fragment_layout, container, false);

        listView = (ListView) view.findViewById(R.id.modes_list_view);

        ArrayList<Mode> modes = new ArrayList<>();

        modes.add(new Mode(R.drawable.normal_mode));
        modes.add(new Mode(R.drawable.missile_mode));
        modes.add(new Mode(R.drawable.turret_hell_mode));
        modes.add(new Mode(R.drawable.survival_mode));
        modes.add(new Mode(R.drawable.impossible_mode));

        adapter = new ModesAdapter(getActivity(), 0, modes, listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                adapter.setIndexChosen(position);

                adapter.startAnimation(view);

                AppData.saveMode(position);
            }
        });

        listView.setAdapter(adapter);

        return view;
    }
}
