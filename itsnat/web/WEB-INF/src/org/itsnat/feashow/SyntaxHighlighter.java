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

package org.itsnat.feashow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletContext;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.tmpl.MarkupTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTextAreaElement;

public class SyntaxHighlighter
{
    public SyntaxHighlighter()
    {
    }

    public static void highlightJava(HTMLTextAreaElement textAreaElem,ItsNatDocument itsNatDoc)
    {
        String javaPath = textAreaElem.getAttribute("name");
        String path = javaPath.replace('.','/') + ".java";

        ServletContext context = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet().getItsNatServletContext().getServletContext();
        String pathPrefix = context.getRealPath("/");
        path = pathPrefix + "/WEB-INF/src/" + path;

        highlightFile(javaPath,path,"UTF-8",textAreaElem,itsNatDoc);
    }

    public static void highlightMarkup(HTMLTextAreaElement textAreaElem,ItsNatDocument itsNatDoc)
    {
        String markupName = textAreaElem.getAttribute("name");

        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();

        MarkupTemplate template;
        template = servlet.getItsNatDocFragmentTemplate(markupName);
        if (template == null)
            template = servlet.getItsNatDocumentTemplate(markupName);
        if (template == null)
            throw new RuntimeException("Template not found: " + markupName);

        String url = (String)template.getSource();
        String path = url.substring("file:".length());
        String encoding = template.getEncoding();

        highlightFile(markupName,path,encoding,textAreaElem,itsNatDoc);
    }

    private static void highlightFile(String name,String path,String encoding,HTMLTextAreaElement textAreaElem,ItsNatDocument itsNatDoc)
    {
        StringBuffer code = new StringBuffer();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),encoding));
            String line = reader.readLine();
            while (line != null)
            {
                code.append( line );
                code.append( '\n' );
                line = reader.readLine();
            }
            reader.close();
        }
        catch(FileNotFoundException ex) { throw new RuntimeException(ex); }
        catch(UnsupportedEncodingException ex) { throw new RuntimeException(ex); }
        catch(IOException ex) { throw new RuntimeException(ex); }

        ItsNatDOMUtil.setTextContent(textAreaElem,code.toString());

        Document doc = itsNatDoc.getDocument();
        Element script = doc.createElement("script");
        ItsNatDOMUtil.setTextContent(script,"window.dp.SyntaxHighlighter.HighlightAll(\"" + name + "\"); \n");

        textAreaElem.getParentNode().appendChild(script);
    }

}
