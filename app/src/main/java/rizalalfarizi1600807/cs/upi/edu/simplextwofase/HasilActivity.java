package rizalalfarizi1600807.cs.upi.edu.simplextwofase;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import Jama.Matrix;


import java.util.ArrayList;

import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model.CalcWrapper;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model.LeftConst;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Util.Simplex;

import static rizalalfarizi1600807.cs.upi.edu.simplextwofase.Util.Simplex.MAXIMIZE;

public class HasilActivity extends AppCompatActivity {
    public static final String EXTRA_BAHAN = "extra_bahan";
    private Toolbar toolbar;
    private CalcWrapper bahan;
    private TableLayout tableFase1;
    private LinearLayout fase1Wrapper,fase2Wrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();

        bahan = getIntent().getParcelableExtra(EXTRA_BAHAN);
        convertToArray();
        count();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }


    public void initUI(){
        tableFase1 = (TableLayout) findViewById(R.id.table_fase1);
        tableFase1.setStretchAllColumns(true);

        fase1Wrapper = (LinearLayout) findViewById(R.id.fase1_wrapper);
        fase2Wrapper = (LinearLayout) findViewById(R.id.fase2_wrapper);

    }

    private double[][] constraintLeftSide;
    private double[] constraintRightSide;
    private HitungActivity.Constraint[] constraintOperator;
    private double[] objectiveFunction;
    private double[] oriZ;

    public void convertToArray(){
        objectiveFunction = new double[bahan.variable.size()];
        constraintOperator = new HitungActivity.Constraint[bahan.operator.size()];
        constraintRightSide = new double[bahan.valConst.size()];
        constraintLeftSide = new double[bahan.listConst.size()][bahan.variable.size()];
        oriZ = new double[bahan.variable.size()];;
        //objective
        int i = 0;
        for (double d:bahan.variable) {
            objectiveFunction[i] = d;
            oriZ[i] = d;
            i++;
        }

        //right
        i = 0;
        for (double d:bahan.valConst) {
            constraintRightSide[i] = d;
            i++;
        }

        //operator
        i = 0;
        for (HitungActivity.Constraint d:bahan.operator) {
            constraintOperator[i] = d;
            i++;
        }


        //leftside
        i = 0;
        for (LeftConst l:bahan.listConst) {
            int j=0;
            for (double d: l.leftConst) {
                constraintLeftSide[i][j] = d;
                j++;
            }
            i++;
        }



    }

    public void count(){
        boolean opt = false;
        if(bahan.option.equals("MAX")){
            opt = true;
        }else{
            opt = false;
        }

        tableFase1.removeAllViews();


        //FASE 1
        double[] Rprog= findR();
        for (int i =0; i<objectiveFunction.length;i++){
            objectiveFunction[i] = Rprog[i];
        }
        double Rval = Rprog[Rprog.length-1];

        Simplex.Modeler model = new Simplex.Modeler(constraintLeftSide, constraintRightSide,constraintOperator, objectiveFunction,Rval);

        Simplex fase1 = new Simplex(model.getTableaux(),model.getNumberOfConstraint(),model.getNumberOfOriginalVariable(), opt, fase1Wrapper, getApplicationContext(), 1);
        //TIDAK ADA SOLUSI
        if(fase1.value()!=0){
            Toast.makeText(this,"Solusi tidak fisibel",Toast.LENGTH_SHORT).show();
            fase2Wrapper.setVisibility(View.INVISIBLE);
        }else{
            //fase2
            Simplex.Modeler model2 = new Simplex.Modeler(constraintLeftSide, constraintRightSide,constraintOperator, oriZ,0);
            Simplex fase2 = new Simplex(model2.getTableaux(),model.getNumberOfConstraint(),model.getNumberOfOriginalVariable(), opt, fase2Wrapper, getApplicationContext(), 0);


            double[] x = fase2.primal();

            TextView br = new TextView(this);
            br.setPadding(0,20,0,0);
            br.setText("Hasil dari persoalan: ");
            fase2Wrapper.addView(br);

            int index = 1;
            for (int i = 0; i < x.length; i++){
                TextView tvHasil = new TextView(this);
                tvHasil.setText("x " + index + " = " + x[i]);
                tvHasil.setTextColor(Color.parseColor("#000000"));
                index++;
                fase2Wrapper.addView(tvHasil);
            }

            TextView solution = new TextView(this);
            solution.setTextColor(Color.parseColor("#000000"));
            solution.setText("Z = " + fase2.value());
            fase2Wrapper.addView(solution);

            //FASE 2
            double[][] fase2table = fase1.getLastTable();

            double[][] lhs = new double[model.getNumberOfConstraint()][model.getNumberOfOriginalVariable()+model.getNumberOfConstraint()];
            double[] rhs = new double[model.getNumberOfConstraint()];

            for (int row=0;row<model.getNumberOfConstraint();row++){
                for (int col=0;col<model.getNumberOfOriginalVariable()+model.getNumberOfConstraint();col++){
                    lhs[row][col] = fase2table[row][col];

                }
            }

            for (int row=0;row<model.getNumberOfConstraint();row++){
                rhs[row] = fase2table[row][model.getNumberOfConstraint()+model.getNumberOfOriginalVariable()];
            }

            ArrayList<Integer> deletedIndex = removeZero(lhs);

            double[][] finallhs = new double[model.getNumberOfConstraint()][model.getNumberOfOriginalVariable()+model.getNumberOfConstraint() - deletedIndex.size()];
            for (int row=0;row<model.getNumberOfConstraint();row++){
                int realIn = 0;
                for (int col=0;col<model.getNumberOfOriginalVariable() + model.getNumberOfConstraint();col++){
                    if(!deletedIndex.contains(col)) {
                        finallhs[row][realIn] = fase2table[row][col];
                        realIn++;
                    }

                }
            }


            //Creating Matrix Objects with arrays
            Matrix lmatrix = new Matrix(finallhs);
            Matrix rmatrix = new Matrix(rhs,3);

            //Calculate Solved Matrix
            //Matrix answ = lmatrix.solve(rmatrix);


            double[] Zprog = findZ(oriZ,fase1.getLastTable(),model.getNumberOfOriginalVariable(),model.getNumberOfConstraint());
            fase2table[model.getNumberOfConstraint()] = Zprog;
//        Simplex fase2 = new Simplex(fase2table,model.getNumberOfConstraint(),model.getNumberOfOriginalVariable(), opt, fase2Wrapper, getApplicationContext(), 0);


        }



    }

    public double[] findR(){
        double[] objectiveFunctionWithValue;
        objectiveFunctionWithValue = new double[bahan.variable.size()+1];

        ArrayList<Integer> indexHasR = new ArrayList<>();
        for (int i=0; i<constraintOperator.length;i++){
            if(constraintOperator[i].equals(HitungActivity.Constraint.equal) || constraintOperator[i].equals(HitungActivity.Constraint.greatherThan)){
                indexHasR.add(i);
            }
        }

        for (int i:indexHasR) {
            for(int j=0;j<bahan.variable.size();j++){
                objectiveFunctionWithValue[j] += constraintLeftSide[i][j];
            }
            objectiveFunctionWithValue[objectiveFunctionWithValue.length-1] += constraintRightSide[i];
        }

        return  objectiveFunctionWithValue;
    }

    public double[] findZ(double[] originalZ, double[][] table, int numOfVar, int numOfConst){
        double[] res = new double[numOfVar+numOfConst+1];
        double[][] tempTable = table;

        for (int i= 0; i<originalZ.length;i++){
            double x = originalZ[i];
            for (int row=0;row<numOfConst;row++){
                if(table[row][i]==1){
                    //multiple with x
                    for(int col=numOfVar;col<=numOfConst+numOfVar;col++){
                        double hasil = x * table[row][col];
                        res[col]+=hasil;
                    }
                }
            }
        }

        return res;

    }

    public ArrayList<Integer> removeZero(double[][] arr){
        ArrayList<Integer> indexDelete = new ArrayList<>();

        int numRow = arr.length;
        int numCol = arr[0].length;
        boolean stat;
        for (int i=0;i<numCol;i++){
            stat=false;
            for(int j=0;j<numRow;j++){
                if(arr[j][i]!=0){
                    stat = true;
                }
            }
            if(!stat) indexDelete.add(i);
        }

        return indexDelete;
    }
}
