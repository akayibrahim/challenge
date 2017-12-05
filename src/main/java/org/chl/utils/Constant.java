package org.chl.utils;

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
}
