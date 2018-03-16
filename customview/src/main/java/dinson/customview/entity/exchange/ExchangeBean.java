package dinson.customview.entity.exchange;

/**
 * 汇率兑换服务器返回response
 */
public class ExchangeBean {
    @Override
    public String toString() {
        return "ExchangeBean{" +
            "timestamp=" + timestamp +
            ", base='" + base + '\'' +
            ", rates=" + rates +
            '}';
    }

    /**
     * disclaimer : Usage subject to terms: https://openexchangerates.org/terms
     * license : https://openexchangerates.org/license
     * timestamp : 1507881600
     * base : USD
     * rates : {"AED":3.673014,"AFN":68.3715,"ALL":113.175,"AMD":481.75,"ANG":1.7889,"AOA":165.9215,"ARS":17.4152,"AUD":1.275517,"AWG":1.789995,"AZN":1.69,"BAM":1.653557,"BBD":2,"BDT":82.634271,"BGN":1.6536,"BHD":0.377263,"BIF":1757.35,"BMD":1,"BND":1.353003,"BOB":6.925014,"BRL":3.170489,"BSD":1,"BTC":1.77730108E-4,"BTN":65.045068,"BWP":10.310514,"BYN":1.972866,"BZD":2.020139,"CAD":1.246468,"CDF":1574.900794,"CHF":0.975214,"CLF":0.02317,"CLP":625.456164,"CNH":6.57768,"CNY":6.5864,"COP":2959.75,"CRC":575.285,"CUC":1,"CUP":25.5,"CVE":93.375,"CZK":21.866522,"DJF":178.97,"DKK":6.293023,"DOP":47.548744,"DZD":113.5655,"EGP":17.62,"ERN":15.32359,"ETB":26.605507,"EUR":0.845407,"FJD":2.0334,"FKP":0.750924,"GBP":0.750924,"GEL":2.477604,"GGP":0.750924,"GHS":4.389541,"GIP":0.750924,"GMD":47.25,"GNF":8956.8,"GTQ":7.361466,"GYD":207.555,"HKD":7.80727,"HNL":23.451477,"HRK":6.349806,"HTG":63.468772,"HUF":260.7465,"IDR":13502.186005,"ILS":3.49987,"IMP":0.750924,"INR":64.875,"IQD":1170.55,"IRR":33654.5,"ISK":105.05,"JEP":0.750924,"JMD":128.515,"JOD":0.708801,"JPY":112.00544118,"KES":103.286953,"KGS":68.409874,"KHR":4048.95,"KMF":415.851107,"KPW":900,"KRW":1128.79,"KWD":0.30209,"KYD":0.835156,"KZT":335.155,"LAK":8323.65,"LBP":1511.4,"LKR":153.985,"LRD":118.057618,"LSL":13.526435,"LYD":1.36946,"MAD":9.407305,"MDL":17.463,"MGA":3056.65,"MKD":52.06,"MMK":1368,"MNT":2456.36303,"MOP":8.060022,"MRO":365.795,"MUR":34.0235,"MVR":15.409873,"MWK":725.014944,"MXN":18.90003,"MYR":4.2195,"MZN":60.912666,"NAD":13.526435,"NGN":360.78,"NIO":30.372436,"NOK":7.898239,"NPR":104.360191,"NZD":1.399228,"OMR":0.384999,"PAB":1,"PEN":3.253579,"PGK":3.20934,"PHP":51.425,"PKR":105.279978,"PLN":3.604265,"PYG":5654,"QAR":3.734987,"RON":3.879377,"RSD":100.886638,"RUB":57.587,"RWF":835.79,"SAR":3.7503,"SBD":7.806858,"SCR":13.619789,"SDG":6.692607,"SEK":8.113992,"SGD":1.354227,"SHP":0.750924,"SLL":7643.00921,"SOS":579.73,"SRD":7.448,"SSP":129.6836,"STD":20714.562388,"SVC":8.768184,"SYP":514.98999,"SZL":13.531086,"THB":33.0765,"TJS":8.819206,"TMT":3.50998,"TND":2.450695,"TOP":2.242212,"TRY":3.652566,"TTD":6.738795,"TWD":30.137,"TZS":2247.474747,"UAH":26.672807,"UGX":3637.95,"USD":1,"UYU":29.325395,"UZS":8049.466667,"VEF":10.11795,"VND":22709.725001,"VUV":106.150052,"WST":2.521477,"XAF":554.550507,"XAG":0.05789222,"XAU":7.7172E-4,"XCD":2.70255,"XDR":0.706904,"XOF":554.550507,"XPD":0.00101941,"XPF":100.883866,"XPT":0.00106667,"YER":250.306642,"ZAR":13.406285,"ZMW":9.666113,"ZWL":322.355011}
     */

