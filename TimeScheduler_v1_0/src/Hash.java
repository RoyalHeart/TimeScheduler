package src;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;

public class Hash {
    public static String hashPassword(String password) throws RuntimeException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String myHashHex = Hex.encodeHexString(digest);
            return myHashHex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
