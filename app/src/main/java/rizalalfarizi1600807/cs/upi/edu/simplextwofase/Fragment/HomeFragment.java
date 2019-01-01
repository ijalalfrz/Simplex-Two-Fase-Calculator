package rizalalfarizi1600807.cs.upi.edu.simplextwofase.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rizalalfarizi1600807.cs.upi.edu.simplextwofase.HitungActivity;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model.Calc;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btnMax,btnMin;
    private TextView tvVar,tvCons,tvWarn;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);

        return view;
    }


    public void initUI(View v){
        btnMax = (Button) v.findViewById(R.id.btn_max);
        btnMin = (Button) v.findViewById(R.id.btn_min);
        tvCons = (TextView) v.findViewById(R.id.num_cons);
        tvVar = (TextView) v.findViewById(R.id.num_var);
        tvWarn = (TextView) v.findViewById(R.id.warning);

        btnMin.setOnClickListener(this);
        btnMax.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String constraint = tvCons.getText().toString();
        String variable = tvVar.getText().toString();
        if(variable.equals("") || constraint.equals("") || variable.equals("0") || constraint.equals("0")){
            tvWarn.setText("Constraint atau Variabel tidak boleh kosong!");
            tvWarn.setVisibility(View.VISIBLE);
        }else{

            Intent intent = new Intent(getActivity(),HitungActivity.class);
            Calc calc = new Calc();
            calc.cons = Integer.parseInt(constraint);
            calc.var = Integer.parseInt(variable);

            if(v.getId()==R.id.btn_max){
                calc.option = "MAX";
            }else{
                calc.option = "MIN";
            }
            intent.putExtra(HitungActivity.EXTRA_OPTION,calc);
            startActivity(intent);
        }

    }
}
