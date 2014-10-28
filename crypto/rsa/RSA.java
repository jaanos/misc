public class RSA {
    final static int ST_UP=10;
    public static Uporabnik[] user = new Uporabnik[ST_UP];
    
    public static void main(String[] args) {
        System.out.println("\nRSA messaging shell");
        System.out.println("v0.1          by JV");
        prijava();
    }
    
    public static void prijava() {
        System.out.print("\nUporabnisko ime: ");
        String id = BranjePodatkov.preberiString();
        if (id.equals(""))
            return;
        Uporabnik u = najdiUporabnika(id);
        if (u == null)
            novUporabnik(id);
        else
            u.login();
    }
    
    public static void razbij(String msg) {
        for (int i=0; i < msg.length(); i++) {
            System.out.print((int)(msg.charAt(i)));
            if (i+1 < msg.length())
                System.out.print(",");
        }
        System.out.println();
    }
    
    public static Uporabnik najdiUporabnika(String id) {
        for (int i=0; i < user.length; i++) {
            try {
                if (id.equals(user[i].getUser()) || id.equals(user[i].getEmail()))
                    return user[i];
            }
            catch(NullPointerException e) {}
        }
        return null;
    }
    
    public static void novUporabnik(String id) {
        int i;
        for (i=0; i < user.length; i++)
            if (user[i] == null)
                break;
        if (i == user.length) {
            System.out.println("Maksimalno stevilo uporabnikov je ze dosezeno!\n");
            prijava();
            return;
        }
        System.out.println("\nUporabnik " + id + " neznan, ustvarjam novega.");
        String geslo;
        boolean napaka = false;
        do {
            if (napaka)
                System.out.println("Prazno geslo! Ponovi vnos!");
            System.out.print("Vnesi geslo: ");
            geslo = BranjePodatkov.preberiString();
            napaka = true;
        } while (geslo.equals(""));
        System.out.print("Ponovno vnesi geslo: ");
        if (!geslo.equals(BranjePodatkov.preberiString())) {
            System.out.println("Gesli se ne ujemata!\n");
            prijava();
            return;
        }
        System.out.print("Vnesi ime: ");
        String ime = BranjePodatkov.preberiString();
        System.out.print("Vnesi priimek: ");
        String priimek = BranjePodatkov.preberiString();
        String email;
        Uporabnik check;
        napaka = false;
        do {
            if (napaka)
                System.out.println("Email ze obstaja!");
            System.out.print("Vnesi email: ");
            email = BranjePodatkov.preberiString();
            check = najdiUporabnika(email);
            napaka = true;
        } while (check != null);
        
        user[i] = new Uporabnik(id, geslo, ime, priimek, email);
        user[i].shell();
    }
}

class Uporabnik {
    final static int ST_MSG=10;
    String user, geslo, ime, priimek, email, from, naslov, vsebina;
    Sporocilo[] inbox = new Sporocilo[ST_MSG];
    Sporocilo[] sent = new Sporocilo[ST_MSG];
    int neprebrana=0;
    boolean sifriraj=false, overi=false, shrani=true;
    Uporabnik prejemnik;
    RSAKljuc kljuc;
    
    public Uporabnik(String user, String geslo) {
        this.user = user;
        this.geslo = geslo;
        this.kljuc = new RSAKljuc();
    }
    
    public Uporabnik(String user, String geslo, String ime, String priimek, String email) {
        this(user, geslo);
        this.ime = ime;
        this.priimek = priimek;
        this.email = email;
        this.from = email;
    }
    
    public void login() {
        System.out.print("Vnesi geslo: ");
        String geslo = BranjePodatkov.preberiString();
        if (preveriGeslo(geslo))
            shell();
        else {
            System.out.println("Napacno geslo!");
            RSA.prijava();
        }
    }
    
    public int checkMail() {
        int n=0;
        for (int i=0; i < inbox.length; i++) {
            try {
                if (!inbox[i].getPrebrano())
                    n++;
            }
            catch(NullPointerException e) {}
        }
        return n;
    }
    
    public void shell() {
        int n=checkMail();
        if (n > neprebrana)
            System.out.println("Imas " + n + " nov" + (n>0 && n<3 ? (n==1 ? "o" : "i") : (n>4 || n==0 ? "ih" : "a")) + " sporocil" + (n>0 && n<3 ? (n==1 ? "o" : "i") : (n>4 || n==0 ? "" : "a")) + ".");
        neprebrana = n;
        System.out.print("> ");
        String[] ukaz = tokenize(BranjePodatkov.preberiString().trim());
        if (ukaz.length == 0) {}
        else if (ukaz[0].equalsIgnoreCase("logoff")) {
            neprebrana = 0;
            RSA.prijava();
            return;
        }
        else if (ukaz[0].equalsIgnoreCase("exit"))
            return;
        else if (ukaz[0].equalsIgnoreCase("send"))
            sendMail(ukaz);
        else if (ukaz[0].equalsIgnoreCase("read"))
            readMail(ukaz);
        else if (ukaz[0].equalsIgnoreCase("mailconfig"))
            mailSettings(ukaz);
        else if (ukaz[0].equalsIgnoreCase("rsaconfig"))
            cypherSettings(ukaz);
        else if (ukaz[0].equalsIgnoreCase("userconfig"))
            userSettings(ukaz);
        else if (ukaz[0].equalsIgnoreCase("password"))
            menjajGeslo();
        else if (ukaz[0].equalsIgnoreCase("newkey"))
            menjajKljuc();
        else if (ukaz[0].equalsIgnoreCase("unread"))
            neprebrana = -1;
        else if (ukaz[0].equalsIgnoreCase("hack"))
            hack(ukaz);
        else 
            System.out.println("Neznan ukaz \"" + ukaz[0] + "\"");
        shell();
    }
    
