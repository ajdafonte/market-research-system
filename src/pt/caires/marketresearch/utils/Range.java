package pt.caires.marketresearch.utils;

/**
 * Abstract class that represent a generic number range.
 *
 * @param <T>
 * @author acaires
 */
public abstract class Range<T extends Number>
{

    // data members
    private T low;
    private T high;

    /**
     * @param low
     * @param high
     */
    public Range(final T low, final T high)
    {
        this.low = low;
        this.high = high;
    }

    public T getLow()
    {
        return this.low;
    }

    public T getHigh()
    {
        return this.high;
    }

    public void setLow(final T low)
    {
        this.low = low;
    }

    public void setHigh(final T high)
    {
        this.high = high;
    }

    public abstract boolean contains(T value);

    @Override
    public abstract String toString();

}
