package cz.muni.fi.mathml.mathml2text.converter.operation;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 * Various mathematical symbols that do not represent operations, but are
 * widely used.
 * 
 * @author Maros Kucbel
 * @date 2013-01-27T17:19:35+0100
 */
public enum Symbol {
    
    /** Greek letters */
    ALPHA("alpha", "alpha", "&alpha;", "Alpha", "&Alpha;", "&#x391;", "&#x3B1;", "&#913;", "&#945;"),
    BETA("beta", "beta", "&beta;", "Beta", "&Beta;", "&#x392;", "&#x3B2;", "&#914;", "&#946;"),
    GAMMA("gamma", "gamma", "&gamma;", "Gamma", "&Gamma;", "&#x393;", "&#x3B3;", "&#915;", "&#947;"),
    DELTA("delta", "delta", "&delta;", "Delta", "&Delta;", "&#x394;", "&#x3B4;", "&#916;", "&#948;"),
    EPSILON("epsilon", "epsilon", "&epsilon;", "Epsilon", "&Epsilon;", "&#x395;", "&#x3B5;", "&#917;", "&#949;"),
    ZETA("zeta", "zeta", "&zeta;", "Zeta", "&Zeta;", "&#x396;", "&#x3B6;", "&#918;", "&#950;"),
    ETA("eta", "eta", "&eta;", "Eta", "&Eta;", "&#x397;", "&#x3B7;", "&#919;", "&#951;"),
    THETA("theta", "theta", "&theta;", "Theta", "&Theta;", "&#x398;", "&#x3B8;", "&#920;", "&#952;"),
    IOTA("iota", "iota", "&iota;", "Iota", "&Iota;", "&#x399;", "&#x3B9;", "&#921;", "&#953;"),
    KAPPA("kappa", "kappa", "&kappa;", "Kappa", "&Kappa;", "&#x39A;", "&#x3BA;", "&#922;", "&#954;"),
    LAMBDA("lambda", "lambda", "&lambda;", "Lambda", "&Lambda;", "&#x39B;", "&#x3BB;", "&#923;", "&#955;"),
    MU("mu", "mu", "&mu;", "Mu", "&Mu;", "&#x39C;", "&#x3BC;", "&#924;", "&#956;"),
    NU("nu", "nu", "&nu;", "Nu", "&Nu;", "&#x39D;", "&#x3BD;", "&#925;", "&#957;"),
    XI("xi", "xi", "&xi;", "Xi", "&Xi;", "&#x39E;", "&#x3BE;", "&#926;", "&#958;"),
    OMICRON("omicron", "omicron", "&omicron;", "Omicron", "&Omicron;", "&#x39F;", "&#x3BF;", "&#927;", "&#959;"),
    PI("pi", "pi", "&pi;", "Pi", "&Pi;", "&#x3A0;", "&#x3C0;", "&#928;", "&#960;"),
    RHO("rho", "rho", "&rho;", "Rho", "&Rho;", "&#x3A1;", "&#x3C1;", "&#929;", "&#961;"),
    SIGMA("sigma", "sigma", "&sigma;", "Sigma", "&Simga;", "sigmaf", "&sigmaf;", "&#x3A3;", "&#x3C3;", "&#931;", "&#963;", "&#x3C2;", "&#962;"),
    TAU("tau", "tau", "&tau;", "Tau", "&Tau;", "&#x3A4;", "&#x3C4;", "&#932;", "&#964;"),
    UPSILON("upsilon", "upsilon", "&upsilon;", "Upsilon", "&Upsilon;", "&#x3A5;", "&#x3C5;", "&#933;", "&#965;"),
    PHI("phi", "phi", "&phi;", "Phi", "&Phi;", "&#x3A6;", "&#x3C6;", "&#934;", "&#966;"),
    CHI("chi", "chi", "&chi;", "Chi", "&Chi;", "&#x3A7;", "&#x3C7;", "&#935;", "&#967;"),
    PSI("psi", "psi", "&psi;", "Psi", "&Psi;", "&#x3A8;", "&#x3C8;", "&#936;", "&#968;"),
    OMEGA("omega", "omega", "&omega;", "Omega", "&Omega;", "&#x3A9;", "&#x3C9;", "&#937;", "&#969;"),
    /** Parentheses */
    ROUND_OPEN("open_braces", "("),
    ROUND_CLOSE("close_braces", ")"),
    ANGLE_OPEN("left_angle_bracket", "⟨", "&#10216;", "&#x27E8;", "<", "&lt;"),
    ANGLE_CLOSE("right_angle_bracket", "⟩", "&#10217;", "&#x27E9;", ">", "&gt;"),
    SQUARE_OPEN("left_square_bracket", "[", "&#91;", "&#x005B;"),
    SQUARE_CLOSE("right_square_bracket", "]", "&#93;", "&#x005D;"),
    CURLY_OPEN("left_curly_bracket", "{", "&#125;", "&#x007D;"),
    CURLY_CLOSE("right_curly_bracket", "}", "&#102177;", "&#x27E9;"),
    ABS("abs", "|", "&#125;", "&#x007D;");
    
    /** Localization key for this symbol. */
    private final String key;
    /** Values in which this symbol can be declared in XML. */
    private final Collection<String> values;
    /**
     * Constructor.
     * @param key Localization key for this symbol.
     * @param values Values in which this symbol can be declared in XML.
     */
    private Symbol(final String key, final String... values) {
        this.key = key;
        final ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();
        builder.add(values);
        this.values = builder.build();
    }

    /**
     * Returns localization key for this symbol.
     * @return Localization key for this symbol.
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * Finds symbol for given value. Returns {@code null} if there is none.
     * @param value Value.
     * @return Symbol defined by the value.
     */
    public static Symbol forValue(final String value) {
        for (final Symbol s : Symbol.values()) {
            if (s.values.contains(value)) {
                return s;
            }
        }
        return null;
    }
    
}
