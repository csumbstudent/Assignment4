import java.util.Arrays;
import java.util.Scanner;
public class Assignment4{
   public static void main(String[] args){
      String[] sImageIn =
      {
         "                                               ",
         "                                               ",
         "                                               ",
         "     * * * * * * * * * * * * * * * * * * * * * ",
         "     *                                       * ",
         "     ****** **** ****** ******* ** *** *****   ",
         "     *     *    ****************************** ",
         "     * **    * *        **  *    * * *   *     ",
         "     *   *    *  *****    *   * *   *  **  *** ",
         "     *  **     * *** **   **  *    **  ***  *  ",
         "     ***  * **   **  *   ****    *  *  ** * ** ",
         "     *****  ***  *  * *   ** ** **  *   * *    ",
         "     ***************************************** ",  
         "                                               ",
         "                                               ",
         "                                               "

      };
      String[] sImageIn_2 =
      {
            "                                          ",
            "                                          ",
            "* * * * * * * * * * * * * * * * * * *     ",
            "*                                    *    ",
            "**** *** **   ***** ****   *********      ",
            "* ************ ************ **********    ",
            "** *      *    *  * * *         * *       ",
            "***   *  *           * **    *      **    ",
            "* ** * *  *   * * * **  *   ***   ***     ",
            "* *           **    *****  *   **   **    ",
            "****  *  * *  * **  ** *   ** *  * *      ",
            "**************************************    ",
            "                                          ",
            "                                          ",
            "                                          ",
            "                                          "

      };
      BarcodeImage bi = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bi);
      dm.translateImageToText();
      dm.displayToConsole();
      dm.displayImageToConsole();
      
      bi = new BarcodeImage(sImageIn_2);
      dm.translateImageToText();
      dm.displayToConsole();
      dm.displayImageToConsole();
      
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayToConsole();
      dm.displayImageToConsole();
   }
}
class BarcodeImage implements Cloneable{
   public static final int MAX_HEIGHT = 30, MAX_WIDTH = 65;
   public static final char BLACK_CHAR = '*', WHITE_CHAR = ' ';
   private boolean[][] image_data;
   public BarcodeImage(){
      this.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for(boolean[] row : this.image_data)
         Arrays.fill(row, false);
   }
   public BarcodeImage(String[] data){
      //An image should always be rectangular.
      this();
      if(this.checkSize(data)){
         //If the size is valid, execution can continue.
         int offsetY = BarcodeImage.MAX_HEIGHT - data.length;
         int offsetX = BarcodeImage.MAX_WIDTH - data[0].length();
         for(int i = 0; i < data.length; i++){
            for(int x = 0; x < data[i].length(); x++){
               this.image_data[i+offsetY][x] = (data[i].charAt(x) == BarcodeImage.BLACK_CHAR) ? true : false;
            }
         }
      }
   }
   public BarcodeImage clone(){
      BarcodeImage newImage = new BarcodeImage();
      try{
         newImage = (BarcodeImage)super.clone();
         //Deep copy the image_data array
         if(this.image_data.length < 1)
            return newImage;
         newImage.image_data = new boolean[this.image_data.length][];
         if(this.image_data[0].length < 1)
            return newImage;
         for(int i = 0; i < this.image_data.length; i++){
            newImage.image_data[i] = Arrays.copyOf(this.image_data[i], this.image_data[i].length);
         }
      }
      catch(CloneNotSupportedException e){};
      return newImage;
      
   }
   public void setPixel(int row, int col, boolean value){
      if(this.image_data.length > row && this.image_data[0].length > col)
         this.image_data[row][col] = value;
   }
   public boolean getPixel(int row, int col){
      if(this.image_data.length > row && this.image_data.length >= 1 && this.image_data[row].length > col){
         return this.image_data[row][col];  
      }
      return false;
   }
   private boolean checkSize(String[] data){
      if(data == null || data.length <= 0 || data.length > BarcodeImage.MAX_HEIGHT || data[0].length() == 0 || data[0].length() > MAX_WIDTH)
         return false;
      return true;
   }
   public void displayToConsole(){
      for(boolean[] row : this.image_data){
         for(boolean value : row){
            if(value == true)
               System.out.print(BarcodeImage.BLACK_CHAR);
            else
               System.out.print(BarcodeImage.WHITE_CHAR);
         }
         System.out.print("\n");
      }
   }
}
interface BarcodeIO{
   public boolean scan(BarcodeImage bc);
}

