# Simple Graph Editor

This is a Kotlin-based application that allows users to visualize and interact with directed graphs. The user can define graphs by entering an edge list, toggle vertices to enable/disable them, and see the resulting graph diagram generated by PlantUML. The application uses Kotlin's coroutines for asynchronous operations and provides a responsive and smooth user interface.

## Features

- **Graph Visualization**: The directed graph is visualized in a display area where users can see the structure of the graph's edges.
- **Interactive Input Area**: Users can define their graphs through an input area by entering an edge list (e.g., `A -> B` for a directed edge from A to B).
- **Vertex List**: The list of all vertices is displayed, and users can enable/disable specific vertices. Disabled vertices are excluded from the graph diagram.
- **Automatic Graph Refresh**: The diagram refreshes automatically when the user modifies the graph input or toggles vertices.
- **File Management**: Users can open, save, and create new graph definitions from files.
  
## Requirements

- Kotlin 2.1.10 or newer
- Java 21 or newer (for building and running the project)

## Dependencies

- **PlantUML** for generating graph images.
- **Kotlin Coroutines** for managing asynchronous tasks and background operations. This includes swing coroutines.

## How to Run

1. Clone the repository to your local machine.

2. Build and run the project using IntelliJ IDEA or another IDE that supports Kotlin development.

3. Launch the application (`src/main/kotlin/Main.kt`) and start interacting with the graph by entering edges into the input area. The directed graph will be visualized in real-time.

## Constants

Constant values are declared in the `src/main/kotlin/utils/Utils.kt` file. These constants are used throughout the application to control various behaviors and settings.

- **CACHE_LIMIT**: Defines the maximum number of images to cache for faster graph rendering. Default is `1`.
- **INPUT_DELAY**: The delay (in milliseconds) applied when changing any input. Default is `300L`.
- **VERTICES_CHUNK**: The size of the chunks when processing and displaying vertices in the Vertices Panel. Default is `100`.
- **MAX_LOADING_TIME**: The maximum time (in milliseconds) allowed for loading graph images before timing out. Default is `10000L`.
- **PRINT_TIMERS**: A flag that controls whether or not to print the elapsed time for generating graph images. Default is `false`.
- **SHOW_LOADING_ON_FREEZE**: A flag that determines whether a loading indicator should be shown when the application is frozen. Default is `false`.
- **MAX_INPUT_LENGTH**: The maximum allowed length of the graph input text. Default is `10000` characters.
- **MAX_LINE_COUNT**: The maximum number of lines allowed in the graph input. Default is `1500` lines.

These constants provide control over key aspects of the application, such as performance, UI responsiveness, and input validation.

## Tests

Tests are available in the `src/test/kotlin/` directory, where unit tests for the core logic and functionalities of the application are implemented. These tests cover various components of the application, ensuring the correctness of graph visualization and input parsing.

Additionally, example graph data files are located in the `data/` directory. These files contain edge lists representing graphs of different sizes (e.g., 15, 100, 1000, or 10000 edges). These data files are used to validate the application's ability to handle different graph sizes and ensure performance remains optimal.
