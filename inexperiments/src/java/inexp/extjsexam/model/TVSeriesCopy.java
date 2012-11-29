/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inexp.extjsexam.model;

/**
 *
 * @author jmarranz
 */
public class TVSeriesCopy extends TVSeries
{
    protected TVSeries original;

    public TVSeriesCopy(TVSeries original)
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
