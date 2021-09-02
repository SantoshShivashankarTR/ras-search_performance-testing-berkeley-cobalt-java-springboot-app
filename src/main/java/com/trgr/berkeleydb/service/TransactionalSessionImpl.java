package com.trgr.berkeleydb.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sleepycat.db.Database;
import com.sleepycat.db.SecondaryDatabase;
import org.apache.log4j.Logger;

import com.trgr.cobalt.search.berkeley.BerkeleyConfig;
import com.trgr.cobalt.search.berkeley.BerkeleyDataAccess;
import com.trgr.cobalt.search.berkeley.BerkeleyMultiDataAccess;
import com.trgr.cobalt.search.berkeley.BerkeleyWritableDataAccess;
import com.trgr.cobalt.search.berkeley.BerkeleyWritableMultiDataAccess;
import com.trgr.cobalt.search.berkeley.WeakDefaultBerkeleyStateBlock;
import com.trgr.cobalt.search.concurrent.QueuingExecutor;
import com.trgr.cobalt.search.dal.DataAccess;
import com.trgr.cobalt.search.dal.MultiDataAccess;
import com.trgr.cobalt.search.dal.MultiIndexed;
import com.trgr.cobalt.search.dal.SimpleDataAccess;
import com.trgr.cobalt.search.dal.SimpleMultiDataAccess;
import com.trgr.cobalt.search.dal.UnifiedDataAccessObject;
import com.trgr.cobalt.search.dal.UnifiedMultiDataAccessObject;
import com.trgr.cobalt.search.web.session.TransactionalSession;

@SuppressWarnings("rawtypes")
class TransactionalSessionImpl implements TransactionalSession
{
    private static final Logger LOG = Logger.getLogger(TransactionalSessionImpl.class);

    private String sessionId = "";
    private String transactionId = "";

    TransactionalSessionImpl(
        final WeakDefaultBerkeleyStateBlock stateBlock,
        final Collection<BerkeleyConfig> configs,
        final QueuingExecutor executor,
        final String transactionId,
        final String sessionId)
    {
        this.sessionId = sessionId;
        this.transactionId = transactionId;
        loadDataAccessMappings(dataAccess, stateBlock, configs, executor);
    }

    @Override
    public String getSessionId()
    {
        return sessionId;
    }

    @Override
    public String getTransactionId()
    {
        return transactionId;
    }

    @Override
    public <K, V> DataAccess<K, V> getDataAccessFor(final Class<V> daoType, final Class<?>... typeParams)
    {
        if (typeParams.length < 1)
        {
            return dataAccess.get(daoType);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public <PK, SK, V extends MultiIndexed<PK, SK>> MultiDataAccess<PK, SK, V> getMultiDataAccessFor(
        final Class<V> daoType, final Class<?>... typeParams)
    {
        if (typeParams.length < 1)
        {
            return (MultiDataAccess<PK, SK, V>) dataAccess.get(daoType);
        }
        throw new UnsupportedOperationException();
    }

    //--------------------------------------------------------------------------

    protected final Map<Class, DataAccess> dataAccess = new LinkedHashMap<>();

    protected static void loadDataAccessMappings(
        final Map<Class, DataAccess> dataAccess,
        final WeakDefaultBerkeleyStateBlock stateBlock,
        final Collection<BerkeleyConfig> configs,
        final QueuingExecutor executor)
    {
        final boolean record = "record".equals(System.getProperty("db_record"));
        if (record) LOG.info("Notice: recording berkeley databases.");

        for (final BerkeleyConfig config : configs)
        {
            if (LOG.isDebugEnabled()) LOG.debug("database: " + config.getFilePath());
            final Database database = stateBlock.getDatabase(config.getFilePath());

            final DataAccess dal;
            if (isBlank(config.getSecondaryFilePath()))
            {
                final SimpleDataAccess berkeley;
                if (!record)
                {
                    berkeley = new BerkeleyDataAccess(//
                        database,
                        config.getKeyBinding(),
                        config.getSessionObjectFactory());
                }
                else
                {
                    berkeley = new BerkeleyWritableDataAccess(
                        database,
                        config.getKeyBinding(),
                        config.getSessionObjectFactory(),
                        stateBlock.getWritableDatabase(config.getFilePath()));

                    LOG.info("Recording berkeley: " + config.getFilePath());
                }

                dal = new UnifiedDataAccessObject(berkeley, executor);
            }
            else
            {
                final SecondaryDatabase secondaryDatabase = stateBlock.getSecondaryDatabase(config.getSecondaryFilePath());
                final SimpleMultiDataAccess berkeleyMultiDataAccess;
                if (!record)
                {
                    berkeleyMultiDataAccess = new BerkeleyMultiDataAccess(
                        database,
                        secondaryDatabase,
                        config.getKeyBinding(),
                        config.getSecondaryKeyBinding(),
                        config.getSessionObjectFactory());
                }
                else
                {
                    berkeleyMultiDataAccess = new BerkeleyWritableMultiDataAccess(
                        database,
                        secondaryDatabase,
                        config.getKeyBinding(),
                        config.getSecondaryKeyBinding(),
                        config.getSessionObjectFactory(),
                        stateBlock.getWritableDatabase(config.getFilePath()),
                        stateBlock.getWritableSecondaryDatabase(config.getSecondaryFilePath()));

                    LOG.info("Recording berkeley: " + config.getFilePath() + '\t' + config.getSecondaryFilePath());
                }

                dal = new UnifiedMultiDataAccessObject(berkeleyMultiDataAccess, executor);
            }

            dataAccess.put(config.getClassToCreate(), dal);
        }
    }
}
