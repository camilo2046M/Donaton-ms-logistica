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

##  Cobertura de Código con JaCoCo

Este microservicio utiliza **JaCoCo (Java Code Coverage)** para medir la cobertura de las pruebas unitarias y de integración, asegurando que se cumpla con el estándar mínimo requerido del 60%.

### 🛠 Paso a Paso para Inicializar y Ver el Reporte

Sigue estos pasos para ejecutar la suite de pruebas y generar el reporte analítico y gráfico:

#### 1. Agregar el Plugin al Proyecto (Ya configurado)
El archivo `pom.xml` incluye el plugin oficial de JaCoCo en la sección de construcción (`<build>` -> `<plugins>`):
```xml`

    <plugin>
       <groupId>org.jacoco</groupId>
       <artifactId>jacoco-maven-plugin</artifactId>
       <version>0.8.11</version>
       <executions>
           <execution>
               <goals>
                   <goal>prepare-agent</goal>
               </goals>
           </execution>
           <execution>
               <id>report</id>
               <phase>test</phase>
               <goals>
                   <goal>report</goal>
               </goals>
           </execution>
       </executions>
      </plugin> 

## 2. Ejecutar los Tests y Generar las Métricas

En Windows (PowerShell):

```.\mvnw clean test ```

En Linux / macOS o Git Bash:

```./mvnw clean test```

## Alternativa Gráfica (IntelliJ IDEA):
Abre la pestaña lateral Maven ➡️ Despliega Lifecycle ➡️ Presiona Ctrl y haz doble clic en clean y luego en test.

Al finalizar con éxito, verás el mensaje BUILD SUCCESS en la consola.

## 3. Visualizar el Reporte Gráfico Interactivos
Una vez finalizados los tests, JaCoCo compila un sitio web local con tablas y gráficos de barras de cobertura.

Navega en tu explorador de archivos hasta la siguiente ruta dentro del proyecto:
target/site/jacoco/

Busca y abre el archivo index.html haciendo doble clic (se abrirá en tu navegador web como Chrome o Edge).

Ahí podrás analizar el desglose detallado por paquetes (controller, service, repository), líneas ejecutadas y caminos lógicos cubiertos.