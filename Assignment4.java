/* Assignment 4: Barcode Reader
   Description: This program defines the classes necessary
   to read and write string data from and to a text-based 
   representation of a QR-like image, where data is encoded
   as a binary picture. This program demonstrates the
   functionality of the classes with a basic driver program.
   
   Team
   -------------------
   Christopher Rendall
   Caroline Lancaster
   Daniel Kushner   
*/

import java.util.Arrays;
import java.util.Scanner;
/* Class Assignment4
   Description: This class provides the entry point main()
                for this program. */
public class Assignment4{
   
   /* main
      Description: The entry point for this program.
      In:  A string array of arguments - not used.
      Out: Nothing */
   public static void main(String[] args){
      //Two string objects hold unique barcode images
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
      //Perform the necessary steps as shown in the assignmetn specifications
      BarcodeImage bi = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bi);
      
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      bi = new BarcodeImage(sImageIn_2);
      dm.scan(bi);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      //Request a string from the user to create their own barcode image.
      Scanner keyIn = new Scanner(System.in);
      String text = "";
      System.out.println("Create your own barcode image!");
      do{
         System.out.println("Enter a string: ");
         text = keyIn.nextLine();
         if(text.length() < 1 || text.length() > BarcodeImage.MAX_WIDTH - 3)
            System.out.println("The string must be between 1 and " + (BarcodeImage.MAX_WIDTH - 3) + " characters.");
      } while(!dm.readText(text));
      dm.generateImageFromText();
      System.out.println("Your Barcode:");
      dm.displayImageToConsole();
      System.out.println("You entered the following string: ");
      dm.translateImageToText(); //Translate the image to text, just to show that this functionality works.
      dm.displayTextToConsole();
   }
}
/* Class BarcodeImage
   Description: This class is a logical representation of a barcode image.
                Because BarcodeImages are binary, the internal image data
                is represented as a 2-d boolean array. This class provides
                functions necessary to create and manipulate the internal
                image data. This class implements the Cloneable interface,
                and provides a clone() function to deep copy the image data.*/
class BarcodeImage implements Cloneable{
   //The maximum width and height of the internal image.
   public static final int MAX_HEIGHT = 30, MAX_WIDTH = 65;
   //BLACK_CHAR and WHITE_CHAR are defined here, as they seem to belong to this class.
   //The BarcodeImage(String[]) constructor does not make sense without having defined
   //values for a character representation of true and false. It also does not make
   //sense for BarcodeImage to know about or rely on DataMatrix for these values.
   public static final char BLACK_CHAR = '*', WHITE_CHAR = ' ';
   private boolean[][] image_data;
   /* BarcodeImage()
      Description: A constructor. Sets the image_data to a "blank" image.
      In:  Nothing
      Out: Nothing */
   public BarcodeImage(){
      this.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for(boolean[] row : this.image_data)
         Arrays.fill(row, false);
   }
   /* BarcodeImage(String[])
      Description: A constructor. Crates an internal representation of the image
                   in the String[] parameter.
      In:  A 1-d String array containing a barcode image.
      Out: Nothing */
   public BarcodeImage(String[] data){
      //Call the default constructor to create the blank internal canvas.
      this();
      //If data can be drawn on the internal canvas, do so.
      if(this.checkSize(data)){
         //offsetY is used to draw the image in the lower left of the canvas.
         int offsetY = BarcodeImage.MAX_HEIGHT - data.length;
         for(int i = 0; i < data.length; i++){
            for(int x = 0; x < data[i].length(); x++){
               //If the character at the current position is a BLACK_CHAR, set the current position on the canvas
               //to true. Otherwise, set it to false.
               this.image_data[i+offsetY][x] = (data[i].charAt(x) == BarcodeImage.BLACK_CHAR) ? true : false;
            }
         }
      }
   }
   /* BarcodeImage clone()
      Description: An overridden clone function that produces a deep copy of the
                   BarcodeImage object.
      In:  Nothing
      Out: A cloned BarcodeImage object */
   public BarcodeImage clone(){
      BarcodeImage newImage = new BarcodeImage();
      //Super.clone() may return CloneNotSupportedException, so try and catch that.
      try{
         newImage = (BarcodeImage)super.clone();
         
         //Deep copy the image_data array
         
         //If the source image is empty, return an empty image.
         if(this.image_data.length < 1)
            return newImage;
         newImage.image_data = new boolean[this.image_data.length][];
         //If the source image has one row, but no columns, return the image now.
         if(this.image_data[0].length < 1)
            return newImage;
         //Copy each row into the copy's image_data.
         for(int i = 0; i < this.image_data.length; i++){
            newImage.image_data[i] = Arrays.copyOf(this.image_data[i], this.image_data[i].length);
         }
      }
      catch(CloneNotSupportedException e){};
      return newImage;
      
   }
   /* void setPixel(int, int, boolean)
      Description: This function sets the value at image_data[y][x] to the boolean value 
                   passed to it.
      In:  The affected row and column, and a boolean value to set the pixel to.
      Out: Nothing */
   public void setPixel(int row, int col, boolean value){
      //If the row and column specified are out of ounds, do nothing.
      if(this.image_data.length > row && this.image_data[0].length > col)
         this.image_data[row][col] = value;
   }
   /* boolean getPixel(int row, int col)
      Description: This function returns the boolean value at the specified row
                   and column. */
   public boolean getPixel(int row, int col){
      if(this.image_data.length > row && this.image_data.length >= 1 && this.image_data[row].length > col){
         return this.image_data[row][col];  
      }
      return false;
   }
   /* boolean checkSize(String[] data)
      Description: This function returns a boolean value indicating whether the 
                   size of the string array data can produce a 2-d boolean array
                   as determined by the bounds of MAX_WIDTH and MAX_HEIGHT and the
                   validity of the data contained with the string array.*/
   private boolean checkSize(String[] data){
      //If data is nothing, is empty, is greater than the allowable maximum, less than the allowable maximum, or if the width
      //of the rows within it are 0, then return false; it will not produce a valid 2-d boolean array.
      for(String row : data)
         if(row.length() == 0 || row.length() > BarcodeImage.MAX_WIDTH)
            return false;
      if(data == null || data.length <= 0 || data.length > BarcodeImage.MAX_HEIGHT)
         return false;
      return true;
   }
   /* void displayToConsole()
      Description: This function prints a visual representation of the data contained in image_data.
                   The visual characters associated with each boolean value is defined as part
                   of this class.*/
   public void displayToConsole(){
      for(boolean[] row : this.image_data){
         for(boolean value : row){
            if(value)
               System.out.print(BarcodeImage.BLACK_CHAR);
            else
               System.out.print(BarcodeImage.WHITE_CHAR);
         }
         System.out.print("\n");
      }
   }
}
/* interface BarcodeIO
   Description: This interface guarantees that the implementation has
                the functions necessary to perform basic input and output
                with barcode images. */