    private String disclaimer;
    private String license;
    private int timestamp;
    private String base;
    private RatesBean rates;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public RatesBean getRates() {
        return rates;
    }

    public void setRates(RatesBean rates) {
        this.rates = rates;
    }

    public static class RatesBean {

        @Override
        public String toString() {
            return "RatesBean{" +
                "CNY=" + CNY +
                ", HKD=" + HKD +
                ", XAG=" + XAG +
                ", XAU=" + XAU +
                '}';
        }

        /**
         * AED : 3.673014
         * AFN : 68.3715
         * ALL : 113.175
         * AMD : 481.75
         * ANG : 1.7889
         * AOA : 165.9215
         * ARS : 17.4152
         * AUD : 1.275517
         * AWG : 1.789995
         * AZN : 1.69
         * BAM : 1.653557
         * BBD : 2
         * BDT : 82.634271
         * BGN : 1.6536
         * BHD : 0.377263
         * BIF : 1757.35
         * BMD : 1
         * BND : 1.353003
         * BOB : 6.925014
         * BRL : 3.170489
         * BSD : 1
         * BTC : 1.77730108E-4
         * BTN : 65.045068
         * BWP : 10.310514
         * BYN : 1.972866
         * BZD : 2.020139
         * CAD : 1.246468
         * CDF : 1574.900794
         * CHF : 0.975214
         * CLF : 0.02317
         * CLP : 625.456164
         * CNH : 6.57768
         * CNY : 6.5864
         * COP : 2959.75
         * CRC : 575.285
         * CUC : 1
         * CUP : 25.5
         * CVE : 93.375
         * CZK : 21.866522
         * DJF : 178.97
         * DKK : 6.293023
         * DOP : 47.548744
         * DZD : 113.5655
         * EGP : 17.62
         * ERN : 15.32359
         * ETB : 26.605507
         * EUR : 0.845407
         * FJD : 2.0334
         * FKP : 0.750924
         * GBP : 0.750924
         * GEL : 2.477604
         * GGP : 0.750924
         * GHS : 4.389541
         * GIP : 0.750924
         * GMD : 47.25
         * GNF : 8956.8
         * GTQ : 7.361466
         * GYD : 207.555
         * HKD : 7.80727
         * HNL : 23.451477
         * HRK : 6.349806
         * HTG : 63.468772
         * HUF : 260.7465
         * IDR : 13502.186005
         * ILS : 3.49987
         * IMP : 0.750924
         * INR : 64.875
         * IQD : 1170.55
         * IRR : 33654.5
         * ISK : 105.05
         * JEP : 0.750924
         * JMD : 128.515
         * JOD : 0.708801
         * JPY : 112.00544118
         * KES : 103.286953
         * KGS : 68.409874
         * KHR : 4048.95
         * KMF : 415.851107
         * KPW : 900
         * KRW : 1128.79
         * KWD : 0.30209
         * KYD : 0.835156
         * KZT : 335.155
         * LAK : 8323.65
         * LBP : 1511.4
         * LKR : 153.985
         * LRD : 118.057618
         * LSL : 13.526435
         * LYD : 1.36946
         * MAD : 9.407305
         * MDL : 17.463
         * MGA : 3056.65
         * MKD : 52.06
         * MMK : 1368
         * MNT : 2456.36303
         * MOP : 8.060022
         * MRO : 365.795
         * MUR : 34.0235
         * MVR : 15.409873
         * MWK : 725.014944
         * MXN : 18.90003
         * MYR : 4.2195
         * MZN : 60.912666
         * NAD : 13.526435
         * NGN : 360.78
         * NIO : 30.372436
         * NOK : 7.898239
         * NPR : 104.360191
         * NZD : 1.399228
         * OMR : 0.384999
         * PAB : 1
         * PEN : 3.253579
         * PGK : 3.20934
         * PHP : 51.425
         * PKR : 105.279978
         * PLN : 3.604265
         * PYG : 5654
         * QAR : 3.734987
         * RON : 3.879377
         * RSD : 100.886638
         * RUB : 57.587
         * RWF : 835.79
         * SAR : 3.7503
         * SBD : 7.806858
         * SCR : 13.619789
         * SDG : 6.692607
         * SEK : 8.113992
         * SGD : 1.354227
         * SHP : 0.750924
         * SLL : 7643.00921
         * SOS : 579.73
         * SRD : 7.448
         * SSP : 129.6836
         * STD : 20714.562388
         * SVC : 8.768184
         * SYP : 514.98999
         * SZL : 13.531086
         * THB : 33.0765
         * TJS : 8.819206
         * TMT : 3.50998
         * TND : 2.450695
         * TOP : 2.242212
         * TRY : 3.652566
         * TTD : 6.738795
         * TWD : 30.137
         * TZS : 2247.474747
         * UAH : 26.672807
         * UGX : 3637.95
         * USD : 1
         * UYU : 29.325395
         * UZS : 8049.466667
         * VEF : 10.11795
         * VND : 22709.725001
         * VUV : 106.150052
         * WST : 2.521477
         * XAF : 554.550507
         * XAG : 0.05789222
         * XAU : 7.7172E-4
         * XCD : 2.70255
         * XDR : 0.706904
         * XOF : 554.550507
         * XPD : 0.00101941
         * XPF : 100.883866
         * XPT : 0.00106667
         * YER : 250.306642
         * ZAR : 13.406285
         * ZMW : 9.666113
         * ZWL : 322.355011
         */

