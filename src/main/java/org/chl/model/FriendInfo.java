package org.chl.model;

public class FriendInfo {
    private String name;
    private String surname;
    private String facebookID;
    private Boolean privateMember;
    private Boolean isMyFriend;
    private Boolean isRequestFriend;
    private int followerCount;
    private int followingCount;

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public Boolean getPrivateMember() {
        return privateMember;
    }

    public void setPrivateMember(Boolean privateMember) {
        this.privateMember = privateMember;
    }

    public Boolean getMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(Boolean myFriend) {
        isMyFriend = myFriend;
    }

    public Boolean getRequestFriend() {
        return isRequestFriend;
    }

    public void setRequestFriend(Boolean requestFriend) {
        isRequestFriend = requestFriend;
    }
}
