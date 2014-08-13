/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.template;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.tmpl.MarkupTemplate;
import org.itsnat.impl.core.*;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class MarkupTemplateImpl extends ItsNatUserDataImpl implements MarkupTemplate
{
    protected MarkupTemplateDelegateImpl delegate;
    protected final XercesDOMParserWrapperImpl parser; // El parser NO es multihilo, ha de sincronizarse antes de parsear
    protected final String name;
    protected final String mime;
    protected final ItsNatServletImpl servlet; // El servlet en el que se registró
    protected String encoding; // Puede cambiar
    protected boolean onLoadCachingDOMNodes;
    protected final int namespaceOfMIME;

    /**
     * Creates a new instance of MarkupTemplateImpl
     */
    public MarkupTemplateImpl(String name,String mime,MarkupSourceImpl source,ItsNatServletImpl servlet)
    {
        super(true);

        this.name = name;
        this.mime = mime;

        // A día de hoy sólo admitimos paths de archivos locales pero en el futuro podrán ser streams etc (pasando el usuario directamente el InputSource)
        this.servlet = servlet;

        ItsNatServletConfig servletConfig = servlet.getItsNatServletConfig();

        this.encoding = servletConfig.getDefaultEncoding();

        // El que decide si es HTML o XHTML no es el template sino el MIME suministrado
        // Como el mime no cambia no hay problema
        this.namespaceOfMIME = NamespaceUtil.getNamespaceCode(mime);

        this.onLoadCachingDOMNodes = servletConfig.isOnLoadCacheStaticNodes(getMIME());

        this.delegate = createMarkupTemplateDelegate(source);

        this.parser = createMarkupParser(encoding);
    }

    public abstract MarkupTemplateDelegateImpl createMarkupTemplateDelegate(MarkupSourceImpl source);

    public XercesDOMParserWrapperImpl getMarkupParser()
    {
        return parser; // Usar en monohilo
    }

    protected XercesDOMParserWrapperImpl createMarkupParser(String defaultEncoding)
    {
        return XercesDOMParserWrapperImpl.createXercesDOMParserWrapper(namespaceOfMIME,defaultEncoding);
    }

    public abstract boolean isDocFragment();


    public boolean isMIME_XHTML()
    {
         return NamespaceUtil.isMIME_XHTML(namespaceOfMIME);
    }

    public boolean isMIME_HTML()
    {
         return NamespaceUtil.isMIME_HTML(namespaceOfMIME);
    }

    public boolean isMIME_HTML_or_XHTML()
    {
         return NamespaceUtil.isMIME_HTML_or_XHTML(namespaceOfMIME);
    }

    public boolean isMIME_OTHERNS()
    {
         return NamespaceUtil.isMIME_OTHERNS(namespaceOfMIME);
    }

    public boolean isMIME_SVG()
    {
         return NamespaceUtil.isMIME_SVG(namespaceOfMIME);
    }

    public boolean isMIME_XUL()
    {
         return NamespaceUtil.isMIME_XUL(namespaceOfMIME);
    }

    public boolean isMIME_XML()
    {
         return NamespaceUtil.isMIME_XML(namespaceOfMIME);
    }
    
    public boolean isMIME_ANDROID_LAYOUT()
    {
         return NamespaceUtil.isMIME_ANDROID_LAYOUT(namespaceOfMIME);
    }    

    public String getNamespace()
    {
        return NamespaceUtil.getNamespace(namespaceOfMIME);
    }

    public String getName()
    {
        return name;
    }

    public String getMIME()
    {
        // No hacemos método set porque el dato se suministra ya por el constructor explícitamente
        return mime;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        checkIsAlreadyUsed();
        this.encoding = encoding;
    }

    public boolean isOnLoadCacheStaticNodes()
    {
        return onLoadCachingDOMNodes;
    }

    public void setOnLoadCacheStaticNodes(boolean value)
    {
        checkIsAlreadyUsed();
        this.onLoadCachingDOMNodes = value;
    }

    public Object getSource()
    {
        return delegate.getSource();
    }

    public MarkupTemplateVersionImpl getNewestMarkupTemplateVersion(ItsNatServletRequest request,ItsNatServletResponse response)
    {
        return delegate.getNewestMarkupTemplateVersion(request,response);
    }

    public ItsNatServlet getItsNatServlet()
    {
        return servlet;
    }

    public ItsNatServletImpl getItsNatServletImpl()
    {
        return servlet;
    }

    public abstract MarkupTemplateVersionImpl createMarkupTemplateVersion(InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response);

    public void checkIsAlreadyUsed()
    {
        // Para detectar un mal uso en tiempo de desarrollo, los hilos no son relevantes.
        if (delegate.isTemplateAlreadyUsed())
            throw new ItsNatException("Template is frozen because some document/fragment was already loaded",this);
    }

    public boolean isItsNatTagsAllowed()
    {
        return delegate.isItsNatTagsAllowed();
    }

    public static void writeObject(MarkupTemplateImpl template,ObjectOutputStream out) throws IOException
    {
        String templateType;
        if (template instanceof ItsNatDocumentTemplateImpl) templateType = "document";
        else templateType = "docFrag";
        String templateName = template.getName();

        out.writeObject(templateType);
        out.writeObject(templateName);
    }

    public static String[] readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        String templateType = (String)in.readObject();
        String templateName = (String)in.readObject();

        return new String[]{ templateType, templateName };
    }

    public static MarkupTemplateImpl getMarkupTemplate(ItsNatServletImpl itsNatServlet,String[] templateId)
    {
        String templateType = templateId[0];
        String templateName = templateId[1];

        if (templateType.equals("document"))
            return itsNatServlet.getItsNatDocumentTemplateImpl(templateName);
        else
            return itsNatServlet.getItsNatDocFragmentTemplateImpl(templateName);
    }

}
