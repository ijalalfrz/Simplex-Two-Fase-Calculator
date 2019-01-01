package rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import java.util.ArrayList;

import rizalalfarizi1600807.cs.upi.edu.simplextwofase.HitungActivity;

public class CalcWrapper implements Parcelable {
    public ArrayList<Double> variable;
    public ArrayList<LeftConst> listConst;
    public ArrayList<Double> valConst;
    public ArrayList<HitungActivity.Constraint> operator;
    public String option;

    public CalcWrapper() {
        variable = new ArrayList<>();
        listConst = new ArrayList<>();
        valConst = new ArrayList<>();
        operator = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.variable);
        dest.writeTypedList(this.listConst);
        dest.writeList(this.valConst);
        dest.writeList(this.operator);
        dest.writeString(this.option);
    }

    protected CalcWrapper(Parcel in) {
        this.variable = new ArrayList<Double>();
        in.readList(this.variable, Double.class.getClassLoader());
        this.listConst = in.createTypedArrayList(LeftConst.CREATOR);
        this.valConst = new ArrayList<Double>();
        in.readList(this.valConst, Double.class.getClassLoader());
        this.operator = new ArrayList<HitungActivity.Constraint>();
        in.readList(this.operator, HitungActivity.Constraint.class.getClassLoader());
        this.option = in.readString();
    }

    public static final Creator<CalcWrapper> CREATOR = new Creator<CalcWrapper>() {
        @Override
        public CalcWrapper createFromParcel(Parcel source) {
            return new CalcWrapper(source);
        }

        @Override
        public CalcWrapper[] newArray(int size) {
            return new CalcWrapper[size];
        }
    };
}
