# Bank Microservice

## Descripción

Este microservicio se encarga de gestionar bancos y sus sucursales. Proporciona una API RESTful para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los bancos y sus sucursales.

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **RestTemplate**
- **JUnit 5**
- **Mockito**
- **H2 Database**

## Patrones de Diseño Utilizados

### 1. **Domain-Driven Design (DDD)**
- **Entidades y Agregados**: Las entidades `Bank` y `Branch` representan los modelos de dominio. `Bank` es el agregado raíz que contiene una colección de `Branch`.
- **Repositorios**: `BankRepository` se utiliza para acceder a los datos de los bancos.
- **Servicios de Aplicación**: `BankService` contiene la lógica de negocio y coordina las operaciones entre el repositorio y los controladores.

### 2. **Mapper Pattern**
- Utilizamos `BankMapper` para convertir entre las entidades de dominio (`Bank`, `Branch`) y los DTOs (`BankRequestDTO`, `BankResponseDTO`).

### 3. **Controller Pattern**
- `BankController` maneja las solicitudes HTTP y delega las operaciones al `BankService`.

## Características de Robustez

- **Validación de Datos**: Utilización de `jakarta.validation` para validar las entradas de los usuarios.
- **Manejo de Excepciones**: Captura y manejo global de excepciones comunes como `EntityNotFoundException` para proporcionar respuestas HTTP adecuadas.
- **Transacciones**: Utilización de `@Transactional` para asegurar la consistencia de los datos en operaciones de escritura.

## Características de Escalabilidad

- **Desacoplamiento**: El microservicio es autónomo y puede ser desplegado y escalado independientemente.
- **Comunicación Asíncrona**: Utilización de `RestTemplate` para consumir servicios externos de manera eficiente.

## Características de Mantenibilidad

- **Modularidad**: El código está organizado en módulos claros (controladores, servicios, repositorios, modelos).
- **Pruebas Unitarias**: Utilización de JUnit y Mockito para asegurar una alta cobertura de pruebas y facilitar el mantenimiento del código.
- **Documentación**: La API está documentada utilizando Swagger/OpenAPI.

## Uso de H2 en Memoria

Para facilitar las pruebas y el desarrollo, utilizamos la base de datos H2 en memoria. La configuración de H2 se encuentra en el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```
## Creación de Schema y Datos de Prueba
El schema de la base de datos y los datos de prueba se crean automáticamente al iniciar la aplicación. Esto se configura en el archivo `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

## Documentación de la API
La documentación de la API se genera automáticamente utilizando Swagger. Para ver la documentación de la API, inicia el microservicio y navega a la siguiente URL en tu navegador:

<http://localhost:8080/swagger-ui.html>


## Ejecución de Pruebas

Para ejecutar todas las pruebas unitarias, utiliza el siguiente comando:

```bash
mvn test
```

## Ejecución del Microservicio

Para ejecutar el microservicio localmente, utiliza el siguiente comando:

```bash
mvn spring-boot:run
```
