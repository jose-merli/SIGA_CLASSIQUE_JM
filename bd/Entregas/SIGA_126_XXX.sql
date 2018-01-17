Carpeta con script SIGA_126_002, aunque ahora es para la 126_003, porque hubo una entrega urgente anteriormente.

-- Ejecutados en Integracion por AAG el 17/07/2017 a las 11:35

UPDATE ECOM_CEN_EX_COLUMN C SET C.NORMALIZACION = 'DOÑA.\s|D\.\s|Dº\s|D\u00aa\s|\p{Punct}|^(\u00A0)|(\u00A0)$' WHERE C.NOM_COL = 'NOMBRE';
UPDATE ECOM_CEN_EX_COLUMN C SET C.NORMALIZACION = '[[\p{Punct}]&&[-]&&[.]]|^(\u00A0)|(\u00A0)$' WHERE C.NOM_COL IN ('APELLIDO1', 'APELLIDO2');

-- Ejecutados en Integracion por AAG el 17/07/2017 a las 15:35

-- SIGA_126_004
f_Siga_Asuntoasociado_MV

Update Gen_Recursos rec Set descripcion = 'Aplicado en Pago' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 1;
Update Gen_Recursos rec Set descripcion = 'Aplicat en Pagament' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 2;
Update Gen_Recursos rec Set descripcion = 'Aplicado en Pago#EU' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 3;
Update Gen_Recursos rec Set descripcion = 'Aplicado en Pago#GL' Where idrecurso = 'factSJCS.datosMovimientos.literal.pago' And rec.Idlenguaje = 4;


Incluir en la siguiente versión R1707_0027 y R1707_0029. Estas dos incidencias deben además ir acompañadas de subida de ecom.

insert into gen_parametros (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
SELECT P.MODULO, P.PARAMETRO, P.VALOR, SYSDATE, 0, 3004, P.IDRECURSO
FROM GEN_PARAMETROS P WHERE P.PARAMETRO LIKE 'KS_%' AND P.IDINSTITUCION = 2023;


alter table ECOM_CEN_DATOS_INCIDENCIAS add FECHAMODIFICACION date  null;
alter table ECOM_CEN_DATOS_INCIDENCIAS add USUMODIFICACION number(5)  null;

UPDATE Ecom_Cen_Datos_Incidencias DI SET DI.USUMODIFICACION = 0, DI.FECHAMODIFICACION = TO_DATE('1900/01/01', 'YYYY/MM/DD');

alter table ECOM_CEN_DATOS_INCIDENCIAS modify FECHAMODIFICACION not null;
alter table ECOM_CEN_DATOS_INCIDENCIAS modify USUMODIFICACION not null;

insert into ecom_cen_maestro_incidenc(idcensomaestroincidencias, descripcion, fechamodificacion)
values (8, 'ERROR_INTERNO', SYSDATE);


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ERROR_INTERNO', 'Error interno', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ERROR_INTERNO', 'Error interno#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ERROR_INTERNO', 'Error interno#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ERROR_INTERNO', 'Error interno#GL', 0, '4', sysdate, 0, '19');

-- Ejecutados en Integracion por AAG el 17/07/2017 a las 15:35

-- 126_005

f_Siga_Asuntoorigen_MV.fnc

Update Gen_Properties Pro Set Pro.Valor = '50'
 Where Parametro Like '%axBackupIndex'
 And parametro In ('log4j.appender.PRA.MaxBackupIndex', 'log4j.appender.STRUTS.MaxBackupIndex', 'log4j.appender.ficheroSOAP.maxBackupIndex');
Update Gen_Properties Pro Set Pro.Valor = '100'
 Where Parametro Like '%axBackupIndex'
 And parametro In ('log4j.appender.fichero.maxBackupIndex');

-- Ejecutados en Integracion por AAG el 11/08/2017 a las 10:30


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.inicioObligatorio', 'Es necesario rellenar fecha de Inicio', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.inicioObligatorio', 'És necessari omplir data d''inici', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.inicioObligatorio', 'Es necesario rellenar fecha de Inicio#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.inicioObligatorio', 'Es necesario rellenar fecha de Inicio#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.finObligatorio', 'Es necesario rellenar fecha de Fin', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.finObligatorio', 'És necessari omplir data de final', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.finObligatorio', 'Es necesario rellenar fecha de fin#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.comisiones.finObligatorio', 'Es necesario rellenar fecha de fin#GL', 0, '4', sysdate, 0, '19');

