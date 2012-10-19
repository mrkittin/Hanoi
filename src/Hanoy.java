import java.util.AbstractMap;
import java.util.Map;
import java.util.Stack;

public class Hanoy {
    static final int maxDisk = 5;
    static int currentMove = 0;
    //stems storage
    static Stem[] stems = new Stem[3];



    public static void main(String[] args) {
        //initialize 3 stems
        for (int i = 0; i < 3; i++) {
            stems[i] = new Stem();
        }

        //add some disks to the first(left) stem
        for (int i = maxDisk ; i > 0; i--) {
            stems[0].addDisk(i);
        }
        printStatus();

        //main cycle
        int currMax = maxDisk;
        int tries = 0;
        while(currMax > 0) {
            tries++; if (tries > 100) break;
            if (currMax == stems[0].getHighestDisk() || currMax == stems[1].getHighestDisk()) {
                move(findByHighestDisk(currMax), stems[2]);
                printStatus();
                currMax--;
            }
            if (currMax == 2) {
                move1and2toNeededStem(stems[2]);
                return;
            }
            else {
                moveAllDisksExceptCurrentMaxToStem1or2(currMax, invert1to2andViceVersa(findByAnyDisk(currMax)));
            }
        }
    }

    private static void moveAllDisksExceptCurrentMaxToStem1or2(int currMax, Stem neededStem) {
        //external scheduled moves storage
        Stack<AbstractMap.SimpleEntry<Integer, Stem>> scOpStackEx = new Stack<AbstractMap.SimpleEntry<Integer, Stem>>();
        //internal scheduled moves storage
        Stack<AbstractMap.SimpleEntry<Integer, Stem>> scOpStackInt = new Stack<AbstractMap.SimpleEntry<Integer, Stem>>();
        int swarmLow = currMax - 1;
        int swarmHigh = 1;
        Stem tempStem = neededStem;

        //cycle: iterate through all disks in swarm and move them to needed stem
        for (int i = swarmLow; i >= swarmHigh; i--) {
            if (i == 2) {
                move1and2toNeededStem(tempStem);
                while (!scOpStackEx.isEmpty()) {
                        if (move(scOpStackEx)) {
                            printStatus();
                        } else {
                            move1and2toNeededStem(findStemThatIsNotAorB(findByHighestDisk(1), findByHighestDisk(scOpStackEx.peek().getKey())));
                            move(scOpStackEx);
                            printStatus();
                        }

                    if (findByAnyDisk(swarmLow).equals(neededStem) && findByAnyDisk(swarmHigh).equals(neededStem)) {
                        return;
                    }
                    if (findByHighestDisk(swarmLow) != null && findByHighestDisk(swarmLow).equals(neededStem)) {
                        tempStem = findByHighestDisk(swarmLow);
                        for (int k = swarmLow-1; k >= swarmHigh; k--) {
                            if (k > 2) {
                                scheduleMove(k, tempStem, scOpStackInt);
                                tempStem = findStemThatIsNotAorB(tempStem, findByHighestDisk(1));
                            }
                            if (k == 2) {
                                if (scOpStackInt.isEmpty()) {
                                    move1and2toNeededStem(neededStem);
                                    return;
                                }
                                move1and2toNeededStem(findStemThatIsNotAorB(findByHighestDisk(swarmLow), findByHighestDisk(1)));
                                if (!scOpStackInt.isEmpty()) {
                                    for (int z = 0; z <= scOpStackEx.size(); z++) {
                                        if (move(scOpStackInt)) {
                                            printStatus();
                                        } else {
                                            move1and2toNeededStem(findStemThatIsNotAorB(findByHighestDisk(1), findByHighestDisk(scOpStackEx.elements().nextElement().getKey())));
                                            move(scOpStackInt);
                                            printStatus();
                                        }
                                    }
                                } else {
                                    //do nothing
                                }
                            }
                            if (k == 1) {
                                move1and2toNeededStem(neededStem);
                                return;
                            }
                        }
                    }
                }
                return;
            }
            if (i > 2) {
                scheduleMove(i, tempStem, scOpStackEx);
                tempStem = findStemThatIsNotAorB(tempStem, findByHighestDisk(1));
            }
        }
    }

