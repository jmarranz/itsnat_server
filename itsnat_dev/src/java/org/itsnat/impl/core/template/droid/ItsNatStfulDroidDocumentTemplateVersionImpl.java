/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.template.droid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.doc.droid.ItsNatStfulDroidDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.domutil.NodeConstraints;
import org.itsnat.impl.core.markup.parse.XercesDOMParserWrapperImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateImpl;
import org.itsnat.impl.core.template.ItsNatStfulDocumentTemplateVersionImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionDelegateImpl;
import org.itsnat.impl.core.util.MapEntryImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public class ItsNatStfulDroidDocumentTemplateVersionImpl extends ItsNatStfulDocumentTemplateVersionImpl
{
    protected String androidNamespacePrefix;
    protected LinkedList<Map.Entry<String,String>> namespacesDeclared = new LinkedList<Map.Entry<String,String>>();
    protected List<ScriptCode> scriptCodeList = new LinkedList<ScriptCode>();
    
    public ItsNatStfulDroidDocumentTemplateVersionImpl(ItsNatStfulDocumentTemplateImpl docTemplate, InputSource source, long timeStamp, ItsNatServletRequest request, ItsNatServletResponse response)
    {
        super(docTemplate, source, timeStamp, request, response);
        
        extractScriptElements();        
        
        readNamespaces();
    }
    
    public String getAndroidNamespacePrefix()
    {
        return androidNamespacePrefix;
    }
    
    protected void extractScriptElements()
    {
        Document doc = this.templateDoc;
        // Recuerda que el <script> principal nunca llega a ser DOM pues se mete como cadena en la string del documento serializado
        NodeList scriptElemList = doc.getElementsByTagName("script");
        int len = scriptElemList.getLength();
        if (len > 0)
        {
            ServletContext servContext = getMarkupTemplate().getItsNatServletImpl().getServlet().getServletConfig().getServletContext();
            for(int i = 0; i < len; i++)
            {
                ScriptCode scriptCodeItem = null;
                Element scriptElem = (Element)scriptElemList.item(i);
                String src = scriptElem.getAttribute("src");
                if (!"".equals(src))
                {    
                    URI uri = null;
                    try { uri = new URI(src); }
                    catch (URISyntaxException ex) { throw new ItsNatException(ex);  }
                    
                    String scheme = uri.getScheme();
                    if (scheme == null)
                        scriptCodeItem = new ScriptFile(src,servContext);                        
                    else if (scheme.equals("http") || scheme.equals("https"))                 
                        scriptCodeItem = new ScriptURI(uri);
                    else
                        throw new ItsNatException("Scheme not allowed or supported: " + scheme); // para evitar el uso de "file:" etc
                }
                else
                {
                    String code = DOMUtilInternal.getTextContent(scriptElem, true);
                    scriptCodeItem = new ScriptCodeInline(code);
                }
                scriptCodeList.add(scriptCodeItem);
            }

            while(scriptElemList.getLength() > 0)
            {
                Element scriptElem = (Element)scriptElemList.item(0);
                scriptElem.getParentNode().removeChild(scriptElem);
            }
        }
    }    
    
    public List<String> getScriptCodeList()
    {
        List<String> res = new LinkedList<String>();
        for(ScriptCode item : scriptCodeList)
            res.add(item.getCode());
        return res;
    }
    
    protected void readNamespaces()
    {
        Element rootElem = templateDoc.getDocumentElement();
        if (rootElem.hasAttributes())
        {
            NamedNodeMap attribs = rootElem.getAttributes();
            for(int i = 0; i < attribs.getLength(); i++)
            {
                Attr attr = (Attr)attribs.item(i);
                String name = attr.getName();
                if (name.startsWith("xmlns:")) // Esperamos prefijo si o si por eso incluimos los ":". getPrefix en este caso NO devuelve "xmlns"
                {
                    int pos = name.indexOf(':');
                    String prefix = name.substring(pos + 1);    
                    String namespaceURI = attr.getValue();
                    namespacesDeclared.add(new MapEntryImpl<String,String>(prefix,namespaceURI));
                    
                    if (NamespaceUtil.ANDROID_NAMESPACE.equals(namespaceURI))
                    {
                        this.androidNamespacePrefix = prefix;
                    }
                }
            }
        }
        
        if (androidNamespacePrefix == null) this.androidNamespacePrefix = "android"; // MUY RARO        
    }
    
    public String wrapBodyAsDocument(String source)
    {
        // Es curioso porque "source" puede contener elementos propios
        // del <head> como <title> <link> etc y el parser de Xerces se lo traga
        // y los elementos HTMLElement son del tipo adecuado así que
        // no hacemos métodos específicos para parsear elementos destinados
        // al <head>.

        StringBuilder code = new StringBuilder();
        code.append( "<root" );
        for(Map.Entry<String,String> entry : namespacesDeclared)
        {
            code.append( " xmlns:" + entry.getKey() + "=\"" + entry.getValue() + "\"");
        }

        code.append( ">" );
        code.append( source );
        code.append( "</root>" );

        return code.toString();
    }

    @Override    
    public Element getBodyParentElement(Document doc)
    {
        return doc.getDocumentElement(); 
    }
    
    @Override
    protected ItsNatDocumentImpl createItsNatDocument(Document doc, Browser browser, String requestURL, ItsNatSessionImpl session, boolean stateless)
    {
        return new ItsNatStfulDroidDocumentImpl(doc,this,browser,requestURL,session,stateless);
    }

    @Override
    protected MarkupTemplateVersionDelegateImpl createMarkupTemplateVersionDelegate()
    {
        return new StfulDroidTemplateVersionDelegateImpl(this);        
    }
    
    @Override
    public Document parseDocumentOrFragment(InputSource input,XercesDOMParserWrapperImpl parser,boolean isFragment)
    {
        Document doc = super.parseDocumentOrFragment(input,parser,isFragment);
                
        // Filtramos los comentarios, son incordio y total no se manifiestan en el arbol de View, este método también se usa para los fragments 
        NodeConstraints rule = new NodeConstraints()
        {
            public boolean match(Node node, Object context)
            {
                return node.getNodeType() == Node.COMMENT_NODE;
            }            
        };
        LinkedList<Node> commentList = DOMUtilInternal.getChildNodeListMatching(doc,rule,true,null);
        if (commentList != null)
        {
            for(Node comment : commentList)
                comment.getParentNode().removeChild(comment);
        }
        
        return doc;
    }    
    
}
