package com.trgr.berkeleydb.search;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;

import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseClickstream;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.trialcourtorders.TrialCourtOrderMetadata;
import com.trgr.cobalt.search.dal.MultiIndexed;
import com.trgr.cobalt.search.svm.ScorableBase2;

public class SessionStco extends ScorableBase2<SessionStco, FeatureName> implements MultiIndexed<Integer, String> {
	private static final String CASE = "Case"; //$NON-NLS-1$
    private static final String TRIAL_ORDER = "TrialOrder"; //$NON-NLS-1$

    private final String type;
    private final boolean isTrialOrder;
    private final String[] citedCaseKeyNumbers;
    private final Integer[] citedCases;
    private final Integer[] citedStatutes;
    private final Integer[] coClickedCases;
    private final Float[] coClickedCasesCount;
    private final Float[] coClickedTCOCounts;
    private final Integer[] coClickedTCOs;
    private final int courtlineCode;
    private final int searchKey;
    private final int serialNumber;

    public SessionStco(final CaseMetadata caseMetadata, final CaseClickstream caseClickstream)
    {
        super(FeatureName.class, caseMetadata.getGuid());

        type = CASE;
        isTrialOrder = false;

        citedCases = caseMetadata.getCitedCases();
        citedStatutes = caseMetadata.getCitedStatute();
        courtlineCode = caseMetadata.getCourtlineCode();
        searchKey = caseMetadata.getSearchKey();
        //_secondaryKeys = caze.getSecondaryGuids();
        serialNumber = caseMetadata.getSerialNumber();

        final Set<String> citedCaseKeyNumbersSet = caseMetadata.getKeyNumberToCitingDocCounts().keySet();
        citedCaseKeyNumbers = citedCaseKeyNumbersSet.toArray(EMPTY_STRING_ARRAY);

        // Convert int[] to Integer[] for co-clicked cases
        if (caseClickstream != null)
        {
            final int[] coClickedCasesIds = caseClickstream.getRelatedCaseDocumentIds();
            coClickedCases = new Integer[coClickedCasesIds.length];
            for (int i = 0; i < coClickedCasesIds.length; i += 1)
            {
                coClickedCases[i] = coClickedCasesIds[i];
            }

            // Convert int[] to Float[] for co-clicked cases count
            final int[] coClickedCasesCountIds = caseClickstream.getRelatedCaseDocumentCounts();
            coClickedCasesCount = new Float[coClickedCasesCountIds.length];
            for (int i = 0; i < coClickedCasesCountIds.length; i += 1)
            {
                // may need to be normalized
                coClickedCasesCount[i] = (float) coClickedCasesCountIds[i];
            }

            coClickedTCOCounts = caseClickstream.getRelatedTCOCounts();
            coClickedTCOs = ArrayUtils.toObject(caseClickstream.getRelatedTCODocuments());
        }
        else
        {
            coClickedCases = EMPTY_INTEGER_OBJECT_ARRAY;
            coClickedCasesCount = EMPTY_FLOAT_OBJECT_ARRAY;

            coClickedTCOs = EMPTY_INTEGER_OBJECT_ARRAY;
            coClickedTCOCounts = EMPTY_FLOAT_OBJECT_ARRAY;
        }
    }

    public SessionStco(final TrialCourtOrderMetadata tcoMetadata)
    {
        super(FeatureName.class, tcoMetadata.getGuid());

        type = TRIAL_ORDER;
        isTrialOrder = true;

        courtlineCode = tcoMetadata.getCourtlineCode();
        searchKey = tcoMetadata.getSearchKey();
        serialNumber = tcoMetadata.getSerialNumber();

        if (tcoMetadata.getCitedCaseKeyNumbers() == null)
        {
            citedCaseKeyNumbers = EMPTY_STRING_ARRAY;
        }
        else
        {
            citedCaseKeyNumbers = tcoMetadata.getCitedCaseKeyNumbers();
        }

        if (tcoMetadata.getCitedCases() == null)
        {
            citedCases = EMPTY_INTEGER_OBJECT_ARRAY;
        }
        else
        {
            citedCases = tcoMetadata.getCitedCases();
        }

        if (tcoMetadata.getCitedStatutes() == null)
        {
            citedStatutes = EMPTY_INTEGER_OBJECT_ARRAY;
        }
        else
        {
            citedStatutes = tcoMetadata.getCitedStatutes();
        }

        if (tcoMetadata.getCoClickedCases() == null)
        {
            coClickedCases = EMPTY_INTEGER_OBJECT_ARRAY;
        }
        else
        {
            coClickedCases = tcoMetadata.getCoClickedCases();
        }
        if (tcoMetadata.getCoClickedCasesCount() == null)
        {
            coClickedCasesCount = EMPTY_FLOAT_OBJECT_ARRAY;
        }
        else
        {
            coClickedCasesCount = tcoMetadata.getCoClickedCasesCount();
        }

        if (tcoMetadata.getCoClickedTCOCounts() == null)
        {
            coClickedTCOCounts = EMPTY_FLOAT_OBJECT_ARRAY;
        }
        else
        {
            coClickedTCOCounts = tcoMetadata.getCoClickedTCOCounts();
        }

        if (tcoMetadata.getCoClickedTCOs() == null)
        {
            coClickedTCOs = EMPTY_INTEGER_OBJECT_ARRAY;
        }
        else
        {
            coClickedTCOs = tcoMetadata.getCoClickedTCOs();
        }
    }

    public SessionStco(final int searckKey, final String guid)
    {
        super(FeatureName.class, guid);

        type = TRIAL_ORDER;
        isTrialOrder = true;

        searchKey = searckKey;
        serialNumber = searckKey;

        citedCaseKeyNumbers = EMPTY_STRING_ARRAY;
        citedCases = EMPTY_INTEGER_OBJECT_ARRAY;
        citedStatutes = EMPTY_INTEGER_OBJECT_ARRAY;
        coClickedCases = EMPTY_INTEGER_OBJECT_ARRAY;
        coClickedCasesCount = EMPTY_FLOAT_OBJECT_ARRAY;
        coClickedTCOCounts = EMPTY_FLOAT_OBJECT_ARRAY;
        coClickedTCOs = EMPTY_INTEGER_OBJECT_ARRAY;
        courtlineCode = -1;
    }

    @Override
    public int compareTo(final SessionStco that)
    {
        return serialNumber - that.serialNumber;
    }

    //

    public String[] getCitedCaseKeyNumbers()
    {
        return citedCaseKeyNumbers;
    }

    public Integer[] getCitedCases()
    {
        return citedCases;
    }

    public Integer[] getCitedStatutes()
    {
        return citedStatutes;
    }

    public Integer[] getCoClickedCases()
    {
        return coClickedCases;
    }

    public Float[] getCoClickedCasesCount()
    {
        return coClickedCasesCount;
    }

    public Float[] getCoClickedTCOCounts()
    {
        return coClickedTCOCounts;
    }

    public Integer[] getCoClickedTCOs()
    {
        return coClickedTCOs;
    }

    public int getCourtlineCode()
    {
        return courtlineCode;
    }

    @Override
    public String getLoggingId()
    {
        return Integer.toString(getSerialNumber());
    }

    @Override
    public Integer getPrimaryKey()
    {
        return searchKey;
    }

    public int getSearchKey()
    {
        return searchKey;
    }

    @Override
    public String[] getSecondaryKeys()
    {
        return new String[] {getGuid()};
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public String getType()
    {
        return type;
    }

    public boolean isCase()
    {
        return !isTrialOrder;
    }

    public boolean isTrialOrder()
    {
        return isTrialOrder;
    }
}
