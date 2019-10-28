/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit;

import com.saprun.test.magnit.ex.CalcException;
import com.saprun.test.magnit.util.LogUtil;
import com.saprun.test.magnit.util.MathUtil;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 17.10.2019 16:57:26
 */
public class MainClass {

    private DB db;

    private int num;

    /**
     * Библиотека для работы с XML.
     * @see XMLLib
     */
    private XMLLib xmlLib = XMLLib.JAXB;

    public MainClass() {
        db = new DB();
    }

    public Class<? extends Driver> getDbDriverClass() {
        return db.getDriverClass();
    }

    public void setDbDriverClass(Class<? extends Driver> driverClass) {
        db.setDriverClass(driverClass);
    }

    public String getDbUrl() {
        return db.getUrl();
    }

    public void setDbUrl(String url) {
        db.setUrl(url);
    }

    public String getDbUser() {
        return db.getUser();
    }

    public void setDbUser(String user) {
        db.setUser(user);
    }

    public String getDbPassword() {
        return db.getPassword();
    }

    public void setDbPassword(String password) {
        db.setPassword(password);
    }

    public XMLLib getXmlLib() {
        return xmlLib;
    }

    public void setXmlLib(XMLLib xmlLib) throws IllegalArgumentException {
        if(xmlLib == null) throw new IllegalArgumentException("XmlLib type is null");
        this.xmlLib = xmlLib;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private boolean setDbNums(Connection con) throws SQLException {
        String sqlDelete = "DELETE FROM test";
        String sqlAdd = "WITH RECURSIVE temp(num) AS (VALUES (1) " +
                        "UNION ALL SELECT num + 1 FROM temp WHERE num < ?) " +
                     "INSERT INTO test (field) " +
                        "SELECT num as field FROM temp";

        try(PreparedStatement psDelete = con.prepareStatement(sqlDelete);
                PreparedStatement psAdd = con.prepareStatement(sqlAdd)) {
            //Удаляем данные
            psDelete.executeUpdate();
            psDelete.close();

            //Добавляем новые данные
            psAdd.setInt(1, getNum());
            int result = psAdd.executeUpdate();
            psAdd.close();

            return result > 0;
        }
    }

    private List<Integer> readDbNums(Connection con) throws SQLException {
        String sql = "SELECT field FROM test";

        List<Integer> result = new ArrayList<>(getNum());
        try(PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            //Чтение данных
            while(rs.next()) {
                result.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

            return result;
        }
    }

    /**
     * Расчёт.
     * @return
     * @throws CalcException
     */
    public long calc() throws CalcException {
        if(getNum() <= 0) return getNum();

        long result = 0;
        try(Connection con = db.connect()) {
            long start = System.currentTimeMillis();
            //Запись в БД
            if(!setDbNums(con)) {
                LogUtil.error("Write data to DB error");
            }
            LogUtil.info((System.currentTimeMillis() - start), "Write to DB");

            //Чтение из БД
            start = System.currentTimeMillis();
            List<Integer> nums = readDbNums(con);
            if(nums.size() != getNum()) {
                LogUtil.error("Read data from DB error");
            }
            LogUtil.info((System.currentTimeMillis() - start), "Read from DB: size = %s", nums.size());

            //Запись в файл
            String fileName = "write-" + getXmlLib().name() + ".xml";
            start = System.currentTimeMillis();
            if(!XML.write(getXmlLib(), fileName, nums, getNum() < 10000)) {
                LogUtil.error("Write data to xml file error");
            }
            LogUtil.info((System.currentTimeMillis() - start), "Write data to xml file '%s'", fileName);

            //Трансформация xslt
            String transformedFileName = "transformed-" + fileName;
            start = System.currentTimeMillis();
            if(!XML.transform(fileName, transformedFileName, "transform.xslt")) {
                LogUtil.error("XSLT transform error");
            }
            LogUtil.info((System.currentTimeMillis() - start), "XSLT transform to file '%s'", transformedFileName);

            //Чтение из файла
            start = System.currentTimeMillis();
            Collection<Integer> nums2 = XML.read(getXmlLib(), transformedFileName);
            if(nums2.size() != getNum()) {
                LogUtil.error("Read data from xml file error");
            }
            LogUtil.info((System.currentTimeMillis() - start), "Read data from xml file. Size = %s", nums2.size());

            //Сумма
            start = System.currentTimeMillis();
            result = MathUtil.sum(nums2);
            LogUtil.info((System.currentTimeMillis() - start), "Sum");

            return result;
        }
        catch(Exception ex) {
            throw new CalcException("Calc error", ex);
        }
    }
}