        private double AED;
        private double AFN;
        private double ALL;
        private double AMD;
        private double ANG;
        private double AOA;
        private double ARS;
        private double AUD;
        private double AWG;
        private double AZN;
        private double BAM;
        private double BBD;
        private double BDT;
        private double BGN;
        private double BHD;
        private double BIF;
        private double BMD;
        private double BND;
        private double BOB;
        private double BRL;
        private double BSD;
        private double BTC;
        private double BTN;
        private double BWP;
        private double BYN;
        private double BZD;
        private double CAD;
        private double CDF;
        private double CHF;
        private double CLF;
        private double CLP;
        private double CNH;
        private double CNY;
        private double COP;
        private double CRC;
        private double CUC;
        private double CUP;
        private double CVE;
        private double CZK;
        private double DJF;
        private double DKK;
        private double DOP;
        private double DZD;
        private double EGP;
        private double ERN;
        private double ETB;
        private double EUR;
        private double FJD;
        private double FKP;
        private double GBP;
        private double GEL;
        private double GGP;
        private double GHS;
        private double GIP;
        private double GMD;
        private double GNF;
        private double GTQ;
        private double GYD;
        private double HKD;
        private double HNL;
        private double HRK;
        private double HTG;
        private double HUF;
        private double IDR;
        private double ILS;
        private double IMP;
        private double INR;
        private double IQD;
        private double IRR;
        private double ISK;
        private double JEP;
        private double JMD;
        private double JOD;
        private double JPY;
        private double KES;
        private double KGS;
        private double KHR;
        private double KMF;
        private double KPW;
        private double KRW;
        private double KWD;
        private double KYD;
        private double KZT;
        private double LAK;
        private double LBP;
        private double LKR;
        private double LRD;
        private double LSL;
        private double LYD;
        private double MAD;
        private double MDL;
        private double MGA;
        private double MKD;
        private double MMK;
        private double MNT;
        private double MOP;
        private double MRO;
        private double MUR;
        private double MVR;
        private double MWK;
        private double MXN;
        private double MYR;
        private double MZN;
        private double NAD;
        private double NGN;
        private double NIO;
        private double NOK;
        private double NPR;
        private double NZD;
        private double OMR;
        private double PAB;
        private double PEN;
        private double PGK;
        private double PHP;
        private double PKR;
        private double PLN;
        private double PYG;
        private double QAR;
        private double RON;
        private double RSD;
        private double RUB;
        private double RWF;
        private double SAR;
        private double SBD;
        private double SCR;
        private double SDG;
        private double SEK;
        private double SGD;
        private double SHP;
        private double SLL;
        private double SOS;
        private double SRD;
        private double SSP;
        private double STD;
        private double SVC;
        private double SYP;
        private double SZL;
        private double THB;
        private double TJS;
        private double TMT;
        private double TND;
        private double TOP;
        private double TRY;
        private double TTD;
        private double TWD;
        private double TZS;
        private double UAH;
        private double UGX;
        private double USD;
        private double UYU;
        private double UZS;
        private double VEF;
        private double VND;
        private double VUV;
        private double WST;
        private double XAF;
        private double XAG;
        private double XAU;
        private double XCD;
        private double XDR;
        private double XOF;
        private double XPD;
        private double XPF;
        private double XPT;
        private double YER;
        private double ZAR;
        private double ZMW;
        private double ZWL;

