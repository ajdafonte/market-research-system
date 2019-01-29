package pt.caires.marketresearch.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.simple.JSONObject;

import pt.caires.marketresearch.model.DataFields;
import pt.caires.marketresearch.model.SubjectSurvey;
import pt.caires.marketresearch.utils.Constants;
import pt.caires.marketresearch.utils.IntegerRange;


/**
 * Main class that starts the Client. Also contains the implementation related with the client menu.
 *
 * @author acaires
 */
public class MainClient
{

    public static void main(final String[] args)
    {

        try (final Socket sock = new Socket(InetAddress.getLocalHost(), Integer.parseInt(args[0]));
             final Scanner scan = new Scanner(System.in))
        {

            // create client instance and initialize streams            
            System.out.println(">> Connected to server...");
            final JsonClient jsonClient = new JsonClient();
            final OutputStreamWriter writerStream = new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8);
            final InputStreamReader readerStream = new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8);
            final BufferedReader reader = new BufferedReader(readerStream);

            // show main menu and ask user to input data
            String userOpt;
            do
            {
                showMainMenu();
                userOpt = scan.next();
                switch (userOpt)
                {
                    case "1":
                    {
                        processMainMenuOpt1(scan, writerStream, reader, jsonClient);
                        break;
                    }
                    case "2":
                        processMainMenuOpt2(writerStream, reader, jsonClient);
                        break;
                    case "3":
                        processMainMenuOpt3(writerStream, reader);
                        break;
                    default:
                        System.out.println(">> Invalid option.");
                        break;
                }
            }
            while (!userOpt.equals("3"));
        }
        catch (final UnknownHostException ex)
        {
            System.err.println(">> Invalid host definition: " + ex);
        }
        catch (final IOException ex)
        {
            System.err.println(">> Error manipulating streams: " + ex);
        }

    }

    private static void showMainMenu()
    {
        System.out.println(">>>>>>> Caravelo - Market Survey Client <<<<<<<<");
        System.out.println("------------------------------------------------");
        System.out.println("Please choose one of the following options:");
        System.out.println("1 Request for information on available Market Surveys");
        System.out.println("2 Provider should send information on available Market Survey");
        System.out.println("3 Exit");
    }

    private static void showOpt1SubMenu()
    {
        System.out.println(">> Request for information on available Market Surveys <<");
        System.out.println("Choose one of the available subjects:");
        System.out.println(SubjectSurvey.EMPLOYEES.getId() + " " + SubjectSurvey.EMPLOYEES.getName());
        System.out.println(SubjectSurvey.FOOTABLL_COACHES.getId() + " " + SubjectSurvey.FOOTABLL_COACHES.getName());
        System.out.println(SubjectSurvey.SINGERS.getId() + " " + SubjectSurvey.SINGERS.getName());
    }

    private static DataFields processOpt1UserInput(final Scanner scan, final int subjectSurveyValue)
    {
        // get genderInput
        System.out.println(">> Please choose gender (\"M\" or \"F\")");
        String genderInput = scan.next();
        while (!DataFields.validateGender(genderInput))
        {
            genderInput = scan.next();
        }
        // get age range
        System.out.println(">> Please provide low value for age range");
        String lowAgeInput = scan.next();
        System.out.println(">> Please provide high value for age range");
        String highAgeInput = scan.next();
        while (!DataFields.validateIntegerRange(lowAgeInput, highAgeInput))
        {
            lowAgeInput = scan.next();
            highAgeInput = scan.next();
        }

        // get income range
        System.out.println(">> Please provide low value for income range");
        String lowIncomeInput = scan.next();
        System.out.println(">> Please provide high value for income range");
        String highIncomeInput = scan.next();
        while (!DataFields.validateIntegerRange(lowIncomeInput, highIncomeInput))
        {
            lowIncomeInput = scan.next();
            highIncomeInput = scan.next();
        }
        // get country
        System.out.println(">> Please provide country (3 letters)");
        String countryInput = scan.next();
        while (!DataFields.validateCountry(countryInput))
        {
            countryInput = scan.next();
        }

        return new DataFields(subjectSurveyValue, genderInput,
            new IntegerRange(Integer.parseInt(lowAgeInput),
                Integer.parseInt(highAgeInput)),
            new IntegerRange(Integer.parseInt(lowIncomeInput),
                Integer.parseInt(highIncomeInput)),
            countryInput);
    }

    private static void processMainMenuOpt1(final Scanner scan, final OutputStreamWriter writerStream,
                                            final BufferedReader reader, final JsonClient jsonClient)
    {
        try
        {
            String userOptSubject;
            int subjectSurveyValue = 0;
            do
            {
                showOpt1SubMenu();
                userOptSubject = scan.next();
                switch (userOptSubject)
                {
                    case "1":
                    {
                        subjectSurveyValue = SubjectSurvey.EMPLOYEES.getValue();
                        break;
                    }
                    case "2":
                    {
                        subjectSurveyValue = SubjectSurvey.FOOTABLL_COACHES.getValue();
                        break;
                    }
                    case "3":
                    {
                        subjectSurveyValue = SubjectSurvey.SINGERS.getValue();
                        break;
                    }
                    default:
                    {
                        System.out.println(">> Invalid option.");
                        break;
                    }
                }
            }
            while (!"123".contains(userOptSubject));

            // ask user for more information - data fields
            final DataFields dataFields = processOpt1UserInput(scan, subjectSurveyValue);

            // generate JSONObject - request
            final JSONObject request = jsonClient.createClientMessage(dataFields);

            // show request that would be sended to server
            jsonClient.printClientMessage(request.toJSONString());

            // send request to Server
            writerStream.write(request.toJSONString() + "\n");
            writerStream.flush();

            System.out.println(">> Waiting for server response...");

            // wait and handle response by server
            final String serverResponse = reader.readLine();
            final JSONObject response = jsonClient.handleServerMessage(serverResponse);

            // show response sended by server
            jsonClient.printServerMessage(response.toJSONString());

        }
        catch (final IOException ex)
        {
            System.err.println(">> Error manipulating streams: " + ex);
        }
    }

    private static void processMainMenuOpt2(final OutputStreamWriter writerStream, final BufferedReader reader, final JsonClient jsonClient)
    {
        try
        {
            // generate special message to Server
            final JSONObject message = jsonClient.createClientMessage();

            // show message that would be sended to server
            jsonClient.printClientMessage(message.toJSONString());

            // send special message to Server
            writerStream.write(message.toJSONString() + "\n");
            writerStream.flush();

            System.out.println(">> Waiting for server response...");

            // wait and handle response by server
            final String serverResponse = reader.readLine();
            final JSONObject response = jsonClient.handleServerMessage(serverResponse);

            // show response sended by server
            jsonClient.printServerMessage(response.toJSONString());
        }
        catch (final IOException ex)
        {
            System.err.println(">> Error manipulating streams: " + ex);
        }
    }

    private static void processMainMenuOpt3(final OutputStreamWriter writerStream, final BufferedReader reader)
    {
        try
        {
            // send message to server - QUIT
            writerStream.write(Constants.QUIT_MESSAGE + "\n");
            writerStream.flush();
            // close streams
            reader.close();
            writerStream.close();
            // quit application
            System.exit(0);
        }
        catch (final IOException ex)
        {
            System.err.println(">> Error manipulating streams: " + ex);
        }
    }

}
