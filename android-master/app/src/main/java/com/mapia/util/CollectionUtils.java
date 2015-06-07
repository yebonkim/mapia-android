package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 6..
 */

import java.util.Collection;

public class CollectionUtils
{
    public static boolean isEmpty(final Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(final Collection collection) {
        return collection != null && collection.size() > 0;
    }
}