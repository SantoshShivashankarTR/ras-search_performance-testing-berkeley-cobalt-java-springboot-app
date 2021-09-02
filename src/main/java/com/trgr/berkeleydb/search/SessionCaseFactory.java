package com.trgr.berkeleydb.search;

import com.sleepycat.bind.tuple.TupleBinding;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata;
import com.trgr.cobalt.search.berkeley.sessionobjects.SessionObjectFactory;

public class SessionCaseFactory extends SessionObjectFactory<CaseMetadata, SessionCase>
{
    public SessionCaseFactory(final TupleBinding<CaseMetadata> valueBinding)
    {
        super(valueBinding);
    }

    @Override
    public SessionCase createSessionObject(final CaseMetadata dwObject)
    {
        return new SessionCase(dwObject);
    }
}