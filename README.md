# Laboratorio7_Thomas_AppToDo_Backend
Este repositorio hace parte del laboratorio 7 de la materia de DOSW, sección BackEnd de la aplicación propuesta AppToDo

| Name                            | GitHub Username                   |
|---------------------------------|-----------------------------------|
| Daniel Santiago Morales Perdomo | `Fukuyaku88`                      |
| Edgar Daniel Ruiz Patiño        | `DaniRuiz11`                      |
| Miguel Ángel Acero Laverde      | `Miguelim101`                     |
| Thomas Sebastian García Gomez   | `thomassebastiangarciagomez-ctrl` |

## Parte 1: Desarollo del laboratorio

Primero, para esta parte se realizara la actualizacion de diferentes sistemas para aceptar las nuevas implementaciones dentro del codigo, lo primero, es actualizar los diagramas C4, donde los diagramas asociados serán los siguientes


1. Diagrama de contexto:



2. Diagrama de contenedores:



3. Diagrama de componentes:



Ahora, despues de realizar esta actualizacion, se tienen que agregar las dependencias necesarias dentro del pom.xml para poder realizar la integracion adecuada de este sistema. Las dependencias que se agregaran son las siguientes.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security-oauth2-resource-server</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security-oauth2-resource-server-test</artifactId>
    <scope>test</scope>
</dependency>
```

Con esto, ya tendremos las dependencias adecuadas para poder realizar la integracion de todo el sistema de seguridad en nuestra aplicacion AppToDo.

