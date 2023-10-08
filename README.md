# Sistema Backend - API del sistema de inventario de vacunación de empleados

- El proyecto detalla una API para llevar un registro del inventario del estado de vacunación de los empleados de una empresa.

## Pre-requisitos 📋

- Internet.
- Navegador web actual.
- Java versión 11 y spring boot 3.0.0
- PostgreSQL.

## Base de datos 📦

- Nombre de la base de datos: vaccine
- Importar el archivo vaccine.sql
- Revisar y modificar las credenciales en el archivo application.properties

## Ejecución 🛠️

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas._

- Clonar o descargar el repositorio.
- En la carpeta del proyecto se encontrará el ejecutable del proyecto .jar
- Abrir una terminal dentro de la carpeta del proyecto y ejecutar el siguiente comando.

    ```bash
   java -jar vaccination.jar


## Funcionamiento 🚀

- Documentación en Swagger: http://localhost:8080/swagger-ui/#/.

- Peticiones en Postman: https://dark-crater-723972.postman.co/workspace/New-Team-Workspace~c4faee8c-cd92-4e04-a1bb-77241e73218f/collection/10790120-70ab8420-6f02-497a-ab6c-a3b2ec9249f1?action=share&creator=10790120.

- Para cualquier ejecución primero se debe logear y obtener su JWT.
- Utilizar Authorization en el header con su respectivo JWT.
- Las credenciales de administrador para el login son: 
    - usuario: sebas1197
    - clave: sG!JAFz$

- Las pruebas unitarias se encuentran en Test Packages ec.com.kruger.vaccine.service



## Autor ✒️

* **Ing.Sebastián Landázuri G** 