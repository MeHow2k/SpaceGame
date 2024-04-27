package Constants;

/// PLIK ZE ZMIENNYMI GLOBALNYMI

public class C {
    public static final String VERSION= "1.0.0dev";

    public static int FRAME_HEIGHT=850;
    public static int FRAME_WIDTH=800;
    public static int GAMESTATE=100; // 0- gra 1-menu glowne 2-menusettings 3-how to play 4- autorzy 5- wybor skinow przed gra, 100 - intro
    public static int LEVEL=10;
    public static int LAST_LEVEL=41;
    public static boolean GODMODE=false;
    public static int playerSkin=0; //0-default 1-next 2-next skin
    public static int totalPoints=0;
    public static int playerLives=3;
    public static int weaponUpgrade=1;
    public static boolean isFirerateUpgrade=false;
    public static boolean shieldActivated=false;
    public static int SHIELD_COOLDOWN_TIME=200;
    public static boolean isLevelCreated = false;
    public static int shieldCooldown=SHIELD_COOLDOWN_TIME;
    public static int highscorePoints=0;
    public static int highscoreLevel=0;
    public static int cursorPosition=0;
    public static int cursorSettingsPosition=0;
    public static int cursorBeforeGamePosition=5;
    public static int musicVolume=0;
    public static int soundVolume=0;
    public static int intro_delay=0;
    public static long playtime=0;
    public static int gamesPlayed=0;
    public static boolean isMuted=false;
    public static boolean PAUSE;
}