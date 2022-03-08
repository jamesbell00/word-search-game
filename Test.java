import javax.swing.JFrame;


public class Test {

	public static void main( String args[] ) throws Exception {
		Model gameModel = new Model();
		Model.Game game = gameModel.new Game();
        Window mainWindow = new Window( game );
        mainWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );	
        mainWindow.setSize( 700, 475 );
        mainWindow.setVisible( true );
        game.addWindow( mainWindow );
        Controller controller = new Controller( game, mainWindow );
        game.addController( controller );
        mainWindow.addController( controller );
	}
}