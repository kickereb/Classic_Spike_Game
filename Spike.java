import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Spike extends PApplet {

public void setup() {
  
}

float birdColor = color(0, 100, 200);
float difficulty = 2;
float diff = 4;
String diffText = "Normal"; 
int s = 0;
float a = 25;
float y = 300;
float vy = 3;
float b = 1;
int score = 00;
float[] spikeYValues = new float[] {
  50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550
};

int gameState; //0 = pre-grame, 1 = in-game, 2 = game over, 3 = setting, 4 = help

public void draw() {
  background(240);

  LeftSpikes leftspikes = new LeftSpikes();
  RightSpikes rightspikes = new RightSpikes();
  Bird bird = new Bird();
  topSpikes tSpikes = new topSpikes();
  colorSelector cS = new colorSelector();

  fill(200);
  ellipse(width/2, height/2, 200, 200);

  if (gameState==0) {
    noStroke();
    tSpikes.draw();
    if (keyPressed) {
      if (key == 's') {
        gameState = 3;
      }
      if (key == 'h') {
        gameState = 4;
      }
    }
    translate(-350, -50);
    textAlign(CENTER, CENTER);
    textSize(30);
    fill(240);
    text("Click to start", width/2, 40+(height/2));
    if (mousePressed) {
      gameState = 1;
    }
    //Settings & help
    fill(150);
    textSize(20);
    text("Press 's' for settings", width/2, (height/2)+170);
    text("Press 'h' for help", width/2, (height/2)+200);
  }

  if (gameState==1)                    //Start Game
  {
    bird.draw();
    tSpikes.draw();
    translate(-350, -50);
    pushMatrix();
    for (float spikeY : spikeYValues) {
      pushMatrix();
      translate(0, spikeY);
      if (s==1) {
        leftspikes.draw();
      } else {
        rightspikes.draw();
      }
      float leftD = dist(a, y, 25, spikeY-12.5f);
      float rightD = dist(a, y, (width-25), spikeY-12.5f);
      if (leftD < 12.5f && leftD > -12.5f && s==1) {
        gameState=2;
        leftD = 50;
      }
      if (rightD < 25 && rightD > -12.5f && s==0) {
        gameState=2;
        rightD = 50;
      }
      popMatrix();
    }
    popMatrix();
    if (y > height-25 || y < 25) {
      gameState=2;
    }
  }

  if (gameState==2) {
    fill(50);
    ellipse(a, y, 50, 50);
    tSpikes.draw();
    translate(-350, -50);
    pushMatrix();
    leftspikes.draw();
    popMatrix();
    rightspikes.draw();
    score = score;
    textAlign(CENTER, CENTER);
    textSize(30);
    fill(150);
    text("Thanks", width/2, 220);
    text("Press any key to restart", width/2, 170+(height/2));
    fill(240);
    text("Score", width/2, 105+(height/2));
    textSize(100);
    text(score, width/2, 40+(height/2));
    if (keyPressed) {
      gameState=0;
      s = 0;
      a = 25;
      y = 300;
      vy = 3;
      b = 1;
      score = 00;
    }
  }
  if (gameState == 3) {
    noStroke();
    tSpikes.draw();
    translate(-350, 0);
    fill(240);
    ellipse(width/2, height/2, 200, 200);
    textAlign(CENTER, CENTER);
    textSize(20);
    fill(110);
    text("Select Color:", width/2, 185);
    text("Press 'x' to go back", width/2, 550);
    if (keyPressed && key == 'x') {
      gameState = 0;
    }
    cS.draw();
    float c = get(mouseX, mouseY);
    if (mousePressed) {
      birdColor = color(c);
    }
    fill(birdColor);
    noStroke();
    ellipse(width/2, height/2, 50, 50);

    //Difficulty 
    fill(110);
    text("Difficulty", width/2, 100);
    text("Select Difficulty:", width/2, 420);
    fill(150);
    text(diffText, width/2, 120);
    text("Press 'i' for increasing difficulty\n 'e' for easy\n 'n' for normal\n 'h' for hard", width/2, 480);
    if (keyPressed) {
      if (key == 'i') {
        difficulty = 0;
      }
      if (key == 'e') {
        difficulty = 1;
      }
      if (key == 'n') {
        difficulty = 2;
      }
      if (key == 'h') {
        difficulty = 3;
      }
    }
    if (difficulty == 0) {
      diff = 5-(score/20);
      diffText = "Increasing";
      if (diff < 3) {
        diff = 3;
      }
    }
    if (difficulty == 1) {
      diff = 5;
      diffText = "Easy";
    }
    if (difficulty == 2) {
      diff = 4;
      diffText = "Normal";
    }
    if (difficulty == 3) {
      diff = 3;
      diffText = "Hard";
    }
  }
  if (gameState == 4) {
    tSpikes.draw();
    translate(-350, 0);
    textAlign(CENTER, CENTER);
    textSize(20);
    /*fill(240);
     noStroke();
     ellipse(width/2, height/2, 210, 220);*/
    fill(110);
    text("Press 'x' to go back", width/2, 550);
    text("Description:", width/2, 80);
    text("Instructions:", width/2, 350);
    fill(150);
    text("The object of the game is to jump between the two sides while avoiding the spikes. There are spikes on each wall, and they change every time you hit the wall. Every time you successfully avoid the spikes, you gain a point. The game is over when you hit a spike.", 55, 40, 300, 330);
    text("To jump, just simply presss The Space Bar. You can change the difficulty or color of your character by going to settings. Any other instructions will be posted where they need to be.", 55, 200, 300, 470);
    if (keyPressed && key == 'x') {
      gameState = 0;
    }
  }
}

float va=4;

class Bird {
  Bird() {
  }
  public void draw() {
    //    vy = vy *1.03;
    if (a>=(width-25)) {
      s=1;
      score++;
      randomizeSpikeYValues();
    }
    if (a==25) {
      s=0;
      score++;
      randomizeSpikeYValues();
    }
    if (s==0) {
      a=a+va;
    } else {
      a=a-va;
    }
    if (keyPressed) {
      if (key == ' ')
      {
        vy=5;
      }
    } else {
      vy -= 0.3f;
    }
    y=y-vy;
    if (y >(height-5)) {
      y = height-5;
    }
    if (y < 5) {
      y = 5;
    }
    noStroke();
    //score;
    textAlign(CENTER, CENTER);
    textSize(100);
    fill(240);
    text(score, width/2, 290);
    //bird
    fill(birdColor);
    ellipse(a, y, 50, 50);
  }
}

public void randomizeSpikeYValues() {
  int spike = 0;
  for (int i = 0; i < spikeYValues.length; i++) {
    spike+= 50*((int) random(diff)+1);
    spikeYValues[i] = spike;
  }
}

class colorSelector {
  public void draw() {
    for (int rings=60; rings<200; rings++) {
      noFill();
      stroke((rings*2)-120, 200, 250);
      ellipse(width/2, height/2, rings, rings);
    }
  }
}
class LeftSpikes {
  LeftSpikes() {
  }
  public void draw() {
    fill(100);
    triangle(0, 0, 25, 25, 0, 50);
  }
}

class RightSpikes {
  RightSpikes() {
  }
  public void draw() {
    fill(100);
    triangle(width, 0, width-25, 25, width, 50);
  }
}

class topSpikes {
  topSpikes() {
  }
  public void draw() {
    translate(-50, 0);
    fill(100);
    for (int x=0; x<400; x=x+50) {
      translate(50, 0);
      triangle(0, 0, 50, 0, 25, 25);
      triangle(0, height, 25, height-25, 50, height);
    }
  }
}
  public void settings() {  size(400, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Spike" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
