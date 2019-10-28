/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit;

import com.saprun.test.magnit.util.Util;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 17.10.2019 16:57:49
 */
public class DB {

    private Class<? extends Driver> driverClass = org.postgresql.Driver.class;

    private String url;

    private String user;

	private String password;

    public DB() {
    }

    public Class<? extends Driver> getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(Class<? extends Driver> driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection connect() throws RuntimeException, SQLException {
        try {
            if(Util.isEmpty(getUrl())) {
                throw new SQLException("Url is null or empty");
            }
            if(DriverManager.getDriver(getUrl()) == null) {
                if(getDriverClass() == null) {
                    throw new SQLException("Driver class is null");
                }

                DriverManager.registerDriver(getDriverClass().newInstance());
            }
            return DriverManager.getConnection(getUrl(), getUser(), getPassword());
        }
        catch(SQLException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new RuntimeException("Can't create connection: " + ex.getMessage(), ex);
        }
	}
}
