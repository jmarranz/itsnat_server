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
package org.itsnat.impl.core.markup.render;

import java.io.IOException;
import java.io.Writer;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.OutputFormat;

/**
 *
 * @author jmarranz
 */
public class ItsNatXercesHTMLSerializerOldDocFragment extends ItsNatXercesHTMLSerializerOld
{
    protected boolean doingComment;
    protected boolean doingContent;    
    
    public ItsNatXercesHTMLSerializerOldDocFragment(/*boolean xhtml,*/ Writer writer, OutputFormat format)
    {
        super(writer,format);    
    }
    
    @Override
    protected ElementState content()
        throws IOException
    {
        this.doingContent = true;
        ElementState state = super.content();
        this.doingContent = false;
        return state;
    }
    
    /**
     * La razón de este método y de los métodos comment() y content()
     * es la siguiente:
     * Cuando se serializa un comentario en solitario se usa el método
     * que usa un DocFragment, el problema es que en este caso
     * isDocumentState() devuelve true erróneamente ejecutándose
       de forma indeseada el siguiente código en el método comment():

       if ( isDocumentState() ) {
            if ( _preRoot == null )
                _preRoot = new Vector();
            _preRoot.addElement( fStrBuffer.toString() );
        }  else ...

        El devolver false en isDocumentState() no es la solución pues
        el método content() que es ejecutado durante comment() y ANTES
        del código anterior, necesita que isDocumentState() devuelva true
        para funcionar correctamente.
        Por tanto detectamos que hemos entrado en el proceso del comentario
        y que ya hemos llamado a content() en ese caso isDocumentState()
        devuelve false.
     * @return 
     */
    @Override
    protected boolean isDocumentState()
    {
        boolean serializingSingleComment = doingComment && !doingContent;
        if (serializingSingleComment)
            return false;
        
        return super.isDocumentState();
    }

    @Override
    public void comment( String text )
        throws IOException
    {
        this.doingComment = true;
        super.comment(text);
        this.doingComment = false;
    }    
}
