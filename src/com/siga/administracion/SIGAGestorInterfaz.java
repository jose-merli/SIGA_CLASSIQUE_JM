package com.siga.administracion;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ColumnConstants;
import com.atos.utils.Persistible;
import com.atos.utils.Row;
import com.siga.Utilidades.SIGAReferences;


/**
 * @author tomas.narros
 *	<p>Clase de Gestión del interfaz gráfico de la aplicación, 
 *  dependiente de localización.</p> 
 */
public class SIGAGestorInterfaz implements Persistible {
	
	/**
	 * <p>Nombre del archivo de propiedades donde se encuentran definidos las
	 * combinaciones de colores, así como los tipos de letra, asociados a
	 * sus códigos correspondientes.</p>
	 * <p>Asímismo, se define cuál es la combinación por defecto de la aplicación</p>
	 */
	public static final String interfaceOptionsFile="interface.properties";
	
	//Mapeo de campos y tabla
	//public static final String TN_SIGA_INTERFAZ="SIGA_INTERFAZ";
	public static final String T_NOMBRETABLA="ADM_GESTORINTERFAZ";
	//public static final String FN_IDINSTITUCION="IDINSTITUCION";
	public static final String C_ADM_GESTORINTERFAZ_ID="ADM_GESTORINTERFAZ_ID";
	//public static final String FN_IDCOLOR="IDCOLOR";
	public static final String C_COLOR="COLOR";
	//public static final String FN_IDTIPOLETRA="IDTIPOLETRA";
	public static final String C_TIPOLETRA="TIPOLETRA";
	//public static final String FN_LOGOIMAGEN="LOGOIMAGEN";
	public static final String C_LOGO="LOGO";
	public static final String C_USUMODIFICACION=ColumnConstants.FIELD_USER_MODIFICATION;
	public static final String C_FECHAMODIFICACION=ColumnConstants.FIELD_DATE_MODIFICATION;
	
	protected static final String fontPrefix="font.style";
	protected static final String colorPrefix="color";
	protected static final String logoPrefix="logo";
		
	private String idInstitucion;
	private Row record;
	//para implementar los métodos de integridad de datos
	private Hashtable oldData;
	private boolean foundInDataBase=false;
	private String userName;
	
	protected SIGAGestorInterfaz() {		
	}
	
	/**
	 * <p>Crea una nueva instancia de la clase, cargando de base de datos
	 * el estilo almacenado para una institución determinada, si lo hay.<p>
	 * <p>En caso contrario, crea un registro de interfaz, con los estilos 
	 * por defecto parametrizado en el archivo de propiedades.</p>
	 * @param sIdInstitucion
	 */
	public SIGAGestorInterfaz(String sIdInstitucion) throws ClsExceptions {
		this.idInstitucion=sIdInstitucion;
		foundInDataBase=search();
		if(!foundInDataBase) {
			generarRegistroPorDefecto();
		} else {
			oldData=(Hashtable)record.getRow().clone();
		}
	}
	
	/**
	 * @return Código de referencia de la combinación de colores
	 */
	public String getColorCode() {
		return record.getString(C_COLOR);
	}

	/**
	 * @param code Código de referencia de la combinación de colores
	 */
	public void setColorCode(String code) {
		record.setValue(C_COLOR, code);
	}

	/**
	 * @return Código de referencia del tipo de letra
	 */
	public String getFontTypeCode() {
		return record.getString(C_TIPOLETRA);
	}

	/**
	 * @param code Código de referencia de la combinación de colores
	 */
	public void setFontTypeCode(String code) {
		record.setValue(C_TIPOLETRA, code);
	}


	/**
	 * @return Nombre del archivo de Imagen del logotipo
	 */
	public String getLogoImg() {
		return record.getString(C_LOGO);
	}
	
	/**
	 * @return Nombre del archivo de Imagen del logotipo
	 */
	public void setLogoImg(String fileName) {
		record.setValue(C_LOGO, fileName);
	}
	
	
	/**
	 * @return para implementar los métodos de integridad de datos
	 */
	public Hashtable getOldData() {
		return oldData;
	}

	public Row getRecord() {
		return record;
	}

	/**
	 * @return Codigo del usuaio
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param hashtable para implementar los métodos de integridad de datos en las modificaciones
	 */
	public void setOldData(Hashtable hashtable) {
		oldData = hashtable;
	}

	/** 
	 * Nombre del usuario.
	 * Es obligatorio para procesos de actualizacinhón de la base de datos.
	 * @param string Codigo del usuario
	 */
	public void setUserName(String string) {
		userName = string;
	}
	
	/**
	 * <p>Actualiza la confirguracion del Interfaz para la localización.</p>
	 * @return Boolean <code>true</code> si todo ha ido bien. 
	 */
	public boolean actualizarInterfaz() throws ClsExceptions {
		boolean ok=false;
		if(userName==null) {
			throw new ClsExceptions("ERROR DE PROGRAMACIÓN :: com.siga.admin.SIGAGestorInterfaz.actualizarInterfaz() :: El código de Usuario es Obligatorio para actualizar el Interfaz.");
		}
		record.setValue(C_USUMODIFICACION, getUserName());
		record.setValue(C_FECHAMODIFICACION, "SYSDATE");
		if(this.foundInDataBase) {
			record.setCompareData(oldData);			
			ok=update();
		} else {			
			ok=add();
		}		
		return ok;	
	}


