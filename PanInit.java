import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanInit {
    
    public static void main(String[] args) {
        /**
         * open a window to request the path of the directory
        */
        
        String directory = "directorio";
        String visaRegex = "\\b4[0-9]{12}(?:[0-9]{3})?\\b";
        String mastercardRegex = "\\b5[1-5][0-9]{14}\\b";
        String amexRegex = "\\b3[47][0-9]{13}\\b";
        String discoverRegex = "\\b6(?:011|5[0-9]{2})[0-9]{12}\\b";
        String unionPayRegex = "\\b62[0-9]{14,17}\\b";
        
        File dir = new File(directory);
        File[] files = dir.listFiles();
        
        if (files == null) {
            System.err.println("El directorio " + directory + " no existe");
            return;
        }
        
        Map<String, Integer> cardCounts = new HashMap<>();
        cardCounts.put("Visa", 0);
        cardCounts.put("Mastercard", 0);
        cardCounts.put("American Express", 0);
        cardCounts.put("Discover", 0);
        cardCounts.put("Union Pay", 0);
        
        List<String> cardDetails = new ArrayList<>();
        
        for (File file : files) {
            if (file.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineNum = 0;
                    Pattern visaPattern = Pattern.compile(visaRegex);
                    Pattern mastercardPattern = Pattern.compile(mastercardRegex);
                    Pattern amexPattern = Pattern.compile(amexRegex);
                    Pattern discoverPattern = Pattern.compile(discoverRegex);
                    Pattern unionPayPattern = Pattern.compile(unionPayRegex);
                    
                    while ((line = br.readLine()) != null) {
                        lineNum++;
                        
                        Matcher visaMatcher = visaPattern.matcher(line);
                        while (visaMatcher.find()) {
                            String creditCard = visaMatcher.group();
                            if (isValidCreditCardNumber(creditCard)) {
                                cardCounts.put("Visa", cardCounts.get("Visa") + 1);
                                cardDetails.add("Tarjeta Visa encontrada en " + file.getName() + ", línea " + lineNum + ": " + creditCard);
                            }
                        }
                        
                        Matcher mastercardMatcher = mastercardPattern.matcher(line);
                        while (mastercardMatcher.find()) {
                            String creditCard = mastercardMatcher.group();
                            if (isValidCreditCardNumber(creditCard)) {
                                cardCounts.put("Mastercard", cardCounts.get("Mastercard") + 1);
                                cardDetails.add("Tarjeta Mastercard encontrada en " + file.getName() + ", línea " + lineNum + ": " + creditCard);
                            }
                        }
                        
                        Matcher amexMatcher = amexPattern.matcher(line);
                        while (amexMatcher.find()) {
                            String creditCard = amexMatcher.group();
                            if (isValidCreditCardNumber(creditCard)) {
                                cardCounts.put("American Express", cardCounts.get("American Express") + 1);
                                cardDetails.add("Tarjeta American Express encontrada en " + file.getName() + ", línea " + lineNum + ":" + creditCard);
                            }
                        }
                        Matcher discoverMatcher = discoverPattern.matcher(line);
                        while (discoverMatcher.find()) {
                            String creditCard = discoverMatcher.group();
                            if (isValidCreditCardNumber(creditCard)) {
                                cardCounts.put("Discover", cardCounts.get("Discover") + 1);
                                cardDetails.add("Tarjeta Discover encontrada en " + file.getName() + ", línea " + lineNum + ": " + creditCard);
                            }
                        }
                        
                        Matcher unionPayMatcher = unionPayPattern.matcher(line);
                        while (unionPayMatcher.find()) {
                            String creditCard = unionPayMatcher.group();
                            if (isValidCreditCardNumber(creditCard)) {
                                cardCounts.put("Union Pay", cardCounts.get("Union Pay") + 1);
                                cardDetails.add("Tarjeta Union Pay encontrada en " + file.getName() + ", línea " + lineNum + ": " + creditCard);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        
        System.out.println("Resumen:");
        System.out.println("Tarjetas Visa encontradas: " + cardCounts.get("Visa"));
        System.out.println("Tarjetas Mastercard encontradas: " + cardCounts.get("Mastercard"));
        System.out.println("Tarjetas American Express encontradas: " + cardCounts.get("American Express"));
        System.out.println("Tarjetas Discover encontradas: " + cardCounts.get("Discover"));
        System.out.println("Tarjetas Union Pay encontradas: " + cardCounts.get("Union Pay"));
        
        System.out.println("\nDetalles:");
        for (String detail : cardDetails) {
            System.out.println(detail);
        }
    }
    
    private static boolean isValidCreditCardNumber(String creditCardNumber) {
        creditCardNumber = creditCardNumber.replaceAll("[^0-9]+", "");
        int sum = 0;
        boolean alternate = false;
        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(creditCardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}    
