// all java packages needed
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

// When I started to get into coding 2018/19, I used the Processing language to make simple games in Uni, then in free time, I created ClickyBird

public class Main implements ActionListener, MouseListener, KeyListener{

  
    // setting up variables
    public static Main clickyBird; // sets var FB to new FB
    public final int WIDTH = 800, HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird; 
    public int ticks, yMotion, score; // gives the bird motion
    public boolean gameOver, started;
    public ArrayList<Rectangle> columns; //defining type Rectangle
    
    public Random rand;
    
    public Main(){
        
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        
        
        renderer = new Renderer();
        rand = new Random();
        // setting the jframe aka GUI of the game
        jframe.add(renderer);
        jframe.setTitle("Clicky Bird"); //setting program title
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //when X is pressed, screen closes
        jframe.setSize(WIDTH, HEIGHT); //size
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);
        
        bird = new Rectangle(WIDTH / 2 -10, HEIGHT / 2 -10, 20, 20); //the bird and its size (x and y co-ords, centering it in screen)
        columns = new ArrayList<Rectangle>(); 
        
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        
        
        timer.start();
    }

    
    public void addColumn(boolean start){ //adding the columns to the game and screen
        int space = 300; //space between each column
        int width = 100;//column width
        int height = 50 + rand.nextInt(300);//random height of column to the max height
        
        if(start){ // if these are the Starting columns do this -
        columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height)); //move column to the right and make it 120 so its above the "ground"
        columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));//adds the upper column and gives us space
        }
        
        else{ // if not, do these, which continues the columns -
        columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height)); //gets column from arraylist, starts at position 0, adds 600 
        columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space)); //gets column from array list, starts from pos 0
            
        }
        
}
        public void paintColumn(Graphics g, Rectangle column){
        
        g.setColor(Color.black.darker()); //setting colours to the columns
        g.fillRect(column.x, column.y, column.width, column.height); //setting column position
    }
    
    public void jump(){
        if(gameOver){ //if the game is over then...
            
        bird = new Rectangle(WIDTH / 2 -10, HEIGHT / 2 -10, 20, 20); //create bird
        columns.clear(); // clear columns
        yMotion = 0; //clear position
        score = 0; //clear the score
        
        addColumn(true); //add the columns back
        addColumn(true);
        addColumn(true);
        addColumn(true);
        
            gameOver = false;
        }
        
        if(!started){ //if the game hasnt started then this makes it start
           started = true; 
        }
        
        else if(!gameOver){ //keep the bird at 0 centre position
            if(yMotion > 0){ //moves the bird
                yMotion = 0;
            }
            yMotion -= 10;//moves the bird
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        
        int speed = 10; // speed of bird 
        ticks++; //increase ticks by 1
        
        if( started){ // if game has started then...
              
        for( int i = 0; i < columns.size(); i++ ){ //if i is less than the column size, then add a column
            Rectangle column = columns.get(i); //gets the column
            column.x -= speed;
        }
        
        if(ticks % 2 == 0 && yMotion < 15) //
        {
            yMotion += 2; //
        }
        
        for( int i = 0; i < columns.size(); i++ ){
            Rectangle column = columns.get(i);
            
            if( column.x + column.width < 0){
                columns.remove(column); //column is removed 
                
                if(column.y == 0){
                    addColumn(false); //dont add second upper column
                }
                
            }
        }
       
        bird.y += yMotion; // makes bird fall
        
        for(Rectangle column : columns){
            
            if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 5 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10){
                score++; // if the bird passes through the columns without hitting them, then the score is increased - also only accounts for it 1 time
            }
            
            if(column.intersects(bird)){ //if the bird hits the column, then gameOver=true; , meaning the game is over.
                gameOver = true;
                
                if(bird.x <= column.x){
                    bird.x = column.x - bird.width; //if the bird falls, the column will move the bird - just an aesthetic thing
                }  
                else{
                    if(column.y != 0 )
                    {
                        bird.y = column.y - bird.height;
                    }
                    else if (bird.y < column.height) {
                        bird.y = column.height;
                    }

                        
                    }
                       
                }
                
            }
        }
        
        if(bird.y > HEIGHT - 120 || bird.y < 0){ // if the bird goes over max height / min height, the game ends
            
            gameOver = true;
        }
        
        if(bird.y + yMotion >= HEIGHT - 120){
            bird.y = HEIGHT - 120 - bird.height;
            gameOver = true;
        }
    
        
        renderer.repaint(); //repaints all the graphics as the screen moves
    }
    
    
    public void repaint(Graphics g){
        
        // setting the colours for the game and their placements
        g.setColor(Color.magenta); // the background will be magenta
		g.fillRect(0, 0, WIDTH, HEIGHT); 

		g.setColor(Color.blue); // the columns will be blue
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.gray); // the ground will be gray
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.cyan); // the bird will be cyan
		g.fillRect(bird.x, bird.y, bird.width, bird.height); //setting the birds position on screen
                
                for(Rectangle column : columns){ //iterates through columns and paints column on screen
                    paintColumn(g, column);
                }
                
                g.setColor(Color.white);
                g.setFont(new Font("Helvetica Neue", 1, 100)); // sets font colour and size
                
                 if( !started){
                    g.drawString("Click to start!", 75, HEIGHT/2-50); // draws the text string and it's position on screen
                }
                
                if( gameOver){
                    g.drawString("Game Over", 100, HEIGHT/2-50); //draws the text string and its postion on screen
                }
                
                if(!gameOver && started){
                    g.drawString(String.valueOf(score), WIDTH/2 - 25, 100); //shows score
                }
                
    }
    
    
    
    public static void main(String[] args) {
        
       clickyBird = new Main();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump(); //makes the bird 'jump' when mouse is clicked
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){ //makes the bird 'jump' when space-bar is clicked
            jump();
        }
    }
    
}

    

