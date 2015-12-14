package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Glavni objekat za slo?enije aplikacije, koje ?e imati vi?e odvojenih stanja. Treba ga
 * instancirati samo jednom, ovaj objekat je vlasnik prozora i preko njega se postavlja
 * trenutno aktivno stanje, koje ?e onda da bude pozivano za a?uriranje i iscrtavanje, te
 * ?e samo ono dobijati input doga?aje.
 * @author Aleksandar
 *
 */
public class GameHost implements MouseListener, 
MouseWheelListener, MouseMotionListener, KeyListener
{
	public enum GFMouseButton
	{
		None,
		Left,
		Middle,
		Right,
		WheelUp,
		WheelDown,
		WheelLeft,
		WheelRight,
		Special1,
		Special2
	}

	private int screenX = 640;
	private int screenY = 480;
	
	private static JFrame myFrame = null;
	private static JPanel myPanel = null;
	private String title = "RAF GameHost";
	
	private Color backColor = Color.blue;
	private boolean clearBackBuffer = true;
	
	private int mouseX = 0;
	private int mouseY = 0;
	
	private int updateRate = 30;
	private boolean updatesRunning = true;
	private boolean renderRunning = true;
	
	private boolean useHQ = false;
	
	private Thread runnerThread = null;
	
	private boolean[] mouseButtons = new boolean[GFMouseButton.values().length];
	private boolean[] keyboardKeys = new boolean[1024];
	
	private GameState currentState = null;
	private GameState nextState = null;
	
	private HashMap<String, GameState> states = new HashMap<String, GameState>();
	
	private boolean gameRunning = true;
	
	/**
	 * Glavni objekat za slo?enije aplikacije, koje ?e imati vi?e odvojenih stanja. Treba ga
	 * instancirati samo jednom, ovaj objekat je vlasnik prozora i preko njega se postavlja
	 * trenutno aktivno stanje, koje ?e onda da bude pozivano za a?uriranje i iscrtavanje, te
	 * ?e samo ono dobijati input doga?aje.
	 * @param title naslov prozora
	 * @param sizeX ?irina prostora za crtanje u pikselima
	 * @param sizeY visina prostora za crtanje u pikselima
	 */
	public GameHost(String title, int sizeX, int sizeY)
	{
		if(sizeX < 320) sizeX = 320;
		if(sizeY < 240) sizeY = 240;
		
		if(sizeX > 2048) sizeX = 2048;
		if(sizeY > 2048) sizeY = 2048;
		
		screenX = sizeX;
		screenY = sizeY;
		
		myPanel = new JPanel(true) 
		{
			private static final long serialVersionUID = 3272657569861826743L;

			@Override
		    protected void paintComponent(Graphics g)
			{
				if(clearBackBuffer)
				{
					g.setColor(backColor);
					g.fillRect(0, 0, screenX, screenY);
				}
				
				if(useHQ) 
				{
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				}
				else
				{
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				}
				
				if(currentState != null)
					currentState.render((Graphics2D)g, getWidth(), getHeight());
			}
		};
		
		myPanel.setSize(screenX, screenY);
		
		if(title != null) this.title = title;
		
		this.title = title;
		
		myPanel.addMouseListener(this);
		myPanel.addMouseMotionListener(this);
		myPanel.addMouseWheelListener(this);
		myPanel.addKeyListener(this);
		
		initGameWindow();
		
		runnerThread = new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				while(gameRunning)
				{
					long startTime = System.currentTimeMillis();
					if(updatesRunning) tick();
					if(renderRunning) myPanel.repaint();
					try
					{
						long frameTime = System.currentTimeMillis() - startTime;
						Thread.sleep(1000 / updateRate - frameTime);
					} 
					catch (InterruptedException e) { }
				}
				
				System.exit(0);
			}
		});
		
		runnerThread.start();
	}
	
	/**
	 * Vra?a prijavljene GameState objekte po nazivu
	 * @param name naziv koji tra?eni GameState daje u getName() pozivu
	 * @return referenca na GameState ako je na?en, ili null ako nije
	 */
	public GameState getState(String name)
	{
		return states.get(name);
	}
	
	/**
	 * Prelazak na novo stanje, po referenci
	 * @param nextState referenca na GameState objekat, trebao bi biti jedan od prijavljenih
	 */
	public void setState(GameState nextState)
	{
		if(nextState == null) return;
		if(currentState == nextState) return;
		
		this.nextState = nextState;
	}
	
	/**
	 * Prelazak na novo stanje, po nazivu
	 * @param stateName naziv stanja, kako ga vra?a njegova getName() metoda
	 */
	public void setState(String stateName)
	{
		setState(getStateByName(stateName));
	}
	
	/**
	 * Referenca na trenutno aktivno stanje
	 * @return referenca na GameState objekat
	 */
	public GameState getCurrentState()
	{
		return currentState;
	}
	
	/**
	 * Tra?enje reference na bilo koje od trneutno prijavljenih stanja
	 * @param name naziv stanja, kako ga vra?a njegova getName() metoda
	 * @return referenca ako je stanje prona?eno, null ako nije
	 */
	public GameState getStateByName(String name)
	{
		return states.get(name);
	}
	
	/**
	 * Prijavljuje stanje na ovaj Host, ovo *ne bi trebalo ru?no raditi*, jer
	 * se prijavljivanje radi u konstruktoru stanja.
	 * @param state referenca na stanje koje se registruje
	 */
	public void registerState(GameState state)
	{
		if(state == null)
			return;
		
		if(!states.containsValue(state))
			states.put(state.getName(), state);
	}
	
	private void tick()
	{
		if(nextState != null)
		{
			if(currentState != null) currentState.suspendState();
			
			currentState = nextState;
			nextState = null;
			
			currentState.resumeState();
		}
		
		if(currentState != null) currentState.update();
	}
	
	private void initGameWindow()
	{
		if(myFrame != null)
		{
			System.out.println("initGameWindow() already called, can't do again");
			return;
		}
		
		myFrame = new JFrame(title);
		myFrame.setLayout(new BorderLayout());
		
		myPanel.setPreferredSize(new Dimension(screenX, screenY));
		myPanel.setMaximumSize(new Dimension(screenX, screenY));
		myFrame.add(myPanel, BorderLayout.CENTER);
		myFrame.setResizable(false);
		myFrame.pack();
		myFrame.setVisible(true);
		myFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		myFrame.addKeyListener(this);
		
		myFrame.setIgnoreRepaint(true);
		
		//handleWindowInit();
		
		myFrame.addWindowListener(new java.awt.event.WindowAdapter() {
	            public void windowClosing(java.awt.event.WindowEvent evt){
	            	if(currentState != null)
	            	{
	            		if(currentState.handleWindowClose())
	            			gameRunning = false;
	            	}
	            	else
	            	{
	            		gameRunning = false;
	            	}
	            }
	       });
	}
	
	/**
	 * Metod koji programski prekida izvr?avanje aplikacije ?im se trenutni frejm zavr?i.
	 */
	public void exit()
	{
		gameRunning = false;
	}
	
	/**
	 * Metod koji daje ?irinu prostora za crtanje, po?to prozor nije resizable, treba
	 * biti ista vrijednost koja je zadata i u konstruktoru.
	 * @return ?irina u pikselima
	 */
	public int getWidth()
	{
		return screenX;
	}
	
	/**
	 * Metod koji daje visinu prostora za crtanje, po?to prozor nije resizable, treba
	 * biti ista vrijednost koja je zadata i u konstruktoru.
	 * @return visina u pikselima
	 */
	public int getHeight()
	{
		return screenY;
	}
	
	/**
	 * Metod koji ?e da iscrta zadato stanje, ali u off-screen sliku, umjesto na ekran.
	 * Obratiti pa?nju da se ovo ne pozove iz render() metode istog stanja, jer bi to
	 * izazvalo beskona?nu rekurziju.
	 * @param canvas objekat slike u koju ?e se crtati, mo?e biti null, pa ?e nova slika biti alocirana;
	 * ako se ovo radi ?esto, bolje je imati jednu unaprijed alociranu sliku koja ?e se reciklirati.
	 * @param state stanje koje se treba iscrtati, bi?e pozvan njegov render() metod
	 * @return vra?a referencu na proslije?enu ili novokonstruisanu sliku, u koju je iscrtano stanje
	 */
	public BufferedImage renderSnapshot(BufferedImage canvas, GameState state)
	{
		if(canvas == null)
			canvas = new BufferedImage(screenX, screenY, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g = (Graphics2D)canvas.getGraphics();
		
		if(clearBackBuffer)
		{
			g.setColor(backColor);
			g.fillRect(0, 0, screenX, screenY);
		}
		
		if(useHQ) 
		{
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		else
		{
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		
		if(state != null)
			state.render(g, screenX, screenY);
		
		return canvas;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		GFMouseButton button = GFMouseButton.None;
		if(arg0.getButton() == 1) button = GFMouseButton.Left;
		if(arg0.getButton() == 2) button = GFMouseButton.Middle;
		if(arg0.getButton() == 3) button = GFMouseButton.Right;
		
		if(arg0.getButton() == 5) button = GFMouseButton.Special1;
		if(arg0.getButton() == 4) button = GFMouseButton.Special2;
		
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		
		mouseButtons[button.ordinal()] = true;
		
		if(currentState != null)
			currentState.handleMouseDown(mouseX, mouseY, button);	
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		GFMouseButton button = GFMouseButton.None;
		if(arg0.getButton() == 1) button = GFMouseButton.Left;
		if(arg0.getButton() == 2) button = GFMouseButton.Middle;
		if(arg0.getButton() == 3) button = GFMouseButton.Right;
		
		if(arg0.getButton() == 5) button = GFMouseButton.Special1;
		if(arg0.getButton() == 4) button = GFMouseButton.Special2;
		
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		
		mouseButtons[button.ordinal()] = false;
		
		if(currentState != null)
			currentState.handleMouseUp(mouseX, mouseY, button);	
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() >= 0 && arg0.getKeyCode() < 1024)
			keyboardKeys[arg0.getKeyCode()] = true;
		
		if(currentState != null)
			currentState.handleKeyDown(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		if(arg0.getKeyCode() >= 0 && arg0.getKeyCode() < 1024)
			keyboardKeys[arg0.getKeyCode()] = false;
		
		if(currentState != null)
			currentState.handleKeyUp(arg0.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		
		if(currentState != null)
			currentState.handleMouseMove(mouseX, mouseY);
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		
		if(currentState != null)
			currentState.handleMouseMove(mouseX, mouseY);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0)
	{
		if(arg0.getWheelRotation() > 0)
		{
			mouseButtons[GFMouseButton.WheelDown.ordinal()] = true;
			mouseX = arg0.getX();
			mouseY = arg0.getY();
			
			if(currentState != null)
				currentState.handleMouseDown(mouseX, mouseY, GFMouseButton.WheelDown);
			
			mouseButtons[GFMouseButton.WheelDown.ordinal()] = false;
		}
		else if(arg0.getWheelRotation() < 0)
		{
			mouseButtons[GFMouseButton.WheelUp.ordinal()] = true;
			mouseX = arg0.getX();
			mouseY = arg0.getY();
			
			if(currentState != null)
				currentState.handleMouseDown(mouseX, mouseY, GFMouseButton.WheelUp);
			
			mouseButtons[GFMouseButton.WheelUp.ordinal()] = false;
		}
	}
	
	/**
	 * Upit trenutnog stanja tastera mi?a, putem GFMouseButton enuma
	 * @param button taster za koje se upit vr?i
	 * @return true ako je taster pritisnut, false ako nije
	 */
	public boolean isMouseButtonDown(GFMouseButton button)
	{
		return mouseButtons[button.ordinal()];
	}
	
	/**
	 * Upit trenutnog stanja tastera na tastaturi
	 * @param keyCode kod tipke, koristiti konstante iz KeyEvent.VK_*
	 * @return true ako je taster pritisnug, false ako nije
	 */
	public boolean isKeyDown(int keyCode)
	{
		if(keyCode >= 0 && keyCode < 1024)
			return keyboardKeys[keyCode];
		else
			return false;
	}
	
	/**
	 * Daje referencu na JFrame prozor aplikacije
	 * @return JFrame
	 */
	public JFrame getWindow()
	{
		return myFrame;
	}
	
	/**
	 * Pali ili gasi automatsko ?i??enje pozadine prije render() poziva
	 * @param clr true za automatsko brisanje, sa false se prethodna slika zadr?ava  
	 */
	public void setBackgroundClear(boolean clr)
	{
		clearBackBuffer = clr;
	}
	
	/**
	 * Boja na koju ?e biti postavljena pozadina, ako je setBackgroundClear uklju?eno
	 * @param c boja pozadine
	 */
	public void setBackgroundClearColor(Color c)
	{
		backColor = c;
	}
	
	/**
	 * Trenutna horizontalna pozicija kursora mi?a u prostoru za crtanje
	 * @return X koordinata kursora u pikselima
	 */
	public int getMouseX()
	{
		return mouseX;
	}
	
	/**
	 * Trenutna vertikalna pozicija kursora mi?a u prostoru za crtanje
	 * @return Y koordinata kursora u pikselima
	 */
	public int getMouseY()
	{
		return mouseY;
	}
	
	/**
	 * Postavlja hint za vi?i kvalitet iscrtavanja koji ?e se onda automatski primjenjivati
	 * nad Graphics2D objektom koji se daje u render() metodi
	 * @param hq true za vi?i kvalitet interpolacije i uklju?en anti-aliasing primitiva
	 */
	public void setHighQuality(boolean hq)
	{
		useHQ = hq;
	}
	
	/**
	 * Postavlja ikonicu prozora
	 * @param icon Image objekat (mo?e biti BufferedImage)
	 */
	public void setIcon(Image icon)
	{
		myFrame.setIconImage(icon);
	}
	
	/**
	 * Postavlja ciljnu frekvenciju a?uriranja u Hz/fps. Tajming je realizovan jednostavnim
	 * sleep metodama, zbog ?ega je mogu? neravnomijeran tok izvr?avanja (judder). Ukoliko
	 * a?uriranje i iscrtavanje traje du?e od (1 / fps) sekundi, svukupan tempo izvr?avanja
	 * ?e se usporiti na tu brzinu, nije implementiran nikakav dinami?ki update ili frameskipping.  
	 * @param fps ciljni broj a?uriranih i iscrtanih okvira u sekundi, od 1 do 120
	 */
	public void setUpdateRate(int fps)
	{
		if(fps >= 1 && fps < 120)
		{
			updateRate = fps;
		}
		else
		{
			System.out.println("Valid range for setUpdateRate is 1 - 120");
		}
	}
	
	/**
	 * Vra?a ciljnu frekvenciju a?uriranja u Hz/fps
	 * @return frekvencija u Hz
	 */
	public int getUpdateRate()
	{
		return updateRate;
	}
}