class DataMatrix implements BarcodeIO{
   public BarcodeImage image = new BarcodeImage();
   private String text = "";
   private int actualWidth = 0, actualHeight = 0;
   public DataMatrix(){
   }
   public DataMatrix(BarcodeImage image){
      this.image = image.clone();
      this.cleanImage(this.image);
      this.actualWidth = this.computeSignalWidth();
      this.actualHeight = this.computeSignalHeight();
   }
   public boolean scan(BarcodeImage bc){
      return true;  
   }
   private void cleanImage(BarcodeImage image){
      //Beginning at the bottom of the image, move up until black is found.
      boolean doCopy = false;
      int offsetX = 0, offsetY = image.MAX_HEIGHT - 1;
      for(int i = image.MAX_HEIGHT - 1; i > 0; i--){
         boolean blackFound = false;
         for(int x = 0; x < image.MAX_WIDTH; x++){
            boolean pixel = image.getPixel(i, x);
            if(pixel){
               //A black character has been found.
               doCopy = true;
               blackFound = true;
            }
            if(doCopy && blackFound){
               image.setPixel(i, x, false);
               image.setPixel(offsetY, offsetX, pixel);
               offsetX++;
            }
         }
         if(blackFound)
            offsetY--;
         if(!blackFound && doCopy)
            break;
         offsetX = 0;
      }
   }
   public boolean translateImageToText(){
      this.text = "";
      for(int i = 1; i < this.actualWidth; i++){
            this.text += readCharFromCol(i);
         }
      return true;
   }
   public char[] charToPowersOf2(char value){
      char[] powers = new char[8];
      Arrays.fill(powers, (char)0);
      for(int i = 7; i >= 0; i--){
         if((int)value % (int)Math.pow(2, i) < (int)value){
            powers[i] = 1;
            value = (char)((int)value % (int)Math.pow(2, i));
         }
      } 
      return powers;
   }
   public boolean readText(String text){
      this.text = text;
      return true;
   }
   private char readCharFromCol(int col){
      char value = 0;
      for(int i = this.image.MAX_HEIGHT - 1; i > (this.image.MAX_HEIGHT - 2 - this.actualHeight + 2); i--){
         int power = this.image.MAX_HEIGHT - 2 - i;
         if(this.image.getPixel(i, col) == true)
            value += (char)Math.pow(2, power);
      }
      return value;
   }
   private void writeCharToCol(int col, char value){
      this.image.setPixel(this.image.MAX_HEIGHT - 1, col, true);
      for(int i = this.image.MAX_HEIGHT - 2; i > this.image.MAX_HEIGHT - 10; i--){
         int power = this.image.MAX_HEIGHT - 2 - i + 1;
         int divisor = (int)Math.pow(2, power);
         if((int)value % divisor >= Math.pow(2, power - 1)){
            this.image.setPixel(i, col, true);
            if((int)value % divisor == (int)value)
               break;
         }
      }
      if(col % 2 == 0 && col >= 2)
         this.image.setPixel(this.image.MAX_HEIGHT - 10, col, true);
   }
   public boolean generateImageFromText(){
      this.image = new BarcodeImage();
      for(int i = this.image.MAX_HEIGHT - 1; i > (this.image.MAX_HEIGHT - 1) - 10; i--)
         this.image.setPixel(i, 0, true);
      for(int i = 0; i < this.text.length(); i++){
         this.writeCharToCol(i + 1, text.charAt(i));     
      }
      
      return true;
   }
   public void displayImageToConsole(){
      for(int i = this.image.MAX_HEIGHT - this.actualHeight; i < this.image.MAX_HEIGHT; i++){
         for(int x = 0; x < this.actualWidth; x++){
            if(this.image.getPixel(i, x))
               System.out.print(BarcodeImage.BLACK_CHAR);
            else
               System.out.print(BarcodeImage.WHITE_CHAR);
         }
         System.out.print("\n");
      }
   }
   public String getText(){
      return this.text;
   }
   public int getActualHeight(){
      return this.actualHeight;
   }
   public int getActualWidth(){
      return this.actualWidth;  
   }
   private int computeSignalHeight(){
      int height = 0;
      if(this.image.getPixel(this.image.MAX_HEIGHT-1, 0) == false)
         return 0;
      for(int i = this.image.MAX_HEIGHT - 1; i > 0; i--){
         if(this.image.getPixel(i, 0) == true)
            height++;
         else
            break;
      }
      return height;
   }
   public void displayToConsole(){
      System.out.println(this.text);
   }
   private int computeSignalWidth(){
      int width = 0;
      if(this.image.getPixel(this.image.MAX_HEIGHT-1, 0) == false)
         return 0;
      for(int i = 0; i < this.image.MAX_WIDTH; i++){
           if(this.image.getPixel(this.image.MAX_HEIGHT-1, i) == true)
              width++;
           else
              break;
      }
      return width;
   }
}