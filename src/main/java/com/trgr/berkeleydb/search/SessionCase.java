package com.trgr.berkeleydb.search;

import java.util.Date;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;

import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseClickstream;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.CaseMetadata;
import com.trgr.cobalt.dw.objects.reportbuilderdocs.cases.KeyCiteFlag;
import com.trgr.cobalt.search.svm.data.ScorableCase;
import com.trgr.cobalt.search.util.GuidUtil;

public class SessionCase extends ScorableCase<FeatureName> {
	private boolean ocbReranked;

    public SessionCase(final CaseMetadata caseMetadata)
    {
        super(FeatureName.class, caseMetadata);
    }

    @VisibleForTesting
    public SessionCase(final int searchKey)
    {
        this(searchKey, GuidUtil.buildPaddedGuid(searchKey));
    }

    @VisibleForTesting
    public SessionCase(final int searchKey, final String guid)
    {
        this(newCaseMetadata(searchKey, guid));
    }

    private static CaseMetadata newCaseMetadata(final int searchKey, final String guid)
    {
        final CaseMetadata caseMetadata = new CaseMetadata(searchKey);
        caseMetadata.setSerialNumber(searchKey);

        caseMetadata.setGuid(guid);
        caseMetadata.setSecondaryGuids(new String[] {guid});

        return caseMetadata;
    }

    //

    public double getAgedKeyciteCiteCountFeature()
    {
        return getBerkeleyObject().getAgedKeyciteCiteCountFeature();
    }

    public short[] getBriefCourtlineCodes()
    {
        return getBerkeleyObject().getBriefCourtlineCodes();
    }

    public Integer[] getCitedAnalytical()
    {
        return getBerkeleyObject().getCitedAnalytical();
    }

    public Integer[] getCitedBrief()
    {
        return getBerkeleyObject().getCitedBrief();
    }

    public Integer[] getCitedCases()
    {
        return getBerkeleyObject().getCitedCases();
    }

    public int getCitedDocumentsCount()
    {
        return getBerkeleyObject().getCitedDocumentsCount();
    }

    public Integer[] getCitingAnalyticals()
    {
        return getBerkeleyObject().getCitingAnalyticals();
    }

    public int getCitingAnalyticalsCount()
    {
        return getBerkeleyObject().getCitingAnalyticalsCount();
    }

    public Integer[] getCitingBriefs()
    {
        return getBerkeleyObject().getCitingBriefs();
    }

    public int getCitingBriefsCount()
    {
        return getBerkeleyObject().getCitingBriefsCount();
    }

    public Integer[] getCitingCases()
    {
        return getBerkeleyObject().getCitingCases();
    }

    public int getCitingCasesCount()
    {
        return getBerkeleyObject().getCitingCasesCount();
    }

    public int getCitingDocumentsCount()
    {
        return getBerkeleyObject().getCitingDocumentsCount();
    }

    public int getCitingPmmCount()
    {
        return getBerkeleyObject().getCitingPmmCount();
    }

    public int getCourtLevel()
    {
        return getBerkeleyObject().getCourtLevel();
    }

    public int getCourtlineCode()
    {
        return getBerkeleyObject().getCourtlineCode();
    }

    public double getFederalCourtLevelFeature()
    {
        return getBerkeleyObject().getFederalCourtLevelFeature();
    }

    public Date getFiledDate()
    {
        return getBerkeleyObject().getFiledDate();
    }

    public Integer[] getHeadnoteCounts()
    {
        return getBerkeleyObject().getHeadnoteCounts();
    }

    public KeyCiteFlag getKeyCiteFlag()
    {
        return getBerkeleyObject().getKeyCiteFlag();
    }

    public String[] getKeyNumbers()
    {
        return getBerkeleyObject().getKeyNumberCodes();
    }

    public Map<String, Integer> getKeyNumbersCited()
    {
        return getBerkeleyObject().getKeyNumberToCitingDocCounts();
    }

    public double getOtherCourtLevelFeature()
    {
        return getBerkeleyObject().getOtherCourtLevelFeature();
    }

