package org.chl.util;

import java.util.Arrays;

/**
 * Created by ibrahim on 11/28/2017.
 */
public class Constant {
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
}
