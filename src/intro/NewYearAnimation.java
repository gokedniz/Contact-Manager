package intro;

import java.io.IOException;
import java.util.Random;


/**
 * Class for displaying a New Year animation in the console.
 * 
 * <p>
 * The animation features a Christmas tree, presents, and falling snow using
 * ANSI escape codes for colors.
 * Users can skip the animation by pressing 'q' followed by Enter.
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class NewYearAnimation {

    /**
     * ANSI escape codes for console colors.
     */
    public static final String RESET = "\u001B[0m";
    /**
     * ANSI escape code for green text.
     */
    public static final String GREEN = "\u001B[32m";
    /**
     * ANSI escape code for red text.
     */
    public static final String RED = "\u001B[31m";
    /**
     * ANSI escape code for yellow text.
     */
    public static final String YELLOW = "\u001B[33m";
    /**
     * ANSI escape code for blue text.
     */
    public static final String BLUE = "\u001B[34m";
    /**
     * ANSI escape code for magenta text.
     */
    public static final String MAGENTA = "\u001B[35m";
    /**
     * ANSI escape code for cyan text.
     */
    public static final String CYAN = "\u001B[36m";
    /**
     * ANSI escape code for dark brown text.
     */
    public static final String DARK_BROWN = "\u001B[38;5;94m";

    /**
     * Clears the console screen.
     */
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

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
     * Runs the New Year animation sequence.
     */
    public static void animate() {
        Random rand = new Random();
        int width = 80;

        // ------------------------
        // 1) TREE + GIFTS ASCII
        // ------------------------
        String[] art = new String[] {
                "",
                "",
                "",

                YELLOW + "               *",
                "              ***",
                "          ***********",
                "           *********",
                "          ***********",
                "           **     **",
                "           *       *" + RESET,

                GREEN + "               /\\",
                "              /  \\",
                "             /++++\\",
                "            /  ()  \\",
                "           /  (**)  \\",
                "          /++++++++++\\",
                "         /   (----)   \\",
                "        /   (  ()  )   \\",
                "       /+++++\\  /+++++++\\",
                "      / (**)  \\/  (**)   \\",
                "     /   (    \\/    )     \\",
                "    /++++++++++++++++++++++\\",
                "   /  *  ** *** **  *       \\",
                "  /  ****  ******  ****      \\",
                " /++++++++++\\  **  /++++++++++\\",
                "/   (**)     \\ ** /    (**)    \\",
                "     (  )     \\**/    (  )      ",
                "_________________________________" + RESET,

                DARK_BROWN + "           |||||||||||||",
                "           |||||||||||||",
                "           |||||||||||||" + RESET,

                RED + "   [======]   " + BLUE + "[====]   " + MAGENTA + "[========]   " + CYAN + "[======]",
                RED + "   |  *   |   " + BLUE + "| ** |   " + MAGENTA + "|   *    |   " + CYAN + "| *** |",
                RED + "   | ***  |   " + BLUE + "| ** |   " + MAGENTA + "|  ***   |   " + CYAN + "| *** |",
                RED + "   '------'   " + BLUE + "'----'   " + MAGENTA + "'--------'   " + CYAN + "'------'",

                BLUE + "   [===========]   " + RED + "[=======]   " + CYAN + "[====]   " + MAGENTA + "[==========]",
                BLUE + "   |  ***  *** |   " + RED + "|  *  * |   " + CYAN + "| ** |   " + MAGENTA + "|  *  **   |",
                BLUE + "   |  *******  |   " + RED + "| ***** |   " + CYAN + "| ** |   " + MAGENTA + "|  ******  |",
                BLUE + "   '-----------'   " + RED + "'-------'   " + CYAN + "'----'   " + MAGENTA + "'----------'"
                        + RESET
        };

        // ------------------------
        // 2) SNOW ASCII (for overlay)
        // ------------------------
        String[] snow = new String[] {
                "",
                "",
                "",
                "",
                "               ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ ⠀⠀⠀⠀⢰⠒⠒⢲⠆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "                             ⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "                         ⠀⠀⣸⠓⢦⣀⠀⠀⢸⠀⠀⢸⠀⠀⠀⣠⠴⢻⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "                      ⣀⡀⠀⠀⠈⢧⣀⠀⠈⠓⢦⣸⠀⠀⢸⣀⠴⠊⠁⠀⣠⠿⠀⠀⣀⡀⠀⠀⠀⠀⠀⠀⠀",
                "              ⠀⠀⠀⠀⠀⠀⠀⠀⡏⠉⠹⡀⠀⠀⠈⠑⠦⡀⠀⠘⠂⠀⠈⠀⠀⣠⠔⠋⠀⠀⠀⢸⠉⠉⡇⠀⠀⠀⠀⠀⠀⠀",
                "               ⢀⣴⣄⡀⠀⠀⠀⢀⠀⠀⡇⠀⠀⠀⠀⠀⠈⠳⢤⡀⠀⢠⠴⠋⠀⠀⠀⠀⠀⠀⣿⠀⢸⡇⠀⠀⠀⢀⡴⣆⠀",
                "              ⢠⡞⠀⠀⠙⠲⢄⣀⣼⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⢸⡇⣀⡴⠚⠉⠀⠈⣧",
                "              ⠈⠙⠲⢄⡀⠀⠀⠈⠛⠂⠀⣿⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⠘⠋⠁⠀⠀⣀⡤⠚⠉",
                "              ⠀⠀⠀⠀⠉⠓⣦⡄⠀⠀⠀⠛⠦⣄⠀⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⢀⣠⠞⠃⠀⠀⠀⢠⡔⠋⠁⠀⠀⠀",
                "              ⠀⠀⣀⡤⠖⠋⠁⠀⣀⡤⣄⡀⠀⠈⠙⠢⣄⡀⠀⢸⠀⠀⢸⠀⠀⢀⡤⠖⠉⠀⠀⣀⡤⣄⡀⠀⠈⠛⠲⢤⣀⠀",
                "              ⠐⢿⡁⠀⣀⡤⠖⠋⠁⠀⠀⠉⠓⢦⣀⠀⠀⠉⠲⢼⡀⠀⢸⠶⠚⠁⠀⢀⣠⠴⠊⠁⠀⠀⠈⠑⠢⢄⡀⠀⢨⡟",
                "       ⠀      ⠀⠛⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠲⣄⡀⠐⠀⠀⠀⠀⣀⡤⠖⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠋⠀",
                "              ⠀⣀⡀ ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠔⠋⠁⠀⠀⠀⠈⠛⠦⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "              ⢀⣼⠋⠉⠓⠦⣄⡀⠀⠀⠀⠀⣀⠤⠚⠉⠀⠀⣠⢴⠂⠀⢸⣤⣀⠀⠈⠙⠲⢤⡀⠀⠀⠀⠀⣀⡤⠔⠊⠉⠙⣆",
                "              ⠀⠉⠓⠦⣄⡀⠀⠉⠓⠦⠖⠋⠁⠀⢀⣠⠖⠋⠀⢸⠀⠀⢸⠀⠈⠓⠦⣄⠀⠀⠈⠓⠦⠖⠋⠁⠀⢀⣠⠔⠚⠉",
                "                 ⠀⠀⣉⡷⠂⠀⠀⠀⣀⡤⠞⠋⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠈⠙⠦⣄⡀⠀⠀⠀⠶⣾⡉⠀⠀⠀⠀",
                "              ⠀⢀⣠⠔⠋⠁⠀⠀⣠⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠈⡇⠀⢠⡀⠀⠀⠉⠓⠦⣀⠀",
                "              ⠸⣏⠀⠀⠀⣠⠴⠊⢻⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⡇⠀⢸⡏⠓⢦⣀⠀⠀⢀⡿",
                "              ⠀⠘⢧⠖⠋⠀⠀⠀⠘⠀⠀⡇⠀⠀⠀⠀⠀⠀⣠⡼⠃⠀⠸⢦⡀⠀⠀⠀⠀⠀⠀⣿⠀⢸⡇⠀⠀⠈⠑⠦⡞⠁",
                "              ⠀⠀⠀⠀⠀⠀⠀⠀⡆⠀⣠⡇⠀⠀⠀⣀⡴⠚⠁⠀⡄⠀⢀⠀⠉⠲⢄⡀⠀⠀⠀⢹⡀⠈⡇⠀⠀⠀⠀⠀⠀⠀",
                "                      ⠛⠋⠉⠀⠀⡴⠚⠁⠀⣀⠴⢺⠀⠀⢸⠓⢤⡀⠀⠉⠳⣤⠀⠈⠉⠙⠃⠀⠀⠀⠀⠀⠀⠀",
                "               ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢱⣤⡴⠊⠁⠀⢸⠀⠀⢸⠀⠀⠉⠳⢤⣰⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "                       ⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⢸⠀⠀⢸⠀⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "       ⠀       ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣀⣀⣸⠆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀",
                "        "
        };

        // ------------------------
        // 1) TREE SECTION: 17 seconds
        // ------------------------
        long stopTime = System.currentTimeMillis() + 17_000L;
        while (System.currentTimeMillis() < stopTime) {
            if (checkSkip())
                return; // Check skip
            clear();
            for (String line : art) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == ' ' && rand.nextDouble() < 0.15)
                        sb.append('*');
                    else
                        sb.append(c);
                }
                while (sb.length() < width) {
                    if (rand.nextDouble() < 0.05)
                        sb.append('*');
                    else
                        sb.append(' ');
                }
                System.out.println(sb.toString());
            }
            System.out.println("\n[Press 'q' + Enter to skip]");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // ------------------------
        // 2) TRANSITION: 17 seconds
        // ------------------------
        int transitionSteps = 17;
        int stepDurationMs = 1000;
        String[] colors = new String[] { RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN };

        for (int step = 1; step <= transitionSteps; step++) {
            long frameEnd = System.currentTimeMillis() + stepDurationMs;
            while (System.currentTimeMillis() < frameEnd) {
                if (checkSkip())
                    return; // Check skip
                clear();
                double phase = System.currentTimeMillis() / 120.0;

                for (int r = 3; r < art.length; r++) {
                    String base = art[r].replace(RESET, "");
                    int offset = (int) (Math.round(Math.sin((r * 0.5) + phase) * 6));
                    offset += rand.nextInt(3) - 1;

                    StringBuilder line = new StringBuilder();
                    if (offset > 0)
                        line.append(" ".repeat(offset));

                    String rowColor = colors[(r + step + rand.nextInt(colors.length)) % colors.length];
                    line.append(rowColor).append(base).append(RESET);

                    while (line.length() < width) {
                        double revealProb = step * 0.10;
                        if (rand.nextDouble() < revealProb * 0.02)
                            line.append('*');
                        else
                            line.append(' ');
                    }

                    // Snow overlay
                    int snowRowIndex = r - Math.max(0, (art.length - snow.length) / 2);
                    if (snowRowIndex >= 0 && snowRowIndex < snow.length) {
                        String snowLine = snow[snowRowIndex];
                        StringBuilder over = new StringBuilder();
                        for (int i = 0; i < Math.min(snowLine.length(), width); i++) {
                            char kc = snowLine.charAt(i);
                            if (kc != ' ' && rand.nextDouble() < (step * 0.10))
                                over.append(kc);
                            else
                                over.append(' ');
                        }
                    }

                    System.out.println(line.toString());
                }
                System.out.println("\n[Press 'q' + Enter to skip]");
            }
        }

        // ------------------------
        // 3) FINAL SNOWFLAKE: 2 seconds
        // ------------------------
        clear();
        for (String line : snow)
            System.out.println(line);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
