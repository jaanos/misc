import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
//import henson.midp.Float;
 
public class ProvaMIDlet
  extends MIDlet
  implements ItemCommandListener {

  private Form mainForm;
  private Display display = Display.getDisplay(this);
  private TextField zaslon;
  private StringItem plus, minus, krat, deljeno, koren, jeenako, procent, plusminus, zbrisi, sin, cos, tan, asin, acos, atan, exp, log, log10, pow, n0, n1, n2, n3, n4, n5, n6, n7, n8, n9, pika, pi, e, sqrt, fact, inv, pobrisi, shrani, preberi, polje;
  private Command lastC, plusC, minusC, kratC, deljenoC, korenC, jeenakoC, procentC, plusminusC, zbrisiC, sinC, cosC, tanC, asinC, acosC, atanC, expC, logC, log10C, powC, n0C, n1C, n2C, n3C, n4C, n5C, n6C, n7C, n8C, n9C, pikaC, piC, eC, sqrtC, factC, invC, pobrisiC, shraniC, preberiC;
  private boolean started=false, clear=false;
  private Float x, m;

  public void startApp() {
    if (started) return;
    started=true;
    mainForm = new Form("Kalkulator");
    
    zaslon = new TextField(null,  "0", 128, TextField.DECIMAL);
    plus = new StringItem(null, "  +  ", Item.BUTTON);
    minus = new StringItem(null, "  -  ", Item.BUTTON);
    krat = new StringItem(null, "  *  ", Item.BUTTON);
    deljeno = new StringItem(null, "  /  ", Item.BUTTON);
    jeenako = new StringItem(null, "  =  ", Item.BUTTON);
    procent = new StringItem(null, "  %  ", Item.BUTTON);
    plusminus = new StringItem(null, "  +/-  ", Item.BUTTON);
    zbrisi = new StringItem(null, "  C  ", Item.BUTTON);
    sin = new StringItem(null, "  sin  ", Item.BUTTON);
    cos = new StringItem(null, "  cos  ", Item.BUTTON);
    tan = new StringItem(null, "  tan  ", Item.BUTTON);
    asin = new StringItem(null, "  asin  ", Item.BUTTON);
    acos = new StringItem(null, "  acos  ", Item.BUTTON);
    atan = new StringItem(null, "  atan  ", Item.BUTTON);
    exp = new StringItem(null, "  e^x  ", Item.BUTTON);
    log = new StringItem(null, "  log  ", Item.BUTTON);
    log10 = new StringItem(null, "  log10  ", Item.BUTTON);
    pow = new StringItem(null, "  x^y  ", Item.BUTTON);
    n0 = new StringItem(null, "  0  ", Item.BUTTON);
    n1 = new StringItem(null, "  1  ", Item.BUTTON);
    n2 = new StringItem(null, "  2  ", Item.BUTTON);
    n3 = new StringItem(null, "  3  ", Item.BUTTON);
    n4 = new StringItem(null, "  4  ", Item.BUTTON);
    n5 = new StringItem(null, "  5  ", Item.BUTTON);
    n6 = new StringItem(null, "  6  ", Item.BUTTON);
    n7 = new StringItem(null, "  7  ", Item.BUTTON);
    n8 = new StringItem(null, "  8  ", Item.BUTTON);
    n9 = new StringItem(null, "  9  ", Item.BUTTON);
    pika = new StringItem(null, "  .  ", Item.BUTTON);
    pi = new StringItem(null, "  pi  ", Item.BUTTON);
    e = new StringItem(null, "  e  ", Item.BUTTON);
    sqrt = new StringItem(null, "  sqrt  ", Item.BUTTON);
    fact = new StringItem(null, "  x!  ", Item.BUTTON);
    inv = new StringItem(null, "  x^-1  ", Item.BUTTON);
    pobrisi = new StringItem(null, "  CE  ", Item.BUTTON);
    shrani = new StringItem(null, "  M  ", Item.BUTTON);
    preberi = new StringItem(null, "  MR  ", Item.BUTTON);
    polje = new StringItem(null, "");
    mainForm.append(zaslon);
    mainForm.append(zbrisi);
    mainForm.append(pobrisi);
    mainForm.append(polje);
    mainForm.append("\n");
    mainForm.append(shrani);
    mainForm.append(preberi);
    mainForm.append("\n");
    mainForm.append(n1);
    mainForm.append(n2);
    mainForm.append(n3);
    mainForm.append("\n");
    mainForm.append(n4);
    mainForm.append(n5);
    mainForm.append(n6);
    mainForm.append("\n");
    mainForm.append(n7);
    mainForm.append(n8);
    mainForm.append(n9);
    mainForm.append("\n");
    mainForm.append(n0);
    mainForm.append(plusminus);
    mainForm.append(pika);
    mainForm.append("\n");
    mainForm.append(plus);
    mainForm.append(minus);
    mainForm.append(jeenako);
    mainForm.append("\n");
    mainForm.append(krat);
    mainForm.append(deljeno);
    mainForm.append(procent);
    mainForm.append("\n");
    mainForm.append(sin);
    mainForm.append(cos);
    mainForm.append(tan);
    mainForm.append("\n");
    mainForm.append(asin);
    mainForm.append(acos);
    mainForm.append(atan);
    mainForm.append("\n");
    mainForm.append(log);
    mainForm.append(log10);
    mainForm.append("\n");
    mainForm.append(exp);
    mainForm.append(pow);
    mainForm.append("\n");
    mainForm.append(sqrt);
    mainForm.append(fact);
    mainForm.append(inv);
    mainForm.append("\n");
    mainForm.append(pi);
    mainForm.append(e);
    Command exitCommand = new Command("Izhod", Command.EXIT, 0);
    n0C = new Command("0", Command.ITEM, 0);
    n0.setDefaultCommand(n0C);
    n0.setItemCommandListener(this);
    n1C = new Command("1", Command.ITEM, 0);
    n1.setDefaultCommand(n1C);
    n1.setItemCommandListener(this);
    n2C = new Command("2", Command.ITEM, 0);
    n2.setDefaultCommand(n2C);
    n2.setItemCommandListener(this);
    n3C = new Command("3", Command.ITEM, 0);
    n3.setDefaultCommand(n3C);
    n3.setItemCommandListener(this);
    n4C = new Command("4", Command.ITEM, 0);
    n4.setDefaultCommand(n4C);
    n4.setItemCommandListener(this);
    n5C = new Command("5", Command.ITEM, 0);
    n5.setDefaultCommand(n5C);
    n5.setItemCommandListener(this);
    n6C = new Command("6", Command.ITEM, 0);
    n6.setDefaultCommand(n6C);
    n6.setItemCommandListener(this);
    n7C = new Command("7", Command.ITEM, 0);
    n7.setDefaultCommand(n7C);
    n7.setItemCommandListener(this);
    n8C = new Command("8", Command.ITEM, 0);
    n8.setDefaultCommand(n8C);
    n8.setItemCommandListener(this);
    n9C = new Command("9", Command.ITEM, 0);
    n9.setDefaultCommand(n9C);
    n9.setItemCommandListener(this);
    pikaC = new Command(".", Command.ITEM, 0);
    pika.setDefaultCommand(pikaC);
    pika.setItemCommandListener(this);
    plusC = new Command("+", Command.ITEM, 0);
    plus.setDefaultCommand(plusC);
    plus.setItemCommandListener(this);
    minusC = new Command("-", Command.ITEM, 0);
    minus.setDefaultCommand(minusC);
    minus.setItemCommandListener(this);
    kratC = new Command("*", Command.ITEM, 0);
    krat.setDefaultCommand(kratC);
    krat.setItemCommandListener(this);
    deljenoC = new Command("/", Command.ITEM, 0);
    deljeno.setDefaultCommand(deljenoC);
    deljeno.setItemCommandListener(this);
    jeenakoC = new Command("=", Command.ITEM, 0);
    jeenako.setDefaultCommand(jeenakoC);
    jeenako.setItemCommandListener(this);
    procentC = new Command("%", Command.ITEM, 0);
    procent.setDefaultCommand(procentC);
    procent.setItemCommandListener(this);
    plusminusC = new Command("+/-", Command.ITEM, 0);
    plusminus.setDefaultCommand(plusminusC);
    plusminus.setItemCommandListener(this);
    zbrisiC = new Command("C", Command.ITEM, 0);
    zbrisi.setDefaultCommand(zbrisiC);
    zbrisi.setItemCommandListener(this);
    sinC = new Command("sin", Command.ITEM, 0);
    sin.setDefaultCommand(sinC);
    sin.setItemCommandListener(this);
    cosC = new Command("cos", Command.ITEM, 0);
    cos.setDefaultCommand(cosC);
    cos.setItemCommandListener(this);
    tanC = new Command("tan", Command.ITEM, 0);
    tan.setDefaultCommand(tanC);
    tan.setItemCommandListener(this);
    asinC = new Command("asin", Command.ITEM, 0);
    asin.setDefaultCommand(asinC);
    asin.setItemCommandListener(this);
    acosC = new Command("acos", Command.ITEM, 0);
    acos.setDefaultCommand(acosC);
    acos.setItemCommandListener(this);
    atanC = new Command("atan", Command.ITEM, 0);
    atan.setDefaultCommand(atanC);
    atan.setItemCommandListener(this);
    expC = new Command("e^", Command.ITEM, 0);
    exp.setDefaultCommand(expC);
    exp.setItemCommandListener(this);
    logC = new Command("log", Command.ITEM, 0);
    log.setDefaultCommand(logC);
    log.setItemCommandListener(this);
    log10C = new Command("log10", Command.ITEM, 0);
    log10.setDefaultCommand(log10C);
    log10.setItemCommandListener(this);
    powC = new Command("^", Command.ITEM, 0);
    pow.setDefaultCommand(powC);
    pow.setItemCommandListener(this);
    piC = new Command("pi", Command.ITEM, 0);
    pi.setDefaultCommand(piC);
    pi.setItemCommandListener(this);
    eC = new Command("e", Command.ITEM, 0);
    e.setDefaultCommand(eC);
    e.setItemCommandListener(this);
    sqrtC = new Command("sqrt", Command.ITEM, 0);
    sqrt.setDefaultCommand(sqrtC);
    sqrt.setItemCommandListener(this);
    factC = new Command("!", Command.ITEM, 0);
    fact.setDefaultCommand(factC);
    fact.setItemCommandListener(this);
    invC = new Command("^-1", Command.ITEM, 0);
    inv.setDefaultCommand(invC);
    inv.setItemCommandListener(this);
    pobrisiC = new Command("CE", Command.ITEM, 0);
    pobrisi.setDefaultCommand(pobrisiC);
    pobrisi.setItemCommandListener(this);
    shraniC = new Command("M", Command.ITEM, 0);
    shrani.setDefaultCommand(shraniC);
    shrani.setItemCommandListener(this);
    preberiC = new Command("MR", Command.ITEM, 0);
    preberi.setDefaultCommand(preberiC);
    preberi.setItemCommandListener(this);
    mainForm.addCommand(exitCommand);
    mainForm.setCommandListener(new CommandListener() {
        public void commandAction(Command c, Displayable s) {
            if (c.getCommandType() == Command.EXIT)
                notifyDestroyed();
        }
    });

    display.setCurrent(mainForm);
  }
  
  private boolean prazno() {
    boolean vejica = false;
    for (int i=0; i < zaslon.getString().length(); i++)
        if (zaslon.getString().charAt(i) == '.') {
            vejica = true;
            break;
        }
    return Float.parse(zaslon.getString(), 10).toLong() == 0 && !vejica;
  }

  public void pauseApp () {}

  public void destroyApp(boolean unconditional) {}

  public void commandAction(Command c, Item s) {
    polje.setText("");
    if (zaslon.getString().length() == 0)
        zaslon.setString("0");
    if (c == shraniC) {
        m = new Float(Float.parse(zaslon.getString(), 10));
        clear = true;
        return;
    }
    if (c == n0C) {
        if (clear) {
            zaslon.setString("0");
            clear = false;
        } else if (!prazno())
            zaslon.setString(zaslon.getString() + "0");
        return;
    }
    if (c == n1C || c == n2C || c == n3C || c == n4C || c == n5C || c == n6C || c == n7C || c == n8C || c == n9C) {
        if (!prazno() && !clear)
            zaslon.setString(zaslon.getString() + c.getLabel());
        else {
            zaslon.setString(c.getLabel());
            clear = false;
        }
        return;
    }
    if (c == pikaC) {
        try {
            if (prazno() || clear) {
                zaslon.setString("0.");
                clear = false;
            } else
                zaslon.setString(zaslon.getString() + ".");
        } catch (IllegalArgumentException e) {}
        return;
    }
    if (c == zbrisiC) {
        x = null;
        zaslon.setString("0");
        clear = false;
        lastC = null;
    }
    if (c == pobrisiC) {
        zaslon.setString("0");
        clear = false;
    }
    clear = true;
    if (lastC == null || lastC == jeenakoC || lastC == procentC) {
        x = Float.parse(zaslon.getString(), 10);
    }
    if (c == plusminusC) {
        try {
            zaslon.setString(Float.parse(zaslon.getString(),10).Neg().toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == preberiC) {
        try {
            zaslon.setString(m.toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == piC) {
        try {
            zaslon.setString(Float.PI.toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == eC) {
        try {
            zaslon.setString(Float.E.toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == sqrtC) {
        try {
            zaslon.setString(Float.sqrt(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == invC) {
        try {
            zaslon.setString(Float.pow(Float.parse(zaslon.getString(),10),new Float(-1)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == factC) {
        try {
            long a;
            if (!Float.Int(Float.parse(zaslon.getString(),10)).Less(Float.ZERO) && !Float.Frac(Float.parse(zaslon.getString(),10)).Great(Float.ZERO))
                a = Float.parse(zaslon.getString(),10).toLong();
            else
                return;
            long b=1;
            for (int i=0; i < a; i++)
                b*=i+1;
            zaslon.setString(b + "");
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == sinC) {
        try {
            zaslon.setString(Float.sin(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == cosC) {
        try {
            zaslon.setString(Float.cos(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == tanC) {
        try {
            zaslon.setString(Float.tan(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == asinC) {
        try {
            zaslon.setString(Float.asin(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == acosC) {
        try {
            zaslon.setString(Float.acos(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == atanC) {
        try {
            zaslon.setString(Float.atan(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == expC) {
        try {
            zaslon.setString(Float.exp(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == logC) {
        try {
            zaslon.setString(Float.log(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (c == log10C) {
        try {
            zaslon.setString(Float.log10(Float.parse(zaslon.getString(),10)).toString());
        } catch (IllegalArgumentException e) {
            napaka();
        }
        return;
    }
    if (lastC == plusC) {
        if (x == null)
            x = new Float();
        x = x.Add(Float.parse(zaslon.getString(), 10));
    }
    if (lastC == minusC) {
        if (x == null)
            x = new Float();
        x = x.Sub(Float.parse(zaslon.getString(), 10));
    }
    if (lastC == kratC) {
        if (x == null)
            x = new Float(1);
        x = x.Mul(Float.parse(zaslon.getString(), 10));
    }
    if (lastC == deljenoC) {
        if (x == null)
            x = new Float(1);
        x = x.Div(Float.parse(zaslon.getString(), 10));
    }
    if (lastC == powC) {
        if (x == null)
            x = new Float(1);
        x = Float.pow(x, Float.parse(zaslon.getString(), 10));
    }
    if (c == procentC) {
        if (lastC == deljenoC) {
            x = x.Mul(100);
        } else if (lastC == kratC) {
            x = x.Div(100);
        }
    } else if (c != jeenakoC)
        polje.setText(c.getLabel());
    try {
        zaslon.setString(x.toString());
    } catch (IllegalArgumentException e) {
        napaka();
    }
    lastC = c;
  }
  
  public void napaka() {
    x = null;
    zaslon.setString("0");
    polje.setText("Napaka!");
  }
}

class Float
{
  /** ERROR constant */
  final static private Float ERROR=new Float(Long.MAX_VALUE, Long.MAX_VALUE);
  /** Number of itterations in sqrt method, if you want to make calculations
   * more precise set ITNUM=6,7,... or ITMUN=4,3,... to make it faster */
  final static private int ITNUM=5;
  /** Square root from 3 */
  final static public Float SQRT3=new Float(1732050807568877294L, -18L);
  /** The Float value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter */
  final static public Float PI=new Float(3141592653589793238L, -18L);
  /** Zero constant */
  final static public Float ZERO=new Float();
  /** One constant */
  final static public Float ONE=new Float(1L);
  /** The Float value that is closer than any other to e, the base of the natural logarithms */
  final static public Float E = new Float(271828182845904512L, -17);
  /** Log10 constant */
  final static public Float LOG10 = new Float(2302585092994045684L, -18);
  //
  /** Pi/2 constant */
  final static public Float PIdiv2=PI.Div(2L);
  /** Pi/4 constant */
  final static public Float PIdiv4=PIdiv2.Div(2L);
  /** Pi/6 constant */
  final static public Float PIdiv6=PIdiv2.Div(3L);
  /** Pi/12 constant */
  final static public Float PIdiv12=PIdiv6.Div(2L);
  /** Pi*2 constant */
  final static public Float PImul2=PI.Mul(2L);
  /** Pi*4 constant */
  final static public Float PImul4=PI.Mul(4L);
  /** ln(0.5) constant */
  final static public Float LOGdiv2 = new Float(-6931471805599453094L, -19);
  /** Mantissa */
  public long m_Val;
  /** Exponent */
  public long m_E;
  /** Limit of value */
  private long maxLimit=Long.MAX_VALUE/100;
  //
  /**
   * Create object with zero inside
   */
  public Float()
  {
    m_Val=m_E=0;
  }
  /**
   * Create object and makes initialization with value
   * @param value long - value
   */
  public Float(long value)
  {
    m_Val=value;
    m_E=0;
  }
  /**
   * Create object and makes initialization both mantissa and exponent
   * @param value long - mantissa
   * @param e long - exponent
   */
  public Float(long value, long e)
  {
    m_Val=value;
    if(m_Val==0)
      m_E=0;
    else
      m_E=e;
  }
  /**
   * Create object and makes initialization by other Float object
   * @param value Float - source object
   */
  public Float(Float value)
  {
    m_Val=value.m_Val;
    if(m_Val==0)
      m_E=0;
    else
      m_E=value.m_E;
  }
  /**
   * Convert Float object to long number
   * @return long - number
   */
  public long toLong()
  {
    long tmpE=m_E;
    long tmpVal=m_Val;
    //
    while(tmpE!=0)
    {
      if(tmpE<0)
      {
        tmpVal/=10;
        tmpE++;
      }
      else
      {
        tmpVal*=10;
        tmpE--;
      }
    }
    return tmpVal;
  }
  /**
   * Convert Float object to string without exponent
   * @return String - string
   */
  public String toShortString()
  {
    if(isError())
      return "NaN";
    //
    StringBuffer sb=new StringBuffer();
    sb.append(m_Val);
    int len=(int)m_E;
    if(len>0)
    {
      for(int k=0; k<len; k++)
        sb.append("0");
      len=0;
    }
    //
    String str=sb.toString();
    len += str.length();
    //
    if(m_Val<0L)
    {
      if(len>1)
        return str.substring(0, len);
    }
    else
    {
      if(len>0)
        return str.substring(0, len);
    }
    //
    return "0";
  }
  /**
   * Check value of current Float object is NaN
   * @return boolean - true-if NaN, false-if not
   */
  public boolean isError()
  {
    return (this.m_Val==ERROR.m_Val && this.m_E==ERROR.m_E);
  }
  /** Convert Float object to string */
  public String toString()
  {
    if(isError())
      return "NaN";
    //
    RemoveZero();
    //
    Long l=new Long(m_Val);
    String str=l.toString();
    int len=str.length();
    boolean neg=false;
    if(m_Val<0L)
    {
      neg=true;
      str=str.substring(1, len);
      len--;
    }
    //
    StringBuffer sb=new StringBuffer();
    //
    if(m_E<0L)
    {
      int absE=(int)Math.abs(m_E);
      if(absE<len)
      {
        sb.append(str.substring(0, len-absE));
        sb.append(".");
        sb.append(str.substring(len-absE));
      }
      else
      {
        sb.append(str);
        for(int  i=0; i<(absE-len); i++)
          sb.insert(0, "0");
        sb.insert(0, "0.");
      }
    }
    else
    {
      if(len+m_E>6)
      {
        sb.append(str.charAt(0));
        if(str.length()>1)
        {
          sb.append(".");
          sb.append(str.substring(1));
        }
        else
          sb.append(".0");
        sb.append("E"+(len-1+m_E));
      }
      else
      {
        sb.append(str);
        for(int i=0; i<m_E; i++)
          sb.append("0");
      }
    }
    //
    str=sb.toString();
    sb=null;
    if(neg)
      str="-"+str;
    //
    return str;
  }
  /**
   * Append value of argument to value of current Float object and return as new Float object
   * @param value Float - argument
   * @return Float - current+value
   */
  public Float Add(Float value)
  {
    if(value.Equal(ZERO))
      return new Float(this);
    //
    long e1=m_E;
    long e2=value.m_E;
    long v1=m_Val;
    long v2=value.m_Val;
    // E must be equal in both operators
    while (e1 != e2)
    {
      if(e1 > e2)
      {
        if(Math.abs(v1)<maxLimit)
        {
          v1*=10;
          e1--;
        }
        else
        {
          v2/=10;
          e2++;
        }
      }
      else
      if(e1 < e2)
      {
        if(Math.abs(v2)<maxLimit)
        {
          v2*=10;
          e2--;
        }
        else
        {
          v1/=10;
          e1++;
        }
      }
    }
    //
    if( (v1>0 && v2>Long.MAX_VALUE-v1) || (v1<0 && v2<Long.MIN_VALUE-v1) )
    {
      v1/=10; e1++;
      v2/=10; e2++;
    }
    //
    if(v1>0 && v2>Long.MAX_VALUE-v1)
      return new Float(ERROR);
    else
    if(v1<0 && v2<Long.MIN_VALUE-v1)
      return new Float(ERROR);
    //
    return new Float(v1+v2, e1);
  }
  /**
   * Subtract value of argument from value of current Float object and return as new Float object
   * @param value Float - argument
   * @return Float - current-value
   */
  public Float Sub(Float value)
  {
    if(value.Equal(ZERO))
      return new Float(m_Val, m_E);
    return Add(new Float(-value.m_Val, value.m_E));
  }
  /**
   * Divide value of current Float object on argument and return as new Float object
   * @param value Float - argument
   * @return Float - current/value
   */
  public Float Mul(long value)
  {
    return Mul(new Float(value, 0));
  }
  /**
   * Multiply value of current Float object on argument and return as new Float object
   * @param value Float - argument
   * @return Float - current*value
   */
  public Float Mul(Float value)
  {
    if(value.Equal(ZERO) || this.Equal(ZERO))
      return new Float(ZERO);
    if(value.Equal(ONE))
      return new Float(this);
    //
    boolean negative1=(m_Val<0);
    if(negative1) m_Val=-m_Val;
    boolean negative2=(value.m_Val<0);
    if(negative2) value.m_Val=-value.m_Val;
    // Check overflow and underflow
    do
    {
      if(value.m_Val>m_Val)
      {
        if(Long.MAX_VALUE/m_Val<value.m_Val)
        {
          value.m_Val/=10;
          value.m_E++;
        }
        else
          break;
      }
      else
      {
        if(Long.MAX_VALUE/value.m_Val<m_Val)
        {
          m_Val/=10;
          m_E++;
        }
        else
          break;
      }
    } while(true);
    //
    if(negative1) m_Val=-m_Val;
    if(negative2) value.m_Val=-value.m_Val;
    //
    long e=m_E+value.m_E;
    long v=m_Val*value.m_Val;
    return new Float(v, e);
  }
  /**
   * Divide value of current Float object on argument and return as new Float object
   * @param value Float - argument
   * @return Float - current/value
   */
  public Float Div(long value)
  {
    return Div(new Float(value, 0));
  }
  /**
   * Divide value of current Float object on argument and return as new Float object
   * @param value Float - argument
   * @return Float - current/value
   */
  public Float Div(Float value)
  {
    if(value.Equal(ONE))
      return new Float(this);
    //
    long e1=m_E;
    long e2=value.m_E;
    long v2=value.m_Val;
    if(v2==0L)
      return new Float(ERROR);
    long v1=m_Val;
    if(v1==0L)
      return new Float(ZERO);
    //
    long val=0L;
    while(true)
    {
      val+=(v1/v2);
      v1%=v2;
      if(v1==0L || Math.abs(val)>(Long.MAX_VALUE/10L))
        break;
      if(Math.abs(v1)>(Long.MAX_VALUE/10L))
      {
        v2/=10L;
        e2++;
      }
      else
      {
        v1*=10L;
        e1--;
      }
      val*=10L;
    }
    //
    Float f=new Float(val, e1-e2);
    f.RemoveZero();
    return f;
  }
  public void RemoveZero()
  {
    if(m_Val==0)
      return;
    while ( m_Val%10 == 0 )
    {
     m_Val/=10;
     m_E++;
    }
  }
  /**
   * Is value of current Float object greater?
   * @param x Float - argument
   * @return boolean - true-if current value is greater x, false-if not
   */
  public boolean Great(Float x)
  {
    long e1=m_E;
    long e2=x.m_E;
    long v1=m_Val;
    long v2=x.m_Val;
    //
    while (e1 != e2)
    {
      if(e1 > e2)
      {
        if(Math.abs(v1)<maxLimit)
        {
          v1*=10;
          e1--;
        }
        else
        {
          v2/=10;
          e2++;
        }
      }
      else
      if(e1 < e2)
      {
        if(Math.abs(v2)<maxLimit)
        {
          v2*=10;
          e2--;
        }
        else
        {
          v1/=10;
          e1++;
        }
      }
    }
    //
    return v1>v2;
  }
  /**
   * Is value of current Float object less?
   * @param x Float - argument
   * @return boolean - true-if current value is less x, false-if not
   */
  public boolean Less(long x)
  {
    return Less(new Float(x, 0));
  }
  /**
   * Is value of current Float object less?
   * @param x Float - argument
   * @return boolean - true-if current value is less x, false-if not
   */
  public boolean Less(Float x)
  {
    long e1=m_E;
    long e2=x.m_E;
    long v1=m_Val;
    long v2=x.m_Val;
    //
    while (e1 != e2)
  {
    if(e1 > e2)
    {
      if(Math.abs(v1)<maxLimit)
      {
        v1*=10;
        e1--;
      }
      else
      {
        v2/=10;
        e2++;
      }
    }
    else
    if(e1 < e2)
    {
      if(Math.abs(v2)<maxLimit)
      {
        v2*=10;
        e2--;
      }
      else
      {
        v1/=10;
        e1++;
      }
    }
  }
  //
  return v1<v2;
  }
  /**
   * Equal with current Float object?
   * @param x Float - argument
   * @return boolean - true-if equal, false-if not
   */
  public boolean Equal(Float x)
  {
    long e1=m_E;
    long e2=x.m_E;
    long v1=m_Val;
    long v2=x.m_Val;
    //
    if((v1==0 && v2==0) || (v1==v2 && e1==e2))
      return true;
    // Values with exponent differences more than 20 times never could be equal
    /*
    if(Math.abs(e1-e2)>20)
      return false;
    */
    long diff=e1-e2;
    if(diff<-20 || diff>20)
      return false;
    //
    while (e1 != e2)
    {
      if(e1 > e2)
      {
        if(Math.abs(v1)<maxLimit)
        {
          v1*=10;
          e1--;
        }
        else
        {
          v2/=10;
          e2++;
        }
      }
      else
      if(e1 < e2)
      {
        if(Math.abs(v2)<maxLimit)
        {
          v2*=10;
          e2--;
        }
        else
        {
          v1/=10;
          e1++;
        }
      }
    }
    //
    return (v1==v2);
  }
  /**
   * Reverse sign of value in current Float object and return as new Float object
   * @return Float - new Float object
   */
  public Float Neg()
  {
    return new Float(-m_Val, m_E);
  }
  /**
   * Returns the trigonometric sine of an angle. Special cases: If the argument is NaN or an infinity, then the result is NaN. If the argument is zero, then the result is a zero with the same sign as the argument. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - an angle, in radians
   * @return Float - the sine of the argument
   */
  static public Float sin(Float x)
  {
    while( x.Great(PI) )
      x=x.Sub(PImul2);
    while( x.Less(PI.Neg()) )
      x=x.Add(PImul2);
    // x*x*x
    Float m1=x.Mul(x.Mul(x));
    Float q1=m1.Div(6L);
    // x*x*x*x*x
    Float m2=x.Mul(x.Mul(m1));
    Float q2=m2.Div(120L);
    // x*x*x*x*x*x*x
    Float m3=x.Mul(x.Mul(m2));
    Float q3=m3.Div(5040L);
    // x*x*x*x*x*x*x*x*x
    Float m4=x.Mul(x.Mul(m3));
    Float q4=m4.Div(362880L);
    // x*x*x*x*x*x*x*x*x*x*x
    Float m5=x.Mul(x.Mul(m4));
    Float q5=m5.Div(39916800L);
    //
    Float result=x.Sub(q1).Add(q2).Sub(q3).Add(q4).Sub(q5);
    // 1e-6
    if(result.Less(new Float(-999999, -6)))
      return new Float(-1L);
    // 1e-6
    if(result.Great(new Float(999999, -6)))
      return new Float(1L);
    // 5e-4
    if(result.Great(new Float(-5, -4)) && result.Less(new Float(5, -4)))
      return new Float(0L);
    //
    return result;
  }
  /**
   * Returns the trigonometric cosine of an angle. Special cases: If the argument is NaN or an infinity, then the result is NaN. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - an angle, in radians
   * @return Float - the cosine of the argument
   */
  static public Float cos(Float x)
  {
    while( x.Great(PI) )
      x=x.Sub(PImul2);
    while( x.Less(PI.Neg()) )
      x=x.Add(PImul2);
    // x*x
    Float m1=x.Mul(x);
    Float q1=m1.Div(2L);
    // x*x*x*x
    Float m2=m1.Mul(m1);
    Float q2=m2.Div(24L);
    // x*x*x*x*x*x
    Float m3=m1.Mul(m2);
    Float q3=m3.Div(720L);
    // x*x*x*x*x*x*x*x
    Float m4=m2.Mul(m2);
    Float q4=m4.Div(40320L);
    // x*x*x*x*x*x*x*x*x*x
    Float m5=m4.Mul(m1);
    Float q5=m5.Div(3628800L);
    Float result=ONE.Sub(q1).Add(q2).Sub(q3).Add(q4).Sub(q5);
    // 1e-6
    if(result.Less(new Float(-999999, -6)))
      return new Float(-1L);
    // 1e-6
    if(result.Great(new Float(999999, -6)))
      return new Float(1L);
    // 5e-4
    if(result.Great(new Float(-5, -4)) && result.Less(new Float(5, -4)))
      return new Float(0L);
    //
    return result;
  }
  /**
   * Returns the correctly rounded positive square root of a double value. Special cases: If the argument is NaN or less than zero, then the result is NaN. If the argument is positive infinity, then the result is positive infinity. If the argument is positive zero or negative zero, then the result is the same as the argument. Otherwise, the result is the double value closest to the true mathematical square root of the argument value
   * @param x Float - a value
   * @return Float - the positive square root of a. If the argument is NaN or less than zero, the result is NaN
   */
  static public Float sqrt(Float x)
  {
    int sp=0;
    boolean inv=false;
    Float a,b;
    //
    if(x.Less(ZERO))
      return new Float(ERROR);
    if(x.Equal(ZERO))
      return new Float(ZERO);
    if(x.Equal(ONE))
      return new Float(ONE);
    // argument less than 1 : invert it
    if(x.Less(ONE))
    {
      x=ONE.Div(x);
      inv=true;
    }
    //
    long e=x.m_E/2;
    // exponent compensation
    Float tmp=new Float(x.m_Val, x.m_E-e*2);
    // process series of division by 16 until argument is <16
    while(tmp.Great(new Float(16L)))
    {
      sp++;
      tmp=tmp.Div(16L);
    }
    // initial approximation
    a=new Float(2L);
    // Newtonian algorithm
    for(int i=ITNUM; i>0; i--)
    {
      b=tmp.Div(a);
      a=a.Add(b);
      a=a.Div(2L);
    }
    // multiply result by 4 : as much times as divisions by 16 took place
    while(sp>0)
    {
      sp--;
      a=a.Mul(4L);
    }
    // exponent compensation
    a.m_E+=e;
    // invert result for inverted argument
    if(inv)
      a=ONE.Div(a);
    return a;
  }
  /**
   * Returns the trigonometric tangent of an angle. Special cases: If the argument is NaN or an infinity, then the result is NaN. If the argument is zero, then the result is a zero with the same sign as the argument. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - an angle, in radians
   * @return Float - the tangent of the argument
   */
  static public Float tan(Float x)
  {
    Float c=cos(x);
    if(c.Equal(ZERO)) return new Float(ERROR);
    return (sin(x).Div(c));
  }
  /**
   * Returns a new Float object initialized to the value represented by the specified String
   * @param str String - the string to be parsed
   * @param radix int - basement of number
   * @return Float - the Float object represented by the string argument
   */
  static public Float parse(String str, int radix)
  {
    // Abs
    boolean neg=false;
    if(str.charAt(0)=='-')
    {
      str=str.substring(1);
      neg=true;
    }
    //
    int pos=str.indexOf(".");
    long exp=0;
    // Find exponent position
    int pos2=str.indexOf('E');
    if(pos2==-1) pos2=str.indexOf('e');
    //
    if(pos2!=-1)
    {
      String tmp=new String(str.substring(pos2+1));
      exp=Long.parseLong(tmp);
      str=str.substring(0, pos2);
    }
    //
    if(pos!=-1)
    {
      for(int m=pos+1; m<str.length(); m++)
      {
        if(Character.isDigit(str.charAt(m)))
          exp--;
        else
          break;
      }
      str=str.substring(0, pos)+str.substring(pos+1);
      while(str.length()>1 && str.charAt(0)=='0' && str.charAt(1)!='.')
        str=str.substring(1);
    }
    //
    long result=0L;
    int len=str.length();
    //
    StringBuffer sb=new StringBuffer(str);
    while(true)
    {
      // Long value can't have length more than 20
      while(len>20)
      {
        // Very large number for Long
        sb=sb.deleteCharAt(len-1);
        // Compensation of removed zeros
        if(len<pos || pos==-1)
          exp++;
        //
        len--;
      }
      //
      try
      {
        result=Long.parseLong(sb.toString(), radix);
        if(neg)
          result=-result;
        break;
      }
      catch(Exception e)
      {
        // Very large number for Long
        sb=sb.deleteCharAt(len-1);
        // Compensation of removed zeros
        if(len<pos || pos==-1)
          exp++;
        //
        len--;
      }
    }
    sb=null;
    //
    Float newValue=new Float(result, exp);
    newValue.RemoveZero();
    return newValue;
  }
  /**
   * Returns the arc cosine of an angle, in the range of 0.0 through pi. Special case: If the argument is NaN or its absolute value is greater than 1, then the result is NaN. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - the value whose arc cosine is to be returned
   * @return Float - the arc cosine of the argument
   */
  static public Float acos(Float x)
  {
    Float f=asin(x);
    if(f.isError())
      return f;
    return PIdiv2.Sub(f);
  }
  /**
   * Returns the arc sine of an angle, in the range of -pi/2 through pi/2. Special cases: If the argument is NaN or its absolute value is greater than 1, then the result is NaN. If the argument is zero, then the result is a zero with the same sign as the argument. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - the value whose arc sine is to be returned
   * @return Float - the arc sine of the argument
   */
  static public Float asin(Float x)
  {
    if( x.Less(ONE.Neg()) || x.Great(ONE) ) return new Float(ERROR);
    if( x.Equal(ONE.Neg()) ) return PIdiv2.Neg();
    if( x.Equal(ONE) ) return PIdiv2;
    return atan(x.Div(sqrt(ONE.Sub(x.Mul(x)))));
  }
  /**
   * Returns the arc tangent of an angle, in the range of -pi/2 through pi/2. Special cases: If the argument is NaN, then the result is NaN. If the argument is zero, then the result is a zero with the same sign as the argument. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - the value whose arc tangent is to be returned
   * @return Float - the arc tangent of the argument
   */
  static public Float atan(Float x)
  {
      boolean signChange=false;
      boolean Invert=false;
      int sp=0;
      Float x2, a;
      // check up the sign change
      if(x.Less(ZERO))
      {
          x=x.Neg();
          signChange=true;
      }
      // check up the invertation
      if(x.Great(ONE))
      {
          x=ONE.Div(x);
          Invert=true;
      }
      // process shrinking the domain until x<PI/12
      while(x.Great(PIdiv12))
      {
          sp++;
          a=x.Add(SQRT3);
          a=ONE.Div(a);
          x=x.Mul(SQRT3);
          x=x.Sub(ONE);
          x=x.Mul(a);
      }
      // calculation core
      x2=x.Mul(x);
      a=x2.Add(new Float(14087812, -7));
      a=new Float(55913709, -8).Div(a);
      a=a.Add(new Float(60310579, -8));
      a=a.Sub(x2.Mul(new Float(5160454, -8)));
      a=a.Mul(x);
      // process until sp=0
      while(sp>0)
      {
          a=a.Add(PIdiv6);
          sp--;
      }
      // invertation took place
      if(Invert) a=PIdiv2.Sub(a);
      // sign change took place
      if(signChange) a=a.Neg();
      //
      return a;
  }
  /**
   * Converts rectangular coordinates (x, y) to polar (r, theta). This method computes the phase theta by computing an arc tangent of y/x in the range of -pi to pi. Special cases: If either argument is NaN, then the result is NaN. If the first argument is positive zero and the second argument is positive, or the first argument is positive and finite and the second argument is positive infinity, then the result is positive zero. If the first argument is negative zero and the second argument is positive, or the first argument is negative and finite and the second argument is positive infinity, then the result is negative zero. If the first argument is positive zero and the second argument is negative, or the first argument is positive and finite and the second argument is negative infinity, then the result is the double value closest to pi. If the first argument is negative zero and the second argument is negative, or the first argument is negative and finite and the second argument is negative infinity, then the result is the double value closest to -pi. If the first argument is positive and the second argument is positive zero or negative zero, or the first argument is positive infinity and the second argument is finite, then the result is the double value closest to pi/2. If the first argument is negative and the second argument is positive zero or negative zero, or the first argument is negative infinity and the second argument is finite, then the result is the double value closest to -pi/2. If both arguments are positive infinity, then the result is the double value closest to pi/4. If the first argument is positive infinity and the second argument is negative infinity, then the result is the double value closest to 3*pi/4. If the first argument is negative infinity and the second argument is positive infinity, then the result is the double value closest to -pi/4. If both arguments are negative infinity, then the result is the double value closest to -3*pi/4. A result must be within 2 ulps of the correctly rounded result. Results must be semi-monotonic
   * @param y Float - the ordinate coordinate
   * @param x Float - the abscissa coordinate
   * @return Float - the theta component of the point (r, theta) in polar coordinates that corresponds to the point (x, y) in Cartesian coordinates
   */
  static public Float atan2(Float y, Float x)
  {
    // if x=y=0
    if(y.Equal(ZERO) && x.Equal(ZERO))
      return new Float(ZERO);
    // if x>0 atan(y/x)
    if(x.Great(ZERO))
      return atan(y.Div(x));
    // if x<0 sign(y)*(pi - atan(|y/x|))
    if(x.Less(ZERO))
    {
      if(y.Less(ZERO))
        return Float.PI.Sub(atan(y.Div(x))).Neg();
      else
        return Float.PI.Sub(atan(y.Div(x).Neg()));
    }
    // if x=0 y!=0 sign(y)*pi/2
    if(y.Less(ZERO))
      return PIdiv2.Neg();
    else
      return new Float(PIdiv2);
  }

  // precise
  // x=-35 diff=1.48%
  // x=-30 diff=0.09%
  // x=30 diff=0.09%
  // x=31 diff=0.17%
  // x=32 diff=0.31%
  // x=33 diff=0.54%
  // x=34 diff=0.91%
  // x=35 diff=1.46%
  /**
   * Returns Euler's number e raised to the power of a double value. Special cases: If the argument is NaN, the result is NaN. If the argument is positive infinity, then the result is positive infinity. If the argument is negative infinity, then the result is positive zero. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - the exponent to raise e to
   * @return Float - the value e^x, where e is the base of the natural logarithms
   */
  static public Float exp(Float x)
  {
    if(x.Equal(ZERO))
      return new Float(ONE);
    //
    Float f=new Float(ONE);
    long d=1;
    Float k=null;
    boolean isless=x.Less(ZERO);
    if(isless)
      x=x.Neg();
    k=new Float(x).Div(d);
    //
    for(long i=2; i<50; i++)
    {
      f=f.Add(k);
      k=k.Mul(x).Div(i);
    }
    //
    if(isless)
      return ONE.Div(f);
    else
      return f;
  }

  // precise
  // x=25 diff=0.12%
  // x=30 diff=0.25%
  // x=35 diff=0.44%
  // x=40 diff=0.67%
  /**
   * Internal log subroutine
   * @param x Float
   * @return Float
   */
  static private Float _log(Float x)
  {
    if(!x.Great(ZERO))
      return new Float(ERROR);
    //
    Float f=new Float(ZERO);
    // Make x to close at 1
    int appendix=0;
    while(x.Great(ZERO) && x.Less(ONE))
    {
      x=x.Mul(2);
      appendix++;
    }
    //
    x=x.Div(2);
    appendix--;
    //
    Float y1=x.Sub(ONE);
    Float y2=x.Add(ONE);
    Float y=y1.Div(y2);
    //
    Float k=new Float(y);
    y2=k.Mul(y);
    //
    for(long i=1; i<50; i+=2)
    {
      f=f.Add(k.Div(i));
      k=k.Mul(y2);
    }
    //
    f=f.Mul(2);
    for(int i=0; i<appendix; i++)
      f=f.Add(LOGdiv2);
    //
    return f;
  }
  /**
   * Returns the natural logarithm (base e) of a double value. Special cases: If the argument is NaN or less than zero, then the result is NaN. If the argument is positive infinity, then the result is positive infinity. If the argument is positive zero or negative zero, then the result is negative infinity. A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic
   * @param x Float - a number greater than 0.0
   * @return Float - the value ln(x), the natural logarithm of x
   */
  static public Float log(Float x)
  {
    if(!x.Great(ZERO))
      return new Float(ERROR);
    //
    if(x.Equal(ONE))
      return new Float(ZERO);
    //
    if(x.Great(Float.ONE))
    {
      x=ONE.Div(x);
      return _log(x).Neg();
    }
    return _log(x);
  }

  static public Float log10(Float x)
  {
    if(!x.Great(ZERO))
      return new Float(ERROR);
    //
    if(x.Equal(ONE))
      return new Float(ZERO);
    //
    Float f=log(x);
    if(f.isError())
      return f;
    return f.Div(LOG10);
  }
/*
  static public Float log10(Float x)
  {
    if(!x.Great(ZERO))
      return new Float(ERROR);
    //
    boolean neg=false;
    if(x.m_Val<0)
    {
      neg=true;
      x.m_Val=-x.m_Val;
    }
    //
    int index=0;
    if(x.Great(Float.ONE))
    {
      // Áîëüøå 1
      while(x.Great(Float.ONE))
      {
        x=x.Div(10);
        index++;
      }
    }
    else
    {
      // Ìåíüøå èëè ðàâíî 1
      while(x.Less(Float.ONE))
      {
        x=x.Mul(10);
        index--;
      }
    }
    //
    Float res=new Float(index);
    if(!x.Equal(ONE))
      res=res.Add(log(x).Div(LOG10));
    //
    if(neg)
      return Float.ONE.Div(res);
    else
      return res;
  }
 */

  // precise y=3.5
  // x=15 diff=0.06%
  // x=20 diff=0.40%
  // x=25 diff=1.31%
  // x=30 diff=2.95%
  // if x negative y must be integer value
  /**
   * Returns the value of the first argument raised to the power of the second argument. Special cases: If the second argument is positive or negative zero, then the result is 1.0. If the second argument is 1.0, then the result is the same as the first argument. If the second argument is NaN, then the result is NaN. If the first argument is NaN and the second argument is nonzero, then the result is NaN. If the absolute value of the first argument is greater than 1 and the second argument is positive infinity, or the absolute value of the first argument is less than 1 and the second argument is negative infinity, then the result is positive infinity. If the absolute value of the first argument is greater than 1 and the second argument is negative infinity, or the absolute value of the first argument is less than 1 and the second argument is positive infinity, then the result is positive zero. If the absolute value of the first argument equals 1 and the second argument is infinite, then the result is NaN. If the first argument is positive zero and the second argument is greater than zero, or the first argument is positive infinity and the second argument is less than zero, then the result is positive zero. If the first argument is positive zero and the second argument is less than zero, or the first argument is positive infinity and the second argument is greater than zero, then the result is positive infinity. If the first argument is negative zero and the second argument is greater than zero but not a finite odd integer, or the first argument is negative infinity and the second argument is less than zero but not a finite odd integer, then the result is positive zero. If the first argument is negative zero and the second argument is a positive finite odd integer, or the first argument is negative infinity and the second argument is a negative finite odd integer, then the result is negative zero. If the first argument is negative zero and the second argument is less than zero but not a finite odd integer, or the first argument is negative infinity and the second argument is greater than zero but not a finite odd integer, then the result is positive infinity. If the first argument is negative zero and the second argument is a negative finite odd integer, or the first argument is negative infinity and the second argument is a positive finite odd integer, then the result is negative infinity. If the first argument is finite and less than zero if the second argument is a finite even integer, the result is equal to the result of raising the absolute value of the first argument to the power of the second argument if the second argument is a finite odd integer, the result is equal to the negative of the result of raising the absolute value of the first argument to the power of the second argument if the second argument is finite and not an integer, then the result is NaN. If both arguments are integers, then the result is exactly equal to the mathematical result of raising the first argument to the power of the second argument if that result can in fact be represented exactly as a double value. (In the foregoing descriptions, a floating-point value is considered to be an integer if and only if it is finite and a fixed point of the method ceil or, equivalently, a fixed point of the method floor. A value is a fixed point of a one-argument method if and only if the result of applying the method to the value is equal to the value.) A result must be within 1 ulp of the correctly rounded result. Results must be semi-monotonic.
   * @param x Float - the base
   * @param y Float - the exponent
   * @return Float - the value a^b
   */
  static public Float pow(Float x, Float y)
  {
    if(x.Equal(ZERO))
      return new Float(ZERO);
    if(x.Equal(ONE))
      return new Float(ONE);
    if(y.Equal(ZERO))
      return new Float(ONE);
    if(y.Equal(ONE))
      return new Float(x);
    //
    long l=y.toLong();
    boolean integerValue=y.Equal(new Float(l));
    //
    if(integerValue)
    {
      boolean neg=false;
      if(y.Less(0))
        neg=true;
      //
      Float result=new Float(x);
      for(long i=1; i<(neg?-l:l); i++)
        result=result.Mul(x);
      //
      if(neg)
        return ONE.Div(result);
      else
        return result;
    }
    else
    {
      if(x.Great(ZERO))
        return exp(y.Mul(log(x)));
      else
        return new Float(ERROR);
    }
  }
  /**
   * Returns the smallest (closest to negative infinity) double value that is not less than the argument and is equal to a mathematical integer. Special cases: If the argument value is already equal to a mathematical integer, then the result is the same as the argument. If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument. If the argument value is less than zero but greater than -1.0, then the result is negative zero
   * @param x Float - a value
   * @return Float - the smallest (closest to negative infinity) floating-point value that is not less than the argument and is equal to a mathematical integer
   */
  static public Float ceil(Float x)
  {
    long tmpVal=x.m_Val;
    //
    if(x.m_E<0)
    {
      long coeff=1;
      //
      if(x.m_E>-19)
      {
        for(long i=0; i<-x.m_E; i++)
          coeff*=10;
        tmpVal/=coeff;
        tmpVal*=coeff;
        if(x.m_Val-tmpVal>0)
          tmpVal+=coeff;
      }
      else
      if(tmpVal>0)
        return ONE;
      else
        return ZERO;
    }
    //
    return new Float(tmpVal, x.m_E);
  }
  /**
   * Returns the largest (closest to positive infinity) double value that is not greater than the argument and is equal to a mathematical integer. Special cases: If the argument value is already equal to a mathematical integer, then the result is the same as the argument. If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument
   * @param x Float - a value
   * @return Float - the largest (closest to positive infinity) floating-point value that is not greater than the argument and is equal to a mathematical integer
   */
  static public Float floor(Float x)
  {
    long tmpVal=x.m_Val;
    //
    if(x.m_E<0)
    {
      long coeff=1;
      //
      if(x.m_E>-19)
      {
        for(long i=0; i<-x.m_E; i++)
          coeff*=10;
        tmpVal/=coeff;
        tmpVal*=coeff;
        if(x.m_Val-tmpVal<0)
          tmpVal-=coeff;
      }
      else
      if(tmpVal<0)
        return ONE.Neg();
      else
        return ZERO;
    }
    //
    return new Float(tmpVal, x.m_E);
  }
  /**
   * Returns the absolute value of a Float object. If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned. Special cases: If the argument is positive zero or negative zero, the result is positive zero. If the argument is infinite, the result is positive infinity. If the argument is NaN, the result is NaN
   * @param x Float - the argument whose absolute value is to be determined
   * @return Float - the absolute value of the argument
   */
  static public Float abs(Float x)
  {
    if(x.m_Val<0)
      return x.Neg();
    return new Float(x);
  }
  /**
   * Integer part of Float object
   * @param x Float - source Float object
   * @return Float - result Float object
   */
  static public Float Int(Float x)
  {
    long tmpVal=x.m_Val;
    //
    if(x.m_E<0)
    {
      long coeff=1;
      //
      if(x.m_E>-19)
      {
        for(long i=0; i<-x.m_E; i++)
          coeff*=10;
        tmpVal/=coeff;
        tmpVal*=coeff;
      }
      else
        return Float.ZERO;
    }
    //
    return new Float(tmpVal, x.m_E);
  }
  /**
   * Fractional part of Float object
   * @param x Float - source Float object
   * @return Float - result Float object
   */
  static public Float Frac(Float x)
  {
    return x.Sub(Int(x));
  }
  /**
   * Converts an angle measured in degrees to an approximately equivalent angle measured in radians. The conversion from degrees to radians is generally inexact
   * @param x Float - an angle, in degrees
   * @return Float - the measurement of the angle x in radians
   */
  static public Float toRadians(Float x)
  {
    return x.Mul(PI).Div(180L);
  }
  /**
   * Converts an angle measured in radians to an approximately equivalent angle measured in degrees. The conversion from radians to degrees is generally inexact; users should not expect cos(toRadians(90.0)) to exactly equal 0.0
   * @param x Float - an angle, in radians
   * @return Float - the measurement of the angle angrad in degrees
   */
  static public Float toDegrees(Float x)
  {
    return x.Mul(180L).Div(PI);
  }
}
