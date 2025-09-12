package de.geosphere.speechplaning.mockup

import de.geosphere.speechplaning.data.SpiritualStatus
import de.geosphere.speechplaning.data.model.Congregation
import de.geosphere.speechplaning.data.model.District
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.model.Speech

@Suppress("LargeClass")
class MockedListOfDummyClasses {
    private constructor()
    companion object {
        val speakersMockupList =
            mutableListOf<Speaker>(
                Speaker(
                    id = "1",
                    nameFirst = "Alfred",
                    nameLast = "Meier",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+478889988542555",
                    email = "alfred.meier@email.com",
                    speechNumberIds = mutableListOf(1,4, 6),
                    spiritualStatus = SpiritualStatus.ELDER,
                ),
                Speaker(
                    id = "2",
                    nameFirst = "Berta",
                    nameLast = "Schmidt",
                    isActive = false,
                    phone = "+01414558788",
                    mobile = "+4788899885342555",
                    email = "berta.schmidt@email.com",
                    speechNumberIds = emptyList(),
                    spiritualStatus = SpiritualStatus.MINISTERIAL_SERVANT,
                ),
                Speaker(
                    id = "3",
                    nameFirst = "Charlie",
                    nameLast = "Müller",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+478889962288555",
                    email = "charlie.mueller@email.com",
                    speechNumberIds = mutableListOf(21,25),
                    spiritualStatus = SpiritualStatus.UNKNOWN,
                ),
                Speaker(
                    id = "4",
                    nameFirst = "Diana",
                    nameLast = "Weber",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+478889988563255",
                    email = "diana.weber@email.com",
                    speechNumberIds = mutableListOf(32),
                    spiritualStatus = SpiritualStatus.ELDER,
                ),
                Speaker(
                    id = "5",
                    nameFirst = "Emil",
                    nameLast = "Fischer",
                    isActive = false,
                    phone = "+01414558788",
                    mobile = "+47885389988555",
                    email = "emil.fischer@email.com",
                    speechNumberIds = emptyList(),
                    spiritualStatus = SpiritualStatus.MINISTERIAL_SERVANT,
                ),
                Speaker(
                    id = "6",
                    nameFirst = "Frieda",
                    nameLast = "Wagner",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+47888998855425",
                    email = "frieda.wagner@email.com",
                    speechNumberIds = mutableListOf(41,42,43),
                    spiritualStatus = SpiritualStatus.ELDER,
                ),
                Speaker(
                    id = "7",
                    nameFirst = "Gustav",
                    nameLast = "Becker",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+47888964988555",
                    email = "gustav.becker@email.com",
                    speechNumberIds = mutableListOf(11,12,20),
                    spiritualStatus = SpiritualStatus.MINISTERIAL_SERVANT,
                ),
                Speaker(
                    id = "8",
                    nameFirst = "Hanna",
                    nameLast = "Hoffmann",
                    isActive = false,
                    phone = "+01414558788",
                    mobile = "+478889935288555",
                    email = "hanna.hoffmann@email.com",
                    speechNumberIds = emptyList(),
                    spiritualStatus = SpiritualStatus.UNKNOWN,
                ),
                Speaker(
                    id = "9",
                    nameFirst = "Ingo",
                    nameLast = "Schulz",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+47888998834555",
                    email = "ingo.schulz@email.com",
                    speechNumberIds = mutableListOf(21,22),
                    spiritualStatus = SpiritualStatus.ELDER,
                ),
                Speaker(
                    id = "10",
                    nameFirst = "Julia",
                    nameLast = "Richter",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+47888998855555",
                    email = "julia.richter@email.com",
                    speechNumberIds = mutableListOf(25),
                    spiritualStatus = SpiritualStatus.MINISTERIAL_SERVANT,
                ),
                Speaker(
                    id = "11",
                    nameFirst = "Karl",
                    nameLast = "Klein",
                    isActive = false,
                    phone = "+01414558788",
                    mobile = "+47888934988555",
                    email = "karl.klein@email.com",
                    speechNumberIds = emptyList(),
                    spiritualStatus = SpiritualStatus.ELDER,
                ),
                Speaker(
                    id = "12",
                    nameFirst = "Lena",
                    nameLast = "Lang",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+47888998568555",
                    email = "lena.lang@email.com",
                    speechNumberIds = mutableListOf(21,22,32),
                    spiritualStatus = SpiritualStatus.MINISTERIAL_SERVANT,
                ),
                Speaker(
                    id = "13",
                    nameFirst = "Max",
                    nameLast = "Schwarz",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+4788839988555",
                    email = "max.schwarz@email.com",
                    speechNumberIds = mutableListOf(44),
                    spiritualStatus = SpiritualStatus.UNKNOWN,
                ),
                Speaker(
                    id = "14",
                    nameFirst = "Nora",
                    nameLast = "Braun",
                    isActive = false,
                    phone = "+01414558788",
                    mobile = "+4788899488555",
                    email = "nora.braun@email.com",
                    speechNumberIds = emptyList(),
                    spiritualStatus = SpiritualStatus.ELDER,
                ),
                Speaker(
                    id = "15",
                    nameFirst = "Otto",
                    nameLast = "Wolf",
                    isActive = true,
                    phone = "+01414558788",
                    mobile = "+478889988655",
                    email = "otto.wolf@email.com",
                    speechNumberIds = mutableListOf(40,44),
                    spiritualStatus = SpiritualStatus.MINISTERIAL_SERVANT,
                ),
            )

        @Suppress("MaxLineLength")
        val speechesMockupList =
            mutableListOf<Speech>(
                Speech(id = "1", number = "1", subject = "Wie gut kenne ich Gott?", active = true),
                Speech(
                    id = "2",
                    number = "2",
                    subject = "Die „letzten Tage“ – wer wird sie überleben?",
                    active = true
                ),
                Speech(
                    id = "3",
                    number = "3",
                    subject = "Mit Jehovas vereinter Organisation in Richtung Ewigkeit",
                    active = true
                ),
                Speech(
                    id = "4",
                    number = "4",
                    subject = "Sichtbare Belege für die Existenz Gottes",
                    active = true
                ),
                Speech(
                    id = "5",
                    number = "5",
                    subject = "Wie kann man als Familie glücklich sein?",
                    active = true
                ),
                Speech(
                    id = "6",
                    number = "6",
                    subject = "Die Sintflut – nicht nur eine Geschichte",
                    active = true
                ),
                Speech(
                    id = "7",
                    number = "7",
                    subject = "Sich den „Vater tiefen Mitgefühls“ zum Vorbild nehmen",
                    active = true
                ),
                Speech(
                    id = "8",
                    number = "8",
                    subject = "Für Gott und nicht für sich selbst leben",
                    active = true
                ),
                Speech(id = "9", number = "9", subject = "Gottes Wort hören und danach leben", active = true),
                Speech(id = "10", number = "10", subject = "Bei allem, was wir tun, ehrlich sein", active = true),
                Speech(
                    id = "11",
                    number = "11",
                    subject = "„Kein Teil der Welt“ sein – so wie Christus",
                    active = true
                ),
                Speech(
                    id = "12",
                    number = "12",
                    subject = "Autorität – ist es Gott wichtig, wie wir darüber denken?",
                    active = true
                ),
                Speech(
                    id = "13",
                    number = "13",
                    subject = "Gottes Ansicht über Sexualität und Ehe",
                    active = true
                ),
                Speech(
                    id = "14",
                    number = "14",
                    subject = "Durch Sauberkeit und Reinheit Jehova ehren",
                    active = true
                ),
                Speech(id = "15", number = "15", subject = "Wie wir „allen Gutes tun“", active = true),
                Speech(
                    id = "16",
                    number = "16",
                    subject = "Wie man sein Verhältnis zu Gott vertieft",
                    active = true
                ),
                Speech(id = "17", number = "17", subject = "Gott mit allem ehren, was wir haben", active = true),
                Speech(id = "18", number = "18", subject = "Ist Jehova meine „Festung“?", active = true),
                Speech(
                    id = "19",
                    number = "19",
                    subject = "Wie kann man erfahren, was in Zukunft geschieht?",
                    active = true
                ),
                Speech(
                    id = "20",
                    number = "20",
                    subject = "Ist für Gott die Zeit gekommen, die Welt zu regieren?",
                    active = true
                ),
                Speech(
                    id = "21",
                    number = "21",
                    subject = "Das Vorrecht schätzen, zu Gottes Königreich zu gehören",
                    active = true
                ),
                Speech(
                    id = "22",
                    number = "22",
                    subject = "Ziehen wir vollen Nutzen aus allem, wofür Jehova sorgt?",
                    active = true
                ),
                Speech(id = "23", number = "23", subject = "Unser Leben hat einen Sinn", active = true),
                Speech(
                    id = "24",
                    number = "24",
                    subject = "„Eine besonders kostbare Perle“ – habe ich sie gefunden?",
                    active = true
                ),
                Speech(id = "25", number = "25", subject = "Dem Geist der Welt widerstehen", active = true),
                Speech(id = "26", number = "26", subject = "Bin ich Gott wichtig?", active = true),
                Speech(id = "27", number = "27", subject = "Ein guter Start in die Ehe", active = true),
                Speech(id = "28", number = "28", subject = "In der Ehe Liebe und Respekt zeigen", active = true),
                Speech(
                    id = "29",
                    number = "29",
                    subject = "Elternsein – eine verantwortungsvolle, aber lohnende Aufgabe",
                    active = true
                ),
                Speech(
                    id = "30",
                    number = "30",
                    subject = "Die Kommunikation in der Familie verbessern – wie?",
                    active = true
                ),
                Speech(id = "31", number = "31", subject = "Brauche ich Gott in meinem Leben?", active = true),
                Speech(
                    id = "32",
                    number = "32",
                    subject = "Wie man mit den Sorgen des Lebens fertigwird",
                    active = true
                ),
                Speech(id = "33", number = "33", subject = "Wird Ungerechtigkeit jemals enden?", active = true),
                Speech(
                    id = "34",
                    number = "34",
                    subject = "Werde ich das Zeichen zum Überleben bekommen?",
                    active = true
                ),
                Speech(id = "35", number = "35", subject = "Können wir ewig leben? Wenn ja, wie?", active = true),
                Speech(
                    id = "36",
                    number = "36",
                    subject = "Ist mit dem jetzigen Leben alles vorbei?",
                    active = true
                ),
                Speech(
                    id = "37",
                    number = "37",
                    subject = "Was bringt es, sich von Gott leiten zu lassen?",
                    active = true
                ),
                Speech(
                    id = "38",
                    number = "38",
                    subject = "Wie kann man das Ende der Welt überleben?",
                    active = true
                ),
                Speech(
                    id = "39",
                    number = "39",
                    subject = "Jesus Christus hat die Welt besiegt – wie und wann?",
                    active = true
                ),
                Speech(id = "40", number = "40", subject = "Was die nahe Zukunft bringt", active = true),
                Speech(
                    id = "41",
                    number = "41",
                    subject = "„Bleibt stehen und seht, wie Jehova euch rettet“",
                    active = true
                ),
                Speech(id = "42", number = "42", subject = "Kann Liebe Hass besiegen?", active = true),
                Speech(id = "43", number = "43", subject = "Tue ich, was Gott von mir erwartet?", active = true),
                Speech(id = "44", number = "44", subject = "Was bringen uns die Lehren Jesu?", active = true),
                Speech(id = "45", number = "45", subject = "Den „Weg zum Leben“ gehen", active = true),
                Speech(
                    id = "46",
                    number = "46",
                    subject = "Bleiben wir zuversichtlich bis zum Ende",
                    active = true
                ),
                Speech(id = "47", number = "47", subject = "(Nicht verfügbar)", active = false),
                Speech(id = "48", number = "48", subject = "Als Christ Loyalität beweisen", active = true),
                Speech(
                    id = "49",
                    number = "49",
                    subject = "Eine gereinigte Erde – wer wird darauf leben?",
                    active = true
                ),
                Speech(id = "50", number = "50", subject = "Wie man gute Entscheidungen trifft", active = true),
                Speech(id = "51", number = "51", subject = "Verändert die Wahrheit mein Leben?", active = true),
                Speech(id = "52", number = "52", subject = "Wer ist mein Gott?", active = true),
                Speech(id = "53", number = "53", subject = "Denke ich so wie Gott?", active = true),
                Speech(
                    id = "54",
                    number = "54",
                    subject = "Wie man den Glauben an Gott und seine Versprechen stärkt",
                    active = true
                ),
                Speech(
                    id = "55",
                    number = "55",
                    subject = "Wie kann man sich einen guten Namen bei Gott machen?",
                    active = true
                ),
                Speech(id = "56", number = "56", subject = "Wessen Führung kann man vertrauen?", active = true),
                Speech(id = "57", number = "57", subject = "Unter Verfolgung standhaft bleiben", active = true),
                Speech(id = "58", number = "58", subject = "Woran erkennt man echte Christen?", active = true),
                Speech(id = "59", number = "59", subject = "Man erntet, was man sät", active = true),
                Speech(id = "60", number = "60", subject = "Was gibt meinem Leben Sinn?", active = true),
                Speech(
                    id = "61",
                    number = "61",
                    subject = "Wessen Versprechen kann man vertrauen?",
                    active = true
                ),
                Speech(id = "62", number = "62", subject = "Echte Hoffnung – wo zu finden?", active = true),
                Speech(
                    id = "63",
                    number = "63",
                    subject = "Habe ich den Geist eines Evangeliumsverkündigers?",
                    active = true
                ),
                Speech(id = "64", number = "64", subject = "Liebe ich das Vergnügen oder Gott?", active = true),
                Speech(
                    id = "65",
                    number = "65",
                    subject = "Frieden fördern in einer Welt voller Wut",
                    active = true
                ),
                Speech(id = "66", number = "66", subject = "Kann ich bei der Ernte mitarbeiten?", active = true),
                Speech(
                    id = "67",
                    number = "67",
                    subject = "Über Gottes Wort und die Schöpfung intensiv nachdenken",
                    active = true
                ),
                Speech(
                    id = "68",
                    number = "68",
                    subject = "Vergeben wir einander weiterhin großzügig",
                    active = true
                ),
                Speech(
                    id = "69",
                    number = "69",
                    subject = "Warum ist es wichtig, dass wir selbstlose Liebe zeigen?",
                    active = true
                ),
                Speech(
                    id = "70",
                    number = "70",
                    subject = "Warum Gott unser ganzes Vertrauen verdient",
                    active = true
                ),
                Speech(id = "71", number = "71", subject = "Warum wir „wach bleiben“ müssen", active = true),
                Speech(
                    id = "72",
                    number = "72",
                    subject = "Liebe – das Kennzeichen wahrer Christen",
                    active = true
                ),
                Speech(id = "73", number = "73", subject = "„Ein weises Herz bekommen“ – wie?", active = true),
                Speech(id = "74", number = "74", subject = "Jehovas Augen schauen auf uns", active = true),
                Speech(
                    id = "75",
                    number = "75",
                    subject = "Jehovas Herrschaft – unterstütze ich sie?",
                    active = true
                ),
                Speech(
                    id = "76",
                    number = "76",
                    subject = "Biblische Grundsätze – eine Hilfe bei heutigen Problemen?",
                    active = true
                ),
                Speech(id = "77", number = "77", subject = "„Seid immer gastfreundlich“", active = true),
                Speech(id = "78", number = "78", subject = "Jehova zu dienen bringt Freude", active = true),
                Speech(
                    id = "79",
                    number = "79",
                    subject = "Für welche Freundschaft werde ich mich entscheiden?",
                    active = true
                ),
                Speech(
                    id = "80",
                    number = "80",
                    subject = "Wissenschaft oder Bibel – worauf sollte man seine Hoffnung setzen?",
                    active = true
                ),
                Speech(id = "81", number = "81", subject = "Kann ich ein Bibellehrer sein?", active = true),
                Speech(id = "82", number = "82", subject = "(Nicht verfügbar)", active = true),
                Speech(
                    id = "83",
                    number = "83",
                    subject = "Müssen Christen die Zehn Gebote halten?",
                    active = true
                ),
                Speech(
                    id = "84",
                    number = "84",
                    subject = "Dem entgehen, was dieser Welt bevorsteht",
                    active = true
                ),
                Speech(
                    id = "85",
                    number = "85",
                    subject = "Eine gute Botschaft in einer gewalttätigen Welt",
                    active = true
                ),
                Speech(id = "86", number = "86", subject = "Welche Gebete erhört Gott?", active = true),
                Speech(
                    id = "87",
                    number = "87",
                    subject = "Was für ein Verhältnis habe ich zu Gott?",
                    active = true
                ),
                Speech(
                    id = "88",
                    number = "88",
                    subject = "Warum nach biblischen Maßstäben leben?",
                    active = true
                ),
                Speech(id = "89", number = "89", subject = "Den Durst nach Wahrheit stillen", active = true),
                Speech(id = "90", number = "90", subject = "Das wirkliche Leben ergreifen", active = true),
                Speech(
                    id = "91",
                    number = "91",
                    subject = "Die Gegenwart des Messias und seine Herrschaft",
                    active = true
                ),
                Speech(
                    id = "92",
                    number = "92",
                    subject = "Die Rolle der Religion im Weltgeschehen",
                    active = true
                ),
                Speech(
                    id = "93",
                    number = "93",
                    subject = "Naturkatastrophen – werden sie jemals enden?",
                    active = true
                ),
                Speech(
                    id = "94",
                    number = "94",
                    subject = "Die wahre Religion stillt die Bedürfnisse der menschlichen Gesellschaft",
                    active = true
                ),
                Speech(id = "95", number = "95", subject = "Spiritismus – warum gefährlich?", active = true),
                Speech(id = "96", number = "96", subject = "Welche Zukunft hat die Religion?", active = true),
                Speech(
                    id = "97",
                    number = "97",
                    subject = "Sich in einer schlechten Welt nichts zuschulden kommen lassen",
                    active = true
                ),
                Speech(id = "98", number = "98", subject = "„Die Szene dieser Welt wechselt“", active = true),
                Speech(id = "99", number = "99", subject = "Warum man der Bibel vertrauen kann", active = true),
                Speech(
                    id = "100",
                    number = "100",
                    subject = "Wie kann ich starke und dauerhafte Freundschaften aufbauen?",
                    active = true
                ),
                Speech(id = "101", number = "101", subject = "Jehova – der „große Schöpfer“", active = true),
                Speech(
                    id = "102",
                    number = "102",
                    subject = "Den Prophezeiungen der Bibel Aufmerksamkeit schenken",
                    active = true
                ),
                Speech(
                    id = "103",
                    number = "103",
                    subject = "Wie man im Dienst für Gott Freude finden kann",
                    active = true
                ),
                Speech(
                    id = "104",
                    number = "104",
                    subject = "Wie können Eltern mit feuerfestem Material bauen?",
                    active = true
                ),
                Speech(
                    id = "105",
                    number = "105",
                    subject = "In allen unseren Prüfungen Trost finden",
                    active = true
                ),
                Speech(
                    id = "106",
                    number = "106",
                    subject = "Die Zerstörung der Erde wird von Gott bestraft",
                    active = true
                ),
                Speech(
                    id = "107",
                    number = "107",
                    subject = "Was bringt mir ein gut geschultes Gewissen?",
                    active = true
                ),
                Speech(
                    id = "108",
                    number = "108",
                    subject = "Wir können zuversichtlich in die Zukunft schauen!",
                    active = true
                ),
                Speech(id = "109", number = "109", subject = "Das Königreich Gottes ist nah", active = true),
                Speech(
                    id = "110",
                    number = "110",
                    subject = "Gott steht in einer glücklichen Familie an erster Stelle",
                    active = true
                ),
                Speech(
                    id = "111",
                    number = "111",
                    subject = "Kann die Menschheit vollständig geheilt werden?",
                    active = true
                ),
                Speech(id = "112", number = "112", subject = "(Nicht verfügbar)", active = false),
                Speech(
                    id = "113",
                    number = "113",
                    subject = "Wie können Jugendliche glücklich und erfolgreich sein?",
                    active = true
                ),
                Speech(
                    id = "114",
                    number = "114",
                    subject = "Für die Wunder der Schöpfung dankbar sein",
                    active = true
                ),
                Speech(
                    id = "115",
                    number = "115",
                    subject = "Schützen wir uns vor den hinterhältigen Angriffen des Teufels",
                    active = true
                ),
                Speech(id = "116", number = "116", subject = "Bei seinem Umgang wählerisch sein", active = true),
                Speech(
                    id = "117",
                    number = "117",
                    subject = "Wie man das Böse mit dem Guten besiegen kann",
                    active = true
                ),
                Speech(
                    id = "118",
                    number = "118",
                    subject = "Jugendlichen gegenüber so eingestellt sein wie Jehova",
                    active = true
                ),
                Speech(
                    id = "119",
                    number = "119",
                    subject = "Warum es gut ist, als Christ kein Teil der Welt zu sein",
                    active = true
                ),
                Speech(
                    id = "120",
                    number = "120",
                    subject = "Warum man sich jetzt Gottes Herrschaft unterordnen sollte",
                    active = true
                ),
                Speech(id = "121", number = "121", subject = "Ein geeintes Volk wird gerettet", active = true),
                Speech(id = "122", number = "122", subject = "Weltfrieden – woher zu erwarten?", active = true),
                Speech(id = "123", number = "123", subject = "Warum Christen anders sein müssen", active = true),
                Speech(
                    id = "124",
                    number = "124",
                    subject = "Stammt die Bibel wirklich von Gott?",
                    active = true
                ),
                Speech(
                    id = "125",
                    number = "125",
                    subject = "Warum die Menschheit ein Lösegeld benötigt",
                    active = true
                ),
                Speech(id = "126", number = "126", subject = "Wer kann gerettet werden?", active = true),
                Speech(id = "127", number = "127", subject = "Was geschieht, wenn wir sterben?", active = true),
                Speech(
                    id = "128",
                    number = "128",
                    subject = "Ist die Hölle wirklich ein Ort feuriger Qual?",
                    active = true
                ),
                Speech(
                    id = "129",
                    number = "129",
                    subject = "Ist die Dreieinigkeit eine biblische Lehre?",
                    active = true
                ),
                Speech(id = "130", number = "130", subject = "Die Erde wird für immer bestehen", active = true),
                Speech(id = "131", number = "131", subject = "(Nicht verfügbar)", active = false),
                Speech(id = "132", number = "132", subject = "(Nicht verfügbar)", active = false),
                Speech(
                    id = "133",
                    number = "133",
                    subject = "Der Ursprung des Menschen – ist es wichtig, was man glaubt?",
                    active = true
                ),
                Speech(
                    id = "134",
                    number = "134",
                    subject = "Sollten Christen den Sabbat halten?",
                    active = true
                ),
                Speech(id = "135", number = "135", subject = "Die Heiligkeit von Leben und Blut", active = true),
                Speech(
                    id = "136",
                    number = "136",
                    subject = "Wie denkt Gott über den Gebrauch von Bildern in der Anbetung?",
                    active = true
                ),
                Speech(
                    id = "137",
                    number = "137",
                    subject = "Sind die in der Bibel berichteten Wunder wirklich geschehen?",
                    active = true
                ),
                Speech(
                    id = "138",
                    number = "138",
                    subject = "Gutes Urteilsvermögen in einer verdorbenen Welt",
                    active = true
                ),
                Speech(
                    id = "139",
                    number = "139",
                    subject = "Göttliche Weisheit in einer wissenschaftlich orientierten Welt",
                    active = true
                ),
                Speech(
                    id = "140",
                    number = "140",
                    subject = "Jesus Christus – wer er wirklich ist",
                    active = true
                ),
                Speech(
                    id = "141",
                    number = "141",
                    subject = "Das Seufzen der Menschheit – wann wird es enden?",
                    active = true
                ),
                Speech(
                    id = "142",
                    number = "142",
                    subject = "Warum sollten wir bei Jehova Zuflucht suchen?",
                    active = true
                ),
                Speech(
                    id = "143",
                    number = "143",
                    subject = "Auf den Gott allen Trostes vertrauen",
                    active = true
                ),
                Speech(
                    id = "144",
                    number = "144",
                    subject = "Eine loyale Versammlung unter der Führung Christi",
                    active = true
                ),
                Speech(id = "145", number = "145", subject = "Wer ist wie Jehova, unser Gott?", active = true),
                Speech(id = "146", number = "146", subject = "Bildung zur Ehre Jehovas nutzen", active = true),
                Speech(
                    id = "147",
                    number = "147",
                    subject = "Vertrauen wir auf die rettende Macht Jehovas",
                    active = true
                ),
                Speech(
                    id = "148",
                    number = "148",
                    subject = "Das Leben so sehen, wie Gott es sieht",
                    active = true
                ),
                Speech(id = "149", number = "149", subject = "Unseren Weg mit Gott gehen", active = true),
                Speech(
                    id = "150",
                    number = "150",
                    subject = "Ist die heutige Welt zum Untergang verurteilt?",
                    active = true
                ),
                Speech(
                    id = "151",
                    number = "151",
                    subject = "Jehova ist seinem Volk „eine sichere Zuflucht“ ",
                    active = true
                ),
                Speech(
                    id = "152",
                    number = "152",
                    subject = "Das wahre Armageddon – warum und wann?",
                    active = true
                ),
                Speech(
                    id = "153",
                    number = "153",
                    subject = "Den „Ehrfurcht einflößenden Tag“ fest im Sinn behalten!",
                    active = true
                ),
                Speech(
                    id = "154",
                    number = "154",
                    subject = "Die Menschenherrschaft – auf der Waage gewogen",
                    active = true
                ),
                Speech(
                    id = "155",
                    number = "155",
                    subject = "Ist für Babylon die Stunde der Urteilsvollstreckung gekommen?",
                    active = true
                ),
                Speech(
                    id = "156",
                    number = "156",
                    subject = "Der Gerichtstag – Grund zur Angst oder zur Hoffnung?",
                    active = true
                ),
                Speech(
                    id = "157",
                    number = "157",
                    subject = "Wahre Christen lassen Gottes Lehren anziehend wirken",
                    active = true
                ),
                Speech(
                    id = "158",
                    number = "158",
                    subject = "Seien wir mutig und vertrauen wir auf Jehova",
                    active = true
                ),
                Speech(
                    id = "159",
                    number = "159",
                    subject = "In einer gefährlichen Welt Sicherheit finden",
                    active = true
                ),
                Speech(id = "160", number = "160", subject = "Die christliche Identität bewahren", active = true),
                Speech(
                    id = "161",
                    number = "161",
                    subject = "Warum nahm Jesus Leid und Tod auf sich?",
                    active = true
                ),
                Speech(id = "162", number = "162", subject = "Befreiung aus einer finsteren Welt", active = true),
                Speech(
                    id = "163",
                    number = "163",
                    subject = "Warum sollten wir Ehrfurcht vor dem wahren Gott haben?",
                    active = true
                ),
                Speech(id = "164", number = "164", subject = "Ist Gott noch Herr der Lage?", active = true),
                Speech(
                    id = "165",
                    number = "165",
                    subject = "Wessen Wertvorstellungen teilen wir?",
                    active = true
                ),
                Speech(
                    id = "166",
                    number = "166",
                    subject = "Mit Glauben und Mut in die Zukunft blicken",
                    active = true
                ),
                Speech(
                    id = "167",
                    number = "167",
                    subject = "Vernünftig handeln in einer unvernünftigen Welt ",
                    active = true
                ),
                Speech(id = "168", number = "168", subject = "Sicherheit in einer unruhigen Welt", active = true),
                Speech(
                    id = "169",
                    number = "169",
                    subject = "Warum sich von der Bibel leiten lassen?",
                    active = true
                ),
                Speech(
                    id = "170",
                    number = "170",
                    subject = "Wer eignet sich, die Menschheit zu regieren?",
                    active = true
                ),
                Speech(
                    id = "171",
                    number = "171",
                    subject = "In Frieden leben – heute und für immer",
                    active = true
                ),
                Speech(id = "172", number = "172", subject = "In welchem Ruf stehe ich bei Gott?", active = true),
                Speech(
                    id = "173",
                    number = "173",
                    subject = "Gibt es vom Standpunkt Gottes aus eine wahre Religion?",
                    active = true
                ),
                Speech(
                    id = "174",
                    number = "174",
                    subject = "Gottes neue Welt – wer darf darin leben?",
                    active = true
                ),
                Speech(id = "175", number = "175", subject = "Was macht die Bibel glaubwürdig?", active = true),
                Speech(
                    id = "176",
                    number = "176",
                    subject = "Echter Frieden und echte Sicherheit – wann?",
                    active = true
                ),
                Speech(
                    id = "177",
                    number = "177",
                    subject = "Wo finden wir in schwierigen Zeiten Hilfe?",
                    active = true
                ),
                Speech(id = "178", number = "178", subject = "Den „Weg der Integrität“ gehen", active = true),
                Speech(
                    id = "179",
                    number = "179",
                    subject = "Auf Gottes Königreich bauen – nicht auf Illusionen",
                    active = true
                ),
                Speech(
                    id = "180",
                    number = "180",
                    subject = "Warum die Auferstehung für uns eine Realität sein sollte",
                    active = true
                ),
                Speech(id = "181", number = "181", subject = "Ist es später, als wir denken?", active = true),
                Speech(
                    id = "182",
                    number = "182",
                    subject = "Was das Reich Gottes schon heute für uns tut",
                    active = true
                ),
                Speech(id = "183", number = "183", subject = "Den Blick von Wertlosem wegwenden", active = true),
                Speech(id = "184", number = "184", subject = "Ist mit dem Tod alles vorbei?", active = true),
                Speech(
                    id = "185",
                    number = "185",
                    subject = "Was bewirkt die Wahrheit in unserem Leben?",
                    active = true
                ),
                Speech(
                    id = "186",
                    number = "186",
                    subject = "Sich Gottes glücklichem Volk anschließen",
                    active = true
                ),
                Speech(
                    id = "187",
                    number = "187",
                    subject = "Warum lässt ein liebevoller Gott das Böse zu?",
                    active = true
                ),
                Speech(
                    id = "188",
                    number = "188",
                    subject = "Vertrauen wir voller Zuversicht auf Jehova?",
                    active = true
                ),
                Speech(
                    id = "189",
                    number = "189",
                    subject = "Seinen Weg mit Gott zu gehen bringt Segen – jetzt und für immer",
                    active = true
                ),
                Speech(
                    id = "190",
                    number = "190",
                    subject = "Vollkommenes Familienglück – ein Versprechen von Gott",
                    active = true
                ),
                Speech(
                    id = "191",
                    number = "191",
                    subject = "Wie Liebe und Glaube die Welt besiegen",
                    active = true
                ),
                Speech(
                    id = "192",
                    number = "192",
                    subject = "Bin ich auf dem Weg zum ewigen Leben?",
                    active = true
                ),
                Speech(
                    id = "193",
                    number = "193",
                    subject = "In der „schweren Zeit“ gerettet werden",
                    active = true
                ),
                Speech(
                    id = "194",
                    number = "194",
                    subject = "Wie göttliche Weisheit uns zugutekommt",
                    active = true
                )
            )

        val congregationMockupList =
            mutableListOf<Congregation>(
                Congregation(
                    id = "Grafing-Süd",
                    name = "Grafing-Süd",
                    address = "Bahnhofstrasse",
                    meetingTime = "",
                    active = true
                ),
                Congregation(
                    id = "Grafing-Nord",
                    name = "Grafing-Nord",
                    address = "Bahnhofstrasse",
                    meetingTime = "",
                    active = true
                ),
            )

        val districtMockupList =
            mutableListOf<District>(
                District(
                    id = "100",
                    circuitOverseerId = "",
                    name = "Grafing",
                    active = true
                ),
                District(
                    id = "101",
                    circuitOverseerId = "",
                    name = "Ebersberg",
                    active = true
                ),
                District(
                    id = "102",
                    circuitOverseerId = "",
                    name = "Glonn",
                    active = true
                ),

            )
    }
}
