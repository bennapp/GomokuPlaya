import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;
import java.lang.Math;

public class GPlayer{
	public static long start;
	public static char p;
	public static char o;
	
	public static char[][] initBoard(int n){ //1 milli
		char[][] board = new char[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				board[i][j] = '.';
			}
		}
		return board;
	}

	public static char[][] stringToBoard(String s, int n){ //beggining from board?? not in tourny
		String[] strings = s.split("\n");
		char[][] board = new char[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				board[i][j] = strings[i].charAt(j);
			}
		}
		return board;
	}

	public static String boardToString(char[][] board){ //2 milli right before done outputting
		String boardString = "";
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				boardString +=  String.valueOf(board[i][j]);
			}
			boardString += String.valueOf('\n');
		}
		return boardString;
	}

	public static void move(char player, int x, int y, Node node){ //2 millis, 3 millis gplayer, 2 in node, but it might make the cost of creating a node cheaper
		node.board[x][y] = player;
		node.move = new int[]{x, y};
	}

	public static boolean[] isTerminalUtil(char[][] board, int m, char p){
		//boolean[][][] marked = new boolean[board.length][board[0].length][4];
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(!(board[i][j] == '.')){
					for(int k=0; k<4; k++){
						int	lineCount = vectorCount(board, i, j, k, board[i][j], 1, 0);
						if(lineCount == m){
							boolean u = false;
							if(board[i][j] == p){
								u = true;
							}
							return new boolean[] {true, u};
						}
					}
				}
			}
		}
		return new boolean [] {false, false};
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
		// int max = 0;
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(board[i][j] == '.'){
					for(int k=0; k<4; k++){
						int next = vectorCount(board, i, j, k, p, 0, 0);
						// if(next > max){
							// max = next;
						// }
						if(next == (m-1)){
							int[] lastSpot = nextDirection(i, j, k);
							//p("i = " + i);
							//p("j = " + j);
							//p("lastSpot[0] = " + lastSpot[0] );
							//p("lastSpot[1] = " + lastSpot[1] );
							lastSpot[0] = i + (lastSpot[0] - i) * m;
							lastSpot[1] = j + (lastSpot[1] - j) * m;
							//p("lastSpot[0] = " + lastSpot[0] );
							//p("lastSpot[1] = " + lastSpot[1] );
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
		// p(max);
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
									p("i = " + i );
									p("j = " + j );
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
		boolean[] result = isTerminalUtil(node.board, node.m, p);
		if(result[0] == false){
			int value = 0;
			char o;
			if(p == 'x'){
				o = 'o';
			} else {
				o = 'x';
			}
			int sfc = 12;
			int fc = 6;
			int otc = 4;
			int stc = 2;

			value += (straightFourCount(node.board, p, node.m) - 2*fc) * sfc;
			value += -( (straightFourCount(node.board, o, node.m) - 2*fc) * sfc) -1;
			value += (fourCount(node.board, p, node.m) - otc - stc) * fc;
			value += -( (fourCount(node.board, o, node.m) - otc - stc) * fc) -1;
			value += (openThreeCount(node.board, p, node.m) - 2*stc) * otc;
			value += -( (openThreeCount(node.board, o, node.m) - 2*stc) * otc) -1;
			value += (openThreeCount(node.board, p, node.m)) * stc;
			value += -( (openThreeCount(node.board, o, node.m)) * stc) -1;

			return value;
		} else {
			if(result[1] == true){
				return 300;
			} else {
				return -301;
			}
		}
	}


	public static int[] miniMaxDesc(Node node, int depth){
		node.value = maxVal(node, depth);

		int testm = -1;
		int[] testMax = node.children.peek();

		Iterator<int[]> i = node.children.iterator();
		while(i.hasNext()){
			int[] testA = i.next();
			p("testA[0]" + testA[0]);
			if(testm < testA[0]){
				testm = testA[0];
				testMax = testA;
				if(testA[0] == -1){
					System.err.println("something went wrong should not have -1");
				}
			}
		}
		int[] max = node.children.peek();

		if(max[0] != testm){
			p(max[0]);
			p(testm);
			System.err.println("priority que not working");
		}

		return testMax; //test to make sure that the priority que is working.
		// return node.children.peek().move;
	}

	public static int maxVal(Node node, int depth){
		boolean[] term = isTerminalUtil(node.board, node.m, p);
		if(term[0] || depth == 0){
			return heuristicVal(node, p);
		}
		int max = -1;
		createChildren(node, p);
		Iterator<int[]> i = node.children.iterator();
		while(i.hasNext()){
			int[] test = i.next();
			char[][] childBoard = deepCopy(node.board);
			Node child = new Node(node.n, node.m, childBoard, node);
			move(p, test[1], test[2], child);

			test[0] = minVal(child, depth -1);
			max = Math.max(max, test[0]);
		}
		return max;
	}

	public static int minVal(Node node, int depth){
		boolean[] term = isTerminalUtil(node.board, node.m, o);
		if(term[0] || depth == 0){
			return heuristicVal(node, o);
		}
		int max = -1;
		createChildren(node, o);
		Iterator<int[]> i = node.children.iterator();
		while(i.hasNext()){
			int[] test = i.next();
			char[][] childBoard = deepCopy(node.board);
			Node child = new Node(node.n, node.m, childBoard, node);
			move(o, test[1], test[2], child);

			test[0] = maxVal(child, depth -1);
			max = Math.max(max, test[0]);
		}
		return max;
	}

	public static void createChildren(Node node, char u){
		//for(int i=0; i<node.n; i++){
		//	for(int j=0; j<node.n; j++){
		//		if(node.board[i][j] == '.'){
		//			node.children.add(new int[] {-1, i, j});
		//		}
		//	}
		//}
		int l = node.board.length;
		int i = node.move[0];
		int j = node.move[1];
		boolean z = false;
		for(int k = 1; k<node.board.length; k++){
			if(k%2 == 0){
				z = true;
			} else {
				z = false;
			}
			for(int a = 0; a < k; a++){
				if(z){
					i--;
				}else{
					i++;
				}
				if(i > -1 && j > -1 && i < l && j < l){
					if(node.board[i][j] == '.'){
						node.children.add(new int[] {-1, i, j});

					}
				}
			}
			for(int a = 0; a < k; a++){
				if(z){
					j--;
				}else{
					j++;
				}
				if(i > -1 && j > -1 && i < l && j < l){
					if(node.board[i][j] == '.'){
						node.children.add(new int[] {-1, i, j});
					}
				}
			}
		}
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
	
	public static void p(Object p){System.out.println(p);}//for lazy typing
	public static void main(String[] args){
		//int n = args[0];
		//int m = args[1];
		//long s = args[2];
		//char p = args[3].charAt(0);
		int n = 5;
		p = 'x';
		int m = 4;
		long s = 5000;

		if(p == 'x'){
			o = 'o';
		} else {
			o = 'x';
		}

		int count = 0;

		char[][] board = initBoard(n);
		Node node = new Node(n, m, board, null);
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
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String input;
		if(p == 'x'){
			//make the first move
			// move(p, 1, 1, node);
		}
		try{
			//this could be optimized with a mod count
			while((input = stdIn.readLine()) != null ){
				if (input.charAt(0) == '.' || input.charAt(0) == 'o' || input.charAt(0) == 'x'){ //5 millis
				//do nothing
				} else {
					String[] inputString = input.split(" ");
					int x = Integer.parseInt(inputString[0]);
					int y = Integer.parseInt(inputString[1]);
					move(o, x, y, node);

					//move(p, 2, 1, node);
					//move(o, 2, 2, node);
					//move(o, 2, 3, node);
					//move(o, 2, 4, node);
					//move(o, 2, 5, node);
					//move(p, 2, 6, node);
					//p(boardToString(node.board));
					//p("fourCount x = " + fourCount(node.board, p, m));
					//p("fourCount o = " + fourCount(node.board, o, m));
					//p("straightFourCount x = " + straightFourCount(node.board, p, m));
					//p("straightFourCount o = " + straightFourCount(node.board, o, m));
					//p("openThreeCount x = " + openThreeCount(node.board, p, m));
					//p("openThreeCount o = " + openThreeCount(node.board, o, m));
					//p("splitThreeCount x = " + splitThreeCount(node.board, p, m));
					//p("splitThreeCount o = " + splitThreeCount(node.board, o, m));
					//p("isterm[0] = " + isTerminalUtil(node.board, m, o)[0]);
					//p("isterm[1] = " + isTerminalUtil(node.board, m, o)[1]);
					
					// int[] myMove = miniMaxDesc(node, 4);
					// move(p, myMove[1], myMove[2], node);
					// p("ais move = " + myMove[1] + " " + myMove[2]);
					// p(boardToString(node.board));
					// node = new Node(node.n, node.m, deepCopy(node.board), node);

					//createChildren(node, 'x');
					//Iterator<int[]> i = node.children.iterator();
					//while(i.hasNext()){
					//	int[] test = i.next();
					//	p("test[1] = "+ test[1]);
					//	p("test[2] = "+ test[2]);
					//	Node child = new Node(node.n, node.m, deepCopy(node.board), node);
					//	move('x', test[1], test[2], child);
					//	p(boardToString(child.board));
					//}


					//make sure not out of bounds?

				}
			}

		long begin = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		System.out.println("total time = " + (end - begin));

		} catch (IOException e){
			e.printStackTrace();
		}
	}
}