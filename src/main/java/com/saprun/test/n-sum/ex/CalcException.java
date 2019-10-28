/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.ex;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 17:27:37
 */
public class CalcException extends Exception {

    public CalcException(Throwable cause) {
        super(cause);
    }

    public CalcException(String message, Throwable cause) {
        super(message, cause);
    }
}
