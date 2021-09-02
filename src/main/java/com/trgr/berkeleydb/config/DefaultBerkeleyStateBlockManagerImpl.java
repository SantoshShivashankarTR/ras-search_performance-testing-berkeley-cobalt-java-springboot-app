package com.trgr.berkeleydb.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.db.LockDetectMode;
import com.sleepycat.db.SecondaryConfig;
import com.sleepycat.db.SecondaryDatabase;
import com.trgr.berkeleydb.search.SessionCase;
import com.trgr.berkeleydb.search.SessionCaseFactory;
import com.trgr.berkeleydb.service.BerkeleyStateBlockImpl;
import com.trgr.berkeleydb.service.TransactionalSessionFactoryImpl;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata.BerkeleyBinding;
import com.trgr.cobalt.infrastructure.berkeley.BerkeleyEnvMgr;
import com.trgr.cobalt.search.berkeley.BerkeleyConfig;
import com.trgr.cobalt.search.berkeley.BerkeleyProduct;
import com.trgr.cobalt.search.berkeley.BerkeleyStateBlock;
import com.trgr.cobalt.search.berkeley.BerkeleyStateBlockManager;
import com.trgr.cobalt.search.berkeley.DefaultBerkeleyStateBlock;
import com.trgr.cobalt.search.berkeley.DefaultBerkeleyStateBlockManager;
import com.trgr.cobalt.search.berkeley.WeakBerkeleyStateBlock;
import com.trgr.cobalt.search.berkeley.WeakDefaultBerkeleyStateBlock;
import com.trgr.cobalt.search.berkeley.sessionobjects.SessionObjectFactory;
import com.trgr.cobalt.search.concurrent.CobaltThreadFactory;
import com.trgr.cobalt.search.concurrent.CobaltThreadPoolExecutor;
import com.trgr.cobalt.search.util.IntegerBinding;
import com.trgr.cobalt.search.util.StringBinding;
import com.trgr.cobalt.search.web.session.DefaultTransactionalSessionFactory;

import static org.apache.commons.lang3.StringUtils.removeEnd;

@Configuration
public class DefaultBerkeleyStateBlockManagerImpl extends BerkeleyStateBlockManager {
	

	private String envPath = "\\\\C225yliappci.int.thomsonreuters.com\\appdb\\cache\\";

	public DefaultBerkeleyStateBlockManagerImpl(EnvironmentConfig environmentConfig, DatabaseConfig primaryConfig,
			SecondaryConfig secondaryConfig, List<BerkeleyConfig> configs, long closeDatabaseHandlesTimeoutInMillis,
			BerkeleyEnvMgr envManager, String dbGrouping) throws DatabaseException, IOException {

		
		super( environmentConfig,  primaryConfig,
				 secondaryConfig,  configs,  closeDatabaseHandlesTimeoutInMillis,
				 envManager,  dbGrouping);
	}

	private DefaultBerkeleyStateBlock berkeleyStateBlock;

	// Spring-core-berkeley.xml Start

	@Bean
	public static Long closeDatabaseHandlesTimeoutInMillis() {

		return 1800000L;

	}

	@Bean
	public static String dbGrouping() {

		return "Search";

	}

