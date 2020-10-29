package no.oslomet.cs.algdat.Eksamen;



import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) { // skal ligge i class SBinTre, programkode i kompendiet 5.2.3 a)
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                 // én verdi mer i treet;//én endring gjort i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {// hører til klassen SBinTre, programkode fra kompendiet 5.2.8 d)
        if (verdi == null || !inneholder(verdi) || antall()==0) return false; // treet har ingen nullverdier

        Node<T> p = rot, q = null;// q skal være forelder til p

        while (p != null) {// leter etter verdi
            int cmp = comp.compare(verdi,p.verdi);   // sammenligner
            if (cmp < 0) {q = p; p = p.venstre;}  // går til venstre
            else if (cmp > 0) {q = p; p = p.høyre;} // går til høyre
            else break;  // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null){   // Tilfelle 1) og 2)
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot){ //hvis rot skal fjernes
                rot = b;
                if(rot!=null) { //hvis rot er eneste i treet
                    rot.forelder = null;
                }
            } else if (p == q.venstre){  //hvis et venstrebarn skal fjernes
                q.venstre = b;
                if(b!=null) {   //hvis det finnes et barn til fjernet element
                    b.forelder = q;
                }
            } else{   //hvis er høyrebarn skal fjernes
                q.høyre = b;
                if(b!=null) {   //hvis det finnes et barn til fjernet element
                    b.forelder = q;
                }
            }
        } else{ // Tilfelle 3)
            Node<T> s = p, r = p.høyre;  // finner neste i inorden
            while (r.venstre != null) {
                s = r;  // s er forelder til r
                r = r.venstre;
            }
            p.verdi = r.verdi;  // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }
        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        int teller=0;  //teller som teller antall som er fjernet
        if(verdi==null || antall==0) return teller; //slik at jeg ikke får fjernet elementer jeg ikke skal(0 blir returnert)
        else{
            while(inneholder(verdi)){  //holder på så lenge verdi fremdeles er i treet
                fjern(verdi);  //kaller på fjernmetoden, antall oppdateres der
                teller++;   //legger til i teller hver gang jeg har fjernet et element
            }
        }
        return teller;   //returnerer teller til slutt
    }

    public int antall(T verdi) {
        int teller = 0;    //bruker teller her også til å telle antall elementer som er like
        if(antall()>0 && inneholder(verdi)){    //sjekk for om verdi i det hele tatt er i treet, og om treet "eksisterer"
            Node <T> node = rot;
            while(node!=null) { //holder på til jeg er ute av treet
                int cmp = comp.compare(verdi,node.verdi);   //sammenligner verdier
                if(cmp<0) node=node.venstre;
                else if(cmp>0) node=node.høyre;
                else{
                    node=node.høyre;
                    teller++;   //for like verdier teller jeg en opp
                }
            }
        }
        return teller;  //returnerer resultatet
    }

    public void nullstill() {//fjerner første postorden hver gang, den har ingen barn, slik at den er enklest å fjerne
        if(antall>0){   //sjekker at treet ikke er tomt
            Node <T> p = førstePostorden(rot), q = p.forelder;  //oppretter en node og foreldre til noden
            while(antall>1){
                if(p==q.venstre){ //sjekk om jeg skal fjerne venstre eller høyrebarn
                    q.venstre=null;
                    p.verdi=null;
                } else{
                    p.verdi=null;
                    q.høyre=null;
                }
                p=førstePostorden(rot); //finner neste i postorden, slik at jeg vet jeg ikke har noen barn å ta hensyn til
                antall--;   //trekker fra i antall etter jeg har fjernet
            }
            rot=null;  //til slutt nullstilles noden
            antall--;  //trekker fra i antall etter jeg har fjernet
        }
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {
        while(p.forelder!=null){ //kommer meg først opp til rot-noden
            p=p.forelder;
        }
        while (true) { //holder på helt fram til p.venstre og p.høyre er lik null
            if (p.venstre != null) p = p.venstre; //ønsker å gå mot venstre hvis mulig
            else if (p.høyre != null) p = p.høyre; //går til høyre hvis venstre ikke går
            else return p;   //returnerer node p hvis den ikke har noen barn
        }
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        if (p.forelder == null) return null;    //returnerer null dersom jeg er på rot-noden
        else if (p == p.forelder.høyre) p = p.forelder;     //hvis p er et høyrebarn returneres foreldrenoden
        else if (p == p.forelder.venstre) {     //litt annerledes for venstrebarn
            if (p.forelder.høyre == null) p = p.forelder;   //enkelt for alenebarn
            else {
                p = p.forelder.høyre;   //samme while-løkke som i førstePostorden()
                while (p.venstre != null || p.høyre!=null){
                    if(p.venstre!=null) p = p.venstre;
                    else p=p.høyre;
                    }
                }
            }
        return p;  //returnerer noden jeg kom fram til
    }

    public void postorden(Oppgave<? super T> oppgave) {
        if (antall > 0) {
            Node<T> p = førstePostorden(rot);
            oppgave.utførOppgave(p.verdi);
            while (p.forelder!=null) {  //kjører en while-løkke så lenge p.forelder ikke er null (ikke kommet til rotnoden)
                p = nestePostorden(p);
                assert p != null;
                oppgave.utførOppgave(p.verdi);
            }
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        Node<T> p = førstePostorden(rot);
        oppgave.utførOppgave(p.verdi);
        postordenRecursive(p,oppgave);

    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (antall > 0) {
            if (p.forelder != null) {
                p = nestePostorden(p);
                assert p != null;
                oppgave.utførOppgave(p.verdi);
                postordenRecursive(p, oppgave);  //rekursiv metode, kaller seg selv, setter p til nestepostorden hver gang
            }
        }
    }

    public ArrayList<T> serialize() {//Programkode 5.1.6 a) (gjort litt om for å fungere for dette eksempelet)
        if (tom()) return null;                  // tomt tre

        Queue<Node<T>> queue = new ArrayDeque<>();
        ArrayList<T> liste = new ArrayList<>();

        queue.add(rot);                     // legger inn roten
        while (queue.size()>0)                    // så lenge som køen ikke er tom
        {
            Node <T> p = queue.poll();            // tar ut fra køen
            liste.add(p.verdi);                      //legger inn p (fjernet element fra køen) i queue

            if (p.venstre != null) queue.add(p.venstre);
            if (p.høyre != null) queue.add(p.høyre);
        }
        return liste;
    }


    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre <K> tre = new EksamenSBinTre<>(c);  //oppretter et nytt tre
        for (K verdi : data) {  //har en ArrayList med verdier som legges inn i treet
            tre.leggInn(verdi);
        }
        return tre; //nytt tre returneres
    }



} // ObligSBinTre
