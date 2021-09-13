package com.trgr.berkeleydb.search;

import static com.google.common.collect.ImmutableSet.copyOf;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.trgr.cobalt.search.norm.ApproximationNormalizer;
import com.trgr.cobalt.search.norm.FactorNormalizer;
import com.trgr.cobalt.search.norm.FactorPowerNormalizer;
import com.trgr.cobalt.search.norm.FormsNormalizer;
import com.trgr.cobalt.search.norm.GammaNormalizer;
import com.trgr.cobalt.search.norm.LogarithmicNormalizer;
import com.trgr.cobalt.search.norm.MinMaxNormalizer;
import com.trgr.cobalt.search.norm.Normalizer;
import com.trgr.cobalt.search.norm.PolyfunctionalModelNormalizer;
import com.trgr.cobalt.search.norm.PowerNormalizer;
import com.trgr.cobalt.search.norm.Sigmoid2dModelNormalizer;

public enum FeatureName {
	AubAverageRank,

    AubCitelistFrequency,

    AubViewFrequency,

    AubPrintFrequency,

    AubCiteFrequency,

    AubViewPrintCiteFrequency,

    AubNextClickedFrequency,

    AubPreviousClickedFrequency,

    AubPreviousNextClickedFrequency,

    AubFromHyperlinkFindFrequency,

    AubFromOtherFindFrequency,

    AubToHyperlinkFindFrequency,

    AubRevistedFrequency,

    AubFirstClickedFrequency,

    AubWeightedSum,

    StcoCaseHistory,

    StcoType,

    StcoClickstreamBinaryCourtOrderJuris,

    StcoClickstreamNumberOfSourcesCourtOrderJuris,

    StcoClickstreamMaxCrb,

    StcoClickstreamMaxCourtOrderJuris,

    StcoClickstreamBinaryCrb,

    StcoClickstreamNumberOfSourcesCrb,

    StcoJurisScore,

    StatutesKeyCiteAllCasesHn,

    StatutesKeyCiteJurisHn,

    StatutesKeyCiteNodAllCasesHn,

    StatutesKeyCiteNodJurisHn,

    StatutesKeyCiteNodKnfFilteredAllCasesHn,

    StatutesKeyCiteNodKnfFilteredJurisHn,

    StatutesKeyCiteKnfFilteredAllCasesHn,

    StatutesKeyCiteKnfFilteredJurisHn,

    CareRank,

    CareScore,

    AllCasesDatabaseCount(new FactorNormalizer(100.0)),

    AllCasesDatabaseRank(new FactorPowerNormalizer(0.8)),

    AllHeadnoteDatabaseCount(new FactorNormalizer(100.0)),

    AllHeadnoteDatabaseRank(new FactorPowerNormalizer(0.7)),

    Confidence(new FactorNormalizer(5.0)),

    JurisdictionCaseCount(new FactorNormalizer(100.0)),

    JurisdictionCaseRank(new FactorPowerNormalizer(0.8)),

    JurisdictionHeadnoteCount(new FactorNormalizer(100.0)),

    JurisdictionHeadnoteRank(new FactorPowerNormalizer(0.7)),

    IsOtherCourt,

    IsFederalCourtLevel,

    IsStateCourtLevel,

    SimpleKeyciteCiteCount(new GammaNormalizer(false)),

    AgedKeyciteCiteCount(new LogarithmicNormalizer(0.0, 10.0 / Math.log(10.0))),

    KeyciteRedFlag,

    KeyciteYellowFlag,

    KeyciteCitedFlag,

    KeyciteHistoryFlag,

    KnfOverlapNorm(new PowerNormalizer(1.0, -1.0, 0.7)),

    KnfKNCosine,

    KnfKNRecall,

    KnfKNPrecision,

    KNOverlapNorm(new PowerNormalizer(1.0, -1.0, 0.7)),

    KNCosine,

    KNRecall,

    KNPrecision,

