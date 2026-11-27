# Usability Study: ProRunVis vs VS Code Debugger

## Purpose

This study compares two debugging approaches for finding bugs in Java programs:
1. **ProRunVis** — a control-flow visualization tool
2. **VS Code Debugger** — a traditional step-through debugger

## Participants

- 4 participants, split into two groups of 2
- All participants should have basic Java programming knowledge

## Setup

### Before the study
- ProRunVis is running on localhost:8080
- VS Code is open with the Java Extension Pack installed
- Both programs (A and B) are pre-instrumented with SrcTracer for ProRunVis
- The trace files for both programs are ready to load into ProRunVis
- A timer (phone stopwatch) is ready

### Group assignment
| | Program A (Scheduler) | Program B (Student Scores) |
|---|---|---|
| **Group 1** (Participant 1, 2) | ProRunVis | VS Code Debugger |
| **Group 2** (Participant 3, 4) | VS Code Debugger | ProRunVis |

## Procedure (per participant, ~35 minutes total)

### Step 1: Introduction (5 minutes)
Read the following to the participant:

> "Thank you for participating. In this study you will try to find a bug in two small Java programs using two different tools. For one program you will use ProRunVis, a tool that visualizes the control flow of a program. For the other you will use the VS Code debugger. There are no right or wrong answers — we are evaluating the tools, not you."

Then give a brief demo of ProRunVis (2-3 minutes):
- Show how to open a directory and load a trace file
- Explain the color highlighting: green = executed, blue = jump/link, purple = loop
- Show how to click on loops to change iterations
- Show how to click on blue links to jump between method calls

### Step 2: Task 1 (10 minutes maximum)
1. Give the participant the program (A or B depending on group) as a printed sheet or on screen
2. Point out the expected output described in the comment at the top
3. Tell them: "This program has a bug. The actual output does not match the expected output. Please find the bug."
4. Start the timer
5. Provide the appropriate tool (ProRunVis or VS Code)
6. Record:
   - Time taken to find the bug (cap at 10 minutes)
   - Whether they found the correct bug (yes / no / partial)
   - Any observations about how they used the tool

### Step 3: Short break (2 minutes)

### Step 4: Task 2 (10 minutes maximum)
- Same procedure as Task 1, but with the other program and the other tool

### Step 5: Questionnaire (5 minutes)
Hand the participant the questionnaire to fill out.

## What counts as "found the bug"

- **Yes**: The participant identifies the exact line and explains what is wrong
  - Program A: line with `<=` in hasConflict should be `<`
  - Program B: line with `failures[i]++` should be `failures[j]++`
- **Partial**: The participant identifies the correct method or area but not the exact fix
- **No**: The participant did not find the bug or identified a wrong line

## Data to collect per participant

| Field | Value |
|---|---|
| Participant ID | P1 / P2 / P3 / P4 |
| Group | 1 or 2 |
| Task 1 tool | ProRunVis / VS Code |
| Task 1 program | A / B |
| Task 1 time (seconds) | ___ |
| Task 1 bug found | Yes / Partial / No |
| Task 2 tool | ProRunVis / VS Code |
| Task 2 program | A / B |
| Task 2 time (seconds) | ___ |
| Task 2 bug found | Yes / Partial / No |
| Questionnaire answers | Q1-Q6 (1-5), Q7-Q8 (text) |

## After all sessions: Evaluation

### Quantitative analysis

#### 1. Time comparison
Calculate the average time per tool across all tasks:
- Average time with ProRunVis = (sum of all ProRunVis task times) / 4
- Average time with VS Code = (sum of all VS Code task times) / 4

Present as a table:

| Tool | Avg Time (s) | Tasks Completed (out of 4) | Bug Found Rate |
|---|---|---|---|
| ProRunVis | ___ | ___ | ___% |
| VS Code | ___ | ___ | ___% |

#### 2. Success rate comparison
- How many participants found the bug with ProRunVis vs VS Code
- Count Yes as 1, Partial as 0.5, No as 0

#### 3. Per-program comparison (to check fairness)
Compare times for Program A across both tools and Program B across both tools.
If one program is consistently faster regardless of tool, note it as a limitation.

### Qualitative analysis

#### 4. Questionnaire summary
For Q1-Q6: calculate the average score per question.
Present as a table:

| Question | Avg Score (1-5) |
|---|---|
| Q1: Visualization helped understanding | ___ |
| Q2: Found bug faster with ProRunVis | ___ |
| Q3: ProRunVis was easy to use | ___ |
| Q4: VS Code debugging was straightforward | ___ |
| Q5: Would use ProRunVis again | ___ |
| Q6: Color highlighting was helpful | ___ |

For Q7-Q8: summarize common themes from the open answers.

#### 5. Observations
Summarize any patterns you noticed during the sessions:
- Did participants using ProRunVis tend to look at the flow first or the code first?
- Did VS Code users set breakpoints immediately or read the code first?
- Were there common points of confusion with either tool?

### Limitations to report
- Small sample size (4 participants) — results are indicative, not statistically significant
- Participants may have varying Java experience levels
- The brief ProRunVis demo gives VS Code an advantage since participants likely already know VS Code
- Only two programs tested — results may not generalize to all bug types
