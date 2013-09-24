package inexp.jreloadex.jproxy.impl;

import java.net.URL;

/**
 *
 * @author jmarranz
 */
public class JReloaderClassLoader extends ClassLoader
{
    protected JReloaderEngine engine;
    
    public JReloaderClassLoader(JReloaderEngine engine,ClassLoader classLoader)
    {
        super(classLoader);
        
        this.engine = engine;
    }

    public synchronized Class defineClass(String className,byte[] content)
    {
        return defineClass(className,content, 0, content.length);    
    }
    
    @Override
    protected synchronized Class<?> findClass(String name) throws ClassNotFoundException 
    {
        Class<?> cls = findLoadedClass(name);
        if (cls == null)
            return getParent().loadClass(name); // Dará un ClassNotFoundException si no puede cargarla
        
        return cls;
    }    

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException 
    {
        // Inspiraciones en URLClassLoader.findClass y en el propio análisis de ClassLoader.loadClass
        // Lo tenemos que redefinir porque el objetivo es recargar todas las clases hot-reloaded en este ClassLoader y no delegar en el parent 
        // (el comportamiento por defecto de loadClass) pues las clases cargadas con el parent tenderán a cargar las clases vinculadas con dicho ClassLoader
        // Aunque siempre que podemos hacemos un defineClass, lo hacemos también aquí porque las innerclasses y las clases miembro de un .java generan sus propios
        // .class que sólo son conocidos de antemano analizando el .java (no en el .class) o en el momento de cargar, las clases miembro se podrían obtener de Class.getClasses() pero no así
        // las innerclasses.
        Class<?> cls = findLoadedClass(name);
        if (cls == null)
        {                
            if (engine.isHotLoadableClass(name))
            {
                String relClassPath = name.replaceAll("\\.", "/") + ".class"; 
            
                URL urlClass = getResource(relClassPath);
                byte[] classBytes = JReloaderUtil.readURL(urlClass);            
                cls = defineClass(name,classBytes); 
            }
            
            if (cls == null)
            {
                cls = getParent().loadClass(name); // Dará un ClassNotFoundException si no puede cargarla
            }
        }        
        
        if (cls == null) throw new ClassNotFoundException(name);
        
	if (resolve) {
	    resolveClass(cls);
	}
        return cls;
    }
    

}
