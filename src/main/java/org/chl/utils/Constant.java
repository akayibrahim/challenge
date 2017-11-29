package org.chl.utils;

/**
 * Created by ibrahim on 11/28/2017.
 */
public class Constant {
    public enum TYPE {
        PRIVATE("private"),
        PUBLIC("public"),
        SELF("self");

        private String type;

        TYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public boolean equals(String type) {
            return this.type.equals(type);
        }
    }

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
}
