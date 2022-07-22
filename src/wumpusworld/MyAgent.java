package wumpusworld;
import java.io.*;
/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    private World w;
    int rnd;
    
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
    
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction()
    {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        Decision_tree t1 = new Decision_tree();
        
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            t1.clear_adj_list();
            t1.reset_x_and_y();
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }

        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
            if(cX==t1.getadjx() && cY==t1.getadjy())
            {
                 t1.inc_breeze();
            }
           
        }
        if (w.hasStench(cX, cY))
        {
            if(cX==t1.getadjx() && cY==t1.getadjy())
            {
               System.out.println("I am in a Stench");
               t1.inc_stench();
            }
            
        }
        if (w.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
            t1.inc_pit();
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        
         rnd = t1.get_move(w);
         
        
        if (rnd==0)
        {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_MOVE);
        }
        
        if (rnd==1)
        {
            w.doAction(World.A_MOVE);
        }
                
        if (rnd==2)
        {
            w.doAction(World.A_TURN_LEFT);   
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_MOVE);

        }
                        
        if (rnd==3)
        {
            w.doAction(World.A_TURN_RIGHT);
            w.doAction(World.A_MOVE);
  
        }

        if(w.hasWumpus(w.getPlayerX(), w.getPlayerY()))
        {
           t1.inc_wumpus();
           t1.clear_adj_list();
           t1.reset_x_and_y();
        }
                
    }    
    
     /**
     * Genertes a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
    
    
}

