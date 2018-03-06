Pkg_Siga_Fusion_Personas
V_pcajg_ejg

PROC_RESPUESTAEJG_2003

Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 1;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.#CA' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 2;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.#EU' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 3;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.#GL' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 4;

delete FROM SCS_ESTADOEJG
 WHERE (IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO) IN
       (SELECT ER.IDINSTITUCION, ER.IDTIPOEJG, ER.ANIO, ER.NUMERO
          FROM CAJG_EJGREMESA ER
         WHERE ER.IDREMESA = 2105
           AND ER.IDINSTITUCION = 2003)
   and trunc(fechamodificacion) = '22/02/2018';
delete CAJG_REMESAESTADOS ER
 where er.idremesa = 2105
   and er.idinstitucion = 2003
   and idestado in (1, 2, 3);

delete cajg_respuesta_ejgremesa ejgremesa
 where idejgremesa in (select idejgremesa
                         from CAJG_EJGREMESA
                        where idinstitucion = 2003
                          and idremesa = 2105);

update PCAJG_ALC_INT_CAB cab
   set Cab_Est_Id = 2
 where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio, cab.cab_ejg_idtipo,
        cab.cab_ejg_numero) in
       (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
          from CAJG_EJGREMESA e
         where idinstitucion = 2003
           and idremesa = 2105)
   and trunc(cab_fechaenvio) > '21/02/2018';


-- Ejecutados en Integracion por AAG el 05/03/2018 a las 18:35
-- 127_003:
