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
        // Basit boşluk bırakma yöntemi (IDE konsolları için en güvenlisi)
        System.out.print("\n".repeat(50));
    }

    public void pressEnterToContinue() {
        System.out.println(YELLOW + "\n➜ Press ENTER to return to menu..." + RESET);
        scanner.nextLine();
    }

    // --- SANATSAL LOGO ---
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

    // --- BAŞLIKLAR VE MESAJLAR ---
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

    // --- TABLO OLUŞTURUCU ---
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
            } catch (Exception e) {
                printError("Invalid date. Use DD-MM-YYYY.");
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
            if (input.matches("^[+]?[0-9\\s\\-]{7,20}$")) {
                return input;
            }
            printError("Invalid phone number format.");
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
            printInfo("No data to visualize for: " + title);
            return;
        }

        // Toplam değeri hesapla (Yüzde hesabı için)
        // Eğer veri tipi dağılım değilse (örn: toplam sayı sayma) en büyük değere göre
        // scale edebiliriz.
        // Ancak burada yüzde gösterimi için toplamı alıyoruz.
        int total = data.values().stream().mapToInt(Integer::intValue).sum();

        // Eğer total 0 ise (örn: tüm değerler 0) hata vermemesi için 1 yapalım
        if (total == 0)
            total = 1;

        // En uzun etiketi bul (Hizalama için)
        int maxKeyLength = data.keySet().stream().mapToInt(String::length).max().orElse(10);

        printSectionHeader(title);

        // Datayı value'ya göre sort edelim (Büyükten küçüğe şık durur)
        java.util.List<java.util.Map.Entry<String, Integer>> sortedList = new java.util.ArrayList<>(data.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        String[] colors = { CYAN, GREEN, YELLOW, PURPLE, BLUE, RED };
        int colorIdx = 0;

        for (java.util.Map.Entry<String, Integer> entry : sortedList) {
            String label = entry.getKey();
            int value = entry.getValue();

            // Yüzde hesabı
            double percentage = ((double) value / total) * 100;

            // Çubuk uzunluğu (Maksimum 40 karakter)
            // Ancak "Opsiyonel Alanlar" grafiğinde total mantığı biraz farklı işleyebilir
            // (Çünkü her kişi her alana sahip olabilir).
            // Yine de görsel tutarlılık için bu formül iş görür.
            int barLength = (int) ((percentage * 40) / 100);

            // Eğer değer var ama bar hesaplamada 0 çıkıyorsa en az 1 karakter göster
            if (barLength == 0 && value > 0)
                barLength = 1;

            String color = colors[colorIdx % colors.length];

            // 1. Etiketi yazdır
            System.out.printf(WHITE + "%" + maxKeyLength + "s " + YELLOW + "│ " + color, label);

            // 2. Bar'ı animasyonlu yazdır (Karakter karakter)
            for (int i = 0; i < barLength; i++) {
                System.out.print("█");
                try {
                    Thread.sleep(10); // Animasyon hızı (ms)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // 3. Boşlukları tamamla (Hizalama için opsiyonel, ama temiz durur)
            int remainingSpace = 40 - barLength;
            System.out.print(" ".repeat(remainingSpace));

            // 4. Değeri ve yüzdeyi yazdır
            System.out.printf(WHITE + " %d (%%%.1f)%n" + RESET, value, percentage);

            colorIdx++;
        }
        System.out.println();
    }
}