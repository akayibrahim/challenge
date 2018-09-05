package org.chl.util;

import org.chl.model.Subjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ibrahim on 11/28/2017.
 */
public class Constant {
    public static final String ACCEPT = "accepted";
    public static final String REJECT = "rejected";
    public static final String YOUR_TEAM = "supported your team.";
    public static final String YOUR_OPPONENT_TEAM = "supported your opponent team.";
    public static final String SPACE = " ";
    public static final String ACCEPT_REQUEST = "request you for accept of %s challenge.";
    public static final String JOIN_REQUEST_CONTENT = "request you for join to %s challenge.";
    public static final String JOINED_TO_CHALLENGE = "joined to %s challenge.";
    public static final String DONT_JOINED_TO_CHALLENGE = "refused %s challenge.";
    public static final String COMMENTED = "commented: ";
    public static final String PROOFED_CHALLENGE = "proved %s challenge.";
    public static final String START_TO_FOLLOWING = "start to following %s.";
    public static final String START_TO_FOLLOW_YOU = "start to follow you.";
    public static final String ACCEPT_FOLLOWER_REQUEST = "accept your following request.";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String YOU = "You";
    public static final String SUPPORT_YOU = "supported you.";
    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String PUBLIC_CHL = "public";
    public static final String TEAM_CHL = "team";
    public static final String SELF_CHL = "self";

    public enum ANSWER {
        ACCEPT("1"),
        REJECT("0");

        private String answer;

        ANSWER(String answer) {
            this.answer = answer;
        }

        public String getAnswer() {
            return answer;
        }
    }

    public enum STATUS {
        FINISH("has finished %s challenge"),
        JOIN("has joined challenge"),
        PROOF("has proved challenge"),
        NEW_PROOF("has a new proved challenge"),
        NEW("has a new %s challenge");

        private String status;

        STATUS(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public enum VISIBILITY {
        JUST(3),
        FRIENDS(2),
        EVERYONE(1);

        private Integer code;

        VISIBILITY(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

    public enum TYPE {
        SELF,
        PUBLIC,
        PRIVATE;
    }

    public enum POPULARITY {
        SUPPORT,
        COMMENT
    }

    public enum PUSH_NOTIFICATION {
        DONE("Your challenge time is done!", "What is results?"),
        COMMENT("A new comment!", ""),
        PROOF("A new proof!", ""),
        SUPPORT("Someone supports u!", ""),
        ACCEPT("Accept it!", ""),
        JOIN("Join it!", ""),
        FOLLOWER("A new follower!", ""),
        FOLLOWING("A new following!", ""),
        FRIEND_REQUEST("A new follower!", "");

        private String messageTitle;
        private String message;

        PUSH_NOTIFICATION(String messageTitle, String message) {
            this.messageTitle = messageTitle;
            this.message = message;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public String getMessage() {
            return message;
        }
    }

    public static List<Subjects> toList(Subject[] values, boolean isSelf) {
        List<Subjects> subjects = new ArrayList<>();
        for (Subject subject : values) {
            if (isSelf && !subject.isSelf)
                continue;
            Subjects sub = new Subjects();
            sub.setName(subject.toString().replace("_", " "));
            subjects.add(sub);
        }
        return subjects;
    }

    public enum ACTIVITY {
        COMMENT,
        PROOF,
        SUPPORT,
        FOLLOWING,
        FOLLOWER,
        JOIN,
        ACCEPT,
        FRIEND_REQUEST,
        ACCEPT_FRIEND_REQUEST;
    }

    public enum REQUEST_TYPE {
        JOIN,
        ACCEPT;
    }
}
