package waverunner.example.victorling8.waverunner.ModesScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.ScoresScreen.MyLinearLayoutManager;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.ScoresScreen.RecyclerViewAdapter;
import waverunner.example.victorling8.waverunner.ScoresScreen.RecyclerViewItemInformation;

public class SettingsActivity extends AppCompatActivity
{
    private RecyclerView header;
    private RecyclerViewAdapter headerAdapter;
    private ArrayList<RecyclerViewItemInformation> headerItems = new ArrayList<>();

    private int selectedFragmentIndex = 0;

    private ModesFragment modesFragment;
    private SettingsColourFragment skinsFragment;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private RecyclerViewAdapter.RecyclerViewCallback callback = new RecyclerViewAdapter.RecyclerViewCallback()
    {
        @Override
        public void onClick(int index)
        {
            if (selectedFragmentIndex == index) return;

            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(fragments.get(index))
                    .detach(fragments.get(selectedFragmentIndex))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

            selectedFragmentIndex = index;
        }
    };

    private static final String MODES = "Modes";
    private static final String SKINS = "Skins";

    private static final int MODES_COLOUR = 0xff66ff99;
    private static final int SKINS_COLOUR = 0xffffbb33;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        if (savedInstanceState == null)
        {
            modesFragment = new ModesFragment();
            skinsFragment = new SettingsColourFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.settings_container, modesFragment, "modesFragment")
                    .attach(modesFragment)
                    .add(R.id.settings_container, skinsFragment, "skinsFragment")
                    .detach(skinsFragment)
                    .commit();
        }
        else
        {
            modesFragment = (ModesFragment) getSupportFragmentManager().findFragmentByTag("modesFragment");
            skinsFragment = (SettingsColourFragment) getSupportFragmentManager().findFragmentByTag("skinsFragment");

            if (! modesFragment.isDetached())
            {
                selectedFragmentIndex = 0;
            }
            else if (! skinsFragment.isDetached())
            {
                selectedFragmentIndex = 1;
            }
        }

        fragments.add(modesFragment);
        fragments.add(skinsFragment);

        header = (RecyclerView) findViewById(R.id.settings_recycler_view);

        MyLinearLayoutManager manager = new MyLinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        header.setLayoutManager(manager);

        headerItems.add(new RecyclerViewItemInformation(MODES, MODES_COLOUR));
        headerItems.add(new RecyclerViewItemInformation(SKINS, SKINS_COLOUR));

        headerAdapter = new RecyclerViewAdapter(headerItems, callback);

        headerAdapter.setSelectedIndex(selectedFragmentIndex);

        header.setAdapter(headerAdapter);
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
}
