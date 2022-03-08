import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class Model {
    // Files of respective categories stored in File instances
	private static final URL fruitPath = Model.class.getResource("fruits.txt");
    private static final URL animalPath = Model.class.getResource("animals.txt");
    private static final URL placePath = Model.class.getResource("places.txt");

    // Categories created with category name and their file which contains the words
    private static final Category fruits = new Category("Fruits", fruitPath);
    private static final Category animals = new Category("Animals", animalPath);
    private static final Category places = new Category("Places", placePath);

    // Lists of categories and categories already used
    private static final ArrayList<Category> categories = new ArrayList<>(Arrays.asList(fruits, animals, places));
    private static ArrayList<Category> usedCategories = new ArrayList();

    // High score is global, it does not pertain to a specific player
    private static final Random rand = new Random();
    private int highScore = 0;

    private static class Category {
        private String categoryName;
        private File file;
        public Category(String categoryName, URL path) {   // constructor initializes fields
            this.categoryName=categoryName;
            this.file= new File(path.getFile());
        }

        public String getCategoryName() {
            return this.categoryName;
        }

        public int getWordCount() {                         // gets number of words in category's file
            int count=0;
            try {
                Scanner scanner = new Scanner(this.file);
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                    count++;
                }
                scanner.close();
            } catch(FileNotFoundException e) {
                System.out.println("Error: File Not Found");
                e.printStackTrace();
            }
            return count;
        }

        public String getWordAtIndex(int index) throws Exception {      // gets word at specific line of file
            int count=0; String word=""; String temp="";
            try {
                Scanner scanner = new Scanner(this.file);
                while (scanner.hasNextLine()) {
                    count++;
                    temp = scanner.nextLine();
                    if (count == index) {
                        word = temp;
                    }

                }
            }catch(FileNotFoundException e) {
                System.out.println("error");
                e.printStackTrace();
            }
            return word;
        }
    }
    private static class Player {       // class for each individual player (one per game)
        private int score;
        private int level = 1;

        public int getScore() {
            return this.score;
        }

        public int getLevel() {
            return this.level;
        }

        public void updateScore(int score) {
            this.score = score;
        }
        
        public void resetScore() {
        	updateScore(0);
        }

        public void advanceLevel() {
            level++;
        }
        public void resetLevel() {
        	level = 1;
        }
    }

    private static class TimerModel {
        private int initSeconds = 60;
        private int seconds;
        public TimerModel() {
             seconds = initSeconds;
        }


        public int decrement() {
        	seconds--;
        	return seconds;
        }
        public int getSeconds() {
            return seconds;
        }
        public void reset() {
        	seconds = initSeconds;
        }

    }

    public class Table {
    	private String alpha = "abcdefghijklmnopqrstuvwxyz";
        private int tableSize;
        private int numWords = 0;
        private char[][] rows;
        private int[][] wordPos;
        private Game game;
        private List<String> words = new ArrayList<>();
        private List<Character> randomLetters = new ArrayList<>();
        
        public Table( Game game, int playerLevel ) throws Exception {
            switch (playerLevel) {
	            case 1 -> {
	                tableSize = 5;
	            }
	            case 2 -> {
	                tableSize = 7;
	            }
	            case 3 -> {
	                tableSize = 9;
	            }
	        }
            this.game = game;
            rows = new char[ tableSize ][ tableSize ];
            generateWords();
        }

        public Category selectRandomCategory() {
            int cat = rand.nextInt(categories.size());              // chooses random category
            Category selectedCat = categories.get(cat);
            game.setCategory( selectedCat.getCategoryName() );
            while (true) {
                if (!usedCategories.contains(selectedCat)) {
                    return selectedCat;
                }
            }
        }

        public void generateWords() throws Exception {
            //      make sure same category isn't picked twice
            //      limit -> word length limit
            //      numWords -> number of words

            //      # words and length depending on level (level 1: <=5,   level 2: <=7    level 3: <=9)
            //      level 1: 2 words
            //      level 2: 4 words
            //      level 3: 5 words
            switch (tableSize) {
                    case 5 -> {
                    	numWords = 3;
                    }
                    case 7 -> {
                    	numWords = 4;
                    }
                    case 9 -> {
                    	numWords = 5;
                    }
            }

            Category selectedCat = this.selectRandomCategory();

            while (this.words.size()<numWords) {                    // generate list of 5 random words from fruit file
                int n = rand.nextInt(selectedCat.getWordCount());   // read single RANDOM word from file
                String word = selectedCat.getWordAtIndex(n);


                if (!this.words.contains(word) &&       // verifies word isn't already in list
                        !word.equals("") &&                 // verifies word is non-empty
                        word.length()<=tableSize) {             // adds constraint of word length

                    this.words.add(word);
                }
            }
            wordPos = new int[ numWords ][ 4 ];
            System.out.println(words);
            mapWordsToTable();
        }

        public List<String> getWords() {
            return this.words;
        }

        public int[][] getWordPos() {
            return wordPos;
        }

        public void fill( char[][] mappedTable ) {
        // # random letters = total # letters - # letters from category word
            for (int i = 0; i < tableSize; i++) {
                for (int j = 0; j < tableSize; j++) {
                	if (mappedTable[i][j] == ' ') {
		                int n = rand.nextInt(26);
		                mappedTable[i][j] = alpha.charAt(n);
                	}
                }

            }
            rows = mappedTable;
        }
        public char[][] getRows() {
            return rows;        
        }
        public int getSize() {
        	return tableSize;
        }
        private void mapWordsToTable() {
        	int[][][][] initialValidityTables = generateInitialValidityTables();
        	char[][] mappedTable = generateMappedTable( initialValidityTables );
        	fill( mappedTable );
        }
        private char[][] generateMappedTable( int[][][][] initialValidityTables ) {
    		System.out.println("GENERATING MAPPED TABLE ...");
        	int n = words.size();
        	char[][][] mappedTable = new char[ tableSize ][ tableSize ][n];
        	char[][] finalMappedTable = new char[ tableSize ][ tableSize ];
        	int iterationCounter = 0;
        	while ( true ) {
        		iterationCounter++;
        		System.out.println("ITERATION "  + iterationCounter + "...");
        		boolean valid = true;
        		mappedTable = new char[ tableSize ][ tableSize ][n];
	        	// Make mapped table full of ' '
        		System.out.println("CLEARING MAPPED TABLE "  + iterationCounter + "...");
	        	for (int ni = 0; ni < n; ni++) {
	        		for (int i = 0; i < tableSize; i++) {
		                for (int j = 0; j < tableSize; j++) {
		                	mappedTable[i][j][ni] = ' ';
		                }
		            }
	        	}
        		System.out.println("MAPPED TABLE CLEARED");
	        	int[][] seed = generateSeed( n );
	    		System.out.println("SEED GENERATED!");
	        	int[][][] validityTables = new int[ tableSize ][ tableSize ][ n ];
	        	for ( int ni = 0; ni < n; ni++) {
	        		String currentWord = words.get(ni);
	        		int direction = seed[ni][0];
	        		System.out.println("WORD " + ( ni + 1 ) + ": " + currentWord);
	        		System.out.println("WORD " + ( ni + 1 ) + " DIR: " + direction);
	        		int dof = 0;
	        		if (ni != 0) { 
		        		// 1a. Find new validity tables based on previous word location
	        			nextValidityTable( validityTables, initialValidityTables, mappedTable, currentWord, direction, ni );
	        		} else {
	        			// 1b. Copy initial validity table for first word to the new validity table
	            		for (int i = 0; i < tableSize; i++) {
	                        for (int j = 0; j < tableSize; j++) {
	                        	validityTables[i][j][0] = initialValidityTables[i][j][direction][0];
	                        }
	                    }
	        		}
	    			// 2. Find DOF on new validity tables
	        		for (int i = 0; i < tableSize; i++) {
	                    for (int j = 0; j < tableSize; j++) {
	                    	if ( validityTables[i][j][ni] == 1 ) {
	                    		dof++;
	                    	}
	                    }
	                }
	        		System.out.println("WORD " + ( ni + 1 ) + " DOF: " + dof );
	        		if ( dof == 0 ) {
	            		// 3. If DOF == 0, we must reconsider the previous orientation
	        			valid = false;
                		System.out.println("TRYING AGAIN.... ");
                		System.out.println(" ");
	        			break;
	        		} else {
	            		// 4. Get random number within DOF
	            		seed[ni][1] = rand.nextInt( dof );
	            		System.out.println("WORD " + ( ni + 1 ) + " RAND POS: " + seed[ni][1] );
	            		// 5. Decrement DOF counter until we get to 0
	            		int dofCounter = seed[ni][1];
	            		boolean matched = false;
	            		for (int i = 0; i < tableSize; i++) {
	                        for (int j = 0; j < tableSize; j++) {
	                        	if ( validityTables[i][j][ni] == 1 ) {
	                            	if ( dofCounter == 0 ) {
	                            		System.out.println("RANDOM MATCHED ");
	                            		// 6. Map words into matrix
	                            		mappedTable = mapWord( mappedTable, currentWord, direction, i, j, ni );
	                                	matched = true;
	                            		break;
	                            	} 
	                        		dofCounter--;
	                        	}
	                        }
	                        if ( matched ) { break;}
	                    }
	        		}
	        		System.out.println("");
	        		System.out.println("MAPPED TABLE:");
	            	for (int i = 0; i < tableSize; i++) {
	                    for (int j = 0; j < tableSize; j++) {
	                    	if ( ni == n-1 ) {
	                    		// 7. Copy matrix to next level
	                    		finalMappedTable[i][j] = mappedTable[i][j][ni];
	                    	} else {
	                    		mappedTable[i][j][ni+1] = mappedTable[i][j][ni];
	                    	}
	                    	System.out.print( mappedTable[i][j][ni] );
	                    }
	            		System.out.println("");
	                }
	        	}
	        	if ( valid ) {break;}
        	}
			return finalMappedTable;
        }
        private int[][][][] generateInitialValidityTables() {
        	int n = words.size();
        	int[][][][] initialValidityTables = new int[ tableSize ][ tableSize ][ 8 ][ n ];
        	for ( int in = 0; in < n; in++ ) {
    			String currentWord = words.get( in );
            	System.out.println( currentWord );
            	for ( int orientation = 0; orientation < 4; orientation++ ) {
        			int wordLength = currentWord.length();
            		switch( orientation ) {
	                    case 0 -> {
	                        for (int i = 0; i < tableSize; i++) {
	                        	//System.out.println("");
	                            for (int j = 0; j < tableSize; j++) {
	                            	if ( i + wordLength > tableSize ) {
		                            	initialValidityTables[i][j][orientation][in] = 0;
		                            	//System.out.print( 0 );
	
	                            	} else {
		                            	initialValidityTables[i][j][orientation][in] = 1;
		                            	//System.out.print( 1 );
	                            	}
	                            }
	                        }
	                    }
	                    case 1 -> {
	                        for (int i = 0; i < tableSize; i++) {
	                        	//System.out.println("");

	                            for (int j = 0; j < tableSize; j++) {
	                            	if ( j + wordLength > tableSize ) {
		                            	initialValidityTables[i][j][orientation][in] = 0;
		                            	initialValidityTables[i][j][orientation + 3][in] = 0;
		                            	//System.out.print( 0 );
	                            	} else {
		                            	initialValidityTables[i][j][orientation][in] = 1;
		                            	// DR
		                            	if (initialValidityTables[i][j][orientation - 1][in] == 1) {
			                            	initialValidityTables[i][j][4][in] = 1;
		                            	} else {
			                            	initialValidityTables[i][j][4][in] = 0;
		                            	}
		                            	//System.out.print( 1 );
	                            	}
	                            }
	                        }
	                    }
	                    case 2 -> {
	                        for (int i = 0; i < tableSize; i++) {
	                        	//System.out.println("");

	                            for (int j = 0; j < tableSize; j++) {
	                            	if ( i < wordLength - 1 ) {
		                            	initialValidityTables[i][j][orientation][in] = 0;
		                            	//System.out.print( 0 );
	                            	} else {
		                            	initialValidityTables[i][j][orientation][in] = 1;
		                            	//System.out.print( 1 );
		                            	// UR
		                            	if (initialValidityTables[i][j][1][in] == 1) {
			                            	initialValidityTables[i][j][7][in] = 1;
		                            	} else {
			                            	initialValidityTables[i][j][7][in] = 0;
		                            	}
	                            	}
	                            }
	                        }
	                    }
	                    case 3 -> {
	                        for (int i = 0; i < tableSize; i++) {
	                        	//System.out.println("");

	                            for (int j = 0; j < tableSize; j++) {
	                            	if ( j < wordLength - 1 ) {
		                            	initialValidityTables[i][j][orientation][in] = 0;
		                            	//System.out.print( 0 );
	                            	} else {
		                            	initialValidityTables[i][j][orientation][in] = 1;
		                            	// DL
		                            	if (initialValidityTables[i][j][0][in] == 1) {
			                            	initialValidityTables[i][j][5][in] = 1;
		                            	} else {
			                            	initialValidityTables[i][j][5][in] = 0;
		                            	}
		                            	// UL
		                            	if (initialValidityTables[i][j][2][in] == 1) {
			                            	initialValidityTables[i][j][6][in] = 1;
		                            	} else {
			                            	initialValidityTables[i][j][6][in] = 0;
		                            	}
		                            	//System.out.print( 1 );	
	                            	}
	                            }
	                        }
	                    }
            		}
            	}
            }
        	return initialValidityTables;
        }
        private char[][][] mapWord( char[][][] matrix, String currentWord, int direction, int i, int j, int n ) {
        	wordPos[ n ][ 0 ] = j;
        	wordPos[ n ][ 1 ] = i;
        	switch( direction ) {
            case 0 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i+nj][j][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 1 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i][j+nj][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 2 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i-nj][j][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 3 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i][j-nj][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 4 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i+nj][j+nj][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 5 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i+nj][j-nj][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 6 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i-nj][j-nj][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
            case 7 -> {
            	int nj;
            	for ( nj = 0; nj < currentWord.length(); nj++ ) {
            		matrix[i-nj][j+nj][n] = currentWord.charAt( nj );
            		System.out.println("PARSING LETTER "+ nj + "...");
            	}
            }
        	}
        	wordPos[n][2] = direction;
        	wordPos[n][3] = currentWord.length();
    		System.out.println(n + " " + wordPos[ n ][ 0 ] + " " + wordPos[ n ][ 1 ] + " " + wordPos[ n ][ 2 ] + " " + wordPos[ n ][ 3 ] );
            return matrix;        
        }
    	private	int[][][] nextValidityTable( int[][][] validityTable, int[][][][] initialValidityTables, char[][][] mappedTable, String currentWord, int direction, int ni ) {
    		int ret;
    		for (int i = 0; i < tableSize; i++) {
                for (int j = 0; j < tableSize; j++) {
                	if ( initialValidityTables[i][j][direction][ni] == 1 ) {
                		ret = 0;
                		switch( direction ) {
    	                    case 0 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
    	                    		if ( !(mappedTable[i+nj][j][ni] == currentWord.charAt( nj ) || mappedTable[i+nj][j][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");
    	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 1 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
    	                    		if ( !(mappedTable[i][j+nj][ni] == currentWord.charAt( nj ) || mappedTable[i][j+nj][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");

    	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 2 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
    	                    		if ( !(mappedTable[i-nj][j][ni] == currentWord.charAt( nj ) || mappedTable[i-nj][j][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");
    	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 3 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
    	                    		if ( !(mappedTable[i][j-nj][ni] == currentWord.charAt( nj ) || mappedTable[i][j-nj][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");

    	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 4 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
       	                    		if ( !(mappedTable[i+nj][j+nj][ni] == currentWord.charAt( nj ) || mappedTable[i+nj][j+nj][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");

       	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 5 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
       	                    		if ( !(mappedTable[i+nj][j-nj][ni] == currentWord.charAt( nj ) || mappedTable[i+nj][j-nj][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");
       	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 6 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
       	                    		if ( !(mappedTable[i-nj][j-nj][ni] == currentWord.charAt( nj ) || mappedTable[i-nj][j-nj][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");

       	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }
    	                    case 7 -> {
    	                    	for ( int nj = 0; nj < currentWord.length(); nj++ ) {
       	                    		if ( !(mappedTable[i-nj][j+nj][ni] == currentWord.charAt( nj ) || mappedTable[i-nj][j+nj][ni] == ' ' ) ) {
                            			System.out.println("NO MATCH");

       	                    			ret = -1;
    	                    			break;
    	                    		}
    	                    	}
    	                    }   
                		}
                		if (ret == 0) {
                			System.out.println("MATCH");
                			validityTable[i][j][ni] = 1;
                		}
                	}
                }
            }
    		return validityTable;
    	}
	    private int[][] generateSeed( int n ) {
    		System.out.println("GENERATING SEED...");
	    	int[][] seed = new int[n][3];
	    	for (int i = 0; i < n; i++ ) {
	    		seed[i][0] = rand.nextInt( 8 );
	    	}
	    	return seed;
	    }
    }

    public class Game {
        private Timer timer = new Timer();
        private List<String> remWords;
        private List<String> words;
        private TimerModel timerModel = new TimerModel();
        private Player player = new Player();
        private Table table;
        private String category;
        private Window mainWindow;
        private Controller controller;
        private Controller.TimerController timerController;
        
	    public void addController( Controller newController ) {
		    	controller = newController;
        }
        public void start() throws Exception {
            timerModel.reset();
            player.resetLevel();
            player.resetScore();
            table = new Table( this, player.getLevel() );
            words = new ArrayList<String> (table.getWords() );
            remWords = new ArrayList<String> (table.getWords() );
            mainWindow.loadGame( table );
	    	timerController = controller.new TimerController();
            timerController.start();
        }
        public void stop( boolean win ) {
            mainWindow.loadEndScreen( win );
        }
        public void addWindow( Window window ) {
        	mainWindow = window;
        }
        public void updateHighScore() {
            highScore = player.getScore();        // updates high score
        }
        public int getHighScore() {
            return highScore;                     // returns high score
        }
        public void nextLevel() throws Exception {
        	if ( getLevel() == 3 ) {
        		mainWindow.loadEndScreen( true );
        	} else {
                player.advanceLevel();
                timerModel.reset();
    	    	timerController = controller.new TimerController();
                table = new Table( this, player.getLevel() );
                words = new ArrayList<String> (table.getWords() );
                remWords = new ArrayList<String> (table.getWords() );
                mainWindow.loadGame( table );
                timerController.start();
        	}
        }
        public int getLevel(){
            return player.getLevel();
        }
        public int[][] getWordPositions(){
            return table.getWordPos();
        }
        public int verifyWord(String word) {
            word = word.toLowerCase();
            int index;
            System.out.println(words);

            if ( remWords.remove( word ) ) {
                System.out.println(words);
            	index = words.indexOf(word);
                System.out.println("WTF"+index);
                if (remWords.size() == 0) {
                	mainWindow.winLevel();
                }
            } else {
            	index = -1;
            }

            System.out.println(index);
            return index;  // returns positive index if the word is in list of generated words
        }
        public void setCategory( String cat ) {
        	category = cat;
        }
        public String getCategory() {
        	return category;
        }
        public int getTime() {
        	return timerModel.getSeconds();
        }
        public int decrementTimer() {
        	return timerModel.decrement();
        }
        public void stopTimer() {
        	timerController.stop();
        }
        public int getScore() {
        	return player.getScore();
        }
        public void increaseScore( int n ) {
        	player.updateScore( player.getScore() + n );
        	mainWindow.updateScore();
        }
    }
}