        public double getAED() {
            return AED;
        }

        public void setAED(double AED) {
            this.AED = AED;
        }

        public double getAFN() {
            return AFN;
        }

        public void setAFN(double AFN) {
            this.AFN = AFN;
        }

        public double getALL() {
            return ALL;
        }

        public void setALL(double ALL) {
            this.ALL = ALL;
        }

        public double getAMD() {
            return AMD;
        }

        public void setAMD(double AMD) {
            this.AMD = AMD;
        }

        public double getANG() {
            return ANG;
        }

        public void setANG(double ANG) {
            this.ANG = ANG;
        }

        public double getAOA() {
            return AOA;
        }

        public void setAOA(double AOA) {
            this.AOA = AOA;
        }

        public double getARS() {
            return ARS;
        }

        public void setARS(double ARS) {
            this.ARS = ARS;
        }

        public double getAUD() {
            return AUD;
        }

        public void setAUD(double AUD) {
            this.AUD = AUD;
        }

        public double getAWG() {
            return AWG;
        }

        public void setAWG(double AWG) {
            this.AWG = AWG;
        }

        public double getAZN() {
            return AZN;
        }

        public void setAZN(double AZN) {
            this.AZN = AZN;
        }

        public double getBAM() {
            return BAM;
        }

        public void setBAM(double BAM) {
            this.BAM = BAM;
        }

        public double getBBD() {
            return BBD;
        }

        public void setBBD(double BBD) {
            this.BBD = BBD;
        }

        public double getBDT() {
            return BDT;
        }

        public void setBDT(double BDT) {
            this.BDT = BDT;
        }

        public double getBGN() {
            return BGN;
        }

        public void setBGN(double BGN) {
            this.BGN = BGN;
        }

        public double getBHD() {
            return BHD;
        }

        public void setBHD(double BHD) {
            this.BHD = BHD;
        }

        public double getBIF() {
            return BIF;
        }

        public void setBIF(double BIF) {
            this.BIF = BIF;
        }

        public double getBMD() {
            return BMD;
        }

        public void setBMD(double BMD) {
            this.BMD = BMD;
        }

        public double getBND() {
            return BND;
        }

        public void setBND(double BND) {
            this.BND = BND;
        }

        public double getBOB() {
            return BOB;
        }

        public void setBOB(double BOB) {
            this.BOB = BOB;
        }

        public double getBRL() {
            return BRL;
        }

        public void setBRL(double BRL) {
            this.BRL = BRL;
        }

        public double getBSD() {
            return BSD;
        }

        public void setBSD(double BSD) {
            this.BSD = BSD;
        }

        public double getBTC() {
            return BTC;
        }

        public void setBTC(double BTC) {
            this.BTC = BTC;
        }

        public double getBTN() {
            return BTN;
        }

        public void setBTN(double BTN) {
            this.BTN = BTN;
        }

        public double getBWP() {
            return BWP;
        }

        public void setBWP(double BWP) {
            this.BWP = BWP;
        }

        public double getBYN() {
            return BYN;
        }

        public void setBYN(double BYN) {
            this.BYN = BYN;
        }

        public double getBZD() {
            return BZD;
        }

        public void setBZD(double BZD) {
            this.BZD = BZD;
        }

        public double getCAD() {
            return CAD;
        }

        public void setCAD(double CAD) {
            this.CAD = CAD;
        }

        public double getCDF() {
            return CDF;
        }

        public void setCDF(double CDF) {
            this.CDF = CDF;
        }

        public double getCHF() {
            return CHF;
        }

        public void setCHF(double CHF) {
            this.CHF = CHF;
        }

        public double getCLF() {
            return CLF;
        }

        public void setCLF(double CLF) {
            this.CLF = CLF;
        }

