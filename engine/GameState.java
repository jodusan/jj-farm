package engine;

import engine.GameHost.GFMouseButton;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Apstraktna klasa kao polazna ta?ka za implementaciju jednog stanja aplikacije,
 * namijenjena da se koristi u kombinaciji sa GameHost objektom koji je vlasnik
 * prozora u kome ?e ovakva stanja da se a?uriraju i iscrtavaju i iz kog ?e dobijati
 * input doga?aje.
 * @author Aleksandar Stan?i?
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
	 * Omota? za renderSnapshot poziv GameHost objekta, iscrtava trenutno stanje u off-screen sliku
	 * @param canvas 
	 * @return
	 */
	public BufferedImage renderSnapshot(BufferedImage canvas)
	{
		return host.renderSnapshot(canvas, this);
	}
	
	/**
	 * Metod koji ?e biti pozvan kada korisnik poku?a da zatvori prozor (klik na X, Alt+F4, itd.)
	 * @return true ako ?elite dozvoliti zatvranje prozora, false ako ga ?elite ignorisati
	 */
	public abstract boolean handleWindowClose();
	
	/**
	 * Tekstualno ime stanja, bi?e kori?teno za getState() i setState() pozive u GameHost objektu
	 * @return konstantno ime ovog stanja
	 */
	public abstract String getName();
	
	/**
	 * Metod koji ?e biti pozvan pri prvobitnom pokretanju stanja ili pri prebacivanju sa drugog stanja
	 */
	public abstract void resumeState();
	
	/**
	 * Metod koji ?e biti pozvan prije prelaska na drugo stanje
	 */
	public abstract void suspendState();
	
	/**
	 * Metod koji treba da obavi kompletno iscrtavanje cijelog frejma, poziva se automatski,
	 * zadatom frekvencijom (update rate). Ne treba da sadr?i nikakva logi?ka a?uriranja, samo crtanje.
	 * @param g Graphics2D objekat preko koga se obavlja crtanje na ekran
	 * @param sw ?irina trenutnog prostora za crtanje
	 * @param sh visina trenutnog prostora za crtanje
	 */
	public abstract void render(Graphics2D g, int sw, int sh);
	
	/**
	 * Metod koji treba da a?urira stanje igre, poziva se prije render() poziva, jednakom frekvencijom.
	 */
	public abstract void update();
	
	/**
	 * Metod koji ?e biti pozvan na pritisak tastera mi?a (okretanja scroll to?ka se tako?e smatraju tasterima) 
	 * @param x X koordinata u pikselima na kojima je kursor bio u trenutku pritiska
	 * @param y Y koordinata u pikselima na kojima je kursor bio u trenutku pritiska
	 * @param button dugme mi?a koje je pritisnuto, iz GFMouseButton enuma
	 */
	public abstract void handleMouseDown(int x, int y, GFMouseButton button);
	
	/**
	 * Metod koji ?e biti pozvan na pu?tanje tastera mi?a (okretanja scroll to?ka se tako?e smatraju tasterima) 
	 * @param x X koordinata u pikselima na kojima je kursor bio u trenutku pu?tanja
	 * @param y Y koordinata u pikselima na kojima je kursor bio u trenutku pu?tanja
	 * @param button dugme mi?a koje je pu?teno, iz GFMouseButton enuma
	 */
	public abstract void handleMouseUp(int x, int y, GFMouseButton button);
	
	/**
	 * Metod koji se poziva pri svakoj promjeni pozicije kursora mi?a, bez obzira da li su tasteri pritisnuti.
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
	 * Metod koji se poziva kada je pu?teni taster na tastaturi.
	 * @param keyCode kod tipke koja je pu?tena, porediti sa vrijednostima iz KeyEvent.VK_*
	 */
	public abstract void handleKeyUp(int keyCode);
}
