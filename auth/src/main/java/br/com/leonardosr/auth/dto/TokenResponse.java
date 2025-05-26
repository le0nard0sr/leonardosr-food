package br.com.leonardosr.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("expires_in")
    private Integer expiresIn;
    
    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("not-before-policy")
    private Integer notBeforePolicy;
    
    @JsonProperty("session_state")
    private String sessionState;
    
    @JsonProperty("scope")
    private String scope;

    public TokenResponse(String token) {
        this.accessToken = token;
        this.tokenType = "Bearer";
    }
} 