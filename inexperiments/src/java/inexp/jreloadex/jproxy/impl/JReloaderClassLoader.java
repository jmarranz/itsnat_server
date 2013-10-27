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
    
    public synchronized Class defineClass(ClassDescriptor classDesc)
    {    
        String className = classDesc.getClassName();
        byte[] classBytes = classDesc.getClassBytes();
        Class clasz = defineClass(className,classBytes, 0, classBytes.length);   
        classDesc.setLastLoadedClass(clasz);
        return clasz;
    }
    
    @Override
    protected synchronized Class<?> findClass(String name) throws ClassNotFoundException 
    {
        Class<?> cls = findLoadedClass(name);
        if (cls == null)
            return getParent().loadClass(name); // Dará un ClassNotFoundException si no puede cargarla
        
        return cls;
    }    

    public synchronized Class loadClass(ClassDescriptor classDesc)
    {    
        Class clasz = classDesc.getLastLoadedClass();
        if (clasz != null && clasz.getClassLoader() == this) return clasz; // Glup, ya fue cargada
        return defineClass(classDesc); 
    }    
    
    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException 
    {
        // Inspiraciones en URLClassLoader.findClass y en el propio análisis de ClassLoader.loadClass
        // Lo redefinimos por si acaso porque el objetivo es recargar todas las clases hot-reloaded en este ClassLoader y no delegar en el parent 
        // (el comportamiento por defecto de loadClass) pues las clases cargadas con el parent tenderán a cargar las clases vinculadas con dicho ClassLoader
        
        // En teoría este método redefinido no es necesario porque manualmente detectamos los cambios de código fuente, recompilamos y recargamos explícitamente
        // pero al hacer un defineClass de una clase con innerclasses debería cargar las inner clases miembro dependientes por aquí, el caso es que todavía no he 
        // detectado ese caso por lo que la necesidad de este método está cuestionada

        Class<?> cls = findLoadedClass(name);
        if (cls == null)
        {
            ClassDescriptor classDesc = engine.getClassDescriptor(name);
            if (classDesc != null)
            {
                byte[] classBytes = classDesc.getClassBytes();
                if (classBytes == null) // No debería ocurrir ¡¡¡NUNCA!!!
                {
                    String relClassPath = ClassDescriptor.getRelativeClassFilePathFromClassName(name); 
                    URL urlClass = getResource(relClassPath);
                    classBytes = JReloaderUtil.readURL(urlClass);   
                    classDesc.setClassBytes(classBytes);
                }
                
                cls = defineClass(classDesc); 
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
