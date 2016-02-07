

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package textpad;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

/**
 *
 * @author siva
 * @version 1.0
 */
public class TextPad{

    /**
     * @param args the command line arguments
     */
    
    //TextArea For EditorArea
    private JTextArea editortext;
    
    //Frame contains all the window elements
    private JFrame frame;
    
    //Title of the frame
    private String title;
    
    // Menu Bar
    private JMenuBar menubar;
    
    //Menus
    private JMenu filemenu;
    private JMenu editmenu;
    private JMenu viewmenu;
    private JMenu searchmenu;
    private JMenu formatmenu;
    private JMenu convertcase;
     
    //Menu Items
    private JMenuItem newitem;
    private JMenuItem openitem;
    private JMenuItem exititem;
    private JMenuItem saveitem;
    private JMenuItem saveasitem;
    private JMenuItem undoitem;
    private JMenuItem redoitem;
    private JMenuItem cutitem;
    private JMenuItem copyitem;
    private JMenuItem pasteitem;
    private JMenuItem deleteitem;
    private JMenuItem datetimeitem;
    private JMenuItem selectallitem;
    private JMenuItem textcoloritem;
    private JMenu copytoclipboardmenu;
    private JMenuItem copyfilenameitem;
    private JMenuItem copyfilepathitem;
    private JMenuItem copydirpathitem;
    private JMenuItem fontmenuitem;
    private JMenuItem tolowercaseitem;
    private JMenuItem touppercaseitem;
    private JMenuItem finditem;
    private JMenuItem replaceitem;
    private JMenuItem advancedsearch;
    private JMenuItem zoomin;
    private JMenuItem zoomout;
    
    //Checkbox menu items
    private JCheckBoxMenuItem setreadonlyitem;
    private JCheckBoxMenuItem viewtoolbar;
    private JCheckBoxMenuItem viewstatusbar;
    private JCheckBoxMenuItem fullscreenview;
    private JCheckBoxMenuItem readview;
    private JCheckBoxMenuItem wordwrap;
    
    //Toolbar
    private JToolBar toolbar;
    
    //Toolbar buttons
    private JButton newbtn;
    private JButton openbtn;
    private JButton savebtn;
    private JButton copybtn;
    private JButton pastebtn;
    private JButton cutbtn;
    private JButton searchbtn;
    private JButton undobtn;
    private JButton redobtn;
    private JButton deletebtn;
    private JButton zoominbtn;
    private JButton zoomoutbtn;
   
    //Combo boxes for fontsize and style
    private JComboBox fontsizecombo;
    private JComboBox fontstylecombo;
    
    private JTextField gotoline;
    //Statusbar
    private JLabel statusbar;
   
    //For filename for the current file
    private String filename;
   
    private JScrollPane scrollpane;
   
    //Number of lines and num of characters for textarea
    private int NUM_LINES=50;
    private int NUM_CHARS=60;
   
    //Font 
    private String fontname;
    private int fontstyle;
    private int fontsize;
    private int lineno;
    //used in font panel for display sampletext when font changes
    private JLabel sampletxt;
    
    private String text;
    private static int end,pos;
  
    //search and replace strings in search & replace panel
    private String srchstr, replacestr;
    
    //Selected file from Filechooser and it's Parentfile' fullpath
    private  File selectedfile,parentfile;
    
    //list all the files in Selectedfile's directory
    private  File [] dirfiles;
    
    //ColorChooser for choose a textcolor
    private JColorChooser clrchooser;
    
    //List for Available fonts
    private JList fontlist;
   
   //Buttons for Find & replace panel
    private JButton fndnxtbtn,rplcebtn,replceallbtn;
    
    //Highlighter for highlights the text when searching a text
    private Highlighter.HighlightPainter panit;
    
   
    //Popupmenu when user click's clicked right mouse button in textarea
    private JPopupMenu popupmenu; 
    
    //Popupmenuitems
    private JMenuItem popupundoitem;
    private JMenuItem popupredoitem;
    private JMenuItem popupcutitem;
    private JMenuItem popupcopyitem;
    private JMenuItem popuppasteitem;
    private JMenuItem popupdeleteitem;
    private JMenuItem popupfontitem;
    private JMenu popupconvertcasemenu;
    private JMenuItem popuptoloweritem;
    private JMenuItem popuptoupperitem;
   
    //represents the file's saved state, if file successfully saved it's changed to 1
    private static int savflg=0;
    
    //represents the file's saved state when user click's the close button. if file successfully saved it's changed to 1
    private static int closesavflg=0;
   
