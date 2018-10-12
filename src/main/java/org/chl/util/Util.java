package org.chl.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Objects;

public class Util {
    public static Pageable getPageable(int page, Sort sort, int size) {
        return new PageRequest(page, size, sort);
    }

    public static boolean isTimesUp(Constant.TYPE type, Boolean done, Boolean homeWin, Boolean awayWin, Boolean join, Boolean proof, Date untilDate) {
        boolean isTimeOver = untilDate != null && untilDate.compareTo(new Date()) < 0 ? true : false;
        if (type.equals(Constant.TYPE.SELF)) {
            return isTimeOver && !(!done || (done && isNotNullAndTrue(homeWin)));
        } else if (type.equals(Constant.TYPE.PRIVATE)) {
            return isTimeOver && !(!done || (done && (isNotNullAndTrue(homeWin) || isNotNullAndTrue(awayWin))));
        } else if (type.equals(Constant.TYPE.PUBLIC)) {
            return isTimeOver && ((!done && join && !isNotNullAndTrue(proof) || (done && !isNotNullAndTrue(proof))));
        }
        return false;
    }

    public static boolean isNotNullAndTrue(Boolean bool) {
        return !Objects.isNull(bool) && bool ? true : false;
    }
}
