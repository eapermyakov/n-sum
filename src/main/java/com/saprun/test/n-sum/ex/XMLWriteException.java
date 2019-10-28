/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.ex;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 16:53:19
 */
public class XMLWriteException extends Exception {

    public XMLWriteException(Throwable cause) {
        super(cause);
    }

    public XMLWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
