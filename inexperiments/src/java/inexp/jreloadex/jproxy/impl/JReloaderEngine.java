package inexp.jreloadex.jproxy.impl;

import inexp.jreloadex.jproxy.impl.comp.JReloaderCompilerInMemory;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author jmarranz
 */
public class JReloaderEngine 
{
    protected JReloaderCompilerInMemory compiler = new JReloaderCompilerInMemory();
    protected ClassLoader parentClassLoader;
    protected JReloaderClassLoader customClassLoader;
    protected JavaSourcesSearch sourcesSearch;
    protected Map<String,ClassDescriptorSourceFile> hotLoadableClasses;
    protected String classFolder; // Puede ser nulo (es decir NO salvar como .class los cambios)
    
    public JReloaderEngine(ClassLoader parentClassLoader,String pathSources,String classFolder,long scanPeriod)
    {
        this.parentClassLoader = parentClassLoader;
        this.classFolder = classFolder;
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);
        this.sourcesSearch = new JavaSourcesSearch(pathSources,parentClassLoader);       
        updateTimestamps(); // Primera vez para detectar cambios en los .java respecto a los .class mientras el servidor estaba parado
        
        
        Timer timer = new Timer();        
        TimerTask task = new TimerTask()
        {
            @Override
            public void run() 
            {
                try
                {
                    updateTimestamps();
                }        
                catch(Exception ex)
                {
                    ex.printStackTrace(System.err); // Si dejamos subir la excepción se acabó el timer
                }
            }
        };                
        timer.schedule(task, scanPeriod, scanPeriod);  // Ojo, después de recursiveJavaFileSearch()      
    }

    public synchronized boolean isHotLoadableClass(String className)
    {
        // Las innerclasses no están como tales en hotLoadableClasses pues sólo está la clase contenedora pero también la consideramos hotloadable
        int pos = className.lastIndexOf('$');
        if (pos != -1) className = className.substring(0, pos);

        return hotLoadableClasses.containsKey(className);
    }
    
    public synchronized ClassDescriptor getClassDescriptor(String className)
    {
        // Puede ser el de una innerclass
        // Las innerclasses no están como tales en hotLoadableClasses pues sólo está la clase contenedora pero también la consideramos hotloadable
        int pos = className.lastIndexOf('$');
        boolean inner;        
        if (pos != -1)
        {
            className = className.substring(0, pos);
            inner = true;
        }
        else inner = false;
        ClassDescriptorSourceFile sourceDesc = hotLoadableClasses.get(className);        
        if (!inner) return sourceDesc;
        return sourceDesc.getInnerClassDescriptor(className);
    }
            
    public synchronized <T> Class<?> findClass(String className)
    {     
        // Si ya está cargada la devuelve, y si no se cargó por ningún JReloaderClassLoader se intenta cargar por el parent ClassLoader, por lo que siempre devolverá distinto de null si la clase está en el classpath, que debería ser lo normal       
        try 
        { 
            return customClassLoader.findClass(className); 
        }
        catch (ClassNotFoundException ex) 
        {
            return null;
        }
    }
    
    private synchronized void addNewClassLoader()
    {
        for(ClassDescriptorSourceFile hotClass : hotLoadableClasses.values())
        {
            hotClass.resetLastLoadedClass(); // resetea también las innerclasses
        }
        
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);               
    }
    
    private void compileAndReloadClass(ClassDescriptorSourceFile hotLoadClass)
    {
        hotLoadClass.cleanSourceCodeChanged(); // El código fuente nuevo puede haber cambiado totalmente las innerclasses antiguas (añadido, eliminado)
        
        compiler.compileClass(hotLoadClass,customClassLoader,hotLoadableClasses);
        
        reloadClass(hotLoadClass);
    }        
    
    private void reloadClass(ClassDescriptorSourceFile hotLoadClass)
    {
        customClassLoader.loadClass(hotLoadClass); 
        
        LinkedList<ClassDescriptor> innerClassDescList = hotLoadClass.getInnerClassDescriptors();
        if (innerClassDescList != null)
        {
            for(ClassDescriptor innerClassDesc : innerClassDescList)
            {
                customClassLoader.loadClass(innerClassDesc); 
            }
        }        
    }
    
    private synchronized void updateTimestamps()
    {
        LinkedList<ClassDescriptorSourceFile> updatedClasses = new LinkedList<ClassDescriptorSourceFile>();
        LinkedList<ClassDescriptorSourceFile> newClasses = new LinkedList<ClassDescriptorSourceFile>();        

        this.hotLoadableClasses = sourcesSearch.recursiveJavaFileSearch(hotLoadableClasses,updatedClasses,newClasses);  // La primera vez hotLoadableClasses es null

        if (!updatedClasses.isEmpty() || !newClasses.isEmpty())
        {   
            addNewClassLoader();
                        
            for(ClassDescriptorSourceFile hotClass : updatedClasses)
            {
                compileAndReloadClass(hotClass);
            }
            
            for(ClassDescriptorSourceFile hotClass : newClasses)
            {
                compileAndReloadClass(hotClass);
            }               
            
            for(Map.Entry<String,ClassDescriptorSourceFile> entry : hotLoadableClasses.entrySet())
            {
                //String className = entry.getKey();
                ClassDescriptorSourceFile hotClass = entry.getValue();
                if (updatedClasses.contains(hotClass))
                    continue;
                if (newClasses.contains(hotClass))
                    continue;   
                
                reloadClass(hotClass);
            }
         
        }
    }
    
}
