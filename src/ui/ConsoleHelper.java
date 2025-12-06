package ui;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class for console UI interactions and formatting.
 * 
 * <p>
 * Provides methods for displaying formatted output with ANSI color codes, user
 * input handling,
 * and dynamic table generation. Supports validation for various input types
 * including email,
 * phone numbers, dates, and custom string validation. Includes ASCII art and
 * themed UI components
 * for menu systems and data visualization.
 * </p>
 * 
 * <p>
 * <strong>Features:</strong>
 * <ul>
 * <li>Color-coded console output with ANSI codes</li>
 * <li>Menu system with formatted headers, options, and footers</li>
 * <li>Dynamic table rendering with automatic column width calculation</li>
 * <li>Input validation for email, phone, date, and numeric values</li>
 * <li>Animated horizontal bar charts for data visualization</li>
 * <li>Screen management (clear, pause on input)</li>
 * </ul>
 * </p>
 * 
 * @author Group 10
 * @version 1.0
 */
public class ConsoleHelper {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    private Scanner scanner;

    public ConsoleHelper() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Clears the console screen by printing 50 newlines.
     */
    public void clearScreen() {
        System.out.print("\n".repeat(50));
    }

    /**
     * Waits for user to press ENTER before continuing.
     * 
     * <p>
     * Displays a yellow prompt and reads input from user.
     * </p>
     */
    public void pressEnterToContinue() {
        System.out.println(YELLOW + "\n➜ Press ENTER to return to menu..." + RESET);
        scanner.nextLine();
    }

    /**
     * Prints ASCII art logo for Contact Manager application.
     * 
     * <p>
     * Displays the application title in cyan bold text with decorative characters.
     * </p>
     */
    public void printAsciiArt() {
        System.out.println(CYAN + BOLD);
        System.out.println("   ______            __             __     __  ___                                   ");
        System.out.println("  / ____/___  ____  / /_____ ______/ /_   /  |/  /___ _____  ____ _____ ____  _____");
        System.out.println(" / /   / __ \\/ __ \\/ __/ __ `/ ___/ __/  / /|_/ / __ `/ __ \\/ __ `/ __ `/ _ \\/ ___/");
        System.out.println("/ /___/ /_/ / / / / /_/ /_/ / /__/ /_   / /  / / /_/ / / / / /_/ / /_/ /  __/ /    ");
        System.out.println(
                "\\____/\\____/_/ /_/\\__/\\__,_/\\___/\\__/  /_/  /_/\\__,_/_/ /_/\\__,_/\\__, /\\___/_/     ");
        System.out.println("                                                                /____/                 ");
        System.out.println(RESET);
    }

    /**
     * Prints a formatted title in a box with cyan borders.
     * 
     * @param title The title text to display (will be converted to uppercase).
     */
    public void printTitle(String title) {
        String border = "═".repeat(title.length() + 6);
        System.out.println(CYAN + "\n╔" + border + "╗");
        System.out.println("║   " + WHITE + BOLD + title.toUpperCase() + CYAN + "   ║");
        System.out.println("╚" + border + "╝" + RESET);
    }

    /**
     * Prints a section header with purple decorative line.
     * 
     * @param title The section title (will be converted to uppercase).
     */
    public void printSectionHeader(String title) {
        System.out.println(
                "\n" + PURPLE + "── " + BOLD + title.toUpperCase() + RESET + PURPLE + " " + "─".repeat(40) + RESET);
    }

    /**
     * Prints an error message in red with error symbol.
     * 
     * @param message The error message to display.
     */
    public void printError(String message) {
        System.out.println(RED + "✖ ERROR: " + message + RESET);
    }

    /**
     * Prints a success message in green with checkmark symbol.
     * 
     * @param message The success message to display.
     */
    public void printSuccess(String message) {
        System.out.println(GREEN + "✔ SUCCESS: " + message + RESET);
    }

    /**
     * Prints an info message in blue with info symbol.
     * 
     * @param message The informational message to display.
     */
    public void printInfo(String message) {
        System.out.println(BLUE + "ℹ INFO: " + message + RESET);
    }