        public double getCLP() {
            return CLP;
        }

        public void setCLP(double CLP) {
            this.CLP = CLP;
        }

        public double getCNH() {
            return CNH;
        }

        public void setCNH(double CNH) {
            this.CNH = CNH;
        }

        public double getCNY() {
            return CNY;
        }

        public void setCNY(double CNY) {
            this.CNY = CNY;
        }

        public double getCOP() {
            return COP;
        }

        public void setCOP(double COP) {
            this.COP = COP;
        }

        public double getCRC() {
            return CRC;
        }

        public void setCRC(double CRC) {
            this.CRC = CRC;
        }

        public double getCUC() {
            return CUC;
        }

        public void setCUC(double CUC) {
            this.CUC = CUC;
        }

        public double getCUP() {
            return CUP;
        }

        public void setCUP(double CUP) {
            this.CUP = CUP;
        }

        public double getCVE() {
            return CVE;
        }

        public void setCVE(double CVE) {
            this.CVE = CVE;
        }

        public double getCZK() {
            return CZK;
        }

        public void setCZK(double CZK) {
            this.CZK = CZK;
        }

        public double getDJF() {
            return DJF;
        }

        public void setDJF(double DJF) {
            this.DJF = DJF;
        }

        public double getDKK() {
            return DKK;
        }

        public void setDKK(double DKK) {
            this.DKK = DKK;
        }

        public double getDOP() {
            return DOP;
        }

        public void setDOP(double DOP) {
            this.DOP = DOP;
        }

        public double getDZD() {
            return DZD;
        }

        public void setDZD(double DZD) {
            this.DZD = DZD;
        }

        public double getEGP() {
            return EGP;
        }

        public void setEGP(double EGP) {
            this.EGP = EGP;
        }

        public double getERN() {
            return ERN;
        }

        public void setERN(double ERN) {
            this.ERN = ERN;
        }

        public double getETB() {
            return ETB;
        }

        public void setETB(double ETB) {
            this.ETB = ETB;
        }

        public double getEUR() {
            return EUR;
        }

        public void setEUR(double EUR) {
            this.EUR = EUR;
        }

        public double getFJD() {
            return FJD;
        }

        public void setFJD(double FJD) {
            this.FJD = FJD;
        }

        public double getFKP() {
            return FKP;
        }

        public void setFKP(double FKP) {
            this.FKP = FKP;
        }

        public double getGBP() {
            return GBP;
        }

        public void setGBP(double GBP) {
            this.GBP = GBP;
        }

        public double getGEL() {
            return GEL;
        }

        public void setGEL(double GEL) {
            this.GEL = GEL;
        }

        public double getGGP() {
            return GGP;
        }

        public void setGGP(double GGP) {
            this.GGP = GGP;
        }

        public double getGHS() {
            return GHS;
        }

        public void setGHS(double GHS) {
            this.GHS = GHS;
        }

        public double getGIP() {
            return GIP;
        }

        public void setGIP(double GIP) {
            this.GIP = GIP;
        }

        public double getGMD() {
            return GMD;
        }

        public void setGMD(double GMD) {
            this.GMD = GMD;
        }

        public double getGNF() {
            return GNF;
        }

        public void setGNF(double GNF) {
            this.GNF = GNF;
        }

        public double getGTQ() {
            return GTQ;
        }

        public void setGTQ(double GTQ) {
            this.GTQ = GTQ;
        }

        public double getGYD() {
            return GYD;
        }

        public void setGYD(double GYD) {
            this.GYD = GYD;
        }

        public double getHKD() {
            return HKD;
        }

        public void setHKD(double HKD) {
            this.HKD = HKD;
        }

        public double getHNL() {
            return HNL;
        }

        public void setHNL(double HNL) {
            this.HNL = HNL;
        }

        public double getHRK() {
            return HRK;
        }

        public void setHRK(double HRK) {
            this.HRK = HRK;
        }

        public double getHTG() {
            return HTG;
        }

        public void setHTG(double HTG) {
            this.HTG = HTG;
        }

        public double getHUF() {
            return HUF;
        }

        public void setHUF(double HUF) {
            this.HUF = HUF;
        }

        public double getIDR() {
            return IDR;
        }

        public void setIDR(double IDR) {
            this.IDR = IDR;
        }

