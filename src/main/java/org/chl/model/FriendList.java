package org.chl.model;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 11/28/2017.
 */
@Validated
@Document
public class FriendList {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the memberId parameter")
    private String memberId;
    @NotEmpty(message="You need to pass the friendMemberId parameter")
    private String friendMemberId;

    private Member friendMemberInfo;
    @NotEmpty(message="You need to pass the isFollowed parameter")
    private Boolean followed;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    private Boolean deleted;

    private Boolean requested;

    public Boolean getRequested() {
        return requested == null ? false : requested;
    }

    public void setRequested(Boolean requested) {
        this.requested = requested;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public Member getFriendMemberInfo() {
        return friendMemberInfo;
    }

    public void setFriendMemberInfo(Member friendMemberInfo) {
        this.friendMemberInfo = friendMemberInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFriendMemberId() {
        return friendMemberId;
    }

    public void setFriendMemberId(String friendMemberId) {
        this.friendMemberId = friendMemberId;
    }

}
