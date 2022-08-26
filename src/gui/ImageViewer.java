package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.ArgsException;
import model.QuadTree;

import java.util.List;

/**
 * A gui program to generate raw image of compressed and uncompressed file
 * @author Shivani Singh, ss5243@rit.edu
 * @author Ronit Boddu, rb1209@rit.edu
 */
public class ImageViewer extends Application {
    /**
     * 2D array to store the pixel values of compressed and uncompressed files.
     */
    private int[][] rawImage;

    /**
     * gets the raw image by reading compressed file or uncompressed file.
     */
    @Override
    public void init(){
        List<String> args = getParameters().getRaw();
        QuadTree QT = new QuadTree();
        if(args.size()==1){
            rawImage = QT.readUncompressed(args.get(0));
        }
        else{
            rawImage = QT.readCompressed(args.get(1));
        }
    }

    /**
     * creates gui image out of raw image.
     * @param stage stage.
     * @throws Exception Exception.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Group group = new Group();
        Canvas canvas = new Canvas(rawImage.length,rawImage.length);
        System.out.println(rawImage.length);
        //canvas.setWidth(600);
        //canvas.setHeight(600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        float indexXaxis = 0;
        float indexYaxis = 0;
        for (int i = 0; i < rawImage.length; i++) {
            for (int j = 0; j < rawImage.length; j++) {
                gc.setFill(Color.rgb(rawImage[i][j], rawImage[i][j], rawImage[i][j]));
                gc.fillRect(j, i, 1, 1);
                //indexXaxis = indexXaxis + 1;
            }
            //indexXaxis = 0;
            //indexYaxis = indexYaxis + 1;
        }
        group.getChildren().addAll(canvas);
        Scene scene = new Scene(group);
        stage.setTitle("2D Array!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  The main method. It checks the number of arguments passed in the program and launches the
     *  Application.
     * @param args Arguments.
     */
    public static void main(String[] args) {
        try{
            if((args.length!=1 && args.length!=2) || (args.length==2 && !args[0].equals("-c"))){
            throw new ArgsException("Usage: ImageViewer [-c] <filename>\n" +
                    "-c Reads in a compressed image file. If this option is not present, the file " +
                    "is considered to be uncompressed.");
            }
        }catch (ArgsException ae){
            System.out.println(ae.getMessage());
            System.exit(0);
        }
        Application.launch(args);
    }
}