        public double getILS() {
            return ILS;
        }

        public void setILS(double ILS) {
            this.ILS = ILS;
        }

        public double getIMP() {
            return IMP;
        }

        public void setIMP(double IMP) {
            this.IMP = IMP;
        }

        public double getINR() {
            return INR;
        }

        public void setINR(double INR) {
            this.INR = INR;
        }

        public double getIQD() {
            return IQD;
        }

        public void setIQD(double IQD) {
            this.IQD = IQD;
        }

        public double getIRR() {
            return IRR;
        }

        public void setIRR(double IRR) {
            this.IRR = IRR;
        }

        public double getISK() {
            return ISK;
        }

        public void setISK(double ISK) {
            this.ISK = ISK;
        }

        public double getJEP() {
            return JEP;
        }

        public void setJEP(double JEP) {
            this.JEP = JEP;
        }

        public double getJMD() {
            return JMD;
        }

        public void setJMD(double JMD) {
            this.JMD = JMD;
        }

        public double getJOD() {
            return JOD;
        }

        public void setJOD(double JOD) {
            this.JOD = JOD;
        }

        public double getJPY() {
            return JPY;
        }

        public void setJPY(double JPY) {
            this.JPY = JPY;
        }

        public double getKES() {
            return KES;
        }

        public void setKES(double KES) {
            this.KES = KES;
        }

        public double getKGS() {
            return KGS;
        }

        public void setKGS(double KGS) {
            this.KGS = KGS;
        }

        public double getKHR() {
            return KHR;
        }

        public void setKHR(double KHR) {
            this.KHR = KHR;
        }

        public double getKMF() {
            return KMF;
        }

        public void setKMF(double KMF) {
            this.KMF = KMF;
        }

        public double getKPW() {
            return KPW;
        }

        public void setKPW(double KPW) {
            this.KPW = KPW;
        }

        public double getKRW() {
            return KRW;
        }

        public void setKRW(double KRW) {
            this.KRW = KRW;
        }

        public double getKWD() {
            return KWD;
        }

        public void setKWD(double KWD) {
            this.KWD = KWD;
        }

        public double getKYD() {
            return KYD;
        }

        public void setKYD(double KYD) {
            this.KYD = KYD;
        }

        public double getKZT() {
            return KZT;
        }

        public void setKZT(double KZT) {
            this.KZT = KZT;
        }

        public double getLAK() {
            return LAK;
        }

        public void setLAK(double LAK) {
            this.LAK = LAK;
        }

        public double getLBP() {
            return LBP;
        }

        public void setLBP(double LBP) {
            this.LBP = LBP;
        }

        public double getLKR() {
            return LKR;
        }

        public void setLKR(double LKR) {
            this.LKR = LKR;
        }

        public double getLRD() {
            return LRD;
        }

        public void setLRD(double LRD) {
            this.LRD = LRD;
        }

        public double getLSL() {
            return LSL;
        }

        public void setLSL(double LSL) {
            this.LSL = LSL;
        }

        public double getLYD() {
            return LYD;
        }

        public void setLYD(double LYD) {
            this.LYD = LYD;
        }

        public double getMAD() {
            return MAD;
        }

        public void setMAD(double MAD) {
            this.MAD = MAD;
        }

        public double getMDL() {
            return MDL;
        }

        public void setMDL(double MDL) {
            this.MDL = MDL;
        }

        public double getMGA() {
            return MGA;
        }

        public void setMGA(double MGA) {
            this.MGA = MGA;
        }

        public double getMKD() {
            return MKD;
        }

        public void setMKD(double MKD) {
            this.MKD = MKD;
        }

        public double getMMK() {
            return MMK;
        }

        public void setMMK(double MMK) {
            this.MMK = MMK;
        }

        public double getMNT() {
            return MNT;
        }

        public void setMNT(double MNT) {
            this.MNT = MNT;
        }

        public double getMOP() {
            return MOP;
        }

        public void setMOP(double MOP) {
            this.MOP = MOP;
        }

        public double getMRO() {
            return MRO;
        }

        public void setMRO(double MRO) {
            this.MRO = MRO;
        }

        public double getMUR() {
            return MUR;
        }

        public void setMUR(double MUR) {
            this.MUR = MUR;
        }

        public double getMVR() {
            return MVR;
        }

        public void setMVR(double MVR) {
            this.MVR = MVR;
        }

