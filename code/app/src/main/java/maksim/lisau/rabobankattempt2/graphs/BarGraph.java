package maksim.lisau.rabobankattempt2.graphs;
import processing.core.*;
/**
 * Created by willi on 9/12/2017.
 */

public class BarGraph extends Graph {
    String ylabel,xlabel;
    BarGraph(GraphStream g[], String s[], Grapher p){
        super(g,s,p);
        xlabel = s[2];
        ylabel = s[1];
    }
    float ayscale=1;
    void draw(){

        float ahi = main.aheight-padding-bottomkeyspace;
        netwidth  = main.awidth -2*padding-leftkeyspace  -rightkeyspace; //width of graph
        netheight = main.aheight-2*padding-bottomkeyspace-topkeyspace; //height of graph
        indexmin = (int)((float)xstart/xscale); //values to start
        indexmax = (int)((float)(xstart+netwidth)/xscale); // values to end

        yscale += (ayscale-yscale)/4f;

        main.pushMatrix();
        main.fill(50);
        main.textAlign(main.CENTER, main.CENTER);
        main.text(ylabel,padding,padding+topkeyspace,leftkeyspace-50,netheight);//yaxis label
        main.text(xlabel,padding+leftkeyspace,ahi,netwidth,bottomkeyspace-padding);//xaxis label
        main.text(title,padding,padding,main.awidth-2*padding,topkeyspace-padding);
        main.translate(padding+leftkeyspace,ahi);
        main.stroke(50);

        main.textAlign(main.RIGHT);
        main.line(0,0,0,-netheight);// y axis
        for(int i = 0;i<netheight;i+=15){
            main.stroke(50);
            main.line(0,-i,-5,-i);
            float yaxis = (i+this.ystart)/yscale;
            main.text(""+main.round(yaxis)  ,-10,-i);
            main.stroke(50,100);
            main.line(0,-i,netwidth,-i);
        }

        main.line(0,0,netwidth,0);// x axis




        main.fill(main.scheme[0]);
        //main.clip(0,-netheight,netwidth,netheight);
        main.line(0,this.ystart,netwidth,this.ystart);
        main.noStroke();

        float maxdev=0;
        float mindev=0;
        main.rectMode(main.CORNERS);
        for(int i = indexmin;i<=indexmax;i++){//draw bars
            main.textAlign(main.RIGHT);
            main.fill(main.scheme[0]);
            Node current = streams[0].get(i);
            if(current==null){
                continue;
            }
            main.rectMode(main.CORNERS);
            float value = ((Number)current.value.get(0)).floatValue();//value to graph

            maxdev = (value>0)?main.max(maxdev,value):maxdev;
            mindev = (value<0)?main.max(mindev,main.abs(value)):mindev;

            float rx=xscale*(i-indexmin)+1,ry=this.ystart,rx2=rx+xscale-3,ry2=ry-yscale*value;
            ry=main.constrain(ry,-netheight,0);
            ry2=main.constrain(ry2,-netheight,0);
            main.rect(rx,ry,main.constrain(rx2,0,netwidth),ry2,2);
            //
            main.fill(230);
            boolean iscontained = main.textWidth(externals[i+3])<main.abs(yscale*value);
            int sign = (int)Math.signum(yscale*value);
            if(iscontained){
                main.vtext(externals[i+3],rx+xscale*0.9f,ry-5,15,-main.abs(yscale*value)*sign); //if bar can contain text
            }else{
                //otherwise text outside
                main.fill(30);
                if((yscale*value)<0||main.textWidth(externals[i+3])>main.abs(-ry-5)){
                    //if bar<0
                    main.vtext(externals[i+3],rx+xscale,main.min(ry,ry2)-5,15,-(netheight+ry+5));
                }else{
                    main.textAlign(main.LEFT);
                    main.vtext(externals[i+3],rx+xscale,ry+5,15,-ry-5);
                }
            }
        }
        //noClip();

        //main.noClip();
        main.rectMode(main.CORNER);
        main.popMatrix();
        if(selected==-1){
            ayscale = (0.8f*netheight/(maxdev+mindev));
            if(ayscale<1){
                float inva = 1f/ayscale;
                float twopow = main.log(inva)/main.log(2);
                twopow -= twopow%1;
                ayscale = 1f/main.round(main.pow(2,twopow));
            }else if(ayscale>1){
                ayscale = (int)ayscale;
            }
        }
        if(ayscale<=0){
            ayscale=1;

        }
    }

    void onDragged(int mx,int my,int dirx,int diry){
        xstart-=dirx;
        ystart+=diry;
        //main.println("dragggeddddddd",dirx,diry,xstart,ystart);
    }

    void onTapped(int mx,int my){
        float ahi = main.aheight-padding-bottomkeyspace;
        for(int i = indexmin;i<=indexmax;i++){

            Node current = streams[0].get(i);
            if(current==null){
                continue;
            }

            float value = ((Number)current.value.get(0)).floatValue();//value to graph
            float rx=xscale*(i-indexmin)+1,ry=this.ystart,rx2=rx+xscale-3,ry2=ry-yscale*value;
            ry=main.constrain(ry,-netheight,0);
            ry2=main.constrain(ry2,-netheight,0);
            rx2 = main.constrain(rx2,0,netwidth);
            //rect(rx,ry,,ry2,2);
            //float scale=1;
            if(main.pointInside(mx-(padding+leftkeyspace),my-ahi,main.scale*rx,main.scale*ry,main.scale*(rx2-rx),main.scale*(ry2-ry))){
                if(selected==i){
                    selected = -1;
                }
                selected = i;
                ayscale = 0.65f*netheight/main.abs(value);
                return;
            }
        }
        selected = -1;
    }
}
