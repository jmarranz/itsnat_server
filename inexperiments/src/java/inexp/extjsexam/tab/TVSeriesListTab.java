/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import inexp.extjsexam.model.TVSeries;
import javax.swing.table.DefaultTableModel;
import org.itsnat.core.NameValue;

/**
 *
 * @author jmarranz
 */
public class TVSeriesListTab extends TabContainingTable
{
    public TVSeriesListTab(ExtJSExampleDocument parent)
    {
        super(parent);
    }

    public String getTitle()
    {
        return "TV Series";
    }

    public void fillTableWithData()
    {
        TVSeries[] tvSeriesList = extJSDoc.getDataModel().getTVSeriesList();
        for(int i = 0; i < tvSeriesList.length; i++)
        {
            TVSeries tvSeries = tvSeriesList[i];
            addRow(tvSeries);
        }
    }

    public void updateName(String name,Object modelObj)
    {
        TVSeries tv = (TVSeries)modelObj;
        tv.setName(name);
        extJSDoc.getDataModel().updateTVSeries(tv);
    }

    public void updateDescription(String desc,Object modelObj)
    {
        TVSeries tv = (TVSeries)modelObj;
        tv.setDescription(desc);
        extJSDoc.getDataModel().updateTVSeries(tv);
    }

    public void addNewItem(String name,String desc)
    {
        TVSeries tvSeries = new TVSeries(name,desc);
        getDataModel().addTVSeries(tvSeries);

        addRow(tvSeries);
    }

    public void addRow(TVSeries tvSeries)
    {
        DefaultTableModel tableModel = (DefaultTableModel)tableComp.getTableModel();
            tableModel.addRow(new NameValue[] {
                new NameValue(tvSeries.getName(),tvSeries),
                new NameValue(tvSeries.getDescription(),tvSeries) }
                );
    }

    public void removeItemDB(Object modelObj)
    {
        getDataModel().removeTVSeries((TVSeries)modelObj);
    }
}
