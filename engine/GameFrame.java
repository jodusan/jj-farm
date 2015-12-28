/*---------------------------------------------------------
 *  This class is used for visual front-end. Most important
 *  methods are update and render, where object states are
 *  updated and drawn on the screen for each frame. It is
 *  extended and not used directly.
 *---------------------------------------------------------*/

package engine;

import javax.swing.*;
import java.awt.*;

public abstract class GameFrame extends JPanel {
    private static final long serialVersionUID = 6058915663486070170L;

    private int screenX = 640;
    private int screenY = 480;

    private static JFrame myFrame = null;
    private String title = "";

    private Color backColor = Color.black;
    private boolean clearBackBuffer = true;

    private int updateRate = 60;
    private boolean updatesRunning = true;
    private boolean renderRunning = true;

    private Thread runnerThread = null;

    public GameFrame(String title, int sizeX, int sizeY) {
        super(true);

        screenX = sizeX;
        screenY = sizeY;

        setSize(screenX, screenY);

        if (title != null) this.title = title;

        this.title = title;

        runnerThread = new Thread(() -> {
            while (true) {
                long startTime = System.currentTimeMillis();
                if (updatesRunning) tick();
                if (renderRunning) repaint();
                try {
                    long frameTime = System.currentTimeMillis() - startTime;
                    long sleepTime = 1000 / updateRate - frameTime;
                    if (sleepTime > 0)
                        Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.out.println("You done fucked up");
                }
            }
        });

        //runnerThread.start();
    }

    /*---------------------------------------------------------
     *  Main thread which calls update() and render() methods.
     *  It needs to be called for application to start.
     *  The best place for it to be called is at the end of
     *  constructor, after all resources are loaded.
     *---------------------------------------------------------*/
    public void startThread() {
        if (!runnerThread.isAlive())
            runnerThread.start();
        else
            System.out.println("Already running!");
    }

    private void tick() {
        update();
    }


    /*---------------------------------------------------------
     *  Initialise JFrame with main panel. Needs to be called
     *  at the end of constructor.
     *---------------------------------------------------------*/
    public void initGameWindow() {
        if (myFrame != null) {
            System.out.println("initGameWindow() already called, can't do again");
            return;
        }

        myFrame = new JFrame(title);
        myFrame.setLayout(new BorderLayout());

        setPreferredSize(new Dimension(screenX, screenY));
        setMaximumSize(new Dimension(screenX, screenY));
        myFrame.add(this, BorderLayout.CENTER);
        myFrame.setResizable(false);
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        myFrame.setIgnoreRepaint(true);

        handleWindowInit();

        myFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                handleWindowDestroy();
                System.exit(0);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (clearBackBuffer) {
            g.setColor(backColor);
            g.fillRect(0, 0, screenX, screenY);
        }

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        render((Graphics2D) g, getWidth(), getHeight());
    }

    protected JFrame getWindow() {
        return myFrame;
    }

    protected void setBackgroundClear(boolean clr) {
        clearBackBuffer = clr;
    }


    /*---------------------------------------------------------
     *  Sets background color
     *---------------------------------------------------------*/
    protected void setBackgroundClearColor(Color c) {
        backColor = c;
    }


    /*---------------------------------------------------------
     *  Main window icon
     *---------------------------------------------------------*/
    protected void setIcon(Image icon) {
        myFrame.setIconImage(icon);
    }


    /*---------------------------------------------------------
     *  Sets refresh rate ( Hz/fps )
     *  Timing is made with simple sleep methods which can
     *  cause judder. No dynamic update or frame skipping.
     *---------------------------------------------------------*/
    protected void setUpdateRate(int fps) {
        if (fps >= 1 && fps < 120) {
            updateRate = fps;
        } else {
            System.out.println("Valid range for setUpdateRate is 1 - 120");
        }
    }


    /*---------------------------------------------------------
     *  Fires within initGameWindow() call after
     *  the window is constructed.
     *---------------------------------------------------------*/
    public abstract void handleWindowInit();


    /*---------------------------------------------------------
     *  Fires when user quits the application.
     *  (Presses the X on the window)
     *---------------------------------------------------------*/
    public abstract void handleWindowDestroy();


    /*---------------------------------------------------------
     *  Draws objects on the window. No updates!
     *---------------------------------------------------------*/
    public abstract void render(Graphics2D g, int sw, int sh);


    /*---------------------------------------------------------
     *  Updates the states of objects. No drawing!
     *---------------------------------------------------------*/
    public abstract void update();

}
