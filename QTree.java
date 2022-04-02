package model;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class representing the quadtree data structure.
 *
 * @author  Shreya Pramod    sp3045@rit.edu
 */
public class QTree {

    /** Arraylist holding the values read from the input file */
    private static ArrayList<Integer> nodeList = new ArrayList<>();

    /** 2D imageArray for compressed file*/
    private static int[][] imageArray;

    /** command line argument */
    private String[] userInput;

    /**
     * Initializing the command line argument
     */
    public QTree(String[] args){
        this.userInput = args;
    }

    /**
     * Obtains the file size.
     *
     * @return size of the file
     */
    public int findFileSize() {
        String fileName;
        if (userInput[0].equals("-c"))
            fileName = userInput[1];

        else
            fileName = userInput[0];
        String numberOnly = fileName.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly.substring(0, (numberOnly.length() / 2)));
    }

    /**
     * Creates the 2-D image array based on the user input.
     *
     * @return 2-D image array
     */
    public int[][] getImageArray(int fileSize, Scanner sc) {
        int[][] fileVal = new int[fileSize][fileSize];
        for (int i = 0; i < fileSize; i++) {
            for (int j = 0; j < fileSize; j++) {
                while ((sc.hasNextLine()) && (j < fileSize)) {
                    int data = Integer.parseInt(sc.nextLine());
                    fileVal[i][j] = data;
                    j++;
                }
                break;
            }
        }
        return fileVal;
    }

    /**
     * Reads the uncompressed file given as user input.
     *
     * @return updated group containing the pixelated values.
     */
    public Group readUncompressedFile(int[][] fileVal){

        int fileSize = findFileSize();
        Group group = new Group();
        Canvas canvas = new Canvas(550, 550);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < fileSize; i++) {
            for (int j = 0; j < fileSize; j++) {

                //Checking for only integer values in the file
                if (Math.floor(fileVal[i][j])!=fileVal[i][j]) {
                    try {
                        throw new QTException("Not integer");
                    }
                    catch (QTException e){
                        System.exit(0);
                    }
                }

                Color c = Color.rgb(fileVal[i][j], fileVal[i][j], fileVal[i][j]);
                gc.setFill(c);

                if (i < fileSize && j <= fileSize) {
                    gc.fillRect(j, i, 1, 1);
                } else if (j >= fileSize) {
                    continue;
                }
            }
        }
        group.getChildren().add(canvas);
        return group;
    }

    /**
     * Creates the QuadTree
     *
     * @return the node that contains the complete tree.
     */
    private QTNode createQuadTree(int index){
        QTNode ul = null, ur = null, ll = null, lr= null;
        QTNode node = null;
        if (nodeList.get(index)!=-1) {
            int nodeVal = nodeList.get(index);
            nodeList.remove(index);
            return new QTNode(nodeVal);
        }
        else if (nodeList.get(index) == -1){
            if (nodeList.get(index) == -1) {
                nodeList.remove(index);
            }
            node = new QTNode(-1, ul = createQuadTree(index),
                    ur = createQuadTree(index),
                    ll = createQuadTree(index),
                    lr = createQuadTree(index));
        }
        return node;
    }

    /**
     * Prints the quad tree in preorder traversal.
     */
    private void printPreorder(QTNode node) {
        if(node.getValue() == -1){
            System.out.print(node.getValue()+" ");
            printPreorder(node.getUpperLeft());
            printPreorder(node.getUpperRight());
            printPreorder(node.getLowerLeft());
            printPreorder(node.getLowerRight());
        }
        else
            System.out.print(node.getValue()+" ");
    }

    /**
     * Converts the quadtree into a 2-D image array
     *
     * @return 2-D image array
     */
    private int[][] convertToRawImage(QTNode node,int startx,int starty,int fileSize) {
        if (node.getValue()!=-1){
            for(int p=startx;p<startx+fileSize;p++){
                for(int q=starty;q<starty+fileSize;q++){
                    imageArray[p][q]=node.getValue();
                }
            }
        }

        if (node.getValue()==-1){
            convertToRawImage(node.getUpperLeft(),startx,starty,fileSize/2);
            convertToRawImage(node.getUpperRight(),startx,starty+fileSize/2,fileSize/2);
            convertToRawImage(node.getLowerLeft(),startx+fileSize/2,starty,fileSize/2);
            convertToRawImage(node.getLowerRight(),startx+fileSize/2,starty+fileSize/2,fileSize/2);
        }

        return imageArray;
    }

    /**
     * Processes the Compressed file
     *
     * @return group containing the pixelated compressed file values.
     */
    public Group readCompressedFile() throws FileNotFoundException, QTException {
        String fileName = userInput[1];
        Group group = new Group();
        File file = new File(fileName);

        int fileSize = findFileSize();

        imageArray = new int[fileSize][fileSize];

        System.out.println("Uncompressing: "+fileName);

        Scanner sc = new Scanner(file);
        sc.nextLine();

        while(sc.hasNextLine()) {
            nodeList.add(Integer.parseInt(sc.nextLine()));
        }

        QTNode node = createQuadTree(0);

        System.out.print("QTree: ");
        printPreorder(node);

        imageArray = convertToRawImage(node,0,0,fileSize);

        group = readUncompressedFile(imageArray);

        return group;
    }
}
