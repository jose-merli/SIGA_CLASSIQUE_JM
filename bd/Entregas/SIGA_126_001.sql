
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad psíquica víctima de abuso o maltrato', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad psíquica víctima de abuso o maltrato#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad psíquica víctima de abuso o maltrato#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
values ('gratuita.personaJG.literal.personaConDiscapacidad', 'Persona con discapacidad psíquica víctima de abuso o maltrato#EU', 0, '3', sysdate, 0, '19');

alter table SCS_CARACTASISTENCIA add PERSONACONDISCAPACIDAD VARCHAR2(1) DEFAULT 0;


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', 'Último acceso', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', 'Último acceso#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', 'Último acceso#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
       values ('general.usuario.literal.ultimoAcceso', 'Último acceso#GL', 0, '4', sysdate, 0, '19');

       
       
insert into gen_procesos 
(IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION,TRANSACCION,IDPARENT,NIVEL) 
            values ('84V','GEN',1,'Y',sysdate,0,'HIDDEN_InformacionUsuario','GEN_InformacionUsuario','0',10);
            
            
alter table adm_certificados add email VARCHAR2(320);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) 
 values ('messages.general.errorEmailCertificado', 'El email del certificado no se corresponde con el almacenado en el sistema#EU', 0, '3', sysdate, 0, '19');
 
 
 INSERT INTO Cer_Camposcertificados (IDCAMPOCERTIFICADO, NOMBRE,TIPOCAMPO,NOMBRESALIDA,CAPTURARDATOS,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (89,'FECHA_ESTADO_COLEGIAL','F','FECHA_ESTADO_COLEGIAL','N',SYSDATE,0);

update GEN_CATALOGOS_MULTIIDIOMA set migrado = 'N' where nombretabla = 'CER_CAMPOSCERTIFICADOS';

declare
    p_codretorno varchar2(4000);
      p_datoserror varchar2(4000);
   begin
     proc_act_recursos(p_codretorno, p_datoserror);
     dbms_output.put_line(p_codretorno || ': ' || p_datoserror);
   end;
   
   
   alter table scs_personajg add IDPAISDIR VARCHAR2(3);
   alter table scs_personajg add POBLACIONEXTRANJERA VARCHAR2(100);
   
   
--añadimos la columna a la tabla DESIGNAS
 alter table scs_actuaciondesigna add IDMOVIMIENTO number(10); 
--creamos la FK

alter table scs_actuaciondesigna
  add constraint FK_ACTUACIONDESIG_MOVIMIENTOS foreign key (IDINSTITUCION, IDMOVIMIENTO)
  references fcs_movimientosvarios (IDINSTITUCION, IDMOVIMIENTO);
  
  
--añadimos la columna a la tabla ACTUACIONES DE UNA ASISTENCIA
 alter table SCS_ACTUACIONASISTENCIA add IDMOVIMIENTO number(10); 
--creamos la FK

alter table SCS_ACTUACIONASISTENCIA
  add constraint FK_ACTUACIONASIS_MOVIMIENTOS foreign key (IDINSTITUCION, IDMOVIMIENTO)
  references fcs_movimientosvarios (IDINSTITUCION, IDMOVIMIENTO);  
  
  
  --añadimos la columna a la tabla ASISTENCIA
 alter table SCS_ASISTENCIA add IDMOVIMIENTO number(10); 
--creamos la FK

alter table SCS_ASISTENCIA
  add constraint FK_ASISTENCIA_MOVIMIENTOS foreign key (IDINSTITUCION, IDMOVIMIENTO)
  references fcs_movimientosvarios (IDINSTITUCION, IDMOVIMIENTO);  
  
  
  
--añadimos la columna a la tabla GUARDIAS
 alter table scs_cabeceraguardias add IDMOVIMIENTO number(10); 
--creamos la FK

alter table scs_cabeceraguardias
  add constraint FK_GUARDIAS_MOVIMIENTOS foreign key (IDINSTITUCION, IDMOVIMIENTO)
  references fcs_movimientosvarios (IDINSTITUCION, IDMOVIMIENTO);  
  
  
f_Siga_Asuntoasociado_MV


create index FK_SCS_ACTUACIONDESIGNA_MOV on SCS_ACTUACIONDESIGNA (IDINSTITUCION,IDMOVIMIENTO)
  tablespace TS_SIGA_SCS_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 59M
    next 1M
    minextents 1
    maxextents unlimited
  );
  
  
  create index FK_SCS_ACTUACIONASISTENCIA_MOV on SCS_ACTUACIONASISTENCIA (IDINSTITUCION, IDMOVIMIENTO)
  tablespace TS_SIGA_SCS_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 37M
    next 1M
    minextents 1
    maxextents unlimited
  );

  
  create index FK_ASISTENCIA_MOV on SCS_ASISTENCIA (IDINSTITUCION, IDMOVIMIENTO)
  tablespace TS_SIGA_SCS_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 43M
    next 1M
    minextents 1
    maxextents unlimited
  );
  
  create index FK_CABECERAGUARDIAS_MOV on scs_cabeceraguardias (IDINSTITUCION, IDMOVIMIENTO)
  tablespace TS_SIGA_SCS_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 43M
    next 1M
    minextents 1
    maxextents unlimited
  );

  
 Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionDesigna.titulo', 'Actuación de designación', '0', 1, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionDesigna.titulo', 'Actuación de designación#CA', '0', 2, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionDesigna.titulo', 'Actuación de designación#EU', '0', 3, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionDesigna.titulo', 'Actuación de designación#GL', '0', 4, Sysdate, 0, '19');
  
  
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionAsistencias.titulo', 'Actuación de asistencia', '0', 1, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionAsistencias.titulo', 'Actuación de asistencia#CA', '0', 2, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionAsistencias.titulo', 'Actuación de asistencia#EU', '0', 3, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.ActuacionAsistencias.titulo', 'Actuación de asistencia#GL', '0', 4, Sysdate, 0, '19');
  
  
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.asistencia.titulo', 'Asistencia', '0', 1, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.asistencia.titulo', 'Asistencia#CA', '0', 2, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.asistencia.titulo', 'Asistencia#EU', '0', 3, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.asistencia.titulo', 'Asistencia#GL', '0', 4, Sysdate, 0, '19');
  
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.guardia.titulo', 'Guardia', '0', 1, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.guardia.titulo', 'Guardia#CA', '0', 2, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.guardia.titulo', 'Guardia#EU', '0', 3, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.guardia.titulo', 'Guardia#GL', '0', 4, Sysdate, 0, '19');
  
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.movimientosVarios.titulo', 'Movimientos varios', '0', 1, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.movimientosVarios.titulo', 'Movimientos varios#CA', '0', 2, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.movimientosVarios.titulo', 'Movimientos varios#EU', '0', 3, Sysdate, 0, '19');
  Insert Into gen_recursos  (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad)
  Values  ('movimientosVarios.movimientosVarios.titulo', 'Movimientos varios#GL', '0', 4, Sysdate, 0, '19');



