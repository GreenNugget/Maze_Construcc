public class TimeCalculator{
int minutes=0;
int seconds=0;

    public void calcTimeforMaze(int totalDimonds, int xSize, int ySize){
        if(xSize/ySize < 1){
            refreshMinutes(ySize, xSize);
        }else{
            refreshMinutes(ySize, xSize);
            if(totalDimonds >6 && totalDimonds*.10 + seconds <= 60){
                refreshMinutes(ySize, xSize);
            }else{
                minutes+=1;          
            }
        }
        if(minutes ==0){
           minutes=2;
        }
    }

    private int refreshMinutes(int coordY,int coordX){
        return minutes+=(coordY/ coordX)*1+1;
    }
    
    public int getMinutes(){
        return minutes;    
    }

    public int getSeconds(){
        return seconds;
    }
}
