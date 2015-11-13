package com.yesy.drccurve;

import android.util.Log;

/**
 * User: ysy
 * Date: 2015/11/5
 * Time: 15:23
 */
public class DrcContrller {

    private double[] x_in, xPEAK, XRMSTWO, gn;
    private int count, length, count_p, length_p;
    private double AT, RT, ta, tr, TAV, tm, LogIn, LogPeak, LogRms, fn, G;
    private MyPoint NT, ET, CT, LT;

    /**
     * 斜率
     */
    private double NS, ES, CS;

    public DrcContrller() {
        count = 0;
        length = 3;
        x_in = new double[length];
        count_p = 0;
        length_p = 2;
        xPEAK = new double[length_p];
        XRMSTWO = new double[2];
        gn = new double[2];

        NT = new MyPoint(-70.0f, -70.0f);
        ET = new MyPoint(-50.0f, -50.0f);
        CT = new MyPoint(-25.0f, -25.0f);
        LT = new MyPoint(-10.0f, -10.0f);
        NS = (ET.y - NT.y) / (ET.x - NT.x);
        ES = (CT.y - ET.y) / (CT.x - ET.x);
        CS = (LT.y - CT.y) / (LT.x - CT.x);

        ta = 0.02;
        tr = 0.08;
        tm = 0.02;
        AT = 1.0 - Math.pow(Math.E, ((-0.0000498866213151927) / ta));
        RT = 1.0 - Math.pow(Math.E, ((-0.0000498866213151927) / tr));
        TAV = 1.0 - Math.pow(Math.E, ((-0.0000498866213151927) / tm));
    }

    public double getGn(double input) {
        double result = 0.0;
        int indexp_1 = count_p - 1;
        if (indexp_1 < 0) indexp_1 = length_p - 1;
        x_in[count] = input;
        if (Math.abs(x_in[count]) > xPEAK[indexp_1]) {
            xPEAK[count_p] = (1.0 - AT) * xPEAK[indexp_1] + AT * Math.abs(x_in[count]);
        } else {
            xPEAK[count_p] = (1.0 - RT) * xPEAK[indexp_1];
        }

        XRMSTWO[count_p] = (1.0 - TAV) * XRMSTWO[indexp_1] + TAV * (x_in[count] * x_in[count]);

        LogPeak = Math.log(xPEAK[count_p]) / Math.log10(2.0);
        if (LogPeak >= LT.x) {
            LogIn = LogPeak;
            G = LT.y - LogPeak;
            fn = Math.pow(2.0, G);
        } else {
            LogRms = Math.log(Math.sqrt(XRMSTWO[count_p])) / Math.log10(2.0);
            if (LogRms < NT.x) {
                fn = 0.0;
            } else if (LogRms < ET.x) {
                G = NT.y + NS * (LogRms - NT.x) - LogRms;
                fn = Math.pow(2.0, G);
            } else if (LogRms < CT.x) {
                G = ET.y + ES * (LogRms - ET.x) - LogRms;
                fn = Math.pow(2.0, G);
            } else {
                G = CT.y + CS * (LogRms - CT.x) - LogRms;
                fn = Math.pow(2.0, G);
            }
        }

        if (fn > gn[indexp_1]) {
            gn[count_p] = (1 - (AT)) * gn[indexp_1] + AT * fn;
        } else {
            gn[count_p] = (1 - (RT)) * gn[indexp_1] + RT * fn;
        }

        result = gn[count_p] * x_in[count];
        Log.e("Test:", "xPEAK=" + Math.log10(xPEAK[count_p]));

        if (++count >= length) count = 0;
        if (++count_p >= length_p) count_p = 0;
        return result;
    }
}
