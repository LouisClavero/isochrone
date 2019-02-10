package ch.epfl.isochrone.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JViewport;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.epfl.isochrone.geo.PointOSM;
import ch.epfl.isochrone.geo.PointWGS84;
import ch.epfl.isochrone.tiledmap.CachedTileProvider;
import ch.epfl.isochrone.tiledmap.ColorTable;
import ch.epfl.isochrone.tiledmap.IsochroneTileProvider;
import ch.epfl.isochrone.tiledmap.OSMTileProvider;
import ch.epfl.isochrone.tiledmap.TileProvider;
import ch.epfl.isochrone.tiledmap.TransparentTileProvider;
import ch.epfl.isochrone.timetable.Date;
import ch.epfl.isochrone.timetable.Date.Month;
import ch.epfl.isochrone.timetable.FastestPathTree;
import ch.epfl.isochrone.timetable.Graph;
import ch.epfl.isochrone.timetable.SecondsPastMidnight;
import ch.epfl.isochrone.timetable.Service;
import ch.epfl.isochrone.timetable.Stop;
import ch.epfl.isochrone.timetable.TimeTable;
import ch.epfl.isochrone.timetable.TimeTableReader;

public final class IsochroneTL {
    private static final String OSM_TILE_URL = "http://b.tile.openstreetmap.org/";
    private static final int INITIAL_ZOOM = 11;
    private static final PointWGS84 INITIAL_POSITION = new PointWGS84(Math.toRadians(6.476), Math.toRadians(46.613));
    private static final String INITIAL_STARTING_STOP_NAME = "Lausanne-Flon";
    private static final int INITIAL_DEPARTURE_TIME = SecondsPastMidnight.fromHMS(6, 8, 0);
    private static final Date INITIAL_DATE = new Date(1, Month.OCTOBER, 2013);
    private static final int WALKING_TIME = 5 * 60;
    private static final double WALKING_SPEED = 1.25;

    private final TiledMapComponent tiledMapComponent;

    private ColorTable colorTable;
    private Date initialDate;
    private String initialStartingStopName;
    private int initialZoom;
    private int walkingTime;
    private double walkingSpeed;
    private Stop initialStartingStop;
    private Set<Stop> stops;
    TimeTableReader t = new TimeTableReader("/time-table/");      
    TimeTable time = t.readTimeTable(); 
    private Set<Service> initialService;
    private Graph g;
    private FastestPathTree fpt;
    private int initialDepartureTime = INITIAL_DEPARTURE_TIME;
    private TileProvider transparentTileProvider;
    private Map<String, Stop> stopNames;
    
    public IsochroneTL() throws IOException {

        initialDate = INITIAL_DATE;
        int initialDepartureTime = INITIAL_DEPARTURE_TIME;
        walkingTime=WALKING_TIME;
        walkingSpeed = WALKING_SPEED;
        initialZoom = INITIAL_ZOOM;
        stops = time.stops();
        initialStartingStopName = INITIAL_STARTING_STOP_NAME;
        initialStartingStop = stopNames.get(initialStartingStopName);
        Graph g = t.readGraphForServices(time.stops(), time.servicesForDate(initialDate), walkingTime, walkingSpeed);
        FastestPathTree fpt = g.fastestPaths(initialStartingStop, initialDepartureTime);
        TileProvider bgTileProvider = new CachedTileProvider(new OSMTileProvider(new URL(OSM_TILE_URL)));
        tiledMapComponent = new TiledMapComponent(initialZoom);
        List<Color> colorList = new ArrayList<>();
        colorTable = new ColorTable(walkingTime, colorList);
        TileProvider isochronetp = new IsochroneTileProvider(fpt, colorTable, walkingSpeed);
        TileProvider transparentTP = new TransparentTileProvider(isochronetp, 0.5);
        tiledMapComponent.addTP(bgTileProvider);
        tiledMapComponent.addTP(transparentTP);

    }

//    private static Map<String, Stop> generateMapStop(Set<Stop> stops) {
//        Map<String, Stop> map = new HashMap<>();
//        for (Stop stop : stops) {
//            map.put(stop.name(), stop);
//        }
//        return map;
//
//    }

    private JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(tiledMapComponent);
        PointOSM startingPosOSM = INITIAL_POSITION.toOSM(tiledMapComponent.zoom());
        viewPort.setViewPosition(new Point(startingPosOSM.roundedX(), startingPosOSM.roundedY()));

        final JPanel copyrightPanel = createCopyrightPanel();