        public double getMWK() {
            return MWK;
        }

        public void setMWK(double MWK) {
            this.MWK = MWK;
        }

        public double getMXN() {
            return MXN;
        }

        public void setMXN(double MXN) {
            this.MXN = MXN;
        }

        public double getMYR() {
            return MYR;
        }

        public void setMYR(double MYR) {
            this.MYR = MYR;
        }

        public double getMZN() {
            return MZN;
        }

        public void setMZN(double MZN) {
            this.MZN = MZN;
        }

        public double getNAD() {
            return NAD;
        }

        public void setNAD(double NAD) {
            this.NAD = NAD;
        }

        public double getNGN() {
            return NGN;
        }

        public void setNGN(double NGN) {
            this.NGN = NGN;
        }

        public double getNIO() {
            return NIO;
        }

        public void setNIO(double NIO) {
            this.NIO = NIO;
        }

        public double getNOK() {
            return NOK;
        }

        public void setNOK(double NOK) {
            this.NOK = NOK;
        }

        public double getNPR() {
            return NPR;
        }

        public void setNPR(double NPR) {
            this.NPR = NPR;
        }

        public double getNZD() {
            return NZD;
        }

        public void setNZD(double NZD) {
            this.NZD = NZD;
        }

        public double getOMR() {
            return OMR;
        }

        public void setOMR(double OMR) {
            this.OMR = OMR;
        }

        public double getPAB() {
            return PAB;
        }

        public void setPAB(double PAB) {
            this.PAB = PAB;
        }

        public double getPEN() {
            return PEN;
        }

        public void setPEN(double PEN) {
            this.PEN = PEN;
        }

        public double getPGK() {
            return PGK;
        }

        public void setPGK(double PGK) {
            this.PGK = PGK;
        }

        public double getPHP() {
            return PHP;
        }

        public void setPHP(double PHP) {
            this.PHP = PHP;
        }

        public double getPKR() {
            return PKR;
        }

        public void setPKR(double PKR) {
            this.PKR = PKR;
        }

        public double getPLN() {
            return PLN;
        }

        public void setPLN(double PLN) {
            this.PLN = PLN;
        }

        public double getPYG() {
            return PYG;
        }

        public void setPYG(double PYG) {
            this.PYG = PYG;
        }

        public double getQAR() {
            return QAR;
        }

        public void setQAR(double QAR) {
            this.QAR = QAR;
        }

        public double getRON() {
            return RON;
        }

        public void setRON(double RON) {
            this.RON = RON;
        }

        public double getRSD() {
            return RSD;
        }

        public void setRSD(double RSD) {
            this.RSD = RSD;
        }

        public double getRUB() {
            return RUB;
        }

        public void setRUB(double RUB) {
            this.RUB = RUB;
        }

        public double getRWF() {
            return RWF;
        }

        public void setRWF(double RWF) {
            this.RWF = RWF;
        }

        public double getSAR() {
            return SAR;
        }

        public void setSAR(double SAR) {
            this.SAR = SAR;
        }

        public double getSBD() {
            return SBD;
        }

        public void setSBD(double SBD) {
            this.SBD = SBD;
        }

        public double getSCR() {
            return SCR;
        }

        public void setSCR(double SCR) {
            this.SCR = SCR;
        }

        public double getSDG() {
            return SDG;
        }

        public void setSDG(double SDG) {
            this.SDG = SDG;
        }

        public double getSEK() {
            return SEK;
        }

        public void setSEK(double SEK) {
            this.SEK = SEK;
        }

        public double getSGD() {
            return SGD;
        }

        public void setSGD(double SGD) {
            this.SGD = SGD;
        }

        public double getSHP() {
            return SHP;
        }

        public void setSHP(double SHP) {
            this.SHP = SHP;
        }

        public double getSLL() {
            return SLL;
        }

        public void setSLL(double SLL) {
            this.SLL = SLL;
        }

        public double getSOS() {
            return SOS;
        }

        public void setSOS(double SOS) {
            this.SOS = SOS;
        }

        public double getSRD() {
            return SRD;
        }

        public void setSRD(double SRD) {
            this.SRD = SRD;
        }

        public double getSSP() {
            return SSP;
        }

        public void setSSP(double SSP) {
            this.SSP = SSP;
        }

        public double getSTD() {
            return STD;
        }