    public String[] tokenize(String ukaz) {
        if (ukaz.length() == 0)
            return new String[0];
        int n=0, pos=0;
        for (int i=0; i < ukaz.length(); i++)
            if (ukaz.charAt(i) == ' ')
                n++;
        String[] token = new String[n+1];
        for (int i=1; i < token.length; i++) {
            token[i-1] = ukaz.substring(pos,ukaz.indexOf(' ',pos)).trim();
            pos = ukaz.indexOf(' ',pos)+1;
        }
        token[n] = ukaz.substring(pos,ukaz.length()).trim();
        return token;
    }
    
    public void sendMail(String[] args) {
        prejemnik=null;
        
        for (int i=1; i < args.length; i++) {
            if (args[i].equals("-s") || args[i].equals("--sifriraj"))
                sifriraj = true;
            else if (args[i].equals("!s") || args[i].equals("!!sifriraj"))
                sifriraj = false;
            else if (args[i].equals("-o") || args[i].equals("--overi"))
                overi = true;
            else if (args[i].equals("!o") || args[i].equals("!!overi"))
                overi = false;
            else if ((args[i].startsWith("-t:") || args[i].startsWith("--to:")) && RSA.najdiUporabnika(args[i].substring(args[i].indexOf(':')+1,args[i].length())) != null)
                prejemnik = RSA.najdiUporabnika(args[i].substring(args[i].indexOf(':')+1,args[i].length()));
            else if (args[i].startsWith("-f:") || args[i].startsWith("--from:"))
                from = args[i].substring(args[i].indexOf(':')+1,args[i].length());
        }
        
        vnesiPrejemnika();
        napisiSporocilo("", "");
        sendMailMenu();
    }
    
    public void napisiSporocilo(String subject, String quote) {
        System.out.println();
        if (!subject.equals(""))
            System.out.println("Za privzeti naslov \"" + subject + "\" pritisni Enter.");
        System.out.print("Vnesi naslov sporocila: ");
        naslov = BranjePodatkov.preberiString();
        if (naslov.equals(""))
            naslov = subject;
        
        System.out.println("\nVnesi sporocilo. Koncas ga z vrstico, ki ima samo piko (.).\n");
        StringBuffer msg = new StringBuffer(quote);
        System.out.print(quote);
        String tmp = BranjePodatkov.preberiString();
        while (!tmp.equals(".")) {
            msg.append(tmp + '\n');
            tmp = BranjePodatkov.preberiString();
        }
        vsebina = msg.toString();
    }

    public void vnesiPrejemnika() {
        boolean napaka=false;
        while (prejemnik == null) {
            if (napaka)
                System.out.println("Prejemnik ne obstaja!");
            System.out.print("\nPrejemnik: ");
            String tmp = BranjePodatkov.preberiString();
            if (tmp.equals("")) {
                shell();
                return;
            }
            prejemnik = RSA.najdiUporabnika(tmp);
            napaka = true;
        }
    }
    
    public void sendMailMenu() {
        System.out.println("\nIzberi:");
        System.out.println("1   Poslji sporocilo");
        System.out.println("2   Nastavitve posiljanja");
        System.out.println("3   Nastavitve sifriranja");
        System.out.println("4   Zavrzi sporocilo");
        System.out.print("Vnesi izbiro: ");
        int izbira = BranjePodatkov.preberiInt();
        String[] args = new String[0];
        switch (izbira) {
            case 1:
                if (shrani) {
                    boolean poslano = addSent(new Sporocilo(from, prejemnik, naslov, vsebina));
                    if (!poslano)
                        System.out.println("Napaka pri shranjevanju sporocila. Preveri zasedenost mape sent.");
                }
                if (sifriraj && overi)
                    vsebina = prejemnik.getKljuc().digitalniPodpis(vsebina, kljuc);
                else if (sifriraj)
                    vsebina = prejemnik.getKljuc().kodirajJavni(vsebina);
                else if (overi)
                    vsebina = kljuc.kodirajPrivatni(vsebina);
                Sporocilo msg = new Sporocilo(from, prejemnik, naslov, vsebina, sifriraj, overi);
                boolean poslano = prejemnik.addInbox(msg);
                if (!poslano)
                    System.out.println("Napaka pri posiljanju sporocila. Prejemnik ima verjetno polno mapo inbox");
                return;
            case 2:
                mailSettings(args);
                break;
            case 3:
                cypherSettings(args);
                break;
            case 4:
                from = "";
                prejemnik = null;
                naslov = "";
                vsebina = "";
                return;
            default:
                System.out.println("Napacna izbira!");
        }
        
        sendMailMenu();
    }

