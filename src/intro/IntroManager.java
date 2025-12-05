package intro;

public class IntroManager {

    public static void playIntro() {
        // --- Music Configuration ---
        String MP3_PATH;
        String MUSIC_PLAYER;

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Windows
            MP3_PATH = "C:\\jingle bells uzun.mp3";
            MUSIC_PLAYER = "cmd.exe";
        } else {
            // macOS
            MP3_PATH = "/Users/gulfemkomurcu/Desktop/jingle bells uzun.mp3";
            MUSIC_PLAYER = "afplay";
        }

        Process musicProcess = null;

        try {
            // Start Music
            String[] musicCommand;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows needs the args to be separate
                // Using the logic that worked: "cmd.exe", "/c", "start wmplayer \"path\""
                musicCommand = new String[] { "cmd.exe", "/c", "start wmplayer \"" + MP3_PATH + "\"" };
            } else {
                musicCommand = new String[] { MUSIC_PLAYER, MP3_PATH };
            }

            musicProcess = new ProcessBuilder(musicCommand).inheritIO().start();

            // Run Animation
            YeniYilAnimasyon.animate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Stop Music
            if (musicProcess != null && musicProcess.isAlive()) {
                musicProcess.destroyForcibly();
            }
        }
    }
}
