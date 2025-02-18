package hangoutsclone.hangouts.requests;

import java.util.List;

public class GroupChatRequest {

    private List<Integer> userIds;
    private String groupName;
    private String groupImage;

    public GroupChatRequest() {
    }


    public GroupChatRequest(List<Integer> userIds, String groupName, String groupImage) {
        this.userIds = userIds;
        this.groupName = groupName;
        this.groupImage = groupImage;
    }


    public List<Integer> getUserIds() {
        return userIds;
    }
    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupImage() {
        return groupImage;
    }
    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

}