    public void mailSettings(String[] args) {
        boolean menu = true;
        for (int i=1; i < args.length; i++) {
            boolean tmp = false;
            if (args[i].equals("-s") || args[i].equals("--shrani"))
                shrani = true;
            else if (args[i].equals("!s") || args[i].equals("!!shrani"))
                shrani = false;
            else if (args[i].startsWith("-f:") || args[i].startsWith("--from:"))
                from = args[i].substring(args[i].indexOf(':')+1,args[i].length());
            else
                tmp = true;
            menu = menu && tmp;
        }
        if (!menu)
            return;
        
        System.out.println("\nNastavitve posiljanja\n");

        if (prejemnik != null)
            System.out.println("1   Prejemnik");
        System.out.println("2   Posiljatelj");
        System.out.println("3   Shranjevanje " + (shrani ? "(vkljuceno)" : "(izkljuceno)"));
        System.out.println("4   Nazaj");
        System.out.print("Vnesi izbiro: ");
        int izbira = BranjePodatkov.preberiInt();
        
        switch (izbira) {
            case 2:
                System.out.println("\nTrenutni naslov posiljatelja: " + from);
                System.out.println("Prazen vnos ne bo spremenil nastavitve.");
                System.out.println("Pozor! Overjenih sporocil ne bo mogoce brati, ce posiljatelj ni enak tvojemu uporabniskemu imenu oziroma e-postnemu naslovu!");
                System.out.print("Vnesi posiljatelja: ");
                String posiljatelj = BranjePodatkov.preberiString();
                if (!posiljatelj.equals(""))
                    from = posiljatelj;
                break;
            case 3:
                System.out.print("Ali naj se sporocila shranjujejo lokalno? ");
                String jn = BranjePodatkov.preberiString();
                shrani = jaNe(jn, shrani);
                break;
            case 4:
                return;
            case 1:
                if (prejemnik != null) {
                    prejemnik = null;
                    vnesiPrejemnika();
                    break;
                }
            default:
                System.out.println("Napacna izbira!");
        }

        mailSettings(args);
    }

    public boolean jaNe(String jn, boolean bool) {
        if (jn.length() == 0)
            return bool;
        switch(jn.charAt(0)) {
            case 'd':
            case 'D':
            case 'j':
            case 'J':
            case 'y':
            case 'Y':
                return true;
            case 'n':
            case 'N':
                return false;
            default:
                return bool;
        }
    }
    
    public void cypherSettings(String[] args) {
        boolean menu = true;
        for (int i=1; i < args.length; i++) {
            boolean tmp = false;
            if (args[i].equals("-s") || args[i].equals("--sifriraj"))
                sifriraj = true;
            else if (args[i].equals("!s") || args[i].equals("!!sifriraj"))
                sifriraj = false;
            else if (args[i].equals("-o") || args[i].equals("--overi"))
                overi = true;
            else if (args[i].equals("!o") || args[i].equals("!!overi"))
                overi = false;
            else if (args[i].equals("-k") || args[i].equals("--key"))
                menjajKljuc();
            else
                tmp = true;
            menu = menu && tmp;
        }
        if (!menu)
            return;

        System.out.println("\nNastavitve sifriranja\n");

        System.out.println("1   Sifriranje " + (sifriraj ? "(vkljuceno)" : "(izkljuceno)"));
        System.out.println("2   Overjanje " + (overi ? "(vkljuceno)" : "(izkljuceno)"));
        System.out.println("3   Menjaj kljuc");
        System.out.println("4   Nazaj");
        System.out.print("Vnesi izbiro: ");
        int izbira = BranjePodatkov.preberiInt();
        String jn;
        
        switch (izbira) {
            case 1:
                System.out.print("Ali naj se sporocila sifrirajo? ");
                jn = BranjePodatkov.preberiString();
                sifriraj = jaNe(jn, sifriraj);
                break;
            case 2:
                System.out.print("Ali naj se sporocila overjajo? ");
                jn = BranjePodatkov.preberiString();
                overi = jaNe(jn, overi);
                break;
            case 3:
                menjajKljuc();
                break;
            case 4:
                return;
            default:
                System.out.println("Napacna izbira!");
        }

        cypherSettings(args);
    }

    
    public void readMail(String[] args) {
        boolean menu = true;
        int n;
        String msg;
        Sporocilo[] mapa = inbox;
        for (int i=1; i < args.length; i++) {
            try {
                n = Integer.parseInt(args[i]);
                mapa[n].izpisi(n);
                if (neprebrana > 0 && mapa == inbox)
                    neprebrana--;
                inboxShell(n, mapa);
                menu = false;
            }
            catch(NumberFormatException e) {
                if (args[i].equals("-i") || args[i].equals("--inbox"))
                    mapa = inbox;
                else if (args[i].equals("-s") || args[i].equals("--sent"))
                    mapa = sent;
            }
            catch(NullPointerException e) {}
            catch(IndexOutOfBoundsException e) {}
        }
        if (!menu)
            return;
        
        if (mapa == sent)
            System.out.println("\nPoslana sporocila:");
        else
            System.out.println("\nPrejeta sporocila:");
        for (int i=0; i < mapa.length; i++) {
            try {
                if (mapa == sent)
                    System.out.println(i + (mapa[i].getPrebrano() ? "" : "*") + "\tZa: " + mapa[i].getPrejemnik() + ", naslov: " + mapa[i].getNaslov());
                else
                    System.out.println(i + (mapa[i].getPrebrano() ? "" : "*") + "\tOd: " + mapa[i].getPosiljatelj() + ", naslov: " + mapa[i].getNaslov());
            }
            catch(NullPointerException e) {}
        }
        if (mapa == sent)
            System.out.println("\n" + mapa.length + "\tMapa inbox");
        else
            System.out.println("\n" + mapa.length + "\tMapa sent");
        System.out.println((mapa.length+1) + "\tNazaj");
        System.out.print("Vnesi izbiro: ");
        int izbira = BranjePodatkov.preberiInt();
        
        if (izbira == mapa.length+1)
            return;
        else if (izbira == mapa.length) {
            args = new String[2];
            if (mapa == sent)
                args[1] = "-i";
            else if (mapa == inbox) {
                args[1] = "-s";
            }
        } else if (izbira < 0 || izbira > mapa.length+1)
            System.out.println("Napacna izbira!");
        else {
            try {
                mapa[izbira].izpisi(izbira);
                if (neprebrana > 0 && mapa == inbox)
                    neprebrana--;
                inboxShell(izbira, mapa);
            }
            catch(NullPointerException e) {
                System.out.println("Napacna izbira!");
            }
        }
        readMail(args);
    }
    
