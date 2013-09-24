package inexp.jreloadex.jproxy.impl;

/**
 *
 * @author jmarranz
 */
public class HotLoadableClass 
{
    protected String className;
    protected long timestamp;
    protected String path; 
    protected byte[] classBytes;
    protected Class clasz;
    
    public HotLoadableClass(String className,String path, long timestamp) 
    {
        this.className = className;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getClassName() 
    {
        return className;
    }

    public String getPath()
    {
        return path;
    }
    
    public long getTimestamp() 
    {
        return timestamp;
    }

    public void updateTimestamp(long timestamp)
    {
        long oldTimestamp = this.timestamp;
        this.timestamp = timestamp;
        if (oldTimestamp != timestamp)
        {
            // Como ha cambiado la clase, reseteamos las dependencias
            setClassBytes(null);
            setLastLoadedClass(null);
        }
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

}
