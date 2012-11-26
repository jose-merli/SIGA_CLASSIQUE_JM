package com.siga.Utilidades;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;

 

/**
 * Paginador sensitivo a mayusculas/minusculas y acentos
 * Solo utilizar en CONSULTAS
 * 
 * @author Ana.Combarros
 */
@Deprecated
public class PaginadorCaseSensitiveBind extends PaginadorSQLBind
{
	//
	// CONSTRUCTORES
	//
	//public PaginadorCaseSensitiveBind () throws ClsExceptions {}
	public PaginadorCaseSensitiveBind (String query,
									   Hashtable codigos)
			throws ClsExceptions
	{
		super(query, codigos);
	}
	public PaginadorCaseSensitiveBind (String query1, String query2,
									   Hashtable codigos)
			throws ClsExceptions
	{
		super (query1, query2, codigos);
	}
	public PaginadorCaseSensitiveBind (String query, String[] cabeceras,
									   Hashtable codigos)
			throws ClsExceptions
	{
		super (query, cabeceras, codigos);
	}
	


	//
	// GETTERS
	//
	public String		getQueryOriginal()	{return this.queryOriginal;}
	public Hashtable	getCodigos()		{return this.codigos;}
	
	protected void inicializarBind(String query,Hashtable codigos) throws ClsExceptions {

		try {
			this.codigos=codigos;
		    ReadProperties properties= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties properties = new ReadProperties("SIGA.properties");
			String numRegistros = properties.returnProperty("paginador.registrosPorPagina", true);
			this.numeroRegistrosPagina = numRegistros != null ? Integer.parseInt(numRegistros) : NUM_REGISTROS;
			String pagCache = properties.returnProperty("paginador.distanciaPaginasCache", true);
			this.distanciaPagCache = pagCache != null ? Integer.parseInt(pagCache) : DISTANCIA_PAGINAS_CACHE;
			this.queryOriginal = query;
///////////////////////////////////////////////////////////////////////////////////////////////////
			//Viendo que el order by consume recursos y que no afecta en nada
			//para el count lo quitamos
			int endIndex = queryOriginal.toLowerCase().lastIndexOf("order by");
			String queryCountSinOrder ;
			if(endIndex!=-1)
				queryCountSinOrder = queryOriginal.substring(0,endIndex);
			else
				queryCountSinOrder = queryOriginal;

			// Inicializar numeroTotalRegistros y ultimaPagina
			RowsContainer rc = new RowsContainer();
			ArrayList queryList= UtilidadesBDAdm.quitaCamposQuery(new String(queryCountSinOrder),codigos);
			String queryCountSinOrderSinCampos = (String)queryList.get(0);
			Hashtable codigosReducido = (Hashtable)queryList.get(1);
			String count = null;
			//Ha fallado el quitar los ordenes a los campos
			count = "SELECT  /*+ no_merge */ COUNT(*) AS NUMREGISTROS FROM (" + queryCountSinOrderSinCampos + ")";
			rc.queryBind(count, codigosReducido);
			
			Row row = (Row) rc.get(0);

			this.numeroTotalRegistros = Integer.parseInt(row.getString("NUMREGISTROS"));

			if (this.numeroTotalRegistros > 0) {
				if ((this.numeroTotalRegistros / this.numeroRegistrosPagina) > 0) {
					int paginaDePico = (this.numeroTotalRegistros % this.numeroRegistrosPagina) > 0 ? 1
							: 0;
					this.ultimaPagina = (this.numeroTotalRegistros / this.numeroRegistrosPagina)
							+ paginaDePico;
				} else {
					this.ultimaPagina = 1;
				}
			} else {
				this.ultimaPagina = 0;

			}
		} catch (Exception e) {
			throw new ClsExceptions(e, e.getMessage());
		}
	}