    public short[] getPmmCourtlineCodes()
    {
        return getBerkeleyObject().getPmmCourtlineCodes();
    }

    public int getSearchKey()
    {
        return getBerkeleyObject().getSearchKey();
    }

    public double getSimpleKeyciteCiteCountFeature()
    {
        return getBerkeleyObject().getSimpleKeyciteCiteCountFeature();
    }

    public double getStateCourtLevelFeature()
    {
        return getBerkeleyObject().getStateCourtLevelFeature();
    }

    public boolean isImportant()
    {
        return getBerkeleyObject().getIsImportantCase();
    }

    public boolean isOcbReranked()
    {
        return ocbReranked;
    }

    //

    public void setAgedKeyciteCiteCountFeature()
    {
        getBerkeleyObject().setAgedKeyciteCiteCountFeature();
    }

    public void setAgedKeyciteCiteCountFeature(final double agedKeyciteCiteCount)
    {
        getBerkeleyObject().setAgedKeyciteCiteCountFeature(agedKeyciteCiteCount);
    }

    public void setBriefCourtlineCodes(final short[] briefCourtlines)
    {
        getBerkeleyObject().setBriefCourtlineCodes(briefCourtlines);
    }

    public void setCitedAnalytical(final Integer[] citedAnalytical)
    {
        getBerkeleyObject().setCitedAnalytical(citedAnalytical);
    }

    public void setCitedBrief(final Integer[] citedBrief)
    {
        getBerkeleyObject().setCitedBrief(citedBrief);
    }

    public void setCitedCases(final Integer[] citedCases)
    {
        getBerkeleyObject().setCitedCases(citedCases);
    }

    public void setCitingAnalyticals(final Integer[] citingAnalyticals)
    {
        getBerkeleyObject().setCitingAnalyticals(citingAnalyticals);
    }

    public void setCitingBriefs(final Integer[] citingBriefs)
    {
        getBerkeleyObject().setCitingBriefs(citingBriefs);
    }

    public void setCitingCases(final Integer[] citingCases)
    {
        getBerkeleyObject().setCitingCases(citingCases);
    }

    public void setCitingPmmCount(final int count)
    {
        getBerkeleyObject().setCitingPmmCount(count);
    }

    public void setCourtLevel(final int courtLevel)
    {
        getBerkeleyObject().setCourtLevel(courtLevel);
    }

    public void setCourtlineCode(final int courtlineCode)
    {
        getBerkeleyObject().setCourtlineCode(courtlineCode);
    }

    public void setFiledDate(final Date date)
    {
        getBerkeleyObject().setFiledDate(date);
    }

    public void setHeadnoteCounts(final Integer[] hnoteCounts)
    {
        getBerkeleyObject().setHeadnoteCounts(hnoteCounts);
    }

    public void setKeyCiteFlag(final String keyCiteFlag)
    {
        getBerkeleyObject().setKeyCiteFlag(keyCiteFlag);
    }

    public void setKeyNumbers(final String[] keyNoCode)
    {
        getBerkeleyObject().setKeyNumberCodes(keyNoCode);
    }

    public void setKeyNumberToCitingDocCounts(final Map<String, Integer> keyNumberMap)
    {
        getBerkeleyObject().setKeyNumberToCitingDocCounts(keyNumberMap);
    }

    public void setOcbReranked(final boolean ocbReranked)
    {
        this.ocbReranked = ocbReranked;
    }

    public void setPmmCourtlineCodes(final short[] pmmCourtlines)
    {
        getBerkeleyObject().setPmmCourtlineCodes(pmmCourtlines);
    }

    public void setSimpleKeyciteCiteCountFeature(final double count)
    {
        getBerkeleyObject().setSimpleKeyciteCiteCountFeature(count);
    }

    //--------------------------------------------------------------------------

    private volatile SessionStco stco;

    public SessionStco toSessionStco(final CaseClickstream caseClickstream)
    {
        if (stco == null)
        {
            synchronized (this)
            {
                if (stco == null)
                {
                    stco = new SessionStco(getBerkeleyObject(), caseClickstream);
                }
            }
        }
        return stco;
    }
}
