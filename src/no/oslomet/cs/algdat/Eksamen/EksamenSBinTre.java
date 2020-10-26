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

    public boolean leggInn(T verdi) {
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

    public boolean fjern(T verdi) { // Programkode 5.2 8 d)
        if (verdi == null) return false;  // treet har ingen nullverdier, og verdien må være i treet
        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null) {          // leter etter verdi
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi
        if (p.venstre == null || p.høyre == null){  // Tilfelle 1) og 2)
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot){
                rot = b;
                if(rot!=null) {
                    rot.forelder = null;
                }
            }
            else if (p == q.venstre){
                q.venstre = b;
                if(b!=null) {
                    b.forelder = q;
                }
            }
            else{
                q.høyre = b;
                if(b!=null) {
                    b.forelder = q;
                }
            }
        }
        else{  // Tilfelle 3)
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null) {
                s = r;    // s er forelder til r
                r = r.venstre;
            }
            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }
        antall--;   // det er nå én node mindre i tree;//gjort en endring
        return true;
    }

    public int fjernAlle(T verdi) {
        int teller=0;
        if(verdi==null || antall==0 || !inneholder(verdi)) return teller;
        else{
            while(inneholder(verdi)){
                fjern(verdi);
                teller++;
            }
        }
        return teller;
    }

    public int antall(T verdi) {
        int teller = 0;
        if(antall()>0 && inneholder(verdi)){
            Node <T> node = rot;
            while(node!=null) {
                if(comp.compare(verdi,node.verdi)<0) node=node.venstre;
                else if(comp.compare(verdi,node.verdi)>0) node=node.høyre;
                else{
                    node=node.høyre;
                    teller++;
                }
            }
        }
        return teller;
    }

    public void nullstill() {//fjerner første postorden hver gang, den har ingen barn, slik at den er enklest å fjerne
        if(antall()>0){
            Node <T> p = førstePostorden(rot), q = p.forelder;
            while(antall>1 && p!=null){
                if(p==q.venstre){
                    q.venstre=null;
                }
                else{
                    q.høyre=null;
                }
                p=nestePostorden(p);
                antall--;
                antall();
            }
            rot=null;
            antall--;
        }
    }

    private static <T> Node<T> førstePostorden(Node<T> p) { //kompendiet programkode 5.1.7 h)
        while(p.forelder!=null){
            p=p.forelder;
        }
        while (true) {//så lenge p er ulik null kjøres programmet
            if (p.venstre != null) p = p.venstre; //sjekker om venstrebarn er ulik null, trigges --> p = p.venstre
            else if (p.høyre != null) p = p.høyre;//sjekker om høyrebarn er ulik null, trigges --> p = p.høyre
            else return p; //returnerer p når de over ikke trigges lenger
        }
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        //fra kompendiet programkode 5.1.7
        if (p.forelder == null) return null;             //returnerer null hvis p er forelder (siste verdi i postorden)
        else if (p == p.forelder.høyre) p = p.forelder;  //sjekker om p er et høyre barn, hvis det er forelder neste i postorden
        else if (p == p.forelder.venstre) {              //sjekker om p er venstrebarn
            if (p.forelder.høyre == null) p = p.forelder;//ser om høyrebarn til forelder ikke eksisterer, hvis dette
                                                         //blir neste postorden p sin forelder
            else {
                p = p.forelder.høyre;                    //sin forelder sitt høyrebarn.
                while (p.venstre != null || p.høyre!=null){
                    if(p.venstre!=null) p = p.venstre;   //hvis ingen over trigger, går jeg så lagt ned mot venstre fra p
                    else p=p.høyre;                      //else p=p.høyre;
                    }

                }

            }
        return p;//returnerer p ettersom jeg har endret på den i if-sjekkene
    }

    public void postorden(Oppgave<? super T> oppgave) {
        if (antall > 0) {
            Node<T> p = førstePostorden(rot);
            oppgave.utførOppgave(p.verdi);
            while (p.forelder!=null) {
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
        if(p.forelder!=null) {
            p = nestePostorden(p);
            assert p != null;
            oppgave.utførOppgave(p.verdi);
            postordenRecursive(p, oppgave);
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
        EksamenSBinTre <K> tre = new EksamenSBinTre<>(c);
        for (K verdi : data) {
            tre.leggInn(verdi);
        }
        return tre;
    }


    public static void main(String[] args) {
    }


} // ObligSBinTre