    KnfKNCitedCountByKeyNumber(new LogarithmicNormalizer(10.0, 10.0)),

    KnfKNCitedCountByCase,

    KnfKNRank,

    InSearchResult_ALLCASE,

    SearchRank_ALLCASE(new PowerNormalizer(0.0, 1.0, 0.8)),

    SearchResultFraction_ALLCASE,

    InSearchResult_CASE,

    SearchRank_CASE(new PowerNormalizer(0.0, 1.0, 0.8)),

    SearchResultFraction_CASE,

    InSearchResult_HEADNOTE(new PowerNormalizer(1.0, -1.0, 0.5)),

    SearchRank_HEADNOTE(new PowerNormalizer(0.0, 1.0, 0.8)),

    SearchResultFraction_HEADNOTE,

    InSearchResult_ALLHEADNOTE(new PowerNormalizer(1.0, -1.0, 0.5)),

    SearchRank_ALLHEADNOTE(new PowerNormalizer(0.0, 1.0, 0.8)),

    SearchResultFraction_ALLHEADNOTE,

    ClickSources,

    ClickFeatureCumulativeCoClicks(new GammaNormalizer(false)),

    ClickFeatureMinConf(new ApproximationNormalizer(false)),

    ClickFeatureMaxConf(new ApproximationNormalizer(false)),

    ClickFeatureAvgConf,

    ClickFeatureKhalid,

    NumCoCitedByCases(new LogarithmicNormalizer(0.0, 4.0)),

    NumCoCitedByCasesNormBySrc(new LogarithmicNormalizer(3.0, 4.0)),

    NumCoCitedCasesNormBySrc(new LogarithmicNormalizer(4.0, 4.0)),

    x2sourcePrec(new LogarithmicNormalizer(5.0, 5.0)),

    NumSourcesCiting,

    x2candRec(new LogarithmicNormalizer(5.0, 5.0)),

    NumBriefsThatCiteMeAndSource(new MinMaxNormalizer()),

    TotalNumSourcesThatMutualBriefsCites(new MinMaxNormalizer()),

    NumAnalyticThatCiteMeAndSource(new MinMaxNormalizer()),

    TotalNumSourcesThatMutualAnalyticCites(new MinMaxNormalizer()),

    NumUnknownsThatCiteMeAndSource(new MinMaxNormalizer()),

    TotalNumSourcesThatMutualUnknownCites(new MinMaxNormalizer()),

    NumSources,

    StatutesSearchScore,

    StatutesCaseNotesOfDecisionCount,

    StatutesHeadnoteKeyNumberCount(new LogarithmicNormalizer(0.01, 3.5)),

    StatutesLibraryReferenceKeyNumberCount,

    StatutesKeyNumberTopicLibararyReferenceCount,

    StatutesKeyNumberParentLibraryReferenceCount,

    StatutesCaseKeyNumberNotesOfDecisionCount,

    StatutesKeyNumberCountDistinct(new LogarithmicNormalizer(1.5, 1.5)),

    StatutesKeyNumberCountWeighted(new LogarithmicNormalizer(0.0, 5.0)),

    StatutesKeyNumberConditionalProbability,

    StatutesSearchReranked,

    KeyciteScore,

    JurisdictionScore,

    ClickstreamSources,

    ClickstreamRankOfCoClicks,

    AverageConfidenceLogicalSearch,

    KeynumberScore,

    SearchCite,

    JurisScore(new PolyfunctionalModelNormalizer()),

    SupremeCourtScore(new Sigmoid2dModelNormalizer()),

    TrialDocOrder,

    TrialDocSource,

    TrialDocCRB,

    StatutesClickAvgConfARB,

    StatutesClickAvgConfCF,

    StatutesClickAvgConfSTATUTE,

    StatutesCumCoClicksARB(new GammaNormalizer(false)),

    StatutesCumCoClicksCF(new GammaNormalizer(false)),

    StatutesCumCoClicksREGULATION(new GammaNormalizer(false)),

