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

package org.itsnat.impl.core.doc;

import java.io.IOException;
import java.io.Serializable;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulOwnerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.domimpl.ElementDocContainer;
import org.itsnat.impl.core.req.RequestImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.w3c.dom.Document;

/**
 *
 * @author jmarranz
 */
public class BoundElementDocContainerImpl implements HasUniqueId,Serializable
{
    protected ItsNatStfulDocumentImpl itsNatDoc; // Contenedor del iframe/object/embed/applet
    protected ElementDocContainerWrapperImpl elemWrap;
    protected String docName;
    protected UniqueId idObj;
    protected int randomNumber; // Ayuda a los intentos de vincular falsos elemWrap aunque no es demasiado problema esto. También puede servir para extrañas "expiraciones"
    protected ItsNatDocumentImpl contentItsNatDoc; // El documento contenido. No obligamos a que tenga estado, pues al menos el documento hijo conocerá al padre en tiempo de carga.
    protected String urlOriginal; // Copia de respaldo para restaurar en el elemento cuando sea preciso.

    public BoundElementDocContainerImpl(ElementDocContainer elem,String docName,ItsNatStfulDocumentImpl itsNatDoc)
    {
        this.elemWrap = elem.getElementDocContainerWrapper();
        this.docName = docName;
        this.itsNatDoc = itsNatDoc;
        this.idObj = itsNatDoc.getUniqueIdGenerator().generateUniqueId("dc"); //dc = document container
        this.randomNumber = itsNatDoc.getRandom().nextInt();

        this.elemWrap.setBoundElementDocContainer(this);
    }

    public String getId()
    {
        return idObj.getId();
    }

    public UniqueId getUniqueId()
    {
        return idObj;
    }

    public ElementDocContainerWrapperImpl getElementDocContainerWrapper()
    {
        return elemWrap;
    }

    public ElementDocContainer getElementDocContainer()
    {
        if (elemWrap == null) return null;
        return elemWrap.getElementDocContainer();
    }

    public String getDocName()
    {
        return docName;
    }

    public int getRandomNumber()
    {
        return randomNumber;
    }

    public String getURLOriginal()
    {
        return urlOriginal;
    }

    public void saveURLOriginal(String urlOriginal)
    {
        this.urlOriginal = urlOriginal;
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return itsNatDoc;
    }

    public ItsNatDocumentImpl getContentItsNatDocument()
    {
        return contentItsNatDoc;
    }

    public void setContentItsNatDocument(ItsNatDocumentImpl contentItsNatDoc)
    {
        // Es posible que this.contentItsNatDoc no sea nulo, es el caso
        // seguramente de reload del iframe/object/embed/applet
        if (this.contentItsNatDoc != null)
            this.contentItsNatDoc.setParentHTMLDocContainer(null); // Quitamos el actual

        this.contentItsNatDoc = contentItsNatDoc;
        if (contentItsNatDoc != null)
            contentItsNatDoc.setParentHTMLDocContainer(this);
    }

    public Document getContentDocument()
    {
        if (contentItsNatDoc == null)
            return null;
        return contentItsNatDoc.getDocument();
    }

    protected static String getNameFromQuery(java.net.URL urlObj)
    {
        String docName = null;

        // Vemos si el url del iframe/object/embed/applet tiene el formato deseado

        // Con el URL además de filtrar la parte query de la parte ref (#)
        // evitamos intentos de engaño con URLs mal formadas.
        String query = urlObj.getQuery(); // La parte tras el ? (y antes del #), tenemos la seguridad de que existe
        String token;
        if (query.indexOf('&') != -1) token = "&";
        else token = null;
        String[] params;
        if (token != null) params = query.split(token);
        else params = new String[] { query };

        for(int i = 0; i < params.length; i++)
        {
            String param = params[i];
            int pos = param.indexOf('=');
            String name = param.substring(0,pos);
            String value = param.substring(pos + 1);
            if (name.equals("itsnat_doc_name"))
            {
                if (docName != null) throw new ItsNatException("Unexpected duplication of itsnat_doc_name parameter"); // Hay que evitar al máximo cualquier intento de engaño
                docName = value;
            }
            else if (name.equals("itsnat_action"))
            {
                // A día de hoy la carga de una página no tiene un "itsnat_action"
                // por lo que si existe es que este iframe/object/embed/applet tiene otro fin.
                docName = null;
                break;
            }
        }

        return docName;
    }

