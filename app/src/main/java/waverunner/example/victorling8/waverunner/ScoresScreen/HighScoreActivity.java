package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.R;

public class HighScoreActivity extends AppCompatActivity
{
    private RecyclerView header;
    private RecyclerViewAdapter headerAdapter;
    private ArrayList<RecyclerViewItemInformation> headerItems = new ArrayList<>();

    private int selectedFragmentIndex = 0;

    private HighScoreFragment highScoreFragment;
    private EnemiesFragment enemiesFragment;
    private ItemsFragment itemsFragment;

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

    private static final String HIGH_SCORES = "High Scores";
    private static final String ENEMIES = "Enemies";
    private static final String ITEMS = "Items";

    private static final int HIGH_SCORES_COLOUR = 0xffff66cc;
    private static final int ENEMIES_COLOUR = 0xff66ccff;
    private static final int ITEMS_COLOUR = 0xffffff66;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_screen);

        if (savedInstanceState == null)
        {
            highScoreFragment = new HighScoreFragment();
            enemiesFragment = new EnemiesFragment();
            itemsFragment = new ItemsFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.high_score_container, enemiesFragment, "enemiesFragment")
                    .detach(enemiesFragment)
                    .add(R.id.high_score_container, itemsFragment, "itemsFragment")
                    .detach(itemsFragment)
                    .add(R.id.high_score_container, highScoreFragment, "highScoreFragment")
                    .attach(highScoreFragment)
                    .commit();

            fragments.add(highScoreFragment);
            fragments.add(enemiesFragment);
            fragments.add(itemsFragment);
        }
        else
        {
            highScoreFragment = (HighScoreFragment) getSupportFragmentManager().findFragmentByTag("highScoreFragment");
            enemiesFragment = (EnemiesFragment) getSupportFragmentManager().findFragmentByTag("enemiesFragment");
            itemsFragment = (ItemsFragment) getSupportFragmentManager().findFragmentByTag("itemsFragment");

            fragments.add(highScoreFragment);
            fragments.add(enemiesFragment);
            fragments.add(itemsFragment);

            if (! highScoreFragment.isDetached())
            {
                selectedFragmentIndex = 0;
            }
            else if (! enemiesFragment.isDetached())
            {
                selectedFragmentIndex = 1;
            }
            else if (! itemsFragment.isDetached())
            {
                selectedFragmentIndex = 2;
            }
        }

        header = (RecyclerView) findViewById(R.id.high_score_recycler_view);

        MyLinearLayoutManager manager = new MyLinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        header.setLayoutManager(manager);

        headerItems.add(new RecyclerViewItemInformation(HIGH_SCORES, HIGH_SCORES_COLOUR));
        headerItems.add(new RecyclerViewItemInformation(ENEMIES, ENEMIES_COLOUR));
        headerItems.add(new RecyclerViewItemInformation(ITEMS, ITEMS_COLOUR));

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
