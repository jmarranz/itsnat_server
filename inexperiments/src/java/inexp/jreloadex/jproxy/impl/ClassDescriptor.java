package inexp.jreloadex.jproxy.impl;

/**
 *
 * @author jmarranz
 */
public class ClassDescriptor 
{
    protected String className;
    protected byte[] classBytes;
    protected Class clasz;    
    
    public ClassDescriptor(String className) 
    {
        this.className = className;
    }    
    
    public String getClassName() 
    {
        return className;
    }
    
    public byte[] getClassBytes() 
    {
        return classBytes;
    }
    
    public void setClassBytes(byte[] classBytes)
    {
        this.classBytes = classBytes;
    }

    public Class getLastLoadedClass() 
    {
        return clasz;
    }

    public void setLastLoadedClass(Class clasz) 
    {
        this.clasz = clasz;
    }    
  
    /*
    public String getClassFileNameFromClassName()
    {    
        return getClassFileNameFromClassName(className);
    }
    */
    
    public static String getClassFileNameFromClassName(String className)
    {
        // Es válido también para las innerclasses (ej Nombre$Otro => Nombre$Otro.class,  Nombre$1 => Nombre$1.class 
        int pos = className.lastIndexOf(".");
        if(pos != -1) className = className.substring(pos + 1);
        return className + ".class";    
    }
    
    public static String getRelativeClassFilePathFromClassName(String className)
    {
        return className.replace('.','/') + ".class";    
    }
    
    public static String getClassNameFromRelativeClassFilePath(String path)
    {
        // Ej. org/w3c/dom/Element.class => org.w3c.dom.Element
        String binaryName = path.replaceAll("/", ".");
        return binaryName.replaceAll(".class$", "");    // El $ indica "el .class del final" 
    }
    
    public static String getClassNameFromPackageAndClassFileName(String packageName,String fileName)
    {
        String className = packageName + "." + fileName;
        return className.replaceAll(".class$", "");    // El $ indica "el .class del final" 
    }
}
