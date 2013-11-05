package inexp.jreloadex.jproxy.impl;

import inexp.jreloadex.jproxy.impl.comp.JReloaderCompilerInMemory;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    protected Map<String,ClassDescriptorSourceFile> sourceFileMap;
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
        // Las innerclasses no están como tales en sourceFileMap pues sólo está la clase contenedora pero también la consideramos hotloadable
        int pos = className.lastIndexOf('$');
        if (pos != -1) className = className.substring(0, pos);

        return sourceFileMap.containsKey(className);
    }
    
    public synchronized ClassDescriptor getClassDescriptor(String className)
    {
        // Puede ser el de una innerclass
        // Las innerclasses no están como tales en sourceFileMap pues sólo está la clase contenedora pero también la consideramos hotloadable
        String parentClassName;
        int pos = className.lastIndexOf('$');        
        boolean inner;        
        if (pos != -1)
        {
            parentClassName = className.substring(0, pos);
            inner = true;
        }
        else
        {
            parentClassName = className;
            inner = false;
        }
        ClassDescriptorSourceFile sourceDesc = sourceFileMap.get(parentClassName);        
        if (!inner) return sourceDesc;
        return sourceDesc.getInnerClassDescriptor(className,true);
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
        for(ClassDescriptorSourceFile sourceFile : sourceFileMap.values())
        {
            sourceFile.resetLastLoadedClass(); // resetea también las innerclasses
        }
        
        this.customClassLoader = new JReloaderClassLoader(this,parentClassLoader);               
    }
    
    private void compileReloadAndSaveClass(ClassDescriptorSourceFile sourceFile)
    {
        sourceFile.cleanSourceCodeChanged(); // El código fuente nuevo puede haber cambiado totalmente las innerclasses antiguas (añadido, eliminado)
        
        compiler.compileClass(sourceFile,customClassLoader,sourceFileMap);
        
        reloadClass(sourceFile,false); // No hace falta que detectemos las innerclasses porque al compilar se "descubren"

        saveClass(sourceFile);        
    }        
    
    private void reloadClass(ClassDescriptorSourceFile sourceFile,boolean detectInnerClasses)
    {
        Class clasz = customClassLoader.loadClass(sourceFile);      

        LinkedList<ClassDescriptor> innerClassDescList = sourceFile.getInnerClassDescriptors(); 
        if (innerClassDescList != null && !innerClassDescList.isEmpty())
        {
            for(ClassDescriptor innerClassDesc : innerClassDescList)
            {
                customClassLoader.loadClass(innerClassDesc);           
            }
        }        
        else if (detectInnerClasses)
        {
            clasz.getDeclaredClasses(); // Provoca que las inner clases indirectamente se procesen y carguen a través del JReloaderClassLoader de la clase padre clasz
            
            // Ahora bien, lo anterior NO sirve para las anonymous inner classes, afortunadamente en ese caso podemos conocer y cargar por fuerza bruta
            // http://stackoverflow.com/questions/1654889/java-reflection-how-can-i-retrieve-anonymous-inner-classes?rq=1
            
            for(int i = 1; i < Integer.MAX_VALUE; i++)
            {
                String anonClassName = sourceFile.getClassName() + "$" + i;                 
                Class innerClasz = customClassLoader.loadInnerClass(sourceFile,anonClassName);
                if (innerClasz == null) break; // No hay más o no hay ninguna (si i es 1)
            } 
        }     
    }    
    
    private void saveClass(ClassDescriptorSourceFile sourceFile)
    {
        if (classFolder == null) return;
        
        // Salvamos la clase principal
        {
            String classFilePath = ClassDescriptor.getAbsoluteClassFilePathFromClassNameAndClassPath(sourceFile.getClassName(),classFolder);
            JReloaderUtil.saveFile(classFilePath,sourceFile.getClassBytes());
        }

        // Salvamos las innerclasses si hay
        LinkedList<ClassDescriptor> innerClassDescList = sourceFile.getInnerClassDescriptors();            
        if (innerClassDescList != null && !innerClassDescList.isEmpty())
        {
            for(ClassDescriptor innerClassDesc : innerClassDescList)
            {
                String classFilePath = ClassDescriptor.getAbsoluteClassFilePathFromClassNameAndClassPath(innerClassDesc.getClassName(),classFolder);
                JReloaderUtil.saveFile(classFilePath,innerClassDesc.getClassBytes());                
            }
        }                      
     
    }    
    
    private synchronized void updateTimestamps()
    {
        LinkedList<ClassDescriptorSourceFile> updatedSourceFiles = new LinkedList<ClassDescriptorSourceFile>();
        LinkedList<ClassDescriptorSourceFile> newSourceFiles = new LinkedList<ClassDescriptorSourceFile>();        

        this.sourceFileMap = sourcesSearch.recursiveJavaFileSearch(sourceFileMap,updatedSourceFiles,newSourceFiles);  // La primera vez sourceFileMap es null

        if (!updatedSourceFiles.isEmpty() || !newSourceFiles.isEmpty())
        {   
            addNewClassLoader();
                        
            for(ClassDescriptorSourceFile sourceFile : updatedSourceFiles)
            {
                compileReloadAndSaveClass(sourceFile);
            }
            
            for(ClassDescriptorSourceFile sourceFile : newSourceFiles)
            {
                compileReloadAndSaveClass(sourceFile);
            }               
            
            for(Map.Entry<String,ClassDescriptorSourceFile> entry : sourceFileMap.entrySet())
            {
                //String className = entry.getKey();
                ClassDescriptorSourceFile sourceFile = entry.getValue();
                if (updatedSourceFiles.contains(sourceFile))
                    continue;
                if (newSourceFiles.contains(sourceFile))
                    continue;   
                
                reloadClass(sourceFile,true); // Ponemos detectInnerClasses a true porque son archivos fuente que posiblemente nunca se hayan tocado desde la carga inicial y por tanto quizás se desconocen las innerclasses
            }
         
        }
    }
    
}
