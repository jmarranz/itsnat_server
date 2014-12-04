/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itsnat.impl.core.template;

import org.itsnat.impl.core.servlet.ItsNatServletImpl;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDocumentTemplateNormalWebImpl extends ItsNatStfulDocumentTemplateNormalImpl
{
    public ItsNatStfulDocumentTemplateNormalWebImpl(String name, String mime, MarkupSourceImpl source, ItsNatServletImpl servlet)
    {
        super(name, mime, source, servlet);
    }
    
}
