# Exámenes de Sangre — Arquitectura Hexagonal y patrones GoF

Aplicación de escritorio en **JavaFX** para registrar exámenes de sangre, listarlos y guardarlos o cargarlos desde archivos **binarios (.dat)** o **CSV (.csv)**.

Es la refactorización del proyecto base [WolframDev/ParadigmasExamen2](https://github.com/WolframDev/ParadigmasExamen2), que solo traía la interfaz con los métodos vacíos. Se reorganizó con **Arquitectura Hexagonal (puertos y adaptadores)** y se aplicaron tres patrones GoF: **Builder**, **Adapter** y **Strategy**.

**Autor:** Juan Rosero
**Asignatura:** Paradigmas de Programación — Politécnico Grancolombiano

> Diagnóstico, diagramas, informes, análisis de SonarQube y video de sustentación: ver la **Wiki** del repositorio.

---

## 1. Funcionalidades

| Botón | Qué hace |
|---|---|
| **Guardar** | Registra un examen (código, paciente, costo, tipo de sangre, factor RH) en memoria. Valida los datos y rechaza códigos repetidos. |
| **Listar** | Muestra los exámenes registrados en el área de mensajes. |
| **Limpiar** | Limpia el formulario y el área de mensajes. |
| **Serializar** | Guarda todos los exámenes en un archivo con el **formato seleccionado** (Binario o CSV). |
| **Deserializar** | Carga un archivo con el formato seleccionado y **reemplaza** los exámenes en memoria. |

En la carpeta `ejemplos/` hay un `examenes_ejemplo.csv` listo para cargar con *Deserializar* y formato CSV.

---

## 2. Arquitectura hexagonal

```
src/main/java/co/edu/poli/examen2
├── dominio
│   ├── modelo        ExamenSangre, ExamenSangreBuilder (Builder), TipoSangre, FactorRh, FormatoArchivo
│   └── excepcion     ExamenException y sus subclases
├── aplicacion
│   ├── puerto/entrada   RegistrarExamenUseCase, ListarExamenesUseCase,
│   │                    SerializarExamenesUseCase, DeserializarExamenesUseCase
│   ├── puerto/salida    ExamenRepository, ArchivoExamenesPort
│   └── servicio         Un servicio por caso de uso
└── infraestructura
    ├── ui               ControlExamen            (adaptador de entrada JavaFX)
    ├── persistencia     RepositorioMemoriaAdapter (adaptador de salida: memoria)
    ├── archivo          ArchivoExamenesAdapter    (adaptador de salida: archivos)
    │   └── strategy     EstrategiaSerializacion, SerializacionBinaria, SerializacionCsv
    └── web              App (punto de entrada y ensamblado de dependencias)
```

- El **dominio** no depende de nada externo.
- La **aplicación** solo conoce el dominio y sus propios puertos (interfaces).
- La **infraestructura** implementa los puertos. `App` crea los adaptadores y los inyecta en los servicios y en el controlador.

---

## 3. Patrones GoF

| Categoría | Patrón | Clases | Problema que resuelve |
|---|---|---|---|
| Creacional | **Builder** | `ExamenSangreBuilder` → `ExamenSangre` | Crear el examen paso a paso y validar todas las reglas en `build()`; nunca existe un examen inválido. |
| Estructural | **Adapter** | `ControlExamen`, `RepositorioMemoriaAdapter`, `ArchivoExamenesAdapter` | Conectar el núcleo con JavaFX, la memoria y el disco sin que el núcleo los conozca. |
| Comportamiento | **Strategy** | `EstrategiaSerializacion`, `SerializacionBinaria`, `SerializacionCsv` (contexto: `ArchivoExamenesAdapter`) | Cambiar el formato del archivo sin `if/else`; un formato nuevo es solo una clase nueva. |

La justificación completa está en `actividad3.pdf` (Wiki).

---

## 4. Requisitos

- **JDK 17** o superior (el código compila con `release 11`)
- **Maven 3.8+** (o el Maven integrado de Eclipse)
- JavaFX lo descarga Maven automáticamente; no hay que instalarlo.

---

## 5. Instalar y ejecutar

### Con Maven (consola)

```bash
git clone https://github.com/Juanes04af/ParadigmasExamen2.git
cd ParadigmasExamen2/Examen2
mvn clean javafx:run
```

### Con Eclipse

1. `File > Import > Maven > Existing Maven Projects` y seleccionar la carpeta `Examen2`.
2. Clic derecho en el proyecto `> Maven > Update Project`.
3. Ejecutar: clic derecho en el proyecto `> Run As > Maven build...` con el goal `clean javafx:run`.

---

## 6. Pruebas, cobertura y JavaDoc

```bash
mvn test                 # 45 pruebas unitarias (JUnit 5)
mvn verify               # pruebas + reporte de cobertura en target/site/jacoco/index.html
mvn javadoc:javadoc      # documentacion en target/site/apidocs/index.html
```

Las pruebas cubren el Builder, los casos de uso (con un adaptador falso del puerto de archivos), el adaptador en memoria, las dos estrategias y el adaptador de archivos.

---

## 7. Análisis con SonarQube

```bash
# SonarQube local en http://localhost:9000 (por ejemplo con Docker)
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community

# Analisis (generar un token en SonarQube > My Account > Security)
mvn clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=TU_TOKEN
```

La imagen del resumen del análisis está en la Wiki.

---

## 8. Declaración de uso de IA

| Herramienta | Actividad | Uso |
|---|---|---|
| Claude (Anthropic) | Actividad 1 | Apoyo en la revisión del código original, redacción del diagnóstico y generación de los diagramas en Mermaid. |
| Claude (Anthropic) | Actividades 2 y 3 | Apoyo en la propuesta de estructura hexagonal, implementación de los patrones Builder, Adapter y Strategy, pruebas unitarias, JavaDoc y diagramas UML. |
| Claude (Anthropic) | Actividad 4 | Apoyo en la redacción de este README. |

Todo el código y los documentos fueron revisados, ejecutados y validados por el autor, que es responsable del resultado final.
