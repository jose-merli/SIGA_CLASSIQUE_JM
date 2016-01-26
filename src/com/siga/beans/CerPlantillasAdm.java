package com.siga.beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.EjecucionPLs;
import com.siga.general.FiltroFicheros;
import com.siga.general.SIGAException;

public class CerPlantillasAdm extends MasterBeanAdministrador
{
    private String sDirectorioRecursos = "";
    
	public CerPlantillasAdm(UsrBean usuario)
	{
	    super(CerPlantillasBean.T_NOMBRETABLA, usuario);
	    
	    sDirectorioRecursos="recursos" + File.separator;
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerPlantillasBean.C_IDINSTITUCION,
		        		   CerPlantillasBean.C_IDTIPOPRODUCTO,
		        		   CerPlantillasBean.C_IDPRODUCTO,
		        		   CerPlantillasBean.C_IDPRODUCTOINSTITUCION,
		        		   CerPlantillasBean.C_IDPLANTILLA,
		        		   CerPlantillasBean.C_DESCRIPCION,
		        		   CerPlantillasBean.C_PORDEFECTO,
		        		   CerPlantillasBean.C_FECHAMODIFICACION,
						   CerPlantillasBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerPlantillasBean.C_IDINSTITUCION, CerPlantillasBean.C_IDTIPOPRODUCTO, CerPlantillasBean.C_IDPRODUCTO, CerPlantillasBean.C_IDPRODUCTOINSTITUCION,CerPlantillasBean.C_IDPLANTILLA};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerPlantillasBean bean = null;

