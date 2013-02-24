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

package org.itsnat.impl.core.util.lic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author jmarranz
 */
public class LicenseChanger
{
    public static final String SOURCE_PATH = "C:\\trabajo\\empresa\\opensource\\itsnat_dev\\src\\java\\org\\itsnat";
    public static final String FILE_TEST = "C:\\trabajo\\empresa\\opensource\\itsnat_dev\\src\\java\\org\\itsnat\\impl\\core\\util\\WeakSetImpl.java";
    public static final boolean SIMULATION = true;

    public static void main(String[] args) throws Exception
    {
        if (!SIMULATION)
        {
            processTreeFile(new File(SOURCE_PATH));
        }
        else
        {
            processFile(new File(FILE_TEST));
        }
    }

    public static void processTreeFile(File file) throws Exception
    {
        if (file.isDirectory())
        {
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++)
            {
                processTreeFile(files[i]);
            }
        }
        else processFile(file);
    }

    public static void processFile(File file) throws Exception
    {
        System.out.println(file.getAbsolutePath());
        //System.out.println(file.length());

        StringBuilder str = new StringBuilder();
        FileInputStream input = new FileInputStream(file);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[10*1024];
        int res;
        while((res = input.read(buffer)) != -1)
        {
            str.append(new String(buffer,0,res));
            output.write(buffer, 0, res);
        }
        
        input.close();
        output.close();

        int[] posEndLicense = new int[] {608,597,580}; // posición del "*/"

        String content = str.toString();

        int type = getLicenseFormat(content,posEndLicense);
        if (type == -1)
            return;

        System.out.println("Licensed: TRUE " + type);

        byte[] fileData = output.toByteArray();
        int offset = posEndLicense[type] + 2;
        byte[] fileDataWithoutLicense = new byte[fileData.length - offset];
        System.arraycopy(fileData, offset, fileDataWithoutLicense, 0, fileDataWithoutLicense.length);

        byte[] newLicenseBytes = readNewLicense();

        writeToFile(file.getAbsolutePath(),newLicenseBytes,fileDataWithoutLicense);
    }

    public static void writeToFile(String filePath,byte[] newLicenseBytes,byte[] fileDataWithoutLicense) throws Exception
    {
        if (!SIMULATION)
        {
            OutputStream output = new FileOutputStream(new File(filePath));
            output.write(newLicenseBytes);
            output.write(fileDataWithoutLicense);
            output.close();
        }
        else
        {
            for(int i = 0; i < newLicenseBytes.length; i++)
            {
                System.out.print((char)newLicenseBytes[i]);
            }

            for(int i = 0; i < fileDataWithoutLicense.length; i++)
            {
                System.out.print((char)fileDataWithoutLicense[i]);
            }
        }

    }

    public static int getLicenseFormat(String content,int[] posEndLicense)
    {
        int pos = content.indexOf("ItsNat Java Web Application Framework");
        if (pos == -1) return -1;

        if (pos > 6) return -1;

        pos = content.indexOf("*/");

        int type;
        switch(pos)
        {
            case 608: type = 0; break;
            case 597: type = 1; break;
            case 580: type = 2; break;
            default: return -1;
        }

        pos = content.indexOf("GNU Affero General Public License");
        if (pos < 0) return -1;
        if (pos > posEndLicense[type]) return -1; // Por si acaso

        return type;
    }

    public static byte[] readNewLicense() throws Exception
    {
        InputStream input = LicenseChanger.class.getResourceAsStream("LICENSE.txt");
        return readFile(input);
    }

    public static byte[] readFile(InputStream input) throws Exception
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[10*1024];
        int res;
        while((res = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, res);
        }

        input.close();
        output.close();

        return output.toByteArray();
    }
}
