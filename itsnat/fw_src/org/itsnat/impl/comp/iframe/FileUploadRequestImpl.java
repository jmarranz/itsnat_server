/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/
package org.itsnat.impl.comp.iframe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletRequest;
import org.itsnat.comp.iframe.FileUploadRequest;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

/**
 *
 * @author jmarranz
 */
public class FileUploadRequestImpl implements FileUploadRequest
{
    protected static final byte CR = 0x0D;
    protected static final byte LF = 0x0A;
    protected static final byte[] HEADER_SEPARATOR = { CR, LF, CR, LF };
    protected static final int HEADER_PART_SIZE_MAX = 10240;

    protected HTMLIFrameFileUploadImpl parent;
    protected String contentDispositionType; // Por ahora no lo hacemos público porque no veo qué aporte
    protected String contentDispositionName;
    protected String contentDispositionFileName;
    protected String contentType;
    protected KnownSizeInputStreamImpl fileStream;

    public FileUploadRequestImpl(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ServletRequest servReq = request.getServletRequest();
        String encoding = servReq.getCharacterEncoding(); // Esperamos "UTF-8" pues lo imponemos nosotros al recibir el request
        int size = servReq.getContentLength();
        InputStream input;
        try{ input = servReq.getInputStream(); } catch(IOException ex) { throw new ItsNatException(ex); }

        // -----------------------------7da399d2b03e4
        // Content-Disposition: form-data; name="fileContent"; filename="C:\tmp\portatil.txt"
        // Content-Type: text/plain
        // CR/LF
        // ...
        // CR/LF
        // -----------------------------7da399d2b03e4--CR/LF

        int beginSepSize = readBeginSeparator(input);
        int[] headerByteSize = new int[1];
        readHeaders(input,encoding,headerByteSize);
        int endSepSize = beginSepSize + 4; // El limitador tiene dos '--' que el más tras el número, un CR/LF antes y al final
        int fileSize = size - beginSepSize - headerByteSize[0] - endSepSize;
        this.fileStream = new KnownSizeInputStreamImpl(input,fileSize);
    }

    public String getFieldName()
    {
        return contentDispositionName;
    }

    public String getFileName()
    {
        return contentDispositionFileName;
    }

    public String getContentType()
    {
        return contentType;
    }

    public long getFileSize()
    {
        return fileStream.getSizeMax();
    }

    public InputStream getFileUploadInputStream()
    {
        return fileStream;
    }

    public void processHeaders(String headers)
    {
        // Content-Disposition: form-data; name="fileContent"; filename="C:\tmp\portatil.txt"
        // Content-Type: text/plain

        String header;
        int pos = headers.indexOf("\r\n");
        while(pos > 0)
        {
            header = headers.substring(0,pos);
            parseHeaderNameValue(header);
            headers = headers.substring(pos + 2);
            pos = headers.indexOf("\r\n");
        }
    }

    public void parseHeaderNameValue(String header)
    {
        String headerLower = header.toLowerCase();
        int pos = headerLower.indexOf("content-disposition:"); // Por si acaso lo buscamos en minúsculas
        if (pos == 0)
        {
            // http://www.ietf.org/rfc/rfc2183.txt
            // Ej: Content-Disposition: form-data; name="fileContent"; filename="C:\tmp\portatil.txt"
            header = header.substring("content-disposition:".length());
            pos = header.indexOf(';');
            String dispType = header.substring(0,pos);
            dispType = dispType.trim(); // Quitamos el espacio primero si existe
            this.contentDispositionType = dispType;
            do
            {
                header = header.substring(pos + 1);
                pos = header.indexOf(';');
                if (pos == -1) pos = header.length();
                String param = header.substring(0,pos);
                param = param.trim(); // Quitamos el espacio primero si existe
                int posEq = param.indexOf('=');
                if (posEq != -1)
                {
                    String paramName = param.substring(0,posEq);
                    String paramValue = param.substring(posEq + 1);
                    paramValue = paramValue.substring(1,paramValue.length() - 1); // Quitamos las "
                    if ("name".equals(paramName))
                        this.contentDispositionName = paramValue;
                    else if ("filename".equals(paramName))
                        this.contentDispositionFileName = paramValue;
                    // Otros parámetros los ignoramos
                }
            }
            while(pos != header.length());
        }
        else
        {
            pos = headerLower.indexOf("content-type:");  // Por si acaso lo buscamos en minúsculas
            if (pos == 0)
            {
                // Ej. Content-Type: text/plain
                header = header.substring("content-type:".length());
                header = header.trim(); // Quitamos el espacio al comienzo
                this.contentType = header;
            }
        }
    }

    public static int readBeginSeparator(InputStream input)
    {
        // Ej. -----------------------------7da399d2b03e4
        // terminado en un CR/LF
        int b;
        int size = 0;
        do
        {
            try
            {
                b = input.read();
            }
            catch (IOException e) { throw new ItsNatException("Input stream ended unexpectedly"); }
            size++;
        }
        while(b != CR);

        try
        {
            b = input.read();
        }
        catch (IOException e) { throw new ItsNatException("Input stream ended unexpectedly"); }

        if (b != LF) throw new ItsNatException("Malformed header");
        size++;
        return size;
    }


    public String readHeaders(InputStream input,String encoding,int[] readedBytes)
    {
        int i = 0;
        int b;
        ByteArrayOutputStream byteHeader = new ByteArrayOutputStream(); // Así soportamos caracteres multibyte
        int size = 0;
        while (i < HEADER_SEPARATOR.length)
        {
            try
            {
                b = input.read(); // No esperamos cabeceras grandes (un único archivo) leemos byte a byte sin buffer
            }
            catch (IOException e) { throw new ItsNatException("Input stream ended unexpectedly"); }
            size++;
            if (size > HEADER_PART_SIZE_MAX)
                throw new ItsNatException("Header section has more than " + HEADER_PART_SIZE_MAX + " bytes (maybe it is not properly terminated)");

            if (b == HEADER_SEPARATOR[i]) i++; // Parece que estamos en el separador
            else i = 0; // Falsa alarma, vuelta a empezar

            byteHeader.write(b);
        }
        readedBytes[0] += size;

        String header = null;
        if (encoding != null)
        {
            try
            {
                header = byteHeader.toString(encoding);
            }
            catch (UnsupportedEncodingException ex)
            {
                header = byteHeader.toString(); // Encoding por defecto, por si acaso
            }
        }
        else
        {
            header = byteHeader.toString();
        }

        processHeaders(header);

        return header;
    }
}
