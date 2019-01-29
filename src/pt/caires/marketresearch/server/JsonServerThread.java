package pt.caires.marketresearch.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pt.caires.marketresearch.model.DataFields;
import pt.caires.marketresearch.model.Person;
import pt.caires.marketresearch.model.SubjectSurvey;
import pt.caires.marketresearch.utils.Constants;
import pt.caires.marketresearch.utils.IntegerRange;


/**
 * Class that represents a JsonServer that serves a certain JsonClient. Also contains the MarketSurvey API implementation.
 *
 * @author acaires
 */
public class JsonServerThread extends Thread implements MarketSurveyServer<JSONObject, JSONArray>
{

    private final Socket sock;

    JsonServerThread(final Socket cliSock)
    {
        this.sock = cliSock;
    }

    @Override
    public void run()
    {
        OutputStreamWriter writerStream = null;
        BufferedReader reader = null;
        try
        {
            // initialize streams
            final InputStreamReader readerStream = new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8);
            reader = new BufferedReader(readerStream);
            writerStream = new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8);

            while (true)
            {
                // wait for client message
                final String clientMsg = reader.readLine();
                printClientMessage(clientMsg);

                // validates if connection with client should be closed
                if (clientMsg.equals(Constants.QUIT_MESSAGE))
                {
                    sock.close();
                    return;
                }
                else
                {

                    // handles with client message
                    final JSONObject jsonRespObj = handleClientMessage(clientMsg);
                    printServerMessage(jsonRespObj.toJSONString());

                    // send response to client
                    writerStream.write(jsonRespObj.toJSONString() + "\n");
                    writerStream.flush();
                }
            }
        }
        catch (final IOException ex)
        {
            System.err.println(">> Error manipulating streams: " + ex);
        }
        finally
        {
            try
            {
                if (writerStream != null)
                {
                    writerStream.close();
                }
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (final IOException ex)
            {
                System.err.println(">> Error closing streams: " + ex);
            }
        }
    }

    private List<Person> loadDataFromDB(final int target)
    {
        final List<Person> data = new ArrayList<>();

        // read file into stream
        final String fileName = Constants.DB_FILENAME;
        final InputStream inStream = JsonServerThread.class.getResourceAsStream(fileName);
        try (final Scanner scan = new Scanner(inStream))
        {
            while (scan.hasNextLine())
            {
                final String dbInfoLine = scan.nextLine();
                if (dbInfoLine.startsWith(String.valueOf(target)))
                {
                    final String[] splitted = dbInfoLine.split("\\s+");
                    // read person properties
                    final Person p = new Person(splitted[1], // name
                        splitted[2], // gender
                        Integer.parseInt(splitted[3]), // age
                        Integer.parseInt(splitted[4]), // income
                        splitted[5]);                  // country
                    // save item
                    data.add(p);
                }
            }
        }
        return data;
    }

    // debug method
    private void printDataLoaded(final List<Person> data)
    {
        System.out.println(">> Data Loaded:");
        data.forEach(System.out::println);
    }

    private JSONObject createJsonPerson(final Person p)
    {
        final JSONObject jObj = new JSONObject();
        jObj.put("name", p.getName());
        jObj.put("gender", p.getGender());
        jObj.put("age", p.getAge());
        jObj.put("income", p.getIncome());
        jObj.put("country", p.getCountry());
        return jObj;
    }

    private JSONArray getResponseData(final int targetSurvey, final DataFields dataReqFields)
    {
        final JSONArray result = new JSONArray();

        // load info from db by subject
        final List<Person> data = loadDataFromDB(targetSurvey);
        printDataLoaded(data);

        // validate if was possible to load data from db
        if (!data.isEmpty())
        {
            data.forEach((person) -> {
                if (dataReqFields != null)
                {
                    // case - getDataSpecificMarkeySurvey
                    if (dataReqFields.getGender().equals(person.getGender())
                        && dataReqFields.getAgeRange().contains(person.getAge())
                        && dataReqFields.getIncomeRange().contains(person.getIncome())
                        && dataReqFields.getCountry().equals(person.getCountry()))
                    {
                        // create JSONObject that represents a "Person" and add to result array
                        result.add(createJsonPerson(person));
                    }
                }
                else
                {
                    // case - getRandomDataSpecificMarkeySurvey
                    // create JSONObject that represents a "Person" and add to result array
                    result.add(createJsonPerson(person));
                }
            });
        }
        return result;
    }

    @Override
    public JSONObject getDataSpecificMarkeySurvey(final JSONObject request)
    {
        // extract data fields contained in request in order 
        // to obtain the data that should be included in the response
        final JSONObject jSurveyObj = (JSONObject) request.get("survey");
        final int subjectSurveyValue = (int) (long) jSurveyObj.get("subject");
        final JSONObject jTargetObj = (JSONObject) jSurveyObj.get("target");
        final String gender = (String) jTargetObj.get("gender");
        final JSONArray jAgeRange = (JSONArray) jTargetObj.get("age");
        final IntegerRange ageRange = new IntegerRange((int) (long) jAgeRange.get(0), (int) (long) jAgeRange.get(1));
        final JSONArray jIncomeRange = (JSONArray) jTargetObj.get("income");
        final IntegerRange incomeRange = new IntegerRange((int) (long) jIncomeRange.get(0), (int) (long) jIncomeRange.get(1));
        final String country = (String) jSurveyObj.get("country");
        final DataFields dataFields = new DataFields(subjectSurveyValue, gender, ageRange, incomeRange, country);

        return createServerMessage(subjectSurveyValue, getResponseData(subjectSurveyValue, dataFields));
    }

    @Override
    public JSONObject getRandomDataSpecificMarkeySurvey()
    {
        // define what MarketSurvey should be obtained
        final int subjectSurveyValue = SubjectSurvey.getRandomValue();

        return createServerMessage(subjectSurveyValue, getResponseData(subjectSurveyValue, null));
    }

    @Override
    public JSONObject createServerMessage(final int subjectSurveyValue, final JSONArray data)
    {
        final JSONObject jObjResponse = new JSONObject();

        // add subject
        jObjResponse.put("subject", subjectSurveyValue);

        // add provider
        final JSONObject jObjProvider = new JSONObject();
        jObjProvider.put("id", Constants.PROVIDER_ID);
        jObjProvider.put("name", Constants.PROVIDER_NAME);
        jObjResponse.put("provider", jObjProvider);

        // calculate and add data
        jObjResponse.put("data", data);

        return jObjResponse;
    }

    @Override
    public JSONObject handleClientMessage(final String clientMsg)
    {
        JSONObject response = null;
        final JSONParser parser = new JSONParser();
        try
        {
            final JSONObject request = (JSONObject) parser.parse(clientMsg);

            // validate if message is a request or not (has the 'survey' object)
            if (request.containsKey("survey"))
            {
                // handles the request for MarketSurvey information
                response = getDataSpecificMarkeySurvey(request);
            }
            else
            {
                // "triggers" the exception case - no request received, 
                // server send "random" MarketSurvey information               
                response = getRandomDataSpecificMarkeySurvey();
            }
        }
        catch (final ParseException ex)
        {
            System.err.println(">> Error parsing string: " + ex.getMessage());
        }
        return response;
    }

    @Override
    public void printClientMessage(final String clientMsg)
    {
        System.out.println(">> Message received from Client");
        System.out.println(clientMsg + "\n");
    }

    @Override
    public void printServerMessage(final String serverMsg)
    {
        System.out.println(">> Message sended by Server");
        System.out.println(serverMsg + "\n");
    }

}