    public static BoundElementDocContainerImpl register(ElementDocContainer elem,ItsNatStfulDocumentImpl itsNatDoc)
    {
        String urlOriginal = elem.getElementDocContainerWrapper().getURL();
        String url = urlOriginal;
        url = url.trim(); // Los navegadores son tolerantes a los espacios al ppio y al final

        // Formato esperado:
        // 1) Relativo: "?itsnat_doc_name=page&...#..." (la parte ref # es opcional y el itsnat_doc_name no necesariamente el primero)
        // 2) Absoluto: "URLdelDocPadre?itsnat_doc_name=page&...#..."
        if (url.equals("")) return null; // No definida o sin valor

        String docName = null;
        try
        {
            if (url.startsWith("?"))
            {
                StringBuilder path = new StringBuilder();
                path.append("http://localhost:8080/context/servlet");
                path.append(url); // Añadimos la query y la posible parte ref (tras #)

                java.net.URL urlObj = new java.net.URL(path.toString());
                docName = getNameFromQuery(urlObj);
            }
            else // Caso de posible URL absoluto
            {
                // La URL del documento y la del iframe/object/embed/applet
                // deben coincidir hasta la query string.
                java.net.URL urlObj = new java.net.URL(url);
                java.net.URL urlDocObj = new java.net.URL(itsNatDoc.getRequestURL());
                if (!urlDocObj.getProtocol().equals(urlObj.getProtocol()))
                    return null;
                if (!urlDocObj.getHost().equals(urlObj.getHost()))
                    return null;
                if (urlDocObj.getPort() != urlObj.getPort())
                    return null;
                if (!urlDocObj.getPath().equals(urlObj.getPath())) // getPath() no incluye la query string
                    return null;
                docName = getNameFromQuery(urlObj);
            }
        }
        catch(Exception ex)
        {
            return null; // No tiene el formato de URL relativo esperado o los parámetros están malformados
        }

        if (docName == null) return null;

        return itsNatDoc.addBoundElementDocContainer(elem, docName);
    }

    public static boolean unRegister(ElementDocContainer elem,ItsNatStfulDocumentImpl itsNatDoc)
    {
        BoundElementDocContainerImpl bindInfo = elem.getElementDocContainerWrapper().getBoundElementDocContainer();
        if (bindInfo == null) return false;

        bindInfo.unRegister();
        return true;
    }

    public void unRegister()
    {
        if (elemWrap == null) throw new ItsNatException("INTERNAL ERROR"); // ¿Desregistrando dos veces?
        setContentItsNatDocument(null);
        itsNatDoc.removeBoundElementDocContainer(this);
        elemWrap.setBoundElementDocContainer(null);
        this.elemWrap = null; // Para que no pueda reutilizarse
    }

    protected static java.net.URL buildAbsURLUntilQuery(String url,StringBuilder newURL)
    {
        java.net.URL urlObj;
        try
        {
            urlObj = new java.net.URL(url);
        }
        catch(IOException ex)
        {
            throw new ItsNatException(ex);
        }

        newURL.append(urlObj.getProtocol());
        newURL.append(":");
        String authority = urlObj.getAuthority();
        if ((authority != null) && (authority.length() > 0))
        {
            newURL.append("//");
            newURL.append(authority);
        }
        String path = urlObj.getPath();
        if (path != null)
            newURL.append(path);

        return urlObj;
    }

    public String generateURLForClientOwner(ClientDocumentStfulOwnerImpl clientDoc)
    {
        // Formamos de nuevo la URL que nos interesa reutilizando la original pero con nuevos parámetros
        // Sabemos que es correcta.
        String urlOriginal = elemWrap.getURL(); // Es preciso copiar ahora pues es modificada en caso de éxito. Aunque no exista nunca es nulo

        String url = urlOriginal;
        url = url.trim(); // Los navegadores son tolerantes a los espacios al ppio y al final

        StringBuilder newURL = new StringBuilder();
        java.net.URL urlObj;
        if (url.startsWith("?")) // URL relativa
        {
            StringBuilder urlAbs = new StringBuilder();
            urlAbs.append("http://localhost:8080/context/servlet");  // URL de patrón para poder manipular el resto de URL 
            urlAbs.append(url); // Añadimos la query y la posible parte ref (tras #)

            try
            {
                urlObj = new java.net.URL(urlAbs.toString());
            }
            catch(IOException ex)
            {
                throw new ItsNatException(ex);
            }

            // Generamos también una URL relativa
            newURL.append("?" + urlObj.getQuery());

            // No es necesaria el id estándar (la cookie) de sesión
            // porque tanto en el caso de iframe con un MIME soportado
            // por el propio navegador, como en el caso de Adobe SVG Viewer,
            // el cookie del padre es automáticamente compartido por el documento hijo.
            // Si descubrimos un plugin que acepte URLs relativos y que el cookie
            // no sea compartido, entonces añadiremos también el id estándar de la sesión.
        }
        else // URL absoluto. Generamos también un URL absoluto
        {
            urlObj = buildAbsURLUntilQuery(url,newURL);

            // Necesitamos el jsessionid pues probablemente este es el caso de
            // Savarese Ssrc SVG, que aparte de necesitar URLs absolutos,
            // el plugin realmente instancia un FireFox cuya integración con el Internet Explorer
            // es débil, de hecho la sessión de la página abierta por el plugin
            // será diferente a la del padre.
            // No hay problema de seguridad pues el jsessionid se enviará como parte
            // de la página padre. Al enviarse de nuevo al padre este número es
            // tremendamente difícil de "acertar" por un usuario malicioso desde "otro lugar".
            // No utilizamos el "URL rewriting" que podría conseguir que la sesión
            // fuera la del padre, pues no se porqué pero también está presente
            // el JSESSIONID como cookie pero diferente, no se exactamente de donde sale, pero si hay cookie se ignora
            // (en Tomcat 5.5) el JSESSIONID de la URL. Enviamos el JSESSIONID como parámetro y ya está.

            // Tiene necesariamente query porque debe declararse el itsnat_doc_name
            newURL.append("?" + urlObj.getQuery());
            String stdSessionId = clientDoc.getItsNatSessionImpl().getStandardSessionId();
            newURL.append("&itsnat_jsessionid_parent=" + stdSessionId);
        }

        newURL.append("&itsnat_doc_parent_id=" + itsNatDoc.getId());
        newURL.append("&itsnat_element_parent_id=" + getId());
        newURL.append("&itsnat_element_sec_num=" + getRandomNumber());

        String ref = urlObj.getRef();
        if (ref != null)
            newURL.append("#" + ref);

        return newURL.toString();
    }

