package pt.caires.marketresearch.server;

/**
 * MarketSurveyServer interface - declares specific API methods for the MarketSurvey assignment.
 *
 * @param <T>
 * @param <S>
 * @author acaires
 */
public interface MarketSurveyServer<T, S> extends Server<T, S>
{

    // API Ops
    T getDataSpecificMarkeySurvey(T request);

    T getRandomDataSpecificMarkeySurvey();
}
