<?xml version="1.0"?>

<!--
    Document   : mathml2text.xsl
    Created on : 1. únor 2011, 16:36
    Author     : Maros Kucbel
    Description:
        Generating text from MathML.
-->

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xpath-default-namespace="http://www.w3.org/1998/Math/MathML"
                xmlns:saxon="http://icl.com/saxon"
                extension-element-prefixes="saxon">

    <xsl:output method="text"
        indent="no"
        encoding="UTF-8"
        saxon:character-representation="native;hexadecimal"/>

    <xsl:strip-space elements="math"/>

    <xsl:param name="lang"/>
    <xsl:variable name="loc" select="document(concat($lang, '.xml'))/child::*" />

    <xsl:template match="/">
        <xsl:apply-templates select="math"/>
    </xsl:template>

    <!--DONE-->
    <xsl:template match="math">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="mrow">
        <xsl:choose>
            <xsl:when test="child::*[1]/name()='munder' or child::*[1]/name()='munderover' or child::*[1]/name()='msubsup'">
                <xsl:apply-templates select="child::*[1]" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="mrow" mode="root">
        <xsl:apply-templates />
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="mi" mode="root">
        <xsl:value-of select="."/>
        <xsl:if test="$lang='sk'">
            <xsl:value-of select="$loc/child::*[@name='root_suffix']"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mi">
        <xsl:param name="val" select="normalize-space(.)"/>
        <xsl:choose>
            <xsl:when test="$val='&#x391;' or $val='&#x3B1;'">
                <xsl:value-of select="$loc/child::*[@name='alpha']"/>
            </xsl:when>
            <xsl:when test="$val='&#x392;' or $val='&#x3B2;'">
                <xsl:value-of select="$loc/child::*[@name='beta']"/>
            </xsl:when>
            <xsl:when test="$val='&#x393;' or $val='&#x3B3;'">
                <xsl:value-of select="$loc/child::*[@name='gamma']"/>
            </xsl:when>
            <xsl:when test="$val='&#x394;' or $val='&#x3B4;'">
                <xsl:value-of select="$loc/child::*[@name='delta']"/>
            </xsl:when>
            <xsl:when test="$val='&#x395;' or $val='&#x3B5;'">
                <xsl:value-of select="$loc/child::*[@name='epsilon']"/>
            </xsl:when>
            <xsl:when test="$val='&#x396;' or $val='&#x3B6;'">
                <xsl:value-of select="$loc/child::*[@name='zeta']"/>
            </xsl:when>
            <xsl:when test="$val='&#x397;' or $val='&#x3B7;'">
                <xsl:value-of select="$loc/child::*[@name='eta']"/>
            </xsl:when>
            <xsl:when test="$val='&#x398;' or $val='&#x3B8;'">
                <xsl:value-of select="$loc/child::*[@name='theta']"/>
            </xsl:when>
            <xsl:when test="$val='&#x399;' or $val='&#x3B9;'">
                <xsl:value-of select="$loc/child::*[@name='iota']"/>
            </xsl:when>
            <xsl:when test="$val='&#x39A;' or $val='&#x3BA;'">
                <xsl:value-of select="$loc/child::*[@name='kappa']"/>
            </xsl:when>
            <xsl:when test="$val='&#x39B;' or $val='&#x3BB;'">
                <xsl:value-of select="$loc/child::*[@name='lambda']"/>
            </xsl:when>
            <xsl:when test="$val='&#x39C;' or $val='&#x3BC;'">
                <xsl:value-of select="$loc/child::*[@name='mu']"/>
            </xsl:when>
            <xsl:when test="$val='&#x39D;' or $val='&#x3BD;'">
                <xsl:value-of select="$loc/child::*[@name='nu']"/>
            </xsl:when>
            <xsl:when test="$val='&#x39E;' or $val='&#x3BE;'">
                <xsl:value-of select="$loc/child::*[@name='xi']"/>
            </xsl:when>
            <xsl:when test="$val='&#x39F;' or $val='&#x3BF;'">
                <xsl:value-of select="$loc/child::*[@name='omicron']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A0;' or $val='&#x3C0;'">
                <xsl:value-of select="$loc/child::*[@name='pi']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A1;' or $val='&#x3C1;'">
                <xsl:value-of select="$loc/child::*[@name='rho']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A3;' or $val='&#x3C3;' or $val='&#x3C2;'">
                <xsl:value-of select="$loc/child::*[@name='sigma']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A4;' or $val='&#x3C4;'">
                <xsl:value-of select="$loc/child::*[@name='tau']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A5;' or $val='&#x3C5;'">
                <xsl:value-of select="$loc/child::*[@name='upsilon']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A6;' or $val='&#x3C6;'">
                <xsl:value-of select="$loc/child::*[@name='phi']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A7;' or $val='&#x3C7;'">
                <xsl:value-of select="$loc/child::*[@name='chi']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A8;' or $val='&#x3C8;'">
                <xsl:value-of select="$loc/child::*[@name='psi']"/>
            </xsl:when>
            <xsl:when test="$val='&#x3A9;' or $val='&#x3C9;'">
                <xsl:value-of select="$loc/child::*[@name='omega']"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="following-sibling::*[1]/name()='mi'">
            <xsl:text> </xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mn" mode="root">
        <xsl:text>NMBZ</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>:</xsl:text>
    </xsl:template>

    <xsl:template match="mn" mode="squared">
        <xsl:choose>
            <xsl:when test=". = 2">
                <xsl:value-of select="$loc/child::*[@name='squared']"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>NMBR</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>:</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="mn">
        <xsl:text>NMBR</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>:</xsl:text>
    </xsl:template>

    <xsl:template match="mo">
        <xsl:param name="op" select="normalize-space(.)"/>
        <xsl:choose>
            <xsl:when test="$op='+'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='add']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='-' or $op='&#8722;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='subtract']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='*' or $op='&#x2062;' or $op='&#x2061;' or $op='&#215;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='multiply']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='/' or $op='&#247;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='divide']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='='">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='equals']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#177;' or $op='&#x00B1;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='add']"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='subtract']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='ln'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='natural_logarithm']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='sin'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='sine']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='cos'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='cosine']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='tan'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='tangent']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='cotg'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='cotangent']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='lim'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='lim']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='(' or $op='{'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='open_braces']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op=')' or $op='}'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='close_braces']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#8747;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='integral']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#8518;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='dee']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#60;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='lt']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#62;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='gt']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#8804;' or $op='&#x2264;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='leq']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#8805;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='geq']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="$op='&#x02061;'">
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>ERROR:&lt;</xsl:text>
                <xsl:value-of select="./name()"/>
                <xsl:text>&gt;</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>&lt;/</xsl:text>
                <xsl:value-of select="./name()"/>
                <xsl:text>&gt;</xsl:text>
                <xsl:text>:</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="mfrac[@linethickness=0]">
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='binomial_coefficient']"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="child::*[1]"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='choose']"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="child::*[2]"/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="mfrac">
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='fraction']"/>
        <xsl:choose>
            <xsl:when test="count(child::*[1]/child::*) > 1">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='open_braces']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[1]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='close_braces']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[1]"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='divided_by']"/>
        <xsl:text> </xsl:text>
        <xsl:choose>
            <xsl:when test="count(child::*[2]/child::*) > 1">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='open_braces']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='close_braces']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="msub">
        <xsl:choose>
            <xsl:when test="child::*[1]/text() = 'log' and child::*[2]/text() = 'e'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='natural_logarithm']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:when test="child::*[1]/text() = 'log'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='logarithm_base']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='logarithm_from']"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[1]"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="msup">
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="child::*[1]"/>
        <xsl:text> </xsl:text>
        <xsl:if test="child::*[2]!='2'">
            <xsl:value-of select="$loc/child::*[@name='superscript']"/>
            <xsl:text> </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="child::*[2]" mode="squared"/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="msqrt">
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='square_root']"/>
        <xsl:text> </xsl:text>
        <xsl:if test="count(child::*) > 1">
            <xsl:text> </xsl:text>
            <xsl:value-of select="$loc/child::*[@name='open_braces']"/>
            <xsl:text> </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="child::*"/>
        <xsl:if test="count(child::*) > 1">
            <xsl:text> </xsl:text>
            <xsl:value-of select="$loc/child::*[@name='close_braces']"/>
            <xsl:text> </xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mroot">
