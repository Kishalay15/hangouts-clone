package hangoutsclone.hangouts.response;

public class AuthResponse {
    
    private String jwt;
    private boolean isVerified;

    public AuthResponse(String jwt, boolean isVerified) {
        super();
        this.jwt = jwt;
        this.isVerified = isVerified;
    }
}