    public void inboxShell(int n, Sporocilo[] mapa) {
        if (mapa == sent)
            System.out.print("sent#" + n + "> ");
        else
            System.out.print("inbox#" + n + "> ");
        String[] ukaz = tokenize(BranjePodatkov.preberiString().trim());
        if (ukaz.length == 0 || ukaz[0].equals("read") || ukaz[0].equals("exit"))
            return;
        else if (ukaz[0].equals("unread")) {
            mapa[n].setPrebrano(false);
            if (mapa == inbox)
                neprebrana++;
        } else if (ukaz[0].equals("delete")) {
            mapa[n] = null;
            return;
        }
        else if (ukaz[0].equals("reply")) {
            prejemnik = RSA.najdiUporabnika(mapa[n].getPosiljatelj());
            if (prejemnik == null)
                System.out.println("Napaka: ni mogoce identificirati posiljatelja!");
            else {
                napisiSporocilo((mapa[n].getNaslov().startsWith("Re:") ? "" : "Re: ") + mapa[n].getNaslov(), mapa[n].quote());
                sendMailMenu();
                return;
            }
        } else if (ukaz[0].equals("forward")) {
            prejemnik = null;
            vnesiPrejemnika();
            napisiSporocilo((mapa[n].getNaslov().startsWith("Fwd:") ? "" : "Fwd: ") + mapa[n].getNaslov(), mapa[n].quote());
            sendMailMenu();
            return;
        } else 
            System.out.println("Neznan ukaz \"" + ukaz[0] + "\"");
        inboxShell(n, mapa);
    }
    
    public void userSettings(String[] args) {
        boolean menu = true;
        for (int i=1; i < args.length; i++) {
            boolean tmp = false;
            if ((args[i].startsWith("-n:") || args[i].startsWith("--name:")))
                ime = args[i].substring(args[i].indexOf(':')+1,args[i].length());
            else if (args[i].startsWith("-s:") || args[i].startsWith("--surname:"))
                priimek = args[i].substring(args[i].indexOf(':')+1,args[i].length());
            else if (args[i].startsWith("-e:") || args[i].startsWith("--email:"))
                email = args[i].substring(args[i].indexOf(':')+1,args[i].length());
            else if (args[i].equals("-p") || args[i].equals("--password"))
                menjajGeslo();
            else
                tmp = true;
            menu = menu && tmp;
        }
        
        if (!menu)
            return;
        
        System.out.println("\nIzberi:");
        System.out.println("1   Ime");
        System.out.println("2   Priimek");
        System.out.println("3   E-postni naslov");
        System.out.println("4   Geslo");
        System.out.println("5   Nazaj");
        System.out.print("Vnesi izbiro: ");
        int izbira = BranjePodatkov.preberiInt();
        
        switch (izbira) {
            case 1:
                System.out.println("\nTvoje ime: " + ime);
                System.out.println("Prazen vnos ne bo spremenil nastavitve.");
                System.out.print("Vnesi ime: ");
                String name = BranjePodatkov.preberiString();
                if (!name.equals(""))
                    ime = name;
                break;
            case 2:
                System.out.println("\nTvoj priimek: " + priimek);
                System.out.println("Prazen vnos ne bo spremenil nastavitve.");
                System.out.print("Vnesi priimek: ");
                String surname = BranjePodatkov.preberiString();
                if (!surname.equals(""))
                    priimek = surname;
                break;
            case 3:
                System.out.println("\nTvoj e-postni naslov: " + email);
                System.out.println("Prazen vnos ne bo spremenil nastavitve.");
                System.out.println("Pozor! Sprememba e-postnega naslova lahko pomeni, da tvojih sifriranih oziroma overjenih ze poslanih sporocil ne bo mogoce vec brati!");
                System.out.print("Vnesi e-postni naslov: ");
                String posta = BranjePodatkov.preberiString();
                if (!posta.equals("")) {
                    if (from.equals(email))
                        from = posta;
                    email = posta;
                }
                break;
            case 4:
                menjajGeslo();
            case 5:
                return;
            default:
                System.out.println("Napacna izbira!");
        }
        
        userSettings(args);
    }
    
