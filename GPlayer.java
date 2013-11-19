import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;

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
		boolean[][][] marked = new boolean[board.length][board[0].length][4];
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				if(!(board[i][j] == '.')){
					for(int k=0; k<4; k++){
						int	lineCount = vectorCount(board, marked, i, j, k, board[i][j], 0);
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

	public static int vectorCount(char[][] board, boolean[][][] marked, int i, int j, int k, char p, int c){
		marked[i][j][k] = true;
		c += 1;
		int[] nextDirection = nextDirection(i, j, k);
		i = nextDirection[0];
		j = nextDirection[1];
		if(i< 0 || j <0 || i>board.length || j>board[0].length || board[i][j] != p){//next spot is off the board
			return c;
		}
		if(board[i][j] == p){
			return vectorCount(board, marked, i, j, k, p, c);
		} //implicit else
		return c;
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

	public static int[] miniMaxDesc(Node node){
		node.value = maxVal(node);
		Node action = node.children.peek();

		p(node.value == action.value);

		//value and this value should be the same
		return action.move; //test to make sure that the priority que is working.
		// return node.children.peek().move; //test to make sure that the priority que is working.
		//return max
	}

	public static int maxVal(Node node){
		boolean[] term = isTerminalUtil(node.board, node.m, p);
		if(term[0]){
			return term[1] ? 1 : 0;
		}
		int v = -1;
		createChildren(node, p);
		v = minVal(node.children.peek());
		return v;
	}

	public static int minVal(Node node){
		boolean[] term = isTerminalUtil(node.board, node.m, o);
		if(term[0]){
			return term[1] ? 1 : 0;
		}
		int v = -1;
		createChildren(node, o);
		v = maxVal(node.children.peek());
		return v;
	}

	public static void createChildren(Node node, char u){
		for(int i=0; i<node.n; i++){
			for(int j=0; j<node.n; j++){
				if(node.board[i][j] == '.'){
					Node child = new Node(node.n, node.m, deepCopy(node.board), node);
					move(u, i, j, child);
					node.children.add(child);
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
		int n = 3;
		p = 'x';
		int m = 5;
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
					//make sure a node move works
					
					createChildren(node, 'x');
					Iterator<Node> i = node.children.iterator();
					while(i.hasNext()){
						Node test = i.next();
						p(boardToString(test.board));
					}

					p(boardToString(node.board));

					// int[] myMove = miniMaxDesc(node, p);
					// move(p, myMove[0], myMove[1], node);
					// p(myMove[0] + " " + myMove[1]);
					// p(boardToString(node.board));
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