package intro;

/**
 * Manages the application's introductory sequence.
 * 
 * <p>
 * Handles the playback of the intro music and triggers the visual animation
 * implemented in {@link YeniYilAnimasyon}. Dynamically selects logic based on
 * the operating system (Windows vs MacOS).
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class IntroManager {

    /**
     * Plays the introductory music and animation.
     * 
     * <p>
     * Detects the operating system to choose the appropriate media player
     * (cmd.exe/wmplayer for Windows, afplay for MacOS). Starts the background
     * (cmd.exe/wmplayer for Windows, afplay for MacOS). Starts the background
     * music process and launches the console animation. The music process is left
     * running in the background to ensure playback continues during the application
     * usage.
     * </p>
     */
    public static void playIntro() {
        // --- Music Configuration ---
        String MP3_PATH;
        String MUSIC_PLAYER;

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Windows
            MP3_PATH = "C:\\intro.mp3";
            MUSIC_PLAYER = "cmd.exe";
        } else {
            // macOS
            MP3_PATH = "/Users/gulfemkomurcu/Desktop/intro.mp3";
            MUSIC_PLAYER = "afplay";
        }

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

            new ProcessBuilder(musicCommand).inheritIO().start();

            // Run Animation
            NewYearAnimation.animate();

            // Clear any buffered input (like 'q' + Enter) to prevent immediate menu
            // selection
            while (System.in.available() > 0) {
                System.in.read();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Music continues playing in the background as per user request (especially for
        // Mac)
    }
}