-- Ejecutados en Integracion por AAG el 14/08/2017 a las 09:40

-- SIGA_126_006

insert into ADM_CONTADOR (IDINSTITUCION, IDCONTADOR, NOMBRE, DESCRIPCION, MODIFICABLECONTADOR, MODO, CONTADOR, PREFIJO
, SUFIJO, LONGITUDCONTADOR, 
FECHARECONFIGURACION, RECONFIGURACIONCONTADOR, RECONFIGURACIONPREFIJO, RECONFIGURACIONSUFIJO
, IDTABLA, IDCAMPOCONTADOR, IDCAMPOPREFIJO, IDCAMPOSUFIJO
, IDMODULO, GENERAL, FECHAMODIFICACION
, USUMODIFICACION, FECHACREACION, USUCREACION) 
SELECT I.IDINSTITUCION, 'REINTEGROSXUNTA', 'ENVIO DE REINTEGROS A LA XUNTA'
, 'Contador para el envío de reintegros a la Xunta', '1', 0, 0, '2017', null, 5
, to_date('01-01-2018', 'dd-mm-yyyy'), '0', '2018', null
, 'CAJG_REMESARESOLUCION', 'NUMERO', 'PREFIJO', 'SUFIJO'
, 10, '0', SYSDATE, 0, SYSDATE, -1 
FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION IN (2025, 2044, 2064);

--Insertar una nueva línea en CAJG_TIPOREMESA 
insert into CAJG_TIPOREMESA (IDINSTITUCION, IDTIPOREMESA, NOMBRE, IDCONTADOR, FECHAMODIFICACION, USUMODIFICACION, JAVACLASS) 
SELECT I.IDINSTITUCION, 4, 'Envío de reintegros a la Xunta', 'REINTEGROSXUNTA', sysdate, 0, null--'com.siga.gratuita.pcajg.resoluciones.XuntaSolicitudEnvioReintegros'
FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION IN (2025, 2044, 2064);


--Creamos el proceso que utilizara SIGA
insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL) 
values ('12U', 'JGR', 1, 'Y', sysdate, 0, 'Envío reintegros Xunta', 'JGR_EnvioReintegrosXunta', '004', 10);

--Damos permiso al administrador general 

insert into adm_tiposacceso(idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion) 
SELECT '12U','ADG',sysdate,0,3,I.IDINSTITUCION
FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION IN (2025, 2044, 2064);

--Configuramos la opción de menú SJCS > e - Comunicaciones > Envío reintegros Xunta

 
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.envioReintegrosXunta', 'Envío reintegros Xunta', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.envioReintegrosXunta', 'Envío reintegros Xunta#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.envioReintegrosXunta', 'Envío reintegros Xunta#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.envioReintegrosXunta', 'Envío reintegros Xunta#GL', 0, '4', sysdate, 0, '19');


insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('12U', 22220, 160, '605', sysdate, 0, null, 'menu.sjcs.ecomunicaciones.envioReintegrosXunta', null, '12U', '1');


-- Ejecutados en Integracion por AAG el 17/08/2017 a las 14:15




=======
update gen_parametros p set p.valor = REPLACE(P.VALOR, 'angel.corral@redabogacia.org', 'extdes1@redabogacia.org') WHERE P.VALOR LIKE '%angel.corral@redabogacia.org%';



UPDATE SCS_PROCURADOR P SET P.IDCOLPROCURADOR = 'P50297' WHERE P.IDINSTITUCION = 2083 AND P.IDCOLPROCURADOR IS NULL;
UPDATE SCS_PROCURADOR P SET P.IDCOLPROCURADOR = 'P22125' WHERE P.IDINSTITUCION = 2034 AND P.IDCOLPROCURADOR IS NULL;
UPDATE SCS_PROCURADOR P SET P.IDCOLPROCURADOR = 'P44216' WHERE P.IDINSTITUCION = 2073 AND P.IDCOLPROCURADOR IS NULL;


