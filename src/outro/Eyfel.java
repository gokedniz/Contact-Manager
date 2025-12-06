package outro;

import java.io.IOException;
import java.lang.Process;
import java.util.concurrent.TimeUnit;

/**
 * Handles the "Eyfel Tower" animation sequence (Outro).
 */
public class Eyfel {

    public static final String ANSI_RESET = "\u001B[0m";

    // Original Eyfel tones
    public static final String FW_WHITE = "\u001B[97m";
    public static final String FW_YELLOW = "\u001B[93m";
    public static final String FW_ORANGE = "\u001B[38;5;208m";
    public static final String FW_RED = "\u001B[31m";
    public static final String FW_PURPLE = "\u001B[35m";

    // Galatasaray colors
    public static final String GS_YELLOW = "\u001B[93m";
    public static final String GS_RED = "\u001B[31m";

    // Blue, Red, White
    public static final String NEW_BLUE = "\u001B[34m";
    public static final String NEW_RED = "\u001B[31m";
    public static final String NEW_WHITE = "\u001B[97m";

    // New Red-White colors
    public static final String RW_RED = "\u001B[31m"; // Red
    public static final String RW_WHITE = "\u001B[97m"; // White

    public static final String CLEAR_SCREEN = "\u001B[2J\u001B[H";
    private static final String PADDING = "                    "; // Right padding

    // Static variable to store music process
    private static Process musicProcess;

    // --- EYFEL TOWER DRAWING ---
    private static final String[] TOWER_LINES = {
            PADDING + "                $$ ",
            PADDING + "                $$",
            PADDING + "               $$$$  ",
            PADDING + "               $$$$",
            PADDING + "                $$",
            PADDING + "                $$",
            PADDING + "                $$",
            PADDING + "                $$",
            PADDING + "                $$",
            PADDING + "                $$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "               $$$$",
            PADDING + "              $$$$$$",
            PADDING + "              $$$$$$",
            PADDING + "              $$$$$$",
            PADDING + "              $$$$$$",
            PADDING + "             $$$$$$$$",
            PADDING + "             $$$$$$$$",
            PADDING + "             $$$$$$$$",
            PADDING + "             $$$$$$$$",
            PADDING + "            $$$$$$$$$$",
            PADDING + "            $$$$$$$$$$",
            PADDING + "          $$$$$$$$$$$$$$",
            PADDING + "          $$$$$$$$$$$$$$",
            PADDING + "           $$$$$$$$$$$$",
            PADDING + "           $$$$$$$$$$$$",
            PADDING + "          $$$$$$$$$$$$$$",
            PADDING + "          $$$$$     $$$$",
            PADDING + "         $$$$$$     $$$$$",
            PADDING + "        $$$$$$       $$$$$",
            PADDING + "        $$$$$$       $$$$$",
            PADDING + "       $$$$$$         $$$$$",
            PADDING + "   $$$$$$$$$$ $ $ $ $ $$$$$$$$$",
            PADDING + "   $$$$$$$$$$$$$$$$$$$$$$$$$$$$",
            PADDING + "   $$$$$$$$$$$$$$$$$$$$$$$$$$$$",
            PADDING + "   $$$$$$$$$$$$$$$$$$$$$$$$$$$$",
            PADDING + "  $$$$$$$$$             $$$$$$$$",
            PADDING + " $$$$$$$$$$             $$$$$$$$$",
            PADDING + "$$$$$$$$$                 $$$$$$$$$"
    };

    /**
     * Checks if the user pressed 'q' (or 'Q') to skip the animation.
     * 
     * @return true if skipped, false otherwise.
     */
    private static boolean checkSkip() {
        try {
            int avail = System.in.available();
            if (avail > 0) {
                byte[] buff = new byte[avail];
                System.in.read(buff);
                String s = new String(buff);
                // Check if any character in the buffer is 'q' or 'Q'
                if (s.toLowerCase().contains("q"))
                    return true;
            }
        } catch (IOException e) {
            // Check failed, ignore
        }
        return false;
    }