   // undomanager for make undo & redo in editortext
    protected UndoManager undomanager;
    private Image icon;
     private ImageIcon appicon ;
    /* Constructor 
     * Here add menubar items added to menubar, toolbar buttons are added to Toolbar.
     * 
     * Menubar ,Toolbar,Editortext(TextArea)and status bar added to Frame
     * 
     * Add  listeners to particular menuitems and toolbar buttons.
    
    */
    public TextPad()     {
            frame=new JFrame();
            frame.setTitle("TextPad");
       
            ClassLoader cldr=Thread.currentThread().getContextClassLoader();
            
            //get icon as INPUTSTREAM
            InputStream input=cldr.getResourceAsStream("icon.png");
             
            try 
            {
                icon = ImageIO.read(input);
                appicon=new ImageIcon(icon);
                frame.setIconImage(icon);
            }
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            //When close btn click ask for save so set DO_NOTHING_ON_CLOSE
                
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            
            //assign size of textarea
            
            editortext=new JTextArea(NUM_LINES,NUM_CHARS);
            
            //Insets is used to assign set margin to editortext
            Insets s;
            s=new Insets(15,15,15,15);
            
            editortext.setMargin(s);
            //add editertetx into scrollpane
            scrollpane=new JScrollPane(editortext);
        
            editortext.requestFocusInWindow();
            //Set default font as TimesNeWRoman and Size is 12 And Style is palin to editortext
            Font defaultfont=new Font("TimesNewRoman",Font.PLAIN,12);
            editortext.setFont(defaultfont);
        
            //add scrollpane to contentpane
            frame.add(scrollpane);
            menubar=new JMenuBar();
            filemenu=new JMenu("File");
            editmenu=new JMenu("Edit");
            viewmenu=new JMenu("View");
            searchmenu=new JMenu("Search");

            //create menuitems
            newitem =new JMenuItem("New");
            openitem=new JMenuItem("Open");
            saveitem=new JMenuItem("Save");
            saveasitem=new JMenuItem("Save As");
            exititem=new JMenuItem("Exit");
            setreadonlyitem=new JCheckBoxMenuItem("Set ReadOnly");
            setreadonlyitem.setState(false);
            
            //Assign Shortcut Keys to  menus and their items
            filemenu.setMnemonic(KeyEvent.VK_F);
            newitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
            newitem.setMnemonic(KeyEvent.VK_N);
            openitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK)); 
            openitem.setMnemonic(KeyEvent.VK_O);
            saveitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
            saveitem.setMnemonic(KeyEvent.VK_S);
            exititem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.ALT_MASK));
            exititem.setMnemonic(KeyEvent.VK_X);
        
             //Add Filemenu items to filemenu
            filemenu.add(newitem);
            filemenu.add(openitem);
            filemenu.addSeparator();
            filemenu.add(saveitem);
            filemenu.add(saveasitem);
            filemenu.addSeparator();
            filemenu.add(setreadonlyitem);
            filemenu.addSeparator();
            filemenu.add(exititem);
        
       
            undoitem=new JMenuItem("Undo");
            redoitem=new JMenuItem("Redo");
            cutitem=new JMenuItem("Cut");
            copyitem=new JMenuItem("Copy");
            pasteitem=new JMenuItem("Paste");
            deleteitem=new JMenuItem("Delete");
            selectallitem=new JMenuItem("Select All");
            datetimeitem=new JMenuItem("Time/Date");
            copytoclipboardmenu=new JMenu("Copy to Clipboard");
            copyfilepathitem=new JMenuItem("Copy File Path to ClipBoard");
            copyfilenameitem=new JMenuItem("Copy File Name to ClipBoard");
            copydirpathitem=new JMenuItem("Copy Dir.path to ClipBoard");
            textcoloritem=new JMenuItem("Text Color");
            convertcase=new JMenu("Convert Case");
            tolowercaseitem =new JMenuItem("To Lowercase");
            touppercaseitem=new JMenuItem("To Uppercase");
            editmenu.setMnemonic(KeyEvent.VK_E);
            undoitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK)); 
            redoitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,ActionEvent.CTRL_MASK)); 
            copyitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
            undoitem.setMnemonic(KeyEvent.VK_Z);
            redoitem.setMnemonic(KeyEvent.VK_Y);
            copyitem.setMnemonic(KeyEvent.VK_C);
            pasteitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK)); 
            pasteitem.setMnemonic(KeyEvent.VK_V);
            deleteitem.setMnemonic(KeyEvent.VK_DELETE);
            deleteitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
            selectallitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
            cutitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
            cutitem.setMnemonic(KeyEvent.VK_X);
            copytoclipboardmenu.add(copyfilenameitem);
            copytoclipboardmenu.add(copyfilepathitem);
            copytoclipboardmenu.add(copydirpathitem);
            convertcase.add(tolowercaseitem);
            convertcase.add(touppercaseitem);
       
             //addSeperator is used to add space bettween two menuitems in menu
            editmenu.add(undoitem);
            editmenu.add(redoitem);
            editmenu.addSeparator();
            editmenu.add(cutitem);
            editmenu.add(copyitem);
            editmenu.add(pasteitem);
            editmenu.add(deleteitem);
            editmenu.addSeparator();
            editmenu.add(selectallitem);
            editmenu.addSeparator();
            editmenu.add(datetimeitem);
            editmenu.addSeparator();
            editmenu.add(copytoclipboardmenu);
            editmenu.addSeparator();
            editmenu.add(convertcase);
            
       
            //View menu and it's items            
            viewtoolbar=new JCheckBoxMenuItem("ToolBar");
            viewtoolbar.setState(true);
            viewstatusbar=new JCheckBoxMenuItem("StatusBar");
            viewstatusbar.setState(true);
            fullscreenview =new JCheckBoxMenuItem("View FullScreen");
            fullscreenview.setState(false);
            readview =new JCheckBoxMenuItem("Read View");
            zoomin=new JMenuItem("Zoomin");
            zoomout=new JMenuItem("ZoomOut");
            viewmenu.add(viewtoolbar);
            viewmenu.addSeparator();
            viewmenu.add(viewstatusbar);
            viewmenu.addSeparator();
            viewmenu.add(fullscreenview);
            viewmenu.addSeparator();
            viewmenu.add(readview);
            viewmenu.addSeparator();
            viewmenu.add(zoomin);
            viewmenu.add(zoomout);
            viewmenu.setMnemonic(KeyEvent.VK_V);
            
      
            //format menu and it's items
            formatmenu=new JMenu("Format");
            wordwrap=new JCheckBoxMenuItem("Word wrap");
            wordwrap.setState(false);
            fontmenuitem=new JMenuItem("Font");
            formatmenu.add(wordwrap);
            formatmenu.addSeparator();
            formatmenu.add(fontmenuitem);
            formatmenu.addSeparator();
            formatmenu.add(textcoloritem);
        
            //Find menu and it;s items
            finditem=new JMenuItem("Find");
            replaceitem=new JMenuItem("Replace");
            advancedsearch=new JMenuItem("Advanced search");
            searchmenu.setMnemonic(KeyEvent.VK_S);
            finditem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,ActionEvent.CTRL_MASK));
            
            searchmenu.add(finditem);
            searchmenu.add(replaceitem);
            //searchmenu.add(advancedsearch);

            //Add all menus to menubar
            menubar.add(filemenu);
            menubar.add(editmenu);
            menubar.add(formatmenu);
            menubar.add(viewmenu);
            menubar.add(searchmenu);
        
            //Setmenubar to frame
            frame.setJMenuBar(menubar);
       
            //Toolbar and it's buttons
            toolbar=new JToolBar();
            InputStream newip=cldr.getResourceAsStream("new.png");
            newbtn=new JButton() ;
            Image newic;
            try 
            {
                //create for image for new icon
                newic=ImageIO.read(newip);
                //create ImageIcon from the image newic
                ImageIcon ib=new ImageIcon(newic);
                //set newic as newbtn's icon
                newbtn.setIcon(ib);
                newbtn.setToolTipText("New file");
            } 
            
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            InputStream openip=cldr.getResourceAsStream("open.png");
            Image openimg;
            try 
            {
                openimg=ImageIO.read(openip);
                ImageIcon openicon=new ImageIcon(openimg);
                openbtn=new JButton(openicon);
                openbtn.setToolTipText("Open File");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
            InputStream saveip=cldr.getResourceAsStream("save.png");
            Image saveimg;
            try 
            {
                saveimg=ImageIO.read(saveip);
                ImageIcon saveicon=new ImageIcon(saveimg);
                savebtn=new JButton(saveicon);
                savebtn.setToolTipText("save");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
       
         
            InputStream cutip=cldr.getResourceAsStream("cut.png");
            Image cutimg;
            try 
            {
                cutimg=ImageIO.read(cutip);
                ImageIcon cuticon=new ImageIcon(cutimg);
                cutbtn=new JButton(cuticon);
                cutbtn.setToolTipText("Cut");
            }
            catch (IOException ex)
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            InputStream copyip=cldr.getResourceAsStream("copy.png");
            Image copyimg;
            try 
            {
                copyimg=ImageIO.read(copyip);
                ImageIcon copyicon=new ImageIcon(copyimg);
                 copybtn=new JButton(copyicon);
                 copybtn.setToolTipText("Copy");
            }
            catch (IOException ex)
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    
            InputStream pasteip=cldr.getResourceAsStream("paste.png");
            Image pasteimg;
            try 
            {
                pasteimg=ImageIO.read(pasteip);
                ImageIcon pasteicon=new ImageIcon(pasteimg);
                pastebtn=new JButton(pasteicon);
                pastebtn.setToolTipText("Paste");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
    
            InputStream deleteip=cldr.getResourceAsStream("delete.png");
            Image deleteimg;
            try 
            {
                deleteimg=ImageIO.read(deleteip);
                ImageIcon deleteicon=new ImageIcon(deleteimg);
                deletebtn=new JButton(deleteicon);
                deletebtn.setToolTipText("Delete");
            } 
            catch (IOException ex)
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
      
            InputStream srchip=cldr.getResourceAsStream("search.png");
            Image srchimg;
            try 
            {
                srchimg=ImageIO.read(srchip);
                ImageIcon searchicon=new ImageIcon(srchimg);
                searchbtn=new JButton(searchicon);
                searchbtn.setToolTipText("Find");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            InputStream undoip=cldr.getResourceAsStream("undo.png");
            Image undoimg;
            try 
            {
             undoimg=ImageIO.read(undoip);
             ImageIcon undoicon=new ImageIcon(undoimg);
             undobtn=new JButton(undoicon);
             undobtn.setToolTipText("Undo");
            } 
            catch (IOException ex)  
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            InputStream redoip=cldr.getResourceAsStream("redo.png");
            Image redoimg;
            try 
            {
                redoimg=ImageIO.read(redoip);
                ImageIcon redoicon=new ImageIcon(redoimg);
                redobtn=new JButton(redoicon);
                redobtn.setToolTipText("Redo");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
            }
      
            InputStream zoominip=cldr.getResourceAsStream("zoomIn.png");
            Image zoominimg;
            try
            {
                zoominimg=ImageIO.read(zoominip);
                ImageIcon zoominicon=new ImageIcon(zoominimg);
                zoominbtn=new JButton(zoominicon);
                        
                zoominbtn.setToolTipText("Zoom In");
            }
            catch(IOException e)
            {
                 Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, e);
            }
            
      
            
            InputStream zoomoutip=cldr.getResourceAsStream("zoomOut.png");
            Image zoomoutimg;
            try
            {
                zoomoutimg=ImageIO.read(zoomoutip);
                ImageIcon zoomouticon=new ImageIcon(zoomoutimg);
                zoomoutbtn=new JButton(zoomouticon);
                zoomoutbtn.setToolTipText("Zoom Out");
            }
            catch(IOException e)
            {
                 Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, e);
            }
            
            zoomin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD,ActionEvent.CTRL_MASK));
            zoomout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT,ActionEvent.CTRL_MASK));
            //Dimensions for fontsizecombo and fontstylecombo
            Dimension fontsizedim=new Dimension(50,30);
            Dimension fontstyledim=new Dimension(75,30);
            fontsizecombo=new JComboBox();
            fontsizecombo.setMaximumSize(fontsizedim );
            
            //Add  fontsizes to fontsizecombo
            setfontsizecombo();
            //Initially "12" is selected in fontsizecombo because defaultfont has size 12
            fontsizecombo.setSelectedItem(makeobject("12"));
            fontstylecombo=new JComboBox();
            fontstylecombo.setMaximumSize(fontstyledim);
            
            //Add font styles to fontstylecombo
            setfontstylecombo();
            fontstylecombo.setSelectedItem(makeobject("PLAIN"));
             
            //GraphicsEnvironment for getting available fonts
            GraphicsEnvironment gpen= GraphicsEnvironment.getLocalGraphicsEnvironment();
            //get allthe avilabel fontfamily names in array
            String[] fonts=gpen.getAvailableFontFamilyNames();
            fontlist=new JList(fonts);
            fontlist.setSelectedValue(makeobject("TimesNewRoman"), true);
            
            gotoline=new JTextField(5);
          
          
            gotoline.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                gotolinelistener();
            }

            
           });
           
           
          
            //Add toolbar menus to toolbar
            toolbar.add(newbtn);
            toolbar.add(openbtn);
            toolbar.add(savebtn);
            toolbar.add(copybtn);
            toolbar.add(cutbtn);
            toolbar.add(pastebtn);
            toolbar.add(deletebtn);
            toolbar.addSeparator();
            toolbar.add(undobtn);
            toolbar.add(redobtn);
            toolbar.addSeparator();
            toolbar.add(searchbtn);
            toolbar.addSeparator();
            toolbar.add(zoominbtn);
            toolbar.add(zoomoutbtn);
            toolbar.addSeparator();
            toolbar.add(gotoline);
            toolbar.addSeparator();
            toolbar.add(fontstylecombo);
            toolbar.addSeparator();
            toolbar.add(fontsizecombo);
            statusbar=new JLabel();
            
            //PopupMenu items
     
            popupmenu=new JPopupMenu();
            popupundoitem=new JMenuItem("Undo");
            popupredoitem=new JMenuItem("Redo");
            popupcutitem=new JMenuItem("Cut");
            popupcopyitem=new JMenuItem("Copy");
            popuppasteitem=new JMenuItem("Paste");
            popupdeleteitem=new JMenuItem("Delete");
            popupconvertcasemenu=new JMenu("Convert Case");
            popuptoloweritem =new JMenuItem("To Lowercase");
            popuptoupperitem =new JMenuItem("To uppercase");
            popupfontitem=new JMenuItem("Font");
     
            popupconvertcasemenu.add(popuptoupperitem);
            popupconvertcasemenu.add(popuptoloweritem);
     
            //add PopupmenuItems
            popupmenu.add(popupundoitem);
            popupmenu.add(popupredoitem);
            popupmenu.addSeparator();
            popupmenu.add(popupcutitem);
            popupmenu.add(popupcopyitem);
            popupmenu.add(popuppasteitem);
            popupmenu.add(popupdeleteitem);
            popupmenu.addSeparator();
            popupmenu.add(popupfontitem);
            popupmenu.addSeparator();
            popupmenu.add(popupconvertcasemenu);
            
            //add status bar to bottom of the frame
            frame.add(statusbar,BorderLayout.SOUTH);
            frame.add(toolbar,BorderLayout.BEFORE_FIRST_LINE);
            editortext.requestFocus();
            
            //Highlighter for Selecting texts when find & replace operations
            panit=new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
            
            //Undomanager for undo & redo operations
            undomanager=new UndoManager();
   
    frame.addWindowListener(new WindowAdapter()
    {
     //call when close btn clicked    
          public void windowClosing(WindowEvent e)
            {
               // ask user to save or exit
                saveBeforeExit();
            }
     });
         
    zoomin.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Font f=editortext.getFont();
                Font newfont=new Font(f.getName(),f.getStyle(),f.getSize()+2);
                editortext.setFont(newfont);
                
            }
    
    });
    zoomout.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                 Font f=editortext.getFont();
                 Font newfont;
                if(f.getSize()>2)
                {
                    newfont=new Font(f.getName(),f.getStyle(),f.getSize()-2);
                    editortext.setFont(newfont);
                }
            }
    
    
    });
    
    zoominbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Font f=editortext.getFont();
                Font newfont=new Font(f.getName(),f.getStyle(),f.getSize()+2);
                editortext.setFont(newfont);
                
            }
    
    });
    zoomoutbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                 Font f=editortext.getFont();
                 Font newfont;
                if(f.getSize()>2)
                {
                    newfont=new Font(f.getName(),f.getStyle(),f.getSize()-2);
                    editortext.setFont(newfont);
                }
            }
    
    
    });
    
    
    editortext.addMouseListener(new MouseListener()
    {
          @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton()==e.BUTTON3)
                {
                    // show the popupmenu when clicks Mouse Right click
                    popupmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
          // Unimplemented Methods
         @Override
            public void mouseExited(MouseEvent e) {
                
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }
     
 });
     
     editortext.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) 
            {
                //Remove all the highlights when focus changed from replace panel to editortext
                editortext.getHighlighter().removeAllHighlights();
            }

            @Override
            public void focusLost(FocusEvent e) {
                ;
            }
    });
    
   
   //Listens every cursor update in editortext
    editortext.addCaretListener(new CaretListener()
    {

            @Override
            public void caretUpdate(CaretEvent e) 
            {
                try
                {
                     int offset=editortext.getCaretPosition();
                     int line=editortext.getLineOfOffset(offset)+1;
                    
                     //Add Line no statusbar
                     statusbar.setText("Line no:"+line+"/"+editortext.getLineCount());
                    
                     // For every caret movement update undo redo buttons & menuitems   
                    updatundoredovisible();
                
                }
                catch(BadLocationException exp)
                {
                    
                }
          // Editortext was empty
                if(editortext.getText().equals(""))
                {
                   
                    cutitem.setEnabled(false);
                    finditem.setEnabled(false);
                    replaceitem.setEnabled(false);
                    copyitem.setEnabled(false);
                    deleteitem.setEnabled(false);
                    deletebtn.setEnabled(false);
                    cutbtn.setEnabled(false);
                    copybtn.setEnabled(false);
                    selectallitem.setEnabled(false);
      
            }
              //Editortext not empty
             else
                {  //if any text in editor text set enabled some items
                     selectallitem.setEnabled(true);
                     finditem.setEnabled(true);
                     replaceitem.setEnabled(true);
                     tolowercaseitem.setEnabled(false);
                     touppercaseitem.setEnabled(false);
                }
                //No Selected text in editortext
             if(editortext.getSelectedText()==null)
                { 
                 //set enabled false for items which related to selected text such copy ,cut etc
                    cutitem.setEnabled(false);
                    copyitem.setEnabled(false);
                    deleteitem.setEnabled(false);
                    deletebtn.setEnabled(false);
                    cutbtn.setEnabled(false);
                    copybtn.setEnabled(false);
                    tolowercaseitem.setEnabled(false);
                    touppercaseitem.setEnabled(false);
                    popupcutitem.setEnabled(false);
                    popuptoloweritem.setEnabled(false);
                    popuptoupperitem.setEnabled(false);
                    popupcopyitem.setEnabled(false);
                    popupdeleteitem.setEnabled(false);
                    popupconvertcasemenu.setEnabled(false);
               
                }
        
             //some texts are selected
            else
                {
                    cutitem.setEnabled(true);
                    deletebtn.setEnabled(true);
                    copyitem.setEnabled(true);
                    deleteitem.setEnabled(true);
                    cutbtn.setEnabled(true);
                    copybtn.setEnabled(true);
                    tolowercaseitem.setEnabled(true);
                    touppercaseitem.setEnabled(true);
                    popupcutitem.setEnabled(true);
                    popuptoloweritem.setEnabled(true);
                    popuptoupperitem.setEnabled(true);
                    popupcopyitem.setEnabled(true);
                    popupdeleteitem.setEnabled(true);
                    popupconvertcasemenu.setEnabled(true);
             }
               
          }

           
     });
      
    //Add listeners to all the menuitems and toolbar Buttons
    
    newitem.addActionListener(new ActionListener()
    {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                newlistener();
                
            }
     });
      
      newbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
              newlistener();
            }
      });
      
      openitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                openlistener();
           }
       });
      
      openbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                 openlistener();
            }
      });
      
      saveitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                  savelistener();
            }
   });
      
      
      savebtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
               savelistener();
            }   
      });
      
      
    saveasitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                  saveaslistener();
            }
      });
      
      setreadonlyitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(setreadonlyitem.isSelected())
                    editortext.setEditable(false);
                else
                    editortext.setEditable(true);
            }
      });
      
      
      exititem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                  System.exit(0);
            }
      
      });
   
       
       //Undo & redo operations
      
       editortext.getDocument().addUndoableEditListener(new UndoableEditListener(){

            @Override
            public void undoableEditHappened(UndoableEditEvent e) 
            {
               undomanager.addEdit(e.getEdit());
            }
   });
      
        undobtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    undomanager.undo();
                }
                catch(Exception exp)
                {
                    
                }
                //update undo & redo items visibile
            updatundoredovisible();
            }

     });
    
        
       redobtn.addActionListener(new ActionListener()
       {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try
                {
                    undomanager.redo();
                }
                catch(Exception exp)
                {
                    
                }
                //update undo & redo items visibile
                 updatundoredovisible();
            }
    });  
       
       
       undoitem.addActionListener(new ActionListener()
       {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try
                {
                    undomanager.undo();
                   
                }
                catch(Exception exp)
                {
                    
                }
        //update undo & redo items visibile
                updatundoredovisible();
            
            }

      });
     
       redoitem.addActionListener(new ActionListener()
       {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                try
                {
                    undomanager.redo();
                }
                catch(Exception exp)
                {
                    
                }
                  //update undo & redo items visibile       
                updatundoredovisible();
            }
     
     });
       
         cutitem.addActionListener(new ActionListener()
         {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
         //Cut the selected text
          editortext.cut();
          
            }
          
      });
      
       cutbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
         //Cut the selected text
             editortext.cut();
          
            }
         });
       
       
       pasteitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
         //paste the  text at caret position  from clipboard.
                editortext.paste();
            }
      
      });
       
      pastebtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                 //paste the  text at caret position  from clipboard.
                editortext.paste();
            }
      
      });
      
      popupcopyitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                 //Copy the selectedtext to clipboard
              editortext.copy();
            }
     });
      
      
      copyitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
               //Copy the selectedtext to clipboard
                editortext.copy();
            }
     });
     
      copybtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
      //Copy the selectedtext to clipboard
             editortext.copy();
            }
     });
      
     
   
      deleteitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                editortext.cut();
                //After cut the string from editortext set Clipboard content to " "
                StringSelection s=new StringSelection(" ");
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null);
            }
     });
       
      deletebtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
        
                editortext.cut();
                //After cut the string from editortext set Clipboard content to " "
                StringSelection s=new StringSelection(" ");
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null);
            }
     });
      
       searchbtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
              findlistener();
         
            }
       
       });
      selectallitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                 editortext.selectAll();
            }
      });
      datetimeitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                //Get the current date
                 Date curdate=new Date();
                 String currentdate=String.valueOf(curdate);
                 editortext.insert(currentdate,editortext.getCaretPosition());
            }
      
      });
      
     copyfilenameitem.addActionListener(new ActionListener()
     {

            @Override
            public void actionPerformed(ActionEvent e) 
              {
                copyfilenamelistener();
             }
      });
     
      copyfilepathitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
            
                copyfilepathlistener();
            }
      });
   
      copydirpathitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
      
                copydirpathlistener();
            }
     
     }); 
      
       textcoloritem.addActionListener(new ActionListener()
       {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                clrchooser=new JColorChooser();
                Color selectedcolor=clrchooser.showDialog(null, "Select Text Color", Color.BLACK);
                
                
                editortext.setForeground(selectedcolor);
            }
      });
      
      tolowercaseitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                tolowercaselistener();
            }

      });
      
    
     touppercaseitem.addActionListener(new ActionListener()
     {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                touppercaselistener();
                
            }

            
      });
      
    fullscreenview.addActionListener(new ActionListener()
    {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
              
                fullscreenviewlistener();
            }
   
   });
  
   readview.addActionListener(new ActionListener()
   {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                readviewlistener();
            }
            
 });
   
       
    finditem.addActionListener(new ActionListener()
    {
            @Override
            public void actionPerformed(ActionEvent e) 
            {

                    findlistener();
            }
                

      });
    
    replaceitem.addActionListener(new ActionListener()
    {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                    replacelistener();
            }

     });
      advancedsearch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                     advancedsearchlistener();
            }

           
      
      });
      
  wordwrap.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                  if(wordwrap.isSelected())
                       editortext.setLineWrap(true);
                  else
                    editortext.setLineWrap(false);
            }
      
      });
      
      
      
      viewtoolbar.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(viewtoolbar.isSelected())
                        toolbar.setVisible(true);
                else
                    toolbar.setVisible(false);
            }
      
      });
      
      viewstatusbar.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(viewstatusbar.isSelected())
                    statusbar.setVisible(true);
                else
                    statusbar.setVisible(false);
            }
      
      });
      
     fontmenuitem.addActionListener(new ActionListener()
     {
          
        @Override
            public void actionPerformed(ActionEvent e) 
        {
        
                fontmenulistener();
              
            }
     });
      
     fontsizecombo.addItemListener(new ItemListener()
     {

            @Override
            public void itemStateChanged(ItemEvent e) 
            {
                    fontsizelistener();
              
            }
     });
       
     
    fontstylecombo.addItemListener(new ItemListener()
    {
        @Override
            public void itemStateChanged(ItemEvent e) 
            {
                    fontstylelistener();
            }

     });
       
     // Add listeners to popupmenuitems
    
      popupdeleteitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                 editortext.cut();
                 StringSelection s=new StringSelection(" ");
                 Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null);
            }
     }); 
    
     popuptoloweritem.addActionListener(new ActionListener()
     {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                     tolowercaselistener();
            }
     
     });
    
     popuptoupperitem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
               touppercaselistener();
                
            }

            
      });
    
     popupfontitem.addActionListener(new ActionListener()
     {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                 fontmenulistener();
              
            }

     });
     
     popupundoitem.addActionListener(new ActionListener()
     {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try
                {
                    undomanager.undo();
                   
                }
                catch(Exception exp)
                {
                    
                }
            updatundoredovisible();
            }

     });
    
        
      popupredoitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
               try
                {
                    undomanager.redo();
                }
                catch(Exception exp)
                {
                    
                }
                updatundoredovisible();
                 
            }
     
        });  
     
     
      popupcutitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
         
                  editortext.cut();
          
            }
          
      });
      
    
      popuppasteitem.addActionListener(new ActionListener()
      {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                 editortext.paste();
            }
      
      });
       
        
        frame.setVisible(true);
        frame.setSize(700,700);
        Dimension minframesize=new Dimension(500,600);
        frame.setMinimumSize(minframesize);
                
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
      
          try
    {
            //Install the WebLookAnd Feel from external libraries
   
       
              /*  WebLookAndFeel.install();
           WebLookAndFeel.updateAllComponentUIs();*/
       
     }
    catch(Exception e)
    {
       JOptionPane.showMessageDialog(null, "Look & feel exception");
        System.out.println("look &  fell exception");
    }
        
        
        TextPad t=  new TextPad();
      //Directly open the File Without open Application 
      if(args!=null)
      { /*get the path of the filename as args when user choose "Open with" option 
       * Create Object t for access non static variables and methods inside the static menthod
       */
          t.filename=args[0];
            //if the file was successfully opened
               if(t.openFile(t.filename))
               {
                   //set title to the frame
                   t.frame.setTitle(new File(t.filename).getName()+" -TextPad");
               }
      }
      
    }

    //This method converts the string to Object
    private Object makeobject(String item) {
        return (Object)item;
    }
    
     private void updatundoredovisible() 
     {
                undobtn.setEnabled(undomanager.canUndo());
                redobtn.setEnabled(undomanager.canRedo());
                undoitem.setEnabled(undomanager.canUndo());
                redoitem.setEnabled(undomanager.canRedo());
                popupundoitem.setEnabled(undomanager.canUndo()); 
                popupredoitem.setEnabled(undomanager.canRedo());           
      }

    private void setfontsizecombo() 
    {
        //Add font sizes to fontsizecombo
        
        fontsizecombo.addItem(makeobject("8"));
        fontsizecombo.addItem(makeobject("10"));
        fontsizecombo.addItem(makeobject("12"));
        fontsizecombo.addItem(makeobject("16"));
        fontsizecombo.addItem(makeobject("18"));
        fontsizecombo.addItem(makeobject("20"));
        fontsizecombo.addItem(makeobject("22"));
        fontsizecombo.addItem(makeobject("24"));
        fontsizecombo.addItem(makeobject("28"));
        fontsizecombo.addItem(makeobject("36"));
        fontsizecombo.addItem(makeobject("40"));
        fontsizecombo.addItem(makeobject("44"));
        fontsizecombo.addItem(makeobject("48"));
        fontsizecombo.addItem(makeobject("50"));
        fontsizecombo.addItem(makeobject("54"));
        fontsizecombo.addItem(makeobject("60"));
        fontsizecombo.addItem(makeobject("66"));
        fontsizecombo.addItem(makeobject("72"));
        
    }

    private void setfontstylecombo() {
        
        //Add items to fontstylecombo
        fontstylecombo.addItem(makeobject("BOLD"));
        fontstylecombo.addItem(makeobject("ITALIC"));
        fontstylecombo.addItem(makeobject("PLAIN"));
    }
