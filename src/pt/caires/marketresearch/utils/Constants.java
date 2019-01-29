package pt.caires.marketresearch.utils;

/**
 * Class that holds some relevant constants used in the application.
 *
 * @author acaires
 */
public final class Constants
{

    public static final String DB_FILENAME = "../resources/db.txt";
    public static final String PROVIDER_ID = "TGI";
    public static final String PROVIDER_NAME = "Kantar";
    public static final String REQUESTER_ID = "CVO";
    public static final String REQUESTER_NAME = "Caravelo";
    public static final String QUIT_MESSAGE = "QUIT";

    private Constants()
    {
        throw new AssertionError();
    }

}
