package rizalalfarizi1600807.cs.upi.edu.simplextwofase.Util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import rizalalfarizi1600807.cs.upi.edu.simplextwofase.HitungActivity;

public class Simplex {

    private double[][] tableaux; // tableaux
    private int numberOfConstraints; // number of constraints
    private int numberOfOriginalVariables; // number of original variables

    private boolean maximizeOrMinimize;

    public static final boolean MAXIMIZE = true;
    public static final boolean MINIMIZE = false;
    private Context ctx;
    private LinearLayout wrapper;

    private int[] basis; // basis[i] = basic variable corresponding to row i

    public Simplex(double[][] tableaux, int numberOfConstraint, int numberOfOriginalVariable, boolean maximizeOrMinimize, LinearLayout wrapper, Context ctx) {
        this.maximizeOrMinimize = maximizeOrMinimize;
        this.numberOfConstraints = numberOfConstraint;
        this.numberOfOriginalVariables = numberOfOriginalVariable;
        this.tableaux = tableaux;

        basis = new int[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++)
            basis[i] = numberOfOriginalVariables + i;
        this.ctx = ctx;
        this.wrapper = wrapper;
        solve();

    }

    // run simplex algorithm starting from initial BFS
    private void solve() {
        while (true) {

            show();
            int q = 0;
            // find entering column q
            if (maximizeOrMinimize) {
                q = dantzig();
            } else {
                q = dantzigNegative();
            }
            if (q == -1)
                break; // optimal

            // find leaving row p
            int p = minRatioRule(q);
            if (p == -1)
                throw new ArithmeticException("Linear program is unbounded");

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;

        }
    }

    // index of a non-basic column with most positive cost
    private int dantzig() {
        int q = 0;
        for (int j = 1; j < numberOfConstraints + numberOfOriginalVariables; j++)
            if (tableaux[numberOfConstraints][j] > tableaux[numberOfConstraints][q])
                q = j;

        if (tableaux[numberOfConstraints][q] <= 0)
            return -1; // optimal
        else
            return q;
    }

    // index of a non-basic column with most negative cost
    private int dantzigNegative() {
        int q = 0;
        for (int j = 1; j < numberOfConstraints + numberOfOriginalVariables; j++)
            if (tableaux[numberOfConstraints][j] < tableaux[numberOfConstraints][q])
                q = j;

        if (tableaux[numberOfConstraints][q] >= 0)
            return -1; // optimal
        else
            return q;
    }

