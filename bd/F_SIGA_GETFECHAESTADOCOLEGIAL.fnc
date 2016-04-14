 CREATE OR REPLACE FUNCTION "USCGAE_DESA"."F_SIGA_GETFECHAESTADOCOLEGIAL" 
(
  P_IDPERSONA IN NUMBER 
, P_IDINSTITUCION IN NUMBER 
, P_FECHA IN DATE 
) RETURN DATE AS 
/****************************************************************************************************************/
/* Nombre:        F_SIGA_GETFECHAESTADOCOLEGIAL                                                                          *
/*  				                                                                         		                        */
/* Parametros            IN/OUT   Descripcion                                                    Tipo de Datos  */
/* -------------------   ------   ------------------------------------------------------------   -------------  */
/* P_IDPERSONA	           IN	      identificador de la persona                                       NUMBER    */
/* P_IDINSTITUCION         IN	      identificador de la institucion                                   NUMBER    */
/*          												                                                                            */
/* Version:        1.0											                                                                    */
/* Fecha Creacion: 30/03/2016                                                                                   */
/* Autor:		       José Mansilla García-Gil                                                                     */
/* Descripción:    Función que devolverá la fecha del estado actual del colegiado dado.                         */
/* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
/* ------------------   ---------------------------------   --------------------------------------------------- */
/****************************************************************************************************************/

/* Declaracion de variables */

  V_fechaEstado Date:=NULL;
BEGIN
 begin
         select max(fechaestado) INTO V_fechaEstado
         from cen_datoscolegialesestado 
         where idinstitucion = P_IDINSTITUCION  
            and idpersona = P_IDPERSONA
            and trunc(fechaestado) <= trunc(P_FECHA);
         
         EXCEPTION
           WHEN NO_DATA_FOUND THEN
                V_fechaEstado := NULL;
                RETURN V_fechaEstado;
        end;
  RETURN V_fechaEstado;
END F_SIGA_GETFECHAESTADOCOLEGIAL;

/
