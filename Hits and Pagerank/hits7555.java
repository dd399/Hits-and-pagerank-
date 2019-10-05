//Dhwanil Desai CS610 7555 Hits Algo implementation created on  04/15/2019

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class hits7555 {

    public static void main(String[] args) {

        int[][] graph = null;
        int no_of_iterations = 0;
        int no_of_vertices = 0;
        int no_of_edges = 0;
        double error_rate = 0, init_value = 0;
        
        int a = Integer.parseInt(args[0]);
        if (a > 0)
            no_of_iterations = a;
        else if (a < 0)
            error_rate = (double)Math.pow(10, a);
        else
            error_rate = (double)Math.pow(10, -5);
        String file_path = args[2];
        File f = new File(file_path);
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(f));
            String[] line1 = br1.readLine().split(" ");
            no_of_vertices = Integer.parseInt(line1[0]);
            no_of_edges = Integer.parseInt(line1[1]);
            graph = new int[no_of_vertices][no_of_vertices];
            for(int i = 0; i<no_of_edges; i++) {
                String[] line = br1.readLine().split(" ");
                graph[Integer.parseInt(line[0])][Integer.parseInt(line[1])] = 1;
            }
            br1.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, wrong name or the file does not exist");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error in reading the file properly");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Getting error in generating the matrix");
            e.printStackTrace();
        }
        double bb=  Double.parseDouble(args[1]);
        if (bb >= 0)
            init_value = bb;
        else
            init_value = (double)Math.pow(graph.length, 1 / bb);

        if(no_of_vertices > 10) {
            init_value = (double)(1.0/no_of_vertices);
            no_of_iterations = 0;
            error_rate = (double)Math.pow(10, -5);
        }

        double[] scores_auth = new double[no_of_vertices];
        double[] scores_hub = new double[no_of_vertices];
        for (int i = 0; i<no_of_vertices; i++) {
            scores_auth[i] = init_value;
            scores_hub[i] = init_value;
        }


        if (no_of_iterations > 0) {
            boolean base_point = true;
            int iter = 0;
            for (int i = 0; i < no_of_iterations; i++) {
                double normalised = 0;
                if(base_point) {
                    base_point = false;
                    if(graph.length<=10) {
                        System.out.printf("Base  : %2d :",iter);
                        for(int j = 0; j<no_of_vertices; j++)
                            System.out.printf("A/H[%2d]=%.7f/%.7f ", j, scores_auth[j], scores_hub[j]);
                        System.out.println();
                    }
                }
                iter++;
                if(graph.length<=10)
                    System.out.printf("Iter  : %2d :",iter);

                // Calculating Authority Scores
                for (int row = 0; row<no_of_vertices; row++) {
                    double authorised = 0;
                    for (int col = 0; col<no_of_vertices; col++) {
                        if (graph[col][row] == 1) {
                            authorised += scores_hub[col];
                        }
                    }
                    normalised += authorised * authorised;
                    scores_auth[row] = authorised;
                }
                normalised = (double)Math.sqrt(normalised);
                for (int j = 0; j < no_of_vertices; j++) {
                    scores_auth[j] /= normalised;
                }
                normalised = 0;
                for (int row = 0; row<no_of_vertices; row++) {
                    double hub = 0;
                    for (int col = 0; col<no_of_vertices; col++) {
                        if(graph[row][col] == 1)
                            hub += scores_auth[col];
                    }
                    normalised += hub * hub;
                    scores_hub[row] = hub;
                }
                normalised = (double)Math.sqrt(normalised);
                for (int j = 0; j < no_of_vertices; j++) {
                    scores_hub[j] /= normalised;
                }
                if(graph.length <= 10) {
                    for(int j = 0; j<no_of_vertices; j++) {
                        System.out.printf("A/H[%2d]=%.7f/%.7f ", j, scores_auth[j], scores_hub[j]);
                    }
                    System.out.println();
                }
            }
            if(graph.length>10) {
                System.out.println("Iter  : "+iter);
                for(int i = 0; i<no_of_vertices; i++) {
                    System.out.printf("A/H[%2d]=%.7f/%.7f ", i, scores_auth[i], scores_hub[i]);
                    System.out.println();
                }
            }
        }
        else {
            double rate_current = 1;
            int iter = 0;
            boolean flag = true, base = true;
            while (rate_current >= error_rate) {
                double[] scores_oldauth = scores_auth.clone();
                double[] scores_oldhub = scores_hub.clone();

                double normalised = 0;
                if(base) {
                    base = false;
                    if(graph.length<=10) {
                        System.out.printf("Base  : %2d :",iter);
                        for(int j = 0; j<no_of_vertices; j++)
                            System.out.printf("A/H[%2d]=%.7f/%.7f ", j, scores_auth[j], scores_hub[j]);
                        System.out.println();
                    }
                }
                iter++;
                if(graph.length<=10) {
                    System.out.printf("Iter  : %2d :",iter);
                }


                for (int row = 0; row<no_of_vertices; row++) {
                    double authority = 0;
                    for (int col = 0; col<no_of_vertices; col++) {

                        if (graph[col][row] == 1) {
                            authority += scores_hub[col];
                        }
                    }
                    normalised += authority * authority;
                    scores_auth[row] = authority;
                }
                normalised = (double)Math.sqrt(normalised);
                for (int j = 0; j < no_of_vertices; j++) {
                    scores_auth[j] /= normalised;
                }

                for (int i = 0; i<no_of_vertices; i++) {
                    if(flag==true) {
                        flag=false;
                        rate_current = Math.abs(scores_auth[i] - scores_oldauth[i]);
                    }
                    else {
                        if(rate_current < (Math.abs(scores_auth[i] - scores_oldauth[i])))
                            rate_current = Math.abs(scores_auth[i] - scores_oldauth[i]);
                    }
                }
                normalised = 0;
                for (int row = 0; row<no_of_vertices; row++) {
                    double hub = 0;
                    for (int col = 0; col<no_of_vertices; col++) {
                        if(graph[row][col] == 1)
                            hub += scores_auth[col];
                    }
                    normalised += hub * hub;
                    scores_hub[row] = hub;
                }
                normalised = (double)Math.sqrt(normalised);
                for (int j = 0; j < no_of_vertices; j++) {
                    scores_hub[j] /= normalised;
                }
                if(graph.length <= 10) {
                    for(int j = 0; j<no_of_vertices; j++) {
                        System.out.printf("A/H[%2d]=%.7f/%.7f ", j, scores_auth[j], scores_hub[j]);
                    }
                    System.out.println();
                }
                for (int i = 0; i<no_of_vertices; i++) {
                    if(flag==true) {
                        flag=false;
                        rate_current = Math.abs(scores_hub[i] - scores_oldhub[i]);
                    }
                    else {
                        if(rate_current < (Math.abs(scores_hub[i] - scores_oldhub[i])))
                            rate_current = Math.abs(scores_hub[i] - scores_oldhub[i]);
                    }
                }
                flag = true;
            }
            if(graph.length>10) {
                System.out.println("Iter  : "+iter);
                for(int i = 0; i<no_of_vertices; i++) {
                    System.out.printf("A/H[%2d]=%.7f/%.7f", i, scores_auth[i], scores_hub[i]);
                    System.out.println();
                }
            }
        }
    }

}
