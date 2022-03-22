package SwingFrames;

import src.ChampionshipManager;
import Enums.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PointsTableFrame extends JFrame {

    ChampionshipManager f1cManager;

//    Creating all JPanels.
    JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
    JPanel pointsTablePanel = new JPanel();
    JPanel optionButtonsPanel = new JPanel(); //new FlowLayout(FlowLayout.CENTER, 10, 10)
    JPanel raceTablePanel = new JPanel();
    JPanel lastRaceTablePanel = new JPanel();
    JPanel driverRacesTablePanel = new JPanel();

    JTextField searchTextField;

//    Creating all JTables.
    JTable pointsTable;
    JTable raceTable;
    JTable lastRaceTable;
    JTable driverRacesTable;

//    Creating all DefaultTableModels that are required to populate tables.
    DefaultTableModel dtmPointsTable;
    DefaultTableModel dtmRaceTable;
    DefaultTableModel dtmLastRaceTable;
    DefaultTableModel dtmDriverRacesTable;

//    JScrollPanes to all tables.
    JScrollPane pointsTableScrollPane;
    JScrollPane raceTableScrollPane;
    JScrollPane lastRaceTableScrollPane;
    JScrollPane driverRacesTableScrollPane;

//    2D Arrays to store the data of the tables.
    String[][] pointsTableContent;
    String[][] raceTableContent;
    String[][] lastRaceTableContent;
    String[][] driverRacesTableContent;

//    All JButtons that are required.
    JButton searchBarButton;
    JButton sortByPointsDSCButton;
    JButton sortByPointsASCButton;
    JButton sortByFirstPosDescButton;
    JButton addRandomRaceButton;
    JButton getRaceTableButton;
    JButton addRandomRaceWithProbButton;

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

//    Constructor: Positioning the main panels of GUI and calling the method to show the points table,
    public PointsTableFrame(String[][] tableContent, ChampionshipManager f1cManagerRef) {

        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1015, 400);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.GRAY);
        this.getRootPane().setBackground(Color.GRAY);

//        Initializing the championship manager reference and table content 2d array.
        this.f1cManager = f1cManagerRef;
        this.pointsTableContent = tableContent;

//        Positioning of the JPanels.
        searchBarPanel.setBackground(Color.GRAY);
        searchBarPanel.setBounds(0,0,1000,50);

        pointsTablePanel.setBackground(Color.GRAY);
        pointsTablePanel.setLayout(null);
        pointsTablePanel.setBounds(0,50,1000,200);

        optionButtonsPanel.setBackground(Color.GRAY);
        optionButtonsPanel.setLayout(null);
        optionButtonsPanel.setBounds(0,250,1000,100);

        raceTablePanel.setBackground(Color.GRAY);
        raceTablePanel.setBounds(0,350,1000,200);

        lastRaceTablePanel.setBackground(Color.GRAY);
        lastRaceTablePanel.setBounds(0,350,1000,200);

        driverRacesTablePanel.setBackground(Color.GRAY);
        driverRacesTablePanel.setBounds(0,350,1000,200);

//        Getting the points table to the GUI
        showMainWindow();
    }

