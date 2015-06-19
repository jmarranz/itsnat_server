package org.itsnat.droid.impl.browser;

import org.itsnat.droid.ItsNatDroidException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Jose on 18/06/2015.
 */
public class HttpFileCache
{
    private long maxCacheSize;
    private long currentCacheSize = 0;
    private Map<String,FileCached> mapByUrl = new HashMap<String,FileCached>();
    private SortedMap<String,FileCached> mapByLastAccess = new TreeMap<String,FileCached>();

    public HttpFileCache(long maxCacheSize)
    {
        this.maxCacheSize = maxCacheSize;
    }

    public void setMaxCacheSize(long maxCacheSize)
    {
        this.maxCacheSize = maxCacheSize;
    }

    public synchronized FileCached get(String url)
    {
        FileCached file = mapByUrl.get(url);
        if (file == null) return null;
        long prevLastAccess = file.getLastAccessTimestamp();
        long currentLastAccess = file.updateLastAccessTimestamp();
        if (currentLastAccess != prevLastAccess) // por si acaso ocurre dos accesos del mismo archivo en el mismo milisegundo
        {
            FileCached file2 = mapByLastAccess.remove(genLastAccessKey(prevLastAccess,file));
            if (file2 != file) throw new ItsNatDroidException("Internal Error"); // file2 en teoria DEBE ser igual que file, pero por si acaso...
            mapByLastAccess.put(genLastAccessKey(currentLastAccess,file), file);
        }

        return file;
    }

    public synchronized void put(FileCached file)
    {
        FileCached oldFile = mapByUrl.get(file.getUrl()); // La intención es eliminarlo no necesitamos actualizar el LastAccessTimestamp y registrarlo y por tanto no llamamos a get(url)
        if (oldFile != null)
            removeInternal(oldFile); // asi aseguramos que dos put a la vez con el mismo FileCached solo inserten un FileCached no dos

        if (mapByUrl.put(file.getUrl(),file) != null) throw new ItsNatDroidException("Internal Error");
        long lastAccess = file.getLastAccessTimestamp();
        if (mapByLastAccess.put(genLastAccessKey(lastAccess,file), file) != null) throw new ItsNatDroidException("Internal Error");

        currentCacheSize += file.getContentByteArray().length;

        if (currentCacheSize > maxCacheSize)
            cleanLessUsedFilesToMaxCacheSize();
    }

    public synchronized void remove(FileCached file)
    {
        FileCached oldFile = get(file.getUrl());
        if (oldFile == null) return;
        removeInternal(file); // asi aseguramos que dos remove a la vez con el mismo FileCached solo elimine uno y el otro no falle
    }

    private void removeInternal(FileCached file)
    {
        if (mapByUrl.remove(file.getUrl()) != file) throw new ItsNatDroidException("Internal Error");
        long lastAccess = file.getLastAccessTimestamp();
        if (mapByLastAccess.remove(genLastAccessKey(lastAccess,file)) != file) throw new ItsNatDroidException("Internal Error");

        currentCacheSize -= file.getContentByteArray().length;
    }

    private void cleanLessUsedFilesToMaxCacheSize()
    {
        for(Iterator<Map.Entry<String,FileCached>> it = mapByLastAccess.entrySet().iterator(); it.hasNext(); )
        {
            if (currentCacheSize <= maxCacheSize) break;

            Map.Entry<String,FileCached> entry = it.next();
            FileCached file = entry.getValue();
            currentCacheSize -= file.getContentByteArray().length;
            mapByUrl.remove(file.getUrl());
            it.remove();
        }
    }

    private String genLastAccessKey(long lastAccess,FileCached file)
    {
        return lastAccess + "-" + file.getUrl();
    }

    static class FileCached
    {
        private String url;
        private long lastModified;
        private byte[] contentByteArray;
        private long lastAccessTimestamp;

        public FileCached(String url, long lastModified, byte[] contentByteArray)
        {
            this.url = url;
            this.lastModified = lastModified;
            this.contentByteArray = contentByteArray;
            this.lastAccessTimestamp = System.currentTimeMillis();
        }

        public String getUrl() {
            return url;
        }

        public long getLastModified() {
            return lastModified;
        }

        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }

        public byte[] getContentByteArray() {
            return contentByteArray;
        }

        public void setContentByteArray(byte[] contentByteArray) {
            this.contentByteArray = contentByteArray;
        }

        private long getLastAccessTimestamp() {
            return lastAccessTimestamp;
        }

        private long updateLastAccessTimestamp()
        {
            return this.lastAccessTimestamp = System.currentTimeMillis();
        }
    }
}


