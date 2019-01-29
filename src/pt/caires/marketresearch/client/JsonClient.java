package pt.caires.marketresearch.client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pt.caires.marketresearch.model.DataFields;
import pt.caires.marketresearch.utils.Constants;
import pt.caires.marketresearch.utils.IntegerRange;


/**
 * JsonClient - represents a JSON Client.
 *
 * @author acaires
 */
public class JsonClient implements Client<JSONObject>
{

    @Override
    public JSONObject createClientMessage()
    {
        // creates a dummy message
        final JSONObject msg = new JSONObject();
        msg.put("dummy-message", "ping");
        return msg;
    }

    @Override
    public JSONObject createClientMessage(final DataFields dataFields)
    {
        final JSONObject request = new JSONObject();

        // requester
        final JSONObject requester = new JSONObject();
        requester.put("id", Constants.REQUESTER_ID);
        requester.put("name", Constants.REQUESTER_NAME);
        // add requester to msg
        request.put("requester", requester);

        // provider
        final JSONObject provider = new JSONObject();
        provider.put("id", Constants.PROVIDER_ID);
        provider.put("name", Constants.PROVIDER_NAME);
        // add provider to msg
        request.put("provider", provider);

        // survey
        final JSONObject survey = new JSONObject();
        // - subject
        survey.put("subject", dataFields.getSubjectSurveyValue());

        // - target
        final JSONObject target = new JSONObject();
        target.put("gender", dataFields.getGender());
        final JSONArray jsonAgeRange = new JSONArray();
        final IntegerRange ageRange = dataFields.getAgeRange();
        jsonAgeRange.add(ageRange.getLow());
        jsonAgeRange.add(ageRange.getHigh());
        target.put("age", jsonAgeRange);
        final JSONArray jsonIncomeRange = new JSONArray();
        final IntegerRange incomeRange = dataFields.getIncomeRange();
        jsonIncomeRange.add(incomeRange.getLow());
        jsonIncomeRange.add(incomeRange.getHigh());
        target.put("income", jsonIncomeRange);
        survey.put("target", target);
        survey.put("country", dataFields.getCountry());
        // add survey to msg
        request.put("survey", survey);

        return request;
    }

    @Override
    public JSONObject handleServerMessage(final String serverMsg)
    {
        JSONObject jsonObject = null;
        try
        {
            final JSONParser parser = new JSONParser();
            jsonObject = (JSONObject) parser.parse(serverMsg);
        }
        catch (final ParseException ex)
        {
            System.err.println(">> Error parsing string: " + ex.getMessage());
        }
        return jsonObject;
    }

    @Override
    public void printClientMessage(final String clientMsg)
    {
        System.out.println(">> Message sended by Client");
        System.out.println(clientMsg + "\n");
    }

    @Override
    public void printServerMessage(final String serverMsg)
    {
        System.out.println(">> Message received from Server");
        System.out.println(serverMsg + "\n");
    }
}
