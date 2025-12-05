package ui;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleHelper {
    // Renk Kodları
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

    // --- EKRAN KONTROLÜ ---
    public void clearScreen() {
        System.out.print("\n".repeat(50));
    }

    public void pressEnterToContinue() {
        System.out.println(YELLOW + "\n➜ Press ENTER to return to menu..." + RESET);
        scanner.nextLine();
    }

    // --- SANATSAL LOGO (CONTACT MANAGER) ---
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

    // --- KUTULU BAŞLIKLAR VE MESAJLAR ---
    public void printTitle(String title) {
        String border = "═".repeat(title.length() + 6);
        System.out.println(CYAN + "\n╔" + border + "╗");
        System.out.println("║   " + WHITE + BOLD + title.toUpperCase() + CYAN + "   ║");
        System.out.println("╚" + border + "╝" + RESET);
    }

    public void printSectionHeader(String title) {
        System.out.println(
                "\n" + PURPLE + "── " + BOLD + title.toUpperCase() + RESET + PURPLE + " " + "─".repeat(40) + RESET);
    }

    public void printError(String message) {
        System.out.println(RED + "✖ ERROR: " + message + RESET);
    }

    public void printSuccess(String message) {
        System.out.println(GREEN + "✔ SUCCESS: " + message + RESET);
    }

    public void printInfo(String message) {
        System.out.println(BLUE + "ℹ INFO: " + message + RESET);
    }

    public void printWarning(String message) {
        System.out.println(YELLOW + "⚠ WARNING: " + message + RESET);
    }

    // --- MENÜ SİSTEMİ ---
    public void printMenuHeader(String title) {
        int dashCount = 46 - title.length();
        if (dashCount < 0)
            dashCount = 0;
        System.out
                .println(PURPLE + "\n┌── " + BOLD + title + RESET + PURPLE + " " + "─".repeat(dashCount) + "┐" + RESET);
    }

    public void printMenuOption(int number, String description) {
        int padding = (number < 10) ? 45 : 44;
        System.out.printf(PURPLE + "│ " + CYAN + "[%d]" + RESET + " %-" + padding + "s" + PURPLE + "│%n" + RESET,
                number, description);
    }

    public void printMenuFooter() {
        System.out.println(PURPLE + "└" + "─".repeat(50) + "┘" + RESET);
    }

    // --- DİNAMİK TABLO OLUŞTURUCU ---
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

    // --- INPUT METOTLARI ---
    public String readString(String prompt) {
        System.out.print(YELLOW + "➜ " + prompt + ": " + RESET);
        return scanner.nextLine().trim();
    }

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

    public String readRequiredString(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (!input.isEmpty())
                return input;
            printError("This field cannot be empty.");
        }
    }

    public String readEmail(String prompt, boolean required) {
        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) {
                if (!required)
                    return null;
                printError("Email is required.");
                continue;
            }

            // Check for spaces
            if (input.contains(" ")) {
                printError("Email cannot contain spaces.");
                continue;
            }

            // More realistic email regex:
            // - Local part: allows alphanumeric, dots, underscores, plus, hyphens
            // - @ symbol
            // - Domain part: allows alphanumeric, dots, hyphens
            // - TLD: at least 2 letters
            if (input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                return input;
            }
            printError("Invalid email format. Please try again.");
        }
    }

    public String readPhone(String prompt, boolean required) {
        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) {
                if (!required)
                    return null;
                printError("Phone number is required.");
                continue;
            }
            // Regex: Optional +, digits, spaces, dashes. Min 7 chars.
            if (input.matches("^[+]?[0-9\\s\\-]{7,20}$")) {
                return input;
            }
            printError("Invalid phone number format. Use digits, spaces, or dashes.");
        }
    }

    public String readGender(String prompt) {
        while (true) {
            String input = readString(prompt + " (K/E)");
            if (input.isEmpty()) {
                return null;
            }
            if (input.equalsIgnoreCase("K")) {
                return "K";
            }
            if (input.equalsIgnoreCase("E")) {
                return "E";
            }
            printError("Invalid gender. Please enter 'K' or 'E'.");
        }
    }

    // --- GRAFİK ÇİZİCİ
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

            String bar = "█".repeat(barLength);
            String color = colors[colorIdx % colors.length];

            System.out.printf(
                    WHITE + "%" + maxKeyLength + "s " + YELLOW + "│ " + color + "%-40s " + WHITE + "%d (%%%.1f)%n"
                            + RESET,
                    label, bar, value, percentage);

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
        Thread.sleep(600);
    }
}