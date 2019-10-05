// Dhwanil Desai CS610 7555 PRP Pagerank Algo created on  04/15/2019

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class pgrk7555 {

    public static void main(String[] args) {
        int[][] graph = null;
        int iterations = 0;             //Number of iterations
        int no_of_vertices = 0;       // number of no_of_vertices
        int no_of_edges = 0;          // number of no_of_edges
        double error_rate = 0, init_value = 0;
        double constant_d = 0.85;



        int a = Integer.parseInt(args[0]);
        if (a > 0)
            iterations = a;
        else if (a < 0)
            error_rate = (double)Math.pow(10, a);
        else
            error_rate = (double)Math.pow(10, -5);
        String filepath = args[2];
        File f = new File(filepath);
        try {
            BufferedReader b_read = new BufferedReader(new FileReader(f));
            String[] line1 = b_read.readLine().split(" ");
            no_of_vertices = Integer.parseInt(line1[0]);
            no_of_edges = Integer.parseInt(line1[1]);
            graph = new int[no_of_vertices][no_of_vertices];
            for (int i = 0; i < no_of_edges; i++) {
                String[] line = b_read.readLine().split(" ");
                graph[Integer.parseInt(line[0])][Integer.parseInt(line[1])] = 1;
            }
            b_read.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, wrong name or the file does not exist.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading the file properly");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Getting error in generating the matrix");
            e.printStackTrace();
        }

        double bb = Double.parseDouble(args[1]);
        if (bb >= 0)
            init_value = bb;
        else
            init_value = (double)Math.pow(graph.length, 1 / bb);

        if(no_of_vertices > 10) {
            init_value = 1.0/no_of_vertices;
            iterations = 0;
            error_rate = (double)Math.pow(10, -5);
        }

        
        double[] page_rank = new double[no_of_vertices];
        for(int i = 0; i<no_of_vertices; i++) {
            page_rank[i] = init_value;
        }

        
        double[] cost_vect = new double[no_of_vertices];
        for(int row = 0; row<no_of_vertices; row++) {
            for(int column = 0; column<no_of_vertices; column++) {
                if(graph[row][column] == 1)
                    cost_vect[row]++;
            }
        }
        if(iterations>0) {
            boolean base = true;
            int iter = 0;
            for(int i=0; i<iterations; i++) {
                double[] page_rank_old  = page_rank.clone();
                if(base) {
                    base = false;
                    if(graph.length<=10) {
                        System.out.printf("Base  : %2d :",iter);
                        for(int j = 0; j<no_of_vertices; j++)
                            System.out.printf("P[%2d]=%.7f ",j, page_rank[j]);
                        System.out.println();
                    }
                }
                iter++;
                if(graph.length<=10)
                    System.out.printf("Iter  : %2d :",iter);

                
                for(int row = 0; row<no_of_vertices; row++) {
                    double temp = 0;
                    for(int col = 0; col<no_of_vertices; col++) {
                        if(graph[col][row]==1) {
                            temp += (page_rank_old[col] / cost_vect[col]);
                        }
                    }
                    temp = (1-constant_d) / no_of_vertices + (constant_d*temp);
                    page_rank[row] = temp;
                }
                if(base == false) {
                    if(graph.length<=10) {
                        for(int j = 0; j<no_of_vertices; j++)
                            System.out.printf("P[%2d]=%.7f ",j, page_rank[j]);
                        System.out.println();
                    }
                }
            }
            if(graph.length>10) {
                System.out.println("Iter  : "+iter);
                for(int j = 0; j<no_of_vertices; j++) {
                    System.out.printf("P[%2d]=%.7f",j, page_rank[j]);
                    System.out.println();
                }
            }
        }
        else {
            boolean base = true;
            double current_errorrate = 1;
            boolean flag = true;
            int iter = 0;
            while(current_errorrate >= error_rate) {
                double[] pgRnkOld  = page_rank.clone();
                if(base == true) {
                    base = false;
                    if(graph.length<=10) {
                        System.out.printf("Base  : %2d :",iter);
                        for(int j = 0; j<no_of_vertices; j++)
                            System.out.printf("P[%2d]=%.7f ",j, page_rank[j]);
                        System.out.println();
                    }
                }
                iter++;
                if(graph.length<=10)
                    System.out.printf("Iter  : %2d :",iter);

                //Calculating PageRank
                for(int row = 0; row<no_of_vertices; row++) {
                    double temp = 0;
                    for(int col = 0; col<no_of_vertices; col++) {
                        if(graph[col][row]==1) {
                            temp += (pgRnkOld[col] / cost_vect[col]);
                        }
                    }
                    temp = (1-constant_d) / no_of_vertices + (constant_d*temp);
                    page_rank[row] = temp;
                }
                if(base == false) {
                    if(graph.length<=10) {
                        for(int j = 0; j<no_of_vertices; j++)
                            System.out.printf("P[%2d]=%.7f ",j, page_rank[j]);
                        System.out.println();
                    }
                }

                for(int j = 0; j<no_of_vertices; j++) {
                    if(flag == true) {
                        flag = false;
                        current_errorrate = Math.abs(page_rank[j] - pgRnkOld[j]);
                    }
                    else {
                        if(current_errorrate < Math.abs(page_rank[j] - pgRnkOld[j]))
                            current_errorrate = Math.abs(page_rank[j] - pgRnkOld[j]);
                    }
                }
                flag = true;
            }
            if(graph.length>10) {
                System.out.println("Iter  : "+iter);
                for(int j = 0; j<no_of_vertices; j++) {
                    System.out.printf("P[%2d]=%.7f",j, page_rank[j]);
                    System.out.println();
                }
            }
        }

    }
}
