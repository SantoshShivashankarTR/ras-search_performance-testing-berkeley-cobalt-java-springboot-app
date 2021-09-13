package com.trgr.berkeleydb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.context.annotation.Bean;

import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.db.LockDetectMode;
import com.sleepycat.db.SecondaryDatabase;
import com.trgr.cobalt.infrastructure.berkeley.BerkeleyUtilities;
import com.trgr.cobalt.search.berkeley.BerkeleyStateBlock;
import static org.apache.commons.lang3.StringUtils.removeEnd;


public class BerkeleyStateBlockImpl extends BerkeleyStateBlock
{


	public BerkeleyStateBlockImpl(Environment environment) throws FileNotFoundException, DatabaseException {
		
		super(environment());
		String latestAvailability = getLatestProductAvailability("Search", "37");
		System.setProperty(BERKELEY_DEFAULT_PATH , "\\\\C225yliappci.int.thomsonreuters.com\\appdb\\data/Search/37/" + latestAvailability);
		System.setProperty(BERKELEY_LOADER_SWITCH, "ON");
	}
	
    public static String getLatestProductAvailability(final String product, final String version)
    {
        File latestAvailableProductOffering =
        		 new File("//C225yliappci.int.thomsonreuters.com/appdb/data" + "/" + product + "/" + version + "/LatestProductAvailability");
        if (latestAvailableProductOffering.exists())
        {
            BufferedReader br = null;
            try
            {
                br = new BufferedReader(new FileReader(latestAvailableProductOffering));
                return br.readLine();
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    br.close();
                }
                catch (final Exception e)
                {
                }
            }
        }
        return null;
    }
		
	@Override
	protected Iterable<Database> getPDBs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterable<SecondaryDatabase> getSDBs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterable<Database> getWritableDatabases() {
		// TODO Auto-generated method stub
		return null;
	}

	public static  Environment environment() throws FileNotFoundException, DatabaseException {
	 String envPath = "\\\\C225yliappci.int.thomsonreuters.com\\appdb\\cache\\";
	   Environment environment = new Environment(new File(envPath), envConfig());
	   return environment;
	}
	   
	public static  EnvironmentConfig envConfig() {

		EnvironmentConfig berkeleyEnvironmentLargeCacheConfig = new EnvironmentConfig();

		berkeleyEnvironmentLargeCacheConfig.setAllowCreate(true);
		berkeleyEnvironmentLargeCacheConfig.setInitializeCache(true);
		berkeleyEnvironmentLargeCacheConfig.setInitializeLocking(false);
		berkeleyEnvironmentLargeCacheConfig.setThreaded(true);
		berkeleyEnvironmentLargeCacheConfig.setLockDetectMode(LockDetectMode.NONE);
		berkeleyEnvironmentLargeCacheConfig.setPrivate(true);
		berkeleyEnvironmentLargeCacheConfig.setCacheSize(524288000); // 524288000
		berkeleyEnvironmentLargeCacheConfig.setCacheCount(16);
		berkeleyEnvironmentLargeCacheConfig.setDirectDatabaseIO(false);

		return berkeleyEnvironmentLargeCacheConfig;
	}
}