    StatutesCumCoClicksSTATUTE(new GammaNormalizer(false)),

    StatutesClickSourcesARB,

    StatutesClickSourcesCF,

    StatutesClickSourcesREGULATION,

    StatutesKeyciteTrialDocPMM,

    StatutesKeyciteTrialDocBRIEF,

    StatutesSearchReranked_1,

    StatutesSearchReranked_2,

    StatutesKNNODCount,

    StatutesIndexScore,

    StatutesIndexRank,

    StatuteSearchFrequency,

    StatuteAnalyticCitation,

    StatutesClickSourcesSTATUTE,

    PmmJurisScore,

    KeyciteCRB,

    KeyciteFilteredCRB,

    PmmRelationCRB,

    KeyciteCaseKeynumber,

    KeyciteStatute,

    RelationPmmJuris,

    PmmCaseAggJurisScore,

    PmmCaseAggKeyciteCRB,

    PmmCaseAggKeyciteStatute,

    CaseAggRelationPmmJuris,

    PmmCaseAggKeyciteCaseKeynumber,

    PmmCaseAggCombinedProb,

    LocateAuthWestLawRank,

    LocateAuthWestlawScore,

    LocateAuthLociRank,

    LocateAuthLociScore,

    LocateAuthYear,

    LocateAuthCourtLevel,

    LocateAuthCaseCites,

    LocateAuthBriefCites,

    LocateAuthTotalCites,

    RegulationActivityPrint,

    RegulationActivityView,

    RegulationActivityKeycite,

    RegulationNODCases,

    RegulationCitedOthers,

    RegulationCitedReg,

    RegulationCitingOthers,

    RegulationCitingReg,

    RegulationCoclickSrcOthers,

    RegulationCoclickTrgOthers,

    RegulationCoclickReg,

    RegulationSiblings,

    RegulationKNTopicOverlap,

    RegulationNODKNSim,

    RegulationNODTopicSim,

    RegulationRefOthers,

    RegulationRefRegs,

    RegulationSearchRank,

    RegulationSearchScore,

    AnalyticalNLScore,

    AnalyticalNLMaxScore,

    KeyciteRecommends,

    SearchRecommends,

    // auto-registered features (order matters):

    BigFormsArbFermiScore(new FormsNormalizer()),
    BigFormsSrbFermiScore(new FormsNormalizer()),

    PatentFullRank,
    PatentAbstractRank,
    PatentFirstClaimRank,
    PatentTitleRank,
    PatentClaimsRank,
    PatentIPCScore,
    PatentCiteScore,
    PatentIPCLevelOne,
    PatentIPCLevelTwo,
    PatentIPCLevelThree,
    PatentIPCLevelFour,
    PatentIPCLevelFive,
    PatentIPCAllScore,
    PatentRankFullByAbstract,
    PatentRankFullByFirstClaim,
    PatentRankFullByClaims,
    PatentRankAbstractByFull,
    PatentRankFirstClaimByFull,
    PatentRankAllClaimByFull;

    //--------------------------------------------------------------------------

    public enum BaseFeatureName
    {
        InSearchResult,
        SearchRank,
        SearchResultFraction,
    }

    private static final String ALLCASE_TYPE = "_" + ContentTypeNames.ALLCASE; //$NON-NLS-1$
    private static final String ALLHEADNOTE_TYPE = "_" + ContentTypeNames.ALLHEADNOTE; //$NON-NLS-1$
    private static final String CASE_TYPE = "_" + ContentTypeNames.CASE; //$NON-NLS-1$
    private static final String HEADNOTE_TYPE = "_" + ContentTypeNames.HEADNOTE; //$NON-NLS-1$

