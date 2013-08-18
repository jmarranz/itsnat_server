package inexp.extjsexam.model;

/**
 *
 * @author jmarranz
 */
public class CarModel
{
    protected String name;
    protected String description;

    public CarModel(String name,String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
