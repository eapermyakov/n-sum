/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.pojo;

import com.saprun.test.magnit.XML;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 12:22:58
 */
@XmlRootElement(name = XML.ELEMENT_NAME_ENTRY)
@XmlAccessorType(XmlAccessType.FIELD)
public class Entry {

    @XmlElement(name = XML.ELEMENT_NAME_FIELD)
    private int field;

    public Entry() {
    }

    public Entry(int field) {
        this.field = field;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }
}
