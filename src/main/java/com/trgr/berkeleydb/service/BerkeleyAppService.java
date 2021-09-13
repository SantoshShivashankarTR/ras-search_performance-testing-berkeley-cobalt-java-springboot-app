package com.trgr.berkeleydb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.SecondaryDatabase;
import com.trgr.berkeleydb.config.DefaultBerkeleyStateBlockManagerImpl;
import com.trgr.berkeleydb.search.SessionCase;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata;
import com.trgr.cobalt.infrastructure.util.ReflectionUtils;
import com.trgr.cobalt.search.berkeley.BerkeleyConfig;
import com.trgr.cobalt.search.berkeley.BerkeleyDataAccess;
import com.trgr.cobalt.search.berkeley.BerkeleyMultiDataAccess;
import com.trgr.cobalt.search.berkeley.WeakDefaultBerkeleyStateBlock;
import com.trgr.cobalt.search.berkeley.sessionobjects.SessionObjectFactory;
import com.trgr.cobalt.search.dal.SimpleDataAccess;
import com.trgr.cobalt.search.dal.SimpleMultiDataAccess;

@Service
public class BerkeleyAppService {

	private List<DefaultBerkeleyStateBlockManagerImpl> berkeleyStateBlockManagers;
	private Map<String, DefaultBerkeleyStateBlockManagerImpl> managersByDatabase;
	private Map<String, BerkeleyConfig> configsByDatabase;

	private String stripDbSuffix(final String database) {
		return database.substring(0, database.length() - ".db".length()); //$NON-NLS-1$
	}

	public BerkeleyAppService(
			@Autowired(required = true) final List<DefaultBerkeleyStateBlockManagerImpl> berkeleyStateBlockManagers)
			throws DatabaseException, IOException {
		this.berkeleyStateBlockManagers = berkeleyStateBlockManagers;

		managersByDatabase = new HashMap<>();
		configsByDatabase = new HashMap<>();

		for (final DefaultBerkeleyStateBlockManagerImpl manager : berkeleyStateBlockManagers) {
			for (final BerkeleyConfig config : manager.getConfigs()) {
				managersByDatabase.put(stripDbSuffix(config.getFilePath()), manager);
				configsByDatabase.put(stripDbSuffix(config.getFilePath()), config);
			}
		}

	}

	public List<Object> listEntries(final String database, final int limit) throws FileNotFoundException {
		if (berkeleyStateBlockManagers == null) {
			return ImmutableList.of();
		}

		final Map<String, DefaultBerkeleyStateBlockManagerImpl> managersByDatabase = new HashMap<>();
		final Map<String, BerkeleyConfig> configsByDatabase = new HashMap<>();

		for (final DefaultBerkeleyStateBlockManagerImpl manager : berkeleyStateBlockManagers) {
			for (final BerkeleyConfig config : manager.getConfigs()) {
				managersByDatabase.put(stripDbSuffix(config.getFilePath()), manager);
				configsByDatabase.put(stripDbSuffix(config.getFilePath()), config);
			}
		}

		final DefaultBerkeleyStateBlockManagerImpl bsbm = managersByDatabase.get(database);
		final BerkeleyConfig conf = configsByDatabase.get(database);

		final WeakDefaultBerkeleyStateBlock stateBlock = bsbm.getStateBlock();

		final Database bdb = stateBlock.getDatabase(conf.getFilePath());

		final TupleBinding<?> valueBinding = ReflectionUtils.readField(
				ReflectionUtils.getField(conf.getSessionObjectFactory().getClass(), "valueBinding"), //$NON-NLS-1$
				conf.getSessionObjectFactory());

		final Iterator<?> iter = berkeleyEntries(bdb, conf.getKeyBinding(),
				valueBinding != null ? new SessionObjectFactory<>(valueBinding) : conf.getSessionObjectFactory())
						.iterator();

		final List<Object> entries = new ArrayList<>();

		File file = new File("CaseMetadata100k.txt");
		// Instantiating the PrintStream class
		PrintStream stream = new PrintStream(file);
		File guidsFile = new File("CaseMetadata100kGuids.txt");
		// Instantiating the PrintStream class
		PrintStream guidsStream = new PrintStream(guidsFile);
		try
		{
			int count = 0;
			while (count < limit && iter.hasNext()) {
				count++;
				final CaseMetadata doc = (CaseMetadata) iter.next();
				stream.append(doc.getSearchKey() + " ");
				guidsStream.println(doc.getGuid() + " ");
				entries.add(doc);
			}
		}
		finally
		{
			stream.flush();
			stream.close();
			guidsStream.flush();
			guidsStream.close();
		}

		return entries;
	}