    private static final Map<String, FeatureName> CONTENT_NAMES = ImmutableMap.<String, FeatureName>builder()
        .put(BaseFeatureName.InSearchResult + ALLCASE_TYPE, InSearchResult_ALLCASE)
        .put(BaseFeatureName.SearchRank + ALLCASE_TYPE, SearchRank_ALLCASE)
        .put(BaseFeatureName.SearchResultFraction + ALLCASE_TYPE, SearchResultFraction_ALLCASE)
        .put(BaseFeatureName.InSearchResult + CASE_TYPE, InSearchResult_CASE)
        .put(BaseFeatureName.SearchRank + CASE_TYPE, SearchRank_CASE)
        .put(BaseFeatureName.SearchResultFraction + CASE_TYPE, SearchResultFraction_CASE)
        .put(BaseFeatureName.InSearchResult + HEADNOTE_TYPE, InSearchResult_HEADNOTE)
        .put(BaseFeatureName.SearchRank + HEADNOTE_TYPE, SearchRank_HEADNOTE)
        .put(BaseFeatureName.SearchResultFraction + HEADNOTE_TYPE, SearchResultFraction_HEADNOTE)
        .put(BaseFeatureName.InSearchResult + ALLHEADNOTE_TYPE, InSearchResult_ALLHEADNOTE)
        .put(BaseFeatureName.SearchRank + ALLHEADNOTE_TYPE, SearchRank_ALLHEADNOTE)
        .put(BaseFeatureName.SearchResultFraction + ALLHEADNOTE_TYPE, SearchResultFraction_ALLHEADNOTE)
        .build();

