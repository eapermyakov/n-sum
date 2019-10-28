/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.ex;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 16:53:28
 */
public class XMLReadException extends Exception {

    public XMLReadException(Throwable cause) {
        super(cause);
    }

    public XMLReadException(String message, Throwable cause) {
        super(message, cause);
    }
}