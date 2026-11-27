/**
 * This program analyzes a list of student exam scores across multiple
 * subjects. It calculates each student's average, determines their
 * overall grade, finds the top student, and identifies which subjects
 * had the most failures (below 50).
 *
 * Expected output:
 *   Alice:  avg 78, grade C
 *   Bob:    avg 41, grade F
 *   Clara:  avg 88, grade B
 *   David:  avg 64, grade D
 *
 *   Top student: Clara with average 88
 *   Failures per subject:
 *     Math: 1
 *     English: 1
 *     Physics: 1
 *     History: 0
 *   Subject with most failures: Math (1)
 *
 * The program contains a bug. Find it.
 */
public class ProgramB {

    static String[] students = {"Alice", "Bob", "Clara", "David"};
    static String[] subjects = {"Math", "English", "Physics", "History"};

    static int[][] scores = {
        {82, 90, 55, 85},   // Alice
        {35, 50, 28, 52},   // Bob
        {91, 78, 95, 88},   // Clara
        {70, 42, 75, 72}    // David
    };

    static int calculateAverage(int studentIdx) {
        int sum = 0;
        int count = 0;
        for (int j = 0; j < subjects.length; j++) {
            sum += scores[studentIdx][j];
            count++;
        }
        return sum / count;
    }

    static char determineGrade(int average) {
        if (average >= 90) {
            return 'A';
        } else if (average >= 80) {
            return 'B';
        } else if (average >= 70) {
            return 'C';
        } else if (average >= 60) {
            return 'D';
        }
        return 'F';
    }

    static int findTopStudent(int[] averages) {
        int topIdx = 0;
        for (int i = 1; i < averages.length; i++) {
            if (averages[i] > averages[topIdx]) {
                topIdx = i;
            }
        }
        return topIdx;
    }

    static int[] countFailuresPerSubject() {
        int[] failures = new int[subjects.length];
        for (int j = 0; j < subjects.length; j++) {
            for (int i = 0; i < students.length; i++) {
                if (scores[i][j] < 50) {
                    failures[i]++;
                }
            }
        }
        return failures;
    }

    static int findWorstSubject(int[] failures) {
        int worstIdx = 0;
        for (int j = 1; j < failures.length; j++) {
            if (failures[j] > failures[worstIdx]) {
                worstIdx = j;
            }
        }
        return worstIdx;
    }

    static void printStudentSummary(int[] averages) {
        for (int i = 0; i < students.length; i++) {
            char grade = determineGrade(averages[i]);
            String name = students[i];
            while (name.length() < 7) {
                name = name + " ";
            }
            System.out.println(name + " avg " + averages[i]
                    + ", grade " + grade);
        }
    }

    static void printFailureSummary(int[] failures) {
        System.out.println("\nFailures per subject:");
        for (int j = 0; j < subjects.length; j++) {
            System.out.println("  " + subjects[j] + ": " + failures[j]);
        }

        int worstIdx = findWorstSubject(failures);
        System.out.println("Subject with most failures: "
                + subjects[worstIdx] + " (" + failures[worstIdx] + ")");
    }

    static void analyze() {
        int[] averages = new int[students.length];
        for (int i = 0; i < students.length; i++) {
            averages[i] = calculateAverage(i);
        }

        printStudentSummary(averages);

        int topIdx = findTopStudent(averages);
        System.out.println("\nTop student: " + students[topIdx]
                + " with average " + averages[topIdx]);

        int[] failures = countFailuresPerSubject();
        printFailureSummary(failures);
    }

    public static void main(String[] args) {
        analyze();
    }
}
