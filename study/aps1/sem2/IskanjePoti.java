import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;

public class IskanjePoti {
    public static void main(String[] args) {
        JFrame o = new JFrame();
        o.setTitle("Iskanje poti");
        o.setDefaultCloseOperation(o.EXIT_ON_CLOSE);
        o.setSize(1000, 754);
        o.getContentPane().add(new IPPanel(o));
        o.setVisible(true);
    }
}

class IPPanel extends JPanel implements ChangeListener, ActionListener {
    static final float[][][] TROPICAL = {{{1, 0}, {1, 0}, {0, 0}}, {{0.4F, 0}, {0.8F, 0}, {0.8F, 0}}, {{0, 0}, {0, 0}, {1, 0}}, {{0.5F, 0}, {1, 0}, {0, 0}}};
    static final float[][][] ARCTIC = {{{0.8F, 0.1F}, {0.88F, 0.11F}, {0.64F, 0.08F}}, {{0, 0.2F}, {0.3F, 0.5F}, {0.3F, 0.5F}}, {{0, 0}, {0, 0.5F}, {0.5F, 0.5F}}, {{0.3F, 0.7F}, {0.3F, 0.7F}, {0.3F, 0.7F}}};
    static final float[][][] MOUNTAIN = {{{0.5F, 0.25F}, {0.5F, 0.25F}, {0.5F, 0.25F}}, {{0.75F, 0}, {0.75F, 0}, {0.75F, 0.25F}}, {{0, 0}, {0, 0}, {0.5F, 0.5F}}, {{0.5F, 0.5F}, {0.5F, 0.5F}, {0.5F, 0.5F}}};
    static final float[][][] CONTINENTAL = {{{0.8F, 0}, {0.4F, 0}, {0, 0}}, {{0, 0}, {1, 0}, {0.75F, 0}}, {{0, 0}, {0, 0}, {0.75F, 0.25F}}, {{0, 0}, {1, 0}, {0, 0}}};
    
    static int sizeX, sizeY;
    static double offsetX, offsetY;
    static double stepX, stepY;
    
    float[][][] climate = TROPICAL;
    JFrame o;
    Tocka[][] t;
    Tocka[] path;
    Tocka start, end;
    double min, max;
    Graphics buffer;
    BufferedImage offscreen;
    File file;
    boolean changed = true;
    boolean opening = true;
    
    JLabel X = new JLabel("X:");
    JLabel posX = new JLabel();
    JLabel Y = new JLabel("Y:");
    JLabel posY = new JLabel();
    JLabel Z = new JLabel("Z:");
    JLabel posZ = new JLabel();
    JLabel startPoint = new JLabel("Začetek:");
    JLabel startPos = new JLabel();
    JLabel endPoint = new JLabel("Konec:");
    JLabel endPos = new JLabel();
    JLabel length = new JLabel("Dolžina:");
    JLabel pathLength = new JLabel();
    JLabel points = new JLabel("Število točk v poti:");
    JLabel pointsN = new JLabel();
    JLabel pointsTotal = new JLabel("Število dovoljenih točk:");
    JLabel pointsTotalN = new JLabel();
    JLabel pointsChecked = new JLabel("Število pregledanih točk:");
    JLabel pointsCheckedN = new JLabel();
    JLabel timeLabel = new JLabel("Porabljen čas:");
    JLabel time = new JLabel();
    JSlider maxValue = new JSlider();
    JSlider minValue = new JSlider();
    JButton refresh = new JButton("Osveži");
    JLabel clim = new JLabel("Podnebje");
    JRadioButton tropical = new JRadioButton("Tropsko");
    JRadioButton arctic = new JRadioButton("Arktično");
    JRadioButton mountain = new JRadioButton("Gorsko");
    JRadioButton continental = new JRadioButton("Celinsko");
    ButtonGroup bgr = new ButtonGroup();
    JButton open = new JButton("Odpri . . .");
    JLabel openedFile = new JLabel();
    JCheckBox diagonal = new JCheckBox("Dovoli diagonalne poti");
    JLabel sizeXlabel = new JLabel("Širina (X):");
    JLabel sizeYlabel = new JLabel("Višina (Y):");
    JLabel offsetXlabel = new JLabel("Najmanjši X:");
    JLabel offsetYlabel = new JLabel("Najmanjši Y:");
    JLabel stepXlabel = new JLabel("Razmak X:");
    JLabel stepYlabel = new JLabel("Razmak Y:");
    JTextField sizeXval = new JTextField("721");
    JTextField sizeYval = new JTextField("721");
    JTextField offsetXval = new JTextField("16040");
    JTextField offsetYval = new JTextField("4960");
    JTextField stepXval = new JTextField("25");
    JTextField stepYval = new JTextField("25");
    JButton load = new JButton("Naloži");
    JLabel error = new JLabel();
    JFileChooser chooser = new JFileChooser();
    Zoom zoom = new Zoom();
    Relief relief = new Relief();
    
