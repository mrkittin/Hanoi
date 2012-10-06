public class Hanoy {
    static final int maxDisk = 3;
    static int move = 0;
    //out stems storage
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
            tries++; if (tries > 10) break;
            if (currMax == stems[0].getHighestDisk() || currMax == stems[1].getHighestDisk()) {
                if (move(findByHighestDisk(currMax), stems[2])) {
                    printStatus();
                    currMax--;
                } else {
                    //TODO
                }
            } else {
                moveAllDisksExceptCurrentMaxToStem1or2(currMax, invert1to2andViceVersa(findByAnyDisk(currMax)));
            }
        }
    }

    private static void moveAllDisksExceptCurrentMaxToStem1or2(int currMax, Stem neededStem) {
        boolean success = false;
        int tries = 0;
        while (!success) {
            tries++; if (tries > 10) break;
            if (findByHighestDisk(currMax-1) != null) {
                if (move(findByHighestDisk(currMax-1), neededStem)) {
                    printStatus();
                    success = true;
                } else {
                    //TODO
                }
            } else {
                if (neededStem.equals(stems[1])) {
                    move(findByAnyDisk(1), stems[2]); printStatus();
                } else {
                    move(findByAnyDisk(1), neededStem); printStatus();
                }
                move(findByAnyDisk(2), findAvailableStem(2, findByAnyDisk(2))); printStatus();
                move(findByHighestDisk(1), findByHighestDisk(2)); printStatus(); break; //TODO resolve this break
            }
        }
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
        int res = 0;
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
        System.out.print("move " + move + ": ");
        System.out.print(stems[0].toString());
        System.out.print(stems[1].toString());
        System.out.println(stems[2].toString());
        move++;
    }


}