//    Method to show the main window with points table and other components(Buttons, TextFields etc.)
    public void showMainWindow() {
        this.setTitle("Points table");

//        textfield to get the search text from user.
        searchTextField = new JTextField();
        searchTextField.setMinimumSize(new Dimension(200, 24));
        searchTextField.setPreferredSize(new Dimension(200, 24));
        searchTextField.setFont(new Font("Arial", Font.ITALIC, 15));
        searchTextField.setBackground(Color.LIGHT_GRAY);
        searchBarPanel.add(searchTextField);

//        button to call method get the results and update the table.
        searchBarButton = new JButton("Search");
        searchBarButton.setBackground(Color.LIGHT_GRAY);
        searchBarButton.addActionListener(e -> {
            try {
                sortPointsTable(SortOp.POINTS_DSC);
                pointsTableContent = f1cManager.getSearchResults(searchTextField.getText()); //getting the search results returned from method.
                dtmPointsTable.setRowCount(0);
                for (String[] tableRow: this.pointsTableContent) {
                    dtmPointsTable.addRow(tableRow);
                }
                setVisible(true);
                JOptionPane.showMessageDialog(this, "Click on driver to watch the race history of driver.");
            } catch (IllegalArgumentException ex) {
                showErrorMsg(ex.getMessage());
            } catch (Exception ex) {
                showErrorMsg("An unexpected error occurred!");
            }
        });
        searchBarPanel.add(searchBarButton);

//        showing the points table.
        String[] ptColumnNames = {"Index No.", "Driver Name", "Constructor Team", "No. of Points", "1st Places", "2nd Places", "3rd Places", "No. of Races"};
        dtmPointsTable = new DefaultTableModel(pointsTableContent, ptColumnNames);
        pointsTable = new JTable(dtmPointsTable) {
            public boolean isCellEditable(int pointsTableContent, int columnNames){
                return false;
            }
        };

//        when the user clicked on a driver
        pointsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRowIndex = pointsTable.getSelectedRow();

                try {
//                    get the driver index from the table.
                    int driverIndex = Integer.parseInt(dtmPointsTable.getValueAt(selectedRowIndex, 0).toString())-1;
                    driverRacesTableContent = f1cManager.getDriverRacesTable(driverIndex); //call the method to get the driver race history and assigning it to the variable.

                    showDriverRacesTable();
                } catch (Exception ignored) {

                }
            }
        });
        pointsTable.setPreferredScrollableViewportSize(new Dimension(950, 150));
        pointsTable.setPreferredSize(null);
        pointsTable.setFillsViewportHeight(true);
        pointsTable.setBackground(Color.LIGHT_GRAY);
        pointsTable.setRowHeight(24);
        pointsTable.setFont(new Font("Arial", Font.BOLD, 15));

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        pointsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        pointsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        for (int i=3; i<8; i++) {
            pointsTable.getColumnModel().getColumn(i).setPreferredWidth(30);
            pointsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        pointsTableScrollPane = new JScrollPane(pointsTable);
        pointsTableScrollPane.setBounds(25,0,950,200);
        pointsTablePanel.add(pointsTableScrollPane);

//        button to sort points table with default sorting (no. of points(desc) and no of first positions(desc))
        sortByPointsDSCButton = new JButton("Sort by points (Descending)");
        sortByPointsDSCButton.setBackground(Color.LIGHT_GRAY);
        sortByPointsDSCButton.setBounds(25,10, 318, 35);
        sortByPointsDSCButton.addActionListener(e -> sortPointsTable(SortOp.POINTS_DSC));
        optionButtonsPanel.add(sortByPointsDSCButton);

//        button to sort points table with no. of points(asc)
        sortByPointsASCButton = new JButton("Sort by points (Ascending)");
        sortByPointsASCButton.setBackground(Color.LIGHT_GRAY);
        sortByPointsASCButton.setBounds(343,10, 318, 35);
        sortByPointsASCButton.addActionListener(e -> sortPointsTable(SortOp.POINTS_ASC));
        optionButtonsPanel.add(sortByPointsASCButton);

//        button to sort points table with no. of first positions(asc)
        sortByFirstPosDescButton = new JButton("Sort by first positions (Descending)");
        sortByFirstPosDescButton.setBackground(Color.LIGHT_GRAY);
        sortByFirstPosDescButton.setBounds(661,10, 316, 35);
        sortByFirstPosDescButton.addActionListener(e -> sortPointsTable(SortOp.FIRSTPOS_DSC));
        optionButtonsPanel.add(sortByFirstPosDescButton);

//        button to add a random race and call method tp show the details of the race.
        addRandomRaceButton = new JButton("Add random race");
        addRandomRaceButton.setBackground(Color.LIGHT_GRAY);
        addRandomRaceButton.setBounds(25,45, 318, 35);
        addRandomRaceButton.addActionListener(e -> {
            try {
                lastRaceTableContent = f1cManager.addRandomRace(RaceAddOp.WITHOUT_PROB);
                sortPointsTable(SortOp.POINTS_DSC);
                showLastRaceTable();
            } catch (IllegalArgumentException ex) {
                showErrorMsg(ex.getMessage());
            } catch (Exception ex) {
                showErrorMsg("An unexpected error occurred!");
            }
        });
        optionButtonsPanel.add(addRandomRaceButton);

//        button to add a random race (first position must include considering probability) and call method tp show the details of the race
        addRandomRaceWithProbButton = new JButton("Add random race (With Prob.)");
        addRandomRaceWithProbButton.setBackground(Color.LIGHT_GRAY);
        addRandomRaceWithProbButton.setBounds(343,45, 318, 35);
        addRandomRaceWithProbButton.addActionListener(e -> {
            try {
                lastRaceTableContent = f1cManager.addRandomRace(RaceAddOp.WITH_PROB);
                sortPointsTable(SortOp.POINTS_DSC);
                showLastRaceTable();
            } catch (IllegalArgumentException ex) {
                showErrorMsg(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMsg("An unexpected error occurred!");
            }
        });
        optionButtonsPanel.add(addRandomRaceWithProbButton);

//        button to show the previous races
        getRaceTableButton = new JButton("Show previous races");
        getRaceTableButton.setBackground(Color.LIGHT_GRAY);
        getRaceTableButton.setBounds(661,45, 316, 35);
        getRaceTableButton.addActionListener(e -> {
            raceTableContent = f1cManager.getRaceTable();
            showRaceTable();
        });
        optionButtonsPanel.add(getRaceTableButton);

//        after adding all Tables, Buttons, TextFields to relevant panels, adding the three panels to the main frame.
        add(searchBarPanel);
        add(pointsTablePanel);
        add(optionButtonsPanel);

        setVisible(true);
    }

//    method to add race table of history of the specific driver to the panel and panel to the frame.
    private void showDriverRacesTable() {
        driverRacesTablePanel.removeAll(); //removing current components in case the method used for a previous driver.

//        disable the visibility of other tables. (lastRaceTable, RaceTable, and driverRacesTable appears in the same position. so only one table can show at a time.
        lastRaceTablePanel.setVisible(false);
        raceTablePanel.setVisible(false);

//        adding the driverRacesTable to the relevant panel.
        String[] driverRacesTableColumnNames = {"Race Date", "Driver Name", "Place of the driver"};
        dtmDriverRacesTable = new DefaultTableModel(this.driverRacesTableContent, driverRacesTableColumnNames);
        driverRacesTable = new JTable(dtmDriverRacesTable) {
            public boolean isCellEditable(int driverRacesTableContent, int driverRacesTableColumnNames){
                return false;
            }
        };
        driverRacesTable.setPreferredScrollableViewportSize(new Dimension(900, 200));
        driverRacesTable.setFillsViewportHeight(true);
        driverRacesTable.setBackground(Color.LIGHT_GRAY);
        driverRacesTable.setRowHeight(24);
        driverRacesTable.setFont(new Font("Arial", Font.BOLD, 15));

        driverRacesTableScrollPane = new JScrollPane(driverRacesTable);
        driverRacesTableScrollPane.setBounds(25,25,950,200);
        driverRacesTablePanel.add(driverRacesTableScrollPane);

//        adding the panel to the frame.
        add(driverRacesTablePanel);
        this.setSize(1015, 600);
        driverRacesTablePanel.setVisible(true);
        setVisible(true);
    }

//    method to add table containing previous races.
    private void showRaceTable() {
        raceTablePanel.removeAll();
        lastRaceTablePanel.setVisible(false);
        driverRacesTablePanel.setVisible(false);

//        adding raceTable to the relevant panel.
        String[] raceTableColumnNames = {"Race Date", "1st Place", "2st Place", "3st Place"};
        dtmRaceTable = new DefaultTableModel(raceTableContent, raceTableColumnNames);
        raceTable = new JTable(dtmRaceTable) {
            public boolean isCellEditable(int raceTableContent, int raceTableColumnNames){
                return false;
            }
        };
        raceTable.setPreferredScrollableViewportSize(new Dimension(900, 200));
        raceTable.setFillsViewportHeight(true);
        raceTable.setBackground(Color.LIGHT_GRAY);
        raceTable.setRowHeight(24);
        raceTable.setFont(new Font("Arial", Font.BOLD, 15));

        raceTableScrollPane = new JScrollPane(raceTable);
        raceTableScrollPane.setBounds(25,25,950,200);
        raceTablePanel.add(raceTableScrollPane);

//        adding the panel to the frame.
        add(raceTablePanel);
        this.setSize(1015, 600);
        raceTablePanel.setVisible(true);
        setVisible(true);
    }

//    method to add the table containing details of the last generated race.
    private void showLastRaceTable() {
        raceTablePanel.setVisible(false);
        driverRacesTablePanel.setVisible(false);
        lastRaceTablePanel.removeAll();

//        adding the lastRaceTable to the relevant panel.
        String[] lastRaceTableColumnNames = {"Driver Name", "Race Position"};
        dtmLastRaceTable = new DefaultTableModel(lastRaceTableContent, lastRaceTableColumnNames);
        lastRaceTable = new JTable(dtmLastRaceTable) {
            public boolean isCellEditable(int lastRaceTableContent, int lastRaceTableColumnNames){
                return false;
            }
        };
        lastRaceTable.setPreferredScrollableViewportSize(new Dimension(900, 150));
        lastRaceTable.setFillsViewportHeight(true);
        lastRaceTable.setBackground(Color.LIGHT_GRAY);
        lastRaceTable.setRowHeight(24);
        lastRaceTable.setFont(new Font("Arial", Font.BOLD, 15));

        lastRaceTableScrollPane = new JScrollPane(lastRaceTable);
        lastRaceTableScrollPane.setBounds(25,25,950,200);
        lastRaceTablePanel.add(lastRaceTableScrollPane);

//        adding the panel to the frame.
        add(lastRaceTablePanel);
        this.setSize(1015, 600);
        lastRaceTablePanel.setVisible(true);
        setVisible(true);
    }

//    method to sort the points table with the required sorting.
    private void sortPointsTable(SortOp sortOp) {
        this.pointsTableContent = f1cManager.getPointsTable(sortOp); //get the points table after the required sorting.
        dtmPointsTable.setRowCount(0);
        for (String[] tableRow: this.pointsTableContent) {
            dtmPointsTable.addRow(tableRow);
        }
        setVisible(true);
    }

//    simple method to get the error message and show it in a dialogue box as a error message.
    private void showErrorMsg(String errorMsg){
        JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
