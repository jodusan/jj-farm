package rafgfxlib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Set statičnih pomoćnih funkcija za rad sa grafikom 
 * @author Aleksandar Stančić
 *
 */
public class Util 
{
	/**
	 * Kreira novi {@link WritableRaster} u RGB ili RGBA modu, nezavisan od postojećih slika.
	 * @param width širina u pixelima
	 * @param height Visina u pixelima
	 * @param alpha Da li će imati alpha kanal (RGB/RGBA)
	 * @return Novi {@link WritableRaster} objekat
	 */
	public static WritableRaster createRaster(int width, int height, boolean alpha)
	{
		if(alpha)
		{
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			return image.getRaster();
		}
		else
		{
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			return image.getRaster();
		}
	}
	
	/**
	 * Kreira novu {@link BufferedImage} sliku, postavljajući joj sadržaj na zadati {@link WritableRaster}.
	 * @param raster Raster sa željenim pixelima
	 * @return {@link BufferedImage} objekat sa zadatim sadržajem
	 */
	public static BufferedImage rasterToImage(WritableRaster raster)
	{
		if(raster.getNumBands() == 3) // RGB
		{
			BufferedImage image = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			image.setData(raster);
			return image;
		}
		else if(raster.getNumBands() == 4) // RGBA
		{
			BufferedImage image = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			image.setData(raster);
			return image;
		}
		else
		{
			System.out.println("LOLWUT?");
			return null;
		}
	}
	
