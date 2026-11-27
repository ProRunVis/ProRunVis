#!/usr/bin/env python3
"""Generate Java programs of varying complexity for ProRunVis evaluation.

Three scaling axes:
  1. Trace length  — loop iterations (controls how many trace tokens are produced)
  2. Nesting depth — nested if/else chains (controls AST depth and trace branching)
  3. File count    — multiple classes calling each other (controls project size)

Usage:
  python3 generate-benchmarks.py [output_dir]

Default output: ./generated/
"""

import os
import sys
import textwrap

OUT_DIR = sys.argv[1] if len(sys.argv) > 1 else os.path.join(os.path.dirname(__file__), "generated")


def write_file(subdir, filename, content):
    path = os.path.join(OUT_DIR, subdir, filename)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w") as f:
        f.write(textwrap.dedent(content))
    return path


# ---------------------------------------------------------------------------
# Axis 1: Trace length — loop with N iterations
# Each iteration produces: IF(condition) -> IN -> IF(i%2==0) -> IN or OUT
# So roughly 3-4 trace tokens per iteration.
# ---------------------------------------------------------------------------

LOOP_SIZES = [10, 100, 1_000, 10_000, 50_000]

for n in LOOP_SIZES:
    name = f"trace_len_{n}"
    write_file(name, "TraceLen.java", f"""\
        class TraceLen {{

            static int compute(int n) {{
                int sum = 0;
                for (int i = 0; i < n; i++) {{
                    if (i % 2 == 0) {{
                        sum += i;
                    }} else {{
                        sum -= 1;
                    }}
                    if (i % 5 == 0) {{
                        sum *= 2;
                    }}
                }}
                return sum;
            }}

            public static void main(String[] args) {{
                int result = compute({n});
                System.out.println("result = " + result);
            }}
        }}
    """)
    print(f"  {name}: loop of {n} iterations")


# ---------------------------------------------------------------------------
# Axis 2: Nesting depth — nested if/else chains
# Each level adds an if/else with a condition based on depth.
# ---------------------------------------------------------------------------

NESTING_DEPTHS = [5, 10, 20, 50]

def build_nested_if(depth, indent=8):
    if depth == 0:
        return " " * indent + "sum += 1;\n"
    pad = " " * indent
    cond = f"x % {depth + 2} == 0"
    inner_if = build_nested_if(depth - 1, indent + 4)
    inner_else = " " * (indent + 4) + f"sum += {depth};\n"
    return (
        f"{pad}if ({cond}) {{\n"
        f"{inner_if}"
        f"{pad}}} else {{\n"
        f"{inner_else}"
        f"{pad}}}\n"
    )

for d in NESTING_DEPTHS:
    name = f"nesting_{d}"
    nested_code = build_nested_if(d)
    write_file(name, "Nesting.java", f"""\
class Nesting {{

    static int compute(int x) {{
        int sum = 0;
        for (int i = 0; i < 100; i++) {{
            x = x + i;
{nested_code}        }}
        return sum;
    }}

    public static void main(String[] args) {{
        int result = compute(42);
        System.out.println("result = " + result);
    }}
}}
""")
    print(f"  {name}: {d} levels deep, 100 iterations")


# ---------------------------------------------------------------------------
# Axis 3: File count — N classes calling each other in a chain
# Worker0.process() -> Worker1.process() -> ... -> WorkerN.process()
# Each worker has some control flow to generate trace tokens.
# ---------------------------------------------------------------------------

FILE_COUNTS = [1, 5, 10, 20]

for fc in FILE_COUNTS:
    name = f"files_{fc}"

    for i in range(fc):
        if i < fc - 1:
            next_call = f"Worker{i + 1}.process(x + 1)"
        else:
            next_call = "x * 2"

        write_file(name, f"Worker{i}.java", f"""\
class Worker{i} {{

    static int process(int x) {{
        int result = 0;
        if (x % 3 == 0) {{
            result = x + {i};
        }} else if (x % 3 == 1) {{
            result = x - {i};
        }} else {{
            result = x * {i + 1};
        }}

        for (int j = 0; j < 10; j++) {{
            if (j % 2 == 0) {{
                result += j;
            }}
        }}

        return result + {next_call};
    }}
}}
""")

    write_file(name, "Main.java", f"""\
class Main {{

    public static void main(String[] args) {{
        int result = Worker0.process(7);
        System.out.println("result = " + result);
    }}
}}
""")
    print(f"  {name}: {fc} worker classes + Main")


# ---------------------------------------------------------------------------
# Combined: large project with long traces and some nesting
# ---------------------------------------------------------------------------

write_file("combined_large", "LargeProgram.java", """\
class LargeProgram {

    static int heavyLoop(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                if (i % 4 == 0) {
                    if (i % 8 == 0) {
                        sum += i * 3;
                    } else {
                        sum += i * 2;
                    }
                } else {
                    sum += i;
                }
            } else {
                if (i % 3 == 0) {
                    sum -= i;
                } else {
                    sum += 1;
                }
            }

            switch (i % 4) {
                case 0:
                    sum += 10;
                    break;
                case 1:
                    sum -= 5;
                    break;
                case 2:
                    sum *= 2;
                    break;
                default:
                    sum += 3;
            }
        }
        return sum;
    }

    static int recursive(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n % 2 == 0) {
            return n + recursive(n - 1);
        } else {
            return recursive(n - 1);
        }
    }

    public static void main(String[] args) {
        int a = heavyLoop(5000);
        int b = recursive(100);
        System.out.println("result = " + (a + b));
    }
}
""")
print("  combined_large: heavy loop (5000) + recursion (100) + switch + nesting")


print(f"\nAll benchmarks generated in: {OUT_DIR}")
print(f"Total: {len(LOOP_SIZES) + len(NESTING_DEPTHS) + len(FILE_COUNTS) + 1} programs")