        public void setSTD(double STD) {
            this.STD = STD;
        }

        public double getSVC() {
            return SVC;
        }

        public void setSVC(double SVC) {
            this.SVC = SVC;
        }

        public double getSYP() {
            return SYP;
        }

        public void setSYP(double SYP) {
            this.SYP = SYP;
        }

        public double getSZL() {
            return SZL;
        }

        public void setSZL(double SZL) {
            this.SZL = SZL;
        }

        public double getTHB() {
            return THB;
        }

        public void setTHB(double THB) {
            this.THB = THB;
        }

        public double getTJS() {
            return TJS;
        }

        public void setTJS(double TJS) {
            this.TJS = TJS;
        }

        public double getTMT() {
            return TMT;
        }

        public void setTMT(double TMT) {
            this.TMT = TMT;
        }

        public double getTND() {
            return TND;
        }

        public void setTND(double TND) {
            this.TND = TND;
        }

        public double getTOP() {
            return TOP;
        }

        public void setTOP(double TOP) {
            this.TOP = TOP;
        }

        public double getTRY() {
            return TRY;
        }

        public void setTRY(double TRY) {
            this.TRY = TRY;
        }

        public double getTTD() {
            return TTD;
        }

        public void setTTD(double TTD) {
            this.TTD = TTD;
        }

        public double getTWD() {
            return TWD;
        }

        public void setTWD(double TWD) {
            this.TWD = TWD;
        }

        public double getTZS() {
            return TZS;
        }

        public void setTZS(double TZS) {
            this.TZS = TZS;
        }

        public double getUAH() {
            return UAH;
        }

        public void setUAH(double UAH) {
            this.UAH = UAH;
        }

        public double getUGX() {
            return UGX;
        }

        public void setUGX(double UGX) {
            this.UGX = UGX;
        }

        public double getUSD() {
            return USD;
        }

        public void setUSD(double USD) {
            this.USD = USD;
        }

        public double getUYU() {
            return UYU;
        }

        public void setUYU(double UYU) {
            this.UYU = UYU;
        }

        public double getUZS() {
            return UZS;
        }

        public void setUZS(double UZS) {
            this.UZS = UZS;
        }

        public double getVEF() {
            return VEF;
        }

        public void setVEF(double VEF) {
            this.VEF = VEF;
        }

        public double getVND() {
            return VND;
        }

        public void setVND(double VND) {
            this.VND = VND;
        }

        public double getVUV() {
            return VUV;
        }

        public void setVUV(double VUV) {
            this.VUV = VUV;
        }

        public double getWST() {
            return WST;
        }

        public void setWST(double WST) {
            this.WST = WST;
        }

        public double getXAF() {
            return XAF;
        }

        public void setXAF(double XAF) {
            this.XAF = XAF;
        }

        public double getXAG() {
            return XAG;
        }

        public void setXAG(double XAG) {
            this.XAG = XAG;
        }

        public double getXAU() {
            return XAU;
        }

        public void setXAU(double XAU) {
            this.XAU = XAU;
        }

        public double getXCD() {
            return XCD;
        }

        public void setXCD(double XCD) {
            this.XCD = XCD;
        }

        public double getXDR() {
            return XDR;
        }

        public void setXDR(double XDR) {
            this.XDR = XDR;
        }

        public double getXOF() {
            return XOF;
        }

        public void setXOF(double XOF) {
            this.XOF = XOF;
        }

        public double getXPD() {
            return XPD;
        }

        public void setXPD(double XPD) {
            this.XPD = XPD;
        }

        public double getXPF() {
            return XPF;
        }

        public void setXPF(double XPF) {
            this.XPF = XPF;
        }

        public double getXPT() {
            return XPT;
        }

        public void setXPT(double XPT) {
            this.XPT = XPT;
        }

        public double getYER() {
            return YER;
        }

        public void setYER(double YER) {
            this.YER = YER;
        }

        public double getZAR() {
            return ZAR;
        }

        public void setZAR(double ZAR) {
            this.ZAR = ZAR;
        }

        public double getZMW() {
            return ZMW;
        }

        public void setZMW(double ZMW) {
            this.ZMW = ZMW;
        }

        public double getZWL() {
            return ZWL;
        }

        public void setZWL(double ZWL) {
            this.ZWL = ZWL;
        }
    }
}
