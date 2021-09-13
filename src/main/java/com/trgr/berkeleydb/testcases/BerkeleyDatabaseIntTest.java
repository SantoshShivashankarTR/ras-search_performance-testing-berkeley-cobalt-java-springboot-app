/*
 * package com.trgr.berkeleydb.testcases;
 * 
 * import static org.fest.assertions.Assertions.assertThat;
 * 
 * import java.util.Iterator; import java.util.List;
 * 
 * import com.sleepycat.db.Database; import org.junit.Test; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Qualifier;
 * 
 * import com.trgr.berkeleydb.config.DefaultBerkeleyStateBlockManagerImpl;
 * import com.trgr.cobalt.search.berkeley.BerkeleyConfig; import
 * com.trgr.cobalt.search.berkeley.BerkeleyDataAccess; import
 * com.trgr.cobalt.search.berkeley.BerkeleyStateBlockManager; import
 * com.trgr.cobalt.search.berkeley.WeakDefaultBerkeleyStateBlock; import
 * com.trgr.cobalt.search.concurrent.QueuingExecutor; import
 * com.trgr.cobalt.search.dal.SimpleDataAccess; import
 * com.trgr.cobalt.search.dal.UnifiedDataAccessObject;
 * 
 * public final class BerkeleyDatabaseIntTest { protected final QueuingExecutor
 * jmxExecutor = null;
 * 
 * @Autowired
 * 
 * @Qualifier("SearchBerkeleyStateBlockManager") private
 * List<DefaultBerkeleyStateBlockManagerImpl> stateBlockManager;
 * 
 * @Test public <K, V> void testDatabases() { final
 * WeakDefaultBerkeleyStateBlock weakFermiStateBlock =
 * ((DefaultBerkeleyStateBlockManagerImpl) stateBlockManager).getStateBlock();
 * 
 * for (final BerkeleyConfig config : ((BerkeleyStateBlockManager)
 * stateBlockManager).getConfigs()) { final Database database =
 * weakFermiStateBlock.getDatabase(config.getFilePath());
 * 
 * final SimpleDataAccess<K, V> berkeley = new BerkeleyDataAccess<K, V>(//
 * database, config.getKeyBinding(), config.getSessionObjectFactory());
 * 
 * final SimpleDataAccess<K, V> udao = new UnifiedDataAccessObject<>(berkeley,
 * jmxExecutor);
 * 
 * final Iterator<V> it = udao.iterator();
 * 
 * System.out.println("it" + it.toString()); assertThat(it).isNotNull();
 * 
 * if (it.hasNext()) assertThat(it.next()).isNotNull(); } } }
 */