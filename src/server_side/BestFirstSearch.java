package server_side;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class BestFirstSearch implements Searcher
{

    @Override
    public String FindRoute(Searchable s,String start,String end)
    {
        PriorityQueue<State> open = new PriorityQueue<State>(s.HowMany(), new RouteComparator());
        List<State> closed=new ArrayList<State>();
        open.add(new State(start,s.PriceTo(start)));
        while (!open.isEmpty())
        {
            State n=open.poll();
            closed.add(n);
            if(n.GetState().equals(end))
            {
                String fathers="";
                fathers+=n.GetState()+",";
                while(n.GetCameFrom()!=null)
                {
                    n=n.GetCameFrom();
                    fathers+=n.GetState()+",";
                }
                return IndexToDir(fathers);
            }
            List<String> sucList=s.Successors(n.GetState());
            for (String hooga : sucList)
            {
                State currentN=new State(hooga,n.GetCost()+s.PriceTo(hooga));
                currentN.SetCameFrom(n);
                if((!closed.contains(currentN))&&(!open.contains(currentN)))
                {
                    open.add(currentN);
                }
                else if(!closed.contains(currentN))
                {
                    Iterator<State> it = open.iterator();
                    while (it.hasNext())
                    {
                        State checkingState=it.next();
                        if(checkingState.equals(currentN))
                        {
                            if(currentN.GetCost()<checkingState.GetCost())
                            {
                                open.remove(currentN);
                                open.add(currentN);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String IndexToDir(String fathers)
    {
        fathers=fathers.substring(0, fathers.length() - 1);
        fathers=new StringBuilder(fathers).reverse().toString();
        String[] parts = fathers.split(",");
        String answer="";
        for (int i = 0; i < parts.length-3; i=i+2)
        {
            //System.out.println(parts[i+1]+","+parts[i]);
            int childRow=Integer.parseInt(parts[i+3]);
            int childCol=Integer.parseInt(parts[i+2]);
            int fatherRow=Integer.parseInt(parts[i+1]);
            int fatherCol=Integer.parseInt(parts[i]);
            if(fatherRow>childRow) {answer+="Up";}
            else if(fatherRow<childRow) {answer+="Down";}
            else if(fatherCol>childCol) {answer+="Left";}
            else {answer+="Right";}
            answer+=",";
        }
        answer=answer.substring(0, answer.length() - 1);
        return answer;
    }
}

class RouteComparator implements Comparator<State>{

    public int compare(State s1, State s2)
    {
        if (s1.GetCost() > s2.GetCost())
            return 1;
        else if (s1.GetCost() < s2.GetCost())
            return -1;
        return 0;
    }
}
