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

package org.itsnat.core.tmpl;

import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.w3c.dom.DocumentFragment;

/**
 * Represents an (X)HTML markup fragment template. Concrete fragments are created
 * using this template and can be inserted into documents.
 *
 * <p>An (X)HTML fragment contains actually two fragments: the &lt;head&gt; content
 * and the &lt;body&gt; content. The &lt;itsnat:include&gt; element uses the appropriated fragment
 * automatically, otherwise call the required fragment calling the specific <code>loadDocumentFragmentX</code>
 * method</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatHTMLDocFragmentTemplate extends ItsNatDocFragmentTemplate
{
    /**
     * Creates a <code>org.w3c.dom.DocumentFragment</code> using the &lt;head&gt;
     * content of the markup template.
     * This <code>DocumentFragment</code> is ready to
     * be inserted into the specified <code>org.w3c.dom.Document</code>.
     *
     * @param docTarget the document used to create the fragment.
     * @return the document fragment ready to be inserted.
     */
    public DocumentFragment loadDocumentFragmentHead(ItsNatDocument docTarget);

    /**
     * Creates a <code>org.w3c.dom.DocumentFragment</code> using the &lt;head&gt;
     * content of the markup template.
     * This <code>DocumentFragment</code> is ready to
     * be inserted into the specified <code>org.w3c.dom.Document</code>.
     *
     * <p>Request and reponse parameters can be null, only are useful when the template
     * is loaded using a custom {@link TemplateSource}
     * </p>
     *
     * @param docTarget the document used to create the fragment.
     * @param request current request. Maybe null.
     * @param response current response. Maybe null.
     * @return the document fragment ready to be inserted.
     */
    public DocumentFragment loadDocumentFragmentHead(ItsNatDocument docTarget,ItsNatServletRequest request,ItsNatServletResponse response);

    /**
     * Creates a <code>org.w3c.dom.DocumentFragment</code> using the &lt;body&gt;
     * content of the markup template.
     * This <code>DocumentFragment</code> is ready to
     * be inserted into the specified <code>org.w3c.dom.Document</code>.
     *
     * @param docTarget the document used to create the fragment.
     * @return the document fragment ready to be inserted.
     */
    public DocumentFragment loadDocumentFragmentBody(ItsNatDocument docTarget);

    /**
     * Creates a <code>org.w3c.dom.DocumentFragment</code> using the &lt;body&gt;
     * content of the markup template.
     * This <code>DocumentFragment</code> is ready to
     * be inserted into the specified <code>org.w3c.dom.Document</code>.
     *
     * <p>Request and reponse parameters can be null, only are useful when the template
     * is loaded using a custom {@link TemplateSource}
     * </p>
     *
     * @param docTarget the document used to create the fragment.
     * @param request current request. Maybe null.
     * @param response current response. Maybe null.
     * @return the document fragment ready to be inserted.
     */
    public DocumentFragment loadDocumentFragmentBody(ItsNatDocument docTarget,ItsNatServletRequest request,ItsNatServletResponse response);
}
