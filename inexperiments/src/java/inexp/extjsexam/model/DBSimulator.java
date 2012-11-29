/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Very simple simulation of a data base.
 *
 * @author jmarranz
 */
public class DBSimulator
{
    protected List<CarModel> cars = new LinkedList<CarModel>();
    protected List<TVSeries> tvSeries = new LinkedList<TVSeries>();

    public DBSimulator()
    {
        cars.add(new CarModel("Ford Focus","Seats 5, starting at $14.995"));
        cars.add(new CarModel("Ford Mustang","Seats 4, starting at $20.430"));
        cars.add(new CarModel("Ford Fusion","Seats 5, starting at $19.035"));

        tvSeries.add(new TVSeries("Grey's Anatomy","Hospital employees involved on personal relationships all of the time"));
        tvSeries.add(new TVSeries("X-Files","Two freak investigators pursuing paranormal phenomenons"));
        tvSeries.add(new TVSeries("ER","Continuously stressed employees of a hospital"));
    }

    public synchronized CarModel[] getCarModelList()
    {
        CarModelCopy[] carsCopy = new CarModelCopy[cars.size()];
        int i = 0;
        for(Iterator<CarModel> it = cars.iterator(); it.hasNext(); i++)
        {
            CarModel car = it.next();
            carsCopy[i] = new CarModelCopy(car);
        }
        return carsCopy;
    }

    public synchronized void addCarModel(CarModel car)
    {
        if (car instanceof CarModelCopy) throw new RuntimeException("Already inserted");
        cars.add(car);
    }

    public synchronized void removeCarModel(CarModel car)
    {
        if (car instanceof CarModelCopy)
            car = ((CarModelCopy)car).original;
        cars.remove(car);
    }

    public synchronized void updateCarModel(CarModel car)
    {
        if (!(car instanceof CarModelCopy)) return;
        ((CarModelCopy)car).updateOriginal();
    }

    public synchronized TVSeries[] getTVSeriesList()
    {
        TVSeriesCopy[] tvSeriesCopy = new TVSeriesCopy[tvSeries.size()];
        int i = 0;
        for(Iterator<TVSeries> it = tvSeries.iterator(); it.hasNext(); i++)
        {
            TVSeries tv = it.next();
            tvSeriesCopy[i] = new TVSeriesCopy(tv);
        }
        return tvSeriesCopy;
    }

    public synchronized void addTVSeries(TVSeries tv)
    {
        if (tv instanceof TVSeriesCopy) throw new RuntimeException("Already inserted");
        tvSeries.add(tv);
    }

    public synchronized void removeTVSeries(TVSeries tv)
    {
        if (tv instanceof TVSeriesCopy)
            tv = ((TVSeriesCopy)tv).original;
        tvSeries.remove(tv);
    }

    public synchronized void updateTVSeries(TVSeries tv)
    {
        if (!(tv instanceof TVSeriesCopy)) return;
        ((TVSeriesCopy)tv).updateOriginal();
    }
}
