package outro;

import java.io.IOException;
import java.lang.Process;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for managing the Eyfel Tower outro animation and music.
 * 
 * <p>Provides methods to play a colorful ASCII art animation of the Eyfel Tower
 * accompanied by background music. Supports skipping the animation via user input.
 * </p>
 * 
 * <p>The animation cycles through various color themes including original,
 * Galatasaray, New Year, and Red-White themes.</p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class Eiffel {
    /** Resets the console color to default. */
    public static final String ANSI_RESET = "\u001B[0m";

    /** White color code used for fireworks animation. */
    public static final String FW_WHITE = "\u001B[97m";
    
    /** Yellow color code used for fireworks animation. */
    public static final String FW_YELLOW = "\u001B[93m";
    
    /** Orange color code used for fireworks animation. */
    public static final String FW_ORANGE = "\u001B[38;5;208m";
    
    /** Red color code used for fireworks animation. */
    public static final String FW_RED = "\u001B[31m";
    
    /** Purple color code used for fireworks animation. */
    public static final String FW_PURPLE = "\u001B[35m";

    /** Yellow color code used for the Galatasaray (GS) theme elements. */
    public static final String GS_YELLOW = "\u001B[93m";
    
    /** Red color code used for the Galatasaray (GS) theme elements. */
    public static final String GS_RED = "\u001B[31m";

    /** Blue color code for the new year theme. */
    public static final String NEW_BLUE = "\u001B[34m";
    
    /** Red color code for the new year theme. */
    public static final String NEW_RED = "\u001B[31m";
    
    /** White color code for the new year theme. */
    public static final String NEW_WHITE = "\u001B[97m";

    /** Red color code for red/white theme elements. */
    public static final String RW_RED = "\u001B[31m";
    
    /** White color code for red/white theme elements. */
    public static final String RW_WHITE = "\u001B[97m";

    /** ANSI sequence to clear the screen and move cursor to top-left. */
    public static final String CLEAR_SCREEN = "\u001B[2J\u001B[H";
    
    /** Padding string used for centering the ASCII art. */
    private static final String PADDING = "                    "; 

    /** Static variable to manage the background music process. */
    private static Process musicProcess;

    
    /**
     * Eyfel Tower ASCII Art Lines
     */
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
            MP3_PATH = "C:\\outro.mp3";
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
