package src;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;

/**
 * Hash class to hash SHA-256.
 * 
 * @author Tam Thai Hoang
 */
public class Hash {
    /**
     * Hash the given string with SHA-256.
     * 
     * @param input The string to hash
     * @return The hashed string
     */
    public static String hashPassword(String input) throws RuntimeException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            String myHashHex = Hex.encodeHexString(digest);
            return myHashHex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
