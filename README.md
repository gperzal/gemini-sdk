# ☕ Gemini SDK for Java

![JitPack](https://jitpack.io/v/gperzal/gemini-sdk.svg)
![Java](https://img.shields.io/badge/Java-21-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Build](https://img.shields.io/badge/build-success-brightgreen.svg)

---

## 🧠 Overview

**Gemini SDK** es una librería Java ligera, segura y sin dependencias de frameworks pesados (como Spring), que permite comunicarse directamente con la **API oficial de Google Gemini**.

El SDK está diseñado para ser:
- 🔒 **Seguro** (sin exponer API Keys ni depender de cURL directo)
- ⚡ **Rápido** (usa `HttpClient` moderno de Java 21)
- 🧩 **Sencillo de integrar** (solo necesitas una API Key)
- 🪶 **Minimalista** (no requiere Spring AI ni Vertex AI SDK)
- 💡 **Flexible** (permite trabajar con texto, archivos o listas de datos)

---

## 🚫 Por qué no usamos `curl`, `Spring AI` ni `Vertex AI`

### ❌ `curl` directo:
Usar `curl` con la API de Gemini, aunque funcional, es **inseguro y poco práctico**:
- expone la **API Key** en texto plano;
- carece de manejo de errores controlado;
- no permite integración nativa con objetos Java.

Ejemplo inseguro:
```bash
curl "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"   -H "Content-Type: application/json"   -H "X-goog-api-key: <API_KEY>"   -d '{"contents":[{"parts":[{"text":"Hello world"}]}]}'
```

### ⚠️ `Spring AI` y `Vertex AI`
Estas herramientas son potentes, pero **sobredimensionadas para SDKs ligeros**:
- Requieren configuración compleja (`application.yml`, beans, autowiring…)
- Añaden dependencias reactivas (WebFlux, Reactor, gRPC)
- Dificultan la distribución como librería pura

Gemini SDK se centra en **uso rápido y portable**, sin dependencias externas, ideal para proyectos CLI, microservicios o integraciones personalizadas.

---

## ⚙️ Instalación (vía JitPack)

Agrega el repositorio de **JitPack** a tu `pom.xml`:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Y luego la dependencia:

```xml
<dependency>
  <groupId>com.github.gperzal</groupId>
  <artifactId>gemini-sdk</artifactId>
  <version>v1.2</version>
</dependency>
```

O si usas **Gradle**:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.gperzal:gemini-sdk:v1.2'
}
```

---

## 🔐 Configuración

Crea un archivo `.env` en la raíz de tu proyecto con las variables:

```
GEMINI_API_KEY=tu_api_key_de_google
GEMINI_MODEL=gemini-1.5-flash
```

> 💡 Puedes obtener tu API key desde [Google AI Studio](https://aistudio.google.com/app/apikey).

El SDK leerá automáticamente estas variables usando `dotenv-java`, o bien las tomará desde el entorno del sistema.

---

## 🧩 Ejemplo de uso básico

```java
import com.gperzal.gemini.GoogleGenerativeAI;

public class Demo {
    public static void main(String[] args) {
        GoogleGenerativeAI ai = GoogleGenerativeAI.connect();

        String respuesta = ai.prompt("Explícame cómo funciona el aprendizaje automático en 3 frases.");
        System.out.println("🤖 Gemini responde:\n" + respuesta);
    }
}
```

---

## 📄 Ejemplo con archivo CSV

El SDK permite enviar **archivos de datos** (por ejemplo CSV o JSON) junto al prompt.  
Esto convierte automáticamente el archivo en una **tabla Markdown legible por Gemini**, optimizando la comprensión del modelo.

```java
import com.gperzal.gemini.GoogleGenerativeAI;
import java.io.File;

public class DemoCSV {
    public static void main(String[] args) {
        GoogleGenerativeAI ai = GoogleGenerativeAI.connect();

        File file = new File("D:\data\phones.csv");
        String respuesta = ai.prompt("¿Cuál es la marca más repetida y el sistema operativo más usado?", file);

        System.out.println("📊 Respuesta de Gemini:");
        System.out.println(respuesta);
    }
}
```

Internamente, el SDK convierte el CSV en algo así:
```markdown
| brand | os |
|-------|----|
| Samsung | Android |
| Apple | iOS |
| Apple | iOS |
| Xiaomi | Android |
```

---

## 🧠 Prompt con listas o colecciones

Puedes pasar listas u objetos simples directamente:

```java
List<String> ventas = List.of(
  "Juan - Región Norte - $12000",
  "María - Región Sur - $18000",
  "Carlos - Región Centro - $15000"
);

String respuesta = ai.prompt("Indica el top vendedor del mes:", ventas);
System.out.println(respuesta);
```

---

## 🧰 Estructura del SDK

```
com.gperzal.gemini/
├── GoogleGenerativeAI.java   → Clase principal del SDK
├── GeminiClient.java         → Cliente HTTP para la API REST
├── GeminiRequest.java        → Representación de la solicitud JSON
├── GeminiResponse.java       → Procesamiento del JSON de respuesta
├── GeminiException.java      → Excepciones personalizadas
└── GeminiApplication.java    → Ejemplo ejecutable (demo)
```

---

## 🔍 Seguridad y mejores prácticas

✅ Nunca expongas tu **API Key** en código fuente  
✅ Usa `.env` o variables del sistema  
✅ Evita compartir tu `.env` en repos públicos  
✅ Maneja los errores con `GeminiException` para respuestas HTTP no exitosas  
✅ Los métodos `prompt()` y `generate()` encapsulan todas las validaciones

---

## 💬 Métodos disponibles

| Método | Descripción |
|--------|--------------|
| `connect()` | Crea una instancia del SDK leyendo `.env` o variables de entorno |
| `prompt(String)` | Envía un prompt de texto simple |
| `prompt(String, File)` | Envía un prompt junto con un archivo (CSV, JSON, TXT, etc.) |
| `prompt(String, List<?>)` | Envía un prompt con una lista de datos estructurados |
| `getModel()` / `getApiKey()` | Devuelve el modelo o clave actual usada por la instancia |

---

## 🧪 Ejemplo de salida esperada

```
🤖 Gemini responde:
La marca más común es **Samsung**, con sistema operativo **Android**.
```

---

## 🧱 Requisitos

- Java 21 o superior ☕
- Maven 3.9+
- Conexión a internet
- API Key válida de [Google AI Studio](https://aistudio.google.com/)

---

## 🧩 Licencia

Este proyecto está bajo la licencia **MIT** — puedes usarlo libremente en proyectos personales o comerciales.  
Consulta el archivo `LICENSE` para más detalles.

---

## 💬 Créditos

Desarrollado por [**Guido Pérez Zelaya**](https://github.com/gperzal)  
→ Ingeniero en Informática • Full Stack & AI Developer  
→ Autor del SDK **FactorAI Gemini** 💡

---

## 🚀 Contribuciones

¡Las contribuciones son bienvenidas!  
Abre un **Pull Request** o crea un **Issue** en  
👉 [github.com/gperzal/gemini-sdk](https://github.com/gperzal/gemini-sdk)

---

## 🪄 Futuras versiones

- [ ] Soporte multimodal (texto + imagen)
- [ ] Serialización a JSON estructurado
- [ ] Streaming de respuesta en tiempo real
- [ ] Plugin de integración con Spring Boot

---

## 📦 Distribución JitPack

Puedes ver la versión actual y builds en  
👉 [https://jitpack.io/#gperzal/gemini-sdk](https://jitpack.io/#gperzal/gemini-sdk)

---

> “El objetivo de este SDK es devolver el control a los desarrolladores:  
>  usar Gemini con total libertad, sin frameworks forzados ni dependencias innecesarias.”  
> — *Guido Pérez Zelaya*
