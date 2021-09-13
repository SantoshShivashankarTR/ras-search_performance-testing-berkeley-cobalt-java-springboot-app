package com.trgr.berkeleydb.repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trgr.berkeleydb.search.SessionCase;
import com.trgr.berkeleydb.testcases.DBRepository;
import com.trgr.berkeleydb.testcases.data.Result;
import com.trgr.cobalt.search.dal.DataAccess;
import com.trgr.cobalt.search.dal.MultiDataAccess;
import com.trgr.cobalt.search.dal.MultiIndexed;
import com.trgr.cobalt.search.web.session.TransactionalSessionFactory;

public class DBRepositoryBerkeley implements DBRepository<String, Result> {
	private static final Logger log = LoggerFactory.getLogger(DBRepositoryBerkeley.class);

	private final Map<Class<?>, DataAccess<?, ?>> dataAccess = new LinkedHashMap<>();

	private final TransactionalSessionFactory transactionalSessionFactory;

	public DBRepositoryBerkeley(TransactionalSessionFactory transactionalSessionFactory) {
		this.transactionalSessionFactory = transactionalSessionFactory;
	}

	@Override
	public Map<String, Result> getSValues(Iterable<String> keys) {

		// Cases
		// MultiDataAccess<Integer, String, SessionCase> caseDal =
		// getMultiDataAccessFor(SessionCase.class);
		MultiDataAccess<Integer, String, SessionCase> caseDal = transactionalSessionFactory.newInstance()
				.getMultiDataAccessFor(SessionCase.class);
		if (caseDal != null) {
			Map<String, SessionCase> guidToCase = caseDal.getSecondaryValues(keys);
			log.info("Results: " + guidToCase);
		}
		
		//TODO Firstly need to implement a single call to Berkeley

		Map<String, Result> res = new HashMap<>();
		res.put("Status", null);

		// TODO

		return res;
	}

	@SuppressWarnings("unchecked")
	private <PK, SK, V extends MultiIndexed<PK, SK>> MultiDataAccess<PK, SK, V> getMultiDataAccessFor(
			final Class<V> daoType) {
		return (MultiDataAccess<PK, SK, V>) dataAccess.get(daoType);
	}

	public void addDataAccess(Class<?> key, DataAccess<?, ?> dal) {
		dataAccess.put(key, dal);
	}
}