    public void menjajGeslo() {
        System.out.print("Vnesi staro geslo: ");
        String staroGeslo = BranjePodatkov.preberiString();
        System.out.print("Vnesi novo geslo: ");
        String geslo1 = BranjePodatkov.preberiString();
        System.out.print("Ponovi novo geslo: ");
        String geslo2 = BranjePodatkov.preberiString();
        if (novoGeslo(staroGeslo, geslo1, geslo2))
            System.out.println("Geslo uspesno spremenjeno!");
        else
            System.out.println("Sprememba gesla ni uspela!");
    }
    
    public void menjajKljuc() {
        System.out.println("Pozor! Menjava kljuca bo onemogocila branje ze poslanih overjenih ter prejetih sifriranih sporocil.");
        System.out.print("Ali zelis res zamenjati kljuc? ");
        String jn = BranjePodatkov.preberiString();
        if (jaNe(jn,false)) {
            kljuc = new RSAKljuc();
            System.out.println("Kljuc uspesno zamenjan!");
        }
    }

    public void hack(String[] args) {
        Uporabnik u = null;
        try {
            u = RSA.najdiUporabnika(args[1]);
        }
        catch(IndexOutOfBoundsException e) {}

        if (u == null) {
            System.out.print("Vnesi uporabnika: ");
            u = RSA.najdiUporabnika(BranjePodatkov.preberiString());
        }

        if (u == null) {
            System.out.println("Uporabnik ne obstaja!");
            return;
        } else {
            args = new String[2];
            args[1] = u.getUser();
        }
        

        System.out.println("\nPrejeta sporocila uporabnika " + u + ":");
        for (int i=0; i < u.inbox.length; i++) {
            try {
                if (!u.inbox[i].getPrebrano())
                    System.out.println(i + "\tOd: " + u.inbox[i].getPosiljatelj() + ", naslov: " + u.inbox[i].getNaslov());
            }
            catch(NullPointerException e) {}
        }
        System.out.println("\n" + u.inbox.length + "\tMenjaj uporabnika");
        System.out.println((u.inbox.length+1) + "\tNazaj");
        System.out.print("Vnesi izbiro: ");
        int izbira = BranjePodatkov.preberiInt();
        
        if (izbira == u.inbox.length+1)
            return;
        else if (izbira == u.inbox.length)
            args = new String[1];
        else if (izbira < 0 || izbira > u.inbox.length+1)
            System.out.println("Napacna izbira!");
        else {
            try {
                if (!u.inbox[izbira].getPrebrano()) {
                    System.out.println("\nSporocilo #" + izbira);
                    StringBuffer msg = new StringBuffer("Od: ");
                    msg.append(((RSA.najdiUporabnika(u.inbox[izbira].getPosiljatelj()) == null) ? u.inbox[izbira].getPosiljatelj() : RSA.najdiUporabnika(u.inbox[izbira].getPosiljatelj()).toString()) + "\nZa: ");
                    msg.append(u.inbox[izbira].getPrejemnik() + "\nSifrirano: " + (u.inbox[izbira].getSifrirano() ? "ja" : "ne") + "\nOverjeno: " + (u.inbox[izbira].getOverjeno() ? "ja" : "ne") + "\nNaslov: " + u.inbox[izbira].getNaslov() + "\n\n");
                    if (u.inbox[izbira].getOverjeno())
                        msg.append(RSA.najdiUporabnika(u.inbox[izbira].getPosiljatelj()).getKljuc().dekodirajJavni(u.inbox[izbira].getVsebina()));
                    else
                        msg.append(u.inbox[izbira].getVsebina());
                    System.out.println('\n' + msg.toString() + '\n');
                    hackShell(izbira, u);
                } else
                    System.out.println("Napacna izbira!");
            }
            catch(NullPointerException e) {
                System.out.println("Napacna izbira!");
            }
        }
        hack(args);
    }

