#drop database if exists DBTonysKinal2019087;
Create database DBTonysKinal2019087;

Use DBTonysKinal2019087;

create table Empresas(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(10) not null,
    primary key PK_codigoEmpresa (codigoEmpresa) 
);

create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
	descripcion varchar(100) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
	descripcion varchar(150) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto(codigoPresupuesto),
    constraint FK_Presupuesto_Empresa
		foreign key(codigoEmpresa) references Empresas(codigoEmpresa) 
);

create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(100) not null,
    horaServicio time not null,
    lugarServicio varchar(100) not null,
    telefonoContacto varchar(10) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio(codigoServicio),
    constraint FK_Servicios_Empresas
		foreign key(codigoEmpresa) references Empresas(codigoEmpresa) 
);

create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150)not null,
    telefonoContacto varchar(10) not null,
    gradoCocinero varchar(50) not null,
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado(codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado
		foreign key(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null auto_increment,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios
		foreign key(codigoServicio) references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados
		foreign key(codigoEmpleado) references Empleados(codigoEmpleado) 
);


create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato(codigoPlato),
    constraint FK_Platos_TipoPlato
		foreign key(codigoTipoPlato) references TipoPlato(codigoTipoPlato) 
);


create table Servicios_has_Platos(
    codigoServicio int not null,
    codigoPlato int not null,
    constraint FK_Servicios_has_Platos_Servicios
		foreign key(codigoServicio) references Servicios(codigoServicio) ,
	constraint FK_Servicios_has_Platos_Platos
		foreign key(codigoPlato) references Platos(codigoPlato) 
);


create table Productos_has_Platos(
    codigoProducto int not null,
    codigoPlato int not null,
    constraint FK_Productos_has_Platos_Productos
		foreign key(codigoProducto) references Productos(codigoProducto),
	constraint FK_Productos_has_Platos_Platos
		foreign key(codigoPlato) references Platos(codigoPlato) 
);

#**************************************************************************************************************************************************************************#
#Procedimieto almacenado Agregar 

delimiter $$
	 create procedure sp_AgregarEmpresa( in nombreEmpresa varchar(150),
	 in direccion varchar(150) , in telefono varchar(10))
		begin
			insert into Empresas( nombreEmpresa, direccion, telefono) 
			values	( nombreEmpresa, direccion, telefono);
		end $$
delimiter ;
call sp_AgregarEmpresa ("Alimentos Nuticionales De C.A S.A "," SAN JOSE VILLA NUEVA GUATEMALA, Villa Nueva","24396933");
call sp_AgregarEmpresa ("Torta del chavo","Mixco","55384679");
call sp_AgregarEmpresa ("Comedor doña Chonita ","Santa Rosa","47822343");
call sp_AgregarEmpresa ("Comedor Lucia","San Lucas","25987465");
call sp_AgregarEmpresa ("Funda Juanita","Antigua Guatemala","35478921");
call sp_AgregarEmpresa ("Tacos Pedro","Puerto Barrios","45861098");
call sp_AgregarEmpresa ("Hamburguesas Wicho","San Martin","32574960");

#Listar Empresas
delimiter $$
	 create procedure sp_ListarEmpresas()
		begin
			select codigoEmpresa, nombreEmpresa, direccion, telefono from Empresas;
		end $$
delimiter ;
call sp_ListarEmpresas();

#Actualizar Empresas
delimiter $$
	 create procedure sp_ActualizarEmpresa (in ce int,in ne varchar(150),in d varchar(150),in t varchar(10))
		begin
			update Empresas set nombreEmpresa=ne, direccion=d, telefono=t where codigoEmpresa=ce;
		end $$
delimiter ;

#Buscar Empresas
delimiter $$
	 create procedure sp_BuscarEmpresa (ce int)
		begin
			select * from Empresas where codigoEmpresa=ce;
		end $$
delimiter ;
call sp_BuscarEmpresa (2);

#Eliminar Empresas
delimiter $$
	 create procedure sp_EliminarEmpresa(ce int)
		begin
			delete from Empresas where codigoEmpresa=ce;
		end $$
