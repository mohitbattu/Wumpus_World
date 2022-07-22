/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;
import java.util.*; 

/**
 *S
 * @author Johan Tejning,Devi Priya,Simron Pahdi
 */
public class Decision_tree {
    private static int movements;
    private static int destination_x=1;
    private static int destination_y=1;
    private static double breeze_to_pit;
    private static double amount_of_breezes;
    private static double stench_to_wumpus;
    private static double amount_of_stench;
    private static double amount_of_wumpus;
    private static double amount_of_pit;
    private static List<List<Integer>> Adjecent_nodes = new ArrayList<>();

  
    public void reset_x_and_y()
    {
        destination_x=1;
        destination_y=1;
    }
    
    public int getadjx()
    {
        return destination_x;
    }
    
    public int getadjy()
    {
        return destination_y;
    }

    public void shoot(int rnd, World w)
    {
        
        System.out.println(rnd);
        if (rnd==0)
        {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_SHOOT);
        }
        
        if (rnd==1)
        {
            w.doAction(World.A_SHOOT);
        }
                
        if (rnd==2)
        {
            w.doAction(World.A_TURN_LEFT);   
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_SHOOT);

        }
                        
        if (rnd==3)
        {
            w.doAction(World.A_TURN_RIGHT);
            w.doAction(World.A_SHOOT);
  
        }
    }
    public double get_probability_for_wumpus()
    {
         double dangerscore=0;
      if(movements==0)
      {
          return 0;
      }
      else if(amount_of_stench==0)
      {
          return 0;
      }
      else
      {
          if(stench_to_wumpus==0)
          {
              dangerscore=(((stench_to_wumpus+1)/amount_of_stench)*((amount_of_wumpus+1)/movements))/((amount_of_stench/movements)+2);
          }
          else
          {
              dangerscore= ((stench_to_wumpus/amount_of_stench) * (amount_of_wumpus/movements))/(amount_of_stench/movements);
          }
      }
      return dangerscore;
    }
    
    public double get_probability_for_pit()
    {
      double dangerscore=0;
      if(movements==0)
      {
          return 0;
      }
      else if(amount_of_breezes==0)
      {
          return 0;
      }
      else
      {
          if(breeze_to_pit==0)
          {
              dangerscore=(((breeze_to_pit+1)/amount_of_breezes)*((amount_of_pit+1)/movements))/((amount_of_breezes/movements)+2);
          }
          else
          {
              dangerscore= ((breeze_to_pit/amount_of_breezes) * (amount_of_pit/movements))/(amount_of_breezes/movements);
          }
      }
      return dangerscore;
    }
   
    public void remove_from_adjecent_list(int inputx, int inputy)
    {
         int removex=0;
         int removey=0;
         List<List<Integer>> copy_list = new ArrayList<>(Adjecent_nodes);
         for(List<Integer>L : Adjecent_nodes)
         {
             removex=L.get(0);
             removey=L.get(1);
            if(removex==inputx && removey==inputy)
            {
                copy_list.remove(L);
            }
         }
         Adjecent_nodes=new ArrayList<>(copy_list);
         
    }
    public double calc_danger_score(int currentx, int currenty, int adjx, int adjy, World this_world)
    {
        int breeze_found_previously=0;
        int stench_found_previously = 0;
        double dangerscore=0;
        int current_width=adjx;
        int current_height=adjy;
        int right=adjx+1;
        int left=adjx-1;
        int up=adjy+1;
        int down=adjy-1; 
        int x=0;
        int y=0;
        int direction_x=0;
        int direction_y=0;
        int rnd=0;
        if(this_world.isVisited(current_width, up))
        {
            
           if(this_world.hasBreeze(current_width, up) && this_world.hasStench(current_width, up))
           {
               if(stench_found_previously >breeze_found_previously)
               {
                   stench_found_previously++;
                   dangerscore=get_probability_for_wumpus() * stench_found_previously;
                   dangerscore+=0.00001;
               }
               else if(breeze_found_previously>stench_found_previously)
               {
                   breeze_found_previously++;
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
               }
               else
               {
                   stench_found_previously++;
                   breeze_found_previously++;
                   dangerscore+=get_probability_for_wumpus();
                   dangerscore+=get_probability_for_pit();
                   dangerscore+=0.00001;
               }
               if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
           }
           else if(this_world.hasBreeze(current_width, up))
           {
               
               if(stench_found_previously >breeze_found_previously)
               {
                   return 0;
               }
               breeze_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
               }
               else
               {
                    dangerscore+=get_probability_for_pit();
               }
              
              
           }

          else if(this_world.hasStench(current_width, up))
           {
               
               if(breeze_found_previously>stench_found_previously)
               {
                   return 0;
               }
               stench_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_wumpus() *stench_found_previously;
               }
               else
               {
                    dangerscore+=get_probability_for_wumpus();
               }
               if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
               dangerscore+=0.00001;
           }
           else
           {
               return 0;
           }
           
        }
        if(this_world.isVisited(current_width, down))
        {
             if(this_world.hasBreeze(current_width, down) && this_world.hasStench(current_width, down))
            {
               if(stench_found_previously >breeze_found_previously)
               {
                   stench_found_previously++;
                   dangerscore=get_probability_for_wumpus() * stench_found_previously;
                   dangerscore+=0.00001;
               }
               else if(breeze_found_previously>stench_found_previously)
               {
                   breeze_found_previously++;
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
                   
               }
               else
               {
                                  
                   stench_found_previously++;
                   breeze_found_previously++;
                   dangerscore+=get_probability_for_wumpus();
                   dangerscore+=get_probability_for_pit();
                   dangerscore+=0.00001;
               }
                if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
               
            }
           else if(this_world.hasBreeze(current_width, down))
           {
               
               if(stench_found_previously >breeze_found_previously)
               {
                   return 0;
               }
               breeze_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
               }
               else
               {
                    dangerscore+=get_probability_for_pit();
               }
              
              
           }
          else if(this_world.hasStench(current_width, down))
           {
               if(breeze_found_previously>stench_found_previously)
               {
                   return 0;
               }
               stench_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_wumpus() *stench_found_previously;
               }
               else
               {
                    dangerscore+=get_probability_for_wumpus();
               }
               if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
               dangerscore+=0.00001;
           }
           else
          {
              return 0;
          }
        }
        if(this_world.isVisited(right, current_height))
        {
            if(this_world.hasBreeze(right, current_height) && this_world.hasStench(right, current_height))
            {
               if(stench_found_previously >breeze_found_previously)
               {
                   stench_found_previously++;
                   dangerscore=get_probability_for_wumpus() * stench_found_previously;
                   dangerscore+=0.00001;
               }
               else if(breeze_found_previously>stench_found_previously)
               {
                   breeze_found_previously++;
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
               }
               else
               {
                   stench_found_previously++;
                   breeze_found_previously++;
                   dangerscore+=get_probability_for_wumpus();
                   dangerscore+=get_probability_for_pit();
               }
               if(stench_found_previously>=2)
               {
                   
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
                  
            }
            
            else if(this_world.hasBreeze(right, current_height))
           {
               
               if(stench_found_previously >breeze_found_previously)
               {
                   return 0;
               }
               breeze_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
               }
               else
               {
                    dangerscore+=get_probability_for_pit();
               }
              
              
           }
           else if(this_world.hasStench(right, current_height))
           {
               if(breeze_found_previously>stench_found_previously)
               {
                   return 0;
               }
               stench_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_wumpus() *stench_found_previously;
               }
               else
               {
                    dangerscore+=get_probability_for_wumpus();
               }
               if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
               dangerscore+=0.00001;
           }
           else
          {
              return 0;
          }
        }
        if(this_world.isVisited(left, current_height))
        {
            if(this_world.hasBreeze(left, current_height) && this_world.hasStench(left, current_height))
            {
               if(stench_found_previously >breeze_found_previously)
               {
                   stench_found_previously++;
                   dangerscore=get_probability_for_wumpus() * stench_found_previously;
                   dangerscore+=0.00001;
               }
               else if(breeze_found_previously>stench_found_previously)
               {
                   breeze_found_previously++;
                   dangerscore=get_probability_for_pit() *breeze_found_previously;
                  
               }
               else
               {
                   stench_found_previously++;
                   breeze_found_previously++;
                   dangerscore+=get_probability_for_wumpus();
                   dangerscore+=get_probability_for_pit();
               }
                if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
            }
           else if(this_world.hasBreeze(left, current_height))
           {
               
               if(stench_found_previously >breeze_found_previously)
               {
                   return 0;
               }
               breeze_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_pit() *breeze_found_previously ;
               }
               else
               {
                    dangerscore+=get_probability_for_pit();
               }
              
              
           }
           else if(this_world.hasStench(left, current_height))
           {
               if(breeze_found_previously>stench_found_previously)
               {
                   return 0;
               }
               stench_found_previously++;
               if(stench_found_previously >=1 && breeze_found_previously>=1)
               {
                   dangerscore=get_probability_for_wumpus() *stench_found_previously;
               }
               else
               {
                    dangerscore+=get_probability_for_wumpus();
               }
               if(stench_found_previously>=2)
               {
                    direction_x= adjx-this_world.getPlayerX();
                    direction_y= adjy-this_world.getPlayerY();
                    int tempx=destination_x;
                    int tempy=destination_y;
                    destination_x=adjx;
                    destination_y=adjy;
                    rnd=this.choose_direction(this_world.getPlayerX(), this_world.getPlayerY(), direction_x, direction_y, this_world);
                    shoot(rnd,this_world);
                    destination_x=tempx;
                    destination_y=tempy;
                    return this.calc_danger_score(currentx, currenty, adjx, adjy, this_world);
               }
               dangerscore+=0.00001;
           }
            else
           {
               return 0;
           }
        }
     
        return dangerscore; 
    }
    
    public void clear_adj_list()
    {
        Adjecent_nodes.clear();
    }
    public ArrayList<Integer> choose_next_node(int current_x, int current_y, World this_world)
    {
        int cX=0;
        int cY=0;
        double dangerscore=1000;
        double temp=0;
        ArrayList<Integer>next_node=new ArrayList<Integer>();
        for(List<Integer>L : Adjecent_nodes)
        {
            cX=L.get(0);
            cY=L.get(1);
            temp=calc_danger_score(current_x,current_y,cX,cY,this_world);
            if(temp<dangerscore)
            {
                dangerscore=temp;
                if(!next_node.isEmpty())
                {
                    next_node.clear();

                }
                next_node.add(cX);
                next_node.add(cY);
            }
        }
        return next_node;
    }
    public int choose_direction(int current_x, int current_y,int direction_x,int direction_y,World this_world)
    {
        boolean right=false;
        boolean left=false;
        boolean up=false;
        boolean down=false;
        int old_direction=0;
        int new_direction=-1;
        int rnd=0;
        if(direction_x<0 && direction_y==0) //go left
        {
            
               
                    if(this_world.isVisited(current_x-1, current_y) || (current_x-1 == destination_x && current_y == destination_y))
                    {
                         if(!this_world.hasPit(current_x-1, current_y))
                        {   
                            new_direction=3;
                            left=true;
                         }
                    }
           
        }
        else if(direction_x>0 && direction_y==0) //go right 
        {
             
               
                    if(this_world.isVisited(current_x+1, current_y)|| (current_x+1 == destination_x && current_y == destination_y))
                    {
                         if(!this_world.hasPit(current_x+1, current_y))
                        {
                            new_direction=1;
                            right=true;
                        }
                    }
           
        }
        else if(direction_x==0 && direction_y<0) //go down 
        {
            
               
                     if(this_world.isVisited(current_x, current_y-1)|| (current_x == destination_x && current_y-1 == destination_y))
                    {
                         if(!this_world.hasPit(current_x, current_y-1))
                        {
                            new_direction=2;
                            down=true;
                        }
                    }
            
        }
        else if(direction_x==0 && direction_y>0) //go up
        {
            
                
                   if(this_world.isVisited(current_x, current_y+1)|| (current_x == destination_x && current_y+1 == destination_y))
                   {
                      if(!this_world.hasPit(current_x, current_y+1))
                      {
                         new_direction=0;
                         up=true;
                      }
                  }
           
        }

        else if(direction_x>0 && direction_y>0) //go right and up 
        {
            
            
                if(!this_world.hasPit(current_x+1, current_y) && this_world.isVisited(current_x+1, current_y) )
                {
                     new_direction=1;
                     right=true;
                }
                else if(!this_world.hasPit(current_x, current_y+1) && this_world.isVisited(current_x, current_y+1))
                {
 
                       new_direction=0;
                       up=true;
                }
        }
        else if(direction_x>0 && direction_y<0) //go right and down 
        {
            
                if(!this_world.hasPit(current_x+1, current_y) && this_world.isVisited(current_x+1, current_y))
                {
                    
                        new_direction=1;
                        right=true;
                  
           
                }
            
                else if(!this_world.hasPit(current_x, current_y-1) && this_world.isVisited(current_x, current_y-1) )
                {
 
                         new_direction=2;
                         down=true;
                }
        }
        else if(direction_x<0 && direction_y<0) //go left and down 
        {
             if(!this_world.hasPit(current_x-1, current_y) && this_world.isVisited(current_x-1, current_y))//try left
             {
                     new_direction=3;
                     left=true;

             }
             else if(!this_world.hasPit(current_x, current_y-1) && this_world.isVisited(current_x, current_y-1))//try down
             {

                    new_direction=2;
                    down=true;
             }
                     
        }
         else if(direction_x<0 && direction_y>0) //go left and up
        {
             if(!this_world.hasPit(current_x-1, current_y) && this_world.isVisited(current_x-1, current_y))//try left 
             {

                    new_direction=3;
                    left=true;
             }
             else if(!this_world.hasPit(current_x, current_y+1) && this_world.isVisited(current_x, current_y+1))//try up 
             {
  
                    new_direction=2;
                    up=true;
             }
                     
        }
        if(new_direction==-1)
        {
           //remove_from_adjecent_list(adjx,adjy);

            this.remove_from_adjecent_list(destination_x, destination_y);
            rnd=this.calc_next_move(this_world);
            return rnd;
        }
        old_direction=this_world.getDirection();
        if(old_direction==0)
        {
            if(right)
            {
                rnd=3;
            }
            else if(left)
            {
                rnd=0;
            }
            else if(up)
            {
                rnd=1;
            }
            else if(down)
            {
                rnd=2;
            }
        }
        else if(old_direction==1)
        {
            if(right)
            {
                rnd=1;
            }
            else if(left)
            {
                rnd=2;
            }
            else if(up)
            {
                rnd=0;
            }
            else if(down)
            {
                rnd=3;
            }
        }
        else if(old_direction==2)
        {
            if(right)
            {
                rnd=0;
            }
            else if(left)
            {
                rnd=3;
            }
            else if(up)
            {
                rnd=2;
            }
            else if(down)
            {
                rnd=1;
            }
        }
        else if(old_direction==3)
        {
            if(right)
            {
                rnd=2;
            }
            else if(left)
            {
                rnd=1;
            }
            else if(up)
            {
                rnd=3;
            }
            else if(down)
            {
                rnd=0;
            }
        }
        
        return rnd;
    }
    public void remove_duplicates()
    {
         List<List<Integer>> templist = new ArrayList<>(new HashSet<>(Adjecent_nodes));
         Adjecent_nodes = new ArrayList<>(templist);
    }
    public void add_adjecent(int cx, int cy, World this_world)
    {
        List<Integer>temp1 = new ArrayList<>();
        List<Integer>temp2 = new ArrayList<>();
        List<Integer>temp3 = new ArrayList<>();
        List<Integer>temp4 = new ArrayList<>();
        int current_width=cx;
        int current_height=cy;
        int right=cx+1;
        int left=cx-1;
        int up=cy+1;
        int down=cy-1; 
        if(this_world.isValidPosition(current_width, up))
        {
            if(this_world.isUnknown(current_width, up))
            {
                temp1.add(current_width);
                temp1.add(up);
                Adjecent_nodes.add(temp1);
           
            }
        }
        if(this_world.isValidPosition(current_width, down))
        {
            if(this_world.isUnknown(current_width, down))
            {
                temp2.add(current_width);
                temp2.add(down);
                Adjecent_nodes.add(temp2);
            
            }
        }
        if(this_world.isValidPosition(left, current_height))
        {
            if(this_world.isUnknown(left, current_height))
            {
                temp3.add(left);
                temp3.add(current_height);
                Adjecent_nodes.add(temp3);

            }
        }
        if(this_world.isValidPosition(right, current_height))
        {
            if(this_world.isUnknown(right, current_height))
            {
                  
                temp4.add(right);
                temp4.add(current_height);
                Adjecent_nodes.add(temp4);
            }
        }
        
        remove_duplicates();
    }
    public void inc_stench()
    {
        amount_of_stench++;
   
    }
    public void inc_breeze()
    {
        amount_of_breezes++;
    }
    public void inc_pit()
    {
         breeze_to_pit++;
         amount_of_pit++;
    }
    public void inc_wumpus()
    {
       stench_to_wumpus++;
       amount_of_wumpus++;
    }
    public int get_move(World this_world)
    {
        int move=0;
        int cX = this_world.getPlayerX();
        int cY = this_world.getPlayerY();
        int direction_x=0;
        int direction_y=0;
          if(cX==destination_x && cY==destination_y)
          {
            if(!Adjecent_nodes.isEmpty())
            {
              remove_from_adjecent_list(cX,cY);
            }
             move=calc_next_move(this_world);
          }
          else
          {
             direction_x=destination_x-cX;
             direction_y=destination_y-cY;
             move=choose_direction(cX,cY,direction_x,direction_y,this_world);
             movements++;
          }

        return move;
    }
    public int calc_next_move(World this_world)
    {
        int cX = this_world.getPlayerX();
        int cY = this_world.getPlayerY();
        int gotox=0;
        int gotoy=0;
        int direction_x=0;
        int direction_y=0;
        int direction=0;
        add_adjecent(cX,cY,this_world);
        List<Integer>goto_node = new ArrayList<>(choose_next_node(cX,cY,this_world));
        gotox=goto_node.get(0);
        gotoy=goto_node.get(1);
        destination_x=gotox;
        destination_y=gotoy;
        direction_x=gotox-cX;
        direction_y=gotoy-cY;
        direction=choose_direction(cX,cY,direction_x,direction_y,this_world);
        movements++;
        return direction;
    }
    
      
}
