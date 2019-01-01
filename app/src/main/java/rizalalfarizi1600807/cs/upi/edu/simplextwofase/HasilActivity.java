package rizalalfarizi1600807.cs.upi.edu.simplextwofase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TableLayout;


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
    }

    private double[][] constraintLeftSide;
    private double[] constraintRightSide;
    private HitungActivity.Constraint[] constraintOperator;
    private double[] objectiveFunction;

    public void convertToArray(){
        objectiveFunction = new double[bahan.variable.size()];
        constraintOperator = new HitungActivity.Constraint[bahan.operator.size()];
        constraintRightSide = new double[bahan.valConst.size()];
        constraintLeftSide = new double[bahan.listConst.size()][bahan.variable.size()];

        //objective
        int i = 0;
        for (double d:bahan.variable) {
            objectiveFunction[i] = d;
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
        Simplex.Modeler model = new Simplex.Modeler(constraintLeftSide, constraintRightSide,constraintOperator, objectiveFunction);

        Simplex simplex = new Simplex(model.getTableaux(),model.getNumberOfConstraint(),model.getNumberOfOriginalVariable(), MAXIMIZE,tableFase1);
        double[] x = simplex.primal();
        for (int i = 0; i < x.length; i++){

            System.out.println("x[" + i + "] = " + x[i]);
        }

        System.out.println("Solution: " + simplex.value());
    }
}
