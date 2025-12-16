package br.com.fiap.cheffy.domain;

public enum ProfileType {

    CLIENT("cliente"),
    OWNER("dono");

    private final String value;

    ProfileType(String value) {
        this.value = value;
    }

    public String getType() {
        return value;
    }
}
