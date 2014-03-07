package com.siga.informes;

import java.util.Vector;
import java.util.Hashtable;

import com.aspose.words.IMailMergeDataSource;


/**
 * Descripcion: Clase que recoge un vector de Hashtable y el nombre de la región donde cargar los datos
 * @author Leandro
 */

public class DataMailMergeDataSource implements IMailMergeDataSource{
	
	Vector lista;
	String region;
    int mRecordIndex;

    /**
     * Descripcion: Constructor de la clase
     * @param nombreRegion 
     * @param lista: Vector de Hashtable con la información
     */
    public DataMailMergeDataSource(String nombreRegion, Vector lista)
    {
        this.lista = lista;
        // When the data source is initialized, it must be positioned before the first record.
        mRecordIndex= -1;
        region = nombreRegion;
    }

   
    public String getTableName()
    {
        return region;
    }

    public boolean getValue(String fieldName,  Object[] fieldValue)
    {
    	Hashtable registro = (Hashtable)lista.get(mRecordIndex);
    	if (registro.containsKey(fieldName)) {
            fieldValue[0] = (String)registro.get(fieldName);
            return true;
        }
        else
        {
            // A field with this name was not found,
            // return false to the Aspose.Words mail merge engine.
            fieldValue[0] = null;
            return false;
        }
    }
   
    public boolean moveNext()
    {
        if (isEof())
            return false;

        mRecordIndex++;

        return (!isEof());
    }

    private boolean isEof()
    {
        return (mRecordIndex >= lista.size());
    }


	/* (non-Javadoc)
	 * @see com.aspose.words.IMailMergeDataSource#getChildDataSource(java.lang.String)
	 */
	@Override
	public IMailMergeDataSource getChildDataSource(String arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}