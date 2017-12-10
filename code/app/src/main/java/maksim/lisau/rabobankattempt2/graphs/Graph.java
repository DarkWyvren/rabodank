package maksim.lisau.rabobankattempt2.graphs;

import processing.core.PApplet;

/**
 * Created by willi on 9/12/2017.
 */

public abstract class Graph{
    GraphStream[] streams;
    String externals[];
    float panx = 0, pany = 0;
    float padding = 10;

    float yscale = 1;
    float xscale = 20;
    float ystart = 0;
    float xstart = 0;
    int selected = -1;
    float leftkeyspace = 80;
    float rightkeyspace = 30;

    float bottomkeyspace = 35;
    float topkeyspace = 35;

    float netwidth ;
    float netheight ;

    int indexmin = 0;
    int indexmax = 0;

    String title;
    Grapher main;
    Graph(GraphStream[] streams, String externals[], Grapher p){
        this.streams = streams;
        this.externals = externals;
        title = externals[0];
        netwidth =  p.width -2*padding-leftkeyspace  -rightkeyspace;
        netheight = p.height -2*padding-bottomkeyspace-topkeyspace;
        main = p;
    }

    abstract void draw();

    abstract  void onDragged(int mx,int my,int dirx,int diry);

    abstract void onTapped(int mx,int my);
}