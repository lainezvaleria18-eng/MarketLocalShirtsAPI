package org.esfe.seguridad;

public final class RolNormalizador {

    private RolNormalizador() {
    }

    public static String normalizar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "CLIENTE";
        }
        String valor = nombre.replace("ROLE_", "").trim().toUpperCase();
        if (valor.contains("ADMIN")) {
            return "ADMIN";
        }
        return "CLIENTE";
    }
}
