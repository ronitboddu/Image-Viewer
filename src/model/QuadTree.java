package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;
import java.util.List;

/**
 * Reads Uncompressed and Compressed and returns raw Image.
 * @author Shivani Singh, ss5243@rit.edu
 * @author Ronit Boddu, rb1209@rit.edu
 */
public class QuadTree {
    /**
     * 2D array to store the raw image.
     */
    private int[][] rawImage;
    /**
     * Linked List to store the data read from compressed text file and create Quad Tree from it.
     */
    private LinkedList<Integer> QTLinkedList;
    /**
     * String to store the preorder traversal.
     */
    private String preOrder;
    /**
     * Array List to store the data read from Uncompressed text file.
     */
    private final List<Integer> storeRawData;

    /**
     * Constructor to initialize preorder string and arraylist to store file data.
     */
    public QuadTree(){
        preOrder="";
        storeRawData = new ArrayList<>();
    }

    /**
     * Reads Uncompressed file and stores the pixel values in the 2d array
     * @param filename Filename to read the data from
     * @return raw image i.e 2D representation of pixel values.
     */
    public int[][] readUncompressed(String filename){
        String Filepath = System.getProperty("user.dir").replace("\\","/")
                    +"/images/uncompressed/"+filename;
        File file = new File(Filepath);
        if(!Files.isReadable(file.toPath())){
            try {
                throw new FileNotFoundExcep("Error: file not found or file is not readable.");
            }catch (FileNotFoundExcep fnfe){
                System.out.println(fnfe.getMessage());
                System.exit(0);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(Filepath))){
            String line;
            while((line=br.readLine())!=null){
                try{
                    if(Integer.parseInt(line)<0 || Integer.parseInt(line)>255){
                        try {
                            throw new OutOfRangePixel("Error: Pixel value cannot be less than 0 or greater " +
                                        "than 255");
                        }catch (OutOfRangePixel orp){
                            System.out.println(orp.getMessage());
                            System.exit(0);
                        }
                    }
                }catch (NumberFormatException nfe){
                    System.out.println("Error: File contains string as pixel value.");
                    System.exit(0);
                }

                storeRawData.add(Integer.parseInt(line));
            }
            double sqrt = Math.sqrt(storeRawData.size());
            if((sqrt-Math.floor(sqrt))!=0){
                try {
                    throw new ImageSizeException("Error: Image size "+ storeRawData.size()
                                +" is not a square");
                }catch (ImageSizeException ise){
                    System.out.println(ise.getMessage());
                    System.exit(0);
                }
            }
            int i = (int)sqrt;
            rawImage = new int[i][i];
            int index=0;
            for(int k=0;k<rawImage.length;k++){
                for(int l=0;l<rawImage[k].length;l++) {
                    rawImage[k][l] = storeRawData.get(index++);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawImage;
    }

    /**
     * Read compressed file, create quad tree and parse it into a raw image.
     * @param filename Filename to read the data from.
     * @return raw image i.e 2D representation of pixel values.
     */
    public int[][] readCompressed(String filename){
        int[][] rawImage=null;
        String Filepath = System.getProperty("user.dir").replace("\\","/")
                    +"/images/compressed/"+filename;
        File file = new File(Filepath);
        if(!Files.isReadable(file.toPath())){
            try {
                throw new FileNotFoundExcep("Error: file not found or file is not readable.");
            }catch (FileNotFoundExcep fnfe){
                System.out.println(fnfe.getMessage());
                System.exit(0);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader
                (Filepath))){
            String matrixSize= br.readLine();
            int i = (int)Math.sqrt(Integer.parseInt(matrixSize));
            rawImage = new int[i][i];
            String line;
            QTLinkedList = new LinkedList<>();
            while((line=br.readLine())!=null){
                QTLinkedList.add(Integer.parseInt(line));
            }
            QTNode rootQTNode = buildQuadTree(QTLinkedList);
            rawImage = buildRawImage(rootQTNode,Integer.parseInt(matrixSize));
            System.out.println("Uncompressed: "+filename);
            System.out.println("QTree: "+createPreOrder(rootQTNode));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawImage;
    }

    /**
     * Builds Quad Tree.
     * @param QTLinkedList stores the data read from file.
     * @return Root Node
     */
    private QTNode buildQuadTree(LinkedList<Integer> QTLinkedList){
        if (QTLinkedList.size()==0) return null;
        int elem=QTLinkedList.pop();
        if (elem==-1){
            QTNode qtNode = new QTNode(elem,
                    buildQuadTree(QTLinkedList),
                    buildQuadTree(QTLinkedList),
                    buildQuadTree(QTLinkedList),
                    buildQuadTree(QTLinkedList));
            return qtNode;
        }
        else return new QTNode(elem);
    }

    /**
     * Builds raw image from quad tree.
     * @param newQTNode root node.
     * @param matrixSize matrix size representing the number of pixels.
     * @return raw image i.e 2D representation of pixel values.
     */
    public int[][] buildRawImage(QTNode newQTNode, int matrixSize){
        int rows=(int)Math.sqrt(matrixSize),cols= (int)Math.sqrt(matrixSize);
        int[][] rawImage=new int[rows][cols];
        int[] ul= new int[]{0,0};
        parseInorder(newQTNode,rawImage,rows,ul);
        return rawImage;
    }

    /**
     * Recursive function to convert Quad Tree to 2D raw image.
     * @param newQTNode root node.
     * @param rawImage 2D array to store pixel data.
     * @param matrixSize size of the 2D array.
     * @param ul upper left start point for subtrees.
     */
    private void parseInorder(QTNode newQTNode,int[][] rawImage, int matrixSize,int[] ul){
        if(newQTNode.getValue()==-1){
            int factor=matrixSize/2;
            parseInorder(newQTNode.getUpperLeft(),rawImage,factor,ul);
            parseInorder(newQTNode.getUpperRight(),rawImage,factor,new int[]{ul[0],ul[1]+factor});
            parseInorder(newQTNode.getLowerLeft(),rawImage,factor,new int[]{ul[0]+factor,ul[1]});
            parseInorder(newQTNode.getLowerRight(),rawImage,factor,new int[]{ul[0]+factor,ul[1]+factor});
        }
        else{
            for(int i=ul[0];i<ul[0]+matrixSize;i++){
                for(int j=ul[1];j<ul[1]+matrixSize;j++){
                    rawImage[i][j] = newQTNode.getValue();
                }
            }
        }
    }

    /**
     * create preorder traversed string from quad Tree.
     * @param root root Node of the quadtree
     * @return preorder String.
     */
    private String createPreOrder(QTNode root){
        if (root!=null){
            preOrder+=root.getValue() +" ";
            createPreOrder(root.getUpperLeft());
            createPreOrder(root.getUpperRight());
            createPreOrder(root.getLowerLeft());
            createPreOrder(root.getLowerRight());
        }
        return preOrder;
    }
}
