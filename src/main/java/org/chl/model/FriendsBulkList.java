package org.chl.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by ibrahim on 16/08/2018.
 */
@Validated
@Document
public class FriendsBulkList {
    private List<String> friendMemberIdList;
    private String memberId;

    public List<String> getFriendMemberIdList() {
        return friendMemberIdList;
    }

    public void setFriendMemberIdList(List<String> friendMemberIdList) {
        this.friendMemberIdList = friendMemberIdList;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