interface BarcodeIO{
   //An implementation must be able to scan a barcode image.
   public boolean scan(BarcodeImage bc);
   //An implementation must be able to read in text for barcode generation.
   public boolean readText(String text);
   //An implementation must be able to generate an image from the text.
   public boolean generateImageFromText();
   //An implementation must be able to translate an image to text.
   public boolean translateImageToText();
   //An implementation must be able to display the text stored within it to the console.
   public void displayTextToConsole();
   //An implementation must be able to display the image stored within it to the console.
   public void displayImageToConsole();
}

/* class DataMatrix
   Description: This class implements the interface BarcodeIO. It provides
                the ability to read text strings and textual representations
                of barcode images. It can convert a barcode image to text,
                and text to a barcode image.*/
class DataMatrix implements BarcodeIO{
   public BarcodeImage image;
   private String text = "";
   private int actualWidth = 0, actualHeight = 0;
   /* DataMatrix()
      Description: This is a basic constructor. It initializes the BarcodeImage member.*/
   public DataMatrix(){
      this.image = new BarcodeImage();
   }
   /* DataMatrix(BarcodeImage image)
      Description: This constructor calls the default constructor then scans the passed image
                   into its BarcodeImage data member.*/
   public DataMatrix(BarcodeImage image){
      this();
      this.scan(image);
   }
   /* public boolean scan(BarcodeImage bc)
      Description: This function creates a copy of bc to store in its internal data
                   member. It then cleans the image and calcultes the actual width
                   and height of the signal. If the signal exists and it is 10 pixels
                   tall, then the image is considered valid and this function returns
                   true. */
   public boolean scan(BarcodeImage bc){
      //Clone bc and store the clone in image. bc must be cloned, otherwise
      //any changes to the object at the memory location stored in bc will
      //change this.image as well, since objects are references.
      this.image = bc.clone();
      this.cleanImage(this.image);
      this.actualWidth = this.computeSignalWidth();
      this.actualHeight = this.computeSignalHeight(); 
      if(this.actualWidth > 0 && this.actualHeight == 10)
         return true;  
      return false;
   }
   /* void cleanImage(BarcodeImage image)
      Description: This function moves the signal stored in image to the lower
                   left corner of the image. This does not need to return
                   a BarcodeImage because objects are references. Any changes
                   made to image will be made to the object in the calling
                   function as well.
                   
                   This function copies the signal to the lower left of the
                   image as it runs. No helper functions are used, as it is
                   efficient enough, and only requires at most one iteration
                   through the image.*/
   private void cleanImage(BarcodeImage image){
      //doCopy determines whether to copy the pixel beign iterated over to
      //the corresponding position in the left bottom of the image.
      boolean doCopy = false;
      //offsetX and offsetY are the location at which to place the pixel
      //being iterated over.
      int offsetX = 0, offsetY = image.MAX_HEIGHT - 1;
      for(int i = image.MAX_HEIGHT - 1; i > 0; i--){
         //blackFound is set to true if a black pixel is found on the current row.
         boolean blackFound = false;
         //This for loop iterates through each pixel on the current row.
         for(int x = 0; x < image.MAX_WIDTH; x++){
            boolean pixel = image.getPixel(i, x);
            //If the pixel at the current position is set to true, then
            //"black" has been found on this line.
            if(pixel){
               //doCopy is set to true. If this is the first "black" pixel
               //found in the image, then copying will begin.
               doCopy = true;
               //Black has been found on the current line.
               blackFound = true;
            }
            //If copying is in progress and black has been found on the current line
            //then set the image at the current location to false. Then, set the
            //image at the offset position to the value of pixel.
            if(doCopy && blackFound){
               image.setPixel(i, x, false);
               image.setPixel(offsetY, offsetX, pixel);
               offsetX++;
            }
         }
         //If black was found on the line, then move "up" to the next offset line.
         if(blackFound)
            offsetY--;
         //If black was not found on the current line, but copying is in progress,
         //then the signal has ended. The loop can be broken from.
         if(!blackFound && doCopy)
            break;
         offsetX = 0;
      }
   }
   /* boolean translateImageToText()
      Description: This function translates the internal barcode image to text
                   and then stores that text in the text data member. This returns
                   true if the text can be read, and false if the image cannot be
                   read. */
   public boolean translateImageToText(){
      this.text = "";
      //If the barcode image contains no signal, or the height of the signal
      //is greater than or less than 10 (since a byte is 8 pixels tall - 2^0 - 2^7,
      //and two rows are needed for the spine/alternating top).
      if(this.actualWidth < 1 || this.actualHeight != 10)
         return false;
      //Read each column from the image and append each character to the text data member.
      for(int i = 1; i < this.actualWidth - 1; i++){
            this.text += readCharFromCol(i);
         }
      return true;
   }
   /* boolean readText(String text)
      Description: This function stores the string argument into the internal text data member
                   if it is valid. If text is valid, then it returns true. Otherwise, it
                   returns false.*/
   public boolean readText(String text){
      //If text contains something and is not too long, then it is valid and can be stored.
      if(text != null && text.length() > 0 && text.length() <= BarcodeImage.MAX_WIDTH - 3){
         this.text = text;
         return true;
      }
      return false;
   }
   /* char readCharFromCol(int col)
      Description: This function returns the character stored in the specified column.*/
   private char readCharFromCol(int col){
      //value will hold the final calculated value of the column. Initialize it to 0.
      char value = 0;
      //Iterate through each row of the signal, starting at the bottom.
      for(int i = this.image.MAX_HEIGHT - 1; i > (this.image.MAX_HEIGHT - this.actualHeight); i--){
         //As i is decremented and this loop moves up through the signal, increment the power
         //of 2 represented by the current row.
         int power = this.image.MAX_HEIGHT - 2 - i;
         //If the pixel in the current row and column is set to true, then
         //its value is added to value.
         if(this.image.getPixel(i, col) == true)
            value += (char)Math.pow(2, power);
      }
      return value;
   }
   /* void writeCharToCol(int col, char value)
      Description: This function writes the character value to the specified column.*/
   private void writeCharToCol(int col, char value){
      //Set the very bottom pixel of the current column to solid, as it is part
      //of the spine.
      this.image.setPixel(this.image.MAX_HEIGHT - 1, col, true);
      //This loop iterates through each of the eright rows that makes up a column, starting at the bottom.
      for(int i = this.image.MAX_HEIGHT - 2; i > this.image.MAX_HEIGHT - 10; i--){
         //As i is decremented and this loop moves up through the signal, increment the power
         //of 2 represented by the row above the current row.
         //The row above the current row is used to determine whether the current row should
         //be set to true.
         int power = this.image.MAX_HEIGHT - 2 - i + 1;
         //The divisor is the actual numerical value represented by the current row.
         int divisor = (int)Math.pow(2, power);
         //If modulus of value % divisor is greater than or equal to the value represented by the
         //current row, then set the current row to true.
         if((int)value % divisor >= Math.pow(2, power - 1)){
            this.image.setPixel(i, col, true);
            //Once the value of the divisor is greater than value, the modulus
            //will be equal to value. At this point, the loop can break, as no more
            //rows will be set to true.
            if((int)value % divisor == (int)value)
               break;
         }
      }
      //Draw a black pixel above even columns, as specified by the assignment
      //directions.
      if(col % 2 == 0 && col >= 2)
         this.image.setPixel(this.image.MAX_HEIGHT - 10, col, true);
   }
   /* boolean generateImageFromText()
      Description: This function sets the text data member to value contained
                   within the image data member. It returns false if the text
                   cannot be drawn.*/
   public boolean generateImageFromText(){
      //Start with a blank canvas.
      this.image = new BarcodeImage();
      //Set the left spine to true.
      for(int i = this.image.MAX_HEIGHT - 1; i > (this.image.MAX_HEIGHT - 1) - 10; i--)
         this.image.setPixel(i, 0, true);
      //Iterate through each character in text
      for(int i = 0; i < this.text.length(); i++){
         //Write the character to the current column.
         this.writeCharToCol(i + 1, text.charAt(i));
         //If the last character has been drawn, then
         //write character value 170, which will set
         //every other row to true, creating the 
         //right border of the image.
         if(i == this.text.length() - 1){
            this.writeCharToCol(i + 2, (char)170);
         }
      }
      //computer the actual width and height of the iamge.
      this.actualWidth = this.computeSignalWidth();
      this.actualHeight = this.computeSignalHeight();
      //If a valid image was drawn, then return true.
      if(this.actualWidth > 0 && this.actualHeight == 10)
         return true;
      return false;
   }
   /* void displayImageToConsole()
      Description: This function displays the signal stored in the image data
                   member to the console, with a border.*/
   public void displayImageToConsole(){
      for(int i = 0; i <= this.actualWidth + 1; i++)
         System.out.print("-");
      System.out.print("\n");
      for(int i = this.image.MAX_HEIGHT - this.actualHeight; i < this.image.MAX_HEIGHT; i++){
         System.out.print("|");
         for(int x = 0; x < this.actualWidth; x++){
            if(this.image.getPixel(i, x))
               System.out.print(BarcodeImage.BLACK_CHAR);
            else
               System.out.print(BarcodeImage.WHITE_CHAR);
         }
         System.out.print("|\n");
      }
      for(int i = 0; i <= this.actualWidth + 1; i++)
         System.out.print("-");
      System.out.print("\n");
   }
   /* String getText()
      Description: Basic accessor function for the text data member.*/
   public String getText(){
      return this.text;
   }
   /* int getActualHeight()
      Description: Basic accessor function for the actualHeight data member.*/
   public int getActualHeight(){
      return this.actualHeight;
   }
   /* int getActualWidth()
      Description: Basic accessor function for the actualWidth data member.*/
   public int getActualWidth(){
      return this.actualWidth;  
   }
   /* int computeSignalHeight()
      Description: This function computes the height of the signal stored
                   within the image data member.*/
   private int computeSignalHeight(){
      int height = 0;
      //If the bottom left pixel of the image is false, then no signal
      //is contained within the current image.
      if(this.image.getPixel(this.image.MAX_HEIGHT-1, 0) == false)
         return 0;
      //Starting at the bottom left, count the number of rows that
      //contain the spine of the signal.
      for(int i = this.image.MAX_HEIGHT - 1; i > 0; i--){
         if(this.image.getPixel(i, 0) == true)
            height++;
         else
            break;
      }
      return height;
   }
   /* void displayTextToConsole()
      Description: This function display the value of the text data member to the console.*/
   public void displayTextToConsole(){
      System.out.println(this.text);
   }
   /* int computeSignalHeight()
      Description: This function computes the width of the signal stored
                   within the image data member.*/
   private int computeSignalWidth(){
      int width = 0;
      //If the bottom left pixel of the image is false, then no signal
      //is contained within the current image.
      if(this.image.getPixel(this.image.MAX_HEIGHT-1, 0) == false)
         return 0;
      //Starting at the very left, count the number of columns that contain
      //the spine of the signal.
      for(int i = 0; i < this.image.MAX_WIDTH; i++){
           if(this.image.getPixel(this.image.MAX_HEIGHT-1, i) == true)
              width++;
           else
              break;
      }
      return width;
   }
}