	//
	// METODOS
	//
/*	private void inicializar (String query, Hashtable codigos)
			throws ClsExceptions
	{
		try
		{
			this.codigos = codigos;
			ReadProperties properties = new ReadProperties ("SIGA.properties");
			String numRegistros = properties.returnProperty ("paginador.registrosPorPagina", true);
			this.numeroRegistrosPagina = numRegistros != null ? Integer.parseInt (numRegistros) : NUM_REGISTROS;
			String pagCache = properties.returnProperty ("paginador.distanciaPaginasCache", true);
			this.distanciaPagCache = pagCache!=null ? Integer.parseInt (pagCache) : DISTANCIA_PAGINAS_CACHE;
			this.queryOriginal = query;
			
			// Inicializar numeroTotalRegistros y ultimaPagina
			String count = "SELECT COUNT(1) FROM ("+query+")";
			RowsContainer rc = new RowsContainer();
			rc.queryNLSBind(count, codigos);
			Row row = (Row) rc.get(0);
			
			this.numeroTotalRegistros = Integer.parseInt (row.getString ("COUNT(1)"));
			
			if (this.numeroTotalRegistros > 0){
				if ((this.numeroTotalRegistros / this.numeroRegistrosPagina) > 0){
					int paginaDePico = (this.numeroTotalRegistros%this.numeroRegistrosPagina)>0?1:0;
					this.ultimaPagina=(this.numeroTotalRegistros/this.numeroRegistrosPagina)+paginaDePico;
				}
				else {
					this.ultimaPagina=1;
				}
			}
			else {
				this.ultimaPagina=0;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D.");
		}
	} //inicializar()
*/	
	public Vector obtenerPagina (int pagina) throws ClsExceptions
	{
		Vector retorno=null;
		
		try
		{
			//comprobando si esta ya en CACHE
			if (pagina >= this.paginaInicialCache &&
				pagina <= this.paginaFinalCache)
			{
				retorno = obtenerPaginaCache (pagina-this.paginaInicialCache+1,
						pagina==ultimaPagina);
				paginaActual = pagina;
			}
			else {
				int rowmax = (pagina+DISTANCIA_PAGINAS_CACHE)*
						this.numeroRegistrosPagina;
				int rowmin = (pagina-DISTANCIA_PAGINAS_CACHE-1)*
						this.numeroRegistrosPagina+1;
				
				if (rowmin < 0) {
					rowmin=1;
					this.paginaInicialCache=1;
				}
				else {
					this.paginaInicialCache = pagina - DISTANCIA_PAGINAS_CACHE;
				}
				
				if (rowmax > numeroTotalRegistros) {
					rowmax = numeroTotalRegistros;
					this.paginaFinalCache = ultimaPagina;
				}
				else {
					this.paginaFinalCache = pagina + DISTANCIA_PAGINAS_CACHE;
				}
				
				//select para actualizar la CACHE
				setCache (rowmin, rowmax);
				retorno = obtenerPaginaCache (pagina-this.paginaInicialCache+1,
						pagina==ultimaPagina);
				paginaActual = pagina;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, e.getMessage());
		}
		
		return retorno;
	} //obtenerPagina ()
	
	private Vector obtenerPaginaCache (int pagina, boolean ultimaPag)
	{
		Vector retorno = new Vector();
		
		if (ultimaPag) {
			int resto = numeroTotalRegistros%numeroRegistrosPagina;
			resto = resto>0 ? resto:numeroRegistrosPagina;
			for (int i=0; i<resto; i++) {
				retorno.add (cache.get (numeroRegistrosPagina*(pagina-1)+i));
			}
		}
		else {
			for (int i=0; i<numeroRegistrosPagina; i++) {
				retorno.add (cache.get (numeroRegistrosPagina*(pagina-1)+i));
			}
		}
		
		return retorno;
	} //obtenerPaginaCache ()
	
	private void setCache (int rowmin, int rowmax) throws ClsExceptions
	{
		Hashtable codigosAux;
		int contador;
		String queryTrasSustituir;
		Pattern pattern;
		Matcher matcher;
		
		try
		{
			//obteniendo los parametros de la select
			codigosAux = (Hashtable) codigos.clone();
			contador = codigosAux.size();
			
			//obteniendo la consulta
			queryTrasSustituir = this.query;
			
			//sustituyendo en orden los rowmin y rowmax
			pattern = Pattern.compile ("rowmax");
	        matcher = pattern.matcher (queryTrasSustituir);
			contador++;
			codigosAux.put (new Integer (contador), String.valueOf (rowmax));
			queryTrasSustituir = matcher.replaceAll (":" + contador);
			
			pattern = Pattern.compile ("rowmin");
	        matcher = pattern.matcher (queryTrasSustituir);
			contador++;
			codigosAux.put (new Integer (contador), String.valueOf (rowmin));
			queryTrasSustituir = matcher.replaceAll (":" + contador);
	        
	        //cacheando la pagina
			RowsContainer rc = new RowsContainer();
			rc.queryBind (queryTrasSustituir, codigosAux);
			if (rc!=null) {
			    this.cache = rc.getAll();
			}
		}
		catch (ClsExceptions e) {
			throw new ClsExceptions (e, e.getMessage());
		}
	} //setCache ()
}