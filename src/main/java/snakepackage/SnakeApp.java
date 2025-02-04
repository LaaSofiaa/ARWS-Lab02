package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};

    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    // Estblecer los botones y labels para el juego
    private JButton startButton, pauseButton, resumeButton;
    private JLabel statusLabel;
    // Establecer las variables de control del juego
    private boolean gamePaused = false;
    private boolean gameStarted = false;
    // crear los objetos de sincronizacion
    public final Object lockGui = new Object();
    public final Object lock = new Object();
    public final Object lockSnake = new Object();
    private final Object lockPause = new Object();

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);

        // Initialize snakes

        for (int i = 0; i < MAX_THREADS; i++) {
            snakes[i] = new Snake(i + 1, spawn[i], i + 1,lockGui,lock,lockSnake,lockPause);
        }

        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());

        // Inicializar los botones
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");

        // Añadir los eventos a los botones
        startButton.addActionListener(e -> startGame());
        pauseButton.addActionListener(e -> pauseGame());
        resumeButton.addActionListener(e -> resumeGame());

        actionsBPabel.add(startButton);
        actionsBPabel.add(pauseButton);
        actionsBPabel.add(resumeButton);

        frame.add(actionsBPabel,BorderLayout.SOUTH);

        statusLabel = new JLabel("Game not started");
        frame.add(statusLabel,BorderLayout.NORTH);

        // Desactivar los botones de pausa y reanudar
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);

        // Crear hilo de control del juego
        new Thread(() -> monitorGameState()).start();
    }

    private void monitorGameState() {
        while (true) {
            if (gameStarted && !gamePaused) {
                // Contar cuantos jugadores estan vivos
                int aliveSnakes = countAliveSnakes();

                //Establecer serpienete ganadora
                if (aliveSnakes ==1) {
                    Snake winner = findWinnigSnake();

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Game Over! Snakes " + winner.getIdt() + " wins!");
                        startButton.setEnabled(false);
                        pauseButton.setEnabled(false);
                        resumeButton.setEnabled(false);
                    });
                    // Establecer la serpiente ganadora y marcar el final del juego
                    winner.setSnakeEnd(true);

                    gameStarted = false;
                    break;
                }

                updateInfoLabel();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SnakeApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Contar cuantas serpientes estan vivas
    private int countAliveSnakes() {
        int aliveSnakes = 0;
        for (Snake snake : snakes) {
            if (!snake.isSnakeEnd()) {
                aliveSnakes++;
            }
        }
        return aliveSnakes;
    }

    // Encontrar la serpiente ganadora
    private Snake findWinnigSnake() {
        for (Snake snake : snakes) {
            if (!snake.isSnakeEnd()) {
                return snake;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.frame.setVisible(true);
    }

    // Iniciar el juego
    private void startGame() {
        if (!gameStarted) {
            gameStarted = true;
            gamePaused = false;
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
            statusLabel.setText("Game started");

            for (int i = 0; i != MAX_THREADS; i++) {
                snakes[i].addObserver(board);
                thread[i] = new Thread(snakes[i]);
                thread[i].start();
            }
        }
    }

    // Pausar el juego
    private void pauseGame() {
        if (gameStarted && !gamePaused) {
            gamePaused = true;
            startButton.setEnabled(false);
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);

            for (Snake snake : snakes) {
                snake.pause();
            }
            updateInfoLabel();
        }
    }

    // Reanudar el juego
    private void resumeGame() {
        if (gameStarted && gamePaused) {
            gamePaused = false;
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);

            for (Snake snake : snakes) {
                snake.resume();
            }
            statusLabel.setText("Game started Again");
        }
    }

    // Actualizar la etiqueta de informacion
    private void updateInfoLabel() {
        Snake LongestSnake = findLongestSnake();
        String worstSnake = findWorstSnake();
        String info = "Longest Snake: " + LongestSnake.getIdt() + " - " + LongestSnake.getBody().size() + " cells | Worst Snake: " + worstSnake;
        statusLabel.setText(info);
    }



    // Encontrar la serpiente mas larga
    private Snake findLongestSnake() {
        Snake longestSnake = snakes[0];
        for (Snake snake : snakes) {
            if (!snake.isSnakeEnd() && snake.getBody().size() > longestSnake.getBody().size()) {
                longestSnake = snake;
            }
        }
        return longestSnake;
    }

    // Encontrar la serpiente peor
    private String findWorstSnake() {
        String diedFirst = Snake.getDiedFirst();
        if(diedFirst.equals("No Snake Died Yet")){
            diedFirst = "No one died";
        }
        return diedFirst;
    }

    public static SnakeApp getApp() {
        return app;
    }

}
