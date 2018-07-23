package org.chl.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Util {
    public static Pageable getPageable(int page, Sort sort, int size) {
        return new PageRequest(page, size, sort);
    }
}
