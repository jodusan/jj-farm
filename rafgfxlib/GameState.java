package rafgfxlib;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import rafgfxlib.GameHost.GFMouseButton;

/**
 * Apstraktna klasa kao polazna tačka za implementaciju jednog stanja aplikacije,
 * namijenjena da se koristi u kombinaciji sa GameHost objektom koji je vlasnik
 * prozora u kome će ovakva stanja da se ažuriraju i iscrtavaju i iz kog će dobijati
 * input događaje.
 * @author Aleksandar Stančić
 *
 */
public abstract class GameState
{
	/**
	 * Referenca na GameHost objekat kom ovo stanje pripada, inicijalizuje se u konstruktoru,
	 * preko njega se pristupa metodama poput getWidth(), getMouseX(), isKeyDown() i sl.
	 */
	protected final GameHost host;
	
	/**
	 * Obavezan konstruktor koji upisuje host polje za ovo stanje, a i obavlja registraciju
	 * stanja kor GameHost objekta.
	 * @param host prethodno konstruisan GameHost objekat
	 */
	public GameState(GameHost host)
	{
		this.host = host;
		host.registerState(this);
	}
	
	/**
	 * Omotač za renderSnapshot poziv GameHost objekta, iscrtava trenutno stanje u off-screen sliku
	 * @param canvas 
	 * @return
	 */
	public BufferedImage renderSnapshot(BufferedImage canvas)
	{
		return host.renderSnapshot(canvas, this);
	}
	
	/**
	 * Metod koji će biti pozvan kada korisnik pokuša da zatvori prozor (klik na X, Alt+F4, itd.)
	 * @return true ako želite dozvoliti zatvranje prozora, false ako ga želite ignorisati
	 */
	public abstract boolean handleWindowClose();
	
	/**
	 * Tekstualno ime stanja, biće korišteno za getState() i setState() pozive u GameHost objektu
	 * @return konstantno ime ovog stanja
	 */
	public abstract String getName();
	
	/**
	 * Metod koji će biti pozvan pri prvobitnom pokretanju stanja ili pri prebacivanju sa drugog stanja
	 */
	public abstract void resumeState();
	
	/**
	 * Metod koji će biti pozvan prije prelaska na drugo stanje
	 */
	public abstract void suspendState();
	
	/**
	 * Metod koji treba da obavi kompletno iscrtavanje cijelog frejma, poziva se automatski,
	 * zadatom frekvencijom (update rate). Ne treba da sadrži nikakva logička ažuriranja, samo crtanje.
	 * @param g Graphics2D objekat preko koga se obavlja crtanje na ekran
	 * @param sw širina trenutnog prostora za crtanje
	 * @param sh visina trenutnog prostora za crtanje
	 */
	public abstract void render(Graphics2D g, int sw, int sh);
	
	/**
	 * Metod koji treba da ažurira stanje igre, poziva se prije render() poziva, jednakom frekvencijom.
	 */
	public abstract void update();
	
	/**
	 * Metod koji će biti pozvan na pritisak tastera miša (okretanja scroll točka se takođe smatraju tasterima) 
	 * @param x X koordinata u pikselima na kojima je kursor bio u trenutku pritiska
	 * @param y Y koordinata u pikselima na kojima je kursor bio u trenutku pritiska
	 * @param button dugme miša koje je pritisnuto, iz GFMouseButton enuma
	 */
	public abstract void handleMouseDown(int x, int y, GFMouseButton button);
	
	/**
	 * Metod koji će biti pozvan na puštanje tastera miša (okretanja scroll točka se takođe smatraju tasterima) 
	 * @param x X koordinata u pikselima na kojima je kursor bio u trenutku puštanja
	 * @param y Y koordinata u pikselima na kojima je kursor bio u trenutku puštanja
	 * @param button dugme miša koje je pušteno, iz GFMouseButton enuma
	 */
	public abstract void handleMouseUp(int x, int y, GFMouseButton button);
	
	/**
	 * Metod koji se poziva pri svakoj promjeni pozicije kursora miša, bez obzira da li su tasteri pritisnuti.
	 * @param x X koordinata kursora u pikselima
	 * @param y Y koordinata kursora u pikselima
	 */
	public abstract void handleMouseMove(int x, int y);
	
	/**
	 * Metod koji se poziva kada je pritisnut taster na tastaturi.
	 * @param keyCode kod tipke koja je pritisnuta, porediti sa vrijednostima iz KeyEvent.VK_*
	 */
	public abstract void handleKeyDown(int keyCode);
	
	/**
	 * Metod koji se poziva kada je pušteni taster na tastaturi.
	 * @param keyCode kod tipke koja je puštena, porediti sa vrijednostima iz KeyEvent.VK_*
	 */
	public abstract void handleKeyUp(int keyCode);
}
