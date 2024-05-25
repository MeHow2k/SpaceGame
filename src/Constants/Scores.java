package Constants;

public class Scores {
        private String playerName;
        private int totalPoints;
        private String playTime;

        public Scores(String playerName, int totalPoints, String playTime) {
            this.playerName = playerName;
            this.totalPoints = totalPoints;
            this.playTime = playTime;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public String getPlayTime() {
            return playTime;
        }

}
