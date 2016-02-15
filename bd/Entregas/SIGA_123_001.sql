-- Add comments to the columns 
comment on column SCS_ASISTENCIA.IDORIGENASISTENCIA
  is '10 ICA SIGA 20 Colegiado SIGA  30 Volante expres m�vil 40 Centralita de Guardias';

  -- Add/modify columns 
alter table SCS_ACTUACIONDESIGNA add ANIOPROCEDIMIENTO NUMBER(4);

UPDATE scs_actuaciondesigna SET anioprocedimiento = anio where idinstitucion = 2014 and numeroprocedimiento is not null;
  
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones#EU', 0, '3', sysdate, 0, '19');
 
ALTER TABLE CEN_HISTORICO
ADD OBSERVACIONES VARCHAR2(1000);

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna#EU', 0, '3', sysdate, 0, '19');
 
ALTER TABLE CEN_CUENTASBANCARIAS
MODIFY ABONOCARGO VARCHAR2(1) DEFAULT 'C' NULL;

ALTER TABLE CEN_SOLICMODICUENTAS
MODIFY ABONOCARGO VARCHAR2(1) DEFAULT 'C' NULL;