package maksim.lisau.rabobankattempt2.graphs;
import processing.core.*;
import java.util.ArrayList;
/**
 * Created by willi on 9/12/2017.
 */

public class Grapher extends PApplet {

    float scale = 4;
    int aheight,awidth;

    int swidth=800,sheight=500;
    final int scheme[] = {color(20,120,200),color(200,120,20)};

    public Grapher(float dpi,int w,int h){

        super();
        swidth=w;
        sheight=h;
        scale = dpi/170;
        println("SCALE:",dpi,scale);
        println("DIM:",w,h);
        scale = constrain(scale,1,4);

    }
    public Grapher(float dpi,int w,int h,GraphStream stream[],GraphType s, ArrayList<String> externals){
        super();
        swidth=w;
        sheight=h;
        scale = dpi/170;
        println("SCALE:",dpi,scale);
        println("DIM:",w,h);
        scale = constrain(scale,1,4);
        if(!s.equals(GraphType.BAR)&&!s.equals(GraphType.LINE)){
            throw new UnsupportedOperationException("bar and line graphs only");

        }
        switch(s){
            case BAR:
                b = new BarGraph(stream, externals.toArray(new String[]{}),this);
                break;
            case LINE:
                b = new LineGraph(stream, externals.toArray(new String[]{}),this);
                break;
            default:
                break;

        }


    }
    Graph b;
    public void settings() {
        size(swidth, sheight);
    }
    public void setup(){
        //size(400,400);
        awidth = (int)(width/scale);
        aheight = (int)(height/scale);
        GraphStream s = new GraphStream("Test",new Node[]{});
        GraphStream s2 = new GraphStream("Test2",new Node[]{});
        ArrayList<String> externals = new ArrayList();
        externals.add("title tietel");
        externals.add("fucks i give");
        externals.add("time");
        for(int i = 0;i<25;i++){
            s.insert(new Node(random(-100,200),i));
            s2.insert(new Node(random(-100,200),i));
            externals.add("dw"+i);
        }
        if(b==null) {
            b = new LineGraph(new GraphStream[]{s, s2}, externals.toArray(new String[]{}), this);
        }
    }
    int tick=0;
    @Override
    public void draw(){

        background(222);
        stroke(0);
        noFill();
        rect(2,2,width-4,height-4);
        scale(scale);
            b.draw();
            updateInput();

        text(tick+"",awidth,300);
        tick++;
        //ellipse(random(width),random(height),50,50);
    }
    @Override
    public void mousePressed(){
        println("press");
        isholding = true;
        initialpress = new PVector(mouseX,mouseY);
    }
    public void mouseReleased(){
        println("release");
        if(isholding&&duration<30 && initialpress.dist(mouse)<5){ //otherwise its a tap action
            istap = true;
        }
        duration = 0;
        isholding=false;
    }
    boolean isholding = false;
    PVector initialpress = new PVector(0,0);
    PVector difference;
    float duration = 0;

    boolean istap,isdrag;
    PVector pmouse,mouse = new PVector(0,0);
    void updateInput(){
        istap = false;
        isdrag = false;
        pmouse=mouse;
        mouse = new PVector(mouseX,mouseY); //convery mouse to vector
        if(mousePressed){
            duration++;
            if(duration>30 || initialpress.dist(mouse)>5){ //held for a long time or moved fast = a drag action
                isdrag = true;
            }
        }else{
            if(isholding&&duration<30 && initialpress.dist(mouse)<5){ //otherwise its a tap action
                istap = true;
            }
            duration = 0;
            isholding=false;
        }
        if(isholding&&isdrag){
            //placeholders
            b.onDragged(mouseX,mouseY,(int)((mouse.x-pmouse.x)/scale),(int)((mouse.y-pmouse.y)/scale));
        }
        else if(istap){
            b.onTapped(mouseX,mouseY);
        }


        textAlign(RIGHT);
        text("held:"+isholding+"",width,20);
        text("tap:"+istap+"",width,40);
        text("dragged:"+isdrag+"",width,60);
        text("duration:"+duration+"",width,80);
    }


    public void vtext(String s,float x,float y){
        pushMatrix();
        translate(x,y);
        rotate(PI/2f);
        text(s,0,0);
        popMatrix();
    }

    public void vtext(String s,float x,float y,float w,float h){
        pushMatrix();
        translate(x,y);
        rotate(PI/2f);
        text(s,0,0,h,w);
        popMatrix();
    }

    boolean pointInside(float px,float py,float rx,float ry,float w,float h){
        if(h<0){
            h=-h;
            ry-=h;
        }
        if(w<0){
            w=-w;
            rx-=w;
        }
        return px>rx&&py>ry&&px<rx+w&&py<ry+h;
    }
    boolean pointInside(int px,int py,float rx,float ry,float rad){
        return sqrd(px-rx)+sqrd(py-ry)<rad*rad;
    }

    float sqrd(float x){
        return x*x;
    }

}
