# Laboratorio Hilos y Concurrencia 02

## Integrantes
- Sebastián Cardona
- Laura Gil
- Zayra Gutiérrez

## Docente
**Javier Toquica Barrera**

## Universidad
Escuela Colombiana de Ingeniería Julio Garavito
---

## Contenido
1. [Introducción](#introducción)
2. [Desarrollo del Laboratorio](#desarrollo-del-laboratorio)
   - [Parte I: Control de hilos con wait/notify](#parte-i-control-de-hilos-con-waitnotify)
   - [Parte II: Optimización de concurrencia en SnakeRace](#parte-ii-optimización-de-concurrencia-en-snakerace)
3. [Conclusiones](#conclusiones)
---

## Introducción
En el ámbito de la programación concurrente, el uso adecuado de hilos y mecanismos de sincronización es crucial para garantizar el correcto funcionamiento de aplicaciones que requieren ejecución simultánea de múltiples procesos. Este laboratorio tiene como objetivo explorar y aplicar conceptos fundamentales de concurrencia en Java, utilizando herramientas como `wait()`, `notify()` y `notifyAll()` para la gestión de hilos.

El laboratorio consta de dos partes:
1. **PrimeFinder:** Implementación de una pausa y reanudación en la búsqueda de números primos utilizando sincronización de hilos.
2. **SnakeRace:** Optimización de concurrencia en un juego basado en el clásico "Snake", identificando y solucionando condiciones de carrera.

---

## Desarrollo del Laboratorio

### Parte I: Control de hilos con wait/notify

#### Descripción del Proyecto PrimeFinder
PrimeFinder es un programa que calcula números primos de manera concurrente, distribuyendo la búsqueda entre `N` hilos independientes. Se realizaron modificaciones para implementar un mecanismo de pausa y reanudación mediante `wait()` y `notifyAll()`.

#### Estructura del Código
- **PrimeFinderThread:** Hilo encargado de buscar números primos en un rango específico.
- **Control:** Maneja la ejecución, pausa y reanudación de los hilos.
- **Main:** Inicia la ejecución del programa.

#### Funcionamiento
1. **Inicio:** Se crea una instancia de `Control` que configura los hilos `PrimeFinderThread`.
2. **Ejecución:** Los hilos buscan números primos en su rango asignado.
3. **Pausa:** Cada `t` milisegundos, los hilos se detienen y muestran los primos encontrados.
4. **Reanudación:** Al presionar ENTER, los hilos continúan la ejecución.
![image](https://github.com/user-attachments/assets/42eca98a-6071-4451-bb77-9e2d3eb53a2d)

6. **Finalización:** El programa termina cuando todos los hilos completan su tarea.
![image](https://github.com/user-attachments/assets/daadc482-d71a-46cc-b799-f9e375b1833b)


#### Consideraciones Importantes
- Uso de `synchronized` para garantizar acceso exclusivo a los recursos compartidos.
- Implementación de `wait()` y `notifyAll()` para la correcta sincronización de los hilos.

---

### Parte II: Optimización de concurrencia en SnakeRace

#### Descripción del Proyecto SnakeRace
SnakeRace es una versión concurrente del clásico juego Snake. Cada serpiente funciona como un hilo autónomo que toma decisiones aleatorias cada 500 ms. Se analizaron problemas de concurrencia y se realizaron ajustes para mejorar la estabilidad del programa.

#### Problemas Identificados
1. **Condiciones de carrera:** Acceso simultáneo a la GUI y estructuras compartidas como la lista de serpientes.
2. **Uso inadecuado de colecciones:** Excepciones al modificar colecciones en ejecución.
3. **Esperas activas innecesarias:** Reducción del consumo de CPU mediante sincronización eficiente.

#### Soluciones Implementadas
- **Sincronización de acceso a recursos compartidos** mediante `synchronized`.
- **Uso de objetos de bloqueo** para evitar inconsistencias en la GUI.
- **Creación de botones** para iniciar, pausar y reanudar el juego.
- **Visualización de estadísticas** al pausar el juego (serpiente más larga y la primera en morir).

#### Funcionamiento Mejorado
1. **Inicio:** Los hilos de las serpientes comienzan su ejecución al presionar "Start".
3. **Movimiento:** Cada serpiente se mueve de manera autónoma dentro del tablero.
5. **Pausa:** Al presionar "Pause", los hilos entran en `wait()` y se muestran estadísticas.
6. **Reanudación:** Al presionar "Resume", los hilos continúan su ejecución con `notifyAll()`.
7. **Finalización:** Cuando queda solo una serpiente viva, el juego finaliza automáticamente.

---

## Conclusiones
- Se comprendió la importancia de la **sincronización de hilos** para evitar condiciones de carrera.
- Se aplicaron técnicas avanzadas de concurrencia para **optimizar el rendimiento y estabilidad** de programas multihilo.
- Se reforzó la utilización de `wait()`, `notify()` y `notifyAll()` para la correcta gestión de procesos concurrentes.
- Se mejoró la experiencia de usuario mediante la implementación de **controles de ejecución en la interfaz gráfica**.

---