    public void hackShell(int n, Uporabnik u) {
        System.out.print(u.getUser() + "/inbox#" + n + "> ");
        String[] ukaz = tokenize(BranjePodatkov.preberiString().trim());
        if (ukaz.length == 0 || ukaz[0].equals("read") || ukaz[0].equals("exit"))
            return;
        else if (ukaz[0].equals("reply")) {
            prejemnik = RSA.najdiUporabnika(u.inbox[n].getPosiljatelj());
            if (prejemnik == null)
                System.out.println("Napaka: ni mogoce identificirati posiljatelja!");
            else if (u.inbox[n].getSifrirano())
                System.out.println("Napaka: ni mogoce desifrirati sporocila!");
            else {
                napisiSporocilo((u.inbox[n].getNaslov().startsWith("Re:") ? "" : "Re: ") + u.inbox[n].getNaslov(), u.inbox[n].quote());
                sendMailMenu();
                return;
            }
        } else if (ukaz[0].equals("forward")) {
            if (u.inbox[n].getSifrirano())
                System.out.println("Napaka: ni mogoce desifrirati sporocila!");
            else {
                prejemnik = null;
                vnesiPrejemnika();
                napisiSporocilo((u.inbox[n].getNaslov().startsWith("Fwd:") ? "" : "Fwd: ") + u.inbox[n].getNaslov(), u.inbox[n].quote());
                sendMailMenu();
                return;
            }
        } else 
            System.out.println("Neznan ukaz \"" + ukaz[0] + "\"");
        hackShell(n, u);
    }

    public String getUser() {
        return user;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getIme() {
        return ime;
    }
    
    public String getPriimek() {
        return priimek;
    }
    
    public RSAKljuc getKljuc() {
        return kljuc;
    }
    
    public boolean preveriGeslo(String geslo) {
        return geslo.equals(this.geslo);
    }
    
    public void setIme(String ime) {
        this.ime = ime;
    }
    
    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }
    
    public boolean novoGeslo(String staroGeslo, String geslo1, String geslo2) {
        if (preveriGeslo(staroGeslo) && geslo1.equals(geslo2))
            this.geslo = geslo1;
        else
            return false;
        return true;
    }
    
    public boolean addInbox(Sporocilo msg) {
        for (int i=0; i < inbox.length; i++)
            if (inbox[i] == null) {
                inbox[i] = msg;
                return true;
            }
        return false;
    }
    
    public boolean addSent(Sporocilo msg) {
        for (int i=0; i < sent.length; i++)
            if (sent[i] == null) {
                sent[i] = msg;
                return true;
            }
        return false;
    }
    
    public boolean equals(Uporabnik u) {
        return user.equals(u.getUser()) || email.equals(u.getEmail());
    }
    
    public String toString() {
        return user + " (" + ime + " " + priimek + ", " + email + ")";
    }
}

class Sporocilo {
    String vsebina, naslov, posiljatelj;
    Uporabnik prejemnik;
    boolean sifrirano, overjeno, prebrano;
    
    public Sporocilo(String from, Uporabnik to, String naslov, String vsebina) {
        this.naslov = naslov;
        this.vsebina = vsebina;
        this.posiljatelj = from;
        this.prejemnik = to;
        this.sifrirano = false;
        this.overjeno = false;
        this.prebrano = true;
    }
    
    public Sporocilo(String from, Uporabnik to, String naslov, String vsebina, boolean sifrirano, boolean overjeno) {
        this.naslov = naslov;
        this.vsebina = vsebina;
        this.posiljatelj = from;
        this.prejemnik = to;
        this.sifrirano = sifrirano;
        this.overjeno = overjeno;
        this.prebrano = false;
        
        if (RSA.najdiUporabnika(from) == null)
            this.overjeno = false;
    }
    
    public Sporocilo(Sporocilo msg, boolean sifrirano, boolean overjeno) {
        this.naslov = msg.getNaslov();
        this.vsebina = msg.getVsebina();
        this.posiljatelj = msg.getPosiljatelj();
        this.prejemnik = msg.getPrejemnik();
        this.sifrirano = sifrirano;
        this.overjeno = overjeno;
        this.prebrano = false;
        
        if (RSA.najdiUporabnika(msg.getPosiljatelj()) == null)
            this.overjeno = false;
    }
    
    public String getVsebina() {
        return vsebina;
    }
    
    public String getNaslov() {
        return naslov;
    }
    
    public String getPosiljatelj() {
        return posiljatelj;
    }
    
    public Uporabnik getPrejemnik() {
        return prejemnik;
    }
    
    public boolean getSifrirano() {
        return sifrirano;
    }
    
    public boolean getOverjeno() {
        return overjeno;
    }
        
    public boolean getPrebrano() {
        return prebrano;
    }
    
    public void setPrebrano(boolean prebrano) {
        this.prebrano = prebrano;
    }
    
