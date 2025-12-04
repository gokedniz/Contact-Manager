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
        System.out.println("\\____/\\____/_/ /_/\\__/\\__,_/\\___/\\__/  /_/  /_/\\__,_/_/ /_/\\__,_/\\__, /\\___/_/     ");
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
        System.out.println("\n" + PURPLE + "── " + BOLD + title.toUpperCase() + RESET + PURPLE + " " + "─".repeat(40) + RESET);
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
    // 1. BAŞLIK METODU
    public void printMenuHeader(String title) {
        // Çizdirmenin Temel Mantığı(hayır rastgele sayılar yazmadım):
        // Sabitler: ┌── (3) + Boşluk (1) + Başlık (N) + Boşluk (1) + Çizgi (X) + ┐ (1)
        // Toplam Genişlik = 52 olmalı.
        // 3 + 1 + N + 1 + X + 1 = 52
        // 6 + N + X = 52  =>  X = 46 - N
        
        int dashCount = 46 - title.length();
        // Eksiye düşmemesi için güvenlik önlemi
        if (dashCount < 0) dashCount = 0; 
        
        System.out.println(PURPLE + "\n┌── " + BOLD + title + RESET + PURPLE + " " + "─".repeat(dashCount) + "┐" + RESET);
    }

    // 2. SEÇENEK METODU
    public void printMenuOption(int number, String description) {
        // Temel Mantık:
        // Sol Sabitler: │ (1) + Boşluk (1) = 2 Karakter
        // Sağ Sabit: │ (1) Karakter
        // Köşeli Parantez Grubu:
        //   - Tek haneli ise [1] -> 3 Karakter
        //   - Çift haneli ise [10] -> 4 Karakter
        
        // Toplam Genişlik (52) hedefine ulaşmak için metin alanı (pading yani) dinamik olmalı:
        // Tek hane: 52 - (2 sol + 3 sayı + 1 boşluk + 1 sağ) = 45 birim boşluk
        // Çift hane: 52 - (2 sol + 4 sayı + 1 boşluk + 1 sağ) = 44 birim boşluk
        
        int padding = (number < 10) ? 45 : 44;
        
        System.out.printf(PURPLE + "│ " + CYAN + "[%d]" + RESET + " %-" + padding + "s" + PURPLE + "│%n" + RESET, number, description);
    }
    // 3. ALT ÇİZGİ METODU
    public void printMenuFooter() {
        // Temel Mantık:
        // └ (1) + Çizgi (50) + ┘ (1) = 52 Karakter
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
                if (row[i] == null) row[i] = "-";
                if (row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        StringBuilder separator = new StringBuilder("┼");
        for (int w : colWidths) separator.append("─".repeat(w + 2)).append("┼");
        
        String topBorder = separator.toString().replace("┼", "┬");
        String midBorder = separator.toString();
        String botBorder = separator.toString().replace("┼", "┴");

        System.out.println(YELLOW + topBorder.substring(0, 1).replace("┬", "┌") + 
                           topBorder.substring(1, topBorder.length()-1) + 
                           topBorder.substring(topBorder.length()-1).replace("┬", "┐") + RESET);

        System.out.print(YELLOW + "│");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" " + WHITE + BOLD + "%-" + colWidths[i] + "s" + YELLOW + " │", headers[i].toUpperCase());
        }
        System.out.println(RESET);

        System.out.println(YELLOW + midBorder.substring(0, 1).replace("┼", "├") + 
                           midBorder.substring(1, midBorder.length()-1) + 
                           midBorder.substring(midBorder.length()-1).replace("┼", "┤") + RESET);

        for (String[] row : data) {
            System.out.print(YELLOW + "│" + RESET);
            for (int i = 0; i < row.length; i++) {
                System.out.printf(" %-" + colWidths[i] + "s " + YELLOW + "│" + RESET, row[i]);
            }
            System.out.println();
        }

        System.out.println(YELLOW + botBorder.substring(0, 1).replace("┴", "└") + 
                           botBorder.substring(1, botBorder.length()-1) + 
                           botBorder.substring(botBorder.length()-1).replace("┴", "┘") + RESET);
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
            System.out.print(YELLOW + "➜ " + prompt + " (YYYY-MM-DD): " + RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                return null;
            try {
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                printError("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }
    // --- GRAFİK ÇİZİCİ
    public void printHorizontalBarChart(String title, java.util.Map<String, Integer> data) {
        if (data.isEmpty()) {
            printInfo("No data to visualize.");
            return;
        }

        // 1. Toplam sayıyı bul (Yüzde hesabı için)
        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        
        // 2. En uzun domain ismini bul (Hizalama için)
        int maxKeyLength = data.keySet().stream().mapToInt(String::length).max().orElse(10);
        
        printSectionHeader(title);

        // 3. SIRALAMA İŞLEMİ (Sorting): Büyükten küçüğe
        java.util.List<java.util.Map.Entry<String, Integer>> sortedList = new java.util.ArrayList<>(data.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue())); // Value'ya göre Descending sort

        // Renk döngüsü
        String[] colors = {CYAN, GREEN, YELLOW, PURPLE, BLUE};
        int colorIdx = 0;

        for (java.util.Map.Entry<String, Integer> entry : sortedList) {
            String label = entry.getKey();
            int value = entry.getValue();
            
            // Yüzde Hesabı
            double percentage = (total > 0) ? ((double) value / total) * 100 : 0;
            
            // Çubuk Uzunluğu (Max 40 karakter)
            int barLength = (int) ((percentage * 40) / 100);
            if (barLength == 0 && value > 0) barLength = 1; 

            String bar = "█".repeat(barLength);
            String color = colors[colorIdx % colors.length];

            // ÇIKTI FORMATI GÜNCELLENDİ:
            // [Domain     ] │ [Çubuk          ]  7 (%15.2)
            System.out.printf(WHITE + "%" + maxKeyLength + "s " + YELLOW + "│ " + color + "%-40s " + WHITE + "%d (%%%.1f)%n" + RESET, 
                            label, bar, value, percentage);
            
            colorIdx++;
        }
        System.out.println(); 
    }
}