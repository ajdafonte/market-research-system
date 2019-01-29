package pt.caires.marketresearch.utils;

/**
 * Class that represent an Integer range.
 *
 * @author acaires
 */
public class IntegerRange extends Range<Integer>
{

    public IntegerRange(final Integer low, final Integer high)
    {
        super(low, high);
    }

    @Override
    public boolean contains(final Integer value)
    {
        return (getLow().compareTo(value) <= 0 && getHigh().compareTo(value) >= 0);
    }

    @Override
    public String toString()
    {
        return "[" + getLow() + "," + getHigh() + "]";
    }

}