ACTUALIZAR VISTA DE DESARROLLO (LOCAL) LLAMADA V_PCAJG_EJG

-- Ejecutados en Integracion por AAG el 11/09/2017 a las 14:15

--Informe iconómico de alcala

-- Create/Recreate indexes 
create index SI_403_SCS_EJG_ANIO on SCS_EJG (idinstitucion, anio);

insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL) 
values ('12Y', 'JGR', 1, 'Y', sysdate, 0, 'EJG informacion económica', 'JGR_E-Comunicaciones_InfEconomico', '007', 10);

--Damos permiso al administrador general de Alcalá a ese proceso

insert into adm_tiposacceso
   (idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion) 
 values
   ('12Y','ADG',sysdate,0,3,2003);
--Configuramos la opción de menú SJCS > e - Comunicaciones > EJGs: Remesas de información económica

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.InfEconomico', 'EJGs: Remesas informacion económica', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.InfEconomico', 'EJGs: Remesas informacion económica', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.InfEconomico', 'EJGs: Remesas informacion económica#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.ecomunicaciones.InfEconomico', 'EJGs: Remesas informacion económica#EU', 0, '3', sysdate, 0, '19');

insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('12Y', 22233, 160, '606', sysdate, 0, null, 'menu.sjcs.ecomunicaciones.InfEconomico', null, '12Y', '1');

-- Add/modify columns 
alter table CAJG_REMESA add IDTIPOREMESA number(1) default 0;


insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (59, 5, 'Validación para el envío de informe económico a la CAM', 1, '1', sysdate, 0);

insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (68, 1, 'Solicitar información completa expediente económico', 1, '1', sysdate, 0);


--CAJG canarias

insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (63, 1, 'Valida envío expedientes(EJIS)', 1, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (64, 1, 'Generacion xml Envío expedientes(EJIS)', 1, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (66, 1, 'Recibir acuses erróneos(Atlante)', 5, '1', sysdate, 0);
insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (67, 1, 'Recibir acuses no erróneos(Atlante)', 5, '1', sysdate, 0);

insert into GEN_RECURSOS (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
values ('messages.cajg.generacionXML', 'La generación del fichero xml se ha programado correctamente. Vuelva a consultar la remesa pasados unos minutos.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
values ('messages.cajg.generacionXML', 'La generación del fichero xml se ha programado correctamente. Vuelva a consultar la remesa pasados unos minutos.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
values ('messages.cajg.generacionXML', 'La generación del fichero xml se ha programado correctamente. Vuelva a consultar la remesa pasados unos minutos.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
values ('messages.cajg.generacionXML', 'La generación del fichero xml se ha programado correctamente. Vuelva a consultar la remesa pasados unos minutos.#GL', 0, '4', sysdate, 0, '19');

update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Tipo de PCAJG que usa el colegio: 
0	PCAJG no activo. 
1	PCAJG activo en modo TXT. 
2	PCAJG activo en modo FTP para colegios catalanes. 
3	PCAJG activo para colegios de Aragón. 
4	PCAJG activo en modo WebService para el envío de expedientes a Pamplona y Cantabria. Integración SIGA-Asigna o SIGA-Vereda. 
5	PCAJG activo envío expedientes para Alcalá de Henares. 
6 PCAJG activo para la integración con la Xunta de Galicia. 
7 PCAJG activo para la integración con el Gobierno Vasco. 
8 PCAJG activo para la integración con la Generalitat Valenciana. 
9 PCAJG activo para la Junta de Andalucía.
10 PCAJG activo para la integracion con Gobierno de Canarias' where idrecurso='scs.parametro.pcajg.tipo' and idlenguaje='1';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Tipo de PCAJG que usa el colegio: 
0	PCAJG no activo. 
1	PCAJG activo en modo TXT. 
2	PCAJG activo en modo FTP para colegios catalanes. 
3	PCAJG activo para colegios de Aragón. 
4	PCAJG activo en modo WebService para el envío de expedientes a Pamplona y Cantabria. Integración SIGA-Asigna o SIGA-Vereda. 
5	PCAJG activo envío expedientes para Alcalá de Henares. 
6 PCAJG activo para la integración con la Xunta de Galicia. 
7 PCAJG activo para la integración con el Gobierno Vasco. 
8 PCAJG activo para la integración con la Generalitat Valenciana. 
9 PCAJG activo para la Junta de Andalucía.
10 PCAJG activo para la integracion con Gobierno de Canarias#CA' where idrecurso='scs.parametro.pcajg.tipo' and idlenguaje='2';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Tipo de PCAJG que usa el colegio: 
0	PCAJG no activo. 
1	PCAJG activo en modo TXT. 
2	PCAJG activo en modo FTP para colegios catalanes. 
3	PCAJG activo para colegios de Aragón. 
4	PCAJG activo en modo WebService para el envío de expedientes a Pamplona y Cantabria. Integración SIGA-Asigna o SIGA-Vereda. 
5	PCAJG activo envío expedientes para Alcalá de Henares. 
6 PCAJG activo para la integración con la Xunta de Galicia. 
7 PCAJG activo para la integración con el Gobierno Vasco. 
8 PCAJG activo para la integración con la Generalitat Valenciana. 
9 PCAJG activo para la Junta de Andalucía.
10 PCAJG activo para la integracion con Gobierno de Canarias#EU' where idrecurso='scs.parametro.pcajg.tipo' and idlenguaje='3';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Tipo de PCAJG que usa el colegio: 
0	PCAJG no activo. 
1	PCAJG activo en modo TXT. 
2	PCAJG activo en modo FTP para colegios catalanes. 
3	PCAJG activo para colegios de Aragón. 
4	PCAJG activo en modo WebService para el envío de expedientes a Pamplona y Cantabria. Integración SIGA-Asigna o SIGA-Vereda. 
5	PCAJG activo envío expedientes para Alcalá de Henares. 
6 PCAJG activo para la integración con la Xunta de Galicia. 
7 PCAJG activo para la integración con el Gobierno Vasco. 
8 PCAJG activo para la integración con la Generalitat Valenciana. 
9 PCAJG activo para la Junta de Andalucía.
10 PCAJG activo para la integracion con Gobierno de Canarias#GL' where idrecurso='scs.parametro.pcajg.tipo' and idlenguaje='4';

-- Create/Recreate indexes 
drop index SI_PERSONAJG_NIF;
create index SI_PERSONAJG_NIF on SCS_PERSONAJG (NIF, IDINSTITUCION)
  tablespace TS_SIGA_SCS_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 52M
    next 1M
    minextents 1
    maxextents unlimited
  );

-- 126_007: Ejecutados en Integracion por AAG el 14/09/2017 a las 10:45
  
  -- 126_007: Ejecutados en Integracion por AAG el 14/09/2017 a las 10:45
  
  insert into GEN_PARAMETROS (MODULO, PARAMETRO, VALOR, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, IDRECURSO)
values ('ECOM', 'INFORMEECONOMICO_WS_URL', 'personalizar por intitucion', sysdate, 0, 0, 'ecom.parametro.pcajg.informeeconomico.UrlWs');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ecom.parametro.pcajg.informeeconomico.UrlWs', 'Url del servicio web de envío económico a la CAM', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ecom.parametro.pcajg.informeeconomico.UrlWs', 'Url del servicio web de envío económico a la CAM#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ecom.parametro.pcajg.informeeconomico.UrlWs', 'Url del servicio web de envío económico a la CAM#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('ecom.parametro.pcajg.informeeconomico.UrlWs', 'Url del servicio web de envío económico a la CAM#GL', 0, '4', sysdate, 0, '19');

--126_007: Ejecutados en Integracion por JTA el 14/09/2017 a las 14:07

insert into ADM_CONTADOR (IDINSTITUCION, IDCONTADOR, NOMBRE, DESCRIPCION, MODIFICABLECONTADOR, MODO, CONTADOR, PREFIJO, SUFIJO, LONGITUDCONTADOR, FECHARECONFIGURACION, RECONFIGURACIONCONTADOR, RECONFIGURACIONPREFIJO, RECONFIGURACIONSUFIJO, IDTABLA, IDCAMPOCONTADOR, IDCAMPOPREFIJO, IDCAMPOSUFIJO, IDMODULO, GENERAL, FECHAMODIFICACION, USUMODIFICACION, FECHACREACION, USUCREACION) values (2003, 'REMESAECONOMICA', 'REMESA DE INFORMACION ECÓNOMICA DE EJGS', 'Contador para las remesas de informacion economica de EJGs', '1', 0, 0, '2017', null, 5, to_date('01-01-2018', 'dd-mm-yyyy'), '0', '2018', null, 'CAJG_REMESA', 'NUMERO', 'PREFIJO', 'SUFIJO', 10, '0', SYSDATE, 0, SYSDATE, -1);

--126_007: Ejecutados en Integracion por JTA el 14/09/2017 a las 14:27
  
insert into gen_properties (fichero, parametro, valor) values ('SIGA', 'mail.smtp.actualizacioncenso.port', '25');
insert into gen_properties (fichero, parametro, valor) values ('SIGA', 'mail.smtp.port', '25');

--126_007: Ejecutados en Integracion por AAG el 18/09/2017 a las 12:42

insert into ECOM_OPERACION (IDOPERACION, IDSERVICIO, NOMBRE, MAXREINTENTOS, ACTIVO, FECHAMODIFICACION, USUMODIFICACION)
values (48, 5, 'Envío de informe económico a la CAM', 1, '1', sysdate, 0);

--126_007: Ejecutados en Integracion por JTA el 18/09/2017 a las 14:55

update cajg_ejgremesa set recibida = 0 where recibida = 3;
update cajg_ejgremesa set recibida = 1 where recibida = 2;
update cajg_ejgremesa set recibida = 1 where recibida = 4;
update cajg_ejgremesa set recibida = 1 where recibida = 5;
delete cajg_ejgremesaestado where idestado in (2,3,4,5);

--126_007: Ejecutados en Integracion por JTA el 19/09/2017 a las 11:40

--126_008

F_SIGA_GETDIRECCIONCLIENTE

Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (90, 'DIRECCION_FACTURACION', 'A', 'DIRECCION_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (91, 'DOMICILIO_FACTURACION', 'A', 'DOMICILIO_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (92, 'CODIGOPOSTAL_FACTURACION', 'A', 'CODIGOPOSTAL_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (93, 'POBLACION_FACTURACION', 'A', 'POBLACION_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (94, 'PROVINCIA_FACTURACION', 'A', 'PROVINCIA_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (95, 'TELEFONO_FACTURACION', 'A', 'TELEFONO_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (96, 'MOVIL_FACTURACION', 'A', 'MOVIL_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (97, 'F_FACTURACION', 'A', 'FAX_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);
Insert Into cer_camposcertificados (idcampocertificado, nombre, tipocampo, nombresalida, capturardatos, fechamodificacion, usumodificacion)
Values (98, 'EMAIL_FACTURACION', 'A', 'EMAIL_FACTURACION (o de otra si no hay de Facturación)', 'N', Sysdate, 0);

Declare
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  Update Gen_Catalogos_Multiidioma
     Set Migrado = 'N'
   Where Nombretabla = 'CER_CAMPOSCERTIFICADOS'; --Marcar la tabla como No traducida
  Proc_Act_Recursos(p_Codretorno, p_Datoserror);
  Dbms_Output.Put_Line(p_Codretorno || ': ' || p_Datoserror);
End;


Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.fichaCliente.facturacion.servicios.avisobaja', '¡DE BAJA! - ', '0', 1, Sysdate, 0, '15');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.fichaCliente.facturacion.servicios.avisobaja', 'DE BAIXA! - ', '0', 2, Sysdate, 0, '15');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.fichaCliente.facturacion.servicios.avisobaja', '¡DE BAJA! - #EU', '0', 3, Sysdate, 0, '15');
Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
Values  ('censo.fichaCliente.facturacion.servicios.avisobaja', '¡DE BAJA! - #GL', '0', 4, Sysdate, 0, '15');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cen.cargaWS.manual.activo', 'Parámetro que indica si está disponible el colegio para seleccionarlo en el combo para una sincronización manual del censo.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cen.cargaWS.manual.activo', 'Parámetro que indica si está disponible el colegio para seleccionarlo en el combo para una sincronización manual del censo.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cen.cargaWS.manual.activo', 'Parámetro que indica si está disponible el colegio para seleccionarlo en el combo para una sincronización manual del censo.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cen.cargaWS.manual.activo', 'Parámetro que indica si está disponible el colegio para seleccionarlo en el combo para una sincronización manual del censo.#GL', 0, '4', sysdate, 0, '19');


insert into gen_parametros (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
values ('CEN', 'CEN_WS_CARGA_MANUAL_ACTIVO', '0', sysdate, 0, 0, 'scs.parametro.cen.cargaWS.activo');
insert into gen_parametros (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
SELECT 'CEN', 'CEN_WS_CARGA_MANUAL_ACTIVO', '1', sysdate, 0, I.IDINSTITUCION, 'scs.parametro.cen.cargaWS.activo'
FROM CEN_INSTITUCION I WHERE I.IDINSTITUCION IN (2001,2012,2033,2037,2045,2046,2052,2056,2066,2080,2081);


Pkg_Siga_Retenciones_Sjcs

--126_008: Ejecutados en Integracion por AAG el 02/10/2017 a las 09:25

--126_009 

PKG_SIGA_FACTURACION_SJCS

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.parametro.censo.ws.horaEjecucion', 'Indica la hora de ejecución de la petición de censo por WebService al colegio. Debería ser posterior al parámetro CEN_WS_CARGA_DIA_HORA porque si no se ejecuta mañana.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.parametro.censo.ws.horaEjecucion', 'Indica la hora de ejecución de la petición de censo por WebService al colegio. Debería ser posterior al parámetro CEN_WS_CARGA_DIA_HORA porque si no se ejecuta mañana.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.parametro.censo.ws.horaEjecucion', 'Indica la hora de ejecución de la petición de censo por WebService al colegio. Debería ser posterior al parámetro CEN_WS_CARGA_DIA_HORA porque si no se ejecuta mañana.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.parametro.censo.ws.horaEjecucion', 'Indica la hora de ejecución de la petición de censo por WebService al colegio. Debería ser posterior al parámetro CEN_WS_CARGA_DIA_HORA porque si no se ejecuta mañana.#GL', 0, '4', sysdate, 0, '19');


insert into gen_parametros (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
values('CEN', 'CENSO_WS_HORA_PETICION', '22:05', SYSDATE, 0, 0, 'cen.parametro.censo.ws.horaEjecucion');
insert into gen_parametros (modulo, parametro, valor, fechamodificacion, usumodificacion, idinstitucion, idrecurso)
values('CEN', 'CENSO_WS_HORA_PETICION', '0:10', SYSDATE, 0, 2081, 'cen.parametro.censo.ws.horaEjecucion');

--126_009: Ejecutados en Integracion por AAG el 13/10/2017 a las 13:50

--126_010

Pkg_Siga_Censo


Drop materialized view V_CENSO_COLEGIACIONES;
Drop materialized view V_CENSO_LETRADOS_OOJJ;

V_CENSO_COLEGIACIONES;
V_CENSO_LETRADOS_OOJJ;

--126_010: Ejecutados en Integracion por AAG el 27/10/2017 a las 14:40


--126_011

Update Gen_Procesos pro Set pro.Transaccion = 'CEN_ModificarNumeroColegiado' Where pro.Idproceso = '12P';

--126_011: Ejecutados en Integracion por AAG el 13/11/2017 a las 13:00

update gen_procesos set descripcion = 'Solicitar Alta Turno' where idproceso = '9A1';

insert into adm_tiposacceso
  (idproceso,
   idperfil,
   fechamodificacion,
   usumodificacion,
   derechoacceso,
   idinstitucion)
  (select '9A1', idperfil, sysdate, 0, derechoacceso, idinstitucion
     from adm_tiposacceso padre
    where idproceso = '9S1'
      and not exists (select 1
             from adm_tiposacceso hijo
            where hijo.idproceso = '9A1'
              and hijo.idperfil = padre.idperfil
              and hijo.idinstitucion = padre.idinstitucion
           
           ));

update adm_tiposacceso hijo set hijo.derechoacceso  = (select padre.derechoacceso
             from adm_tiposacceso padre
            where padre.idproceso = '9S1'
              and hijo.idperfil = padre.idperfil
              and hijo.idinstitucion = padre.idinstitucion) where hijo.idproceso = '9A1' and (hijo.idinstitucion,hijo.idperfil) not in ((2014,'ABG'));

--126_012: Ejecutados en Integracion por AAG el 27/11/2017 a las 09:30

--eJECUTAR V_WS_2064_EJG

--126_013
PKG_SIGA_FACTURACION_SJCS

--126_013: Ejecutados en Integracion por AAG el 04/12/2017 a las 10:30

update scs_personajg set nombre = trim(nombre),apellido1 = trim(apellido1),apellido2 = trim(apellido2) where idinstitucion = &idinstitucion;

--126_014: Ejecutados en Integracion por AAG el 11/12/2017 a las 10:30

alter table SCS_GRUPOGUARDIACOLEGIADO_HIST
  drop constraint FK_SCS_GRUPOGUARDIA;
alter table SCS_GRUPOGUARDIACOLEGIADO_HIST
  drop constraint FK_SCS_GRUPOGUARDIACOLEGIADO;

--126_014: Ejecutados en Integracion por AAG el 11/12/2017 a las 12:30

  coger PKG_SIGA_FACTURACION 

  alter table ENV_LISTACORREOS modify nombre VARCHAR2(130);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.grupos.confirmar.listacorreo', '¿Desea crear una lista de correo cuyos destinatarios sean las personas que pertenezcan a este grupo fijo? Pulse ACEPTAR para crear el grupo fijo y la lista de correo y CANCELAR para crear únicamente el grupo fijo.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.grupos.confirmar.listacorreo', '¿Desea crear una lista de correo cuyos destinatarios sean las personas que pertenezcan a este grupo fijo? Pulse ACEPTAR para crear el grupo fijo y la lista de correo y CANCELAR para crear únicamente el grupo fijo.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.grupos.confirmar.listacorreo', '¿Desea crear una lista de correo cuyos destinatarios sean las personas que pertenezcan a este grupo fijo? Pulse ACEPTAR para crear el grupo fijo y la lista de correo y CANCELAR para crear únicamente el grupo fijo.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.grupos.confirmar.listacorreo', '¿Desea crear una lista de correo cuyos destinatarios sean las personas que pertenezcan a este grupo fijo? Pulse ACEPTAR para crear el grupo fijo y la lista de correo y CANCELAR para crear únicamente el grupo fijo.#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.listacorreo.descripcion.automatica', 'Lista de correo creada automáticamente al crear el grupo fijo', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.listacorreo.descripcion.automatica', 'Lista de correo creada automáticamente al crear el grupo fijo#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.listacorreo.descripcion.automatica', 'Lista de correo creada automáticamente al crear el grupo fijo#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.gestion.listacorreo.descripcion.automatica', 'Lista de correo creada automáticamente al crear el grupo fijo#GL', 0, '4', sysdate, 0, '19');

--126_015: Ejecutados en Integracion por AAG el 15/12/2017 a las 14:45

Pkg_Siga_Retenciones_Sjcs



--126_016: Ejecutados en Integracion por AAG el 05/01/2018 a las 09:45

Update Gen_Recursos rec Set descripcion = 'La fecha efectiva de baja debe ser igual o posterior a la fecha de fin del último periodo facturado de dicho servicio ({0}).', fechamodificacion = Sysdate, usumodificacion = 0 Where rec.Idrecurso = 'messages.Servicios.GestionSolicitudes.FechaEfectivaMenorFacturacion' And rec.Idlenguaje = 1;
Update Gen_Recursos rec Set descripcion = 'La data efectiva de baixa ha de ser igual o posterior a la data de fi de l''últim període facturat de dit servei ({0}).', fechamodificacion = Sysdate, usumodificacion = 0 Where rec.Idrecurso = 'messages.Servicios.GestionSolicitudes.FechaEfectivaMenorFacturacion' And rec.Idlenguaje = 2;
Update Gen_Recursos rec Set descripcion = 'La fecha efectiva de baja debe ser igual o posterior a la fecha de fin del último periodo facturado de dicho servicio ({0}).#EU', fechamodificacion = Sysdate, usumodificacion = 0 Where rec.Idrecurso = 'messages.Servicios.GestionSolicitudes.FechaEfectivaMenorFacturacion' And rec.Idlenguaje = 3;
Update Gen_Recursos rec Set descripcion = 'La fecha efectiva de baja debe ser igual o posterior a la fecha de fin del último periodo facturado de dicho servicio ({0}).#GL', fechamodificacion = Sysdate, usumodificacion = 0 Where rec.Idrecurso = 'messages.Servicios.GestionSolicitudes.FechaEfectivaMenorFacturacion' And rec.Idlenguaje = 4;

--126_016: Ejecutados en Integracion por AAG el 05/01/2018 a las 13:45

--126_017:

PKG_SIGA_FACTURACION_SJCS


insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL)
 values ('99J', 'JGR', 1, 'Y', sysdate, 0, 'Gestión de Tipos de Asistencia', 'JGR_TipoAsistenciaColegio', '004', 10);
 
-- carga de permisos para colegios
declare

cursor c_aux is
 select idinstitucion
from cen_institucion
Where idinstitucion between 2001 and 2099;

begin
 
 for rec in c_aux loop
  begin
   insert into adm_tiposacceso
   (idproceso, idperfil, fechamodificacion, usumodificacion, derechoacceso, idinstitucion) 
 values
   ('99J','ADG',sysdate,0,3,rec.idinstitucion);

 exception
  when others then
       dbms_output.put_line('Error='||sqlerrm);  
       rollback;
 end;   
 commit;
 end loop;

end;  
/

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.tiposAsistencia', 'Tipos de Asistencia', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.tiposAsistencia', 'Tipos de Asistencia#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.tiposAsistencia', 'Tipos de Asistencia#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.sjcs.tiposAsistencia', 'Tipos de Asistencia#EU', 0, '3', sysdate, 0, '19');


insert into GEN_MENU (IDMENU, ORDEN, TAGWIDTH, IDPARENT, FECHAMODIFICACION, USUMODIFICACION, URI_IMAGEN, IDRECURSO, GEN_MENU_IDMENU, IDPROCESO, IDLENGUAJE)
values ('99J', 21810, 160, '128', sysdate, 0, null, 'menu.sjcs.tiposAsistencia', null, '99J', '1');

-- Create table
create table SCS_TIPOASISTENCIAGUARDIA
(
  idtipoguardia           NUMBER(2) not null,
  idinstitucion           NUMBER(4) not null,
  idtipoasistenciacolegio NUMBER(3) not null,
  fechamodificacion       DATE not null,
  usumodificacion         NUMBER(5) not null
)
tablespace TS_SIGA_SCS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16K
    next 8K
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCS_TIPOASISTENCIAGUARDIA
  add constraint PK_SCS_TIPOAISTENCIAGUARDIA primary key (IDTIPOGUARDIA, IDINSTITUCION, IDTIPOASISTENCIACOLEGIO)
  using index 
  tablespace TS_SIGA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table SCS_TIPOASISTENCIAGUARDIA
  add constraint FK_SCSTIPOASISTENCIA foreign key (IDINSTITUCION, IDTIPOASISTENCIACOLEGIO)
  references SCS_TIPOASISTENCIACOLEGIO (IDINSTITUCION, IDTIPOASISTENCIACOLEGIO);
alter table SCS_TIPOASISTENCIAGUARDIA
  add constraint FK_SCSTIPOGUARDIAS foreign key (IDTIPOGUARDIA)
  references SCS_TIPOSGUARDIAS (IDTIPOGUARDIA);

-- carga de guardias para tipos de  aistencia colegio
declare

cursor c_aux is
 select tg.idtipoguardia,
       tac.idinstitucion,
       tac.idtipoasistenciacolegio
  from scs_tipoasistenciacolegio tac,
       scs_tiposguardias         tg,
       cen_institucion           i
 where tac.idinstitucion = i.idinstitucion
   and i.idinstitucion between 2001 and 2099;

begin
 
 for rec in c_aux loop
  begin
  insert into scs_tipoasistenciaguardia
  (idtipoguardia, idinstitucion, idtipoasistenciacolegio, fechamodificacion, usumodificacion)
values
  (rec.idtipoguardia, rec.idinstitucion, rec.idtipoasistenciacolegio, sysdate, 0) ;
  


 exception
  when others then
       dbms_output.put_line('Error='||sqlerrm);  
       rollback;
 end;   
 commit;
 end loop;

end;  
/

