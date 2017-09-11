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

update gen_parametros p set p.valor = REPLACE(P.VALOR, 'angel.corral@redabogacia.org', 'extdes1@redabogacia.org') WHERE P.VALOR LIKE '%angel.corral@redabogacia.org%';

UPDATE SCS_PROCURADOR P SET P.IDCOLPROCURADOR = 'P50297' WHERE P.IDINSTITUCION = 2083 AND P.IDCOLPROCURADOR IS NULL;
UPDATE SCS_PROCURADOR P SET P.IDCOLPROCURADOR = 'P22125' WHERE P.IDINSTITUCION = 2034 AND P.IDCOLPROCURADOR IS NULL;
UPDATE SCS_PROCURADOR P SET P.IDCOLPROCURADOR = 'P44216' WHERE P.IDINSTITUCION = 2073 AND P.IDCOLPROCURADOR IS NULL;


ACTUALIZAR VISTA DE DESARROLLO (LOCAL) LLAMADA V_PCAJG_EJG

-- Ejecutados en Integracion por AAG el 11/09/2017 a las 14:15

