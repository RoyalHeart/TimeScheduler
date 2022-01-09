package src;

import java.security.MessageDigest;
// import java.util.Base64;
import org.apache.commons.codec.binary.Hex;

// import javax.xml.bind.DatatypeConverter;

public class Hash {

    public static String hashPassword(String password) throws RuntimeException {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            // String myHash64 = Base64.getEncoder().encodeToString(digest);
            String myHashHex = Hex.encodeHexString(digest);
            // System.out.println(myHash64);
            // System.out.println(myHashHex);
            return myHashHex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
