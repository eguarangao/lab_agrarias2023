package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {
    public  String hashPassword(String plainTextPassword) {
        // Genera un hash de contraseña encriptado con BCrypt
        String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12)); // El valor 12 es el costo de hashing

        return hashedPassword;
    }

    public  boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        // Verifica si la contraseña en texto plano coincide con el hash almacenado en la base de datos
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
