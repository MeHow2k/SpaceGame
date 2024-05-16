package Constants;

/// PLIK ZE ZMIENNYMI GLOBALNYMI

public class C {
    public static final String VERSION= "1.0.2dev";//last 1.0.1
    public static int LANGUAGE=999; //0-en 1-pl-
    public static boolean DEVELOPMENT=true;
    public static String SECRETKEY = "fg54w3uhwa23y56tdxse45uy";

    public static int FRAME_HEIGHT=850;
    public static int FRAME_WIDTH=800;
    public static int GAMESTATE=100; // 0- gra 1-menu glowne 2-menusettings 3-how to play 4- autorzy 5- wybor skinow przed gra, 6-osiagniecia
    // 22-playernameseletion,21-playersettings, , 100 - intro, 99 - wybor jezyka
    public static int LEVEL=0;
    public static int LAST_LEVEL=51;
    public static boolean GODMODE=false;
    public static String PLAYER_NAME="";
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
    public static int cursorPositionColumn=0;
    public static int cursorPlayerSettingsPosition=0;
    public static int cursorBeforeGamePosition=5;
    public static int cursorLanguagePosition=0;
    public static int cursorAchievementsColumn=0;
    public static int cursorAchievementsRow=0;
    public static int cursorPlayerNamePosition=0;
    public static int musicVolume=0;
    public static int soundVolume=0;
    public static boolean canEnterName=false;//do aktywacji wpisywania nazwy
    /////////////////////////////////obsługa osiągnięć////////////////////////////////////////////
    public static int[] achievements = new int[30];//tablica osiagniec
    public static String ARCH_PLAYER_NAME="";
    public static boolean isAchivement0done = false;//win a game
    public static boolean isAchivement1done = false;//beat 10lvl boss
    public static boolean isAchivement2done = false;//beat 20lvl boss
    public static boolean isAchivement3done = false;//beat 30lvl boss
    public static boolean isAchivement4done = false;//beat 40lvl boss
    public static boolean isAchivement5done = false;//beat 50lvl boss

    public static int intro_delay=0;
    public static long playtime=0;
    public static int gamesPlayed=0;
    public static boolean hasPlayerName=false;
    public static boolean isMuted=false;
    public static boolean isFPSon=false;
    public static boolean PAUSE;
}