	public List<String> listDatabases() {
		if (berkeleyStateBlockManagers == null) {
			return ImmutableList.of();
		}
		final List<String> dbs = new ArrayList<>();

		for (final DefaultBerkeleyStateBlockManagerImpl manager : berkeleyStateBlockManagers) {
			for (final BerkeleyConfig config : manager.getConfigs()) {
				dbs.add(stripDbSuffix(config.getFilePath()));
			}
		}

		return dbs;
	}

	public List<Long> listEntriesTesting() throws IOException {
		File file = new File("CaseMetadata100kGuidsToSearchKeys.txt");
		Scanner sc = new Scanner(file);
		List<Long> fileTime = new ArrayList<>();

		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		//System.out.println(timeStampPattern.format(java.time.LocalDateTime.now()));
		String name = "CasesMetadata_Seq_BDBPrimary_" + (timeStampPattern.format(LocalDateTime.now()));
		
		final List<Long> getTimeCaseMetadata = new ArrayList<>();
		File outputFile = new File(name);
		// Instantiating the PrintStream class
		PrintStream stream = new PrintStream(outputFile);

		try {
			long durationInNano;
			long durationInMillis;

			final DefaultBerkeleyStateBlockManagerImpl bsbm = managersByDatabase.get("CaseMetadata");
			final BerkeleyConfig conf = configsByDatabase.get("CaseMetadata");

			final WeakDefaultBerkeleyStateBlock stateBlock = bsbm.getStateBlock();
			
			final Database bdb = stateBlock.getDatabase(conf.getFilePath());
			final SecondaryDatabase secondaryDatabase = stateBlock.getSecondaryDatabase(conf.getSecondaryFilePath());

			SimpleMultiDataAccess<Integer, String, SessionCase> simpleDataAccess = (SimpleMultiDataAccess<Integer, String, SessionCase>) getBerkeleyMultiDataAccess(bdb,
					secondaryDatabase, conf.getKeyBinding(), conf.getSecondaryKeyBinding(), conf.getSessionObjectFactory());

			String header = "Key \t" + "nano sec \t" + "milli sec";
			stream.println(header);
			
			int found = 0;
			int missing = 0;
			
			while (sc.hasNextInt()) {
				// make call here
				int nextInt = sc.nextInt();
				long startTime = System.nanoTime();				
				
				SessionCase caseMetadata = simpleDataAccess.getValue(nextInt);
				
				if (caseMetadata == null)
				{
					missing++;
				}
				else
				{
					found++;
				}
				
				if (found % 1000 == 0)
				{
					System.out.println("Found " + found);
				}

				long endTime = System.nanoTime();

				durationInNano = (endTime - startTime); // Total execution time in nano seconds
				fileTime.add(durationInNano);
				durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
				
				String content = nextInt + "\t" + durationInNano + "\t" + durationInMillis;
				stream.println(content);
				
			}
			
			System.out.println("Found " + found + " out of " + (found + missing));
		} finally {
			stream.flush();
			stream.close();
			sc.close();
		}

		if (fileTime.size() > 0) {
			getTimeCaseMetadata
					.add(fileTime.stream().mapToLong(duration -> duration.longValue()).sum() / fileTime.size());
			getTimeCaseMetadata.add(
					TimeUnit.NANOSECONDS.toMillis(fileTime.stream().mapToLong(duration -> duration.longValue()).sum()));
			System.out.println("Average Time for 100K search Key calls in nanoseconds"
					+ (fileTime.stream().mapToLong(duration -> duration.longValue()).sum() / fileTime.size()));
			System.out.println("Total Time for 100K search Key calls in milliseconds" + TimeUnit.NANOSECONDS
					.toMillis(fileTime.stream().mapToLong(duration -> duration.longValue()).sum()));
		}

		return getTimeCaseMetadata;
	}
	
