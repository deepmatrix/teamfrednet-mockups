

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowEvent;



public class Main 
    extends java.awt.Frame
{
    private final static long EVENT_MASK =  (AWTEvent.WINDOW_EVENT_MASK);


    public final static class LM
        implements java.awt.LayoutManager2
    {

        public LM(){
            super();
        }

        private void layout(Container parent, Component comp){
        }

        public void addLayoutComponent(String name, Component comp){
            this.layout(comp.getParent(),comp);
        }
        public void removeLayoutComponent(Component comp){
        }
        public Dimension preferredLayoutSize(Container parent){
            return parent.getSize();
        }
        public Dimension minimumLayoutSize(Container parent){
            return parent.getSize();
        }
        public void layoutContainer(Container parent){
            for (Component comp : parent.getComponents()){
                this.layout(parent,comp);
            }
        }
        public void addLayoutComponent(Component comp, java.lang.Object constraints){
        }
        public Dimension maximumLayoutSize(Container parent){
            
            return parent.getSize();
        }
        public float getLayoutAlignmentX(Container parent){
            
            return 0f;
        }
        public float getLayoutAlignmentY(Container parent){
            
            return 0f;
        }
        public void invalidateLayout(Container parent){
            if (0 < parent.countComponents())
                this.layout(parent,parent.getComponent(0));
        }
    }

    private Canvas bg, fg;


    public Main(){
        super();
        this.enableEvents(EVENT_MASK);
        this.setLayout(new LM());
    }


    public void init(){

        Dimension size = this.getSize();
        Insets insets = this.getInsets();
        size.width -= (insets.left+insets.right);
        size.height -= (insets.top+insets.bottom);

        this.fg = new Canvas();
        this.fg.setBackground(Color.white);
        this.fg.setForeground(Color.white);
        {
            int w = size.width / 2;
            int h = size.height / 2;
            int x = (w/2);
            int y = (h/2);
            this.fg.setSize(w,h);
            this.fg.setLocation(x,y);
        }

        this.add(fg);
        this.bg = new Canvas();
        this.bg.setBackground(Color.black);
        this.bg.setForeground(Color.black);
        {
            this.bg.setSize(size);
            this.bg.setLocation(insets.left,insets.top);
        }
        this.add(bg);
        this.doLayout();
    }
    @Override
    public void processWindowEvent(WindowEvent event) {
        super.processWindowEvent(event);

        switch(event.getID()) {
        case WindowEvent.WINDOW_OPENED:

            this.init();

            break;

        case WindowEvent.WINDOW_CLOSING:

            this.setVisible(false);
            this.dispose();

            break;

        case WindowEvent.WINDOW_CLOSED:
            System.exit(0);
            break;
        }
    }
    public void paint(Graphics g){
        super.paint(g);
    }
    public static void main(String[] args) throws Exception {

        int x = 0;
        int y = 0;
        int width = 800;
        int height = 600;
        String title = "Team FREDNET Canvas";


        for (int i = 0, n = args.length; i < n; i++) {
            String arg = args[i];

            if (arg.equals("-x")) {
                String value = args[++i];
                x = Integer.parseInt(value);
            }
            else if (arg.equals("-y")) {
                String value = args[++i];
                y = Integer.parseInt(value);
            }
            else if (arg.equals("-w")) {
                String value = args[++i];
                width = Integer.parseInt(value);
            }
            else if (arg.equals("-h")) {
                String value = args[++i];
                height = Integer.parseInt(value);
            }
        }


        Main frame = new Main();

        frame.setTitle(title);
        frame.setLocation(x, y);
        frame.setSize(width, height);

        frame.setVisible(true);
    }

}
