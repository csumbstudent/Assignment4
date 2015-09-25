public class Assignment5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
public interface BarcodeIO
{
	public boolean scan(BarcodeImage bc);
	public boolean readText(String text);
	public boolean generateImageFromText();
	public boolean translateImageToText();
	public void displayTextToConsole();
	public void displayImageToConsole();
}

public class BarcodeImage implements Cloneable
{
public static final int MAX_HEIGHT = 30; // rows
public static final int MAX_WIDTH = 65; // columns
private boolean[][] image_data;

public BarcodeImage()
{
  image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
  
  for(int i = 0; i < MAX_HEIGHT; i++)
  {
   for(int j = 0; j < MAX_WIDTH; j++)
   {
    image_data[i][j] = false;
   }
  }
}


public BarcodeImage(String[] str_data)
{
  image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
  
  for(int i = 0, k = 0; i < MAX_HEIGHT && k < str_data.length; i++, k++)
  {
   for(int j = 0; j < MAX_WIDTH && j < str_data[k].length(); j++)
   {
    if(str_data[k].charAt(j) == ' ')
     image_data[i][j] = false;
    else
     image_data[i][j] = true;
   
   }
  }
}
  

public boolean getPixel(int row, int col)
{
  return image_data[row][col];
}

public boolean setPixel(int row, int col, boolean value)
{
  if(row < MAX_HEIGHT && col < MAX_WIDTH)
  {
   image_data[row][col] = value;
   return true;
  }
  else
  {
   return false;
  }
}

public Object clone() throws CloneNotSupportedException
{
  BarcodeImage newBarcodeImage = (BarcodeImage) super.clone();  
  return newBarcodeImage;
}
}