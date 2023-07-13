package com.sistema.blog.seguridad;

public class JWTAuthResponseDTO {
    private String tokenAcceso;
    private String tipoDeToken = "Bearer";

    public JWTAuthResponseDTO(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }

    public JWTAuthResponseDTO(String tokenAcceso, String tipoDeToken) {
        this.tokenAcceso = tokenAcceso;
        this.tipoDeToken = tipoDeToken;
    }

    public String getTokenAcceso() {
        return tokenAcceso;
    }

    public void setTokenAcceso(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }

    public String getTipoDeToken() {
        return tipoDeToken;
    }

    public void setTipoDeToken(String tipoDeToken) {
        this.tipoDeToken = tipoDeToken;
    }
}
