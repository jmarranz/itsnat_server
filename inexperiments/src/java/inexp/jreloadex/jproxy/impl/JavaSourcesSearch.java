package inexp.jreloadex.jproxy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class JavaSourcesSearch 
{
    protected String pathSources;
    protected ClassLoader parentClassLoader;
    
    public JavaSourcesSearch(String pathSources,ClassLoader parentClassLoader)
    {
        this.pathSources = new File(pathSources).getAbsolutePath(); // Para normalizar
        this.parentClassLoader = parentClassLoader;
    }
        
    public Map<String,ClassDescriptorSourceFile> recursiveJavaFileSearch(Map<String,ClassDescriptorSourceFile> oldClassMap,LinkedList<ClassDescriptorSourceFile> updatedClasses,LinkedList<ClassDescriptorSourceFile> newClasses)
    {
        Map<String,ClassDescriptorSourceFile> newClassMap = new HashMap<String,ClassDescriptorSourceFile>();
        String[] children = new File(pathSources).list(); 
        recursiveJavaFileSearch(pathSources,children,oldClassMap,newClassMap,updatedClasses,newClasses);
        return newClassMap;
    }
    
    private void recursiveJavaFileSearch(String parentPath,String[] relPathList,Map<String,ClassDescriptorSourceFile> oldClassMap,Map<String,ClassDescriptorSourceFile> newClassMap,LinkedList<ClassDescriptorSourceFile> updatedClasses,LinkedList<ClassDescriptorSourceFile> newClasses)
    {
        for(String relPath : relPathList)
        {
            File file = new File(parentPath + "/" + relPath);        
            if (file.isDirectory())
            {
                String[] children = file.list();   
                recursiveJavaFileSearch(file.getAbsolutePath(),children,oldClassMap,newClassMap,updatedClasses,newClasses);
            }
            else
            {
                int pos = relPath.lastIndexOf('.');
                if (pos > 0) 
                {
                    String ext = relPath.substring(pos+1);
                    if ("java".equals(ext))
                    {
                        String path = file.getAbsolutePath();
                        String className = ClassDescriptorSourceFile.getClassNameFromSourceFileAbsPath(path, pathSources);
                        long timestampSourceFile = file.lastModified();
                        ClassDescriptorSourceFile hotClass;
                        if (oldClassMap != null)
                        {
                            hotClass = oldClassMap.get(className);
                            
                            if (hotClass != null) // Si es null es que la clase es nueva
                            {
                                long oldTimestamp = hotClass.getTimestamp();
                                if (timestampSourceFile > oldTimestamp)
                                {
                                    hotClass.updateTimestamp(timestampSourceFile);
                                    updatedClasses.add(hotClass);
                                }
                                
                                oldClassMap.remove(className); // Para que sólo queden las clases que han sido eliminadas
                            }          
                            else // Clase nueva
                            {
                                hotClass = new ClassDescriptorSourceFile(className,file,timestampSourceFile);
                                newClasses.add(hotClass);
                            }
                        }
                        else  // Primera vez, vemos si el código fuente se ha cambiado respecto a los .class en el sistema de archivos
                        {
                            String relClassPath = ClassDescriptor.getRelativeClassFilePathFromClassName(className);
                            java.net.URL urlClass = parentClassLoader.getResource(relClassPath);
                            if (urlClass != null)
                            {
                                String urlClassExt = urlClass.toExternalForm();
                                long timestampCompiledClass = urlClassExt.startsWith("file:") ? new File(urlClass.getPath()).lastModified() : 0;  // 0 cuando está en un JAR

                                if (timestampSourceFile > timestampCompiledClass)
                                {
                                    // Si el .class está en un JAR no hay forma de saber si el fuente .java es más actual que el .class por lo que siempre se considerará que el archivo fuente ha sido modificado
                                    hotClass = new ClassDescriptorSourceFile(className,file,timestampSourceFile);
                                    updatedClasses.add(hotClass);
//System.out.println("UPDATED: " + className + " " + urlClass.toExternalForm() + " " + (timestampSourceFile - timestampCompiledClass));
                                }
                                else
                                {
                                    // Esto es lo normal, que el .class sea más reciente que el .java
                                    hotClass = new ClassDescriptorSourceFile(className,file,timestampCompiledClass);
//System.out.println("NOT UPDATED: " + className + " " + urlClass.toExternalForm() + " " + (timestampSourceFile - timestampCompiledClass));                                    
                                }
                                
                                byte[] classBytes = JReloaderUtil.readURL(urlClass);
                                hotClass.setClassBytes(classBytes);
                            }
                            else // No hay .class, es un archivo fuente nuevo
                            {
                                hotClass = new ClassDescriptorSourceFile(className,file,timestampSourceFile);
                                newClasses.add(hotClass);
                            }
                        }

                        newClassMap.put(className,hotClass);
                    }
                }
            }                
        }
    }    
    

}
