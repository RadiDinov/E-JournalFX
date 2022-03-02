package SaltHasher;

import java.security.SecureRandom;

import org.bouncycastle.crypto.generators.OpenBSDBCrypt;

public class BCryptPasswordHashing {

    private static final SecureRandom  secureRandom = new SecureRandom();

    private final int costFactor;

    public BCryptPasswordHashing(int costFactor) {
        this.costFactor = costFactor;
    }

    public final String hashPassword(char[] password) {
        if (password == null) {
            return null;
        }

        final byte[] salt = this.getSalt();
        final String bCryptHash = OpenBSDBCrypt.generate(password, salt, this.costFactor);
        return bCryptHash;
    }

    public final boolean verifyPassword(char[] password, String storedPassword) {
        if (password == null) {
            return (storedPassword == null);
        }

        if (storedPassword == null) {
            return false;
        }

        return OpenBSDBCrypt.checkPassword(storedPassword, password);
    }

    private byte[] getSalt() {
        byte[] salt = new byte[16]; // BCrypt salt size
        secureRandom.nextBytes(salt);
        return salt;
    }


}