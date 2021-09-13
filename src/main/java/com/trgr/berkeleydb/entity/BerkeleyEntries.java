package com.trgr.berkeleydb.entity;

import java.util.List;

public class BerkeleyEntries
{
    private String database;
    private List<Object> entries;
    private List<Long> times;

    public String getDatabase()
    {
        return database;
    }

    public void setDatabase(final String database)
    {
        this.database = database;
    }

    public List<Object> getEntries()
    {
        return entries;
    }

    public void setEntries(final List<Object> entries)
    {
        this.entries = entries;
    }
    
    public List<Long> getTimes()
    {
        return times;
    }

    public void setTimes(final List<Long> times)
    {
        this.times = times;
    }
}
