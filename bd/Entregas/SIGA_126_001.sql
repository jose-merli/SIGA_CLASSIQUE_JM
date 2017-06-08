
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