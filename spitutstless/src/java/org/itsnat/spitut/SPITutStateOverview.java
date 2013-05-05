
package org.itsnat.spitut;

public class SPITutStateOverview extends SPITutState
{
    public SPITutStateOverview(SPITutMainDocument spiTutDoc,boolean showPopup)
    {
        super(spiTutDoc);

        if (showPopup) showOverviewPopup();
        else cleanOverviewPopup();
    }

    public void showOverviewPopup()
    {
        new SPITutStateOverviewShowPopup(this);
    }

    public void cleanOverviewPopup()
    {    
        SPITutStateOverviewShowPopup.dispose(this);
    }
    

    @Override
    public String getStateTitle()
    {
        return "Overview";
    }

    @Override
    public String getStateName()
    {
        return "overview";
    }
}
