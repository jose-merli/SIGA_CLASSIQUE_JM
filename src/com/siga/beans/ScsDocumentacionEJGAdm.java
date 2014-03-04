package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.*;

//Clase: ScsDocumentacionEJGAdm 
//Autor: julio.vicente@atosorigin.com
//Ultima modificación: 03/02/2005
/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update...
 * 
 */
public class ScsDocumentacionEJGAdm extends MasterBeanAdministrador {

	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDocumentacionEJGAdm(UsrBean usuario) {
		super(ScsDocumentacionEJGBean.T_NOMBRETABLA, usuario);
	}
		
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador de la fiesta que se va a insertar.
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;		
		int contador = 0;
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
  			String sql ="SELECT NVL(MAX(IDDOCUMENTACION), 0) + 1 FROM SCS_DOCUMENTACIONEJG WHERE  IDINSTITUCION =  "+ entrada.get(ScsDocumentacionEJGBean.C_IDINSTITUCION);
						
  			
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				entrada.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION,(String)prueba.get("IDDOCUMENTACION"));				
			}
			
			//Lo quito de aqui pq si llamas varias veces a este metodo te da la vuelta a las fechas :S
			// Ahora adaptamos las fechas al formato de la base de datos
//			if ((entrada.containsKey(ScsDocumentacionEJGBean.C_FECHALIMITE)) && (!((String)entrada.get(ScsDocumentacionEJGBean.C_FECHALIMITE)).equals(""))) entrada.put(ScsDocumentacionEJGBean.C_FECHALIMITE,GstDate.getApplicationFormatDate("",entrada.get(ScsDocumentacionEJGBean.C_FECHALIMITE).toString()));
//			if ((entrada.containsKey(ScsDocumentacionEJGBean.C_FECHAENTREGA)) && (!((String)entrada.get(ScsDocumentacionEJGBean.C_FECHAENTREGA)).equals(""))) entrada.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,GstDate.getApplicationFormatDate("",entrada.get(ScsDocumentacionEJGBean.C_FECHAENTREGA).toString()));
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCIÓN. CÁLCULO DE IDDOCUMENTACION");
		};
		
		return entrada;
	}	

	/** Funcion update (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el update 
	 *  @return true -> OK false -> Error 
	 * */
	public boolean update(Hashtable hash) throws ClsExceptions {
		try {
			return this.update(hash,null);
		}
		catch (Exception e)	{
			throw new ClsExceptions (e, "Error al ejecutar el 'update' en B.D. " + e.getMessage());
		}
	}
	
	/** Funcion select (String where). Devuele todos los campos de los registros que cumplan los criterios.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectAll(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT * FROM " + ScsDocumentacionEJGBean.T_NOMBRETABLA + " " + where ;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'selectAll' en B.D."); 
		}		
		return datos;
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_DOCUMENTACION con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = 
				" WHERE " + ScsDocumentacionEJGBean.C_IDINSTITUCION + " = " + hash.get(ScsDocumentacionEJGBean.C_IDINSTITUCION) + 
				" AND " + ScsDocumentacionEJGBean.C_IDDOCUMENTACION+ "=" +(String)hash.get(ScsDocumentacionEJGBean.C_IDDOCUMENTACION);		
			datos = this.selectAll(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}

	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * */
	protected String[] getCamposBean() {
		
		String[] campos= {	ScsDocumentacionEJGBean.C_IDINSTITUCION,		ScsDocumentacionEJGBean.C_IDTIPOEJG,
							ScsDocumentacionEJGBean.C_ANIO,					ScsDocumentacionEJGBean.C_NUMERO,
							ScsDocumentacionEJGBean.C_IDDOCUMENTACION,		ScsDocumentacionEJGBean.C_FECHALIMITE,
							ScsDocumentacionEJGBean.C_DOCUMENTACION,		ScsDocumentacionEJGBean.C_FECHAENTREGA,
							ScsDocumentacionEJGBean.C_FECHAMODIFICACION,	ScsDocumentacionEJGBean.C_USUMODIFICACION,
							ScsDocumentacionEJGBean.C_REGENTRADA,			ScsDocumentacionEJGBean.C_REGSALIDA,
							ScsDocumentacionEJGBean.C_PRESENTADOR,ScsDocumentacionEJGBean.C_IDPRESENTADORMAESTRO,			ScsDocumentacionEJGBean.C_IDDOCUMENTO,
						 	ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO,ScsDocumentacionEJGBean.C_IDFICHERO};

		return campos;
	}

	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todas las claves del bean
	 * */
	protected String[] getClavesBean() {
		
		String[] campos= {ScsDocumentacionEJGBean.C_IDDOCUMENTACION
				, ScsDocumentacionEJGBean.C_IDINSTITUCION
				};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDocumentacionEJGBean bean = null;
		
		try {
			bean = new ScsDocumentacionEJGBean();
			
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_IDINSTITUCION));
			
			if (hash.get(ScsDocumentacionEJGBean.C_IDINSTITUCION).toString()!="") {
				bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_IDINSTITUCION));
			}
			if (hash.get(ScsDocumentacionEJGBean.C_IDTIPOEJG).toString()!="") {
				bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_IDTIPOEJG));
			}
			bean.setAnio((UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_ANIO)));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_NUMERO));
			bean.setIdDocumentacion(UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_IDDOCUMENTACION));
			bean.setFechaLimite(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_FECHALIMITE));
			bean.setFechaEntrega(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_FECHAENTREGA));
			bean.setDocumentacion(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_DOCUMENTACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDocumentacionEJGBean.C_USUMODIFICACION));		
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_FECHAMODIFICACION));
			bean.setRegEntrada(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_REGENTRADA));		
			bean.setRegSalida(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_REGSALIDA));
			bean.setPresentador(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_PRESENTADOR));
			bean.setIdPresentadorMaestro(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_IDPRESENTADORMAESTRO));	
			bean.setIdDocumento(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_IDDOCUMENTO));
			bean.setIdTipoDocumento(UtilidadesHash.getString(hash,ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO));
			bean.setIdFichero(UtilidadesHash.getLong(hash,ScsDocumentacionEJGBean.C_IDFICHERO));
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}		
		return bean;

	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			ScsDocumentacionEJGBean b = (ScsDocumentacionEJGBean) bean;
			htData.put(ScsDocumentacionEJGBean.C_IDINSTITUCION, String.valueOf(b.getIdInstitucion()));
			htData.put(ScsDocumentacionEJGBean.C_IDTIPOEJG, b.getIdTipoEJG());
			htData.put(ScsDocumentacionEJGBean.C_ANIO, b.getAnio());
			htData.put(ScsDocumentacionEJGBean.C_FECHAENTREGA, String.valueOf(b.getFechaEntrega()));
			htData.put(ScsDocumentacionEJGBean.C_FECHALIMITE, String.valueOf(b.getFechaLimite()));
			htData.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION, b.getIdDocumentacion());
			htData.put(ScsDocumentacionEJGBean.C_DOCUMENTACION, b.getDocumentacion());			
			htData.put(ScsDocumentacionEJGBean.C_FECHAMODIFICACION, b.getFechaMod());
			htData.put(ScsDocumentacionEJGBean.C_USUMODIFICACION, String.valueOf(b.getUsuMod()));
			htData.put(ScsDocumentacionEJGBean.C_NUMERO, String.valueOf(b.getNumero()));
			htData.put(ScsDocumentacionEJGBean.C_REGENTRADA, String.valueOf(b.getRegEntrada()));
			htData.put(ScsDocumentacionEJGBean.C_REGSALIDA, String.valueOf(b.getRegSalida()));
			htData.put(ScsDocumentacionEJGBean.C_PRESENTADOR, String.valueOf(b.getPresentador()));
			htData.put(ScsDocumentacionEJGBean.C_IDPRESENTADORMAESTRO, String.valueOf(b.getIdPresentadorMaestro()));
			htData.put(ScsDocumentacionEJGBean.C_IDDOCUMENTO, String.valueOf(b.getIdDocumento()));
			htData.put(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, String.valueOf(b.getIdTipoDocumento()));
			htData.put(ScsDocumentacionEJGBean.C_IDFICHERO, String.valueOf(b.getIdFichero()));
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		return htData;
	}

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener las selecciones. 
	 * 
	 * @return vector de Strings con los campos con los que se desea realizar la ordenación.
	 */
	protected String[] getOrdenCampos() {

		String[] campos= {ScsDocumentacionEJGBean.C_IDINSTITUCION, ScsDocumentacionEJGBean.C_IDTIPOEJG, ScsDocumentacionEJGBean.C_ANIO, ScsDocumentacionEJGBean.C_NUMERO};
		return campos;
	}
	
	/**
	 * Efectúa un SELECT en la tabla SCS_DOCUMENTACION con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector buscar(Hashtable hash,String idioma) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			
		      
			sql.append("SELECT to_char(DE.FECHALIMITE,'dd/mm/yyyy') FECHALIMITE,     to_char(DE.FECHAENTREGA,'dd/mm/yyyy')       FECHAENTREGA,    DE.REGENTRADA,     DE.REGSALIDA, ");
			sql.append("DECODE(PERSONA.IDPERSONA, ");
			sql.append("NULL, ");
			sql.append("'IDMAESTROPRESENTADOR_' || MAESTROPRESENTADOR.IDPRESENTADOR, ");
			sql.append("'IDPERSONAJG_' || PERSONA.IDPERSONA) IDPRESENTADOR, ");
		       
			sql.append("DECODE(PERSONA.IDPERSONA, ");
			sql.append("NULL, ");
			sql.append("F_SIGA_GETRECURSO(MAESTROPRESENTADOR.DESCRIPCION, ");
			sql.append(idioma);
			sql.append("), ");
			
			sql.append("PERSONA.NOMBRE || ' ' || PERSONA.APELLIDO1 || ' ' || ");
			sql.append("PERSONA.APELLIDO2 || ' (' || ");
			sql.append("DECODE((SELECT 1 ");
			sql.append("FROM DUAL ");
			sql.append("WHERE PERSONA.IDPERSONA = FAMILIA.IDPERSONA ");
			sql.append("OR FAMILIA.SOLICITANTE = 1), ");
			sql.append("1, ");
			sql.append("F_SIGA_GETRECURSO_ETIQUETA('gratuita.busquedaEJG.literal.solicitante', ");
			sql.append(idioma);
			sql.append("), ");
			sql.append("(SELECT F_SIGA_GETRECURSO(SCS_PARENTESCO.DESCRIPCION, ");
			sql.append(idioma);
			sql.append(") ");
			sql.append("FROM SCS_PARENTESCO ");
			sql.append("WHERE SCS_PARENTESCO.IDINSTITUCION =   FAMILIA.IDINSTITUCION ");
			sql.append("AND SCS_PARENTESCO.IDPARENTESCO =    FAMILIA.IDPARENTESCO)) || ')') AS PRESENTADOR ");
			sql.append(",     D.ABREVIATURA DOCUMENTO,     DE.IDDOCUMENTACION,     DE.IDINSTITUCION ");

			sql.append("FROM SCS_DOCUMENTACIONEJG  DE,     SCS_DOCUMENTOEJG      D,      SCS_UNIDADFAMILIAREJG FAMILIA, ");
			sql.append("SCS_PERSONAJG         PERSONA,	SCS_PRESENTADOR       MAESTROPRESENTADOR ");
			sql.append("WHERE DE.IDTIPODOCUMENTO = D.IDTIPODOCUMENTOEJG ");
			sql.append("AND DE.IDINSTITUCION = D.IDINSTITUCION ");
			sql.append("AND DE.IDDOCUMENTO = D.IDDOCUMENTOEJG ");
			sql.append("AND FAMILIA.IDPERSONA = PERSONA.IDPERSONA ");
			sql.append("AND FAMILIA.IDINSTITUCION = PERSONA.IDINSTITUCION ");
		      
			sql.append("AND DE.IDINSTITUCION = FAMILIA.IDINSTITUCION(+) ");
			sql.append("AND DE.IDTIPOEJG = FAMILIA.IDTIPOEJG(+) ");
			sql.append("AND DE.ANIO = FAMILIA.ANIO(+) ");
			sql.append("AND DE.NUMERO = FAMILIA.NUMERO(+) ");
			sql.append("AND DE.PRESENTADOR = FAMILIA.IDPERSONA(+) ");
		      
			sql.append("AND DE.IDMAESTROPRESENTADOR = MAESTROPRESENTADOR.IDPRESENTADOR(+) ");
			sql.append("AND DE.IDINSTITUCION = MAESTROPRESENTADOR.IDINSTITUCION(+) ");
		      
			sql.append("AND DE.IDINSTITUCION = ");
			sql.append(hash.get(ScsDocumentacionEJGBean.C_IDINSTITUCION));
			sql.append(" AND DE.IDTIPOEJG = ");
			sql.append(hash.get(ScsDocumentacionEJGBean.C_IDTIPOEJG));
			sql.append(" AND DE.ANIO = ");
			sql.append(hash.get(ScsDocumentacionEJGBean.C_ANIO));
			sql.append(" AND DE.NUMERO = ");
			sql.append(hash.get(ScsDocumentacionEJGBean.C_NUMERO));

			sql.append("ORDER BY PRESENTADOR ");
			
				
			try { 
				RowsContainer rc = new RowsContainer(); 	
				if (rc.query(sql.toString())) {
					for (int i = 0; i < rc.size(); i++)	{		
						Row fila = (Row) rc.get(i);
						Hashtable registro = (Hashtable)fila.getRow();
						if (registro != null) 
							datos.add(registro);
					}
				}
			}catch (Exception e) {
				throw new ClsExceptions (e, "Excepcion en ScsDocumentacionEJG.buscar(). Consulta SQL:"+sql);
			}
		} 
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}
	/**
	 * Insertar en un vector cada fila como una tabla hash del resultado de ejecutar la query
	 * @param Hashtable miHash: tabla hash de datos necesarios para la consulta SQL.
	 * @param String consulta: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con tablas hash. Cada tabla hash es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		}catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsDelitosDesignaAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	
	
	public Vector selectGenericoNLS(String consulta) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD	
		try { 
			RowsContainer rc = new RowsContainer(); 	
			if (rc.queryNLS(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{		
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable)fila.getRow();
					if (registro != null) 
						datos.add(registro);
				}
			}
		}catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en ScsDelitosDesignaAdm.selectGenerico(). Consulta SQL:"+consulta);
		}
		return datos;	
	}	
	/**
	 * Efectúa un SELECT en la tabla SCS_DOCUMENTACION con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de búsqueda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector buscarDocumentacionPendiente(String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions {
		Vector datos = null; 
		
		try { 
			String sql = 
				"select "+ ScsDocumentacionEJGBean.C_DOCUMENTACION+
				" from "+ ScsDocumentacionEJGBean.T_NOMBRETABLA+
				" where " + ScsDocumentacionEJGBean.C_IDINSTITUCION + " = " + institucion + 
				" and " + ScsDocumentacionEJGBean.C_IDTIPOEJG + " = " + tipoEJG + 
				" and " + ScsDocumentacionEJGBean.C_ANIO + " = " + anio + 
				" and " + ScsDocumentacionEJGBean.C_NUMERO + " = " + numero + 
				" and " + ScsDocumentacionEJGBean.C_FECHAENTREGA + " is null " +
				" order by " + ScsDocumentacionEJGBean.C_DOCUMENTACION;						   	
			datos = this.selectGenerico(sql);
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}


	public int deleteDocumentacionPresentador(String idInstitucionEJG, String idTipoEJG, String anioEJG,
			String numeroEJG, String presentador) throws ClsExceptions {
		
		String sqlDelete = "DELETE FROM " + ScsDocumentacionEJGBean.T_NOMBRETABLA +
			" WHERE " + ScsDocumentacionEJGBean.C_IDINSTITUCION + " = " + idInstitucionEJG +
			" AND " + ScsDocumentacionEJGBean.C_IDTIPOEJG + " = " + idTipoEJG +
			" AND " + ScsDocumentacionEJGBean.C_ANIO + " = " + anioEJG +
			" AND " + ScsDocumentacionEJGBean.C_NUMERO + " = " + numeroEJG +
			" AND " + ScsDocumentacionEJGBean.C_PRESENTADOR + " = " + presentador;

		Row row = new Row();
		int borrados =  row.deleteSQL(sqlDelete);
		ClsLogging.writeFileLog("Registros de " + ScsDocumentacionEJGBean.T_NOMBRETABLA + " borrados = " + borrados, 10);
		return borrados;
	}


	public RowsContainer getDocumentacionEJG(String idInstitucion, String idTipoEJG, String anio, String numero,
			String idioma) throws ClsExceptions {
		
		String sql = "SELECT EJG.NUMEJG, EJG.ANIO, DECODE(EJG.IDPERSONAJG,PER.IDPERSONA, 1,0) AS INTERESADO" +
				", DECODE(U.SOLICITANTE, 1, f_siga_getrecurso_etiqueta('gratuita.busquedaEJG.literal.solicitante', " + idioma + ")" +
						", (select f_siga_getrecurso(P.DESCRIPCION, " + idioma + ")" +
						" FROM SCS_PARENTESCO P" +
						" WHERE P.IDINSTITUCION = U.IDINSTITUCION" +
						" AND P.IDPARENTESCO = U.IDPARENTESCO)) AS SOLICITANTE_PARENTESCO" +
				", DOC.DOCUMENTACION" +
				", DOC.PRESENTADOR, DOC.IDDOCUMENTACION, DOC.IDFICHERO, TDOC.IDTIPODOCUMENTOEJG" +
				", f_siga_getrecurso(TDOC.DESCRIPCION, " + idioma + ") DES_TIPODOC" +
				", f_siga_getrecurso(DOCU.DESCRIPCION, " + idioma + ") DES_DOCU" +
				", PER.NOMBRE, PER.APELLIDO1, PER.APELLIDO2" +
				", DECODE(PER.DIRECCION, NULL, '-', ((SELECT (UPPER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+idioma+"), 1, 1))) || (LOWER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+idioma+"),2))) FROM CEN_TIPOVIA TV WHERE TV.IDTIPOVIA = PER.IDTIPOVIA AND TV.IDINSTITUCION = PER.IDINSTITUCION) || ' ' || PER.DIRECCION || ' ' || PER.NUMERODIR || ' ' || PER.ESCALERADIR || ' ' || PER.PISODIR || ' ' || PER.PUERTADIR)) as DIRECCION " +
				", PER.CODIGOPOSTAL"+
				", PER.IDLENGUAJE"+
				", (SELECT POB.NOMBRE FROM Cen_Poblaciones POB WHERE POB.IDPOBLACION=PER.IDPOBLACION) AS POBLACION"+
				", (SELECT PRO.NOMBRE FROM Cen_Provincias PRO WHERE PRO.IDPROVINCIA=PER.IDPROVINCIA) AS PROVINCIA"+
				", (SELECT F_SIGA_GETRECURSO(PAI.NOMBRE,1) FROM CEN_PAIS PAI WHERE PAI.IDPAIS=PER.IDPAIS) AS PAIS"+			
				", EJG.FECHALIMITEPRESENTACION, ejg.FECHAPRESENTACION, DOC.Fechalimite as FECLIMITEPRESENTADOC, DOC.Fechaentrega as FECPRESENTACIONDOC" + 
				
				" FROM SCS_EJG EJG, SCS_DOCUMENTACIONEJG DOC, SCS_TIPODOCUMENTOEJG TDOC, SCS_DOCUMENTOEJG DOCU" +
				"		, SCS_PERSONAJG PER, SCS_UNIDADFAMILIAREJG U" +
				
				" WHERE EJG.IDINSTITUCION = DOC.IDINSTITUCION" +
					" AND EJG.IDTIPOEJG = DOC.IDTIPOEJG" +
					" AND EJG.ANIO = DOC.ANIO" +
					" AND EJG.NUMERO = DOC.NUMERO" +				
					" AND DOC.IDINSTITUCION = TDOC.IDINSTITUCION" +
					" AND DOC.IDTIPODOCUMENTO = TDOC.IDTIPODOCUMENTOEJG" +
					" AND DOCU.IDINSTITUCION = DOC.IDINSTITUCION" +
					" AND DOCU.IDTIPODOCUMENTOEJG = DOC.IDTIPODOCUMENTO" +
					" AND DOCU.IDDOCUMENTOEJG = DOC.IDDOCUMENTO" +
					" AND DOC.IDINSTITUCION = PER.IDINSTITUCION " +
					" AND DOC.PRESENTADOR = PER.IDPERSONA" +
					
					" AND DOC.IDTIPOEJG = U.IDTIPOEJG" +
					" AND DOC.ANIO = U.ANIO" +
					" AND DOC.NUMERO = U.NUMERO" +				
					" AND DOC.IDINSTITUCION = U.IDINSTITUCION" +
					" AND DOC.PRESENTADOR = U.IDPERSONA" +
					
					 " AND EJG.IDTIPOEJG = " + idTipoEJG +
					 " AND EJG.IDINSTITUCION = " + idInstitucion +
					 " AND EJG.ANIO = " + anio +
					 " AND EJG.NUMERO = " + numero +
					 " ORDER BY INTERESADO DESC, U.SOLICITANTE DESC, DOC.PRESENTADOR, DOC.IDTIPODOCUMENTO,  DOC.IDDOCUMENTACION";

		RowsContainer rowscontainer = new RowsContainer();

		if (rowscontainer.query(sql)) {
			return rowscontainer;
		} else {
			return null;
		}
	}
	
	public int copiaDocumentacionPresentador(String idInstitucionEJG, String idTipoEJG, String anioEJG,
		String numeroEJG, String presentadorAnterior, String presentadorNuevo) throws ClsExceptions {
		
		String sqlUpdate = "update scs_documentacionejg set presentador = " + presentadorNuevo +
			" WHERE " + ScsDocumentacionEJGBean.C_IDINSTITUCION + " = " + idInstitucionEJG +
			" AND " + ScsDocumentacionEJGBean.C_IDTIPOEJG + " = " + idTipoEJG +
			" AND " + ScsDocumentacionEJGBean.C_ANIO + " = " + anioEJG +
			" AND " + ScsDocumentacionEJGBean.C_NUMERO + " = " + numeroEJG +
			" AND " + ScsDocumentacionEJGBean.C_PRESENTADOR + " = " + presentadorAnterior;

		Row row = new Row();
		int copiados =  row.updateSQL(sqlUpdate);
		ClsLogging.writeFileLog("Registros de " + ScsDocumentacionEJGBean.T_NOMBRETABLA + " copiados = " + copiados, 10);
		return copiados;
	}
}