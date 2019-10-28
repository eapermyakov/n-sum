/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit;

import com.saprun.test.magnit.ex.CalcException;
import com.saprun.test.magnit.util.LogUtil;
import com.saprun.test.magnit.util.Util;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 17.10.2019 16:57:05
 */
public class Main {

    /* Параметры подключения к БД */
    private static final String DB_URL = "jdbc:postgresql://10.10.0.69/magnit-test";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Qwerty1";

    /**
     * @param args the command line arguments
     * @throws com.saprun.test.magnit.ex.CalcException
     */
    public static void main(String[] args) throws CalcException {
        //Число N
        int num = 0;
        try {
            if(Util.isNotEmpty(args)) {
                num = Integer.parseInt(args[0]);
            }
        }
        catch(NumberFormatException ex) {
            LogUtil.error("Argument must be an integer");
            return;
        }

        LogUtil.info("--- JAXB -------------");
        long start = System.currentTimeMillis();
        MainClass c = new MainClass();
        //Параметры подключения к БД
        c.setDbUrl(DB_URL);
        c.setDbUser(DB_USER);
        c.setDbPassword(DB_PASSWORD);
        //Библиотека, используемая для записи в файл (чтения из файла)
        c.setXmlLib(XMLLib.JAXB);
        c.setNum(num);
        long result = c.calc();
        LogUtil.info((System.currentTimeMillis() - start), "Result 1 + ... + %s = %s", c.getNum(), result);

        LogUtil.info("--- JDOM -------------");
        start = System.currentTimeMillis();
        c = new MainClass();
        c.setDbUrl(DB_URL);
        c.setDbUser(DB_USER);
        c.setDbPassword(DB_PASSWORD);
        c.setXmlLib(XMLLib.JDOM);
        c.setNum(num);
        result = c.calc();
        LogUtil.info((System.currentTimeMillis() - start), "Result 1 + ... + %s = %s", c.getNum(), result);
    }

}