alter table FCS_FACT_ACTUACIONDESIGNA modify PORCENTAJEFACTURADO NUMBER(5,2);
alter table SCS_ACREDITACIONPROCEDIMIENTO modify PORCENTAJE NUMBER(5,2);
alter table FCS_HISTO_ACREDITACIONPROC modify PORCENTAJE NUMBER(5,2);

alter table FCS_FACT_ACTUACIONDESIGNA add IMPORTEFACTURADO NUMBER(10,2);
Update Fcs_Fact_Actuaciondesigna Fac Set Fac.Importefacturado = Round(Fac.Precioaplicado * Fac.Porcentajefacturado / 100, 2);
alter table FCS_FACT_ACTUACIONDESIGNA modify IMPORTEFACTURADO not null;

Delete From Adm_Tiposacceso tip Where tip.Idproceso = ‘602’;
Delete From Gen_Procesos pro Where pro.Idproceso = ‘602’; /*Transaccion = ‘FCS_DetalleFacturacion’*/

Drop Function f_siga_totalimporteacredit;

PKG_SIGA_FACTURACION_SJCS
PKG_SIGA_REGULARIZACION_SJCS
PKG_SIGA_PAGOS_SJCS
V_SIGA_EXCEL_FACJG_TURNOS
V_SIGA_SJCS_FACTURACION
V_WS_JE_2064_DESIGNA
V_WS_JE_2064_DESIGNAPROCURADOR


INSERT INTO gen_procesos
  (idproceso, idmodulo, traza, target, fechamodificacion, usumodificacion, descripcion, transaccion, idparent, nivel)
VALUES
  ('12P', 'CEN', 1, 'Y', SYSDATE, 0, 'Modificar número de colegiado', 'CEN_DatosColegiales', '121', 10);
  
  
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('message.cambioNumColegiado.regtel', 'Ha ocurrido un error al modificar el título de la colección delcolegiado en Regtel', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('message.cambioNumColegiado.regtel', 'Ha ocurrido un error al modificar el título de la colección delcolegiado en Regtel#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('message.cambioNumColegiado.regtel', 'Ha ocurrido un error al modificar el título de la colección delcolegiado en Regtel#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('message.cambioNumColegiado.regtel', 'Ha ocurrido un error al modificar el título de la colección delcolegiado en Regtel#EU', 0, '3', sysdate, 0, '19');=======

Pkg_Siga_Retenciones_Sjcs

Update Gen_Properties Pro Set Pro.Valor = '400'
 Where Parametro Like '%axBackupIndex';
Update Gen_Properties Pro Set Pro.Valor = '100MB'
 Where Parametro Like '%axFileSize';

-- Ejecutado en Integracion por AAG el 21/06 14:30

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.cambioNumColegiado.regtel', 'Cambios realizados correctamente. No se ha podido modificar el título de la colección en Regtel', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.cambioNumColegiado.regtel', 'Cambios realizados correctamente. No se ha podido modificar el título de la colección en Regtel#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.cambioNumColegiado.regtel', 'Cambios realizados correctamente. No se ha podido modificar el título de la colección en Regtel#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('error.cambioNumColegiado.regtel', 'Cambios realizados correctamente. No se ha podido modificar el título de la colección en Regtel#EU', 0, '3', sysdate, 0, '19');

Cambiado PKG_SIGA_RETENCIONES_SJCS 