    private static void scheduleMove(int disk, Stem stem, Stack<AbstractMap.SimpleEntry<Integer, Stem>> where) {
        where.push(new AbstractMap.SimpleEntry<Integer, Stem>(disk, stem));
    }

    private static void move1and2toNeededStem(Stem neededStem) {
        Stem loc1 = findByHighestDisk(1);
        Stem loc2 = findByAnyDisk(2);
        move(loc1, findStemThatIsNotAorB(loc1, neededStem)); printStatus();
        move(loc2, neededStem); printStatus();
        move(findByHighestDisk(1), neededStem); printStatus();
    }

    private static Stem findStemThatIsNotAorB(Stem a, Stem b) {
        for (Stem stem : stems) {
            if (stem.equals(a) || stem.equals(b)) {
                //going further
            } else return stem;
        }
        return null;
    }

    private static Stem invert1to2andViceVersa(Stem stem) {
        if (stem.equals(stems[0])) return stems[1];
        if (stem.equals(stems[1])) return stems[0];
        return null;
    }

    public static Stem findByHighestDisk(int disk) {
        for (Stem stt : stems) {
            if (stt.getHighestDisk() == disk) {
                return stt;
            }
        }
        return null;
    }

    public static Stem findByAnyDisk(int disk) {
        for (Stem stt : stems) {
            if (stt.getAllDisks().contains(disk)) return stt;
        }
        return null;
    }

    private static int findBiggestAvail() {
        int temp = Math.max(stems[0].getHighestDisk(), stems[1].getHighestDisk());
        if (temp > stems[3].getHighestDisk()) {
            return temp;
        } else return stems[3].getHighestDisk();
    }

    private static int findSmallestAvail() {
        int res;
        int temp = Math.min(stems[0].getHighestDisk(), stems[1].getHighestDisk());
        if (temp < stems[2].getHighestDisk()) {
            res = temp;
        } else res = stems[2].getHighestDisk();
        if (res == 0) {
            return findBiggestAvail();
        }
        return res;
    }

    static boolean move(Stem start, Stem finish) {
        if ((start.getHighestDisk() < finish.getHighestDisk() || finish.getHighestDisk() == 0) && start.getHighestDisk() != 0) {
            int disk = start.removeDisk();
            return finish.addDisk(disk);
        }
        else return false;
    }

    static boolean move(Stack<AbstractMap.SimpleEntry<Integer, Stem>> stack) {
        AbstractMap.SimpleEntry entry = (AbstractMap.SimpleEntry)stack.pop();
        int disk = (Integer)entry.getKey();
        Stem finish = (Stem)entry.getValue();
        Stem start = findByHighestDisk(disk);
        if (move(start, finish)) {
            return true;
        }
        else {
            scheduleMove(disk, finish, stack);
            return false;
        }
    }

    private static Stem findAvailableStem(int disk, Stem initialStem) {
        if (initialStem.equals(stems[0])) {
            if (stems[1].getHighestDisk() > disk || stems[1].getHighestDisk() == 0) return stems[1];
            if (stems[2].getHighestDisk() > disk || stems[2].getHighestDisk() == 0) return stems[2];
        }
        if (initialStem.equals(stems[1])) {
            if (stems[0].getHighestDisk() > disk || stems[0].getHighestDisk() == 0) return stems[0];
            if (stems[2].getHighestDisk() > disk || stems[2].getHighestDisk() == 0) return stems[2];
        }
        return null;
    }

    private static void printStatus() {
        System.out.print("move " + currentMove + ": ");
        System.out.print(stems[0].toString());
        System.out.print(stems[1].toString());
        System.out.println(stems[2].toString());
        currentMove++;
    }

}