		try
		{
			bean = new CerPlantillasBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CerPlantillasBean.C_IDINSTITUCION));
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash, CerPlantillasBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto(UtilidadesHash.getInteger(hash, CerPlantillasBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion(UtilidadesHash.getInteger(hash, CerPlantillasBean.C_IDPRODUCTOINSTITUCION));
			bean.setIdPlantilla(UtilidadesHash.getInteger(hash, CerPlantillasBean.C_IDPLANTILLA));
			bean.setDescripcion(UtilidadesHash.getString(hash, CerPlantillasBean.C_DESCRIPCION));
			bean.setPorDefecto(UtilidadesHash.getString(hash, CerPlantillasBean.C_PORDEFECTO));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			CerPlantillasBean b = (CerPlantillasBean) bean;

			UtilidadesHash.set(htData, CerPlantillasBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CerPlantillasBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData, CerPlantillasBean.C_IDPRODUCTO, b.getIdProducto());
			UtilidadesHash.set(htData, CerPlantillasBean.C_IDPRODUCTOINSTITUCION, b.getIdProductoInstitucion());
			UtilidadesHash.set(htData, CerPlantillasBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, CerPlantillasBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CerPlantillasBean.C_PORDEFECTO, b.getPorDefecto());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return null;
    }

    public Vector obtenerListaPlantillas(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion)
    {
    	try
    	{
    		String sWhere = " WHERE " +
    						CerPlantillasBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
    						CerPlantillasBean.C_IDTIPOPRODUCTO + "=" + idTipoProducto + " AND " +
    						CerPlantillasBean.C_IDPRODUCTO + "=" + idProducto + " AND " +
    						CerPlantillasBean.C_IDPRODUCTOINSTITUCION + "=" + idProductoInstitucion;

    		return select(sWhere);
    		
    	}

    	catch(Exception e)
    	{
    		return null;
    	}
    }

    /**
     * Obtiene la plantilla por defecto para este certificado. SI no tiene definido por defecto devuelve la primera que encuentra. 
     * Si no tiene plantilla configurada devuelve nulo.
     * @param idInstitucion
     * @param idTipoProducto
     * @param idProducto
     * @param idProductoInstitucion
     * @return Id de la plantilla f�sica del certificado. Nulo si no existe.
     * @throws ClsExceptions en caso de error
     */
    public String obtenerPlantillaDefecto(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion) throws ClsExceptions
    {
    	String salida=null; 
    	try
    	{
    		//aalg: se a�ade el idproducto al where
    		String sWhere = " WHERE " +
    						CerPlantillasBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
    						CerPlantillasBean.C_IDTIPOPRODUCTO + "=" + idTipoProducto + " AND " +
    						CerPlantillasBean.C_IDPRODUCTO + "=" + idProducto + " AND " +
    						CerPlantillasBean.C_IDPRODUCTOINSTITUCION + "=" + idProductoInstitucion + " AND " +
    						CerPlantillasBean.C_PORDEFECTO + "='S'";

    		Vector v = select(sWhere);
    		if (v!=null && v.size()>0) {
    			CerPlantillasBean b = (CerPlantillasBean) v.get(0);
    			salida = b.getIdPlantilla().toString();
    		} else {
				v = obtenerListaPlantillas(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion); 
				if (v!=null && v.size()>0) {
					CerPlantillasBean b = (CerPlantillasBean) v.get(0);
					salida = b.getIdPlantilla().toString();
				}
    		}

    		return salida;
    	}
    	catch(Exception e)
    	{
    		throw new ClsExceptions(e, "Error al obtener la plantilla por defecto de un certificado.");
    	}
    }

   	public String getNuevoID(Hashtable entrada) throws ClsExceptions
	{
		String sID="0";

		try
		{
			RowsContainer rc = new RowsContainer();

			String sSQL="";

			sSQL = " SELECT MAX(" + CerPlantillasBean.C_IDPLANTILLA + ") AS " + CerPlantillasBean.C_IDPLANTILLA +
				   " FROM " + nombreTabla +
				   " WHERE " + CerPlantillasBean.C_IDINSTITUCION + "=" + entrada.get(CerPlantillasBean.C_IDINSTITUCION) + " AND " +
				   			   CerPlantillasBean.C_IDTIPOPRODUCTO + "=" + entrada.get(CerPlantillasBean.C_IDTIPOPRODUCTO) + " AND " +
				   			   CerPlantillasBean.C_IDPRODUCTO + "=" + entrada.get(CerPlantillasBean.C_IDPRODUCTO) + " AND " +
				   			   CerPlantillasBean.C_IDPRODUCTOINSTITUCION + "=" + entrada.get(CerPlantillasBean.C_IDPRODUCTOINSTITUCION);

			if (rc.findForUpdate(sSQL))
			{
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();

				String sIDAux = (String)ht.get(CerPlantillasBean.C_IDPLANTILLA);

				if (sIDAux!=null && !sIDAux.equals(""))
				{
					sID = "" + (Integer.parseInt(sIDAux)+1);
				}
			}

			return sID;
		}

		catch (ClsExceptions e)
		{
			throw new ClsExceptions (e, "Error al obtener el MAX(IDPLANTILLA).");
		}
	}

   	public boolean importarPlantilla(String descripcionPlantilla,
   	        						 String idInstitucion,
   	        						 String idTipoProducto,
   	        						 String idProducto,
   	        						 String idProductoInstitucion,
   	        						 String idPlantilla,
   	        						 File fPlantilla,
   	        						 boolean bPorDefecto,
   	        						 boolean bZIP,String extension) throws SIGAException, ClsExceptions
   	{
   	    try {
   	    	
	   	    boolean bGrabarFicheroPlantilla=fPlantilla!=null;
	   	    boolean bInsertDB=(idPlantilla==null || idPlantilla.equals("")) ? true : false;
	
		    Hashtable htDatos = new Hashtable();
	
		    htDatos.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucion);
		    htDatos.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
		    htDatos.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
		    htDatos.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
	
		    if (bPorDefecto)
		    {
		        Vector vDatos = select(htDatos);
	
		        if (vDatos!=null)
		        {
		            for (int i=0; i<vDatos.size(); i++)
		            {
		                CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(i);
	
		                beanPlantilla.setPorDefecto("N");
		                beanPlantilla.setUsuMod(null);
		                beanPlantilla.setFechaMod(null);
	
		                if (!update(beanPlantilla))
		                {
		                    return false;
		                }
		            }
		        }
		    }
	
		    String idPlantilla2 = (idPlantilla==null || idPlantilla.equals("")) ? getNuevoID(htDatos) : idPlantilla;
		    htDatos.put(CerPlantillasBean.C_IDPLANTILLA, idPlantilla2);
	
		    if (bInsertDB)
		    {
			    htDatos.put(CerPlantillasBean.C_DESCRIPCION, descripcionPlantilla);
	
			    String sPorDefecto = (bPorDefecto) ? "S" : "N" ;
			    htDatos.put(CerPlantillasBean.C_PORDEFECTO, sPorDefecto);
	
		        if (!insert(htDatos))
			    {
			        return false;
				}
		    }
	
		    else
		    {
		    	htDatos.remove("FECHAMODIFICACION");
		        Vector vDatos = select(htDatos);
	
		        CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(0);
	
		        beanPlantilla.setDescripcion(descripcionPlantilla);
		        beanPlantilla.setPorDefecto(bPorDefecto ? "S" : "N");
	
		        if (!update(beanPlantilla))
		        {
		            return false;
		        }
		    }
	
	        if (bGrabarFicheroPlantilla)
	        {
	            if (!grabarFicheroPlantilla(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla2, fPlantilla, bZIP,extension))
	            {
	                if (bInsertDB)
	                {
	                	htDatos.remove("FECHAMODIFICACION");
	                	Vector vDatos = select(htDatos);
	
	        	        CerPlantillasBean beanPlantilla = (CerPlantillasBean)vDatos.elementAt(0);
	
	        	        delete(beanPlantilla);
	                }
	
	                return false;
	            }
	
	            return true;
	        }
	
	        else
	        {
	            return true;
	        }
        
   	    } catch (SIGAException se) {
        	ClsLogging.writeFileLog("CerPlantillasAdm.importarPlantilla() Error SIGA "+se.toString(),10);
        	throw se;
   	    } catch (ClsExceptions e) {
        	ClsLogging.writeFileLog("CerPlantillasAdm.importarPlantilla() Error CLS "+e.toString(),10);
        	throw e;
   	    }
   	}

   	private boolean grabarFicheroPlantilla(String idInstitucion,
				 						   String idTipoProducto,
				 						   String idProducto,
				 						   String idProductoInstitucion,
				 						   String idPlantilla,
				 						   File fPlantilla,
				 						   boolean bZIP,String extension) throws SIGAException, ClsExceptions
   	{
   	    BufferedWriter bw = null;
   	    BufferedReader br = null; 
   	    try
   	    {
	        String sCompuesto = idTipoProducto + "_" + idProducto + "_" + idProductoInstitucion + "_" + idPlantilla;
	        String sNombreFinal = fPlantilla.getParent() + File.separator + sCompuesto;
	        String sDirectorioFinal = fPlantilla.getParent();

	        Vector vFiles = getFicherosPlantilla(sDirectorioFinal, sCompuesto, idPlantilla);

            File[] fFicheros1 = (File[])vFiles.elementAt(0);
            File[] fFicheros2 = (File[])vFiles.elementAt(1);

	        if (idPlantilla==null || idPlantilla.equals(""))
	        {
	            if ((fFicheros1!=null && fFicheros1.length>0) || (fFicheros2!=null && fFicheros2.length>0))
	            {
	                fPlantilla.delete();

	                throw new SIGAException("messages.certificados.error.archivoexiste");
	            }
	        }

	        else
	        {
	            if (fFicheros1!=null)
	            {
	                for (int i=0; i<fFicheros1.length; i++)
	                {
	                    File fAux = fFicheros1[i];
	                    fAux.delete();
	                }
	            }

	            if (fFicheros2!=null)
	            {
	                for (int i=0; i<fFicheros2.length; i++)
	                {
	                    File fAux = fFicheros2[i];
	                    fAux.delete();
	                }
	            }
	        }

	        if (bZIP)
	        {
	            try
	            {
	                ZipFile zf = new ZipFile(fPlantilla.getAbsolutePath());

	                Enumeration enumer = zf.entries();
	                int cont=0;
	                String sNombreRecursos=sCompuesto;

	                Hashtable htNombresRecursos = new Hashtable();

	                File fDirectorio = new File(sDirectorioFinal + File.separator + sDirectorioRecursos);
                    fDirectorio.mkdirs();

	                while (enumer.hasMoreElements())
	                {
	                    ZipEntry ze=(ZipEntry)enumer.nextElement();
	                    String sNombreAux=ze.getName();

	                    if (!ze.isDirectory())
	                    {
	                        InputStream is = zf.getInputStream(ze);
	                        FileOutputStream fos = null;

	                        //if (cont==0)
	                        if (sNombreAux.endsWith(".fo"))
	                        {
	                            fos = new FileOutputStream(sNombreFinal + ".tmp");
	                        }

	                        else
	                        {
	                            htNombresRecursos.put(sNombreAux.substring(sDirectorioRecursos.length()), sNombreRecursos + "_" + cont);
	                            
	                            fos = new FileOutputStream(sDirectorioFinal + File.separator + sDirectorioRecursos + sNombreRecursos + "_" + cont);
	                        }

	                        byte[] buf = new byte[10000];
	                        int length=0;

	                        while ((length=is.read(buf))>-1)
	                        {
	                            fos.write(buf, 0, length);
	                        }

	                        is.close();
	                        fos.close();

	                        cont++;
	                    }
	                }

	                zf.close();

	                File fTemp = new File(sNombreFinal + ".tmp");

	                br = new BufferedReader(new FileReader(fTemp));
	                String sLeido="";
	                int iLeido=0;

	                while ((iLeido=br.read())!=-1)
	                {
	                    sLeido += (char)iLeido;
	                }

	                br.close();

	                Enumeration enumNombresRecursos = htNombresRecursos.keys();

	                while (enumNombresRecursos.hasMoreElements())
	                {
	                    String sTemp1 = (String)enumNombresRecursos.nextElement();
	                    String sTemp2 = (String)htNombresRecursos.get(sTemp1);

	                    // RGG cambio para que el replace cambie solo el nombre de la imagen pero no su camino
	                    if (sTemp1.lastIndexOf("/")!=-1) {
	                    	String auxiliar1 = sTemp1.substring(0,sTemp1.lastIndexOf("/")+1);
	                    	sTemp2 = auxiliar1 + sTemp2;
	                    }

	                    sLeido = sLeido.replaceAll(sTemp1, sTemp2);
	                }

	                bw = new BufferedWriter(new FileWriter(sNombreFinal));

	                bw.write(sLeido);
	                bw.close();

	                fPlantilla.deleteOnExit();
	                fPlantilla.delete();

	                //fTemp.renameTo(new File(sNombreFinal));
	                fTemp.deleteOnExit();
	                fTemp.delete();
	            }

	            catch(Exception e)
	            {
	            	return false;
	            } finally {
	                try {
	                    bw.close();
	                    br.close(); 
	                } catch (Exception eee) {}
	            }
	        }

	        else
	        {
	            if(extension.equals("doc"))
	            	sNombreFinal = sNombreFinal +"."+ extension;
	        	File fFicheroDestino = new File(sNombreFinal);
	            fPlantilla.renameTo(fFicheroDestino);
	        }

	        return true;
   	    }

   	    catch(Exception e)
   	    {
   	        return false;
   	    }
	}

   	public File descargarPlantilla(String idInstitucion,
				 				   String idTipoProducto,
				 				   String idProducto,
				 				   String idProductoInstitucion,
				 				   String idPlantilla) throws SIGAException, ClsExceptions
	{
   	    ZipOutputStream zos = null;
   	    File fPlantilla=null;
   	    FileInputStream fis=null;
   	    
   	    try
   	    {
	        String sCompuesto = idTipoProducto + "_" + idProducto + "_" + idProductoInstitucion + "_" + idPlantilla;
	        String sNombreFinal = getPathPlantillasFromDB() + File.separator + idInstitucion + File.separator + sCompuesto;
	        fPlantilla = new File(sNombreFinal);

	        String sDirectorioFinal = fPlantilla.getParent();

	        Vector vFiles = getFicherosPlantilla(sDirectorioFinal, sCompuesto, idPlantilla);

            File[] fFicheros1 = (File[])vFiles.elementAt(0);
            File[] fFicheros2 = (File[])vFiles.elementAt(1);

            if (fFicheros2==null || fFicheros2.length==0)
            {
                if (fFicheros1==null || fFicheros1.length==0)
                {
                    throw new SIGAException("messages.general.error.ficheroNoExiste");
                }

                else
                {
                    return new File(sNombreFinal);
                }

            }

            fPlantilla = new File(sNombreFinal + ".zip");

            fis=new FileInputStream(new File(sNombreFinal));
            zos = new ZipOutputStream(new FileOutputStream(fPlantilla));

            ZipEntry ze = new ZipEntry(sCompuesto+".fo");

            zos.putNextEntry(ze);

            byte[] buffer = new byte[8192];
            int got;

            while ((got = fis.read(buffer, 0, buffer.length)) > 0)
            {
                zos.write(buffer, 0, got);
            }

            fis.close();
            zos.closeEntry();
           
            for (int i=0; i<fFicheros2.length; i++)
            {
                ze = new ZipEntry(sDirectorioRecursos + fFicheros2[i].getName());
                zos.putNextEntry(ze);
                fis=new FileInputStream(fFicheros2[i]);

                buffer = new byte[8192];

                while ((got = fis.read(buffer, 0, buffer.length)) > 0)
                {
                    zos.write(buffer, 0, got);
                }

                fis.close();

                zos.closeEntry();
            }

            zos.close();

   	        return fPlantilla;
   	    }

   	    catch(Exception e)
   	    {
   	        try {
   	            fis.close();
   	        } catch (Exception eee) {}
   	        
   	        if (zos!=null)
   	        {
   	            try
   	            {
   	                zos.close();
   	            }

   	            catch(Exception ex)
   	            {
   	                e.printStackTrace();
   	            }
   	        }

   	        throw new SIGAException("messages.general.error.ficheroNoExiste");
   	    }
	}

   	public boolean borrarPlantilla(String idInstitucion,
		  	 					   String idTipoProducto,
		  	 					   String idProducto,
		  	 					   String idProductoInstitucion,
		  	 					   String idPlantilla) throws SIGAException, ClsExceptions
	{
   	    String sCompuesto = idTipoProducto + "_" + idProducto + "_" + idProductoInstitucion + "_" + idPlantilla;
   	    String sNombreFinal = getPathPlantillasFromDB() + File.separator + idInstitucion + File.separator + sCompuesto;
        File fPlantilla = new File(sNombreFinal);

        String sDirectorioFinal = fPlantilla.getParent();

        Vector vFiles = getFicherosPlantilla(sDirectorioFinal, sCompuesto, idPlantilla);

        File[] fFicheros1 = (File[])vFiles.elementAt(0);
        File[] fFicheros2 = (File[])vFiles.elementAt(1);

        if (fFicheros1!=null)
        {
            for (int i=0; i<fFicheros1.length; i++)
            {
                fFicheros1[i].delete();
            }
        }

        if (fFicheros2!=null)
        {
            for (int i=0; i<fFicheros2.length; i++)
            {
                fFicheros2[i].delete();
            }
        }

   	    Hashtable hash = new Hashtable();

	    hash.put(CerPlantillasBean.C_IDINSTITUCION, idInstitucion);
	    hash.put(CerPlantillasBean.C_IDTIPOPRODUCTO, idTipoProducto);
	    hash.put(CerPlantillasBean.C_IDPRODUCTO, idProducto);
	    hash.put(CerPlantillasBean.C_IDPRODUCTOINSTITUCION, idProductoInstitucion);
	    hash.put(CerPlantillasBean.C_IDPLANTILLA, idPlantilla);

	    return delete(hash);
	}

   	private Vector getFicherosPlantilla(String sDirectorio, String sPrefijo, String idPlantilla)
   	{
   	    Vector vFiles = new Vector();

        FiltroFicheros filtro = new FiltroFicheros(sPrefijo + (idPlantilla==null || idPlantilla.equals("") ? "_" : ""));
        File[] fFicheros1 = (new File(sDirectorio)).listFiles(filtro);
        //File[] fFicheros2 = (new File(sDirectorio + File.separator + "recursos")).listFiles(filtro);
        File[] fFicheros2 = (new File(sDirectorio + File.separator + sDirectorioRecursos)).listFiles(filtro);

        vFiles.add(fFicheros1);
        vFiles.add(fFicheros2);

   	    return vFiles;
   	}

   	public File obtenerPlantilla(String idInstitucion,
			   					 String idTipoProducto,
			   					 String idProducto,
			   					 String idProductoInstitucion,
			   					 String idPlantilla) throws SIGAException, ClsExceptions
   	{
   	    try
   	    {
   	    	String sCompuesto = idTipoProducto + "_" + idProducto + "_" + idProductoInstitucion + "_" + idPlantilla;
   	   	    String sNombreFinal = getPathPlantillasFromDB() + File.separator + idInstitucion + File.separator + sCompuesto;
   	    	StringBuffer rutaCompletaFicheroDoc = new StringBuffer(sNombreFinal);
			rutaCompletaFicheroDoc.append(".doc");
			File fileDoc = new File(rutaCompletaFicheroDoc.toString());
			if (fileDoc.exists()) 
				return fileDoc;
			else
				return new File(sNombreFinal);
   	    }

   	    catch(Exception e)
   	    {
   	        return null;
   	    }
   	}

	private String getPathPlantillasFromDB() throws SIGAException
	{
	    String sPath="";

	    try
	    {
	        String sWhere = " WHERE " + GenParametrosBean.C_PARAMETRO + "='PATH_PLANTILLAS' AND "+
	                                    GenParametrosBean.C_MODULO + "='CER' AND " +
	                                    GenParametrosBean.C_IDINSTITUCION + "=0";

	        GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);
	        Vector vParametros = admParametros.select(sWhere);

	        if (vParametros==null || vParametros.size()==0)
	        {
	            throw new SIGAException("messages.certificados.error.importarplantilla");
	        }

	        GenParametrosBean beanParametros = (GenParametrosBean)vParametros.elementAt(0);
	        sPath = beanParametros.getValor();
	    }

	    catch(Exception e)
	    {
	        throw new SIGAException("messages.certificados.error.importarplantilla");
	    }

	    return sPath;
	}

	/*public Hashtable darFormatoCampos(String idIntitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPlantilla, Hashtable ht) throws ClsExceptions
	{
	    Hashtable htDatos = new Hashtable();

	    try
	    {
	        CerProducInstiCampCertifAdm admAdm = new CerProducInstiCampCertifAdm(this.usrbean);

	        Vector vCampos = admAdm.obtenerCampos(idIntitucion, idTipoProducto, idProducto, idProductoInstitucion);

	        String sNombreCampo="";
	        String sValorCampo="";

	        for (int i=0; i<vCampos.size(); i++)
	        {
	            Hashtable htAux = (Hashtable)vCampos.elementAt(i);

			    String sIdNombre = (String)htAux.get(CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO);
			    String sTipoCampo= (String)htAux.get(CerCamposCertificadosBean.C_TIPOCAMPO);
			    String sCapturarDatos = (String)htAux.get(CerCamposCertificadosBean.C_CAPTURARDATOS);
			    String sNombre = (String)htAux.get(CerCamposCertificadosBean.C_NOMBRE);
			    String sValor = (String)htAux.get(CerProducInstiCampCertifBean.C_VALOR);
			    String sIdFormato = (String)htAux.get(CerFormatosBean.C_IDFORMATO);
			    String sFormato = (String)htAux.get(CerFormatosBean.C_FORMATO);
			    String sDescripcion = (String)htAux.get(CerFormatosBean.C_DESCRIPCION);

			    sNombreCampo=sNombre;
			    sValorCampo = (sCapturarDatos!=null && sCapturarDatos.equalsIgnoreCase("S")) ? sValor : (String)ht.get(sNombre);

			    if (sValorCampo==null || sValorCampo.equals(""))
			    {
			        sValorCampo="";
			    }

			    else
			    {
		            if (sTipoCampo.equals(CerCamposCertificadosAdm.T_ALFANUMERICO))
		            {
		                if (sIdFormato.equals(CerFormatosAdm.K_TODO_MAYUSCULAS))
		                {
		                    sValorCampo=sValorCampo.toUpperCase();
		                }

		                else if (sIdFormato.equals(CerFormatosAdm.K_TODO_MINUSCULAS))
		                {
		                    sValorCampo=sValorCampo.toLowerCase();
		                }

		                else if (sIdFormato.equals(CerFormatosAdm.K_PRIMERA_MAYUSCULA))
		                {
		                	
		                     sValorCampo = sValorCampo.substring(0,1).toUpperCase() + sValorCampo.substring(1).toLowerCase();
		                	
		                }

		                else if (sIdFormato.equals(CerFormatosAdm.K_PRIMERAS_MAYUSCULAS))
		                {
		                    StringTokenizer st = new StringTokenizer(sValorCampo, " ");
		                    sValorCampo="";

		                    while (st.hasMoreTokens())
		                    {
		                        String token = st.nextToken();

		                        sValorCampo += token.substring(0,1).toUpperCase() + token.substring(1).toLowerCase();
		                        sValorCampo += " ";
		                    }
		                }

		                /*else
		                {
		                    throw new ClsExceptions(null, "Error al dar formato a los campos");
		                }*/
		          // / }/

		            /*else if (sTipoCampo.equals(CerCamposCertificadosAdm.T_NUMERICO))
		            {
		                try
		                {
		                    //De momento no se hace nada.
		                }

		                catch(Exception e)
		                {
		                    throw new ClsExceptions(e, "Error al dar formato num�rico");
		                }
		            }*/

		          /*  else if (sTipoCampo.equals(CerCamposCertificadosAdm.T_FECHA))
		            {
		                /*try
		                {
		                    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

		                    Date out = sdf.parse(sValorCampo);

		                    sValorCampo=sdf.format(out);
		                }

		                catch(Exception e)
		                {
		                    throw new ClsExceptions(e, "Error al dar formato fecha");
		                }*/
		                //Si es del tipo dia de mes de anio utilizaremos el procedimiento
		            	// PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra
		         /*       try
		                {
		                    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);

		                    Date out = sdf.parse(sValorCampo);
		                    
		                    Locale locale;
	                        SimpleDateFormat sdfout;
	                        
		                    if (sFormato==null||sFormato.equals("")){
		                        locale = new Locale("ES");
		                        sdfout = new SimpleDateFormat("dd/MM/yyyy",locale);
		                    } else if(sFormato!=null && sFormato.equalsIgnoreCase("dia de mes de anio")){
		                    	sdfout = null;
		                    }else {
		                    
		                        //Obtenemos el idioma
		                        if (sFormato.indexOf("%%")==-1){
			                        locale = new Locale("ES","es");
			                        sdfout = new SimpleDateFormat(sFormato,locale);
		                        } else {
		                            String language = sFormato.substring(sFormato.length()-2, sFormato.length());
		                            sFormato = sFormato.substring(0,sFormato.length()-4);
		                            locale = new Locale("ES",language);			                        
		                        }
		                        sdfout = new SimpleDateFormat(sFormato,locale);
		                    }
		                    if(sdfout == null){
		                    	
		                    	String rc[] = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA
								(sValorCampo.substring(0,11),"dma","1");
		                    }
		                    else
		                    sValorCampo=sdfout.format(out);
		                }

		                catch(Exception e)
		                {
		                    throw new ClsExceptions(e, "Error al dar formato fecha");
		                }
		            }

		            else
		            {
		                throw new ClsExceptions(null, "Error al dar formato a los campos");
		            }
			    }

			    htDatos.put(sNombreCampo, sValorCampo);
	        }
	    }

	    catch(Exception e)
	    {
	        throw new ClsExceptions(e, "Error al dar formato a los campos");
	    }

	    return htDatos;
	}*/
	public Hashtable darFormatoCampos(String idIntitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPlantilla, String idioma, Hashtable ht) throws ClsExceptions {
		Hashtable htDatos = new Hashtable();

		try {
			CerProducInstiCampCertifAdm admAdm = new CerProducInstiCampCertifAdm(this.usrbean);

			Vector vCampos = admAdm.obtenerCampos(idIntitucion, idTipoProducto, idProducto, idProductoInstitucion);

			String sNombreCampo = "";
			String sValorCampo = "";

			for (int i = 0; i < vCampos.size(); i++) {
				Hashtable htAux = (Hashtable) vCampos.elementAt(i);

				String sIdNombre = (String) htAux.get(CerCamposCertificadosBean.C_IDCAMPOCERTIFICADO);
				String sTipoCampo = (String) htAux.get(CerCamposCertificadosBean.C_TIPOCAMPO);
				String sCapturarDatos = (String) htAux.get(CerCamposCertificadosBean.C_CAPTURARDATOS);
				String sNombre = (String) htAux.get(CerCamposCertificadosBean.C_NOMBRE);
				String sValor = (String) htAux.get(CerProducInstiCampCertifBean.C_VALOR);
				String sIdFormato = (String) htAux.get(CerFormatosBean.C_IDFORMATO);
				String sFormato = (String) htAux.get(CerFormatosBean.C_FORMATO);
				String sDescripcion = (String) htAux.get(CerFormatosBean.C_DESCRIPCION);

				sNombreCampo = sNombre;
				sValorCampo = (sCapturarDatos != null && sCapturarDatos.equalsIgnoreCase("S")) ? sValor : (String) ht.get(sNombre);

				if (sValorCampo == null || sValorCampo.equals("")) {
					sValorCampo = "";
				}

				else {
					if (sTipoCampo.equals(CerCamposCertificadosAdm.T_ALFANUMERICO)) {
						if (sIdFormato.equals(CerFormatosAdm.K_TODO_MAYUSCULAS)) {
							sValorCampo = sValorCampo.toUpperCase();
						}

						else if (sIdFormato.equals(CerFormatosAdm.K_TODO_MINUSCULAS)) {
							sValorCampo = sValorCampo.toLowerCase();
						}

						else if (sIdFormato.equals(CerFormatosAdm.K_PRIMERA_MAYUSCULA)) {

							sValorCampo = sValorCampo.substring(0, 1).toUpperCase() + sValorCampo.substring(1).toLowerCase();

						}

						else if (sIdFormato.equals(CerFormatosAdm.K_PRIMERAS_MAYUSCULAS)) {
							StringTokenizer st = new StringTokenizer(sValorCampo, " ");
							sValorCampo = "";

							while (st.hasMoreTokens()) {
								String token = st.nextToken();

								sValorCampo += token.substring(0, 1).toUpperCase() + token.substring(1).toLowerCase();
								sValorCampo += " ";
							}
						}

						/*
						 * else { throw new ClsExceptions(null,
						 * "Error al dar formato a los campos"); }
						 */
					}

					/*
					 * else if
					 * (sTipoCampo.equals(CerCamposCertificadosAdm.T_NUMERICO))
					 * { try { //De momento no se hace nada. }
					 * 
					 * catch(Exception e) { throw new ClsExceptions(e,
					 * "Error al dar formato num�rico"); } }
					 */

					else if (sTipoCampo.equals(CerCamposCertificadosAdm.T_FECHA)) {
						/*
						 * try { SimpleDateFormat sdf=new
						 * SimpleDateFormat("dd/MM/yyyy");
						 * 
						 * Date out = sdf.parse(sValorCampo);
						 * 
						 * sValorCampo=sdf.format(out); }
						 * 
						 * catch(Exception e) { throw new ClsExceptions(e,
						 * "Error al dar formato fecha"); }
						 */
						// Si es del tipo dia de mes de anio utilizaremos el
						// procedimiento
						// PKG_SIGA_FECHA_EN_LETRA.f_siga_fechacompletaenletra
						try {
							SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);

							Date out = sdf.parse(sValorCampo);

							Locale locale;
							SimpleDateFormat sdfout;

							if (sFormato == null || sFormato.equals("") || ((sFormato != null && sFormato.equalsIgnoreCase("dia de mes de anio")) || (sFormato != null && sFormato.equalsIgnoreCase("dd 'de ' MMMMM ' de ' yyyy")))) {
								locale = new Locale("ES");
								sdfout = new SimpleDateFormat("dd/MM/yyyy", locale);
							} else {

								// Obtenemos el idioma
								if (sFormato.indexOf("%%") == -1) {
									locale = new Locale("ES", "es");
									sdfout = new SimpleDateFormat(sFormato, locale);
								} else {
									String language = sFormato.substring(sFormato.length() - 2, sFormato.length());
									sFormato = sFormato.substring(0, sFormato.length() - 4);
									locale = new Locale("ES", language);
								}
								sdfout = new SimpleDateFormat(sFormato, locale);
							}
							if (sFormato != null && sFormato.equalsIgnoreCase("dia de mes de anio")) {

								String fecha = sdfout.format(out);
								sValorCampo = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(fecha, "dma", idioma);
								// sValorCampo = rc[0];
							} else if (sFormato != null && sFormato.equalsIgnoreCase("dd 'de ' MMMMM ' de ' yyyy")) {
								String fecha = sdfout.format(out);
								sValorCampo = EjecucionPLs.ejecutarPLPKG_SIGA_FECHA_EN_LETRA(fecha, "m", idioma);
							} else
								sValorCampo = sdfout.format(out);
						}

						catch (Exception e) {
							throw new ClsExceptions(e, "Error al dar formato fecha");
						}
					}

					else {
						throw new ClsExceptions(null, "Error al dar formato a los campos");
					}
				}

				htDatos.put(sNombreCampo, sValorCampo);
			}
		}

		catch (Exception e) {
			throw new ClsExceptions(e, "Error al dar formato a los campos");
		}

		return htDatos;
	}
	/**
	 * Obtienes las subplantillas de una plantilla
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @param idPlantilla
	 * @return
	 * @throws ClsExceptions 
	 */
	   public Vector obtenerListaRelacionesPlantillas(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion,String idPlantilla) throws ClsExceptions
	    {
	    	try
	    	{
	    		String sql = "  select plant.idInstitucion, plant.idTipoProducto, plant.Idproducto, "+
							 "	plant.Idproductoinstitucion, plant.Idplantilla, plant.descripcion, "+
							 "	plant.fechamodificacion, plant.usumodificacion, plant.pordefecto "+
							 "	from cer_plantillas plant, cer_relacion_plantillas rel_plant "+
							 "	where "+
							 "	rel_plant.IDPLANTILLAPADRE = "+idPlantilla+" and "+ 
							 "	rel_plant.IDINSTITUCION = " +idInstitucion+" and "+
							 "	rel_plant.IDTIPOPRODUCTO = "+idTipoProducto+" and "+
							 "	rel_plant.IDPRODUCTO = "+idProducto+" and "+
							 "	rel_plant.IDPRODUCTOINSTITUCION= "+idProductoInstitucion+" and "+
							 "	plant.idPlantilla = rel_plant.Idplantilla and "+
							 "	plant.IDINSTITUCION  = rel_plant.IDINSTITUCION  and "+
							 "	plant.IDTIPOPRODUCTO  = rel_plant.IDTIPOPRODUCTO and "+
							 "	plant.IDPRODUCTO  = rel_plant.IDPRODUCTO and "+
							 "	plant.IDPRODUCTOINSTITUCION  = rel_plant.IDPRODUCTOINSTITUCION ";

	    		return selectSQL(sql);
	    		
	    	}

	    	catch(Exception e)
	    	{
	    		throw new ClsExceptions(e, "Error al obtener la lista de relaciones entre plantillas");
	    	}
	    }
	    /**
	     * Comprueba si la plantilla es hijo
	     * @param idInstitucion
	     * @param idTipoProducto
	     * @param idProducto
	     * @param idProductoInstitucion
	     * @param idPlantilla
	     * @return
	     * @throws ClsExceptions 
	     */
	    public Vector comprobarSiEsHijo(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion,String idPlantilla) throws ClsExceptions
	    {
	    	try
	    	{
	    		String sql =   "select plant.idInstitucion, plant.idTipoProducto, plant.Idproducto, "+
							 "	plant.Idproductoinstitucion, plant.Idplantilla, plant.descripcion, "+
							 "	plant.fechamodificacion, plant.usumodificacion, plant.pordefecto "+
	    						" from cer_plantillas plant, cer_relacion_plantillas rel_plant "+
	    						" where rel_plant.IDPLANTILLA = "+idPlantilla+
	    						" and rel_plant.IDINSTITUCION = "+idInstitucion+
	    						" and rel_plant.IDTIPOPRODUCTO = "+idTipoProducto+
	    						" and rel_plant.IDPRODUCTO ="+idProducto+
	    						" and rel_plant.IDPRODUCTOINSTITUCION ="+idProductoInstitucion+
	    						" and plant.idPlantilla = rel_plant.Idplantilla "+
	    						" and plant.IDINSTITUCION = rel_plant.IDINSTITUCION "+
	    						" and plant.IDTIPOPRODUCTO = rel_plant.IDTIPOPRODUCTO "+
	    						" and plant.IDPRODUCTO = rel_plant.IDPRODUCTO "+
	    						" and plant.IDPRODUCTOINSTITUCION = "+
	    						" rel_plant.IDPRODUCTOINSTITUCION ";

	    		return selectSQL(sql);
	    		
	    	}

	    	catch(Exception e)
	    	{
	    		throw new ClsExceptions(e, "Error al comprobar si es hijo");
	    	}
	    }
	    /**
	     * Obtiene las plantillas que se pueden asociar a una plantilla y que no est�n ya asociadas
	     * @param idInstitucion
	     * @param idTipoProducto
	     * @param idProducto
	     * @param idProductoInstitucion
	     * @param idPlantilla
	     * @return
	     * @throws ClsExceptions 
	     */
	    public Vector obtenerComboRelacionesPlantillas(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion,String idPlantilla) throws ClsExceptions
	    {
	    	try
	    	{	  
	    		String sql = " select * from cer_plantillas "+
		                      " where idPlantilla!= "+idPlantilla+
		                      " and idPlantilla not in(select idPlantilla from cer_relacion_plantillas where idPlantillaPadre="+idPlantilla+
																			                      " and 	IDINSTITUCION = "+idInstitucion+" and "+ 
																			         			  "	IDTIPOPRODUCTO = "+idTipoProducto+" and "+
																			         			  " IDPRODUCTO = "+idProducto+" and "+
																			         			  " IDPRODUCTOINSTITUCION= "+idProductoInstitucion+")"+
		                      "  and 	IDINSTITUCION = "+idInstitucion+" and "+ 
		         			  "	IDTIPOPRODUCTO = "+idTipoProducto+" and "+
		         			  " IDPRODUCTO = "+idProducto+" and "+
		         			  " IDPRODUCTOINSTITUCION= "+idProductoInstitucion+
		         			  "  and idPlantilla not in  (select idPlantillaPadre "+
		         			  " from cer_relacion_plantillas "+
		         			  " where IDINSTITUCION = "+idInstitucion+
		         			  " and IDTIPOPRODUCTO = "+idTipoProducto+
		         			  " and IDPRODUCTO = "+idProducto+
		         			  " and IDPRODUCTOINSTITUCION = "+idProductoInstitucion+")";
	               
	               return selectSQL(sql);
	    		
	    	}

	    	catch(Exception e)
	    	{
	    		throw new ClsExceptions(e, "Error al obtener obtener la lista de plantillas que a�n se pueden asociar");
	    	}
	    }
	    
	    /**
	     * Inserta la relaci�n entre la plantilla y la subplantilla
	     * @param idPlantillaRelacion
	     * @param idPlantilla
	     * @param idInstitucion
	     * @param idTipoProducto
	     * @param idProducto
	     * @param idProductoInstitucion
	     * @return
	     * @throws ClsExceptions 
	     */
	    public boolean insertarRelacionPlantillas(String idPlantillaRelacion, String idPlantilla, String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion) throws ClsExceptions
	    {
	    	try
	    	{
	    		 
	    		String sql = "insert into CER_RELACION_PLANTILLAS "+
	    					  "(IDPLANTILLA, IDPLANTILLAPADRE, IDINSTITUCION, IDTIPOPRODUCTO, IDPRODUCTO, IDPRODUCTOINSTITUCION, FECHA_ALTA,USU_ALTA) "+
	    					  "values ("+idPlantillaRelacion+","+idPlantilla+","+idInstitucion+","+idTipoProducto+","+idProducto+","+idProductoInstitucion+",SYSDATE,0)";

	    		return insertSQL(sql);
	    		
	    	}

	    	catch(Exception e)
	    	{
	    		throw new ClsExceptions(e, "Error al insertar la relaci�n entre plantillas");
	    	}
	    }
	    
	 	/**
	 	 * Borrar la relaci�n con la plantilla
	 	 * @param idInstitucion
	 	 * @param idTipoProducto
	 	 * @param idProducto
	 	 * @param idProductoInstitucion
	 	 * @param idPlantilla
	 	 * @param idPlantillaPadre
	 	 * @return
	 	 * @throws SIGAException
	 	 * @throws ClsExceptions
	 	 */
	   	public boolean borrarRelacionPlantilla(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPlantilla, String idPlantillaPadre) 
		{
			
			try
	    	{
				String sql = null;
				sql = "DELETE FROM CER_RELACION_PLANTILLAS WHERE IDPLANTILLA="+idPlantilla+ 
						" AND IDPLANTILLAPADRE="+idPlantillaPadre+" AND IDINSTITUCION ="+idInstitucion+" AND IDTIPOPRODUCTO="+idTipoProducto+" AND IDPRODUCTO="+idProducto+" AND IDPRODUCTOINSTITUCION="+idProductoInstitucion;
				
				return deleteSQL(sql);
				
	    	}catch(Exception e)
	    	{
	    		return false;
	    	}	
		}
	   	/**
	   	 * Borrar cualquier relaci�n que tenga la plantilla, tanto si es hija como si no.
	   	 * @param idInstitucion
	   	 * @param idTipoProducto
	   	 * @param idProducto
	   	 * @param idProductoInstitucion
	   	 * @param idPlantilla
	   	 * @return
	   	 * @throws SIGAException
	   	 * @throws ClsExceptions
	   	 */
	   	public boolean borrarHijosOPadres(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPlantilla) throws SIGAException, ClsExceptions
		{
	   		try
	    	{
				String sql = null;
				
				sql = "DELETE FROM CER_RELACION_PLANTILLAS WHERE (IDPLANTILLA="+idPlantilla+" OR IDPLANTILLAPADRE="+idPlantilla+") AND IDINSTITUCION ="+idInstitucion+" AND IDTIPOPRODUCTO="+idTipoProducto+" AND IDPRODUCTO="+idProducto+" AND IDPRODUCTOINSTITUCION="+idProductoInstitucion;
				
				return deleteSQL(sql);
				
	    	}catch(Exception e)
	    	{
	    		return false;
	    	}	
		}

	    
}