    private static final BiMap<String, FeatureName> RESEARCH_NAMES = HashBiMap.create(200);
    static
    {
        RESEARCH_NAMES.put("RPRank", CareRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("RPScore", CareScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("allCaseDBCount", AllCasesDatabaseCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("allCaseDBRank", AllCasesDatabaseRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("allHeadnoteDBCount", AllHeadnoteDatabaseCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("allHeadnoteDBRank", AllHeadnoteDatabaseRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("jurCaseDBCount", JurisdictionCaseCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("jurCaseDBRank", JurisdictionCaseRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("jurHeadnoteDBCount", JurisdictionHeadnoteCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("jurHeadnoteDBRank", JurisdictionHeadnoteRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("confScore", Confidence); //$NON-NLS-1$

        RESEARCH_NAMES.put("isOtherCourt", IsOtherCourt); //$NON-NLS-1$
        RESEARCH_NAMES.put("FederalCourtLevel", IsFederalCourtLevel); //$NON-NLS-1$
        RESEARCH_NAMES.put("StateCourtLevel", IsStateCourtLevel); //$NON-NLS-1$
        RESEARCH_NAMES.put("SimpleKeyciteCiteCount", SimpleKeyciteCiteCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("AgedKeyciteCiteCount", AgedKeyciteCiteCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("KeyciteRedFlag", KeyciteRedFlag); //$NON-NLS-1$
        RESEARCH_NAMES.put("KeyciteYellowFlag", KeyciteYellowFlag); //$NON-NLS-1$
        RESEARCH_NAMES.put("KeyciteCitedFlag", KeyciteCitedFlag); //$NON-NLS-1$
        RESEARCH_NAMES.put("KeyciteHistoryFlag", KeyciteHistoryFlag); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNF-KNOverlapNorm", KnfOverlapNorm); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNF-KNCosine", KnfKNCosine); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNF-KNRecall", KnfKNRecall); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNF-KNPrecision", KnfKNPrecision); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNOverlapNorm", KNOverlapNorm); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNCosine", KNCosine); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNRecall", KNRecall); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNPrecision", KNPrecision); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNF-KeyciteCiteCountByKN", KnfKNCitedCountByKeyNumber); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNF-KeyciteCiteCountByCase", KnfKNCitedCountByCase); //$NON-NLS-1$
        RESEARCH_NAMES.put("KnfRank", KnfKNRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("InSearchResult_ALLCASES", InSearchResult_ALLCASE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchRank_ALLCASES", SearchRank_ALLCASE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchResultFraction_ALLCASES", SearchResultFraction_ALLCASE); //$NON-NLS-1$
        RESEARCH_NAMES.put("InSearchResult_JURIS", InSearchResult_CASE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchRank_JURIS", SearchRank_CASE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchResultFraction_JURIS", SearchResultFraction_CASE); //$NON-NLS-1$
        RESEARCH_NAMES.put("InSearchResult_JURIS-HN", InSearchResult_HEADNOTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchRank_JURIS-HN", SearchRank_HEADNOTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchResultFraction_JURIS-HN", SearchResultFraction_HEADNOTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("InSearchResult_ALLCASES-HN", InSearchResult_ALLHEADNOTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchRank_ALLCASES-HN", SearchRank_ALLHEADNOTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchResultFraction_ALLCASES-HN", SearchResultFraction_ALLHEADNOTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickSources", ClickSources); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureCumulativeCoClicks", ClickFeatureCumulativeCoClicks); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureMinConf", ClickFeatureMinConf); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureMaxConf", ClickFeatureMaxConf); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureAvgConf", ClickFeatureAvgConf); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureKhalid", ClickFeatureKhalid); //$NON-NLS-1$
        RESEARCH_NAMES.put("NumCoCitedByCases", NumCoCitedByCases); //$NON-NLS-1$
        RESEARCH_NAMES.put("NumCoCitedByCasesNormBySrc", NumCoCitedByCasesNormBySrc); //$NON-NLS-1$
        RESEARCH_NAMES.put("NumCoCitedCasesNormBySrc", NumCoCitedCasesNormBySrc); //$NON-NLS-1$
        RESEARCH_NAMES.put("x2sourcePrec", x2sourcePrec); //$NON-NLS-1$
        RESEARCH_NAMES.put("NumSourcesCiting", NumSourcesCiting); //$NON-NLS-1$
        RESEARCH_NAMES.put("x2candRec", x2candRec); //$NON-NLS-1$
        RESEARCH_NAMES.put("FEATURE_KEYCITE_UNIT_BRIEF_NUMBER", NumBriefsThatCiteMeAndSource); //$NON-NLS-1$
        RESEARCH_NAMES.put("FEATURE_KEYCITE_BRIEF_LINK", TotalNumSourcesThatMutualBriefsCites); //$NON-NLS-1$
        RESEARCH_NAMES.put("FEATURE_KEYCITE_ANALYTIC_NUMBER", NumAnalyticThatCiteMeAndSource); //$NON-NLS-1$
        RESEARCH_NAMES.put("FEATURE_KEYCITE_ANALYTIC_LINK", TotalNumSourcesThatMutualAnalyticCites); //$NON-NLS-1$
        RESEARCH_NAMES.put("FEATURE_KEYCITE_UNKNOWN_NUMBER", NumUnknownsThatCiteMeAndSource); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureAvgConfLogicalSearch_JURIS", AverageConfidenceLogicalSearch); //$NON-NLS-1$

        RESEARCH_NAMES.put("FEATURE_KEYCITE_UNKNOWN_LINK", TotalNumSourcesThatMutualUnknownCites); //$NON-NLS-1$
        RESEARCH_NAMES.put("NumSources", NumSources); //$NON-NLS-1$

        RESEARCH_NAMES.put("CaseKNNoDCount", StatutesCaseKeyNumberNotesOfDecisionCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("CaseNoDCount", StatutesCaseNotesOfDecisionCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("HNKNCount", StatutesHeadnoteKeyNumberCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNConditionalProb", StatutesKeyNumberConditionalProbability); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNCountDistinct", StatutesKeyNumberCountDistinct); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNCountWeighted", StatutesKeyNumberCountWeighted); //$NON-NLS-1$
        RESEARCH_NAMES.put("LibRefKNParentCount", StatutesKeyNumberParentLibraryReferenceCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("LibRefKNTopicCount", StatutesKeyNumberTopicLibararyReferenceCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("LibRefKNCount", StatutesLibraryReferenceKeyNumberCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchCite", SearchCite); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchReranked", StatutesSearchReranked); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchReranked_1", StatutesSearchReranked_1); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchReranked_2", StatutesSearchReranked_2); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNNODCount", StatutesKNNODCount); //$NON-NLS-1$
        RESEARCH_NAMES.put("SearchScore", StatutesSearchScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("searchFreq", StatuteSearchFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("CLICKSTREAM_SOURCES", ClickstreamSources); //$NON-NLS-1$
        RESEARCH_NAMES.put("JURISDICTION_SCORE", JurisdictionScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_SCORE", KeyciteScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYNUMBER_SCORE", KeynumberScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("JURIS_SCORE", JurisScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("NONJURIS_SCORE", SupremeCourtScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("TRIALDOC_CRB", TrialDocCRB); //$NON-NLS-1$
        RESEARCH_NAMES.put("TRIAL_DOC_ORDER", TrialDocOrder); //$NON-NLS-1$
        RESEARCH_NAMES.put("TRIAL_DOC_SOURCE", TrialDocSource); //$NON-NLS-1$

        RESEARCH_NAMES.put("KEYCITE_ALLCASES-HN", StatutesKeyCiteAllCasesHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("ANALYTIC_CITATION", StatuteAnalyticCitation); //$NON-NLS-1$

        RESEARCH_NAMES.put("KEYCITE_JURIS-HN", StatutesKeyCiteJurisHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_NOD_ALLCASES-HN", StatutesKeyCiteNodAllCasesHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_NOD_JURIS-HN", StatutesKeyCiteNodJurisHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_NOD_KNF_FILT_ALLCASES-HN", StatutesKeyCiteNodKnfFilteredAllCasesHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_NOD_KNF_FILT_JURIS-HN", StatutesKeyCiteNodKnfFilteredJurisHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE__KNF_FILT_ALLCASES-HN", StatutesKeyCiteKnfFilteredAllCasesHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE__KNF_FILT_JURIS-HN", StatutesKeyCiteKnfFilteredJurisHn); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureAvgConf_ARB", StatutesClickAvgConfARB); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureAvgConf_CF", StatutesClickAvgConfCF); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureAvgConf_STATUTE", StatutesClickAvgConfSTATUTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureCumulativeCoClicks_ARB", StatutesCumCoClicksARB); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureCumulativeCoClicks_CF", StatutesCumCoClicksCF); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureCumulativeCoClicks_REGULATION", StatutesCumCoClicksREGULATION); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickFeatureCumulativeCoClicks_STATUTE", StatutesCumCoClicksSTATUTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickSources_ARB", StatutesClickSourcesARB); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickSources_CF", StatutesClickSourcesCF); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickSources_REGULATION", StatutesClickSourcesREGULATION); //$NON-NLS-1$
        RESEARCH_NAMES.put("ClickSources_STATUTE", StatutesClickSourcesSTATUTE); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_PMM-JURIS", StatutesKeyciteTrialDocPMM); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_BRIEF", StatutesKeyciteTrialDocBRIEF); //$NON-NLS-1$
        RESEARCH_NAMES.put("LuceneIndexScore", StatutesIndexScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("LuceneIndexRank", StatutesIndexRank); //$NON-NLS-1$

        RESEARCH_NAMES.put("PMM_JURIS_SCORE", PmmJurisScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_CRB", KeyciteCRB); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_FILTERED_CRB", KeyciteFilteredCRB); //$NON-NLS-1$
        RESEARCH_NAMES.put("RELATION_CRB", PmmRelationCRB); //$NON-NLS-1$
        RESEARCH_NAMES.put("KC_CASE_KEYNUMBER", KeyciteCaseKeynumber); //$NON-NLS-1$
        RESEARCH_NAMES.put("KEYCITE_STATUTE", KeyciteStatute); //$NON-NLS-1$
        RESEARCH_NAMES.put("RELATION_PMM-JURIS", RelationPmmJuris); //$NON-NLS-1$
        RESEARCH_NAMES.put("CASE_AGG_JURIS_SCORE", PmmCaseAggJurisScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("CASE_AGG_KEYCITE_CRB", PmmCaseAggKeyciteCRB); //$NON-NLS-1$
        RESEARCH_NAMES.put("CASE_AGG_KEYCITE_STATUTE", PmmCaseAggKeyciteStatute); //$NON-NLS-1$
        RESEARCH_NAMES.put("CASE_AGG_RELATION_PMM-JURIS", CaseAggRelationPmmJuris); //$NON-NLS-1$
        RESEARCH_NAMES.put("CASE_AGG_KC_CASE_KEYNUMBER", PmmCaseAggKeyciteCaseKeynumber); //$NON-NLS-1$
        RESEARCH_NAMES.put("CASE_AGG_COMBINED_PROB", PmmCaseAggCombinedProb); //$NON-NLS-1$

        RESEARCH_NAMES.put("ActivityPrint", RegulationActivityPrint); //$NON-NLS-1$
        RESEARCH_NAMES.put("ActivityView", RegulationActivityView); //$NON-NLS-1$
        RESEARCH_NAMES.put("ActivityKeycite", RegulationActivityKeycite); //$NON-NLS-1$
        RESEARCH_NAMES.put("NODCases", RegulationNODCases); //$NON-NLS-1$
        RESEARCH_NAMES.put("CitedOthers", RegulationCitedOthers); //$NON-NLS-1$
        RESEARCH_NAMES.put("CitedReg", RegulationCitedReg); //$NON-NLS-1$
        RESEARCH_NAMES.put("CitingOthers", RegulationCitingOthers); //$NON-NLS-1$
        RESEARCH_NAMES.put("CitingReg", RegulationCitingReg); //$NON-NLS-1$
        RESEARCH_NAMES.put("CoclickSrcOthers", RegulationCoclickSrcOthers); //$NON-NLS-1$
        RESEARCH_NAMES.put("CoclickTrgOthers", RegulationCoclickTrgOthers); //$NON-NLS-1$
        RESEARCH_NAMES.put("CoclickReg", RegulationCoclickReg); //$NON-NLS-1$
        RESEARCH_NAMES.put("Siblings", RegulationSiblings); //$NON-NLS-1$
        RESEARCH_NAMES.put("KNTopicOverlap", RegulationKNTopicOverlap); //$NON-NLS-1$
        RESEARCH_NAMES.put("NODKNSim", RegulationNODKNSim); //$NON-NLS-1$
        RESEARCH_NAMES.put("NODTopicSim", RegulationNODTopicSim); //$NON-NLS-1$
        RESEARCH_NAMES.put("RefOthers", RegulationRefOthers); //$NON-NLS-1$
        RESEARCH_NAMES.put("RefRegs", RegulationRefRegs); //$NON-NLS-1$
        RESEARCH_NAMES.put("RegsSearchRank", RegulationSearchRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("RegsSearchScore", RegulationSearchScore); //$NON-NLS-1$

        RESEARCH_NAMES.put("TCO_CASE_HISTORY", StcoCaseHistory); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_TYPE", StcoType); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_CLICK_STREAM_BINARY_COURTORDER-JURIS", StcoClickstreamBinaryCourtOrderJuris); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_CLICK_STREAM_NO_OF_SOURCES_COURTORDER-JURIS", StcoClickstreamNumberOfSourcesCourtOrderJuris); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_CLICK_STREAM_MAX_CRB", StcoClickstreamMaxCrb); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_CLICK_STREAM_MAX_COURTORDER-JURIS", StcoClickstreamMaxCourtOrderJuris); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_CLICK_STREAM_BINARY_CRB", StcoClickstreamBinaryCrb); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_CLICK_STREAM_NO_OF_SOURCES_CRB", StcoClickstreamNumberOfSourcesCrb); //$NON-NLS-1$
        RESEARCH_NAMES.put("TCO_JURIS_SCORE", StcoJurisScore); //$NON-NLS-1$

        RESEARCH_NAMES.put("avgRank", AubAverageRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("citeListFre", AubCitelistFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("viewFre", AubViewFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("printFre", AubPrintFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("citeFre", AubCiteFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("vpcFre", AubViewPrintCiteFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("isNextClicked", AubNextClickedFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("isPrevClicked", AubPreviousClickedFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("isPrevNextClicked", AubPreviousNextClickedFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("viewFromHFind", AubFromHyperlinkFindFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("viewFromOFind", AubFromOtherFindFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("viewToHFind", AubToHyperlinkFindFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("isRevisited", AubRevistedFrequency); //$NON-NLS-1$
        RESEARCH_NAMES.put("isFirstClicked", AubFirstClickedFrequency); //$NON-NLS-1$

        RESEARCH_NAMES.put("wlrank", LocateAuthWestLawRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("wlscore", LocateAuthWestlawScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("locirank", LocateAuthLociRank); //$NON-NLS-1$
        RESEARCH_NAMES.put("lociscore", LocateAuthLociScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("year", LocateAuthYear); //$NON-NLS-1$
        RESEARCH_NAMES.put("courtlevel", LocateAuthCourtLevel); //$NON-NLS-1$
        RESEARCH_NAMES.put("casecites", LocateAuthCaseCites); //$NON-NLS-1$
        RESEARCH_NAMES.put("briefcites", LocateAuthBriefCites); //$NON-NLS-1$
        RESEARCH_NAMES.put("totalcites", LocateAuthTotalCites); //$NON-NLS-1$

        // ARB changes
        RESEARCH_NAMES.put("zscore", AnalyticalNLScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("max", AnalyticalNLMaxScore); //$NON-NLS-1$
        RESEARCH_NAMES.put("fromKeycite", KeyciteRecommends); //$NON-NLS-1$
        RESEARCH_NAMES.put("fromSearch", SearchRecommends); //$NON-NLS-1$

        // auto-register any remaining features by name
        for (final FeatureName value : values())
        {
            if (!RESEARCH_NAMES.values().contains(value))
            {
                RESEARCH_NAMES.put(value.name(), value);
            }
        }
    }

    //--------------------------------------------------------------------------

    public static final Function<FeatureName, String> TO_RESEARCH_NAME = new Function<FeatureName, String>() {
        @Override
        public String apply(final FeatureName name)
        {
            return name == null ? null : getResearchName(name);
        }
    };

    //

    public static FeatureName getTypedFeatureName(final BaseFeatureName baseName, final String type)
    {
        return CONTENT_NAMES.get(baseName + "_" + type); //$NON-NLS-1$
    }

    public static FeatureName getFeatureFromResearchName(final String researchName)
    {
        return RESEARCH_NAMES.get(researchName);
    }

    public static String getResearchName(final FeatureName name)
    {
        return RESEARCH_NAMES.inverse().get(name);
    }

    public static Set<String> getAllResearchNames()
    {
        return copyOf(RESEARCH_NAMES.keySet());
    }

    // Case-insensitive alternative to valueOf(String)?
    public static FeatureName toEnum(final String name)
    {
        if (name != null)
        {
            for (final FeatureName currentName : values())
            {
                if (name.equalsIgnoreCase(currentName.toString()))
                {
                    return currentName;
                }
            }
        }
        throw new IllegalArgumentException("Unknown Feature Name : " + name); //$NON-NLS-1$
    }

    //--------------------------------------------------------------------------

    private final Normalizer normalizer;

    FeatureName()
    {
        normalizer = null;
    }

    FeatureName(final Normalizer normalizer)
    {
        this.normalizer = normalizer;
    }

    public Normalizer normalizer()
    {
        return normalizer;
    }

    public boolean hasNormalizer()
    {
        return (normalizer != null);
    }
}
