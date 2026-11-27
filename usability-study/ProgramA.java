/**
 * This program manages a simple scheduling system. It takes a list of
 * meetings with start and end times, validates them, detects conflicts
 * between meetings, and prints a summary of the schedule.
 *
 * Input meetings:
 *   "Team Standup"   09:00 - 09:30
 *   "Code Review"    09:15 - 10:00
 *   "Design Meeting" 10:00 - 11:00
 *   "Lunch"          12:00 - 13:00
 *   "Sprint Planning"13:00 - 14:30
 *   "1:1 with Manager"14:00 - 14:30
 *
 * Expected output:
 *   CONFLICT: "Code Review" overlaps with "Team Standup"
 *   CONFLICT: "1:1 with Manager" overlaps with "Sprint Planning"
 *   Valid meetings: 4
 *   Conflicting meetings: 2
 *
 * The program contains a bug. Find it.
 */
public class ProgramA {

    static String[] names = {
        "Team Standup", "Code Review", "Design Meeting",
        "Lunch", "Sprint Planning", "1:1 with Manager"
    };
    static int[] startHours =   {9,  9,  10, 12, 13, 14};
    static int[] startMinutes = {0,  15, 0,  0,  0,  0};
    static int[] endHours =     {9,  10, 11, 13, 14, 14};
    static int[] endMinutes =   {30, 0,  0,  0,  30, 30};

    static int toMinutes(int hour, int minute) {
        return hour * 60 + minute;
    }

    static boolean isValidMeeting(int index) {
        int start = toMinutes(startHours[index], startMinutes[index]);
        int end = toMinutes(endHours[index], endMinutes[index]);
        if (start >= end) {
            return false;
        }
        if (startHours[index] < 8 || endHours[index] > 18) {
            return false;
        }
        return true;
    }

    static boolean hasConflict(int meetingA, int meetingB) {
        int startA = toMinutes(startHours[meetingA], startMinutes[meetingA]);
        int endA = toMinutes(endHours[meetingA], endMinutes[meetingA]);
        int startB = toMinutes(startHours[meetingB], startMinutes[meetingB]);
        int endB = toMinutes(endHours[meetingB], endMinutes[meetingB]);

        if (startA <= endB && startB <= endA) {
            return true;
        }
        return false;
    }

    static void sortByStartTime() {
        for (int i = 0; i < names.length - 1; i++) {
            int minIdx = i;
            int minTime = toMinutes(startHours[i], startMinutes[i]);
            for (int j = i + 1; j < names.length; j++) {
                int time = toMinutes(startHours[j], startMinutes[j]);
                if (time < minTime) {
                    minTime = time;
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                String tmpName = names[i];
                names[i] = names[minIdx];
                names[minIdx] = tmpName;

                int tmp;
                tmp = startHours[i]; startHours[i] = startHours[minIdx]; startHours[minIdx] = tmp;
                tmp = startMinutes[i]; startMinutes[i] = startMinutes[minIdx]; startMinutes[minIdx] = tmp;
                tmp = endHours[i]; endHours[i] = endHours[minIdx]; endHours[minIdx] = tmp;
                tmp = endMinutes[i]; endMinutes[i] = endMinutes[minIdx]; endMinutes[minIdx] = tmp;
            }
        }
    }

    static String formatTime(int hours, int minutes) {
        String h = (hours < 10) ? "0" + hours : "" + hours;
        String m = (minutes < 10) ? "0" + minutes : "" + minutes;
        return h + ":" + m;
    }

    static void printSchedule() {
        sortByStartTime();

        boolean[] conflicting = new boolean[names.length];

        for (int i = 0; i < names.length; i++) {
            if (!isValidMeeting(i)) {
                System.out.println("INVALID: \"" + names[i] + "\"");
                continue;
            }
            for (int j = i + 1; j < names.length; j++) {
                if (hasConflict(i, j)) {
                    System.out.println("CONFLICT: \"" + names[j]
                            + "\" overlaps with \"" + names[i] + "\"");
                    conflicting[i] = true;
                    conflicting[j] = true;
                }
            }
        }

        int conflictCount = 0;
        for (int i = 0; i < conflicting.length; i++) {
            if (conflicting[i]) {
                conflictCount++;
            }
        }

        System.out.println("Valid meetings: "
                + (names.length - conflictCount));
        System.out.println("Conflicting meetings: " + conflictCount);
    }

    public static void main(String[] args) {
        printSchedule();
    }
}