/**
 * 
 * @param filename (fullpath) of the file to be opened
 * @return true if file successfully saved else false
 */
        private boolean openFile(String filename) {
                //if file successfully opened success is true else it is false
                boolean success;
               this.filename=filename;
                String ipline,editorstring="";
                FileReader fr;
                BufferedReader br;
                try
                {
                    fr=new FileReader(filename);
                    
                    
                    br=new BufferedReader(fr);
                    ipline=br.readLine();
                    //read the file untill last line
                    
                    while(ipline!=null)
                    {
                     //Concat previous String(editorstring) with newline(opline)
                        editorstring=editorstring+ ipline+"\n";
                        ipline=br.readLine();
                    }
                    editortext.setText(editorstring);
                    success=true;
                   
                 }
                catch(Exception e)
                {
                    
                    success=false;
                    
                }
                if(success=true)
                {
                     File f=new File(filename);
                    System.out.println("openwith:"+filename);
                    
                }
                return success;
            }
        
        
        
        
       
       /**
        * 
        * @param filename (fullpath) of the file to be saved
        * @return true if file successfully saved else false
        */
        private boolean savefile(String filename) {
                boolean success;
                String editorstring;
                FileWriter fwr;
                PrintWriter pwr;
                try
                {System.out.println("in save file");
                    fwr=new FileWriter(filename);
                    pwr=new PrintWriter(fwr);
                    editorstring=editortext.getText();
                  editortext.write(pwr);
                    pwr.close();
                    success=true;
                }
                catch(IOException e)
                
                {
                    System.out.println("file not saved");
                    success=false;
                }
                if(success==true)
                    
                {
                    System.out.println("file saved");
                    File savedfile=new File(filename);
                    String title=savedfile.getName();
                    frame.setTitle(title+" -TextPad");
                }
                return success;
            }

       private void newlistener() 
       {
           //new file with contents ,it not saved till now
                   if((filename==null)&&(editortext.getText().length()>0))
                   
                {
                 int result= JOptionPane.showConfirmDialog(null,"Do you Want to save this file before open new file?","TextPad",JOptionPane.YES_NO_CANCEL_OPTION ,JOptionPane.WARNING_MESSAGE,null);
                
                 
                         
                 switch(result)
                   {
                       case JOptionPane.YES_OPTION:
                                savelistener();
                                break;
                           
                       case JOptionPane.NO_OPTION:
                                editortext.setText("");
                                filename=null;
                                frame.setTitle("TextPad");
                                undomanager.die();
                                editortext.requestFocusInWindow();
                                break;
                           
                       case JOptionPane.CANCEL_OPTION:
                                break;
                   }
                   /*
                    * savflg represents the file was successfully saved or not. if saved savflag is 1 else savflg is 0 ,if the file was saved open 
                    * new file otherwise no change in editrotext
                    
                    */
                     if(savflg==1)
                     { editortext.setText("");
                            filename=null;
                            frame.setTitle("TextPad");
                            undomanager.die();
                           editortext.requestFocusInWindow();
                         savflg=0;
                     }
              
                   
                }
                   //already saved file
                else if(filename!=null)
                {
                   int result= JOptionPane.showConfirmDialog(null,"Do you Want to save Chaneges?","TextPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null );
                   
                   switch(result)
                   {
                       case JOptionPane.YES_OPTION:
                          //save the file 
                             if(savefile(filename))
                             {
                                editortext.setText("");
                                filename=null;
                                frame.setTitle("TextPad");
                                undomanager.die();
                                editortext.requestFocusInWindow();
                           }
                                 break;
                       
                       case JOptionPane.NO_OPTION:
                                 editortext.setText("");
                                 filename=null;
                                 frame.setTitle("TextPad");
                                 editortext.requestFocusInWindow();
                                 undomanager.die();
                                 break;
                       
                       case JOptionPane.CANCEL_OPTION:
                                 break;
                   }
              }
               //A new file with no content and not have filename
                else if((filename==null)&&(editortext.getText().length()==0))
                        {
                             editortext.setText("");
                             filename=null;
                             frame.setTitle("TextPad");
                             editortext.requestFocusInWindow();
                             undomanager.die();
                        }
                //not suitable all the above cases
                else
                    ;
                 //when open HIdes some sub menus
             cutitem.setEnabled(false);
             copyitem.setEnabled(false);
             deleteitem.setEnabled(false);
             cutbtn.setEnabled(false);
             copybtn.setEnabled(false);
   
       }             

       private void openlistener()
       {
                
                //flag is used for after save or not save the file view open dialog box
                int status,flag=0;
       JFileChooser filechoose=new JFileChooser();
            if((filename==null)&&(editortext.getText().length()>0))
       {
                    
        int result= JOptionPane.showConfirmDialog(null,"Do you Want to save this file before open new file?","TextPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
                     switch(result)
                        {
                       case JOptionPane.YES_OPTION:
                                savelistener();
                                if(savflg==1)
                                {
                                   
                                    // file saved succssfully
                                     flag=1;
                                     
                                     savflg=0;
                             }
                                break;
                       case JOptionPane.NO_OPTION:
                                flag=1;
                                editortext.requestFocusInWindow();
                                break;
                       case JOptionPane.CANCEL_OPTION:
                                break;
                   }
                     
                    if(savflg==1)
                     { 
                            editortext.setText("");
                            filename=null;
                            frame.setTitle("TextPad");
                            undomanager.die();
                            editortext.requestFocusInWindow();
                            savflg=0;
                     }
        }
          else if((filename==null)&&(editortext.getText().length()==0))      
        {
            
           filechoose.setFileSelectionMode(JFileChooser.FILES_ONLY);

            status=filechoose.showOpenDialog(frame);
           
            
            
            if(status==JFileChooser.APPROVE_OPTION)
                {
                    selectedfile=filechoose.getSelectedFile();
                    
                    if(!selectedfile.isDirectory()){
                        filename=selectedfile.getPath();
                    if (!openFile(filename))
                    {
                        JOptionPane.showMessageDialog(null,"Error reading " +filename, "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        File openedfilename=new File(filename);
                      //get opened file name for set title of frame
                        title=openedfilename.getName();
                        frame.setTitle(title+" -TextPad");
             
                    }
                    }
                }
        }       
                else
                {
                     int result= JOptionPane.showConfirmDialog(null,"Do you Want to save Chaneges?","TextPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null );
                      switch(result)
                       {
                           case JOptionPane.YES_OPTION:
                                  if( savefile(filename))
                                  flag=1;
                                  break;
                            case JOptionPane.NO_OPTION:
                                  flag=1;
                                  break;
                       case JOptionPane.CANCEL_OPTION:
                                   break;
                    }
                }    
                if(flag==1)
                {
                    
                   
                    filechoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    status=filechoose.showOpenDialog(frame);
                    if(status==JFileChooser.APPROVE_OPTION)
                    {
                        
                        selectedfile=filechoose.getSelectedFile();
                        
                        if(!selectedfile.isDirectory())
                        {
                            filename=selectedfile.getPath();
                        if (!openFile(filename))
                        {
                            JOptionPane.showMessageDialog(null,  "Error reading " +filename, "Error",JOptionPane.ERROR_MESSAGE);
                        }
             
                        else
                        {
                            File openedfilename=new File(filename);
                            //Get opened file name for set title of the frame
                            title=openedfilename.getName();
                            frame.setTitle(title+" -TextPad");
                            //delete all undo redo history whem open another file
                            undomanager.die();
                        }
                        }
                  }
              }
            else
                 ;            
                
            }
            
          private void savelistener() 
          {
                 int status,flag=1;
                   //first time save the file
             
                if(filename==null)
                {
                        System.out.println("filname null");
                        JFileChooser filechoose=new JFileChooser();
                        status=filechoose.showSaveDialog(frame);
                
                        if(status==JFileChooser.APPROVE_OPTION)
                        {
                            selectedfile=filechoose.getSelectedFile();
                            
                            //get full path of the selected file
                            filename=selectedfile.getPath();
                           
                            //Get parent file of the selected file
                            parentfile=new File(selectedfile.getParent());
                           
                            //List parentfile's Files And subdirectories
                            dirfiles=parentfile.listFiles();
                
                            for(int i=0;i<dirfiles.length;i++)
                            { 
                                if(filename.equals(dirfiles[i].getPath()))
                                {   
                                    System.out.println("file exist");
                                    flag=0;
                                }
                            }
                            System.out.println("Flag:"+flag) ;
                        }
                    else
                        System.out.println("not approve button") ;
                
                // overwrite the old file
                if(flag==0)
                {
                   File existfile=new File(filename);
                   //get already exist's filename
                   String existfilename=existfile.getName();
                   
                   int result= JOptionPane.showConfirmDialog(null,existfilename+" already exists \n Do you Want to Overwrite Existing file?","TextPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
                   switch(result)
                   {
                       case JOptionPane.YES_OPTION:
                            if(savefile(filename))
                            {
                                savflg=1;
                                closesavflg=1;
                                System.out.println("old file saved");
                           }
                           break;
                       case JOptionPane.NO_OPTION:
                            System.out.println("cancel");
                            filename=null ;
                           break;
                       case JOptionPane.CANCEL_OPTION:
                           break;
                          
                 }
               }
               
                //Save new file
                
                else 
                {
                    if(filename!=null)
                    {
                        if(!savefile(filename))
                    { 
                        System.out.println("not suitable");
                        JOptionPane.showMessageDialog(null,"Can't Save"+filename,"Error",JOptionPane.ERROR_MESSAGE);
                        
                    }
                    else 
                        {
                            savflg=1;
                            closesavflg=1;
                            System.out.println("new file saved");
                        }
                     }
                }
           }
             
                if(filename!=null)
                {
                    System.out.println("filname  not null");
                    if(! savefile(filename))
                         JOptionPane.showMessageDialog(null,"Can't Save"+filename,"Error",JOptionPane.ERROR_MESSAGE);
                   else
                   {
                        savflg=1;
                        closesavflg=1;
                   }
                       
                }
                
             
            }
            private void saveaslistener() 
            {
                    int status,flag=1;
                    JFileChooser filechoose=new JFileChooser();
                    status=filechoose.showSaveDialog(frame);
                   if(status==JFileChooser.APPROVE_OPTION)
                    {
                    selectedfile=filechoose.getSelectedFile();
                    filename=selectedfile.getPath();
                    parentfile=new File(selectedfile.getParent());
                    dirfiles=parentfile.listFiles();
                    for(int i=0;i<dirfiles.length;i++)
                 {
                        if(filename.equals(dirfiles[i].getPath()))
                {
                       flag=0;
                }
            }
           if(flag==0)
               {
                    File existfile=new File(filename);
                    String existfilename=existfile.getName();
                    int result= JOptionPane.showConfirmDialog(null,existfilename+" already Exists \n Do you Want to Overwrite Existing file?","TextPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
                    switch(result)
                   {
                       case JOptionPane.YES_OPTION:
                           if(savefile(filename))
                                System.out.println("old file saved");
                           break;
                       case JOptionPane.NO_OPTION:
                            System.out.println("cancel");
                          break;
                       case JOptionPane.CANCEL_OPTION:
                           break;
                   }
               }
             else 
                {
                    if(!savefile(filename))
                    {
                        JOptionPane.showMessageDialog(null,"Can't Save"+filename,"Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else 
                        System.out.println("new file saved");
                }
             }
                else 
                    ;
            }
            
          private void copyfilenamelistener() 
          {
                try 
                {
                    if(filename!=null)
                    {
                        File file=new File(filename);
                        StringSelection Filename=new StringSelection(file.getName());
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(Filename, null);
                  }
                   else
                   {
                       StringSelection nullstr=new StringSelection("null");
                       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(nullstr, null);
                   }
                                          
                } 
                catch (Exception ex) 
                {
                    Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
                }
        
            }
        private void copyfilepathlistener() {
                   try 
                   {
                        if(filename!=null)
                   {
                       StringSelection filepath=new StringSelection(filename);
                       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filepath, null);
                   }
                 else
                   {
                       StringSelection nullstr=new StringSelection("null");
                       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(nullstr, null);
                   }  
                  //  editortext.insert(filename,editortext.getCaretPosition());
                   
                } 
                catch (Exception ex) 
                {
                    Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }       
        private void copydirpathlistener() 
        {
                   try
                {
          
                 if(filename!=null)
                 {
                     File file=new File(filename);
                     File ParentFile=file.getParentFile();
                     StringSelection dirpath=new StringSelection(ParentFile.getAbsolutePath());
                     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(dirpath, null);
          
              }
                else
                   {
                       StringSelection nullstr=new StringSelection("null");
                       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(nullstr, null);
                   }
             }
            catch(Exception excp)
            {
          
            }
       }

            private void tolowercaselistener() 
            {
                String lowerstr= editortext.getSelectedText().toLowerCase();
                int startpt=editortext.getSelectionStart();
                int end=editortext.getSelectionEnd();
                System.out.println(startpt+" "+end+" "+lowerstr);
                System.out.println(editortext.getText());
                editortext.replaceRange(lowerstr, startpt, end);
                System.out.println(editortext.getText());
      
             
            }
        private void touppercaselistener() 
        {
                String upperstr= editortext.getSelectedText().toUpperCase();
                int startpt=editortext.getSelectionStart();
                int end=editortext.getSelectionEnd();
                System.out.println(startpt+" "+end+" "+upperstr);
                System.out.println(editortext.getText());
                editortext.replaceRange(upperstr, startpt, end);
                System.out.println(editortext.getText());
        }

        private void fullscreenviewlistener() 
        {
                   KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        //When escape key pressed the event occured
           Action escapeAction = new AbstractAction() 
           {
            public void actionPerformed(ActionEvent e) 
            {
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null); 
                menubar.setVisible(true);
                 viewtoolbar.setState(true);
                toolbar.setVisible(true);
                viewstatusbar.setState(true);
                statusbar.setVisible(true);
                fullscreenview.setState(false);
                frame.setResizable(true);
            }
            };
      frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);     
    
        //Check fullscreen is supported or not
        if(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported())
        {
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
                menubar.setVisible(false);
                viewtoolbar.setState(false);
                toolbar.setVisible(false);
                viewstatusbar.setState(false);
                statusbar.setVisible(false);
                frame.setResizable(false);
          }
        }
        
        private void gotolinelistener()
        {
           //Input verifier for check the gotoline textfield 
           InputVerifier linenoisint=null;
           linenoisint=new InputVerifier(){
            
            @Override
            public boolean verify(JComponent input) {
              boolean isint;
                try
              {
                //if the gotoline textfield's value can converted to Int set isint to true; 
                lineno=Integer.parseInt(gotoline.getText());
                isint=true;
                   
              }
                catch(NumberFormatException ex)
                { 
                    //the gotoline's value can't converted to int so set isint to false; 
                    isint=false;
                    JOptionPane.showMessageDialog(null, "Line no must be integer");
                }
                return isint;
            }
                       
                   };
          //set inputverifier to gotoline
           gotoline.setInputVerifier(linenoisint);
        try 
        {
       //move focus to editortext  
       editortext.requestFocusInWindow();
       //set caretposition to starting of the line given in gotoline
       editortext.setCaretPosition(editortext.getLineStartOffset(lineno-1));
            
       } 
        catch (BadLocationException ex) 
        {
            //Line not found
            JOptionPane.showMessageDialog(null, "There is no Line numbered "+" '"+lineno+"' "+ "in this doucment");
        }
       
   }

    private void readviewlistener() 
    {
                 try
               {
                    if(readview.isSelected())
                    {
                        editortext.setEditable(false);
                        toolbar.setVisible(false);
                        viewtoolbar.setState(false);
                        statusbar.setVisible(false); 
                        viewstatusbar.setState(false);
                  
                    }
                    else
                    {
                         editortext.setEditable(true);
                        toolbar.setVisible(true);
                        viewtoolbar.setState(true);
                        statusbar.setVisible(true); 
                        viewstatusbar.setState(true);
                    }
              }
               catch(Exception exp)
               {
                   
               }
            }
         
  
        private void findlistener() 
             {
                  System.out.println("find ");
                 JPanel findpanel=new JPanel()    ;
                 final JTextField fnd=new JTextField(20);
                 JButton findbtn=new JButton("Find");
                 findpanel.add(new JLabel("Find: "));
                 findpanel.add(fnd);
                 findpanel.add(findbtn);
                 
                 text=editortext.getText();                   
                 editortext.setCaretPosition(0);
                 System.out.println("find and ");
                 findbtn.addActionListener(new ActionListener()
                 {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                    System.out.println("find Button");
                    srchstr=fnd.getText();
                    System.out.println("caret:"+editortext.getCaretPosition());
                    System.out.println("last:"+text.lastIndexOf(srchstr));
                   
                    if(srchstr!=null)
                    {
                         System.out.println("srch"+srchstr);
                         end=editortext.getCaretPosition();
                         int index=text.indexOf(srchstr,end);
                        System.out.println("index:"+index);
                         if(index>=0 )
                         {
                                try {
                                    end=index+srchstr.length();
                                    System.out.println("start:"+index+"end:"+end);
                                    editortext.getHighlighter().removeAllHighlights();
                                    editortext.getHighlighter().addHighlight(index, end, panit);
                                    editortext.setCaretPosition(end);
                                  
                                } catch (BadLocationException ex)
                                {
                                    Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
                                }
                         }
                         //when search string not found index is -1
                         if(index==-1)
                            
                         {
                             JOptionPane.showMessageDialog(null,"Can't find "+ "\t" +srchstr);  
                           editortext.setCaretPosition(0);
                         }
                         }
                    }

                    
                   
                });
        int result=JOptionPane.showConfirmDialog(null,findpanel,"Find",JOptionPane.PLAIN_MESSAGE);   
        
             
             }
        
        private void replacelistener()
        {
                
                JPanel replacepanel=new JPanel();
                System.out.println("find and replace");
                final JTextField srch=new JTextField(20);
                final JTextField rplce=new JTextField(20);
                fndnxtbtn=new JButton("Find Next");
                rplcebtn=new JButton("Replace");
                replceallbtn=new JButton("Replace All");
                JLabel fnd=new JLabel("Find What");
                JLabel replace=new JLabel("Replace");
               
                //Add elements to replace panel
                replacepanel.add(fnd);
                replacepanel.add(srch);
                replacepanel.add(replace);
                replacepanel.add(rplce);
                replacepanel.add(fndnxtbtn);;
                replacepanel.add(rplcebtn);;
                replacepanel.add(replceallbtn);
                editortext.setCaretPosition(0);
                text=editortext.getText();
                fndnxtbtn.setEnabled(false);
                rplcebtn.setEnabled(false);
                replceallbtn.setEnabled(false);
             
             srch.addCaretListener(new CaretListener()
             {
                 @Override
                    public void caretUpdate(CaretEvent e) 
                 {
                            editortext.setCaretPosition(0);
                                if(srch.getText()!=null)
                                   fndnxtbtn.setEnabled(true);
                  }
                        
               });
             rplce.addCaretListener(new CaretListener()
             {
                  @Override
                    public void caretUpdate(CaretEvent e)
                  {
                     if(rplce.getText()!=null)
                              {
                                   rplcebtn.setEnabled(true);
                                   replceallbtn.setEnabled(true);
                               }
                    }
              });
                        
             
             fndnxtbtn.addActionListener(new ActionListener()
             {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                   
                        System.out.println("find Button");
                        text=editortext.getText();
                        srchstr=srch.getText();
                        System.out.println("caret:"+editortext.getCaretPosition());
                        System.out.println("last:"+text.lastIndexOf(srchstr));
                    
                        if(srchstr!=null)
                        {
                        
                             System.out.println("srch"+srchstr);
                            end =editortext.getCaretPosition();
                            int index=text.indexOf(srchstr,end);
                            System.out.println("index:"+index);
                            if(index>=0 )
                                {
                                    try 
                                    {
                                        end=index+srchstr.length();
                                        System.out.println("start:"+index+"end:"+end);
                                        editortext.getHighlighter().removeAllHighlights();
                                        if(editortext.getHighlighter()==null)
                                            System.out.println("remove highlight");
                                               
                                        editortext.getHighlighter().addHighlight(index, end, panit);
                                    
                                        editortext.setCaretPosition(end);
                                  
                                    }
                                    catch (BadLocationException ex) 
                                    {
                                        System.out.println("excep");
                                        Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                         if(index==-1)
                            {
                                 JOptionPane.showMessageDialog(null,"Can't find "+ "\t" +srchstr);  
                                editortext.setCaretPosition(0);
                         }
                    }

                        
                       } 
        });
             
            rplcebtn.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                             System.out.println(" replace button");
                            srchstr=srch.getText();
                     
                            if(srchstr!=null)
                        {
                             replacestr=rplce.getText();
                            System.out.println("srch"+srchstr+"rple:"+replacestr);
                            int index=text.indexOf(srchstr,editortext.getCaretPosition());
                            System.out.println("caret:"+editortext.getCaretPosition());
                            if(index>=0 && replacestr.length()>0 )
                            {
                                try 
                                {
                                    end=index+srchstr.length();
                                    System.out.println("start:"+index+"end:"+end);
                                    editortext.getHighlighter().removeAllHighlights();
                                    editortext.getHighlighter().addHighlight(index, end, panit);
                                    editortext.replaceRange(replacestr, index, end);
                                    pos=editortext.getCaretPosition();
                                     System.out.println(editortext.getText());
                                   System.out.println("pos:"+pos);
                                   editortext.setCaretPosition(pos);
                               text=editortext.getText();
                                } 
                                catch (BadLocationException ex) 
                                {
                                    Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
                                }
                         }
                         if(index==-1)
                           
                         {
                            JOptionPane.showMessageDialog(null,"Can't find "+ "\t" +srchstr);  
                            editortext.setCaretPosition(0);
                         }
                    }
                   
                 }
      });
          
            replceallbtn.addActionListener(new ActionListener()
            {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        System.out.println(" replace all button");
                          text=editortext.getText();
                         srchstr=srch.getText();
                       replacestr=rplce.getText();
                         System.out.println("srch"+srchstr+"rple:"+replacestr);
                         int index=text.indexOf(srchstr);
                         while(index>=0 && replacestr.length()>0 )
                         {
                            try {
                                end=index+srchstr.length();
                                System.out.println("start:"+index+"end:"+end);
                                editortext.getHighlighter().removeAllHighlights(); 
                                editortext.getHighlighter().addHighlight(index, end, panit);
                                editortext.replaceRange(replacestr, index, end);
                                System.out.println(editortext.getText());
                                text=editortext.getText();
                                index=text.indexOf(srchstr,editortext.getCaretPosition());
                                System.out.println("index:"+index);
                           if(index==-1)
                                {
                               editortext.setCaretPosition(0);
                               JOptionPane.showMessageDialog(null,"Can't find "+ "\t" +srchstr);  
                                }
                            }   
                            catch (BadLocationException ex) 
                            {
                                Logger.getLogger(TextPad.class.getName()).log(Level.SEVERE, null, ex);
                            }
                         }
                    }
            
         });
            int result=JOptionPane.showConfirmDialog(null,replacepanel,"Find & Replace",JOptionPane.PLAIN_MESSAGE);
           
        }
        
     private void advancedsearchlistener() 
     
     {
     JPanel advancedsrchpanel=new JPanel();
     JLabel pathlabel=new JLabel("Select Folder");
     JLabel keylabel=new JLabel("what find");
     JTextField srchpath=new JTextField(20);
     JTextField srchkey=new JTextField(20);
     JButton search=new JButton("Search");
     JButton cancel=new JButton("Cancel");
    advancedsrchpanel.add(pathlabel);
    advancedsrchpanel.add(search); 
    advancedsrchpanel.add(srchpath);
     advancedsrchpanel.add(keylabel);
     advancedsrchpanel.add(srchkey);
    advancedsrchpanel.add(cancel);
     
            }
        private void fontmenulistener() 
        {
                 JPanel fontpanel=new JPanel(new BorderLayout());
                JScrollPane scrollpane=new JScrollPane();
                scrollpane.getViewport().add(fontlist);
                sampletxt=new JLabel("Sample Text");
            // sampletxt.sets
                sampletxt.setPreferredSize(new Dimension(500, 70));
                fontpanel.add(scrollpane,BorderLayout.NORTH);
                 JPanel smpletxtpanel=new JPanel();
                smpletxtpanel.add(sampletxt);
                smpletxtpanel.setSize(5, 15);
   //fontpanel.add(smpletxtpanel);
                fontpanel.add(smpletxtpanel, BorderLayout.SOUTH);
   
        fontlist.addListSelectionListener(new ListSelectionListener()
        {
            @Override
                    public void valueChanged(ListSelectionEvent e) 
            {
                        if(!e.getValueIsAdjusting())
                        {
                            fontname=(String)fontlist.getSelectedValue();
                             String   selectedfont=String.valueOf(fontstylecombo.getSelectedItem());
                 if(selectedfont.equals("BOLD"))
                        fontstyle=Font.BOLD;
                if(selectedfont.equals("ITALIC"))
                        fontstyle=Font.ITALIC;
                if(selectedfont.equals("PLAIN"))
                        fontstyle=Font.PLAIN;
                Font f=editortext.getFont();
                fontsize=Integer.parseInt(String.valueOf(f.getSize()));
                 sampletxt.setFont(new Font(fontname,fontstyle,fontsize));
                       }
                }
        
        });
        
       int result= JOptionPane.showConfirmDialog(null,fontpanel,"Select Font",JOptionPane.OK_CANCEL_OPTION);
       switch(result)
       {
           case JOptionPane.OK_OPTION:
               editortext.setFont(new Font(fontname,fontstyle,fontsize));
                break;
           case JOptionPane.CANCEL_OPTION:
               break;
       }
         
            }
            
       private void fontsizelistener() 
       {
                  if(fontlist.isSelectionEmpty())
                {
                    System.out.println("no thing selected");
                    fontname="TimesNewRoman" ;
                }
               else
                {
                    System.out.println(" selected");
                    fontname=String.valueOf(fontlist.getSelectedValue());
                 }
          
            String   selectedfont=String.valueOf(fontstylecombo.getSelectedItem());
            if(selectedfont.equals("BOLD"))
                    fontstyle=Font.BOLD;
            if(selectedfont.equals("ITALIC"))
                    fontstyle=Font.ITALIC;
            if(selectedfont.equals("PLAIN"))
                    fontstyle=Font.PLAIN;
            fontsize=Integer.parseInt(String.valueOf(fontsizecombo.getSelectedItem()));
            editortext.setFont(new Font(fontname,fontstyle,fontsize));
          
          
            }
        private void fontstylelistener() 
        {
            if(fontlist.isSelectionEmpty())
                { //Default font is TIMESNEWROMAN
                    fontname="TimesNewRoman"    ;
                    System.out.println("no thing selected");
                }
                else
                {
                    fontname=String.valueOf(fontlist.getSelectedValue());
                    System.out.println("font selected");
                }
                
                String   selectedfont=String.valueOf(fontstylecombo.getSelectedItem());
                if(selectedfont.equals("BOLD"))
                        fontstyle=Font.BOLD;
                 if(selectedfont.equals("ITALIC"))
                        fontstyle=Font.ITALIC;
                if(selectedfont.equals("PLAIN"))
                        fontstyle=Font.PLAIN;
          Font f=editortext.getFont();
                fontsize=Integer.parseInt(String.valueOf(f.getSize()));
                editortext.setFont(new Font(fontname,fontstyle,fontsize));
           }

        private void saveBeforeExit() 
        {
                if(filename==null)
                {
                int result=JOptionPane.showConfirmDialog(null,"Do you want to save this file before exit","WARNING",JOptionPane.YES_NO_CANCEL_OPTION);
                switch(result)
                {
                    case JOptionPane.YES_OPTION:
                        savelistener();
                        if(closesavflg==1)
                           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        break;
                    case JOptionPane.NO_OPTION:
                         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                }
           }
                else
                {
                  int result=JOptionPane.showConfirmDialog(null,"Do you want to save changes to"+filename+"?","WARNING",JOptionPane.YES_NO_CANCEL_OPTION);
                switch(result)
                {
                    case JOptionPane.YES_OPTION:
                        if(savefile(filename))
                           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        break;
                    case JOptionPane.NO_OPTION:
                         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                }
                }
                
            }

}
