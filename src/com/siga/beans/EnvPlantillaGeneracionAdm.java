package com.siga.beans;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.atos.utils.*;
import com.siga.general.*;
import com.siga.Utilidades.*;

public class EnvPlantillaGeneracionAdm extends MasterBeanAdministrador
{
    private String sDirectorioRecursos = "";
    
	public EnvPlantillaGeneracionAdm(UsrBean usuario)
	{
	    super(EnvPlantillaGeneracionBean.T_NOMBRETABLA, usuario);
	    
	    sDirectorioRecursos="recursos" + File.separator;
	}

	protected String[] getCamposBean()
	{
		String[] campos = {EnvPlantillaGeneracionBean.C_IDINSTITUCION,
		        		   EnvPlantillaGeneracionBean.C_IDTIPOENVIOS,
		        		   EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS,
		        		   EnvPlantillaGeneracionBean.C_IDPLANTILLA,
		        		   EnvPlantillaGeneracionBean.C_DESCRIPCION,
		        		   EnvPlantillaGeneracionBean.C_PORDEFECTO,
		        		   EnvPlantillaGeneracionBean.C_TIPOARCHIVO,
		        		   EnvPlantillaGeneracionBean.C_FECHAMODIFICACION, 
						   EnvPlantillaGeneracionBean.C_USUMODIFICACION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {EnvPlantillaGeneracionBean.C_IDINSTITUCION, EnvPlantillaGeneracionBean.C_IDTIPOENVIOS, EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS, EnvPlantillaGeneracionBean.C_IDPLANTILLA};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    EnvPlantillaGeneracionBean bean = null;

		try
		{
			bean = new EnvPlantillaGeneracionBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, EnvPlantillaGeneracionBean.C_IDINSTITUCION));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, EnvPlantillaGeneracionBean.C_IDTIPOENVIOS));
			bean.setIdPlantillaEnvios(UtilidadesHash.getInteger(hash, EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS));
			bean.setIdPlantilla(UtilidadesHash.getInteger(hash, EnvPlantillaGeneracionBean.C_IDPLANTILLA));
			bean.setDescripcion(UtilidadesHash.getString(hash, EnvPlantillaGeneracionBean.C_DESCRIPCION));
			bean.setPorDefecto(UtilidadesHash.getString(hash, EnvPlantillaGeneracionBean.C_PORDEFECTO));
			bean.setTipoArchivo(UtilidadesHash.getString(hash, EnvPlantillaGeneracionBean.C_TIPOARCHIVO));
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

			EnvPlantillaGeneracionBean b = (EnvPlantillaGeneracionBean) bean;

			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS, b.getIdPlantillaEnvios());
			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_IDPLANTILLA, b.getIdPlantilla());
			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_PORDEFECTO, b.getPorDefecto());
			UtilidadesHash.set(htData, EnvPlantillaGeneracionBean.C_TIPOARCHIVO, b.getTipoArchivo());
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
        return new String[]{EnvPlantillaGeneracionBean.C_DESCRIPCION};
    }
    
    public Vector obtenerListaPlantillas(String idInstitucion, String idTipoEnvios, String idPlantilla)
    {
    	try
    	{
    		String sWhere = " WHERE " + 
    						EnvPlantillaGeneracionBean.C_IDINSTITUCION + "=" + idInstitucion + " AND " +
    						EnvPlantillaGeneracionBean.C_IDTIPOENVIOS + "=" + idTipoEnvios + " AND " +
    						EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS + "=" + idPlantilla;
    		
    		return select(sWhere);
    	}
    	
    	catch(Exception e)
    	{
    		return null;
    	}
    }
    
   	public String getNuevoID(Hashtable entrada) throws ClsExceptions 
	{
		String sID="0";
		
		try
		{
			RowsContainer rc = new RowsContainer();

			String sSQL="";
			
			sSQL = " SELECT MAX(" + EnvPlantillaGeneracionBean.C_IDPLANTILLA + ") AS " + EnvPlantillaGeneracionBean.C_IDPLANTILLA + 
				   " FROM " + nombreTabla +
				   " WHERE " + EnvPlantillaGeneracionBean.C_IDINSTITUCION + "=" + entrada.get(EnvPlantillaGeneracionBean.C_IDINSTITUCION) + " AND " +
				   			   EnvPlantillaGeneracionBean.C_IDTIPOENVIOS + "=" + entrada.get(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS) + " AND " +
				   			   EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS + "=" + entrada.get(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS);
		
			if (rc.findForUpdate(sSQL))
			{
				Row fila = (Row) rc.get(0);
				Hashtable ht = fila.getRow();
				
				String sIDAux = (String)ht.get(EnvPlantillaGeneracionBean.C_IDPLANTILLA);
				
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
   	        						 String idTipoEnvios, 
   	        						 String idPlantillaEnvios, 
   	        						 String idPlantilla, 
   	        						 File fPlantilla, 
   	        						 boolean bPorDefecto, 
   	        						 String extension) throws SIGAException, ClsExceptions 
   	{
   	    boolean bGrabarFicheroPlantilla=fPlantilla!=null;
   	    boolean bInsertDB=(idPlantilla==null || idPlantilla.equals("")) ? true : false;
   	    
	    Hashtable htDatos = new Hashtable();
   		    
	    htDatos.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION, idInstitucion);
	    htDatos.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS, idTipoEnvios);
	    htDatos.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS, idPlantillaEnvios);
	    //htDatos.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA, idPlantilla);

	    if (bPorDefecto)
	    {
	        Vector vDatos = select(htDatos);
	        
	        if (vDatos!=null)
	        {
	            for (int i=0; i<vDatos.size(); i++)
	            {
	                EnvPlantillaGeneracionBean beanPlantilla = (EnvPlantillaGeneracionBean)vDatos.elementAt(i);
	                
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
	    
	    //String idPlantilla2 = (idPlantilla==null || idPlantilla.equals("")) ? getNuevoID(htDatos) : idPlantilla; 
	    String idPlantilla2 = (idPlantilla==null || idPlantilla.equals("")) ? getNuevoID(htDatos) : idPlantilla;
	    htDatos.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA, idPlantilla2);
	    
	    if (bInsertDB)
	    {
		    htDatos.put(EnvPlantillaGeneracionBean.C_DESCRIPCION, descripcionPlantilla);
		    
		    String sPorDefecto = (bPorDefecto) ? "S" : "N" ;
		    htDatos.put(EnvPlantillaGeneracionBean.C_PORDEFECTO, sPorDefecto);
		    if(extension.toLowerCase().equals("zip"))
		    	htDatos.put(EnvPlantillaGeneracionBean.C_TIPOARCHIVO, "fo");
		    else
		    htDatos.put(EnvPlantillaGeneracionBean.C_TIPOARCHIVO, extension);

	        if (!insert(htDatos))
		    {
		        return false;
			}
	    }
	    
	    else
	    {
	        Vector vDatos = select(htDatos);
	        
	        EnvPlantillaGeneracionBean beanPlantilla = (EnvPlantillaGeneracionBean)vDatos.elementAt(0);
	        
	        beanPlantilla.setDescripcion(descripcionPlantilla);
	        beanPlantilla.setPorDefecto(bPorDefecto ? "S" : "N");
	        
	        if (!update(beanPlantilla))
	        {
	            return false;
	        }
	    }
	    
        if (bGrabarFicheroPlantilla)
        {
            //if (!grabarFicheroPlantilla(idInstitucion, idTipoProducto, idProducto, idProductoInstitucion, idPlantilla2, fPlantilla, bZIP))
            if (!grabarFicheroPlantilla(idInstitucion, idTipoEnvios, idPlantillaEnvios, idPlantilla2, fPlantilla, extension))
            {
                if (bInsertDB)
                {
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
   	}
   	
   	private boolean grabarFicheroPlantilla(String idInstitucion, 
				 						   String idTipoEnvios, 
				 						   String idPlantillaEnvios, 
				 						   String idPlantilla, 
				 						   File fPlantilla, 
				 						   String extension) throws SIGAException, ClsExceptions
   	{
   	    try
   	    {
   	        String sCompuesto = idTipoEnvios + "_" + idPlantillaEnvios + "_" + idPlantilla;
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
	        
	        if (extension.toLowerCase().equals("zip"))
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
	                        if (sNombreAux.toLowerCase().endsWith(".doc")){
	                        	throw new SIGAException("messages.certificados.error.nocomunicacion");
	                        }
	                        else if (sNombreAux.endsWith(".fo"))
	                        {
	                            fos = new FileOutputStream(sNombreFinal + ".tmp");
	                        }
	                        
	                        else
	                        {
//	                            htNombresRecursos.put(sNombreAux.substring(sDirectorioRecursos.length()), sNombreRecursos + "_" + cont);
	                            htNombresRecursos.put(sNombreAux, sNombreRecursos + "_" + cont);

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
	                
	                BufferedReader br = new BufferedReader(new FileReader(fTemp));
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
	                
	                BufferedWriter bw = new BufferedWriter(new FileWriter(sNombreFinal));
	                
	                bw.write(sLeido);
	                bw.close();
	                
	                fPlantilla.deleteOnExit();
	                fPlantilla.delete();
	                
	                //fTemp.renameTo(new File(sNombreFinal));
	                fTemp.deleteOnExit();
	                fTemp.delete();
	            }
	            catch(SIGAException e)
	            {	throw e;
	                
	            }
	            catch(Exception e)
	            {
	                e.printStackTrace();
	            }
	        }
	        
	        else 
	        {
//	        	if(extension.toLowerCase().equals("doc")){
//	        		sNombreFinal += ".doc";
//	        	}
	        	File fFicheroDestino = new File(sNombreFinal);
	            
	            fPlantilla.renameTo(fFicheroDestino);
	        }
	        
	        return true;
   	    }
   	 catch(SIGAException e)
     {	throw e;
         
     }
   	    catch(Exception e)
   	    {
   	        return false;
   	    }
	}
   	
   	public File descargarPlantilla(String idInstitucion, 
				 				   String idTipoEnvios, 
				 				   String idPlantillaEnvios, 
				 				   String idPlantilla,String tipoArchivo) throws SIGAException, ClsExceptions 
	{
   	    ZipOutputStream zos = null;
   	    File fPlantilla=null;

   	    try
   	    {
	        String sCompuesto = idTipoEnvios + "_" + idPlantillaEnvios + "_" + idPlantilla;
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
                    return fPlantilla;
                }

            }
            
            
            fPlantilla = new File(sNombreFinal + ".zip");
//            System.out.println(fPlantilla.get);
            
            FileInputStream fis=new FileInputStream(new File(sNombreFinal));
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
		  	 					   String idTipoEnvios, 
		  	 					   String idPlantillaEnvios, 
		  	 					   String idPlantilla) throws SIGAException, ClsExceptions 
	{
   	    String sCompuesto = idTipoEnvios + "_" + idPlantillaEnvios + "_" + idPlantilla;
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
	    
	    hash.put(EnvPlantillaGeneracionBean.C_IDINSTITUCION, idInstitucion);
	    hash.put(EnvPlantillaGeneracionBean.C_IDTIPOENVIOS, idTipoEnvios);
	    hash.put(EnvPlantillaGeneracionBean.C_IDPLANTILLAENVIOS, idPlantillaEnvios);
	    hash.put(EnvPlantillaGeneracionBean.C_IDPLANTILLA, idPlantilla);
	    
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

	public String getPathPlantillasFromDB() throws SIGAException
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
	
   	public File obtenerPlantilla(String idInstitucion, 
			   					 String idTipoEnvios, 
			   					 String idPlantillaEnvios, 
			   					 String idPlantilla) throws SIGAException, ClsExceptions
   	{
   	    try
   	    {
   	   	    String sCompuesto = idTipoEnvios + "_" + idPlantillaEnvios + "_" + idPlantilla;
   	   	    String sNombreFinal = getPathPlantillasFromDB() + File.separator + idInstitucion + File.separator + sCompuesto;
   	   	    
   	   	    // RGG cambio para crear los directorios en tiempo real
   	   	    File aux = null; 
   	   	    try {
   	   	        aux = new File(sNombreFinal);
		   	} catch (Exception e) {
				throw new SIGAException("messages.envios.error.noPlantilla");
		    }
		   	
			if (!aux.exists()) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			} else
		    if (!aux.canRead()){
				throw new ClsExceptions ("Error de lectura de la plantilla. "+aux.getAbsolutePath());
//				throw new SIGAException("facturacion.nuevoFichero.literal.errorLectura");
		    }		   	
   	 		File auxDir = new File(aux.getParent());
			auxDir.mkdirs();
	    
   	        return aux;
   	    }
   	    catch(SIGAException se)
   	    {
   	        return null;
   	    	//throw se;
   	    }
   	    catch(Exception e)
   	    {
   	        return null;
   	        //throw new ClsExceptions(e,"Error al obtener la plantilla");
   	    }
   	}
   	public List<EnvPlantillasEnviosBean> getPlantillasEnvio(String idTipoEnvio,String idPlantilla,
			String idInstitucion) throws ClsExceptions {
		StringBuffer where = new StringBuffer("");
		where.append(" WHERE IDTIPOENVIOS = ");
		where.append(idTipoEnvio);
		where.append(" AND IDPLANTILLA = ");
		where.append(idInstitucion);
		where.append(" AND IDINSTITUCION = ");
		where.append(idInstitucion);
		
		return select(where.toString());
	}
 }