	/**
	 * Pretvara zadati raster u 3D float matricu, mapirajući RGB(A) vrijednosti
	 * od 0 do 255 u opseg od 0 do 1. Matrica će biti dimenzija :
	 * [širina u px] x [visina u px] x [3 ili 4 kanala]
	 * @param raster Raster u RGB ili RGBA formatu
	 * @return 3D float matrica
	 */
	public static float[][][] rasterToFloatMap(WritableRaster raster)
	{
		if(raster.getNumBands() == 3) // RGB
		{
			float[][][] frgb = new float[raster.getWidth()][raster.getHeight()][3];
			int[] irgb = new int[3];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					raster.getPixel(x, y, irgb);
					frgb[x][y][0] = irgb[0] / 255.0f;
					frgb[x][y][1] = irgb[1] / 255.0f;
					frgb[x][y][2] = irgb[2] / 255.0f;
				}
			}
			return frgb;
		}
		else if(raster.getNumBands() == 4) // RGBA
		{
			float[][][] frgb = new float[raster.getWidth()][raster.getHeight()][4];
			int[] irgb = new int[4];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					raster.getPixel(x, y, irgb);
					frgb[x][y][0] = irgb[0] / 255.0f;
					frgb[x][y][1] = irgb[1] / 255.0f;
					frgb[x][y][2] = irgb[2] / 255.0f;
					frgb[x][y][3] = irgb[3] / 255.0f;
				}
			}
			return frgb;
		}
		else
		{
			System.out.println("LOLWUT?");
			return null;
		}
	}
	
	/**
	 * Zapisuje 0-1 float matricu nazad u raster, mapirajući vrijednosti kanala
	 * na cjelobrojni 0-255 opseg, clampujući vrijednosti iznad i ispod.
	 * @param fmap 3D float matrica (pogledati {@link rasterToFloatMap})
	 * @param raster Raster u koji se vrši upis, mora se podudarati po dimenzijama i broju kanala
	 * @return Uspjeh operacije
	 */
	public static boolean writeFloatMapToRaster(float fmap[][][], WritableRaster raster)
	{
		if(fmap[0][0].length != raster.getNumBands())
		{
			System.out.println("Channel number mismatch!");
			return false;
		}
		
		if(fmap.length != raster.getWidth())
		{
			System.out.println("Width mismatch!");
			return false;
		}
		
		if(fmap[0].length != raster.getHeight())
		{
			System.out.println("Height mismatch!");
			return false;
		}
		
		if(raster.getNumBands() == 3) // RGB
		{
			int[] rgb = new int[3];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					rgb[0] = (int)(fmap[x][y][0] * 255.0f);
					rgb[1] = (int)(fmap[x][y][1] * 255.0f);
					rgb[2] = (int)(fmap[x][y][2] * 255.0f);
					
					if(rgb[0] < 0) rgb[0] = 0;
					if(rgb[1] < 0) rgb[1] = 0;
					if(rgb[2] < 0) rgb[2] = 0;
					
					if(rgb[0] > 255) rgb[0] = 255;
					if(rgb[1] > 255) rgb[1] = 255;
					if(rgb[2] > 255) rgb[2] = 255;
					
					raster.setPixel(x, y, rgb);
				}
			}
		}
		else
		{
			int[] rgba = new int[4];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					rgba[0] = (int)(fmap[x][y][0] * 255.0f);
					rgba[1] = (int)(fmap[x][y][1] * 255.0f);
					rgba[2] = (int)(fmap[x][y][2] * 255.0f);
					rgba[3] = (int)(fmap[x][y][3] * 255.0f);
					
					if(rgba[0] < 0) rgba[0] = 0;
					if(rgba[1] < 0) rgba[1] = 0;
					if(rgba[2] < 0) rgba[2] = 0;
					if(rgba[3] < 0) rgba[3] = 0;
					
					if(rgba[0] > 255) rgba[0] = 255;
					if(rgba[1] > 255) rgba[1] = 255;
					if(rgba[2] > 255) rgba[2] = 255;
					if(rgba[3] > 255) rgba[3] = 255;
					
					raster.setPixel(x, y, rgba);
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Rasterizacija matrice float vrijednosti u RGB(A) raster linearno interpolirajući između dvije RGB(A) boje.
	 * @param fmap 2D float matrica
	 * @param fmin minimum opsega float vrijednosti (one ispod će biti zaokružene na ovu)
	 * @param fmax maksimum opsega float vrijednosti (one preko će biti zaokružene na ovu)
	 * @param colMin boja koja odgovara fmin vrijednosti
	 * @param colMax boja koja odgovara fmax vrijednosti
	 * @param raster raster u koji će biti upisana slika
	 * @return uspjeh operacije, false ako se dimenzije ne poklapaju ili raster nije podržanog tipa
	 */
	public static boolean mapFloatMapToRaster(float fmap[][], float fmin, float fmax, int[] colMin, int[] colMax, WritableRaster raster)
	{
		if(fmap.length != raster.getWidth())
		{
			System.out.println("Width mismatch!");
			return false;
		}
		
		if(fmap[0].length != raster.getHeight())
		{
			System.out.println("Height mismatch!");
			return false;
		}
		
		if(raster.getNumBands() == 3) // RGB
		{
			int[] rgb = new int[3];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					float grad = (fmap[x][y] - fmin) / (fmax - fmin);
					if(grad < 0.0f) grad = 0.0f;
					if(grad > 1.0f) grad = 1.0f;
					lerpRGBi(colMin, colMax, grad, rgb);
					
					if(rgb[0] < 0) rgb[0] = 0;
					if(rgb[1] < 0) rgb[1] = 0;
					if(rgb[2] < 0) rgb[2] = 0;
					
					if(rgb[0] > 255) rgb[0] = 255;
					if(rgb[1] > 255) rgb[1] = 255;
					if(rgb[2] > 255) rgb[2] = 255;
					
					raster.setPixel(x, y, rgb);
				}
			}
		}
		else
		{
			int[] rgba = new int[4];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					float grad = (fmap[x][y] - fmin) / (fmax - fmin);
					if(grad < 0.0f) grad = 0.0f;
					if(grad > 1.0f) grad = 1.0f;
					lerpRGBi(colMin, colMax, grad, rgba);
					
					if(rgba[0] < 0) rgba[0] = 0;
					if(rgba[1] < 0) rgba[1] = 0;
					if(rgba[2] < 0) rgba[2] = 0;
					if(rgba[3] < 0) rgba[3] = 0;
					
					if(rgba[0] > 255) rgba[0] = 255;
					if(rgba[1] > 255) rgba[1] = 255;
					if(rgba[2] > 255) rgba[2] = 255;
					if(rgba[3] > 255) rgba[3] = 255;
					
					raster.setPixel(x, y, rgba);
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Rasterizacija matrice float vrijednosti u RGB(A) raster linearno interpolirajući kroz gradijent (array) boja.
	 * @param fmap 2D float matrica
	 * @param fmin minimum opsega float vrijednosti (one ispod će biti zaokružene na ovu)
	 * @param fmax maksimum opsega float vrijednosti (one preko će biti zaokružene na ovu)
	 * @param gradient niz RGB(A) boja koje će biti proporcionalno raspoređene u gradijent
	 * @param raster raster u koji će biti upisana slika
	 * @return uspjeh operacije, false ako se dimenzije ne poklapaju ili raster nije podržanog tipa
	 */
	public static boolean mapFloatMapViaGradient(float fmap[][], float fmin, float fmax, int[][] gradient, WritableRaster raster)
	{
		if(fmap.length != raster.getWidth())
		{
			System.out.println("Width mismatch!");
			return false;
		}
		
		if(fmap[0].length != raster.getHeight())
		{
			System.out.println("Height mismatch!");
			return false;
		}
		
		if(raster.getNumBands() == 3) // RGB
		{
			int[] rgb = new int[3];
			for(int y = 0; y < raster.getHeight(); ++y)
			{
				for(int x = 0; x < raster.getWidth(); ++x)
				{
					float grad = (fmap[x][y] - fmin) / (fmax - fmin);
					gradientSample(gradient, grad, rgb);
					
					if(rgb[0] < 0) rgb[0] = 0;
					if(rgb[1] < 0) rgb[1] = 0;
					if(rgb[2] < 0) rgb[2] = 0;
					
					if(rgb[0] > 255) rgb[0] = 255;
					if(rgb[1] > 255) rgb[1] = 255;
					if(rgb[2] > 255) rgb[2] = 255;
					
					raster.setPixel(x, y, rgb);
				}
			}
			
			return true;
		}
		else
		{
			System.out.println("FAIL");
			return false;
		}
	}
	
	/**
	 * Skalira jednu 2D float matricu u drugu, bilinearno interpolirajući vrijednosti
	 * @param source izvorna matrica
	 * @param destination matrica u koju će biti upisane vrijednosti za njene dimenzije
	 */
	public static void floatMapRescale(float[][] source, float[][] destination)
	{
		int sourceW = source.length;
		int sourceH = source[0].length;
		int targetW = destination.length;
		int targetH = destination[0].length;
		
		for(int x = 0; x < targetW; x++)
		{
			float fx = (float)x / targetW;
			
			for(int y = 0; y < targetH; y++)
			{
				float fy = (float)y / targetH;
				
				float sx = fx * sourceW;
				float sy = fy * sourceH;
				
				int isx0 = (int)sx;
				int isy0 = (int)sy;
				int isx1 = isx0 + 1;
				int isy1 = isy0 + 1;
				if(isx1 >= sourceW) isx1 = sourceW - 1;
				if(isy1 >= sourceH) isy1 = sourceH - 1;
				
				sx -= isx0;
				sy -= isy0;
				
				float a = lerpF(source[isx0][isy0], source[isx1][isy0], sx);
				float b = lerpF(source[isx0][isy1], source[isx1][isy1], sx);
				
				destination[x][y] = lerpF(a, b, sy);
			}
		}
	}
	
	/**
	 * Obavlja MAD (multiply + add) operaciju nad dvije 2D float mape. Formula je:
	 * 
	 *  destination[x][y] = source[x][y] * multiplier
	 * 
	 * @param source izvorne vrijednosti
	 * @param destination matrica u koju se upisuje (mora biti istih dimenzija)
	 * @param multiplier vrijednost kojom će svako polje biti pomnoženo prije sabiranja
	 */
	public static void floatMapMAD(float[][] source, float[][] destination, float multiplier)
	{
		if(source.length != destination.length)
		{
			System.out.println("floatMapMAD width mismatch!");
			return;
		}
		
		if(source[0].length != destination[0].length)
		{
			System.out.println("floatMapMAD height mismatch!");
			return;
		}
		
		for(int x = 0; x < source.length; ++x)
		{
			for(int y = 0; y < source.length; ++y)
			{
				destination[x][y] += source[x][y] * multiplier;
			}
		}
	}
	
	/**
	 * Proizvodi niz boja (gradijent) od slike učitavajući sve boje iz njenog prvog reda
	 * @param image slika proizvoljne veličine (samo prvi red piksela će biti upotrijebljen)
	 * @return niz od image.getWidth() boja
	 */
	public static int[][] imageToGradient(BufferedImage image)
	{
		WritableRaster raster = image.getRaster();
		int[][] gradient = new int[image.getWidth()][raster.getNumBands()];
		for(int x = 0; x < image.getWidth(); x++)
			raster.getPixel(x, 0, gradient[x]);
		return gradient;
	}
	
	/**
	 * Obavlja linearnu interpolaciju kroz niz boja (gradijent), gdje će za x = 0 rezultat biti boja 
	 * na indeksu [0], a za x = 1 će biti [length - 1], sa interpoliranim rezultatima između. 
	 * @param gradient niz boja proizvoljne dužine
	 * @param x faktor za interpolaciju, od 0 do 1
	 * @param result unaprijed alociran niz od bar 3 elementa (za RGB) u koji će biti upisan rezultat
	 */
	public static void gradientSample(int[][] gradient, float x, int[] result)
	{
		if(x < 0.0f) x = 0.0f;
		if(x > 1.0f) x = 1.0f;
		
		x *= (gradient.length - 1);
		int ix0 = (int)x;
		x -= ix0;
		int ix1 = ix0 + 1;
		if(ix1 >= gradient.length) ix1 = gradient.length - 1;
		
		lerpRGBi(gradient[ix0], gradient[ix1], x, result);
	}
	
	/**
	 * Otvara "Open image" dijalog u kom korisnik može da odabere BMP, PNG, GIF ili JPEG sliku sa diska.
	 * @param startingDir Početni direktorijum, može biti null
	 * @param parentWindow Roditeljski prozor za dijalog, može biti null
	 * @return {@link BufferedImage} slika ako je uspješno, inače null
	 */
	public static BufferedImage browseForImage(String startingDir, Component parentWindow)
	{
		if(startingDir == null) startingDir = ".";
		
		JFileChooser fileChooser = new JFileChooser(startingDir);
		
		fileChooser.setMultiSelectionEnabled(false);
		
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() 
			{
				return "Supported image formats (BMP, PNG, GIF, JPEG)";
			}
			
			@Override
			public boolean accept(File f) 
			{
				String lowerName = f.getName().toLowerCase();
				
				if(f.isDirectory()) return true;
				return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || 
						lowerName.endsWith(".png") || lowerName.endsWith(".bmp") ||
						lowerName.endsWith(".gif") || f.isDirectory();
			}
		});
		
		int dlgResult = fileChooser.showDialog(parentWindow, "Open image...");
		
		if(dlgResult == JFileChooser.APPROVE_OPTION)
			return loadImage(fileChooser.getSelectedFile().getAbsolutePath());
		else
			return null;
	}
	
	/**
	 * Učitava sliku sa zadate putanje i vraća je kao {@link BufferedImage} objekat.
	 * @param fileName Putanja do slike
	 * @return {@link BufferedImage} objekat sa zadatom slikom ako je uspje�no, inaće null
	 */
	public static BufferedImage loadImage(String fileName)
	{
		if(fileName.startsWith("/"))
		{
			fileName = fileName.substring(1);
			URL imgURL = Util.class.getClassLoader().getResource(fileName);
			try
			{
				BufferedImage image = ImageIO.read(imgURL.toURI().toURL());
				return image;
			} 
			catch (IOException e)
			{
				return null;
			} 
			catch (URISyntaxException e)
			{
				return null;
			}
		}
		else
		{
			try 
			{
				BufferedImage image = ImageIO.read(new File(fileName));
				return image;
			} 
			catch (IOException e) 
			{
				return null;
			}
		}
	}
	
	/**
	 * Snima {@link BufferedImage} sliku iz memorije na disk u JPEG, BMP, GIF ili PNG formatu.
	 * @param image {@link BufferedImage} objekat slika koju treba snimiti
	 * @param fileName Naziv/putanja fajla, mora imati ekstenziju, upotrijebiće se za odabir formata.
	 * @return Uspjeh operacije
	 */
	public static boolean saveImage(BufferedImage image, String fileName)
	{
		String lowerName = fileName.toLowerCase();
		String format = "JPG";
		
		if(lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg"))
			format = "JPG";
		else if(lowerName.endsWith(".png"))
			format = "PNG";
		else if(lowerName.endsWith(".bmp"))
			format = "BMP";
		else
		{
			System.out.println("Invalid filename/format");
			return false;
		}
		
		try 
		{
			ImageIO.write(image, format, new File(fileName));
		} 
		catch (IOException e) 
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Prikazuje GUI dijalog za odabir boje, vraćajući odabranu boju kao RGB niz od 3 elementa. 
	 * @param color ako je naveden niz, u njega će biti upisana boja, ako ne, preuzeti return vrijednost
	 * @param title naslov dijaloga
	 * @param parent parent komponenta/prozor, ako postoji, smije biti null
	 * @return vraća novi niz sa bojom, ili onaj proslijeđen kroz color parametar
	 */
	public static int[] pickColor(int[] color, String title, Component parent)
	{
		if(color != null)
		{
			if(color.length < 3 || color.length > 4)
			{
				System.out.println("Color int[] array is NOT 3 or 4 elements long!");
				color = new int[3];
			}
		}
		
		if(color == null) color = new int[4];
		
		if(title == null) title = "Choose a color";
		
		Color rcolor = JColorChooser.showDialog(parent, title, Color.white);
		
		if(rcolor != null)
		{
			color[0] = rcolor.getRed();
			color[1] = rcolor.getGreen();
			color[2] = rcolor.getBlue();
			
			if(color.length == 4) color[3] = rcolor.getAlpha();
			
			return color;
		}
		else
		{
			return color;
		}
	}
	
	/**
	 * Linearna interpolacija (lerp) između dva int broja
	 * @param a počatak opsega
	 * @param b kraj opsega
	 * @param x faktor od 0 do 1
	 * @return interpolirana int vrijednost
	 */
	public static int lerpI(int a, int b, float x)
	{
		return (int)(a + (b - a) * x);
	}
	
	/**
	 * Linearna interpolacija (lerp) između dva float broja
	 * @param a počatak opsega
	 * @param b kraj opsega
	 * @param x faktor od 0 do 1
	 * @return interpolirana float vrijednost
	 */
	public static float lerpF(float a, float b, float x)
	{
		return a + (b - a) * x;
	}
	
	/**
	 * Linearna interpolacija (lerp) između dvije RGB int boje
	 * @param a početna boja
	 * @param b krajnja boja
	 * @param x faktor od 0 do 1
	 * @param result RGB int niz u koji će se upisati rezultat
	 */
	public static void lerpRGBi(int[] a, int[] b, float x, int[] result)
	{
		result[0] = (int)(a[0] + (b[0] - a[0]) * x);
		result[1] = (int)(a[1] + (b[1] - a[1]) * x);
		result[2] = (int)(a[2] + (b[2] - a[2]) * x);
	}
	
	/**
	 * Linearna interpolacija (lerp) između dvije RGBA int boje
	 * @param a početna boja
	 * @param b krajnja boja
	 * @param x faktor od 0 do 1
	 * @param result RGBA int niz u koji će se upisati rezultat
	 */
	public static void lerpRGBAi(int[] a, int[] b, float x, int[] result)
	{
		result[0] = (int)(a[0] + (b[0] - a[0]) * x);
		result[1] = (int)(a[1] + (b[1] - a[1]) * x);
		result[2] = (int)(a[2] + (b[2] - a[2]) * x);
		result[3] = (int)(a[3] + (b[3] - a[3]) * x);
	}
	
	/**
	 * Linearna interpolacija (lerp) između dvije RGB int boje, sa float rezultatom
	 * @param a početna boja
	 * @param b krajnja boja
	 * @param x faktor od 0 do 1
	 * @param result RGB float niz u koji će se upisati rezultat
	 */
	public static void lerpRGBif(int[] a, int[] b, float x, float[] result)
	{
		result[0] = a[0] + (b[0] - a[0]) * x;
		result[1] = a[1] + (b[1] - a[1]) * x;
		result[2] = a[2] + (b[2] - a[2]) * x;
	}
	
	/**
	 * Linearna interpolacija (lerp) između dvije RGBA int boje, sa float rezultatom
	 * @param a početna boja
	 * @param b krajnja boja
	 * @param x faktor od 0 do 1
	 * @param result RGBA float niz u koji će se upisati rezultat
	 */
	public static void lerpRGBAif(int[] a, int[] b, float x, float[] result)
	{
		result[0] = a[0] + (b[0] - a[0]) * x;
		result[1] = a[1] + (b[1] - a[1]) * x;
		result[2] = a[2] + (b[2] - a[2]) * x;
		result[3] = a[3] + (b[3] - a[3]) * x;
	}
	
	/**
	 * Linearna interpolacija (lerp) između dvije RGB float boje
	 * @param a početna boja
	 * @param b krajnja boja
	 * @param x faktor od 0 do 1
	 * @param result RGB float niz u koji će se upisati rezultat
	 */
	public static void lerpRGBf(float[] a, float[] b, float x, float[] result)
	{
		result[0] = a[0] + (b[0] - a[0]) * x;
		result[1] = a[1] + (b[1] - a[1]) * x;
		result[2] = a[2] + (b[2] - a[2]) * x;
	}
	
	/**
	 * Linearna interpolacija (lerp) između dvije RGBA int boje
	 * @param a početna boja
	 * @param b krajnja boja
	 * @param x faktor od 0 do 1
	 * @param result RGBA float niz u koji će se upisati rezultat
	 */
	public static void lerpRGBAf(float[] a, float[] b, float x, float[] result)
	{
		result[0] = a[0] + (b[0] - a[0]) * x;
		result[1] = a[1] + (b[1] - a[1]) * x;
		result[2] = a[2] + (b[2] - a[2]) * x;
		result[3] = a[3] + (b[3] - a[3]) * x;
	}
	
	/**
	 * Konverzija standardnog Java Color objekta u RGB int niz
	 * @param c boja koja se konvertuje
	 * @return int[3] niz koji sadrži RGB vrijednosti
	 */
	public static int[] colorToArray(Color c)
	{
		int[] rgb = new int[3];
		rgb[0] = c.getRed();
		rgb[1] = c.getGreen();
		rgb[2] = c.getBlue();
		return rgb;
	}
	
	/**
	 * Konverzija standardnog Java Color objekta u RGBA int niz
	 * @param c boja koja se konvertuje
	 * @return int[4] niz koji sadrži RGBA vrijednosti
	 */
	public static int[] colorToArrayA(Color c)
	{
		int[] rgb = new int[4];
		rgb[0] = c.getRed();
		rgb[1] = c.getGreen();
		rgb[2] = c.getBlue();
		rgb[3] = c.getAlpha();
		return rgb;
	}
	
	/**
	 * Ograničava vrijednosti iz int[3] niza na opseg 0 - 255
	 * @param color niz koji će biti ograničen
	 */
	public static void clampRGB(int[] color)
	{
		if(color[0] < 0) color[0] = 0;
		if(color[1] < 0) color[1] = 0;
		if(color[2] < 0) color[2] = 0;
		if(color[0] > 255) color[0] = 255;
		if(color[1] > 255) color[1] = 255;
		if(color[2] > 255) color[2] = 255;
	}
	
	/**
	 * Ograničava vrijednosti iz int[4] niza na opseg 0 - 255
	 * @param color niz koji će biti ograničen
	 */
	public static void clampRGBA(int[] color)
	{
		if(color[0] < 0) color[0] = 0;
		if(color[1] < 0) color[1] = 0;
		if(color[2] < 0) color[2] = 0;
		if(color[3] < 0) color[3] = 0;
		if(color[0] > 255) color[0] = 255;
		if(color[1] > 255) color[1] = 255;
		if(color[2] > 255) color[2] = 255;
		if(color[3] > 255) color[3] = 255;
	}
	
	/**
	 * Radi point sample (nearest-neighbour) nad rasterom, birajući boju na osnovu zaokruženih (u, v)
	 * koordinata, sa automatskom provjerom opsega (clamp-to-edge).
	 * @param src raster iz kog se čita
	 * @param u horizontalna (X) koordinata u pikselima
	 * @param v vertikalna (Y) koordinata u pikselima
	 * @param color niz u koji će biti upisana pročitana boja
	 */
	public static void pointSample(WritableRaster src, float u, float v, int[] color)
	{
		int x = (int)u;
		int y = (int)v;
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x >= src.getWidth()) x = src.getWidth() - 1;
		if(y >= src.getHeight()) x = src.getHeight() - 1;
		src.getPixel(x, y, color);
	}
	
	/**
	 * Radi bilinearni sample nad RGB rasterom, interpolirajući boju na osnovu (u, v)
	 * koordinata, sa automatskom provjerom opsega (clamp-to-edge).
	 * @param src raster iz kog se čita
	 * @param u horizontalna (X) koordinata u pikselima
	 * @param v vertikalna (Y) koordinata u pikselima
	 * @param color niz u koji će biti upisana interpolirana boja
	 */
	public static void bilSample(WritableRaster src, float u, float v, int[] color)
	{
		float[] a = new float[3];
		float[] b = new float[3];
		
		int[] UL = new int[3];
		int[] UR = new int[3];
		int[] LL = new int[3];
		int[] LR = new int[3];

		int x0 = (int)u;
		int y0 = (int)v;
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		
		u -= x0;
		v -= y0;
		
		if(x0 < 0) x0 = 0;
		if(y0 < 0) y0 = 0;
		if(x1 < 0) x1 = 0;
		if(y1 < 0) y1 = 0;
		
		if(x0 >= src.getWidth()) x0 = src.getWidth() - 1;
		if(y0 >= src.getHeight()) y0 = src.getHeight() - 1;
		if(x1 >= src.getWidth()) x1 = src.getWidth() - 1;
		if(y1 >= src.getHeight()) y1 = src.getHeight() - 1;
		
		src.getPixel(x0, y0, UL);
		src.getPixel(x1, y0, UR);
		src.getPixel(x0, y1, LL);
		src.getPixel(x1, y1, LR);
		
		lerpRGBif(UL, UR, u, a);
		lerpRGBif(LL, LR, u, b);
		
		color[0] = (int)(lerpF(a[0], b[0], v));
		color[1] = (int)(lerpF(a[1], b[1], v));
		color[2] = (int)(lerpF(a[2], b[2], v));
		
		clampRGB(color);
	}
	
	/**
	 * Radi bilinearni sample nad RGBA rasterom, interpolirajući boju na osnovu (u, v)
	 * koordinata, sa automatskom provjerom opsega (clamp-to-edge).
	 * @param src raster iz kog se čita
	 * @param u horizontalna (X) koordinata u pikselima
	 * @param v vertikalna (Y) koordinata u pikselima
	 * @param color niz u koji će biti upisana interpolirana boja
	 */
	public static void bilSampleA(WritableRaster src, float u, float v, int[] color)
	{
		float[] a = new float[4];
		float[] b = new float[4];
		
		int[] UL = new int[4];
		int[] UR = new int[4];
		int[] LL = new int[4];
		int[] LR = new int[4];

		int x0 = (int)u;
		int y0 = (int)v;
		int x1 = x0 + 1;
		int y1 = y0 + 1;
		
		u -= x0;
		v -= y0;
		
		if(x0 < 0) x0 = 0;
		if(y0 < 0) y0 = 0;
		if(x1 < 0) x1 = 0;
		if(y1 < 0) y1 = 0;
		
		if(x0 >= src.getWidth()) x0 = src.getWidth() - 1;
		if(y0 >= src.getHeight()) y0 = src.getHeight() - 1;
		if(x1 >= src.getWidth()) x1 = src.getWidth() - 1;
		if(y1 >= src.getHeight()) y1 = src.getHeight() - 1;
		
		src.getPixel(x0, y0, UL);
		src.getPixel(x1, y0, UR);
		src.getPixel(x0, y1, LL);
		src.getPixel(x1, y1, LR);
		
		lerpRGBAif(UL, UR, u, a);
		lerpRGBAif(LL, LR, u, b);
		
		color[0] = (int)(lerpF(a[0], b[0], v));
		color[1] = (int)(lerpF(a[1], b[1], v));
		color[2] = (int)(lerpF(a[2], b[2], v));
		color[2] = (int)(lerpF(a[3], b[3], v));
		
		clampRGBA(color);
	}
	
	/**
	 * Sabire RGB vrijednosti, po principu: base += addition
	 * @param base početna vrijednost, na koju se sabire
	 * @param addition sabirak
	 */
	public static void addRGB(int[] base, int[] addition)
	{
		base[0] += addition[0];
		base[1] += addition[1];
		base[2] += addition[2];
	}
	
	/**
	 * Sabire dvije RGBA vrijednosti, po principu: base += addition
	 * @param base početna vrijednost, na koju se sabire
	 * @param addition sabirak
	 */
	public static void addRGBA(int[] base, int[] addition)
	{
		base[0] += addition[0];
		base[1] += addition[1];
		base[2] += addition[2];
		base[3] += addition[3];
	}
	
	/**
	 * Dijeli RGB vrijednost skalarom, po principu: base /= divider
	 * @param base početna vrijednost, koja se dijeli
	 * @param divider djelilac
	 */
	public static void divideRGB(int[] base, int divider)
	{
		base[0] /= divider;
		base[1] /= divider;
		base[2] /= divider;
	}
	
	/**
	 * Dijeli RGBA vrijednost skalarom, po principu: base /= divider
	 * @param base početna vrijednost, koja se dijeli
	 * @param divider djelilac
	 */
	public static void divideRGBA(int[] base, int divider)
	{
		base[0] /= divider;
		base[1] /= divider;
		base[2] /= divider;
		base[3] /= divider;
	}
	
	/**
	 * Množi RGB vrijednost skalarom, po principu: base *= multiplier
	 * @param base početna vrijednost, koja se množi
	 * @param multiplier faktor
	 */
	public static void multiplyRGB(int[] base, float multiplier)
	{
		base[0] *= multiplier;
		base[1] *= multiplier;
		base[2] *= multiplier;
	}
	
	/**
	 * Množi RGBA vrijednost skalarom, po principu: base *= multiplier
	 * @param base početna vrijednost, koja se množi
	 * @param multiplier faktor
	 */
	public static void multiplyRGBA(int[] base, float multiplier)
	{
		base[0] *= multiplier;
		base[1] *= multiplier;
		base[2] *= multiplier;
		base[3] *= multiplier;
	}
	
	/**
	 * Dijeli zadatu sliku na određen broj jednakih blokova (tiles) vraćajući ih kao 1D niz slika,
	 * pročitan iz prvobitne slike left-to-right, top-to-bottom.
	 * @param numX broj blokova po horizontali
	 * @param numY broj blokova po vertikali
	 * @param sheet izvorna slika, veličina bloka se računa iz nje i numX, numY vrijednosti
	 * @return 1D niz slika
	 */
	public static BufferedImage[] cutTiles1D(int numX, int numY, BufferedImage sheet)
	{
		BufferedImage[] tileSet = new BufferedImage[numX * numY];
		int tileW = (sheet.getWidth() / numX);
		int tileH = (sheet.getHeight() / numY);
		for(int y = 0, i = 0; y < numY; ++y)
		{
			for(int x = 0; x < numX; ++x, ++i)
			{
				tileSet[i] = new BufferedImage(tileW, tileH, sheet.getType());
				Graphics g = tileSet[i].getGraphics();
				g.drawImage(sheet, 0, 0, tileW, tileH, x * tileW, y * tileH, (x+1) * tileW, (y+1) * tileH, null);
				g.dispose();
			}
		}
		return tileSet;
	}
	
	/**
	 * Dijeli zadatu sliku na određen broj jednakih blokova (tiles) vraćajući ih kao 2D matricu slika,
	 * pročitan iz prvobitne slike left-to-right, top-to-bottom.
	 * @param numX broj blokova po horizontali
	 * @param numY broj blokova po vertikali
	 * @param sheet izvorna slika, veličina bloka se računa iz nje i numX, numY vrijednosti
	 * @return 2D matrica slika, [red][kolona]
	 */
	public static BufferedImage[][] cutTiles2D(int numX, int numY, BufferedImage sheet)
	{
		BufferedImage[][] tileSet = new BufferedImage[numY][numX];
		int tileW = (sheet.getWidth() / numX);
		int tileH = (sheet.getHeight() / numY);
		for(int y = 0; y < numY; ++y)
		{
			for(int x = 0; x < numX; ++x)
			{
				tileSet[y][x] = new BufferedImage(tileW, tileH, sheet.getType());
				Graphics g = tileSet[y][x].getGraphics();
				g.drawImage(sheet, 0, 0, tileW, tileH, x * tileW, y * tileH, (x+1) * tileW, (y+1) * tileH, null);
				g.dispose();
			}
		}
		return tileSet;
	}
}