    public String quote() {
        StringBuffer msg = new StringBuffer("> " + toString());
        int n=0, i=0;
        while (toString().indexOf('\n',n) > -1) {
            i++;
            msg.insert(toString().indexOf('\n',n) + 2*i+1 , "> ");
            n = toString().indexOf('\n',n) + 1;
        }
        msg.insert(0,posiljatelj + " je napisal/a:\n");
        msg.append("\n\n");
        return msg.toString();
    }
    
    public void izpisi(int n) {
        prebrano = true;
        System.out.println("\nSporocilo #" + n);
        StringBuffer msg = new StringBuffer("Od: ");
        msg.append(((RSA.najdiUporabnika(posiljatelj) == null) ? posiljatelj : RSA.najdiUporabnika(posiljatelj).toString()) + "\nZa: ");
        msg.append(prejemnik + "\nSifrirano: " + (sifrirano ? "ja" : "ne") + "\nOverjeno: " + (overjeno ? "ja" : "ne") + "\nNaslov: " + naslov + "\n\n");
        msg.append(toString());
        System.out.println('\n' + msg.toString() + '\n');
    }
    
    public String toString() {
        String msg;
        if (sifrirano && overjeno)
            return RSA.najdiUporabnika(posiljatelj).getKljuc().overiPodpis(vsebina, prejemnik.getKljuc());
        else if (sifrirano)
            return prejemnik.getKljuc().dekodirajPrivatni(vsebina);
        else if (overjeno)
            return RSA.najdiUporabnika(posiljatelj).getKljuc().dekodirajJavni(vsebina);
        else
            return vsebina;
    }
}

class RSAKljuc {
    long m, d, e;
    final static long spMeja=4096;
    final static long zgMeja=8192;

    public RSAKljuc() {
        generirajKljuc();
    }
    
    public String kodirajJavni(String msg) {
        long[] koda = kodirajSporocilo(msg);
        String sifra=sifrirajSporocilo(koda,Math.min(d,e));
        return sifra;
    }
    
    public String dekodirajJavni(String msg) {
        long[] koda = dekodirajSporocilo(msg);
        String sifra=sifrirajSporocilo(koda,Math.min(d,e));
        return strip(sifra);
    }
    
    public String kodirajPrivatni(String msg) {
        long[] koda = kodirajSporocilo(msg);
        String sifra=sifrirajSporocilo(koda,Math.max(d,e));
        return sifra;
    }
    
    public String dekodirajPrivatni(String msg) {
        long[] koda = dekodirajSporocilo(msg);
        String sifra=sifrirajSporocilo(koda,Math.max(d,e));
        return strip(sifra);
    }
    
    public String digitalniPodpis(String msg, RSAKljuc k) {
        String sifra=kodirajJavni(msg);
        sifra=razsiri(sifra);
        //System.out.println("Razsirjena sifra:\n" + sifra);
        sifra=k.kodirajPrivatni(sifra);
        return sifra;
    }

    public String overiPodpis(String msg, RSAKljuc k) {
        String sifra=dekodirajJavni(msg);
        //System.out.println("Dekodirana sifra:\n" + sifra);
        sifra=stisni(sifra);
        sifra=k.dekodirajPrivatni(sifra);
        return sifra;
    }
    
    public void generirajKljuc() {
        long[] pr = generirajPrastevila();
        int a=(int)(Math.floor(pr.length*Math.random())), b;
        do {
            b=(int)(Math.floor(pr.length*Math.random()));
        } while (b==a);
        m=pr[a]*pr[b];
        //System.out.println(pr[a] + " " + pr[b]);
        long phi = (pr[a]-1)*(pr[b]-1);
        najdiDE(phi);
        //System.out.println("Javni kljuc: " + m + ", " + Math.min(d,e));
        //System.out.println("Privatni kljuc: " + m + ", " + Math.max(d,e));
        //System.out.println((d*e)%phi);
    }
    
    public static long[] generirajPrastevila() {
        long[] pr=new long[(int)(zgMeja+1)/2];
        int n=0;
        
        pr[0]=2;
        for (int i=1; 2*i < zgMeja; i++)
            pr[i]=2*i+1;
        
        for (int i=1; i < Math.sqrt(zgMeja); i++) {
            if (pr[i] != 0) {
                for (int j=(int)(Math.pow(pr[i], 2)/2); 2*j < zgMeja; j++) {
                    if (pr[j]%pr[i] == 0)
                        pr[j]=0;
                }
            }
        }

        for (int i=0; 2*i < zgMeja; i++) {
            if (pr[i] >= spMeja)
                n++;
        }
        
        long[] prastevila=new long[n];

        for (int i=0, j=0; 2*i < zgMeja; i++) {
            if (pr[i] >= spMeja) {
                prastevila[j] = pr[i];
                j++;
            }
        }
        
        return prastevila;
    }
    
