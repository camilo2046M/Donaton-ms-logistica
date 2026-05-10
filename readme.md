# Microservicio de Gestión Logística

Este microservicio es el componente inteligente del ecosistema **Donatón**. Se encarga de la planificación automática de envíos, la asignación de transporte basada en el tipo de carga y el seguimiento del estado de las entregas.

##  Inteligencia de Transporte

El servicio cuenta con un motor de decisiones (`EnvioFactory`) que asigna automáticamente el medio de transporte:
- **AÉREO**: Para artículos críticos (ej. Medicamentos).
- **MARÍTIMO**: Para artículos pesados o voluminosos (ej. Maquinaria).
- **TERRESTRE**: Para artículos estándar (ej. Ropa, Alimentos).

##  Tecnologías Utilizadas

* **Java 17/21** (Optimizado para entornos modernos).
* **Spring Boot 3.3.x**
* **Spring Cloud OpenFeign**: Para comunicación síncrona con el Microservicio de Donaciones.
* **Spring Data JPA**: Persistencia en PostgreSQL.
* **Lombok**: Productividad en el desarrollo.
* **Mockito**: Cobertura de tests unitarios (>60%).

## Instalación y Ejecución
Clonar el repositorio.

Navegar a la carpeta del proyecto.

Ejecutar el comando Maven:
```mvn spring-boot:run```

##  Configuración y Dependencias

Este microservicio **depende** de que el servicio de Donaciones esté en ejecución.

### Requisitos
1. Crear base de datos en PostgreSQL:
   vsql
   CREATE DATABASE bd_logistica;

# Archivo de Configuración (application.yml)
aqui podemos encontrar la configuraxion para la conexion con el microservicio de donaciones
```
spring:
application:
name: donaton-ms-logistica

datasource:

    url: jdbc:postgresql://localhost:5432/bd_logistica
    username: postgres
    password: 12345
    driver-class-name: org.postgresql.Driver

jpa:
hibernate:

      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

donaton:
ms:
donaciones:
url: http://localhost:8081/api/donaciones
cloud:
compatibility-verifier:
enabled: false

server:
port: 8082

```
## Endpoints Principales (API)
1. Registrar Donación
   POST /api/donaciones/crear
   Permite registrar una nueva donación enviando los parámetros requeridos (tipo, monto, nombre, objeto).

2. Buscador para Logística
   GET /api/donaciones/buscar/{palabra}
   Nota: Este endpoint es consumido por el microservicio de Logística a través de Feign Client para planificar envíos automáticos.

3. Actualizar Estado
   PATCH /api/donaciones/{id}/completar
   Cambia el estado de la donación a COMPLETADA una vez que Logística confirma la entrega.

## Pruebas Unitarias
Para ejecutar los tests de cobertura y asegurar la calidad del código:
```mvn test ```