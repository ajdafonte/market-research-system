package pt.caires.marketresearch.model;

/**
 * Class that represents a Person with a certain characteristics - record in "db".
 *
 * @author acaires
 */
public class Person
{

    // data members
    private String name;
    private String gender;
    private int age;
    private int income;
    private String country;

    public Person(final String name, final String genre, final int age, final int income, final String country)
    {
        this.name = name;
        this.gender = genre;
        this.age = age;
        this.income = income;
        this.country = country;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(final String gender)
    {
        this.gender = gender;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(final int age)
    {
        this.age = age;
    }

    public int getIncome()
    {
        return income;
    }

    public void setIncome(final int income)
    {
        this.income = income;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(final String country)
    {
        this.country = country;
    }

    @Override
    public String toString()
    {
        return "Person{" + "name=" + name + ", genre=" + gender + ", age=" + age + ", income=" + income + ", country=" + country + '}';
    }

}
