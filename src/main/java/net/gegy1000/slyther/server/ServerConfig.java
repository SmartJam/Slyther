package net.gegy1000.slyther.server;

import net.gegy1000.slyther.game.Configuration;

public class ServerConfig implements Configuration {
    public int gameRadius = 21600;
    /** maximum snake length in body parts units */
    public int mscps = 411;
    public int sectorSize = 300;
    public int sectorsAlongEdge = 114;
    /** coef. to calculate angular speed change depending snake speed*/
    public float spangDv = 4.8F;
    /** (Maybe nsp stands for "node speed"?) */
    public float nsp1 = 5.39F;
    public float nsp2 = 0.4F;
    public float nsp3 = 14.0F;
    /** basic snake angular speed */
    public float snakeTurnSpeed = 0.033F;
    /** angle in rad per 8ms at which prey can turn */
    public float preyTurnSpeed = 0.028F;
    /** snake tail speed ratio */
    public float cst = 0.43F;
    public int serverPort = 444;
    public int leaderboardLength = 10;
    public long leaderboardUpdateFrequency = 5000;
    public int maxSpawnFoodPerSector = 10;
    public int minNaturalFoodSize = 5;
    public int maxNaturalFoodSize = 8;
    public long respawnFoodDelay = 10000;
}
