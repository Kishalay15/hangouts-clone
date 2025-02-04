package hangoutsclone.hangouts.requests;

public class UpdateUserRequest {
    
    private String name;
    private String dp;

    public UpdateUserRequest(String name, String dp) {
        this.name = name;
        this.dp = dp;
    }

    public UpdateUserRequest() {
    }
}