    class Zoom extends JPanel {
        int x, y;
        
        public void paintComponent(Graphics g) {
            if (offscreen != null) {
                g.drawImage(offscreen,0,0,120,120,x-30,y-30,x+30,y+30,this);
            }
            g.setColor(Color.RED);
            g.drawLine(0,60,56,60);
            g.drawLine(64,60,120,60);
            g.drawLine(60,0,60,56);
            g.drawLine(60,64,60,120);
        }
    };
    
    class Relief extends JPanel implements MouseListener, MouseMotionListener {
        public void paintComponent(Graphics g) {
            if (changed && t != null) {
                float c;
                for (int i=0; i < t.length; i++) {
                    for (int j=0; j < t[i].length; j++) {
                        if (t[i][j] == null) {
                            buffer.setColor(Color.WHITE);
                        } else {
                            c = (float)((t[i][j].z - min)/(max - min));
                            if (t[i][j].z >= minValue.getValue() && t[i][j].z < maxValue.getValue()) {
                                buffer.setColor(new Color(c*climate[0][0][0] + climate[0][0][1], c*climate[0][1][0] + climate[0][1][1], c*climate[0][2][0] + climate[0][2][1]));
                                t[i][j].enabled = true;
                            } else {
                                if (t[i][j].z < minValue.getValue() && t[i][j].z >= maxValue.getValue()) {
                                    buffer.setColor(new Color(c*climate[1][0][0] + climate[1][0][1], c*climate[1][1][0] + climate[1][1][1], c*climate[1][2][0] + climate[1][2][1]));
                                } else if (t[i][j].z < minValue.getValue()) {
                                    buffer.setColor(new Color(c*climate[2][0][0] + climate[2][0][1], c*climate[2][1][0] + climate[2][1][1], c*climate[2][2][0] + climate[2][2][1]));
                                } else {
                                    buffer.setColor(new Color(c*climate[3][0][0] + climate[3][0][1], c*climate[3][1][0] + climate[3][1][1], c*climate[3][2][0] + climate[3][2][1]));
                                }
                                t[i][j].enabled = false;
                            }
                        }
                        buffer.drawLine(i,t[i].length-j-1,i,t[i].length-j-1);
                    }
                }
                changed = false;
            }
            if (path != null) {
                buffer.setColor(Color.RED);
                for (int i=0; i < path.length; i++) {
                    buffer.drawLine((int)(path[i].x/stepX-offsetX),t[0].length-(int)(path[i].y/stepY-offsetY)-1,(int)(path[i].x/stepX-offsetX),t[0].length-(int)(path[i].y/stepY-offsetY)-1);
                }
            }
            g.drawImage(offscreen,0,0,this);
        }
        
        public void mouseClicked(MouseEvent e) {
            if (start == null) {
                start = t[e.getX()][t.length-e.getY()-1];
                if (start != null && !start.enabled)
                    start = null;
                if (start != null) {
                    startPos.setText(start.toString());
                    endPos.setText("");
                    pathLength.setText("");
                    pointsN.setText("");
                    pointsTotalN.setText("");
                    pointsCheckedN.setText("");
                    time.setText("");
                }
            } else {
                end = t[e.getX()][t.length-e.getY()-1];
                if (end != null && end.enabled) {
                    endPos.setText(end.toString());
                    DijkstraHeap dh = new DijkstraHeap(t);
                    long ms = System.currentTimeMillis();
                    for (int i=0; i < t.length; i++) {
                        for (int j=0; j < t[i].length; j++) {
                            if (t[i][j] != null && t[i][j] != start && t[i][j].enabled) {
                                dh.add(t[i][j]);
                            }
                        }
                    }
                    int n = dh.size() + 1;
                    int[] t = {0};
                    path = dh.dijkstra(start, end, t);
                    ms = System.currentTimeMillis() - ms;
                    if (path == null) {
                        pathLength.setText("Pot ne obstaja!");
                    } else {
                        pathLength.setText(Math.round(path[path.length-1].path*1000)/1000F+"");
                        pointsN.setText(path.length+"");
                    }
                    pointsTotalN.setText(n+"");
                    pointsCheckedN.setText(t[0]+"");
                    time.setText((ms/1000.0) + " s");
                    start = end = null;
                    changed = true;
                    repaint();
                }
            }
        }
        
        public void mousePressed(MouseEvent e) {}
            
        public void mouseReleased(MouseEvent e) {}
            
        public void mouseEntered(MouseEvent e) {}
            
        public void mouseExited(MouseEvent e) {
            posX.setText("");
            posY.setText("");
            posZ.setText("");
        }
            
