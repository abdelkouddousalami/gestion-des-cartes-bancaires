package util;

import java.util.Random;

public class CarteUtil {
    
    private static final Random random = new Random();
    
    public static String genererNumeroCarte() {
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }
    
    public static boolean validerNumeroCarte(String numero) {
        if (numero == null || numero.length() != 16) {
            return false;
        }
        
        for (char c : numero.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        
        return true;
    }
}