<!--  doriesit sklonovanie      -->
        <xsl:if test="count(child::*[2]/child::*) > 1">
            <xsl:text> </xsl:text>
            <xsl:value-of select="$loc/child::*[@name='open_braces']"/>
            <xsl:text> </xsl:text>
        </xsl:if>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="child::*[2]" mode="root"/>
        <xsl:text> </xsl:text>
        <xsl:if test="count(child::*[2]/child::*) > 1">
            <xsl:text> </xsl:text>
            <xsl:value-of select="$loc/child::*[@name='close_braces']"/>
            <xsl:text> </xsl:text>
        </xsl:if>
        <xsl:value-of select="$loc/child::*[@name='root']"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="child::*[1]"/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="ms">
        <xsl:text> "</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>" </xsl:text>
    </xsl:template>

    <xsl:template match="mtext">
        <xsl:text> </xsl:text>
        <xsl:value-of select="."/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="mfenced">
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='open_braces']"/>
        <xsl:text> </xsl:text>
        <xsl:for-each select="child::*">
            <xsl:apply-templates select="."/>
            <xsl:if test="count(./following-sibling::*)&gt;0">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text> </xsl:text>
        <xsl:value-of select="$loc/child::*[@name='close_braces']"/>
        <xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="munder">
        <xsl:choose>
            <xsl:when test="child::*[1]='lim'">
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[1]"/>
                <xsl:text> </xsl:text>

                <xsl:apply-templates select="following-sibling::*[1]"/>

                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='as']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]/child::*[1]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='approaches']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]/child::*[3]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>ERROR:&lt;</xsl:text>
                <xsl:value-of select="./name()"/>
                <xsl:text>&gt;</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>&lt;/</xsl:text>
                <xsl:value-of select="./name()"/>
                <xsl:text>&gt;</xsl:text>
                <xsl:text>:</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="munderover | msubsup">
        <xsl:choose>
            <xsl:when test="child::*[1]='&#8747;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='definite']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[1]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='from']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='to']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[3]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='of']"/>
                <xsl:text> </xsl:text>

                <xsl:apply-templates select="following-sibling::*[1]"/>
            </xsl:when>
            <xsl:when test="child::*[1]='&#8721;'">
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='sum']"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='from']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='to']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[3]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='of']"/>
                <xsl:text> </xsl:text>

                <xsl:apply-templates select="following-sibling::*[1]"/>
            </xsl:when>
            <xsl:when test="child::*[1]/name()='mi'">
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[1]"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[2]"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="$loc/child::*[@name='superscript']"/>
                <xsl:text> </xsl:text>
                <xsl:apply-templates select="child::*[3]"/>
                <xsl:text> </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>ERROR:&lt;</xsl:text>
                <xsl:value-of select="./name()"/>
                <xsl:text>&gt;</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>&lt;/</xsl:text>
                <xsl:value-of select="./name()"/>
                <xsl:text>&gt;</xsl:text>
                <xsl:text>:</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="mspace | mpadded | mstyle"/>

    <xsl:template match="child::*" priority="-1">
        <xsl:text>ERROR:&lt;</xsl:text>
        <xsl:value-of select="./name()"/>
        <xsl:text>&gt;</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>&lt;/</xsl:text>
        <xsl:value-of select="./name()"/>
        <xsl:text>&gt;</xsl:text>
        <xsl:text>:</xsl:text>
    </xsl:template>

</xsl:stylesheet>