        public void mouseDragged(MouseEvent e) {}
            
        public void mouseMoved(MouseEvent e) {
            Tocka p = t[e.getX()][t.length-e.getY()-1];
            if (p != null) {
                posX.setText(p.x+"");
                posY.setText(p.y+"");
                posZ.setText(p.z+"");
            } else {
                mouseExited(e);
            }
            zoom.x = e.getX();
            zoom.y = e.getY();
            zoom.repaint();
        }
    }
    
    public IPPanel(JFrame o) {
        this.o = o;
        tropical.setSelected(true);
        diagonal.setSelected(DijkstraHeap.diagonal);
        bgr.add(tropical);
        bgr.add(arctic);
        bgr.add(mountain);
        bgr.add(continental);
        relief.addMouseListener(relief);
        relief.addMouseMotionListener(relief);
        minValue.addChangeListener(this);
        maxValue.addChangeListener(this);
        refresh.addActionListener(this);
        tropical.addActionListener(this);
        arctic.addActionListener(this);
        mountain.addActionListener(this);
        continental.addActionListener(this);
        open.addActionListener(this);
        diagonal.addActionListener(this);
        load.addActionListener(this);
        sizeXlabel.setVisible(false);
        sizeYlabel.setVisible(false);
        offsetXlabel.setVisible(false);
        offsetYlabel.setVisible(false);
        stepXlabel.setVisible(false);
        stepYlabel.setVisible(false);
        sizeXval.setVisible(false);
        sizeYval.setVisible(false);
        offsetXval.setVisible(false);
        offsetYval.setVisible(false);
        stepXval.setVisible(false);
        stepYval.setVisible(false);
        load.setVisible(false);
        error.setVisible(false);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        relief.setBounds(0,0,sizeX,sizeY);
        X.setBounds(sizeX+10,10,25,20);
        posX.setBounds(sizeX+40,10,200,20);
        Y.setBounds(sizeX+10,30,25,20);
        posY.setBounds(sizeX+40,30,200,20);
        Z.setBounds(sizeX+10,50,25,20);
        posZ.setBounds(sizeX+40,50,200,20);
        diagonal.setBounds(sizeX+10,75,200,20);
        startPoint.setBounds(sizeX+10,100,50,20);
        startPos.setBounds(sizeX+60,100,200,20);
        endPoint.setBounds(sizeX+10,120,50,20);
        endPos.setBounds(sizeX+60,120,200,20);
        length.setBounds(sizeX+10,140,50,20);
        pathLength.setBounds(sizeX+60,140,200,20);
        points.setBounds(sizeX+10,160,150,20);
        pointsN.setBounds(sizeX+160,160,100,20);
        pointsTotal.setBounds(sizeX+10,180,150,20);
        pointsTotalN.setBounds(sizeX+160,180,100,20);
        pointsChecked.setBounds(sizeX+10,200,150,20);
        pointsCheckedN.setBounds(sizeX+160,200,100,20);
        timeLabel.setBounds(sizeX+10,220,150,20);
        time.setBounds(sizeX+160,220,100,20);
        maxValue.setBounds(sizeX+10,250,250,30);
        minValue.setBounds(sizeX+10,280,250,30);
        refresh.setBounds(sizeX+150,310,100,20);
        zoom.setBounds(sizeX+10,310,120,120);
        clim.setBounds(sizeX+150,340,100,20);
        tropical.setBounds(sizeX+150,360,100,20);
        arctic.setBounds(sizeX+150,380,100,20);
        mountain.setBounds(sizeX+150,400,100,20);
        continental.setBounds(sizeX+150,420,100,20);
        open.setBounds(sizeX+10,440,120,20);
        openedFile.setBounds(sizeX+150,440,100,20);
        add(relief);
        add(X);
        add(posX);
        add(Y);
        add(posY);
        add(Z);
        add(posZ);
        add(diagonal);
        add(startPoint);
        add(startPos);
        add(endPoint);
        add(endPos);
        add(length);
        add(pathLength);
        add(points);
        add(pointsN);
        add(pointsTotal);
        add(pointsTotalN);
        add(pointsChecked);
        add(pointsCheckedN);
        add(timeLabel);
        add(time);
        add(maxValue);
        add(minValue);
        add(refresh);
        add(zoom);
        add(clim);
        add(tropical);
        add(arctic);
        add(mountain);
        add(continental);
        add(open);
        add(openedFile);
        if (opening) {
            sizeXlabel.setBounds(sizeX+10,480,100,20);
            sizeYlabel.setBounds(sizeX+10,500,100,20);
            offsetXlabel.setBounds(sizeX+10,520,100,20);
            offsetYlabel.setBounds(sizeX+10,540,100,20);
            stepXlabel.setBounds(sizeX+10,560,100,20);
            stepYlabel.setBounds(sizeX+10,580,100,20);
            sizeXval.setBounds(sizeX+110,480,100,20);
            sizeYval.setBounds(sizeX+110,500,100,20);
            offsetXval.setBounds(sizeX+110,520,100,20);
            offsetYval.setBounds(sizeX+110,540,100,20);
            stepXval.setBounds(sizeX+110,560,100,20);
            stepYval.setBounds(sizeX+110,580,100,20);
            load.setBounds(sizeX+10,610,250,20);
            error.setBounds(sizeX+10,610,250,20);
            add(sizeXlabel);
            add(sizeYlabel);
            add(offsetXlabel);
            add(offsetYlabel);
            add(stepXlabel);
            add(stepYlabel);
            super.add(sizeXval);
            super.add(sizeYval);
            super.add(offsetXval);
            super.add(offsetYval);
            super.add(stepXval);
            super.add(stepYval);
            add(load);
            add(error);
        }
    }
    
