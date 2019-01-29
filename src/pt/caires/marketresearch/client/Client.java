package pt.caires.marketresearch.client;

import pt.caires.marketresearch.model.DataFields;


/**
 * Client interface - methods that a certain client should implement.
 *
 * @param <T>
 * @author acaires
 */
public interface Client<T>
{

    T createClientMessage();

    T createClientMessage(DataFields dataFields);

    T handleServerMessage(String serverMsg);

    void printClientMessage(String clientMsg);

    void printServerMessage(String serverMsg);

}
