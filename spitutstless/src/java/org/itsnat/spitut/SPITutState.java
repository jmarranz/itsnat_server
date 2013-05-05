
package org.itsnat.spitut;

import org.itsnat.core.html.ItsNatHTMLDocument;

public abstract class SPITutState
{
    protected SPITutMainDocument spiTutDoc;

    public SPITutState(SPITutMainDocument spiTutDoc)
    {
        this.spiTutDoc = spiTutDoc;

        spiTutDoc.registerState(this);
    }

    public SPITutMainDocument getSPITutMainDocument()
    {
        return spiTutDoc;
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return spiTutDoc.getItsNatHTMLDocument();
    }

    public abstract String getStateTitle();
    public abstract String getStateName();
}
