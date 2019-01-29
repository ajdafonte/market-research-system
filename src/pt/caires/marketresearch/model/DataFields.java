package pt.caires.marketresearch.model;

import pt.caires.marketresearch.utils.IntegerRange;


/**
 * Class that represent all relevant data fields that should be considered handling requests (client and server).
 *
 * @author acaires
 */
public class DataFields
{

    // data members
    private int subjectSurveyValue;
    private String gender;
    private IntegerRange ageRange;
    private IntegerRange incomeRange;
    private String country;

    public DataFields(final int subjectId, final String gender, final IntegerRange ageRange, final IntegerRange incomeRange, final String country)
    {
        this.subjectSurveyValue = subjectId;
        this.gender = gender;
        this.ageRange = ageRange;
        this.incomeRange = incomeRange;
        this.country = country;
    }

    public int getSubjectSurveyValue()
    {
        return subjectSurveyValue;
    }

    public void setSubjectSurveyValue(final int subjectSurveyValue)
    {
        this.subjectSurveyValue = subjectSurveyValue;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(final String gender)
    {
        this.gender = gender;
    }

    public IntegerRange getAgeRange()
    {
        return ageRange;
    }

    public void setAgeRange(final IntegerRange ageRange)
    {
        this.ageRange = ageRange;
    }

    public IntegerRange getIncomeRange()
    {
        return incomeRange;
    }

    public void setIncomeRange(final IntegerRange incomeRange)
    {
        this.incomeRange = incomeRange;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(final String country)
    {
        this.country = country;
    }

    // Validators

    public static boolean validateGender(final String genderStr)
    {
        if (genderStr != null && (genderStr.equals("M") || genderStr.equals("F")))
        {
            return true;
        }
        else
        {
            System.out.println(">> Invalid gender value. Indicate a correct gender (M or F).");
            return false;
        }
    }

    public static boolean validateIntegerRange(final String lowRangeStr, final String highRangeStr)
    {
        final int lowRangeValue;
        final int highRangeValue;
        // valid lowRangeStr syntax
        if (lowRangeStr != null && lowRangeStr.matches("[0-9]+"))
        {
            lowRangeValue = Integer.parseInt(lowRangeStr);
        }
        else
        {
            System.out.println(">> Invalid low value. Indicate a correct range, for example: [30 40].");
            return false;
        }
        // valid highRangeStr syntax
        if (highRangeStr != null && highRangeStr.matches("[0-9]+"))
        {
            highRangeValue = Integer.parseInt(highRangeStr);
        }
        else
        {
            System.out.println(">> Invalid high value. Indicate a correct range, for example: [30 40].");
            return false;
        }
        // validates if upper is biggest than lower
        if (highRangeValue >= lowRangeValue)
        {
            return true;
        }
        else
        {
            System.out.println(">> Low value is biggest than high value. Indicate a correct range, for example: [30 40].");
            return false;
        }
    }

    public static boolean validateCountry(final String countryStr)
    {
        if (countryStr != null && countryStr.length() == 3)
        {
            return true;
        }
        else
        {
            System.out.println(">> Invalid country value. Indicate a correct value, for example: POR.");
            return false;
        }
    }

}
