-- INI 123_003bis

Cambios en V_CENSO_LETRADOS, V_CENSO_COLEGIACIONES y V_CENSO_LETRADOS_OOJJ

-- Los ejecutara Azucena fuera de horario laboral en los 4 entornos.


--administracion de permisos

INSERT INTO GEN_PROCESOS (IDPROCESO,IDMODULO,TRAZA,TARGET,FECHAMODIFICACION,USUMODIFICACION,DESCRIPCION,TRANSACCION,IDPARENT,NIVEL )  VALUES  ('172', 'CEN', 1, 'Y', SYSDATE, 0,'Configuracion de perfil de colegio','CEN_ConfigPerfilColegio',161,10 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('172','ADG',SYSDATE, 0,3,2000 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('172','DCA',SYSDATE, 0,3,2000 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('172','FC',SYSDATE, 0,0,2000 );

INSERT INTO GEN_PROCESOS (IDPROCESO,IDMODULO,TRAZA,TARGET,FECHAMODIFICACION,USUMODIFICACION,DESCRIPCION,TRANSACCION,IDPARENT,NIVEL )  VALUES  ('173', 'CEN', 1, 'Y', SYSDATE, 0,'Nueva columna para perfil de colegio','CEN_NuevaColumnaPerfil',161,10 );

INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('173','ADG',SYSDATE, 0,3,2000 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('173','DCA',SYSDATE, 0,3,2000 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('173','FC',SYSDATE, 0,0,2000 );



-- creacion de secuencia para ecom_cen_ex_perfil

create sequence SEQ_ECOM_CEN_EX_PERFIL start with 356;

-- inserts para etiquetas (modificar los cambios para el tema de los menus antes de la subida)

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.nombrecolumna', 'Nombre de Columna', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.nombrecolumna', 'Nombre de Columna#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.nombrecolumna', 'Nombre de Columna#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.nombrecolumna', 'Nombre de Columna#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.tipocolumna', 'Tipo de Columna', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.tipocolumna', 'Tipo de Columna#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.tipocolumna', 'Tipo de Columna#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.tipocolumna', 'Tipo de Columna#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.titulo.dialog', 'Nueva columna para perfil', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.titulo.dialog', 'Nueva columna para perfil#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.titulo.dialog', 'Nueva columna para perfil#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.literal.titulo.dialog', 'Nueva columna para perfil#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.colegio.titulo', 'Gestión de Perfiles', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.colegio.titulo', 'Gestión de Perfiles#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.colegio.titulo', 'Gestión de Perfiles#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.perfil.colegio.titulo', 'Gestión de Perfiles#EU', 0, '3', sysdate, 0, '19');
 
  insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.configp', 'Configurar Perfil', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.configp', 'Configurar Perfil#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.configp', 'Configurar Perfil#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.configp', 'Configurar Perfil#EU', 0, '3', sysdate, 0, '19');
 
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.censo.gestionCensoWS.mantenimiento.cargas', 'Mantenimiento de cargas', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.censo.gestionCensoWS.mantenimiento.cargas', 'Mantenimiento de cargas#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.censo.gestionCensoWS.mantenimiento.cargas', 'Mantenimiento de cargas#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('menu.censo.gestionCensoWS.mantenimiento.cargas', 'Mantenimiento de cargas#EU', 0, '3', sysdate, 0, '19');


 insert into GEN_MENU (IDMENU,ORDEN,TAGWIDTH,IDPARENT,FECHAMODIFICACION,USUMODIFICACION,URI_IMAGEN,IDRECURSO,GEN_MENU_IDMENU,IDPROCESO,IDLENGUAJE,MIGRADO,URL_ACCESO) 
 values ('160','16000','180','0',sysdate,'0',null,'menu.censo.gestionCensoWS.mantenimiento.cargas',null,'160','1','0',null);

 insert into GEN_MENU (IDMENU,ORDEN,TAGWIDTH,IDPARENT,FECHAMODIFICACION,USUMODIFICACION,URI_IMAGEN,IDRECURSO,GEN_MENU_IDMENU,IDPROCESO,IDLENGUAJE,MIGRADO,URL_ACCESO) 
 values ('172','17200','160','160',sysdate,'0',null,'general.configp',null,'172','1','0',null);

 
 insert into GEN_PROCESOS (IDPROCESO,IDMODULO,TRAZA,TARGET,FECHAMODIFICACION,USUMODIFICACION,DESCRIPCION,TRANSACCION,IDPARENT,NIVEL) 
 values ('160','CEN','1','Y',SYSDATE,'0','Mantenimiento de cargas',null,'0','10');

UPDATE GEN_MENU SET idparent =160 WHERE IDMENU  = '161' AND IDPROCESO  = '161';



INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('160','ADG',SYSDATE, 0,3,2000 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('160','DCA',SYSDATE, 0,3,2000 );
INSERT INTO ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION ) VALUES ('160','FC',SYSDATE, 0,0,2000 );


 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.literal.aviso.carga.webservice', 'Para evitar posibles errores en la carga, compruebe el colegio seleccionado.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.literal.aviso.carga.webservice', 'Para evitar posibles errores en la carga, compruebe el colegio seleccionado.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.literal.aviso.carga.webservice', 'Para evitar posibles errores en la carga, compruebe el colegio seleccionado.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.literal.aviso.carga.webservice', 'Para evitar posibles errores en la carga, compruebe el colegio seleccionado.#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.actualizar', 'Actualiza', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.actualizar', 'Actualiza#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.actualizar', 'Actualiza#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.actualizar', 'Actualiza#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.dialogo.nuevo.carga.webservice', 'ActualizaciÃ³n de webservice', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.dialogo.nuevo.carga.webservice', 'ActualizaciÃ³n de webservice#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.dialogo.nuevo.carga.webservice', 'ActualizaciÃ³n de webservice#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.dialogo.nuevo.carga.webservice', 'ActualizaciÃ³n de webservice#EU', 0, '3', sysdate, 0, '19');


 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.processed.success.deleteRemesa', 'El proceso de eliminación de remesa está en curso. Este proceso tardará unos instantes en terminar', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.processed.success.deleteRemesa', 'El proceso de eliminación de remesa está en curso. Este proceso tardará unos instantes en terminar#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.processed.success.deleteRemesa', 'El proceso de eliminación de remesa está en curso. Este proceso tardará unos instantes en terminar#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.processed.success.deleteRemesa', 'El proceso de eliminación de remesa está en curso. Este proceso tardará unos instantes en terminar#EU', 0, '3', sysdate, 0, '19');


 INSERT INTO ECOM_CEN_MAESTRO_ESTAD_ENVIO  (IDESTADOENVIO, DESCRIPCION,FECHAMODIFICACION)  VALUES  (6,'ELIMINANDO',SYSDATE);
 
 
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.estadoEnvio.ELIMINANDO', 'Eliminando', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.estadoEnvio.ELIMINANDO', 'Eliminando#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.estadoEnvio.ELIMINANDO', 'Eliminando#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.ws.estadoEnvio.ELIMINANDO', 'Eliminando#EU', 0, '3', sysdate, 0, '19');

