package waverunner.example.victorling8.waverunner.ModesScreen;

public class Mode
{
    public int drawableId;

    public Mode(int drawableId)
    {
        this.drawableId = drawableId;
    }

    public static String getSearchString(int id)
    {
        if (id == ModesFragment.NORMAL_MODE)
        {
            return "Normal";
        }
        else if (id == ModesFragment.MISSILE_MODE)
        {
            return "MissileMode";
        }
        else if (id == ModesFragment.TURRET_HELL_MODE)
        {
            return "TurretHell";
        }
        else if (id == ModesFragment.SURVIVAL_MODE)
        {
            return "Survival";
        }
        else if (id == ModesFragment.IMPOSSIBLE_MODE)
        {
            return "Impossible";
        }

        return "Normal";
    }

    public static int getModifiedMedalScore(int score, int mode)
    {
        if (mode == ModesFragment.NORMAL_MODE)
        {
            return score;
        }
        else if (mode == ModesFragment.MISSILE_MODE)
        {
            return (int) (((double) 163 / 170) * score);
        }
        else if (mode == ModesFragment.TURRET_HELL_MODE)
        {
            return (int) (((double) 116 / 170) * score);
        }
        else if (mode == ModesFragment.SURVIVAL_MODE)
        {
            return (int) (((double) 142 / 170) * score);
        }
        else
        {
            return (int) (((double) 94 / 170) * score);
        }
    }
}
