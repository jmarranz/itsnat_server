/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itsnat.impl.core.template;

import org.itsnat.core.tmpl.ItsNatDroidDocumentTemplate;
import org.itsnat.impl.core.servlet.ItsNatServletConfigImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDocumentTemplateNormalDroidImpl extends ItsNatStfulDocumentTemplateNormalImpl implements ItsNatDroidDocumentTemplate
{
    protected int bitmapDensityReference;
    
    public ItsNatStfulDocumentTemplateNormalDroidImpl(String name, String mime, MarkupSourceImpl source, ItsNatServletImpl servlet)
    {
        super(name, mime, source, servlet);
        
        ItsNatServletConfigImpl servletConfig = servlet.getItsNatServletConfigImpl();
        this.bitmapDensityReference = servletConfig.getBitmapDensityReference();        
    }

    public int getBitmapDensityReference()
    {
        return bitmapDensityReference;
    }

    public void setBitmapDensityReference(int density)
    {
        checkIsAlreadyUsed();        
        this.bitmapDensityReference = density;
    }
    
}
