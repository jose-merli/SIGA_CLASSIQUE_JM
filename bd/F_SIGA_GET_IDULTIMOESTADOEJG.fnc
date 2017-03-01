create or replace function F_SIGA_GET_IDULTIMOESTADOEJG(P_INSTITUCION IN SCS_EJG.IDINSTITUCION%type,
                                                 P_IDTIPOEJG   IN SCS_EJG.IDTIPOEJG%type,
                                                 P_ANIO        IN SCS_EJG.ANIO%type,
                                                 P_NUMERO      IN SCS_EJG.NUMERO%type)
  return SCS_ESTADOEJG.IDESTADOEJG%type is
  /*
  * Nombre:       F_SIGA_GET_IDULTIMOESTADOEJG
  * Descripcion:  Funcion para la obtencion del ultimo estado de un EJG
  *
  * Fecha Creacion: 16/03/2009
  * Autor:          Angel Corral
  */

  v_result SCS_ESTADOEJG.IDESTADOEJG%type;

  cursor c_estados is
  SELECT E.IDESTADOEJG
    FROM SCS_ESTADOEJG E
   WHERE E.IDINSTITUCION = P_INSTITUCION
     AND E.IDTIPOEJG = P_IDTIPOEJG
     AND E.ANIO = P_ANIO
     AND E.NUMERO = P_NUMERO
     AND E.FECHABAJA IS NULL
   ORDER BY TRUNC(E.FECHAINICIO) DESC, E.IDESTADOPOREJG DESC;

begin

  v_result := null;
  for i in c_estados loop
    v_result := i.IDESTADOEJG;
    exit;
  end loop;

  return(v_result);
end F_SIGA_GET_IDULTIMOESTADOEJG;
/