    /**
     * Plays the outro animation with music.
     */
    public static void playOutro() {
        // Music is now started separately via startMusic() call.
        
        System.out.println(CLEAR_SCREEN);
        System.out.println("🗼 Animation started. Duration: Approx. 64 seconds (4 full cycles).");
        System.out.println("---");
        System.out.println("[Press 'q' + Enter to skip]");

        // --- ANIMATION SETTINGS ---
        int delayMs = 100; // 0.1 seconds
        // For each color set to last 4 seconds: 4 seconds / 0.1 seconds = 40 frames
        int framesPerColorSet = 40;

        // Four 4-second transitions (4 + 4 + 4 + 4 = 16 seconds/cycle)

        // For 68 seconds total (song), closest full cycle count is 4 (4 * 16 = 64
        // seconds).
        // Program ends after 64 seconds.
        final int TOTAL_CYCLES = 4;

        String[] originalColors = { FW_WHITE, FW_YELLOW, FW_ORANGE, FW_YELLOW, FW_RED, FW_PURPLE };
        String[] galatasarayColors = { GS_YELLOW, GS_RED };
        String[] blueRedWhiteColors = { NEW_BLUE, NEW_RED, NEW_WHITE, NEW_RED, NEW_BLUE, NEW_WHITE };
        // New Red-White set (Minimalist flag colors)
        String[] redWhiteColors = { RW_RED, RW_WHITE, RW_RED, RW_WHITE };

        for (int i = 0; i < TOTAL_CYCLES; i++) {
            // Step 1: Original Tones (4 seconds)
            if (animateTower(originalColors, framesPerColorSet, delayMs))
                break;

            // Step 2: Galatasaray Tones (4 seconds)
            if (animateTower(galatasarayColors, framesPerColorSet, delayMs))
                break;

            // Step 3: Blue/Red/White Tones (4 seconds)
            if (animateTower(blueRedWhiteColors, framesPerColorSet, delayMs))
                break;

            // Step 4: Red/White Tones (4 seconds)
            if (animateTower(redWhiteColors, framesPerColorSet, delayMs))
                break;
        }

        // Clear terminal and end music process when animation finishes.
        System.out.print(CLEAR_SCREEN);
        System.out.println("\n🎉 Animation and music completed successfully!");

        // Manually terminate music process (if still playing)
        if (musicProcess != null && musicProcess.isAlive()) {
            musicProcess.destroyForcibly();
        }

    }

    /**
     * Runs a segment of the Eyfel Tower animation.
     * 
     * @param colorSet The set of colors to cycle through.
     * @param frames   The number of frames to display.
     * @param delayMs  The delay in milliseconds between frames.
     * @return true if skipped, false otherwise
     */
    private static boolean animateTower(String[] colorSet, int frames, int delayMs) {
        int len = colorSet.length;

        for (int frame = 0; frame < frames; frame++) {
            if (checkSkip())
                return true; // Check skip

            System.out.print(CLEAR_SCREEN);

            for (int i = 0; i < TOWER_LINES.length; i++) {
                // Color cycling using (i + frame)
                int colorIndex = (i + frame) % len;
                String color = colorSet[colorIndex];
                System.out.println(color + TOWER_LINES[i] + ANSI_RESET);
            }
            System.out.println("\n[Press 'q' + Enter to skip]");

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return true;
            }
        }
        return false;
    }

    /**
     * Starts the outro music sequence.
     * Accessible publicly to allow earlier start (e.g. during credits).
     */
    public static void startMusic() {
        // --- Music Settings ---
        final String MP3_PATH;
        final String MUSIC_PLAYER;

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Windows
            MP3_PATH = "C:\\user.mp3";
            MUSIC_PLAYER = "cmd.exe";
        } else {
            // macOS
            MP3_PATH = "/Users/gulfemkomurcu/Desktop/outro.mp3";
            MUSIC_PLAYER = "afplay";
        }

        // 1. Start Music Process
        startMusicProcess(MUSIC_PLAYER, MP3_PATH);

        // 2. Setup Shutdown Hook (Stops music if program is closed with Ctrl+C)
        setupShutdownHook();
    }

    /**
     * Internal method to launch the process.
     */
    private static void startMusicProcess(String player, String path) {
        new Thread(() -> {
            try {
                System.out.println("🎵 Music process starting: " + path);
                String[] musicCommand;
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    musicCommand = new String[] { "cmd.exe", "/c", "start wmplayer \"" + path + "\"" };
                } else {
                    musicCommand = new String[] { player, path };
                }

                musicProcess = new ProcessBuilder(musicCommand).start();

            } catch (IOException e) {
                System.err.println("\nERROR: Music player command failed. Is command (" + player + ") correct?");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sets up a Shutdown Hook to stop the music process when the program terminates
     * (Ctrl+C).
     */
    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n---");
            System.out.println("Shutdown Hook: Creating music process...");

            if (musicProcess != null && musicProcess.isAlive()) {
                musicProcess.destroyForcibly();
                try {
                    musicProcess.waitFor(5, TimeUnit.SECONDS);
                    System.out.println("Music stopped successfully.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }));
    }
}
