package com.boot.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * list通用处理方法
 *
 * @author epl
 */
public class ListUtils {

    /**
     * 根据指定长度拆分集合
     *
     * @param list 集合
     * @param len 集合长度
     * @return 小集合的集合
     */
    public static List<List<?>> splitList(List<?> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }
        List<List<?>> result = new ArrayList<List<?>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<?> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}