    public void stateChanged(ChangeEvent e) {
        changed = true;
        path = null;
        start = end = null;
        startPos.setText("");
        endPos.setText("");
        pathLength.setText("");
        pointsN.setText("");
        pointsTotalN.setText("");
        pointsCheckedN.setText("");
        time.setText("");
    }
    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == tropical) {
            climate = TROPICAL;
        } else if (src == arctic) {
            climate = ARCTIC;
        } else if (src == mountain) {
            climate = MOUNTAIN;
        } else if (src == continental) {
            climate = CONTINENTAL;
        } else if (src == open) {
            if (chooser.showOpenDialog(this) == chooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                openedFile.setText(file.getName());
                opening = true;
                sizeXlabel.setVisible(true);
                sizeYlabel.setVisible(true);
                offsetXlabel.setVisible(true);
                offsetYlabel.setVisible(true);
                stepXlabel.setVisible(true);
                stepYlabel.setVisible(true);
                sizeXval.setVisible(true);
                sizeYval.setVisible(true);
                offsetXval.setVisible(true);
                offsetYval.setVisible(true);
                stepXval.setVisible(true);
                stepYval.setVisible(true);
                load.setVisible(true);
                error.setVisible(true);
                return;
            }
        } else if (src == diagonal) {
            DijkstraHeap.diagonal = diagonal.isSelected();
        } else if (src == load) {
            int sizeX, sizeY;
            double offsetX, offsetY, stepX, stepY;
            try {
                sizeX = Integer.parseInt(sizeXval.getText());
                sizeY = Integer.parseInt(sizeYval.getText());
                offsetX = Double.parseDouble(offsetXval.getText());
                offsetY = Double.parseDouble(offsetYval.getText());
                stepX = Double.parseDouble(stepXval.getText());
                stepY = Double.parseDouble(stepYval.getText());
            } catch (NumberFormatException ex) {
                error.setText("Napaka!");
                return;
            }
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.stepX = stepX;
            this.stepY = stepY;
            error.setText("");
            t = new Tocka[sizeX][sizeY];
            Insets in = o.getInsets();
            int tmp;
            o.setSize(sizeX + in.left + in.right + 270, ((tmp = sizeY + in.top + in.bottom) < 700 ? 700 : tmp));
            double[] mm = new double[2];
            long ms = System.currentTimeMillis();
            Branje.read(file, t, mm);
            ms = System.currentTimeMillis() - ms;
            min = mm[0];
            max = mm[1];
            offscreen = new BufferedImage(t.length, t[0].length, BufferedImage.TYPE_INT_RGB);
            minValue.setValue((int)min);
            minValue.setMinimum((int)min);
            minValue.setMaximum((int)(max+1));
            maxValue.setMinimum((int)min);
            maxValue.setMaximum((int)(max+1));
            maxValue.setValue((int)(max+1));
            buffer = offscreen.getGraphics();
            sizeXlabel.setVisible(false);
            sizeYlabel.setVisible(false);
            offsetXlabel.setVisible(false);
            offsetYlabel.setVisible(false);
            stepXlabel.setVisible(false);
            stepYlabel.setVisible(false);
            sizeXval.setVisible(false);
            sizeYval.setVisible(false);
            offsetXval.setVisible(false);
            offsetYval.setVisible(false);
            stepXval.setVisible(false);
            stepYval.setVisible(false);
            load.setVisible(false);
            error.setVisible(false);
            startPos.setText("");
            endPos.setText("");
            pathLength.setText("");
            pointsN.setText("");
            pointsTotalN.setText("");
            pointsCheckedN.setText("");
            time.setText((ms/1000.0) + " s");
            opening = false;
        }
        changed = true;
        repaint();
    }
    
    public Component add(Component c) {
        c.setFocusable(false);
        return super.add(c);
    }
}