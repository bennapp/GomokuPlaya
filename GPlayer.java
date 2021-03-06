//Ben Nappier
//ben2113
//Gomoku Agent

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.lang.Math;

public class GPlayer implements Runnable{

	public static char playerOne;
	public static long s;
	public static long begin;
	public static long end;
	public static boolean timedOut;
	public static int[] myMove;
	public static int[] tempMyMove;
	public static Node node;
	
	public static char[][] initBoard(int n){
		char[][] board = new char[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				board[i][j] = '.';
			}
		}
		return board;
	}

	//public static char[][] stringToBoard(String s, int n){ //beggining from board?? not in tourny
	//	String[] strings = s.split("\n");
	//	char[][] board = new char[n][n];
	//	for(int i=0; i<n; i++){
	//		for(int j=0; j<n; j++){
	//			board[i][j] = strings[i].charAt(j);
	//		}
	//	}
	//	return board;
	//}

	public static String boardToString(char[][] board){ //2 milli right before done outputting
		String boardString = " ";
		for(int i =0; i<board.length; i++){
			boardString += i%10 + " ";
		}
		boardString += "\n";
		for(int i=0; i<board.length; i++){
			boardString += i%10 ;
			for(int j=0; j<board[0].length; j++){
				boardString +=  String.valueOf(board[i][j]) + " ";
			}
			boardString += String.valueOf('\n');
		}
		return boardString;
	}

	public static void move(char player, int x, int y, Node node){ //2 millis, 3 millis gplayer, 2 in node, but it might make the cost of creating a node cheaper
		node.board[x][y] = player;
		node.move = new int[]{x, y};
	}

	public static boolean isTerminal(char[][] board, int m){
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(!(board[i][j] == '.')){
					for(int k=0; k<4; k++){
						int	lineCount = vectorCount(board, i, j, k, board[i][j], 1, 0);
						if(lineCount == m){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean fiveCount(char[][] board, char p, int m){
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] == p){
					for(int k=0; k<4; k++){
						int	lineCount = vectorCount(board, i, j, k, p, 1, 0);
						if(lineCount == m){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static int vectorCount(char[][] board, int i, int j, int k, char p, int c, int f){
		int[] nextDirection = nextDirection(i, j, k);
		i = nextDirection[0];
		j = nextDirection[1];

		char o;
		if(p == 'x'){
			o = 'o';
		} else {
			o = 'x';
		}

		if(i<0 || j<0){ //short circuiting not working?? order matters here and next three ifs
			return c;
		}
		if(i>=board.length || j>=board[0].length){
			return c;
		}
		if(board[i][j] == o){
			return c;
		}
		if( board[i][j] == '.' ){
			f -= 1;
		}
		if(f < 0){
			return c;
		}
		
		c += 1;
		return vectorCount(board, i, j, k, p, c, f);
	}

	public static int[] nextDirection(int i, int j, int k){
		if(k == 0){
			j+=1;
		} else if(k ==1){
			j+=1;
			i+=1;
		} else if(k ==2){
			i+=1;
		} else if(k ==3){
			j-=1;
			i+=1;
		}
		return new int[]{i, j};
	}

	public static int straightFourCount(char[][] board, char p, int m){ 
		int count = 0;
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] == '.'){
					for(int k=0; k<4; k++){
						int next = vectorCount(board, i, j, k, p, 0, 0);
						if(next == (m-1)){
							int[] lastSpot = nextDirection(i, j, k);
							lastSpot[0] = i + (lastSpot[0] - i) * m;
							lastSpot[1] = j + (lastSpot[1] - j) * m;
							if(lastSpot[0] > -1 && lastSpot[1] > -1 && lastSpot[0] < board.length && lastSpot [1] < board.length){
								if(board[lastSpot[0]][lastSpot[1]] == '.'){
									count++;
								}
							}
						}
					}
				}
			}
		}
		return count;
	}

	public static int fourCount(char[][] board, char p, int m){
		int count = 0;
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] == '.'){
					for(int k=0; k<4; k++){
						int next = vectorCount(board, i, j, k, p, 0, 0);
						if(next == (m-1)){
							count++;
						}
					}
				}
				if(board[i][j] == p){
					for(int k=0; k<4; k++){
						int next = vectorCount(board, i, j, k, p, 1, 1);
						if(next == m){
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	public static int openThreeCount(char[][] board, char p, int m){ //same thing as a open 3 count just m-1
		return straightFourCount(board, p, m-1);
	}

	public static int splitThreeCount(char[][] board, char p, int m){
		int count = 0;
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] == '.'){
					for(int k=0; k<4; k++){
						int next = vectorCount(board, i, j, k, p, 1, 1);
						if(next == (m)){
							int[] lastSpot = nextDirection(i, j, k);
							lastSpot[0] = i + (lastSpot[0] - i) * m;
							lastSpot[1] = j + (lastSpot[1] - j) * m;
							if(lastSpot[0] > -1 && lastSpot[1] > -1 && lastSpot[0] < board.length && lastSpot [1] < board.length){
								if(board[lastSpot[0]][lastSpot[1]] == '.'){
									count++;
								}
							}
						}
					}
				}
			}
		}
		return count;
	}

	public static int heuristicVal(Node node, char p){
		char o = getO(p);
		int value = 0;

		int win = 100000;
		int sfc = 0; //is two fc
		int fc = 4000;
		int otc = 0; //is two split 3
		int stc = 2000;

		//value += straightFourCount(node.board, p, node.m) * sfc;
		//value -= straightFourCount(node.board, o, node.m) * (sfc+1);
		value += fourCount(node.board, p, node.m)  * fc;
		value -= fourCount(node.board, o, node.m)  * (fc+1);
		//value += openThreeCount(node.board, p, node.m)  * otc;
		//value -= openThreeCount(node.board, o, node.m) * (otc+1);
		value += splitThreeCount(node.board, p, node.m) * stc;
		value -= splitThreeCount(node.board, o, node.m)  * (stc+1);

		if(fiveCount(node.board, p, node.m)){
			value += win;
		}
		if(fiveCount(node.board, o, node.m)){
			value -= win;
		}		

		return value;
	}


	public static int[] miniMaxDesc(Node node, int depth, int alpha, int beta, char p, char o, int numMove){
		node.value = maxVal(node, depth, alpha, beta, p, o, numMove);

		int[] maxChild = node.children.peek();

		 // int testm = -9999;
		 // int z = 0;
		 // int[] testMax = new int[] {-1, -1, -1};
		 
		 //Random randomGen = new Random();
		 //int x = randomGen.nextInt(node.children.size()/node.n);
		 //int[] randomMove = new int[] {-1, -1, -1};

		 //boolean same = true;

		// Iterator<int[]> i = node.children.iterator();

	// 	while(!(node.children.isEmpty())){
	// 		int[] testA = node.children.poll();
	// 		if(testm < testA[0]){
	// 			testm = testA[0];
	// 			testMax = testA;
	// 		}
	// 		////if(testA[0] != maxChild[0]){
	// ////			same = false;
	// 	////	}
	// 		////if(z == x){
	// 		////	randomMove = testA;
	// 		////}
	// 		if(z < 5){
	// 			p("testA[0]" + testA[0]); //BENMM
	// 			p("testA[0]" + testA[1]);
	// 			p("testA[0]" + testA[2]);
	// 			Node nodeNew = new Node(node.n, node.m, deepCopy(node.board), true);
	// 			move(p, testA[1], testA[2], nodeNew);
				
	// 			p(boardToString(nodeNew.board));
	// 			p("h val = " + heuristicVal(nodeNew, p));
	// 			p("isTerminal = " + isTerminal(nodeNew.board, nodeNew.m));
	// 			p("fiveCount x = " + fiveCount(nodeNew.board, playerOne, nodeNew.m));
	// 			p("fiveCount o = " + fiveCount(nodeNew.board, o, nodeNew.m));
	// 		}
	// 		z++;
	// 	}
		 //int[] max = node.children.peek();
		 //if(testMax != max){
		 //	p(max[0]);
		 //	p(testMax);
		 //	System.err.println("priority que not working");
		 //}

		 //if(same){
		 //	maxChild = randomMove;
		 //}

		return maxChild;
	}

	public static int maxVal(Node node, int depth, int alpha, int beta, char p, char o, int numMove){
		boolean term = isTerminal(node.board, node.m);
		if(term || depth == 0 || timedOut){
			return heuristicVal(node, p);
		}

		LinkedList<int[]> children = createChildren(node, numMove);
		while(!(children.isEmpty())){
			int[] childVals = children.pop();
			Node child = new Node(node.n, node.m, deepCopy(node.board), true);
			move(p, childVals[1], childVals[2], child);
			childVals[0] = minVal(child, depth -1, alpha, beta, p, o, numMove);
			node.children.add(childVals);
			if(alpha < childVals[0]){
				alpha = childVals[0];
			}
			if(alpha >= beta){
				return alpha;
			}
		}
		// Iterator<int []> i = node.children.iterator();
		// while(i.hasNext()){
		// 	int[] childVals = i.next(); 
		// 	if(alpha < childVals[0]){
		// 		alpha = childVals[0];
		// 	}
		// 	if(alpha >= beta){
		// 		return alpha;
		// 	}
		// }
		return alpha;
	}

	public static int minVal(Node node, int depth, int alpha, int beta, char p, char o, int numMove){
		boolean term = isTerminal(node.board, node.m);
		if(term || depth == 0 || timedOut){
			return heuristicVal(node, p);
		}

		LinkedList<int[]> children = createChildren(node, numMove);
		while(!(children.isEmpty())){
			int[] childVals = children.pop();
			Node child = new Node(node.n, node.m, deepCopy(node.board), false); //BENMIN
			move(o, childVals[1], childVals[2], child);
			childVals[0] = maxVal(child, depth -1, alpha, beta, p, o, numMove);
			node.children.add(childVals);
		
			if(beta > childVals[0]){
				beta = childVals[0];
			}
			if(beta <= alpha){
				return beta;
			}
		}
		//Iterator<int []> i = node.children.iterator();
		//while(i.hasNext()){
		//	int[] childVals = i.next(); 
		//	if(beta > childVals[0]){
		//		beta = childVals[0];
		//	}
		//	if(beta <= alpha){
		//		return beta;
		//	}
		//}
		return beta;
	}

	//couldn't quite get this.
	public static int getChildLess(Node node, int numMove){ //this is to remove some of the outside moves at the start
		double val = 0;
		double inc = ((double)numMove / (node.n * node.n)) * (double)(node.n/2);
		if (node.board.length/2 - inc -2 > 0){
			val = node.board.length/2 - inc -2;
		}
		return 0;
	}

	public static LinkedList<int[]> createChildren(Node node, int numMove){ //creates children in a spiral around prev move
		LinkedList<int[]> children = new LinkedList<int[]>();
		int length = node.board.length;
		int i = node.move[0];
		int j = node.move[1];
		boolean z = false;
		for(int k = 1; k< length + 5  /* - (getChildLess(node, numMove)))*/; k++){
			z = k%2 == 0 ? true : false;
			for(int a = 0; a < k; a++){
				i = z ? i-1 : i+1;
				if(i > -1 && j > -1 && i < length && j < length){
					if(node.board[i][j] == '.'){
						children.add(new int[] {-1, i, j});
					}
				}
			}
			for(int a = 0; a < k; a++){
				j = z ? j-1 : j+1;
				if(i > -1 && j > -1 && i < length && j < length){
					if(node.board[i][j] == '.'){
						children.add(new int[] {-1, i, j});
					}
				}
			}
		}
		return children;
	}

	public static char[][] deepCopy(char[][] board){
		char[][] newboard = new char[board.length][board.length];
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board.length; j++){
				newboard[i][j] = board[i][j];
			}
		}
		return newboard;
	}

	public static int[] generateRandMove(char[][] board){
		Random randomGen = new Random();
		int x = randomGen.nextInt(board.length);
		int y = randomGen.nextInt(board.length);
		while(! (board[x][y] == '.')){
			x = randomGen.nextInt(board.length);
			y = randomGen.nextInt(board.length);
		}
		return new int[] {x, y};
	}	

	public static void isTimedOut(){
		if((System.currentTimeMillis() - begin + 50) > s){
			timedOut = true;
		}
	}

	public static char getO(char p){
		char o = 'x';
		if(p == 'x'){
			o = 'o';
		}
		return o;
	}

	public static void isFinished(Node node, int m, char p, int numMove){
		boolean isFinished = isTerminal(node.board, m);
		if(isFinished){
			if(fiveCount(node.board, p, m)){
				System.out.println("Player " + p + " has won.");
			} else if(numMove == node.n*node.n){
				System.out.println("Game was a tie");
			}
			else {
				System.out.println("Player " + getO(p) + " has won.");
			}
			System.exit(0);
		}
	}

	public static void p(Object p){System.out.println(p);}//for lazy typing
	public static void main(String[] args){
		int n = 0;
		int m = 0;
		int mode = 0;
		char p = '?';
		boolean dlock = false; //dlock for debugging ai
		int numMove = 0;

		try{
			n = Integer.parseInt(args[0]);
			m = Integer.parseInt(args[1]);
			s = Integer.parseInt(args[2]);
			p = args[3].charAt(0);
			mode = Integer.parseInt(args[4]);
			
		} catch (Exception e){
			System.out.println("Invalid Args.");
			System.out.println("Try <boardLength> <winChain> <timeLimit> <x or o> <mode>");
			System.out.println("Mode1) player vs AI");
			System.out.println("Mode2) randomGen vs AI");
			System.out.println("Mode3) AI vs AI");
		}
		
		playerOne = p;
		char o = getO(playerOne);
		char[][] board = initBoard(n);
		node = new Node(n, m, board, true);

		if(playerOne == 'x' && mode != 4){
			int firstMove = n/2;
			p(firstMove + " " + firstMove);
			node = new Node(node.n, node.m, deepCopy(node.board), true);
			move(playerOne, firstMove, firstMove, node);
			p(boardToString(node.board));
			numMove++;
			
			end = System.currentTimeMillis();
		}
		

		if(mode == 1){
		try{

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String input;
			while((input = stdIn.readLine()) != null ){
				if (input.charAt(0) == '.' || input.charAt(0) == 'o' || input.charAt(0) == 'x'){ //5 millis
				//do nothing
				} else {

					begin = System.currentTimeMillis();
					
					String[] inputString = input.split(" ");
					int x = Integer.parseInt(inputString[0]);
					int y = Integer.parseInt(inputString[1]);
					if(!(x <0 || y<0 || x>=n || y>=n) &&node.board[x][y] == '.'){
						move(o, x, y, node);
						numMove++;
					} else {
						System.out.println("Invalid move. " + x + " " + y + " Move ignored. Technically " + playerOne + " has won. Continue to play for fun" );
					}
					
					int d = 1; //start depth you may want to make this dynamic
					myMove = new int[]{-1, -1, -1};
					tempMyMove = new int[]{-1, -1, -1};
					timedOut = false;

					if(dlock){
						myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
						p(myMove[1] + " " + myMove[2]);
						p("move value = " + myMove[0]);
						end = System.currentTimeMillis();
						tempMyMove[1] = myMove[1];
						tempMyMove[2] = myMove[2];
						tempMyMove[0] = myMove[0];
						node = new Node(node.n, node.m, deepCopy(node.board), true);
						move(playerOne, tempMyMove[1], tempMyMove[2], node);
						p(boardToString(node.board));
						p("d = " + d);
					} else{
						new Thread(new GPlayer()).start();
						while(!timedOut){
							myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
							d++;
							p("d = " + (d-1));
						}
					}




					numMove ++;
					isFinished(node, m, playerOne, numMove);
					long current = System.currentTimeMillis();

					p("begin - end = " + (end - begin));
					p("end - current = " + (current - end));
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		}//end mode1
		if(mode == 2){
			while(true){
				System.out.println("Random Move Player's turn:");

				int[] randMove = generateRandMove(node.board);
				p(randMove[0] + " " + randMove[1]);
				node = new Node(node.n, node.m, deepCopy(node.board), true);
				move(o, randMove[0], randMove[1], node);
				p(boardToString(node.board));
				numMove++;

				isFinished(node, m, playerOne, numMove);

				System.out.println("AI's turn:");
				begin = System.currentTimeMillis();
				int d = 1; //start depth you may want to make this dynamic
				myMove = new int[]{-1, -1, -1};
				tempMyMove = new int[]{-1, -1, -1};
				timedOut = false;
				isTimedOut();
				new Thread(new GPlayer()).start();
				while(!timedOut){
					myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
					d++;
				}
				isFinished(node, m, playerOne, numMove);
				numMove++;
			}
		}
		if(mode == 3){
			while(true){
				System.out.println("o's turn:");
				playerOne = 'o';
				o = getO(playerOne);

				begin = System.currentTimeMillis();
				int d = 1; //start depth you may want to make this dynamic
				myMove = new int[]{-1, -1, -1};
				tempMyMove = new int[]{-1, -1, -1};
				timedOut = false;
				
				if(dlock){
					myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
					p(myMove[1] + " " + myMove[2]);
					end = System.currentTimeMillis();
					tempMyMove[1] = myMove[1];
					tempMyMove[2] = myMove[2];
					tempMyMove[0] = myMove[0];
					node = new Node(node.n, node.m, deepCopy(node.board), true);
					move(playerOne, tempMyMove[1], tempMyMove[2], node);
					p(boardToString(node.board));
				} else{
					new Thread(new GPlayer()).start();
					while(!timedOut){
						myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
						d++;
					}
				}
				numMove++;

				isFinished(node, m, playerOne, numMove);

				System.out.println("x's turn:");
				playerOne = 'x';
				o = getO(playerOne);

				begin = System.currentTimeMillis();
				d = 1; //start depth you may want to make this dynamic
				myMove = new int[]{-1, -1, -1};
				tempMyMove = new int[]{-1, -1, -1};
				timedOut = false;
				if(dlock){
					myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
					p(myMove[1] + " " + myMove[2]);
					end = System.currentTimeMillis();
					tempMyMove[1] = myMove[1];
					tempMyMove[2] = myMove[2];
					tempMyMove[0] = myMove[0];
					node = new Node(node.n, node.m, deepCopy(node.board), true);
					move(playerOne, tempMyMove[1], tempMyMove[2], node);
					p(boardToString(node.board));
				} else{
					new Thread(new GPlayer()).start();
					while(!timedOut){
						myMove = miniMaxDesc(node, d, -999999, 999999, playerOne, o, numMove);
						d++;
					}
				}
				numMove++;

				isFinished(node, m, playerOne, numMove);
			}
		}//end mode 3
		if(mode == 4){ //debugging
			// s = 5000;
			// int n = 9;
			// int m = 5;
			// playerOne = 'x';
			// char o = getO(playerOne);
	
			// char[][] board = initBoard(n);
			// node = new Node(n, m, board, true);
			//String boardString = boardToString(node.board, node.n);
			//node.board = stringToBoard(boardString, n);
			//int[] dir = nextDirection(0, 0, 2);
			//p(dir[0]);
			//p(dir[1]);
			//node.board = move('x', 0, 3, node.board);
			//node.board = move('x', 1, 2, node.board);
			//node.board = move('x', 2, 1, node.board);
			//boolean[][][] marked = new boolean[board.length][board[0].length][4];
			// p("vcount = "+ vectorCount(board, marked, 5, 5, 2, 'x', 0));
			//System.out.println(boardToString(node.board, n));
			//p("isTerminal = " + isTerminal(board, 4));
			//p("isTerminal = " + isTerminal(board, 3));
			
			// move(playerOne, 2, 1, node);
			// move(o, 2, 3, node);
			// move(o, 2, 4, node);

//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . x . . . . . . . 
//. . . . . . . o x . . . . . . 
//. . . . . o x x x x o . . . . 
//. . . . . . . o o . o . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
//. . . . . . . . . . . . . . . 
// //. . . . . . . . . . . . . . . 


// 			move('o', 2, 2, node);
// 			move('o', 2, 3, node);
// 			move('o', 2, 4, node);
// 			move('o', 2, 5, node);
// 			move('x', 2, 0, node);

// 			//move('o', 4, 1, node);
// 			//move('x', 4, 2, node);
// 			//move('x', 4, 3, node);
// 			//move('x', 4, 4, node);
// 			//move('x', 4, 5, node);
// 			// move(playerOne, 2, 6, node);
// 			// move(o, 2, 7, node);
// 			p(boardToString(node.board));

// 			//BEN4
// 			p("isTerminal = " + isTerminal(node.board, node.m));
// 			p("fiveCount x = " + fiveCount(node.board, playerOne, m));
// 			p("fiveCount o = " + fiveCount(node.board, o, m));

// 			p("fourCount x = " + fourCount(node.board, playerOne, m));
// 			p("fourCount o = " + fourCount(node.board, o, m));
// 			p("straightFourCount x = " + straightFourCount(node.board, playerOne, m));
// 			p("straightFourCount o = " + straightFourCount(node.board, o, m));
// 			p("openThreeCount x = " + openThreeCount(node.board, playerOne, m));
// 			p("openThreeCount o = " + openThreeCount(node.board, o, m));
// 			p("splitThreeCount x = " + splitThreeCount(node.board, playerOne, m));
// 			p("splitThreeCount o = " + splitThreeCount(node.board, o, m));
// 			p("heuristicVal o = " + heuristicVal(node, o));
// 			p("heuristicVal x = " + heuristicVal(node, playerOne));

			//myMove = miniMaxDesc(node, 2, -999999, 999999, playerOne, o, 1);
			//node = new Node(node.n, node.m, deepCopy(node.board), true);
			//move(playerOne, myMove[1], myMove[2], node);
			//p(boardToString(node.board));


			// p("isterm[0] = " + isTerminalUtil(node.board, m, o)[0]);
			// p("isterm[1] = " + isTerminalUtil(node.board, m, o)[1]);
					
			// LinkedList<int[]> children = createChildren(node);
			// while(!children.isEmpty()){
			// 	int[] test = children.pop();
			// 	p("test[1] = "+ test[1]);
			// 	p("test[2] = "+ test[2]);
			// 	Node child = new Node(node.n, node.m, deepCopy(node.board), node);
			// 	move('x', test[1], test[2], child);
			// 	p(boardToString(child.board));
			// }
					//make sure it is not a draw
					//add a 2 in a row thing m-3
					//idfs start based on timer?

					//agent
			//four counts a otc but then that open tc subs two spc
		}

		
	}

	public void run(){
		while(!timedOut){
			isTimedOut();
		}
		p(myMove[1] + " " + myMove[2]);
		// p("move value = " + myMove[0]);
		node = new Node(node.n, node.m, deepCopy(node.board), true);
		move(playerOne, myMove[1], myMove[2], node);
		end = System.currentTimeMillis();
		p(boardToString(node.board));
	}
}