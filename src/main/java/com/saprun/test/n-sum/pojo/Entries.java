/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.pojo;

import com.saprun.test.magnit.XML;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 12:22:21
 */
@XmlRootElement(name = XML.ELEMENT_NAME_ENTRIES)
@XmlAccessorType(XmlAccessType.FIELD)
public class Entries {

    @XmlElement(name = XML.ELEMENT_NAME_ENTRY)
    private List<Entry> entries;

    public Entries() {
    }

    public Entries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getEntries() {
        if(entries == null) entries = new ArrayList<>();
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public boolean addEntry(Entry entry) {
        return getEntries().add(entry);
    }
}
