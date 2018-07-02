package org.chl.util;

import org.chl.model.Subjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ibrahim on 11/28/2017.
 */
public class Constant {
    public static final String ACCEPT = "accept";
    public static final String REJECT = "reject";
    public static final String YOUR_TEAM = "support your team.";
    public static final String YOUR_OPPONENT_TEAM = "support your opponent team.";
    public static final String SPACE = " ";
    public static final String ACCEPT_REQUEST = "request you for accept of %s challenge.";
    public static final String JOIN_REQUEST_CONTENT = "request you for join to %s challenge.";
    public static final String JOINED_TO_CHALLENGE = "joined to %s challenge.";
    public static final String COMMENTED = "commented: ";
    public static final String PROOFED_CHALLENGE = "proofed %s challenge.";
    public static final String START_TO_FOLLOWING = "start to following %s.";
    public static final String START_TO_FOLLOW_YOU = "start to follow you.";

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
        FINISH("has finished challenge"),
        JOIN("has joined challenge"),
        PROOF("has proofed challenge"),
        NEW_PROOF("has a new proofed challenge"),
        NEW("has a new challenge");

        private String status;

        STATUS(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
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
        DONE("Your challenge time is done!", "What is results?");

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
        ACCEPT;
    }

    public enum REQUEST_TYPE {
        JOIN,
        ACCEPT;
    }
}
