import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
	private Window mainWindow;
	private Model.Game game;
	// Sounds
	private static URL soundbyteCorrectWord;
	private static java.applet.AudioClip correctWordClip;
	private static URL soundbyteWin;
	private static java.applet.AudioClip winClip;
	private static URL soundbyteLose;
	private static java.applet.AudioClip loseClip;
	private static URL soundbyteWrongWord;
	private static java.applet.AudioClip wrongAnswerClip;
	
	public Controller( Model.Game game, Window window ) throws MalformedURLException {
		mainWindow = window;
		this.game = game;
		try {
			soundbyteCorrectWord = new File("CorrectWordSound.wav").toURI().toURL();
			correctWordClip = java.applet.Applet.newAudioClip(soundbyteCorrectWord);
			soundbyteWin = new File("WinSound.wav").toURI().toURL();
			winClip = java.applet.Applet.newAudioClip(soundbyteWin);
			soundbyteLose = new File("LoseSound.wav").toURI().toURL();
			loseClip = java.applet.Applet.newAudioClip(soundbyteLose);
			soundbyteWrongWord = new File("WrongAnswer.wav").toURI().toURL();
			wrongAnswerClip = java.applet.Applet.newAudioClip(soundbyteWrongWord);

		} catch (MalformedURLException e) {
			e.printStackTrace();

		}
	}
    public class NewGameButtonHandler implements ActionListener {
        public void actionPerformed( ActionEvent event ) { 
			try {
				game.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    public class QuitButtonHandler implements ActionListener {
        public void actionPerformed( ActionEvent event ) { 
			mainWindow.dispose();
        }
    }
    public class NextLevelButtonHandler implements ActionListener {
        public void actionPerformed( ActionEvent event ) { 
			try {
				game.nextLevel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    public class TextFieldHandler implements ActionListener {
        public void actionPerformed( ActionEvent event ) { 
			String answer =  mainWindow.getAnswer();
			int index = game.verifyWord( answer );
			System.out.println( index );
			if ( index != -1 ) {
				game.increaseScore( game.getLevel() * (game.getTime() * answer.length() + 5) );
				SoundController.play("correctWord");
				int[][] wordPos = game.getWordPositions();
				mainWindow.drawSolution(wordPos[index][0], wordPos[index][1], wordPos[index][2], wordPos[index][3]);
			} else {
				SoundController.play("incorrectWord");
			}
			mainWindow.clearTextField();
		}
    }
    public static class SoundController {
    	public static void play( String sound ) {
    		switch (sound) {
    		case "correctWord" -> {
				correctWordClip.play();
    		}
       		case "incorrectWord" -> {
       			wrongAnswerClip.play();
       		}
       		case "win" -> {
       			winClip.play();
       		}
       		case "lose" -> {
       			loseClip.play();
       		}
       		default -> {}
    		}
    	}
    }
    public class TimerController {
    	Timer timer = new Timer();
        TimerTask task = new Task();
		public void start() {
	        timer.schedule(task, 1000, 1000);
		}
		public void stop() {
			timer.cancel();
			timer.purge();
		}
    }
    private class Task extends TimerTask {
        public void run() {
            int t = game.decrementTimer();
            if (t == 0) {
				SoundController.play("lose");
            	game.stop( false );
            }
            mainWindow.updateTimer();
        }
    }
}
