package cz.muni.fi.mathml.mathml2text.converter.operation;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;


/**
 * Enumeration of supported mathematical operations and their possible 
 * representation in presentation MathML (<code>&lt;mo&gt;</code>, <code>&lt;mi&gt;</code>) 
 * as well as in content MathML (<code>&lt;csymbol&gt;</code>, <code>&lt;ci&gt;</code>).
 * 
 * @author Maros Kucbel
 * @date 2012-09-16T16:54:09+0100
 */
public enum Operation {
    /**
     * Addition operator.
     */
    ADD("plus", OperationType.INFIX_OR_PREFIX, "+", "&plus;", "plus"),
    /**
     * Subtraction operator.
     */
    SUBTRACT("minus", OperationType.INFIX_OR_PREFIX, "-", "&minus;", "minus"),
    /**
     * Multiplication operator.
     */
    MULTIPLY("times", OperationType.INFIX, "&times;", "*", "times", "&sdot;", "&#8901;", "&#x22C5;", "⋅", 
            "&#8290;", "&#x2062;", "⁢", "&#8727;", "&#x2217;", "∗", "∙", "&#8729;", "&#x2219;", "&lowast;"),
    /**
     * Division operator.
     */
    DIVIDE("divide", OperationType.INFIX, "/", "&divide;", "divide", ":"),
    /**
     * Equals.
     */
    EQUALS("equals", OperationType.INFIX, "=", "eq"),
    /**
     * Logarithm.
     */
    LOGARITHM("logarithm", OperationType.SPECIAL, "log"),
    /**
     * Natural logarithm.
     */
    NATURAL_LOGARITHM("natural_logarithm", OperationType.PREFIX, "ln"),
    /**
     * Square. (to the power of 2)
     */
    SQUARE("square", OperationType.SPECIAL, "2"),
    /**
     * Limit.
     */
    LIMIT("limit", OperationType.SPECIAL, "lim", "limit"),
    /**
     * A variable approaches a value.
     */
    APPROACHES("approaches", OperationType.INFIX, "&rarr;", "rarr", "→"),
    /**
     * Integral.
     */
    INTEGRAL("integral", OperationType.WITH_INTERVAL, "int", "&int;"),
    /**
     * Contour integral.
     */
    CONTOUR_INTEGRAL("contour_integral", OperationType.WITH_INTERVAL, "&#8750;", "&#x222E;", "∮"),
    /**
     * Differential of integral.
     */
    DIFFERENTIAL("differential", OperationType.SPECIAL, "dd", "&dd;", "&#8518;", "&#x2146;", "ⅆ"),
    /**
     * Summation.
     */
    SUMMATION("summation", OperationType.WITH_INTERVAL, "sum", "&sum;"),
    /**
     * Product.
     */
    PRODUCT("product", OperationType.WITH_INTERVAL, "prod", "&prod;", "∏", "&amp;prod;"),
    /**
     * Sine.
     */
    SIN("sin", OperationType.PREFIX, "sin"),
    /**
     * Cosine.
     */
    COS("cos", OperationType.PREFIX, "cos"),
    /**
     * Tangent
     */
    TAN("tan", OperationType.PREFIX, "tan"),
    /**
     * Secant.
     */
    SEC("sec", OperationType.PREFIX, "sec"),
    /**
     * Cosecant.
     */
    CSC("csc", OperationType.PREFIX, "csc"),
    /**
     * Cotangent.
     */
    COT("cot", OperationType.PREFIX, "cot", "cotg"),
    /**
     * Hyperbolic sine.
     */
    SINH("sinh", OperationType.PREFIX, "sinh"),
    /**
     * Hyperbolic cosine.
     */
    COSH("cosh", OperationType.PREFIX, "cosh"),
    /**
     * Hyperbolic tangent.
     */
    TANH("tanh", OperationType.PREFIX, "tanh"),
    /**
     * Hyperbolic secant.
     */
    SECH("sech", OperationType.PREFIX, "sech"),
    /**
     * Hyperbolic cosecant.
     */
    CSCH("csch", OperationType.PREFIX, "csch"),
    /**
     * Hyperbolic cotangent.
     */
    COTH("coth", OperationType.PREFIX, "coth"),
    /**
     * Arcsine.
     */
    ARCSIN("arcsin", OperationType.PREFIX, "arcsin"),
    /**
     * Arccosine.
     */
    ARCCOS("arccos", OperationType.PREFIX, "arccos"),
    /**
     * Arctangent.
     */
    ARCTAN("arctan", OperationType.PREFIX, "arctan"),
    /**
     * Inverse hyperbolic cosine.
     */
    ARCCOSH("arccosh", OperationType.PREFIX, "arccosh"),
    /**
     * Arccotangent.
     */
    ARCCOT("arccot", OperationType.PREFIX, "arccot"),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCCOTH("arccoth", OperationType.PREFIX, "arccoth"),
    /**
     * Inverse cosecant.
     */
    ARCCSC("arccsc", OperationType.PREFIX, "arccsc"),
    /**
     * Inverse hyperbolic cosecant.
     */
    ARCCSCH("arccsch", OperationType.PREFIX, "arccsch"),
    /**
     * Inverse secant.
     */
    ARCSEC("arcsec", OperationType.PREFIX, "arcsec"),
    /**
     * Inverse hyperbolic secant.
     */
    ARCSECH("arcsech", OperationType.PREFIX, "arcsech"),
    /**
     * Inverse hyperbolic sine.
     */
    ARCSINH("arcsinh", OperationType.PREFIX, "arcsinh"),
    /**
     * Inverse hyperbolic tangent.
     */
    ARCTANH("arctanh", OperationType.PREFIX, "arctanh"),
    /**
     * Not equal.
     */
    NOT_EQUAL_TO("neq", OperationType.INFIX, "neq", "&neq;", "&ne;", "&#8800;", "&#x2260;"),
    /**
     * Greater than.
     */
    GREATER_THAN("gt", OperationType.INFIX, "gt", "&gt;"),
    /**
     * Less than.
     */
    LESS_THAN("lt", OperationType.INFIX, "lt", "&lt;"),
    /**
     * Greater than or equal to.
     */
    GREATER_THAN_OR_EQUAL_TO("geq", OperationType.INFIX, "geq", "&geq;", "&ge;", "&#10878;", "&#x2A7E;", "⩾", "&#8807;", "&#x2267;", "≧"),
    /**
     * Less than or equal to.
     */
    LESS_THAN_OR_EQUAL_TO("leq", OperationType.INFIX, "leq", "&leq;", "&le;", "&#10877;", "&#x2A7D;", "⩽", "&#8806;", "&#x2266;", "≦"),
    /**
     * Equivalence.
     */
    EQUIVALENT_TO("equivalent", OperationType.INFIX, "equivalent", "&hArr;", "⇔", "&#8596;", "&#x2194;", "↔", "&harr;", "&hArr;"),
    /**
     * Approximation.
     */
    APPROXIMATELY("approx", OperationType.INFIX, "approx", "≈"),
    /**
     * Logical and.
     */
    AND("and", OperationType.INFIX, "and", "&and;"),
    /**
     * Logical or.
     */
    OR("or", OperationType.INFIX, "or", "&or;"),
    /**
     * Logical xor.
     */
    XOR("xor", OperationType.INFIX, "xor"),
    /**
     * Logical implication.
     */
    IMPLIES("implies", OperationType.INFIX, "implies", "&#8658;", "&#x21D2;", "⇒", "&rArr;"),
    /**
     * Factor of.
     */
    FACTOR_OF("factorof", OperationType.INFIX, "factorof"),
    /**
     * Union of sets.
     */
    UNION("union", OperationType.INFIX, "union", "&#8746;", "&#x222A;", "∪", "&cup;"),
    /**
     * Intersection of sets.
     */
    INTERSECT("intersect", OperationType.INFIX, "intersect", "&#8745;", "&#x2229;", "∩", "&cap;"),
    /**
     * Set inclusion.
     */
    IN("in", OperationType.INFIX, "in", "∈", "&isin;", "&#8714;", "&#x220A;", "&#8712;", "&#x2208;", "∊"),
    /**
     * Negated set inclusion.
     */
    NOTIN("notin", OperationType.INFIX, "notin", "&#8713;", "&#x2209;", "∉", "&notin;"),
    /**
     * Subset.
     */
    SUBSET("subset", OperationType.INFIX, "subset", "⊂", "&sub;"),
    /**
     * Proper subset.
     */
    PRSUBSET("prsubset", OperationType.INFIX, "prsubset"),
    /**
     * Negated subset.
     */
    NOTSUBSET("notsubset", OperationType.INFIX, "notsubset"),
    /**
     * Negated proper subset.
     */
    NOTPRSUBSET("notprsubset", OperationType.INFIX, "notprsubset"),
    /**
     * Set difference.
     */
    SETDIFF("setdiff", OperationType.INFIX, "setdiff"),
    /**
     * Logical negation.
     */
    NOT("not", OperationType.PREFIX, "not"),
    /**
     * Absolute value.
     */
    ABSOLUTE_VALUE("abs", OperationType.PREFIX, "abs", "|"),
    /**
     * Function that rounds down to the nearest integer.
     */
    FLOOR("floor", OperationType.PREFIX, "floor"),
    /**
     * Function that rounds up to the nearest integer.
     */
    CEILING("ceiling", OperationType.PREFIX, "ceiling"),
    /**
     * Exponentiation with base <i>e</i>.
     */
    EXPONENTIAL("exp", OperationType.PREFIX, "exp"),
    /**
     * Set cardinality.
     */
    CARDINALITY("card", OperationType.SPECIAL, "card"),
    /**
     * Remainder.
     */
    REMAINDER("rem", OperationType.SPECIAL, "rem"),
    /**
     * Integer division operator.
     */
    QUOTIENT("quotient", OperationType.SPECIAL, "quotient"),
    /**
     * Exponentiation.
     */
    EXPONENTIATION("power", OperationType.SPECIAL, "power"),
    /**
     * Root extraction.
     */
    ROOT("root", OperationType.SPECIAL, "root", "&radic;", "&#8730;", "&#x221A;", "√"),
    /**
     * Open parentheses.
     */
    OPEN_BRACES("open_braces", OperationType.SPECIAL, "("),
    /**
     * Close parentheses.
     */
    CLOSE_BRACES("close_braces", OperationType.SPECIAL, ")"),
    /**
     * Open angle braces.
     */
    LEFT_ANGLE_BRACKET("left_angle_bracket", OperationType.SPECIAL, "⟨", "&#10216;", "&#x27E8;"),
    /**
     * Close angle braces.
     */
    RIGHT_ANGLE_BRACKET("right_angle_bracket", OperationType.SPECIAL, "⟩", "&#10217;", "&#x27E9;"),
    /**
     * Open square braces.
     */
    LEFT_SQUARE_BRACKET("left_square_bracket", OperationType.SPECIAL, "[", "&#91;", "&#x005B;"),
    /**
     * Close square braces.
     */
    RIGHT_SQUARE_BRACKET("right_square_bracket", OperationType.SPECIAL, "]", "&#93;", "&#x005D;"),
    /**
     * Open curly braces.
     */
    LEFT_CURLY_BRACKET("left_curly_bracket", OperationType.SPECIAL, "{", "&#125;", "&#x007D;"),
    /**
     * Close curly braces.
     */
    RIGHT_CURLY_BRACKET("right_curly_bracket", OperationType.SPECIAL, "}", "&#102177;", "&#x27E9;"),
    /**
     * Superscript.
     */
    SUPERSCRIPT("superscript", OperationType.SPECIAL, "superscript", "sup"),
    /**
     * Subscript.
     */
    SUBSCRIPT("subscript", OperationType.SPECIAL, "subscript", "sub"),
    /**
     * Some files use ci instead of csymbol to express function.
     */
    CI("ci", OperationType.SPECIAL),
    /**
     * Assign value to variable.
     */
    ASSIGN("assign", OperationType.SPECIAL, ":="),
    /**
     * Tilde above a letter.
     */
    TILDE("tilde", OperationType.EVERY_ARGUMENT, "~", "&#126;", "&#x007E;", "&#x7E;"),
    /**
     * Dash above a letter.
     */
    DASHED("dashed", OperationType.EVERY_ARGUMENT, "¯", "&#175;", "&#x00AF;", "&macr;"),
    /**
     * Interval.
     */
    INTERVAL("interval", OperationType.SPECIAL, "interval"),
    /**
     * Function composition.
     */
    COMPOSE("compose", OperationType.SPECIAL, "compose"),
    /**
     * Vector (a matrix with one column).
     */
    VECTOR("vector", OperationType.SPECIAL, "vector"),
    /**
     * Direct sum.
     */
    DIRECT_SUM("direct_sum", OperationType.INFIX, "&oplus;", "⊕"),
    /**
     * Plus minus.
     */
    PLUS_MINUS("plus_minus", OperationType.INFIX, "&plusmn;", "±"),
    /**
     * Minus plus
     */
    MINUS_PLUS("minus_plus", OperationType.INFIX, "&#8723;", "&#x2213;", "∓"),
    /**
     * Almost equal.
     */
    SIMEQ("simeq", OperationType.INFIX, "≃"),
    /**
     * Roughly similar.
     */
    SIM("sim", OperationType.INFIX, "∼", "&#8764;", "&#x223C;", "&sim;"),
    /**
     * Far greater than.
     */
    FAR_GT("far_gt", OperationType.INFIX, "≫"),
    /**
     * Far lower than.
     */
    FAR_LT("far_lt", OperationType.INFIX, "≪"),
    /**
     * Comma.
     */
    COMMA("comma", OperationType.INFIX, ",", "&#44;", "&#8291;", "&#x2063;", "⁣"),
    /**
     * Semicolon.
     */
    SEMICOLON("semicolon", OperationType.INFIX, ";"),
    /**
     * Dot.
     */
    DOT("dot", OperationType.INFIX, ".", "&#46;"),
    /**
     * Proportional to.
     */
    PROPORTIONAL_TO("proportional_to", OperationType.INFIX, "∝", "&#8733;", "&#x221D;", "&prop;", "prop"),
    /**
     * Less than or equivalent to.
     */
    LT_EQUIV("lt_equiv", OperationType.INFIX, "≲", "&#8818;", "&#x2272;"),
    /**
     * Greater than or equivalent to.
     */
    GT_EQUIV("gt_equiv", OperationType.INFIX, "≳", "&#8819;", "&#x2273;"),
    /**
     * Identical to.
     */
    IDENTICAL("identical_to", OperationType.INFIX, "≡", "&#8801;", "&#x2261;", "&equiv;"),
    /**
     * Set.
     */
    SET("set", OperationType.PREFIX_MULTI, "set"),
    /**
     * List.
     */
    LIST("list", OperationType.PREFIX_MULTI, "list"),
    /**
     * Prime - minutes, feet.
     */
    PRIME("prime", OperationType.INFIX, "&#8242;", "&#x2032;", "&prime;"),
    /**
     * Double prime - seconds, inches.
     */
    DOUBLE_PRIME("double_prime", OperationType.INFIX, "&#8243;", "&#x2033;", "&Prime;"),
    /**
     * A dot above a letter.
     */
    DOT_ABOVE("dot_above", OperationType.EVERY_ARGUMENT, "&#729;", "&#x2D9;", "˙"),
    /**
     * An up pointing arrowhead above a letter.
     */
    UP_ARROWHEAD("up_arrowhead", OperationType.EVERY_ARGUMENT, "&#8963;", "&#x2303;", "⌃", "&#94;", "&#x005E;", "^"),
    /**
     * Orthogonal to.
     */
    PERPENDICULAR("perpendicular", OperationType.INFIX, "&#10178;", "&#x27C2;", "⟂", "&#8869;", "&#x22A5;", "⊥", "&perp;"),
    /**
     * Diaeresis above a letter.
     */
    DIAERESIS("diaeresis", OperationType.EVERY_ARGUMENT, "¨", "&#168;", "&#x00A8;", "&uml;"),
    /**
     * Partial differentiation.
     */
    PARTIALDIFF("partialdiff", OperationType.PREFIX, "partialdiff", "&#8706;", "&#x2202;", "&part;"),
    /**
     * Factorial.
     */
    FACTORIAL("factorial", OperationType.PREFIX, "factorial", "!"),
    /**
     * Minimum.
     */
    MINIMUM("min", OperationType.PREFIX_MULTI, "min"),
    /**
     * Maximum.
     */
    MAXIMUM("max", OperationType.PREFIX_MULTI, "max"),
    /**
     * Direct product.
     */
    DIRECT_PRODUCT("direct_product", OperationType.INFIX, "&#8857;", "&#x2299;", "⊙"),
    /**
     * Backward difference.
     */
    BACKWARD_DIFFERENCE("backward_difference", OperationType.PREFIX, "&#8711;", "&#x2207;", "∇", "&nabla;"),
    /**
     * Asymptotic to.
     */
    ASYMPTOTIC_TO("asymptotic_to", OperationType.INFIX, "&#8776;", "&#x2248;", "≈", "&asymp;"),
    /**
     * For all.
     */
    FOR_ALL("for_all", OperationType.PREFIX_MULTI, "&#8704;", "&#x2200;", "forall", "∀"),
    /**
     * Exists.
     */
    EXISTS("exists", OperationType.PREFIX_MULTI, "exists"),
    /**
     * Divides.
     */
    DIVIDES("divides", OperationType.INFIX, "&#8739;", "&#x2223;", "∣"),
    /**
     * Tensor product.
     */
    TENSOR_PRODUCT("tensor_product", OperationType.INFIX, "&#8855;", "&#x2297;", "⊗", "&otimes;"),
    /**
     * Maps to.
     */
    MAPS_TO("maps_to", OperationType.INFIX, "↦", "&#8614;", "&#x21A6;"),
    /**
     * Greater than equal to or less than.
     */
    GREATER_THAN_EQUAL_TO_OR_LESS_THAN("greater_equal_less", OperationType.INFIX, "&#8923;", "&#x22DB;", "⋛"),
    /**
     * Set minus.
     */
    SET_MINUS("set_minus", OperationType.INFIX, "&#8726;", "&#x2216;", "∖", "\\"),
    /**
     * Subset or equal to.
     */
    SUBSET_EQUAL("subset_equal", OperationType.INFIX, "&#8838;", "&#x2286;", "⊆", "&sube;"),
    /**
     * Transpose.
     */
    TRANSPOSE("transpose", OperationType.EVERY_ARGUMENT, "†", "&#8224;", "&#x2020;", "&dagger;"),
    /**
     * Upwards arrow.
     */
    UPWARDS_ARROW("upwards_arrow", OperationType.SPECIAL, "↑", "&#8593;", "&#x2191;", "&uarr;"),
    /**
     * Downwards arrow.
     */
    DOWNWARDS_ARROW("downwards_arrow", OperationType.SPECIAL, "↓", "&#8595;", "&#x2193;"),
    /**
     * Less than or approximate.
     */
    LESS_THAN_OR_APPROXIMATE("less_than_or_approximate", OperationType.INFIX, "&#10885;", "&#x2A85;", "⪅"),
    /**
     * Greater than or approximate.
     */
    GREATER_THAN_OR_APPROXIMATE("greater_than_or_approximate", OperationType.INFIX, "&#10886;", "&#x2A86;", "⪆"),
    /**
     * Function application.
     */
    FUNCTION_APPLICATION("function_application", OperationType.SPECIAL, "&#8289;", "&#x2061;", "⁡"),
    /**
     * Approaches the limit.
     */
    APPROACHES_THE_LIMIT("approaches_the_limit", OperationType.INFIX, "&#8784;", "&#x2250;", "≐"),
    /**
     * Ring above a letter.
     */
    RING_ABOVE("ring_above", OperationType.EVERY_ARGUMENT, "̊", "&#778;", "&#x030A;"),
    /**
     * Double integral.
     */
    DOUBLE_INTEGRAL("double_integral", OperationType.WITH_INTERVAL, "&#8748;", "&#x222C;", "∬"),
    /**
     * Entails.
     */
    ENTAILS("entails", OperationType.INFIX, "⊧", "&#8871;", "&#x22A7;"),
    /**
     * Inference.
     */
    INFERS("infers", OperationType.INFIX, "⊢", "&#8866;", "&#x22A2;"),
    /**
     * Superset or equal.
     */
    SUPERSET_EQUAL("superset_equal", OperationType.INFIX, "&#8839;", "&#x2287;", "⊇", "&supe;"),
    /**
     * Superset.
     */
    SUPERSET("superset", OperationType.INFIX, "&#8835;", "&#x2283;", "⊃", "&sup;"),
    /**
     * Not identical.
     */
    NOT_IDENTICAL("not_identical", OperationType.INFIX, "&#8802;", "&#x2262;", "≢"),
    /**
     * Precedes or equal.
     */
    PRECEDES_OR_EQUAL("precedes_or_equal", OperationType.INFIX, "&#10927;", "&#x2AAF;", "⪯", "&#8828;", "&#x227C;", "≼"),
    /**
     * Succeeds or equal.
     */
    SUCCEEDS_OR_EQUAL("succeeds_or_equal", OperationType.INFIX, "&#10928;", "&#x2AB0;", "⪰", "&#8829;", "&#x227D;", "≽"),
    /**
     * Delta.
     */
    DELTA("delta", OperationType.INFIX, "Δ", "&#916;", "&#x0394;"),
    /**
     * Contains as member.
     */
    CONTAINS_AS_MEMBER("contains_as_member", OperationType.INFIX, "&#8715;", "&#x220B;", "∋", "∍", "&#8717;", "&#x220D;", "&ni;"),
    /**
     * Wreath product.
     */
    WREATH_PRODUCT("wreath_product", OperationType.INFIX, "&#8768;", "&#x2240;", "≀"),
    /**
     * Breve.
     */
    BREVE("breve", OperationType.EVERY_ARGUMENT, "&#728;", "&#x02D8;", "˘"),
    /**
     * Not superset.
     */
    NOT_SUPERSET("not_superset", OperationType.INFIX, "&#8837;", "&#x2285;", "⊅"),
    /**
     * Succeeds or not equal to.
     */
    SUCCEEDS_OR_NOT_EQUAL("succeeds_or_not_equal", OperationType.INFIX, "&#10934;", "&#x2AB6;", "⪶"),
    /**
     * Not subset.
     */
    NOT_SUBSET("not_subset", OperationType.INFIX, "&#8836;", "&#x2284;", "⊄"),
    /**
     * Precedes.
     */
    PRECEDES("precedes", OperationType.INFIX, "&#8826;", "&#x227A;", "≺"),
    /**
     * Succeeds.
     */
    SUCCEEDS("succeeds", OperationType.INFIX, "&#8827;", "&#x227B;", "≻"),
    /**
     * Subset not equal.
     */
    SUBSET_NOT_EQUAL("subset_not_equal", OperationType.INFIX, "&#8842;", "&#x228A;", "⊊"),
    /**
     * Normal subgroup.
     */
    NORMAL_SUBGROUP("normal_subgroup", OperationType.INFIX, "&#8882;", "&#x22B2;", "⊲"),
    /**
     * Neither less nor equal to.
     */
    NOT_LESS_NOT_EQUAL("not_less_not_equal", OperationType.INFIX, "&#8816;", "&#x2270;", "≰"),
    /**
     * Coproduct.
     */
    COPRODUCT("coproduct", OperationType.WITH_INTERVAL, "&#8720;", "&#x2210;", "∐"),
    /**
     * Normal subgroup or equal to.
     */
    NORMAL_SUBGROUP_OR_EQUAL("normal_subgroup_or_equal", OperationType.INFIX, "&#8884;", "&#x22B4;", "⊴"),
    /**
     * Not precede or equal.
     */
    NOT_PRECEDE_OR_EQUAL("not_precede_or_equal", OperationType.INFIX, "&#8928;", "&#x22E0;", "⋠"),
    /**
     * Netheir subset or equal to.
     */
    NOT_SUBSET_NOT_EQUAL("not_subset_not_equal", OperationType.INFIX, "&#8840;", "&#x2288;", "⊈"),
    /**
     * Symmetric diffrence.
     */
    SYMMETRIC_DIFFERENCE("symmetric_diffrence", OperationType.INFIX, "&#8854;", "&#x2296;", "⊖"),
    /**
     * Not less than.
     */
    NOT_LESS("not_less", OperationType.INFIX, "&#8814;", "&#x226E;", "≮"),
    /**
     * Does not exist.
     */
    NOT_EXISTS("not_exists", OperationType.PREFIX_MULTI, "&#8708;", "&#x2204;", "∄"),
    /**
     * Lowest common multiple.
     */
    LCM("lcm", OperationType.PREFIX_MULTI, "lcm"),
    /**
     * Greatest common divisor.
     */
    GCD("gcd", OperationType.PREFIX_MULTI, "gcd"),
    /**
     * Congruent to.
     */
    CONGRUENT("congruent", OperationType.INFIX, "&#8773;", "&#x2245;", "&cong;", "≅"),
    /**
     * End of proof.
     */
    END_OF_PROOF("end_of_proof", OperationType.SPECIAL, "&#8718;", "&#x220E;", "∎"),
    /**
     * N-ary union.
     */
    UNION_NARY("union_nary", OperationType.WITH_INTERVAL, "&#8899;", "&#x22C3;", "⋃"),
    /**
     * N-ary logical and.
     */
    AND_NARY("and_nary", OperationType.WITH_INTERVAL, "&#8896;", "&#x22C0;", "⋀"),
    /**
     * N-ary logical or.
     */
    OR_NARY("or_nary", OperationType.WITH_INTERVAL, "&#8897;", "&#x22C1;", "⋁"),
    /**
     * N-ary intersection.
     */
    INTERSECT_NARY("intersect_nary", OperationType.WITH_INTERVAL, "&#8898;", "&#x22C2;", "⋂"),
    /**
     * Not almost equal to.
     */
    NOT_ALMOST_EQUAL("not_almost_equal", OperationType.INFIX, "&#8777;", "&#x2249;", "≉"),
    /**
     * Top element.
     */
    TOP_ELEMENT("top_element", OperationType.SPECIAL, "&#8868;", "&#x22A4;", "⊤"),
    /**
     * Contains as normal subgroup.
     */
    CONTAINS_AS_NORMAL_SUBGROUP("contains_as_normal_subgroup", OperationType.INFIX, "&#8883;", "&#x22B3;", "⊳"),
    /**
     * Function composition.
     */
    FUNCTION_COMPOSITION("function_composition", OperationType.INFIX, "&#8728;", "&#x2218;", "∘"),
    /**
     * Parallel to.
     */
    PARALLEL_TO("parallel_to", OperationType.INFIX, "&#8741;", "&#x2225;", "∥"),
    /**
     * Empty or {@code null} function.
     */
    EMPTY("empty", OperationType.INFIX);
    
