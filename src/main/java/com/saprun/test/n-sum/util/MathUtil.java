/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.util;

import java.util.Collection;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 17:38:28
 */
public class MathUtil {

	private static final Double LONGMIN = Double.valueOf(Long.MIN_VALUE);

	private static final Double LONGMAX = Double.valueOf(Long.MAX_VALUE);


    private static void isLongOverflow(Double num) {
        if(num.compareTo(LONGMIN) < 0
                || num.compareTo(LONGMAX) > 0) {
            throw new ArithmeticException("Overflow");
        }
    }

    public static long sum(Collection<Integer> c) {
        if(c == null || c.isEmpty()) return 0;

        Double sum = 0D;
        for(Integer i : c) {
            if(i == null) continue;
            sum += i;
            isLongOverflow(sum);
        }
        return sum.longValue();
    }

}
