package com.trgr.berkeleydb.service;

import static com.trgr.cobalt.infrastructure.logging.GUID.guid;

import com.trgr.berkeleydb.config.DefaultBerkeleyStateBlockManagerImpl;
import com.trgr.cobalt.search.berkeley.BerkeleyStateBlockManager;
import com.trgr.cobalt.search.berkeley.DefaultBerkeleyStateBlockManager;
import com.trgr.cobalt.search.concurrent.QueuingExecutor;
import com.trgr.cobalt.search.web.session.TransactionalSession;
import com.trgr.cobalt.search.web.session.TransactionalSessionFactory;

public class TransactionalSessionFactoryImpl implements TransactionalSessionFactory
{
    protected final QueuingExecutor executor;
    protected final DefaultBerkeleyStateBlockManagerImpl manager;

    public TransactionalSessionFactoryImpl(final QueuingExecutor executor, final DefaultBerkeleyStateBlockManagerImpl defaultBerkeleyStateBlockManagerImpl)
    {
        this.executor = executor;
        this.manager = defaultBerkeleyStateBlockManagerImpl;
    }

    @Override
    public TransactionalSession newInstance()
    {
        return newInstance(guid(), guid());
    }

	@Override
	public TransactionalSession newInstance(String transactionGuid, String sessionGuid) {
		return new TransactionalSessionImpl(manager.getStateBlock(), manager.getConfigs(), executor, transactionGuid, sessionGuid);

	}

}