    /**
     * Localization key for this operation.
     */
    private final String key;
    /**
     * Type of the operation.
     */
    private final OperationType type;
    /**
     * Possible symbols for this operation.
     */
    private final Collection<String> symbols; 
    /**
     * Constructor.
     * @param key Localization key.
     * @param symbols Possible symbols.
     */
    private Operation(final String key, final OperationType type, final String... symbols) {
        this.key = key;
        this.type = type;
        final ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();
        builder.add(symbols);
        this.symbols = builder.build();
    }
    
    /**
     * Returns localization key for this operation.
     * @return Localization key for this operation.
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * Returns the type of the operation.
     * @return Type of the operation.
     */
    public OperationType getType() {
        return this.type;
    }
    
    /**
     * Returns symbols for this operation.
     * @return Symbols for this operation.
     */
    public Collection<String> getSymbols() {
        return this.symbols;
    }
    
    /**
     * Finds operation for given symbol. Returns {@code null} if there isn't one.
     * @param symbol Symbol.
     * @return Operation for symbol.
     */
    public static Operation forSymbol(final String symbol) {
        if (StringUtils.isBlank(symbol)) {
            return Operation.EMPTY;
        }
        for (final Operation operation : Operation.values()) {
            if (operation.getSymbols().contains(symbol)) {
                return operation;
            }
        }
        return null;
    }
    
}
