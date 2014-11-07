package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflater.ClassDesc;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescDrawable extends ClassDesc<Drawable>
{
    //protected static MethodContainer<ViewGroup.LayoutParams> methodGenerateLP = new MethodContainer<ViewGroup.LayoutParams>(ViewGroup.class, "generateDefaultLayoutParams", null);

    //protected Constructor<View> constructor1P;
    //protected Constructor<View> constructor3P;

    public ClassDescDrawable(ClassDescDrawableMgr classMgr, String className, ClassDescDrawable parentClass)
    {
        super(classMgr, className, parentClass);
    }

    public ClassDescDrawableMgr getClassDescDrawableMgr()
    {
        return (ClassDescDrawableMgr) classMgr;
    }

    public ClassDescDrawable getParentClassDescDrawable()
    {
        return (ClassDescDrawable) getParentClassDesc();
    }

    public Class<Drawable> getDrawableClass()
    {
        return getDeclaredClass();
    }

    protected AttrDescDrawable getAttrDescDrawable(String name)
    {
        return (AttrDescDrawable) getAttrDesc(name);
    }

    protected boolean isAttributeIgnored(String namespaceURI, String name)
    {
        return false;
    }

    public boolean setAttribute(Drawable draw, AttrParsed attr, InflatedDrawable inflated)
    {
        if (!isInit()) init();

        String namespaceURI = attr.getNamespaceURI();
        String name = attr.getName(); // El nombre devuelto no contiene el namespace
        String value = attr.getValue();

        if (isAttributeIgnored(namespaceURI, name)) return false; // Se trata de forma especial en otro lugar

        if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDescDrawable attrDesc = getAttrDescDrawable(name);
            if (attrDesc != null)
            {
                attrDesc.setAttribute(draw, attr);
            }
            else
            {
                // Es importante recorrer las clases de abajo a arriba pues algún atributo se repite en varios niveles tal y como minHeight y minWidth
                // y tiene prioridad la clase más derivada
                ClassDescDrawable parentClass = getParentClassDescDrawable();
                if (parentClass != null)
                {
                    parentClass.setAttribute(draw, attr, inflated);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrDrawableInflaterListener listener = inflated.getAttrDrawableInflaterListener();
                    if (listener != null)
                    {
                        PageImpl page = (inflated instanceof InflatedDrawablePage) ? ((InflatedDrawablePage) inflated).getPageImpl() : null;
                        listener.setAttribute(page, draw, namespaceURI, name, value);
                    }
                }
            }
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrDrawableInflaterListener listener = inflated.getAttrDrawableInflaterListener();
            if (listener != null)
            {
                PageImpl page = (inflated instanceof InflatedDrawablePage) ? ((InflatedDrawablePage) inflated).getPageImpl() : null;
                listener.setAttribute(page, draw, namespaceURI, name, value);
            }
        }

        return true;
    }


    public boolean removeAttribute(Drawable draw, String namespaceURI, String name, InflatedDrawable inflated)
    {
        if (!isInit()) init();

        if (isAttributeIgnored(namespaceURI, name)) return false; // Se trata de forma especial en otro lugar

        if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDescDrawable attrDesc = getAttrDescDrawable(name);
            if (attrDesc != null)
            {
                attrDesc.removeAttribute(draw);
            }
            else
            {
                ClassDescDrawable parentClass = getParentClassDescDrawable();
                if (parentClass != null)
                {
                    parentClass.removeAttribute(draw, namespaceURI, name, inflated);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrDrawableInflaterListener listener = inflated.getAttrDrawableInflaterListener();
                    if (listener != null)
                    {
                        PageImpl page = (inflated instanceof InflatedDrawablePage) ? ((InflatedDrawablePage) inflated).getPageImpl() : null;
                        listener.removeAttribute(page, draw, namespaceURI, name);
                    }
                }
            }
        }
        else
        {
            // No se encuentra opción de proceso custom
            AttrDrawableInflaterListener listener = inflated.getAttrDrawableInflaterListener();
            if (listener != null)
            {
                PageImpl page = (inflated instanceof InflatedDrawablePage) ? ((InflatedDrawablePage) inflated).getPageImpl() : null;
                listener.removeAttribute(page, draw, namespaceURI, name);
            }
        }

        return true;
    }

}
