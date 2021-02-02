import java.rmi.Naming;
import java.util.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class PlayGame extends JFrame implements Runnable {

	private static final String KEY = "rmi://localhost:10101/";
	static String id ;


	private boolean isRunning;
	private Image dbImage;
	private Graphics dbg;
	public static int[][] levelMap;
	public static int[][] levelObjects;
	private boolean drawMap = true;
	private Image topRightCorner, topLeftCorner, topPiece, bottomPiece, rightPiece, leftPiece, allPiece, 
	bottomRightCorner, bottomLeftCorner, verticalSides, horizontalSides, lightBlue, whitePiece, noSides, greenPiece,
	rightEnd, bottomEnd, leftEnd, topEnd, playerPic,playerPic2, food, enemy;
	private MyRectangle player, winSquare,player2=new MyRectangle(30, 30,  40, 40);
	private ArrayList<MyRectangle> ground = new ArrayList<MyRectangle>();
	private ArrayList<MyRectangle> enemies = new ArrayList<MyRectangle>();
	private ArrayList<MyRectangle> foodObjects = new ArrayList<MyRectangle>();
	
	private int enemySpeed = 7;
	private int initialX, initialY, foodCount;
	
	private PrintWriter writer;
	
	public static int currentLevel;
	
	private List<String> dataFile;
	private String path;
	
	private boolean died, winLevel;
	private int deaths = 0;
	static IOnlineGames onlineGames;
	static String idPlayer2 ;
	int i=0;
	public static void Online()
	{
		 try {
            onlineGames = (IOnlineGames) Naming.lookup(KEY + "Server");
            id = onlineGames.initPlayer();
           
           	System.out.println("Player ID "+id);
            idPlayer2= onlineGames.waitOnlinePlayer(String.valueOf(currentLevel),id);
            // System.out.println(idPlayer2);

            System.out.println("Player2 ID "+idPlayer2);

                       

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public PlayGame(int width, int height, String title) {
		Online();
		setSize(width, height);
		setTitle(title);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		isRunning = true;
		addKeyListener(new AL());
		//addMouseListener(new ML());
		winLevel = false;
		System.out.println( "LEVEL" +currentLevel);
		foodCount = 0;

		for(int i=0; i < levelObjects.length; i++) {
			for(int j=0; j < levelObjects[i].length; j++) {	
				if(levelObjects[i][j] == 12) {
					player = new MyRectangle(30, 30, j * 40, i *40);
					initialX = player.x;
					initialY = player.y;
				}
				
				if(levelObjects[i][j] == 20) {
					foodCount++;
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					MyRectangle currentFood = new MyRectangle(20, 20, x, y);
					currentFood.i = i;
					currentFood.j = j;
					foodObjects.add(currentFood);
				}
				
				//Enemy moving up				
				if(levelObjects[i][j] >= 25 && (levelObjects[i][j] - 25) % 8 == 0) {
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					int multiplyFactor = 0;
					
					if(levelObjects[i][j] == 25)
						multiplyFactor = 10;
					else
						multiplyFactor = 11 - ((levelObjects[i][j] - 25) / 8);
					
					MyRectangle enemyP = new MyRectangle(10, 10, x, y);
					enemyP.dy = -enemySpeed;
					enemyP.minY = enemyP.y - (40 * multiplyFactor);
					enemyP.maxY = enemyP.y;
					enemyP.id = 1;
					enemies.add(enemyP);
				}				
				
				
				//Enemy moving down
				if(levelObjects[i][j] >= 26 && (levelObjects[i][j] - 26) % 8 == 0) {
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					int multiplyFactor = 0;
					
					if(levelObjects[i][j] == 26)
						multiplyFactor = 10;
					else
						multiplyFactor = 11 - ((levelObjects[i][j] - 26) / 8);
					
					MyRectangle enemyP = new MyRectangle(10, 10, x, y);
					enemyP.id = 1;
					enemyP.dy = -enemySpeed;
					enemyP.maxY = enemyP.y + (40 * multiplyFactor);
					enemyP.minY = enemyP.y;
					enemies.add(enemyP);
				}
				
				//Enemy moving left
				if(levelObjects[i][j] >= 27 && (levelObjects[i][j] - 27) % 8 == 0) {
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					int multiplyFactor = 0;
					
					if(levelObjects[i][j] == 27)
						multiplyFactor = 10;
					else
						multiplyFactor = 11 - ((levelObjects[i][j] - 27) / 8);
					
					MyRectangle enemyP = new MyRectangle(10, 10, x, y);
					enemyP.id = 2;
					enemyP.dx = -enemySpeed;
					enemyP.minX = enemyP.x - (40 * multiplyFactor);
					enemyP.maxX = enemyP.x;
					enemies.add(enemyP);
				}
					
				//Enemy moving right
				if(levelObjects[i][j] >= 28 && (levelObjects[i][j] - 28) % 8 == 0) {
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					int multiplyFactor = 0;
					
					if(levelObjects[i][j] == 28)
						multiplyFactor = 10;
					else
						multiplyFactor = 11 - ((levelObjects[i][j] - 28) / 8);
					
					MyRectangle enemyP = new MyRectangle(10, 10, x, y);
					enemyP.id = 2;
					enemyP.dx = -enemySpeed;
					enemyP.maxX = enemyP.x + (40 * multiplyFactor);
					enemyP.minX = enemyP.x;
					enemies.add(enemyP);
				}
				
				//Enemy moving in a square of certain radius
				if(levelObjects[i][j] >= 29 && (levelObjects[i][j] - 29) % 8 == 0) {
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					int multiplyFactor = 0;
					
					if(levelObjects[i][j] == 29)
						multiplyFactor = 10;
					else
						multiplyFactor = 11 - ((levelObjects[i][j] - 29) / 8);
					
					MyRectangle enemyP = new MyRectangle(10, 10, x, y);
					enemyP.id = 3;
					enemyP.dx = 0;
					enemyP.dy = -enemySpeed;
					enemyP.maxY = enemyP.y + (40 * multiplyFactor);
					enemyP.minY = enemyP.y;
					enemyP.maxX = enemyP.x + (40 * multiplyFactor);
					enemyP.minX = enemyP.x;
					enemies.add(enemyP);
				}
				
				//Stationary enemy
				if(levelObjects[i][j] == 30) {
					int y = (i * 40) + 10;
					int x = (j * 40) + 10;
					MyRectangle enemyP = new MyRectangle(10, 10, x, y);
					enemyP.dy = 0;
					enemyP.dx = 0;
					enemies.add(enemyP);
				}
			}
		}
		
		for(int i=0; i < levelMap.length; i++) {
			for(int j=0; j < levelMap[i].length; j++) {
				int currentTile = levelMap[i][j];
				switch(currentTile) {
				case 1:
					MyRectangle OtherNewPiece = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(OtherNewPiece);
					break;
				case 2:
					MyRectangle OtherNewPiece1 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(OtherNewPiece1);
					break;
				case 3:
					MyRectangle OtherNewPiece2 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(OtherNewPiece2);
					break;
				case 6:
					MyRectangle piece = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(piece);
					break;
				case 7:
					MyRectangle piece1 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(piece1);
					break;
				case 8:
					winSquare = new MyRectangle(40, 40, j * 40, i * 40);
					break;
				case 9:
					MyRectangle somePiece = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece);
					break;
				case 10:
					MyRectangle somePiece1 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece1);
					break;
				case 11:
					MyRectangle somePiece144 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece144);
					break;
				case 14:
					MyRectangle somePiece2 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece2);
					break;
				case 15:
					MyRectangle somePiece3 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece3);
					break;
				case 17:
					MyRectangle somePiece4 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece4);
					break;
				case 18:
					MyRectangle somePiece5 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece5);
					break;
				case 19:
					MyRectangle somePiece6 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece6);
					break;
				case 22:
					MyRectangle somePiece7 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece7);
					break;
				case 23:
					MyRectangle somePiece8 = new MyRectangle(40, 40, j * 40, i * 40);
					ground.add(somePiece8);
					break;
				}
			}
		}
		
		init();
	}
	
	public class AL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if(keyCode == e.VK_UP)
				player.dy = -5;
			if(keyCode == e.VK_DOWN)
				player.dy = 5;
			if(keyCode == e.VK_LEFT)
				player.dx = -5;
			if(keyCode == e.VK_RIGHT)
				player.dx = 5;
		}
		
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if(keyCode == e.VK_UP)
				player.dy = 0;
			if(keyCode == e.VK_DOWN)
				player.dy = 0;
			if(keyCode == e.VK_RIGHT)
				player.dx = 0;
			if(keyCode == e.VK_LEFT) 
				player.dx = 0;
		}
	}
	
	public void init() {
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage spriteSheet = null;
		
		try {
			spriteSheet = loader.loadImage("spritesheet.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SpriteSheet ss = new SpriteSheet(spriteSheet);
		
		bottomRightCorner = ss.grabSprite(700, 220, 40, 40);
		bottomLeftCorner = ss.grabSprite(750, 220, 40, 40);
		topLeftCorner = ss.grabSprite(800, 220, 40, 40);
		noSides = ss.grabSprite(850, 220, 40, 40);
		greenPiece = ss.grabSprite(900, 220, 40, 40);
		rightEnd = ss.grabSprite(950, 220, 40, 40);
		bottomEnd = ss.grabSprite(1000, 220, 40, 40);
		bottomPiece = ss.grabSprite(700, 270, 40, 40);
		leftPiece = ss.grabSprite(750, 270, 40, 40);
		topRightCorner = ss.grabSprite(800, 270, 40, 40);
		playerPic = ss.grabSprite(855, 275, 30, 30);
		playerPic2 = ss.grabSprite(400, 358, 64, 64);
		lightBlue = ss.grabSprite(900, 270, 40, 40);
		horizontalSides = ss.grabSprite(950, 270, 40, 40);
		leftEnd = ss.grabSprite(1000, 270, 40, 40);
		allPiece = ss.grabSprite(700, 320, 40, 40);
		topPiece = ss.grabSprite(750, 320, 40, 40);
		rightPiece = ss.grabSprite(800, 320, 40, 40);
		food = ss.grabSprite(850, 320, 40, 40);
		whitePiece = ss.grabSprite(900, 320, 40, 40);
		verticalSides = ss.grabSprite(950, 320, 40, 40);
		topEnd = ss.grabSprite(1000, 320, 40, 40);
		enemy = ss.grabSprite(1060, 330, 20, 20);
	}	
	
	public void move() {
		player.x += player.dx;
		player.y += player.dy;
		
		for(int i=0; i < ground.size(); i++) {
			player.blockRectangle(player, ground.get(i));
		}
		
		for(int i=0; i < enemies.size(); i++) {
			
			//For enemies moving up or down
			if(enemies.get(i).id == 1) {
				enemies.get(i).y += enemies.get(i).dy;
				
				if(enemies.get(i).y > enemies.get(i).maxY) {
					enemies.get(i).y = enemies.get(i).maxY;
					enemies.get(i).dy *= -1;
				} else if(enemies.get(i).y < enemies.get(i).minY) {
					enemies.get(i).y = enemies.get(i).minY;
					enemies.get(i).dy *= -1;
				}
			} //For enemies moving left or right 
			else if(enemies.get(i).id == 2) {
				enemies.get(i).x += enemies.get(i).dx;
				
				if(enemies.get(i).x > enemies.get(i).maxX) {
					enemies.get(i).x = enemies.get(i).maxX;
					enemies.get(i).dx *= -1;
				} else if(enemies.get(i).x < enemies.get(i).minX) {
					enemies.get(i).x = enemies.get(i).minX;
					enemies.get(i).dx *= -1;
				}
			} //For enemies moving in a square radius
			else if(enemies.get(i).id == 3) {
				MyRectangle currentEnemy = enemies.get(i);
				
				currentEnemy.x += currentEnemy.dx;
				currentEnemy.y += currentEnemy.dy;
				
				if(currentEnemy.x > currentEnemy.maxX) {
					currentEnemy.x = currentEnemy.maxX;
					currentEnemy.dx = 0;
					currentEnemy.dy = enemySpeed;
				} else if(currentEnemy.x < currentEnemy.minX) {
					currentEnemy.x = currentEnemy.minX;
					currentEnemy.dx = 0;
					currentEnemy.dy = -enemySpeed;
				}
				
				if(currentEnemy.y > currentEnemy.maxY) {
					currentEnemy.y = currentEnemy.maxY;
					currentEnemy.dy = 0;
					currentEnemy.dx = -enemySpeed;
				} else if(currentEnemy.y < currentEnemy.minY){
					currentEnemy.y = currentEnemy.minY;
					currentEnemy.dy = 0;
					currentEnemy.dx = enemySpeed;
				}
			}
			// 123456789
			if(enemies.get(i).blockRectangle(player, enemies.get(i)) != 0) {
				died = true;
				deaths++;
			}
			
			if(died) {
				player.x = initialX;
				player.y = initialY;
				died = false;
				foodCount = 0;
				
				for(int j=0; j < foodObjects.size(); j++) {
					MyRectangle currentFood = foodObjects.get(j);
					currentFood.width = 20;
					currentFood.height = 20;
					if(currentFood.x < 0)
						currentFood.x += 10000;
					
					if(currentFood.y < 0)
						currentFood.y += 10000;
					
					levelObjects[currentFood.i][currentFood.j] = 20;
					foodCount++;
				}
			}
				
		}
		
		for(int i=0; i < foodObjects.size(); i++) {
			MyRectangle currentFood = foodObjects.get(i);
			
			if(currentFood.blockRectangle(currentFood, player) != 0) {
				levelObjects[currentFood.i][currentFood.j] = 0;
				
				currentFood.x -= 10000;
				currentFood.y -= 10000;
				currentFood.width = 0;
				currentFood.height = 0;
				foodCount--;
				System.out.println("Coin Eated : "+foodCount);	
			}
		}
		
		if(player.x + 10 > winSquare.x && player.x + player.width - 10 < winSquare.x + winSquare.width && player.y + 10
				> winSquare.y && player.y + player.height - 10 < winSquare.y + winSquare.height && foodCount == 0) {
			// ###########
			winLevel = true;
			isRunning = false;
			System.out.println("YOU WIN");
			try{
			isRunning = false;
			onlineGames.sendWin(id);
			onlineGames.sendLoss(idPlayer2);
			Main.playGame = false;
			Main.drawMenu = true;
			Main.init();
			setVisible(false);
			// isRunning = false;
			// dispose();
		}catch (Exception e ){};

		}
		
		
	}
	
	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void paintComponent(Graphics g) {
		if(drawMap) {
			for(int i=0; i < levelMap.length; i++) {
				for(int j=0; j < levelMap[i].length; j++) {
					int currentTile = levelMap[i][j];
					switch(currentTile) {
					case 1:
						g.drawImage(bottomRightCorner, j * 40, i * 40, 40, 40, null);
						break;
					case 2:
						g.drawImage(bottomLeftCorner, j * 40, i * 40, 40, 40, null);
						break;
					case 3:
						g.drawImage(topLeftCorner, j * 40, i * 40, 40, 40, null);
						break;
					case 4:
						g.drawImage(noSides, j * 40, i * 40, 40, 40, null);
						break;
					case 5:
						g.drawImage(greenPiece, j * 40, i * 40, 40, 40, null);
						break;
					case 6:
						g.drawImage(rightEnd, j * 40, i * 40, 40, 40, null);
						MyRectangle piece = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(piece);
						break;
					case 7:
						g.drawImage(bottomEnd, j * 40, i * 40, 40, 40, null);
						MyRectangle piece1 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(piece1);
						break;
					case 8:
						g.drawImage(greenPiece, j * 40, i * 40, 40, 40, null);
						break;
					case 9:
						g.drawImage(bottomPiece, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece);
						break;
					case 10:
						g.drawImage(leftPiece, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece1 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece1);
						break;
					case 11:
						g.drawImage(topRightCorner, j * 40, i * 40, 40, 40, null);
						break;
					case 13:
						g.drawImage(lightBlue, j * 40, i * 40, 40, 40, null);
						break;
					case 14:
						g.drawImage(horizontalSides, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece2 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece2);
						break;
					case 15:
						g.drawImage(leftEnd, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece3 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece3);
						break;
					case 17:
						g.drawImage(allPiece, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece4 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece4);
						break;
					case 18:
						g.drawImage(topPiece, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece5 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece5);
						break;
					case 19:
						g.drawImage(rightPiece, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece6 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece6);
						break;
					case 21:
						g.drawImage(whitePiece, j * 40, i * 40, 40, 40, null);
						break;
					case 22:
						g.drawImage(verticalSides, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece7 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece7);
						break;
					case 23:
						g.drawImage(topEnd, j * 40, i * 40, 40, 40, null);
						MyRectangle somePiece8 = new MyRectangle(40, 40, j * 40, i * 40);
						ground.add(somePiece8);
						break;
					}
				}
			}
			
			for(int i=0; i < levelObjects.length; i++) {
				for(int j=0; j < levelObjects[i].length; j++) {
					if(levelObjects[i][j] == 20)
						g.drawImage(food, j * 40, i * 40, 40, 40, null);
				}
			}
			
			for(int i=0; i < enemies.size(); i++) {
				g.drawImage(enemy, enemies.get(i).x, enemies.get(i).y, 20, 20, null);
			}


			g.drawImage(playerPic, player.x, player.y, player.width, player.height, null);
			g.drawImage(playerPic2, player2.x, player2.y, player.width, player.height, null);
		}
		
		repaint();
	}
	
	public void run() {
		try {
			while(isRunning) {
				move();
				
				Thread.sleep(30);

				int x=-1,y=-1;
		
				try {
		           i++;


		            x = onlineGames.readX(idPlayer2);
		            y = onlineGames.readY(idPlayer2);

		            if(x == -1 )
		            {	if(isRunning)
		            	System.out.println("YOU LOSS");

		            	isRunning = false;

		            	Main.playGame = false;
						Main.drawMenu = true;
						Main.init();
						setVisible(false);
						break;
		            }

		            onlineGames.sendPlayerUpdate(player.x,player.y,id);
		            // System.out.println("X:"+x+" Y:"+y);
		        	player2.x =x;
		        	player2.y =y;

		        } catch (Exception e) {
		            e.printStackTrace();
		        }

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}