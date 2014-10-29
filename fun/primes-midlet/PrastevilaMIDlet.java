import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class PrastevilaMIDlet
  extends MIDlet
  implements CommandListener {

   //Inicializacija spremenljivk
   
   private int spMeja=10;
   private int zgMeja=30;
   private Display display = Display.getDisplay(this);
   private Form mainForm = new Form("Praštevila");
   private TextField textSpMeja, textZgMeja;
   private Command najdiPr;
   private boolean started=false;

   public void startApp()
   {
      if (started) return;
      started=true;
      textSpMeja = new TextField("Spodnja meja:", Integer.toString(spMeja), 128, TextField.ANY);
      textZgMeja = new TextField("Zgornja meja:", Integer.toString(zgMeja), 128, TextField.ANY);
      Command exitCommand = new Command("Izhod", Command.EXIT, 0);
      najdiPr = new Command("Poišèi praštevila", Command.SCREEN, 0);
      mainForm.append(textSpMeja);
      mainForm.append(textZgMeja);
      mainForm.addCommand(exitCommand);
      mainForm.addCommand(najdiPr);
      mainForm.setCommandListener(this);
      izracunaj();
      display.setCurrent(mainForm);
   }
           
   public void izracunaj()
   {

      mainForm.deleteAll();
      mainForm.append(textSpMeja);
      mainForm.append(textZgMeja);

      try {
          spMeja=Integer.parseInt(textSpMeja.getString());
          zgMeja=Integer.parseInt(textZgMeja.getString());
      }
      catch(NumberFormatException e) {
          mainForm.append("Napaka: navesti moraš število!\n\n");
          return;
      }

      //Napake...
      String nipr="Med " + spMeja + " in " + zgMeja + " ni praštevil.";
      
      if (spMeja > zgMeja)
      {
          mainForm.append("Napaka: spodnja meja je veèja od zgornje!\n\n");
          return;
      }

      //2 je najmanjše praštevilo - èe je zgornja meja manjša, niti ne preverja
      if (zgMeja < 2)
      {
          if (spMeja==zgMeja)
              mainForm.append(zgMeja + " ni praštevilo.\n\n");
          else
              mainForm.append(nipr + "\n\n");
          return;
      }

      //Tabela naj bo dolga toliko, kolikor je lihih števil do zgornje meje +1 (za sodo praštevilo)
      int[] pr=new int[(zgMeja+1)/2];
      String prastevila="Praštevila med " + spMeja + " in " + zgMeja + ": ";
      int vsota=0;

      //To veè ne škodi:) - sicer bi imeli napake
      if (spMeja < 1)
      {
          spMeja=1;
      }
      
      //Sodo praštevilo
      pr[0]=2;
      
      //Napolnimo tabelo z lihimi števili
      for (int i=1; 2*i < zgMeja; i++)
      {
          pr[i]=2*i+1;
      }

      //Števila, veèja od korena zgornje meje ne bodo izloèila nobenega od svojih veèkratnikov,
      //ker jih bodo izloèila že manjša števila
      for (int i=1; i < zgMeja/2 && zgMeja > 2; i++)
      {
          //Spustimo nepraštevila
          if (pr[i] != 0)
          {
              //Veèkratnike zaènemo iskati pri kvadratu trenutnega praštevila
              for (int j=(int)(pr[i]*pr[i]/2); 2*j < zgMeja; j++)
              {
                  //Veèkratnike nadomestimo z 0
                  if (pr[j]%pr[i] == 0)
                      pr[j]=0;
              }
          }
      }

      //Zberimo praštevila in jih sproti seštevajmo
      for (int i=0; 2*i < zgMeja; i++)
      {
          if (pr[i] >= spMeja)
          {
              prastevila+=pr[i] + " ";
              vsota+=pr[i];
          }
      }
      
      //Izpis
      if ((vsota!=0) && (spMeja==zgMeja))
          mainForm.append(zgMeja + " je praštevilo.\n\n");
      else if (vsota!=0)
      {
          mainForm.append(prastevila + "\n");
          mainForm.append("Vsota praštevil: " + vsota + "\n\n");
      } else if (spMeja==zgMeja)
          mainForm.append(zgMeja + " ni praštevilo.\n\n");
      else
          mainForm.append(nipr + "\n\n");
    }

    public void pauseApp () {}

    public void destroyApp(boolean unconditional) {}

    public void commandAction(Command c, Displayable s) {
      if (c.getCommandType() == Command.EXIT)
          notifyDestroyed();
      else if (c == najdiPr) {
          mainForm.append("\n");
          izracunaj();
      }

    }
    
}