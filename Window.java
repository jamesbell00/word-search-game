import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.GeneralPath;


public class Window extends JFrame {
	// Game
	private Model.Game game;
	
	// Controller
	private Controller controller;

	// Buttons
	private JButton newGameButton = new JButton( "Start New Game" ); 
	private JButton quitButton = new JButton( "Quit" );
	private JButton playAgainButton = new JButton( "Play Again" ); 
	private JButton nextLevelButton = new JButton( "Next Level >>" ); 
	
	// Main Panels
	private JPanel content = new JPanel();
	private JPanel mainMenu;
	private JPanel gamePanel;
	private JPanel tablePanel;
	private JPanel nextLevelPanel = new JPanel();
	private JPanel endScreen;
	private Component gameControlsBox = Box.createRigidArea( new Dimension( 100, 85 ) );
	private JPanel answerPanel = new JPanel();
	
	// Text Field
	private JTextField answerField = new JTextField( );
	
	// Labels
	private JLabel timerLabel = new JLabel();
	private JLabel scoreLabel = new JLabel();
	private JLabel nextLevelLabel = new JLabel( "Well Played!" );
	
	// Borders
	private EmptyBorder tableMargin;
	private EmptyBorder charPanelBorder;
	
	private Border tableBorder = BorderFactory.createLineBorder( new Color( 56, 62, 66 ) );

