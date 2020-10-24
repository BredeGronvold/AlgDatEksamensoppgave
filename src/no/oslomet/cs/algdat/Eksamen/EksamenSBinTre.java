package no.oslomet.cs.algdat.Eksamen;


import java.io.LineNumberReader;
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

        p = new Node(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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

    public void nullstill() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
            Node<T> p = rot;
            p = førstePostorden(p);
            oppgave.utførOppgave(p.verdi);
            while (p.forelder!=null) {
                p = nestePostorden(p);
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
        if (p.forelder == null) {
            return;
        } else {
            p = nestePostorden(p);
            oppgave.utførOppgave(p.verdi);
            postordenRecursive(p, oppgave);
        }
    }

    public ArrayList<T> serialize() {//Programkode 5.1.6 a) (gjort litt om for å fungere for dette eksempelet)
        if (tom()) return null;                  // tomt tre

        ArrayList<Node> kø = new ArrayList<>();   // Se Avsnitt 4.2.2
        kø.add(rot);                     // legger inn roten
        ArrayList<T> queue = new ArrayList<>(); //oppretter en liste som skal returneres

        while (kø.size()>0)                    // så lenge som køen ikke er tom
        {
            Node <T> p = kø.remove(0);            // tar ut fra køen
            queue.add(p.verdi);                      //legger inn p (fjernet element fra køen) i queue

            if (p.venstre != null) kø.add(p.venstre);
            if (p.høyre != null) kø.add(p.høyre);
        }
        return queue;    //returenerer riktig liste
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


    public static void main(String[] args) {
        /*//oppgave 0
        EksamenSBinTre<String> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        System.out.println(tre.antall()); // Utskrift: 0;


        //oppgave 1
        Integer[] a = {4,7,2,9,5,10,8,1,3,6};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.antall()); // Utskrift: 10



        //oppgave 2
        Integer[] a = {4,7,2,9,4,10,8,7,4,6};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.antall()); // Utskrift: 10
        System.out.println(tre.antall(5)); // Utskrift: 0
        System.out.println(tre.antall(4)); // Utskrift: 3
        System.out.println(tre.antall(7)); // Utskrift: 2
        System.out.println(tre.antall(10)); // Utskrift: 1

         */

        //oppgave 3



    }


} // ObligSBinTre
