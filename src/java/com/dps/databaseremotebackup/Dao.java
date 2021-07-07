package com.dps.databaseremotebackup;

import com.dps.dbi.impl.SqlServerInterface;

public class Dao
{
    public static final SqlServerInterface DBI = new SqlServerInterface()
        .username("sa").password("C64Sys64738SQLSERVER").name("master");
}
