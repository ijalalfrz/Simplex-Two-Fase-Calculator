package rizalalfarizi1600807.cs.upi.edu.simplextwofase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model.Calc;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model.CalcWrapper;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model.LeftConst;

public class HitungActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String EXTRA_OPTION = "extra_option";
    private Button btnHitung;
    private Toolbar toolbar;
    private TextView tvOption;
    private LinearLayout zWrapper,consWrapper;
    private ArrayList<EditText> etVar;
    private ArrayList<ArrayList<EditText>> leftConst;
    private ArrayList<EditText> rightConst;
    private Constraint[] defaultOperator;
    private ArrayList<String> spinnerArray;
    private Calc calc;
    public static enum Constraint {
        lessThan, equal, greatherThan
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitung);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calc = getIntent().getParcelableExtra(EXTRA_OPTION);
        initUI();

        etVar = new ArrayList<>();
        leftConst = new ArrayList<>();
        rightConst = new ArrayList<>();

        addVar(calc.var);
        addCons(calc.var,calc.cons);

        if(calc.option.equals("MAX")){
            tvOption.setText("Maximize");
        }else{
            tvOption.setText("Minimize");
        }
        defaultOperator = new Constraint[calc.cons];

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void initUI(){
        spinnerArray = new ArrayList<String>();
        spinnerArray.add("<=");
        spinnerArray.add("=");
        spinnerArray.add(">=");

        btnHitung = (Button) findViewById(R.id.btn_hitung);
        zWrapper = (LinearLayout) findViewById(R.id.z_wrapper);
        consWrapper = (LinearLayout) findViewById(R.id.cons_wrapper);
        tvOption = (TextView) findViewById(R.id.option);

        btnHitung.setOnClickListener(this);


    }

    public void addVar(int num){
        int var = 1;
        for(int i=0; i<num; i++){
            EditText et = new EditText(this);
            etVar.add(et);
            et.setWidth(100);
            et.setTag("var-"+i);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

            zWrapper.addView(et);
            TextView tv = new TextView(this);
            if(i==num-1){
                tv.setText("x"+var);

            }else{

                tv.setText("x"+var+" +");
            }
            zWrapper.addView(tv);
            var++;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addCons(int numVar, int numCons ){
        int baris = 0;
        for (int i=0;i<numCons;i++){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout li = new LinearLayout(this);
            li.setOrientation(LinearLayout.HORIZONTAL);
            int var = 1;
            ArrayList<EditText> temp  = new ArrayList<>();
            for (int j=0;j<numVar;j++){
                EditText et = new EditText(this);
                temp.add(et);
                et.setWidth(100);
                et.setTag("cons-"+baris);
                et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                li.addView(et);
                TextView tv = new TextView(this);
                if(j==numVar-1){
                    tv.setText("x"+var);


                }else{
                    tv.setText("x"+var+" +");
                }
                li.addView(tv);
                var++;
            }
            //OPERATOR
            Spinner spinner = new Spinner(this);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setTag("opt-"+baris);

            spinner.setDropDownWidth(150);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getTag()!=null){

                        int index = Integer.parseInt(parent.getTag().toString().replace("opt-",""));
                        String selectedItemText = (String) parent.getItemAtPosition(position);

                        if(selectedItemText.equals("<=")){
                            defaultOperator[index] = Constraint.lessThan;
                        }else if(selectedItemText.equals("=")){
                            defaultOperator[index] = Constraint.equal;
                        }else{
                            defaultOperator[index] = Constraint.greatherThan;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            li.addView(spinner);

            //VALUES
            EditText et = new EditText(this);
            rightConst.add(et);
            et.setWidth(100);
            et.setTag("val-"+baris);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            li.addView(et);

            leftConst.add(temp);
            consWrapper.addView(li);
            baris++;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_hitung){
            CalcWrapper all = convertValue();
        }
    }

    public CalcWrapper convertValue(){
        CalcWrapper wrapper = new CalcWrapper();
        boolean err = false;

        for (EditText et: etVar) {
            if( TextUtils.isEmpty(et.getText())){
                err = true;
            }else{
                double d = Double.parseDouble(et.getText().toString());
                wrapper.variable.add(d);
            }

        }
        for (ArrayList<EditText> etList: leftConst) {
            LeftConst left = new LeftConst();
            for (EditText et: etList) {
                if( TextUtils.isEmpty(et.getText())){
                    err = true;
                }else {
                    double d = Double.parseDouble(et.getText().toString());
                    left.leftConst.add(d);
                }
            }
            wrapper.listConst.add(left);
        }
        for (EditText et: rightConst) {
            if( TextUtils.isEmpty(et.getText())){
                err = true;
            }else {
                double d = Double.parseDouble(et.getText().toString());
                wrapper.valConst.add(d);
            }
        }

        for (Constraint opt: defaultOperator) {
            wrapper.operator.add(opt);
        }
        wrapper.option = calc.option;

        if(err){
            Toast.makeText(this,"Konstanta harus diisi 0 jika tidak akan dipakai",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this,HasilActivity.class);
            intent.putExtra(HasilActivity.EXTRA_BAHAN,wrapper);
            this.startActivity(intent);

        }

        return wrapper;

    }


}
