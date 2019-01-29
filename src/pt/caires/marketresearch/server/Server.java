package pt.caires.marketresearch.server;

/**
 * Server interface - generic methods that a certain server should implement.
 *
 * @param <T>
 * @param <S>
 * @author acaires
 */
public interface Server<T, S>
{

    // Generic server ops    
    T createServerMessage(int subjectSurveyValue, S data);

    T handleClientMessage(String clientMsg);

    void printClientMessage(String clientMsg);

    void printServerMessage(String serverMsg);

}
