/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.web.customcomp.shared;

import java.io.InputStream;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.TemplateSource;

/**
 *
 * @author jmarranz
 */
public class InputStreamTemplateSource implements TemplateSource
{
    protected InputStream input;
    
    public InputStreamTemplateSource(InputStream input)
    {
        this.input = input;        
    }
    
    @Override
    public boolean isMustReload(ItsNatServletRequest request, ItsNatServletResponse response) 
    {
        return false;
    }

    @Override
    public InputStream getInputStream(ItsNatServletRequest request, ItsNatServletResponse response) 
    {
        return input;
    }   
}
