package pt.caires.marketresearch.model;

import java.util.Random;


/**
 * Enum class that represents all the available subject survey.
 *
 * @author acaires
 */
public enum SubjectSurvey
{

    EMPLOYEES(1, "Employees Satisfaction Survey", 81111600),
    FOOTABLL_COACHES(2, "Football Coaches Survey", 81111700),
    SINGERS(3, "Singers Survey", 81111800);

    private final int id;
    private final String name;
    private final int value;

    SubjectSurvey(final int id, final String name, final int value)
    {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getValue()
    {
        return value;
    }

    public static SubjectSurvey getById(final int id)
    {
        for (final SubjectSurvey ss : values())
        {
            if (ss.getId() == id)
            {
                return ss;
            }
        }
        throw new IllegalArgumentException();
    }

    public static SubjectSurvey getByValue(final int value)
    {
        for (final SubjectSurvey ss : values())
        {
            if (ss.getValue() == value)
            {
                return ss;
            }
        }
        throw new IllegalArgumentException();
    }

    public static int getRandomValue()
    {
        int randValue = 0;
        final int minValue = 1;
        final int maxValue = 3;
        final Random rand = new Random();
        final int randNum = rand.nextInt((maxValue - minValue) + 1) + minValue;
        switch (randNum)
        {
            case 1:
                randValue = EMPLOYEES.getValue();
                break;
            case 2:
                randValue = FOOTABLL_COACHES.getValue();
                break;
            case 3:
                randValue = SINGERS.getValue();
                break;
        }

        return randValue;
    }

}