delimiter ;
call sp_EliminarEmpresa (1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados Productos
delimiter $$
	 create procedure sp_AgregarProductos(in nombreProducto varchar(150),in cantidad int)
		begin
			insert into Productos(nombreProducto,cantidad) 
			values(nombreProducto,cantidad);
		end $$
delimiter ;
call sp_AgregarProductos ("Carnes",200);
call sp_AgregarProductos ("Frutas",201);
#Listar Producto
delimiter $$
	 create procedure sp_ListarProductos()
		begin
			select codigoProducto,nombreProducto,cantidad from Productos;
		end $$
delimiter ;
call sp_ListarProductos;

#Actualizar Producto
delimiter $$
	 create procedure sp_ActualizarProducto(cpr int, npr varchar(150), c int)
		begin
			update Productos set  nombreProducto=npr , cantidad=c where codigoProducto=cpr;
		end $$
delimiter ;
call sp_ActualizarProducto(1,"Especies",250);

#Buscar Producto
delimiter $$
	 create procedure sp_BuscarProducto(cpr int)
		begin
			select * from Productos where codigoProducto=cpr;
		end $$
delimiter ;
call sp_BuscarProducto(1);

#Eliminar Producto
delimiter $$
	 create procedure sp_EliminarProducto(cpr int)
		begin
			delete from Productos where codigoProducto=cpr;
		end $$
delimiter ;
call sp_EliminarProducto(1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados TipoEmpleado
delimiter $$
	 create procedure sp_AgregarTipoEmpleado( in descripcion varchar(100))
		begin
			insert into TipoEmpleado(descripcion)
			values(descripcion);
		end $$
delimiter ;
call sp_AgregarTipoEmpleado ("Encargado de elaborar la comida especial del restaurante");
call sp_AgregarTipoEmpleado ("Encargado de elaborar la comida tradicional del restaurante");
#Listar TipoEmpleado
delimiter $$
	 create procedure sp_ListarTipoEmpleado()
		begin
			select codigoTipoEmpleado, descripcion  from TipoEmpleado;
		end $$
delimiter ;
call sp_ListarTipoEmpleado;

#Actualizar TipoEmpleado
delimiter $$
	 create procedure sp_ActualizarTipoEmpleado(cte int, d varchar(100))
		begin
			update TipoEmpleado set  descripcion=d where codigoTipoEmpleado=cte;
		end $$
delimiter ;
call sp_ActualizarTipoEmpleado(1,"Encargado de realizar la mejor comida de la ciudad");

#Buscar TipoEmpleado
delimiter $$
	 create procedure sp_BuscarTipoEmpleado(cte int)
		begin
			select * from TipoEmpleado where codigoTipoEmpleado=cte;
		end $$
delimiter ;
call sp_BuscarTipoEmpleado(1);

#Eliminar TipoEmpleado
delimiter $$
	 create procedure sp_EliminarTipoEmpleado(cte int)
		begin
			delete from TipoEmpleado where codigoTipoEmpleado=cte;
		end $$
delimiter ;
call sp_EliminarTipoEmpleado(1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados TipoPlato
delimiter $$
	 create procedure sp_AgregarTipoPlato( in descripcion varchar(150))
		begin
			insert into TipoPlato(descripcion)
			values(descripcion);
		end $$
delimiter ;
call sp_AgregarTipoPlato ("La comida Italiana se caracteriza por sus elaboraciones con abundantes verduras, frutas, carnes, pescados, arroz, pastas y panes");
call sp_AgregarTipoPlato ("La comida chapina se caracteriza por sus elaboraciones con abundante uso del maiz y frijol");


#Listar TipoPlato
delimiter $$
	 create procedure sp_ListarTipoPlato()
		begin
			select codigoTipoPlato,descripcion from TipoPlato;
		end $$
delimiter ;
call sp_ListarTipoPlato;

#Actualizar TipoPlato
delimiter $$
	 create procedure sp_ActualizarTipoPlato(ctp int, d varchar(150))
		begin
			update TipoPlato set  descripcion=d where codigoTipoPlato=ctp;
		end $$
delimiter ;
call sp_ActualizarTipoPlato(1,"La comida Guatemalteca se caracteriza por la fusión de dos grandes culturas: ubicación geográfica y topográfica");

#Buscar TipoPlato
delimiter $$
	 create procedure sp_BuscarTipoPlato(ctp int)
		begin
			select * from TipoPlato where codigoTipoPlato=ctp;
		end $$
delimiter ;
call sp_BuscarTipoPlato(1);

#Eliminar TipoEmpleado
delimiter $$
	 create procedure sp_EliminarTipoPlato(ctp int)
		begin
			delete from TipoPlato where codigoTipoPlato=ctp;
		end $$
delimiter ;
call sp_EliminarTipoPlato(1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados Presupuesto
delimiter $$
	 create procedure sp_AgregarPresupuesto( in fechaSolicitud date, in cantidadPresupuesto decimal(10,2),
     in codigoEmpresa int)
		begin
			insert into Presupuesto(fechaSolicitud,cantidadPresupuesto,codigoEmpresa)
			values(fechaSolicitud,cantidadPresupuesto,codigoEmpresa);
		end $$
delimiter ;
call sp_AgregarPresupuesto("2019-08-12",4000.00,2);
call sp_AgregarPresupuesto("2018-08-12",1000.00,3);



#Listar Presupuesto
delimiter $$
	 create procedure sp_ListarPresupuesto()
		begin
			select codigoPresupuesto,fechaSolicitud,cantidadPresupuesto,codigoEmpresa from Presupuesto;
		end $$
delimiter ;
call sp_ListarPresupuesto;


#Actualizar Presupuesto
delimiter $$
	 create procedure sp_ActualizarPresupuesto(cpr int,fs date, cap decimal(10,2),
     ce int)
		begin
			update Presupuesto set fechaSolicitud=fs,cantidadPresupuesto=cap,codigoEmpresa=ce where codigoPresupuesto=cpr;
		end $$
delimiter ;
call sp_ActualizarPresupuesto(1,"2019-06-12",2000.00,2);

#Buscar Presupuesto
delimiter $$
	 create procedure sp_BuscarPresupuesto(cpr int)
		begin
			select * from Presupuesto where codigoPresupuesto=cpr;
		end $$
delimiter ;
call sp_BuscarPresupuesto(1);

#Eliminar Presupuesto
delimiter $$
	 create procedure sp_EliminarPresupuesto(cpr int)
		begin
			delete from Presupuesto where codigoPresupuesto=cpr;
		end $$
delimiter ;
call sp_EliminarPresupuesto(1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados Servicios
delimiter $$
	 create procedure sp_AgregarServicio(in fechaServicio date ,in tipoServicio varchar(100),
	in horaServicio time,in lugarServicio varchar(100) ,in telefonoContacto varchar(10) ,in codigoEmpresa int)
		begin
			insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
			values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
		end $$
delimiter ;
call sp_AgregarServicio("2020-02-01","Boda especial",now(),"Hacienda Santo Tomas GT","55384679",3);
call sp_AgregarServicio("2020-01-01","Graduacion","18:10:30","San Lucas","55784679",4);



#Listar Servicios
delimiter $$
	 create procedure sp_ListarServicios()
		begin
			select codigoServicio, fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa from Servicios;
		end $$
delimiter ;
call sp_ListarServicios;

#Actualizar Servicio
delimiter $$
	 create procedure sp_ActualizarServicio( cs int ,fs date ,ts varchar(100),
	 hs time, ls varchar(100) , tc varchar(10) ,ce int)
		begin
			update Servicios set  fechaServicio=fs, tipoServicio=ts, horaServicio=hs, lugarServicio=ls, telefonoContacto=tc, codigoEmpresa=ce where codigoServicio=cs;
		end $$
delimiter ;
call sp_ActualizarServicio(1,"2020-04-10","Cumpleaños","16:01:30","Hacienda El Rancho","47823692",3);

#Buscar Servicio
delimiter $$
	 create procedure sp_BuscarServicio(cs int)
		begin
			select * from Servicios where codigoServicio=cs;
		end $$
delimiter ;
call sp_BuscarServicio(1);

#Eliminar Servicio
delimiter $$
	 create procedure sp_EliminarServicio(cs int)
		begin
			delete from Servicios where codigoServicio=cs;
		end $$
delimiter ;
call sp_EliminarServicio(1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados Empleados
delimiter $$
	 create procedure sp_AgregarEmpleado(in numeroEmpleado int,in apellidosEmpleado varchar(150),
	in nombresEmpleado varchar(150),in direccionEmpleado varchar(150),in telefonoContacto varchar(10),in gradoCocinero varchar(50),in codigoTipoEmpleado int)
		begin
			insert into Empleados( numeroEmpleado, apellidosEmpleado, nombresEmpleado,direccionEmpleado, telefonoContacto,gradoCocinero,codigoTipoEmpleado)
			values( numeroEmpleado, apellidosEmpleado, nombresEmpleado,direccionEmpleado, telefonoContacto, gradoCocinero,codigoTipoEmpleado);
		end $$
delimiter ;
call sp_AgregarEmpleado(10,"Cardona Ramos","Luis Daniel","Mixco","41329208","Chef",2);
call sp_AgregarEmpleado(11,"Yantuche moran","Juan Riquelme","San Lucas","41329210","parrilla",2);
call sp_AgregarEmpleado(9,"Telles Hernandez","Daniel ","Comunidad","41329210","parrilla",2);



#Listar Empleados
delimiter $$
	 create procedure sp_ListarEmpleados()
		begin
			select codigoEmpleado,numeroEmpleado,apellidosEmpleado,nombresEmpleado,direccionEmpleado,telefonoContacto,codigoTipoEmpleado,gradoCocinero from Empleados;
		end $$
delimiter ;
call sp_ListarEmpleados;

#Actualizar Empleado
delimiter $$
	 create procedure sp_ActualizarEmpleado(ce int, ne int, ap varchar(150),
	 nom varchar(150),dire varchar(150),tc varchar(10), gc varchar(50), ct int)
		begin
			update Empleados set  numeroEmpleado=ne , apellidosEmpleado=ap , nombresEmpleado=nom,direccionEmpleado=dire , telefonoContacto=tc, 
			gradoCocinero=gc,codigoTipoEmpleado=ct where codigoEmpleado=ce;
		end $$
delimiter ;
call sp_ActualizarEmpleado(1,15,"Luch Telles","Luis David","Antigua","40235846","Repostero",2);

#Buscar Empleado
delimiter $$
	 create procedure sp_BuscarEmpleado(ce int)
		begin
			select * from Empleados where codigoEmpleado=ce;
		end $$
delimiter ;
call sp_BuscarEmpleado(1);

#Eliminar Servicio
delimiter $$
	 create procedure sp_EliminarEmpleado(ce int)
		begin
			delete from Empleados where codigoEmpleado=ce;
		end $$
delimiter ;
call sp_EliminarEmpleado(1);

#*************************************************************************************************************************************************************************
#procedimientos Almacenados Servicios_has_Empleados
drop procedure if exists sp_AgregarServicios_has_Empleado
delimiter $$
	 create procedure sp_AgregarServicios_has_Empleado( in fechaEvento date ,in horaEvento time,in lugarEvento varchar(150),
	 in codigoServicio int ,in codigoEmpleado int)
		begin
			insert into Servicios_has_Empleados(fechaEvento, horaEvento, lugarEvento,codigoServicio, codigoEmpleado)
			values(fechaEvento, horaEvento, lugarEvento,codigoServicio, codigoEmpleado);
		end $$
delimiter ;
call sp_AgregarServicios_has_Empleado("2020-02-15","20:00","Amatitlan",2,2);
call sp_AgregarServicios_has_Empleado("2020-01-15","19:00","Mixco",2,3);


#Listar Servicios_has_Empleados
delimiter $$
	 create procedure sp_ListarServicios_has_Empleados()
		begin
			select Servicios_codigoServicio, fechaEvento, horaEvento, lugarEvento,codigoServicio, codigoEmpleado  from Servicios_has_Empleados;
		end $$
delimiter ;
call sp_ListarServicios_has_Empleados;

#Actualizar Servicios_has_Empleado
delimiter $$
	 create procedure sp_ActualizarServicios_has_Empleado(se int,fe date ,he time, le varchar(150),
	 cs int,ce int)
		begin
			update Servicios_has_Empleados set fechaEvento=fe, horaEvento=he, lugarEvento=le,codigoServicio=cs,codigoEmpleado=ce where Servicios_codigoServicio=se;
		end $$
delimiter ;
call sp_ActualizarServicios_has_Empleado(1,"2020-03-25","19:00","Guatemala",2,2);

#Buscar Servicios_has_Empleado
delimiter $$
	 create procedure sp_BuscarServicios_has_Empleado(se int)
		begin
			select * from Servicios_has_Empleados where Servicios_codigoServicio = se;
		end $$
delimiter ;
call sp_BuscarServicios_has_Empleado(1);


#Eliminar Servicios_has_Empleado
delimiter $$
	 create procedure sp_EliminarServicios_has_Empleado(se int)
		begin
			delete from Servicios_has_Empleados where Servicios_codigoServicio = se;
		end $$
delimiter ;


#*************************************************************************************************************************************************************************
#procedimientos Almacenados Platos
delimiter $$
	 create procedure sp_AgregarPlato(in cantidad int,in nombrePlato varchar(50),
	 in descripcionPlato varchar(150),in precioPlato decimal(10,2),in codigoTipoPlato int)
		begin
			insert into Platos(cantidad ,nombrePlato ,descripcionPlato,precioPlato,codigoTipoPlato)
			values(cantidad,nombrePlato ,descripcionPlato,precioPlato,codigoTipoPlato);
		end $$
delimiter ;
call sp_AgregarPlato(60,"Prosciutto di Parma","jamón aludiéndose al curado, que se sirve a poco cocinar, cortado fino",180.00,2);
call sp_AgregarPlato(60,"Corte Ingles","Corte más fino del norte",180.00,2);
call sp_AgregarPlato(50,"Corte Frances","Corte fino europeo",190.00,2);



#Listar Platos
delimiter $$
	 create procedure sp_ListarPlatos()
		begin
			select codigoPlato,cantidad ,nombrePlato ,descripcionPlato,precioPlato,codigoTipoPlato from Platos;
		end $$
delimiter ;
call sp_ListarPlatos;

#Actualizar Plato
delimiter $$
	 create procedure sp_ActualizarPlato(cpl int,c int,np varchar(50),
	 d varchar(150),p decimal(10,2),ct int)
		begin
			update Platos set cantidad=c ,nombrePlato=np ,descripcionPlato=d,precioPlato=p,codigoTipoPlato=ct where codigoPlato=cpl;
		end $$
delimiter ;
call sp_ActualizarPlato(1,40,"tapado","Caldo de marisco con crema de coco",100.00,2);

#Buscar Plato
delimiter $$
	 create procedure sp_BuscarPlato(cpl int)
		begin
			select * from Platos where codigoPlato=cpl;
		end $$
delimiter ;
call sp_BuscarPlato(1);

#Eliminar Plato
delimiter $$
	 create procedure sp_EliminarPlato(cpl int)
		begin
			delete from Platos where codigoPlato=cpl;
		end $$
delimiter ;
call sp_EliminarPlato(1);

#*************************************************************************************************************************************************************************
#Listar Sevicios_has_Platos
delimiter $$
	 create procedure sp_ListarServicios_has_Platos()
		begin
			select Servicios.codigoServicio,Platos.codigoPlato from Servicios inner join platos;
            
		end $$
delimiter ;
call sp_ListarServicios_has_Platos;

delimiter $$
	create procedure sp_BuscarServicios_has_Plato(cs int ,cpl int)
		begin
			select * from Servicios_has_Platos where codigoServicio=cs and codigoPlato=cpl ;
		end $$
delimiter ;



#*************************************************************************************************************************************************************************


delimiter $$
	 create procedure sp_ListarProductos_has_Platos()
		begin
			select productos.codigoProducto,platos.codigoPlato from Productos inner join platos;
		end $$
delimiter ;
call sp_ListarProductos_has_Platos();


delimiter $$
	 create procedure sp_BuscarProductos_has_Plato(cpr int, cpl int)
		begin
			select * from Productos_has_Platos where codigoProducto=cpr and codigoPlato=cpl;
		end $$
delimiter ;
#call sp_BuscarProductos_has_Plato();

#******************************************************************************************************************************************
#subReporte de Presupuesto...
Delimiter $$
create procedure sp_ListarReporte2 (in codEmpresa int)
Begin
	Select * from Empresas e inner join Presupuesto p on 
	e.codigoEmpresa = p.codigoEmpresa where e.codigoEmpresa = codEmpresa
		Group by p.cantidadPresupuesto;
    
End$$	
Delimiter ;

call sp_ListarReporte2(3);
#********************************************************************************************************************************************
#Reporte de presupuesto...
Delimiter $$
create procedure sp_ListarReporte3 (in codEmpresa int)
Begin
	Select * from Empresas e inner join Servicios s on 
	e.codigoEmpresa = s.codigoEmpresa where e.codigoEmpresa = codEmpresa;
    
End$$	
Delimiter ;

call sp_ListarReporte3(3);
#***********************************************************************************************************************************************
#Reporte de Servicios
Delimiter $$
create procedure sp_ListarReporte1 (in codServicio int)
Begin
	select s.tipoServicio,p.cantidad, t.descripcion from TipoPlato t
		inner join Platos p on t.codigoTipoPlato = p.codigoTipoPlato
			inner join Servicios s on p.codigoTipoPlato = S.codigoServicio
				 where s.codigoServicio = codServicio;
    
End$$	
Delimiter ;

call sp_ListarReporte1(2);
#****************************************************************************************************************************************************
#sub Reporte de Servicios
Delimiter $$
create procedure sp_ListarReporte7 (in codServicio int)
Begin
	select s.tipoServicio, pr.nombreProducto from 
			 Servicios s inner join productos pr
				 where s.codigoServicio = codServicio;
    
End$$	
Delimiter ;
call sp_ListarReporte7(2);