	// Variables
	private int tableBorderHeight;
	private int charPanelSize;

	
	public Window( Model.Game game ) {
		super( "Final Project" );
		
		this.game = game;
		setLayout( new FlowLayout() );
		
		// Construct Main Menu Layout
		mainMenu = new JPanel();
		mainMenu.setLayout( new BoxLayout( mainMenu, BoxLayout.Y_AXIS) );
		mainMenu.setAlignmentX(CENTER_ALIGNMENT);
		JPanel logoPanel = new JPanel();
		logoPanel.setBorder( new EmptyBorder( 20, 20, 20, 20 ) );
		ImageIcon gameLogo = new ImageIcon( "gameLogo.jpg" );
		JLabel logoLabel = new JLabel();
		logoLabel.setIcon( gameLogo );
		logoLabel.setHorizontalTextPosition( SwingConstants.CENTER );
		logoLabel.setVerticalTextPosition( SwingConstants.BOTTOM );
		logoPanel.add( logoLabel );
		mainMenu.add( logoPanel );
		JPanel buttonPanel = new JPanel();
		mainMenu.add( buttonPanel, CENTER_ALIGNMENT );
		buttonPanel.setLayout( new GridLayout( 2, 1, 15, 15 ) );
		buttonPanel.setMaximumSize(new Dimension(300, 200));
		buttonPanel.add( newGameButton, CENTER_ALIGNMENT );
		buttonPanel.add( quitButton, CENTER_ALIGNMENT );
		newGameButton.setBackground(Color.BLACK);
		newGameButton.setForeground(Color.BLACK);
		newGameButton.setFocusPainted(false);
		newGameButton.setPreferredSize( new Dimension(300, 60) );
		quitButton.setBackground(Color.BLACK);
		quitButton.setForeground(Color.BLACK);
		quitButton.setFocusPainted(false);
		JPanel highScorePanel = new JPanel();
		highScorePanel.setLayout( new GridLayout( 1, 1, 0, 0 ) );
		JLabel highScoreLabel = new JLabel( "Highscore: " + game.getHighScore() );
		highScorePanel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
		highScoreLabel.setHorizontalAlignment( JLabel.RIGHT );
		highScorePanel.add( highScoreLabel );
		mainMenu.add( highScorePanel );
		
		content.add( mainMenu );

		add( content );

	}
	public void addController( Controller mainController ) {
		controller = mainController;
		
		// Initialize event handlers
	    Controller.NewGameButtonHandler newGameButtonHandler = controller.new NewGameButtonHandler();
		newGameButton.addActionListener( newGameButtonHandler );
		playAgainButton.addActionListener( newGameButtonHandler );

	    Controller.QuitButtonHandler quitButtonHandler = controller.new QuitButtonHandler();
		quitButton.addActionListener( quitButtonHandler );
		
	    Controller.NextLevelButtonHandler nextLevelButtonHandler = controller.new NextLevelButtonHandler();
		nextLevelButton.addActionListener( nextLevelButtonHandler );
				
	    Controller.TextFieldHandler textFieldHandler = controller.new TextFieldHandler();
		answerField.addActionListener( textFieldHandler );
		

	}
	public void loadGame( Model.Table tableModel ) {
		clear();
		
		// Construct Table Panel
		tablePanel = new JPanel();
		int tableSize = tableModel.getSize();
		GridLayout tableLayout = new GridLayout( tableSize, tableSize, 0, 0 );
		tablePanel.setLayout( tableLayout );
		tablePanel.setBorder( tableBorder );
		char rows[][] = tableModel.getRows();
		
		int marginSize = 20;
		if ( game.getLevel() == 1 ) {
			marginSize = 50;
			charPanelSize = 65;
			tableBorderHeight = 40;
			tableMargin = new EmptyBorder( tableBorderHeight, 0, 0, 0 );
		} else if ( game.getLevel() == 2 ) {
			marginSize = 50;
			charPanelSize = 45;
			tableMargin = new EmptyBorder( tableBorderHeight, 0, 0, 0 );
		} else {
			charPanelSize = 45;
			tableBorderHeight = 0;
			tableMargin = new EmptyBorder( tableBorderHeight, 0, 0, 0 );
		}
		
		//List<Character> chars = new ArrayList<>();
		for ( int i = 0; i < tableSize; i++ ) {
			for ( int j = 0; j < tableSize; j++ ) {
				//chars.add(rows[i][j]);
				JPanel charPanel = new JPanel();
				charPanel.setLayout( new GridBagLayout() );
				charPanel.setPreferredSize( new Dimension( charPanelSize, charPanelSize ) );
				charPanel.setBackground( new Color( 255, 255, 255 ) );
				String currentChar = Character.toString( rows[i][j] );
				JLabel charLabel = new JLabel( currentChar.toUpperCase() );
				charPanel.add( charLabel );
				tablePanel.add( charPanel );
			}
		}


		// Construct Game Menu Layout
		JPanel gamePanel = new JPanel();
		gamePanel.setBorder( tableMargin );
		JPanel gameControls = new JPanel();
		gameControls.setLayout( new BoxLayout( gameControls, BoxLayout.	Y_AXIS ) );
		gameControls.setAlignmentX( CENTER_ALIGNMENT );
		gameControls.setPreferredSize( new Dimension( 200, 250 ));
		JPanel levelLabelPanel = new JPanel();
		levelLabelPanel.setLayout( new BoxLayout( levelLabelPanel, BoxLayout.Y_AXIS ) );
		levelLabelPanel.setAlignmentX( CENTER_ALIGNMENT );
		JLabel levelLabel = new JLabel( "<html><h1>Level " + game.getLevel() + "</h1></html>" );
		levelLabel.setHorizontalAlignment( SwingConstants.CENTER );
		JLabel categoryLabel = new JLabel( "<html><h3>" + game.getCategory() + "</h3></html>" );
		categoryLabel.setHorizontalAlignment( SwingConstants.CENTER );
		nextLevelButton.setBackground( new Color( 56, 62, 66 ) );
		nextLevelButton.setForeground(Color.BLACK);
		nextLevelButton.setFont(nextLevelButton.getFont().deriveFont(Font.BOLD, 14f));
		nextLevelButton.setFocusPainted(false);
		nextLevelButton.setPreferredSize( new Dimension(180, 25) );
		gameControlsBox.setVisible( true );
		nextLevelPanel.add( nextLevelLabel );
		nextLevelPanel.add( nextLevelButton );
		nextLevelPanel.setVisible( false );
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout( new GridLayout( 1, 2, 15, 15 ) );
		
		scoreLabel.setHorizontalAlignment( SwingConstants.RIGHT );
		
		answerField.setPreferredSize( new Dimension ( 180, 25 ));
		answerField.setEditable( true );
		answerPanel.add( answerField );
		
		updateTimer();
		updateScore();

		bottomPanel.add( timerLabel );
		bottomPanel.add( scoreLabel );
		
		levelLabelPanel.add( levelLabel);
		levelLabelPanel.add( categoryLabel );

		gameControls.add( levelLabelPanel );
		gameControls.add( answerPanel );
		gameControls.add( Box.createRigidArea( new Dimension( 90, 25 ) ) );
		gameControls.add( gameControlsBox );
		gameControls.add( nextLevelPanel );
		gameControls.add( bottomPanel );
		
		gamePanel.add( Box.createRigidArea( new Dimension( marginSize, marginSize ) ) );
		gamePanel.add( tablePanel );
		gamePanel.add( Box.createRigidArea( new Dimension( marginSize+5, marginSize+5 ) ) );
		gamePanel.add( gameControls );
		gamePanel.add( Box.createRigidArea( new Dimension( marginSize, marginSize ) ) );
				
		content.add( gamePanel );
	}
	public void loadEndScreen( boolean win ) {
		clear();
		
		// Construct End Screen Layout
		endScreen = new JPanel();
		endScreen.setLayout( new BoxLayout( endScreen, BoxLayout.Y_AXIS) );
		endScreen.setAlignmentX( CENTER_ALIGNMENT );
		JPanel endgamePanel = new JPanel();
		JPanel endgameLabelPanel = new JPanel();
		endgameLabelPanel.setLayout( new BoxLayout( endgameLabelPanel, BoxLayout.Y_AXIS ) );
		endgameLabelPanel.setAlignmentX( CENTER_ALIGNMENT );
		endgamePanel.setLayout( new BoxLayout( endgamePanel, BoxLayout.Y_AXIS) );
		endgamePanel.setBorder( new EmptyBorder( 20, 20, 20, 20 ) );
		JLabel endgameLabel = new JLabel();
		if ( win ) {
			endgameLabel.setText( "<html><h1 style=\"font-size:32px;\">YOU WIN!</h1></html>" );
		} else {
			endgameLabel.setText( "<html><h1 style=\"font-size:32px;\">GAME OVER!</h1></html>" );
		}
		endgameLabel.setHorizontalAlignment( SwingConstants.CENTER );
		endgamePanel.add( endgameLabel );
		endgameLabelPanel.add(endgameLabel);
		endScreen.add( Box.createRigidArea( new Dimension( 60, 60 ) ) );
		endScreen.add( endgameLabelPanel );
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout( new BoxLayout( scorePanel, BoxLayout.Y_AXIS) );
		scorePanel.setAlignmentX( CENTER_ALIGNMENT );
		JLabel finalScoreLabel = new JLabel( "Final Score: " + Integer.toString( game.getScore() ) );
		finalScoreLabel.setHorizontalAlignment( SwingConstants.CENTER );
		JLabel newHighScoreLabel = new JLabel( "New Highscore!" );
		newHighScoreLabel.setHorizontalAlignment( SwingConstants.CENTER );
		scorePanel.setBorder( new EmptyBorder( 15, 15, 15, 15 ) );
		scorePanel.add( finalScoreLabel );
		if ( game.getScore() > game.getHighScore() ) {
			game.updateHighScore();
			scorePanel.add( newHighScoreLabel );
		} else {
			scorePanel.add( Box.createRigidArea( new Dimension( 15, 15 ) ) );
		}
		endScreen.add( scorePanel );
		JPanel endButtonPanel = new JPanel();
		mainMenu.add( endButtonPanel, CENTER_ALIGNMENT );
		endButtonPanel.setLayout( new GridLayout( 2, 1, 15, 15 ) );
		endButtonPanel.add( playAgainButton, CENTER_ALIGNMENT );
		endButtonPanel.add( quitButton, CENTER_ALIGNMENT );
		playAgainButton.setBackground( new Color(51, 58, 61) );
		playAgainButton.setForeground(Color.BLACK);
		playAgainButton.setFont(playAgainButton.getFont().deriveFont(Font.BOLD, 14f));
		playAgainButton.setFocusPainted(false);
		playAgainButton.setPreferredSize( new Dimension(300, 60) );
		quitButton.setBackground( new Color( 56, 62, 66 ) );
		quitButton.setForeground(Color.BLACK);
		quitButton.setFont(quitButton.getFont().deriveFont(Font.BOLD, 14f));
		quitButton.setFocusPainted(false);
		endScreen.add( endButtonPanel );

		content.add( endScreen );
		if (win) {
			Controller.SoundController.play("win");
		}

	}
	public void updateTimer() {
		timerLabel.setText( Integer.toString( game.getTime() ) );
	}
	public void updateScore() {
		scoreLabel.setText( "Score: " + Integer.toString( game.getScore() ) );
	}
	public String getAnswer() {
		return answerField.getText();
	}
	public void clearTextField() {
		answerField.setText("");
	};
	public void winLevel() {
		answerField.setEditable( false );
		answerField.setBackground(Color.WHITE);
		gameControlsBox.setVisible( false );
		nextLevelPanel.setVisible( true );
		game.stopTimer();
		game.increaseScore( game.getLevel() * game.getLevel() * 500 );
		Controller.SoundController.play("win");
		if (game.getLevel() == 3) {
			nextLevelButton.setText( "Finish Game" );
		} else {
			nextLevelButton.setText( "Next Level >>" );
		}
	};
	public void drawSolution( int x1, int y1, int dir, int l ) {
		Graphics g = getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		int xoffset;
		int yoffset;
		if (game.getLevel() == 1) {
			xoffset = tablePanel.getX()+5;
			yoffset = tablePanel.getY()+tableBorderHeight-6;
		} else if (game.getLevel() == 2) {
			xoffset = tablePanel.getX();
			yoffset = tablePanel.getY()+tableBorderHeight-15;
		} else {
			xoffset = tablePanel.getX();
			yoffset = tablePanel.getY()+tableBorderHeight+25;
		}
		int yh = 25;
		System.out.println(x1 + " " + y1 + " " + dir + " " + l + " " + charPanelSize );
		
		int x2;
		int y2;
		
		double xx1 = charPanelSize*(x1);
		double yy1 = charPanelSize*(y1);
		double xx2;
		double yy2;
		
        int it = 50;
		
		g2d.setColor( new Color(0, 255, 0, 80));
        GeneralPath d = new GeneralPath();

		
    	switch( dir ) {
        case 0 -> {
            d.moveTo( xx1 + xoffset+9, yy1 + yoffset + yh + 10 );
        	x2 = x1;
        	y2 = y1 + l - 1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.lineTo( xx2 + xoffset + 8, yy2 + yoffset + yh + 10 );
            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset + 9 +0.5*yh+ 0.5*yh*Math.cos( (Math.PI)-(Math.PI/it)*(it-i) ), yy2 + yoffset + 10 + yh + 0.5*yh*Math.sin( (Math.PI)-(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset +10 + yh, yy2 + yoffset + yh + 10 );
            d.lineTo( xx1 + xoffset +10+yh, yy1 + yoffset + yh + 10 );
            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset + 9 +0.5*yh- 0.5*yh*Math.cos( (Math.PI)-(Math.PI/it)*(it-i) ), yy1 + yoffset + 10 + yh - 0.5*yh*Math.sin( (Math.PI)-(Math.PI/it)*(it-i) ) );            
            }
        }
        case 1 -> {
            d.moveTo( xx1 + xoffset + yh - 4 , yy1 + yoffset + yh -3);
        	x2 = x1 + l - 1;
        	y2 = y1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.lineTo( xx2 + xoffset + yh - 2, yy2 + yoffset + yh -3);
            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset - 2 + yh + 0.5*yh*Math.cos( (Math.PI/2)-(Math.PI/it)*(it-i) ), yy2 + yoffset -3 + 1.5*yh + 0.5*yh*Math.sin( (Math.PI/2)-(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset + yh - 2, yy2 + yoffset + 2*yh -3);
            d.lineTo( xx1 + xoffset + yh - 4, yy1 + yoffset + 2*yh -3);
            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset - 4 + yh - 0.5*yh*Math.cos( (Math.PI/2)-(Math.PI/it)*(it-i) ), yy1 + yoffset -3 + 1.5*yh - 0.5*yh*Math.sin( (Math.PI/2)-(Math.PI/it)*(it-i) ) );            
            }
        }
        case 2 -> {
            d.moveTo( xx1 + xoffset+9, yy1 + yoffset + yh + 10 );
        	x2 = x1;
        	y2 = y1 - l + 1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.lineTo( xx2 + xoffset + 8, yy2 + yoffset + yh + 10 );
            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset + 9 +0.5*yh+ 0.5*yh*Math.cos( (Math.PI)-(Math.PI/it)*(it-i) ), yy2 + yoffset + 10 + yh - 0.5*yh*Math.sin( (Math.PI)-(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset +10 + yh, yy2 + yoffset + yh + 10 );
            d.lineTo( xx1 + xoffset +10+yh, yy1 + yoffset + yh + 10 );
            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset + 9 +0.5*yh- 0.5*yh*Math.cos( (Math.PI)-(Math.PI/it)*(it-i) ), yy1 + yoffset + 10 + yh + 0.5*yh*Math.sin( (Math.PI)-(Math.PI/it)*(it-i) ) );            
            }
        }
        case 3 -> {
            d.moveTo( xx1 + xoffset + yh - 4 , yy1 + yoffset + yh -3);
        	x2 = x1 - l + 1;
        	y2 = y1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.lineTo( xx2 + xoffset + yh - 2, yy2 + yoffset + yh -3);
            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset - 2 + yh - 0.5*yh*Math.cos( (Math.PI/2)-(Math.PI/it)*(it-i) ), yy2 + yoffset -3 + 1.5*yh + 0.5*yh*Math.sin( (Math.PI/2)-(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset + yh - 2, yy2 + yoffset + 2*yh -3);
            d.lineTo( xx1 + xoffset + yh - 4, yy1 + yoffset + 2*yh -3);
            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset - 4 + yh + 0.5*yh*Math.cos( (Math.PI/2)-(Math.PI/it)*(it-i) ), yy1 + yoffset -3 + 1.5*yh - 0.5*yh*Math.sin( (Math.PI/2)-(Math.PI/it)*(it-i) ) );            
            }
        }
        case 4 -> {
        	x2 = x1 + l - 1;
        	y2 = y1 + l - 1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.moveTo( xx1 + xoffset + yh - 11, yy1 + yoffset + 2*yh -7);
            d.lineTo( xx2 + xoffset + yh - 11, yy2 + yoffset + 2*yh -7);

            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset -3+ yh+ 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy2 + yoffset -4 + 1.5*yh + 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset + yh +6, yy2 + yoffset + yh );
            d.lineTo( xx1 + xoffset + yh +6, yy1 + yoffset + yh );

            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset -3 + yh - 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy1 + yoffset -4 + 1.5*yh - 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
        }
        case 5 -> {
        	x2 = x1 - l + 1;
        	y2 = y1 + l - 1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.moveTo( xx1 + xoffset + yh + 6, yy1 + yoffset + 2*yh -7);
            d.lineTo( xx2 + xoffset + yh + 6, yy2 + yoffset + 2*yh -7);

            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset -3+ yh- 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy2 + yoffset -4 + 1.5*yh + 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset + yh -11, yy2 + yoffset + yh );
            d.lineTo( xx1 + xoffset + yh -11, yy1 + yoffset + yh );

            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset -3 + yh + 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy1 + yoffset -4 + 1.5*yh - 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
        }
        case 6 -> {
        	x2 = x1 - l + 1;
        	y2 = y1 - l + 1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.moveTo( xx1 + xoffset + yh - 11, yy1 + yoffset + 2*yh -7);
            d.lineTo( xx2 + xoffset + yh - 11, yy2 + yoffset + 2*yh -7);

            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset -3+ yh- 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy2 + yoffset -4 + 1.5*yh - 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset + yh +6, yy2 + yoffset + yh );
            d.lineTo( xx1 + xoffset + yh +6, yy1 + yoffset + yh );

            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset -3 + yh + 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy1 + yoffset -4 + 1.5*yh + 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
        }
        case 7 -> {
        	x2 = x1 + l - 1;
        	y2 = y1 - l + 1;
        	xx2 = charPanelSize*(x2);
        	yy2 = charPanelSize*(y2);
            d.moveTo( xx1 + xoffset + yh + 6, yy1 + yoffset + 2*yh -7);
            d.lineTo( xx2 + xoffset + yh + 6, yy2 + yoffset + 2*yh -7);

            for (int i = 1; i < it; i++) {
                d.lineTo( xx2 + xoffset -3+ yh+ 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy2 + yoffset -4 + 1.5*yh - 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
            d.lineTo( xx2 + xoffset + yh -11, yy2 + yoffset + yh );
            d.lineTo( xx1 + xoffset + yh -11, yy1 + yoffset + yh );

            for (int i = 1; i < it; i++) {
                d.lineTo( xx1 + xoffset -3 + yh - 0.5*yh*Math.cos( -(Math.PI/4)+(Math.PI/it)*(it-i) ), yy1 + yoffset -4 + 1.5*yh + 0.5*yh*Math.sin( -(Math.PI/4)+(Math.PI/it)*(it-i) ) );            
            }
        }
    	}

		//double xx2 = charPanelSize*(x2-1);
		//double yy2 = charPanelSize*(y2-1);


        for (int i = 1; i < it; i++) {
            //d.lineTo( xx2 + xoffset + yh*Math.sin( (Math.PI/2)-(2*Math.PI/it)*i ), yy2 + yoffset + yh*Math.cos( (Math.PI/2)-(2*Math.PI/it)*i ) );            
        }
        //d.lineTo( xx1 + xoffset - yh, yy1 + yoffset - yh );
        for (int i = 1; i < it; i++) {
            //d.lineTo( xx1 + xoffset + yh*-Math.sin( (Math.PI/2)-(2*Math.PI/it)*i ), yy1 + yoffset + yh*-Math.cos( (Math.PI/2)-(2*Math.PI/it)*i ) );            
        }
        d.closePath();
        g2d.fill(d);
	}
	public void clear() {
		content.removeAll();
		content.revalidate();
		content.repaint();
	};
};