	public void retrieveSearchKeys() throws IOException {
		System.out.println("Here");
		List<Long> fileTime = new ArrayList<>();
		File file = new File("CaseMetadata100kGuids.txt");
		System.out.println(file.getAbsolutePath());
		Scanner sc = new Scanner(file);

		final List<Long> getTimeCaseMetadata = new ArrayList<>();
		File outputFile = new File("CaseMetadata100kGuidsToSearchKeys.txt" );
		// Instantiating the PrintStream class
		PrintStream stream = new PrintStream(outputFile);
		
		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		//System.out.println(timeStampPattern.format(java.time.LocalDateTime.now()));
		String name = "CasesMetadata_Seq_BDBSecondary_" + (timeStampPattern.format(LocalDateTime.now()));
		
		File outputFileRun = new File(name);
		// Instantiating the PrintStream class
		PrintStream streamRun = new PrintStream(outputFileRun);

		try {
			
			long durationInNano;
			long durationInMillis;
			final DefaultBerkeleyStateBlockManagerImpl bsbm = managersByDatabase.get("CaseMetadata");
			final BerkeleyConfig conf = configsByDatabase.get("CaseMetadata");

			final WeakDefaultBerkeleyStateBlock stateBlock = bsbm.getStateBlock();
			
			final Database bdb = stateBlock.getDatabase(conf.getFilePath());
			final SecondaryDatabase secondaryDatabase = stateBlock.getSecondaryDatabase(conf.getSecondaryFilePath());

			SimpleMultiDataAccess<Integer, String, SessionCase> simpleDataAccess = (SimpleMultiDataAccess<Integer, String, SessionCase>) getBerkeleyMultiDataAccess(bdb,
					secondaryDatabase, conf.getKeyBinding(), conf.getSecondaryKeyBinding(), conf.getSessionObjectFactory());

			int found = 0;
			
			String header = "Guid \t" + "SearchKey \t" + "nano sec \t" + "milli sec";
			
			while (sc.hasNextLine()) {
				// make call here
				String nextGuid = sc.nextLine().trim();
				long startTime = System.nanoTime();				
				SessionCase doc = simpleDataAccess.getSecondaryValue(nextGuid);
				long endTime = System.nanoTime();

				durationInNano = (endTime - startTime); // Total execution time in nano seconds
				fileTime.add(durationInNano);
				durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);
				if (doc != null)
				{
					String content = doc.getGuid() + "\t" + doc.getSearchKey() + "\t" + durationInNano + "\t" + durationInMillis;
					streamRun.println(content);
				
					stream.append(doc.getSearchKey() + " ");
					found++;
				}
				else
				{
					System.out.println("doc null for :" + nextGuid);
				}
				if (found % 1000 == 0)
				{
					System.out.println("Found " + found);
				}
				
			}
		} finally {
			stream.flush();
			stream.close();
			streamRun.flush();
			streamRun.close();
			sc.close();
		}
		
		if (fileTime.size() > 0) {
			getTimeCaseMetadata
					.add(fileTime.stream().mapToLong(duration -> duration.longValue()).sum() / fileTime.size());
			getTimeCaseMetadata.add(
					TimeUnit.NANOSECONDS.toMillis(fileTime.stream().mapToLong(duration -> duration.longValue()).sum()));
			System.out.println("Average Time for 100K search Key calls in nanoseconds"
					+ (fileTime.stream().mapToLong(duration -> duration.longValue()).sum() / fileTime.size()));
			System.out.println("Total Time for 100K search Key calls in milliseconds" + TimeUnit.NANOSECONDS
					.toMillis(fileTime.stream().mapToLong(duration -> duration.longValue()).sum()));
		}
	}

	private SimpleMultiDataAccess<?, ?, ?> getBerkeleyMultiDataAccess(Database database, SecondaryDatabase secondaryDatabase, final TupleBinding<?> keyBinding,
			final TupleBinding<?> secondaryKeyBinding, final SessionObjectFactory<?, ?> sessionObjectFactory) {
		return new BerkeleyMultiDataAccess(database, secondaryDatabase, keyBinding,
				secondaryKeyBinding, sessionObjectFactory);
	}

	protected SimpleDataAccess<?, ?> berkeleyEntries(final Database database, final TupleBinding<?> keyBinding,
			final SessionObjectFactory<?, ?> sessionObjectFactory) {
		return new BerkeleyDataAccess<>(database, keyBinding, sessionObjectFactory);
	}

	private SimpleDataAccess getBerkeleyDataAccess(Database database, BerkeleyConfig config) {
		return new BerkeleyDataAccess(//
				database, config.getKeyBinding(), config.getSessionObjectFactory());
	}
}
