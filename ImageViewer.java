package gui;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.QTException;
import model.QTree;

import java.io.File;
import java.util.Scanner;

/**
 * The GUI for printing the image based on the pixel file given.
 *
 * @author  Shreya Pramod    sp3045@rit.edu
 */
public class ImageViewer extends Application {

    /**
     * The main routine which handles errors in the command line argument and launches the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args){
        if (args.length == 0){
            System.out.println("$ Usage: java ImageViewer#_filename");
            System.exit(0);
        }

        File file;
        if (args[0].equals("-c")){
            file = new File(args[1]);
        }
        else{
            file = new File(args[0]);
        }
        if (!(file.canRead())) {
            try {
                throw new QTException("Can't read file");
            }
            catch (QTException e){
                System.exit(0);
            }
        }

        Application.launch(args);
    }

    /**
     * The start method calls the respective functions for reading compressed and uncompressed files.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Group group;
        QTree qtree = new QTree(getParameters().getRaw().toArray(new String[0]));
        if (getParameters().getRaw().get(0).equals("-c")) {
            //reading the file for compressed files.
            group = qtree.readCompressedFile();
        }
        else {
            int[][] fileVal;
            int fileSize = qtree.findFileSize();
            //performing read operation for uncompressed files.
            File file = new File(getParameters().getRaw().get(0));
            Scanner sc = new Scanner(file);
            //get the image array of uncompressed file
            fileVal = qtree.getImageArray(fileSize, sc);
            group = qtree.readUncompressedFile(fileVal);
        }
        stage.setScene(new Scene(group));
        stage.show();
    }
}
