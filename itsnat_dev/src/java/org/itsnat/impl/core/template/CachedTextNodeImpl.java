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

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author jmarranz
 */
public class CachedTextNodeImpl extends CachedSubtreeImpl
{
    protected LinkedList entities = new LinkedList();

    public CachedTextNodeImpl(MarkupTemplateVersionImpl template,String markup,String dom)
    {
        super(template,markup);
        // La diferencia entre el markup y el dom es que el markup puede tener
        // entities tipo &amp; que o bien estaban en el markup del template
        // o bien al serializar se han convertido caracteres especiales en entities
        // mientras que en dom las entidades están resueltas (es decir está como &)
        // Podríamos salvar también el dom pero sería duplicar el caché, por lo
        // que salvamos únicamente los valores originales de los entities y sus posiciones
        // para poder así recuperar el dom original resolviendo las entities.

        int posMarkup = 0;
        int posDOM = 0;
        while(posMarkup < markup.length())
        {
            if (isEntityStart(markup,dom,posMarkup,posDOM))
            {
                // Es una entity
                char cDOM = dom.charAt(posDOM);
                int end = markup.indexOf(";",posMarkup + 1); // NO DEBE ser -1
                if (entities == null) this.entities = new LinkedList();
                entities.add(new PairPosChar(posMarkup,end,cDOM));

                posMarkup = end + 1;
            }
            else posMarkup++;
            posDOM++;
        }
    }

    protected static boolean isEntityStart(String markup,String dom,int posMarkup,int posDOM)
    {
        if (markup.charAt(posMarkup) != '&') return false; // No es el comienzo de una entity

        if (dom.charAt(posDOM) != '&') return true; // La entity reemplaza un caracter especial, ciertamente es una entity

        // Tanto en markup como en dom hay un & en ese lugar, el único
        // caso posible de entity es el propio &amp;
        if ((markup.length() - posMarkup) < "&amp;".length())
           return false; // No quedan caracteres suficientes para un &amp;

        int posSemicolon = markup.indexOf(';',posMarkup + 1);
        if (posSemicolon == -1) return false; // No hay un &amp;
        return "&amp;".equals(markup.substring(posMarkup, posSemicolon + 1));
    }

    public String getCode(boolean resolveEntities)
    {
        if (resolveEntities && (entities != null))
        {
            StringBuilder dom = new StringBuilder();
            int prevPosMarkup = 0;
            int posMarkup = 0;
            for(Iterator it = entities.iterator(); it.hasNext(); )
            {
                PairPosChar entity = (PairPosChar)it.next();

                posMarkup = entity.start;
                if (prevPosMarkup < posMarkup) // Puede darse el caso de dos entities seguidos
                    dom.append(markup.substring(prevPosMarkup,posMarkup));

                dom.append(entity.c);

                posMarkup = entity.end + 1;
                prevPosMarkup = posMarkup;
            }

            if (prevPosMarkup < markup.length())
                dom.append(markup.substring(prevPosMarkup)); // Ultimo tramo pendiente

            return dom.toString();
        }
        return markup;
    }

    public static class PairPosChar implements Serializable
    {
        public int start;
        public int end;
        public char c;

        public PairPosChar(int start,int end,char c)
        {
            this.start = start;
            this.end = end;
            this.c = c;
        }
    }
}