    public String generateURLForClientAttached(ClientDocumentAttachedClientImpl clientDocParent)
    {
        // Generamos una URL totalmente nueva, pues el cliente padre es un attached
        // No usamos el ItsNatDocument hijo, pues dicho documento lo más probable
        // es que NO esté todavía cargado por el document owner, por
        // eso pasamos itsnat_doc_parent_id y itsnat_element_parent_id
        // porque el padre y el elemento contenedor SI que existen ahora
        // mismo y los podemos usar para obtener el documento hijo en el request.

        String urlOriginal = elemWrap.getURL(); // Es preciso copiar ahora pues es modificada en caso de éxito. Aunque no exista nunca es nulo

        String url = urlOriginal;
        url = url.trim(); // Los navegadores son tolerantes a los espacios al ppio y al final

        StringBuilder newURL = new StringBuilder();
        java.net.URL urlObj = null;
        if (url.startsWith("?"))
        {
            try
            {
                // El urlObj es únicamente para obtener después el #ref                
                urlObj = new java.net.URL("http://localhost:8080/context/servlet" + url);
            }
            catch(IOException ex) { throw new ItsNatException(ex); }

            newURL.append("?itsnat_action=" + RequestImpl.ITSNAT_ACTION_ATTACH_CLIENT);
        }
        else // URL absoluta
        {
            // Necesitamos hacer un URL absoluto hasta la query (no incluida)
            urlObj = buildAbsURLUntilQuery(url,newURL);
            newURL.append("?itsnat_action=" + RequestImpl.ITSNAT_ACTION_ATTACH_CLIENT);

            String stdSessionId = clientDocParent.getItsNatSessionImpl().getStandardSessionId();
            newURL.append("&itsnat_jsessionid_parent=" + stdSessionId);
        }

        newURL.append("&itsnat_client_parent_id=" + clientDocParent.getId()); // Es más útil el cliente que el documento

        newURL.append("&itsnat_element_parent_id=" + getId());
        newURL.append("&itsnat_element_sec_num=" + getRandomNumber());

        // Añadimos el #ref original por pura cortesía, para que funcione
        // el ejemplo con Savarese Ssrc en modo Full Remote Control que necesita un #ref.
        String ref = urlObj.getRef();
        if (ref != null)
            newURL.append("#" + ref);

        return newURL.toString();
    }

    public void setURLForClientOwner(ClientDocumentStfulOwnerImpl clientDoc)
    {
        if (itsNatDoc.getDocMutationEventListener().isEnabled())
            throw new ItsNatException("INTERNAL ERROR"); // Programación defensiva para que quede claro

        saveURLOriginal(elemWrap.getURL());
        String newURL = generateURLForClientOwner(clientDoc);
        setURL(newURL,clientDoc);
    }

    public void setURLForClientAttached(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (itsNatDoc.getDocMutationEventListener().isEnabled())
            throw new ItsNatException("INTERNAL ERROR"); // Programación defensiva para que quede claro

        saveURLOriginal(elemWrap.getURL());
        String newURL = generateURLForClientAttached(clientDoc);
        setURL(newURL,clientDoc);
    }

    public void restoreOriginalURL(ClientDocumentStfulImpl clientDoc)
    {
        if (itsNatDoc.getDocMutationEventListener().isEnabled())
            throw new ItsNatException("INTERNAL ERROR"); // Programación defensiva para que quede claro

        setURL(getURLOriginal(),clientDoc);
    }

    protected void setURL(String url,ClientDocumentStfulImpl clientDoc)
    {
        elemWrap.setURL(url,clientDoc);
    }
}
