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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