	/** 
	 * @see com.atos.utils.Persistible#add()
	 */
	public boolean add() throws ClsExceptions {
		Object[] fields={C_ADM_GESTORINTERFAZ_ID ,C_COLOR, 
						C_TIPOLETRA, C_LOGO, 
						C_USUMODIFICACION, C_FECHAMODIFICACION};
		int i = record.add(T_NOMBRETABLA, fields);		
		return (i==1);
	}

	/**
	 * @see com.atos.utils.Persistible#update()
	 */
	public boolean update() throws ClsExceptions {
		Object[] pk={C_ADM_GESTORINTERFAZ_ID};
		Object[] fieldsToSet={C_COLOR, C_TIPOLETRA, 
							C_LOGO, 
							C_USUMODIFICACION, 
							C_FECHAMODIFICACION};	
		int i=record.update(T_NOMBRETABLA, pk, fieldsToSet);
		return (i==1);
	}

	/**
	 * Método generado por implementación del Interfaz persistible.
	 * @return siempre <code>0</code>
	 * @see com.atos.utils.Persistible#delete()
	 */
	public int delete() throws ClsExceptions {		
		return 0;
	}
	
	public boolean search() throws ClsExceptions {
		boolean found=false;
		try {
		record=new Row();
		String sql="SELECT "+C_ADM_GESTORINTERFAZ_ID+", "+C_COLOR+", "+        
					C_TIPOLETRA+", "+C_LOGO+
					" FROM "+T_NOMBRETABLA+
					" WHERE "+C_ADM_GESTORINTERFAZ_ID+"= "+idInstitucion+" ";

			found=record.find(sql);
		} catch(ClsExceptions ex) {
			
			throw ex; 
		}		
		return found;
	}
	
	private void generarRegistroPorDefecto() throws ClsExceptions {
		record.setValue(C_ADM_GESTORINTERFAZ_ID, this.idInstitucion);
		Properties all=readOptionsFile();
		
		record.setValue(C_COLOR, all.getProperty("default.color"));
		record.setValue(C_TIPOLETRA, all.getProperty("default.font.style"));
		record.setValue(C_LOGO, all.getProperty("default.logo"));
	}
	
	public Properties getInterfaceOptions() throws ClsExceptions{
		Properties all=readOptionsFile();
		Properties toReturn=null;
		//cadenas para identificar la combinación a subir
		String indiceColor=(String)record.getValue(C_COLOR);
		String colorsToLoad=colorPrefix+"."+record.getValue(C_COLOR);
		String colorCode="."+record.getValue(C_COLOR)+".";
		String fontToLoad=fontPrefix+"."+record.getValue(C_TIPOLETRA);
		//String LogoToLoad=logoPrefix+"."+record.getValue(FN_LOGOIMAGEN);
		String LogoToLoad=""+record.getValue(C_LOGO);
		
		
		//System.out.println("colorsToLoad: "+colorsToLoad);
		//System.out.println("colorCode: "+colorCode);
		//System.out.println("fontToLoad: "+fontToLoad);
		//System.out.println("LogoToLoad: "+LogoToLoad);
		
		Enumeration<Object> en=all.keys();
		while (en.hasMoreElements()) {
			String key=(String)en.nextElement();
			if(key.startsWith(colorsToLoad)||key.startsWith(fontToLoad)||key.startsWith(LogoToLoad)) {
				if(toReturn==null) toReturn=new Properties();
				if(key.startsWith(colorsToLoad)) {
					int codPos=key.indexOf(colorCode);
					String genKey=key.substring(0,codPos)+key.substring(codPos+2);
					toReturn.setProperty(genKey,all.getProperty(key));
				} else if(key.startsWith(fontToLoad)) { //son fuentes
					toReturn.setProperty(fontPrefix, all.getProperty(key));					
				}
				else{ // Es el logo
					//toReturn.setProperty(logoPrefix, all.getProperty(key));
				    toReturn.setProperty(logoPrefix, all.getProperty(key));
				}
			}
		}
		 toReturn.setProperty(colorPrefix, indiceColor);
		return toReturn;
	}
	
	private Properties readOptionsFile() throws ClsExceptions {
		Properties props=new Properties();
		//String file=ClsConstants.RESOURCES_DIR+ClsConstants.FILE_SEP+SIGAGestorInterfaz.interfaceOptionsFile;
		
		//System.out.println("file file: "+file);
					
		try {
			//props.load(new FileInputStream(file));
			props.load(SIGAReferences.getInputReference(SIGAReferences.RESOURCE_FILES.INTERFACE));
		} catch (java.io.IOException e) {
			 throw new ClsExceptions("ERROR DE PROGRAMACION: NO SE ENCUENTRA EL ARCHIVO DE PROPIEDADES!!! "+SIGAReferences.RESOURCE_FILES.INTERFACE.getFileName());
		}
		return props;
	}
	
}
