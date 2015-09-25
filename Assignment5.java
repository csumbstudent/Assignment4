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
public class DataMatrix implements BarcodeIO
{
  public static final char BLACK_CHAR = '*';
  public static final char WHITE_CHAR = ' ';
  private BarcodeImage image;
  private String text;
  private int actualWidth;
  private int actualHeight;


  public DataMatrix()
  {
   image = new BarcodeImage();
   text = "";
  actualWidth = 0;
  actualHeight = 0;
  }


  public DataMatrix(BarcodeImage image)
  {
   if(!scan(image))
    image = new BarcodeImage();
   else
   {
    actualWidth = 0;
    actualHeight = 0;
   }

    text = "";
  }


  public DataMatrix(String text)
  {
    image = new BarcodeImage();
  
    if(!readText(text))
    text = "";
  
   actualWidth = 0;
    actualHeight = 0;  
  }


  public boolean readText(String text)
  {
   if(text == null)
     return false;

    this.text = text;
    return true;
  }


  public boolean scan(BarcodeImage image)
  {
   try
    {
    this.image = (BarcodeImage) image.clone();
    actualWidth = computeSignalWidth();
    actualHeight = computeSignalHeight();
    return true;   
    }
    catch(CloneNotSupportedException ex)
    {
    }
  
   return false;
  }


  public int actualWidth()
  {
   return actualWidth;
  }


  public int actualHeight()
  {
   return actualHeight;
  }

  private int computeSignalWidth()
  {
   int i = 1;

    while(image.getPixel(BarcodeImage.MAX_HEIGHT - i, 0)
      && (i != BarcodeImage.MAX_HEIGHT))
     i++;

   return i - 1;
  }


  private int computeSignalHeight()
  {
    int i = 0;

   while(image.getPixel(29, i) && (i != BarcodeImage.MAX_WIDTH - 1))
    i++;

    return i - 1;
  }

  public boolean generateImageFromText()
  {
      if(text == null)
   return false;

   image = new BarcodeImage();

    for(int i = 0; i < text.length(); i++)
    {
     image.setPixel(BarcodeImage.MAX_HEIGHT - 1, i, true);
    }

    pritChar(0, 255);

    for(int i = 1; i < text.length() + 1; i++)
   {
     if(!pritChar(i, (int) text.charAt(i)))
      return false;
    }

    actualWidth = computeSignalWidth();
    actualHeight = computeSignalHeight();

    return true;
  }

    public boolean translateImageToText()
  {
    String txt = "";
    int ch;

    if(image == null)
    return false;

    for(int i = 0; i < text.length(); i++)
    {
    ch = 0;
    for(int j = BarcodeImage.MAX_HEIGHT - 2; j > BarcodeImage.MAX_HEIGHT - 10; j--)
    {
     if(image.getPixel(j, i))
      ch += Math.pow(2, j - BarcodeImage.MAX_HEIGHT - 2);
    }
    txt += (char) ch;
    }

    text = txt;

    actualWidth = computeSignalWidth();
    actualHeight = computeSignalHeight();
    return true;
  }

  public void displayTextToConsole()
  {
   System.out.println(text);
  }


  public void displayImageToConsole()
  {
   for(int i = 0; i < text.length() + 1; i++)
   {
     for(int j = BarcodeImage.MAX_HEIGHT - actualHeight - 10; j < BarcodeImage.MAX_HEIGHT; j--)
      if(image.getPixel(i, j))
      System.out.print(BLACK_CHAR);
      else
       System.out.println(WHITE_CHAR);
    System.out.println();
    }
  }


  private boolean printChar(int col, int code)
  {
   String str1 = Integer.toBinaryString(code);
   Boolean[] bool = new Boolean[8];
   int pos = 0;

    if(code < 0 || code > 255)
      return false;

    for(int i = 0; i < 8; i++)
      if(str1.charAt(7 - i) == '1')
        bool[i] = true;
      else
        bool[i] = false;

    for(int i = BarcodeImage.MAX_HEIGHT - 2; i < str1.length(); i++, pos++)
      image.setPixel(i, col, bool[pos]);

    return false;

    }
    }
  }