package com.trgr.berkeleydb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.trgr.cobalt.infrastructure.util.ReflectionUtils;

import com.trgr.cobalt.search.berkeley.BerkeleyConfig;
import com.trgr.cobalt.search.berkeley.BerkeleyDataAccess;
import com.trgr.cobalt.search.berkeley.BerkeleyProduct;
import com.trgr.cobalt.search.berkeley.BerkeleyStateBlockManager;
import com.trgr.cobalt.search.berkeley.WeakDefaultBerkeleyStateBlock;
import com.trgr.cobalt.search.berkeley.sessionobjects.SessionObjectFactory;
import com.trgr.cobalt.search.dal.SimpleDataAccess;
import com.trgr.cobalt.search.dal.UnifiedDataAccessObject;
import com.trgr.berkeleydb.config.DefaultBerkeleyStateBlockManagerImpl;
import com.trgr.berkeleydb.search.SessionCase;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata;

@Service
public class BerkeleyAppService
{

    private List<DefaultBerkeleyStateBlockManagerImpl> berkeleyStateBlockManagers;

    
    private String stripDbSuffix(final String database)
    {
        return database.substring(0, database.length() - ".db".length()); //$NON-NLS-1$
    }
    

	
	public BerkeleyAppService(
			@Autowired(required = true) final List<DefaultBerkeleyStateBlockManagerImpl> berkeleyStateBlockManagers)
			throws DatabaseException, IOException {
		this.berkeleyStateBlockManagers = berkeleyStateBlockManagers;
	}
	 
    

    public List<Object> listEntries(final String database, final int limit)
    {
        if (berkeleyStateBlockManagers == null)
        {
            return ImmutableList.of();
        }
        
        final Map<String, DefaultBerkeleyStateBlockManagerImpl> managersByDatabase = new HashMap<>();
        final Map<String, BerkeleyConfig> configsByDatabase = new HashMap<>();

        for (final DefaultBerkeleyStateBlockManagerImpl manager : berkeleyStateBlockManagers)
        {
            for (final BerkeleyConfig config : manager.getConfigs())
            {
                managersByDatabase.put(stripDbSuffix(config.getFilePath()), manager);
                configsByDatabase.put(stripDbSuffix(config.getFilePath()), config);
            }
        }

        final DefaultBerkeleyStateBlockManagerImpl bsbm = managersByDatabase.get(database);
        final BerkeleyConfig conf = configsByDatabase.get(database);

        final WeakDefaultBerkeleyStateBlock stateBlock = bsbm.getStateBlock();

        final Database bdb = stateBlock.getDatabase(conf.getFilePath());

        final TupleBinding<?> valueBinding = ReflectionUtils.readField(
            ReflectionUtils.getField(conf.getSessionObjectFactory().getClass(), "valueBinding"), conf.getSessionObjectFactory()); //$NON-NLS-1$

        final Iterator<?> iter = berkeleyEntries(bdb, conf.getKeyBinding(),
            valueBinding != null ? new SessionObjectFactory<>(valueBinding) : conf.getSessionObjectFactory()).iterator();

        final List<Object> entries = new ArrayList<>();

        int count = 0;
        while (count < limit && iter.hasNext())
        {
            count++;
            final Object doc = iter.next();
            
            entries.add(doc);
        }

        return entries;
    }

    protected Iterable<?> berkeleyEntries(final Database database,
        final TupleBinding<?> keyBinding,
        final SessionObjectFactory<?, ?> sessionObjectFactory)
    {
        return new BerkeleyDataAccess<>(database, keyBinding, sessionObjectFactory);
    }

    public List<String> listDatabases()
    {
        if (berkeleyStateBlockManagers == null)
        {
            return ImmutableList.of();
        }
        final List<String> dbs = new ArrayList<>();

        for (final DefaultBerkeleyStateBlockManagerImpl manager : berkeleyStateBlockManagers)
        {
            for (final BerkeleyConfig config : manager.getConfigs())
            {
                dbs.add(stripDbSuffix(config.getFilePath()));
            }
        }

        return dbs;
    }
    
    
	public List<Long> listEntriesTesting() throws IOException {
		 File file =  new File("C:\\SearchModBerkeley\\CaseMetadata100k.txt");
		 Scanner sc = new Scanner(file);
		long durationInNano ;
		long durationInMillis;
		final List<Long> getTimeCaseMetadata = new ArrayList<>();
		
		List<Long> fileTime  =  new ArrayList<>();
 
			    while (sc.hasNextInt())
			    {
		            
			    long startTime = System.nanoTime();
			      
				SessionCase caseMetadata = new SessionCase(sc.nextInt());

				long endTime = System.nanoTime();
				 
			     durationInNano = (endTime - startTime);  //Total execution time in nano seconds
			     fileTime.add(durationInNano);
			     durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);  //Total execution time in milli seconds
					/*
					 * System.out.println("durationInNano : " + durationInNano);
					 * System.out.println("durationInMillis: " + durationInMillis);
					 * //System.out.println(caseMetadata.getBerkeleyObject().toString());
					 */			  }
			   
			if(fileTime.size()>0)    
			{ 
				getTimeCaseMetadata.add(fileTime.stream().mapToLong(duration->duration.longValue()).sum()/fileTime.size());
				getTimeCaseMetadata.add(TimeUnit.NANOSECONDS.toMillis(fileTime.stream().mapToLong(duration->duration.longValue()).sum()));
			System.out.println("Average Time for 100K search Key calls in nanoseconds" + (fileTime.stream().mapToLong(duration->duration.longValue()).sum()/fileTime.size()));
			System.out.println("Total Time for 100K search Key calls in milliseconds" + TimeUnit.NANOSECONDS.toMillis(fileTime.stream().mapToLong(duration->duration.longValue()).sum()));
			}  
			
			return getTimeCaseMetadata;
	}




}
