package rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LeftConst implements Parcelable {
    public ArrayList<Double> leftConst;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.leftConst);
    }

    public LeftConst() {
        leftConst = new ArrayList<>();
    }

    protected LeftConst(Parcel in) {
        this.leftConst = new ArrayList<Double>();
        in.readList(this.leftConst, Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<LeftConst> CREATOR = new Parcelable.Creator<LeftConst>() {
        @Override
        public LeftConst createFromParcel(Parcel source) {
            return new LeftConst(source);
        }

        @Override
        public LeftConst[] newArray(int size) {
            return new LeftConst[size];
        }
    };
}
