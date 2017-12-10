package maksim.lisau.rabobankattempt2.graphs;

/**
 * Created by willi on 9/12/2017.
 */

public class LineGraph extends Graph {
    String ylabel,xlabel;
    LineGraph(GraphStream g[], String s[],Grapher p){
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
            main.text(""+(main.round(yaxis*100)/100f)  ,-10,-i);
            main.stroke(50,100);
            main.line(0,-i,netwidth,-i);
        }

        main.line(0,0,netwidth,0);// x axis





        //main.clip(0,-netheight,netwidth,netheight);
        main.line(0,this.ystart,netwidth,this.ystart);
        main.noFill();
        float maxdev=0;
        float mindev=0;

        //float prev[] = new float[streams.length];
        for(int i = indexmin;i<=indexmax;i++){//draw bars
            for(int z = 0;z<streams.length;z++){
                main.stroke(main.scheme[z]);
                Node current = streams[z].get(i);
                if(current==null){
                    continue;
                }
                Node next = streams[z].get(i+1);
                if(next==null){
                    continue;
                }
                float value = ((Number)current.value.get(0)).floatValue();//value to graph
                float value2 = ((Number)next.value.get(0)).floatValue();//value to graph

                maxdev = (main.max(value,value2)>0)?main.max(maxdev,main.max(value,value2)):maxdev;
                mindev = (main.min(value,value2)<0)?main.max(mindev,main.abs(main.min(value,value2))):mindev;

                main.line(xscale*(i-indexmin)+1,-yscale*value+ystart,xscale*(i+1-indexmin)+1,-yscale*value2+ystart);
                //rect(xscale*(i-indexmin)+1,this.ystart,xscale-3,-yscale*value,2);
            }
        }
        //noClip();

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
        if(ayscale>10000){
            ayscale=1;

        }
    }

    void onDragged(int mx,int my,int dirx,int diry){
        xstart-=dirx;
        ystart+=diry;
    }

    void onTapped(int mx,int my){

    }
}
