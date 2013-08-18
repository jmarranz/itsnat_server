package inexp.extjsexam.model;

/**
 *
 * @author jmarranz
 */
public class CarModelCopy extends CarModel
{
    protected CarModel original;

    public CarModelCopy(CarModel original)
    {
        super(original.getName(),original.getDescription());

        this.original = original;
    }

    public void updateOriginal()
    {
        original.name = this.name;
        original.description = this.description;
    }

}
