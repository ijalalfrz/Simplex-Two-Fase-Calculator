package rizalalfarizi1600807.cs.upi.edu.simplextwofase.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Calc implements Parcelable {

    public int cons;
    public int var;
    public String option;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cons);
        dest.writeInt(this.var);
        dest.writeString(this.option);
    }

    public Calc() {
    }

    protected Calc(Parcel in) {
        this.cons = in.readInt();
        this.var = in.readInt();
        this.option = in.readString();
    }

    public static final Parcelable.Creator<Calc> CREATOR = new Parcelable.Creator<Calc>() {
        @Override
        public Calc createFromParcel(Parcel source) {
            return new Calc(source);
        }

        @Override
        public Calc[] newArray(int size) {
            return new Calc[size];
        }
    };
}