	@Bean
	public static  EnvironmentConfig berkeleyEnvironmentLargeCacheConfig() {

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

	@Bean
	@Primary // Primary Database Config
	public static DatabaseConfig berkeleyDatabaseConfig() {
		DatabaseConfig berkeleyDatabaseConfig = new DatabaseConfig();
		berkeleyDatabaseConfig.setAllowCreate(false);
		berkeleyDatabaseConfig.setErrorPrefix("BERKELEY_EXCEPTION");
		berkeleyDatabaseConfig.setReadOnly(true);

		return berkeleyDatabaseConfig;
	}

	@Bean // Secondary Database Config
	public static SecondaryConfig berkeleySecondaryDatabaseConfig() {
		SecondaryConfig berkeleySecondaryDatabaseConfig = new SecondaryConfig();
		berkeleySecondaryDatabaseConfig.setAllowCreate(false);
		berkeleySecondaryDatabaseConfig.setAllowPopulate(false);
		berkeleySecondaryDatabaseConfig.setErrorPrefix("BERKELEY_SECONDARY_EXCEPTION");
		berkeleySecondaryDatabaseConfig.setReadOnly(true);

		return berkeleySecondaryDatabaseConfig;
	}

	@Bean
	public static IntegerBinding integerBinding() {
		return new IntegerBinding();
	}

	@Bean
	public LongBinding longBinding() {
		return new LongBinding();
	}

	@Bean
	public static StringBinding optimizedStringBinding() {
		return new StringBinding();
	}

	@Bean
	public com.sleepycat.bind.tuple.StringBinding stringBinding() {
		com.sleepycat.bind.tuple.StringBinding stringBinding = new com.sleepycat.bind.tuple.StringBinding();
		return stringBinding;
	}

//Spring-core-berkeley.xml End

	@Bean // config for Cases
	public static List<BerkeleyConfig> searchBerkeleyConfigs() {
		BerkeleyConfig caseMetadata = new BerkeleyConfig();
		caseMetadata.setFilePath("CaseMetadata.db");
		caseMetadata.setSecondaryFilePath("CaseMetadataSecondaryIndex.db");
		caseMetadata.setKeyBinding(integerBinding());
		caseMetadata.setSecondaryKeyBinding(optimizedStringBinding());
		caseMetadata.setSessionObjectFactory(sessionCaseFactory());
		caseMetadata.setClassToCreate(CaseMetadata.class);

		List<BerkeleyConfig> searchBerkeleyConfigs = new ArrayList<>();
		searchBerkeleyConfigs.add(caseMetadata);

		return searchBerkeleyConfigs;
	}
	
	
	@Bean(initMethod = "initBerkeleyStateBlockManager", destroyMethod = "close")
	@Primary
	  public DefaultBerkeleyStateBlockManagerImpl searchBerkeleyStateBlockManager() throws DatabaseException, IOException { //

		  Environment environment = new Environment(new File(envPath), berkeleyEnvironmentLargeCacheConfig());
		  BerkeleyStateBlockImpl berkeleyStateBlockImpl = new BerkeleyStateBlockImpl(environment);
	  DefaultBerkeleyStateBlockManagerImpl searchBerkeleyStateBlockManager = new DefaultBerkeleyStateBlockManagerImpl(
	  berkeleyEnvironmentLargeCacheConfig(), berkeleyDatabaseConfig(),
	  berkeleySecondaryDatabaseConfig(), searchBerkeleyConfigs(),180000 ,
	  berkeleyEnvMgr(), "Search");
	  
	  BerkeleyProduct searchBerkeleyProduct = new BerkeleyProduct("Search", "37");
	 // searchBerkeleyProduct.setBerkeleyStateBlockManagers((List<BerkeleyStateBlockManager>) searchBerkeleyStateBlockManager);
	  searchBerkeleyStateBlockManager.setBerkeleyProduct(searchBerkeleyProduct);
	  
	  
	  // searchBerkeleyStateBlockManager.initBerkeleyStateBlockManager(); 
	  return searchBerkeleyStateBlockManager;
	  
	}


	@Bean
	public static BerkeleyEnvMgr berkeleyEnvMgr() {

		BerkeleyEnvMgr berkeleyEnvMgr = new BerkeleyEnvMgr();
		return berkeleyEnvMgr;
	}

	@Bean
	public TransactionalSessionFactoryImpl searchTransactionalSessionFactory()
			throws DatabaseException, IOException {

		
		  TransactionalSessionFactoryImpl searchTransactionalSessionFactory = new TransactionalSessionFactoryImpl( dataExecutor(),
				  searchBerkeleyStateBlockManager() );
		 
		return  searchTransactionalSessionFactory;
	}

	@Bean
	CobaltThreadPoolExecutor dataExecutor() {
		return new CobaltThreadPoolExecutor(2, 20, new CobaltThreadFactory("DataExecutor"));
	}

	@Bean
	public static SessionObjectFactory<CaseMetadata, SessionCase> sessionCaseFactory() {
		return new SessionCaseFactory(caseValueBinding());
	}

	@Bean
	public static TupleBinding<CaseMetadata> caseValueBinding() {
		TupleBinding<CaseMetadata> caseValueBinding = new BerkeleyBinding();

		return caseValueBinding;
	}

	@Bean
	public CaseMetadata caseMetadata() {
		CaseMetadata caseMetadata = new CaseMetadata(1);

		return caseMetadata;
	}

	@Override
	protected void createAndSetStateBlock(Environment environment, String dataPath)
			throws FileNotFoundException, DatabaseException {

		berkeleyStateBlock = new DefaultBerkeleyStateBlock(environment, berkeleyDatabaseConfig(),
				berkeleySecondaryDatabaseConfig(), searchBerkeleyConfigs(), dataPath);
	}

	 

	@Override
	public synchronized WeakDefaultBerkeleyStateBlock getStateBlock() {
		WeakDefaultBerkeleyStateBlock weak = new WeakDefaultBerkeleyStateBlock(berkeleyStateBlock);
		return weak;
	}

	@Override
	protected BerkeleyStateBlock getStrongReferenceStateBlock() {
		return berkeleyStateBlock;
	}

	@Override
	protected void reassignStateBlock(BerkeleyStateBlock stateBlock) {
		berkeleyStateBlock = (DefaultBerkeleyStateBlock) stateBlock;
	}

}
