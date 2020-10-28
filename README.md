# Mappeeksamen i Algoritmer og Datastrukturer Høst 2020

# Krav til innlevering

Se oblig-tekst for alle krav, og husk spesielt på følgende:

* Git er brukt til å dokumentere arbeid (minst 2 commits per oppgave, beskrivende commit-meldinger)	
* git bundle er levert inn
* Hovedklassen ligger i denne path'en i git: src/no/oslomet/cs/algdat/Eksamen/EksamenSBinTre.java
* Ingen debug-utskrifter
* Alle testene i test-programmet kjører og gir null feil (også spesialtilfeller)
* Readme-filen her er fyllt ut som beskrevet


# Beskrivelse av oppgaveløsning (4-8 linjer/setninger per oppgave)

Vi har brukt git til å dokumentere arbeidet vårt. Jeg har 16 commits totalt, og hver logg-melding beskriver det jeg har gjort av endringer.

* Oppgave 1: -Kopierte Programkode 5.2 3 a)
              Metoden legger inn en ny node i treet, kan ikke legge inn null-verdier
             -Gjorde en endring på metoden:
              Node klassen skal ta inn forelder slik at
              p = new Node<>(verdi,q);
              
* Oppgave 2: -Laget en (int) teller i toppen av metoden
              Dene returenes uansett
             -Dersom treet ikke er tomt eller innheolder(verdi) er sann, søker jeg gjennom treet
              ved å bruke comp.comare(), slik at jeg går endten høyre eller venstre nedover i treet for å finne verdier jeg ser etter
             -Når verdien jeg ser etter og noden jeg ligger på er like, økes teller med en (teller++)
             -Når teller går utenfor treet (node.høyre/ node.venstre er null) går jeg ut av while-løkken og returnerer telleren
             
* Oppgave 3: -Begge metodene tar inn en node. Derfor starter jeg med å komme meg helt til toppen av teet i første while-løkke
              i metoden førtstePostordem.
             -Når jeg er i toppen av treet bruker jeg igjen en while-løkke for å komme meg nederst mot venstre i treet, og på det "yngste" barnet,
              hvor første postorden ligger. Kjører denne while-løkken helt til det ikke finnes flere barn.
             -nestePostorden starter jeg med noen if-tester, for å finne ut om jeg kan enkelt returnere null om jeg er i toppen av treet,
              forelder hvis jeg er på et høyrebarn og det som må skje for et venstrebarn:
              der returneres forelder hvis p er enebarn.
              ellers må jeg gå i en while-løkke som har samme prinsipp som førstePostorden for å finne den neste i postorden.