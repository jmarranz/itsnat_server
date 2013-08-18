package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import inexp.extjsexam.model.CarModel;
import javax.swing.table.DefaultTableModel;
import org.itsnat.core.NameValue;

/**
 *
 * @author jmarranz
 */
public class CarListTab extends TabContainingTable
{
    public CarListTab(ExtJSExampleDocument parent)
    {
        super(parent);
    }

    public String getTitle()
    {
        return "Cars";
    }

    public void fillTableWithData()
    {
        CarModel[] cars = extJSDoc.getDataModel().getCarModelList();
        for(int i = 0; i < cars.length; i++)
        {
            CarModel car = cars[i];
            addRow(car);
        }
    }

    public void updateName(String name,Object modelObj)
    {
        CarModel car = (CarModel)modelObj;
        car.setName(name);
        extJSDoc.getDataModel().updateCarModel(car);
    }

    public void updateDescription(String desc,Object modelObj)
    {
        CarModel car = (CarModel)modelObj;
        car.setDescription(desc);
        extJSDoc.getDataModel().updateCarModel(car);
    }

    public void addNewItem(String name,String desc)
    {
        CarModel car = new CarModel(name,desc);
        getDataModel().addCarModel(car);

        addRow(car);
    }

    public void addRow(CarModel car)
    {
        DefaultTableModel tableModel = (DefaultTableModel)tableComp.getTableModel();
            tableModel.addRow(new NameValue[] {
                new NameValue(car.getName(),car),
                new NameValue(car.getDescription(),car) }
                );
    }

    public void removeItemDB(Object modelObj)
    {
        getDataModel().removeCarModel((CarModel)modelObj);
    }
}
