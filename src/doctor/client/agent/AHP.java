package doctor.client.agent;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AHP
{
	 ArrayList<int[]> switchers;
	
public double[][] initialize_matrix(double[] p, boolean[] cb, int n)
{
    //initialize the matrix a
    //double a[][]=new double[p.length][p.length];    
    double a[][]=new double[n][n];    
    int k=0;     
  
    switchers = new ArrayList<int[]>();
    
    for(int i=0; i<n; i++)
    {
        for(int j=0; j<n;j++)
        {
            if(i==j)
                a[i][j]=1;
            else if(i<j)
            {

                a[i][j]=p[k];
                if (cb[k] == true){
                	int temp [] = new int[2];
                	temp[0]=i;
                	temp[1]=j;
                	switchers.add(temp);
                }
                
                k++;
            }

            else if(i>j)
                a[i][j]=1/a[j][i];
        }
    }
    return a;
}


public double[][] performSwitch(double[][] a,int n){
	for(int [] coord: this.switchers){
		double tmp;
		tmp = a[coord[0]][coord[1]]; 
		a[coord[0]][coord[1]] = a[coord[1]][coord[0]];
		a[coord[1]][coord[0]] = tmp;
	}
	return a;
}

public double calculateCR(double [][]a, double [] b,int n){
	double cr=0;
	double sum = 0;
	double sumEigenValue=0;
	double ci=0;
	//Remember that n starts from 1 and not 0 when processing this array
	double [] ri={0,0,0.58,0.9,1.12,1.24,1.32,1.41,1.45,1.49};
	double [] columnSum = new double[b.length];
	for(int i=0;i<n;i++){
		for(int j=0;j<n;j++){
			sum = sum + a[j][i];
			
		}//End inner loop
		columnSum[i]=sum;
		System.out.println("Column "+i+" Sum: "+sum);
		sum=0;
	}//End outer loop
	
	for(int i=0;i<b.length;i++){
		sumEigenValue = sumEigenValue + (b[i] * columnSum[i]);
	}
	System.out.println("Sum Eigenvalue: "+sumEigenValue);
	System.out.println("Value on n is : "+n);
	ci = (sumEigenValue - n)/(n-1);
	
	System.out.println("Consistency Index: "+ci);
	cr = ci/ri[n-1];
	return cr;
}
 
public ArrayList<int []> getSwitcher(){
	return this.switchers;
}

public  double[][] mult_matrix(double[][] a, double[][] b)
{
   int rowsInA = a.length;
   int columnsInA = a[0].length; // has to be same as row in B
   int columnsInB = b.length;
   double c[][]=new double[rowsInA][columnsInB];  
   for(int i=0; i<rowsInA; i++){
        for(int j=0; j<columnsInB; j++){
            for(int k=0; k<columnsInA; k++){
                c[i][j] = c[i][j] + a[i][k] * b[k][j];
            }
        }
   }
    return c;
}

public  double[] mult_matrix2(double[][] a, double[]b){
    int rowSize = a.length;
    int colSize = a[0].length;
    double[] c = new double[rowSize];
    for(int i=0;i<rowSize;i++){
        for(int j=0;j<colSize;j++){
            c[i] = c[i] + a[i][j] * b[j];
        }
    }
    return c;
}


public  double[] cal_eigenvector(double[][] a){
    int rowsInA = a.length;
    int columnsInA = a[0].length;
    double sum_rows[] = new double[rowsInA];
    double final_results[] = new double[rowsInA];
    double total = 0;

    for(int i=0; i < rowsInA; i++){
        double sum = 0;
        for(int j=0; j < columnsInA; j++){
            double e = sum + a[i][j];
            sum = e;
        }
        sum_rows[i] = sum;
    }

    for(int i=0; i<sum_rows.length; i++){
       double f = total + sum_rows[i];
       total = f;
    }

    for(int i=0; i<sum_rows.length;i++){
        final_results[i] = sum_rows[i]/total;
    }
    return final_results;
}


public  void show_matrix(double[][] b )
{
    //display the elements of the matrix a
    System.out.println("\nThe matrix a is:");
    for(int i=0; i<b.length;i++)
    {
        for(int j=0; j<b[i].length; j++)
            System.out.printf("%.4f   ",b[i][j]);
        System.out.println();   
    }
}



public void show_array(double[] a){
    //display array elements
    System.out.println("\n The elements of the array is:");
    for(double i: a){
            System.out.printf("%.4f",i);
            System.out.println();
    }
}


public  double[][] convertTo2D(double[] a){
double [][] b = new double[a.length][1];
    for(int i=0;i<a.length;i++){
        b[i][0] = a[i];
    }
    return b;
}

//method to convert to 2 decimal places
private  double roundDecimal(double d){
    double db=0.0;
    DecimalFormat df = new DecimalFormat("#,##0.00");
    try{
            db = Double.valueOf(df.format(d));
    }catch(NumberFormatException e){
        e.printStackTrace();
    }
    return db;
}

}