        final JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 300));

        layeredPane.add(viewPort, new Integer(0));
        layeredPane.add(copyrightPanel, new Integer(1));

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Rectangle newBounds = layeredPane.getBounds();
                viewPort.setBounds(newBounds);
                copyrightPanel.setBounds(newBounds);

                viewPort.revalidate();
                copyrightPanel.revalidate();
            }
        });
        final Point mousePosition = new Point();
        final Point p = new Point();
        layeredPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mousePosition.move(e.getXOnScreen(), e.getYOnScreen()); 
                p.move(viewPort.getViewPosition().x, viewPort.getViewPosition().y);
            }
        });

        layeredPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                viewPort.setViewPosition(new Point(p.x-e.getLocationOnScreen().x-mousePosition.x, p.y-e.getLocationOnScreen().y-mousePosition.y));
            }
        });

        layeredPane.addMouseWheelListener(new MouseAdapter() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e){
                int newZoom = tiledMapComponent.zoom() - e.getWheelRotation();
                if (newZoom<10) {
                    newZoom = 10;
                }
                if (newZoom>19) {
                    newZoom = 19;
                }
                Point mousePosition = e.getPoint();
                Point newPoint = SwingUtilities.convertPoint(layeredPane, e.getPoint(), tiledMapComponent);
                PointOSM pOSM = new PointOSM(newZoom, newPoint.getX(), newPoint.getY());
                pOSM = pOSM.atZoom(newZoom);
                tiledMapComponent.setZoom(newZoom);
                viewPort.setViewPosition(new Point(pOSM.roundedX() - mousePosition.y, pOSM.roundedY() - mousePosition.y));

            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(layeredPane, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createCopyrightPanel() {
        Icon tlIcon = new ImageIcon(getClass().getResource("/images/tl-logo.png"));
        String copyrightText = "Données horaires 2013. Source : Transports publics de la région lausannoise / Carte : © contributeurs d'OpenStreetMap";
        JLabel copyrightLabel = new JLabel(copyrightText, tlIcon, SwingConstants.CENTER);
        copyrightLabel.setOpaque(true);
        copyrightLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        copyrightLabel.setBackground(new Color(0f, 0f, 0f, 0.4f));
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 5, 0));

        JPanel copyrightPanel = new JPanel(new BorderLayout());
        copyrightPanel.add(copyrightLabel, BorderLayout.PAGE_END);
        copyrightPanel.setOpaque(false);
        return copyrightPanel;
    }

    private void start() {
        JFrame frame = new JFrame("Isochrone TL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(createCenterPanel(), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new IsochroneTL().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDate(Date d){
        if(initialDate.compareTo(d)!=0){
            this.initialDate=d;
            updateService();	        
        }
    }

    private void updateService(){
        Set<Service> services = time.servicesForDate(initialDate);
        if(!(services.equals(initialService))){
            this.initialService=services;
            updateGraph();
        }
    }

    private void updateGraph() {
        Graph newGraph = null;
        try {
            newGraph = t.readGraphForServices(stops, initialService, walkingTime, walkingSpeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!newGraph.equals(g)){
            this.g=newGraph;
            updateFastestPathTree();
        }
        
    }

    private void updateFastestPathTree() {
        FastestPathTree fast = g.fastestPaths(initialStartingStop, initialDepartureTime);
        if(!(fast.equals(fpt))){
            this.fpt=fast;
            updateIsochrone();
        }
    }

    private void updateIsochrone() {
       TileProvider ttp = new CachedTileProvider(new TransparentTileProvider(new IsochroneTileProvider(fpt, colorTable, walkingSpeed), 0.5));
       tiledMapComponent.removeTileProvider(transparentTileProvider);
       tiledMapComponent.addTileProvider(ttp);
       this.transparentTileProvider = ttp;
    }
    
    private void setDepartureTime(int newDepartureTime){
        if(SecondsPastMidnight.hours(newDepartureTime)<4){
            setDate(initialDate.relative(-1));
            newDepartureTime+=SecondsPastMidnight.fromHMS(24,0,0);
        }
        if(newDepartureTime != initialDepartureTime){
            this.initialDepartureTime = newDepartureTime;
        }
    }
    
    private void setStop(Stop stop){
        if(!(initialStartingStop.equals(stop))){
            this.initialStartingStop = stop;
        }
    }
    
    @SuppressWarnings("deprecation")
    private JComponent createPanel() {
        JPanel jp = new JPanel(new FlowLayout());
        List<String> names = new ArrayList<String>();
        for(Stop s: stops){
            names.add(s.name());
        }
        java.util.Collections.sort(names);
        List<Stop> stop = new ArrayList<Stop>();
        for(String n : names){
            stop.add(stopNames.get(n));
        }
        final JComboBox<Stop> menu = new JComboBox<Stop>(new Vector<Stop>(stop));
        menu.setSelectedItem(initialStartingStop);
        menu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                setStop((Stop)menu.getSelectedItem());
            }
        });
        
        final SpinnerDateModel sdm = new SpinnerDateModel();
        java.util.Date date = initialDate.toJavaDate();
        date.setHours(SecondsPastMidnight.hours(initialDepartureTime));
        date.setMinutes(SecondsPastMidnight.minutes(initialDepartureTime));
        date.setSeconds(SecondsPastMidnight.seconds(initialDepartureTime));
        sdm.setValue(date);
        sdm.addChangeListener(new ChangeListener() {
        
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setDate(new Date(sdm.getDate()));
                setDepartureTime(SecondsPastMidnight.fromJavaDate(sdm.getDate()));
            }
                
        });
        JSpinner js = new JSpinner(sdm); 
        jp.add(new JLabel("Départ"));
        jp.add(menu);
        jp.add(new JSeparator());
        jp.add(new JLabel("Date et Heure"));
        jp.add(js);
        return jp;
        
    }    

}