    /**
     * Prints a warning message in yellow with warning symbol.
     * 
     * @param message The warning message to display.
     */
    public void printWarning(String message) {
        System.out.println(YELLOW + "⚠ WARNING: " + message + RESET);
    }

    /**
     * Prints a menu header with title and decorative border.
     * 
     * @param title The menu title.
     */
    public void printMenuHeader(String title) {
        int dashCount = 46 - title.length();
        if (dashCount < 0)
            dashCount = 0;
        System.out
                .println(PURPLE + "\n┌── " + BOLD + title + RESET + PURPLE + " " + "─".repeat(dashCount) + "┐" + RESET);
    }

    /**
     * Prints a single menu option with number and description.
     * 
     * @param number      The menu option number.
     * @param description The description of the menu option.
     */
    public void printMenuOption(int number, String description) {
        int padding = (number < 10) ? 45 : 44;
        System.out.printf(PURPLE + "│ " + CYAN + "[%d]" + RESET + " %-" + padding + "s" + PURPLE + "│%n" + RESET,
                number, description);
    }

    /**
     * Prints the closing border of a menu.
     */
    public void printMenuFooter() {
        System.out.println(PURPLE + "└" + "─".repeat(50) + "┘" + RESET);
    }

    /**
     * Prints a formatted table with headers and data rows.
     * 
     * <p>
     * Automatically calculates column widths based on header and data content.
     * Uses box-drawing characters for borders. Data must match header count.
     * </p>
     * 
     * @param headers Array of column header names.
     * @param data    List of rows, where each row is a String array matching header
     *                count.
     */
    public void printTable(String[] headers, List<String[]> data) {
        if (data.isEmpty()) {
            printInfo("No data available to display.");
            return;
        }

        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : data) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] == null)
                    row[i] = "-";
                if (row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        StringBuilder separator = new StringBuilder("┼");
        for (int w : colWidths)
            separator.append("─".repeat(w + 2)).append("┼");

        String topBorder = separator.toString().replace("┼", "┬");
        String midBorder = separator.toString();
        String botBorder = separator.toString().replace("┼", "┴");

        System.out.println(YELLOW + topBorder.substring(0, 1).replace("┬", "┌") +
                topBorder.substring(1, topBorder.length() - 1) +
                topBorder.substring(topBorder.length() - 1).replace("┬", "┐") + RESET);

        System.out.print(YELLOW + "│");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" " + WHITE + BOLD + "%-" + colWidths[i] + "s" + YELLOW + " │", headers[i].toUpperCase());
        }
        System.out.println(RESET);

        System.out.println(YELLOW + midBorder.substring(0, 1).replace("┼", "├") +
                midBorder.substring(1, midBorder.length() - 1) +
                midBorder.substring(midBorder.length() - 1).replace("┼", "┤") + RESET);

        for (String[] row : data) {
            System.out.print(YELLOW + "│" + RESET);
            for (int i = 0; i < row.length; i++) {
                System.out.printf(" %-" + colWidths[i] + "s " + YELLOW + "│" + RESET, row[i]);
            }
            System.out.println();
        }

        System.out.println(YELLOW + botBorder.substring(0, 1).replace("┴", "└") +
                botBorder.substring(1, botBorder.length() - 1) +
                botBorder.substring(botBorder.length() - 1).replace("┴", "┘") + RESET);
    }

    /**
     * Reads a string input from the user.
     * 
     * @param prompt The prompt to display.
     * @return The trimmed user input.
     */
    public String readString(String prompt) {
        System.out.print(YELLOW + "➜ " + prompt + ": " + RESET);
        return scanner.nextLine().trim();
    }

    /**
     * Reads an integer from the user with validation.
     * 
     * <p>
     * Continuously prompts until valid integer is entered.
     * </p>
     * 
     * @param prompt The prompt to display.
     * @return The parsed integer value.
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(YELLOW + "➜ " + prompt + ": " + RESET);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Reads a date from the user with validation.
     * 
     * <p>
     * Accepts dates in DD-MM-YYYY format. Rejects future dates.
     * Returns null if input is empty (optional field).
     * </p>
     * 
     * @param prompt The prompt to display.
     * @return A Date object, or null if input is empty.
     */
    public Date readDate(String prompt) {
        while (true) {
            System.out.print(YELLOW + "➜ " + prompt + " (DD-MM-YYYY): " + RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                return null;
            try {
                // Parse using d-M-uuuu pattern which handles both 05-12-2025 and 5-12-2025
                // We use 'uuuu' for year in STRICT mode instead of 'yyyy'
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("d-M-uuuu")
                        .withResolverStyle(java.time.format.ResolverStyle.STRICT);
                java.time.LocalDate localDate = java.time.LocalDate.parse(input, formatter);

                if (localDate.isAfter(java.time.LocalDate.now())) {
                    printError("Date cannot be in the future.");
                    continue;
                }

                return Date.valueOf(localDate);
            } catch (java.time.format.DateTimeParseException e) {
                printError("Invalid date. Use DD-MM-YYYY (e.g., 05-12-2025 or 5-12-2025).");
            } catch (IllegalArgumentException e) {
                printError("Invalid date format.");
            }
        }
    }

    /**
     * Reads a required string from the user (cannot be empty).
     * 
     * <p>
     * Repeatedly prompts until non-empty input is provided.
     * </p>
     * 
     * @param prompt The prompt to display.
     * @return The trimmed user input (guaranteed non-empty).
     */
    public String readRequiredString(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (!input.isEmpty())
                return input;
            printError("This field cannot be empty.");
        }
    }

    /**
     * Reads and validates an email address.
     * 
     * <p>
     * Validates email format against standard pattern:
     * alphanumeric._%+-@domain.TLD.
     * Rejects emails with spaces. Optionally allows empty input.
     * </p>
     * 
     * @param prompt   The prompt to display.
     * @param required If true, empty input is rejected. If false, returns null on
     *                 empty input.
     * @return A valid email address, or null if optional and empty.
     */
    public String readEmail(String prompt, boolean required) {
        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) {
                if (!required)
                    return null;
                printError("Email is required.");
                continue;
            }

            if (input.contains(" ")) {
                printError("Email cannot contain spaces.");
                continue;
            }

            if (input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                return input;
            }
            printError("Invalid email format. Please try again.");
        }
    }

    /**
     * Reads and validates a phone number.
     * 
     * <p>
     * Accepts format: optional +, digits, spaces, and dashes (7-20 characters).
     * Optionally allows empty input.
     * </p>
     * 
     * @param prompt   The prompt to display.
     * @param required If true, empty input is rejected. If false, returns null on
     *                 empty input.
     * @return A valid phone number, or null if optional and empty.
     */
    public String readPhone(String prompt, boolean required) {
        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) {
                if (!required)
                    return null;
                printError("Phone number is required.");
                continue;
            }
            if (input.matches("^[+]?[0-9\\s\\-]{7,20}$")) {
                return input;
            }
            printError("Invalid phone number format. Use digits, spaces, or dashes.");
        }
    }

    /**
     * Reads gender input from the user.
     * 
     * <p>
     * Accepts 'M' (Male) or 'F' (Female), case-insensitive.
     * Returns null if input is empty.
     * </p>
     * 
     * @param prompt The prompt to display.
     * @return "M" for male, "F" for female, or null if empty.
     */
    public String readGender(String prompt) {
        while (true) {
            String input = readString(prompt + " (M/F)");
            if (input.isEmpty()) {
                return null;
            }
            if (input.equalsIgnoreCase("M")) {
                return "M";
            }
            if (input.equalsIgnoreCase("F")) {
                return "F";
            }
            printError("Invalid gender. Please enter 'M' or 'F'.");
        }
    }

    /**
     * Prints an animated horizontal bar chart for data visualization.
     * 
     * <p>
     * Displays data sorted by value in descending order with percentage labels.
     * Uses rotating color scheme and block characters for visual appeal.
     * Useful for statistics and distribution analysis.
     * </p>
     * 
     * @param title The chart title (section header).
     * @param data  A map of labels to integer values to visualize.
     */
    public void printAnimatedHorizontalBarChart(String title, java.util.Map<String, Integer> data) {
        if (data.isEmpty()) {
            printInfo("No data to visualize.");
            return;
        }

        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        int maxKeyLength = data.keySet().stream().mapToInt(String::length).max().orElse(10);

        printSectionHeader(title);

        java.util.List<java.util.Map.Entry<String, Integer>> sortedList = new java.util.ArrayList<>(data.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        String[] colors = { CYAN, GREEN, YELLOW, PURPLE, BLUE };
        int colorIdx = 0;

        for (java.util.Map.Entry<String, Integer> entry : sortedList) {
            String label = entry.getKey();
            int value = entry.getValue();

            double percentage = (total > 0) ? ((double) value / total) * 100 : 0;

            int barLength = (int) ((percentage * 40) / 100);
            if (barLength == 0 && value > 0)
                barLength = 1;

            String color = colors[colorIdx % colors.length];

            // 1. Print label and separator
            System.out.printf(WHITE + "%" + maxKeyLength + "s " + YELLOW + "│ " + color, label);

            // 2. Print bar growing char by char
            for (int i = 0; i < barLength; i++) {
                System.out.print("█");
                try {
                    Thread.sleep(50); // growing speed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // 3. Fill remaining space
            System.out.print(" ".repeat(40 - barLength));

            // 4. Print value and newline
            System.out.printf(WHITE + " %d (%%%.1f)%n" + RESET, value, percentage);

            colorIdx++;
        }
        System.out.println();
    }

    // --- INTRO METOTLARI (Aşağıyı değiştirmene gerek yok) ---
    public static void animateIntro() {
        System.out.println(CYAN);
        try {
            // --- 1. KISIM: DERS VE PROJE ADI ---
            String[] header = {
                    "   ______ __  __ ____  ______    _____ __ __ _____ ",
                    "  / ____//  |/  // __ \\/ ____/   |__  // // /|__  /",
                    " / /    / /|_/ // /_/ / __/       /_ </ // /_ /_ < ",
                    "/ /___ / /  / // ____// /___    ___/ /__  __/__/ / ",
                    "\\____//_/  /_//_/    /_____/   /____/  /_/ /____/  ",
                    "                                                   ",
                    "    ____  ____  ____       __ ______ ______ ______   ___ ",
                    "   / __ \\/ __ \\/ __ \\     / // ____// ____//_  __/  |__ \\",
                    "  / /_/ / /_/ / / / /__  / // __/  / /      / /     __/ /",
                    " / ____/ _, _/ /_/ // /_/ // /___ / /___   / /     / __/ ",
                    "/_/   /_/ |_|\\____/ \\____//_____/ \\____/  /_/     /____/ "
            };

            // --- 2. KISIM: GRUP ADI ---
            String[] group = {
                    "   ______ ____   ____  __  __ ____      ___   ____ ",
                    "  / ____// __ \\ / __ \\/ / / // __ \\    <  /  / __ \\",
                    " / / __ / /_/ // / / // / / // /_/ /    / /  / / / /",
                    "/ /_/ // _, _// /_/ // /_/ // ____/    / /  / /_/ / ",
                    "\\____//_/ |_| \\____/ \\____//_/        /_/   \\____/  "
            };

            System.out.print("\n\n");
            printWithDelay(header, 200);

            System.out.println("\n" + "=".repeat(60) + "\n");
            printWithDelay(group, 200);

            System.out.println("\n" + "=".repeat(60));
            System.out.println("            D E V E L O P E R   T E A M");
            System.out.println("=".repeat(60) + "\n");

            Thread.sleep(600);

            // --- 3. KISIM: İSİMLER (ASCII) ---

            // GOKDENIZ DEMIRCIOGLU
            printNameBlock(new String[] {
                    "   ______ ____  __ __ ____  _______   __ __ ____ ",
                    "  / ____// __ \\/ //_// __ \\/ ____/ | / // //_  /",
                    " / / __ / / / / ,<  / / / / __/ /  |/ // /  / /  ",
                    "/ /_/ // /_/ / /| |/ /_/ / /___/ /|  // /  / /__ ",
                    "\\____/ \\____/_/ |_/_____/_____/_/ |_//_/  /____/ ",
                    "    ____  _______ __  ___ __ ____  ______ __ ____  ______ __   __ __ ",
                    "   / __ \\/ ____/  |/  // //_// __ \\/ ____// // __ \\/ ____// /  / // / ",
                    "  / / / / __/ / /|_/ // /   / /_/ / /    / // / / / / __ / /  / // /  ",
                    " / /_/ / /___/ /  / // /   / _, _/ /___ / // /_/ / /_/ // /__/ //_/   ",
                    "/_____/_____/_/  /_//_/   /_/ |_|\\____//_/ \\____/\\____//_____/__(_)   "
            });

            // KEREM IRFANOGLU
            printNameBlock(new String[] {
                    "    __ __ ______ ____  ______ __  __",
                    "   / //_// ____// __ \\/ ____//  |/  /",
                    "  / ,<  / __/  / /_/ / __/  / /|_/ / ",
                    " / /| |/ /___ / _, _/ /___ / /  / /  ",
                    "/_/ |_/_____//_/ |_/_____//_/  /_/   ",
                    "    ____  ____  ______ ___     _   __ ____  ______ __   __ __ ",
                    "   /  _/ / __ \\/ ____//   |   / | / // __ \\/ ____// /  / // / ",
                    "   / /  / /_/ / /_   / /| |  /  |/ // / / / / __ / /  / // /  ",
                    " _/ /  / _, _/ __/  / ___ | / /|  // /_/ / /_/ // /__/ //_/   ",
                    "/___/ /_/ |_/_/    /_/  |_|/_/ |_/ \\____/\\____//_____/__(_)   "
            });

            // TAHA SOGUT
            printNameBlock(new String[] {
                    "  ______ ___     __  __ ___ ",
                    " /_  __//   |   / / / //   |",
                    "  / /  / /| |  / /_/ // /| |",
                    " / /  / ___ | / __  // ___ |",
                    "/_/  /_/  |_|/_/ /_//_/  |_|",
                    "   _____ ____   ______ __  __ ______",
                    "  / ___// __ \\ / ____// / / //_  __/",
                    "  \\__ \\/ / / // / __ / / / /  / /   ",
                    " ___/ // /_/ // /_/ // /_/ /  / /   ",
                    "/____/ \\____/ \\____/ \\____/  /_/    "
            });

            // AYSENUR GULFEM KOMURCU
            printNameBlock(new String[] {
                    "    ___ __  __ _____ ______ _   __ __  __ ____ ",
                    "   /   |\\ \\/ // ___// ____// | / // / / // __ \\",
                    "  / /| | \\  / \\__ \\/ __/  /  |/ // / / // /_/ /",
                    " / ___ | / / ___/ // /___/ /|  // /_/ // _, _/ ",
                    "/_/  |_|/_/ /____//_____/_/ |_/ \\____//_/ |_|  ",
                    "   ______ __  __ __     ______ ______ __  ___",
                    "  / ____// / / // /    / ____// ____//  |/  /",
                    " / / __ / / / // /    / /_   / __/  / /|_/ / ",
                    "/ /_/ // /_/ // /___ / __/  / /___ / /  / /  ",
                    "\\____/ \\____//_____//_/    /_____//_/  /_/   ",
                    "    __ __ ____  __  __ __  __ ____   ______ __  __",
                    "   / //_// __ \\/  |/ // / / // __ \\ / ____// / / /",
                    "  / ,<  / / / / /|_/ // / / // /_/ // /    / / / /",
                    " / /| |/ /_/ / /  / // /_/ // _, _// /___ / /_/ / ",
                    "/_/ |_|\\____/_/  /_/ \\____//_/ |_| \\____/ \\____/  "
            });

            System.out.println("\n" + "=".repeat(60));
            System.out.println("              S Y S T E M   L O A D I N G . . .");
            System.out.println("=".repeat(60));
            Thread.sleep(1500);
            System.out.println(RESET);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void printWithDelay(String[] lines, int delay) throws InterruptedException {
        for (String line : lines) {
            System.out.println(line);
            Thread.sleep(delay);
        }
    }

    private static void printNameBlock(String[] lines) throws InterruptedException {
        System.out.println();
        for (String line : lines) {
            System.out.println(line);
            Thread.sleep(50);
        }
        Thread.sleep(1500);
    }

    public static void animateOutro() {
        // Start music immediately with the credits
        outro.Eyfel.startMusic();

        System.out.println(CYAN);
        try {
            System.out.println("\n\n" + "=".repeat(60));

            // --- SPECIAL THANKS ---
            String[] specialThanks = {
                    "   _____ ____  _________________    __    ______  __  ___    _   ____ __ _____ ",
                    "  / ___// __ \\/ ____/ ____/  _/   |  / /   /_  __/ / / /   |  / | / // //_// ___/",
                    "  \\__ \\/ /_/ / __/ / /    / // /| | / /     / /   / /_/ /| | /  |/ // ,<   \\__ \\ ",
                    " ___/ / ____/ /___/ /___ / // ___ |/ /___  / /   / __  / ___ |/ /|  // /| | ___/ / ",
                    "/____/_/   /_____/\\____/___/_/  |_/_____/ /_/   /_/ /_/_/  |_/_/ |_/_/ |_|/____/  "
            };
            printWithDelay(specialThanks, 100);

            System.out.println("=".repeat(60) + "\n");
            Thread.sleep(1000);

            // --- ASST. PROF. ---
            // Biraz daha küçük bir font ile unvan
            String[] asstProf = {
                    "    ___   _____ _____ ______      ____  ____  ____  ______",
                    "   /   | / ___// ___//_  __/     / __ \\/ __ \\/ __ \\/ ____/",
                    "  / /| | \\__ \\ \\__ \\  / /       / /_/ / /_/ / / / / /_    ",
                    " / ___ |___/ /___/ / / /       / ____/ _, _/ /_/ / __/    ",
                    "/_/  |_/____//____/ /_/       /_/   /_/ |_|\\____/_/       "
            };
            printWithDelay(asstProf, 100);

            Thread.sleep(500);

            // --- ILKTAN AR ---
            printNameBlock(new String[] {
                    "    ____ __  __ __ ______  ___    _   __     ___    ____ ",
                    "   /  _// / / //_//_  __/ /   |  / | / /    /   |  / __ \\",
                    "   / / / / / ,<    / /   / /| | /  |/ /    / /| | / /_/ /",
                    " _/ / / /___/| |  / /   / ___ |/ /|  /    / ___ |/ _, _/ ",
                    "/___//_____/_| |_/_/   /_/  |_/_/ |_/    /_/  |_/_/ |_|  "
            });

            Thread.sleep(1000);

            // --- AND ---
            String[] andText = {
                    "             ___    _   __ ____ ",
                    "            /   |  / | / // __ \\",
                    "           / /| | /  |/ // / / /",
                    "          / ___ |/ /|  // /_/ / ",
                    "         /_/  |_/_/ |_//_____/  "
            };
            printWithDelay(andText, 150);

            Thread.sleep(1000);

            // --- FATIH GOLGE ---
            printNameBlock(new String[] {
                    "    ______ ___  ______ ____ __  __   ______ ______ __    ______ ______",
                    "   / ____//   |/_  __//  _// / / /  / ____// __  // /   / ____// ____/",
                    "  / /_   / /| | / /   / / / /_/ /  / / __ / / / // /   / / __ / __/   ",
                    " / __/  / ___ |/ /  _/ / / __  /  / /_/ // /_/ // /___/ /_/ // /___   ",
                    "/_/    /_/  |_/_/  /___//_/ /_/   \\____/ \\____//_____/\\____//_____/   "
            });

            Thread.sleep(1500);

            // --- GOODBYE ---
            String[] goodbye = {
                    "   ______ ____  ____  ____  ____  __  __ ______",
                    "  / ____// __ \\/ __ \\/ __ \\/ __ ) \\ \\/ // ____/",
                    " / / __ / / / / / / / / / / __  |  \\  // __/   ",
                    "/ /_/ // /_/ / /_/ / /_/ / /_/ /   / // /___   ",
                    "\\____/ \\____/\\____/_____/_____/   /_//_____/   "
            };

            System.out.println("\n" + "=".repeat(60) + "\n");
            printWithDelay(goodbye, 200);

            System.out.println("\n" + "=".repeat(60));
            Thread.sleep(1000);
            System.out.println(RESET);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}