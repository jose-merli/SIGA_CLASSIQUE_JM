package com.siga.general;

import java.io.*;

public class FiltroFicheros implements FilenameFilter
{
    String prefijo;
    
    public FiltroFicheros(String prefijo)
    {
        this.prefijo=prefijo;
    }
    
    public boolean accept(File dir, String name)
    {
        return name.startsWith(prefijo);
    }
}