    public void najdiDE(long phi) {
        long tmp=Math.round(Math.random()*10000)*2+3;
        long a, b, c, a1, b1, c1, a2, b2, c2;
        do {
            tmp += 2;
            a=phi;
            a1=1;
            a2=0;
            b=tmp;
            b1=0;
            b2=1;
            //System.out.println(a + " = " + a1 + "*" + phi + " + " + a2 + "*" + tmp);
            //System.out.println(b + " = " + b1 + "*" + phi + " + " + b2 + "*" + tmp);
            do {
                c1=b1;
                b1=a1-(long)(a/b)*b1;
                a1=c1;
                c2=b2;
                b2=a2-(long)(a/b)*b2;
                a2=c2;
                c=b;
                b=a%b;
                a=c;
                //System.out.println(b + " = " + b1 + "*" + phi + " + " + b2 + "*" + tmp);
            } while (b != 0);
            //System.out.println();
        } while (a > 1 || a2 < 0);
        e=tmp;
        d=Math.abs(a2);
    }
    
    public long[] kodirajSporocilo(String msg) {
        long[] koda = new long[(msg.length()+1)/2];
        for (int i=1, j=koda.length-1; i <= msg.length(); i++) {
            if (i%2 == 1)
                koda[j]=(int)(msg.charAt(msg.length()-i));
            else {
                koda[j]+=65536L*(int)(msg.charAt(msg.length()-i));
                j--;
            }
        }
        return koda;
    }
    
    public long[] dekodirajSporocilo(String msg) {
        long[] koda = new long[(msg.length()+2)/3];
        for (int i=1, j=koda.length-1; i <= msg.length(); i++) {
            //System.out.println((int)(msg.charAt(msg.length()-i)));
            if (i%3 == 1)
                koda[j]=(int)(msg.charAt(msg.length()-i));
            else if (i%3 == 2)
                koda[j]+=65536L*(int)(msg.charAt(msg.length()-i));
            else {
                koda[j]+=4294967296L*(int)(msg.charAt(msg.length()-i));
                j--;
            }
        }
        return koda;
    }
    
    public String sifrirajSporocilo(long[] koda, long p) {
        StringBuffer sifra = new StringBuffer();
        long tmp;
        char ch;
        
        for (int i=0; i < koda.length; i++) {
            tmp = modulskaPotenca(koda[i], p);
            //System.out.println(tmp);
            ch = (char)(tmp/4294967296L);
            sifra.append(ch);
            tmp = tmp%4294967296L;
            ch = (char)(tmp/65536L);
            sifra.append(ch);
            tmp = tmp%65536L;
            ch = (char)(tmp);
            sifra.append(ch);
        }
        
        return sifra.toString();
    }
    
    public long modulskaPotenca(long koda, long p) {
        String bin = Long.toString(p,2);
        long[] exp = new long[bin.length()];
        exp[0]=koda;
        double res=koda;
        //System.out.println(koda + "^" + p + " mod " + m);
        for (int i=1; i < exp.length; i++) {
            exp[i] = (exp[i-1]*exp[i-1])%m;
            if (bin.charAt(bin.length()-i-1) == '1') {
                //System.out.println((long)res + "*" + exp[i] + " = " + (long)(res*exp[i]) + " id " + (long)((res*exp[i])%m));
                res = (res*exp[i])%m;
            }
        }
        
        return (long)(res);
    }
    
    public static String strip(String msg) {
        StringBuffer sporocilo = new StringBuffer();
        for (int i=0; i < msg.length(); i++)
            if (msg.charAt(i) != '\u0000')
                sporocilo.append(msg.charAt(i));
        return sporocilo.toString();
    }
    
    public static String razsiri(String msg) {
        StringBuffer sporocilo = new StringBuffer();
        for (int i=0; i < msg.length(); i++) {
            if (msg.charAt(i) != '\u0000') {
                sporocilo.append((char)((int)(msg.charAt(i))/256+2));
                sporocilo.append((char)((int)(msg.charAt(i))%256));
            } else
                sporocilo.append("\u0001\uFFFF");
        }
        return sporocilo.toString();
    }

    public static String stisni(String msg) {
        StringBuffer sporocilo = new StringBuffer();
        int a;
        for (int i=0; i < msg.length(); i+=2) {
            a = ((int)(msg.charAt(i))-2)*256;
            try {
                a = (int)(msg.charAt(i+1)) + a;
                if (a==65279)
                    a=0;
            }
            catch (IndexOutOfBoundsException e) {}
            sporocilo.append((char)(a));
        }
        return sporocilo.toString();
    }
    
    public String toString() {
        return m + ", " + Math.min(d,e);
    }
    
    public boolean equals(RSAKljuc k) {
        return (m == k.m) && (Math.min(d,e) == Math.min(k.d,k.e));
    }
    
    public boolean equals(long m, long d) {
        return (this.m == m) && ((this.d == d) || (this.e == d));
    }
    
    public boolean equals(long m, long d, long e) {
        return (this.m == m) && (Math.min(this.d,this.e) == Math.min(d,e)) && (Math.max(this.d,this.e) == Math.max(d,e));
    }

}