    // find row p using min ratio rule (-1 if no such row)
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < numberOfConstraints; i++) {
            if (tableaux[i][q] <= 0)
                continue;
            else if (p == -1)
                p = i;
            else if ((tableaux[i][numberOfConstraints
                    + numberOfOriginalVariables] / tableaux[i][q]) < (tableaux[p][numberOfConstraints
                    + numberOfOriginalVariables] / tableaux[p][q]))
                p = i;
        }
        return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        // everything but row p and column q
        for (int i = 0; i <= numberOfConstraints; i++)
            for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++)
                if (i != p && j != q)
                    tableaux[i][j] -= tableaux[p][j] * tableaux[i][q] / tableaux[p][q];

        // zero out column q
        for (int i = 0; i <= numberOfConstraints; i++)
            if (i != p)
                tableaux[i][q] = 0.0;

        // scale row p
        for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++)
            if (j != q)
                tableaux[p][j] /= tableaux[p][q];
        tableaux[p][q] = 1.0;
    }

    // return optimal objective value
    public double value() {
        return -tableaux[numberOfConstraints][numberOfConstraints + numberOfOriginalVariables];
    }

    // return primal solution vector
    public double[] primal() {
        double[] x = new double[numberOfOriginalVariables];
        for (int i = 0; i < numberOfConstraints; i++)
            if (basis[i] < numberOfOriginalVariables)
                x[basis[i]] = tableaux[i][numberOfConstraints + numberOfOriginalVariables];
        return x;
    }

    // print tableaux
    public void show() {
        Resources r = ctx.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,r.getDisplayMetrics());

        System.out.println("M = " + numberOfConstraints);
        System.out.println("N = " + numberOfOriginalVariables);

        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableParams.setMargins(0,px,0,0);


        TableLayout table = new TableLayout(ctx);
        table.setStretchAllColumns(true);
        table.setLayoutParams(tableParams);

        //TABLE HEAD
        TableRow trHead = new TableRow(ctx);
        trHead.setPadding(0,0,0,0);
        trHead.setLayoutParams(trParams);
        for (int i = 1; i <= numberOfOriginalVariables; i++){
            TextView th = new TextView(ctx);
            th.setTypeface(Typeface.DEFAULT_BOLD);
            th.setTextColor(Color.rgb(0,0,0));
            th.setText("x"+i);
            th.setGravity(Gravity.CENTER);
            trHead.addView(th);
        }

        for (int i = 1; i <= numberOfConstraints; i++){
            TextView th = new TextView(ctx);
            th.setTypeface(Typeface.DEFAULT_BOLD);
            th.setTextColor(Color.rgb(250, 97, 33));
            th.setText("s"+i);
            th.setGravity(Gravity.CENTER);
            trHead.addView(th);
        }

        TextView th = new TextView(ctx);
        th.setTypeface(Typeface.DEFAULT_BOLD);
        th.setTextColor(Color.rgb(70, 98, 137));
        th.setText("Nilai");
        th.setGravity(Gravity.CENTER);
        trHead.addView(th);

        trHead.setLayoutParams(trParams);
        table.addView(trHead);


        for (int i = 0; i <= numberOfConstraints; i++) {
            TableRow tr = new TableRow(ctx);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);

            for (int j = 0; j <= numberOfConstraints + numberOfOriginalVariables; j++) {
                System.out.printf("%7.2f ", tableaux[i][j]);
                TextView tv = new TextView(ctx);
                tv.setText(String.format("%7.2f",tableaux[i][j]));
                tv.setTextColor(Color.rgb(0,0,0));
                tr.addView(tv);
            }
            table.addView(tr, trParams);
            System.out.println();
        }

        wrapper.addView(table);

        System.out.println("value = " + value());
        for (int i = 0; i < numberOfConstraints; i++)
            if (basis[i] < numberOfOriginalVariables)
                System.out.println("x_"
                        + basis[i]
                        + " = "
                        + tableaux[i][numberOfConstraints
                        + numberOfOriginalVariables]);
        System.out.println();
    }

    // test client
    public static void main(String[] args) {


        double[] objectiveFunc = { 3, 2 };
        double[][] constraintLeftSide = {

                { 4, 3 }, { 4, 1 }, { 4, -1} };
        HitungActivity.Constraint[] constraintOperator = { HitungActivity.Constraint.lessThan, HitungActivity.Constraint.lessThan, HitungActivity.Constraint.lessThan};
        double[] constraintRightSide = { 12, 8, 8 };

        Modeler model = new Modeler(constraintLeftSide, constraintRightSide,constraintOperator, objectiveFunc);

        Simplex simplex = new Simplex(model.getTableaux(), model.getNumberOfConstraint(), model.getNumberOfOriginalVariable(), MAXIMIZE,null,null);
        double[] x = simplex.primal();
        for (int i = 0; i < x.length; i++)
            System.out.println("x[" + i + "] = " + x[i]);
        System.out.println("Solution: " + simplex.value());
    }



    public static class Modeler {
        private double[][] a; // tableaux
        private int numberOfConstraints; // number of constraints
        private int numberOfOriginalVariables; // number of original variables

        public Modeler(double[][] constraintLeftSide,
                       double[] constraintRightSide, HitungActivity.Constraint[] constraintOperator,
                       double[] objectiveFunction) {
            numberOfConstraints = constraintRightSide.length;
            numberOfOriginalVariables = objectiveFunction.length;
            a = new double[numberOfConstraints + 1][numberOfOriginalVariables + numberOfConstraints + 1];

            // initialize constraint
            for (int i = 0; i < numberOfConstraints; i++) {
                for (int j = 0; j < numberOfOriginalVariables; j++) {
                    a[i][j] = constraintLeftSide[i][j];
                }
            }

            for (int i = 0; i < numberOfConstraints; i++)
                a[i][numberOfConstraints + numberOfOriginalVariables] = constraintRightSide[i];

            // initialize slack variable
            for (int i = 0; i < numberOfConstraints; i++) {
                int slack = 0;
                switch (constraintOperator[i]) {
                    case greatherThan:
                        slack = -1;
                        break;
                    case lessThan:
                        slack = 1;
                        break;
                    default:
                }
                a[i][numberOfOriginalVariables + i] = slack;
            }

            // initialize objective function
            for (int j = 0; j < numberOfOriginalVariables; j++)
                a[numberOfConstraints][j] = objectiveFunction[j];
        }

        public double[][] getTableaux() {
            return a;
        }

        public int getNumberOfConstraint() {
            return numberOfConstraints;
        }

        public int getNumberOfOriginalVariable() {
            return numberOfOriginalVariables;
        }
    }


}