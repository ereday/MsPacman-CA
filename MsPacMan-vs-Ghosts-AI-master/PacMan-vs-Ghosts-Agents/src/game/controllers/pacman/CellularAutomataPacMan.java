package game.controllers.pacman;

import game.controllers.Direction;
import game.PacManSimulator;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.modules.Maze;
import game.core.Game;

import java.util.ArrayList;
import java.util.Random;


public class CellularAutomataPacMan extends PacManHijackController {

    public Direction findNewStep(Maze.MazeNode[] n, Maze.MazeNode testes){
        int numOfEdibleGhostNeighbor=0,numOfPowerPillNeighbor=0,numOfGhostNeighbor=0,numOfEmptyNeighbor=0,numOfPillNeighbor=0;
        int edibleGhostIndex=-1,powerPillIndex=-1,ghostIndex=-1,emptyIndex=-1,pillIndex=-1;
        float maxEdibleGhostDV=-1,maxPowerDV=-1,minGhostDV=100000,maxEmptyDV=-1,maxPillDV=-1;
        float decayValue;
        String value;

        System.out.println("***********");
        //System.out.println("suan bulundugum node index:"+testes.index +"bulundugum node value:"+testes.getNodeValue().getValue());
        for(int i=0;i<4;i++) {
            if (n[i] == null) {
                continue;
            }
          //  System.out.println("neigborIndex:"+n[i].index+" neighbor:"+n[i].getNodeValue().getValue()+" decay value:"+n[i].getNodeValue().getDecayValue());
        }

            for(int i=0;i<4;i++){
            if(n[i]==null){
                continue;
            }

            value=((n[i]).getNodeValue()).getValue();
            decayValue=((n[i]).getNodeValue()).getDecayValue();

            if(value.equals("E")){
               //// System.out.println("value equals E");
                numOfEdibleGhostNeighbor+=1;
                if(maxEdibleGhostDV<decayValue){
                    maxEdibleGhostDV=decayValue;
                    edibleGhostIndex=i;
                }

            }
            if(value.equals("P")){
               // System.out.println("value equals P");
                numOfPowerPillNeighbor+=1;
                if(maxPowerDV<decayValue){
                    maxPowerDV=decayValue;
                    powerPillIndex=i;
                }
            }
            if(value.equals("G")){
              //  System.out.println("value equals G");
                numOfGhostNeighbor+=1;
                if(minGhostDV>decayValue){
                    minGhostDV=decayValue;
                    ghostIndex=i;
                }
            }
            if(value.equals("e")){
              //  System.out.println("value equals e");
                numOfEmptyNeighbor+=1;
                if(maxEmptyDV<decayValue){
                    maxEmptyDV=decayValue;
                    emptyIndex=i;
                }

            }
            if(value.equals("p")){
               // System.out.println("value equals p");
                numOfPillNeighbor+=1;
                if(maxPillDV<decayValue){
                    maxPillDV=decayValue;
                    pillIndex=i;
                }
            }
        }
        Direction result=Direction.NONE;
        if(edibleGhostIndex>-1){
            if(edibleGhostIndex==0){
                result= Direction.UP;
            }
            else if(edibleGhostIndex==1){
                result= Direction.RIGHT;
            }
            else if(edibleGhostIndex==2){
                result= Direction.DOWN;
            }
            else{
                result= Direction.LEFT;
            }
        }
        else if(powerPillIndex>-1){
            if(powerPillIndex==0){
                result= Direction.UP;
            }
            else if(powerPillIndex==1){
                result= Direction.RIGHT;
            }
            else if(powerPillIndex==2){
                result= Direction.DOWN;
            }
            else{
                result= Direction.LEFT;
            }
        }

        else if(pillIndex>-1){
            if(pillIndex==0){
                result= Direction.UP;
            }
            else if(pillIndex==1){
                result= Direction.RIGHT;
            }
            else if(pillIndex==2){
                result= Direction.DOWN;
            }
            else{
                result= Direction.LEFT;
            }
        }

        else if(emptyIndex>-1){
            if(emptyIndex==0){
                System.out.println("UP");
                result= Direction.UP;
            }
            else if(emptyIndex==1){
                System.out.println("right");
                result= Direction.RIGHT;
            }
            else if(emptyIndex==2){
                System.out.println("down");
                result= Direction.DOWN;
            }
            else{
                System.out.println("left");
                result= Direction.LEFT;
            }
        }

        else if(ghostIndex>-1){
            if(ghostIndex==0){
                result= Direction.UP;
            }
            else if(ghostIndex==1){
                result= Direction.RIGHT;
            }
            else if(ghostIndex==2){
                result= Direction.DOWN;
            }
            else{
                result= Direction.LEFT;
            }
        }
        if(result==Direction.DOWN){
            System.out.println("down");
        }
        if(result==Direction.UP){
            System.out.println("up");
        }
        if(result==Direction.LEFT){
            System.out.println("left");
        }
        if(result==Direction.RIGHT){
            System.out.println("right");
        }

        System.out.println("***********");
        return result;


    }

    @Override
    public void tick(Game game, long timeDue) {

        /*
        Currently, update number is static, which means, at each time when the agent makes his decision
        we use the same updateNumber, in the final version it will be dynamic.
         */
        int updateNumber=19; // change it just for a constant for a time being
        Maze.MazeNode[] nodes=maze.getNodes();

        // initialize new node values
        for(int i=0; i<nodes.length;i++){

            nodes[i].setNodeValue();
            nodes[i].setDecayValue(1);
        }

        // update Cellular Automata

        while(updateNumber>0){

            ArrayList<Maze.MazeNode> empties=new ArrayList<>();
            for(int i=0;i<nodes.length;i++){
                if (!(nodes[i].isNodeValueEmpty() || nodes[i].isNodeValuePacman()) ) {
                    nodes[i].dominate();
                }
                else if((nodes[i].isNodeValueEmpty())){
                    empties.add(nodes[i]);
                    nodes[i].setDecayValue(1+nodes[i].uniform(-0.5f,0.5f,new Random()));
                }
            }
            for(Maze.MazeNode el:empties){
                if(!el.isNodeValueEmpty()){
                    el.dominate();
                }
            }

            updateNumber-=1;
        }
        // select the new step for the pacman
        int current=game.getCurPacManLoc();
        Maze.MazeNode[] pacmanNeighbors=maze.getNode(current).getNeighbours();
        pacman.set(findNewStep(pacmanNeighbors,maze.getNode(current)));

    }

    public static void main(String[] args) {
        PacManSimulator.play(new CellularAutomataPacMan(), new GameGhosts(false));
    }
}
