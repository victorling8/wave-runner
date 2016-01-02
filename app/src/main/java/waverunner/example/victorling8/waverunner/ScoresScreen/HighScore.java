package waverunner.example.victorling8.waverunner.ScoresScreen;

public class HighScore
{
    public int distance;
    public int coins;
    public boolean skinEnabled;
    public int skinOrColourValue;

    private HighScore(int distance, int coins, boolean skinEnabled, int skinOrColourValue)
    {
        this.distance = distance;
        this.coins = coins;
        this.skinEnabled = skinEnabled;
        this.skinOrColourValue = skinOrColourValue;
    }

    public static HighScore parseStringToHighScore(String string)
    {
        int spaceIndexes[] = new int[3];
        int startIndex = 0;

        try
        {
            for (int i = 0; i < 3; i++)
            {
                spaceIndexes[i] = string.indexOf(" ", startIndex);

                if (spaceIndexes[i] == -1)
                {
                    throw new Exception();
                }

                startIndex = spaceIndexes[i] + 1;
            }

            int distance = Integer.parseInt(string.substring(0, spaceIndexes[0]));
            int coins = Integer.parseInt(string.substring(spaceIndexes[0] + 1, spaceIndexes[1]));

            boolean isSkin = string.substring(spaceIndexes[1] + 1, spaceIndexes[2]).equals("skin");

            int value = Integer.parseInt(string.substring(spaceIndexes[2] + 1));

            return new HighScore(distance, coins, isSkin, value);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
