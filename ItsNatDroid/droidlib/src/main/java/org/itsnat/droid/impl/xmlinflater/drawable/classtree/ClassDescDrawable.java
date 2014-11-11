package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflater.ClassDesc;
import org.itsnat.droid.impl.xmlinflater.ConstructorContainer;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.attr.AttrDescDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class ClassDescDrawable extends ClassDesc<Drawable>
{
    protected ConstructorContainer<Drawable> rootConstructor;

    //protected static MethodContainer<ViewGroup.LayoutParams> methodGenerateLP = new MethodContainer<ViewGroup.LayoutParams>(ViewGroup.class, "generateDefaultLayoutParams", null);

    //protected Constructor<View> constructor1P;
    //protected Constructor<View> constructor3P;

    public ClassDescDrawable(ClassDescDrawableMgr classMgr, String className, ClassDescDrawable parentClass)
    {
        super(classMgr, className, parentClass);
        this.rootConstructor = new ConstructorContainer<Drawable>(this,null);
    }

    public ClassDescDrawableMgr getClassDescDrawableMgr()
    {
        return (ClassDescDrawableMgr) classMgr;
    }

    public ClassDescDrawable getParentClassDescDrawable()
    {
        return (ClassDescDrawable) getParentClassDesc();
    }

    public static PageImpl getPageImpl(InflatedDrawable inflated)
    {
        // A día de hoy nunca es null pero podría serlo en el futuro por simetría con InflatedLayout si el drwable puede leerse de los assets por ej
        return (inflated instanceof InflatedDrawablePage) ? ((InflatedDrawablePage) inflated).getPageImpl() : null;
    }

    public Class<Drawable> getDrawableClass()
    {
        return getDeclaredClass();
    }

    protected AttrDescDrawable getAttrDescDrawable(String name)
    {
        return (AttrDescDrawable) getAttrDesc(name);
    }

    public Drawable createRootDrawable(InflatedDrawable inflatedDrawable, Resources res)
    {
        return rootConstructor.invoke();
    }

    protected boolean isAttributeIgnored(String namespaceURI, String name)
    {
        return false;
    }

    public boolean setAttribute(Drawable draw, AttrParsed attr, InflatedDrawable inflated,Context ctx)
    {
        if (!isInit()) init();

        String namespaceURI = attr.getNamespaceURI();
        String name = attr.getName(); // El nombre devuelto no contiene el namespace

        if (isAttributeIgnored(namespaceURI, name)) return false; // Se trata de forma especial en otro lugar

        if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
        {
            AttrDescDrawable attrDesc = getAttrDescDrawable(name);
            if (attrDesc != null)
            {
                attrDesc.setAttribute(draw, attr,ctx);
            }
            else
            {
                // Es importante recorrer las clases de abajo a arriba pues algún atributo se repite en varios niveles tal y como minHeight y minWidth
                // y tiene prioridad la clase más derivada
                ClassDescDrawable parentClass = getParentClassDescDrawable();
                if (parentClass != null)
                {
                    parentClass.setAttribute(draw, attr, inflated,ctx);
                }
                else
                {
                    // No se encuentra opción de proceso custom
                    AttrDrawableInflaterListener listener = inflated.getAttrDrawableInflaterListener();
                    if (listener != null)
                    {
                        PageImpl page = getPageImpl(inflated); // Puede ser nulo
                        String value = attr.getValue();
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
                PageImpl page = getPageImpl(inflated); // Puede ser nulo
                String value = attr.getValue();
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
                        PageImpl page = getPageImpl(inflated); // Puede ser nulo
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
                PageImpl page = getPageImpl(inflated); // Puede ser nulo
                listener.removeAttribute(page, draw, namespaceURI, name);
            }
        }

        return true;
    }

}
