#include <stdio.h>
#include <string.h>
#include <ctype.h>

// structures   store class
struct Class {
    char time[20];
    char name[50];
    char venue[50];
};

// structure store date
struct Day {
    char name[10];
    int isWeekend;
    int classCount;
    struct Class classes[10];
};

// Global variables
struct Day days[10];   // store only 10 days timetable
char weekendNames[10][10];
int totalDays = 0;
int weekendCount = 0;
int dataExists = 0;

// Convert to uppercase using pointer
void stringToUpper(char *str) {
    while (*str) {
        *str = toupper(*str);
        str++;
    }
}

// Get total days
int Tdays() {
    int days;
    printf("For how many days you want to create timetable: ");
    scanf("%d", &days);
    return days;
}

// Get day names using pointers
void getDayNames(char (*Dname)[10], int Tday) {
    printf("Enter names of your days:\n");
    for (int i = 0; i < Tday; i++) {
        printf("Day %d: ", i + 1);
        scanf("%s", *(Dname + i));
        stringToUpper(*(Dname + i));
    }
}

// Get weekend names using pointers
int getweek(char (*week)[10]) {
    char ans[5];
    int count = 0;
    printf("Is there any weekend (Yes/No): ");
    scanf("%s", ans);

    if (strcmp(ans, "yes") == 0 || strcmp(ans, "Yes") == 0) {
        printf("How many weekends: ");
        scanf("%d", &count);

        for (int i = 0; i < count; i++) {
            printf("Enter name of weekend %d: ", i + 1);
            scanf("%s", *(week + i));
            stringToUpper(*(week + i));
        }
    }
    return count;
}

// Check if day is weekend using pointers
int isWeekendDay(char *day, char (*weekendN)[10], int weekendC) {
    for (int i = 0; i < weekendC; i++) {
        if (strcmp(day, *(weekendN + i)) == 0)
            return 1;
    }
    return 0;
}

// Initialize timetable
void initializeTimetable(int total, char (*Dname)[10], char (*weekendN)[10], int weekendC) {
    totalDays = total;
    weekendCount = weekendC;

    for (int i = 0; i < weekendC; i++) {
        strcpy(weekendNames[i], *(weekendN + i));
    }

    for (int i = 0; i < totalDays; i++) {
        strcpy(days[i].name, *(Dname + i));
        days[i].isWeekend = isWeekendDay(*(Dname + i), weekendN, weekendC);
        days[i].classCount = 0;
    }
}

// Input timetable
void inputTimetable() {
    for (int i = 0; i < totalDays; i++) {
        if (days[i].isWeekend) {
            printf("\n%s - weekend! No classes.\n", days[i].name);
            continue;
        }

        printf("\n--- Timetable for %s ---\n", days[i].name);
        printf("How many classes on %s: ", days[i].name);
        scanf("%d", &days[i].classCount);

        for (int j = 0; j < days[i].classCount; j++) {
            struct Class *cls = &days[i].classes[j];

            printf("\nClass %d:\n", j + 1);
            printf("Enter Time (8:00-9:00): ");
            scanf("%s", cls->time);

            printf("Enter Class Name: ");
            scanf("%s", cls->name);

            printf("Enter Venue: ");
            scanf("%s", cls->venue);
        }
    }
    printf("\n---Timetable Added Successfully---\n\n");
    dataExists = 1;
}

// Display timetable
void displayTimetable() {
    printf("\n=============== FLEXIBLE DIGITAL TIME TABLE ===============\n");
    for (int i = 0; i < totalDays; i++) {
        printf("\n>>> %s <<<\n", days[i].name);

        if (days[i].isWeekend) {
            printf("WEEKEND - No classes, Enjoy\n");
            continue;
        }

        printf("%-5s %-15s %-10s %-6s\n", "No.", "Timing", "Class", "Venue");
        printf("---------------------------------------------------------------\n");

        for (int j = 0; j < days[i].classCount; j++) {
            struct Class *cls = &days[i].classes[j];
            printf("%-5d %-15s %-10s %-6s\n", j + 1, cls->time, cls->name, cls->venue);
        }
    }
    printf("\n=== END OF TIME-TABLE ===\n\n");
}

// Update timetable
void updateTimetable() {
    int dayidx, classidx, choice;

    printf("Available Days:\n");
    for (int i = 0; i < totalDays; i++) {
        printf("%d. %s\n", i + 1, days[i].name);
    }

    printf("Select day number to edit: ");
    scanf("%d", &dayidx);
    dayidx--;

    if (days[dayidx].isWeekend) {
        printf("Cannot edit weekend!\n");
        return;
    }

    printf("Classes on %s:\n", days[dayidx].name);
    for (int j = 0; j < days[dayidx].classCount; j++) {
        struct Class *cls = &days[dayidx].classes[j];
        printf("%d. %s | %s | %s\n", j + 1, cls->time, cls->name, cls->venue);
    }

    printf("\n1. Edit class\n2. Delete class\nChoice: ");
    scanf("%d", &choice);

    if (choice == 1) {
        printf("Enter class number to edit: ");
        scanf("%d", &classidx);
        classidx--;

        struct Class *cls = &days[dayidx].classes[classidx];

        printf("New Time: ");
        scanf("%s", cls->time);
        printf("New Class Name: ");
        scanf("%s", cls->name);
        printf("New Venue: ");
        scanf("%s", cls->venue);
        printf("Class updated!\n");
    }
    else if (choice == 2) {
        printf("Enter class number to delete: ");
        scanf("%d", &classidx);
        classidx--;

        for (int j = classidx; j < days[dayidx].classCount - 1; j++) {
            days[dayidx].classes[j] = days[dayidx].classes[j + 1];
        }

        days[dayidx].classCount--;
        printf("Class deleted!\n");
    }
}

// Main function
int main() {
    char dayNames[10][10];
    char weekNames[10][10];
    int choice;
    int weekendC;

    while (1) {
        printf("====== Flexible Time-Table ======\n");
        printf("1. Add Timetable\n2. View Timetable\n3. Update Timetable\n4. Exit\n");
        printf("Choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                totalDays = Tdays();
                getDayNames(dayNames, totalDays);
                weekendC = getweek(weekNames);
                initializeTimetable(totalDays, dayNames, weekNames, weekendC);
                inputTimetable();
                break;

            case 2:
                if (dataExists) {
                    displayTimetable();
                } else {
                    printf("No timetable found. Add first\n");
                }
                break;

            case 3:
                if (dataExists) {
                    updateTimetable();
                } else {
                    printf("No timetable found. Add first\n");
                }
                break;

            case 4:
                printf("=====Thankyou!=====\n");
                return 0;

            default:
                printf("Invalid choice.\n");
        }
    }
}

