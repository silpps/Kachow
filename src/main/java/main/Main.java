package main;

import view.View;

/**
 * main.Main class for the JavaFX application.
 * This class serves as the entry point for the application.
 * It launches the JavaFX application by calling the launch method.
 */
public class Main {

    /**
     * main.Main method to launch the JavaFX application.
     *
     * @param args command line arguments
     */

    public static void main(String [] args){
        View.launch(View.class);
    }
}
